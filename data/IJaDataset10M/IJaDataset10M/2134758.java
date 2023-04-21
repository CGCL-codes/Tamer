package org.isi.monet.modelling.editor.model.declarations.views;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.isi.monet.modelling.editor.model.Constants;
import org.isi.monet.modelling.editor.model.Model;
import org.isi.monet.modelling.editor.model.declarations.Declaration;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FormViews extends Declaration {

    public FormViews(Declaration parent, Node node) {
        super(parent, node);
        this.type = Constants.DECLARATION_VIEWS;
        this.name = Constants.DECLARATION_VIEWS;
        this.declarations = new ArrayList<Declaration>();
        NodeList declarationsNodes = node.getChildNodes();
        for (int index = 0; index < declarationsNodes.getLength(); index++) {
            Node declaration = declarationsNodes.item(index);
            if (declaration instanceof Element) {
                if (declaration.getNodeName().equalsIgnoreCase(Constants.DECLARATION_VIEW)) declarations.add(new FormView(this, declaration));
            }
        }
    }

    public FormViews(Declaration parent) {
        super(parent);
        this.type = Constants.DECLARATION_VIEWS;
        this.name = Constants.DECLARATION_VIEWS;
        this.declarations = new ArrayList<Declaration>();
    }

    @Override
    public void appendText(PrintWriter writer, String tabs) {
        if (!this.declarations.isEmpty()) {
            writer.print(tabs + "<" + this.type + ">");
            writer.println();
            String indent = tabs + "\t";
            Iterator<Declaration> iterDeclarations = this.declarations.iterator();
            while (iterDeclarations.hasNext()) iterDeclarations.next().appendText(writer, indent);
            writer.print(tabs + "</" + this.type + ">");
            writer.println();
        }
    }

    @Override
    public Model[] getChildren() {
        List<Model> children = new ArrayList<Model>();
        children.addAll(this.declarations);
        return children.toArray(new Model[children.size()]);
    }

    @Override
    public void removeFromParent() {
    }
}
