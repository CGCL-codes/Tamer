package org.jpox.samples.persistentabstracts;

/**
 * Abstract class representing a City.
 * @version $Revision: 1.1 $
 */
public abstract class ACity extends APopulace {

    public abstract Country getCountry();

    public abstract void setCountry(Country ctry);
}
