package engine;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import library.StandardPackage.BIT;
import library.StandardPackage.INTEGER;
import engine.errors.CompilationError;

public class SequenceAggregate<T extends Assignable<T>> implements Aggregate<T>, Assignable<Aggregate<T>>, Iterable<T> {

    protected List<T> value;

    public SequenceAggregate(List<T> explicitContent) {
        value = explicitContent;
    }

    public SequenceAggregate(T... values) {
        this(new ArrayList<T>(Arrays.asList(values)));
    }

    @Override
    public void fillOther(SequenceAggregate<T> aggregate, TimingController controller) {
        if (aggregate.getSize() == getSize()) {
            int i = 0;
            for (T t : aggregate) {
                t.setValue(value.get(i), controller);
                ++i;
            }
        } else {
            throw new CompilationError("Trying to fill aggregate of " + aggregate.getSize() + " elements with " + getSize() + " elements.");
        }
    }

    public T use(INTEGER which, TimingController controller) {
        int w = which.getValue();
        w %= value.size();
        if (w < 0) {
            w += value.size();
        }
        return value.get(w);
    }

    public SequenceAggregate<T> use(Range givenRange, TimingController controller) {
        if ((givenRange.getLow() >= 0) && (givenRange.getHigh()) < value.size()) {
            List<T> content = new ArrayList<T>();
            for (int i : givenRange) {
                content.add(value.get(i));
            }
            SequenceAggregate<T> ret = new SequenceAggregate<T>(content);
            return ret;
        } else {
            throw new CompilationError("Trying to slice range " + givenRange + " out of sequence aggregate of " + value.size() + " elements.");
        }
    }

    @Override
    public void setValue(Aggregate<T> other, TimingController controller) {
        other.fillOther(this, controller);
    }

    @Override
    public List<T> qualifyFor(Range range) {
        if (value.size() != range.getSize()) {
            throw new CompilationError("Trying to qualify sequence aggregate of " + value.size() + " elements for type of " + range.getSize() + " elements.");
        }
        return value;
    }

    public int getSize() {
        return value.size();
    }

    @Override
    public Iterator<T> iterator() {
        return value.iterator();
    }

    @Override
    public String toString() {
        String ret = this.getClass().getSimpleName() + "(";
        for (T t : value) {
            ret += t.toString() + ", ";
        }
        if (value.size() > 0) {
            ret = ret.substring(0, ret.length() - 2);
        }
        ret += ")";
        return ret;
    }

    public static SequenceAggregate<BIT> fromString(char base, String string) throws CompilationError {
        List<BIT> retl = new ArrayList<BIT>();
        int value = Operations.decodeBase(base);
        int fill = (int) (Math.log(value) / Math.log(2));
        for (byte c : string.getBytes()) {
            int v = Operations.valueOfDigit(c);
            if (v > value - 1) {
                throw new CompilationError("Bit string literal of base " + value + " cannot containt digit of value " + v);
            }
            for (int i = fill - 1; i >= 0; --i) {
                if (Operations.isBitAtPosition(v, i)) {
                    retl.add(BIT.of(Type.CONSTANT, BIT.ONE));
                } else {
                    retl.add(BIT.of(Type.CONSTANT, BIT.ZERO));
                }
            }
        }
        return new SequenceAggregate<BIT>(retl);
    }

    @Override
    public ReferenceRepresentation getReferenceRepresentation() {
        ByteArrayOutputStream ret = new ByteArrayOutputStream();
        for (T e : value) {
            byte[] eData = e.getReferenceRepresentation().getData();
            ret.write(eData, 0, eData.length);
        }
        return new ReferenceRepresentation(ret.toByteArray());
    }

    @Override
    public void bind(Aggregate<T> other, TimingController controller) {
        other.bindOther(this, controller);
    }

    @Override
    public void bindOther(SequenceAggregate<T> aggregate, TimingController controller) {
        if (aggregate.getSize() == getSize()) {
            int i = 0;
            for (T t : aggregate) {
                t.bind(value.get(i), controller);
                ++i;
            }
        } else {
            throw new CompilationError("Trying to bind aggregate of " + aggregate.getSize() + " elements to " + getSize() + " elements.");
        }
    }

    public static <ST extends Assignable<ST>> SequenceAggregate<ST> from(ST... elements) {
        return new SequenceAggregate<ST>(elements);
    }
}
