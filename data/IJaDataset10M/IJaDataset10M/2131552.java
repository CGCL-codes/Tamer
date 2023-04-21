package org.in4ama.documentengine.output;

import org.in4ama.documentautomator.Document;

/** Stores some meta-data about the generated document. */
public class OutputDocumentInfo {

    protected String name;

    protected String type;

    protected int pageCount;

    /** Creates a new instance of OutputDocumentInfo. */
    public OutputDocumentInfo(String name, String type, int pageCount) {
        this.name = name;
        this.type = type;
        this.pageCount = pageCount;
    }

    /** Creates new instance of OutputDocumentInfo. */
    public OutputDocumentInfo(String name, String type) {
        this(name, type, -1);
    }

    /** Creates a new instance of OutputDocumentInfo. */
    public OutputDocumentInfo(Document document) {
        this(document, -1);
    }

    /** Creates a new instance of OutputDocumentInfo. */
    public OutputDocumentInfo(Document document, int pageCount) {
        this(document.getName(), document.getType(), pageCount);
    }

    /** Sets the name of the source document. */
    public void setName(String name) {
        this.name = name;
    }

    /** Sets the type of the source document. */
    public void setType(String type) {
        this.type = type;
    }

    /** Returns the source document's name. */
    public String getName() {
        return name;
    }

    /** Returns the source documents' type. */
    public String getType() {
        return type;
    }

    /** Returns the number of pages in the output document,
	 * -1 if it is unknown. */
    public int getPageCount() {
        return pageCount;
    }

    /** Sets the number of pages in the output document. */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public OutputDocumentInfo createCopy() {
        return new OutputDocumentInfo(name, type, pageCount);
    }
}
