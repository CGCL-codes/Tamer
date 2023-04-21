package ezsudoku.controller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBoxMenuItem;

/**
 * Listens to strategy activations.
 *
 * @author Cedric Chantepie (cchantepie@corsaire.fr)
 */
class StrategyActionListener implements ActionListener {

    /**
     */
    private PlateControler controller = null;

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent evt) {
        StrategyManager manager = this.controller.getStrategyManager();
        JCheckBoxMenuItem item = (JCheckBoxMenuItem) evt.getSource();
        String name = item.getText();
        manager.setStrategyEnabled(name, item.getState());
    }

    /**
     */
    protected StrategyActionListener(final PlateControler controller) {
        this.controller = controller;
    }
}
