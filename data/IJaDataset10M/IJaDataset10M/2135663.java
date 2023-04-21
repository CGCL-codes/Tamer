package pedro.mda.config;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import pedro.system.*;
import pedro.util.*;
import pedro.soa.validation.*;
import pedro.mda.config.PedroConfigurationReader;
import pedro.*;
import pedro.io.*;
import pedro.mda.model.*;
import pedro.desktopDeployment.Workspace;
import pedro.desktopDeployment.WorkspaceFileFinder;
import pedro.mda.schema.*;
import javax.xml.parsers.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;

public class PedroConfigurationReader implements SchemaConceptPropertyManager {

    private String topLevelElementName;

    private String dialogTitle;

    private FileMenuConfiguration fileMenuConfiguration;

    private EditMenuConfiguration editMenuConfiguration;

    private OptionsMenuConfiguration optionsMenuConfiguration;

    private ViewMenuConfiguration viewMenuConfiguration;

    private boolean includeWindowMenu;

    private HelpMenuConfiguration helpMenuConfiguration;

    private ArrayList acceptableSchemas;

    private ArrayList customMenuConfigurations;

    private ArrayList recordConfigurations;

    private ArrayList documentValidationServiceConfigurations;

    private ArrayList compatibleSchemas;

    private String masterSchema;

    private boolean ignoreSchemaLocation;

    private boolean enableClassLoader;

    private SessionManager sessionManager;

    private FileLauncher fileLauncher;

    private PedroXMLParsingUtility parsingUtility;

    private URL resourceDirectory;

    public PedroConfigurationReader() {
        dialogTitle = PedroResources.EMPTY_STRING;
        sessionManager = new SessionManager();
        fileLauncher = new FileLauncher();
        acceptableSchemas = new ArrayList();
        customMenuConfigurations = new ArrayList();
        compatibleSchemas = new ArrayList();
        recordConfigurations = new ArrayList();
        documentValidationServiceConfigurations = new ArrayList();
        enableClassLoader = true;
        ignoreSchemaLocation = false;
        parsingUtility = new PedroXMLParsingUtility();
        includeWindowMenu = true;
    }

    public boolean ignoreSchemaLocation() {
        return ignoreSchemaLocation;
    }

    public FileMenuConfiguration getFileMenuConfiguration() {
        return fileMenuConfiguration;
    }

    public EditMenuConfiguration getEditMenuConfiguration() {
        return editMenuConfiguration;
    }

    public OptionsMenuConfiguration getOptionsMenuConfiguration() {
        return optionsMenuConfiguration;
    }

    public ViewMenuConfiguration getViewMenuConfiguration() {
        return viewMenuConfiguration;
    }

    public boolean includeWindowMenu() {
        return includeWindowMenu;
    }

    public HelpMenuConfiguration getHelpMenuConfiguration() {
        return helpMenuConfiguration;
    }

    public CustomMenuConfiguration[] getCustomMenuConfigurations() {
        CustomMenuConfiguration[] results = (CustomMenuConfiguration[]) customMenuConfigurations.toArray(new CustomMenuConfiguration[0]);
        return results;
    }

    /**
     * provides a lookup method for getting configuration data on a particular
     * field in a record.
     *
     * @param recordName the record tag name
     * @param fieldName  the field tag name.  For Configuration records that describe
     *                   the record-level attributes, fieldName will be "null"
     */
    public SchemaConceptConfiguration getConfigurationRecord(String recordClassName, String fieldName) {
        int numberOfRecordConfigurations = recordConfigurations.size();
        if (fieldName == null) {
            for (int i = 0; i < numberOfRecordConfigurations; i++) {
                RecordConfiguration recordConfiguration = (RecordConfiguration) recordConfigurations.get(i);
                String currentRecordClassName = recordConfiguration.getName();
                if (currentRecordClassName.equals(recordClassName) == true) {
                    return recordConfiguration;
                }
            }
            return null;
        } else {
            AttributeFieldConfiguration attributeFieldConfiguration = getAttributeFieldConfiguration(recordClassName, fieldName);
            if (attributeFieldConfiguration == null) {
                EditFieldConfiguration editFieldConfiguration = getEditFieldConfiguration(recordClassName, fieldName);
                if (editFieldConfiguration == null) {
                    ListFieldConfiguration listFieldConfiguration = getListFieldConfiguration(recordClassName, fieldName);
                    return listFieldConfiguration;
                } else {
                    return editFieldConfiguration;
                }
            } else {
                return attributeFieldConfiguration;
            }
        }
    }

    public AttributeFieldConfiguration getAttributeFieldConfiguration(String recordClassName, String fieldName) {
        int numberOfRecordConfigurations = recordConfigurations.size();
        for (int i = 0; i < numberOfRecordConfigurations; i++) {
            RecordConfiguration recordConfiguration = (RecordConfiguration) recordConfigurations.get(i);
            String currentRecordClassName = recordConfiguration.getName();
            if (currentRecordClassName.equals(recordClassName) == true) {
                AttributeFieldConfiguration[] attributeFieldConfigurations = recordConfiguration.getAttributeFieldConfigurations();
                for (int j = 0; j < attributeFieldConfigurations.length; j++) {
                    String currentListName = attributeFieldConfigurations[j].getName();
                    if (currentListName.equals(fieldName) == true) {
                        return attributeFieldConfigurations[j];
                    }
                }
            }
        }
        return null;
    }

    public EditFieldConfiguration getEditFieldConfiguration(String recordClassName, String fieldName) {
        int numberOfRecordConfigurations = recordConfigurations.size();
        for (int i = 0; i < numberOfRecordConfigurations; i++) {
            RecordConfiguration recordConfiguration = (RecordConfiguration) recordConfigurations.get(i);
            String currentRecordClassName = recordConfiguration.getName();
            if (currentRecordClassName.equals(recordClassName) == true) {
                EditFieldConfiguration[] editFieldConfigurations = recordConfiguration.getEditFieldConfigurations();
                for (int j = 0; j < editFieldConfigurations.length; j++) {
                    String currentListName = editFieldConfigurations[j].getName();
                    if (currentListName.equals(fieldName) == true) {
                        return editFieldConfigurations[j];
                    }
                }
            }
        }
        return null;
    }

    public ListFieldConfiguration getListFieldConfiguration(String recordClassName, String fieldName) {
        int numberOfRecordConfigurations = recordConfigurations.size();
        for (int i = 0; i < numberOfRecordConfigurations; i++) {
            RecordConfiguration recordConfiguration = (RecordConfiguration) recordConfigurations.get(i);
            String currentRecordClassName = recordConfiguration.getName();
            if (currentRecordClassName.equals(recordClassName) == true) {
                ListFieldConfiguration[] listFieldConfigurations = recordConfiguration.getListFieldConfigurations();
                for (int j = 0; j < listFieldConfigurations.length; j++) {
                    String currentListName = listFieldConfigurations[j].getName();
                    if (currentListName.equals(fieldName) == true) {
                        return listFieldConfigurations[j];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the name of the top level element in case that the schema
     * is ambiguous that is has several global elements. Returns null
     * if no top level element name was defined.
     *
     * @return the name of the top level element in case that the schema is ambiguous.
     */
    public String getTopLevelElementName() {
        return topLevelElementName;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public FileLauncher getFileLauncher() {
        return fileLauncher;
    }

    public String[] getCompatibleSchemas() {
        String[] results = (String[]) compatibleSchemas.toArray(new String[0]);
        return results;
    }

    public String getMasterSchema() {
        return masterSchema;
    }

    public RecordConfiguration[] getRecordConfigurations() {
        RecordConfiguration[] results = (RecordConfiguration[]) recordConfigurations.toArray(new RecordConfiguration[0]);
        return results;
    }

    public String getStyleSheet() {
        String styleSheet = PedroResources.getStyleSheet();
        return styleSheet;
    }

    public String getDateFormat() {
        return DateValidator.getDateFormatString(DateValidator.getDateFormat());
    }

    public DocumentValidationServiceConfiguration[] getDocumentValidationServiceConfigurations() {
        DocumentValidationServiceConfiguration[] results = (DocumentValidationServiceConfiguration[]) documentValidationServiceConfigurations.toArray(new DocumentValidationServiceConfiguration[0]);
        return results;
    }

    public void setEnableClassLoader(boolean enableClassLoader) {
        this.enableClassLoader = enableClassLoader;
    }

    public void setIgnoreSchemaLocation(boolean ignoreSchemaLocation) {
        this.ignoreSchemaLocation = ignoreSchemaLocation;
    }

    public void parseMainConfigurationFile(InputStream inputStream, URL documentDirectory, URL resourceDirectory) throws Exception {
        if (inputStream == null) {
            return;
        }
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        Element schemaNode = null;
        Node currentChild = document.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                if (currentElement.getTagName().equals("pedro_configuration_schema") == true) {
                    parsePedroConfigurationSchema(currentElement);
                    break;
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    private void parsePedroConfigurationSchema(Element pedroConfigurationSchema) throws Exception {
        Node currentChild = pedroConfigurationSchema.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                if (attributeName.equals("top_level_element")) {
                    topLevelElementName = PedroXMLParsingUtility.getFieldValue(currentElement);
                } else if (attributeName.equals("date_format")) {
                    parseDateFormat(currentElement);
                } else if (attributeName.equals("dialog_title")) {
                    dialogTitle = PedroXMLParsingUtility.getFieldValue(currentElement);
                } else if (attributeName.equals("accepted_schemas")) {
                    parseAcceptedSchemas(currentElement);
                } else if (attributeName.equals("menu_features")) {
                    MenuFeatureConfigurationParser menuConfigurationParser = new MenuFeatureConfigurationParser();
                    menuConfigurationParser.parse(currentElement);
                    fileMenuConfiguration = menuConfigurationParser.getFileMenuConfiguration();
                    editMenuConfiguration = menuConfigurationParser.getEditMenuConfiguration();
                    optionsMenuConfiguration = menuConfigurationParser.getOptionsMenuConfiguration();
                    viewMenuConfiguration = menuConfigurationParser.getViewMenuConfiguration();
                    helpMenuConfiguration = menuConfigurationParser.getHelpMenuConfiguration();
                    customMenuConfigurations = menuConfigurationParser.getCustomMenus();
                    includeWindowMenu = menuConfigurationParser.includeWindowMenu();
                } else if (attributeName.equals("record")) {
                    RecordConfigurationParser recordConfigurationParser = new RecordConfigurationParser();
                    RecordConfiguration recordConfiguration = recordConfigurationParser.parse(currentElement);
                    recordConfigurations.add(recordConfiguration);
                } else if (attributeName.equals("document_validation_service")) {
                    DocumentValidationServiceConfiguration documentValidationServiceConfiguration = parseDocumentValidationServiceConfiguration(currentElement);
                    documentValidationServiceConfigurations.add(documentValidationServiceConfiguration);
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    private void parseAcceptedSchemas(Element acceptedSchemasElement) throws PedroException {
        Node currentChild = acceptedSchemasElement.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                if (attributeName.equals("master_schema")) {
                    masterSchema = PedroXMLParsingUtility.getFieldValue(currentElement);
                } else if (attributeName.equals("compatible_schema")) {
                    parseCompatibleSchema(currentElement);
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    private void parseCompatibleSchema(Element compatibleSchemaElement) throws PedroException {
        Node currentChild = compatibleSchemaElement.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                if (attributeName.equals("name")) {
                    String name = PedroXMLParsingUtility.getFieldValue(currentElement);
                    compatibleSchemas.add(name);
                    break;
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    public void parseSessionFile(File favouritesFile) throws ParserConfigurationException, PedroException, SAXException, IOException {
        if (favouritesFile == null) {
            return;
        }
        sessionManager.parseSessionFile(favouritesFile);
    }

    public void parseExtensionLauncherRegistry(InputStream extensionLauncherStream) throws ParserConfigurationException, PedroException, SAXException, IOException {
        if (extensionLauncherStream == null) {
            return;
        }
        fileLauncher.parseExtensionLauncherRegistry(extensionLauncherStream);
    }

    /**
	* Set the value of masterSchema.
	* @param masterSchema Value to assign to masterSchema.
	*/
    public void setMasterSchema(String masterSchema) {
        this.masterSchema = masterSchema;
    }

    /**
	* adds a compatible schema that a data file may be stamped with
	*/
    public void addCompatibleSchema(String compatibleSchema) {
        compatibleSchemas.add(compatibleSchema);
    }

    public void addRecordConfiguration(RecordConfiguration recordConfiguration) {
        recordConfigurations.add(recordConfiguration);
    }

    /**
	* Sets the name of the top level element in case that the schema
	* is ambiguous that is has several global elements.
	*
	* @param topLevelElementName the name of the top level element.
	*/
    public void setTopLevelElementName(String topLevelElementName) {
        this.topLevelElementName = topLevelElementName;
    }

    private DocumentValidationServiceConfiguration parseDocumentValidationServiceConfiguration(Element documentValidationServiceElement) throws Exception {
        ServiceConfigurationParser serviceConfigurationParser = new ServiceConfigurationParser();
        DocumentValidationServiceConfiguration documentValidationServiceConfiguration = serviceConfigurationParser.parseDocumentValidationServiceConfiguration(documentValidationServiceElement);
        return documentValidationServiceConfiguration;
    }

    /**
     * method should probably be deprecated.  Style sheet should probably
     * be read by pedro.io.SchemaReader
	 */
    private boolean parseDateFormat(Element dateFormatElement) {
        String dateFormatTag = dateFormatElement.getTagName();
        if (dateFormatTag.equals("dateFormat") == false) {
            return false;
        }
        String fieldValue = parsingUtility.getFieldValue(dateFormatElement);
        setDateFormat(fieldValue);
        return true;
    }

    public void setDateFormat(String dateFormat) {
        if (dateFormat.equals(PedroResources.EMPTY_STRING) == false) {
            if (dateFormat.equals("mmddyyyy") == true) {
                DateValidator.setDateFormat(DateValidator.mmddyyyyFormat);
            } else if (dateFormat.equals("yyyymmdd") == true) {
                DateValidator.setDateFormat(DateValidator.yyyymmddFormat);
            } else {
                DateValidator.setDateFormat(DateValidator.ddmmyyyyFormat);
            }
        }
    }

    /**
     * method should probably be deprecated.  Style sheet should probably
     * be read by pedro.io.SchemaReader
     */
    private boolean parseStyleSheet(Element styleSheetElement) {
        String styleSheetTag = styleSheetElement.getTagName();
        if (styleSheetTag.equals("style_sheet") == false) {
            return false;
        }
        try {
            Node fieldChild = styleSheetElement.getFirstChild();
            if (fieldChild.getNodeType() == Node.TEXT_NODE) {
                Text text = (Text) fieldChild;
                String data = text.getData();
                data = data.trim();
                if (data.equals(PedroResources.NO_ATTRIBUTE_VALUE) == false) {
                    setStyleSheet(data);
                }
            }
        } catch (Exception err) {
            SystemLog.addError(err);
        }
        return true;
    }

    /**
	* sets the style sheet used by the configuration file
	*/
    public void setStyleSheet(String styleSheet) {
        PedroResources.setStyleSheet(styleSheet);
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }
}
