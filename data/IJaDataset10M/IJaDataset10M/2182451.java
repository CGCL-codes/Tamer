package gov.sns.apps.orbitcorrect;

import gov.sns.tools.plot.FunctionGraphsJPanel;
import java.util.List;

/**
 * ChartModelListener
 *
 * @author  tap
 * @since Jul 19, 2004
 */
public interface ChartModelListener {

    /**
	 * An event indicating that the chart model's trace sources have changed.
	 * @param model the chart model which posted the event
	 * @param traceSources the chart model's new trace sources
	 */
    public void traceSourcesChanged(ChartModel model, List traceSources);

    /**
	 * An event indicating that the chart's properties have changed.
	 * @param model the chart model which posted the event
	 * @param chart the chart whose properties have changed
	 */
    public void chartPropertiesChanged(ChartModel model, FunctionGraphsJPanel chart);
}
