package ch.skyguide.tools.requirement.hmi;

import ch.skyguide.fdp.common.IObjectListModel;
import ch.skyguide.fdp.common.hmi.framework.table.ObjectTableModel;
import ch.skyguide.fdp.common.hmi.memento.MementoContainer;
import ch.skyguide.tools.requirement.data.AbstractRequirement;
import ch.skyguide.tools.requirement.data.CompletedVersion;
import ch.skyguide.tools.requirement.data.Requirement;
import ch.skyguide.tools.requirement.hmi.enabler.EditingStateManager;
import ch.skyguide.tools.requirement.hmi.model.BeanManagerAndTableModelFactory;

@SuppressWarnings("serial")
public class CompletedVersionRelatedRequirementPanel extends AbstractVersionRelatedRequirementPanel<CompletedVersion> {

    private static class VersionFilter extends AbstractVersionFilter<CompletedVersion> {

        private CompletedVersion version;

        @Override
        public void setVersion(CompletedVersion _version) {
            version = _version;
            fireChanged();
        }

        @Override
        public boolean accept(AbstractRequirement aRequirement) {
            if (aRequirement instanceof Requirement) {
                Requirement requirement = (Requirement) aRequirement;
                return requirement.getCompletedVersion() == version;
            }
            return false;
        }
    }

    public CompletedVersionRelatedRequirementPanel(final IObjectListModel<AbstractRequirement> _allRequirements, final IObjectListModel<CompletedVersion> model, RequirementTool _requirementTool, EditingStateManager editingStateManager, final MementoContainer mementos) {
        super(_allRequirements, model, _requirementTool, editingStateManager, mementos);
    }

    @Override
    protected AbstractVersionFilter<CompletedVersion> getFilter() {
        return new VersionFilter();
    }

    @Override
    protected ObjectTableModel<CompletedVersion> getVersionTableModel(final IObjectListModel<CompletedVersion> model) {
        return BeanManagerAndTableModelFactory.getInstance().createCompletedVersionTableModel(model);
    }

    @Override
    protected CompletedVersion createVersion() {
        final CompletedVersion version = new CompletedVersion();
        version.setName("<release name>");
        return version;
    }
}
