package de.tudresden.inf.lat.jcel.adapter;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * An object of this class is a visitor used to translate an
 * <code>OWLEntity</code> in the OWL API 2 into an <code>OWLEntity</code> in the
 * OWL API 3. This is an auxiliary class used by <code>SimpleTranslator</code>.
 * 
 * @author Julian Mendez
 * 
 */
public class EntityTranslator implements org.semanticweb.owl.model.OWLEntityVisitorEx<OWLEntity> {

    private SimpleTranslator translator = null;

    public EntityTranslator(SimpleTranslator trans) {
        if (trans == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        this.translator = trans;
    }

    public OWLDataFactory getOWLDataFactory() {
        return this.translator.getOWLDataFactory();
    }

    public SimpleTranslator getTranslator() {
        return this.translator;
    }

    @Override
    public OWLEntity visit(org.semanticweb.owl.model.OWLClass cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        return getTranslator().translate(cls);
    }

    @Override
    public OWLEntity visit(org.semanticweb.owl.model.OWLDataProperty dataProperty) {
        if (dataProperty == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        return getTranslator().translate(dataProperty);
    }

    @Override
    public OWLEntity visit(org.semanticweb.owl.model.OWLDataType dataType) {
        if (dataType == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        return getTranslator().translate(dataType);
    }

    @Override
    public OWLEntity visit(org.semanticweb.owl.model.OWLIndividual individual) {
        if (individual == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        return getTranslator().translate(individual);
    }

    @Override
    public OWLEntity visit(org.semanticweb.owl.model.OWLObjectProperty objectProperty) {
        if (objectProperty == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        return getTranslator().translate(objectProperty);
    }
}
