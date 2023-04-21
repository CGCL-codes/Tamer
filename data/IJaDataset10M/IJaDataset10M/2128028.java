package eulergui.inference.codd;

import eulergui.inference.AbstractInferenceEngine;
import eulergui.inference.InferenceEngine;
import eulergui.inference.InferenceEngineFactory;
import eulergui.project.N3Source;
import eulergui.project.Project;
import n3_project.helpers.CoddHelper;
import unif.ITripleStore;

public class CoddInferenceEngine extends AbstractInferenceEngine implements InferenceEngine {

    public CoddInferenceEngine(InferenceEngineFactory inferenceEngineFactory, Project project) {
        super(inferenceEngineFactory, project);
    }

    @Override
    public N3Source launch() {
        try {
            return CoddHelper.getInference(getProject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ITripleStore getTripleStore() {
        return null;
    }
}
