package org.lnicholls.galleon.util;

import java.beans.IntrospectionException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.betwixt.IntrospectionConfiguration;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.PropertySuppressionStrategy;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.lnicholls.galleon.server.Constants;
import org.lnicholls.galleon.server.ServerConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.lnicholls.galleon.app.*;
import org.lnicholls.galleon.server.*;
import org.lnicholls.galleon.togo.*;

/**
 * Utility class to read the conf/configure.xml file on startup
 */
public class Configurator implements Constants {

    private static Logger log = Logger.getLogger(Configurator.class.getName());

    private static String TAG_CONFIGURATION = "configuration";

    private static String TAG_SERVER = "server";

    private static String TAG_APP = "app";

    private static String TAG_TIVO = "tivo";

    private static String TAG_RULE = "rule";

    private static String TAG_MUSIC_PLAYER_CONFIGURATION = "musicPlayerConfiguration";

    private static String TAG_DATA_CONFIGURATION = "dataConfiguration";

    private static String TAG_GOBACK_CONFIGURATION = "goBackConfiguration";

    private static String TAG_SCREENSAVER_CONFIGURATION = "screenSaverConfiguration";

    private static String TAG_DOWNLOAD_CONFIGURATION = "downloadConfiguration";

    private static String ATTRIBUTE_VERSION = "version";

    private static String ATTRIBUTE_URL = "url";

    private static String ATTRIBUTE_PORT = "port";

    private static String ATTRIBUTE_HTTP_PORT = "httpPort";

    private static String ATTRIBUTE_TITLE = "title";

    private static String ATTRIBUTE_NAME = "name";

    private static String ATTRIBUTE_CLASS = "class";

    private static String ATTRIBUTE_RELOAD = "reload";

    private static String ATTRIBUTE_IP_ADDRESS = "ipaddress";

    private static String ATTRIBUTE_PUBLIC_IP_ADDRESS = "publicIpAddress";

    private static String ATTRIBUTE_PIN = "pin";

    private static String ATTRIBUTE_PASSWORD = "password";

    private static String ATTRIBUTE_NET_MASK = "netmask";

    private static String ATTRIBUTE_SHUFFLE_ITEMS = "shuffleItems";

    private static String ATTRIBUTE_DEBUG = "debug";

    private static String ATTRIBUTE_TIMEOUT = "disableTimeout";

    private static String ATTRIBUTE_MENU = "menu";

    private static String ATTRIBUTE_GENERATE_THUMBNAILS = "generateThumbnails";

    private static String ATTRIBUTE_USE_TIVO_BEACON = "useTiVoBeacon";

    private static String ATTRIBUTE_RECORDINGS_PATH = "recordingsPath";

    private static String ATTRIBUTE_MEDIA_ACCESS_KEY = "mediaAccessKey";

    private static String ATTRIBUTE_SKIN = "skin";

    public Configurator() {
        JavaHMO javahmo = new JavaHMO();
    }

    public void load(AppManager appManager) {
        File configureDir = new File(System.getProperty("conf"));
        load(appManager, configureDir);
    }

    public void load(AppManager appManager, File configureDir) {
        try {
            File file = new File(configureDir.getAbsolutePath() + "/configure.xml.rpmsave");
            if (file.exists()) {
                loadDocument(appManager, file);
                save(appManager);
                File oldFile = new File(configureDir.getAbsolutePath() + "/configure.xml.rpmsave.old");
                if (oldFile.exists()) oldFile.delete();
                file.renameTo(oldFile);
            } else {
                loadDocument(appManager, new File(configureDir.getAbsolutePath() + "/configure.xml"));
            }
        } catch (Exception ex) {
            Tools.logException(Configurator.class, ex);
        }
    }

    private void loadDocument(AppManager appManager, File file) {
        ServerConfiguration serverConfiguration = Server.getServer().getServerConfiguration();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            FileInputStream in = null;
            DocumentBuilder builder = factory.newDocumentBuilder();
            in = new FileInputStream(file);
            Document document = builder.parse(in);
            in.close();
            in = null;
            Node domNode = document.getFirstChild();
            if (log.isDebugEnabled()) log.debug("document:" + domNode.getNodeName());
            if (domNode.getNodeName().equalsIgnoreCase(TAG_CONFIGURATION)) {
                NamedNodeMap namedNodeMap = domNode.getAttributes();
                if (namedNodeMap != null) {
                    Node attribute = namedNodeMap.getNamedItem(ATTRIBUTE_VERSION);
                    if (log.isDebugEnabled()) log.debug(domNode.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                    loadDocument(domNode, appManager);
                    if (!attribute.getNodeValue().equals(serverConfiguration.getVersion())) save(appManager);
                }
            }
        } catch (SAXParseException spe) {
            log.error("Parsing error, line " + spe.getLineNumber() + ", uri " + spe.getSystemId());
            log.error("   " + spe.getMessage());
            Tools.logException(Configurator.class, spe);
            Exception x = spe;
            if (spe.getException() != null) x = spe.getException();
            Tools.logException(Configurator.class, x);
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) x = sxe.getException();
            Tools.logException(Configurator.class, x);
        } catch (ParserConfigurationException pce) {
            log.error("Cannot get context" + file.getAbsolutePath());
            Tools.logException(Configurator.class, pce);
        } catch (IOException ioe) {
            log.error("Cannot get context" + file.getAbsolutePath());
            Tools.logException(Configurator.class, ioe);
        } finally {
        }
    }

    private void loadDocument(Node configurationNode, AppManager appManager) {
        ServerConfiguration serverConfiguration = Server.getServer().getServerConfiguration();
        try {
            for (int i = 0; i < configurationNode.getChildNodes().getLength(); i++) {
                Node node = configurationNode.getChildNodes().item(i);
                if (log.isDebugEnabled()) log.debug("node:" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (node.getNodeName().equals(TAG_SERVER)) {
                        if (log.isDebugEnabled()) log.debug("Found server");
                        NamedNodeMap namedNodeMap = node.getAttributes();
                        if (namedNodeMap != null) {
                            Node attribute = namedNodeMap.getNamedItem(ATTRIBUTE_RELOAD);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                try {
                                    int reload = Integer.parseInt(attribute.getNodeValue());
                                    serverConfiguration.setReload(reload);
                                } catch (NumberFormatException ex) {
                                    log.error("Invalid " + ATTRIBUTE_RELOAD + " for " + TAG_SERVER + ": " + attribute.getNodeValue());
                                }
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_PORT);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                try {
                                    int port = Integer.parseInt(attribute.getNodeValue());
                                    serverConfiguration.setPort(port);
                                } catch (NumberFormatException ex) {
                                    log.error("Invalid " + ATTRIBUTE_PORT + " for " + TAG_SERVER + ": " + attribute.getNodeValue());
                                }
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_HTTP_PORT);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                try {
                                    int port = Integer.parseInt(attribute.getNodeValue());
                                    serverConfiguration.setHttpPort(port);
                                } catch (NumberFormatException ex) {
                                    log.error("Invalid " + ATTRIBUTE_HTTP_PORT + " for " + TAG_SERVER + ": " + attribute.getNodeValue());
                                }
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_TITLE);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                serverConfiguration.setName(attribute.getNodeValue());
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_IP_ADDRESS);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                String address = attribute.getNodeValue();
                                if (!Tools.isLocalAddress(address)) {
                                    log.error("Invalid server IP address: " + address);
                                    address = Tools.getLocalIpAddress();
                                    log.debug("Changing IP address to: " + address);
                                }
                                serverConfiguration.setIPAddress(address);
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_PUBLIC_IP_ADDRESS);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                serverConfiguration.setPublicIPAddress(attribute.getNodeValue());
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_PIN);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue().length());
                                serverConfiguration.setPin(attribute.getNodeValue());
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_PASSWORD);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue().length());
                                serverConfiguration.setPassword(attribute.getNodeValue());
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_SHUFFLE_ITEMS);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                serverConfiguration.setShuffleItems(Boolean.valueOf(attribute.getNodeValue()).booleanValue());
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_GENERATE_THUMBNAILS);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                serverConfiguration.setGenerateThumbnails(Boolean.valueOf(attribute.getNodeValue()).booleanValue());
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_RECORDINGS_PATH);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                serverConfiguration.setRecordingsPath(Tools.unEscapeXMLChars(URLDecoder.decode(attribute.getNodeValue(), ENCODING)));
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_MEDIA_ACCESS_KEY);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue().length());
                                serverConfiguration.setMediaAccessKey(attribute.getNodeValue());
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_SKIN);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue().length());
                                serverConfiguration.setSkin(attribute.getNodeValue());
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_DEBUG);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                serverConfiguration.setDebug(Boolean.valueOf(attribute.getNodeValue()).booleanValue());
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_TIMEOUT);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                serverConfiguration.setDisableTimeout(Boolean.valueOf(attribute.getNodeValue()).booleanValue());
                            }
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_MENU);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                serverConfiguration.setMenu(Boolean.valueOf(attribute.getNodeValue()).booleanValue());
                            }
                        }
                    } else if (node.getNodeName().equals(TAG_APP)) {
                        if (log.isDebugEnabled()) log.debug("Found app");
                        NamedNodeMap namedNodeMap = node.getAttributes();
                        if (namedNodeMap != null) {
                            String title = null;
                            String className = null;
                            Node attribute = namedNodeMap.getNamedItem(ATTRIBUTE_NAME);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                title = attribute.getNodeValue();
                            } else log.error("Missing required " + ATTRIBUTE_NAME + " attribute for " + TAG_APP);
                            attribute = namedNodeMap.getNamedItem(ATTRIBUTE_CLASS);
                            if (attribute != null) {
                                if (log.isDebugEnabled()) log.debug(node.getNodeName() + ":" + attribute.getNodeName() + "=" + attribute.getNodeValue());
                                className = attribute.getNodeValue();
                            } else log.error("Missing required " + ATTRIBUTE_CLASS + " attribute for " + TAG_APP);
                            if (className != null) {
                                if (className.indexOf('$') != -1) className = className.substring(0, className.indexOf('$')); else className = className.substring(0, className.length() - "Configuration".length());
                                Object appConfiguration = null;
                                Iterator appDescriptorIterator = appManager.getAppDescriptors().iterator();
                                while (appDescriptorIterator.hasNext()) {
                                    AppDescriptor appDescriptor = (AppDescriptor) appDescriptorIterator.next();
                                    if (appDescriptor.getClassName().equals(className)) {
                                        AppContext appContext = new AppContext(appDescriptor);
                                        if (appContext.getConfiguration() != null) {
                                            try {
                                                BeanReader beanReader = new BeanReader();
                                                beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
                                                beanReader.registerBeanClass("app", appContext.getConfiguration().getClass());
                                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                                OutputFormat of = new OutputFormat("XML", ENCODING, true);
                                                XMLSerializer serializer = new XMLSerializer(bos, of);
                                                serializer.asDOMSerializer();
                                                serializer.serialize((Element) node);
                                                StringReader xmlReader = new StringReader(bos.toString());
                                                bos.close();
                                                appConfiguration = beanReader.parse(xmlReader);
                                                appContext.setConfiguration(appConfiguration);
                                                appManager.createApp(appContext);
                                                if (log.isDebugEnabled()) log.debug("App=" + appContext);
                                            } catch (IntrospectionException ex) {
                                                log.error("Could not load app " + title + " (" + className + ")");
                                            }
                                        } else log.error("Could not find app " + title + " (" + className + ")");
                                    }
                                }
                                if (appConfiguration == null) {
                                    log.error("Could not find app " + title + " (" + className + ")");
                                }
                            }
                        }
                    } else if (node.getNodeName().equals(TAG_TIVO)) {
                        if (log.isDebugEnabled()) log.debug("Found TiVo");
                        try {
                            BeanReader beanReader = new BeanReader();
                            beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
                            beanReader.registerBeanClass("tivo", TiVo.class);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            OutputFormat of = new OutputFormat("XML", ENCODING, true);
                            XMLSerializer serializer = new XMLSerializer(bos, of);
                            serializer.asDOMSerializer();
                            serializer.serialize((Element) node);
                            StringReader xmlReader = new StringReader(bos.toString());
                            bos.close();
                            TiVo tivo = (TiVo) beanReader.parse(xmlReader);
                            serverConfiguration.addTiVo(tivo);
                            if (log.isDebugEnabled()) log.debug("TiVo=" + tivo);
                        } catch (IntrospectionException ex) {
                            log.error("Could not load tivo");
                        }
                    } else if (node.getNodeName().equals(TAG_RULE)) {
                        if (log.isDebugEnabled()) log.debug("Found Rule");
                        try {
                            BeanReader beanReader = new BeanReader();
                            beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
                            beanReader.registerBeanClass("rule", Rule.class);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            OutputFormat of = new OutputFormat("XML", ENCODING, true);
                            XMLSerializer serializer = new XMLSerializer(bos, of);
                            serializer.asDOMSerializer();
                            serializer.serialize((Element) node);
                            StringReader xmlReader = new StringReader(bos.toString());
                            bos.close();
                            Rule rule = (Rule) beanReader.parse(xmlReader);
                            serverConfiguration.addRule(rule);
                            if (log.isDebugEnabled()) log.debug("Rule=" + rule);
                        } catch (IntrospectionException ex) {
                            log.error("Could not load rule");
                        }
                    } else if (node.getNodeName().equals(TAG_MUSIC_PLAYER_CONFIGURATION)) {
                        if (log.isDebugEnabled()) log.debug("Found Music Player Configuration");
                        try {
                            BeanReader beanReader = new BeanReader();
                            beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
                            beanReader.registerBeanClass("musicPlayerConfiguration", MusicPlayerConfiguration.class);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            OutputFormat of = new OutputFormat("XML", ENCODING, true);
                            XMLSerializer serializer = new XMLSerializer(bos, of);
                            serializer.asDOMSerializer();
                            serializer.serialize((Element) node);
                            StringReader xmlReader = new StringReader(bos.toString());
                            bos.close();
                            MusicPlayerConfiguration musicPlayerConfiguration = (MusicPlayerConfiguration) beanReader.parse(xmlReader);
                            serverConfiguration.setMusicPlayerConfiguration(musicPlayerConfiguration);
                            if (log.isDebugEnabled()) log.debug("MusicPlayerConfiguration=" + musicPlayerConfiguration);
                        } catch (IntrospectionException ex) {
                            log.error("Could not load Music Player Configuration");
                        }
                    } else if (node.getNodeName().equals(TAG_DATA_CONFIGURATION)) {
                        if (log.isDebugEnabled()) log.debug("Found Data Configuration");
                        try {
                            BeanReader beanReader = new BeanReader();
                            beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
                            beanReader.registerBeanClass("dataConfiguration", DataConfiguration.class);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            OutputFormat of = new OutputFormat("XML", ENCODING, true);
                            XMLSerializer serializer = new XMLSerializer(bos, of);
                            serializer.asDOMSerializer();
                            serializer.serialize((Element) node);
                            StringReader xmlReader = new StringReader(bos.toString());
                            bos.close();
                            DataConfiguration dataConfiguration = (DataConfiguration) beanReader.parse(xmlReader);
                            serverConfiguration.setDataConfiguration(dataConfiguration);
                            if (log.isDebugEnabled()) log.debug("DataConfiguration=" + dataConfiguration);
                        } catch (IntrospectionException ex) {
                            log.error("Could not load Data Configuration");
                        }
                    } else if (node.getNodeName().equals(TAG_GOBACK_CONFIGURATION)) {
                        if (log.isDebugEnabled()) log.debug("Found GoBack Configuration");
                        try {
                            BeanReader beanReader = new BeanReader();
                            beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
                            beanReader.registerBeanClass("goBackConfiguration", GoBackConfiguration.class);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            OutputFormat of = new OutputFormat("XML", ENCODING, true);
                            XMLSerializer serializer = new XMLSerializer(bos, of);
                            serializer.asDOMSerializer();
                            serializer.serialize((Element) node);
                            StringReader xmlReader = new StringReader(bos.toString());
                            bos.close();
                            GoBackConfiguration goBackConfiguration = (GoBackConfiguration) beanReader.parse(xmlReader);
                            serverConfiguration.setGoBackConfiguration(goBackConfiguration);
                            if (log.isDebugEnabled()) log.debug("GoBackConfiguration=" + goBackConfiguration);
                        } catch (IntrospectionException ex) {
                            log.error("Could not load GoBack Configuration");
                        }
                    } else if (node.getNodeName().equals(TAG_SCREENSAVER_CONFIGURATION)) {
                        if (log.isDebugEnabled()) log.debug("Found Screen Saver Configuration");
                        ScreenSaverConfiguration screenSaverConfiguration = new ScreenSaverConfiguration();
                        screenSaverConfiguration.load((Element) node);
                        serverConfiguration.setScreenSaverConfiguration(screenSaverConfiguration);
                        if (log.isDebugEnabled()) log.debug("ScreenSaverConfiguration=" + screenSaverConfiguration);
                    } else if (node.getNodeName().equals(TAG_DOWNLOAD_CONFIGURATION)) {
                        if (log.isDebugEnabled()) log.debug("Found Download Configuration");
                        try {
                            BeanReader beanReader = new BeanReader();
                            beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
                            beanReader.registerBeanClass("downloadConfiguration", DownloadConfiguration.class);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            OutputFormat of = new OutputFormat("XML", ENCODING, true);
                            XMLSerializer serializer = new XMLSerializer(bos, of);
                            serializer.asDOMSerializer();
                            serializer.serialize((Element) node);
                            StringReader xmlReader = new StringReader(bos.toString());
                            bos.close();
                            DownloadConfiguration downloadConfiguration = (DownloadConfiguration) beanReader.parse(xmlReader);
                            serverConfiguration.setDownloadConfiguration(downloadConfiguration);
                            if (log.isDebugEnabled()) log.debug("DownloadConfiguration=" + downloadConfiguration);
                        } catch (IntrospectionException ex) {
                            log.error("Could not load Download Configuration");
                        }
                    }
                }
            }
        } catch (SAXParseException spe) {
            log.error("Parsing error, line " + spe.getLineNumber() + ", uri " + spe.getSystemId());
            log.error("   " + spe.getMessage());
            Tools.logException(Configurator.class, spe);
            Exception x = spe;
            if (spe.getException() != null) x = spe.getException();
            Tools.logException(Configurator.class, x);
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) x = sxe.getException();
            Tools.logException(Configurator.class, x);
        } catch (IOException ioe) {
            Tools.logException(Configurator.class, ioe, "Cannot get context");
        } catch (Exception ioe) {
            Tools.logException(Configurator.class, ioe, "Cannot get context");
        } finally {
        }
    }

    private Object convertType(Object value, Class conversionType) throws Exception {
        Class classArgs[] = new Class[1];
        classArgs[0] = value.getClass();
        Object objectArgs[] = new Object[1];
        objectArgs[0] = value;
        Constructor constructor = conversionType.getConstructor(classArgs);
        return constructor.newInstance(objectArgs);
    }

    public void save(AppManager appManager) {
        File configureDir = new File(System.getProperty("conf"));
        save(appManager, configureDir);
    }

    public void save(AppManager appManager, File configureDir) {
        ServerConfiguration serverConfiguration = Server.getServer().getServerConfiguration();
        class AppXMLIntrospector extends XMLIntrospector {

            AppXMLIntrospector() {
                super();
                IntrospectionConfiguration appconfig = new IntrospectionConfiguration();
                appconfig.setPropertySuppressionStrategy(new PropertySuppressionStrategy() {

                    @Override
                    public boolean suppressProperty(Class arg0, Class arg1, String arg2) {
                        return false;
                    }
                });
                setConfiguration(appconfig);
            }

            public org.apache.commons.betwixt.Descriptor createXMLDescriptor(org.apache.commons.betwixt.BeanProperty beanProperty) {
                if (beanProperty.getPropertyName().equals("lastModified") || beanProperty.getPropertyName().equals("sourceFormat") || beanProperty.getPropertyName().equals("contentType") || beanProperty.getPropertyName().equals("url") || beanProperty.getPropertyName().equals("group") || beanProperty.getPropertyName().equals("url") || beanProperty.getPropertyName().equals("modified") || beanProperty.getPropertyName().equals("musicPlayerConfiguration") || beanProperty.getPropertyName().equals("dataPlayerConfiguration") || beanProperty.getPropertyName().equals("items")) return null;
                if (beanProperty.getPropertyName().equals("class")) {
                    return super.createXMLDescriptor(new org.apache.commons.betwixt.BeanProperty(beanProperty.getPropertyName(), String.class, new org.apache.commons.betwixt.expression.ClassNameExpression(), beanProperty.getPropertyUpdater()));
                }
                if (beanProperty.getPropertyExpression() != null && beanProperty.getPropertyUpdater() != null) return super.createXMLDescriptor(beanProperty); else return null;
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(configureDir.getAbsoluteFile() + "/configure.xml");
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            StringBuffer buffer = new StringBuffer();
            synchronized (buffer) {
                buffer.append("<?xml version=\"1.0\" encoding=\"").append(ENCODING).append("\"?>\n");
                buffer.append("<").append(TAG_CONFIGURATION).append(" ").append(ATTRIBUTE_VERSION).append("=\"").append(serverConfiguration.getVersion()).append("\">\n");
                buffer.append("<").append(TAG_SERVER).append(" ").append(ATTRIBUTE_TITLE).append("=\"").append(serverConfiguration.getName()).append("\" ").append(ATTRIBUTE_RELOAD).append("=\"").append(serverConfiguration.getReload()).append("\" ").append(ATTRIBUTE_PORT).append("=\"").append(serverConfiguration.getPort()).append("\" ").append(ATTRIBUTE_HTTP_PORT).append("=\"").append(serverConfiguration.getHttpPort()).append("\"");
                if (serverConfiguration.getIPAddress() != null && serverConfiguration.getIPAddress().length() > 0) buffer.append(" ").append(ATTRIBUTE_IP_ADDRESS).append("=\"").append(serverConfiguration.getIPAddress()).append("\"");
                if (serverConfiguration.getPublicIPAddress() != null && serverConfiguration.getPublicIPAddress().length() > 0) buffer.append(" ").append(ATTRIBUTE_PUBLIC_IP_ADDRESS).append("=\"").append(serverConfiguration.getPublicIPAddress()).append("\"");
                if (serverConfiguration.getPin() != null && serverConfiguration.getPin().length() > 0) buffer.append(" ").append(ATTRIBUTE_PIN).append("=\"").append(serverConfiguration.getPin()).append("\"");
                if (serverConfiguration.getPassword() != null && serverConfiguration.getPassword().length() > 0) buffer.append(" ").append(ATTRIBUTE_PASSWORD).append("=\"").append(serverConfiguration.getPassword()).append("\"");
                buffer.append(" ").append(ATTRIBUTE_SHUFFLE_ITEMS).append("=\"").append(serverConfiguration.getShuffleItems()).append("\"");
                buffer.append(" ").append(ATTRIBUTE_GENERATE_THUMBNAILS).append("=\"").append(serverConfiguration.getGenerateThumbnails()).append("\"");
                buffer.append(" ").append(ATTRIBUTE_RECORDINGS_PATH).append("=\"").append(URLEncoder.encode(Tools.escapeXMLChars(serverConfiguration.getRecordingsPath()), ENCODING)).append("\"");
                buffer.append(" ").append(ATTRIBUTE_MEDIA_ACCESS_KEY).append("=\"").append(serverConfiguration.getMediaAccessKey()).append("\"");
                buffer.append(" ").append(ATTRIBUTE_SKIN).append("=\"").append(serverConfiguration.getSkin()).append("\"");
                buffer.append(" ").append(ATTRIBUTE_DEBUG).append("=\"").append(serverConfiguration.isDebug()).append("\"");
                buffer.append(" ").append(ATTRIBUTE_TIMEOUT).append("=\"").append(serverConfiguration.isDisableTimeout()).append("\"");
                buffer.append(" ").append(ATTRIBUTE_MENU).append("=\"").append(serverConfiguration.isMenu()).append("\"");
                buffer.append("/>\n");
                AppContext appContext = null;
                Object[] args = new Object[0];
                Object value = null;
                Iterator appIterator = appManager.getApps().iterator();
                while (appIterator.hasNext()) {
                    try {
                        appContext = (AppContext) appIterator.next();
                        log.debug("App: " + appContext);
                        StringWriter outputWriter = new StringWriter();
                        BeanWriter beanWriter = new BeanWriter(outputWriter);
                        AppXMLIntrospector appXMLIntrospector = new AppXMLIntrospector();
                        appXMLIntrospector.getConfiguration().setAttributesForPrimitives(true);
                        beanWriter.setXMLIntrospector(appXMLIntrospector);
                        beanWriter.enablePrettyPrint();
                        beanWriter.write("app", appContext.getConfiguration());
                        buffer.append(outputWriter.toString());
                    } catch (Exception ex) {
                        Tools.logException(Configurator.class, ex, "Could not save app: " + appContext.getDescriptor().getClassName());
                    }
                }
                TiVo tivo = null;
                Iterator tivoIterator = serverConfiguration.getTiVos().iterator();
                while (tivoIterator.hasNext()) {
                    try {
                        tivo = (TiVo) tivoIterator.next();
                        log.debug("TiVo: " + tivo);
                        StringWriter outputWriter = new StringWriter();
                        BeanWriter beanWriter = new BeanWriter(outputWriter);
                        AppXMLIntrospector appXMLIntrospector = new AppXMLIntrospector();
                        appXMLIntrospector.getConfiguration().setAttributesForPrimitives(true);
                        beanWriter.setXMLIntrospector(appXMLIntrospector);
                        beanWriter.enablePrettyPrint();
                        beanWriter.write("tivo", tivo);
                        buffer.append(outputWriter.toString());
                    } catch (Exception ex) {
                        Tools.logException(Configurator.class, ex, "Could not save tivo: " + tivo.getName());
                    }
                }
                Rule rule = null;
                Iterator ruleIterator = serverConfiguration.getRules().iterator();
                while (ruleIterator.hasNext()) {
                    try {
                        rule = (Rule) ruleIterator.next();
                        log.debug("Rule: " + rule);
                        StringWriter outputWriter = new StringWriter();
                        BeanWriter beanWriter = new BeanWriter(outputWriter);
                        AppXMLIntrospector appXMLIntrospector = new AppXMLIntrospector();
                        appXMLIntrospector.getConfiguration().setAttributesForPrimitives(true);
                        beanWriter.setXMLIntrospector(appXMLIntrospector);
                        beanWriter.enablePrettyPrint();
                        beanWriter.write("rule", rule);
                        buffer.append(outputWriter.toString());
                    } catch (Exception ex) {
                        Tools.logException(Configurator.class, ex, "Could not save rule: " + rule.getCriteriaString());
                    }
                }
                MusicPlayerConfiguration musicPlayerConfiguration = null;
                try {
                    musicPlayerConfiguration = serverConfiguration.getMusicPlayerConfiguration();
                    log.debug("MusicPlayerConfiguration: " + musicPlayerConfiguration);
                    StringWriter outputWriter = new StringWriter();
                    BeanWriter beanWriter = new BeanWriter(outputWriter);
                    AppXMLIntrospector appXMLIntrospector = new AppXMLIntrospector();
                    appXMLIntrospector.getConfiguration().setAttributesForPrimitives(true);
                    beanWriter.setXMLIntrospector(appXMLIntrospector);
                    beanWriter.enablePrettyPrint();
                    beanWriter.write("musicPlayerConfiguration", musicPlayerConfiguration);
                    buffer.append(outputWriter.toString());
                } catch (Exception ex) {
                    Tools.logException(Configurator.class, ex, "Could not save music player configuration: " + musicPlayerConfiguration);
                }
                DataConfiguration dataConfiguration = null;
                try {
                    dataConfiguration = serverConfiguration.getDataConfiguration();
                    log.debug("DataConfiguration: " + dataConfiguration);
                    StringWriter outputWriter = new StringWriter();
                    BeanWriter beanWriter = new BeanWriter(outputWriter);
                    AppXMLIntrospector appXMLIntrospector = new AppXMLIntrospector();
                    appXMLIntrospector.getConfiguration().setAttributesForPrimitives(true);
                    beanWriter.setXMLIntrospector(appXMLIntrospector);
                    beanWriter.enablePrettyPrint();
                    beanWriter.write("dataConfiguration", dataConfiguration);
                    buffer.append(outputWriter.toString());
                } catch (Exception ex) {
                    Tools.logException(Configurator.class, ex, "Could not save data configuration: " + dataConfiguration);
                }
                GoBackConfiguration goBackConfiguration = null;
                try {
                    goBackConfiguration = serverConfiguration.getGoBackConfiguration();
                    log.debug("GoBackConfiguration: " + goBackConfiguration);
                    StringWriter outputWriter = new StringWriter();
                    BeanWriter beanWriter = new BeanWriter(outputWriter);
                    AppXMLIntrospector appXMLIntrospector = new AppXMLIntrospector();
                    appXMLIntrospector.getConfiguration().setAttributesForPrimitives(true);
                    beanWriter.setXMLIntrospector(appXMLIntrospector);
                    beanWriter.enablePrettyPrint();
                    beanWriter.write("goBackConfiguration", goBackConfiguration);
                    buffer.append(outputWriter.toString());
                } catch (Exception ex) {
                    Tools.logException(Configurator.class, ex, "Could not save goback configuration: " + goBackConfiguration);
                }
                ScreenSaverConfiguration screenSaverConfiguration = null;
                try {
                    screenSaverConfiguration = serverConfiguration.getScreenSaverConfiguration();
                    log.debug("ScreenSaverConfiguration: " + screenSaverConfiguration);
                    StringWriter outputWriter = new StringWriter();
                    screenSaverConfiguration.save(TAG_SCREENSAVER_CONFIGURATION, outputWriter);
                    buffer.append(outputWriter.toString());
                } catch (Exception ex) {
                    Tools.logException(Configurator.class, ex, "Could not save goback configuration: " + goBackConfiguration);
                }
                DownloadConfiguration downloadConfiguration = null;
                try {
                    downloadConfiguration = serverConfiguration.getDownloadConfiguration();
                    log.debug("DownloadConfiguration: " + downloadConfiguration);
                    StringWriter outputWriter = new StringWriter();
                    BeanWriter beanWriter = new BeanWriter(outputWriter);
                    AppXMLIntrospector appXMLIntrospector = new AppXMLIntrospector();
                    appXMLIntrospector.getConfiguration().setAttributesForPrimitives(true);
                    beanWriter.setXMLIntrospector(appXMLIntrospector);
                    beanWriter.enablePrettyPrint();
                    beanWriter.write("downloadConfiguration", downloadConfiguration);
                    buffer.append(outputWriter.toString());
                } catch (Exception ex) {
                    Tools.logException(Configurator.class, ex, "Could not save download configuration: " + downloadConfiguration);
                }
                buffer.append("</").append(TAG_CONFIGURATION).append(">\n");
            }
            printWriter.print(buffer.toString());
            printWriter.close();
        } catch (Exception ex) {
            Tools.logException(Configurator.class, ex);
        }
    }
}
