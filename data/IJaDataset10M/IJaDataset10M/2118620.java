package Server;

import TransmitterS.GroupApplyIntelligence;

/**
 * @author LK13
 */
public class GroupApplyIntelligenceImp extends IntelligenceImp implements GroupApplyIntelligence {

    private static final long serialVersionUID = 3936337855914899170L;

    public final int applicant;

    public final int group;

    public GroupApplyIntelligenceImp(int _applicant, int _group) {
        super();
        this.applicant = _applicant;
        this.group = _group;
    }
}
