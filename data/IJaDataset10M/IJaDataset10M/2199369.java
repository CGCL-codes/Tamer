package shellkk.qiq.jdm.supervised.classification;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.supervised.classification.ClassificationCapability;
import javax.datamining.supervised.classification.ClassificationSettings;
import javax.datamining.supervised.classification.ClassificationSettingsFactory;
import shellkk.qiq.jdm.engine.classification.ClassificationBuildEngine;

public class ClassificationSettingsFactoryImpl implements ClassificationSettingsFactory {

    protected ClassificationBuildEngine engine;

    public ClassificationBuildEngine getEngine() {
        return engine;
    }

    public void setEngine(ClassificationBuildEngine engine) {
        this.engine = engine;
    }

    public ClassificationSettings create() throws JDMException {
        ClassificationSettingsImpl set = new ClassificationSettingsImpl();
        return set;
    }

    public boolean supportsCapability(ClassificationCapability capability) throws JDMException {
        return engine.supportsCapability(capability);
    }

    public boolean supportsCapability(MiningAlgorithm algorithm, ClassificationCapability capability) throws JDMException {
        return engine.supportsCapability(algorithm, capability);
    }
}
