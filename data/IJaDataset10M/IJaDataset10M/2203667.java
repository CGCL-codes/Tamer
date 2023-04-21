package database;

/**
 * GraphSegment is designed to be immutable.
 * @author Roo and Joey
 *
 */
public final class GraphSegment {

    private int m_segmentNumber;

    private Double m_proportion;

    private Byte m_dbEnumEqnType;

    private Double m_gradient;

    private Double m_intercept;

    private Double m_frequency;

    private Double m_amplitude;

    private Double m_growthDecayFactor;

    private Byte m_flags;

    GraphSegment() {
    }

    public GraphSegment(int segmentNumber, Double proportion, Byte dbEnumEqnType, Double gradient, Double intercept, Double frequency, Double amplitude, Double growthDecayFactor, Byte flags) {
        m_segmentNumber = segmentNumber;
        m_proportion = proportion;
        m_dbEnumEqnType = dbEnumEqnType;
        m_gradient = gradient;
        m_intercept = intercept;
        m_frequency = frequency;
        m_amplitude = amplitude;
        m_growthDecayFactor = growthDecayFactor;
        m_flags = flags;
    }

    /**
     * @return Returns the amplitude.
     */
    public Double getAmplitude() {
        return m_amplitude;
    }

    /**
     * @return Returns the dbEnumEqnType.
     */
    public Byte getDbEnumEqnType() {
        return m_dbEnumEqnType;
    }

    /**
     * @return Returns the frequency.
     */
    public Double getFrequency() {
        return m_frequency;
    }

    /**
     * @return Returns the gradient.
     */
    public Double getGradient() {
        return m_gradient;
    }

    /**
     * @return Returns the growthDecayFactor.
     */
    public Double getGrowthDecayFactor() {
        return m_growthDecayFactor;
    }

    /**
     * @return Returns the intercept.
     */
    public Double getIntercept() {
        return m_intercept;
    }

    /**
     * @return Returns the proportion.
     */
    public Double getProportion() {
        return m_proportion;
    }

    /**
     * @return Returns the segmentNumber.
     */
    public int getSegmentNumber() {
        return m_segmentNumber;
    }

    /**
     * @param amplitude The amplitude to set.
     */
    protected void setAmplitude(Double amplitude) {
        m_amplitude = amplitude;
    }

    /**
     * @param dbEnumEqnType The dbEnumEqnType to set.
     */
    protected void setDbEnumEqnType(Byte dbEnumEqnType) {
        m_dbEnumEqnType = dbEnumEqnType;
    }

    /**
     * @param frequency The frequency to set.
     */
    protected void setFrequency(Double frequency) {
        m_frequency = frequency;
    }

    /**
     * @param gradient The gradient to set.
     */
    protected void setGradient(Double gradient) {
        m_gradient = gradient;
    }

    /**
     * @param growthDecayFactor The growthDecayFactor to set.
     */
    protected void setGrowthDecayFactor(Double growthDecayFactor) {
        m_growthDecayFactor = growthDecayFactor;
    }

    /**
     * @param intercept The intercept to set.
     */
    protected void setIntercept(Double intercept) {
        m_intercept = intercept;
    }

    /**
     * @param proportion The proportion to set.
     */
    protected void setProportion(Double proportion) {
        m_proportion = proportion;
    }

    /**
     * @param segmentNumber The segmentNumber to set.
     */
    protected void setSegmentNumber(int segmentNumber) {
        m_segmentNumber = segmentNumber;
    }

    public void setFlags(Byte flags) {
        m_flags = flags;
    }

    public Byte getFlags() {
        return m_flags;
    }
}
