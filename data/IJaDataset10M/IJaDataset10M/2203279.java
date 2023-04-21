package moduledefault.classify.knn;

import annotations.ClassifierModuleAnnotation;
import annotations.ModuleAnnotation;
import interfaces.HostInterface;
import interfaces.mining.classify.ClassifierModuleInterface;
import java.util.Collection;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import moduledefault.classify.knn.view.jpanel.JPanelKnn;

/**
 *
 * @author Administrador
 */
@ModuleAnnotation(name = "KNN - K-Nearest Neighbor")
@ClassifierModuleAnnotation
public class KnnClassifierModule implements ClassifierModuleInterface {

    private int K = Knn.k_default;

    private String distanceFunction = Knn.distance_function;

    private String inferenceRule = Knn.inference_rule;

    private HostInterface hi = null;

    private JDialog jDialogConfig = null;

    private JTextArea jTextArea = null;

    private Knn knn = null;

    public KnnClassifierModule() {
        FacadeKnnClassifierModule.setKnnClassifierModule(this);
    }

    public JPanel getPainelConfig() {
        return new JPanelKnn();
    }

    public void setJDialogConfig(JDialog jDialogConfig) {
        this.jDialogConfig = jDialogConfig;
    }

    public void setTextArea(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
    }

    public JPanel getCreatedModel() {
        return null;
    }

    public void train(Object[][] input, Object[] output) {
        knn.train(input, output);
    }

    public Object test(Object[] inputTest) {
        return knn.test(inputTest);
    }

    public ClassifierModuleInterface clone() {
        try {
            return (ClassifierModuleInterface) super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    public void createInstanceClissify(String[] attributes, Collection classes) {
        jTextArea.append("KNN - K-Nearest Neighbor");
        knn = new Knn();
        knn.setDistanceFunction(distanceFunction);
        knn.setInferenceRule(inferenceRule);
        knn.setK(K);
    }

    public void setHost(HostInterface hostInterface) {
        hi = hostInterface;
    }

    public JDialog getJDialogConfig() {
        return jDialogConfig;
    }

    public int getK() {
        return K;
    }

    public void setK(int K) {
        this.K = K;
    }

    public String getDistanceFunction() {
        return distanceFunction;
    }

    public void setDistanceFunction(String distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    public String getInferenceRule() {
        return inferenceRule;
    }

    public void setInferenceRule(String inferenceRule) {
        this.inferenceRule = inferenceRule;
    }

    public JTextArea getJTextArea() {
        return jTextArea;
    }
}
