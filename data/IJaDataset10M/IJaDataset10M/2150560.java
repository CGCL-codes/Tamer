package logahawk.formatters;

import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.testng.*;
import org.testng.annotations.*;
import logahawk.*;
import logahawk.listeners.*;

public class ThreadMessageFormatterTest {

    @Test
    public void test() {
        Random r = new Random();
        MessageFormatter f = new ThreadMessageFormatter(new SimpleDateFormat("yyyy"), false);
        for (Severity severity : Severity.values()) {
            LogMeta meta = new ThreadLogMeta(severity, new Date(), Thread.currentThread());
            String message = "here is the message! " + r.nextInt();
            Pattern p = Pattern.compile("\\d{4}\\s\\[" + Thread.currentThread().getId() + "-" + Thread.currentThread().getName() + "\\]\\s" + meta.getSeverity().getFixedLengthString() + ":\\s" + message);
            String formatted = f.format(meta, message);
            Assert.assertTrue(p.matcher(formatted).matches());
        }
    }
}
