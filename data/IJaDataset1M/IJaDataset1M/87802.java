package repast.simphony.data.logging.outputter;

import repast.simphony.data.logging.BatchAware;
import repast.simphony.data.logging.DataSetContainer;
import repast.simphony.data.logging.LogMessage;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.NonModelAction;

/**
 * Interface for objects that are used to output data. The objects should be
 * able to handle the explicit cleanup() method, but should implicitly
 * initialize themselves when possible.<p/>
 * 
 * When the {@link repast.simphony.engine.schedule.IAction#execute()} method is called,
 * this should cause the outputter to update itself. This could mean forcing a
 * chart to update, causing some data to be written, outputting aggregated data
 * or some other outputter related activity.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
@NonModelAction
public interface Outputter extends DataSetContainer, FormattingHintAware, IAction, BatchAware {

    /**
	 * Causes the chart to be updated based on the passed in info.
	 * 
	 * @param message
	 *            A message containing information, what information is
	 *            necessary is outputter specific.
	 */
    void output(LogMessage message);

    String getOutputterName();

    void setOutputterName(String outputterName);
}
