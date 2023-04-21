package pedro.mda.config;

import pedro.system.*;
import pedro.util.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;

public class MenuFeatureConfigurationParser {

    private PedroXMLParsingUtility parsingUtility;

    private FileMenuConfiguration fileMenuConfiguration;

    private EditMenuConfiguration editMenuConfiguration;

    private OptionsMenuConfiguration optionsMenuConfiguration;

    private ViewMenuConfiguration viewMenuConfiguration;

    private HelpMenuConfiguration helpMenuConfiguration;

    private boolean includeWindowMenu;

    private ArrayList customMenus;

    public MenuFeatureConfigurationParser() {
        customMenus = new ArrayList();
        parsingUtility = new PedroXMLParsingUtility();
        includeWindowMenu = true;
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

    public HelpMenuConfiguration getHelpMenuConfiguration() {
        return helpMenuConfiguration;
    }

    public ArrayList getCustomMenus() {
        CustomMenuConfiguration[] customMenuConfigurations = (CustomMenuConfiguration[]) customMenus.toArray(new CustomMenuConfiguration[0]);
        return customMenus;
    }

    public boolean includeWindowMenu() {
        return includeWindowMenu;
    }

    public void parse(Element menuFeaturesElement) throws Exception {
        Node currentChild = menuFeaturesElement.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                if (attributeName.equals("existing_menus")) {
                    parseExistingMenus(currentElement);
                } else if (attributeName.equals("custom_menu")) {
                    parseCustomMenu(currentElement);
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    private void parseExistingMenus(Element existingMenusElement) throws Exception {
        Node currentChild = existingMenusElement.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                if (attributeName.equals("file_menu")) {
                    parseFileMenu(currentElement);
                } else if (attributeName.equals("edit_menu")) {
                    parseEditMenu(currentElement);
                } else if (attributeName.equals("view_menu")) {
                    parseViewMenu(currentElement);
                } else if (attributeName.equals("options_menu")) {
                    parseOptionsMenu(currentElement);
                } else if (attributeName.equals("help_menu")) {
                    parseHelpMenu(currentElement);
                } else if (attributeName.equals("show_windowsMenu")) {
                    String fieldValue = parsingUtility.getFieldValue(currentElement);
                    includeWindowMenu = parsingUtility.interpretBooleanValue(fieldValue);
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    private void parseFileMenu(Element fileMenuElement) throws Exception {
        fileMenuConfiguration = new FileMenuConfiguration();
        Node currentChild = fileMenuElement.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                String fieldValue = parsingUtility.getFieldValue(currentElement);
                boolean booleanFieldValue = parsingUtility.interpretBooleanValue(fieldValue);
                if (attributeName.equals("show_new_file")) {
                    fileMenuConfiguration.setShowNewFile(booleanFieldValue);
                } else if (attributeName.equals("show_favourites")) {
                    fileMenuConfiguration.setShowFavourites(booleanFieldValue);
                } else if (attributeName.equals("show_spreadsheet_options")) {
                    fileMenuConfiguration.setShowSpreadSheetOptions(booleanFieldValue);
                } else if (attributeName.equals("show_open_file")) {
                    fileMenuConfiguration.setShowOpenFile(booleanFieldValue);
                } else if (attributeName.equals("show_save_file")) {
                    fileMenuConfiguration.setShowSaveFile(booleanFieldValue);
                } else if (attributeName.equals("show_saveAs_file")) {
                    fileMenuConfiguration.setShowSaveAsFile(booleanFieldValue);
                } else if (attributeName.equals("show_close")) {
                    fileMenuConfiguration.setShowClose(booleanFieldValue);
                } else if (attributeName.equals("show_import_records")) {
                    fileMenuConfiguration.setShowImportRecords(booleanFieldValue);
                } else if (attributeName.equals("show_import_from_xml")) {
                    fileMenuConfiguration.setShowImportFromXML(booleanFieldValue);
                } else if (attributeName.equals("show_export_final_submission_format")) {
                    fileMenuConfiguration.setShowExportFinalFormat(booleanFieldValue);
                } else if (attributeName.equals("show_templates")) {
                    fileMenuConfiguration.setShowTemplates(booleanFieldValue);
                } else if (attributeName.equals("show_load_template")) {
                    fileMenuConfiguration.setShowLoadTemplate(booleanFieldValue);
                } else if (attributeName.equals("show_save_template")) {
                    fileMenuConfiguration.setShowSaveTemplate(booleanFieldValue);
                } else if (attributeName.equals("show_exit")) {
                    fileMenuConfiguration.setShowExit(booleanFieldValue);
                } else if (attributeName.equals("plugin")) {
                    PluginConfiguration pluginConfiguration = parsePluginConfiguration(currentElement);
                    fileMenuConfiguration.addPluginConfiguration(pluginConfiguration);
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    private void parseEditMenu(Element editMenuElement) throws Exception {
        editMenuConfiguration = new EditMenuConfiguration();
        Node currentChild = editMenuElement.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                String fieldValue = parsingUtility.getFieldValue(currentElement);
                boolean booleanFieldValue = parsingUtility.interpretBooleanValue(fieldValue);
                if (attributeName.equals("show_copy")) {
                    editMenuConfiguration.setShowCopy(booleanFieldValue);
                } else if (attributeName.equals("show_paste")) {
                    editMenuConfiguration.setShowPaste(booleanFieldValue);
                } else if (attributeName.equals("plugin")) {
                    PluginConfiguration pluginConfiguration = parsePluginConfiguration(currentElement);
                    editMenuConfiguration.addPluginConfiguration(pluginConfiguration);
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    private void parseViewMenu(Element viewMenuElement) throws Exception {
        viewMenuConfiguration = new ViewMenuConfiguration();
        Node currentChild = viewMenuElement.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                String fieldValue = parsingUtility.getFieldValue(currentElement);
                boolean booleanFieldValue = parsingUtility.interpretBooleanValue(fieldValue);
                if (attributeName.equals("show_errors")) {
                    viewMenuConfiguration.setShowErrors(booleanFieldValue);
                } else if (attributeName.equals("show_dependencies")) {
                    viewMenuConfiguration.setShowDependencies(booleanFieldValue);
                } else if (attributeName.equals("show_changes")) {
                    viewMenuConfiguration.setShowChanges(booleanFieldValue);
                } else if (attributeName.equals("show_search")) {
                    viewMenuConfiguration.setShowSearch(booleanFieldValue);
                } else if (attributeName.equals("show_clear")) {
                    viewMenuConfiguration.setShowClear(booleanFieldValue);
                } else if (attributeName.equals("plugin")) {
                    PluginConfiguration pluginConfiguration = parsePluginConfiguration(currentElement);
                    viewMenuConfiguration.addPluginConfiguration(pluginConfiguration);
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    private void parseOptionsMenu(Element optionsMenuElement) throws Exception {
        optionsMenuConfiguration = new OptionsMenuConfiguration();
        Node currentChild = optionsMenuElement.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                String fieldValue = parsingUtility.getFieldValue(currentElement);
                boolean booleanFieldValue = parsingUtility.interpretBooleanValue(fieldValue);
                if (attributeName.equals("show_alerts")) {
                    optionsMenuConfiguration.setShowAlerts(booleanFieldValue);
                } else if (attributeName.equals("show_describe_document")) {
                    optionsMenuConfiguration.setShowDescribeDocument(booleanFieldValue);
                } else if (attributeName.equals("plugin")) {
                    PluginConfiguration pluginConfiguration = parsePluginConfiguration(currentElement);
                    optionsMenuConfiguration.addPluginConfiguration(pluginConfiguration);
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    private void parseHelpMenu(Element helpMenuElement) throws Exception {
        helpMenuConfiguration = new HelpMenuConfiguration();
        Node currentChild = helpMenuElement.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                String fieldValue = parsingUtility.getFieldValue(currentElement);
                boolean booleanFieldValue = parsingUtility.interpretBooleanValue(fieldValue);
                if (attributeName.equals("show_about")) {
                    helpMenuConfiguration.setShowAbout(booleanFieldValue);
                } else if (attributeName.equals("show_schema_information")) {
                    helpMenuConfiguration.setShowSchemaInformation(booleanFieldValue);
                } else if (attributeName.equals("show_context_help")) {
                    helpMenuConfiguration.setShowContextHelp(booleanFieldValue);
                } else if (attributeName.equals("help_document")) {
                    HelpDocument helpDocument = parseHelpDocument(currentElement);
                    helpMenuConfiguration.addHelpDocument(helpDocument);
                } else if (attributeName.equals("plugin")) {
                    PluginConfiguration pluginConfiguration = parsePluginConfiguration(currentElement);
                    helpMenuConfiguration.addPluginConfiguration(pluginConfiguration);
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    private HelpDocument parseHelpDocument(Element helpDocumentElement) throws Exception {
        String label = PedroResources.EMPTY_STRING;
        String link = PedroResources.EMPTY_STRING;
        Node currentChild = helpDocumentElement.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                String fieldValue = parsingUtility.getFieldValue(currentElement);
                if (attributeName.equals("label")) {
                    label = fieldValue;
                } else if (attributeName.equals("link")) {
                    link = fieldValue;
                }
            }
            currentChild = currentChild.getNextSibling();
        }
        HelpDocument helpDocument = new HelpDocument(label, link);
        return helpDocument;
    }

    private void parseCustomMenu(Element customMenuElement) throws Exception {
        CustomMenuConfiguration customMenuConfiguration = new CustomMenuConfiguration();
        Node currentChild = customMenuElement.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                String fieldValue = parsingUtility.getFieldValue(currentElement);
                boolean booleanFieldValue = parsingUtility.interpretBooleanValue(fieldValue);
                if (attributeName.equals("menu_name")) {
                    customMenuConfiguration.setName(fieldValue);
                } else if (attributeName.equals("menu_position")) {
                    int position = parsingUtility.interpretIntegerValue(fieldValue);
                    customMenuConfiguration.setPosition(position);
                } else if (attributeName.equals("tool_tip")) {
                    customMenuConfiguration.setToolTip(fieldValue);
                } else if (attributeName.equals("help_link")) {
                    customMenuConfiguration.setHelpLink(fieldValue);
                } else if (attributeName.equals("plugin")) {
                    PluginConfiguration pluginConfiguration = parsePluginConfiguration(currentElement);
                    customMenuConfiguration.addPluginConfiguration(pluginConfiguration);
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    private PluginConfiguration parsePluginConfiguration(Element currentElement) throws Exception {
        PluginConfigurationParser pluginConfigurationParser = new PluginConfigurationParser();
        return pluginConfigurationParser.parse(currentElement);
    }
}
