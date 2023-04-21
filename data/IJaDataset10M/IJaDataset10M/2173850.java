package MScheme.values;

import MScheme.Value;
import MScheme.List;
import MScheme.Code;
import MScheme.machine.Machine;
import MScheme.environment.StaticEnvironment;
import MScheme.code.CodeList;
import MScheme.exceptions.*;

final class PairOrList extends Pair implements List {

    public static final String id = "$Id: PairOrList.java 419 2001-09-30 20:26:00Z sielenk $";

    private PairOrList(boolean isConst, Value first, Value second) {
        super(isConst, first, second);
    }

    public static List prepend(Value head, List tail) {
        return new PairOrList(false, head, tail);
    }

    public static Pair create(Value first, Value second) {
        return new PairOrList(false, first, second);
    }

    public static Pair createConst(Value first, Value second) {
        return new PairOrList(true, first.getConst(), second.getConst());
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isList() {
        return getSecond().isList();
    }

    public List toList() throws ListExpected {
        return isList() ? this : super.toList();
    }

    public int getLength() throws ListExpected {
        try {
            int result = 1;
            for (List tail = getTail(); !tail.isEmpty(); tail = tail.getTail()) {
                ++result;
            }
            return result;
        } catch (PairExpected e) {
            throw new RuntimeException("unexpected PairExpected");
        }
    }

    public final List getReversed() throws ListExpected {
        try {
            List result = Empty.create();
            for (List rest = this; !rest.isEmpty(); rest = rest.getTail()) {
                result = ListFactory.prepend(rest.getHead(), result);
            }
            return result;
        } catch (PairExpected e) {
            throw new RuntimeException("unexpected PairExpected");
        }
    }

    public Value getHead() {
        return getFirst();
    }

    public List getTail() throws ListExpected {
        return getSecond().toList();
    }

    public Code getCode(StaticEnvironment compilationEnv) throws SchemeException {
        return getHead().getTranslator(compilationEnv).translate(compilationEnv, getTail());
    }

    public CodeList getCodeList(StaticEnvironment compilationEnv) throws SchemeException {
        return CodeList.prepend(getHead().getCode(compilationEnv), getTail().getCodeList(compilationEnv));
    }
}
