package weka.classifiers.neural.lvq.initialise;

import weka.classifiers.neural.common.RandomWrapper;
import weka.classifiers.neural.lvq.model.CodebookVector;
import weka.classifiers.neural.lvq.vectordistance.AttributeDistance;
import weka.classifiers.neural.lvq.vectordistance.DistanceFactory;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Date: 25/05/2004
 * File: CommonRandomInitialiser.java
 * 
 * @author Jason Brownlee
 *
 */
public abstract class CommonInitialiser implements ModelInitialiser {

    protected final RandomWrapper rand;

    protected final Instances trainingInstances;

    protected final int numClasses;

    protected final int numAttributes;

    protected final int classIndex;

    protected final int totalInstances;

    public CommonInitialiser(RandomWrapper aRand, Instances aInstances) {
        rand = aRand;
        trainingInstances = aInstances;
        numClasses = trainingInstances.classAttribute().numValues();
        totalInstances = trainingInstances.numInstances();
        classIndex = trainingInstances.classIndex();
        numAttributes = trainingInstances.numAttributes();
    }

    public void initialiseCodebookVector(CodebookVector aCodebookVector) {
        double[] attributes = getAttributes();
        for (int j = 0; j < attributes.length; j++) {
            if (Instance.isMissingValue(attributes[j])) {
                attributes[j] = rand.getRand().nextDouble();
            }
        }
        aCodebookVector.initialise(attributes, classIndex, numClasses);
    }

    public abstract double[] getAttributes();

    public AttributeDistance[] getAttributeDistanceList() {
        return DistanceFactory.getAttributeDistanceList(trainingInstances);
    }

    public String[] getClassLables() {
        String[] classLabels = new String[numClasses];
        for (int i = 0; i < classLabels.length; i++) {
            classLabels[i] = trainingInstances.classAttribute().value(i);
        }
        return classLabels;
    }

    protected int makeRandomSelection(int aTotalChoices) {
        int selection = rand.getRand().nextInt();
        selection = Math.abs(selection);
        selection = (selection % aTotalChoices);
        return selection;
    }
}
