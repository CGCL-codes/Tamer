package br.ufmg.lcc.arangi.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.commons.BeanHelper;
import br.ufmg.lcc.arangi.commons.ClasspathEntityResolver;
import br.ufmg.lcc.arangi.commons.NVLHelper;

/**
 * 
 * @author Cesar Correia
 * @author Edre Moreira
 */
public class HibernateConfiguration implements IConfiguration {

    protected static Logger log = Logger.getLogger(HibernateConfiguration.class);

    public static String MySQLDialect = "org.hibernate.dialect.MySQLDialect";

    public static String PostgreSQLDialect = "org.hibernate.dialect.PostgreSQLDialect";

    public static String OracleDialect = "org.hibernate.dialect.OracleDialect";

    public static String DB2Dialect = "org.hibernate.dialect.DB2Dialect";

    public static String SybaseDialect = "org.hibernate.dialect.SybaseDialect";

    public static String SQLServerDialect = "org.hibernate.dialect.SQLServerDialect";

    public static String FirebirdDialect = "org.hibernate.dialect.FirebirdDialect";

    public static String InterbaseDialect = "org.hibernate.dialect.InterbaseDialect";

    public static String InformixDialect = "org.hibernate.dialect.InformixDialect";

    public static String IngresDialect = "org.hibernate.dialect.IngresDialect";

    private Configuration configuration;

    private SessionFactory sessionFactory;

    private ClasspathEntityResolver entityResolver;

    private String xmlConfigFile;

    private File hibernateFile;

    private String dialect;

    public Object getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public void buildSessionFactory() throws BasicException {
        try {
            sessionFactory = configuration.buildSessionFactory();
        } catch (HibernateException e) {
            throw BasicException.basicErrorHandling("Error building session factory", "errorMsgBuildingSessionFactory", e, log);
        }
    }

    public void configure() {
        try {
            configuration = (Configuration) BeanHelper.newInstance(configuration.getClass().getName());
        } catch (BasicException e1) {
            e1.printStackTrace();
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(getEntityResolver());
            InputStream inputStream = getClass().getResourceAsStream("/" + xmlConfigFile);
            Document document = builder.parse(inputStream);
            if (dialect != null) {
                NodeList nodeList = document.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node hibernateConfigurationNode = nodeList.item(i);
                    if (hibernateConfigurationNode.getNodeName().equals("hibernate-configuration")) {
                        NodeList hibernateConfigurationChildren = hibernateConfigurationNode.getChildNodes();
                        for (int j = 0; j < hibernateConfigurationChildren.getLength(); j++) {
                            Node sessionFactoryNode = hibernateConfigurationChildren.item(j);
                            if (sessionFactoryNode.getNodeName().equals("session-factory")) {
                                NodeList sessionFactoryChildren = sessionFactoryNode.getChildNodes();
                                for (int k = 0; k < sessionFactoryChildren.getLength(); k++) {
                                    Node propertyNode = sessionFactoryChildren.item(k);
                                    if (propertyNode.getNodeName() != null && propertyNode.getNodeName().equals("property")) {
                                        if (propertyNode.getAttributes() != null) {
                                            NamedNodeMap attributes = propertyNode.getAttributes();
                                            Node n = attributes.getNamedItem("name");
                                            if (n.getNodeValue().equals("dialect")) {
                                                String value = propertyNode.getTextContent();
                                                propertyNode.setTextContent(dialect);
                                                value = propertyNode.getTextContent();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            configuration.configure(document);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setEntityResolver(ClasspathEntityResolver entityResolver) {
        this.entityResolver = entityResolver;
        configuration.setEntityResolver(entityResolver);
    }

    public ClasspathEntityResolver getEntityResolver() {
        return entityResolver;
    }

    public String getXmlConfigFile() {
        return xmlConfigFile;
    }

    public void setXmlConfigFile(String xmlConfigFile) {
        this.xmlConfigFile = xmlConfigFile;
    }

    public Object getUnderlyingConfiguration() {
        return configuration;
    }

    public File getHibernateFile() {
        return hibernateFile;
    }

    public void setHibernateFile(File hibernateFile) {
        this.hibernateFile = hibernateFile;
    }

    public void reconfigure() {
        configure();
    }

    public String getDialect() {
        if (!NVLHelper.isEmpty(dialect)) {
            return dialect;
        }
        return configuration.getProperty("dialect");
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public void setDialectByJDBCDriver(String driver) {
        if (driver.toLowerCase().indexOf("mysql") >= 0) {
            dialect = MySQLDialect;
        } else if (driver.toLowerCase().indexOf("postgre") >= 0) {
            dialect = PostgreSQLDialect;
        } else if (driver.toLowerCase().indexOf("oracle") >= 0) {
            dialect = OracleDialect;
        } else if (driver.toLowerCase().indexOf("db2") >= 0) {
            dialect = DB2Dialect;
        } else if (driver.toLowerCase().indexOf("sybase") >= 0) {
            dialect = SybaseDialect;
        } else if (driver.toLowerCase().indexOf("sqlserver") >= 0) {
            dialect = SQLServerDialect;
        } else if (driver.toLowerCase().indexOf("firebird") >= 0) {
            dialect = FirebirdDialect;
        } else if (driver.toLowerCase().indexOf("interbase") >= 0) {
            dialect = InterbaseDialect;
        } else if (driver.toLowerCase().indexOf("informix") >= 0) {
            dialect = InformixDialect;
        } else if (driver.toLowerCase().indexOf("ingres") >= 0) {
            dialect = IngresDialect;
        }
    }
}
