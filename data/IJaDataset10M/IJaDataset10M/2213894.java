package org.jtools.shovel.convert;

import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;
import org.jpattern.mapper.Mapper;
import org.jtools.mapper.number.ReduceScale;

public class ReduceNumberScale implements NumberAdjustFactory {

    private static final Map<RoundingMode, ReduceNumberScale> cachedInstances = new EnumMap<RoundingMode, ReduceNumberScale>(RoundingMode.class);

    private static ReduceNumberScale internalNewInstance(RoundingMode roundingMode) {
        synchronized (cachedInstances) {
            ReduceNumberScale result = cachedInstances.get(roundingMode);
            if (result == null) cachedInstances.put(roundingMode, result = new ReduceNumberScale(roundingMode));
            return result;
        }
    }

    public static ReduceNumberScale getInstance(RoundingMode roundingMode) {
        if (roundingMode == null) roundingMode = RoundingMode.HALF_UP;
        ReduceNumberScale result = cachedInstances.get(roundingMode);
        if (result == null) result = internalNewInstance(roundingMode);
        return result;
    }

    private final RoundingMode roundingMode;

    protected ReduceNumberScale(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }

    @Override
    public Mapper<Number, Number> newMapper(int precision, int scale) {
        return new ReduceScale(scale, roundingMode);
    }
}
