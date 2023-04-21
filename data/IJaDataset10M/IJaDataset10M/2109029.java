package uk.co.ordnancesurvey.confluence.ui.itemlist.proposal.item;

/**
 * Base class for all rabbit proposals, it consists of a proposal type and a
 * proposal source. The proposal source is an object that is used as the source
 * object that provides the proposal; subclasses of this should implement how
 * the proposal is extracted from the source object. The proposal type is used
 * to determine what to do when this proposal is accepted by he user.
 * 
 * @author rdenaux
 * 
 */
public abstract class BaseRabbitProposal<ProposalSourceType extends Object> {

    private ProposalSourceType proposalSource;

    public abstract RabbitProposalType getProposalType();

    public final ProposalSourceType getProposalSource() {
        return proposalSource;
    }

    public final void setProposalSource(ProposalSourceType proposalSource) {
        this.proposalSource = proposalSource;
        doSetProposalSource();
    }

    /**
	 * Subclasses can use this method to use the newly set proposalSource.
	 */
    protected abstract void doSetProposalSource();
}
