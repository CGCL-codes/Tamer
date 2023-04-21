package org.jabusuite.webclient.address.employee.team;

import org.jabusuite.webclient.controls.JbsExtent;
import org.jabusuite.address.employee.Team;
import org.jabusuite.webclient.dataediting.FmEditJbsBaseObject;
import org.jabusuite.webclient.main.JbsL10N;

public class FmTeamEdit extends FmEditJbsBaseObject {

    private static final long serialVersionUID = -1297461024449936844L;

    public FmTeamEdit() {
        super(JbsL10N.getString("Team.formTitle"), new JbsExtent(300), new JbsExtent(200));
        this.setPnTeamEdit(new PnTeamEdit());
    }

    /**
	 * @return Returns the pnTeamEdit.
	 */
    public PnTeamEdit getPnTeamEdit() {
        return (PnTeamEdit) this.getPnEditJbsObject();
    }

    /**
	 * @param pnTeamEdit The pnTeamEdit to set.
	 */
    public void setPnTeamEdit(PnTeamEdit pnTeamEdit) {
        this.setPnEditJbsObject(pnTeamEdit);
    }

    @Override
    public void createJbsBaseObject() {
        this.setJbsBaseObject(new Team());
    }
}
