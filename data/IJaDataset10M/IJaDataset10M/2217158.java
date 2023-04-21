package pierre.configurationTool2;

import pedro.mda.model.*;
import pedro.system.*;
import pedro.util.SystemLog;
import pedro.soa.plugins.PedroPlugin;
import pedro.configurationTool.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;

public class OntologyIdentifierCreationPlugin implements PedroPlugin {

    private int numberOfFieldsEffected;

    public OntologyIdentifierCreationPlugin() {
        numberOfFieldsEffected = 0;
    }

    /**
	* Interface: PedroPlugin
	*/
    public String getDisplayName() {
        String displayName = PedroResources.getMessage("pedro.plugins.ontologyIdentifiers.title");
        return displayName;
    }

    public String getDescription() {
        String description = PedroResources.getMessage("pedro.plugins.ontologyIdentifiers.description");
        return description;
    }

    public String isWorking() {
        return null;
    }

    public boolean isSuitableForRecordModel(String modelStamp, String recordClassName) {
        return true;
    }

    public void execute(PedroFormContext pedroFormContext) {
        numberOfFieldsEffected = 0;
        PedroUIFactory pedroUIFactory = (PedroUIFactory) pedroFormContext.getApplicationProperty(PedroApplicationContext.USER_INTERFACE_FACTORY);
        RecordModelFactory recordModelFactory = (RecordModelFactory) pedroFormContext.getApplicationProperty(PedroApplicationContext.RECORD_MODEL_FACTORY);
        String defaultPrefix = recordModelFactory.getSchemaName();
        RecordModel currentRecordModel = (RecordModel) pedroFormContext.getDocumentProperty(PedroDocumentContext.CURRENT_RECORD_MODEL);
        RecordModelUtility recordModelUtility = new RecordModelUtility();
        RecordModel pierreConfigurationSchemaRecordModel = recordModelUtility.getRootModel(currentRecordModel);
        ListFieldModel schemaConceptPropertiesListModel = (ListFieldModel) pierreConfigurationSchemaRecordModel.getField("schema_concept_properties");
        ArrayList schemaConceptPropertiesListChildren = schemaConceptPropertiesListModel.getChildren();
        if (schemaConceptPropertiesListChildren.size() == 0) {
            return;
        }
        RecordModel schemaConceptPropertiesRecordModel = (RecordModel) schemaConceptPropertiesListChildren.get(0);
        OntologyIdentifierGenerator generator = new OntologyIdentifierGenerator();
        generator.createIdentifiers(pedroUIFactory, defaultPrefix, schemaConceptPropertiesRecordModel);
    }

    public URL getHelpLink() {
        return null;
    }
}
