package org.jdiagnose.library;

import java.math.BigDecimal;
import org.jdiagnose.DiagnosticUnit;
import org.jdiagnose.DiagnosticException;

/**
 * User: jamie
 * Date: May 31, 2004
 * Time: 8:12:36 PM
 */
public class JavaVersionDiagnostic extends DiagnosticUnit {

    private static final BigDecimal javaClassVersion = new BigDecimal(System.getProperty("java.class.version", "44.0"));

    private static final BigDecimal fourtyFivePointThree = new BigDecimal("45.3");

    private static final BigDecimal fourtySix = new BigDecimal(46);

    private static final BigDecimal fourtySeven = new BigDecimal(47);

    private static final BigDecimal fourtyEight = new BigDecimal(48);

    private static final BigDecimal fourtyNine = new BigDecimal(49);

    private String javaVersion = null;

    public JavaVersionDiagnostic() {
    }

    /**
     * Constructor for JavaVersionDiagnostic.
     * @param javaVersion
     */
    public JavaVersionDiagnostic(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public void diagnoseJavaVersion() throws DiagnosticException {
        assertNotNull("javaVersion property should not be null", javaVersion);
        if (javaVersion.endsWith("+")) {
            double javaVersionDouble = Double.parseDouble(javaVersion.substring(0, javaVersion.length() - 1));
            if (javaVersionDouble == 1.1) {
                assertTrue(javaClassVersion.compareTo(fourtyFivePointThree) >= 0);
            } else if (javaVersionDouble == 1.2) {
                assertTrue(javaClassVersion.compareTo(fourtySix) >= 0);
            } else if (javaVersionDouble == 1.3) {
                assertTrue(javaClassVersion.compareTo(fourtySeven) >= 0);
            } else if (javaVersionDouble == 1.4) {
                assertTrue(javaClassVersion.compareTo(fourtyEight) >= 0);
            } else if (javaVersionDouble == 1.5) {
                assertTrue(javaClassVersion.compareTo(fourtyNine) >= 0);
            } else {
                throw new DiagnosticException("javaVersion to test can only be one of 1.1, 1.2, 1.3, 1.4 or 1.5");
            }
        } else {
            double javaVersionDouble = Double.parseDouble(javaVersion);
            if (javaVersionDouble == 1.1) {
                assertTrue(isJDK11only());
            } else if (javaVersionDouble == 1.2) {
                assertTrue(isJDK12only());
            } else if (javaVersionDouble == 1.3) {
                assertTrue(isJDK13only());
            } else if (javaVersionDouble == 1.4) {
                assertTrue(isJDK14only());
            } else if (javaVersionDouble == 1.5) {
                assertTrue(isJDK15andAbove());
            } else {
                throw new DiagnosticException("javaVersion to test can only be one of 1.1, 1.2, 1.3, 1.4 or 1.5");
            }
        }
    }

    public boolean isJDK11only() {
        return (fourtySix.compareTo(javaClassVersion) > 0) && (fourtyFivePointThree.compareTo(javaClassVersion) <= 0);
    }

    public boolean isJDK12only() {
        return (fourtySeven.compareTo(javaClassVersion) > 0) && (fourtySix.compareTo(javaClassVersion) <= 0);
    }

    public boolean isJDK13only() {
        return (fourtyEight.compareTo(javaClassVersion) > 0) && (fourtySeven.compareTo(javaClassVersion) <= 0);
    }

    public boolean isJDK14only() {
        return (fourtyNine.compareTo(javaClassVersion) > 0) && (fourtyEight.compareTo(javaClassVersion) <= 0);
    }

    public boolean isJDK15andAbove() {
        return (fourtyNine.compareTo(javaClassVersion) <= 0);
    }

    /**
     * @return
     */
    public String getJavaVersion() {
        return javaVersion;
    }

    /**
     * @param string
     */
    public void setJavaVersion(String string) {
        javaVersion = string;
    }
}
