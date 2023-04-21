package issrg.test.xacml;

import issrg.xacml.*;
import issrg.utils.handler.Handler;
import com.sun.xacml.ctx.*;
import com.sun.xacml.attr.*;
import java.io.*;
import java.util.*;
import java.net.URI;
import org.apache.log4j.*;

/**
 *
 * @author ls97
 */
public class TestingPDP {

    private static Logger logger = Logger.getLogger(TestingPDP.class.getName());

    /** Creates a new instance of TestPDP */
    public TestingPDP() {
    }

    public void setLogLevel(String configFile) throws Exception {
        Logger root = Logger.getRootLogger();
        Logger log1, log2;
        Appender appender;
        Level level;
        log1 = Logger.getLogger(TestingPDP.class);
        log2 = Logger.getLogger(Handler.class);
        String priority = "debug";
        String file = null;
        Layout layout = new PatternLayout("%d{dd MM yyyy HH:mm:ss} %-5p %c %x - %m%n");
        String fileName = configFile;
        issrg.utils.handler.Config config = new issrg.utils.handler.Config();
        Properties props = new Properties();
        try {
            InputStream in = new FileInputStream(config.getURL(fileName));
            props.load(in);
            in.close();
            priority = props.getProperty("log-level");
            file = props.getProperty("log-file");
            String lay = props.getProperty("layout");
            if (lay != null) layout = new PatternLayout(lay);
            if (file == null) {
                appender = new WriterAppender(layout, System.out);
            } else {
                appender = new org.apache.log4j.FileAppender(layout, file);
            }
        } catch (Exception e) {
            appender = new WriterAppender(layout, System.out);
        }
        root.removeAllAppenders();
        BasicConfigurator.configure(appender);
        level = Level.toLevel(priority.toUpperCase());
        log1.setLevel(level);
        log2.setLevel(level);
    }

    public static void main(String[] args) {
        TestingPDP test = new TestingPDP();
        String[] policy = { "C:\\home\\CVS\\test.txt" };
        issrg.xacml.PDPConfig config = new issrg.xacml.PDPConfig(policy);
        HashSet subjects = new HashSet();
        HashSet resources = new HashSet();
        HashSet actions = new HashSet();
        HashSet environment = new HashSet();
        try {
            test.setLogLevel("C:\\home\\CVS\\log.cfg");
            issrg.xacml.PDP pdp = new issrg.xacml.PDP(config);
            HashSet attributes = new HashSet();
            URI idURI = new URI("permis:permisRole");
            URI typeURI = new URI("http://www.w3.org/2001/XMLSchema#string");
            AttributeValue attributeValue = AttributeFactory.createAttribute(typeURI, "student");
            Attribute subjectAttribute = new Attribute(idURI, null, null, attributeValue);
            attributes.add(subjectAttribute);
            com.sun.xacml.ctx.Subject subject = new com.sun.xacml.ctx.Subject(attributes);
            subjects.add(subject);
            logger.debug("subject is created");
            idURI = new URI("urn:oasis:names:tc:xacml:1.0:resource:resource-id");
            typeURI = new URI("http://www.w3.org/2001/XMLSchema#string");
            attributeValue = AttributeFactory.createAttribute(typeURI, "cn=test target,o=university of kent,c=gb");
            Attribute resourceAttribute = new Attribute(idURI, null, null, attributeValue);
            resources.add(resourceAttribute);
            logger.debug("resource is created");
            idURI = new URI("urn:oasis:names:tc:xacml:1.0:action:action-id");
            typeURI = new URI("http://www.w3.org/2001/XMLSchema#string");
            attributeValue = AttributeFactory.createAttribute(typeURI, "print");
            Attribute actionAttribute = new Attribute(idURI, null, null, attributeValue);
            actions.add(actionAttribute);
            idURI = new URI("urn:oasis:names:tc:xacml:1.0:action:pages");
            typeURI = new URI("http://www.w3.org/2001/XMLSchema#integer");
            attributeValue = AttributeFactory.createAttribute(typeURI, "99");
            actionAttribute = new Attribute(idURI, null, null, attributeValue);
            actions.add(actionAttribute);
            logger.debug("action is created");
            idURI = new URI("urn:oasis:names:tc:xacml:1.0:environment:balance.student[id(S)]");
            typeURI = new URI("http://www.w3.org/2001/XMLSchema#integer");
            attributeValue = AttributeFactory.createAttribute(typeURI, "100");
            Attribute environmentAttribute = new Attribute(idURI, null, null, attributeValue);
            environment.add(environmentAttribute);
            logger.debug("environment is created");
            com.sun.xacml.ctx.RequestCtx request = new com.sun.xacml.ctx.RequestCtx(subjects, resources, actions, environment);
            logger.debug("request context is created");
            com.sun.xacml.ctx.ResponseCtx response = pdp.evaluate(request);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            response.encode(byteStream, new com.sun.xacml.Indenter());
            String res = byteStream.toString();
            logger.debug(res);
        } catch (Exception e) {
            System.out.println("error:" + e);
        }
    }
}
