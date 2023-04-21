package configuration.classifiers.ensemble;

import game.classifiers.ensemble.ClassifierCascadeGen;
import org.ytoh.configurations.annotations.Component;

/**
 * Configuration bean of the bagging classifiers ensemble
 */
@Component(name = "ClassifierCascadeGenConfig", description = "Configuration of the Cascade Generalization classifiers ensemble")
public class ClassifierCascadeGenConfig extends EnsembleClassifierConfigBase {

    public ClassifierCascadeGenConfig() {
        super();
        classRef = ClassifierCascadeGen.class;
    }
}
