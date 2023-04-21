package websiteschema.weka;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 *
 * @author ray
 */
public class WekaUtil {

    private static final Map<String, Classifier> mapClassifiers = new HashMap<String, Classifier>();

    private static final Map<String, Instances> mapInstances = new HashMap<String, Instances>();

    private static final WekaUtil wu = new WekaUtil();

    public static WekaUtil getWekaUtil() {
        return wu;
    }

    public Classifier getClassifier(String model) {
        loadModel(model);
        return mapClassifiers.get(model);
    }

    public Instances getDataSet(String model) {
        loadModel(model);
        return mapInstances.get(model);
    }

    private synchronized void loadModel(String model) {
        if (!mapInstances.containsKey(model) && !mapClassifiers.containsKey(model)) {
            ObjectInputStream ob = null;
            try {
                if (new File(model).exists()) {
                    ob = new ObjectInputStream(new FileInputStream(model));
                } else {
                    InputStream is = WekaUtil.class.getClassLoader().getResourceAsStream(model);
                    ob = new ObjectInputStream(is);
                }
                Classifier c = (Classifier) ob.readObject();
                Instances trainHeader = (Instances) ob.readObject();
                mapInstances.put(model, trainHeader);
                mapClassifiers.put(model, c);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (null != ob) {
                    try {
                        ob.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
