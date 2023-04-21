package x10me.opt.ir;

import x10me.opt.controlflow.BasicBlock;
import x10me.opt.ir.operand.*;

/**
 * The BBMark instruction class.
 *
 * The header comment for {@link Instruction} contains
 * an explanation of the role of these interior class in 
 * opt compiler's IR.
 */
public abstract class BBMark extends Instruction {

    /**        
    * block use BasicBlockOperand.
    */
    private BasicBlockOperand block;

    /**
   * Get the operand called block from this instruction. Note that the
   * returned operand will still point to its containing instruction.
   *
   * @return the operand called block
   */
    public final BasicBlockOperand getBlock() {
        return block;
    }

    /**
   * Get the operand called block from this instruction clearing its 
   * instruction pointer. The returned operand will not point to 
   * any containing instruction.
   * @return the operand called block
   */
    public final BasicBlockOperand getClearBlock() {
        BasicBlockOperand tmp = block;
        block = null;
        clearOperand(tmp);
        return tmp;
    }

    /**
   * Set the operand called block in the argument
   * instruction to the argument operand. The operand will
   * now point to the argument instruction as its containing
   * instruction.
   * @param o the operand to store.
   */
    public final void setBlock(BasicBlockOperand o) {
        if (o == null) {
            block = null;
        } else {
            assert o.instruction == null;
            block = o;
            o.instruction = this;
        }
    }

    /**
   * Does this instruction have a non-null
   * operand named block?
   * @return <code>true</code> if this instruction has a non-null
   *         operand named block or <code>false</code>
   *         if it does not.
   */
    public final boolean hasBlock() {
        return block != null;
    }

    /**
    * Constructor for BBMark.
    *
    * @param block
    */
    BBMark(BasicBlockOperand block) {
        this.block = block;
    }

    @Override
    public final int getNumberOfOperands() {
        return 0 + 1;
    }

    @Override
    public final int getNumberOfDefs() {
        return 0;
    }

    @Override
    public final int getNumberOfUses() {
        return 1;
    }

    @Override
    public final Operand getOperand(int i) {
        switch(i) {
            case 0:
                return block;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public final void putOperand(int i, Operand op) {
        if (op != null) assert op.instruction == null;
        switch(i) {
            case 0:
                block = (BasicBlockOperand) op;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public final BasicBlock getBasicBlock() {
        return block.block;
    }
}
