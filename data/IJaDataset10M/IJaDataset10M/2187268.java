package org.sti2.elly.reasoning.owl;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.sti2.elly.api.DataType;
import org.sti2.elly.api.Vocabulary;
import org.sti2.elly.api.basics.IAtomicConcept;
import org.sti2.elly.api.basics.IAtomicRole;
import org.sti2.elly.api.factory.IBasicFactory;
import org.sti2.elly.api.factory.ITermFactory;
import org.sti2.elly.api.terms.IIndividual;
import org.sti2.elly.basics.BasicFactory;
import org.sti2.elly.terms.TermFactory;

/**
 * Renderer for OWL Entities.
 * The class is static.
 * 
 * @author Daniel Winkler
 *
 */
public class OWlEntity2EllyEntity {

    private static final IBasicFactory BASIC = BasicFactory.getInstance();

    private static final ITermFactory TERM = TermFactory.getInstance();

    public static IAtomicConcept convert(OWLClass cls) {
        if (cls.isOWLThing()) {
            return Vocabulary.topConcept;
        } else if (cls.isOWLNothing()) {
            return Vocabulary.bottomConcept;
        } else {
            String className = Owl2EllyRendererHelper.getName(cls);
            return BASIC.createAtomicConcept(className);
        }
    }

    public static IAtomicRole convert(OWLObjectProperty property) {
        String propertyName = Owl2EllyRendererHelper.getName(property);
        return BASIC.createAtomicRole(propertyName);
    }

    public static IAtomicRole convert(OWLDataProperty property) {
        String propertyName = Owl2EllyRendererHelper.getName(property);
        return BASIC.createAtomicRole(propertyName);
    }

    public static IIndividual convert(OWLIndividual individual) {
        String individualName = Owl2EllyRendererHelper.getName(individual);
        return TERM.createIndividual(individualName);
    }

    public static IAtomicConcept convert(OWLDataType dataType) {
        String dataTypeName = Owl2EllyRendererHelper.getName(dataType);
        DataType dt = DataType.asDataType(dataTypeName);
        return dt.asConcept();
    }
}
