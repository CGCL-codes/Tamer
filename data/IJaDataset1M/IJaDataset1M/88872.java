package gov.nist.atlas.impl;

import gov.nist.atlas.ATLASElement;
import gov.nist.atlas.Id;
import gov.nist.atlas.IdentifiableATLASElement;
import gov.nist.atlas.spi.ImplementationDelegate;
import gov.nist.atlas.type.ATLASType;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @version $Revision: 1.21 $
 * @author Christophe Laprun
 */
public class IdentifiableATLASElementImpl extends ATLASElementImpl implements IdentifiableATLASElement {

    protected IdentifiableATLASElementImpl() {
    }

    protected IdentifiableATLASElementImpl(ATLASType type, ATLASElement parent, Id id, ImplementationDelegate delegate) {
        initWith(type, parent, id, delegate);
    }

    protected void initWith(ATLASType type, ATLASElement parent, Id id, ImplementationDelegate delegate) {
        initIdAndURI(id, parent);
        super.initWith(type, parent, delegate);
    }

    private final void initIdAndURI(Id id, ATLASElement parent) {
        if (id != null) this.id = id; else this.id = parent.getDefiningCorpus().getATLASImplementation().createNewIdFor(getATLASType());
        initURI(parent);
    }

    protected void initURI(ATLASElement parent) {
        URI corpusURI = parent.getDefiningCorpus().getURI();
        try {
            uri = new URI(corpusURI.getScheme(), corpusURI.getSchemeSpecificPart(), id.getAsString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Shouldn't have happened! What happened was: " + e.getLocalizedMessage(), e);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof IdentifiableATLASElement) {
            IdentifiableATLASElement element = (IdentifiableATLASElement) obj;
            return uri.equals(element.getURI());
        }
        return false;
    }

    public int hashCode() {
        return getURI().hashCode();
    }

    public final Id getId() {
        return id;
    }

    public URI getURI() {
        return uri;
    }

    public String toString() {
        return super.toString() + ID + id.getAsString();
    }

    private Id id;

    protected URI uri;

    private static final String ID = " id=";
}
