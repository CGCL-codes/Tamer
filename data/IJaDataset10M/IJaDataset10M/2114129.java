package com.iver.cit.gvsig.geoprocess.impl.union;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.SHPLayerDefinition;
import com.iver.cit.gvsig.fmap.edition.IWriter;
import com.iver.cit.gvsig.fmap.edition.ShpSchemaManager;
import com.iver.cit.gvsig.fmap.edition.writers.shp.MultiShpWriter;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.geoprocess.core.fmap.AbstractGeoprocessController;
import com.iver.cit.gvsig.geoprocess.core.fmap.GeoprocessException;
import com.iver.cit.gvsig.geoprocess.core.fmap.IGeoprocess;
import com.iver.cit.gvsig.geoprocess.core.gui.AddResultLayerTask;
import com.iver.cit.gvsig.geoprocess.core.gui.IGeoprocessUserEntries;
import com.iver.cit.gvsig.geoprocess.core.gui.OverlayPanelIF;
import com.iver.cit.gvsig.geoprocess.impl.union.fmap.UnionGeoprocess;
import com.iver.utiles.swing.threads.IMonitorableTask;
import com.iver.utiles.swing.threads.MonitorableDecoratorMainFirst;

public class UnionGeoprocessController extends AbstractGeoprocessController {

    private OverlayPanelIF geoProcessingUnionPanel;

    private UnionGeoprocess union;

    public void setView(IGeoprocessUserEntries viewPanel) {
        this.geoProcessingUnionPanel = (OverlayPanelIF) viewPanel;
    }

    public IGeoprocess getGeoprocess() {
        return union;
    }

    public boolean launchGeoprocess() {
        FLyrVect inputLayer = geoProcessingUnionPanel.getInputLayer();
        FLyrVect overlayLayer = geoProcessingUnionPanel.getSecondLayer();
        FLayers layers = geoProcessingUnionPanel.getFLayers();
        File outputFile = null;
        try {
            outputFile = geoProcessingUnionPanel.getOutputFile();
        } catch (FileNotFoundException e3) {
            String error = PluginServices.getText(this, "Error_entrada_datos");
            String errorDescription = PluginServices.getText(this, "Error_seleccionar_resultado");
            geoProcessingUnionPanel.error(errorDescription, error);
            return false;
        }
        if (outputFile == null || (outputFile.getAbsolutePath().length() == 0)) {
            String error = PluginServices.getText(this, "Error_entrada_datos");
            String errorDescription = PluginServices.getText(this, "Error_seleccionar_resultado");
            geoProcessingUnionPanel.error(errorDescription, error);
            return false;
        }
        UnionGeoprocess union = new UnionGeoprocess(inputLayer);
        union.setSecondOperand(overlayLayer);
        SHPLayerDefinition definition = (SHPLayerDefinition) union.createLayerDefinition();
        definition.setFile(outputFile);
        ShpSchemaManager schemaManager = new ShpSchemaManager(outputFile.getAbsolutePath());
        IWriter writer = null;
        try {
            writer = getShpWriter(definition);
        } catch (Exception e1) {
            String error = PluginServices.getText(this, "Error_escritura_resultados");
            String errorDescription = PluginServices.getText(this, "Error_preparar_escritura_resultados");
            geoProcessingUnionPanel.error(errorDescription, error);
            return false;
        }
        boolean found = false;
        if (writer instanceof MultiShpWriter) {
            MultiShpWriter mWriter = (MultiShpWriter) writer;
            if (mWriter.getPointsFile().exists()) {
                found = true;
            }
            if (mWriter.getLinesFile().exists()) {
                found = true;
            }
            if (mWriter.getPolygonsFile().exists()) {
                found = true;
            }
        } else {
            found = outputFile.exists();
        }
        if (found) {
            if (!geoProcessingUnionPanel.askForOverwriteOutputFile(outputFile)) {
                return false;
            }
        }
        union.setResultLayerProperties(writer, schemaManager);
        HashMap params = new HashMap();
        boolean onlySelectedFirst = geoProcessingUnionPanel.onlyFirstLayerSelected();
        boolean onlySelectedSecond = geoProcessingUnionPanel.onlySecondLayerSelected();
        Boolean first = new Boolean(onlySelectedFirst);
        params.put("firstlayerselection", first);
        Boolean second = new Boolean(onlySelectedSecond);
        params.put("secondlayerselection", second);
        try {
            union.setParameters(params);
            union.checkPreconditions();
            IMonitorableTask task1 = union.createTask();
            if (task1 == null) {
                return false;
            }
            AddResultLayerTask task2 = new AddResultLayerTask(union);
            task2.setLayers(layers);
            MonitorableDecoratorMainFirst globalTask = new MonitorableDecoratorMainFirst(task1, task2);
            if (!overlayLayer.isSpatiallyIndexed()) {
                final IMonitorableTask sptIdxTask = geoProcessingUnionPanel.askForSpatialIndexCreation(overlayLayer);
                if (sptIdxTask != null) {
                    PluginServices.backgroundExecution(new Runnable() {

                        public void run() {
                            PluginServices.cancelableBackgroundExecution(sptIdxTask);
                        }
                    });
                }
            }
            if (globalTask.preprocess()) PluginServices.cancelableBackgroundExecution(globalTask);
        } catch (GeoprocessException e) {
            String error = PluginServices.getText(this, "Error_ejecucion");
            String errorDescription = PluginServices.getText(this, "Error_fallo_geoproceso");
            errorDescription = "<html>" + errorDescription + ":<br>" + e.getMessage() + "</html>";
            geoProcessingUnionPanel.error(errorDescription, error);
            return false;
        }
        return true;
    }

    public int getWidth() {
        return 700;
    }

    public int getHeight() {
        return 300;
    }
}
