package it.rm.bracco.pipeline.structures;

import it.rm.bracco.pipeline.utils.LogManager;
import java.util.Iterator;
import java.util.Vector;

/**
 * 
 * A Document is the representation of a document in the pipeline.
 * 
 * A document is generated as output by the CollectionPipe and is the Input for
 * each stage in the DocumentPipe, SectionPipe, PassagePipe and TokenPipe. The
 * IndexingPipe takes a Document as input.
 * 
 * @author Marco Bianchi <bianchi74@gmail.com>
 * 
 */
public class Document {

    private String docID;

    protected Vector<Section> sectionVector;

    public Document() {
        this("");
    }

    public Document(String docID) {
        this.docID = docID;
        sectionVector = new Vector<Section>();
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public void addSection(Section section) {
        section.setSectionID(sectionVector.size());
        sectionVector.add(section);
    }

    public SectionReader getSectionsReader() {
        return new SectionReader((sectionVector).iterator());
    }

    public void replaceSection(String sectionName, Section newSection) {
        Iterator<Section> sections = sectionVector.iterator();
        Section section;
        int counter = 0;
        while (sections.hasNext()) {
            if (!((section = sections.next()).getSectionName()).equalsIgnoreCase(sectionName)) {
                counter++;
            }
        }
        if (counter < sectionVector.size()) {
            sectionVector.set(counter, newSection);
        }
    }

    public int numberOfSections() {
        return sectionVector.size();
    }

    public int numberOfPassages() {
        int nop = 0;
        SectionReader sr = getSectionsReader();
        while (sr.hasMoreSections()) {
            nop = nop + sr.getNextSection().numberOfPassages();
        }
        return nop;
    }

    public int numberOfTokens() {
        int not = 0;
        SectionReader sr = getSectionsReader();
        while (sr.hasMoreSections()) {
            not = not + sr.getNextSection().numberOfTokens();
        }
        return not;
    }

    public String printDocument() {
        StringBuffer sb = new StringBuffer();
        Section s;
        for (Iterator<Section> iterator = sectionVector.iterator(); iterator.hasNext(); ) {
            s = iterator.next();
            sb.append(s.printSection() + "\n");
        }
        return new String(sb);
    }
}
