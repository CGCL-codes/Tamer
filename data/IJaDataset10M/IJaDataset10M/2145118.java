package logahawk.listeners;

import java.util.*;
import org.testng.*;
import org.testng.annotations.*;
import logahawk.*;

/**
 *
 */
public class AppendableListenerTest {

    @Test
    public void basicTest() {
        final String MESSAGE = "message";
        StringBuffer b = new StringBuffer();
        AppendableListener l = new AppendableListener(b);
        l.log(new DefaultLogMeta(Severity.INFO), MESSAGE);
        Assert.assertTrue(b.length() > 0);
        Assert.assertTrue(b.toString().contains(MESSAGE));
    }
}
