package repast.simphony.chart.engine;

import java.io.File;
import repast.simphony.scenario.AbstractDescriptorControllerActionIO;
import repast.simphony.scenario.ActionLoader;

/**
 * @author Nick Collier
 */
public class XYChartControllerActionIO extends AbstractDescriptorControllerActionIO<DefaultXYChartOutputterDescriptorAction, DefaultXYChartOutputterDescriptor> {

    public XYChartControllerActionIO() {
        super(DefaultXYChartOutputterDescriptorAction.class, DefaultXYChartOutputterDescriptor.class);
    }

    public ActionLoader getActionLoader(File actionFile, Object contextID) {
        return new XYChartActionLoader(actionFile, contextID);
    }
}
