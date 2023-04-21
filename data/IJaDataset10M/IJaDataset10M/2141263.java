package org.isi.monet.modelling.editor.model.properties.behavior;

import java.io.PrintWriter;
import org.isi.monet.modelling.editor.model.Constants;
import org.isi.monet.modelling.editor.model.declarations.Declaration;
import org.isi.monet.modelling.editor.model.properties.Property;
import org.w3c.dom.Node;

public class Script extends Property {

    public Script(Declaration parent, Node node) {
        super(parent);
        this.typeProperty = Constants.DECLARATION_BEHAVIOR_SCRIPT;
        this.valueProperty = node.getTextContent();
    }

    public Script(Property parent, Node node) {
        super(parent);
        this.typeProperty = Constants.DECLARATION_BEHAVIOR_SCRIPT;
        this.valueProperty = node.getTextContent();
    }

    public Script(Property parent) {
        super(parent);
        this.typeProperty = Constants.DECLARATION_BEHAVIOR_SCRIPT;
        this.valueProperty = "";
    }

    public Script(Declaration parent) {
        super(parent);
        this.typeProperty = Constants.DECLARATION_BEHAVIOR_SCRIPT;
        this.valueProperty = "";
    }

    @Override
    public void appendText(PrintWriter writer, String tabs) {
        writer.print(tabs + "<" + this.typeProperty + ">");
        String indent = tabs + "\t";
        appendValue(writer, indent);
        writer.print(tabs + "</" + this.typeProperty + ">");
        writer.println();
    }

    private void appendValue(PrintWriter writer, String tabs) {
        String value = this.valueProperty.replaceAll("\t", "");
        value = value.replaceAll(" ", "");
        String lines[] = value.split("\n");
        for (int index = 0; index < lines.length; index++) writer.println(tabs + lines[index]);
    }
}
