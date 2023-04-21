package pierre.api;

import pierre.model.*;
import pedro.mda.config.*;
import pedro.mda.model.*;
import pedro.util.Parameter;
import java.util.ArrayList;
import java.net.URL;

public class SchemaConceptProperties implements SchemaConceptPropertyManager {

    private RecordConfiguration[] recordConfigurations;

    private FileLauncher fileLauncher;

    public SchemaConceptProperties() {
        fileLauncher = new FileLauncher();
    }

    /**
	* @param schemaConceptProperties the Pedro record model that represents the 
	* collection of schema concept properties used in Pierre
	*/
    public void initialiseRecordConfigurations(RecordModel schemaConceptProperties) {
        ArrayList configurations = new ArrayList();
        ListFieldModel recordListFieldModel = (ListFieldModel) schemaConceptProperties.getField("record");
        ArrayList recordListFieldChildren = recordListFieldModel.getChildren();
        int numberOfRecords = recordListFieldChildren.size();
        for (int i = 0; i < numberOfRecords; i++) {
            RecordModel recordModel = (RecordModel) recordListFieldChildren.get(i);
            RecordConfiguration recordConfiguration = parseRecordConfiguration(recordModel);
            configurations.add(recordConfiguration);
        }
        recordConfigurations = (RecordConfiguration[]) configurations.toArray(new RecordConfiguration[0]);
    }

    private RecordConfiguration parseRecordConfiguration(RecordModel recordModel) {
        RecordConfiguration recordConfiguration = new RecordConfiguration();
        parseSchemaConceptProperties(recordConfiguration, recordModel);
        ListFieldModel attributeFieldListModel = (ListFieldModel) recordModel.getField("attribute_field");
        ArrayList attributeFieldListChildren = attributeFieldListModel.getChildren();
        int numberOfAttributeFields = attributeFieldListChildren.size();
        for (int i = 0; i < numberOfAttributeFields; i++) {
            RecordModel attributeFieldRecordModel = (RecordModel) attributeFieldListChildren.get(i);
            AttributeFieldConfiguration attributeFieldConfiguration = parseAttributeFieldConfiguration(attributeFieldRecordModel);
            recordConfiguration.addAttributeFieldConfiguration(attributeFieldConfiguration);
        }
        ListFieldModel editFieldListModel = (ListFieldModel) recordModel.getField("edit_field");
        ArrayList editFieldListChildren = editFieldListModel.getChildren();
        int numberOfEditFields = editFieldListChildren.size();
        for (int i = 0; i < numberOfEditFields; i++) {
            RecordModel editFieldRecordModel = (RecordModel) editFieldListChildren.get(i);
            EditFieldConfiguration editFieldConfiguration = parseEditFieldConfiguration(editFieldRecordModel);
            recordConfiguration.addEditFieldConfiguration(editFieldConfiguration);
        }
        ListFieldModel listFieldListModel = (ListFieldModel) recordModel.getField("list_field");
        ArrayList listFieldListChildren = listFieldListModel.getChildren();
        int numberOfListFields = listFieldListChildren.size();
        for (int i = 0; i < numberOfListFields; i++) {
            RecordModel listFieldRecordModel = (RecordModel) listFieldListChildren.get(i);
            ListFieldConfiguration listFieldConfiguration = parseListFieldConfiguration(listFieldRecordModel);
            recordConfiguration.addListFieldConfiguration(listFieldConfiguration);
        }
        return recordConfiguration;
    }

    private AttributeFieldConfiguration parseAttributeFieldConfiguration(RecordModel attributeFieldRecordModel) {
        AttributeFieldConfiguration attributeFieldConfiguration = new AttributeFieldConfiguration();
        parseSchemaConceptProperties(attributeFieldConfiguration, attributeFieldRecordModel);
        attributeFieldConfiguration.setUnits(attributeFieldRecordModel.getValue("units"));
        BooleanFieldModel isScrollingTextFieldModel = (BooleanFieldModel) attributeFieldRecordModel.getField("is_scrolling_text_field");
        attributeFieldConfiguration.setScrollingTextField(isScrollingTextFieldModel.getBooleanValue());
        OntologyServiceConfiguration[] ontologyServiceConfigurations = parseOntologyServiceConfigurations(attributeFieldRecordModel);
        for (int i = 0; i < ontologyServiceConfigurations.length; i++) {
            attributeFieldConfiguration.addOntologyServiceConfiguration(ontologyServiceConfigurations[i]);
        }
        FieldValidationServiceConfiguration[] fieldValidationServiceConfigurations = parseFieldValidationServiceConfigurations(attributeFieldRecordModel);
        for (int i = 0; i < fieldValidationServiceConfigurations.length; i++) {
            attributeFieldConfiguration.addFieldValidationServiceConfiguration(fieldValidationServiceConfigurations[i]);
        }
        return attributeFieldConfiguration;
    }

    private ListFieldConfiguration parseListFieldConfiguration(RecordModel listFieldRecordModel) {
        ListFieldConfiguration listFieldConfiguration = new ListFieldConfiguration();
        parseSchemaConceptProperties(listFieldConfiguration, listFieldRecordModel);
        return listFieldConfiguration;
    }

    private EditFieldConfiguration parseEditFieldConfiguration(RecordModel editFieldRecordModel) {
        EditFieldConfiguration editFieldConfiguration = new EditFieldConfiguration();
        parseSchemaConceptProperties(editFieldConfiguration, editFieldRecordModel);
        editFieldConfiguration.setUnits(editFieldRecordModel.getValue("units"));
        BooleanFieldModel isScrollingTextFieldModel = (BooleanFieldModel) editFieldRecordModel.getField("is_scrolling_text_field");
        editFieldConfiguration.setScrollingTextField(isScrollingTextFieldModel.getBooleanValue());
        BooleanFieldModel isDisplayNameComponentFieldModel = (BooleanFieldModel) editFieldRecordModel.getField("is_display_name_component");
        editFieldConfiguration.setDisplayNameComponent(isDisplayNameComponentFieldModel.getBooleanValue());
        OntologyServiceConfiguration[] ontologyServiceConfigurations = parseOntologyServiceConfigurations(editFieldRecordModel);
        for (int i = 0; i < ontologyServiceConfigurations.length; i++) {
            editFieldConfiguration.addOntologyServiceConfiguration(ontologyServiceConfigurations[i]);
        }
        FieldValidationServiceConfiguration[] fieldValidationServiceConfigurations = parseFieldValidationServiceConfigurations(editFieldRecordModel);
        for (int i = 0; i < fieldValidationServiceConfigurations.length; i++) {
            editFieldConfiguration.addFieldValidationServiceConfiguration(fieldValidationServiceConfigurations[i]);
        }
        return editFieldConfiguration;
    }

    private OntologyServiceConfiguration[] parseOntologyServiceConfigurations(RecordModel ontologyServicesProvider) {
        ArrayList ontologyServiceConfigurations = new ArrayList();
        ListFieldModel ontologyServicesFieldModel = (ListFieldModel) ontologyServicesProvider.getField("ontology_service");
        ArrayList ontologyServiceListFieldChildren = ontologyServicesFieldModel.getChildren();
        int numberOfOntologyServices = ontologyServiceListFieldChildren.size();
        for (int i = 0; i < numberOfOntologyServices; i++) {
            RecordModel ontologyServiceRecordModel = (RecordModel) ontologyServiceListFieldChildren.get(i);
            OntologyServiceConfiguration ontologyServiceConfiguration = parseOntologyService(ontologyServiceRecordModel);
            ontologyServiceConfigurations.add(ontologyServiceConfiguration);
        }
        OntologyServiceConfiguration[] results = (OntologyServiceConfiguration[]) ontologyServiceConfigurations.toArray(new OntologyServiceConfiguration[0]);
        return results;
    }

    private OntologyServiceConfiguration parseOntologyService(RecordModel ontologyServiceRecordModel) {
        OntologyServiceConfiguration ontologyServiceConfiguration = new OntologyServiceConfiguration();
        ontologyServiceConfiguration.setName(ontologyServiceRecordModel.getValue("name"));
        ontologyServiceConfiguration.setDescription(ontologyServiceRecordModel.getValue("description"));
        ListFieldModel ontologySourceListFieldModel = (ListFieldModel) ontologyServiceRecordModel.getField("ontology_source");
        ArrayList ontologySourceListFieldChildren = ontologySourceListFieldModel.getChildren();
        if (ontologySourceListFieldChildren.size() > 0) {
            RecordModel ontologySourceRecordModel = (RecordModel) ontologySourceListFieldChildren.get(0);
            OntologySourceConfiguration ontologySourceConfiguration = new OntologySourceConfiguration();
            parseServiceConfiguration(ontologySourceConfiguration, ontologySourceRecordModel);
            ontologyServiceConfiguration.setOntologySourceConfiguration(ontologySourceConfiguration);
        }
        ListFieldModel ontologyViewerListFieldModel = (ListFieldModel) ontologyServiceRecordModel.getField("ontology_viewer");
        ArrayList ontologyViewerListFieldChildren = ontologyViewerListFieldModel.getChildren();
        if (ontologyViewerListFieldChildren.size() > 0) {
            RecordModel ontologyViewerRecordModel = (RecordModel) ontologyViewerListFieldChildren.get(0);
            OntologyViewerConfiguration ontologyViewerConfiguration = new OntologyViewerConfiguration();
            parseServiceConfiguration(ontologyViewerConfiguration, ontologyViewerRecordModel);
            ontologyServiceConfiguration.setOntologyViewerConfiguration(ontologyViewerConfiguration);
        }
        return ontologyServiceConfiguration;
    }

    private OntologySourceConfiguration parseOntologySourceConfiguration(RecordModel ontologySourceRecordModel) {
        OntologySourceConfiguration ontologySourceConfiguration = new OntologySourceConfiguration();
        ontologySourceConfiguration.setClassName(ontologySourceRecordModel.getValue("class_name"));
        ontologySourceConfiguration.setFeatureCode(ontologySourceRecordModel.getValue("feature_code"));
        BooleanFieldModel isPersistentFieldModel = (BooleanFieldModel) ontologySourceRecordModel.getField("is_persistent");
        ontologySourceConfiguration.setPersistent(isPersistentFieldModel.getBooleanValue());
        ArrayList parameters = parseParameters(ontologySourceRecordModel);
        ontologySourceConfiguration.setParameters(parameters);
        return ontologySourceConfiguration;
    }

    private OntologyViewerConfiguration parseOntologyViewerConfiguration(RecordModel ontologyViewerRecordModel) {
        OntologyViewerConfiguration ontologyViewerConfiguration = new OntologyViewerConfiguration();
        ontologyViewerConfiguration.setClassName(ontologyViewerRecordModel.getValue("class_name"));
        ontologyViewerConfiguration.setFeatureCode(ontologyViewerRecordModel.getValue("feature_code"));
        BooleanFieldModel isPersistentFieldModel = (BooleanFieldModel) ontologyViewerRecordModel.getField("is_persistent");
        ontologyViewerConfiguration.setPersistent(isPersistentFieldModel.getBooleanValue());
        ArrayList parameters = parseParameters(ontologyViewerRecordModel);
        ontologyViewerConfiguration.setParameters(parameters);
        return ontologyViewerConfiguration;
    }

    private FieldValidationServiceConfiguration[] parseFieldValidationServiceConfigurations(RecordModel fieldValidationServicesProvider) {
        ArrayList fieldValidationServiceConfigurations = new ArrayList();
        ListFieldModel fieldValidationServiceListFieldModel = (ListFieldModel) fieldValidationServicesProvider.getField("field_validation_service");
        ArrayList fieldValidationServiceListFieldChildren = fieldValidationServiceListFieldModel.getChildren();
        int numberOfFieldValidationServices = fieldValidationServiceListFieldChildren.size();
        for (int i = 0; i < numberOfFieldValidationServices; i++) {
            RecordModel fieldValidationServiceRecordModel = (RecordModel) fieldValidationServiceListFieldChildren.get(i);
            FieldValidationServiceConfiguration fieldValidationServiceConfiguration = new FieldValidationServiceConfiguration();
            parseServiceConfiguration(fieldValidationServiceConfiguration, fieldValidationServiceRecordModel);
            fieldValidationServiceConfigurations.add(fieldValidationServiceConfiguration);
        }
        FieldValidationServiceConfiguration[] results = (FieldValidationServiceConfiguration[]) fieldValidationServiceConfigurations.toArray(new FieldValidationServiceConfiguration[0]);
        return results;
    }

    private PluginConfiguration[] parsePluginConfigurations(RecordModel pluginsProvider) {
        ArrayList pluginConfigurations = new ArrayList();
        ListFieldModel pluginsListFieldModel = (ListFieldModel) pluginsProvider.getField("plugin");
        ArrayList pluginsListFieldChildren = pluginsListFieldModel.getChildren();
        int numberOfPlugins = pluginsListFieldChildren.size();
        for (int i = 0; i < numberOfPlugins; i++) {
            RecordModel pluginRecordModel = (RecordModel) pluginsListFieldChildren.get(i);
            PluginConfiguration pluginConfiguration = new PluginConfiguration();
            parseServiceConfiguration(pluginConfiguration, pluginRecordModel);
            pluginConfigurations.add(pluginConfiguration);
        }
        PluginConfiguration[] results = (PluginConfiguration[]) pluginConfigurations.toArray(new PluginConfiguration[0]);
        return results;
    }

    private void parseServiceConfiguration(ServiceConfiguration serviceConfiguration, RecordModel serviceRecordModel) {
        serviceConfiguration.setClassName(serviceRecordModel.getValue("class_name"));
        serviceConfiguration.setFeatureCode(serviceRecordModel.getValue("feature_code"));
        BooleanFieldModel isPersistentFieldModel = (BooleanFieldModel) serviceRecordModel.getField("is_persistent");
        serviceConfiguration.setPersistent(isPersistentFieldModel.getBooleanValue());
        ArrayList parameters = parseParameters(serviceRecordModel);
        serviceConfiguration.setParameters(parameters);
    }

    private ArrayList parseParameters(RecordModel parameterProvider) {
        ArrayList parameters = new ArrayList();
        ListFieldModel parameterListFieldModel = (ListFieldModel) parameterProvider.getField("parameter");
        ArrayList parameterListFieldChildren = parameterListFieldModel.getChildren();
        int numberOfParameters = parameterListFieldChildren.size();
        for (int i = 0; i < numberOfParameters; i++) {
            RecordModel parameterRecordModel = (RecordModel) parameterListFieldChildren.get(i);
            String parameterName = parameterRecordModel.getValue("name");
            String parameterValue = parameterRecordModel.getValue("value");
            Parameter parameter = new Parameter(parameterName, parameterValue);
            parameters.add(parameter);
        }
        return parameters;
    }

    private void parseSchemaConceptProperties(SchemaConceptConfiguration schemaConceptConfiguration, RecordModel schemaConceptRecordModel) {
        schemaConceptConfiguration.setName(schemaConceptRecordModel.getValue("name"));
        schemaConceptConfiguration.setOntologyIdentifier(schemaConceptRecordModel.getValue("ontology_identifier"));
        schemaConceptConfiguration.setToolTip(schemaConceptRecordModel.getValue("tool_tip"));
        schemaConceptConfiguration.setHelpLink(schemaConceptRecordModel.getValue("help_link"));
    }

    /**
	* @param recordClassName a record class defined in the XML schema
	* @param fieldName a field name defined in the XML schema
	* @return a schema concept configuration record associated
	* with a given record class name and field name.  There are different
	* kinds of schema concept configuration objects that can be returned including
	* EditFieldConfiguration, ListFieldConfiguration.  These are defined in the 
	* Pedro package pedro.mda.config.*
	*/
    public SchemaConceptConfiguration getConfigurationRecord(String recordClassName, String fieldName) {
        RecordConfiguration recordConfiguration = getRecordConfiguration(recordClassName);
        if (fieldName == null) {
            return recordConfiguration;
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

    private RecordConfiguration getRecordConfiguration(String recordClassName) {
        for (int i = 0; i < recordConfigurations.length; i++) {
            String currentRecordClassName = recordConfigurations[i].getName();
            if (currentRecordClassName.equals(recordClassName) == true) {
                return recordConfigurations[i];
            }
        }
        return null;
    }

    /**
	* @param recordClassName the name of a record class defined in the XML schema
	* @param fieldName the name of a field class defined in the XML schema
	* @return an attribute field configuration object.  Null is returned if no object
	* is associated with the schema concept.
	*/
    public AttributeFieldConfiguration getAttributeFieldConfiguration(String recordClassName, String fieldName) {
        for (int i = 0; i < recordConfigurations.length; i++) {
            String currentRecordClassName = recordConfigurations[i].getName();
            if (currentRecordClassName.equals(recordClassName) == true) {
                AttributeFieldConfiguration[] attributeFieldConfigurations = recordConfigurations[i].getAttributeFieldConfigurations();
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

    /**
	* @param recordClassName the name of a record class defined in the XML schema
	* @param fieldName the name of a field class defined in the XML schema
	* @return an edit field configuration object.  Null is returned if no object
	* is associated with the schema concept.
	*/
    public EditFieldConfiguration getEditFieldConfiguration(String recordClassName, String fieldName) {
        for (int i = 0; i < recordConfigurations.length; i++) {
            String currentRecordClassName = recordConfigurations[i].getName();
            if (currentRecordClassName.equals(recordClassName) == true) {
                EditFieldConfiguration[] editFieldConfigurations = recordConfigurations[i].getEditFieldConfigurations();
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

    /**
	* @param recordClassName the name of a record class defined in the XML schema
	* @param fieldName the name of a field class defined in the XML schema
	* @return an list field configuration object.  Null is returned if no object
	* is associated with the schema concept.
	*/
    public ListFieldConfiguration getListFieldConfiguration(String recordClassName, String fieldName) {
        for (int i = 0; i < recordConfigurations.length; i++) {
            String currentRecordClassName = recordConfigurations[i].getName();
            if (currentRecordClassName.equals(recordClassName) == true) {
                ListFieldConfiguration[] listFieldConfigurations = recordConfigurations[i].getListFieldConfigurations();
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
	* @return a File Launcher object that retains knowledge about what 
	* applications can be launched for a URI with a given extension 
	* eg: JPG, BMP, etc.  This would appear in query form fields that
	* allow users to browse and specify a file or URI.
	*/
    public FileLauncher getFileLauncher() {
        return fileLauncher;
    }
}
