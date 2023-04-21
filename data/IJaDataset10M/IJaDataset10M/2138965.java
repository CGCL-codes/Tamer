package com.vsetec.mety.core;

import com.vsetec.mety.Collation;
import com.vsetec.mety.Met;
import com.vsetec.mety.MetException;
import java.text.CollationKey;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Date;

/**
 *
 * @author fedd
 */
public class CollStdJava extends MetImpl<WrapperCOLLATION> implements Collation {

    private RuleBasedCollator _rbc = null;

    private RuleBasedCollator _rbcPrimary = null;

    private String _firstChar = null;

    private String _lastChar = null;

    protected CollStdJava(WrapperCOLLATION key) {
        super(key);
    }

    private void _loadRbc() {
        if (_rbc == null) {
            try {
                synchronized (this) {
                    String rules = key.getKept();
                    int firstComma = rules.indexOf(',');
                    int decomp = Integer.parseInt(rules.substring(0, firstComma));
                    firstComma++;
                    int secondComma = rules.indexOf(',', firstComma);
                    int strength = Integer.parseInt(rules.substring(firstComma, secondComma));
                    secondComma++;
                    rules = rules.substring(secondComma);
                    _rbc = new RuleBasedCollator(rules);
                    _rbc.setDecomposition(decomp);
                    _rbc.setStrength(strength);
                    _rbcPrimary = new RuleBasedCollator(rules);
                    _rbcPrimary.setDecomposition(decomp);
                    _rbcPrimary.setStrength(Collator.PRIMARY);
                    int cp;
                    int i = 0;
                    while (!Character.isLetter(cp = rules.codePointAt(i))) {
                        i += Character.charCount(cp);
                    }
                    _firstChar = new String(new int[] { cp }, 0, 1);
                    i = rules.length() - 1;
                    while (!Character.isLetter(cp = rules.codePointBefore(i))) {
                        i -= Character.charCount(cp);
                    }
                    _lastChar = new String(new int[] { cp }, 0, 1);
                }
            } catch (Exception ex) {
                throw new MetException(ex);
            }
        }
    }

    private Object _getValue(Met met) {
        Object val;
        if (met instanceof Txt) {
            val = ((Txt) met).key.getStarting();
        } else if (met instanceof Bin) {
            val = ((Bin) met).key.getFirstBytes();
        } else {
            val = met.getValue();
        }
        return val;
    }

    public Object getCollatingKey(Met met) {
        Object val = _getValue(met);
        if (val instanceof String) {
            String str = (String) val;
            _loadRbc();
            synchronized (_rbc) {
                val = _rbc.getCollationKey(str).toByteArray();
            }
        } else if (val instanceof Number) {
            val = ((Number) val).doubleValue();
        } else if (val instanceof Date) {
            val = (double) ((Date) val).getTime();
        } else if (!(val instanceof byte[])) {
            val = null;
        }
        return val;
    }

    public Object[] getCollatingKeyBounds(Met met, Spec.FilterImpl forFilter) {
        Object val = _getValue(met);
        if (val instanceof String) {
            String str = (String) val;
            _loadRbc();
            CollationKey colkey;
            synchronized (_rbcPrimary) {
                colkey = _rbcPrimary.getCollationKey(str);
            }
            byte[] hi, lo;
            lo = colkey.toByteArray();
            switch(forFilter.key.specialConstant) {
                case STARTING:
                    hi = new byte[lo.length];
                    System.arraycopy(lo, 0, hi, 0, lo.length);
                    int offs = lo.length;
                    hi[--offs] = (byte) 0xFF;
                    hi[--offs] = (byte) 0xFF;
                    hi[--offs] = (byte) 0xFF;
                    hi[--offs] = (byte) 0xFF;
                    return new Object[] { lo, hi };
                case EQ:
                    hi = new byte[lo.length];
                    System.arraycopy(lo, 0, hi, 0, lo.length);
                    hi[lo.length - 1] = (byte) 0xFF;
                    return new Object[] { lo, hi };
                case LE:
                    hi = new byte[lo.length];
                    System.arraycopy(lo, 0, hi, 0, lo.length);
                    hi[lo.length - 1] = (byte) 0xFF;
                    return new Object[] { hi };
                case GT:
                    hi = new byte[lo.length];
                    System.arraycopy(lo, 0, hi, 0, lo.length);
                    hi[lo.length - 1] = (byte) 0xFF;
                    return new Object[] { hi };
                default:
                    return new Object[] { lo };
            }
        } else {
            if (val instanceof Number) {
                return new Object[] { val };
            } else if (val instanceof Date) {
                val = (double) ((Date) val).getTime();
                return new Object[] { val };
            } else if (!(val instanceof byte[])) {
                return new Object[] { null };
            } else {
                return new Object[] { val };
            }
        }
    }
}
