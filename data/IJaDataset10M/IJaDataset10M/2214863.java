package org.matsim.api.basic.v01;

import org.matsim.api.basic.v01.network.BasicNetwork;
import org.matsim.api.basic.v01.population.BasicPopulation;
import org.matsim.core.config.Config;

/**
 * @author dgrether
 */
public interface BasicScenario {

    public BasicNetwork getNetwork();

    public BasicPopulation getPopulation();

    public Config getConfig();

    public Id createId(String string);

    public Coord createCoord(double x, double y);
}
