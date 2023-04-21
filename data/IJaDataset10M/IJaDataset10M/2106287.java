package org.sinaxe.runtime;

import org.dom4j.Element;
import org.sinaxe.*;

public class SinaxeFactory {

    public SinaxeFactory() {
    }

    public RuntimeComponent createComponent(String name, String className, RuntimeComponent parent) throws SinaxeException {
        return new ComponentImpl(name, className, parent);
    }

    public RuntimeComposite createComposite(String name, RuntimeComponent parent) {
        return new CompositeImpl(name, parent);
    }

    public SinaxeProperty createSinaxeProperty(String name, String value) {
        return new PropertyImpl(name, value);
    }

    public SinaxeQuery createSinaxeQuery(String text, RuntimeComponent parent) throws SinaxeException {
        return new QueryImpl(text, parent);
    }

    public SinaxeUpdate createSinaxeUpdate(Element elem, RuntimeComponent parent) throws SinaxeException {
        return createSinaxeUpdate(elem, parent, -1);
    }

    public SinaxeUpdate createSinaxeUpdate(Element elem, RuntimeComponent parent, int lineNum) throws SinaxeException {
        return new UpdateImpl(elem, parent, lineNum);
    }
}
