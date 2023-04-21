package br.ufpe.cin.ontocompo.module.model;

import br.ufpe.cin.ontocompo.module.owlrender.OWLObjectModuleRenderer;
import java.io.Serializable;
import java.net.URI;

/**
 *
 * @author Camila
 */
public class Alignment implements Serializable {

    private URI uri;

    public Alignment() {
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void accept(OWLObjectModuleRenderer visitor) {
        visitor.visit(this);
    }
}
