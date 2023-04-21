package uk.ac.shef.oak.iracema.annotation.ml;

import java.util.List;
import uk.ac.shef.oak.iracema.IracemaException;
import uk.ac.shef.oak.iracema.annotation.DefaultAbstractAnnotator;
import uk.ac.shef.oak.iracema.classifier.ClassifierContainer;
import uk.ac.shef.oak.iracema.document.Document;

public abstract class MachineLearningAnnotator extends DefaultAbstractAnnotator {

    public abstract List<ClassifierContainer> learn(Document[] trainCorpus) throws IracemaException;
}
