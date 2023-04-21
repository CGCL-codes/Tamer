package es.eucm.eadventure.editor.gui.elementpanels.atrezzo;

import javax.swing.JComponent;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.atrezzo.AtrezzoDataControl;
import es.eucm.eadventure.editor.gui.elementpanels.ElementPanel;
import es.eucm.eadventure.editor.gui.elementpanels.PanelTab;

public class AtrezzoPanel extends ElementPanel {

    /**
     * Requiered
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param atrezzoDataControl
     *            Atrezzo controller
     */
    public AtrezzoPanel(AtrezzoDataControl atrezzoDataControl) {
        this.addTab(new AtrezzoLooksPanelTab(atrezzoDataControl));
        this.addTab(new AtrezzoDocPanelTab(atrezzoDataControl));
    }

    private class AtrezzoDocPanelTab extends PanelTab {

        private AtrezzoDataControl atrezzoDataControl;

        public AtrezzoDocPanelTab(AtrezzoDataControl atrezzoDataControl) {
            super(TC.get("Atrezzo.DocPanelTitle"), atrezzoDataControl);
            this.atrezzoDataControl = atrezzoDataControl;
        }

        @Override
        protected JComponent getTabComponent() {
            return new AtrezzoDocPanel(atrezzoDataControl);
        }
    }

    private class AtrezzoLooksPanelTab extends PanelTab {

        private AtrezzoDataControl atrezzoDataControl;

        public AtrezzoLooksPanelTab(AtrezzoDataControl atrezzoDataControl) {
            super(TC.get("Atrezzo.LookPanelTitle"), atrezzoDataControl);
            this.atrezzoDataControl = atrezzoDataControl;
        }

        @Override
        protected JComponent getTabComponent() {
            return new AtrezzoLooksPanel(atrezzoDataControl);
        }
    }
}
