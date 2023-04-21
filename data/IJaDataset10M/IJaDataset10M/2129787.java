package de.lichtflut.infra.ui.flow;

/**
 * A flow encapsulates a sequence of automatic or interactive steps.
 * 
 * Created: 02.03.2008 
 *
 * @author Oliver Tigges
 */
public interface Flow {

    /**
	 * starts the flow.
	 */
    void start();

    Step nextStep();

    boolean hasNext();

    /**
	 * Called when the flow is reactivated after a sub flow has finished
	 * The given {@link FlowDataTransfer} may contain data the sub flow produced. This data
	 * should be processed inside the reactivate method becaus it will be disposed afterwards.
	 * @param subFlowResult
	 */
    void reactivate(FlowDataTransfer subFlowResult);

    FlowDataTransfer terminate();

    void addFlowListener(FlowEventListener listener);
}
