package cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The node representing a default case clause.
 *
 * @author Jiri Schejbal
 */
public class DefaultCaseNode extends XQNode {

    private final ReturnClauseNode returnClauseNode;

    public DefaultCaseNode(String varName, ReturnClauseNode returnClauseNode) {
        assert (returnClauseNode != null);
        if (varName != null) {
            addAttribute(AttrNames.ATTR_VAR_NAME, varName);
        }
        this.returnClauseNode = returnClauseNode;
    }

    @Override
    protected String getElementName() {
        return NodeNames.NODE_DEFAULT_CASE;
    }

    @Override
    public List<XQNode> getSubnodes() {
        return new ArrayList<XQNode>(Arrays.asList(returnClauseNode));
    }
}
