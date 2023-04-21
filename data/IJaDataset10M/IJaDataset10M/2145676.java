package calclipse.caldron.gui.win.actions;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import calclipse.Resource;
import calclipse.caldron.ExceptionHandlers;
import calclipse.caldron.gui.locale.LocalizedAction;
import calclipse.caldron.gui.win.WinListPanel;
import calclipse.core.gui.Win;
import calclipse.core.gui.WinVetoException;

/**
 * Closes the selected Wins.
 * @author T. Sommerland
 */
public class CloseWinAction extends LocalizedAction implements ListSelectionListener {

    private static final long serialVersionUID = 1L;

    public CloseWinAction() {
        super("Close");
        WinListPanel.getInstance().getWinList().addListSelectionListener(this);
        enableDisable();
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
        for (final Win win : WinListPanel.getInstance().getSelectedWins()) {
            try {
                win.close();
            } catch (final WinVetoException ex) {
                ExceptionHandlers.getGeneralHandler().handle(ex);
            }
        }
    }

    @Override
    public void valueChanged(final ListSelectionEvent evt) {
        enableDisable();
    }

    private void enableDisable() {
        setEnabled(WinListPanel.getInstance().getWinList().getSelectedValue() != null);
    }

    @Resource("calclipse.caldron.gui.win.actions.CloseWinAction.accelerator")
    @Override
    public void setAccelerator(final String keyStroke) {
        super.setAccelerator(keyStroke);
    }

    @Resource("calclipse.caldron.gui.win.actions.CloseWinAction.largeIcon")
    @Override
    public void setLargeIcon(final Icon largeIcon) {
        super.setLargeIcon(largeIcon);
    }

    @Resource("calclipse.caldron.gui.win.actions.CloseWinAction.longDescription")
    @Override
    public void setLongDescription(final String longDescription) {
        super.setLongDescription(longDescription);
    }

    @Resource("calclipse.caldron.gui.win.actions.CloseWinAction.mnemonic")
    @Override
    public void setMnemonic(final String keyStroke) {
        super.setMnemonic(keyStroke);
    }

    @Resource("calclipse.caldron.gui.win.actions.CloseWinAction.name")
    @Override
    public void setName(final String name) {
        super.setName(name);
    }

    @Resource("calclipse.caldron.gui.win.actions.CloseWinAction.shortDescription")
    @Override
    public void setShortDescription(final String shortDescription) {
        super.setShortDescription(shortDescription);
    }

    @Resource("calclipse.caldron.gui.win.actions.CloseWinAction.smallIcon")
    @Override
    public void setSmallIcon(final Icon smallIcon) {
        super.setSmallIcon(smallIcon);
    }
}
