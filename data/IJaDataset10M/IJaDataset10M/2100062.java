package net.aa3sd.SMT.interfaces;

import net.aa3sd.SMT.math.BadCalculationException;

/**
 * @author mole
 * 
 */
public interface ResourceAllocationModelInterface {

    /**
	 * @param area the area to set in square miles
	 */
    public void setArea(double area);

    /**
	 * @param area the area to set with units provided
	 */
    public void setArea(double area, String units);

    /**
	 * @param people the people to set
	 */
    public void setPeople(double people);

    /**
	 * @param pace the pace to set
	 */
    public void setPace(double pace);

    /**
	 * @param spacing the spacing to set
	 */
    public void setSpacing(double spacing);

    /**
	 * @param area the area to set in square miles
	 */
    public void setArea(double area, int targetToRecalculate);

    /**
	 * @param area the area to set with units provided
	 */
    public void setArea(double area, String units, int targetToRecalculate);

    /**
	 * @param people the people to set
	 */
    public void setPeople(double people, int targetToRecalculate);

    /**
	 * @param pace the pace to set
	 */
    public void setPace(double pace, int targetToRecalculate);

    /**
	 * @param spacing the spacing to set
	 */
    public void setSpacing(double spacing, int targetToRecalculate);

    /**
	 * @param time the time to set
	 */
    public void setHours(double time);

    public void setHours(double time, int targetToRecalculate);

    public void setAllButArea(double people, double pace, double spacing, double time) throws BadCalculationException;

    public void setAllButPeople(double area, double pace, double spacing, double time) throws BadCalculationException;

    public void setAll(double area, double people, double pace, double spacing, double time);
}
