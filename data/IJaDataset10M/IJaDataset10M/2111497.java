package nl.hhs.i2.team5.happer.gui;

import java.awt.Dimension;
import java.net.URL;

/**
 *
 * @author Team 5
 */
public class MensMenu extends AbstractCelInhoudImpl {

    public MensMenu() {
        super();
        this.setPreferredSize(new Dimension(20, 20));
    }

    @Override
    protected URL getIconUrl() {
        return getClass().getClassLoader().getResource("nl/hhs/i2/team5/happer/gui/iconen/mens_menu.gif");
    }
}
