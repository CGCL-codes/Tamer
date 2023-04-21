package bean;

import java.util.Set;

/**
 *
 * @author sirius
 */
public class RelationBean {

    private int id;

    private String name;

    private Set<IdeaIdeaBean> ideas_related;

    private Set<ProposalProposalBean> proposals_related;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIdeas_related(Set<IdeaIdeaBean> ideas_related) {
        this.ideas_related = ideas_related;
    }

    public Set<IdeaIdeaBean> getIdeas_related() {
        return ideas_related;
    }

    public void setProposals_related(Set<ProposalProposalBean> proposals_related) {
        this.proposals_related = proposals_related;
    }

    public Set<ProposalProposalBean> getProposals_related() {
        return proposals_related;
    }
}
