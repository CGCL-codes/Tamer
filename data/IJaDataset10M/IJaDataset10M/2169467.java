package de.d3web.we.core.semantic;

import org.openrdf.model.URI;
import de.knowwe.core.contexts.StringContext;
import de.knowwe.core.kdom.parsing.Section;

public class DefaultURIContext extends StringContext {

    public static final String CID = "URI_CONTEXT";

    private URI soluri;

    public DefaultURIContext(String sol) {
        setSubject(sol);
    }

    public DefaultURIContext() {
    }

    public void setSubject(String sol) {
        attributes.put("solution", sol);
    }

    public void setSubjectURI(URI solutionuri) {
        soluri = solutionuri;
    }

    public String getSubject() {
        return attributes.get("solution");
    }

    public URI getSolutionURI() {
        if (soluri == null) {
            UpperOntology uo = UpperOntology.getInstance();
            soluri = uo.getHelper().createlocalURI(getSubject());
        }
        return soluri;
    }

    @Override
    public String getCID() {
        return CID;
    }

    @Override
    public boolean isValidForSection(Section s) {
        return true;
    }
}
