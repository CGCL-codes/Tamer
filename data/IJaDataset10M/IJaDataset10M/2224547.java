package gridrm.onto.messaging;

/**
   * Action of adding team advertisement to yellow pages.
* Protege name: CICAddTeam
* @author ontology bean generator
* @version 2009/01/30, 15:01:03
*/
public class CICAddTeam extends CICTeamAction {

    /**
   * Team advertisement.
* Protege name: teamAd
   */
    private OntoData teamAd;

    public void setTeamAd(OntoData value) {
        this.teamAd = value;
    }

    public OntoData getTeamAd() {
        return this.teamAd;
    }
}
