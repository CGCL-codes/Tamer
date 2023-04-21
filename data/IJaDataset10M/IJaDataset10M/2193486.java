package net.sourceforge.gosp.dictionary.data;

import java.util.LinkedList;
import javax.xml.bind.annotation.XmlAttribute;

public class TableOfContentsEntry extends TableOfContents {

    protected Integer page;

    protected String destination;

    protected LinkedList<TableOfContentsEntry> entries = new LinkedList<TableOfContentsEntry>();

    public TableOfContentsEntry() {
    }

    public TableOfContentsEntry(String title, int page) {
        this.title = title;
        this.page = page;
    }

    public TableOfContentsEntry(String title, String destination) {
        this.title = title;
        this.destination = destination;
    }

    @XmlAttribute
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @XmlAttribute
    public String getDestination() {
        return destination;
    }

    public void setDestination(String reference) {
        this.destination = reference;
    }
}
