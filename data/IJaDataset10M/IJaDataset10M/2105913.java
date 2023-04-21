package dndbeans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * 
 * 
 * 
 * $Date: 2006/11/16 02:22:14 $<br>
 * @author tedberg
 * @author $Author: tedberg $
 * @version $Revision: 1.4 $
 * @since Oct 4, 2003
 */
public class ExperienceBean {

    private boolean cohort;

    private long experience;

    private boolean included;

    private long level;

    private int levelAdjustment;

    private String name;

    private transient PropertyChangeSupport propertyChangeListeners = new PropertyChangeSupport(this);

    public ExperienceBean() {
    }

    public void addExperience(long value) {
        long target = getExperienceToLevel();
        setExperience(getExperience() + value);
        if (getExperience() > target) level = calcLevelForExperience(getExperience());
    }

    public long getExperienceToLevel() {
        double target = getEcl() + 1;
        return (long) (target * ((target - 1.0) / 2.0) * 1000);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeListeners.addPropertyChangeListener(l);
    }

    protected long calcLevelForExperience(long xp) {
        long level = 1;
        long runningTotal = 0;
        while (runningTotal <= xp) {
            runningTotal += (level * 1000);
            level++;
        }
        return Math.max(1, level - 1);
    }

    public long getEcl() {
        return getLevel() + getLevelAdjustment();
    }

    public long getExperience() {
        return experience;
    }

    public long getLevel() {
        return level;
    }

    public long getCalculatedLevel() {
        long a = 2 * getExperience() / 1000;
        long b = (long) ((-1 + Math.sqrt(1 + 4 * a)) / 2) + 1;
        return b;
    }

    public int getLevelAdjustment() {
        return levelAdjustment;
    }

    public String getName() {
        return name;
    }

    public boolean isCohort() {
        return cohort;
    }

    public boolean isIncluded() {
        return included;
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeListeners.removePropertyChangeListener(l);
    }

    public void setCohort(boolean newCohort) {
        boolean oldCohort = cohort;
        cohort = newCohort;
        propertyChangeListeners.firePropertyChange("cohort", new Boolean(oldCohort), new Boolean(newCohort));
    }

    public void setExperience(long newExperience) {
        long oldLevel = getLevel();
        long oldExperience = experience;
        experience = newExperience;
        propertyChangeListeners.firePropertyChange("experience", new Long(oldExperience), new Long(newExperience));
        propertyChangeListeners.firePropertyChange("level", new Long(oldLevel), new Long(getLevel()));
    }

    public void setIncluded(boolean newIncluded) {
        boolean oldIncluded = included;
        included = newIncluded;
        propertyChangeListeners.firePropertyChange("included", new Boolean(oldIncluded), new Boolean(newIncluded));
    }

    public void setLevelAdjustment(int newLevelAdjustment) {
        int oldLevelAdjustment = levelAdjustment;
        levelAdjustment = newLevelAdjustment;
        propertyChangeListeners.firePropertyChange("levelAdjustment", new Integer(oldLevelAdjustment), new Integer(newLevelAdjustment));
        propertyChangeListeners.firePropertyChange("ecl", new Long(oldLevelAdjustment + getLevel()), new Long(getEcl()));
    }

    public void setName(String newName) {
        String oldName = name;
        name = newName;
        propertyChangeListeners.firePropertyChange("name", oldName, newName);
    }

    /**
	 * @param l
	 */
    public void setLevel(long l) {
        level = l;
    }
}
