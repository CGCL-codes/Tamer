package ezsudoku.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import ezsudoku.view.PlateView;
import ezsudoku.view.NumberBar;

/**
 * @author Cedric Chantepie (cchantepie@corsaire.fr)
 */
final class PopupMouseListener extends MouseAdapter {

    /**
     */
    private PlateControler controller = null;

    /**
     * {@inheritDoc}
     */
    public void mouseEntered(MouseEvent evt) {
        Object src = evt.getSource();
        JMenuItem item = (JMenuItem) src;
        PlateView view = controller.getView();
        NumberBar bar = view.getTopToolbar();
        if (!item.isEnabled()) {
            view.clearSpotlights();
            view.refreshSpotlights();
            bar.selectRubber();
            return;
        }
        String commandStr = item.getActionCommand();
        Request request = new Request(commandStr);
        Integer value = ChangeValueActionListener.integerValue(request.getActionName());
        ActionEvent actionEvt = new ActionEvent(item, -1, commandStr);
        controller.candiActionListener.actionPerformed(actionEvt);
        bar.select(value);
    }

    /**
     */
    public PopupMouseListener(final PlateControler controller) {
        this.controller = controller;
    }
}
