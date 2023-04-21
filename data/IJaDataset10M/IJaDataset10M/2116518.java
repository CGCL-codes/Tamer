package hu.sztaki.lpds.wfs.net.wsaxis13;

import java.util.*;
import java.sql.*;
import hu.sztaki.lpds.wfs.com.*;
import hu.sztaki.lpds.wfs.service.angie.Base;
import hu.sztaki.lpds.wfs.inf.WfsPortalService;
import hu.sztaki.lpds.wfs.service.angie.plugins.wfi.inf.WorkflowDescriptionGenerator;
import hu.sztaki.lpds.information.local.PropertyLoader;
import hu.sztaki.lpds.wfs.service.angie.datas.JobAndOutputsBean;
import hu.sztaki.lpds.wfs.service.angie.datas.OutputDescrioptionBean;
import hu.sztaki.lpds.wfs.service.angie.datas.WorkflowBean;
import hu.sztaki.lpds.wfs.service.angie.repository.RepositoryServiceImpl;
import hu.sztaki.lpds.wfs.utils.Status;

/**
 * @author krisztian
 */
public class WfsPortalServiceImpl implements WfsPortalService {

    private String sql;

    public WfsPortalServiceImpl() {
    }

    public Vector getAbstractWorkflows(ComDataBean value) {
        Vector res = new Vector();
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            if ((value.getWorkflowID() == null) || ("".equals(value.getWorkflowID())) || ("null".equals(value.getWorkflowID()))) {
                sql = "SELECT name,txt FROM aworkflow WHERE id_portal='" + value.getPortalID() + "' and id_user='" + value.getUserID() + "'";
            } else {
                sql = "SELECT name,txt FROM aworkflow WHERE id_portal='" + value.getPortalID() + "' and id_user='" + value.getUserID() + "' and name='" + value.getWorkflowID() + "'";
            }
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                ComDataBean tmp = new ComDataBean();
                tmp.setWorkflowID(rst.getString(1));
                tmp.setTxt(rst.getString(2));
                res.add(tmp);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("ERROR:" + sql);
            e.printStackTrace();
        }
        return res;
    }

    public Vector getTemplateWorkflows(ComDataBean value) {
        Vector res = new Vector();
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            if ((value.getWorkflowID() == null) || ("".equals(value.getWorkflowID())) || ("null".equals(value.getWorkflowID()))) {
                sql = "SELECT b.name,b.txt,a.txt,b.id,a.name FROM aworkflow as a,workflow as b WHERE id_portal='" + value.getPortalID() + "' and id_user='" + value.getUserID() + "' and id_aworkflow=a.id and wtyp=-1";
            } else {
                sql = "SELECT b.name,b.txt,a.txt,b.id,a.name FROM aworkflow as a,workflow as b WHERE id_portal='" + value.getPortalID() + "' and id_user='" + value.getUserID() + "' and id_aworkflow=a.id and wtyp=-1 and b.name='" + value.getWorkflowID() + "'";
            }
            ResultSet rst = stmt.executeQuery(sql);
            ComDataBean tmp = null;
            ;
            while (rst.next()) {
                tmp = new ComDataBean();
                tmp.setTxt((!"".equals(rst.getString(2))) ? rst.getString(2) : rst.getString(3));
                tmp.setWorkflowID(rst.getString(1));
                tmp.setGraf(rst.getString(5));
                res.add(tmp);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("ERROR:" + sql);
            e.printStackTrace();
        }
        return res;
    }

    public Vector getRealWorkflows(ComDataBean value) {
        Vector res = new Vector();
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            Statement stmt0 = conn.createStatement();
            if ((value.getWorkflowID() == null) || ("".equals(value.getWorkflowID())) || ("null".equals(value.getWorkflowID()))) {
                sql = "SELECT b.name,b.txt,a.txt,b.id,b.wtyp,a.name FROM aworkflow as a,workflow as b WHERE id_portal='" + value.getPortalID() + "' and id_user='" + value.getUserID() + "' and id_aworkflow=a.id and wtyp<>-1";
            } else {
                sql = "SELECT b.name,b.txt,a.txt,b.id,b.wtyp,a.name FROM aworkflow as a,workflow as b WHERE id_portal='" + value.getPortalID() + "' and id_user='" + value.getUserID() + "' and id_aworkflow=a.id and wtyp<>-1 and b.name='" + value.getWorkflowID() + "'";
            }
            ResultSet rst = stmt.executeQuery(sql);
            ComDataBean tmp = null;
            ;
            while (rst.next()) {
                tmp = new ComDataBean();
                String ttmp = "--";
                if (rst.getLong(5) > 0) {
                    sql = "SELECT a.name FROM workflow as a WHERE a.id=" + rst.getLong(5);
                    ResultSet rst0 = stmt0.executeQuery(sql);
                    if (rst0.next()) ttmp = rst0.getString(1);
                }
                sql = "SELECT c.wrtid,c.name,c.value FROM workflow_prop as c WHERE " + rst.getString(4) + "=c.id_workflow ORDER BY c.wrtid,c.name";
                ResultSet rst0 = stmt0.executeQuery(sql);
                String wrtid = "";
                tmp.setTxt((!rst.getString(2).equals("")) ? rst.getString(2) : rst.getString(3));
                tmp.setWorkflowID(rst.getString(1));
                tmp.setGraf(rst.getString(6));
                tmp.setParentWorkflowID(ttmp);
                tmp.setWorkflowtype(PropertyLoader.getInstance().getProperty("guse.wfs.system.defaultworkflowtype"));
                while (rst0.next()) {
                    if (!rst0.getString(1).equals(wrtid)) {
                        if (!wrtid.equals("")) res.add(tmp);
                        wrtid = rst0.getString(1);
                        tmp = new ComDataBean();
                        tmp.setTxt((!rst.getString(2).equals("")) ? rst.getString(2) : rst.getString(3));
                        tmp.setWorkflowID(rst.getString(1));
                        tmp.setGraf(rst.getString(6));
                        tmp.setParentWorkflowID(ttmp);
                        tmp.setWorkflowtype(PropertyLoader.getInstance().getProperty("guse.wfs.system.defaultworkflowtype"));
                    }
                    tmp.setWorkflowRuntimeID(rst0.getString(1));
                    if (rst0.getString(2).equals("text")) tmp.setInstanceTxt(rst0.getString(3));
                    if (rst0.getString(2).equals("status")) {
                        int ti = rst0.getInt(3);
                        if (Status.isFinished(ti) || Status.isError(ti)) tmp.setStatus(rst0.getString(3)); else tmp.setStatus("" + Status.ABORTED);
                    }
                    if (rst0.getString(2).equals("wfiurl")) tmp.setWfiURL(rst0.getString(3));
                    if (rst0.getString(2).equals("storageurl")) tmp.setStorageURL(rst0.getString(3));
                    if (rst0.getString(2).equals("appmain")) tmp.setAppmain(rst0.getString(3));
                    if (rst0.getString(2).equals("wftype")) tmp.setWorkflowtype(rst0.getString(3));
                }
                res.add(tmp);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error:" + sql);
            e.printStackTrace();
        }
        return res;
    }

    public Vector getWorkflowJobs(ComDataBean value) {
        Vector res = new Vector();
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT c.name,c.txt,c.x,c.y,c.id,b.id FROM workflow as b, ajob as c, aworkflow as d WHERE b.name='" + value.getWorkflowID() + "' and c.id_aworkflow=b.id_aworkflow and b.id_aworkflow=d.id and d.id_portal='" + value.getPortalID() + "' and d.id_user='" + value.getUserID() + "' ORDER BY c.name";
            ResultSet rst = stmt.executeQuery(sql);
            JobBean tmpJobData = new JobBean();
            String jobname = "";
            String jid = "";
            String wid = "";
            while (rst.next()) {
                if (!rst.getString(1).equals(jobname)) {
                    if (!jobname.equals("")) {
                        Vector v = new Vector();
                        sql = "SELECT x,y,prejob,preoutput FROM ainput WHERE id_ajob=" + jid;
                        Statement stmti = conn.createStatement();
                        ResultSet rsti = stmti.executeQuery(sql);
                        while (rsti.next()) {
                            v.add(new PortBean("", rsti.getString(1), rsti.getString(2), "input", rsti.getString(3), rsti.getString(4)));
                        }
                        sql = "SELECT x,y,seq FROM aoutput WHERE id_ajob=" + jid;
                        Statement stmto = conn.createStatement();
                        ResultSet rsto = stmto.executeQuery(sql);
                        while (rsto.next()) {
                            v.add(new PortBean(rsto.getString(3), rsto.getString(1), rsto.getString(2), "output"));
                        }
                        tmpJobData.setPorts(v);
                        PreparedStatement stmts = conn.prepareStatement("SELECT name,value FROM job_prop WHERE id_ajob=? and id_workflow=?");
                        stmts.setInt(1, rst.getInt(5));
                        stmts.setInt(2, rst.getInt(6));
                        ResultSet rsts = stmts.executeQuery();
                        while (rsts.next()) {
                            if (rsts.getString(1).equals("resource")) {
                                tmpJobData.setResource(rsts.getString(2));
                            }
                            if (rsts.getString(1).equals("status")) {
                                int ti = rsts.getInt(2);
                                if ((ti == 6) || (ti == 7) || (ti == 21) || (ti == 25)) tmpJobData.setStatus(rsts.getInt(2)); else tmpJobData.setStatus(15);
                            }
                        }
                        res.add(tmpJobData);
                    }
                    tmpJobData = new JobBean();
                    tmpJobData.setJobID(rst.getString(1));
                    tmpJobData.setTxt(rst.getString(2));
                    tmpJobData.setX(rst.getInt(3));
                    tmpJobData.setY(rst.getInt(4));
                    tmpJobData.setStatus(0);
                    tmpJobData.setResource("");
                    jid = rst.getString(5);
                    wid = rst.getString(6);
                    jobname = rst.getString(1);
                }
            }
            PreparedStatement stmts = conn.prepareStatement("SELECT name,value FROM job_prop WHERE id_ajob=? and id_workflow=?");
            stmts.setInt(1, Integer.parseInt(jid));
            stmts.setInt(2, Integer.parseInt(wid));
            ResultSet rsts = stmts.executeQuery();
            while (rsts.next()) {
                if (rsts.getString(1).equals("resource")) {
                    tmpJobData.setResource(rsts.getString(2));
                }
                if (rsts.getString(1).equals("status")) {
                    int ti = rsts.getInt(2);
                    if ((ti != 6) || (ti != 7) || (ti != 21) || (ti != 25)) tmpJobData.setStatus(15); else tmpJobData.setStatus(rsts.getInt(2));
                }
            }
            Vector v = new Vector();
            sql = "SELECT x,y,prejob,preoutput FROM ainput WHERE id_ajob=" + jid;
            Statement stmti = conn.createStatement();
            ResultSet rsti = stmti.executeQuery(sql);
            while (rsti.next()) {
                v.add(new PortBean("", rsti.getString(1), rsti.getString(2), "input", rsti.getString(3), rsti.getString(4)));
            }
            sql = "SELECT x,y,seq FROM aoutput WHERE id_ajob=" + jid;
            Statement stmto = conn.createStatement();
            ResultSet rsto = stmto.executeQuery(sql);
            while (rsto.next()) {
                v.add(new PortBean(rsto.getString(3), rsto.getString(1), rsto.getString(2), "output"));
            }
            tmpJobData.setPorts(v);
            res.add(tmpJobData);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Boolean saveNewWorkflow(ComDataBean value) {
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            long aWorkflowID = 0;
            long gWorkflowID = 0;
            String sql = "";
            if (value.getTyp().intValue() == 1) {
                sql = "SELECT a.id,b.id FROM aworkflow as a,workflow as b WHERE b.name='" + value.getParentWorkflowID() + "' and a.id=b.id_aworkflow and id_user='" + value.getUserID() + "' and id_portal='" + value.getPortalID() + "'";
                ResultSet rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    gWorkflowID = rst.getLong(1);
                    aWorkflowID = rst.getLong(2);
                }
                sql = "INSERT INTO workflow(id_aworkflow,name,txt,wtyp) VALUES(" + gWorkflowID + ",'" + value.getWorkflowID() + "','" + value.getTxt() + "'," + aWorkflowID + ")";
                stmt.executeUpdate(sql);
            }
            if (value.getTyp().intValue() == 0) {
                sql = "SELECT a.id FROM aworkflow as a WHERE a.name='" + value.getParentWorkflowID() + "' and a.id_user='" + value.getUserID() + "' and a.id_portal='" + value.getPortalID() + "'";
                ResultSet rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    aWorkflowID = rst.getLong(1);
                }
                sql = "INSERT INTO workflow(id_aworkflow,name,txt,wtyp) VALUES(" + aWorkflowID + ",'" + value.getWorkflowID() + "','" + value.getTxt() + "',0)";
                stmt.executeUpdate(sql);
            }
            if (value.getTyp().intValue() == 2) {
                sql = "SELECT a.id,b.id FROM aworkflow as a,workflow as b WHERE b.name='" + value.getParentWorkflowID() + "' and a.id=b.id_aworkflow and id_user='" + value.getUserID() + "' and id_portal='" + value.getPortalID() + "'";
                ResultSet rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    gWorkflowID = rst.getLong(1);
                }
                sql = "INSERT INTO workflow(id_aworkflow,name,txt,wtyp) VALUES(" + gWorkflowID + ",'" + value.getWorkflowID() + "','" + value.getTxt() + "',-1)";
                stmt.executeUpdate(sql);
            }
            if (value.getTyp().intValue() == 3) {
                sql = "SELECT a.id FROM aworkflow as a WHERE a.name='" + value.getParentWorkflowID() + "' and id_user='" + value.getUserID() + "' and id_portal='" + value.getPortalID() + "'";
                ResultSet rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    gWorkflowID = rst.getLong(1);
                }
                sql = "INSERT INTO workflow(id_aworkflow,name,txt,wtyp) VALUES(" + gWorkflowID + ",'" + value.getWorkflowID() + "','" + value.getTxt() + "',-1)";
                stmt.executeUpdate(sql);
            }
            if (value.getTyp().intValue() == 4) {
                long tid = 0;
                long wid = 0;
                sql = "SELECT a.id,b.id,b.wtyp FROM aworkflow as a,workflow as b WHERE b.name='" + value.getParentWorkflowID() + "' and a.id=b.id_aworkflow and id_user='" + value.getUserID() + "' and id_portal='" + value.getPortalID() + "'";
                ResultSet rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    gWorkflowID = rst.getLong(1);
                    tid = rst.getLong(3);
                    wid = rst.getLong(2);
                }
                sql = "INSERT INTO workflow(id_aworkflow,name,txt,wtyp) VALUES(" + gWorkflowID + ",'" + value.getWorkflowID() + "','" + value.getTxt() + "'," + tid + ")";
                stmt.executeUpdate(sql);
                ResultSet s = stmt.getGeneratedKeys();
                s.next();
                long id = s.getLong(1);
                Statement stmt0 = conn.createStatement();
                sql = "SELECT a.id_ajob,a.name,a.value,a.label,a.desc,a.inh FROM job_prop as a WHERE id_workflow=" + wid;
                rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    sql = "INSERT INTO job_prop VALUES(" + id + "," + rst.getLong(1) + ",'" + rst.getString(2) + "','" + rst.getString(3) + "','" + rst.getString(4) + "','" + rst.getString(5) + "','" + rst.getString(6) + "')";
                    stmt0.executeUpdate(sql);
                }
                sql = "SELECT a.id_ajob,a.name,a.value FROM job_desc as a WHERE id_workflow=" + wid;
                rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    sql = "INSERT INTO job_desc VALUES(" + id + "," + rst.getLong(1) + ",'" + rst.getString(2) + "','" + rst.getString(3) + "')";
                    stmt0.executeUpdate(sql);
                }
                sql = "SELECT a.id_ainput,a.name,a.value,a.label,a.desc,a.inh FROM input_prop as a WHERE id_workflow=" + wid;
                rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    sql = "INSERT INTO input_prop VALUES(" + id + "," + rst.getLong(1) + ",'" + rst.getString(2) + "','" + rst.getString(3) + "','" + rst.getString(4) + "','" + rst.getString(5) + "','" + rst.getString(6) + "')";
                    stmt0.executeUpdate(sql);
                }
                sql = "SELECT a.id_aoutput,a.name,a.value,a.label,a.desc,a.inh FROM output_prop as a WHERE id_workflow=" + wid;
                rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    sql = "INSERT INTO output_prop VALUES(" + id + "," + rst.getLong(1) + ",'" + rst.getString(2) + "','" + rst.getString(3) + "','" + rst.getString(4) + "','" + rst.getString(5) + "','" + rst.getString(6) + "')";
                    stmt0.executeUpdate(sql);
                }
                sql = "SELECT a.id_ajob,a.user,a.dat,a.mdyid,a.ovalue,a.nvalue,a.port FROM history as a WHERE id_workflow=" + wid;
                rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    sql = "INSERT INTO history VALUES(" + id + "," + rst.getLong(1) + ",'" + rst.getString(2) + "','" + rst.getString(7) + "','" + rst.getString(3) + "','" + rst.getString(4) + "','" + rst.getString(5) + "','" + rst.getString(6) + "')";
                    stmt0.executeUpdate(sql);
                }
            }
            int wfType = value.getTyp().intValue();
            if ((wfType == 0) || (wfType == 1) || (wfType == 4)) {
                workflowConfigDataErrorCheck(value);
            }
            sql = "SELECT id FROM workflow WHERE id_aworkflow=" + aWorkflowID + " and name='" + value.getWorkflowID() + "'";
            ResultSet rs = stmt.executeQuery(sql);
            sql = "";
            String tmpid = null;
            while (rs.next()) tmpid = rs.getString(1);
            if (tmpid != null) {
                sql = "INSERT INTO workflow_prop VALUES(" + tmpid + ",'*','storageurl','" + value.getStorageURL() + "')";
                stmt.executeUpdate(sql);
                sql = "INSERT INTO workflow_prop VALUES(" + tmpid + ",'*','wftype','" + value.getWorkflowtype() + "')";
                stmt.executeUpdate(sql);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Boolean(true);
    }

    public Boolean deleteWorkflowGraf(ComDataBean value) {
        try {
            Connection conn = Base.getConnection();
            long aworkflowID = 0;
            String sql = "SELECT id FROM aworkflow WHERE name=? and id_user=? and id_portal=?";
            PreparedStatement preps = conn.prepareStatement(sql);
            preps.setString(1, value.getWorkflowID());
            preps.setString(2, value.getUserID());
            preps.setString(3, value.getPortalID());
            ResultSet rst = preps.executeQuery();
            while (rst.next()) {
                aworkflowID = rst.getLong(1);
            }
            sql = "SELECT id FROM ajob WHERE id_aworkflow=?";
            preps = conn.prepareStatement(sql);
            preps.setLong(1, aworkflowID);
            rst = preps.executeQuery();
            Vector jobIDs = new Vector();
            while (rst.next()) {
                jobIDs.addElement(rst.getString(1));
            }
            sql = "DELETE FROM ainput WHERE id_ajob=?";
            preps = conn.prepareStatement(sql);
            for (int iv = 0; iv < jobIDs.size(); iv++) {
                Long ajobId = Long.parseLong((String) jobIDs.get(iv));
                preps.setLong(1, ajobId.longValue());
                preps.executeUpdate();
            }
            sql = "DELETE FROM aoutput WHERE id_ajob=?";
            preps = conn.prepareStatement(sql);
            for (int iv = 0; iv < jobIDs.size(); iv++) {
                Long ajobId = Long.parseLong((String) jobIDs.get(iv));
                preps.setLong(1, ajobId.longValue());
                preps.executeUpdate();
            }
            sql = "DELETE FROM ajob WHERE id_aworkflow=?";
            preps = conn.prepareStatement(sql);
            preps.setLong(1, aworkflowID);
            preps.executeUpdate();
            sql = "DELETE FROM aworkflow WHERE id=?";
            preps = conn.prepareStatement(sql);
            preps.setLong(1, aworkflowID);
            preps.executeUpdate();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Boolean(true);
    }

    public Boolean deleteWorkflow(ComDataBean value) {
        Base.getI().deleteFromJobIdCache(value.getPortalID(), value.getUserID(), value.getWorkflowID());
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            long workflowID = 0;
            long wtyp = 0;
            String sql = "SELECT b.id,b.wtyp FROM aworkflow as a,workflow as b WHERE b.name='" + value.getWorkflowID() + "' and a.id_user='" + value.getUserID() + "' and a.id_portal='" + value.getPortalID() + "'";
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                workflowID = rst.getLong(1);
                wtyp = rst.getLong(2);
            }
            if ((value.getWorkflowRuntimeID() == null) || (value.getWorkflowRuntimeID().equals("")) || (value.getWorkflowRuntimeID().endsWith("null"))) {
                Hashtable<String, String> wrtidHash = new Hashtable<String, String>();
                sql = "SELECT wrtid FROM workflow_prop WHERE id_workflow=" + workflowID;
                rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    String wrtid = rst.getString(1);
                    wrtidHash.put(wrtid, "");
                }
                if (!wrtidHash.isEmpty()) {
                    Enumeration<String> wrtidEnum = wrtidHash.keys();
                    while (wrtidEnum.hasMoreElements()) {
                        String wrtid = wrtidEnum.nextElement();
                        sql = "DELETE FROM job_outputs WHERE wrtid='" + wrtid + "'";
                        stmt.executeUpdate(sql);
                    }
                }
                sql = "DELETE FROM workflow WHERE id=" + workflowID;
                stmt.executeUpdate(sql);
                sql = "DELETE FROM status WHERE id_workflow=" + workflowID;
                stmt.executeUpdate(sql);
                sql = "DELETE FROM job_prop WHERE id_workflow=" + workflowID;
                stmt.executeUpdate(sql);
                sql = "DELETE FROM workflow_prop WHERE id_workflow=" + workflowID;
                stmt.executeUpdate(sql);
                sql = "DELETE FROM job_status WHERE id_workflow=" + workflowID;
                stmt.executeUpdate(sql);
                sql = "DELETE FROM history WHERE id_workflow=" + workflowID;
                stmt.executeUpdate(sql);
                sql = "DELETE FROM error_prop WHERE id_workflow=" + workflowID;
                stmt.executeUpdate(sql);
                sql = "DELETE FROM input_prop WHERE id_workflow=" + workflowID;
                stmt.executeUpdate(sql);
                sql = "DELETE FROM output_prop WHERE id_workflow=" + workflowID;
                stmt.executeUpdate(sql);
                if (wtyp == (-1)) {
                    sql = "SELECT b.id,b.wtyp,b.name FROM workflow as b WHERE b.wtyp=" + workflowID;
                    ResultSet rst2 = stmt.executeQuery(sql);
                    Statement stmt2 = conn.createStatement();
                    while (rst2.next()) {
                        workflowID = rst2.getLong(1);
                        sql = "DELETE FROM workflow WHERE id=" + workflowID;
                        stmt2.executeUpdate(sql);
                        sql = "DELETE FROM status WHERE id_workflow=" + workflowID;
                        stmt2.executeUpdate(sql);
                        sql = "DELETE FROM job_prop WHERE id_workflow=" + workflowID;
                        stmt2.executeUpdate(sql);
                        sql = "DELETE FROM workflow_prop WHERE id_workflow=" + workflowID;
                        stmt2.executeUpdate(sql);
                        sql = "DELETE FROM job_status WHERE id_workflow=" + workflowID;
                        stmt2.executeUpdate(sql);
                        sql = "DELETE FROM history WHERE id_workflow=" + workflowID;
                        stmt2.executeUpdate(sql);
                        sql = "DELETE FROM error_prop WHERE id_workflow=" + workflowID;
                        stmt2.executeUpdate(sql);
                    }
                }
            } else {
                sql = "DELETE FROM status WHERE id_workflow=" + workflowID + " and id_rt='" + value.getWorkflowRuntimeID() + "'";
                stmt.executeUpdate(sql);
                sql = "DELETE FROM workflow_prop WHERE id_workflow=" + workflowID + " and wrtid='" + value.getWorkflowRuntimeID() + "'";
                stmt.executeUpdate(sql);
                sql = "DELETE FROM job_status WHERE id_workflow=" + workflowID + " and wrtid='" + value.getWorkflowRuntimeID() + "'";
                stmt.executeUpdate(sql);
                sql = "DELETE FROM job_outputs WHERE wrtid='" + value.getWorkflowRuntimeID() + "'";
                stmt.executeUpdate(sql);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Boolean(true);
    }

    public void submitWorkflow(ComDataBean value) {
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            long workflowID = 0;
            String sql = "SELECT b.id FROM aworkflow as a,workflow as b WHERE b.name='" + value.getWorkflowID() + "' and a.id_user='" + value.getUserID() + "' and a.id_portal='" + value.getPortalID() + "'";
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                workflowID = rst.getLong(1);
            }
            sql = "DELETE FROM status WHERE id_workflow=" + workflowID;
            stmt.executeUpdate(sql);
            sql = "DELETE FROM job_prop WHERE id_workflow=" + workflowID;
            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector getPozition(ComDataBean value) {
        Vector res = new Vector();
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT c.id,c.name,c.txt,c.id_aworkflow,d.name,d.value FROM workflow as a,aworkflow as b, ajob as c, job_prop as d WHERE b.id_portal='" + value.getPortalID() + "' and b.id_user='" + value.getUserID() + "' and a.name='" + value.getWorkflowID() + "' and a.id_aworkflow=b.id and c.id_aworkflow=b.id and c.id=d.id_ajob and a.id=d.id_workflow and (d.name='x' or d.name='y') ORDER BY c.name";
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                JobBean tmpJobData = new JobBean();
                tmpJobData.setJobID(rst.getString(2));
                tmpJobData.setTxt(rst.getString(3));
                String sql0 = "SELECT a.name,a.value FROM job_prop as a, workflow as b WHERE b.id_aworkflow=" + rst.getString(4) + " and b.name='" + value.getWorkflowID() + "' and b.id=a.id_workflow and a.id_ajob=" + rst.getString(1) + " ORDER BY a.name";
                Statement stmt0 = conn.createStatement();
                ResultSet rst0 = stmt0.executeQuery(sql0);
                while (rst0.next()) {
                    if (rst0.getString(1).equals("resource")) {
                        tmpJobData.setResource(rst0.getString(2));
                    } else if (rst0.getString(1).equals("status")) {
                        tmpJobData.setStatus(rst0.getInt(2));
                    }
                }
                res.add(tmpJobData);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Vector getNormalInputs(ComDataBean value) {
        Vector res = new Vector();
        String sql = "SELECT c.name,d.name FROM workflow as a,aworkflow as b, ajob as c, ainput as d WHERE b.id_user='" + value.getUserID() + "' and b.id_portal='" + value.getPortalID() + "' and a.name='" + value.getWorkflowID() + "'  and a.id_aworkflow=b.id and c.id_aworkflow=b.id and c.id=d.id_ajob and c.id=d.id_ajob and d.prejob='' ORDER BY c.name,d.name";
        ResultSet rst;
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                res.add(rst.getString(1) + "/" + rst.getString(2));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
 * WorkflowID meghatarozasa
 * @param pPortalID portal azonosito
 * @param pUser user azonosito
 * @param pWorkflowName  Keresett wf neve
 * @return Template workflow belso azonositoja
 * @throws java.lang.ClassNotFoundException konfiguracios hiba
 * @throws java.sql.SQLException konfiguracios vagy SQL hiba
 * @throws NullPointerException wf nem talalhato
 */
    private WorkflowBean getWorkflowBean(String pPortalID, String pUserID, String pWorkflowName) throws ClassNotFoundException, SQLException, NullPointerException {
        WorkflowBean res = null;
        sql = "SELECT a.id, a.wtyp,id_aworkflow FROM workflow as a, aworkflow as b WHERE b.id_portal=? AND b.id_user=? AND a.id_aworkflow=b.id AND a.name=?";
        Connection conn = Base.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, pPortalID);
        ps.setString(2, pUserID);
        ps.setString(3, pWorkflowName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            res = new WorkflowBean();
            res.setWorkflowid(rs.getLong(1));
            res.setTemplateid(rs.getLong(2));
            res.setId_aworkflow(rs.getLong(3));
        }
        conn.close();
        if (res == null) throw new NullPointerException("Nincs ilyen wf");
        return res;
    }

    /**
 * Define Workflow outputs
 * @param pAWFID abstract wf ID
 * @return Template workflow inner ID
 * @throws java.lang.ClassNotFoundException configuration error
 * @throws java.sql.SQLException configuration or SQL error
 */
    private Vector<JobAndOutputsBean> getWorkflowOutputs(long pAWFID) throws ClassNotFoundException, SQLException {
        Vector<JobAndOutputsBean> res = new Vector<JobAndOutputsBean>();
        sql = "SELECT c.name,c.id,d.name,d.id FROM ajob as c, aoutput as d WHERE c.id_aworkflow=? and c.id=d.id_ajob ORDER BY c.name,d.name";
        Connection conn = Base.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Connection.TRANSACTION_NONE, ResultSet.CONCUR_UPDATABLE);
        ps.setLong(1, pAWFID);
        ResultSet rs = ps.executeQuery();
        boolean jobisleav = false;
        while (rs.next()) {
            jobisleav = false;
            for (JobAndOutputsBean t : res) {
                if (t.getJobname().equals(rs.getString(1))) {
                    t.addOutput(rs.getString(3), new OutputDescrioptionBean(rs.getLong(4), rs.getString(3)));
                    jobisleav = true;
                }
            }
            if (!jobisleav) {
                JobAndOutputsBean t = new JobAndOutputsBean();
                t.setJobid(rs.getLong(2));
                t.setJobname(rs.getString(1));
                t.addOutput(rs.getString(3), new OutputDescrioptionBean(rs.getLong(4), rs.getString(3)));
                res.add(t);
            }
        }
        conn.close();
        return res;
    }

    /**
 * Output propery analysis(embedding,generator)
 * @param pWFID WF inner ID
 * @param pOutputPort current port descriptor bean
 * @return current port descriptor bean
 * @throws java.lang.ClassNotFoundException configuration error
 * @throws java.sql.SQLException configuration or SQL error
 */
    private OutputDescrioptionBean actualOutputPortWithEmbededAndGeneratorProperties(long pWFID, OutputDescrioptionBean pOutputPort) throws ClassNotFoundException, SQLException {
        sql = "SELECT name,value FROM output_prop WHERE id_workflow=? AND	id_aoutput=? AND (name='maincount' OR name='ioutput') ";
        Connection conn = Base.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, pWFID);
        ps.setLong(2, pOutputPort.getId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            if ("maincount".equals(rs.getString(1))) if (1 < rs.getLong(2)) pOutputPort.setGenerator(true);
        }
        conn.close();
        return pOutputPort;
    }

    @Override
    public Vector getNormalOutputs(ComDataBean value) {
        Vector res = new Vector();
        String outputName;
        OutputDescrioptionBean outputPort;
        WorkflowBean workflow;
        Vector<JobAndOutputsBean> workflowjobsoutputs;
        try {
            workflow = getWorkflowBean(value.getPortalID(), value.getUserID(), value.getWorkflowID());
            workflowjobsoutputs = getWorkflowOutputs(workflow.getId_aworkflow());
            for (JobAndOutputsBean jobOutputs : workflowjobsoutputs) {
                Enumeration<String> enm = jobOutputs.getOutputsName();
                while (enm.hasMoreElements()) {
                    outputName = enm.nextElement();
                    outputPort = actualOutputPortWithEmbededAndGeneratorProperties(workflow.getWorkflowid(), jobOutputs.getOutputDescription(outputName));
                    outputPort = actualOutputPortWithEmbededAndGeneratorProperties(workflow.getTemplateid(), outputPort);
                    jobOutputs.setOutputDescription(outputName, outputPort);
                }
            }
            for (JobAndOutputsBean jobOutputs : workflowjobsoutputs) {
                Enumeration<String> enm = jobOutputs.getOutputsName();
                while (enm.hasMoreElements()) {
                    outputName = enm.nextElement();
                    res.add(jobOutputs.getJobname() + "/" + outputName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void test(ComDataBean p) {
        System.out.println(p);
    }

    @Override
    public Vector getWorkflowConfigDataError(ComDataBean value) {
        Vector ret = new Vector();
        try {
            ret = getWorkflowConfigDataErrorReal(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public Vector getWorkflowConfigDataErrorReal(ComDataBean value) throws Exception {
        Vector ret = new Vector();
        Connection conn = Base.getConnection();
        try {
            String esql = "SELECT ep.id_workflow, ep.jobname, ep.id_port, ep.id_error " + "FROM aworkflow as aw, workflow as w, error_prop as ep " + "WHERE aw.id = w.id_aworkflow and aw.id_portal = ? and aw.id_user = ? " + "and w.name = ? and w.id = ep.id_workflow ORDER BY ep.jobname, ep.id_port";
            PreparedStatement prepStmpErrorProp = conn.prepareStatement(esql);
            prepStmpErrorProp.setString(1, value.getPortalID());
            prepStmpErrorProp.setString(2, value.getUserID());
            prepStmpErrorProp.setString(3, value.getWorkflowID());
            ResultSet rs = prepStmpErrorProp.executeQuery();
            while (rs.next()) {
                ret.add(new WorkflowConfigErrorBean(rs.getLong("id_workflow"), rs.getString("jobname"), rs.getString("id_port"), rs.getString("id_error")));
            }
            conn.close();
        } catch (Exception e) {
            conn.close();
            e.printStackTrace();
            throw e;
        }
        return ret;
    }

    /**
     * note : this invocation is needed big SQL resource because of the method invocation within getWorkflowConfigData() method 
     *
     * @param value - workflow descriptor
     */
    public void workflowConfigDataErrorCheck(ComDataBean value) {
        try {
            if ((value.getTyp() == 0) || (value.getTyp() == 1) || (value.getTyp() == 4)) {
                Connection conn = Base.getConnection();
                String esql = "SELECT w.id, w.name, w.txt FROM aworkflow as aw, workflow as w WHERE " + "aw.id = w.id_aworkflow and aw.id_portal = ? and aw.id_user = ? and w.name = ?";
                PreparedStatement prepStmpErrorProp = conn.prepareStatement(esql);
                prepStmpErrorProp.setString(1, value.getPortalID());
                prepStmpErrorProp.setString(2, value.getUserID());
                prepStmpErrorProp.setString(3, value.getWorkflowID());
                ResultSet rs = prepStmpErrorProp.executeQuery();
                if (rs.next()) {
                    workflowConfigDataErrorCheck(rs.getString("id"), value, getWorkflowConfigData(value));
                }
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void workflowConfigDataErrorCheck(String workflowIDStr, ComDataBean value, Vector pData) {
        String wfType = value.getWorkflowtype();
        if ((wfType == null) || ("".equals(wfType)) || ("null".equals(wfType))) {
            wfType = "zen";
        }
        String classname = PropertyLoader.getInstance().getProperty("guse.wfs.wfiplugin." + wfType);
        WorkflowDescriptionGenerator tmp;
        try {
            tmp = (WorkflowDescriptionGenerator) Class.forName(classname).newInstance();
            tmp.workflowConfigDataErrorCheck(workflowIDStr, value, pData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean setWorkflowConfigData(ComDataBean pID, Vector pData) {
        Boolean ret = new Boolean(true);
        try {
            ret = setWorkflowConfigDataReal(pID, pData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public Boolean setWorkflowConfigDataReal(ComDataBean pID, Vector pData) throws Exception {
        Vector res = pData;
        for (int i = 0; i < res.size(); i++) {
            JobPropertyBean t = (JobPropertyBean) res.get(i);
            Iterator it = t.getInherited().keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                String inhvalue = "" + t.getInherited().get(key);
                if (!"---".equals(inhvalue) && !"null".equals(inhvalue)) {
                    for (int j = 0; j < res.size(); j++) {
                        if (inhvalue.equals(((JobPropertyBean) res.get(j)).getName())) {
                            ((JobPropertyBean) res.get(i)).addExe("" + key, "" + ((JobPropertyBean) res.get(j)).getExe().get(key));
                        }
                    }
                }
            }
            try {
                for (int ii = 0; ii < t.getInputs().size(); ii++) {
                    it = ((PortDataBean) t.getInputs().get(ii)).getInherited().keySet().iterator();
                    long portid = ((PortDataBean) t.getInputs().get(ii)).getId();
                    while (it.hasNext()) {
                        Object key = it.next();
                        String inhvalue = "" + ((PortDataBean) t.getInputs().get(ii)).getInherited().get(key);
                        if (!"---".equals(inhvalue) && !"null".equals(inhvalue)) {
                            String[] jp = inhvalue.split("/");
                            for (int j = 0; j < res.size(); j++) {
                                if (jp[0].equals(((JobPropertyBean) res.get(j)).getName())) {
                                    for (int jjp = 0; jjp < ((JobPropertyBean) res.get(j)).getInputs().size(); jjp++) {
                                        if (((PortDataBean) ((JobPropertyBean) res.get(j)).getInputs().get(jjp)).getName().equals(jp[1])) {
                                            String cvalue = "" + ((PortDataBean) ((JobPropertyBean) res.get(j)).getInputs().get(jjp)).getData().get(key);
                                            ((PortDataBean) (((JobPropertyBean) res.get(i)).getInput("" + portid))).getData().put(key, cvalue);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                for (int ii = 0; ii < t.getOutputs().size(); ii++) {
                    it = ((PortDataBean) t.getOutputs().get(ii)).getInherited().keySet().iterator();
                    long portid = ((PortDataBean) t.getOutputs().get(ii)).getId();
                    while (it.hasNext()) {
                        Object key = it.next();
                        String inhvalue = "" + ((PortDataBean) t.getOutputs().get(ii)).getInherited().get(key);
                        if (!"---".equals(inhvalue) && !"null".equals(inhvalue)) {
                            String[] jp = inhvalue.split("/");
                            for (int j = 0; j < res.size(); j++) {
                                if (jp[0].equals(((JobPropertyBean) res.get(j)).getName())) {
                                    for (int jjp = 0; jjp < ((JobPropertyBean) res.get(j)).getOutputs().size(); jjp++) {
                                        if (((PortDataBean) ((JobPropertyBean) res.get(j)).getOutputs().get(jjp)).getName().equals(jp[1])) {
                                            String cvalue = "" + ((PortDataBean) ((JobPropertyBean) res.get(j)).getOutputs().get(jjp)).getData().get(key);
                                            ((PortDataBean) (((JobPropertyBean) res.get(i)).getOutput("" + portid))).getData().put(key, cvalue);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String sql = "SELECT a.id FROM workflow as a,aworkflow as b WHERE b.id_portal='" + pID.getPortalID() + "' and b.id_user='" + pID.getUserID() + "' and a.name='" + pID.getWorkflowID() + "' and a.id_aworkflow=b.id";
        String workflowID = "";
        Connection conn = Base.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                workflowID = rst.getString(1);
            }
            if (workflowID.equals("")) {
                saveNewWorkflow(pID);
                sql = "SELECT a.id FROM workflow as a,aworkflow as b WHERE b.id_portal='" + pID.getPortalID() + "' and b.id_user='" + pID.getUserID() + "' and a.name='" + pID.getWorkflowID() + "' and a.id_aworkflow=b.id";
                stmt = conn.createStatement();
                rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    workflowID = rst.getString(1);
                }
            }
            if ((pID.getTyp() == null) || (pID.getTyp() == 1) || (pID.getTyp() == 0)) {
                sql = "DELETE FROM error_prop WHERE id_workflow=" + workflowID;
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                workflowConfigDataErrorCheck(workflowID, pID, pData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < pData.size(); i++) {
            JobPropertyBean tmp = (JobPropertyBean) pData.get(i);
            sql = "DELETE FROM job_prop WHERE id_workflow=" + workflowID + " and id_ajob=" + tmp.getId();
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                sql = "DELETE FROM job_desc WHERE id_workflow=" + workflowID + " and id_ajob=" + tmp.getId();
                stmt.executeUpdate(sql);
                sql = "INSERT INTO job_prop VALUES(?,?,?,?,?,?,?)";
                PreparedStatement prep = conn.prepareStatement(sql);
                Iterator itl = tmp.getExe().keySet().iterator();
                while (itl.hasNext()) {
                    String key = "" + itl.next();
                    prep.setString(1, workflowID);
                    prep.setLong(2, tmp.getId());
                    prep.setString(3, key);
                    prep.setString(4, (String) tmp.getExe().get(key));
                    prep.setString(5, "" + tmp.getLabel().get(key));
                    prep.setString(6, "" + tmp.getDesc0().get(key));
                    prep.setString(7, "" + tmp.getInherited().get(key));
                    prep.execute();
                }
                for (int j = 0; j < tmp.getInputs().size(); j++) {
                    PortDataBean tmp0 = (PortDataBean) tmp.getInputs().get(j);
                    sql = "DELETE FROM input_prop WHERE id_workflow=" + workflowID + " and id_ainput=" + tmp0.getId();
                    stmt.executeUpdate(sql);
                    sql = "INSERT INTO input_prop VALUES(?, ?, ?, ?,?,?,?)";
                    prep = conn.prepareStatement(sql);
                    itl = tmp0.getData().keySet().iterator();
                    while (itl.hasNext()) {
                        String key = "" + itl.next();
                        prep.setString(1, workflowID);
                        prep.setLong(2, tmp0.getId());
                        prep.setString(3, key);
                        prep.setString(4, (String) tmp0.getData().get(key));
                        prep.setString(5, "" + tmp0.getLabel().get(key));
                        prep.setString(6, "" + tmp0.getDesc().get(key));
                        prep.setString(7, "" + tmp0.getInherited().get(key));
                        prep.execute();
                    }
                }
                boolean maincountflag = false;
                boolean intnameflag = false;
                PortDataBean tmp0 = null;
                for (int j = 0; j < tmp.getOutputs().size(); j++) {
                    tmp0 = (PortDataBean) tmp.getOutputs().get(j);
                    sql = "DELETE FROM output_prop WHERE id_workflow=" + workflowID + " and id_aoutput=" + tmp0.getId();
                    stmt.executeUpdate(sql);
                    sql = "INSERT INTO output_prop VALUES(?,?,?,?,?,?,?)";
                    prep = conn.prepareStatement(sql);
                    itl = tmp0.getData().keySet().iterator();
                    while (itl.hasNext()) {
                        String key = "" + itl.next();
                        if (key.equals("maincount")) maincountflag = true;
                        if (key.equals("intname")) intnameflag = true;
                        prep.setString(1, workflowID);
                        prep.setLong(2, tmp0.getId());
                        prep.setString(3, key);
                        prep.setString(4, (String) tmp0.getData().get(key));
                        prep.setString(5, "" + tmp0.getLabel().get(key));
                        prep.setString(6, "" + tmp0.getDesc().get(key));
                        prep.setString(7, "" + tmp0.getInherited().get(key));
                        prep.execute();
                    }
                }
                if ((!maincountflag) && (tmp0 != null)) {
                    sql = "INSERT INTO output_prop VALUES(" + workflowID + "," + tmp0.getId() + ",'maincount','1','null','null','null')";
                    stmt.executeUpdate(sql);
                }
                if ((!intnameflag) && (tmp0 != null)) {
                    sql = "INSERT INTO output_prop VALUES(" + workflowID + "," + tmp0.getId() + ",'intname','" + tmp0.getName() + "','null','null','null')";
                    stmt.executeUpdate(sql);
                }
                itl = tmp.getDesc().keySet().iterator();
                while (itl.hasNext()) {
                    String key = "" + itl.next();
                    sql = "INSERT INTO job_desc VALUES(" + workflowID + "," + tmp.getId() + ",'" + key + "','" + tmp.getDesc().get(key) + "')";
                    stmt.executeUpdate(sql);
                }
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                String DATE_FORMAT = "yyyy-MM-dd-HH:mm:ss";
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
                sdf.setTimeZone(TimeZone.getDefault());
                String dat = sdf.format(cal.getTime());
                sql = "INSERT INTO history VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                prep = conn.prepareStatement(sql);
                for (int j = 0; j < tmp.getHistory().size(); j++) {
                    prep.setString(1, workflowID);
                    prep.setLong(2, tmp.getId());
                    prep.setString(3, pID.getUserID());
                    prep.setString(4, ((HistoryBean) tmp.getHistory().get(j)).getPort());
                    prep.setString(5, dat);
                    prep.setString(6, ((HistoryBean) tmp.getHistory().get(j)).getMdyid());
                    prep.setString(7, ((HistoryBean) tmp.getHistory().get(j)).getOvalue());
                    prep.setString(8, ((HistoryBean) tmp.getHistory().get(j)).getNvalue());
                    prep.execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Base.getI().deleteAllJobDescriptionFromCache(pID.getPortalID(), pID.getUserID(), pID.getWorkflowID());
        return new Boolean(true);
    }

    public Vector getWorkflowProps(ComDataBean pID) {
        Vector res = new Vector();
        Connection conn = null;
        try {
            String sql = "SELECT a.id FROM workflow as a,aworkflow as b WHERE b.id_portal='" + pID.getPortalID() + "' and b.id_user='" + pID.getUserID() + "' and a.name='" + pID.getWorkflowID() + "' and a.id_aworkflow=b.id";
            String workflowID = "";
            conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                workflowID = rst.getString(1);
            }
            if (workflowID.equals("")) {
                conn.close();
                return res;
            }
            sql = "SELECT value FROM workflow_prop WHERE id_workflow=" + workflowID + " AND name='wfprop'";
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                res.add(rst.getString("value"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void setWorkflowProps(ComDataBean pID, Vector wfprop) {
        System.out.println("WFS:setWorkflowProps:" + wfprop.get(0));
        if (!wfprop.isEmpty()) {
            Connection conn = null;
            try {
                String sql = "SELECT a.id FROM workflow as a,aworkflow as b WHERE b.id_portal='" + pID.getPortalID() + "' and b.id_user='" + pID.getUserID() + "' and a.name='" + pID.getWorkflowID() + "' and a.id_aworkflow=b.id";
                String workflowID = "";
                conn = Base.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    workflowID = rst.getString(1);
                }
                if (workflowID.equals("")) {
                    conn.close();
                    return;
                }
                sql = "DELETE FROM workflow_prop WHERE id_workflow=" + workflowID + " AND name='wfprop'";
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                sql = "INSERT INTO workflow_prop VALUES(?,'*','wfprop',?)";
                PreparedStatement prep = conn.prepareStatement(sql);
                for (int j = 0; j < wfprop.size(); j++) {
                    prep.setLong(1, Long.parseLong(workflowID));
                    prep.setString(2, "" + wfprop.get(j));
                    prep.execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Vector getWorkflowConfigData(ComDataBean value) {
        Vector res = new Vector();
        try {
            res = getWorkflowConfigDataReal(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Vector getWorkflowConfigDataReal(ComDataBean value) throws Exception {
        Vector res = new Vector();
        Connection conn = Base.getConnection();
        try {
            String workflowID = "0";
            String parentWorkflowID = "0";
            String sql = "SELECT a.id, a.wtyp FROM workflow as a, aworkflow as b WHERE b.id_portal='" + value.getPortalID() + "' and b.id_user='" + value.getUserID() + "' and a.name='" + value.getWorkflowID() + "' and a.id_aworkflow=b.id";
            Statement stmt = conn.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                workflowID = rst.getString(1);
                parentWorkflowID = rst.getString(2);
            }
            JobPropertyBean tmp;
            JobPropertyBean ttmp;
            sql = "SELECT c.name, c.txt, c.x, c.y, c.id FROM workflow as a, aworkflow as b, ajob as c WHERE b.id_portal='" + value.getPortalID() + "' and b.id_user='" + value.getUserID() + "' and a.name='" + value.getWorkflowID() + "' and a.id_aworkflow=b.id and c.id_aworkflow=b.id ORDER BY c.name";
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                tmp = new JobPropertyBean();
                tmp = getJobProperty(conn, workflowID, rst.getString(5));
                tmp.setName(rst.getString(1));
                tmp.setTxt(rst.getString(2));
                tmp.setX(rst.getLong(3));
                tmp.setY(rst.getLong(4));
                tmp.setId(rst.getLong(5));
                if (Long.parseLong(parentWorkflowID) > 0) {
                    ttmp = new JobPropertyBean();
                    ttmp = getJobProperty(conn, parentWorkflowID, rst.getString(5));
                    String key = "";
                    Iterator enm = ttmp.getExe().keySet().iterator();
                    while (enm.hasNext()) {
                        key = "" + enm.next();
                        if (ttmp.getLabel().get(key).equals("")) {
                            tmp.addExeLock(key, "" + ttmp.getExe().get(key));
                        } else if (tmp.getExe().get(key) == null) {
                            tmp.addExe(key, "" + ttmp.getExe().get(key));
                        }
                        tmp.addLabel(key, "" + ttmp.getLabel().get(key));
                        tmp.addDesc0(key, "" + ttmp.getDesc0().get(key));
                        tmp.addInherited(key, "" + ttmp.getInherited().get(key));
                    }
                    for (int ii = 0; ii < ttmp.getInputs().size(); ii++) {
                        enm = ((PortDataBean) ttmp.getInputs().get(ii)).getData().keySet().iterator();
                        while (enm.hasNext()) {
                            key = "" + enm.next();
                            if (((PortDataBean) ttmp.getInputs().get(ii)).getLabel().get(key).equals("")) {
                                tmp.getInput("" + ((PortDataBean) ttmp.getInputs().get(ii)).getId()).setDisabled(key, ((PortDataBean) ttmp.getInputs().get(ii)).get(key));
                            } else if (tmp.getInput("" + ((PortDataBean) ttmp.getInputs().get(ii)).getId()).getData().get(key) == null) {
                                tmp.getInput("" + ((PortDataBean) ttmp.getInputs().get(ii)).getId()).getData().put(key, ((PortDataBean) ttmp.getInputs().get(ii)).get(key));
                            }
                            tmp.getInput("" + ((PortDataBean) ttmp.getInputs().get(ii)).getId()).getLabel().put(key, ((PortDataBean) ttmp.getInputs().get(ii)).getLabel().get(key));
                            tmp.getInput("" + ((PortDataBean) ttmp.getInputs().get(ii)).getId()).getDesc().put(key, ((PortDataBean) ttmp.getInputs().get(ii)).getDesc().get(key));
                            tmp.getInput("" + ((PortDataBean) ttmp.getInputs().get(ii)).getId()).getInherited().put(key, ((PortDataBean) ttmp.getInputs().get(ii)).getInherited().get(key));
                            tmp.addExeLock(key, "" + ttmp.getExe().get(key));
                        }
                    }
                    for (int ii = 0; ii < ttmp.getOutputs().size(); ii++) {
                        enm = ((PortDataBean) ttmp.getOutputs().get(ii)).getData().keySet().iterator();
                        while (enm.hasNext()) {
                            key = "" + enm.next();
                            if (((PortDataBean) ttmp.getOutputs().get(ii)).getLabel().get(key).equals("")) {
                                tmp.getOutput("" + ((PortDataBean) ttmp.getOutputs().get(ii)).getId()).setDisabled(key, ((PortDataBean) ttmp.getOutputs().get(ii)).get(key));
                            } else if (tmp.getOutput("" + ((PortDataBean) ttmp.getOutputs().get(ii)).getId()).getData().get(key) == null) {
                                tmp.getOutput("" + ((PortDataBean) ttmp.getOutputs().get(ii)).getId()).getData().put(key, ((PortDataBean) ttmp.getOutputs().get(ii)).get(key));
                            }
                            tmp.getOutput("" + ((PortDataBean) ttmp.getOutputs().get(ii)).getId()).getLabel().put(key, ((PortDataBean) ttmp.getOutputs().get(ii)).getLabel().get(key));
                            tmp.getOutput("" + ((PortDataBean) ttmp.getOutputs().get(ii)).getId()).getDesc().put(key, ((PortDataBean) ttmp.getOutputs().get(ii)).getDesc().get(key));
                            tmp.getOutput("" + ((PortDataBean) ttmp.getOutputs().get(ii)).getId()).getInherited().put(key, ((PortDataBean) ttmp.getOutputs().get(ii)).getInherited().get(key));
                            tmp.addExeLock(key, "" + ttmp.getExe().get(key));
                        }
                    }
                }
                res.add(tmp);
            }
            conn.close();
        } catch (Exception e) {
            conn.close();
            e.printStackTrace();
            throw e;
        }
        return res;
    }

    private JobPropertyBean getJobProperty(Connection pconn, String workflowID, String ajobID) throws Exception {
        JobPropertyBean res = new JobPropertyBean();
        try {
            String sql = "SELECT name, value, label, `desc`, inh FROM job_prop WHERE id_workflow = ? and id_ajob = ?";
            PreparedStatement preps = pconn.prepareStatement(sql);
            preps.setString(1, workflowID);
            preps.setString(2, ajobID);
            ResultSet rst = preps.executeQuery();
            while (rst.next()) {
                res.addExe(rst.getString(1), rst.getString(2));
                res.addLabel(rst.getString(1), rst.getString(3));
                res.addDesc0(rst.getString(1), rst.getString(4));
                res.addInherited(rst.getString(1), rst.getString(5));
            }
            sql = "SELECT id, name, txt, prejob, preoutput, seq, x, y FROM ainput WHERE id_ajob = ?";
            preps = pconn.prepareStatement(sql);
            preps.setString(1, ajobID);
            rst = preps.executeQuery();
            HashMap tmp0 = new HashMap();
            HashMap tmp1 = new HashMap();
            HashMap tmp2 = new HashMap();
            HashMap tmp3 = new HashMap();
            String sql0 = "SELECT name, value, label, `desc`, inh FROM input_prop WHERE id_workflow = ? and id_ainput = ?";
            PreparedStatement preps0 = pconn.prepareStatement(sql0);
            preps0.setString(1, workflowID);
            while (rst.next()) {
                tmp0 = new HashMap();
                tmp1 = new HashMap();
                tmp2 = new HashMap();
                tmp3 = new HashMap();
                PortDataBean idata = new PortDataBean(rst.getLong(1), rst.getLong(6), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getLong(7), rst.getLong(8));
                preps0.setString(2, rst.getString(1));
                ResultSet rst0 = preps0.executeQuery();
                while (rst0.next()) {
                    tmp0.put(rst0.getString(1), rst0.getString(2));
                    tmp1.put(rst0.getString(1), rst0.getString(3));
                    tmp2.put(rst0.getString(1), rst0.getString(4));
                    tmp3.put(rst0.getString(1), rst0.getString(5));
                }
                idata.setData(tmp0);
                idata.setLabel(tmp1);
                idata.setDesc(tmp2);
                idata.setInherited(tmp3);
                res.addInput(idata);
            }
            sql = "SELECT id, name, txt, seq, x, y FROM aoutput WHERE id_ajob = ?";
            preps = pconn.prepareStatement(sql);
            preps.setString(1, ajobID);
            rst = preps.executeQuery();
            sql0 = "SELECT name, value, label, `desc`, inh FROM output_prop WHERE id_workflow = ? and id_aoutput = ?";
            preps0 = pconn.prepareStatement(sql0);
            preps0.setString(1, workflowID);
            while (rst.next()) {
                tmp0 = new HashMap();
                tmp1 = new HashMap();
                tmp2 = new HashMap();
                tmp3 = new HashMap();
                PortDataBean odata = new PortDataBean(rst.getLong(1), rst.getLong(4), rst.getString(2), rst.getString(3), null, null, rst.getLong(5), rst.getLong(6));
                preps0.setString(2, rst.getString(1));
                ResultSet rst0 = preps0.executeQuery();
                while (rst0.next()) {
                    tmp0.put(rst0.getString(1), rst0.getString(2));
                    tmp1.put(rst0.getString(1), rst0.getString(3));
                    tmp2.put(rst0.getString(1), rst0.getString(4));
                    tmp3.put(rst0.getString(1), rst0.getString(5));
                }
                odata.setData(tmp0);
                odata.setLabel(tmp1);
                odata.setDesc(tmp2);
                odata.setInherited(tmp3);
                res.addOutput(odata);
            }
            sql = "SELECT user, dat, mdyid, ovalue, nvalue, port FROM history WHERE id_workflow = ? and id_ajob = ? ORDER BY dat desc LIMIT 0," + PropertyLoader.getInstance().getProperty("history.max");
            preps = pconn.prepareStatement(sql);
            preps.setString(1, workflowID);
            preps.setString(2, ajobID);
            rst = preps.executeQuery();
            while (rst.next()) {
                HistoryBean odata = new HistoryBean(rst.getString(2), rst.getString(6), rst.getString(1), rst.getString(3), rst.getString(4), rst.getString(5));
                res.addHistory(odata);
            }
            sql = "SELECT name, value FROM job_desc WHERE id_workflow = ? and id_ajob = ?";
            preps = pconn.prepareStatement(sql);
            preps.setString(1, workflowID);
            preps.setString(2, ajobID);
            rst = preps.executeQuery();
            while (rst.next()) {
                res.addDesc(rst.getString(1), rst.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return res;
    }

    /**
     * It returnes jobs that contain embedded workflow "iworkflow" references.
     * 
     *
     * It is only listing and returns jobs within embbedding feature
     * 
     * (It used by storage during the zip creation)
     *
     * @return vector in which are JobPropertyBean-s as embedded jobs
     * 
     *
     * (WorkflowXMLBuilder uses)
     *
     * (The purpose of this method to be less SQL invoke and data flow as
     * at simple getWorkflowConfigData().)
     *
     */
    public Vector getEmbedWorkflowConfigData(ComDataBean value) {
        Vector res = new Vector();
        try {
            res = getEmbedWorkflowConfigDataReal(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Vector getEmbedWorkflowConfigDataReal(ComDataBean value) throws Exception {
        Vector res = new Vector();
        Connection conn = Base.getConnection();
        try {
            String workflowID = "0";
            String parentWorkflowID = "0";
            String sql = "SELECT a.id, a.wtyp FROM workflow as a, aworkflow as b WHERE b.id_portal='" + value.getPortalID() + "' and b.id_user='" + value.getUserID() + "' and a.name='" + value.getWorkflowID() + "' and a.id_aworkflow=b.id";
            Statement stmt = conn.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                workflowID = rst.getString(1);
                parentWorkflowID = rst.getString(2);
            }
            JobPropertyBean tmp;
            JobPropertyBean ttmp;
            sql = "SELECT c.name, c.txt, c.x, c.y, c.id FROM workflow as a, aworkflow as b, ajob as c WHERE b.id_portal='" + value.getPortalID() + "' and b.id_user='" + value.getUserID() + "' and a.name='" + value.getWorkflowID() + "' and a.id_aworkflow=b.id and c.id_aworkflow=b.id ORDER BY c.name";
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                tmp = new JobPropertyBean();
                tmp = getEmbedJobProperty(conn, workflowID, rst.getString(5));
                tmp.setName(rst.getString(1));
                tmp.setTxt(rst.getString(2));
                tmp.setX(rst.getLong(3));
                tmp.setY(rst.getLong(4));
                tmp.setId(rst.getLong(5));
                if (Long.parseLong(parentWorkflowID) > 0) {
                    ttmp = new JobPropertyBean();
                    ttmp = getEmbedJobProperty(conn, parentWorkflowID, rst.getString(5));
                    String key = "";
                    Iterator enm = ttmp.getExe().keySet().iterator();
                    while (enm.hasNext()) {
                        key = "" + enm.next();
                        tmp.addExeLock(key, "" + ttmp.getExe().get(key));
                    }
                }
                if (!tmp.getExe().isEmpty()) {
                    res.add(tmp);
                }
            }
            conn.close();
        } catch (SQLException e) {
            conn.close();
            e.printStackTrace();
            throw e;
        }
        return res;
    }

    /**
     * It returns jobs that contain embedded workflow "iworkflow" references.
     * 
     *
     * It is only listing and returns jobs within embbedding feature
     * 
     * (It used by storage during the zip creation)
     *
     * @return vector in which are JobPropertyBean-s as embedded jobs
     * 
     *
     * (WorkflowXMLBuilder uses)
     *
     * The purpose of this method to be less SQL invoke and data flow as
     * at simple getWorkflowConfigData().
     *
     */
    private JobPropertyBean getEmbedJobProperty(Connection pconn, String workflowID, String ajobID) throws Exception {
        JobPropertyBean res = new JobPropertyBean();
        try {
            String sql = "SELECT name, value FROM job_prop WHERE id_workflow = ? and id_ajob = ? and name='iworkflow'";
            PreparedStatement preps = pconn.prepareStatement(sql);
            preps.setString(1, workflowID);
            preps.setString(2, ajobID);
            ResultSet rst = preps.executeQuery();
            while (rst.next()) {
                res.addExe(rst.getString(1), rst.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return res;
    }

    public ComDataBean getWorkflowInstanceDesc(String pValue) {
        ComDataBean res = new ComDataBean();
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT a.name, a.value FROM workflow_prop as a WHERE wrtid='" + pValue + "'";
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                if ("text".equalsIgnoreCase(rst.getString(1))) res.setTxt(rst.getString(2));
                if ("wfiurl".equalsIgnoreCase(rst.getString(1))) res.setWfiURL(rst.getString(2));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Vector getWorkflowInstanceJobs(ComDataBean value) {
        Vector res = new Vector();
        try {
            long lindex = 0;
            if (value.getSize() != null) {
                if (value.getSize() >= 0) {
                    lindex = value.getSize();
                }
            }
            int getmax = 2500;
            long limitFrom = lindex * getmax;
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT b.name,a.pid,a.status,a.resource FROM job_status as a, ajob as b WHERE a.wrtid='" + value.getWorkflowRuntimeID() + "' and a.id_ajob=b.id LIMIT " + limitFrom + ", " + getmax;
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                JobInstanceBean tmp = new JobInstanceBean();
                tmp.setJobID(rst.getString(1));
                tmp.setPID(rst.getInt(2));
                tmp.setStatus(rst.getInt(3));
                tmp.setResource(rst.getString(4));
                tmp.setTim(0);
                res.add(tmp);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * It returns workflow list with given type from repository. 
     *
     * (the result is a vector in which are RepositoryWorkflowBean-s. 
     * 
     * @param RepositoryWorkflowBean bean - workflowType (pl: appl, proj, real, abst, graf)
     * @return workflowList - workflow list
     */
    public Vector getRepositoryItems(RepositoryWorkflowBean bean) {
        Vector res = new Vector();
        try {
            res = new RepositoryServiceImpl().getRepositoryItems(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Rewrite wf graph
     * @param value new wf data
     * @return the result of saving action
     * @see ComDataBean
     */
    public boolean setNewGraf(ComDataBean value) {
        Base.getI().deleteFromJobIdCache(value.getPortalID(), value.getUserID(), value.getWorkflowID());
        try {
            long s = System.currentTimeMillis();
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            sql = "SELECT a.id from aworkflow as a WHERE a.id_portal='" + value.getPortalID() + "' and a.id_user='" + value.getUserID() + "' and a.name='" + value.getGraf() + "' ";
            ResultSet rst = stmt.executeQuery(sql);
            String grafid = "";
            while (rst.next()) {
                grafid = rst.getString("id");
            }
            sql = "SELECT * FROM workflow as w, aworkflow as a WHERE w.name='" + value.getWorkflowID() + "' and a.id_portal='" + value.getPortalID() + "' and a.id_user='" + value.getUserID() + "' and w.id_aworkflow=a.id ";
            rst = stmt.executeQuery(sql);
            String oldgrafid = "";
            String wfid = "";
            while (rst.next()) {
                oldgrafid = rst.getString("a.id");
                wfid = rst.getString("w.id");
            }
            Hashtable oldwf = new Hashtable();
            sql = "SELECT * FROM ajob as aj WHERE aj.id_aworkflow='" + oldgrafid + "' ";
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                oldwf.put("" + rst.getString("name"), "" + rst.getString("id"));
            }
            sql = "SELECT * FROM ajob as aj WHERE aj.id_aworkflow='" + grafid + "' ";
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String jobname = rst.getString("aj.name");
                String id_ajob = rst.getString("aj.id");
                Enumeration ojobnames = oldwf.keys();
                while (ojobnames.hasMoreElements()) {
                    String ojname = "" + ojobnames.nextElement();
                    if (ojname.equals(jobname)) {
                        String oldid_ajob = "" + oldwf.get(ojname);
                        oldwf.remove(ojname);
                        sql = "UPDATE job_prop SET id_ajob='" + id_ajob + "' WHERE id_workflow='" + wfid + "' and id_ajob='" + oldid_ajob + "' ";
                        Statement stmt2 = conn.createStatement();
                        stmt2.executeUpdate(sql);
                        sql = "UPDATE history SET id_ajob='" + id_ajob + "' WHERE id_workflow='" + wfid + "' and id_ajob='" + oldid_ajob + "' and user='" + value.getUserID() + "'";
                        stmt2 = conn.createStatement();
                        stmt2.executeUpdate(sql);
                        Hashtable oldinputs = new Hashtable();
                        sql = "SELECT id,name,prejob,preoutput FROM ainput WHERE id_ajob='" + oldid_ajob + "'";
                        stmt2 = conn.createStatement();
                        ResultSet rst2 = stmt2.executeQuery(sql);
                        while (rst2.next()) {
                            PortDataBean p = new PortDataBean();
                            p.setId(rst2.getLong("id"));
                            p.setPrejob(rst2.getString("prejob"));
                            p.setPreoutput(rst2.getString("preoutput"));
                            oldinputs.put(rst2.getString("name"), p);
                        }
                        sql = "SELECT * FROM ainput as ai WHERE ai.id_ajob='" + id_ajob + "' ";
                        rst2 = stmt2.executeQuery(sql);
                        while (rst2.next()) {
                            String id_ainput = rst2.getString("ai.id");
                            String name = rst2.getString("ai.name");
                            String prejob = rst2.getString("ai.prejob");
                            String preoutput = rst2.getString("ai.preoutput");
                            PortDataBean op = (PortDataBean) oldinputs.get(name);
                            if (op != null && (("".equals(prejob) && "".equals(preoutput) && "".equals(op.getPrejob()) && "".equals(op.getPreoutput())) || (!"".equals(prejob) && !"".equals(preoutput) && !"".equals(op.getPrejob()) && !"".equals(op.getPreoutput())))) {
                                sql = "UPDATE input_prop SET id_ainput='" + id_ainput + "' WHERE id_workflow='" + wfid + "' and id_ainput='" + op.getId() + "' ";
                                Statement stmt3 = conn.createStatement();
                                stmt3.executeUpdate(sql);
                                stmt3.close();
                                oldinputs.remove(name);
                            } else if (op != null && ("".equals(prejob) && "".equals(preoutput) && !"".equals(op.getPrejob()) && !"".equals(op.getPreoutput()))) {
                                sql = "UPDATE input_prop SET id_ainput='" + id_ainput + "' WHERE id_workflow='" + wfid + "' and id_ainput='" + op.getId() + "' AND (name='intname' or name='dpid')";
                                Statement stmt3 = conn.createStatement();
                                stmt3.executeUpdate(sql);
                                sql = "SELECT * FROM output_prop WHERE id_workflow='" + wfid + "' and name='remote' " + "and id_aoutput=(SELECT id FROM aoutput WHERE seq='" + op.getPreoutput() + "' and id_ajob=(SELECT id FROM ajob WHERE id_aworkflow='" + oldgrafid + "' AND name='" + op.getPrejob() + "') )";
                                ResultSet rst3 = stmt3.executeQuery(sql);
                                String remote = "";
                                String rlabel = "";
                                String rdesc = "";
                                String rinh = "";
                                while (rst3.next()) {
                                    remote = rst3.getString("value");
                                    rlabel = rst3.getString("label");
                                    rdesc = rst3.getString("desc");
                                    rinh = rst3.getString("inh");
                                }
                                rst3.close();
                                if (!"".equals(remote)) {
                                    sql = "INSERT INTO input_prop VALUES(" + wfid + "," + id_ainput + ",'remote','" + remote + "','" + rlabel + "','" + rdesc + "','" + rinh + "')";
                                    stmt3.executeUpdate(sql);
                                }
                                stmt3.close();
                            }
                        }
                        if (oldinputs.size() > 0) {
                            Enumeration oinames = oldinputs.keys();
                            String oiids = "";
                            while (oinames.hasMoreElements()) {
                                PortDataBean op = (PortDataBean) oldinputs.get("" + oinames.nextElement());
                                oiids = oiids.concat("id_ainput='" + op.getId() + "' OR ");
                            }
                            sql = "DELETE FROM input_prop WHERE id_workflow='" + wfid + "' AND (" + oiids + " id_ainput='')";
                            stmt2.executeUpdate(sql);
                        }
                        Hashtable oldoutputs = new Hashtable();
                        sql = "SELECT id,name FROM aoutput WHERE id_ajob='" + oldid_ajob + "'";
                        stmt2 = conn.createStatement();
                        rst2 = stmt2.executeQuery(sql);
                        while (rst2.next()) {
                            oldoutputs.put(rst2.getString("name"), "" + rst2.getString("id"));
                        }
                        sql = "SELECT * FROM aoutput as ao WHERE ao.id_ajob='" + id_ajob + "' ";
                        rst2 = stmt2.executeQuery(sql);
                        while (rst2.next()) {
                            String id_aoutput = rst2.getString("ao.id");
                            String name = rst2.getString("ao.name");
                            if (oldoutputs.get(name) != null) {
                                sql = "UPDATE output_prop SET id_aoutput='" + id_aoutput + "' WHERE id_workflow='" + wfid + "' and id_aoutput='" + oldoutputs.get(name) + "' ";
                                Statement stmt3 = conn.createStatement();
                                stmt3.executeUpdate(sql);
                                stmt3.close();
                                oldoutputs.remove(name);
                            }
                        }
                        if (oldoutputs.size() > 0) {
                            Enumeration oonames = oldoutputs.keys();
                            String oiids = "";
                            while (oonames.hasMoreElements()) {
                                oiids = oiids.concat("id_aoutput='" + oldoutputs.get("" + oonames.nextElement()) + "' OR ");
                            }
                            sql = "DELETE FROM output_prop WHERE id_workflow='" + wfid + "' AND (" + oiids + " id_aoutput='')";
                            stmt2.executeUpdate(sql);
                        }
                        stmt2.close();
                    }
                }
            }
            if (oldwf.size() > 0) {
                Enumeration oonames = oldwf.keys();
                String oiids = "";
                while (oonames.hasMoreElements()) {
                    String oid_ajob = "" + oldwf.get("" + oonames.nextElement());
                    oiids = oiids.concat("id_ajob='" + oid_ajob + "' OR ");
                    sql = "DELETE FROM input_prop WHERE id_workflow='" + wfid + "' AND id_ainput IN (SELECT id FROM ainput WHERE id_ajob='" + oid_ajob + "' )";
                    stmt.executeUpdate(sql);
                    sql = "DELETE FROM output_prop WHERE id_workflow='" + wfid + "' AND id_aoutput IN (SELECT id FROM aoutput WHERE id_ajob='" + oid_ajob + "' )";
                    stmt.executeUpdate(sql);
                }
                sql = "DELETE FROM job_prop WHERE id_workflow='" + wfid + "' AND (" + oiids + " id_ajob='')";
                stmt.executeUpdate(sql);
                sql = "DELETE FROM history WHERE id_workflow='" + wfid + "' AND user='" + value.getUserID() + "' AND (" + oiids + " id_ajob='')";
                stmt.executeUpdate(sql);
            }
            sql = "UPDATE workflow SET id_aworkflow='" + grafid + "' WHERE name='" + value.getWorkflowID() + "'";
            stmt.executeUpdate(sql);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Rewrite wf template
     * @param value New wf data
     * @return Result of saving action
     * @see ComDataBean
     */
    public String setNewTemplate(ComDataBean value) {
        Base.getI().deleteFromJobIdCache(value.getPortalID(), value.getUserID(), value.getWorkflowID());
        String newgraf = "";
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT a.id, a.id_aworkflow from workflow as a WHERE a.name='" + value.getGraf() + "' ";
            ResultSet rst = stmt.executeQuery(sql);
            String templeteid = "";
            String grafid = "";
            while (rst.next()) {
                templeteid = rst.getString("id");
                grafid = rst.getString("id_aworkflow");
            }
            sql = "UPDATE workflow SET wtyp='" + templeteid + "', id_aworkflow='" + grafid + "' WHERE name='" + value.getWorkflowID() + "'";
            stmt.executeUpdate(sql);
            sql = "SELECT a.name from aworkflow as a WHERE a.id='" + grafid + "' ";
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                newgraf = rst.getString("name");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newgraf;
    }

    /**
     * It queries the volatile typed output ports list of a workflow.
     *
     * @param ComDataBean workflow descritor bean
     * @return VolatileBean contains 
     * a workflow descriptor comdatabean and a volatile vector that gives information about job volatile
     * output files.
     * The returned volatile vector contains
     * volatileentrybeans:
     */
    public VolatileBean getVolatileOutputs(ComDataBean value) {
        VolatileBean volatileBean = new VolatileBean(value);
        try {
            Connection conn = Base.getConnection();
            long workflowID = 0;
            long aworkflowID = 0;
            String sql = "SELECT w.id, aw.id " + "FROM aworkflow as aw, workflow as w " + "WHERE w.name=? and w.id_aworkflow=aw.id " + "and aw.id_portal=? and aw.id_user=?";
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setString(1, value.getWorkflowID());
            prepStat.setString(2, value.getPortalID());
            prepStat.setString(3, value.getUserID());
            ResultSet rst = prepStat.executeQuery();
            while (rst.next()) {
                workflowID = rst.getLong(1);
                aworkflowID = rst.getLong(2);
            }
            sql = "SELECT aj.name, ao.name, op.value " + "FROM ajob as aj, aoutput as ao, output_prop as op " + "WHERE aj.id_aworkflow=? and op.id_workflow=? " + "and aj.id=ao.id_ajob and op.id_aoutput=ao.id " + "and op.name='intname' " + "and op.id_aoutput IN (SELECT op.id_aoutput " + "FROM ajob as aj, aoutput as ao, output_prop as op " + "WHERE aj.id_aworkflow=? " + "and aj.id=ao.id_ajob and op.id_aoutput=ao.id " + "and op.name='type' and op.value='volatile')";
            prepStat = conn.prepareStatement(sql);
            prepStat.setLong(1, aworkflowID);
            prepStat.setLong(2, workflowID);
            prepStat.setLong(3, aworkflowID);
            rst = prepStat.executeQuery();
            while (rst.next()) {
                String jobName = rst.getString(1);
                String outputName1 = rst.getString(2);
                String outputName2 = rst.getString(3);
                volatileBean.addEntry(new VolatileEntryBean(jobName, outputName1, outputName2));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return volatileBean;
    }
}
