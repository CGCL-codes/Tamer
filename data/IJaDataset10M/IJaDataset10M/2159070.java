package edu.asu.commons.experiment;

import java.io.Serializable;
import edu.asu.commons.conf.ExperimentConfiguration;
import edu.asu.commons.conf.ExperimentRoundParameters;

/**
 * $Id: DataModel.java 1 2008-07-23 22:15:18Z alllee $
 * 
 * Marker interface for data models
 * 
 * @author <a href='Allen.Lee@asu.edu'>Allen Lee</a>
 * @version $Revision: 1 $
 */
public interface DataModel<E extends ExperimentRoundParameters<? extends ExperimentConfiguration<E>>> extends Serializable {

    public E getRoundConfiguration();
}
