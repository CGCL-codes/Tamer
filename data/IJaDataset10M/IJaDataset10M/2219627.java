package edu.upmc.opi.caBIG.caTIES.jess.lexbig;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.concepts.Entity;

public class CaTIES_LexBIGConcept extends CaTIES_LexBIGFact {

    private ResolvedConceptReference resolvedConceptReference = null;

    private String conceptName = "UNDEFINED";

    private String conceptCUI = "UNDEFINED";

    private String conceptTUI = "UNDEFINED";

    private String conceptID = "UNDEFINED";

    private Date conceptCreationSequence;

    private final HashSet parentRoleRelations = new HashSet();

    private final HashSet childRoleRelations = new HashSet();

    private final Hashtable domainForRestrictions = new Hashtable();

    private final Hashtable rangeForRestrictions = new Hashtable();

    public void addParentRoleRelation(CaTIES_LexBIGIsa isaRelation) {
        this.parentRoleRelations.add(isaRelation);
    }

    public void addChildRoleRelation(CaTIES_LexBIGIsa isaRelation) {
        this.childRoleRelations.add(isaRelation);
    }

    public void addDomainForRestriction(CaTIES_LexBIGRestriction restriction) {
        this.domainForRestrictions.put(restriction.getRelationName(), restriction);
    }

    public void addRangeForRestriction(CaTIES_LexBIGRestriction restriction) {
        this.rangeForRestrictions.put(restriction.getRelationName(), restriction);
    }

    public Date getConceptCreationSequence() {
        return conceptCreationSequence;
    }

    public void setConceptCreationSequence(Date conceptCreationSequence) {
        this.conceptCreationSequence = conceptCreationSequence;
    }

    public String getConceptCUI() {
        return conceptCUI;
    }

    public void setConceptCUI(String conceptCUI) {
        this.conceptCUI = conceptCUI;
    }

    public String getConceptID() {
        return conceptID;
    }

    public void setConceptID(String conceptID) {
        this.conceptID = conceptID;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getConceptTUI() {
        return conceptTUI;
    }

    public void setConceptTUI(String conceptTUI) {
        this.conceptTUI = conceptTUI;
    }

    public ResolvedConceptReference getResolvedConceptReference() {
        return resolvedConceptReference;
    }

    public void setResolvedConceptReference(ResolvedConceptReference resolvedConceptReference) {
        this.resolvedConceptReference = resolvedConceptReference;
        setConceptID(resolvedConceptReference.getConceptCode());
        Entity entry = resolvedConceptReference.getReferencedEntry();
        for (int jdx = 0; jdx < entry.getPropertyCount(); jdx++) {
            String propertyName = entry.getProperty(jdx).getPropertyName();
            String propertyValue = entry.getProperty(jdx).getValue().getContent();
            if (propertyValue == null) {
                propertyValue = "UNDEFINED";
            }
            if (propertyName.equals("CONCEPT_NAME")) {
                setConceptName(propertyValue);
            } else if (propertyName.equals("UMLS_CUI")) {
                setConceptCUI(propertyValue);
            } else if (propertyName.equals("NCI_META_CUI")) {
                if (getConceptCUI().equals("UNDEFINED")) {
                    setConceptCUI(propertyValue);
                }
            } else if (propertyName.equals("Semantic_Type")) {
                propertyValue = propertyValue.replace(' ', '_');
                propertyValue = propertyValue.replace(',', '_');
                setConceptTUI(propertyValue);
            }
        }
    }

    public void format() {
        String jessCommand = "(bind ?" + getConceptName();
        jessCommand += " (add-owl-cls ";
        jessCommand += "\"" + getConceptName() + "\"" + " ";
        jessCommand += "\"" + getConceptID() + "\"" + " ";
        jessCommand += "\"" + getConceptCUI() + "\"" + " ";
        jessCommand += "\"" + getConceptTUI() + "\"" + " ";
        jessCommand += "))\n";
        setJessCommand(jessCommand);
    }

    public HashSet getChildRoleRelations() {
        return childRoleRelations;
    }

    public Hashtable getDomainForRestrictions() {
        return domainForRestrictions;
    }

    public HashSet getParentRoleRelations() {
        return parentRoleRelations;
    }

    public Hashtable getRangeForRestrictions() {
        return rangeForRestrictions;
    }
}
