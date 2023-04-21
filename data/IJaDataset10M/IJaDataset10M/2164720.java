package org.wsmostudio.repository.irsIII.query;

import java.util.ArrayList;
import java.util.List;
import org.omwg.ontology.Attribute;
import org.omwg.ontology.Concept;
import com.ontotext.wsmo4j.common.IRIImpl;

/**
 * A query branch, a part of the rule condition.
 * Each branch has a list of attributes selected by the user. 
 * 
 * @author cxtralu
 *
 */
public class Branch implements Element {

    private Concept concept;

    /**
	 * How the concept name is displayed: the description nfp property or the concept name
	 */
    private String displayName;

    /**
	 * The name of the variable
	 */
    private String varName;

    /**
	 * A help message for the user
	 */
    private String info;

    public List<QueryAttribute> getAttributes() {
        return attributes;
    }

    private List<QueryAttribute> attributes;

    public Branch(Concept c, String name, String displName) {
        concept = c;
        varName = name;
        displayName = displName;
        attributes = new ArrayList<QueryAttribute>();
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public Concept getConcept() {
        return concept;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getInfo() {
        return info;
    }

    public void addAttribute(QueryAttribute attr) {
        attributes.add(attr);
    }

    public Attribute findAttribute(String selection) {
        return concept.findAttributes(new IRIImpl(selection)).iterator().next();
    }

    public void removeAttribute(QueryAttribute attribute) {
        if (attribute == null || this.attributes == null || this.attributes.isEmpty()) return;
        this.attributes.remove(attribute);
    }
}
