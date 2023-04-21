package gr.konstant.transonto.interfaces.dul;

import java.util.Set;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import gr.konstant.transonto.exception.*;
import gr.konstant.transonto.interfaces.ExprConcept;

public interface Entity extends ExprConcept {

    public void hasRegion(Region r) throws BackendException;

    public Region getRegion() throws BackendException, BadArgumentException, BadKnowledgeBaseException;

    public void hasLocation(Entity loc) throws BackendException;

    public void addLocation(Entity loc) throws BackendException;

    public Set<Entity> getLocations() throws BackendException, BadKnowledgeBaseException;

    public void isClassifiedBy(DULConcept concept) throws BackendException;

    public Set<DULConcept> isClassifiedBy() throws BackendException, BadKnowledgeBaseException;

    public void isIncludedIn(Situation sit) throws BackendException, BadArgumentException;

    public Set<Situation> isIncludedIn() throws BackendException, BadKnowledgeBaseException;

    public void isReferenceOf(InformationObject t) throws BackendException;

    public Set<InformationObject> getReferences() throws BackendException, BadKnowledgeBaseException;

    void precedes(Entity that, boolean directly) throws BackendException;

    void follows(Entity that, boolean directly) throws BackendException;

    public URI getURI();

    public Resource getResource();

    public void hasLabel(String l) throws BackendException;

    public String hasLabel() throws BackendException, BadKnowledgeBaseException;
}
