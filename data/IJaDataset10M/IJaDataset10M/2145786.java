package org.maverickdbms.basic.text;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.maverickdbms.basic.ConstantString;
import org.maverickdbms.basic.MaverickException;
import org.maverickdbms.basic.Factory;
import org.maverickdbms.basic.MaverickString;

/**
* This is the basic string interface for the package.
*/
public class OconvFormatter extends Formatter {

    private Formatter formatter;

    public OconvFormatter(Factory factory, ConstantString pattern, boolean convNull) throws MaverickException {
        super(factory, TYPE_OCONV, getPatterns(pattern), convNull);
        Class f = (pattern.length() == 0) ? NullFormatter.class : getFormatters()[pattern.charAt(0)];
        try {
            Class[] parameters = { Factory.class, Integer.TYPE, Object[].class, Boolean.TYPE };
            Constructor c = f.getConstructor(parameters);
            Object[] p2 = { factory, new Integer(TYPE_OCONV), patterns, Boolean.valueOf(convNull) };
            formatter = (Formatter) c.newInstance(p2);
        } catch (IllegalAccessException iae) {
            throw new MaverickException(0, iae);
        } catch (InstantiationException ie) {
            throw new MaverickException(0, ie);
        } catch (InvocationTargetException ivte) {
            throw new MaverickException(0, ivte);
        } catch (NoSuchMethodException nsme) {
            throw new MaverickException(0, nsme);
        }
    }

    private static int[][] types;

    private static final int[][] ptypes = { { 'D', PatternParser.INTEGER, PatternParser.STRING }, { 'G', PatternParser.INTEGER, PatternParser.CHARACTER, PatternParser.INTEGER }, { 'L', PatternParser.INTEGER, ',', PatternParser.INTEGER }, { 'M', PatternParser.CHARACTER, PatternParser.STRING }, { 'P', PatternParser.STRING }, { 'R', PatternParser.STRING }, { 'S' }, { 'T', PatternParser.INTEGER, ',', PatternParser.INTEGER }, { 'U', PatternParser.STRING } };

    private static Class[] formatters;

    private static final Object[][] pformatters = { { new Character('D'), DOconvFormatter.class }, { new Character('G'), GFormatter.class }, { new Character('L'), LFormatter.class }, { new Character('M'), MOconvFormatter.class }, { new Character('P'), PFormatter.class }, { new Character('R'), RFormatter.class }, { new Character('S'), SFormatter.class }, { new Character('T'), TFormatter.class }, { new Character('U'), UFormatter.class } };

    private static synchronized int[][] getTypes() {
        if (types == null) {
            types = new int['Z'][];
            for (int i = 0; i < ptypes.length; i++) {
                types[ptypes[i][0]] = ptypes[i];
            }
        }
        return types;
    }

    private static synchronized Class[] getFormatters() {
        if (formatters == null) {
            formatters = new Class['Z'];
            for (int i = 0; i < formatters.length; i++) {
                formatters[i] = ErrorFormatter.class;
            }
            for (int i = 0; i < pformatters.length; i++) {
                formatters[(int) ((Character) pformatters[i][0]).charValue()] = (Class) pformatters[i][1];
            }
        }
        return formatters;
    }

    private static Object[] getPatterns(ConstantString pattern) {
        if (pattern.length() == 0) {
            return new Object[0];
        }
        PatternParser pp = new PatternParser();
        int[] types = getTypes()[pattern.charAt(0)];
        return pp.parse(pattern, types);
    }

    public MaverickString format(MaverickString result, MaverickString status, ConstantString input) throws MaverickException {
        return formatter.format(result, status, input);
    }
}
