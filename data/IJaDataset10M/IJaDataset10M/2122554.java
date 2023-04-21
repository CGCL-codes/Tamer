package com.evaserver.rof.script;

/**
 * is the null literal.
 *
 * @author Max Antoni
 * @version $Revision: 2 $
 */
class LiteralNull extends Expression {

    private static LiteralNull INSTANCE = new LiteralNull();

    private static final long serialVersionUID = 1038963159389130486L;

    /**
     * returns the only instance of the null literal.
     *
     * @return the null literal.
     */
    static LiteralNull instance() {
        return INSTANCE;
    }

    /**
     * Singleton constructor.
     */
    private LiteralNull() {
    }

    public void define(Browser inPlugin, StringBuffer inoutBuffer) {
        inoutBuffer.append(TType.NULL);
    }

    public void defineFormatted(StringBuffer inoutBuffer, String inIndentation) {
        inoutBuffer.append(TType.NULL);
    }

    TType eval(ExecutionContext inEC, boolean inReturnReference) {
        return TNull.INSTANCE;
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
