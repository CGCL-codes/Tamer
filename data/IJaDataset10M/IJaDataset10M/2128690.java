package gov.nasa.jpf.jvm;

import java.text.DecimalFormatSymbols;

/**
 * MJI NativePeer class for java.text.DecimalFormatSymbols library abstraction
 * 
 * we need to intercept the initialization because it is requires
 * file io (properties) based on the Locale
 */
public class JPF_java_text_DecimalFormatSymbols {

    public static void initialize__Ljava_util_Locale_2__V(MJIEnv env, int objRef, int localeRef) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        env.setCharField(objRef, "patternSeparator", dfs.getPatternSeparator());
        env.setCharField(objRef, "percent", dfs.getPercent());
        env.setCharField(objRef, "digit", dfs.getDigit());
        env.setCharField(objRef, "minusSign", dfs.getMinusSign());
        env.setCharField(objRef, "perMill", dfs.getPerMill());
        env.setReferenceField(objRef, "infinity", env.newString(dfs.getInfinity()));
        env.setReferenceField(objRef, "NaN", env.newString(dfs.getNaN()));
        env.setReferenceField(objRef, "currencySymbol", env.newString(dfs.getCurrencySymbol()));
        env.setCharField(objRef, "monetarySeparator", dfs.getMonetaryDecimalSeparator());
        env.setCharField(objRef, "decimalSeparator", dfs.getDecimalSeparator());
        env.setCharField(objRef, "groupingSeparator", dfs.getGroupingSeparator());
        env.setCharField(objRef, "exponential", 'E');
    }
}
