package net.sf.joafip.store.entity.proxy;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.store.service.bytecode.EnhanceException;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class OpcodeNodeDup2X2 extends OpcodeNode {

    public OpcodeNodeDup2X2(final int opcode, final int address, final int lineNumber, final OpcodeNode previousOpcode) {
        super(opcode, address, lineNumber, previousOpcode);
    }

    @Override
    protected StackElement interprete(final StackElement currentStackElement) throws EnhanceException {
        return OPCODE_INTERPRETER.interpreteDup2X2(currentStackElement);
    }
}
