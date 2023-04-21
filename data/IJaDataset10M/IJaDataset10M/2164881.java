package viewer.search;

import model.address.sqlite.SqCity;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public interface CityActivationListener {

    public void cityActivated(SqCity city);
}
