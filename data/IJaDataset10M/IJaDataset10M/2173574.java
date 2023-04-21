package org.obolibrary.obo2owl;

import static junit.framework.Assert.*;
import java.io.IOException;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class UnionOfTest extends OboFormatTestBasics {

    @Test
    public void testUnion() throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
        OWLOntology owlOnt = convertOBOFile("taxon_union_terms.obo");
        assertNotNull(owlOnt);
        OWLOntologyManager manager = owlOnt.getOWLOntologyManager();
        OWLDataFactory df = manager.getOWLDataFactory();
        IRI iri = IRI.create("http://purl.obolibrary.org/obo/NCBITaxon_Union_0000000");
        OWLClass cls = df.getOWLClass(iri);
        boolean ok = false;
        for (OWLObject ec : cls.getEquivalentClasses(owlOnt)) {
            System.out.println(cls + " = " + ec);
            if (ec instanceof OWLObjectUnionOf) {
                ok = true;
            }
        }
        assertTrue(ok);
    }

    private OWLOntology convertOBOFile(String fn) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
        return convert(parseOBOFile(fn), fn);
    }
}
