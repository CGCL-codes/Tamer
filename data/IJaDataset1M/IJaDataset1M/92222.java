package jlibs.xml.sax;

import org.xml.sax.SAXException;

/**
 * ObjectInputSource implementation for Object implementing SAXProducer
 * 
 * @author Santhosh Kumar T
 */
public class SAXInputSource<E extends SAXProducer> extends ObjectInputSource<E> {

    public SAXInputSource(E obj) {
        super(obj);
    }

    @Override
    protected void write(E input, XMLDocument xml) throws SAXException {
        xml.add(input);
    }
}
