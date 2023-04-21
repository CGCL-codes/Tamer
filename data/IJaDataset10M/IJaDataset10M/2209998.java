package preprocessing.methods.FeatureSelection.WekaFeatureSelectionMethods;

import game.data.preprocessing.PreprocessingStorage;
import game.utils.Exceptions.InvalidArgument;
import game.utils.Exceptions.NonExistingAttributeException;
import preprocessing.methods.BasePreprocessor;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.Ranker;
import weka.attributeSelection.ReliefFAttributeEval;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by IntelliJ IDEA.
 * User: pilnya1
 * Date: 8.11.2010
 * Time: 20:46:28
 * To change this template use File | Settings | File Templates.
 */
public class ReliefFMethod extends BasePreprocessor {

    private ASEvaluation eval = new ReliefFAttributeEval();

    private ASSearch ranker = new Ranker();

    public int[] getAttributeSetGeneratedOrder() {
        return attributeSetGeneratedOrder;
    }

    private int[] attributeSetGeneratedOrder;

    /**
     * Copies data from PreprocessingStorage into Instances which are directly usable in Weka.
     *
     * @param store Preprocessing Store holding data to transform
     * @return Data transformed into weka inner format
     */
    protected Instances preprocessingStorageToInstances(PreprocessingStorage store) {
        FastVector attributes = new FastVector();
        int[] inputAttributes = store.getInputAttributesIndices();
        boolean[] useAttribute = new boolean[inputAttributes.length];
        int numImportedAttributes = 0;
        for (int i = 0; i < inputAttributes.length; i++) {
            if (store.getAttributeType(inputAttributes[i]) != PreprocessingStorage.DataType.NUMERIC) {
                useAttribute[i] = false;
                continue;
            }
            attributes.addElement(new Attribute(store.getAttributeName(inputAttributes[i])));
            useAttribute[i] = true;
            numImportedAttributes++;
        }
        if (numImportedAttributes == 0) {
            return null;
        }
        int[] outputAttributes = store.getOutputAttributesIndices();
        FastVector outputNames = new FastVector();
        for (int outputAttribute : outputAttributes) {
            outputNames.addElement(store.getAttributeName(outputAttribute));
        }
        attributes.addElement(new Attribute("Output", outputNames));
        Instances dataset = null;
        try {
            dataset = new Instances("Automatic Preprocessing Dataset", attributes, store.getAttributeLength(0));
            dataset.setClassIndex(attributes.size() - 1);
            for (int inst = 0; inst < store.getAttributeLength(0); inst++) {
                double[] instanceVector = new double[numImportedAttributes + 1];
                int k = 0;
                for (int i = 0; i < inputAttributes.length; i++) {
                    if (!useAttribute[i]) {
                        continue;
                    }
                    Object o = 1.0;
                    try {
                        o = store.getDataItem(inputAttributes[i], inst);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        logger.error("Error vole! <- Miro, ty ses kanec :) Jsem si malem plivnul cokoladu na monitor :)))");
                        e.printStackTrace();
                    }
                    try {
                        double d;
                        d = (Double) o;
                        instanceVector[k] = d;
                        k++;
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                        logger.fatal("Error vole! Attribute " + inputAttributes[i] + ":" + inst + " value: " + o.toString() + "; reason: " + e.getMessage() + "\n");
                        System.out.flush();
                        System.runFinalization();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        System.err.flush();
                        System.out.flush();
                        System.exit(-1);
                    }
                }
                for (int i = 0; i < outputAttributes.length; i++) {
                    if ((Double) store.getDataItem(outputAttributes[i], inst) == 1.0) {
                        instanceVector[instanceVector.length - 1] = i;
                        break;
                    }
                }
                Instance newInstance = new Instance(1.0, instanceVector);
                dataset.add(newInstance);
            }
        } catch (InvalidArgument invalidArgument) {
            invalidArgument.printStackTrace();
            logger.warn("Preprocessing storage appears to be empty");
            return null;
        }
        return dataset;
    }

    @Override
    public boolean run() throws NonExistingAttributeException {
        Instances instances = preprocessingStorageToInstances(store);
        int[] attributeSet = null;
        try {
            eval.buildEvaluator(instances);
            attributeSet = ranker.search(eval, instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (attributeSet == null) {
            return false;
        }
        attributeSetGeneratedOrder = attributeSet;
        System.out.printf(" Results for ReliefFMethod()   : ");
        for (int i = 0; i < attributeSet.length; i++) {
            System.out.printf("%d ", attributeSet[i] + 1);
        }
        System.out.printf("\n");
        return true;
    }

    @Override
    public void finish() {
        attributeSetGeneratedOrder = null;
    }

    @Override
    public boolean isApplyOnTestingData() {
        return true;
    }
}
