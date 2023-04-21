package org.isi.monet.modelling.editor.model.properties.behavior;

import java.io.PrintWriter;
import java.util.Iterator;
import org.isi.monet.modelling.editor.model.Constants;
import org.isi.monet.modelling.editor.model.declarations.Declaration;
import org.isi.monet.modelling.editor.model.properties.Property;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Process extends Property {

    public Process(Declaration parent, Node node) {
        super(parent);
        this.typeProperty = Constants.DECLARATION_BEHAVIOR_PROCESS;
        OnFailure failure = new OnFailure(this);
        NodeList declarationsNodes = node.getChildNodes();
        for (int index = 0; index < declarationsNodes.getLength(); index++) {
            Node declaration = declarationsNodes.item(index);
            if (declaration instanceof Element) {
                if (declaration.getNodeName().equalsIgnoreCase(Constants.DECLARATION_BEHAVIOR_ONFAILURE)) failure = new OnFailure(this, declaration);
                if (declaration.getNodeName().equalsIgnoreCase(Constants.DECLARATION_BEHAVIOR_STEP)) properties.add(new Step(this, declaration));
            }
        }
        properties.add(failure);
    }

    public Process(Declaration parent) {
        super(parent);
        this.typeProperty = Constants.DECLARATION_BEHAVIOR_PROCESS;
        properties.add(new OnFailure(this));
    }

    @Override
    public void appendText(PrintWriter writer, String tabs) {
        writer.print(tabs + "<" + this.typeProperty + ">");
        writer.println();
        String indent = tabs + "\t";
        Iterator<Property> iterProperties = this.properties.iterator();
        while (iterProperties.hasNext()) {
            Property property = iterProperties.next();
            property.appendText(writer, indent);
        }
        writer.print(tabs + "</" + this.typeProperty + ">");
        writer.println();
    }
}
