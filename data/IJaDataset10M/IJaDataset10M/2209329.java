package org.quantumleaphealth.model.trial;

import java.util.Arrays;
import org.quantumleaphealth.ontology.CharacteristicCode;

/**
 * A criterion that is compared to a patient's 
 * treatment regimen characteristic.
 * The superclass' fields store the following:<ul>
 * <li><code>parent</code>: the type of therapy (e.g., chemo, hormone)</li>
 * <li><code>setOperation</code>, <code>requirement</code>, <code>isOther</code>: the agents</li>
 * <li><code>invert</code>: whether or not the count is a maximum</li>
 * </ul> 
 * @author Tom Bechtold
 * @version 2008-02-23
 */
public class TreatmentRegimenCriterion extends AssociativeCriterion {

    /**
     * The settings requirement
     */
    private CharacteristicCode[] settings;

    /**
     * The attributes of the procedure
     */
    private CharacteristicCode[] qualifiers;

    /**
     * The number of regimens required.
     * This is combined with <code>super.isInvert()</code>.
     * @see super{@link #invert()}
     */
    private int count;

    /**
     * The minimum number required for a regimen to be counted
     * or <code>0</code> for no minimum
     */
    private int threshold;

    /**
     * The threshold's unit of measure
     * or <code>null</code> for no threshold
     */
    private CharacteristicCode thresholdUnitOfMeasure;

    /**
     * Returns the settings requirement
     * @return the settings requirement or <code>null</code> for any
     */
    public CharacteristicCode[] getSettings() {
        return settings;
    }

    /**
     * Sets the settings requirement
     * @param settings the settings or <code>null</code> for any
     */
    public void setSettings(CharacteristicCode[] settings) {
        this.settings = settings;
    }

    /**
     * @return the qualifiers
     */
    public CharacteristicCode[] getQualifiers() {
        return qualifiers;
    }

    /**
     * @param qualifiers the qualifiers to set
     */
    public void setQualifiers(CharacteristicCode[] qualifiers) {
        this.qualifiers = qualifiers;
    }

    /**
     * Returns the number of regimens required.
     * @return number of regimens required
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the number of regimens required.
     * @param count number of regimens required
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Toggles the requirement from minimum to maximum
     * or from any to none.
     * This method ensures that the minimum count is at least one.
     * @see org.quantumleaphealth.model.trial.AbstractCriterion#invert()
     */
    @Override
    public void invert() {
        if (isInvert() && (count <= 0)) count = 1; else if (!isInvert() && (count <= 1)) count = 0;
        super.invert();
    }

    /**
     * @return the minimum number required for a regimen to be counted
     *         or <code>0</code> for no minimum
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * @param threshold the minimum number required for a regimen 
     *        to be counted or <code>0</code> for no minimum
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * @return the threshold's unit of measure
     *         or <code>null</code> for no threshold
     */
    public CharacteristicCode getThresholdUnitOfMeasure() {
        return thresholdUnitOfMeasure;
    }

    /**
     * @param thresholdUnitOfMeasure the threshold's unit of measure
     *        or <code>null</code> for no threshold
     */
    public void setThresholdUnitOfMeasure(CharacteristicCode thresholdUnitOfMeasure) {
        this.thresholdUnitOfMeasure = thresholdUnitOfMeasure;
    }

    /**
     * Returns the contents of the fields
     * @return the contents of the fields
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        builder.deleteCharAt(builder.length() - 1);
        builder.append(isInvert() ? "max=" : "min=").append(count);
        if ((settings != null) && (settings.length > 0)) builder.append(",settings=").append(Arrays.toString(settings));
        if ((qualifiers != null) && (qualifiers.length > 0)) builder.append(",qualifiers=").append(Arrays.toString(qualifiers));
        if (thresholdUnitOfMeasure != null) builder.append(",threshold=").append(threshold).append('[').append(thresholdUnitOfMeasure).append(']');
        return builder.append('}').toString();
    }

    /**
     * Version UID for serialization
     */
    private static final long serialVersionUID = 6348944588797800176L;
}
