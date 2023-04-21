package uk.ac.imperial.ma.metric.nav;

import uk.ac.imperial.ma.metric.nav.NavigationTreeNodeRef;
import org.w3c.dom.Element;
import java.util.Vector;

public class Author extends NavigationTreeNodeRef {

    public Author(Element element, NavigationTreeNode parent, Vector vecLinkSources) {
        isVisible = true;
        allowsChildren = true;
        this.parent = parent;
        parent.addChild(this);
        vecChildren = new Vector();
        ref = element.getAttribute("xlink:href");
        vecLinkSources.add(this);
    }

    public short getType() {
        return NavigationTreeNode.AUTHOR;
    }

    public String toString() {
        return "Author Ref: " + ref;
    }

    public String getName() {
        NavigationTreeNode ntn = getLinkTarget();
        switch(ntn.getType()) {
            case NavigationTreeNode.PERSON:
                Person person = (Person) ntn;
                return person.getName();
            default:
                return "Unknown";
        }
    }
}
