package edu.gsbme.gyoza2d.visual.ModelML;

import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.gyoza2d.Control.ImportObject;
import edu.gsbme.gyoza2d.GraphGenerator.GraphGenerator;
import edu.gsbme.menuactiondelegate.MenuActionDelegate;

public class ImportRefControl extends ImportObject {

    public ImportRefControl(Element element, MenuActionDelegate actionDelegate, GraphGenerator graphGenerator) {
        super(element, actionDelegate, graphGenerator);
    }

    @Override
    public String toString() {
        if (returnElement().hasAttribute(Attributes.ref.toString())) return "<html><center><b> Import : " + returnElement().getAttribute(Attributes.ref.toString()) + "</b></center></html>"; else if (returnElement().hasAttribute(Attributes.name.toString())) return "<html><center><b> Import : " + returnElement().getAttribute(Attributes.name.toString()) + "</b></center></html>"; else return "<html><center><b> Import Reference Vertex</b></center></html>";
    }

    @Override
    protected void initVertexMenu() {
    }
}
