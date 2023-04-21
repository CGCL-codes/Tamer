package tree;

import descriptor.AbstractDescriptor;
import symboltable.Symboltable;

public class TypeDecNode extends AbstractNode {

    private final AbstractNode typeDecNode;

    private final AbstractNode idNode;

    private final AbstractNode typeNode;

    public TypeDecNode(AbstractNode typeDecNode, AbstractNode idNode, AbstractNode typeNode) {
        this.typeDecNode = typeDecNode;
        this.idNode = idNode;
        this.typeNode = typeNode;
    }

    @Override
    public void print(int level) {
        System.out.println(toString(level));
        if (typeDecNode != null) typeDecNode.print(level + 1);
        idNode.print(level + 1);
        typeNode.print(level + 1);
    }

    @Override
    public AbstractDescriptor compile(Symboltable st) {
        AbstractDescriptor type;
        if (typeDecNode != null) typeDecNode.compile(st);
        if (typeNode instanceof RecordTypeNode) {
            Symboltable newSt = new Symboltable();
            newSt.setParentSt(st);
            type = typeNode.compile(newSt);
        } else type = typeNode.compile(st);
        st.put(((IdentifierNode) idNode).getId(), type);
        st.setNextAdress(st.getNextAdress() - type.getSpace());
        return null;
    }
}
