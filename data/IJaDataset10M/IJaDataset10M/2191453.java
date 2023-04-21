package net.sourceforge.jabm.report;

import java.util.Map;
import net.sourceforge.jabm.event.EventListener;

/**
 * <p>
 * Objects implementing the <code>Report</code> interface provide reporting
 * functionality by collecting data on simulations. They persist across
 * different <code>Simulation</code> runs and are typically declared as
 * <code>singleton</code> in scope when configured via <a
 * href="http://www.springsource.com/developer/spring">Spring</a>. This allows
 * them to collect summary statistics across different simulation runs.
 * </p>
 * 
 * @see net.sourceforge.jabm.report.ReportVariables.
 * 
 * @author Steve Phelps
 * 
 */
public interface Report extends EventListener {

    /**
	 * Get the values calculated by this report.
	 * 
	 * @return A <code>Map</code> of user-readable variable names to their
	 *         associated values.
	 */
    public Map<Object, Number> getVariableBindings();
}
