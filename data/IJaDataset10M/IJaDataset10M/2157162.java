package android.text.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;
import android.text.LoginFilter.PasswordFilterGMail;
import junit.framework.TestCase;

@TestTargetClass(PasswordFilterGMail.class)
public class LoginFilter_PasswordFilterGMailTest extends TestCase {

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, method = "LoginFilter.PasswordFilterGMail", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, method = "LoginFilter.PasswordFilterGMail", args = { boolean.class }) })
    @ToBeFixed(bug = "1695243", explanation = "miss javadoc")
    public void testConstructor() {
        new PasswordFilterGMail();
        new PasswordFilterGMail(true);
        new PasswordFilterGMail(false);
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "isAllowed", args = { char.class })
    public void testIsAllowed() {
        PasswordFilterGMail passwordFilterGMail = new PasswordFilterGMail();
        assertTrue(passwordFilterGMail.isAllowed('a'));
        assertTrue(passwordFilterGMail.isAllowed((char) 200));
        assertFalse(passwordFilterGMail.isAllowed('\n'));
        assertFalse(passwordFilterGMail.isAllowed((char) 150));
        assertFalse(passwordFilterGMail.isAllowed((char) 0));
        assertFalse(passwordFilterGMail.isAllowed((char) 31));
        assertTrue(passwordFilterGMail.isAllowed((char) 32));
        assertTrue(passwordFilterGMail.isAllowed((char) 127));
        assertFalse(passwordFilterGMail.isAllowed((char) 128));
        assertFalse(passwordFilterGMail.isAllowed((char) 159));
        assertTrue(passwordFilterGMail.isAllowed((char) 160));
        assertTrue(passwordFilterGMail.isAllowed((char) 255));
    }
}
