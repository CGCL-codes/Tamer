package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;

public class StandardMethod extends AbstractStandardObject {

    LispObject[] slots;

    public int getInstanceSlotLength() throws ConditionThrowable {
        return slots.length;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout checkLayout) {
        layout = checkLayout;
    }

    public LispObject getSlot(int index) {
        try {
            return slots[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return type_error(Fixnum.makeFixnum(index), list(SymbolConstants.INTEGER, Fixnum.ZERO, Fixnum.makeFixnum(getInstanceSlotLength())));
        }
    }

    public void setSlot(int index, LispObject value) {
        try {
            slots[index] = value;
        } catch (ArrayIndexOutOfBoundsException e) {
            type_error(Fixnum.makeFixnum(index), list(SymbolConstants.INTEGER, Fixnum.ZERO, Fixnum.makeFixnum(getInstanceSlotLength())));
        }
    }

    public StandardMethod() {
        this(StandardClass.STANDARD_METHOD, StandardClass.STANDARD_METHOD.getClassLayout().getLength());
    }

    protected StandardMethod(LispClass cls, int length) {
        super(cls.getClassLayout());
        slots = new LispObject[length];
        for (int i = slots.length; i-- > 0; ) slots[i] = UNBOUND_VALUE;
    }

    public StandardMethod(StandardGenericFunction gf, Function fastFunction, LispObject lambdaList, LispObject specializers) {
        super(StandardClass.STANDARD_METHOD.getClassLayout());
        slots = new LispObject[layout.getLength()];
        for (int i = slots.length; i-- > StandardMethodClass.SLOT_INDEX_DOCUMENTATION; ) slots[i] = UNBOUND_VALUE;
        slots[StandardMethodClass.SLOT_INDEX_GENERIC_FUNCTION] = gf;
        slots[StandardMethodClass.SLOT_INDEX_LAMBDA_LIST] = lambdaList;
        slots[StandardMethodClass.SLOT_INDEX_SPECIALIZERS] = specializers;
        slots[StandardMethodClass.SLOT_INDEX_QUALIFIERS] = NIL;
        slots[StandardMethodClass.SLOT_INDEX_FUNCTION] = NIL;
        slots[StandardMethodClass.SLOT_INDEX_FAST_FUNCTION] = fastFunction;
        slots[StandardMethodClass.SLOT_INDEX_DOCUMENTATION] = NIL;
    }

    private static final Primitive METHOD_LAMBDA_LIST = new Primitive("method-lambda-list", PACKAGE_SYS, true, "method") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            return checkStandardMethod(arg).slots[StandardMethodClass.SLOT_INDEX_LAMBDA_LIST];
        }
    };

    private static final Primitive SET_METHOD_LAMBDA_LIST = new Primitive("set-method-lambda-list", PACKAGE_SYS, true, "method lambda-list") {

        @Override
        public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
            checkStandardMethod(first).slots[StandardMethodClass.SLOT_INDEX_LAMBDA_LIST] = second;
            return second;
        }
    };

    private static final Primitive _METHOD_QUALIFIERS = new Primitive("%method-qualifiers", PACKAGE_SYS, true, "method") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            return checkStandardMethod(arg).slots[StandardMethodClass.SLOT_INDEX_QUALIFIERS];
        }
    };

    private static final Primitive SET_METHOD_QUALIFIERS = new Primitive("set-method-qualifiers", PACKAGE_SYS, true, "method qualifiers") {

        @Override
        public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
            checkStandardMethod(first).slots[StandardMethodClass.SLOT_INDEX_QUALIFIERS] = second;
            return second;
        }
    };

    private static final Primitive METHOD_DOCUMENTATION = new Primitive("method-documentation", PACKAGE_SYS, true, "method") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            return checkStandardMethod(arg).slots[StandardMethodClass.SLOT_INDEX_DOCUMENTATION];
        }
    };

    private static final Primitive SET_METHOD_DOCUMENTATION = new Primitive("set-method-documentation", PACKAGE_SYS, true, "method documentation") {

        @Override
        public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
            checkStandardMethod(first).slots[StandardMethodClass.SLOT_INDEX_DOCUMENTATION] = second;
            return second;
        }
    };

    public LispObject getFunction() {
        return slots[StandardMethodClass.SLOT_INDEX_FUNCTION];
    }

    @Override
    public String writeToString() throws ConditionThrowable {
        LispObject genericFunction = slots[StandardMethodClass.SLOT_INDEX_GENERIC_FUNCTION];
        if (genericFunction instanceof StandardGenericFunction) {
            LispObject name = ((StandardGenericFunction) genericFunction).getGenericFunctionName();
            if (name != null) {
                FastStringBuffer sb = new FastStringBuffer();
                sb.append(getLispClass().getSymbol().writeToString());
                sb.append(' ');
                sb.append(name.writeToString());
                LispObject specializers = slots[StandardMethodClass.SLOT_INDEX_SPECIALIZERS];
                if (specializers != null) {
                    LispObject specs = specializers;
                    LispObject names = NIL;
                    while (specs != NIL) {
                        LispObject spec = specs.CAR();
                        if (spec instanceof LispClass) names = names.push(((LispClass) spec).getSymbol()); else names = names.push(spec);
                        specs = specs.CDR();
                    }
                    sb.append(' ');
                    sb.append(names.nreverse().writeToString());
                }
                return unreadableString(sb.toString());
            }
        }
        return super.writeToString();
    }

    private static final Primitive _METHOD_GENERIC_FUNCTION = new Primitive("%method-generic-function", PACKAGE_SYS, true) {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            return checkStandardMethod(arg).slots[StandardMethodClass.SLOT_INDEX_GENERIC_FUNCTION];
        }
    };

    private static final Primitive _SET_METHOD_GENERICFUNCTION = new Primitive("%set-method-generic-function", PACKAGE_SYS, true) {

        @Override
        public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
            checkStandardMethod(first).slots[StandardMethodClass.SLOT_INDEX_GENERIC_FUNCTION] = second;
            return second;
        }
    };

    private static final Primitive _METHOD_FUNCTION = new Primitive("%method-function", PACKAGE_SYS, true, "method") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            return checkStandardMethod(arg).slots[StandardMethodClass.SLOT_INDEX_FUNCTION];
        }
    };

    private static final Primitive _SET_METHOD_FUNCTION = new Primitive("%set-method-function", PACKAGE_SYS, true, "method function") {

        @Override
        public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
            checkStandardMethod(first).slots[StandardMethodClass.SLOT_INDEX_FUNCTION] = second;
            return second;
        }
    };

    private static final Primitive _METHOD_FAST_FUNCTION = new Primitive("%method-fast-function", PACKAGE_SYS, true, "method") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            return checkStandardMethod(arg).slots[StandardMethodClass.SLOT_INDEX_FAST_FUNCTION];
        }
    };

    private static final Primitive _SET_METHOD_FAST_FUNCTION = new Primitive("%set-method-fast-function", PACKAGE_SYS, true, "method fast-function") {

        @Override
        public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
            checkStandardMethod(first).slots[StandardMethodClass.SLOT_INDEX_FAST_FUNCTION] = second;
            return second;
        }
    };

    private static final Primitive _METHOD_SPECIALIZERS = new Primitive("%method-specializers", PACKAGE_SYS, true, "method") {

        @Override
        public LispObject execute(LispObject arg) throws ConditionThrowable {
            return checkStandardMethod(arg).slots[StandardMethodClass.SLOT_INDEX_SPECIALIZERS];
        }
    };

    private static final Primitive _SET_METHOD_SPECIALIZERS = new Primitive("%set-method-specializers", PACKAGE_SYS, true, "method specializers") {

        @Override
        public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
            checkStandardMethod(first).slots[StandardMethodClass.SLOT_INDEX_SPECIALIZERS] = second;
            return second;
        }
    };

    private static final StandardGenericFunction METHOD_SPECIALIZERS = new StandardGenericFunction("method-specializers", PACKAGE_MOP, true, _METHOD_SPECIALIZERS, list(SymbolConstants.METHOD), list(StandardClass.STANDARD_METHOD));

    private static final StandardGenericFunction METHOD_QUALIFIERS = new StandardGenericFunction("method-qualifiers", PACKAGE_MOP, true, _METHOD_QUALIFIERS, list(SymbolConstants.METHOD), list(StandardClass.STANDARD_METHOD));

    public static final StandardMethod checkStandardMethod(LispObject first) throws ConditionThrowable {
        if (first instanceof StandardMethod) return (StandardMethod) first;
        return (StandardMethod) type_error(first, SymbolConstants.METHOD);
    }
}
