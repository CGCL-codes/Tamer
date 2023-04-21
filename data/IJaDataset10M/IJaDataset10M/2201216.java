package org.isi.monet.modelling.editor.model.declarations.task;

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

public class SkillsWorkitem extends Declaration {

    public SkillsWorkitem(Declaration parent, Node node) {
        super(parent, node);
        this.type = Constants.DECLARATION_TASK_SKILLS;
        this.name = Constants.DECLARATION_TASK_SKILLS;
        NodeList declarationsNodes = node.getChildNodes();
        for (int index = 0; index < declarationsNodes.getLength(); index++) {
            Node declaration = declarationsNodes.item(index);
            if (declaration instanceof Element) {
                if (declaration.getNodeName() == Constants.DECLARATION_TASK_SKILL) this.declarations.add(new Skill(this, declaration));
            }
        }
        if (this.declarations.isEmpty()) this.declarations.add(new Skill(this));
    }

    public SkillsWorkitem(Declaration parent) {
        super(parent);
        this.type = Constants.DECLARATION_TASK_SKILLS;
        this.name = Constants.DECLARATION_TASK_SKILLS;
        declarations.add(new Skill(this));
    }

    @Override
    public void appendText(PrintWriter writer, String tabs) {
        writer.println(tabs + "<" + this.type + ">");
        String indent = tabs + "\t";
        Iterator<Declaration> iterDeclarations = this.declarations.iterator();
        while (iterDeclarations.hasNext()) iterDeclarations.next().appendText(writer, indent);
        writer.println(tabs + "</" + this.type + ">");
    }

    @Override
    public Model[] getChildren() {
        List<Model> children = new ArrayList<Model>();
        children.addAll(this.declarations);
        return children.toArray(new Model[children.size()]);
    }

    @Override
    public void removeDeclaration(Declaration declaration) {
        if (declarations.remove(declaration)) ((Declaration) getParent()).nameChanged(this);
    }
}
