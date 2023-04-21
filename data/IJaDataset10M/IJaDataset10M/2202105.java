package edu.cmu.sphinx.knowledge.acoustic;

import edu.cmu.sphinx.util.SphinxProperties;
import java.util.Map;

/**
 * Generic interface for a loader of acoustic models
 */
interface Loader {

    /**
     * Gets the pool of means for this loader
     *
     * @return the pool
     */
    public Pool getMeansPool();

    /**
     * Gets the pool of means transformation matrices for this loader
     *
     * @return the pool
     */
    public Pool getMeansTransformationMatrixPool();

    /**
     * Gets the pool of means transformation vectors for this loader
     *
     * @return the pool
     */
    public Pool getMeansTransformationVectorPool();

    public Pool getVariancePool();

    /**
     * Gets the variance transformation matrix pool
     *
     * @return the pool
     */
    public Pool getVarianceTransformationMatrixPool();

    public Pool getMixtureWeightPool();

    public Pool getTransitionMatrixPool();

    public Pool getSenonePool();

    /**
     * Returns the HMM Manager for this loader
     *
     * @return the HMM Manager
     */
    public HMMManager getHMMManager();

    /**
     * Returns the map of context indepent units. The map can be
     * accessed by unit name.
     *
     * @return the map of context independent units.
     */
    public Map getContextIndependentUnits();

    /**
     * logs information about this loader
     */
    public void logInfo();

    /**
    * Returns the size of the left context for context dependent
    * units
    *
    * @return the left context size
    */
    public int getLeftContextSize();

    /**
     * Returns the size of the right context for context dependent
     * units
     *
     * @return the left context size
     */
    public int getRightContextSize();

    /**
     * Returns the properties of the loaded AcousticModel.
     *
     * @return the properties of the loaded AcousticModel, or null if
     *   it has no properties
     */
    public SphinxProperties getModelProperties();
}
