package net.sf.twip;

import net.sf.twip.internal.TwipConfigurationErrorNonStatic;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TwiP.class)
@SuppressWarnings("all")
public class AutoTwipNonStaticFieldTest {

    public static class MyType {
    }

    @AutoTwip
    public MyType[] autoTwip = { new MyType(), new MyType(), new MyType() };

    @Test(expected = TwipConfigurationErrorNonStatic.class)
    public void testAutoTwip(MyType t) {
        assert t != t;
    }
}
