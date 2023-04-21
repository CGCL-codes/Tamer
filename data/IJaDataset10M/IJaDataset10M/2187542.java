package net.sourceforge.freejava.arch.context.sysclg;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Locale;
import junit.framework.TestCase;
import net.sourceforge.freejava.arch.context.StaticContext;
import org.junit.Test;

public class SystemCLGTest extends TestCase {

    @Test
    public void testDefault() throws Exception {
        Charset staticCharset = SystemCLG.charset.get(StaticContext.getInstance());
        assertEquals(Charset.defaultCharset(), staticCharset);
        Locale staticLocale = SystemCLG.locale.get(StaticContext.getInstance());
        assertEquals(Locale.getDefault(), staticLocale);
        File cwd = SystemCLG.cwd.get(StaticContext.getInstance());
        assert cwd != null : "null cwd";
        String cwdPath = cwd.getPath();
        String userDir = System.getProperty("user.dir");
        assertEquals(userDir, cwdPath);
    }
}
