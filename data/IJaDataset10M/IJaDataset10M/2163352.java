package moduledefault.classify.knn;

import javax.swing.JDialog;

/**
 *
 * @author evaristowb
 */
public class FacadeKnnClassifierModule {

    private static KnnClassifierModule knnClassifierModule;

    private FacadeKnnClassifierModule() {
    }

    public static KnnClassifierModule getKnnClassifierModule() {
        return knnClassifierModule;
    }

    public static void setKnnClassifierModule(KnnClassifierModule knnClassifierModule) {
        FacadeKnnClassifierModule.knnClassifierModule = knnClassifierModule;
    }

    public static JDialog getJDialogConfig() {
        return knnClassifierModule.getJDialogConfig();
    }

    public static int getK() {
        return knnClassifierModule.getK();
    }

    public static void setK(int K) {
        knnClassifierModule.setK(K);
    }

    public static String getDistanceFunction() {
        return knnClassifierModule.getDistanceFunction();
    }

    public static void setDistanceFunction(String distanceFunction) {
        knnClassifierModule.setDistanceFunction(distanceFunction);
    }

    public static String getInferenceRule() {
        return knnClassifierModule.getInferenceRule();
    }

    public static void setInferenceRule(String inferenceRule) {
        knnClassifierModule.setInferenceRule(inferenceRule);
    }
}
