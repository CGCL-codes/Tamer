package com.choicemaker.correlation.basic;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import com.choicemaker.api.correlation.ExportException;
import com.choicemaker.api.general.Pair;
import com.choicemaker.core.general.DefaultPair;
import com.choicemaker.correlation.ReflectionUtils;
import com.choicemaker.correlation.basic.TrimmedStringsTestBean.F;

public class TrimmedStringsTest extends TestCase {

    public static final Set<EXPORT_TYPE_JAVA> NULL_EXPORT_FORMATS = null;

    public static final Set<EXPORT_TYPE_JAVA> SUPPORTED_FORMATS = Collections.unmodifiableSet(EnumSet.of(EXPORT_TYPE_JAVA.CLUEMAKER_25X));

    private static final Set<EXPORT_TYPE_JAVA> _tmp_set = EnumSet.allOf(EXPORT_TYPE_JAVA.class);

    static {
        _tmp_set.remove(EXPORT_TYPE_JAVA.CLUEMAKER_25X);
    }

    public static final Set<EXPORT_TYPE_JAVA> UNSUPPORTED_FORMATS = Collections.unmodifiableSet(_tmp_set);

    /** Checks if this test is valid */
    public void testValidation() {
        Set<String> validNames = Utils.getTrimmedStringsTestEnumMethodNames();
        Utils.checkAccessorNames(TrimmedStringsTestBean.class, validNames);
    }

    public void testTrimmedStringsConstructor() {
        for (F f : F.values()) {
            try {
                Method m = ReflectionUtils.getFieldAccessor(TrimmedStringsTestBean.class, f.fieldName);
                if (String.class.isAssignableFrom(m.getReturnType())) {
                    new TrimmedStringsLowerCase<TrimmedStringsTestBean>(f.fieldName, m);
                }
            } catch (Exception x) {
                fail(f.fieldName + ": " + x.toString());
            }
        }
        for (F f : F.values()) {
            try {
                Method m = ReflectionUtils.getFieldAccessor(TrimmedStringsTestBean.class, f.fieldName);
                if (!String.class.isAssignableFrom(m.getReturnType())) {
                    new TrimmedStringsLowerCase<TrimmedStringsTestBean>(f.fieldName, m);
                    fail(f.fieldName + ": accepted invalid field type (" + m.getReturnType().getClass().getName() + ")");
                }
            } catch (Exception x) {
                assertTrue(f.fieldName + ": " + x.toString(), true);
            }
        }
    }

    public void testIsValidForEvaluation() {
        @SuppressWarnings("serial") Set<Boolean> tf = new HashSet<Boolean>() {

            {
                add(true);
                add(false);
            }
        };
        for (boolean b : tf) {
            TrimmedStringsTestBean bean = new TrimmedStringsTestBean(b);
            for (F f : F.values()) {
                try {
                    Method m = ReflectionUtils.getFieldAccessor(TrimmedStringsTestBean.class, f.fieldName, null);
                    if (String.class.isAssignableFrom(m.getReturnType())) {
                        TrimmedStringsLowerCase<TrimmedStringsTestBean> p = new TrimmedStringsLowerCase<TrimmedStringsTestBean>(f.fieldName, m);
                        String fieldValue = (String) m.invoke(bean, (Object[]) null);
                        if (fieldValue != null) {
                            assertTrue(f.fieldName, p.isRecordValidForEvaluation(bean));
                            assertTrue(f.fieldName, p.isFieldValidForEvaluation(fieldValue));
                        } else {
                            assertFalse(f.fieldName, p.isRecordValidForEvaluation(bean));
                            assertFalse(f.fieldName, p.isFieldValidForEvaluation(fieldValue));
                        }
                    }
                } catch (Exception x) {
                    fail(f.fieldName + ": " + x.toString());
                }
            }
        }
    }

    public void testEvaluate() {
        TrimmedStringsTestBean bt = new TrimmedStringsTestBean(true);
        TrimmedStringsTestBean bf = new TrimmedStringsTestBean(false);
        Pair<TrimmedStringsTestBean> pair = new DefaultPair<TrimmedStringsTestBean>(bt, bf);
        for (F f : F.values()) {
            try {
                Method m = ReflectionUtils.getFieldAccessor(TrimmedStringsTestBean.class, f.fieldName, null);
                if (String.class.isAssignableFrom(m.getReturnType())) {
                    TrimmedStringsLowerCase<TrimmedStringsTestBean> p1 = new TrimmedStringsLowerCase<TrimmedStringsTestBean>(f.fieldName, m);
                    boolean b = p1.evaluate(pair);
                    assertTrue(f.fieldName, b == f.expected);
                }
            } catch (Exception x) {
                fail(f.fieldName + ": " + x.toString());
            }
        }
    }

    public void testExport() {
        for (F f : F.values()) {
            try {
                Method m = ReflectionUtils.getFieldAccessor(TrimmedStringsTestBean.class, f.fieldName, null);
                if (String.class.isAssignableFrom(m.getReturnType())) {
                    TrimmedStringsLowerCase<TrimmedStringsTestBean> p = new TrimmedStringsLowerCase<TrimmedStringsTestBean>(f.fieldName, m);
                    Set<EXPORT_TYPE_JAVA> formats = p.getSupportedExportTypes();
                    for (EXPORT_TYPE_JAVA format : formats) {
                        String s = p.exportValidityLogicAsString(format);
                        assertTrue(s != null && !s.isEmpty());
                        s = p.exportEvaluationLogicAsString(format);
                        assertTrue(s != null && !s.isEmpty());
                    }
                    Set<EXPORT_TYPE_JAVA> badFormats = EnumSet.allOf(EXPORT_TYPE_JAVA.class);
                    badFormats.removeAll(formats);
                    for (EXPORT_TYPE_JAVA format : badFormats) {
                        try {
                            p.exportValidityLogicAsString(format);
                            fail(f.name() + ": " + format.name() + ": bad format");
                        } catch (ExportException x) {
                            assertTrue(true);
                        }
                        try {
                            p.exportEvaluationLogicAsString(format);
                            fail(f.name() + ": " + format.name() + ": bad format");
                        } catch (ExportException x) {
                            assertTrue(true);
                        }
                    }
                }
            } catch (Exception x) {
                fail(f.fieldName + ": " + x.toString());
            }
        }
    }
}
