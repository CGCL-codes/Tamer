package net.community.apps.tools.adm.charts;

import javax.swing.SwingUtilities;
import net.community.apps.common.BaseMain;
import net.community.chest.CoVariantReturn;

/**
 * <P>Copyright 2010 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jun 23, 2010 2:08:24 PM
 */
public final class Main extends BaseMain {

    private Main(String... args) {
        super(args);
    }

    @Override
    @CoVariantReturn
    protected MainFrame createMainFrameInstance() throws Exception {
        return new MainFrame(getMainArguments());
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Main(args));
    }
}
