package se.notima.test.bg;

import java.io.File;
import junit.framework.Assert;
import org.junit.Test;
import se.notima.bg.BgEntry;
import se.notima.bg.BgFile;
import se.notima.bg.autogiro.AgFile;
import se.notima.bg.bgmax.BgMaxFile;

public class BgFileTest {

    @Test
    public void testBgFileFactory() throws Exception {
        File file = new File("test/src/se/notima/test/bg/autogiro/Layout D.txt");
        BgFile<? extends BgEntry> bgFile = BgFile.createBgFile(file);
        Assert.assertEquals(AgFile.class, bgFile.getClass());
        new File("test/src/se/notima/test/bg/autogiro/Layout H.txt");
        bgFile = BgFile.createBgFile(file);
        Assert.assertEquals(AgFile.class, bgFile.getClass());
        new File("test/src/se/notima/test/bg/autogiro/Layout F.txt");
        bgFile = BgFile.createBgFile(file);
        Assert.assertEquals(AgFile.class, bgFile.getClass());
        file = new File("test/src/se/notima/test/bg/bgmax/BgMax1.txt");
        bgFile = BgFile.createBgFile(file);
        Assert.assertEquals(BgMaxFile.class, bgFile.getClass());
    }
}
