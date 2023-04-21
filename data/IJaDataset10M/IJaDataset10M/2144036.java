package es.eucm.eadventure.editor.gui.elementpanels.general;

import javax.swing.JComponent;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.general.ChapterDataControl;
import es.eucm.eadventure.editor.gui.elementpanels.ElementPanel;
import es.eucm.eadventure.editor.gui.elementpanels.PanelTab;

public class ChapterPanel extends ElementPanel {

    /**
     * Required.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param chapterDataControl
     *            Chapter controller
     */
    public ChapterPanel(ChapterDataControl chapterDataControl) {
        addTab(new ChapterPanelTab(chapterDataControl));
    }

    private class ChapterPanelTab extends PanelTab {

        private ChapterDataControl sDataControl;

        public ChapterPanelTab(ChapterDataControl sDataControl) {
            super(TC.get("Chapter.Title"), sDataControl);
            this.sDataControl = sDataControl;
        }

        @Override
        protected JComponent getTabComponent() {
            return new ChapterInfoPanel(sDataControl);
        }
    }
}
