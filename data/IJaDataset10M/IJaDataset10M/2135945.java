package com.nokia.ats4.appmodel.perspective.modeldesign.controller;

import com.nokia.ats4.appmodel.MainApplication;
import com.nokia.ats4.appmodel.event.EventQueue;
import com.nokia.ats4.appmodel.event.KendoEvent;
import com.nokia.ats4.appmodel.event.KendoEventListener;
import com.nokia.ats4.appmodel.exception.KendoException;
import com.nokia.ats4.appmodel.grapheditor.event.ImageCellReSizeEvent;
import com.nokia.ats4.appmodel.model.KendoModel;
import com.nokia.ats4.appmodel.model.KendoProject;
import com.nokia.ats4.appmodel.perspective.modeldesign.event.ImportModelEvent;
import com.nokia.ats4.appmodel.perspective.modeldesign.swing.SelectImportedModelDialog;
import com.nokia.ats4.appmodel.util.KendoModelImporter;
import com.nokia.ats4.appmodel.util.Settings;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * ImportAddModelsCommand, this handles application model imports from different project files.
 * Project files and the imported models are selected trough import wizard.
 * This import handles the correct id allocation for imported models, usecases and flags so that
 * there are no duplicates after import. Import is saved to active project only after normal save.
 *
 * @author Hannu-Pekka Hakam&auml;ki
 * @version $Revision: 2 $
 */
public class ImportAddModelsCommand implements KendoEventListener {

    private static Logger log = Logger.getLogger(ImportAddModelsCommand.class);

    /** This is the project that holds the models that will be imported */
    private KendoProject export = null;

    /** Creates a new instance of ImportAddModelsCommand */
    public ImportAddModelsCommand() {
    }

    /** This handles the import execution */
    @Override
    public void processEvent(KendoEvent event) {
        log.info("import started");
        List<KendoModel> selectedModels = selectExportCommand();
        if (selectedModels != null && selectedModels.size() > 0) {
            KendoModelImporter importer = new KendoModelImporter(this.export);
            importer.setCoordinatesForFirstAppModel(((ImportModelEvent) event).getClickPoint());
            importer.importModels(selectedModels);
            log.info("import finished succesfully");
        } else if (selectedModels != null) {
            setToDefault();
            throw new KendoException("No models to import ", "error.import.noModels");
        }
        setToDefault();
        boolean fit = Settings.getBooleanProperty("graph.systemState.fitImageToCell");
        EventQueue.postEvent(new ImageCellReSizeEvent(this, fit));
    }

    /**
     * This method launches export dialog and returns the models that user selects to be imported
     * This method also sets the export variable to point to exported project
     *
     * @return -list of models that user selected to be imported
     */
    private List<KendoModel> selectExportCommand() {
        List<KendoModel> models = new ArrayList<KendoModel>();
        SelectImportedModelDialog chooseDialog = new SelectImportedModelDialog(MainApplication.getMainWindow(), models, this.export);
        this.export = chooseDialog.getProject();
        chooseDialog.closeDialog();
        if (this.export == null) {
            models = null;
        }
        return models;
    }

    private void setToDefault() {
        this.export = null;
    }
}
