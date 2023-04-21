package net.cucup.sample.tomahawk.misc;

import java.io.Serializable;

/**
 * @author Manfred Geiler (latest modification by $Author: grantsmith $)
 * @version $Revision: 472610 $ $Date: 2006-11-08 14:46:34 -0500 (mi鑼� 08 nov 2006) $
 */
public class TabbedPaneBean implements Serializable {

    /**
     * serial id for serialisation versioning
     */
    private static final long serialVersionUID = 1L;

    private boolean _tab1Visible = true;

    private boolean _tab2Visible = true;

    private boolean _tab3Visible = true;

    public boolean isTab1Visible() {
        return _tab1Visible;
    }

    public void setTab1Visible(boolean tab1Visible) {
        _tab1Visible = tab1Visible;
    }

    public boolean isTab2Visible() {
        return _tab2Visible;
    }

    public void setTab2Visible(boolean tab2Visible) {
        _tab2Visible = tab2Visible;
    }

    public boolean isTab3Visible() {
        return _tab3Visible;
    }

    public void setTab3Visible(boolean tab3Visible) {
        _tab3Visible = tab3Visible;
    }
}
