package jaxlib.closure;

import java.nio.ByteBuffer;
import jaxlib.util.BiDi;
import jaxlib.jaxlib_private.CheckArg;

/**
 * Provides tools for using instances of <tt>IntFilter</tt>, <tt>ByteClosure</tt> and <tt>IntTransformer</tt>.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: ForEachByte.java 1073 2004-04-09 18:35:48Z joerg_wassmer $
 */
public abstract class ForEachByte extends Object {

    protected ForEachByte() throws InstantiationException {
        throw new InstantiationException();
    }

    public static void apply(byte[] data, IntTransformer function) {
        apply(data, 0, data.length, BiDi.FORWARD, function);
    }

    public static void apply(byte[] data, int fromIndex, int toIndex, IntTransformer function) {
        apply(data, fromIndex, toIndex, BiDi.FORWARD, function);
    }

    public static void apply(byte[] data, int fromIndex, int toIndex, BiDi dir, IntTransformer function) {
        if (function instanceof IntFunction) {
            ((IntFunction) function).forEachApply(data, fromIndex, toIndex, dir);
            return;
        }
        CheckArg.range(data.length, fromIndex, toIndex);
        for (int i = dir.start(fromIndex, toIndex), end = dir.end(fromIndex, toIndex), sign = dir.sign; i != end; i += sign) data[i] = (byte) function.apply(data[i]);
    }

    public static void apply(ByteBuffer data, int fromIndex, int toIndex, IntTransformer function) {
        apply(data, fromIndex, toIndex, BiDi.FORWARD, function);
    }

    public static void apply(ByteBuffer data, int fromIndex, int toIndex, BiDi dir, IntTransformer function) {
        CheckArg.range(data, fromIndex, toIndex);
        if (fromIndex == toIndex) return;
        if (data.hasArray()) {
            int ao = data.arrayOffset();
            apply(data.array(), ao + fromIndex, ao + toIndex, dir, function);
            return;
        }
        if (function instanceof IntFunction) {
            ((IntFunction) function).forEachApply(data, fromIndex, toIndex, dir);
            return;
        }
        for (int i = dir.start(fromIndex, toIndex), end = dir.end(fromIndex, toIndex), sign = dir.sign; i != end; i += sign) data.put(i, (byte) function.apply(data.get(i)));
    }

    public static int count(byte[] data, IntFilter condition, boolean iF) {
        return countUp(data, 0, data.length, BiDi.FORWARD, condition, iF, -1, false);
    }

    public static int count(byte[] data, int fromIndex, int toIndex, IntFilter condition, boolean iF) {
        return countUp(data, fromIndex, toIndex, BiDi.FORWARD, condition, iF, -1, false);
    }

    public static int count(byte[] data, int fromIndex, int toIndex, BiDi dir, IntFilter condition, boolean iF) {
        return countUp(data, fromIndex, toIndex, dir, condition, iF, -1, false);
    }

    public static int countUp(byte[] data, IntFilter condition, boolean iF, int maxCount) {
        return countUp(data, 0, data.length, BiDi.FORWARD, condition, iF, maxCount, false);
    }

    public static int countUp(byte[] data, int fromIndex, int toIndex, IntFilter condition, boolean iF, int maxCount) {
        return countUp(data, fromIndex, toIndex, BiDi.FORWARD, condition, iF, maxCount, false);
    }

    public static int countUp(byte[] data, int fromIndex, int toIndex, BiDi dir, IntFilter condition, boolean iF, int maxCount) {
        return countUp(data, fromIndex, toIndex, dir, condition, iF, maxCount, false);
    }

    public static int countUp(byte[] data, int fromIndex, int toIndex, BiDi dir, IntFilter condition, boolean iF, int maxCount, boolean stopOnDismatch) {
        CheckArg.maxCount(maxCount);
        CheckArg.range(data.length, fromIndex, toIndex);
        if (maxCount == 0 || fromIndex == toIndex) return 0;
        int count = 0;
        for (int i = dir.start(fromIndex, toIndex), end = dir.end(fromIndex, toIndex), sign = dir.sign; i != end; i += sign) {
            if (condition.accept(data[i]) == iF) {
                if (++count == maxCount) return count;
            } else if (stopOnDismatch) return -(count + 1);
        }
        return count;
    }

    public static int count(ByteBuffer data, int fromIndex, int toIndex, IntFilter condition, boolean iF) {
        return countUp(data, fromIndex, toIndex, BiDi.FORWARD, condition, iF, -1, false);
    }

    public static int count(ByteBuffer data, int fromIndex, int toIndex, BiDi dir, IntFilter condition, boolean iF) {
        return countUp(data, fromIndex, toIndex, dir, condition, iF, -1, false);
    }

    public static int countUp(ByteBuffer data, int fromIndex, int toIndex, IntFilter condition, boolean iF, int maxCount) {
        return countUp(data, fromIndex, toIndex, BiDi.FORWARD, condition, iF, maxCount, false);
    }

    public static int countUp(ByteBuffer data, int fromIndex, int toIndex, BiDi dir, IntFilter condition, boolean iF, int maxCount) {
        return countUp(data, fromIndex, toIndex, dir, condition, iF, maxCount, false);
    }

    public static int countUp(ByteBuffer data, int fromIndex, int toIndex, BiDi dir, IntFilter condition, boolean iF, int maxCount, boolean stopOnDismatch) {
        CheckArg.maxCount(maxCount);
        CheckArg.range(data, fromIndex, toIndex);
        if (maxCount == 0 || fromIndex == toIndex) return 0;
        if (data.hasArray()) {
            int ao = data.arrayOffset();
            return countUp(data.array(), ao + fromIndex, ao + toIndex, dir, condition, iF, maxCount, stopOnDismatch);
        }
        int count = 0;
        for (int i = dir.start(fromIndex, toIndex), end = dir.end(fromIndex, toIndex), sign = dir.sign; i != end; i += sign) {
            if (condition.accept(data.get(i)) == iF) {
                if (++count == maxCount) return count;
            } else if (stopOnDismatch) return -(count + 1);
        }
        return count;
    }

    public static int proceed(byte[] data, ByteClosure procedure) {
        return proceed(data, 0, data.length, BiDi.FORWARD, procedure);
    }

    public static int proceed(byte[] data, int fromIndex, int toIndex, ByteClosure procedure) {
        return proceed(data, fromIndex, toIndex, BiDi.FORWARD, procedure);
    }

    public static int proceed(byte[] data, int fromIndex, int toIndex, BiDi dir, ByteClosure procedure) {
        CheckArg.range(data.length, fromIndex, toIndex);
        int count = 0;
        for (int i = dir.start(fromIndex, toIndex), end = dir.end(fromIndex, toIndex), sign = dir.sign; i != end; i += sign) {
            if (procedure.proceed(data[i])) count++; else break;
        }
        return count;
    }

    public static int proceed(ByteBuffer data, int fromIndex, int toIndex, ByteClosure procedure) {
        return proceed(data, fromIndex, toIndex, BiDi.FORWARD, procedure);
    }

    public static int proceed(ByteBuffer data, int fromIndex, int toIndex, BiDi dir, ByteClosure procedure) {
        CheckArg.range(data, fromIndex, toIndex);
        if (fromIndex == toIndex) return 0;
        if (data.hasArray()) {
            int ao = data.arrayOffset();
            return proceed(data.array(), ao + fromIndex, ao + toIndex, dir, procedure);
        }
        int count = 0;
        for (int i = dir.start(fromIndex, toIndex), end = dir.end(fromIndex, toIndex), sign = dir.sign; i != end; i += sign) {
            if (procedure.proceed(data.get(i))) count++; else break;
        }
        return count;
    }
}
