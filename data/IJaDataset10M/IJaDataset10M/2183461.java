package skribler.languages.wtl;

import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import skribler.ast.TreeNode;
import skribler.ast.TreeNodeDeserializer;
import skribler.ast.expert.ASTExpert;
import skribler.cst.Coder;
import skribler.cst.expert.CSTExpert;
import skribler.editor.Editor;
import skribler.editor.actions.EditorAction;
import skribler.editor.support.LanguageFront;
import skribler.languages.wtl.actions.BuildWebsiteAction;

public class WTLFront implements LanguageFront {

    private static final ImageIcon ICON = new ImageIcon(WTLFront.class.getResource("/skribler/languages/wtl/resources/blog.png"));

    private WTLExpert expert = new WTLExpert();

    private WTLCoder coder = new WTLCoder();

    public ASTExpert getASTExpert() {
        return this.expert;
    }

    public CSTExpert getCSTExpert() {
        return this.expert;
    }

    public Coder getCoder() {
        return this.coder;
    }

    public String[] getExtensions() {
        return new String[] { ".wtl.ast" };
    }

    public String getDescription() {
        return "WASA Templates Lite";
    }

    public String getShorthand() {
        return "WASA Templates Lite";
    }

    public TreeNode getNewAST() {
        final TreeNodeDeserializer deserializer = new TreeNodeDeserializer();
        final TreeNode ast = deserializer.deserialize(WTLFront.class.getResourceAsStream("/skribler/languages/wtl/resources/website.wtl.ast"));
        return ast;
    }

    public Object getIcon() {
        return ICON;
    }

    public List<EditorAction> getActions(Editor editor) {
        final List<EditorAction> actions = new LinkedList<EditorAction>();
        actions.add(new BuildWebsiteAction(editor));
        return actions;
    }
}
