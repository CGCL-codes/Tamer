package de.mxro.freemind;

import java.util.List;
import java.util.Vector;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public abstract class FreeMindObject {

    @XStreamImplicit
    protected final List<FreeMindObject> nodes;

    public FreeMindObject() {
        super();
        this.nodes = new Vector<FreeMindObject>();
    }

    public FreeMindObject addNode(FreeMindObject node) {
        nodes.add(node);
        return this;
    }
}
