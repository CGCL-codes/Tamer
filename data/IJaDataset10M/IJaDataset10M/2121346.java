package calclipse.caldron.gui.search.actions;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import calclipse.Resource;
import calclipse.caldron.gui.locale.LocalizedAction;
import calclipse.caldron.gui.search.SearchPanel;
import calclipse.caldron.gui.tab.TabManager;

/**
 * Focus the search result.
 * @author T. Sommerland
 */
public class FocusSearchAction extends LocalizedAction {

    private static final long serialVersionUID = 1L;

    public FocusSearchAction() {
        super("Search");
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
        TabManager.selectTab(SearchPanel.getInstance().getTextPane(), true);
        SearchPanel.getInstance().getTextPane().setCaretPosition(0);
    }

    @Resource("calclipse.caldron.gui.search.actions.FocusSearchAction.accelerator")
    @Override
    public void setAccelerator(final String keyStroke) {
        super.setAccelerator(keyStroke);
    }

    @Resource("calclipse.caldron.gui.search.actions.FocusSearchAction.largeIcon")
    @Override
    public void setLargeIcon(final Icon largeIcon) {
        super.setLargeIcon(largeIcon);
    }

    @Resource("calclipse.caldron.gui.search.actions.FocusSearchAction.longDescription")
    @Override
    public void setLongDescription(final String longDescription) {
        super.setLongDescription(longDescription);
    }

    @Resource("calclipse.caldron.gui.search.actions.FocusSearchAction.mnemonic")
    @Override
    public void setMnemonic(final String keyStroke) {
        super.setMnemonic(keyStroke);
    }

    @Resource("calclipse.caldron.gui.search.actions.FocusSearchAction.name")
    @Override
    public void setName(final String name) {
        super.setName(name);
    }

    @Resource("calclipse.caldron.gui.search.actions.FocusSearchAction.shortDescription")
    @Override
    public void setShortDescription(final String shortDescription) {
        super.setShortDescription(shortDescription);
    }

    @Resource("calclipse.caldron.gui.search.actions.FocusSearchAction.smallIcon")
    @Override
    public void setSmallIcon(final Icon smallIcon) {
        super.setSmallIcon(smallIcon);
    }
}
