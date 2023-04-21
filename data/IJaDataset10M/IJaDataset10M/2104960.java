package com.chunayev.numerology.gui.action;

import java.awt.event.KeyEvent;
import java.util.Set;
import com.chunayev.numerology.domain.PositionSummary;

/**
 * @author Sergey Chunayev
 */
public class SaveSurnameTokenAction extends AbstractTokenSaveSetAction {

    /**
     * 
     */
    private static final long serialVersionUID = -2141010255765573806L;

    /**
     * 
     */
    public SaveSurnameTokenAction() {
        super("surname", KeyEvent.VK_U);
    }

    @Override
    public Set<String> getModifiedSet(final PositionSummary positionSummary) {
        return positionSummary.getSurnames();
    }
}
