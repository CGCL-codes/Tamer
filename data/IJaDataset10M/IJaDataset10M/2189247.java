package org.gocha.textbox;

import org.gocha.collection.NodesExtracter;
import org.gocha.collection.iterators.TreeWalk;
import org.gocha.collection.iterators.TreeWalkItreator;
import org.gocha.collection.iterators.TreeWalkType;
import org.gocha.gui.GuiFactory;
import org.gocha.gui.GuiNode;
import org.gocha.gui.GuiNodeTag;
import org.gocha.gui.GuiTag;
import org.gocha.gui.GuiUtil;

/**
 * @author gocha
 */
public class DockedMainFrame extends DockedTextFrame {

    @GuiTag(path = { @GuiNodeTag(id = "root"), @GuiNodeTag(id = "tools"), @GuiNodeTag(id = "dev", name = "Разработка"), @GuiNodeTag(id = "devActions") })
    public void devActions() {
        GuiNode rootNode = getHostRootGuiNode();
        if (rootNode == null) return;
        NodesExtracter<GuiNode, GuiNode> ext = new NodesExtracter<GuiNode, GuiNode>() {

            @Override
            public Iterable<GuiNode> extract(GuiNode from) {
                if (from == null) return null;
                return from.getChildren();
            }
        };
        String text = "";
        for (TreeWalk<GuiNode> w : TreeWalkItreator.<GuiNode>createIterable(rootNode, ext, TreeWalkType.ByBranchForward)) {
            String idPath = "";
            for (GuiNode pNode : w.nodePath()) {
                String id = pNode.getId();
                String name = pNode.getName();
                if (id == null) id = "NULL";
                if (name == null) name = "NULL";
                idPath += "/" + id;
            }
            String name = w.currentNode().getName();
            if (name == null) name = "NULL";
            text += "==== ";
            text += "id: " + idPath;
            text += " ===\n";
            text += "name: " + name + "\n";
            if (w.currentNode().getAccelerator() != null) text += "accelerator: " + w.currentNode().getAccelerator().toString() + "\n";
        }
        newTextDocument("actions", text);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        GuiUtil.SetSystemLookAndFeel();
        GuiFactory fact = TextBox.textBoxFactory;
        TextBox.setFactory(fact);
        TextBox.setContext(TextBox.class);
        TextBox.instance().start(args);
        TextBox.textBox().postCreateInitialization();
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new DockedMainFrame().setVisible(true);
            }
        });
    }
}
