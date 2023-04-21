package jms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;

public class JMSSender {

    private static final String HEADERS_FILE_EXTENSION = "headers";

    private static final String OUTBOX_PATH = "outbox";

    private static final String QUEUE_NAME = "queue.name";

    private static final String CONFAC_NAME = "confac.name";

    private static final String URL_PROP = "server.url";

    private static final String USER_PROP = "server.user";

    private static final String PASS_PROP = "server.password";

    private Properties props;

    private Context ctx;

    private Queue queue;

    private QueueSession queueSession;

    private QueueSender sender;

    public JMSSender() throws IOException {
        this.props = new Properties();
        InputStream is = new FileInputStream("conf/sender.properties");
        props.load(is);
        System.out.println("Loaded properties version: " + props.getProperty("version"));
    }

    public static void main(String[] args) throws IOException, NamingException, JMSException {
        JMSSender sender = new JMSSender();
        sender.launch();
    }

    private void launch() throws NamingException, JMSException, IOException {
        initJMSObjects();
        File[] files = getFilesFromOutbox();
        int filesSent = 0;
        for (File file : files) {
            if (getSuffix(file.getName()).equals(HEADERS_FILE_EXTENSION)) {
                System.out.println("not sending headerfile '" + file.getName() + "' as message");
                continue;
            }
            Properties properties = new Properties();
            getHeaders(file, properties);
            sendFile(file, properties);
            filesSent++;
        }
        System.out.println("Finished sending " + filesSent + " files");
        queueSession.close();
    }

    private String getSuffix(String name) {
        String suffix = name.substring(name.lastIndexOf('.') + 1, name.length());
        return suffix;
    }

    /**
	 * Reads in all of the JMS headers in the form of a properties object
	 * @param file
	 * @param properties
	 * @throws IOException 
	 * @throws  
	 */
    private void getHeaders(final File file, Properties properties) throws IOException {
        File[] propertiesFiles = file.getParentFile().listFiles(new FileFilter() {

            public boolean accept(File path) {
                String pathName = path.getName();
                String fileName = file.getName();
                if (getRawFileName(pathName).equalsIgnoreCase(getRawFileName(fileName))) {
                    if (getSuffix(pathName).equals(HEADERS_FILE_EXTENSION)) {
                        System.out.println("found a headers file: " + pathName);
                        return true;
                    }
                }
                return false;
            }

            private String getRawFileName(String fileName) {
                String rawFileName = fileName.substring(0, fileName.lastIndexOf('.'));
                return rawFileName;
            }
        });
        if (propertiesFiles.length > 0 && propertiesFiles[0] != null) {
            properties.load(new FileInputStream(propertiesFiles[0]));
        } else {
            System.out.println("no headers found for " + file.getName());
        }
    }

    private void initJMSObjects() throws NamingException, JMSException {
        Environment env = new Environment();
        env.setProviderUrl(props.getProperty(URL_PROP));
        env.setSecurityPrincipal(props.getProperty(USER_PROP));
        env.setSecurityCredentials(props.getProperty(PASS_PROP));
        this.ctx = env.getContext();
        queue = (Queue) ctx.lookup(props.getProperty(QUEUE_NAME));
        ConnectionFactory confac = (ConnectionFactory) ctx.lookup(props.getProperty(CONFAC_NAME));
        QueueConnection con = (QueueConnection) confac.createConnection();
        queueSession = con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        sender = queueSession.createSender(queue);
        System.out.println("Create JMS objects");
    }

    private void sendFile(File file, Properties properties) throws JMSException, IOException {
        String message = loadFileIntoString(file);
        TextMessage mess = queueSession.createTextMessage(message);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            System.out.println("Setting property " + entry.getKey() + "=" + entry.getValue());
            mess.setStringProperty((String) entry.getKey(), ((String) entry.getValue()));
        }
        sender.send(mess);
        System.out.println("Sent message: " + file.getName());
    }

    private String loadFileIntoString(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        String line = reader.readLine();
        StringBuffer buf = new StringBuffer();
        while (line != null) {
            buf.append(line);
            line = reader.readLine();
        }
        return buf.toString();
    }

    private static File[] getFilesFromOutbox() {
        File outbox = new File(OUTBOX_PATH);
        assert (outbox.isDirectory());
        return outbox.listFiles();
    }
}
