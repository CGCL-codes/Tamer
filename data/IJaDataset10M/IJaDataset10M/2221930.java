package uk.ac.ebi.pride.data.core;

/**
 * The method of product ion selection and activation in a
 * precursor ion scan.
 * User: rwang
 * Date: 05-Feb-2010
 * Time: 14:11:06
 */
public class Product {

    private ParamGroup isolationWindow = null;

    public Product(ParamGroup iswin) {
        this.isolationWindow = iswin;
    }

    public ParamGroup getIsolationWindow() {
        return isolationWindow;
    }

    public void setIsolationWindow(ParamGroup isolationWindow) {
        this.isolationWindow = isolationWindow;
    }
}
