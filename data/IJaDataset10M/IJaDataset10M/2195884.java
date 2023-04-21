package dsrwebserver.missions;

import dsr.models.map.AbstractMapModel;

public class MoonbaseAssault_4Player extends AbstractMission {

    private static final int MAX_TURNS = 30;

    private static final String SIDE1_NAME = "Laser Squad";

    private static final String SIDE2_NAME = "Marsec Corp";

    private static final String SIDE3_NAME = "Globex Inc.";

    private static final String SIDE4_NAME = "Space Ruffians";

    public MoonbaseAssault_4Player() {
        super(AbstractMission.MOONBASE_ASSAULT_4P, CP_OMNI_CORP, 4, SIDE1_NAME, SIDE2_NAME, SIDE3_NAME, SIDE4_NAME, MAX_TURNS, 240, 240, 240, 240, -1, AbstractMission.SHOW_CEILINGS, 7, 7, 7, 7, 1.27f, AbstractMission.INDESTRUCTABLE_WALLS, AbstractMapModel.SLIM_WALLS, "moonbase_assault_4p.txt", IS_NOT_CAMPAIGN, AbstractMission.GR_MULTI_PLAYER, IS_NOT_SNAFU, SPECIFIED_WIN_AT_END, "4p_moonbaseassault.csv");
    }

    @Override
    public String getSideDescription(int side) {
        switch(side) {
            case 1:
                return SIDE1_NAME;
            case 2:
                return SIDE2_NAME;
            case 3:
                return SIDE3_NAME;
            case 4:
                return SIDE4_NAME;
            default:
                throw new RuntimeException("Unknown side: " + side);
        }
    }

    public String getShortDesc() {
        return "Computers must be destroyed.";
    }

    public String getMission1Liner(int side) {
        return "You must destroy the computers";
    }

    public int canBePlayedOnAndroid() {
        return ONLY_ONE_CLIENT;
    }
}
