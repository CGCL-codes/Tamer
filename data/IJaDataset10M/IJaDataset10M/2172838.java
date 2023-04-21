package org.openwar.victory.script.logic;

public class XorLogic implements Logic {

    private Logic[] operands;

    public XorLogic(final Logic[] theOperands) {
        operands = theOperands;
    }

    @Override
    public boolean evaluate() {
        boolean ev = false;
        for (Logic c : operands) {
            if (c.evaluate()) {
                if (!ev) {
                    ev = true;
                    break;
                } else {
                    return false;
                }
            }
        }
        return ev;
    }
}
