package edu.isi.div2.metadesk.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * Class that represents the configuration for MetaDesk. It initializes itself
 * with the details in MetaDesk.xml and when saved, stores them back to
 * MetaDesk.xml.
 * 
 * Current Supported Tags in metadesk.xml
 * 
 * <MetaDesk><Model><DefaultModelURI/><AutoSaveTime/><UserProfileModelURI/>
 * </Model> </MetaDesk>
 * 
 * @author Sameer Maggon (maggon@isi.edu)
 * @version $Id: MetaDeskConfiguration.java,v 1.1 2005/05/24 16:36:41 maggon Exp $
 */
public final class MetaDeskConfiguration {

    private static String emptyXmlFileTags = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<MetaDesk>" + "<Model>" + "<DefaultModelURI/>" + "<AutoSaveTime/>" + "<UserProfileModelURI/>" + "</Model>" + "<PersonalNamespace/>" + "</MetaDesk>";

    /**
	 * Default constructor. Reads the details from the metadesk.xml and
	 * initializes itself.
	 */
    public MetaDeskConfiguration() {
        this(DEFAULT_CONFIGURATION_FILE);
    }

    public MetaDeskConfiguration(String configFile) {
        configurationFile = configFile;
        readConfigFile();
        loadResourceBundles();
    }

    /** Reloads the data in this object from the configuration file . */
    public void reloadConfiguration() {
        readConfigFile();
    }

    /**
	 * Saves the current configuration of the object into the configuration
	 * file.
	 */
    public void saveConfiguration() {
        Document docConfigFile = null;
        try {
            File xmlFile = new File(configurationFile);
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder domParser = factory.newDocumentBuilder();
                docConfigFile = domParser.parse(xmlFile);
            } catch (FactoryConfigurationError fce) {
                fce.printStackTrace();
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (SAXException saxe) {
                saxe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == docConfigFile) return;
        Element elementRoot = docConfigFile.getDocumentElement();
        if (personalNamespace == null || "".equals(personalNamespace)) personalNamespace = DEFAULT_PERSONAL_NAMESPACE;
        NodeList nodeList = elementRoot.getElementsByTagName(PERSONAL_NAMESPACE_TAG);
        Element personameNamespaceElement = (Element) nodeList.item(0);
        if (personameNamespaceElement == null) {
            personameNamespaceElement = docConfigFile.createElement(PERSONAL_NAMESPACE_TAG);
            Text contentText = docConfigFile.createTextNode(personalNamespace);
            personameNamespaceElement.appendChild(contentText);
            elementRoot.appendChild(personameNamespaceElement);
        } else {
            Text contentNode = (Text) personameNamespaceElement.getFirstChild();
            if (contentNode == null) {
                contentNode = docConfigFile.createTextNode(personalNamespace);
                personameNamespaceElement.appendChild(contentNode);
            } else contentNode.setData(personalNamespace);
        }
        nodeList = elementRoot.getElementsByTagName(MODEL_TAG);
        Element modelTagElement = (Element) nodeList.item(0);
        NodeList nodeListMerger = modelTagElement.getElementsByTagName(DEFAULT_MODEL_URI_TAG);
        Element mergerElement = (Element) nodeListMerger.item(0);
        Text contentNode = (Text) mergerElement.getFirstChild();
        if (contentNode == null) {
            if (!isModelWebDAV || !modelURL.toLowerCase().startsWith("http")) contentNode = docConfigFile.createTextNode(modelFile); else contentNode = docConfigFile.createTextNode(modelURL);
            mergerElement.appendChild(contentNode);
        } else {
            if (!isModelWebDAV || !modelURL.toLowerCase().startsWith("http")) contentNode.setData(modelFile); else contentNode.setData(modelURL);
        }
        nodeListMerger = modelTagElement.getElementsByTagName(USER_PROFILE_URI_TAG);
        mergerElement = (Element) nodeListMerger.item(0);
        if (mergerElement == null) {
            Element userProfileModelElement = docConfigFile.createElement(USER_PROFILE_URI_TAG);
            Text contentText = docConfigFile.createTextNode(userProfileModelFile);
            userProfileModelElement.appendChild(contentText);
            modelTagElement.appendChild(userProfileModelElement);
        } else {
            contentNode = (Text) mergerElement.getFirstChild();
            if (contentNode == null) {
                contentNode = docConfigFile.createTextNode(userProfileModelFile);
                mergerElement.appendChild(contentNode);
            } else contentNode.setData(userProfileModelFile);
        }
        nodeListMerger = null;
        nodeListMerger = modelTagElement.getElementsByTagName(AUTO_SAVE_TIME_TAG);
        if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
            Element autoSaveElement = docConfigFile.createElement(AUTO_SAVE_TIME_TAG);
            Text contentText = docConfigFile.createTextNode(String.valueOf(autoSaveTime));
            autoSaveElement.appendChild(contentText);
            modelTagElement.appendChild(autoSaveElement);
        } else {
            mergerElement = (Element) nodeListMerger.item(0);
            contentNode = (Text) mergerElement.getFirstChild();
            if (contentNode == null) {
                contentNode = docConfigFile.createTextNode(String.valueOf(autoSaveTime));
                mergerElement.appendChild(contentNode);
            } else {
                contentNode.setData(String.valueOf(autoSaveTime));
            }
        }
        if (username != null) {
            nodeListMerger = null;
            nodeListMerger = modelTagElement.getElementsByTagName(USERNAME_TAG);
            if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
                Element usernameElement = docConfigFile.createElement(USERNAME_TAG);
                Text contentText = docConfigFile.createTextNode(username);
                usernameElement.appendChild(contentText);
                modelTagElement.appendChild(usernameElement);
            } else {
                mergerElement = (Element) nodeListMerger.item(0);
                contentNode = (Text) mergerElement.getFirstChild();
                if (contentNode == null) {
                    contentNode = docConfigFile.createTextNode(username);
                    mergerElement.appendChild(contentNode);
                } else {
                    contentNode.setData(username);
                }
            }
        }
        if (password != null) {
            nodeListMerger = null;
            nodeListMerger = modelTagElement.getElementsByTagName(PASSWORD_TAG);
            if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
                Element passwordElement = docConfigFile.createElement(PASSWORD_TAG);
                Text contentText = docConfigFile.createTextNode(password);
                passwordElement.appendChild(contentText);
                modelTagElement.appendChild(passwordElement);
            } else {
                mergerElement = (Element) nodeListMerger.item(0);
                contentNode = (Text) mergerElement.getFirstChild();
                if (contentNode == null) {
                    contentNode = docConfigFile.createTextNode(password);
                    mergerElement.appendChild(contentNode);
                } else {
                    contentNode.setData(password);
                }
            }
        }
        nodeListMerger = null;
        nodeListMerger = modelTagElement.getElementsByTagName(METADESK_IM_SERVER_NAME_TAG);
        if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
            Element usernameElement = docConfigFile.createElement(METADESK_IM_SERVER_NAME_TAG);
            Text contentText = docConfigFile.createTextNode(metadeskIMServername);
            usernameElement.appendChild(contentText);
            modelTagElement.appendChild(usernameElement);
        } else {
            mergerElement = (Element) nodeListMerger.item(0);
            contentNode = (Text) mergerElement.getFirstChild();
            if (contentNode == null) {
                contentNode = docConfigFile.createTextNode(metadeskIMServername);
                mergerElement.appendChild(contentNode);
            } else {
                contentNode.setData(metadeskIMServername);
            }
        }
        nodeListMerger = null;
        nodeListMerger = modelTagElement.getElementsByTagName(METADESK_IM_SERVER_PORT_TAG);
        if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
            Element usernameElement = docConfigFile.createElement(METADESK_IM_SERVER_PORT_TAG);
            Text contentText = docConfigFile.createTextNode(String.valueOf(metadeskIMServerPort));
            usernameElement.appendChild(contentText);
            modelTagElement.appendChild(usernameElement);
        } else {
            mergerElement = (Element) nodeListMerger.item(0);
            contentNode = (Text) mergerElement.getFirstChild();
            if (contentNode == null) {
                contentNode = docConfigFile.createTextNode(String.valueOf(metadeskIMServerPort));
                mergerElement.appendChild(contentNode);
            } else {
                contentNode.setData(String.valueOf(metadeskIMServerPort));
            }
        }
        if (isModelWebDAV) {
            nodeListMerger = modelTagElement.getElementsByTagName(WEBDAV_TAG);
            if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
                Element autoSaveElement = docConfigFile.createElement(WEBDAV_TAG);
                modelTagElement.appendChild(autoSaveElement);
            }
        } else {
            nodeListMerger = modelTagElement.getElementsByTagName(WEBDAV_TAG);
            if (null != nodeListMerger && nodeListMerger.getLength() != 0) {
                mergerElement = (Element) nodeListMerger.item(0);
                modelTagElement.removeChild(mergerElement);
            }
        }
        writeDOMtoXMLFile(docConfigFile, configurationFile);
    }

    public ResourceBundle getResourceBundles() {
        return resourceBundle;
    }

    public int getAutoSaveTime() {
        return autoSaveTime;
    }

    public void setAutoSaveTime(int minutes) {
        autoSaveTime = minutes;
    }

    public String getMetaDeskIMUsername() {
        return username;
    }

    public void setMetaDeskIMUsername(String username) {
        this.username = username;
    }

    public String getMetaDeskIMPassword() {
        return password;
    }

    public void setMetaDeskIMPassword(String password) {
        this.password = password;
    }

    public String getMetaDeskServerName() {
        return metadeskIMServername;
    }

    public void setMetaDeskServerName(String hostname) {
        this.metadeskIMServername = hostname;
    }

    public int getMetaDeskServerPort() {
        return metadeskIMServerPort;
    }

    public void setMetaDeskServerPort(int port) {
        this.metadeskIMServerPort = port;
    }

    public String getModelURL() {
        return modelURL;
    }

    public String getModelFileName() {
        return modelFile;
    }

    public void setModelFileName(String name) {
        modelFile = name;
    }

    public void setModelURL(String modelURL) {
        this.modelURL = modelURL;
    }

    public String getUserProfileModelURL() {
        return userProfileModelURL;
    }

    public void setUserProfileModelURL(String modelURL) {
        userProfileModelURL = modelURL;
    }

    public boolean getIsModelWebDAV() {
        return isModelWebDAV;
    }

    public void setIsModelWebDAV(boolean flag) {
        isModelWebDAV = flag;
    }

    public void setPersonalNamespace(String personalNamespace) {
        this.personalNamespace = personalNamespace;
    }

    public String getPersonalNamespace() {
        return personalNamespace;
    }

    public void writeDOMtoXMLFile(Document doc, String targetFile) {
        try {
            Source source = new DOMSource(doc);
            File file = new File(targetFile);
            Result result = new StreamResult(file);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
    }

    private void readConfigFile() {
        Document docConfigFile = null;
        try {
            File xmlFile = new File(configurationFile);
            if (!xmlFile.exists()) {
                xmlFile.createNewFile();
                FileWriter out = new FileWriter(xmlFile);
                out.write(emptyXmlFileTags);
                out.close();
            }
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder domParser = factory.newDocumentBuilder();
                docConfigFile = domParser.parse(xmlFile);
            } catch (FactoryConfigurationError fce) {
                fce.printStackTrace();
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
            } catch (FileNotFoundException fnfe) {
                System.err.println("FileNotFoundException: " + fnfe.getMessage());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (SAXException saxe) {
                saxe.printStackTrace();
            }
            if (docConfigFile == null) return;
            Element elementRoot = docConfigFile.getDocumentElement();
            if (elementRoot == null) return;
            NodeList personalNamespaceList = elementRoot.getElementsByTagName(PERSONAL_NAMESPACE_TAG);
            if (null == personalNamespaceList || personalNamespaceList.getLength() == 0) {
                personalNamespace = DEFAULT_PERSONAL_NAMESPACE;
            } else {
                Element mergerElement = (Element) personalNamespaceList.item(0);
                Text contentNode = (Text) mergerElement.getFirstChild();
                if (contentNode == null) {
                    personalNamespace = DEFAULT_PERSONAL_NAMESPACE;
                } else {
                    personalNamespace = contentNode.getData().trim();
                }
            }
            NodeList nodeList = elementRoot.getElementsByTagName(MODEL_TAG);
            Element modelTagElement = (Element) nodeList.item(0);
            NodeList nodeListMerger = modelTagElement.getElementsByTagName(DEFAULT_MODEL_URI_TAG);
            if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
                modelFile = getDefaultModelFile(DEFAULT_MODEL_FILE_NAME);
                modelURL = "file:///" + modelFile;
            } else {
                Element mergerElement = (Element) nodeListMerger.item(0);
                Text contentNode = (Text) mergerElement.getFirstChild();
                if (contentNode == null) {
                    modelFile = getDefaultModelFile(DEFAULT_MODEL_FILE_NAME);
                    modelURL = "file:///" + modelFile;
                } else {
                    modelURL = contentNode.getData().trim();
                    if (!modelURL.toLowerCase().startsWith("http:")) {
                        modelURL = "file:///" + contentNode.getData().trim();
                        modelFile = contentNode.getData().trim();
                    }
                }
            }
            nodeListMerger = modelTagElement.getElementsByTagName(WEBDAV_TAG);
            if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
                isModelWebDAV = false;
            } else {
                isModelWebDAV = true;
            }
            nodeListMerger = modelTagElement.getElementsByTagName(USER_PROFILE_URI_TAG);
            if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
                userProfileModelFile = getDefaultModelFile(DEFAULT_USER_PROFILE_MODEL_FILE_NAME);
                userProfileModelURL = "file:///" + userProfileModelFile;
            } else {
                Element mergerElement = (Element) nodeListMerger.item(0);
                Text contentNode = (Text) mergerElement.getFirstChild();
                if (contentNode == null) {
                    userProfileModelFile = getDefaultModelFile(DEFAULT_USER_PROFILE_MODEL_FILE_NAME);
                    userProfileModelURL = "file:///" + userProfileModelFile;
                } else {
                    userProfileModelURL = contentNode.getData().trim();
                    if (!userProfileModelURL.toLowerCase().startsWith("http:")) {
                        userProfileModelURL = "file:///" + contentNode.getData().trim();
                        userProfileModelFile = contentNode.getData().trim();
                    }
                }
            }
            nodeListMerger = null;
            nodeListMerger = modelTagElement.getElementsByTagName(AUTO_SAVE_TIME_TAG);
            if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
                autoSaveTime = DEFAULT_AUTOSAVE_TIME_MINUTES;
            } else {
                Element mergerElement = (Element) nodeListMerger.item(0);
                Text contentNode = (Text) mergerElement.getFirstChild();
                if (contentNode == null) {
                    autoSaveTime = DEFAULT_AUTOSAVE_TIME_MINUTES;
                } else {
                    String temp = contentNode.getData().trim();
                    if (null == temp || "".equals(temp)) {
                        autoSaveTime = DEFAULT_AUTOSAVE_TIME_MINUTES;
                    } else {
                        try {
                            autoSaveTime = Integer.parseInt(temp);
                        } catch (NumberFormatException nfe) {
                            autoSaveTime = DEFAULT_AUTOSAVE_TIME_MINUTES;
                        }
                    }
                }
            }
            nodeListMerger = null;
            nodeListMerger = modelTagElement.getElementsByTagName(USERNAME_TAG);
            if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
                username = null;
            } else {
                Element mergerElement = (Element) nodeListMerger.item(0);
                Text contentNode = (Text) mergerElement.getFirstChild();
                if (contentNode == null) {
                    username = null;
                } else {
                    String temp = contentNode.getData().trim();
                    if (null == temp || "".equals(temp)) {
                        username = null;
                    } else {
                        username = temp;
                    }
                }
            }
            nodeListMerger = null;
            nodeListMerger = modelTagElement.getElementsByTagName(PASSWORD_TAG);
            if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
                password = null;
            } else {
                Element mergerElement = (Element) nodeListMerger.item(0);
                Text contentNode = (Text) mergerElement.getFirstChild();
                if (contentNode == null) {
                    password = null;
                } else {
                    String temp = contentNode.getData().trim();
                    if (null == temp || "".equals(temp)) {
                        password = null;
                    } else {
                        password = temp;
                    }
                }
            }
            nodeListMerger = null;
            nodeListMerger = modelTagElement.getElementsByTagName(METADESK_IM_SERVER_NAME_TAG);
            if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
                metadeskIMServername = DEFAULT_METADESK_IM_SERVER_NAME;
            } else {
                Element mergerElement = (Element) nodeListMerger.item(0);
                Text contentNode = (Text) mergerElement.getFirstChild();
                if (contentNode == null) {
                    metadeskIMServername = DEFAULT_METADESK_IM_SERVER_NAME;
                } else {
                    String temp = contentNode.getData().trim();
                    if (null == temp || "".equals(temp)) {
                        metadeskIMServername = DEFAULT_METADESK_IM_SERVER_NAME;
                        ;
                    } else {
                        metadeskIMServername = temp;
                    }
                }
            }
            nodeListMerger = null;
            nodeListMerger = modelTagElement.getElementsByTagName(METADESK_IM_SERVER_PORT_TAG);
            if (null == nodeListMerger || nodeListMerger.getLength() == 0) {
                metadeskIMServerPort = DEFAULT_METADESK_IM_SERVER_PORT;
            } else {
                Element mergerElement = (Element) nodeListMerger.item(0);
                Text contentNode = (Text) mergerElement.getFirstChild();
                if (contentNode == null) {
                    metadeskIMServerPort = DEFAULT_METADESK_IM_SERVER_PORT;
                } else {
                    String temp = contentNode.getData().trim();
                    if (null == temp || "".equals(temp)) {
                        metadeskIMServerPort = DEFAULT_METADESK_IM_SERVER_PORT;
                    } else {
                        try {
                            metadeskIMServerPort = Integer.parseInt(temp);
                        } catch (Exception e) {
                            metadeskIMServerPort = DEFAULT_METADESK_IM_SERVER_PORT;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getDefaultModelFile(String fileName) {
        String filename = System.getProperty("user.dir") + System.getProperty("file.separator") + fileName;
        File file = new File(filename);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) return null;
            } catch (IOException ioe) {
                return null;
            }
        }
        return filename;
    }

    private void loadResourceBundles() {
        currentLocale = Locale.getDefault();
        if (null == currentLocale) currentLocale = new Locale("en", "US");
        resourceBundle = ResourceBundle.getBundle("edu.isi.div2.metadesk.resources.bundles.hikemessages", currentLocale);
    }

    /**
	 * Time that governs the AutoSave module. It is in minutes. When this time
	 * is ellapsed, MetaDesk saves the Model files.
	 */
    private int autoSaveTime = 4;

    /** Default URL of the model that the user uses throughout the session. */
    private String modelURL;

    /** Filename of the default model. */
    private String modelFile;

    /**
	 * Default URL of the profile model that the user uses throughout the
	 * session.
	 */
    private String userProfileModelURL;

    private String userProfileModelFile;

    /** Specifies whether the default model is a WebDAV model or not */
    private boolean isModelWebDAV = false;

    private String personalNamespace;

    private ResourceBundle resourceBundle;

    private Locale currentLocale;

    private String configurationFile;

    private String username;

    private String password;

    private int metadeskIMServerPort;

    private String metadeskIMServername;

    private static final String DEFAULT_CONFIGURATION_FILE = "metadesk.xml";

    private static final int DEFAULT_AUTOSAVE_TIME_MINUTES = 5;

    private static final int DEFAULT_MAX_RECENT_FILES = 10;

    private static final String DEFAULT_MODEL_FILE_NAME = "defaultModel.rdf";

    private static final String DEFAULT_PERSONAL_NAMESPACE = "http://www.isi.edu/~personal/metadesk#";

    private static final String DEFAULT_USER_PROFILE_MODEL_FILE_NAME = "defaultUserProfileModel.rdf";

    private static final String PERSONAL_NAMESPACE_TAG = "PersonalNamespace";

    private static final String MODEL_TAG = "Model";

    private static final String AUTO_SAVE_TIME_TAG = "AutoSaveTime";

    private static final String USERNAME_TAG = "Username";

    private static final String PASSWORD_TAG = "Password";

    private static final String METADESK_IM_SERVER_NAME_TAG = "MetaDeskIMServerName";

    private static final String METADESK_IM_SERVER_PORT_TAG = "MetaDeskIMServerPort";

    private static final String DEFAULT_METADESK_IM_SERVER_NAME = "tiger.isi.edu";

    private static final int DEFAULT_METADESK_IM_SERVER_PORT = 7677;

    private static final String DEFAULT_MODEL_URI_TAG = "DefaultModelURI";

    private static final String USER_PROFILE_URI_TAG = "UserProfileModelURI";

    private static final String WEBDAV_TAG = "Webdav";
}
