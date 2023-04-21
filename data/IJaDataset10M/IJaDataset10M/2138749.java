package objectif.lyon.designer.data.writer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import objectif.lyon.designer.gui.component.DestinationObject;

public class DestinationObjectWriter implements ElementWriter<DestinationObject> {

    @Override
    public void write(Document doc, Node parent, DestinationObject object) {
        Element child = doc.createElement("element");
        child.setAttribute("className", "DestinationObject");
        child.setAttribute("x", String.valueOf(object.getX()));
        child.setAttribute("y", String.valueOf(object.getY()));
        if (object.getDestination().getName() != null) {
            child.setAttribute("name", object.getDestination().getName());
        }
        parent.appendChild(child);
    }
}
