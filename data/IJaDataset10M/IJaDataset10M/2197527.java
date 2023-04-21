package x10me.opt.ir;

import x10me.opt.ir.operand.*;

/**
 * The GuardedSet instruction class.
 *
 * The header comment for {@link Instruction} contains
 * an explanation of the role of these interior class in 
 * opt compiler's IR.
 */
public abstract class GuardedSet extends Instruction implements HasGuard {

    /**        
    * ref use Operand.
    */
    private Operand ref;

    /**
   * Get the operand called ref from this instruction. Note that the
   * returned operand will still point to its containing instruction.
   *
   * @return the operand called ref
   */
    public final Operand getRef() {
        return ref;
    }

    /**
   * Get the operand called ref from this instruction clearing its 
   * instruction pointer. The returned operand will not point to 
   * any containing instruction.
   * @return the operand called ref
   */
    public final Operand getClearRef() {
        Operand tmp = ref;
        ref = null;
        clearOperand(tmp);
        return tmp;
    }

    /**
   * Set the operand called ref in the argument
   * instruction to the argument operand. The operand will
   * now point to the argument instruction as its containing
   * instruction.
   * @param o the operand to store.
   */
    public final void setRef(Operand o) {
        if (o == null) {
            ref = null;
        } else {
            assert o.instruction == null;
            ref = o;
            o.instruction = this;
        }
    }

    /**
   * Does this instruction have a non-null
   * operand named ref?
   * @return <code>true</code> if this instruction has a non-null
   *         operand named ref or <code>false</code>
   *         if it does not.
   */
    public final boolean hasRef() {
        return ref != null;
    }

    /**        
    * val use Operand.
    */
    private Operand val;

    /**
   * Get the operand called val from this instruction. Note that the
   * returned operand will still point to its containing instruction.
   *
   * @return the operand called val
   */
    public final Operand getVal() {
        return val;
    }

    /**
   * Get the operand called val from this instruction clearing its 
   * instruction pointer. The returned operand will not point to 
   * any containing instruction.
   * @return the operand called val
   */
    public final Operand getClearVal() {
        Operand tmp = val;
        val = null;
        clearOperand(tmp);
        return tmp;
    }

    /**
   * Set the operand called val in the argument
   * instruction to the argument operand. The operand will
   * now point to the argument instruction as its containing
   * instruction.
   * @param o the operand to store.
   */
    public final void setVal(Operand o) {
        if (o == null) {
            val = null;
        } else {
            assert o.instruction == null;
            val = o;
            o.instruction = this;
        }
    }

    /**
   * Does this instruction have a non-null
   * operand named val?
   * @return <code>true</code> if this instruction has a non-null
   *         operand named val or <code>false</code>
   *         if it does not.
   */
    public final boolean hasVal() {
        return val != null;
    }

    /**        
    * guard use Operand.
    */
    private Operand guard;

    /**
   * Get the operand called guard from this instruction. Note that the
   * returned operand will still point to its containing instruction.
   *
   * @return the operand called guard
   */
    public final Operand getGuard() {
        return guard;
    }

    /**
   * Get the operand called guard from this instruction clearing its 
   * instruction pointer. The returned operand will not point to 
   * any containing instruction.
   * @return the operand called guard
   */
    public final Operand getClearGuard() {
        Operand tmp = guard;
        guard = null;
        clearOperand(tmp);
        return tmp;
    }

    /**
   * Set the operand called guard in the argument
   * instruction to the argument operand. The operand will
   * now point to the argument instruction as its containing
   * instruction.
   * @param o the operand to store.
   */
    public final void setGuard(Operand o) {
        if (o == null) {
            guard = null;
        } else {
            assert o.instruction == null;
            guard = o;
            o.instruction = this;
        }
    }

    /**
   * Does this instruction have a non-null
   * operand named guard?
   * @return <code>true</code> if this instruction has a non-null
   *         operand named guard or <code>false</code>
   *         if it does not.
   */
    public final boolean hasGuard() {
        return guard != null;
    }

    /**
    * Constructor for GuardedSet.
    *
    * @param ref
    * @param val
    * @param guard
    */
    GuardedSet(Operand ref, Operand val, Operand guard) {
        this.ref = ref;
        this.val = val;
        this.guard = guard;
    }

    @Override
    public final int getNumberOfOperands() {
        return 0 + 2;
    }

    @Override
    public final int getNumberOfDefs() {
        return 0;
    }

    @Override
    public final int getNumberOfUses() {
        return 2;
    }

    @Override
    public final Operand getOperand(int i) {
        switch(i) {
            case 0:
                return ref;
            case 1:
                return val;
            case 2:
                return guard;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public final void putOperand(int i, Operand op) {
        if (op != null) assert op.instruction == null;
        switch(i) {
            case 0:
                ref = (Operand) op;
                break;
            case 1:
                val = (Operand) op;
                break;
            case 2:
                guard = (Operand) op;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }
}
