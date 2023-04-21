package configuration.models.ensemble;

import org.ytoh.configurations.annotations.Component;

/**
 * Configures the ensembling strategy using clustering.
 * There are more definitions how the configuration can look like:
 * when baseModelsDef is
 * - PREDEFINED: all base models have their own configuration bean - added to the list baseModelCfgs
 * - RANDOM: baseModelCfgs contains cfg beans of all models implemented so far. Base models are randomly selected from this list respecting the allowed flag
 * - EVOLVED: the same as random, just allowed models are evolved by genetic algorithm to find best ensemble
 * - UNIFORM: baseModelCfgs contains only one configuration bean, all generated models are of the same type
 * - UNIFORM_RANDOM: baseModelCfgs contains cfg beans of all models implemented so far, one cfg bean is randomly selected and all models are of this type
 */
@Component(name = "EnsembleClusteringConfig", description = "Configuration of the Ensemble clustering strategy")
public class EnsembleClusteringConfig extends ModelEnsembleConfigBase {

    /**
     * Number of clusters used for the clustering algorithm
     */
    private int numberOfClusters = 3;

    /**
     * The size of the part of training data that will be used for training the base models
     */
    private int trainingBaseModelPercent = 70;

    /**
     * Constructor
     */
    public EnsembleClusteringConfig() {
        super();
    }

    /**
     * Get the number of clusters
     *
     * @return Number of clusters
     */
    public int getNumberOfClusters() {
        return numberOfClusters;
    }

    /**
     * Set the number of clusters used in the clustering algorithm
     *
     * @param numberOfClusters Number of clusters
     */
    public void setNumberOfClusters(int numberOfClusters) {
        this.numberOfClusters = numberOfClusters;
    }

    /**
     * Get the size of training data for base models
     *
     * @return Percent of data
     */
    public int getTrainingBaseModelPercent() {
        return trainingBaseModelPercent;
    }

    /**
     * Set the size of training data for base models
     *
     * @param trainingBaseModelPercent Percent of data
     */
    public void setTrainingBaseModelPercent(int trainingBaseModelPercent) {
        this.trainingBaseModelPercent = trainingBaseModelPercent;
    }
}
