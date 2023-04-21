package de.fraunhofer.iitb.owldb;

import java.io.*;
import org.semanticweb.owlapi.io.*;
import org.semanticweb.owlapi.model.*;

/**
 * An Target for the OWLDBOntology.
 * 
 * @author J&ouml;rg Hen&szlig; (KIT)
 * @author J&uuml;rgen Mo&szlig;graber (Fraunhofer IOSB)
 */
public class OWLDBOntologyOutputTarget implements OWLOntologyDocumentTarget {

    final IRI iri;

    /**
	 * An Target for the OWLDBOntology.
	 * 
	 * @param iri The IRI to output to.
	 */
    public OWLDBOntologyOutputTarget(final IRI iri) {
        this.iri = iri;
    }

    /** {@inheritDoc} */
    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public IRI getDocumentIRI() {
        return this.iri;
    }

    /** {@inheritDoc} */
    @Override
    public Writer getWriter() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isOutputStreamAvailable() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDocumentIRIAvailable() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWriterAvailable() {
        return false;
    }
}
