package edu.gsbme.gyoza2d.visual.Metadata;

import org.w3c.dom.Element;
import edu.gsbme.gyoza2d.Control.MetadataObject;
import edu.gsbme.gyoza2d.GraphGenerator.GraphGenerator;
import edu.gsbme.menuactiondelegate.MenuActionDelegate;

public class MetadataControl extends MetadataObject {

    public MetadataControl(Element element, MenuActionDelegate actionDelegate, GraphGenerator graphGenerator) {
        super(element, actionDelegate, graphGenerator);
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    protected void initVertexMenu() {
    }
}
