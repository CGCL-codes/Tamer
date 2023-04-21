package fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp;

/**
 * A representation for a range in a document where a terminal (i.e., a
 * placeholder or a keyword) is expected. The range is expressed using two
 * integers denoting the start of the range including hidden tokens (e.g.,
 * whitespace) and excluding those token (i.e., the part of the document
 * containing the relevant characters).
 */
public class Ocl4tstExpectedTerminal {

    private int followSetID;

    private fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstExpectedElement terminal;

    private int startIncludingHiddenTokens;

    private int startExcludingHiddenTokens;

    private String prefix;

    private org.eclipse.emf.ecore.EStructuralFeature[] containmentTrace;

    public Ocl4tstExpectedTerminal(fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstExpectedElement terminal, int followSetID, org.eclipse.emf.ecore.EStructuralFeature... containmentTrace) {
        super();
        this.terminal = terminal;
        this.followSetID = followSetID;
        this.containmentTrace = containmentTrace;
    }

    public int getFollowSetID() {
        return followSetID;
    }

    public fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstExpectedElement getTerminal() {
        return terminal;
    }

    public String toString() {
        return terminal == null ? "null" : terminal.toString();
    }

    public boolean equals(Object o) {
        return this.terminal.equals(((Ocl4tstExpectedTerminal) o).terminal);
    }

    public void setPosition(int startIncludingHiddenTokens, int startExcludingHiddenTokens) {
        assert startExcludingHiddenTokens <= startExcludingHiddenTokens;
        assert startIncludingHiddenTokens <= startExcludingHiddenTokens;
        this.startIncludingHiddenTokens = startIncludingHiddenTokens;
        this.startExcludingHiddenTokens = startExcludingHiddenTokens;
    }

    public int getStartIncludingHiddenTokens() {
        return startIncludingHiddenTokens;
    }

    public int getStartExcludingHiddenTokens() {
        return startExcludingHiddenTokens;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public org.eclipse.emf.ecore.EStructuralFeature[] getContainmentTrace() {
        return containmentTrace;
    }
}
