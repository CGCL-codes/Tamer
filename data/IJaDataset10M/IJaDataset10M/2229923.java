package net.sf.jzeno.echo.screen;

import net.sf.jzeno.echo.components.AbstractScreen;
import nextapp.echo.DefaultListModel;
import org.apache.log4j.Logger;

/**
 * @author dimitry.dhondt@realsoftware.be
 */
public class ScrapScreen extends AbstractScreen {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(ScrapScreen.class);

    public ScrapScreen() {
        Object[] data = new Object[10];
        data[0] = "atest";
        data[1] = "btest";
        data[2] = "ctest";
        data[3] = "dtest";
        data[4] = "etest";
        data[5] = "ftest";
        data[6] = "gtest";
        data[7] = "htest";
        data[8] = "itest";
    }
}
