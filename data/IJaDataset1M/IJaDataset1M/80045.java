package edu.asu.commons.experiment;

import java.util.logging.Logger;
import edu.asu.commons.command.Command;
import edu.asu.commons.conf.ExperimentConfiguration;
import edu.asu.commons.conf.ExperimentRoundParameters;

/**
 * $Id: Experiment.java 1 2008-07-23 22:15:18Z alllee $
 * 
 * Contract interface for all Experiment subtypes.
 *   
 * 
 * @author Allen Lee
 * @version $Revision: 1 $
 */
public interface Experiment<C extends ExperimentConfiguration<? extends ExperimentRoundParameters<C>>> {

    public Logger getLogger();

    public void start();

    public void stop();

    public int getServerPort();

    public boolean isFull();

    public boolean isRunning();

    public C getConfiguration();

    public void setConfiguration(C configuration);

    public void schedule(Command command);
}
