package jogamp.common;

import java.security.*;

/** Helper routines for logging and debugging. */
public class Debug {

    private static boolean verbose;

    private static boolean debugAll;

    private static AccessControlContext localACC;

    static {
        localACC = AccessController.getContext();
        verbose = isPropertyDefined("jogamp.verbose", true);
        debugAll = isPropertyDefined("jogamp.debug", true);
    }

    static int getIntProperty(final String property, final boolean jnlpAlias) {
        return getIntProperty(property, jnlpAlias, localACC, 0);
    }

    public static int getIntProperty(final String property, final boolean jnlpAlias, final AccessControlContext acc, int defaultValue) {
        int i = defaultValue;
        try {
            String sv = Debug.getProperty(property, jnlpAlias, acc);
            if (null != sv) {
                Integer iv = Integer.valueOf(sv);
                i = iv.intValue();
            }
        } catch (NumberFormatException nfe) {
        }
        return i;
    }

    public static long getLongProperty(final String property, final boolean jnlpAlias, final AccessControlContext acc, long defaultValue) {
        long l = defaultValue;
        try {
            String sv = Debug.getProperty(property, jnlpAlias, acc);
            if (null != sv) {
                Long lv = Long.valueOf(sv);
                l = lv.longValue();
            }
        } catch (NumberFormatException nfe) {
        }
        return l;
    }

    static boolean getBooleanProperty(final String property, final boolean jnlpAlias) {
        return getBooleanProperty(property, jnlpAlias, localACC);
    }

    public static boolean getBooleanProperty(final String property, final boolean jnlpAlias, final AccessControlContext acc) {
        Boolean b = Boolean.valueOf(Debug.getProperty(property, jnlpAlias, acc));
        return b.booleanValue();
    }

    static boolean isPropertyDefined(final String property, final boolean jnlpAlias) {
        return isPropertyDefined(property, jnlpAlias, localACC);
    }

    public static boolean isPropertyDefined(final String property, final boolean jnlpAlias, final AccessControlContext acc) {
        return (Debug.getProperty(property, jnlpAlias, acc) != null) ? true : false;
    }

    static String getProperty(final String property, final boolean jnlpAlias) {
        return getProperty(property, jnlpAlias, localACC);
    }

    public static String getProperty(final String property, final boolean jnlpAlias, final AccessControlContext acc) {
        String s = null;
        if (null != acc && acc.equals(localACC)) {
            s = (String) AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {
                    String val = null;
                    try {
                        val = System.getProperty(property);
                    } catch (Exception e) {
                    }
                    if (null == val && jnlpAlias && !property.startsWith(jnlp_prefix)) {
                        try {
                            val = System.getProperty(jnlp_prefix + property);
                        } catch (Exception e) {
                        }
                    }
                    return val;
                }
            });
        } else {
            try {
                s = System.getProperty(property);
            } catch (Exception e) {
            }
            if (null == s && jnlpAlias && !property.startsWith(jnlp_prefix)) {
                try {
                    s = System.getProperty(jnlp_prefix + property);
                } catch (Exception e) {
                }
            }
        }
        return s;
    }

    public static final String jnlp_prefix = "jnlp.";

    public static boolean verbose() {
        return verbose;
    }

    public static boolean debugAll() {
        return debugAll;
    }

    public static boolean debug(String subcomponent) {
        return debugAll() || isPropertyDefined("jogamp.debug." + subcomponent, true);
    }
}
