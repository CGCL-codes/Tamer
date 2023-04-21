package dsrwebserver.missions;

import dsr.models.map.AbstractMapModel;

public class BattleForSigma7Mission extends AbstractMission {

    private static final int MAX_TURNS = 20;

    private static final String SIDE1_NAME = "Laser Squad";

    private static final String SIDE2_NAME = "Renegade Squad";

    public BattleForSigma7Mission() {
        super(AbstractMission.BATTLE_FOR_SIGMA_7, CP_SIGMA_7, SIDE1_NAME, SIDE2_NAME, MAX_TURNS, 350, 350, -1, AbstractMission.NO_CEILINGS, 0, 0, 0.51f, AbstractMission.STRONG_WALLS, AbstractMapModel.SLIM_WALLS, "battleforsigma7.txt", "", IS_NOT_CAMPAIGN, AbstractMission.GR_NONE, SPECIFIED_WIN_AT_END, "battleforsigma7.csv");
    }

    @Override
    public String getSideDescription(int side) {
        switch(side) {
            case 1:
                return SIDE1_NAME;
            case 2:
                return SIDE2_NAME;
            default:
                throw new RuntimeException("Unknown side: " + side);
        }
    }

    public String getShortDesc() {
        return "Two equal sides must battle it out across a barren landscape.";
    }

    public String getMission1Liner(int side) {
        return "You must kill your opponent's units";
    }

    public int canBePlayedOnAndroid() {
        return ONLY_ONE_CLIENT;
    }
}
