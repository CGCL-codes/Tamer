package xmitools2.model;

/**
 * Base implementation of XmiFactory.
 * 
 * @author rlechner
 */
public class XmiFactoryImp implements XmiFactory {

    public XmiText createText(String text) {
        return new XmiTextImp(text);
    }

    public XmiElement createElement(String name) {
        return new XmiElementImp(name);
    }
}
