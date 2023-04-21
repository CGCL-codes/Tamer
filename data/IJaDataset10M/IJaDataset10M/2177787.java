package net.sf.jode.flow;

import net.sf.jode.type.Type;
import net.sf.jode.decompiler.TabbedPrintWriter;
import net.sf.jode.decompiler.LocalInfo;
import net.sf.jode.expr.Expression;
import net.sf.jode.expr.StoreInstruction;
import net.sf.jode.expr.LocalStoreOperator;
import net.sf.jode.util.SimpleSet;
import java.util.Set;

/**
 * This is the structured block for atomic instructions.
 */
public class InstructionBlock extends InstructionContainer {

    /**
     * The loads that are on the stack before cond is executed.
     */
    VariableStack stack;

    /**
     * The local to which we push to, if the instruction is non void
     */
    LocalInfo pushedLocal = null;

    /**
     * Tells if this expression is a initializing declaration.  This
     * can only be set to true and then never be reset.  It is changed
     * by makeDeclaration.  */
    boolean isDeclaration = false;

    public InstructionBlock(Expression instr) {
        super(instr);
    }

    /**
     * This does take the instr into account and modifies stack
     * accordingly.  It then calls super.mapStackToLocal.
     * @param stack the stack before the instruction is called
     * @return stack the stack afterwards.
     */
    public VariableStack mapStackToLocal(VariableStack stack) {
        VariableStack newStack;
        int params = instr.getFreeOperandCount();
        if (params > 0) this.stack = stack.peek(params);
        if (instr.getType() != Type.tVoid) {
            pushedLocal = new LocalInfo();
            pushedLocal.setType(instr.getType());
            newStack = stack.poppush(params, pushedLocal);
        } else if (params > 0) {
            newStack = stack.pop(params);
        } else newStack = stack;
        return super.mapStackToLocal(newStack);
    }

    public void removePush() {
        if (stack != null) instr = stack.mergeIntoExpression(instr);
        if (pushedLocal != null) {
            Expression store = new StoreInstruction(new LocalStoreOperator(pushedLocal.getType(), pushedLocal)).addOperand(instr);
            instr = store;
        }
        super.removePush();
    }

    /**
     * Tells if this block needs braces when used in a if or while block.
     * @return true if this block should be sorrounded by braces.
     */
    public boolean needsBraces() {
        return isDeclaration || (declare != null && !declare.isEmpty());
    }

    /**
     * Check if this is an local store instruction to a not yet declared
     * variable.  In that case mark this as declaration and return the 
     * variable.
     */
    public void checkDeclaration(Set declareSet) {
        if (instr instanceof StoreInstruction && (((StoreInstruction) instr).getLValue() instanceof LocalStoreOperator)) {
            StoreInstruction storeOp = (StoreInstruction) instr;
            LocalInfo local = ((LocalStoreOperator) storeOp.getLValue()).getLocalInfo();
            if (declareSet.contains(local)) {
                isDeclaration = true;
                declareSet.remove(local);
            }
        }
    }

    /**
     * Make the declarations, i.e. initialize the declare variable
     * to correct values.  This will declare every variable that
     * is marked as used, but not done.
     * @param done The set of the already declare variables.
     */
    public void makeDeclaration(Set done) {
        super.makeDeclaration(done);
        checkDeclaration(declare);
    }

    public void dumpInstruction(TabbedPrintWriter writer) throws java.io.IOException {
        if (isDeclaration) {
            StoreInstruction store = (StoreInstruction) instr;
            LocalInfo local = ((LocalStoreOperator) store.getLValue()).getLocalInfo();
            writer.startOp(writer.NO_PAREN, 0);
            local.dumpDeclaration(writer);
            writer.breakOp();
            writer.print(" = ");
            store.getSubExpressions()[1].makeInitializer(local.getType());
            store.getSubExpressions()[1].dumpExpression(writer.IMPL_PAREN, writer);
            writer.endOp();
        } else {
            try {
                if (instr.getType() != Type.tVoid) {
                    writer.print("PUSH ");
                    instr.dumpExpression(writer.IMPL_PAREN, writer);
                } else instr.dumpExpression(writer.NO_PAREN, writer);
            } catch (RuntimeException ex) {
                writer.print("(RUNTIME ERROR IN EXPRESSION)");
            }
        }
        writer.println(";");
    }
}
