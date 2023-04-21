package tree;

import compiler.Output;
import descriptor.AbstractDescriptor;
import descriptor.RecordDescriptor;
import descriptor.VarDescriptor;
import symboltable.Symboltable;

public class RecordAccessNode extends AbstractNode {

    private final AbstractNode recordId;

    private final AbstractNode itemId;

    public RecordAccessNode(AbstractNode recordIdNode, AbstractNode itemIdNode) {
        this.recordId = recordIdNode;
        this.itemId = itemIdNode;
    }

    @Override
    public void print(int level) {
        System.out.println(toString(level));
        recordId.print(level + 1);
        itemId.print(level + 1);
    }

    @Override
    public AbstractDescriptor compile(Symboltable st) {
        AbstractDescriptor recordTypeDesc = recordId.compile(st);
        Symboltable recordSymboltable;
        if (recordId instanceof IdentifierNode) {
            recordTypeDesc = ((VarDescriptor) st.get(((IdentifierNode) recordId).getId())).getType();
        }
        recordSymboltable = ((RecordDescriptor) recordTypeDesc).getSymboltable();
        itemId.compile(recordSymboltable);
        Output.add("ADD");
        VarDescriptor vdesc = (VarDescriptor) recordSymboltable.get(((IdentifierNode) itemId).getId());
        recordTypeDesc = vdesc.getType();
        return recordTypeDesc;
    }
}
