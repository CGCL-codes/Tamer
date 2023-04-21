package skellib.examples;

import skellib.core.Source;
import skellib.data.LibSVMSource;
import skellib.model.kernel.KernelModel;

public class PredictWithADE {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
        String model_file = "./data/pyrim_rbf.model";
        KernelModel model = new KernelModel(model_file);
        String test_data = "./data/pyrim.svm";
        Source data = new LibSVMSource(test_data);
        for (int i = 0; i < data.getSize(); i++) {
            float p = model.predict(data.getInstance(i));
            float t = data.getValue(i);
            System.out.println("True: " + t + " Predicted: " + p);
        }
    }
}
