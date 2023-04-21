package nps.core;

import java.sql.*;
import java.io.*;
import java.util.List;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.CLOB;
import nps.exception.NpsException;
import nps.util.DefaultLog;

/**
 * ģ�����
 * a new publishing system
 * Copyright (c) 2007
 * @author jialin
 * @version 1.0
 */
public abstract class TemplateBase implements Template, IPortable {

    protected String id = null;

    protected String name = null;

    protected int scope = 1;

    protected String siteid = null;

    protected String creator = null;

    protected String creator_cn = null;

    protected String creator_fullname = null;

    protected java.util.Date createdate = null;

    protected File template = null;

    protected Class clazz = null;

    public TemplateBase(String inId, String inName) {
        id = inId;
        name = inName;
    }

    public String GetId() {
        return id;
    }

    public String GetSiteId() {
        return siteid;
    }

    public void SetSiteId(String s) {
        if (scope == 0 || scope == 1) siteid = null; else siteid = s;
    }

    public int GetScope() {
        return scope;
    }

    public void SetScope(int i) {
        scope = i;
        if (scope == 0 || scope == 1) siteid = null;
    }

    public String GetName() {
        return name;
    }

    public void SetName(String s) {
        name = s;
    }

    public String GetCreatorID() {
        return creator;
    }

    public String GetCreator() {
        return GetCreatorCN();
    }

    public String GetCreatorCN() {
        return creator_cn;
    }

    public String GetCreatorFN() {
        return creator_fullname;
    }

    public void SetCreator(String s) {
        creator = s;
    }

    public void SetCreator(String uname, String deptname, String unitname) {
        creator_cn = uname;
        creator_fullname = uname + "(" + deptname + "/" + unitname + ")";
    }

    public java.util.Date GetCreatedate() {
        return createdate;
    }

    public void SetCreatedate(java.util.Date u) {
        createdate = u;
    }

    public abstract String GetClassName();

    public Class GetClass(boolean reload) throws Exception {
        if (!reload && clazz != null) return clazz;
        String class_name = "nps.runtime." + GetClassName();
        if (reload) {
            clazz = Config.GetClassLoader().ReloadClass(class_name);
        } else {
            clazz = Config.GetClassLoader().loadClass(class_name);
        }
        return clazz;
    }

    public Reader GetTemplate(NpsContext ctxt) throws Exception {
        if (template == null) LoadTemplateFromDatabase(ctxt);
        return new InputStreamReader(new FileInputStream(template), "UTF-8");
    }

    public void Clear() {
        if (template != null) try {
            template.delete();
        } catch (Exception e1) {
        }
        template = null;
    }

    private void LoadTemplateFromDatabase(NpsContext ctxt) throws NpsException {
        String sql = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Reader is = null;
        try {
            sql = "select template from template where id=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                CLOB clob = ((OracleResultSet) rs).getCLOB("template");
                if (clob != null) {
                    FileOutputStream fo = null;
                    OutputStreamWriter so = null;
                    try {
                        is = clob.getCharacterStream();
                        template = new File(Config.OUTPATH_TEMPLATE + File.separator + id + ".template");
                        if (template.exists()) try {
                            template.delete();
                        } catch (Exception e1) {
                        }
                        template.createNewFile();
                        template.deleteOnExit();
                        fo = new FileOutputStream(template);
                        so = new OutputStreamWriter(fo, "UTF-8");
                        int b;
                        while ((b = is.read()) != -1) {
                            so.write(b);
                        }
                    } catch (Exception e) {
                        throw e;
                    } finally {
                        try {
                            so.close();
                        } catch (Exception e) {
                        }
                        try {
                            fo.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            DefaultLog.error(e);
        } finally {
            if (is != null) try {
                is.close();
            } catch (Exception e1) {
            }
            if (rs != null) try {
                rs.close();
            } catch (Exception e1) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e1) {
            }
            is = null;
            rs = null;
            pstmt = null;
        }
    }

    public void GetTemplate(NpsContext ctxt, Writer writer) throws NpsException {
        Reader r = null;
        try {
            r = GetTemplate(ctxt);
            int b;
            while ((b = r.read()) != -1) {
                writer.write(b);
            }
        } catch (Exception e) {
            nps.util.DefaultLog.error_noexception(e);
        } finally {
            if (r != null) try {
                r.close();
            } catch (Exception e) {
            }
        }
    }

    protected String GenerateTemplateID(NpsContext ctxt) throws NpsException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = ctxt.GetConnection().prepareStatement("select seq_template.nextval templateid from dual");
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("templateid");
            }
            return null;
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            try {
                rs.close();
            } catch (Exception e1) {
            }
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
        return null;
    }

    public abstract void Save(NpsContext ctxt, boolean bNew) throws NpsException;

    public void UpdateTemplate(NpsContext ctxt, String content) throws NpsException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "update template set template=empty_clob() where id=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e1) {
            }
            sql = "select template from Template where id=? for update";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(1);
                java.io.Writer writer = clob.getCharacterOutputStream();
                writer.write(content);
                writer.flush();
                try {
                    writer.close();
                } catch (Exception e1) {
                }
            }
            Clear();
            DeleteJavaFiles();
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            try {
                rs.close();
            } catch (Exception e1) {
            }
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
    }

    public void UpdateTemplate(NpsContext ctxt, InputStream in) throws NpsException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "update template set template=empty_clob() where id=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e1) {
            }
            sql = "select template from Template where id=? for update";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(1);
                java.io.Writer writer = clob.getCharacterOutputStream();
                int i;
                while ((i = in.read()) != -1) {
                    writer.write(i);
                }
                writer.flush();
                try {
                    writer.close();
                } catch (Exception e1) {
                }
            }
            Clear();
            DeleteJavaFiles();
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            try {
                rs.close();
            } catch (Exception e1) {
            }
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
    }

    public void DeleteJavaFiles() {
        String javaFileName = Config.OUTPATH_ARTICLE_CLASS + "/nps/runtime/" + GetClassName() + ".java";
        File java_file = new File(javaFileName);
        if (java_file.exists()) try {
            java_file.delete();
        } catch (Exception e) {
        }
        String classFileName = Config.OUTPATH_ARTICLE_CLASS + "/nps/runtime/" + GetClassName() + ".class";
        File class_file = new File(classFileName);
        if (class_file.exists()) try {
            class_file.delete();
        } catch (Exception e) {
        }
    }

    public void Delete(NpsContext ctxt) throws NpsException {
        PreparedStatement pstmt = null;
        try {
            List topics = null;
            if (Config.CACHE) {
                topics = GetTopics(ctxt);
            }
            String sql = "delete from topic_pts where templateid=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
            sql = "update topic set art_template=null where art_template=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
            sql = "delete from template where id=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
            for (int i = 0; topics != null && i < topics.size(); i++) {
                TopicProfile profile = (TopicProfile) topics.get(i);
                Site site = ctxt.GetSite(profile.GetSiteId());
                if (site == null) continue;
                TopicTree topic_tree = site.GetTopicTree();
                if (topic_tree == null) continue;
                Topic topic = topic_tree.GetTopic(profile.GetId());
                if (topic == null) continue;
                if (this instanceof ArticleTemplate) topic.SetArticleTemplate(null); else topic.RemovePageTemplate(id);
            }
            DeleteJavaFiles();
            TemplatePool.GetPool().remove(this);
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
    }

    public abstract List GetTopics(NpsContext ctxt) throws NpsException;

    public void ViewJavaSource(Writer writer) {
        String javaFileName = Config.OUTPATH_ARTICLE_CLASS + "/nps/runtime/" + GetClassName() + ".java";
        File java_file = new File(javaFileName);
        if (!java_file.exists()) {
            try {
                writer.write(GetClassName() + ".java" + "��û����ɡ�ģ�屣����Զ���ɡ�");
                writer.flush();
            } catch (Exception e1) {
            }
            return;
        }
        InputStreamReader fr = null;
        try {
            fr = new InputStreamReader(new FileInputStream(java_file), "UTF-8");
            int b;
            while ((b = fr.read()) != -1) {
                writer.write(b);
            }
            writer.flush();
        } catch (Exception e) {
            nps.util.DefaultLog.error_noexception(e);
        } finally {
            if (fr != null) try {
                fr.close();
            } catch (Exception e1) {
            }
        }
    }

    public void Zip(NpsContext ctxt, ZipOutputStream out) throws Exception {
        if (!ZipExist(out)) {
            ZipInfo(out);
            ZipTemplate(ctxt, out);
        }
    }

    private boolean ZipExist(ZipOutputStream out) {
        String filename_info = GetClassName() + ".template";
        try {
            out.putNextEntry(new ZipEntry(filename_info));
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract void ZipInfo(ZipOutputStream out) throws Exception;

    protected void ZipTemplate(NpsContext ctxt, ZipOutputStream out) throws Exception {
        String filename_info = GetClassName() + ".data";
        out.putNextEntry(new ZipEntry(filename_info));
        Reader r = null;
        try {
            r = GetTemplate(ctxt);
            int b;
            while ((b = r.read()) != -1) {
                out.write(b);
            }
        } finally {
            if (r != null) try {
                r.close();
            } catch (Exception e1) {
            }
            Clear();
            out.closeEntry();
        }
    }

    public class TopicProfile {

        protected String id;

        protected String name;

        protected String siteid;

        protected String sitename;

        protected String unitid;

        protected String unitname;

        public TopicProfile(String id, String name, String siteid, String sitename, String unitid, String unitname) {
            this.id = id;
            this.name = name;
            this.siteid = siteid;
            this.sitename = sitename;
            this.unitid = unitid;
            this.unitname = unitname;
        }

        public String GetId() {
            return id;
        }

        public String GetName() {
            return name;
        }

        public String GetSiteId() {
            return siteid;
        }

        public String GetSiteName() {
            return sitename;
        }

        public String GetUnitId() {
            return unitid;
        }

        public String GetUnitName() {
            return unitname;
        }
    }
}
