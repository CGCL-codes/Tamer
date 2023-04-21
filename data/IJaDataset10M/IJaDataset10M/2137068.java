package piaba.symlib.array;

import piaba.symlib.SymRel;
import piaba.symlib.number.SymInt;
import piaba.symlib.number.SymIntConst;
import piaba.symlib.number.SymNumber;
import piaba.util.Constants;

public class SymArray<T> {

    private T[] array;

    public SymArray(T[] array) {
        this.array = array;
    }

    public void set(SymNumber i, T value) {
        if (i.isCte()) {
            array[i.getInt()] = value;
        } else {
            int j = split(i);
            array[j] = value;
        }
    }

    public T get(SymNumber i) {
        if (i.isCte()) {
            return this.array[i.getInt()];
        } else {
            int j = split(i);
            return this.array[j];
        }
    }

    private int split(SymNumber i) {
        int k = Constants.STATE.getNextChoice(0, array.length - 1);
        Constants.STATE.addConstraint(SymRel.eq(i, SymIntConst.create(k)));
        return k;
    }

    public SymInt length() {
        return SymIntConst.create(array.length);
    }
}
