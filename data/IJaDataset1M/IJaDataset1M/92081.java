package pub.utils;

import java.util.*;

/**
 * GOTerm to wrap a parsed term entry from GOREF file
 */
public class GOTerm {

    private String name;

    private String about_id;

    private String external_id;

    private String definition;

    private List synonyms;

    private Map term2terms;

    private Map relations;

    private String term_id;

    public GOTerm() {
        synonyms = new ArrayList();
        relations = new HashMap();
        term2terms = new HashMap();
    }

    /**
     * Returns the RDF unique identifier of a term.
     */
    public String getAboutId() {
        return about_id;
    }

    /**
     * Initializes this instance's "about" RDF identifier.
     */
    public void setAboutId(String id) {
        this.about_id = id;
    }

    /**
     * Assigns the name of this term.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Assigns an external identifier.
     */
    public void setExternalId(String external_id) {
        this.external_id = external_id;
    }

    /**
     * Assigns a definition string.
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * Adds a new synonym to this term.
     */
    public void addSynonym(String synonym) {
        this.synonyms.add(synonym);
    }

    public void setTerm2TermRelationships(Map relations) {
        this.term2terms = relations;
    }

    public void setTermId(String term_id) {
        this.term_id = term_id;
    }

    public List getSynonyms() {
        return synonyms;
    }

    public Map getTerm2TermRelations() {
        return term2terms;
    }

    public String getTermId() {
        return term_id;
    }

    public String getName() {
        return name;
    }

    public String getExternalId() {
        return external_id;
    }

    public String getDefinition() {
        return definition;
    }

    /** Attach a new relationship to this term. **/
    public void addRelationship(String relationship, String resource_id) {
        if (relations.get(relationship) == null) {
            relations.put(relationship, new ArrayList());
        }
        ((List) relations.get(relationship)).add(resource_id);
    }

    /**
     * Returns a list of all the relation
     */
    public List getRelationshipTypes() {
        return new ArrayList(relations.keySet());
    }

    public List getRelationships(String relationship_type) {
        return (List) relations.get(relationship_type);
    }

    public void addRelation(String related_term_external_id, String relationship) {
        term2terms.put(related_term_external_id, relationship);
    }

    /**
     * Returns true if this is a valid go term (i.e. has a non-empty external id)
     */
    public boolean isValidGoTerm() {
        return (StringUtils.isNotEmpty(external_id));
    }

    public String toString() {
        return "name :" + getName() + "external_id :" + getExternalId() + "relation size " + getTerm2TermRelations().size() + " alias ";
    }
}
