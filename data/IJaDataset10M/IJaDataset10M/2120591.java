package com.iver.cit.gvsig.geoprocess.impl.reproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import org.cresques.cts.IProjection;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.SHPLayerDefinition;
import com.iver.cit.gvsig.fmap.edition.IWriter;
import com.iver.cit.gvsig.fmap.edition.ShpSchemaManager;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.geoprocess.core.fmap.AbstractGeoprocessController;
import com.iver.cit.gvsig.geoprocess.core.fmap.GeoprocessException;
import com.iver.cit.gvsig.geoprocess.core.fmap.IGeoprocess;
import com.iver.cit.gvsig.geoprocess.core.gui.AddResultLayerTask;
import com.iver.cit.gvsig.geoprocess.core.gui.IGeoprocessUserEntries;
import com.iver.cit.gvsig.geoprocess.impl.reproject.fmap.ReprojectGeoprocess;
import com.iver.cit.gvsig.geoprocess.impl.reproject.gui.GeoprocessingReprojectPanel;
import com.iver.utiles.swing.threads.IMonitorableTask;
import com.iver.utiles.swing.threads.MonitorableDecoratorMainFirst;

public class ReprojectGeoprocessController extends AbstractGeoprocessController {

    private GeoprocessingReprojectPanel panel;

    private ReprojectGeoprocess reproject;

    public void setView(IGeoprocessUserEntries viewPanel) {
        this.panel = (GeoprocessingReprojectPanel) viewPanel;
    }

    public IGeoprocess getGeoprocess() {
        return new ReprojectGeoprocess();
    }

    public boolean launchGeoprocess() {
        FLyrVect inputLayer = panel.getInputLayer();
        FLayers layers = panel.getFLayers();
        File outputFile = null;
        try {
            outputFile = panel.getOutputFile();
        } catch (FileNotFoundException e3) {
            String error = PluginServices.getText(this, "Error_entrada_datos");
            String errorDescription = PluginServices.getText(this, "Error_seleccionar_resultado");
            panel.error(errorDescription, error);
            return false;
        }
        if (outputFile == null || (outputFile.getAbsolutePath().length() == 0)) {
            String error = PluginServices.getText(this, "Error_entrada_datos");
            String errorDescription = PluginServices.getText(this, "Error_seleccionar_resultado");
            panel.error(errorDescription, error);
            return false;
        }
        if (outputFile.exists()) {
            if (!panel.askForOverwriteOutputFile(outputFile)) {
                return false;
            }
        }
        reproject = (ReprojectGeoprocess) getGeoprocess();
        reproject.setFirstLayer(inputLayer);
        SHPLayerDefinition definition = (SHPLayerDefinition) reproject.createLayerDefinition();
        definition.setFile(outputFile);
        ShpSchemaManager schemaManager = new ShpSchemaManager(outputFile.getAbsolutePath());
        IWriter writer = null;
        try {
            writer = getShpWriter(definition);
        } catch (Exception e1) {
            String error = PluginServices.getText(this, "Error_escritura_resultados");
            String errorDescription = PluginServices.getText(this, "Error_preparar_escritura_resultados");
            panel.error(errorDescription, error);
            return false;
        }
        reproject.setResultLayerProperties(writer, schemaManager);
        HashMap params = new HashMap();
        boolean onlySelected = panel.isFirstOnlySelected();
        params.put("firstlayerselection", new Boolean(onlySelected));
        IProjection projection = panel.getTargetProjection();
        IProjection previousProj = inputLayer.getProjection();
        if (previousProj.equals(projection)) {
            String error = PluginServices.getText(this, "Error_entrada_datos");
            String errorDesc = PluginServices.getText(this, "Error_proyecciones_iguales");
            panel.error(errorDesc, error);
            return false;
        }
        params.put("targetProjection", projection);
        try {
            reproject.setParameters(params);
            reproject.checkPreconditions();
            IMonitorableTask task1 = reproject.createTask();
            if (task1 == null) {
                return false;
            }
            AddResultLayerTask task2 = new AddResultLayerTask(reproject);
            task2.setLayers(layers);
            MonitorableDecoratorMainFirst globalTask = new MonitorableDecoratorMainFirst(task1, task2);
            if (globalTask.preprocess()) PluginServices.cancelableBackgroundExecution(globalTask);
        } catch (GeoprocessException e) {
            String error = PluginServices.getText(this, "Error_ejecucion");
            String errorDescription = PluginServices.getText(this, "Error_fallo_geoproceso");
            errorDescription = "<html>" + errorDescription + ":<br>" + e.getMessage() + "</html>";
            panel.error(errorDescription, error);
            return false;
        }
        return true;
    }

    public int getWidth() {
        return 750;
    }

    public int getHeight() {
        return 220;
    }
}
