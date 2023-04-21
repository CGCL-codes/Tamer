package de.mxro.freemind;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("node")
public class Node extends FreeMindObject {

    @XStreamAsAttribute
    private String LINK;

    @XStreamAsAttribute
    public String COLOR;

    @XStreamAsAttribute
    public String CREATED;

    @XStreamAsAttribute
    public String ID;

    @XStreamAsAttribute
    public String MODIFIED;

    @XStreamAsAttribute
    public String POSITION;

    @XStreamAsAttribute
    public String TEXT;

    public Node linkWith(String link) {
        this.LINK = link;
        return this;
    }

    public Node(final String text) {
        super();
        this.TEXT = text;
    }
}
