package org.xmlportletfactory;

import org.xmlportletfactory.exceptions.NoParentOrCurrentPathFound;
import org.xmlportletfactory.exceptions.NoResourcesFoundException;
import org.xmlportletfactory.gui.MainWindow;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlportletfactory.utils.CopyUtil;
import org.xmlportletfactory.utils.MetodosComunes;
import org.xmlportletfactory.utils.XMLUtils;
import org.xmlportletfactory.utils.VelocityFileNameFilter;
import org.xmlportletfactory.xml.xmlportletfactory.DefinitionDocument;
import org.xmlportletfactory.xml.xmlportletfactory.DefinitionDocument.Definition;
import org.xmlportletfactory.xml.xmlportletfactory.DefinitionDocument.Definition.Applications;
import org.xmlportletfactory.xml.xmlportletfactory.DefinitionDocument.Definition.Applications.Application;
import org.xmlportletfactory.xml.xmlportletfactory.DefinitionDocument.Definition.Applications.Application.DetailFiles;
import org.xmlportletfactory.xml.xmlportletfactory.DefinitionDocument.Definition.Applications.Application.DetailFiles.DetailFile;
import org.xmlportletfactory.xml.xmlportletfactory.DefinitionDocument.Definition.Applications.Application.FileDef.Fields;
import org.xmlportletfactory.xml.xmlportletfactory.DefinitionDocument.Definition.Applications.Application.FileDef.Fields.Field;
import org.xmlportletfactory.xml.xmlportletfactory.DefinitionDocument.Definition.Applications.Application.FileDef.Fields.Field.Type;
import org.xmlportletfactory.xml.xmlportletfactory.DefinitionDocument.Definition.CommonData;

/**
 * @author Jack A. Rider
 *
 */
public class XMLPortletFactory {

    private static String _generadorNombre = "XMLPortletFactory";

    private static String _generadorVersion = "Abril 2012";

    private static String _generadorComentario = "Version Liferay 6.0.6 & 6.1.0";

    private static String _author = "Jack A. Rider";

    private static String _parentPath;

    private static String _currentPath;

    private static String _sdk_version;

    private static String portletPath;

    private static File XMLFileDef;

    private static String XSDDef;

    private static String XMLDocPath;

    private static String XSDDocPath;

    private static Document XMLDoc;

    private static Properties properties;

    public static String PORLETSDIR_KEY = "xmlportletfactory_porletsdir";

    public static String LASTXML_KEY = "xmlportletfactory_lastxml";

    public static String SDK_VERSION = "generate_for_sdk_version";

    public static String FILE_SEP = System.getProperty("file.separator");

    public static String PROPERTIES_PATH = FILE_SEP + "Resources" + FILE_SEP + "cfg" + FILE_SEP + "xmlportletfactory.properties";

    private static XPath xpath;

    public XMLPortletFactory() throws Exception {
        Logger.getLogger(XMLPortletFactory.class.getName()).setLevel(Level.ALL);
        initProperties();
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static void saveProperties() throws IOException {
        properties.store(new FileOutputStream(_currentPath + PROPERTIES_PATH), null);
    }

    public void startConversion(File file) throws Exception {
        try {
            XMLFileDef = file;
            init();
            generate();
        } catch (NoParentOrCurrentPathFound ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (SAXException ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    private void init() throws NoParentOrCurrentPathFound, SAXException, IOException {
        _parentPath = properties.getProperty(PORLETSDIR_KEY);
        _sdk_version = properties.getProperty(SDK_VERSION);
        portletPath = "";
        XSDDef = "xmlportletfactory.xsd";
        XMLDocPath = XMLFileDef.getAbsolutePath();
        XSDDocPath = _currentPath + FILE_SEP + "Resources" + FILE_SEP + "xml" + FILE_SEP + XSDDef;
        XMLUtils.validateAgainstSchema(XMLDocPath, XSDDocPath);
        XMLDoc = XMLUtils.parseXmlFile(XMLDocPath, false);
        xpath = XPathFactory.newInstance().newXPath();
    }

    private void generate() throws Exception {
        try {
            NodeList XMLApplications = (NodeList) xpath.evaluate("/definition/applications/*", XMLDoc, XPathConstants.NODESET);
            for (int i = 0; i < XMLApplications.getLength(); i++) {
                int application = i + 1;
                String XMLApplicationstrClassName = (String) xpath.evaluate("/definition/applications/application[" + (application) + "]/classDef/name", XMLDoc, XPathConstants.STRING);
                System.out.println("Name:" + XMLApplicationstrClassName);
            }
            String XMLApplicationstrName = (String) xpath.evaluate("/definition/commonData/projectName", XMLDoc, XPathConstants.STRING);
            portletPath = _parentPath + FILE_SEP + XMLApplicationstrName + "-portlet";
            generateDirectories();
            String use_templates = "common";
            generateVelocityFiles(use_templates);
            if (_sdk_version.startsWith("6.0")) {
                use_templates = "6.0";
            } else {
                use_templates = "6.1";
            }
            generateVelocityFiles(use_templates);
        } catch (NoResourcesFoundException ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initProperties() throws Exception, NoParentOrCurrentPathFound {
        properties = new Properties();
        try {
            File currentDir = new File(".");
            _currentPath = currentDir.getCanonicalPath();
            properties.load(new FileInputStream(_currentPath + PROPERTIES_PATH));
        } catch (IOException e) {
            System.out.println("Error in properties file");
            throw new Exception(e.getMessage());
        }
    }

    private static void generateDirectories() throws Exception {
        boolean ok = (new File(portletPath)).mkdirs();
        File sourceLocation = new File(_currentPath + FILE_SEP + "Resources" + FILE_SEP + "PortletStructureAndFiles");
        File targetLocation = new File(portletPath);
        CopyUtil.copyDirectoryStructure(sourceLocation, targetLocation);
    }

    private static void generateVelocityFiles(String use_templates) throws NoResourcesFoundException {
        String resourceDir = _currentPath + FILE_SEP + "Resources" + FILE_SEP + "VelocityTemplates_" + use_templates + FILE_SEP + "PortletFiles";
        String[] dirList = new File(resourceDir).list(new VelocityFileNameFilter());
        if (dirList == null) {
            System.out.println("Specified directory (" + resourceDir + ")does not exist or is not a directory.");
            throw new NoResourcesFoundException(resourceDir);
        } else {
            System.out.println(" Found (" + dirList.length + ") entries in folder (" + resourceDir + ")");
            for (int i = 0; i < dirList.length; i++) {
                System.out.println("-------------------------------------------------------------");
                System.out.println("Generate File from Velocity Template (" + dirList[i] + ")");
                generateVelocityFile(use_templates, dirList[i]);
            }
            System.out.println("-------------------------------------------------------------");
        }
    }

    private static void generateVelocityFile(String use_templates, String fileName) {
        try {
            DefinitionDocument definitionDocument = DefinitionDocument.Factory.parse(new File(XMLDocPath));
            Definition definition = definitionDocument.getDefinition();
            CommonData commonData = definition.getCommonData();
            Applications applications = definition.getApplications();
            for (Application application : applications.getApplicationArray()) {
                String applicationClassName = _parentPath + FILE_SEP + commonData.getProjectName();
                processApplication(use_templates, fileName, commonData, application, applicationClassName, applications);
                processDetail(use_templates, fileName, commonData, application, applicationClassName, applications);
            }
        } catch (XmlException ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void processApplication(String use_templates, String fileName, CommonData commonData, Application application, String applicationClassName, Applications applications) {
        try {
            Fields fields = application.getFileDef().getFields();
            Field[] field = fields.getFieldArray();
            String[] fieldNames = new String[field.length];
            boolean[] fieldShow = new boolean[field.length];
            String[] fieldTitles = new String[field.length];
            String[] fieldTypes = new String[field.length];
            String[] field_regexp = new String[field.length];
            for (int i = 0; i < field.length; i++) {
                fieldNames[i] = field[i].getName();
                fieldShow[i] = field[i].getShowFieldInView();
                fieldTitles[i] = XMLUtils.toUnicode(field[i].getTitle());
                fieldTypes[i] = field[i].getType().getDomNode().getChildNodes().item(1).getLocalName();
                if (fieldTypes[i].equalsIgnoreCase("int")) {
                    field_regexp[i] = field[i].getType().getInt().getRegexp();
                }
                if (fieldTypes[i].equalsIgnoreCase("long")) {
                    field_regexp[i] = field[i].getType().getLong().getRegexp();
                }
                if (fieldTypes[i].equalsIgnoreCase("varchar")) {
                    field_regexp[i] = field[i].getType().getVarchar().getRegexp();
                }
                if (fieldTypes[i].equalsIgnoreCase("numeric")) {
                    field_regexp[i] = field[i].getType().getNumeric().getRegexp();
                }
                if (fieldTypes[i].equalsIgnoreCase("float")) {
                    field_regexp[i] = field[i].getType().getFloat().getRegexp();
                }
            }
            Velocity.init();
            VelocityContext vc = new VelocityContext();
            try {
                BufferedReader in = new BufferedReader(new FileReader(XMLFileDef));
                String str;
                String xml_str = "";
                while ((str = in.readLine()) != null) {
                    xml_str = xml_str + str + "\n";
                }
                in.close();
                vc.put("xml_definition", xml_str);
            } catch (IOException e) {
            }
            vc.put("commonData", commonData);
            vc.put("application", application);
            vc.put("applications", applications.getApplicationArray());
            vc.put("fieldNames", fieldNames);
            vc.put("fieldTitles", fieldTitles);
            vc.put("fieldTypes", fieldTypes);
            vc.put("fieldCount", field.length);
            vc.put("date", new java.util.Date());
            vc.put("version", _generadorVersion);
            vc.put("author", _author);
            vc.put("comments", _generadorComentario);
            vc.put("generator", _generadorNombre);
            vc.put("field_regexp", field_regexp);
            vc.put("file_name", application.getFileDef().getName());
            processVelocityTemplate(use_templates, fileName, applicationClassName, vc);
            processValidation(use_templates, fileName, commonData, application, applicationClassName, applications);
        } catch (Exception ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void processVelocityTemplate(String use_templates, String fileName, String applicationClassName, VelocityContext vc) {
        try {
            Template template = null;
            template = Velocity.getTemplate("/Resources/VelocityTemplates_" + use_templates + "/PortletFiles/" + fileName);
            StringWriter sw = new StringWriter();
            template.merge(vc, sw);
            if (vc.get("createName") != null) {
                System.out.println(" --> " + applicationClassName + "-portlet" + vc.get("createPath") + vc.get("createName"));
                boolean ok = (new File(applicationClassName + "-portlet" + vc.get("createPath"))).mkdirs();
                BufferedWriter FicheroSalida;
                FicheroSalida = new BufferedWriter(new FileWriter(applicationClassName + "-portlet" + vc.get("createPath") + vc.get("createName")));
                FicheroSalida.write(sw.toString());
                FicheroSalida.flush();
                FicheroSalida.close();
            }
        } catch (ResourceNotFoundException ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseErrorException ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MethodInvocationException ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XMLPortletFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void processValidation(String use_templates, String fileName, CommonData commonData, Application application, String applicationClassName, Applications applications) {
        Fields fields = application.getFileDef().getFields();
        Field[] field = fields.getFieldArray();
        for (int i = 0; i < field.length; i++) {
            String[] filesValidationList = getVelocityFilesForValidation();
            for (String fileValidation : filesValidationList) {
                if (fileName.trim().equalsIgnoreCase(fileValidation)) {
                    if (field[i].isSetValidation()) {
                        for (Application validationApp : applications.getApplicationArray()) {
                            if (validationApp.getClassDef().getName().equalsIgnoreCase(field[i].getValidation().getClassName())) {
                                processApplication(use_templates, fileName, commonData, validationApp, applicationClassName, applications);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void processDetail(String use_templates, String fileName, CommonData commonData, Application application, String applicationClassName, Applications applications) {
        DetailFiles df = application.getDetailFiles();
        if (df != null) {
            DetailFile[] detailFiles = application.getDetailFiles().getDetailFileArray();
            for (DetailFile detailFile : detailFiles) {
                String[] filesForDetailList = getVelocityFilesForDetail();
                for (String fileForDetail : filesForDetailList) {
                    if (fileName.trim().equalsIgnoreCase(fileForDetail)) {
                        for (Application detailApp : applications.getApplicationArray()) {
                            if (detailApp.getClassDef().getName().equalsIgnoreCase(detailFile.getDetailFileClassName().trim())) {
                                processApplication(use_templates, fileName, commonData, detailApp, applicationClassName, applications);
                            }
                        }
                    }
                }
            }
        }
    }

    private static String[] getVelocityFilesForDetail() {
        String[] files = { "PortletDAO.vm", "PortletDEF.vm", "PortletTBL.vm", "CreateTableMySQL.vm", "BodyTable_JSP.vm", "New_JSP.vm", "Edit_JSP.vm", "Delete_JSP.vm", "save.vm", "update.vm" };
        return files;
    }

    private static String[] getVelocityFilesForValidation() {
        String[] files = { "Portlet_XXXXXXLocalServiceImpl_java.vm", "PortletDEF.vm", "PortletTBL.vm" };
        return files;
    }

    public static void main(String[] args) throws Exception {
        final XMLPortletFactory xmlPortletFactory = new XMLPortletFactory();
        if (args.length == 0) {
            MainWindow mainWindow = new MainWindow(xmlPortletFactory);
            mainWindow.setVisible(true);
            mainWindow.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    try {
                        xmlPortletFactory.saveProperties();
                        System.exit(0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } else {
            properties.setProperty(LASTXML_KEY, args[0]);
            properties.setProperty(PORLETSDIR_KEY, args[1]);
            properties.setProperty(SDK_VERSION, args[2]);
            File file = new File(args[0]);
            xmlPortletFactory.startConversion(file);
        }
    }
}
