package hu.sztaki.lpds.wfs.net.wsaxis13;

import hu.sztaki.lpds.information.local.PropertyLoader;
import hu.sztaki.lpds.wfs.com.ResourceCollectionBean;
import java.util.*;
import java.sql.*;
import hu.sztaki.lpds.wfs.com.JobStatusBean;
import hu.sztaki.lpds.wfs.com.ComDataBean;
import hu.sztaki.lpds.wfs.inf.WfsWfiService;
import hu.sztaki.lpds.wfs.service.angie.Base;
import hu.sztaki.lpds.wfs.service.angie.plugins.wfi.inf.WorkflowDescriptionGenerator;

/**
 * @author krisztian
 */
public class WfsWfiServiceImpl implements WfsWfiService {

    public WfsWfiServiceImpl() {
    }

    /**
     * It returns the descriptor of a workflow
     * @param pData the workflow data
     * @return descriptor XML
     */
    @Override
    public String getWfiXML(ComDataBean pData) {
        String classname = PropertyLoader.getInstance().getProperty("guse.wfs.wfiplugin." + pData.getWorkflowtype());
        WorkflowDescriptionGenerator tmp;
        try {
            tmp = (WorkflowDescriptionGenerator) Class.forName(classname).newInstance();
            return tmp.getWFIWorkflowDescription(pData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    @Override
    public String getWfiRescueXML(ComDataBean pData, String index) {
        String classname = PropertyLoader.getInstance().getProperty("guse.wfs.wfiplugin." + pData.getWorkflowtype());
        WorkflowDescriptionGenerator tmp;
        try {
            tmp = (WorkflowDescriptionGenerator) Class.forName(classname).newInstance();
            String rescueXml = "";
            synchronized (this) {
                rescueXml = tmp.getWFIWorkflowRescueDescription(pData, index);
            }
            return rescueXml;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    @Override
    public void setStatus(JobStatusBean pData) throws Exception {
        throw new Exception("wfs setStatus() nincs hasznalatban megis meghivodott !!!");
    }

    @Override
    public String getResourceType(JobStatusBean pData) {
        String gridID = "";
        try {
            String sql;
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            sql = "SELECT d.value FROM ajob as a, workflow as b, aworkflow as c, job_prop as d WHERE c.id_portal='" + pData.getPortalID() + "' and c.id_user='" + pData.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pData.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pData.getJobID() + "' and d.id_workflow=b.id and d.id_ajob=a.id and (d.name='grid' or d.name='servicetype')";
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                gridID = rst.getString(1);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gridID;
    }

    public HashMap getCollectionResourceType(ResourceCollectionBean pData) {
        String tmp = " (";
        HashMap res = new HashMap();
        Vector v = new Vector();
        StringTokenizer s = new StringTokenizer(pData.getJobsid(), ",");
        while (s.hasMoreTokens()) {
            String ss = s.nextToken();
            tmp = tmp.concat(" a.name='" + ss + "' or");
            v.add(ss);
        }
        tmp = tmp.substring(0, tmp.length() - 2) + ")";
        try {
            String sql;
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            sql = "SELECT a.name,d.value FROM ajob as a, workflow as b, aworkflow as c, job_prop as d WHERE c.id_portal='" + pData.getPortalID() + "' and c.id_user='" + pData.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pData.getWorkflowID() + "' and a.id_aworkflow=c.id and " + tmp + " and d.id_workflow=b.id and d.id_ajob=a.id and (d.name='grid' or d.name='servicetype' or d.name='mbt') ORDER BY d.value,a.name";
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) res.put(rst.getString(1), rst.getString(2));
            sql = "SELECT b.wtyp FROM workflow as b, aworkflow as c WHERE c.id_portal='" + pData.getPortalID() + "' and c.id_user='" + pData.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pData.getWorkflowID() + "'";
            rst = stmt.executeQuery(sql);
            rst.next();
            if (rst.getInt(1) > 0) {
                sql = "SELECT a.name,d.value,d.label FROM ajob as a, workflow as b, aworkflow as c, job_prop as d WHERE c.id_portal='" + pData.getPortalID() + "' and c.id_user='" + pData.getUserID() + "' and c.id=b.id_aworkflow and b.id=" + rst.getInt(1) + " and a.id_aworkflow=c.id and " + tmp + " and d.id_workflow=b.id and d.id_ajob=a.id and (d.name='grid' or d.name='servicetype')  ORDER BY d.value,a.name";
                rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    if (!rst.getString(3).equals("")) {
                        if (res.get(rst.getString(1)) == null) res.put(rst.getString(1), rst.getString(2));
                    } else res.put(rst.getString(1), rst.getString(2));
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public String setCollectionStatus(Vector pData) {
        try {
            synchronized (this) {
                Base.getI().persistStatusItems(pData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "";
    }

    public String getSubmitData(ComDataBean pValue) {
        try {
            return Base.getI().getJobDescriptionFromCache(pValue.getPortalID(), pValue.getUserID(), pValue.getWorkflowID(), pValue.getWorkflowRuntimeID(), pValue.getJobID());
        } catch (NullPointerException e) {
        }
        String res = "";
        try {
            res = getSubmitDataPack(pValue);
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT b.wtyp FROM workflow as b, aworkflow as c WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "'";
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) if (rst.getLong(1) > 0) {
                sql = "SELECT b.name FROM workflow as b WHERE b.id=" + rst.getLong(1);
                Connection conn0 = Base.getConnection();
                Statement stmt0 = conn0.createStatement();
                ResultSet rst0 = stmt0.executeQuery(sql);
                while (rst0.next()) {
                    pValue.setWorkflowID(rst0.getString(1));
                    res = "<init>" + getSubmitInitData(pValue) + "</init>\n<template>" + getSubmitTemplateDefault(pValue) + "</template>\n<real>" + res + "</real>\n<close>" + getSubmitTemplateClose(pValue) + "</close>\n";
                }
                conn0.close();
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Base.getI().addJobDescriptionToCache(pValue.getPortalID(), pValue.getUserID(), pValue.getWorkflowID(), pValue.getWorkflowRuntimeID(), pValue.getJobID(), "<data>\n" + res + "\n</data>");
        return "<data>\n" + res + "\n</data>";
    }

    public String getSubmitDataPack(ComDataBean pValue) throws SQLException {
        StringBuffer res = new StringBuffer("");
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT d.name,d.value FROM ajob as a, workflow as b, aworkflow as c, job_prop as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id and d.id_workflow=b.id ORDER BY a.name";
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                if (!rst.getString(1).startsWith("tmp*")) res.append("\t<job-property " + rst.getString(1) + "=\"" + repl(rst.getString(2)) + "\" />\n");
            }
            sql = "SELECT d.name,e.name,e.value FROM ajob as a, workflow as b, aworkflow as c, aoutput as d, output_prop as e WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id=e.id_aoutput and e.id_workflow=b.id and d.id_ajob=a.id ORDER BY a.name";
            rst = stmt.executeQuery(sql);
            String outputs = "";
            Hashtable<String, Boolean> ctmp = new Hashtable<String, Boolean>();
            while (rst.next()) {
                ctmp.put(rst.getString(1), new Boolean(true));
                res.append("\t<output-property name=\"" + rst.getString(1) + "\" " + rst.getString(2) + "=\"" + rst.getString(3) + "\" />\n");
            }
            sql = "SELECT d.name FROM ajob as a, workflow as b, aworkflow as c, aoutput as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id ORDER BY a.name";
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                if (ctmp.get(rst.getString(1)) == null) {
                    res.append("\t<output-property name=\"" + rst.getString(1) + "\" intname=\"" + rst.getString(1) + "\" />\n");
                    res.append("\t<output-property name=\"" + rst.getString(1) + "\" maincount=\"1\" />\n");
                }
            }
            sql = "SELECT d.name,e.name,e.value,d.prejob,d.preoutput FROM ajob as a, workflow as b, aworkflow as c, ainput as d, input_prop as e WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id=e.id_ainput and e.id_workflow=b.id  and d.id_ajob=a.id ORDER BY id_ainput,e.name";
            rst = stmt.executeQuery(sql);
            String intname = "";
            boolean b = true;
            String sql0 = "SELECT d.prejob,d.preoutput,d.name,d.seq FROM ajob as a, workflow as b, aworkflow as c, ainput as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id";
            Connection conn0 = Base.getConnection();
            Statement stmt0 = conn0.createStatement();
            ResultSet rst0 = stmt0.executeQuery(sql0);
            while (rst0.next()) {
                String pjob = rst0.getString(1);
                String pport = rst0.getString(2);
                String ssql = "SELECT d.name,e.name,e.value FROM ajob as a, workflow as b, aworkflow as c, aoutput as d, output_prop as e WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + rst0.getString(1) + "' and a.id=d.id_ajob and d.seq='" + rst0.getString(2) + "' and d.id=e.id_aoutput and e.id_workflow=b.id and d.id_ajob=a.id ORDER BY a.name";
                PreparedStatement tps = conn0.prepareStatement("SELECT d.name,e.name,e.value FROM ajob as a, workflow as b, aworkflow as c, aoutput as d, output_prop as e WHERE c.id_portal=? and c.id_user=? and c.id=b.id_aworkflow and b.name=? and a.id_aworkflow=c.id and a.name=? and a.id=d.id_ajob and d.seq=? and d.id=e.id_aoutput and e.id_workflow=b.id and d.id_ajob=a.id ORDER BY a.name");
                tps.setString(1, pValue.getPortalID());
                tps.setString(2, pValue.getUserID());
                tps.setString(3, pValue.getWorkflowID());
                tps.setString(4, rst0.getString(1));
                tps.setString(5, rst0.getString(2));
                ResultSet rst1 = tps.executeQuery();
                int itt = 0;
                while (rst1.next()) {
                    itt++;
                    if (!rst1.getString(2).equals("intname")) res.append("\t<input-property name=\"" + rst0.getString(3) + "\" " + rst1.getString(2) + "=\"\">" + repl(rst1.getString(3)) + "</input-property>\n");
                }
                res.append("\t<input-property name=\"" + rst0.getString(3) + "\" seq=\"\">" + rst0.getString(4) + "</input-property>\n");
                if (itt == 0) res.append("\t<input-property name=\"" + rst0.getString(3) + "\" fake=\"fake\"></input-property>\n");
                tps.close();
            }
            conn0.close();
            while (rst.next()) {
                res.append("\t<input-property name=\"" + rst.getString(1) + "\" " + rst.getString(2) + "=\"\">" + repl(rst.getString(3)) + "</input-property>\n");
            }
            sql = "SELECT d.name,d.value FROM ajob as a, workflow as b, aworkflow as c, job_desc as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id and d.id_workflow=b.id ORDER BY a.name";
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                res.append("\t<job-description  " + rst.getString(1) + "=\"" + repl(rst.getString(2)) + "\" />\n");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "<job>\n" + res.toString() + "</job>\n";
    }

    public String getSubmitTemplateDefault(ComDataBean pValue) throws SQLException {
        StringBuffer res = new StringBuffer("");
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT d.name,d.value FROM ajob as a, workflow as b, aworkflow as c, job_prop as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id and d.id_workflow=b.id and d.label!='' ORDER BY a.name";
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                if (!rst.getString(1).startsWith("tmp*")) res.append("\t<job-property " + rst.getString(1) + "=\"" + repl(rst.getString(2)) + "\" />\n");
            }
            sql = "SELECT d.name,e.name,e.value FROM ajob as a, workflow as b, aworkflow as c, aoutput as d, output_prop as e WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id=e.id_aoutput and e.id_workflow=b.id and d.id_ajob=a.id and e.label!='' ORDER BY a.name";
            rst = stmt.executeQuery(sql);
            String outputs = "";
            int iii = 0;
            while (rst.next()) {
                iii++;
                res.append("\t<output-property name=\"" + rst.getString(1) + "\" " + rst.getString(2) + "=\"" + rst.getString(3) + "\" />\n");
            }
            if (iii == 0) {
                sql = "SELECT d.name FROM ajob as a, workflow as b, aworkflow as c, aoutput as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id ORDER BY a.name";
                rst = stmt.executeQuery(sql);
                while (rst.next()) {
                    res.append("\t<output-property name=\"" + rst.getString(1) + "\" intname=\"" + rst.getString(1) + "\" />\n");
                    res.append("\t<output-property name=\"" + rst.getString(1) + "\" maincount=\"1\" />\n");
                }
            }
            sql = "SELECT d.name,e.name,e.value,d.prejob,d.preoutput FROM ajob as a, workflow as b, aworkflow as c, ainput as d, input_prop as e WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id=e.id_ainput and e.id_workflow=b.id  and d.id_ajob=a.id and e.label!='' ORDER BY id_ainput,e.name";
            rst = stmt.executeQuery(sql);
            String intname = "";
            boolean b = true;
            String sql0 = "SELECT d.prejob,d.preoutput,d.name FROM ajob as a, workflow as b, aworkflow as c, ainput as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id";
            Connection conn0 = Base.getConnection();
            Statement stmt0 = conn0.createStatement();
            ResultSet rst0 = stmt0.executeQuery(sql0);
            while (rst0.next()) {
                String pjob = rst0.getString(1);
                String pport = rst0.getString(2);
                String ssql = "SELECT d.name,e.name,e.value FROM ajob as a, workflow as b, aworkflow as c, aoutput as d, output_prop as e WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + rst0.getString(1) + "' and a.id=d.id_ajob and d.seq='" + rst0.getString(2) + "' and d.id=e.id_aoutput and e.id_workflow=b.id and d.id_ajob=a.id and e.label!='' ORDER BY a.name";
                PreparedStatement tps = conn0.prepareStatement("SELECT d.name,e.name,e.value FROM ajob as a, workflow as b, aworkflow as c, aoutput as d, output_prop as e WHERE c.id_portal=? and c.id_user=? and c.id=b.id_aworkflow and b.name=? and a.id_aworkflow=c.id and a.name=? and a.id=d.id_ajob and d.seq=? and d.id=e.id_aoutput and e.id_workflow=b.id and d.id_ajob=a.id ORDER BY a.name");
                tps.setString(1, pValue.getPortalID());
                tps.setString(2, pValue.getUserID());
                tps.setString(3, pValue.getWorkflowID());
                tps.setString(4, rst0.getString(1));
                tps.setString(5, rst0.getString(2));
                ResultSet rst1 = tps.executeQuery();
                int itt = 0;
                while (rst1.next()) {
                    itt++;
                    if (!rst1.getString(2).equals("intname")) res.append("\t<input-property name=\"" + rst0.getString(3) + "\" " + rst1.getString(2) + "=\"\">" + repl(rst1.getString(3)) + "</input-property>\n");
                }
                if (itt == 0) res.append("\t<input-property name=\"" + rst0.getString(3) + "\" fake=\"fake\"> </input-property>\n");
                tps.close();
            }
            conn0.close();
            while (rst.next()) {
                res.append("\t<input-property name=\"" + rst.getString(1) + "\" " + rst.getString(2) + "=\"\">" + repl(rst.getString(3)) + "</input-property>\n");
            }
            sql = "SELECT d.name,d.value FROM ajob as a, workflow as b, aworkflow as c, job_desc as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id and d.id_workflow=b.id ORDER BY a.name";
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                res.append("\t<job-description  " + rst.getString(1) + "=\"" + repl(rst.getString(2)) + "\" />\n");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "<job>\n" + res.toString() + "</job>\n";
    }

    public String getSubmitInitData(ComDataBean pValue) throws SQLException {
        StringBuffer res = new StringBuffer("");
        try {
            String sql = "SELECT d.name FROM ajob as a, workflow as b, aworkflow as c, aoutput as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id ORDER BY a.name";
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                res.append("\t<output-property name=\"" + rst.getString(1) + "\" intname=\"" + rst.getString(1) + "\" />\n");
                res.append("\t<output-property name=\"" + rst.getString(1) + "\" maincount=\"1\" />\n");
            }
            conn.close();
        } catch (Exception e) {
        }
        return res.toString();
    }

    public String getSubmitTemplateClose(ComDataBean pValue) throws SQLException {
        StringBuffer res = new StringBuffer("");
        try {
            Connection conn = Base.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT d.name,d.value FROM ajob as a, workflow as b, aworkflow as c, job_prop as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id and d.id_workflow=b.id and d.label='' ORDER BY a.name";
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                if (!rst.getString(1).startsWith("tmp*")) res.append("\t<job-property " + rst.getString(1) + "=\"" + repl(rst.getString(2)) + "\" />\n");
            }
            sql = "SELECT d.name,e.name,e.value FROM ajob as a, workflow as b, aworkflow as c, aoutput as d, output_prop as e WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id=e.id_aoutput and e.id_workflow=b.id and d.id_ajob=a.id and e.label='' ORDER BY a.name";
            rst = stmt.executeQuery(sql);
            String outputs = "";
            int iii = 0;
            while (rst.next()) {
                iii++;
                res.append("\t<output-property name=\"" + rst.getString(1) + "\" " + rst.getString(2) + "=\"" + rst.getString(3) + "\" />\n");
            }
            sql = "SELECT d.name,e.name,e.value,d.prejob,d.preoutput FROM ajob as a, workflow as b, aworkflow as c, ainput as d, input_prop as e WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id=e.id_ainput and e.id_workflow=b.id  and d.id_ajob=a.id and e.label='' ORDER BY id_ainput,e.name";
            rst = stmt.executeQuery(sql);
            String intname = "";
            boolean b = true;
            String sql0 = "SELECT d.prejob,d.preoutput,d.name FROM ajob as a, workflow as b, aworkflow as c, ainput as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id";
            Connection conn0 = Base.getConnection();
            Statement stmt0 = conn0.createStatement();
            ResultSet rst0 = stmt0.executeQuery(sql0);
            while (rst0.next()) {
                String pjob = rst0.getString(1);
                String pport = rst0.getString(2);
                String ssql = "SELECT d.name,e.name,e.value FROM ajob as a, workflow as b, aworkflow as c, aoutput as d, output_prop as e WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.id_aworkflow=c.id and a.name='" + rst0.getString(1) + "' and a.id=d.id_ajob and d.seq='" + rst0.getString(2) + "' and d.id=e.id_aoutput and e.id_workflow=b.id and d.id_ajob=a.id and e.label='' ORDER BY a.name";
                PreparedStatement tps = conn0.prepareStatement("SELECT d.name,e.name,e.value FROM ajob as a, workflow as b, aworkflow as c, aoutput as d, output_prop as e WHERE c.id_portal=? and c.id_user=? and c.id=b.id_aworkflow and b.name=? and a.id_aworkflow=c.id and a.name=? and a.id=d.id_ajob and d.seq=? and d.id=e.id_aoutput and e.id_workflow=b.id and d.id_ajob=a.id ORDER BY a.name");
                tps.setString(1, pValue.getPortalID());
                tps.setString(2, pValue.getUserID());
                tps.setString(3, pValue.getWorkflowID());
                tps.setString(4, rst0.getString(1));
                tps.setString(5, rst0.getString(2));
                ResultSet rst1 = tps.executeQuery();
                int itt = 0;
                while (rst1.next()) {
                    itt++;
                    if (!rst1.getString(2).equals("intname")) res.append("\t<input-property name=\"" + rst0.getString(3) + "\" " + rst1.getString(2) + "=\"\">" + repl(rst1.getString(3)) + "</input-property>\n");
                }
                if (itt == 0) res.append("\t<input-property name=\"" + rst0.getString(3) + "\" fake=\"fake\"></input-property>\n");
                tps.close();
            }
            conn0.close();
            while (rst.next()) {
                res.append("\t<input-property name=\"" + rst.getString(1) + "\" " + rst.getString(2) + "=\"\">" + repl(rst.getString(3)) + "</input-property>\n");
            }
            sql = "SELECT d.name,d.value FROM ajob as a, workflow as b, aworkflow as c, job_desc as d WHERE c.id_portal='" + pValue.getPortalID() + "' and c.id_user='" + pValue.getUserID() + "' and c.id=b.id_aworkflow and b.name='" + pValue.getWorkflowID() + "' and a.name='" + pValue.getJobID() + "' and a.id=d.id_ajob and d.id_ajob=a.id and d.id_workflow=b.id ORDER BY a.name";
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                res.append("\t<job-description  " + rst.getString(1) + "=\"" + repl(rst.getString(2)) + "\" />\n");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "<job>\n" + res.toString() + "</job>\n";
    }

    private String repl(String s) {
        try {
            return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
        } catch (Exception e) {
            return "";
        }
    }
}
