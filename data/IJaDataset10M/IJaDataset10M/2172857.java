package edu.ucla.mbi.xml.MIF.elements.controlledVocabularies;

import edu.ucla.mbi.xml.MIF.elements.adminElements.Displayable;
import edu.ucla.mbi.xml.MIF.elements.adminElements.Names;
import edu.ucla.mbi.xml.MIF.elements.adminElements.Label;
import edu.ucla.mbi.xml.MIF.elements.referencing.Xref;
import edu.ucla.mbi.xml.MIF.elements.referencing.ExperimentReferrer;
import edu.ucla.mbi.xml.MIF.elements.referencing.ExperimentRefList;
import edu.ucla.mbi.xml.MIF.elements.referencing.DBReferenceBuilder;
import edu.ucla.mbi.xml.MIF.elements.XMLParentage;
import edu.ucla.mbi.xml.MIF.elements.comparators.ControlledVocabularyTermComparator;
import edu.ucla.mbi.xml.MIF.elements.controlledVocabularies.MolecularInteractions.MIVocabTree;
import edu.ucla.mbi.xml.MIF.elements.controlledVocabularies.MolecularInteractions.DBCitation;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Enumeration;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Dec 11, 2005
 * Time: 3:25:10 PM
 */
public class ControlledVocabularyTerm extends DefaultMutableTreeNode implements Displayable, XMLParentage {

    private String term;

    private String termId;

    private ArrayList<String> synonyms = new ArrayList<String>();

    private int inferredDepth = 0;

    private Names names = new Names();

    private Xref xref;

    public Xref getXref() {
        return xref;
    }

    public void setXref(Xref xref) {
        this.xref = xref;
    }

    public Names getNames() {
        return names;
    }

    protected ControlledVocabularyTerm() {
    }

    public ControlledVocabularyTerm(ControlledVocabularyTerm cvTerm) {
        this.term = cvTerm.term;
        this.termId = cvTerm.getTermId();
        this.synonyms.addAll(cvTerm.getSynonyms());
        this.inferredDepth = cvTerm.getInferredDepth();
        names.setShortLabel(new Label(cvTerm.term));
    }

    public ControlledVocabularyTerm(String line) throws IOException {
        StringReader reader = new StringReader(line);
        while (reader.read() == ' ') inferredDepth++;
        String trimmed = eatBackslash(line).trim();
        trimmed = trimmed.substring(1).trim();
        term = trimmed.substring(0, trimmed.indexOf(";") - 1).trim();
        trimmed = trimmed.substring(trimmed.indexOf(";") + 2).trim();
        termId = trimmed.substring(0, 7);
        trimmed = trimmed.substring(7);
        while (trimmed.indexOf("synonym:") != -1) {
            trimmed = trimmed.substring(trimmed.indexOf("synonym:") + 8);
            String synonym = trimmed.trim().substring(0, (trimmed.indexOf(";") != -1) ? trimmed.indexOf(";") : trimmed.length()).trim();
            trimmed = trimmed.substring((trimmed.indexOf(";") != -1) ? trimmed.indexOf(";") : trimmed.length()).trim();
            addSynonym(synonym);
        }
    }

    public void rebuildXref() {
        if (termId == null || termId.length() == 0) setTermId(MIVocabTree.getInstance().findByTerm(getTerm()).getTermId());
        Xref xref = new Xref();
        DBReferenceBuilder dbBldr = new DBReferenceBuilder();
        dbBldr.createDbReference(true);
        dbBldr.setId(getTermId());
        dbBldr.setDb(DBCitation.getMIVocabReference());
        dbBldr.setRefType("identity");
        dbBldr.setRefTypeAc("MI:0356");
        xref.setPrimaryReference(dbBldr.getDbReference());
        setXref(xref);
    }

    public ControlledVocabularyTerm(String term, String termId) {
        this.term = term;
        this.termId = termId;
    }

    public int getInferredDepth() {
        return inferredDepth;
    }

    public void addSynonym(String synonym) {
        synonyms.add(synonym);
    }

    public String getTerm() {
        return term;
    }

    public String getTermId() {
        return termId;
    }

    public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    public String getLocalRootTermIdentifer() {
        return "MI:0000";
    }

    /**
     * This method is implemented so that I can use SortedSets (like TreeSets) on CVTerms
     * so as to get good Sets of CVTerms that are non-redundant.
     *
     * @param o - a CVTerm
     * @return - 1, 0, -1, as appropriate => zero means equalTo
     */
    public int compareTo(Object o) {
        return new ControlledVocabularyTermComparator().compare(this, o);
    }

    private String eatBackslash(String in) throws IOException {
        StringReader reader = new StringReader(in);
        StringBuffer buf = new StringBuffer();
        while (reader.ready()) {
            char c = (char) reader.read();
            if (c == (char) -1) break;
            if (c != '\\') buf.append(c);
        }
        return buf.toString();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer().append(getTermId()).append(": ").append(getTerm());
        return buf.toString();
    }

    public boolean hasData() {
        return getTerm() != null && getTerm().length() > 0;
    }

    public String toXML() {
        if (hasData()) {
            throw new ClassCastException("Shouldn't be calling ControlledVocabularyTerm.toXML() for cv term!");
        }
        return "";
    }

    public ArrayList<ControlledVocabularyTerm> getChildren() {
        ArrayList<ControlledVocabularyTerm> terms = new ArrayList<ControlledVocabularyTerm>();
        Enumeration e = children();
        while (e.hasMoreElements()) terms.add((ControlledVocabularyTerm) e.nextElement());
        return terms;
    }

    public void setNames(Names names) {
        this.names = names;
    }

    public void addChild(Object child) {
        if (this instanceof ExperimentReferrer && child instanceof ExperimentRefList) {
            ((ExperimentReferrer) this).setExperimentRefList((ExperimentRefList) child);
            return;
        }
        if (child instanceof Names) setNames((Names) child); else if (child instanceof Xref) setXref((Xref) child); else throw new ClassCastException("attempted to add type " + child.getClass() + " to ControlledVocabularyTerm, which is not allowed.");
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public boolean equals(Object obj) {
        ControlledVocabularyTerm cvTerm = (ControlledVocabularyTerm) obj;
        return cvTerm.getTermId().equals(getTermId());
    }
}
