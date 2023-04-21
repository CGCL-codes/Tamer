package gr.konstant.transonto.kb.powder;

import java.net.URI;
import java.util.List;
import org.w3c.dom.Element;
import gr.konstant.transonto.exception.*;
import gr.konstant.transonto.interfaces.Annotation;
import gr.konstant.transonto.interfaces.Anything;
import gr.konstant.transonto.interfaces.ExprValue;
import gr.konstant.transonto.interfaces.impl.AnnotationImpl;
import gr.konstant.transonto.interfaces.impl.ValuationBoole;

public class Attribution extends AnnotationImpl implements Annotation {

    private final Element element;

    /** 
	 * Create an attribution property from an existing element
	 * @param doc
	 * @param label
	 * @param value
	 */
    public Attribution(PowderXML doc, Element element) {
        super(doc, ValuationBoole.TRUE);
        this.element = element;
    }

    @Override
    public String toString() {
        return element.toString();
    }

    @Override
    public PowderXML livesIn() {
        return (PowderXML) super.livesIn();
    }

    @Override
    public List<ExprValue> getAnnotation(URI key) throws UnsupportedFeatureException {
        throw new UnsupportedFeatureException();
    }

    @Override
    public List<Annotation> getAnnotation() throws UnsupportedFeatureException {
        throw new UnsupportedFeatureException();
    }

    @Override
    public Anything getObject() {
        return null;
    }

    @Override
    public URI getKey() {
        System.err.println("node name: " + element.getNodeName());
        System.err.println("NS URI: " + element.getNamespaceURI());
        return URI.create(element.getNodeName());
    }

    @Override
    public ExprValue getValue() {
        return null;
    }
}
