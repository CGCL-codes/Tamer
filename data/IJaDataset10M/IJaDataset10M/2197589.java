package ch.skyguide.tools.requirement.hmi.action;

import java.awt.event.ActionEvent;
import ch.skyguide.fdp.common.hmi.framework.form.FormProxyChangeEvent;
import ch.skyguide.fdp.common.hmi.framework.form.IFormProxy;
import ch.skyguide.tools.requirement.data.IRequirementProject;
import ch.skyguide.tools.requirement.data.RequirementProject;
import ch.skyguide.tools.requirement.hmi.RequirementTreeModel;
import ch.skyguide.tools.requirement.hmi.iso.Version;
import ch.skyguide.tools.requirement.hmi.model.BeanManagerAndTableModelFactory;

@SuppressWarnings("serial")
public class NextMinorVersionAction extends AbstractVersionAction {

    public NextMinorVersionAction(final IFormProxy<RequirementProject> proxy) {
        super(BeanManagerAndTableModelFactory.getInstance().getTranslatedText("menu.NextMinor"), proxy);
    }

    public void actionPerformed(ActionEvent e) {
        final IRequirementProject project = (IRequirementProject) formProxy;
        final Version version = RequirementTreeModel.VERSION_HANDLER.createVersion(project.getVersion());
        if (version != null) {
            project.setVersion(version.nextMinor().toString());
        }
    }

    public void propertyChange(FormProxyChangeEvent evt) {
        if ("Version".equals(evt.getAccessorName())) {
            setEnabled(!hasVersionChanged());
        }
    }
}
