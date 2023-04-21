package com.twolattes.json.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import com.twolattes.json.Json;

public class BigIntegerType extends NullSafeType<BigInteger, Json.Number> {

    @Override
    protected Json.Number nullSafeMarshall(BigInteger entity) {
        return Json.number(BigDecimal.valueOf(entity.longValue()));
    }

    @Override
    protected BigInteger nullSafeUnmarshall(Json.Number object) {
        return BigInteger.valueOf(object.getNumber().longValueExact());
    }

    public Class<BigInteger> getReturnedClass() {
        return BigInteger.class;
    }
}
