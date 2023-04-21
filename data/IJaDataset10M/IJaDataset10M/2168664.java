package com.inetmon.jn.graph.data;

import java.awt.Color;
import java.util.Iterator;
import com.inetmon.jn.graph.IGraphConstant;

/**
 * @author Nicolas Parcollet 
 * 
 * Class that handle the use of statistic data.
 * Statistic data are usually represented bye pie chart or repartition
 * bar chart (%). They are mainly a list of couple Name-Value.
 */
public class StatisticalData extends AbstractData<Statistic> {

    /**
     * Internal ID
     */
    private static final long serialVersionUID = 3546363946195170614L;

    /**
     * Actual total amount of all the values. Actually this total is
     * periodically reset to 0. This is used to see a a view of the datas at a
     * precise time. This total is used to compute percentages on the individual
     */
    protected double valueTotal;

    /**
     * Cumulated value of the datas obtained since the la call to reset or since
     * the construction. Used to show percentage on cumulated values. Comment
     */
    protected double cumulTotal;

    /**
     * Access the actual total value
     * 
     * @return total amount of all value
     */
    public double getValueTotal() {
        return valueTotal;
    }

    /**
     * Acces the cumulated value since the beginning
     * 
     * @return current cumul value
     */
    public double getCumulTotal() {
        return cumulTotal;
    }

    /**
     * Find a specific statistic using a given label. Label are used as key and
     * are unique by construction.
     * 
     * @param label
     *            The label of the statisitc to find.
     * @return The found category, or null if none is found
     */
    public Statistic findStatistic(String label) {
        Iterator<Statistic> iterator = (Iterator<Statistic>) iterator();
        while (iterator.hasNext()) {
            Statistic e = iterator.next();
            if (e.label == label) return e;
        }
        return null;
    }

    /**
     * Default constructor of the class
     */
    public StatisticalData() {
        super();
        valueTotal = 0;
        cumulTotal = 0;
    }

    /**
     * Obtain the label of the statistic at index i in the vector This function
     * is usuless and is implemented to keep a constant structure
     * 
     * @param i
     *            Index in the vector
     * @return label of the specified statistic
     */
    public String getLabel(int i) {
        return ((Statistic) get(i)).label;
    }

    /**
     * Obtain the label of the statistic wich label is lab. This function is
     * usuless and is implemented to keep a constant structure
     * 
     * @param lab
     *            Label from which we want the label
     * @return label of the specified statistic
     */
    public String getLabel(String lab) {
        return findStatistic(lab).label;
    }

    /**
     * Get the value of a specified statistic
     * 
     * @param i
     *            Index in the vector
     * @return value of the specified statistic
     */
    public double getValue(int i) {
        return ((Statistic) get(i)).value;
    }

    /**
     * Get the value of a specified statistic
     * 
     * @param lab
     *            Label of the statistic
     * @return value of the specified statistic
     */
    public double getValue(String lab) {
        return findStatistic(lab).value;
    }

    /**
     * Get the red composant of a specified statistic
     * 
     * @param i
     *            Index in the vector
     * @return red composant of the specified statistic
     */
    public Color getColor(int i) {
        return ((Statistic) get(i)).color;
    }

    /**
     * Get the red composant of a specified statistic
     * 
     * @param lab
     *            Label of the statistic
     * @return red composant of the specified statistic
     */
    public Color getColor(String lab) {
        return findStatistic(lab).color;
    }

    /**
     * Get the cumuled value of a specified statistic
     * 
     * @param i
     *            Index in the vector
     * @return cumuled value of the specified statistic
     */
    public double getCumul(int i) {
        return ((Statistic) get(i)).cumul;
    }

    /**
     * Get the cumuled value of a specified statistic
     * 
     * @param lab
     *            Label of the statistic
     * @return cumuled value of the specified statistic
     */
    public double getCumul(String lab) {
        Statistic s = findStatistic(lab);
        return s.cumul;
    }

    /**
     * Change the label of a statistic. Only if not already used!
     * 
     * @param i
     *            Index in the vector
     * @param label
     *            New label to set
     */
    public void setLabel(int i, String label) {
        if (null == findStatistic(label)) {
            ((Statistic) get(i)).label = label;
        }
    }

    /**
     * Change the label of a statistic. Only if not already used!
     * 
     * @param lab
     *            Label of the statistic to change
     * @param label
     *            New label to set
     */
    public void setLabel(String lab, String label) {
        if (null == findStatistic(label)) {
            findStatistic(lab).label = label;
        }
    }

    /**
     * Change the color of a statistic
     * 
     * @param i
     *            Index in the vector
     * @param color
     *            New color to set
     */
    public void setColor(int i, Color color) {
        ((Statistic) get(i)).color = color;
        rebuildGraph();
    }

    /**
     * Change the color of a statistic
     * 
     * @param lab
     *            Label of the statistic to change
     * @param color
     *            New color to set
     */
    public void setColor(String lab, Color color) {
        findStatistic(lab).color = color;
        rebuildGraph();
    }

    /**
     * Add a value to the current value of a statistic
     * 
     * @param i
     *            Index in the vector
     * @param value
     *            The value to be added
     */
    public void addValue(int i, double value) {
        Statistic s = (Statistic) get(i);
        s.value += value;
        s.cumul += value;
        valueTotal += value;
        cumulTotal += value;
    }

    /**
     * Add a value to the current value of a statistic
     * 
     * @param lab
     *            Label of the category
     * @param value
     *            The value to be added
     */
    public void addValue(String lab, double value) {
        Statistic s = findStatistic(lab);
        if (value - s.cumul == 0) {
        } else {
            s.value = (int) value - s.cumul;
            s.cumul = (int) value;
            cumulTotal = value;
        }
    }

    /**
     * Add a value to the current value of a statistic
     * 
     * @param lab
     *            Label of the category
     * @param value
     *            The value to be added
     */
    public void addValueCum(String lab, double value) {
        Statistic s = findStatistic(lab);
        s.value = (int) value;
        s.cumul = s.cumul + (int) value;
        cumulTotal += value;
    }

    /**
     * Add a new statistic in the vector. Default value are set for value, cumul
     * and color. If statistic already exist, this call has no effect.
     * Registered graph are rebuild after this add.
     * 
     * @param label
     *            Label of the statistic
     */
    public void addStatistic(String label) {
        Statistic e = findStatistic(label);
        if (e == null) {
            e = new Statistic();
            e.label = label;
            e.value = 0;
            e.cumul = 0;
            e.color = IGraphConstant.COLORS[size() % IGraphConstant.COLORS.length];
            add(e);
            rebuildGraph();
        }
    }

    /**
     * Remove an existing statistic. Does nothing if the statistic doesn t
     * exist. Registered graph are rebuild after that.
     * 
     * @param label
     *            LAbel of the statistic
     */
    public void removeStatistic(String label) {
        Statistic s = findStatistic(label);
        if (s != null) {
            cumulTotal -= s.cumul;
            valueTotal -= s.value;
            remove(s);
            rebuildGraph();
        }
    }

    /**
     * Reset all the datas contained in the existing categories. All values are
     * set to 0
     */
    public void reset() {
        Iterator<Statistic> iterator = iterator();
        while (iterator.hasNext()) {
            Statistic s = iterator.next();
            if (s.label.equals("TCP")) {
                System.out.println("cumulTCP : " + s.cumul);
            }
            s.value = 0;
            s.cumul = 0;
            if (s.label.equals("TCP")) {
                System.out.println("afterTCP : " + s.cumul);
            }
        }
        valueTotal = 0;
        cumulTotal = 0;
    }

    public void update() {
        computeValueTotal();
        refreshGraph();
        Iterator<Statistic> iterator = iterator();
        while (iterator.hasNext()) {
            Statistic e = iterator.next();
            e.value = 0;
        }
    }

    public void computeValueTotal() {
        valueTotal = 0;
        Iterator<Statistic> iterator = iterator();
        while (iterator.hasNext()) {
            Statistic e = iterator.next();
            valueTotal += e.value;
        }
    }
}
