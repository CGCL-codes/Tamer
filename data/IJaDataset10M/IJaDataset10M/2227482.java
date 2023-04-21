package ch.skyguide.tools.requirement.hmi.action;

import java.awt.event.KeyEvent;
import ch.skyguide.tools.requirement.data.AbstractRequirement;
import ch.skyguide.tools.requirement.data.RequirementDomain;
import ch.skyguide.tools.requirement.hmi.RequirementFactory;

@SuppressWarnings("serial")
public class AddDomainAction extends AbstractRequirementFactoryAction {

    public AddDomainAction(RequirementFactory _requirementFactory) {
        super("Add sub-domain", _requirementFactory);
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_D));
        putValue(SHORT_DESCRIPTION, "Add a new sub-domain to the clicked domain");
    }

    @Override
    protected AbstractRequirement createNewRequirement() {
        return new RequirementDomain();
    }
}
