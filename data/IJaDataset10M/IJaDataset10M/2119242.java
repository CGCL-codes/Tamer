package org.elogistics.calculation.distance;

import java.util.List;
import org.elogistics.domain.entities.location.City;

/**
 * A factory to create CachedDistanceTable-Objects.
 * 
 * @author Jurkschat, Oliver
 *
 */
public interface IDistanceTableFactory {

    public IDistanceCalculatorStrategie getDistanceCalculatorStrategie();

    public IDistanceTable createDistanceTable(List<City> cities);

    public IDistanceTable createCachedDistanceTable(List<City> cities);
}
