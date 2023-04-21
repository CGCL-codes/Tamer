package org.powerfolder.workflow.lifecycle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.QueueSender;
import javax.jms.QueueReceiver;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.powerfolder.PFException;
import org.powerfolder.PFRuntimeException;
import org.powerfolder.ValueAndClass;
import org.powerfolder.ValueAndClassFactory;
import org.powerfolder.apps.WorkflowApplicationSet;
import org.powerfolder.apps.WorkflowScript;
import org.powerfolder.config.ConfigManager;
import org.powerfolder.config.ConfigManagerFactory;
import org.powerfolder.user.UserHolder;
import org.powerfolder.utils.misc.MiscHelper;
import org.powerfolder.utils.xml.XMLHelper;
import org.powerfolder.version.VersionHelper;
import org.powerfolder.workflow.lifecycle.WorkflowLifecycle;
import org.powerfolder.workflow.model.Workflow;
import org.powerfolder.workflow.model.WorkflowFactory;
import org.powerfolder.workflow.model.WorkflowPrecursor;
import org.powerfolder.workflow.model.attributes.AttributeSet;
import org.powerfolder.workflow.model.attributes.WaitHelper;
import org.powerfolder.workflow.model.history.History;
import org.powerfolder.workflow.model.history.Record;
import org.powerfolder.workflow.model.history.Trace;
import org.powerfolder.workflow.model.script.ReturnValueAndClassContext;
import org.powerfolder.workflow.model.script.RootScriptTagHolder;
import org.powerfolder.workflow.model.script.ScriptTag;
import org.powerfolder.workflow.model.script.ScriptTagCharacteristic;
import org.powerfolder.workflow.model.script.ScriptTagHelper;
import org.powerfolder.workflow.model.script.v1.core.WorkflowTemplateScriptTag;
import org.powerfolder.workflow.model.trigger.Trigger;
import org.powerfolder.workflow.query.BaseDBQueryVendor;
import org.powerfolder.workflow.query.ColumnContainer;
import org.powerfolder.workflow.query.QueryContainer;
import org.powerfolder.workflow.query.ResultSetContainer;
import org.powerfolder.workflow.query.StatementContainer;
import org.powerfolder.workflow.query.SysDecIsGreaterThanOrEqualQC;

public class GenericDB2WorkflowLifecycleBean implements WorkflowLifecycle, SessionBean {

    private SessionContext sessionContext = null;

    public GenericDB2WorkflowLifecycleBean() {
    }

    public void close() {
    }

    public ResultSetContainer queryWorkflows(StatementContainer inStatement) {
        try {
            ResultSetContainer outValue = null;
            Connection con = getDatabaseConnection();
            DB2QueryVendorProprietarySyntax oqvps = new DB2QueryVendorProprietarySyntax();
            BaseDBQueryVendor bdbqv = BaseDBQueryVendor.newBaseDBQueryVendor(oqvps, true);
            outValue = bdbqv.executeStatement(con, inStatement);
            con.close();
            return outValue;
        } catch (SQLException sqle) {
            throw new PFRuntimeException(sqle);
        }
    }

    public void startWorkflow(WorkflowPrecursor inWp) {
        try {
            Connection con = getDatabaseConnection();
            AttributeSet as = inWp.getAttributeSet();
            Trigger t = inWp.getTrigger();
            RootScriptTagHolder rth = inWp.getRootScriptTagHolder();
            History h = inWp.getHistory();
            BigDecimal pfId = getNextIndex(con);
            as.addPublicSysAttr(AttributeSet.ID, ValueAndClassFactory.newValueAndClass(pfId, BigDecimal.class));
            insertWorkflowInstance(con, pfId, WorkflowFactory.newWorkflow(inWp));
            storeWorkflowAttributes(con, pfId, as, true);
            con.close();
            QueueConnectionFactory queueFactory = getWorkflowQueueConnectionFactory();
            QueueConnection queueConnection = queueFactory.createQueueConnection();
            Queue queue = getWorkflowQueue();
            QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueSender sender = queueSession.createSender(queue);
            queueConnection.start();
            ObjectMessage message = queueSession.createObjectMessage();
            message.setObject(pfId);
            sender.send(message);
            queueConnection.close();
        } catch (SQLException sqle) {
            throw new PFRuntimeException(sqle);
        } catch (JMSException jmse) {
            throw new PFRuntimeException(jmse);
        }
    }

    public void updateWorkflow(Workflow inWorkflow) {
        try {
            AttributeSet as = inWorkflow.getAttributeSet();
            ValueAndClass idVac = as.getPublicSysAttr(AttributeSet.ID);
            ValueAndClass statusVac = as.getPublicSysAttr(AttributeSet.STATUS);
            BigDecimal id = (BigDecimal) idVac.getValue();
            String status = (String) statusVac.getValue();
            String workflowText = VersionHelper.createXMLRepresentationOfWorkflow(inWorkflow);
            Connection con = getDatabaseConnection();
            PreparedStatement ps = con.prepareStatement("update pf_workflows set wf_instance = ? where id = ?");
            ps.setCharacterStream(1, new StringReader(workflowText), workflowText.length());
            ps.setBigDecimal(2, id);
            ps.execute();
            ps.close();
            storeWorkflowAttributes(con, id, as, false);
            con.close();
            if (status.equals(Trace.STATUS_ACTIVE)) {
                QueueConnectionFactory queueFactory = getWorkflowQueueConnectionFactory();
                QueueConnection queueConnection = queueFactory.createQueueConnection();
                Queue queue = getWorkflowQueue();
                QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                QueueSender sender = queueSession.createSender(queue);
                queueConnection.start();
                ObjectMessage message = queueSession.createObjectMessage();
                message.setObject(id);
                sender.send(message);
                queueConnection.close();
            }
        } catch (SQLException sqle) {
            throw new PFRuntimeException(sqle);
        } catch (JMSException jmse) {
            throw new PFRuntimeException(jmse);
        }
    }

    public WorkflowScript[] getDeployedScripts() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            WorkflowScript outValue[] = null;
            ConfigManager cm = ConfigManagerFactory.getConfigManager();
            File pfDir = GenericFileWorkflowApplicationSet.getPowerFolderDirectory(cm);
            File scriptsDir = new File(pfDir, "scripts");
            con = getDatabaseConnection();
            ps = con.prepareStatement("select script_name from pf_deployed_scripts order by id");
            rs = ps.executeQuery();
            ArrayList scriptList = new ArrayList();
            while (rs.next()) {
                String nextScript = rs.getString(1);
                File nextScriptFile = new File(scriptsDir, nextScript + ".xml");
                if (nextScriptFile.exists()) {
                    scriptList.add(nextScriptFile);
                }
            }
            outValue = new WorkflowScript[scriptList.size()];
            for (int i = 0; i < scriptList.size(); i++) {
                File nextFile = (File) scriptList.get(i);
                String nextName = MiscHelper.removeFileExtension(nextFile.getName());
                outValue[i] = new GenericFileWorkflowScript(nextFile, nextName);
            }
            cm.close();
            return outValue;
        } catch (SQLException sqle) {
            throw new PFRuntimeException(sqle);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new PFRuntimeException(sqle);
            }
        }
    }

    public void setDeployedScripts(WorkflowScript inWs[]) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            ConfigManager cm = ConfigManagerFactory.getConfigManager();
            File pfDir = GenericFileWorkflowApplicationSet.getPowerFolderDirectory(cm);
            File scriptsDir = new File(pfDir, "scripts");
            if (!scriptsDir.exists()) {
                scriptsDir.mkdirs();
            }
            File deployedScripts[] = scriptsDir.listFiles();
            for (int i = 0; i < deployedScripts.length; i++) {
                if (deployedScripts[i].isFile() && deployedScripts[i].getName().endsWith(".xml")) {
                    deployedScripts[i].delete();
                }
            }
            con = getDatabaseConnection();
            ps = con.prepareStatement("delete from pf_deployed_scripts");
            ps.execute();
            ps.close();
            for (int i = 0; i < inWs.length; i++) {
                WorkflowScript nextScript = inWs[i];
                File nextScriptFile = new File(scriptsDir, nextScript.getName() + ".xml");
                MiscHelper.writeTextFile(nextScriptFile, nextScript.getContent());
                BigDecimal id = getNextIndex(con);
                ps = con.prepareStatement("insert into pf_deployed_scripts " + "(id, script_name)  values (?,?)");
                ps.setBigDecimal(1, id);
                ps.setString(2, nextScript.getName());
                ps.execute();
            }
            cm.close();
        } catch (SQLException sqle) {
            throw new PFRuntimeException(sqle);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new PFRuntimeException(sqle);
            }
        }
    }

    public WorkflowApplicationSet getWorkflowApplicationSet(UserHolder inUh) {
        GenericFileWorkflowApplicationSet outValue = new GenericFileWorkflowApplicationSet(getApplicationsDirectory(inUh));
        File appList[] = getApplicationsDirectory(inUh).listFiles();
        for (int i = 0; i < appList.length; i++) {
            if (appList[i].isDirectory()) {
                GenericFileWorkflowApplication nextWa = new GenericFileWorkflowApplication(appList[i]);
                outValue.registerApplication(nextWa);
            }
        }
        outValue.load();
        return outValue;
    }

    public void setWorkflowApplicationSet(WorkflowApplicationSet inWas, UserHolder inUh) {
        File appList[] = getApplicationsDirectory(inUh).listFiles();
        for (int i = 0; i < appList.length; i++) {
            MiscHelper.deleteFileOrDirectory(appList[i]);
        }
        ((GenericFileWorkflowApplicationSet) inWas).store();
    }

    protected static final File getApplicationsDirectory(UserHolder inUh) {
        File outValue = null;
        ConfigManager cm = ConfigManagerFactory.getConfigManager();
        File pfDir = GenericFileWorkflowApplicationSet.getPowerFolderDirectory(cm);
        File usersDir = new File(pfDir, "users");
        if (!usersDir.exists()) {
            usersDir.mkdirs();
        }
        int id = getFileIdOfUser(inUh);
        File userDir = new File(usersDir, id + "");
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
        outValue = new File(userDir, "applications");
        if (!outValue.exists()) {
            outValue.mkdirs();
        }
        cm.close();
        return outValue;
    }

    private static final int getFileIdOfUser(UserHolder inUh) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps2 = null;
        try {
            int outValue = 0;
            String name = inUh.getName();
            String securityClass = inUh.getSecurityClass();
            con = getDatabaseConnection();
            ps = con.prepareStatement("select id from pf_users " + "where user_name = ? and security_class = ?");
            ps.setString(1, name);
            ps.setString(2, securityClass);
            rs = ps.executeQuery();
            if (rs.next()) {
                outValue = rs.getInt(1);
            } else {
                outValue = getNextIndex(con).intValue();
                ps2 = con.prepareStatement("insert into pf_users " + "(id, user_name, security_class) values (?,?,?)");
                ps2.setInt(1, outValue);
                ps2.setString(2, name);
                ps2.setString(3, securityClass);
                ps2.execute();
            }
            return outValue;
        } catch (SQLException sqle) {
            throw new PFRuntimeException(sqle);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (ps2 != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new PFRuntimeException(sqle);
            }
        }
    }

    public Workflow retrieveWorkflow(Serializable inHandle) {
        try {
            Workflow outValue = null;
            BigDecimal id = null;
            if (inHandle instanceof BigDecimal) {
                id = (BigDecimal) inHandle;
            } else {
                throw new PFRuntimeException("Handle is not a BigDecimal.");
            }
            Connection con = getDatabaseConnection();
            PreparedStatement ps = con.prepareStatement("select wf_instance from pf_workflows where id = ?");
            ps.setBigDecimal(1, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                Reader r = rs.getCharacterStream(1);
                StringBuffer sb = new StringBuffer();
                int nextChar = -1;
                while ((nextChar = r.read()) != -1) {
                    sb.append(((char) nextChar));
                }
                outValue = VersionHelper.createWorkflowFromXMLRepresentation(sb.toString());
            } else {
                throw new PFRuntimeException("No workflow-instance found with id '" + id + "'.");
            }
            rs.close();
            ps.close();
            con.close();
            return outValue;
        } catch (SQLException sqle) {
            throw new PFRuntimeException(sqle);
        } catch (IOException ioe) {
            throw new PFRuntimeException(ioe);
        }
    }

    private static final Connection getDatabaseConnection() {
        try {
            Connection outValue = null;
            Context context = new InitialContext();
            Object dsObject = context.lookup("java:comp/env/jdbc/WorkflowLifecycle/DBStore");
            DataSource ds = (DataSource) PortableRemoteObject.narrow(dsObject, DataSource.class);
            outValue = ds.getConnection();
            return outValue;
        } catch (SQLException sqle) {
            throw new PFRuntimeException(sqle);
        } catch (NamingException ne) {
            throw new PFRuntimeException(ne);
        }
    }

    private static final QueueConnectionFactory getWorkflowQueueConnectionFactory() {
        try {
            QueueConnectionFactory outValue = null;
            Context context = new InitialContext();
            Object object = context.lookup("java:comp/env/jms/powerfolder/QueueConnectionFactory");
            outValue = (QueueConnectionFactory) PortableRemoteObject.narrow(object, QueueConnectionFactory.class);
            return outValue;
        } catch (NamingException ne) {
            throw new PFRuntimeException(ne);
        }
    }

    private static final Queue getWorkflowQueue() {
        try {
            Queue outValue = null;
            Context context = new InitialContext();
            Object object = context.lookup("java:comp/env/jms/powerfolder/WorkflowQueue");
            outValue = (Queue) PortableRemoteObject.narrow(object, Queue.class);
            return outValue;
        } catch (NamingException ne) {
            throw new PFRuntimeException(ne);
        }
    }

    private static final Queue getTriggerQueue() {
        try {
            Queue outValue = null;
            Context context = new InitialContext();
            Object object = context.lookup("java:comp/env/jms/powerfolder/TriggerQueue");
            outValue = (Queue) PortableRemoteObject.narrow(object, Queue.class);
            return outValue;
        } catch (NamingException ne) {
            throw new PFRuntimeException(ne);
        }
    }

    private static final BigDecimal getNextIndex(Connection inCon) {
        try {
            BigDecimal outValue = null;
            PreparedStatement cs = inCon.prepareStatement("SELECT NEXTVAL FOR pf_sequence FROM sysibm.sysdummy1");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                outValue = rs.getBigDecimal(1);
            }
            rs.close();
            cs.close();
            return outValue;
        } catch (SQLException sqle) {
            throw new PFRuntimeException(sqle);
        }
    }

    private static final void insertWorkflowInstance(Connection inCon, BigDecimal inPfId, Workflow inWorkflow) {
        try {
            Document workflowDocument = XMLHelper.loadDocument(VersionHelper.createXMLRepresentationOfWorkflow(inWorkflow));
            StringWriter sw = new StringWriter();
            XMLHelper.writeDocument(workflowDocument, sw);
            String workflowXml = sw.toString();
            StringReader sr = new StringReader(workflowXml);
            PreparedStatement ps = inCon.prepareStatement("insert into pf_workflows (id, wf_instance) values (?,?)");
            ps.setBigDecimal(1, inPfId);
            ps.setCharacterStream(2, sr, workflowXml.length());
            ps.execute();
            ps.close();
        } catch (SQLException sqle) {
            throw new PFRuntimeException(sqle);
        } catch (ParserConfigurationException pce) {
            throw new PFRuntimeException(pce);
        } catch (SAXException saxe) {
            throw new PFRuntimeException(saxe);
        } catch (IOException ioe) {
            throw new PFRuntimeException(ioe);
        }
    }

    private static final void storeWorkflowAttributes(Connection inCon, BigDecimal inPfId, AttributeSet inAs, boolean inIsInsert) {
        try {
            Iterator iter = null;
            iter = inAs.getPublicSysAttrNames();
            while (iter.hasNext()) {
                String attrName = (String) iter.next();
                ValueAndClass vac = inAs.getPublicSysAttr(attrName);
                int pubAttr = BaseDBQueryVendor.PUBLIC;
                int sysAttr = BaseDBQueryVendor.SYSTEM;
                if (inIsInsert) {
                    insertWorkflowAttribute(inCon, inPfId, attrName, vac, pubAttr, sysAttr);
                } else {
                    updateWorkflowAttribute(inCon, inPfId, attrName, vac, sysAttr);
                }
            }
            iter = inAs.getPrivateSysAttrNames();
            while (iter.hasNext()) {
                String attrName = (String) iter.next();
                ValueAndClass vac = inAs.getPrivateSysAttr(attrName);
                int pubAttr = BaseDBQueryVendor.PRIVATE;
                int sysAttr = BaseDBQueryVendor.SYSTEM;
                if (inIsInsert) {
                    insertWorkflowAttribute(inCon, inPfId, attrName, vac, pubAttr, sysAttr);
                } else {
                    updateWorkflowAttribute(inCon, inPfId, attrName, vac, sysAttr);
                }
            }
            iter = inAs.getPublicAppAttrNames();
            while (iter.hasNext()) {
                String attrName = (String) iter.next();
                ValueAndClass vac = inAs.getPublicAppAttr(attrName);
                int pubAttr = BaseDBQueryVendor.PUBLIC;
                int sysAttr = BaseDBQueryVendor.APPLICATION;
                if (inIsInsert) {
                    insertWorkflowAttribute(inCon, inPfId, attrName, vac, pubAttr, sysAttr);
                } else {
                    updateWorkflowAttribute(inCon, inPfId, attrName, vac, sysAttr);
                }
            }
            iter = inAs.getPrivateAppAttrNames();
            while (iter.hasNext()) {
                String attrName = (String) iter.next();
                ValueAndClass vac = inAs.getPrivateAppAttr(attrName);
                int pubAttr = BaseDBQueryVendor.PRIVATE;
                int sysAttr = BaseDBQueryVendor.APPLICATION;
                if (inIsInsert) {
                    insertWorkflowAttribute(inCon, inPfId, attrName, vac, pubAttr, sysAttr);
                } else {
                    updateWorkflowAttribute(inCon, inPfId, attrName, vac, sysAttr);
                }
            }
        } catch (SQLException sqle) {
            throw new PFRuntimeException(sqle);
        }
    }

    private static void insertWorkflowAttribute(Connection inCon, BigDecimal inPfId, String inAttrName, ValueAndClass inVac, int inPubAttr, int inSysAttr) throws SQLException {
        final String INSERT_QUERY = "insert into pf_attributes (" + QueryConstructor.ID + ", " + QueryConstructor.PF_ID + ", " + QueryConstructor.ATTR_NAME + ", " + QueryConstructor.DECIMAL_VALUE + ", " + QueryConstructor.BOOLEAN_VALUE + ", " + QueryConstructor.STRING_VALUE + ", " + QueryConstructor.LONG_STRING_VALUE + ", " + BaseDBQueryVendor.ATTR_TYPE + ", " + BaseDBQueryVendor.ATTR_ACCESS + ", " + BaseDBQueryVendor.ATTR_CLASS + ") values (NEXTVAL FOR pf_sequence,?,?,?,?,?,?,?,?,?)";
        Object value = inVac.getValue();
        Class valueClass = inVac.getValueClass();
        PreparedStatement ps = null;
        ps = inCon.prepareStatement(INSERT_QUERY);
        ps.setBigDecimal(1, inPfId);
        ps.setString(2, inAttrName.toUpperCase());
        ps.setInt(7, inSysAttr);
        ps.setInt(8, inPubAttr);
        if (MiscHelper.isClassNumber(valueClass)) {
            ps.setInt(9, BaseDBQueryVendor.DECIMAL);
            if (value != null) {
                ps.setBigDecimal(3, MiscHelper.convertNumberToBigDecimal(value));
            } else {
                ps.setNull(3, Types.NUMERIC);
            }
            ps.setNull(4, Types.NUMERIC);
            ps.setNull(5, Types.VARCHAR);
            ps.setNull(6, Types.CLOB);
        } else if (value.getClass().getName().equals(Boolean.class.getName())) {
            ps.setInt(9, BaseDBQueryVendor.BOOLEAN);
            Boolean bValue = (Boolean) value;
            ps.setNull(3, Types.NUMERIC);
            if (bValue == null) {
                ps.setNull(4, Types.NUMERIC);
            } else if (bValue.booleanValue()) {
                ps.setInt(4, 1);
            } else {
                ps.setInt(4, 0);
            }
            ps.setNull(5, Types.VARCHAR);
            ps.setNull(6, Types.CLOB);
        } else {
            ps.setInt(9, BaseDBQueryVendor.STRING);
            String sValue = null;
            if (value != null) {
                sValue = value.toString();
            }
            ps.setNull(3, Types.NUMERIC);
            ps.setNull(4, Types.NUMERIC);
            if (sValue == null) {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.CLOB);
            } else if (sValue.length() <= 250) {
                ps.setString(5, sValue);
                ps.setNull(6, Types.CLOB);
            } else {
                ps.setNull(5, Types.VARCHAR);
                ps.setCharacterStream(6, new StringReader(sValue), sValue.length());
            }
        }
        ps.execute();
        ps.close();
    }

    private static void updateWorkflowAttribute(Connection inCon, BigDecimal inPfId, String inAttrName, ValueAndClass inVac, int inSysAttr) throws SQLException {
        final String UPDATE_BOOLEAN = "update pf_attributes set " + QueryConstructor.BOOLEAN_VALUE + " = ? where " + QueryConstructor.PF_ID + " = ? and " + QueryConstructor.ATTR_NAME + " = ? and " + BaseDBQueryVendor.ATTR_TYPE + " = ?";
        final String UPDATE_DECIMAL = "update pf_attributes set " + QueryConstructor.DECIMAL_VALUE + " = ? where " + QueryConstructor.PF_ID + " = ? and " + QueryConstructor.ATTR_NAME + " = ? and " + BaseDBQueryVendor.ATTR_TYPE + " = ?";
        final String UPDATE_STRING = "update pf_attributes set " + QueryConstructor.STRING_VALUE + " = ?, " + QueryConstructor.LONG_STRING_VALUE + " = ? where " + QueryConstructor.PF_ID + " = ? and " + QueryConstructor.ATTR_NAME + " = ? and " + BaseDBQueryVendor.ATTR_TYPE + " = ?";
        Object value = inVac.getValue();
        Class valueClass = inVac.getValueClass();
        PreparedStatement ps = null;
        if (MiscHelper.isClassNumber(valueClass)) {
            ps = inCon.prepareStatement(UPDATE_DECIMAL);
            if (value != null) {
                ps.setBigDecimal(1, MiscHelper.convertNumberToBigDecimal(value));
            } else {
                ps.setNull(1, Types.NUMERIC);
            }
            ps.setBigDecimal(2, inPfId);
            ps.setString(3, inAttrName);
            ps.setInt(4, inSysAttr);
        } else if (value.getClass().getName().equals(Boolean.class.getName())) {
            ps = inCon.prepareStatement(UPDATE_BOOLEAN);
            Boolean bValue = (Boolean) value;
            if (bValue == null) {
                ps.setNull(1, Types.NUMERIC);
            } else if (bValue.booleanValue()) {
                ps.setInt(1, 1);
            } else {
                ps.setInt(1, 0);
            }
            ps.setBigDecimal(2, inPfId);
            ps.setString(3, inAttrName);
            ps.setInt(4, inSysAttr);
        } else {
            ps = inCon.prepareStatement(UPDATE_STRING);
            String sValue = null;
            if (value != null) {
                sValue = value.toString();
            }
            if (sValue == null) {
                ps.setNull(1, Types.VARCHAR);
                ps.setNull(2, Types.CLOB);
            } else if (sValue.length() <= 250) {
                ps.setString(1, sValue);
                ps.setNull(2, Types.CLOB);
            } else {
                ps.setNull(1, Types.VARCHAR);
                ps.setCharacterStream(2, new StringReader(sValue), sValue.length());
            }
            ps.setBigDecimal(3, inPfId);
            ps.setString(4, inAttrName);
            ps.setInt(5, inSysAttr);
        }
        ps.execute();
        ps.close();
    }

    public void fireTrigger(String inTrigger) {
        try {
            QueueConnectionFactory queueFactory = getWorkflowQueueConnectionFactory();
            QueueConnection queueConnection = queueFactory.createQueueConnection();
            Queue queue = getTriggerQueue();
            QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueSender sender = queueSession.createSender(queue);
            queueConnection.start();
            TextMessage message = queueSession.createTextMessage();
            message.setText(inTrigger);
            sender.send(message);
            queueConnection.close();
        } catch (JMSException jmse) {
            throw new PFRuntimeException(jmse);
        }
    }

    public void setSessionContext(SessionContext sc) {
        this.sessionContext = sc;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbCreate() {
    }

    public void ejbRemove() {
    }
}
