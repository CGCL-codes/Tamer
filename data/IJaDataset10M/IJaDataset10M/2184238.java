package net.community.apps.tools.jarscanner;

import java.io.File;
import javax.swing.SwingUtilities;
import net.community.apps.common.BaseMain;
import net.community.chest.CoVariantReturn;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 *
 * <P>Find all JAR(s) that match a specific pattern and contain files
 * matching some pattern</P>
 * 
 * @author Lyor G.
 * @since Oct 21, 2007 7:49:12 AM
 */
public class Main extends BaseMain {

    private Main(final String... args) {
        super(args);
    }

    @Override
    @CoVariantReturn
    protected MainFrame createMainFrameInstance() throws Exception {
        final MainFrame f = new MainFrame();
        final String[] args = getMainArguments();
        final int numArgs = (null == args) ? 0 : args.length;
        for (int aIndex = 0; aIndex < numArgs; aIndex++) {
            final String a = args[aIndex];
            if ("-i".equalsIgnoreCase(a)) {
                aIndex++;
                if (aIndex >= numArgs) throw new IllegalArgumentException("Missing " + a + " option argument");
                final String v = args[aIndex];
                if ((null == v) || (v.length() <= 0)) throw new IllegalArgumentException("Null/empty " + a + " option argument");
                f.setJarsFolder(new File(v));
            } else if ("-ip".equalsIgnoreCase(a)) {
                aIndex++;
                if (aIndex >= numArgs) throw new IllegalArgumentException("Missing " + a + " option argument");
                final String v = args[aIndex];
                if ((null == v) || (v.length() <= 0)) throw new IllegalArgumentException("Null/empty " + a + " option argument");
                f.setIncludedScanPattern(v);
            } else if ("-xp".equalsIgnoreCase(a)) {
                aIndex++;
                if (aIndex >= numArgs) throw new IllegalArgumentException("Missing " + a + " option argument");
                final String v = args[aIndex];
                if ((null == v) || (v.length() <= 0)) throw new IllegalArgumentException("Null/empty " + a + " option argument");
                f.setExcludedScanPattern(v);
            } else if ("-id".equalsIgnoreCase(a)) {
                aIndex++;
                if (aIndex >= numArgs) throw new IllegalArgumentException("Missing " + a + " option argument");
                final String v = args[aIndex];
                if ((null == v) || (v.length() <= 0)) throw new IllegalArgumentException("Null/empty " + a + " option argument");
                f.setDirIncludePattern(v);
            } else if ("-xd".equalsIgnoreCase(a)) {
                aIndex++;
                if (aIndex >= numArgs) throw new IllegalArgumentException("Missing " + a + " option argument");
                final String v = args[aIndex];
                if ((null == v) || (v.length() <= 0)) throw new IllegalArgumentException("Null/empty " + a + " option argument");
                f.setDirExcludePattern(v);
            } else if ("-ij".equalsIgnoreCase(a)) {
                aIndex++;
                if (aIndex >= numArgs) throw new IllegalArgumentException("Missing " + a + " option argument");
                final String v = args[aIndex];
                if ((null == v) || (v.length() <= 0)) throw new IllegalArgumentException("Null/empty " + a + " option argument");
                f.setJarsIncludePattern(v);
            } else if ("-xj".equalsIgnoreCase(a)) {
                aIndex++;
                if (aIndex >= numArgs) throw new IllegalArgumentException("Missing " + a + " option argument");
                final String v = args[aIndex];
                if ((null == v) || (v.length() <= 0)) throw new IllegalArgumentException("Null/empty " + a + " option argument");
                f.setJarsExcludePattern(v);
            } else throw new IllegalArgumentException("Unknown option: " + a);
        }
        return f;
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Main(args));
    }
}
