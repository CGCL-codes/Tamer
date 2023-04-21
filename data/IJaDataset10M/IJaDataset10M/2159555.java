package org.gcreator.pineapple.pinedl.statements;

/**
 * Represents an integer constant
 * @author Luís Reis
 */
public class IntConstant extends Constant {

    public int value = 0;

    public IntConstant(String value) {
        try {
            this.value = Integer.parseInt(value);
        } catch (Exception e) {
        }
    }

    public IntConstant(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
