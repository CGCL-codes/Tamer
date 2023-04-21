package org.primordion.user.app.solarsystem;

import org.primordion.xholon.base.IXholon;
import org.primordion.xholon.base.XholonWithPorts;

/**
 * Sunspot active object.
 * This class simulates the observed 11 year sunspot cycle.
 * This optional active object (AO) can be pasted into the model at runtime.
 * To do this, copy the following XML to the clipboard,
 * and paste it as the last child of Sun:
 * <p>&lt;SunspotCycleAO/&gt;</p>
 * <p>"The Sun is a weakly variable star and its luminosity therefore fluctuates.
 * The major fluctuation is the eleven-year solar cycle (sunspot cycle),
 * which causes a periodic variation of about ±0.1%.
 * Any other variation over the last 200–300 years is thought to be much smaller than this."</p>
 * <p>n = 50•sin{2π/11[(t – 1750) + 11/4]} + 60</p>
 * @see <a href="http://en.wikipedia.org/wiki/Solar_luminosity">Solar luminosity</a>
 * @see <a href="http://au.answers.yahoo.com/question/index?qid=20110522003213AA7CalY">sunspot cycle calculation</a>
 * @author ken
 *
 */
public class SunspotCycleAO extends XholonWithPorts {

    /** The star (ex: Sun) where the sunspots are located. */
    private IXholon star = null;

    private double baseTemperature = 0.0;

    private double minSunspots = 0.0;

    private double multiplier = 1.0;

    public void postConfigure() {
        if (star == null) {
            star = this.getParentNode();
        }
        baseTemperature = ((Star) star).getTemperature();
        minSunspots = calcNumSunspots(6);
        double maxSunspots = calcNumSunspots(1);
        double minTemperature = baseTemperature * -0.001;
        double maxTemperature = baseTemperature * 0.001;
        multiplier = (maxTemperature - minTemperature) / (maxSunspots - minSunspots);
        super.postConfigure();
    }

    public void act() {
        double temperatureDiff = calcTemperatureDiff(getApp().getTimeStep());
        ((Star) star).setTemperature(baseTemperature + temperatureDiff);
        super.act();
    }

    /**
	 * Calculate an average temperature difference for a specific year.
	 * @param year ex: 1751 or 1980
	 * @return The difference in the solar constant.
	 */
    protected double calcTemperatureDiff(int year) {
        return (calcNumSunspots(year) - minSunspots) * multiplier;
    }

    /**
	 * Calculate the number of sunspots for a specific year.
	 * @param year ex: 1750 or 2011
	 * @return A number of sunspots.
	 */
    protected double calcNumSunspots(int year) {
        return 50 * Math.sin(2 * Math.PI / 11 * (year + (11 / 4))) + 60;
    }

    public IXholon getStar() {
        return star;
    }

    public void setStar(IXholon star) {
        this.star = star;
    }
}
