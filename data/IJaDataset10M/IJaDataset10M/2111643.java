package repast.simphony.data.engine;

import com.thoughtworks.xstream.XStream;
import repast.simphony.data.logging.formula.FormulaEvaluator;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.controller.DescriptorControllerAction;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.scenario.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Nick Collier
 */
public class DataSetControllerActionIO extends AbstractDescriptorControllerActionIO<DefaultDataGathererDescriptorAction, DataGathererDescriptor> {

    public static class DataSetActionLoader extends DescriptorActionLoader<DataGathererDescriptor> {

        public DataSetActionLoader(File file, Object contextID) {
            super(file, contextID, DataGathererDescriptor.class, ControllerActionConstants.DATA_SET_ROOT);
        }

        @Override
        protected ControllerAction createAction(DataGathererDescriptor descriptor) {
            return new DefaultDataGathererDescriptorAction(descriptor);
        }

        @Override
        protected ClassLoader getClassLoader() {
            return getClass().getClassLoader();
        }
    }

    public DataSetControllerActionIO() {
        super(DefaultDataGathererDescriptorAction.class, DataGathererDescriptor.class);
    }

    public ActionLoader getActionLoader(File actionFile, Object contextID) {
        return new DataSetActionLoader(actionFile, contextID);
    }

    @Override
    public ActionSaver getActionSaver() {
        return new DescriptorActionSaver() {

            @Override
            public void save(XStream xstream, DescriptorControllerAction action, String filename) throws IOException {
                xstream.omitField(FormulaEvaluator.class, "start");
                super.save(xstream, action, filename);
            }
        };
    }
}
