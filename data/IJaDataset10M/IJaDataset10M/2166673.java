package ch.skyguide.tools.requirement.hmi.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import ch.skyguide.tools.requirement.hmi.RequirementFactory;
import ch.skyguide.tools.requirement.hmi.RequirementTool;
import ch.skyguide.tools.requirement.hmi.model.BeanManagerAndTableModelFactory;

@SuppressWarnings("serial")
public class ExpandAction extends AbstractAction {

    private final RequirementTool tool;

    private final RequirementFactory factory;

    public ExpandAction(final RequirementTool _tool, final RequirementFactory _factory) {
        super(BeanManagerAndTableModelFactory.getInstance().getTranslatedText("menu.expand"));
        tool = _tool;
        factory = _factory;
        putValue(SHORT_DESCRIPTION, BeanManagerAndTableModelFactory.getInstance().getTranslatedText("hint.expand"));
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        factory.expandSelectedDomain(tool);
    }
}
