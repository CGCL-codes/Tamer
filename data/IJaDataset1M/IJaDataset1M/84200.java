package MScheme.functions;

import MScheme.util.Arity;
import MScheme.Value;
import MScheme.machine.Machine;
import MScheme.environment.Environment;
import MScheme.values.*;
import MScheme.exceptions.*;

final class Adder extends Reducer {

    public static final String id = "$Id: Builtins.java 321 2001-09-07 12:05:58Z sielenk $";

    Adder() {
        super(ScmNumber.create(0));
    }

    protected Value combine(Value fst, Value snd) throws NumberExpected {
        return fst.toScmNumber().plus(snd.toScmNumber());
    }
}

final class Suber extends Reducer {

    public static final String id = "$Id: Builtins.java 321 2001-09-07 12:05:58Z sielenk $";

    Suber(ScmNumber first) {
        super(first);
    }

    protected Value combine(Value fst, Value snd) throws NumberExpected {
        return fst.toScmNumber().minus(snd.toScmNumber());
    }
}

final class Multiplier extends Reducer {

    public static final String id = "$Id: Builtins.java 321 2001-09-07 12:05:58Z sielenk $";

    Multiplier() {
        super(ScmNumber.create(1));
    }

    protected Value combine(Value fst, Value snd) throws NumberExpected {
        return fst.toScmNumber().times(snd.toScmNumber());
    }
}

final class Order {

    public static final String id = "$Id: Builtins.java 321 2001-09-07 12:05:58Z sielenk $";

    private static final Arity _arity = Arity.atLeast(2);

    public static final int LT = -2;

    public static final int LE = -1;

    public static final int EQ = 0;

    public static final int GE = 1;

    public static final int GT = 2;

    public static final boolean check(List arguments, int mode) throws RuntimeError, TypeError {
        int len = arguments.getLength();
        if (!_arity.isValid(len)) {
            throw new RuntimeArityError(arguments, _arity);
        }
        ScmNumber curr = arguments.getHead().toScmNumber();
        List tail = arguments.getTail();
        boolean rising = true;
        boolean strict = true;
        boolean falling = true;
        do {
            ScmNumber next = tail.getHead().toScmNumber();
            tail = tail.getTail();
            if (curr.isEqualTo(next)) {
                strict = false;
            } else {
                if (curr.isLessThan(next)) {
                    falling = false;
                } else {
                    rising = false;
                }
                if (!rising & !falling) {
                    return false;
                }
            }
            curr = next;
        } while (!tail.isEmpty());
        switch(mode) {
            case LT:
                return strict & rising;
            case LE:
                return rising;
            case EQ:
                return rising & falling;
            case GE:
                return falling;
            case GT:
                return strict & falling;
        }
        return false;
    }
}

public class Builtins {

    public static final String id = "$Id: Builtins.java 321 2001-09-07 12:05:58Z sielenk $";

    public static final Value eq_3F(Value fst, Value snd) {
        return ScmBoolean.create(fst.eq(snd));
    }

    public static final Value eqv_3F(Value fst, Value snd) {
        return ScmBoolean.create(fst.eqv(snd));
    }

    public static final Value equal_3F(Value fst, Value snd) {
        return ScmBoolean.create(fst.equal(snd));
    }

    public static final Value number_3F(Value argument) {
        return ScmBoolean.create(argument.isScmNumber());
    }

    public static final Value complex_3F(Value argument) {
        return ScmBoolean.create(argument.isScmNumber());
    }

    public static final Value real_3F(Value argument) {
        return ScmBoolean.create(argument.isScmNumber());
    }

    public static final Value rational_3F(Value argument) {
        return ScmBoolean.create(argument.isScmNumber());
    }

    public static final Value integer_3F(Value argument) {
        return ScmBoolean.create(argument.isScmNumber());
    }

    public static final Value exact_3F(Value argument) {
        return ScmBoolean.create(argument.isScmNumber());
    }

    public static final Value inexact_3F(Value argument) {
        return ScmBoolean.createFalse();
    }

    public static final Value _3C(List arguments) throws RuntimeError, TypeError {
        return ScmBoolean.create(Order.check(arguments, Order.LT));
    }

    public static final Value _3C_3D(List arguments) throws RuntimeError, TypeError {
        return ScmBoolean.create(Order.check(arguments, Order.LE));
    }

    public static final Value _3D(List arguments) throws RuntimeError, TypeError {
        return ScmBoolean.create(Order.check(arguments, Order.EQ));
    }

    public static final Value _3E_3D(List arguments) throws RuntimeError, TypeError {
        return ScmBoolean.create(Order.check(arguments, Order.GE));
    }

    public static final Value _3E(List arguments) throws RuntimeError, TypeError {
        return ScmBoolean.create(Order.check(arguments, Order.GT));
    }

    public static final Value zero_3F(Value argument) throws TypeError {
        return ScmBoolean.create(argument.toScmNumber().getInteger() == 0);
    }

    private static final Adder ADDER = new Adder();

    public static final Value _2B(List arguments) throws RuntimeError, TypeError {
        return ADDER.reduceLeft(arguments);
    }

    private static final Arity AT_LEAST_1 = Arity.atLeast(1);

    public static final Value _2D(List arguments) throws RuntimeError, TypeError {
        int len = Function.checkArguments(AT_LEAST_1, arguments);
        ScmNumber first = arguments.getHead().toScmNumber();
        if (len == 1) {
            return first.negated();
        } else {
            return new Suber(first).foldLeft(arguments.getTail());
        }
    }

    private static final Multiplier MULTIPLITER = new Multiplier();

    public static final Value _2A(List arguments) throws RuntimeError, TypeError {
        return MULTIPLITER.reduceLeft(arguments);
    }

    public static final Value not(Value argument) {
        return ScmBoolean.create(!argument.isTrue());
    }

    public static final Value boolean_3F(Value argument) {
        return ScmBoolean.create(argument.isScmBoolean());
    }

    public static final Value pair_3F(Value argument) {
        return ScmBoolean.create(argument.isPair());
    }

    public static final Value cons(Value fst, Value snd) {
        return Pair.createMutable(fst, snd);
    }

    public static final Value car(Value argument) throws PairExpected {
        return argument.toPair().getFirst();
    }

    public static final Value cdr(Value argument) throws PairExpected {
        return argument.toPair().getSecond();
    }

    public static final Value set_2Dcar_21(Value fst, Value snd) throws PairExpected, ImmutableException {
        fst.toPair().setFirst(snd);
        return snd;
    }

    public static final Value set_2Dcdr_21(Value fst, Value snd) throws PairExpected, ImmutableException {
        fst.toPair().setSecond(snd);
        return snd;
    }

    public static final Value null_3F(Value argument) {
        return ScmBoolean.create(argument.eq(Empty.create()));
    }

    public static final Value list_3F(Value argument) {
        return ScmBoolean.create(argument.isList());
    }

    public static final Value list(List argument) {
        return argument.toValue();
    }

    public static final Value length(Value argument) throws ListExpected {
        return ScmNumber.create(argument.toList().getLength());
    }

    public static final Function append = AppendFunction.INSTANCE;

    public static final Value reverse(Value argument) throws ListExpected {
        return argument.toList().getReversed().toValue();
    }

    public static final Function memq = MemqFunction.INSTANCE;

    public static final Function memv = MemvFunction.INSTANCE;

    public static final Function member = MemberFunction.INSTANCE;

    public static final Function assq = AssqFunction.INSTANCE;

    public static final Function assv = AssvFunction.INSTANCE;

    public static final Function assoc = AssocFunction.INSTANCE;

    public static final Value symbol_3F(Value argument) {
        return ScmBoolean.create(argument.isSymbol());
    }

    public static final Value symbol_2D_3Estring(Value argument) throws SymbolExpected {
        return ScmString.create(argument.toSymbol());
    }

    public static final Value string_2D_3Esymbol(Value argument) throws StringExpected {
        return Symbol.create(argument.toScmString());
    }

    public static final Value char_3F(Value argument) {
        return ScmBoolean.create(argument.isScmChar());
    }

    public static final Value char_3C_3F(Value fst, Value snd) throws CharExpected {
        return ScmBoolean.create(fst.toScmChar().getJavaChar() < snd.toScmChar().getJavaChar());
    }

    public static final Value char_3C_3D_3F(Value fst, Value snd) throws CharExpected {
        return ScmBoolean.create(fst.toScmChar().getJavaChar() <= snd.toScmChar().getJavaChar());
    }

    public static final Value char_3D_3F(Value fst, Value snd) throws CharExpected {
        return ScmBoolean.create(fst.toScmChar().getJavaChar() == snd.toScmChar().getJavaChar());
    }

    public static final Value char_3E_3D_3F(Value fst, Value snd) throws CharExpected {
        return ScmBoolean.create(fst.toScmChar().getJavaChar() >= snd.toScmChar().getJavaChar());
    }

    public static final Value char_3E_3F(Value fst, Value snd) throws CharExpected {
        return ScmBoolean.create(fst.toScmChar().getJavaChar() > snd.toScmChar().getJavaChar());
    }

    public static final Value char_2D_3Einteger(Value argument) throws CharExpected {
        return ScmNumber.create(argument.toScmChar().getJavaChar());
    }

    public static final Value integer_2D_3Echar(Value argument) throws NumberExpected {
        return ScmChar.create((char) argument.toScmNumber().getInteger());
    }

    public static final Value char_2Dupcase(Value argument) throws CharExpected {
        return ScmChar.create(Character.toUpperCase(argument.toScmChar().getJavaChar()));
    }

    public static final Value char_2Ddowncase(Value argument) throws CharExpected {
        return ScmChar.create(Character.toLowerCase(argument.toScmChar().getJavaChar()));
    }

    public static final Value string_3F(Value argument) {
        return ScmBoolean.create(argument.isScmString());
    }

    public static final Value vector_3F(Value argument) {
        return ScmBoolean.create(argument.isScmVector());
    }

    public static final Value vector(List arguments) throws ListExpected {
        return ScmVector.create(arguments);
    }

    public static final Value vector_2D_3Elist(Value argument) throws VectorExpected {
        return argument.toScmVector().getList().toValue();
    }

    public static final Value list_2D_3Evector(Value argument) throws ListExpected {
        return ScmVector.create(argument.toList());
    }

    public static final Value procedure_3F(Value argument) {
        return ScmBoolean.create(argument.isFunction());
    }

    public static final Function apply = ApplyFunction.INSTANCE;

    public static final Function call_2Dwith_2Dcurrent_2Dcontinuation = CallCCFunction.INSTANCE;

    public static final Function dynamic_2Dwind = DynamicWindFunction.INSTANCE;

    public static final Value eval(Value fst, Value snd) throws RuntimeError, TypeError {
        try {
            return new Machine(snd.toEnvironment()).evaluate(fst);
        } catch (CompileError e) {
            throw new RuntimeError(fst);
        }
    }

    public static final Value scheme_2Dreport_2Denvironment(Value fst) throws RuntimeError, TypeError {
        if (fst.toScmNumber().getInteger() != 5) {
            throw new RuntimeError(fst);
        }
        return Environment.getSchemeReportEnvironment();
    }

    public static final Value null_2Denvironment(Value fst) throws RuntimeError, TypeError {
        if (fst.toScmNumber().getInteger() != 5) {
            throw new RuntimeError(fst);
        }
        return Environment.getNullEnvironment();
    }

    public static final Value port_3F(Value argument) {
        return ScmBoolean.create(argument.isPort());
    }

    public static final Value input_2Dport_3F(Value argument) throws PortExpected {
        return ScmBoolean.create(argument instanceof InputPort);
    }

    public static final Value output_2Dport_3F(Value argument) throws PortExpected {
        return ScmBoolean.create(argument instanceof OutputPort);
    }

    public static final Value open_2Dinput_2Dfile(Value argument) throws StringExpected, OpenException {
        return argument.isTrue() ? InputPort.create(argument.toScmString()) : InputPort.create();
    }

    public static final Value open_2Doutput_2Dfile(Value argument) throws StringExpected, OpenException {
        return argument.isTrue() ? OutputPort.create(argument.toScmString()) : OutputPort.create();
    }

    public static final Value close_2Dinput_2Dport(Value argument) throws PortExpected, CloseException {
        argument.toInputPort().close();
        return argument;
    }

    public static final Value close_2Doutput_2Dport(Value argument) throws PortExpected, CloseException {
        argument.toOutputPort().close();
        return argument;
    }

    public static final Value read(Value fst) throws RuntimeError, TypeError {
        return fst.toInputPort().read();
    }

    public static final Value read_2Dchar(Value fst) throws RuntimeError, TypeError {
        return fst.toInputPort().readScmChar();
    }

    public static final Value peek_2Dchar(Value fst) throws RuntimeError, TypeError {
        return fst.toInputPort().peekScmChar();
    }

    public static final Value eof_2Dobject_3F(Value fst) {
        return ScmBoolean.create(fst.eq(InputPort.EOF_VALUE));
    }

    public static final Value char_2Dready_3F(Value fst) throws TypeError {
        return ScmBoolean.create(fst.toInputPort().isReady());
    }

    public static final Value write(Value fst, Value snd) throws RuntimeError, TypeError {
        snd.toOutputPort().write(fst);
        return snd;
    }

    public static final Value display(Value fst, Value snd) throws RuntimeError, TypeError {
        snd.toOutputPort().display(fst);
        return snd;
    }

    public static final Value write_2Dchar(Value fst, Value snd) throws RuntimeError, TypeError {
        snd.toOutputPort().writeScmChar(fst.toScmChar());
        return snd;
    }
}
