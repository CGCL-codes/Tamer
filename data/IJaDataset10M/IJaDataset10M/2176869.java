package pedro.desktopDeployment;

import pedro.mda.model.EditFieldModel;
import pedro.system.*;
import pedro.mda.config.*;
import pedro.mda.model.*;
import javax.swing.ToolTipManager;
import pedro.soa.ontology.sources.OntologyContext;
import pedro.soa.EditFieldEditingComponent;
import pedro.soa.GeneralServiceClassFactory;
import pedro.soa.plugins.*;
import pedro.util.HelpEnabledButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.lang.reflect.Constructor;
import javax.swing.*;
import java.util.ArrayList;

public abstract class EditFieldView extends DataFieldView implements ActionListener {

    private String value;

    private Object valueSupplier;

    protected EditFieldModel editFieldModel;

    protected OntologyContext ontologyContext;

    protected HelpEnabledButton edit;

    protected HelpEnabledButton plugins;

    protected SchemaConceptPropertyManager schemaConceptPropertyManager;

    protected ArrayList pluginsList;

    public EditFieldView() {
    }

    public void initialise(EditFieldModel editFieldModel, PedroFormContext pedroFormContext) {
        super.initialise(editFieldModel, pedroFormContext);
        String pluginsText = PedroResources.getMessage("pedro.pluginManager.choosePlugins");
        plugins = new HelpEnabledButton(pedroFormContext, pluginsText, "Plugins.html", true);
        initialiseModelAspects(editFieldModel, pedroFormContext);
        initialiseViewAspects();
    }

    private void initialiseModelAspects(EditFieldModel editFieldModel, PedroFormContext pedroFormContext) {
        this.editFieldModel = editFieldModel;
        schemaConceptPropertyManager = (SchemaConceptPropertyManager) pedroFormContext.getApplicationProperty(PedroApplicationContext.SCHEMA_CONCEPT_PROPERTIES);
        value = PedroResources.EMPTY_STRING;
        ontologyContext = (OntologyContext) pedroFormContext.getProperty(PedroFormContext.ONTOLOGY_CONTEXT);
        pluginsList = new ArrayList();
        RecordModel parentRecordModel = editFieldModel.getContainingRecord();
        String recordClassName = parentRecordModel.getRecordClassName();
        String fieldName = editFieldModel.getName();
        EditFieldConfiguration editFieldConfiguration = schemaConceptPropertyManager.getEditFieldConfiguration(recordClassName, fieldName);
        if (editFieldConfiguration != null) {
            PluginConfiguration[] pluginConfigurations = editFieldConfiguration.getPluginConfigurations();
            if (pluginConfigurations.length > 0) {
                PluginFactory pluginFactory = (PluginFactory) pedroFormContext.getApplicationProperty(PedroApplicationContext.PLUGIN_FACTORY);
                pluginsList.clear();
                for (int i = 0; i < pluginConfigurations.length; i++) {
                    try {
                        String pluginClassName = pluginConfigurations[i].getClassName();
                        PedroPlugin pedroPlugin = (PedroPlugin) pluginFactory.createService(pluginClassName, pluginConfigurations[i].getParameters(), pluginConfigurations[i].isPersistent());
                        if (pluginConfigurations.length == 1) {
                            plugins.setText(pluginConfigurations[i].getName());
                        }
                        pluginsList.add(pedroPlugin);
                    } catch (Exception err) {
                        err.printStackTrace(System.out);
                    }
                }
            }
        }
    }

    private void initialiseViewAspects() {
        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        String editButtonName = PedroResources.getMessage("buttons.edit");
        edit = new HelpEnabledButton(pedroFormContext, editButtonName, "EditFieldComponent.html", true);
        String editToolTip = PedroResources.getMessage("recordView.editFieldEditingComponent.edit.toolTipText");
        edit.setToolTipText(editToolTip);
        toolTipManager.registerComponent(edit);
        String pluginsToolTip = PedroResources.getMessage("pedro.pluginManager.choosePlugins.toolTipText");
        plugins.setToolTipText(pluginsToolTip);
        setUnits(editFieldModel.getUnits());
    }

    /**
	* helps ensure that the field view removes its UI components from the the
	* tool tip manager
	*/
    public void deregisterToolTipComponents() {
        super.deregisterToolTipComponents();
        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        toolTipManager.unregisterComponent(edit);
        toolTipManager.unregisterComponent(plugins);
    }

    /**
     * @return whether there is a difference between the displayed value
     *         and the value saved value contained in the underlying model component.
     */
    public abstract boolean isDirty();

    public String getValue() {
        return value;
    }

    public Object getValueSupplier() {
        return valueSupplier;
    }

    public EditFieldModel getModel() {
        return editFieldModel;
    }

    protected void editFieldWithEditingComponent() {
        EditFieldEditingComponent editFieldEditingComponent = getEditFieldEditingComponent();
        editFieldEditingComponent.editField(pedroFormContext, editFieldModel);
        setFieldValue(editFieldModel.getValue());
    }

    protected EditFieldEditingComponent getEditFieldEditingComponent() {
        String fieldName = editFieldModel.getName();
        RecordModel recordModel = editFieldModel.getContainingRecord();
        String recordClassName = recordModel.getRecordClassName();
        EditFieldConfiguration editFieldConfiguration = (EditFieldConfiguration) schemaConceptPropertyManager.getConfigurationRecord(recordClassName, fieldName);
        if (editFieldConfiguration == null) {
            return null;
        }
        String editFieldEditingComponentClassName = editFieldConfiguration.getEditingComponentClassName();
        if (editFieldEditingComponentClassName.equals(PedroResources.EMPTY_STRING) == true) {
            return null;
        }
        try {
            GeneralServiceClassFactory generalServiceClassFactory = new GeneralServiceClassFactory(pedroFormContext.getApplicationContext());
            Class cls = generalServiceClassFactory.getClass(editFieldEditingComponentClassName);
            Constructor constructor = cls.getConstructor(new Class[0]);
            EditFieldEditingComponent editFieldEditingComponent = (EditFieldEditingComponent) constructor.newInstance(new Object[0]);
            return editFieldEditingComponent;
        } catch (Exception err) {
            err.printStackTrace(System.out);
            return null;
        }
    }

    protected void choosePlugin() {
        pedroFormContext.setProperty(PedroFormContext.CURRENT_FIELD, editFieldModel);
        if (pluginsList.size() == 1) {
            PedroPlugin plugin = (PedroPlugin) pluginsList.get(0);
            plugin.execute(pedroFormContext);
        } else {
            PluginSelectionDialog pluginSelectionDialog = new PluginSelectionDialog(pedroFormContext, pluginsList);
            pluginSelectionDialog.show();
        }
    }

    public abstract void setFieldValue(String value);

    public abstract void keepValue();

    public abstract void restoreValue();

    public void setUnits(String units) {
        if (units.equals(PedroResources.NO_ATTRIBUTE_VALUE) == true) {
            return;
        }
        StringBuffer labelText = new StringBuffer();
        labelText.append(name);
        labelText.append("(");
        labelText.append(units);
        labelText.append(")");
        label.setText(labelText.toString());
    }

    public void setValueSupplier(Object _valueSupplier) {
        this.valueSupplier = _valueSupplier;
    }

    public void actionPerformed(ActionEvent event) {
        Object button = event.getSource();
        if (button == edit) {
            editFieldWithEditingComponent();
        } else if (button == plugins) {
            choosePlugin();
        }
    }
}
