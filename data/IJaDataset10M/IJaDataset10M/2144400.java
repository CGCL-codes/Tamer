package de.uka.aifb.owl.odm.module.seperator;

public class ModuleSeparationConfig {

    private boolean includeIndividuals;

    private boolean includeSuperClassOf;

    private boolean includeSuperPropertyOf;

    private boolean includeRestrictions;

    private static int INIT_TRAVERSAL_COUNT = 1;

    private int traversalCount = INIT_TRAVERSAL_COUNT;

    private boolean includeAllObjectProperties;

    public boolean isIncludeIndividuals() {
        return includeIndividuals;
    }

    public void setIncludeIndividuals(boolean includeIndividuals) {
        this.includeIndividuals = includeIndividuals;
    }

    public boolean isIncludeSuperClassOf() {
        return includeSuperClassOf;
    }

    public void setIncludeSuperClassOf(boolean includeSuperClassOf) {
        this.includeSuperClassOf = includeSuperClassOf;
    }

    public boolean isIncludeSuperPropertyOf() {
        return includeSuperPropertyOf;
    }

    public void setIncludeSuperPropertyOf(boolean includeSuperPropertyOf) {
        this.includeSuperPropertyOf = includeSuperPropertyOf;
    }

    public int getTraversalCount() {
        return traversalCount;
    }

    public void setTraversalCount(int traversalCount) {
        this.traversalCount = traversalCount;
    }

    public void setIncludeRestrictions(boolean includeRestrictions) {
        this.includeRestrictions = includeRestrictions;
    }

    public boolean isIncludeRestrictions() {
        return includeRestrictions;
    }

    public void resetTraversalCount() {
        traversalCount = INIT_TRAVERSAL_COUNT;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ModuleSeparationConfig clone = new ModuleSeparationConfig();
        clone.setIncludeIndividuals(includeIndividuals);
        clone.setIncludeRestrictions(includeRestrictions);
        clone.setIncludeSuperClassOf(includeSuperClassOf);
        clone.setIncludeSuperPropertyOf(includeSuperPropertyOf);
        clone.setTraversalCount(traversalCount);
        return clone;
    }

    public void setIncludeAllObjectProperties(boolean includeAllObjectProperties) {
        this.includeAllObjectProperties = includeAllObjectProperties;
    }

    public boolean isIncludeAllObjectProperties() {
        return includeAllObjectProperties;
    }
}
