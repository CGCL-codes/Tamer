package com.iver.cit.gvsig.geoprocess.impl.merge.fmap;

import java.util.Map;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.driver.exceptions.SchemaEditionException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.drivers.ILayerDefinition;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.operations.strategies.Strategy;
import com.iver.cit.gvsig.fmap.operations.strategies.StrategyManager;
import com.iver.cit.gvsig.geoprocess.core.fmap.AbstractGeoprocess;
import com.iver.cit.gvsig.geoprocess.core.fmap.DeferredFeaturePersisterProcessor;
import com.iver.cit.gvsig.geoprocess.core.fmap.DefinitionUtils;
import com.iver.cit.gvsig.geoprocess.core.fmap.GeoprocessException;
import com.iver.utiles.swing.threads.CancellableMonitorable;
import com.iver.utiles.swing.threads.DefaultCancellableMonitorable;
import com.iver.utiles.swing.threads.IMonitorableTask;

/**
 * This geoprocess is "equivalent" to UNION relational operator.
 * Output layer will have a geometry for each geometry of input layer.
 *
 * Also, output layer will preserve a schema of one of input layers.
 * @author azabala
 *
 * FIXME Hereda de AbstractGeoprocess firstLayer. REVISAR
 *
 */
public class MergeGeoprocess extends AbstractGeoprocess {

    /**
	 * Input layers we are going to merge.
	 */
    private FLyrVect[] inputLayers;

    /**
	 * Input layer whose schema will retain result layer
	 */
    private FLyrVect outputSchemaLayer;

    private ILayerDefinition resultLayerDefinition;

    public MergeGeoprocess() {
    }

    public void setParameters(Map params) throws GeoprocessException {
    }

    public void checkPreconditions() throws GeoprocessException {
        if (inputLayers == null) throw new GeoprocessException("No se han especificado las capas a juntar en un merge");
        if (outputSchemaLayer == null) throw new GeoprocessException("Merge: no se ha especificado que capa determinara los atributos");
        int shapeType = -1;
        try {
            shapeType = inputLayers[0].getShapeType();
            for (int i = 0; i < inputLayers.length; i++) {
                if (inputLayers[i] == null) throw new GeoprocessException("Merge: la capa " + i + " no est� definida -null");
                if (inputLayers[i].getShapeType() != shapeType) throw new GeoprocessException("Las capas a juntar deben tener el mismo tipo de geometria");
            }
        } catch (ReadDriverException e) {
            throw new GeoprocessException("Error al tratar de determinar el tipo de geometria de las capas a mezclar");
        }
    }

    public void process() throws GeoprocessException {
        try {
            createTask().run();
        } catch (Exception e) {
            throw new GeoprocessException("Error al ejecutar el geoproceso merge");
        }
    }

    public void cancel() {
        try {
            schemaManager.removeSchema("");
        } catch (SchemaEditionException e) {
            e.printStackTrace();
        }
    }

    public ILayerDefinition createLayerDefinition() {
        if (this.resultLayerDefinition == null) {
            try {
                this.resultLayerDefinition = DefinitionUtils.createLayerDefinition(this.outputSchemaLayer);
            } catch (Exception e) {
                return null;
            }
        }
        return this.resultLayerDefinition;
    }

    public void setInputLayers(FLyrVect[] inputLayers) {
        this.inputLayers = inputLayers;
        if (inputLayers != null && inputLayers.length > 0) firstLayer = inputLayers[0];
    }

    public void setOutputSchemaLayer(FLyrVect outputSchemaLayer) {
        this.outputSchemaLayer = outputSchemaLayer;
    }

    public IMonitorableTask createTask() {
        try {
            return new MergeMonitorableTask();
        } catch (ReadDriverException e) {
            return null;
        }
    }

    class MergeMonitorableTask implements IMonitorableTask {

        private CancellableMonitorable cancelMonitor = null;

        String MERGE_MESSAGE = PluginServices.getText(this, "Mensaje_juntar");

        String MERGE_NOTE = PluginServices.getText(this, "Mensaje_procesando_juntar");

        String OF = PluginServices.getText(this, "De");

        private boolean finished = false;

        MergeMonitorableTask() throws ReadDriverException {
            initialize();
        }

        void initialize() throws ReadDriverException {
            cancelMonitor = createCancelMonitor();
        }

        private CancellableMonitorable createCancelMonitor() throws ReadDriverException {
            DefaultCancellableMonitorable monitor = new DefaultCancellableMonitorable();
            monitor.setInitialStep(0);
            monitor.setDeterminatedProcess(true);
            int numSteps = 0;
            for (int i = 0; i < inputLayers.length; i++) {
                numSteps += inputLayers[i].getSource().getShapeCount();
            }
            monitor.setFinalStep(numSteps);
            return monitor;
        }

        public int getInitialStep() {
            return cancelMonitor.getInitialStep();
        }

        public int getFinishStep() {
            return cancelMonitor.getFinalStep();
        }

        public int getCurrentStep() {
            return cancelMonitor.getCurrentStep();
        }

        public String getStatusMessage() {
            return MERGE_MESSAGE;
        }

        public String getNote() {
            return MERGE_NOTE + " " + getCurrentStep() + " " + OF + " " + getFinishStep();
        }

        public void cancel() {
            ((DefaultCancellableMonitorable) cancelMonitor).setCanceled(true);
            MergeGeoprocess.this.cancel();
        }

        public void run() throws GeoprocessException {
            DeferredFeaturePersisterProcessor processor = new DeferredFeaturePersisterProcessor(writer);
            try {
                writer.preProcess();
            } catch (StartWriterVisitorException e1) {
                finished = true;
                throw new GeoprocessException("Error al preparar la capa resultado");
            }
            MergeVisitor merge = new MergeVisitor(createLayerDefinition(), processor);
            for (int i = 0; i < inputLayers.length; i++) {
                Strategy strategy = StrategyManager.getStrategy(inputLayers[i]);
                try {
                    strategy.process(merge, cancelMonitor);
                } catch (ReadDriverException e) {
                    finished = true;
                    throw new GeoprocessException("Merge: error al leer la capa " + inputLayers[i].getName());
                } catch (ProcessVisitorException e) {
                    finished = true;
                    throw new GeoprocessException("Merge: error al procesar la capa " + inputLayers[i].getName());
                } catch (VisitorException e) {
                    finished = true;
                    throw new GeoprocessException("Merge: error al procesar la capa " + inputLayers[i].getName());
                }
            }
            try {
                writer.postProcess();
            } catch (StopWriterVisitorException e) {
                finished = true;
                throw new GeoprocessException("Error al preparar la capa resultado");
            }
            finished = true;
        }

        public boolean isDefined() {
            return cancelMonitor.isDeterminatedProcess();
        }

        public boolean isCanceled() {
            return cancelMonitor.isCanceled();
        }

        public boolean isFinished() {
            return finished;
        }

        public void finished() {
        }
    }
}
