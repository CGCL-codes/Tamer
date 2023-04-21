package org.qtitools.validatr.action.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.qtitools.validatr.panel.SourcePanel;
import org.qtitools.validatr.panel.AbstractPanel.ViewType;

/**
 * Implementation of source action.
 * <p>
 * This action sets ROOT view type of source panel.
 */
public class SourceRootAction extends SourceAction {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs action.
	 *
	 * @param sourcePanel source panel of application
	 */
    public SourceRootAction(SourcePanel sourcePanel) {
        super(sourcePanel, ViewType.ROOT);
        putValue(NAME, "Root");
        putValue(MNEMONIC_KEY, KeyEvent.VK_R);
    }

    public void actionPerformed(ActionEvent event) {
        setDesiredViewType();
    }
}
