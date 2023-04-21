package arit.wrappers;

import arit.DecimalArithmetics;
import arit.numbers.ArithmeticNumber;

public class DecimalArithmeticsImpl<T extends ArithmeticNumber<T>> extends ArithmeticsImpl<T> implements DecimalArithmetics<T> {

    public DecimalArithmeticsImpl(T zero, T one) {
        super(zero, one);
    }
}
