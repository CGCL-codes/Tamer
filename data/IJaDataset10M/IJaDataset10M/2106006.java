package x10me.opt.ir;

import java.util.HashMap;
import x10me.opt.ir.operand.Operand;
import x10me.opt.ir.operand.RegisterOperand;
import x10me.types.KnownTypes;
import x10me.types.Type;

public final class RegisterPool {

    private Register start, end;

    /**
   * When 2 registers are necessary to encode a result, such as with a long on
   * 32bit architectures, this hash map remembers the pairing of registers. It's
   * key is the 1st register and the value is the 2nd register.
   */
    private final HashMap<Register, Register> _regPairs = new HashMap<Register, Register>();

    /**
   * All registers are assigned unique numbers; currentNum is the counter
   * containing the next available register number.
   */
    private int currentNum;

    /**
   * The number of active registers.
   */
    private int activeRegisters;

    /**
   * Return the first symbolic register in this pool.
   */
    public Register getFirstSymbolicRegister() {
        return start;
    }

    private void registerListappend(Register reg) {
        if (start == null) {
            start = end = reg;
        } else {
            end.append(reg);
            end = reg;
        }
    }

    private void registerListremove(Register e) {
        if (e == start) {
            if (e == end) {
                start = end = null;
            } else {
                Register next = e.next;
                start = next;
                next.prev = null;
            }
        } else if (e == end) {
            Register prev = e.prev;
            end = prev;
            prev.next = null;
        } else {
            e.remove();
        }
    }

    private Register makeNewReg() {
        Register reg = new Register(currentNum);
        currentNum++;
        registerListappend(reg);
        return reg;
    }

    /**
   * Release a now unused register.
   * NOTE: It is the CALLERS responsibility to ensure that the register is no
   * longer used!!!!
   * @param r the register to release
   */
    public void release(RegisterOperand r) {
        Register reg = r.getRegister();
        if (reg.number == currentNum - 1) {
            currentNum--;
            registerListremove(end);
        }
    }

    /**
   * Remove register from register pool.
   */
    public void removeRegister(Register reg) {
        registerListremove(reg);
    }

    public int getNumberOfActiveRegisters() {
        return activeRegisters;
    }

    /**
   * Gets a new address register.
   *
   * @return the newly created register object
   */
    public Register getAddress() {
        Register reg = makeNewReg();
        reg.setAddress();
        return reg;
    }

    /**
   * Gets a new integer register.
   *
   * @return the newly created register object
   */
    public Register getInteger() {
        Register reg = makeNewReg();
        reg.setInteger();
        return reg;
    }

    /**
   * Gets a new float register.
   *
   * @return the newly created register object
   */
    public Register getFloat() {
        Register reg = makeNewReg();
        reg.setFloat();
        return reg;
    }

    /**
   * Gets a new double register.
   *
   * @return the newly created register object
   */
    public Register getDouble() {
        Register reg;
        reg = makeNewReg();
        reg.setDouble();
        return reg;
    }

    /**
   * Gets a new condition register.
   *
   * @return the newly created register object
   */
    public Register getCondition() {
        Register reg = makeNewReg();
        reg.setCondition();
        return reg;
    }

    /**
   * Gets a new long register.
   *
   * @return the newly created register object
   */
    public Register getLong() {
        Register reg;
        reg = makeNewReg();
        reg.setLong();
        return reg;
    }

    /**
   * Gets a new validation register.
   *
   * @return the newly created register object
   */
    public Register getValidation() {
        Register reg = makeNewReg();
        reg.setValidation();
        return reg;
    }

    /**
   * Get a new register of the same type as the argument register
   *
   * @param template the register to get the type from
   * @return the newly created register object
   */
    public Register getReg(Register template) {
        switch(template.getType()) {
            case Register.ADDRESS_TYPE:
                return getAddress();
            case Register.INTEGER_TYPE:
                return getInteger();
            case Register.FLOAT_TYPE:
                return getFloat();
            case Register.DOUBLE_TYPE:
                return getDouble();
            case Register.CONDITION_TYPE:
                return getCondition();
            case Register.LONG_TYPE:
                return getLong();
            case Register.VALIDATION_TYPE:
                return getValidation();
        }
        throw new Error();
    }

    /**
   * Get a new register of the same type as the argument RegisterOperand
   *
   * @param template the register operand to get the type from
   * @return the newly created register object
   */
    public Register getReg(RegisterOperand template) {
        return getReg(template.getRegister());
    }

    /**
   * Get a new register of the appropriate type to hold values of 'type'
   *
   * @param type the type of values that the register will hold
   * @return the newly created register object
   */
    public Register getReg(Type type) {
        if (type.isLongType()) {
            return getLong();
        } else if (type.isDoubleType()) {
            return getDouble();
        } else if (type.isFloatType()) {
            return getFloat();
        } else if (type == KnownTypes.VALIDATION_TYPE) {
            return getValidation();
        } else if (type.isReferenceType()) {
            return getAddress();
        } else {
            return getInteger();
        }
    }

    /**
   * MIR: Get the other half of the register pair that is
   * associated with the argument register.
   * Note: this isn't incredibly general, but all architectures we're currently
   * targeting need at most 2 machine registers to hold Java data values, so
   * for now don't bother implementing a general mechanism.
   *
   * @param reg a register that may already be part of a register pair
   * @return the register that is the other half of the register pair,
   *         if the pairing doesn't already exist then it is created.
   */
    public Register getSecondReg(Register reg) {
        Register otherHalf = _regPairs.get(reg);
        if (otherHalf == null) {
            otherHalf = getReg(reg);
            _regPairs.put(reg, otherHalf);
            if (reg.isLocal()) otherHalf.setLocal();
            if (reg.isSSA()) otherHalf.setSSA();
        }
        return otherHalf;
    }

    /**
   * Make a temporary register operand to hold values of the specified type
   * (a new register is allocated).
   *
   * @param type the type of values to be held in the temp register
   * @return the new temp
   */
    public RegisterOperand makeTemp(Type type) {
        return new RegisterOperand(getReg(type), type);
    }

    /**
   * Make a temporary register operand that is similar to the argument.
   *
   * @param template the register operand to use as a template.
   * @return the new temp
   */
    public RegisterOperand makeTemp(RegisterOperand template) {
        RegisterOperand temp = new RegisterOperand(getReg(template), template.getType());
        temp.addFlags(template.getFlags());
        return temp;
    }

    /**
   * Make a temporary register operand that can hold the values
   * implied by the passed operand.
   *
   * @param op the operand to use as a template.
   * @return the new temp
   */
    public RegisterOperand makeTemp(Operand op) {
        RegisterOperand result;
        if (op.isRegister()) {
            result = makeTemp((RegisterOperand) op);
        } else {
            result = makeTemp(op.getType());
        }
        return result;
    }

    /**
   * Make a temporary to hold an int (allocating a new register).
   *
   * @return the newly created temporary
   */
    public RegisterOperand makeTempInt() {
        return new RegisterOperand(getInteger(), KnownTypes.INT_TYPE);
    }

    /**
   * Make a temporary to hold a boolean (allocating a new register).
   *
   * @return the newly created temporary
   */
    public RegisterOperand makeTempBoolean() {
        return new RegisterOperand(getInteger(), KnownTypes.BOOLEAN_TYPE);
    }

    /**
   * Make a temporary to hold a float (allocating a new register).
   *
   * @return the newly created temporary
   */
    public RegisterOperand makeTempFloat() {
        return new RegisterOperand(getFloat(), KnownTypes.FLOAT_TYPE);
    }

    /**
   * Make a temporary to hold a double (allocating a new register).
   *
   * @return the newly created temporary
   */
    public RegisterOperand makeTempDouble() {
        return new RegisterOperand(getDouble(), KnownTypes.DOUBLE_TYPE);
    }

    /**
   * Make a temporary to hold a long (allocating a new register).
   *
   * @return the newly created temporary
   */
    public RegisterOperand makeTempLong() {
        return new RegisterOperand(getLong(), KnownTypes.LONG_TYPE);
    }

    /**
   * Make a temporary to hold a condition code (allocating a new register).
   *
   * @return the newly created temporary
   */
    public RegisterOperand makeTempCondition() {
        Register reg = getCondition();
        return new RegisterOperand(reg, KnownTypes.INT_TYPE);
    }

    /**
   * Make a temporary to hold a guard (validation) (allocating a new register).
   *
   * @return the newly created temporary
   */
    public RegisterOperand makeTempValidation() {
        Register reg = getValidation();
        reg.setValidation();
        return new RegisterOperand(reg, KnownTypes.VALIDATION_TYPE);
    }
}
