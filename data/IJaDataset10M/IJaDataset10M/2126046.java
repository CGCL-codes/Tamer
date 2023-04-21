package gnu.testlet.java2.text.DecimalFormat;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;
import java.text.DecimalFormat;
import java.util.Locale;

public class formatExp implements Testlet {

    public void apply(TestHarness harness, DecimalFormat df, String pattern) {
        harness.checkPoint("pattern " + pattern);
        boolean ok = true;
        try {
            df.applyPattern(pattern);
        } catch (IllegalArgumentException x) {
            ok = false;
        }
        harness.check(ok);
    }

    public void test(TestHarness harness) {
        Locale loc = Locale.US;
        Locale.setDefault(loc);
        DecimalFormat df = new DecimalFormat();
        apply(harness, df, "0.0000E0");
        harness.check(df.format(200000), "2.0000E5");
        apply(harness, df, "00.00E00");
        harness.check(df.format(200000), "20.00E04");
        apply(harness, df, "##0.####E0");
        harness.check(df.format(12345), "12.345E3");
        apply(harness, df, "##.###E0");
        harness.check(df.format(12345), "1.2345E4");
        apply(harness, df, "##.###E0");
        harness.check(df.format(12346), "1.2346E4");
        apply(harness, df, "00.###E0");
        harness.check(df.format(12345), "12.345E3");
        harness.check(df.format(1234), "12.34E2");
        harness.check(df.format(0.00123), "12.3E-4");
        apply(harness, df, "0E0");
        harness.check(df.format(-1234.567), "-1E3");
        apply(harness, df, "00E00");
        harness.check(df.format(-1234.567), "-12E02");
        apply(harness, df, "000E00");
        harness.check(df.format(-1234.567), "-123E01");
        apply(harness, df, "0000000000E0");
        harness.check(df.format(-1234.567), "-1234567000E-6");
        apply(harness, df, "0.0E0");
        harness.check(df.format(-1234.567), "-1.2E3");
        apply(harness, df, "00.00E0");
        harness.check(df.format(-1234.567), "-12.35E2");
        harness.check(df.format(-.1234567), "-12.35E-2");
    }
}
