package ch.skyguide.tools.requirement.hmi.search;

import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import ch.skyguide.tools.requirement.data.AbstractRequirement;
import ch.skyguide.tools.requirement.data.RequirementProject;
import ch.skyguide.tools.requirement.hmi.IRequirementPanel;

public class ProjectSubjectSeeker extends AbstractTextComponentSeeker {

    public ProjectSubjectSeeker(final DefaultMutableTreeNode _node) {
        super(_node);
    }

    @Override
    protected String getElementText(AbstractRequirement requirement) {
        final RequirementProject project = (RequirementProject) requirement;
        return project.getSubject();
    }

    @Override
    protected AbstractElementSeeker getNextSeeker() {
        return new ProjectTitleSeeker(getNode());
    }

    @Override
    protected AbstractElementSeeker getPreviousSeeker() {
        return new ProjectHeadingSeeker(getNode());
    }

    @Override
    protected JTextComponent getTextComponent(IRequirementPanel panel) {
        return (JTextComponent) panel.getComponentAt("Subject");
    }
}
