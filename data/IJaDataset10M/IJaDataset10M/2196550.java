package gr.konstant.transonto.kb.mem;

import java.net.URI;
import java.util.List;
import gr.konstant.transonto.exception.*;
import gr.konstant.transonto.interfaces.Annotation;
import gr.konstant.transonto.interfaces.Anything;
import gr.konstant.transonto.interfaces.ExprValue;
import gr.konstant.transonto.interfaces.KnowledgeBase;
import gr.konstant.transonto.interfaces.Valuation;
import gr.konstant.transonto.interfaces.impl.AnnotationImpl;

public class MemAnnotation extends AnnotationImpl implements Annotation {

    public MemAnnotation(KnowledgeBase kb, Valuation v) {
        super(kb, v);
    }

    @Override
    public List<ExprValue> getAnnotation(URI key) throws UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public List<Annotation> getAnnotation() throws UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public URI getKey() {
        return null;
    }

    @Override
    public Anything getObject() {
        return null;
    }

    @Override
    public ExprValue getValue() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
