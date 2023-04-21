package com.marmoush.jann.layer;

/**
 * The Interface ILayerSV.
 */
public interface ILayerSV extends ILayer {

    /**
     * Gets the learn rate.
     *
     * @return the learn rate
     */
    public abstract double getLearnRate();

    /**
     * Sets the learn rate.
     *
     * @param learnRate the new learn rate
     */
    public abstract void setLearnRate(double learnRate);
}
