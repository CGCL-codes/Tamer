package su.msu.cs.lvk.xml2pixy.parser;

import at.ac.tuwien.infosys.www.phpparser.ParseNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Non-recursively deep-first walks ParseNode trees using given visitor.
 */
public class ParseNodeWalker {

    ParseNodeVisitor visitor;

    public ParseNodeWalker(ParseNodeVisitor visitor) {
        this.visitor = visitor;
    }

    public void walk(ParseNode root) {
        if (root == null) return;
        ArrayList<ParseNode> nodes = new ArrayList<ParseNode>();
        nodes.add(root);
        for (int i = 0; i < nodes.size(); i++) {
            ParseNode node = nodes.get(i);
            List children = node.getChildren();
            for (int j = 0; j < children.size(); j++) {
                ParseNode child = (ParseNode) children.get(j);
                nodes.add(i + 1 + j, child);
            }
        }
        for (ParseNode node1 : nodes) {
            visitor.visit(node1);
        }
    }
}
