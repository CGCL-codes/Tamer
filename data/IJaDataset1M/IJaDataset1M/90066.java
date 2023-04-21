package net.homelinux.chaoswg.io.makejavauseful.lazyevaluation;

import java.lang.ref.SoftReference;
import net.homelinux.chaoswg.io.makejavauseful.higherorderfunctions.ConstantFunction;

public class SoftReferencedDelay<T> extends Delay<T> {

    private SoftReference<T> result = null;

    public SoftReferencedDelay(ConstantFunction<T> cf) {
        super(cf);
        this.cf = cf;
    }

    public synchronized T force() {
        T toReturn = result.get();
        if (toReturn == null) {
            toReturn = cf.apply();
            result = new SoftReference<T>(toReturn);
        }
        return toReturn;
    }
}
