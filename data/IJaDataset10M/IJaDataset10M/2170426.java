package calclipse.caldron.gui.win.actions;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import calclipse.Resource;
import calclipse.caldron.gui.locale.LocalizedAction;
import calclipse.caldron.gui.win.WinListPanel;
import calclipse.core.gui.GUI;
import calclipse.core.gui.Win;

/**
 * Positions the selected Wins in the bottom right corner.
 * @author T. Sommerland
 */
public class BottomRightAction extends LocalizedAction implements ListSelectionListener {

    private static final long serialVersionUID = 1L;

    public BottomRightAction() {
        super("Bottom right");
        WinListPanel.getInstance().getWinList().addListSelectionListener(this);
        enableDisable();
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
        final Dimension winPaneSize = GUI.getGUI().getTopLevelWin().getWinPane().getSize();
        final Point location = new Point();
        for (final Win win : WinListPanel.getInstance().getSelectedWins()) {
            location.x = winPaneSize.width - win.getWidth();
            location.y = winPaneSize.height - win.getHeight();
            win.setLocation(location);
        }
    }

    @Override
    public void valueChanged(final ListSelectionEvent evt) {
        enableDisable();
    }

    private void enableDisable() {
        setEnabled(WinListPanel.getInstance().getWinList().getSelectedValue() != null);
    }

    @Resource("calclipse.caldron.gui.win.actions.BottomRightAction.accelerator")
    @Override
    public void setAccelerator(final String keyStroke) {
        super.setAccelerator(keyStroke);
    }

    @Resource("calclipse.caldron.gui.win.actions.BottomRightAction.largeIcon")
    @Override
    public void setLargeIcon(final Icon largeIcon) {
        super.setLargeIcon(largeIcon);
    }

    @Resource("calclipse.caldron.gui.win.actions.BottomRightAction.longDescription")
    @Override
    public void setLongDescription(final String longDescription) {
        super.setLongDescription(longDescription);
    }

    @Resource("calclipse.caldron.gui.win.actions.BottomRightAction.mnemonic")
    @Override
    public void setMnemonic(final String keyStroke) {
        super.setMnemonic(keyStroke);
    }

    @Resource("calclipse.caldron.gui.win.actions.BottomRightAction.name")
    @Override
    public void setName(final String name) {
        super.setName(name);
    }

    @Resource("calclipse.caldron.gui.win.actions.BottomRightAction.shortDescription")
    @Override
    public void setShortDescription(final String shortDescription) {
        super.setShortDescription(shortDescription);
    }

    @Resource("calclipse.caldron.gui.win.actions.BottomRightAction.smallIcon")
    @Override
    public void setSmallIcon(final Icon smallIcon) {
        super.setSmallIcon(smallIcon);
    }
}
