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
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.AttributedString;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.QueueSender;
import javax.jms.QueueReceiver;
import javax.jms.Queue;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Session;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
import org.powerfolder.config.ConfigManager;
import org.powerfolder.config.ConfigManagerFactory;
import org.powerfolder.utils.misc.MiscHelper;
import org.powerfolder.utils.xml.XMLHelper;
import org.powerfolder.workflow.model.Workflow;
import org.powerfolder.workflow.model.attributes.AttributeSet;
import org.powerfolder.workflow.model.attributes.WaitHelper;
import org.powerfolder.workflow.model.history.History;
import org.powerfolder.workflow.model.history.Record;
import org.powerfolder.workflow.model.history.Trace;
import org.powerfolder.workflow.model.script.ReturnValueAndClassContext;
import org.powerfolder.workflow.model.script.RootScriptTagHolder;
import org.powerfolder.workflow.model.script.ScriptTag;
import org.powerfolder.workflow.model.script.ScriptTagHelper;
import org.powerfolder.workflow.model.trigger.Trigger;

public class GenericJMSWorkflowLifecycleBean implements SessionBean {

    private SessionContext sessionContext = null;

    public GenericJMSWorkflowLifecycleBean() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbCreate() {
    }

    public void ejbRemove() {
    }

    public void setSessionContext(SessionContext sc) {
        sessionContext = sc;
    }
}
