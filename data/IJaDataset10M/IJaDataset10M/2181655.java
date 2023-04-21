package be.oniryx.lean.session;

import javax.ejb.Local;

/**
 * User: cedric
 * Date: May 12, 2009
 */
@Local
public interface ProjectBurnDownChart extends BurnDownChart {

    double getCalculatedVelocity();

    String getCalculatedVelocityString();
}
