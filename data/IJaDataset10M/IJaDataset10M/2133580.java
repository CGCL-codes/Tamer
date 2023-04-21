package com.cosylab.vdct.application;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import com.cosylab.gui.components.util.ImageHelper;
import com.cosylab.vdct.model.Model;
import com.cosylab.vdct.VisualDCTI18N;

public class SwitchModelViewAction extends AbstractAction implements Action {

    private static VisualDCTI18N i18n = GUIConstants.i18n;

    static final String GOINTO = "Explore...";

    /**
	 * 
	 */
    private final Model node;

    private VisualDCTEngine engine;

    public SwitchModelViewAction(VisualDCTEngine engine, Model node) {
        super(i18n.getString(GUIConstants.EXPLORE_LABEL), ImageHelper.createImageIcon(GUIConstants.EXPLORE_ICON));
        putValue(SHORT_DESCRIPTION, i18n.getString(GUIConstants.EXPLORE_TOOLTIP));
        this.node = node;
        this.engine = engine;
    }

    public void actionPerformed(ActionEvent e) {
        engine.setView(node);
    }
}
