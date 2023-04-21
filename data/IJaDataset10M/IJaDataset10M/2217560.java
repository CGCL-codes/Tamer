package dsrwebserver.missions;

import ssmith.util.MyList;
import dsr.models.map.AbstractMapModel;

public class ATOTB_2v2 extends AbstractMission {

    private static final int MAX_TURNS = 30;

    private static final int CREDS = 200;

    private static final String SIDE1_NAME = "Laser Platoon A";

    private static final String SIDE2_NAME = "Star Federation A";

    private static final String SIDE3_NAME = "Laser Platoon B";

    private static final String SIDE4_NAME = "Star Federation B";

    public ATOTB_2v2() {
        super(AbstractMission.ATOTB_2V2, CP_SHADOW_CONSPIRACY, 4, SIDE1_NAME, SIDE2_NAME, SIDE3_NAME, SIDE4_NAME, MAX_TURNS, CREDS, CREDS, CREDS, CREDS, -1, AbstractMission.SHOW_CEILINGS, 100, 100, 0, 0, 1.72f, AbstractMission.INDESTRUCTABLE_WALLS, AbstractMapModel.SLIM_WALLS, "ataleoftwobases2vs2.txt", IS_NOT_CAMPAIGN, AbstractMission.GR_LEVEL_2, false, SPECIFIED_WIN_AT_END, "ataleoftwobases2vs2.csv");
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
        return "Two sides must try to destroy each other's master computer.";
    }

    public String getMission1Liner(int side) {
        return "You must destroy your opponent's computer";
    }

    public MyList<Integer> getSidesForSide(int side) {
        switch(side) {
            case 1:
            case 3:
                return MyList.CreateIntsFromInts(1, 3);
            case 2:
            case 4:
                return MyList.CreateIntsFromInts(2, 4);
            default:
                throw new RuntimeException("Unknown side: " + side);
        }
    }

    public int canBePlayedOnAndroid() {
        return ONLY_ONE_CLIENT;
    }

    @Override
    public boolean doWeCareWhoOwnsTheComputers() {
        return true;
    }
}
