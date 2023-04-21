package ch.skyguide.tools.requirement.hmi.action;

import javax.swing.JTable;
import ch.skyguide.tools.requirement.data.HistoryMarkerType;
import ch.skyguide.tools.requirement.data.Roles;
import ch.skyguide.tools.requirement.hmi.RepositoryManager;
import ch.skyguide.tools.requirement.hmi.RequirementTreeModel;
import ch.skyguide.tools.requirement.hmi.model.BeanManagerAndTableModelFactory;
import ch.skyguide.tools.usermgt.IRole;

@SuppressWarnings("serial")
public class MarkProposalAction extends AbstractHistoryMarkerAction {

    public MarkProposalAction(RequirementTreeModel _model, RepositoryManager _repositoryManager, JTable _table) {
        super(BeanManagerAndTableModelFactory.getInstance().getTranslatedText("MarkProposal"), _model, _repositoryManager, _table);
    }

    @Override
    protected HistoryMarkerType getType() {
        return HistoryMarkerType.PROPOSAL;
    }

    @Override
    protected IRole getAuthorizedRole() {
        return Roles.OWNER;
    }
}
