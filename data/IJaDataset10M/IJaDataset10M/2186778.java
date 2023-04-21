package com.microfly.processor;

import com.microfly.exception.NpsException;
import com.microfly.core.NpsContext;
import com.microfly.core.IPortable;
import com.microfly.core.ZipWriter;
import com.microfly.core.User;
import java.util.Date;
import java.util.Vector;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.*;
import oracle.sql.CLOB;
import oracle.jdbc.driver.OracleResultSet;

/**
 * ��̨���񳣹涨��
 *    ֧�ֵ����ԣ�JAVASCRIPT
 *    ֧�ֵĶ�ʱ������DAILY��Offset���������ֵĸ���ģʽ
 * a new publishing system
 * Copyright (c) 2007
 *
 * @author jialin
 * @version 1.0
*/
public class Job implements Serializable, IPortable {

    public static final int JAVASCRIPT = 0;

    public static final int DYNAMICJAVA = 1;

    public static final int METHOD_DAILY = 0;

    public static final int METHOD_OFFSET = 1;

    public static final int METHOD_COMPOSITE = 2;

    private String id = null;

    private int lang = JAVASCRIPT;

    private boolean enabled = true;

    private String name = null;

    private String code = null;

    private String userrunas = null;

    private String userrunas_name = null;

    private String siteid = null;

    private String cron_exp = null;

    private String creator;

    private String creator_name;

    private Date createdate;

    private int lastrunstate;

    private String servername;

    private Date lastrundate;

    private long lastruntime;

    public Job(String id, String name, int lang, String userid, String siteid) {
        this.id = id;
        this.name = name;
        this.lang = lang;
        this.userrunas = userid;
        this.siteid = siteid;
    }

    public Job(NpsContext ctxt, String name, int lang, String userid, String siteid) throws NpsException {
        this.id = GenerateId(ctxt);
        this.name = name;
        this.lang = lang;
        this.userrunas = userid;
        this.siteid = siteid;
        this.createdate = new java.util.Date();
    }

    public static Job GetJob(NpsContext ctxt, String id) throws NpsException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Job job = null;
        try {
            String sql = "select a.*,b.name creator_name,c.name site_name,d.name runas_name from job a,users b,site c,users d" + " where a.creator=b.id and a.site=c.id and a.runas=d.id" + " and a.id=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (!rs.next()) return null;
            return GetJob(rs);
        } catch (Exception e) {
            job = null;
            com.microfly.util.DefaultLog.error(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
        }
        return job;
    }

    public static Vector GetJobs(NpsContext ctxt, String siteid) throws NpsException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector jobs = new Vector();
        try {
            String sql = "select a.*,b.name creator_name,c.name site_name,d.name runas_name from job a,users b,site c,users d" + " where a.creator=b.id and a.site=c.id and a.runas=d.id" + " and a.site=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, siteid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Job job = GetJob(rs);
                jobs.add(job);
            }
        } catch (Exception e) {
            jobs = null;
            com.microfly.util.DefaultLog.error(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
        }
        return jobs;
    }

    public static Job GetJob(ResultSet rs) throws Exception {
        Job job = new Job(rs.getString("id"), rs.getString("name"), rs.getInt("lang"), rs.getString("runas"), rs.getString("site"));
        if (rs.getInt("enabled") == 0) job.Disabled();
        job.SetExp(rs.getString("cronexp"));
        job.SetRunAsName(rs.getString("runas_name"));
        job.SetCreator(rs.getString("creator"));
        job.SeCreatorName(rs.getString("creator_name"));
        job.SetCreateDate(rs.getTimestamp("createdate"));
        job.SetLastRunInfo(rs.getString("servername"), rs.getInt("lastrunstate"), rs.getTimestamp("lastrundate"), rs.getLong("lastruntime"));
        CLOB clob = ((OracleResultSet) rs).getCLOB("code");
        job.SetCode(job.GetClob(clob));
        return job;
    }

    private String GetClob(CLOB clob) throws Exception {
        if (clob == null) return null;
        Reader is = null;
        StringWriter so = null;
        try {
            is = clob.getCharacterStream();
            so = new StringWriter();
            int b;
            while ((b = is.read()) != -1) {
                so.write(b);
            }
            return so.toString();
        } finally {
            if (so != null) try {
                so.close();
            } catch (Exception e) {
            }
            if (is != null) try {
                is.close();
            } catch (Exception e) {
            }
        }
    }

    public void SetCode(String code) {
        this.code = code;
    }

    public void SetExp(String exp) {
        this.cron_exp = exp;
    }

    public String GetExp() {
        return cron_exp;
    }

    public String GetUserRunAsName() {
        return this.userrunas_name;
    }

    public synchronized void SetLastRunInfo(String servername, int state, Date begin, long milliseconds) {
        this.lastrunstate = state;
        this.servername = servername;
        this.lastrundate = begin;
        this.lastruntime = milliseconds;
    }

    public void Enabled() {
        this.enabled = true;
    }

    public void Disabled() {
        this.enabled = false;
    }

    public boolean IsEnable() {
        return enabled;
    }

    public void SetCreator(String creator) {
        this.creator = creator;
    }

    public void SetRunAsName(String name) {
        this.userrunas_name = name;
    }

    public void SeCreatorName(String name) {
        this.creator_name = name;
    }

    public void SetCreateDate(Date date) {
        this.createdate = date;
    }

    public String GetId() {
        return id;
    }

    public String GetName() {
        return name;
    }

    public void SetName(String name) {
        this.name = name;
    }

    public int GetLang() {
        return lang;
    }

    public void SetLang(int l) {
        lang = l;
    }

    public void SetDefaultSiteId(String siteid) {
        this.siteid = siteid;
    }

    public String GetDefaultSiteId() {
        return siteid;
    }

    public void SetUserRunAs(String runas) {
        this.userrunas = runas;
    }

    public String GetUserRunAs() {
        return userrunas;
    }

    public String GetCode() {
        return code;
    }

    public String GetLastServer() {
        return servername;
    }

    public int GetLastRunState() {
        return lastrunstate;
    }

    public Date GetLastRunDate() {
        return lastrundate;
    }

    public long GetLastRunTime() {
        return lastruntime;
    }

    public String GetCreator() {
        return creator;
    }

    public String GetCreatorName() {
        return creator_name;
    }

    public Date GetCreateDate() {
        return createdate;
    }

    private String GenerateId(NpsContext ctxt) throws NpsException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select seq_job.nextval from dual";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            com.microfly.util.DefaultLog.error(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e1) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
        return null;
    }

    public void Save(NpsContext ctxt, boolean bNew) throws NpsException {
        try {
            if (bNew) {
                Save(ctxt);
            } else {
                Update(ctxt);
            }
            UpdateCode(ctxt);
        } catch (Exception e) {
            ctxt.Rollback();
            com.microfly.util.DefaultLog.error(e);
        } finally {
            ctxt.Commit();
        }
    }

    public void Delete(NpsContext ctxt) throws NpsException {
        PreparedStatement pstmt = null;
        try {
            String sql = "delete from job where id=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            com.microfly.util.DefaultLog.error(e);
        } finally {
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
        }
    }

    private void Save(NpsContext ctxt) throws Exception {
        PreparedStatement pstmt = null;
        try {
            String sql = "insert into job(id,lang,name,runas,site,enabled,cronexp,creator,createdate,code) values(?,?,?,?,?,?,?,?,?,empty_clob())";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setInt(2, lang);
            pstmt.setString(3, name);
            pstmt.setString(4, userrunas);
            pstmt.setString(5, siteid);
            pstmt.setInt(6, enabled ? 1 : 0);
            pstmt.setString(7, cron_exp);
            pstmt.setString(8, creator);
            pstmt.setTimestamp(9, new java.sql.Timestamp(createdate.getTime()));
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
        }
    }

    private void Update(NpsContext ctxt) throws Exception {
        PreparedStatement pstmt = null;
        try {
            String sql = "update job set lang=?,name=?,runas=?,site=?,enabled=?,cronexp=? where id=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setInt(1, lang);
            pstmt.setString(2, name);
            pstmt.setString(3, userrunas);
            pstmt.setString(4, siteid);
            pstmt.setInt(5, enabled ? 1 : 0);
            pstmt.setString(6, cron_exp);
            pstmt.setString(7, id);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
        }
    }

    private void UpdateCode(NpsContext ctxt) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        try {
            if (code == null || code.length() == 0) {
                sql = "update job set code=empty_clob() where id=?";
                pstmt = ctxt.GetConnection().prepareStatement(sql);
                pstmt.setString(1, id);
                pstmt.executeUpdate();
                return;
            }
            sql = "update job set code=empty_clob() where id=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            sql = "select code from job where id=? for update";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(1);
                java.io.Writer writer = clob.getCharacterOutputStream();
                writer.write(code);
                writer.flush();
                try {
                    writer.close();
                } catch (Exception e1) {
                }
            }
        } catch (Exception e) {
            com.microfly.util.DefaultLog.error(e);
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

    public void Zip(NpsContext ctxt, ZipOutputStream out) throws Exception {
        ZipInfo(ctxt, out);
        ZipCode(ctxt, out);
    }

    private void ZipInfo(NpsContext ctxt, ZipOutputStream out) throws Exception {
        String filename = "JOB" + GetId() + ".job";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            writer.println(id);
            writer.println(lang);
            writer.println(name);
            writer.println(cron_exp);
            writer.println(enabled);
            writer.println(userrunas);
            writer.println(creator);
            writer.println(createdate);
        } finally {
            out.closeEntry();
        }
    }

    private void ZipCode(NpsContext ctxt, ZipOutputStream out) throws Exception {
        String filename = "JOB" + GetId() + ".data";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            writer.print(code);
        } finally {
            out.closeEntry();
        }
    }

    public void RunImmediately(NpsContext ctxt) throws Exception {
        User runas = ctxt.GetUser().GetUser(ctxt.GetConnection(), userrunas);
        ctxt.GetSite(siteid);
        switch(lang) {
            case JAVASCRIPT:
                JavascriptJob jsp = new JavascriptJob(ctxt, runas, this);
                jsp.run();
                break;
            case DYNAMICJAVA:
                DynamicJavaJob jj = new DynamicJavaJob(ctxt, runas, this);
                jj.run();
                break;
        }
    }
}
