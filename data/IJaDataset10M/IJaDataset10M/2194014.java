package com.evaserver.rof.script;

/**
 * @author Max Antoni
 * @version $Revision: 141 $
 */
class In extends Expression {

    private static final String CODE = " in ";

    private static final long serialVersionUID = -4341588984263826599L;

    private Expression left;

    private Expression right;

    /**
     * standard constructor.
     *
     * @param inLeft the left expression.
     * @param inRight the right expression.
     */
    In(Expression inLeft, Expression inRight) {
        left = inLeft;
        right = inRight;
    }

    public void define(Browser inPlugin, StringBuffer inoutBuffer) {
        left.define(inPlugin, inoutBuffer);
        inoutBuffer.append(CODE);
        right.define(inPlugin, inoutBuffer);
    }

    public void defineFormatted(StringBuffer inoutBuffer, String inIndentation) {
        left.defineFormatted(inoutBuffer, inIndentation);
        inoutBuffer.append(CODE);
        right.defineFormatted(inoutBuffer, inIndentation);
    }

    TType eval(ExecutionContext inContext, boolean inReturnReference) throws ScriptException {
        TType r = right.eval(inContext, false);
        if (!(r instanceof TObject)) {
            String message = Messages.getString("objectExpected", r.toString());
            throw new ScriptException(ScriptException.Type.TYPE_ERROR, message, inContext.getLineNumber());
        }
        TString l = left.eval(inContext, false).toJSString(inContext);
        return TBoolean.valueOf(((TObject) r).hasProperty(l.toString()));
    }

    Expression getLeft() {
        return left;
    }

    Expression getRight() {
        return right;
    }
}
