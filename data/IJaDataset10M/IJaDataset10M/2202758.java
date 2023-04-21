package uk.ac.lkl.migen.system.ai.feedback.ui.callout;

import java.awt.event.*;
import uk.ac.lkl.common.ui.jft.*;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.ExternalInterface;
import uk.ac.lkl.migen.system.expresser.model.AttributeHandle;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;

/**
 * A call out that points to one of more attributes of shapes 
 * (e.g. in property lists). 
 * 
 * @author mavrikis
 */
public class MoreHelpCallOutForAttribute extends MoreHelpExpresserTabPointingCallOut {

    public MoreHelpCallOutForAttribute(int selectedTabbedPanel, BlockShape shape, AttributeHandle<IntegerValue> attributeHandle, String text) {
        super(selectedTabbedPanel, text);
        this.setContent(text);
        addTarget(ExternalInterface.getRowPanelForAttributeOfHandle(selectedTabbedPanel, shape, attributeHandle).getExpressionPanel());
        addHighlightTarget(ExternalInterface.getRowPanelForAttributeOfHandle(selectedTabbedPanel, shape, attributeHandle).getExpressionPanel());
        setHighlight(new CircleHighlight());
        addActionListenerToMainButton(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
            }
        });
    }
}
