package org.geoforge.guitlc.frame.main.tabbedpane;

import java.util.ArrayList;
import javax.help.HelpBroker;
import org.geoforge.guihlp.IHelpOnThisSection;
import org.geoforge.guillc.tabbedpane.TabAbs;
import org.geoforge.guitlc.frame.main.panel.PnlSpaceWelcomeAbs;
import org.geoforge.io.finder.GfrFactoryIconShared;
import org.geoforge.lang.IShrObj;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class TabSpcAbs extends TabAbs implements IHelpOnThisSection {

    protected PnlSpaceWelcomeAbs _pnlWelcome = null;

    public String getKind() {
        return this._strKind;
    }

    protected ArrayList<IHelpOnThisSection> _altPanels_ = null;

    @Override
    public void setEnabledHelpOnThisSection(HelpBroker hbr) {
        for (int i = 0; i < this._altPanels_.size(); i++) {
            this._altPanels_.get(i).setEnabledHelpOnThisSection(hbr);
        }
    }

    protected TabSpcAbs(String strKind) {
        super();
        this._altPanels_ = new ArrayList<IHelpOnThisSection>();
        this._strKind = strKind;
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (this._pnlWelcome != null) {
            super.addTab(PnlSpaceWelcomeAbs.STR_TITLE, GfrFactoryIconShared.s_getHome(GfrFactoryIconShared.INT_SIZE_SMALL), this._pnlWelcome);
        }
        for (int i = 0; i < this._altPanels_.size(); i++) {
            IShrObj objCur = (IShrObj) this._altPanels_.get(i);
            if (!objCur.init()) return false;
        }
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        for (int i = 0; i < this._altPanels_.size(); i++) {
            IShrObj objCur = (IShrObj) this._altPanels_.get(i);
            if (objCur != null) {
                objCur.destroy();
            }
        }
        this._altPanels_.clear();
        this._altPanels_ = null;
        this._strKind = null;
    }

    private String _strKind = null;
}
