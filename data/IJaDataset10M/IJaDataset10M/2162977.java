package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;

public class JavaStackFrame extends StackFrame {

    public final StackTraceElement javaFrame;

    public JavaStackFrame(StackTraceElement javaFrame) {
        this.javaFrame = javaFrame;
    }

    @Override
    public LispObject typeOf() {
        return SymbolConstants.JAVA_STACK_FRAME;
    }

    @Override
    public LispObject classOf() {
        return BuiltInClass.JAVA_STACK_FRAME;
    }

    @Override
    public String writeToString() {
        String result = null;
        final String JAVA_STACK_FRAME = "JAVA-STACK-FRAME";
        try {
            result = unreadableString(JAVA_STACK_FRAME + " " + toLispString().toString());
        } catch (ConditionThrowable t) {
            Debug.trace("Implementation error: ");
            Debug.trace(t);
            result = unreadableString(JAVA_STACK_FRAME);
        }
        return result;
    }

    @Override
    public LispObject typep(LispObject typeSpecifier) throws ConditionThrowable {
        if (typeSpecifier == SymbolConstants.JAVA_STACK_FRAME) return T;
        if (typeSpecifier == BuiltInClass.JAVA_STACK_FRAME) return T;
        return super.typep(typeSpecifier);
    }

    static final Symbol CLASS = internKeyword("CLASS");

    static final Symbol METHOD = internKeyword("METHOD");

    static final Symbol FILE = internKeyword("FILE");

    static final Symbol LINE = internKeyword("LINE");

    static final Symbol NATIVE_METHOD = internKeyword("NATIVE-METHOD");

    public LispObject toLispList() throws ConditionThrowable {
        LispObject result = Lisp.NIL;
        if (javaFrame == null) return result;
        result = result.push(CLASS);
        result = result.push(new SimpleString(javaFrame.getClassName()));
        result = result.push(METHOD);
        result = result.push(new SimpleString(javaFrame.getMethodName()));
        result = result.push(FILE);
        result = result.push(new SimpleString(javaFrame.getFileName()));
        result = result.push(LINE);
        result = result.push(Fixnum.makeFixnum(javaFrame.getLineNumber()));
        if (javaFrame.isNativeMethod()) {
            result = result.push(NATIVE_METHOD);
            result = result.push(SymbolConstants.T);
        }
        return result.nreverse();
    }

    @Override
    public SimpleString toLispString() throws ConditionThrowable {
        return new SimpleString(javaFrame.toString());
    }

    @Override
    public LispObject getParts() throws ConditionThrowable {
        LispObject result = NIL;
        result = result.push(makeCons("CLASS", new SimpleString(javaFrame.getClassName())));
        result = result.push(makeCons("METHOD", new SimpleString(javaFrame.getMethodName())));
        result = result.push(makeCons("FILE", new SimpleString(javaFrame.getFileName())));
        result = result.push(makeCons("LINE", Fixnum.makeFixnum(javaFrame.getLineNumber())));
        result = result.push(makeCons("NATIVE-METHOD", javaFrame.isNativeMethod() ? T : NIL));
        return result.nreverse();
    }
}
