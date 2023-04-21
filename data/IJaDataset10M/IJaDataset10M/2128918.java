package us.wthr.jdem846.model.processing.shading;

import us.wthr.jdem846.ModelContext;
import us.wthr.jdem846.color.ColorAdjustments;
import us.wthr.jdem846.exception.RenderEngineException;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;
import us.wthr.jdem846.math.MathExt;
import us.wthr.jdem846.model.ModelGrid;
import us.wthr.jdem846.model.ModelPoint;
import us.wthr.jdem846.model.ModelPointHandler;
import us.wthr.jdem846.model.annotations.GridProcessing;
import us.wthr.jdem846.model.processing.AbstractGridProcessor;
import us.wthr.jdem846.model.processing.GridProcessingTypesEnum;
import us.wthr.jdem846.model.processing.GridProcessor;

@GridProcessing(id = "us.wthr.jdem846.model.processing.shading.SlopeShadingProcessor", name = "Slope Shading Process", type = GridProcessingTypesEnum.SHADING, optionModel = SlopeShadingOptionModel.class, enabled = true)
public class SlopeShadingProcessor extends AbstractGridProcessor implements GridProcessor, ModelPointHandler {

    private static Log log = Logging.getLog(SlopeShadingProcessor.class);

    protected int[] rgbaBuffer = new int[4];

    public SlopeShadingProcessor() {
    }

    public SlopeShadingProcessor(ModelContext modelContext, ModelGrid modelGrid) {
        super(modelContext, modelGrid);
    }

    @Override
    public void prepare() throws RenderEngineException {
        SlopeShadingOptionModel optionModel = (SlopeShadingOptionModel) this.getProcessOptionModel();
    }

    @Override
    public void process() throws RenderEngineException {
        super.process();
    }

    @Override
    public void onCycleStart() throws RenderEngineException {
    }

    @Override
    public void onModelLatitudeStart(double latitude) throws RenderEngineException {
    }

    @Override
    public void onModelPoint(double latitude, double longitude) throws RenderEngineException {
        ModelPoint modelPoint = modelGrid.get(latitude, longitude);
        double slope = MathExt.degrees(MathExt.pow(MathExt.cos(modelPoint.getNormal()[2]), -1));
        double shade = 1.0 - (2.0 * (slope / 90.0));
        modelPoint.setDotProduct(shade);
        processPointColor(modelPoint, latitude, longitude);
    }

    protected void processPointColor(ModelPoint modelPoint, double latitude, double longitude) throws RenderEngineException {
        double dot = modelPoint.getDotProduct();
        modelPoint.getRgba(rgbaBuffer, false);
        ColorAdjustments.adjustBrightness(rgbaBuffer, dot);
        modelPoint.setRgba(rgbaBuffer, true);
    }

    @Override
    public void onModelLatitudeEnd(double latitude) throws RenderEngineException {
    }

    @Override
    public void onCycleEnd() throws RenderEngineException {
    }
}
