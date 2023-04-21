package cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes;

import java.util.List;

/**
 * The node representing a literal.
 *
 * @author Jiri Schejbal
 */
public class LiteralNode extends ExprNode {

    private final LiteralType literalType;

    private final String value;

    public LiteralNode(LiteralType type, String value) {
        assert (type != null);
        assert (value != null);
        this.literalType = type;
        this.value = value;
    }

    public LiteralNode(StringLiteral stringLiteral) {
        this(LiteralType.STRING, stringLiteral.getValue());
        assert (stringLiteral != null);
        addAttribute(AttrNames.ATTR_QUOT_MARK, stringLiteral.getQuotMark().toString());
    }

    @Override
    protected String getElementName() {
        return NodeNames.NODE_LITERAL;
    }

    @Override
    public List<XQNode> getSubnodes() {
        return null;
    }

    public LiteralType getLiteralType() {
        return literalType;
    }
}
