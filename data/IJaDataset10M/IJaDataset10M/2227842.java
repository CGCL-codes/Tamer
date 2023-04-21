package org.epoline.service.appnum.dl;

public abstract class CheckDigit implements CheckDigitInterface {

    public abstract String calcCheckDigit(String in);

    public static String calc(CheckDigitInterface impl, String in) {
        return impl.calcCheckDigit(in);
    }
}
