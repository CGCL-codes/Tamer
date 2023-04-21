package org.in4ama.documentengine.output;

import java.util.ArrayList;
import org.in4ama.documentautomator.Document;

/** Stores some meta-data of compound output document. */
public class CompoundOutputDocumentInfo extends OutputDocumentInfo {

    private final ArrayList<OutputDocumentInfo> fragments = new ArrayList<OutputDocumentInfo>();

    /** Creates a new instance of compound document info. */
    public CompoundOutputDocumentInfo(String name, String type, int pageCount) {
        super(name, type, pageCount);
    }

    /** Creates a new instance of compound document info. */
    public CompoundOutputDocumentInfo(String name, String type) {
        super(name, type);
    }

    /** Creates a new instance of CompoundOutputDocumentInfo. */
    public CompoundOutputDocumentInfo(Document document) {
        super(document);
    }

    /** Recalculates the total number of pages. */
    public void updatePageCount() {
        int pc = 0;
        for (OutputDocumentInfo fragment : fragments) {
            int fpc = fragment.getPageCount();
            if (fpc >= 0) {
                pc += fpc;
            } else {
                pc = -1;
                break;
            }
            pageCount = pc;
        }
    }

    /** Returns the number of fragments. */
    public int getFragmentCount() {
        return fragments.size();
    }

    /** Returns the fragment at the specified index. */
    public OutputDocumentInfo getFragment(int idx) {
        return fragments.get(idx);
    }

    /** Adds the specified fragment at the end of the fragment list. */
    public void addFragment(OutputDocumentInfo outputDocumentInfo) {
        fragments.add(outputDocumentInfo);
    }

    @Override
    public CompoundOutputDocumentInfo createCopy() {
        CompoundOutputDocumentInfo copy = new CompoundOutputDocumentInfo(name, type, pageCount);
        for (OutputDocumentInfo fragment : fragments) {
            copy.addFragment(fragment.createCopy());
        }
        return copy;
    }
}
