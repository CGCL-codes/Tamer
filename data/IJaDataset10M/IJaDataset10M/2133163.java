package net.entropysoft.dashboard.plugin.monitored;

import java.beans.PropertyChangeListener;
import net.entropysoft.dashboard.plugin.variables.IVariable;
import org.jfree.data.time.TimeSeries;

/**
 * Keeps the previous numeric values of a variable as a {@link TimeSeries}
 * 
 * @author cedric
 * 
 */
public interface IVariableHistory {

    public static final String TIMESERIES_PROPERTY = "timeseries";

    /**
	 * get the value that we are monitoring
	 * 
	 * @return
	 */
    public IVariable getVariable();

    /**
	 * get the TimeSeries associated with the value
	 * 
	 * @return
	 */
    public TimeSeries getTimeSeries();

    public void addPropertyChangeListener(PropertyChangeListener l);

    public void removePropertyChangeListener(PropertyChangeListener l);
}
