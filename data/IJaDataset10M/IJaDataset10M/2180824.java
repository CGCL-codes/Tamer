package tutorials.core;

import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

/**
 * Shows how to create a SparseInstance. A SparseInstance has a default value of
 * 0 for all it attributes, but you can set some of them to other values. Just
 * like the DenseInstance, the SparseInstance also has an optional class label.
 * 
 * @author Thomas Abeel
 * 
 */
public class TutorialSparseInstance {

    /**
     * Shows how to construct a SparseInstance.
     */
    public static void main(String[] args) {
        Instance instance = new SparseInstance(10);
        instance.put(1, 1.0);
        instance.put(3, 2.0);
        instance.put(7, 4.0);
    }
}
