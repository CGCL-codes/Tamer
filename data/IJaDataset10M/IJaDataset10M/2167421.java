package g2.routemaster.model;

import de.upb.tools.sdm.*;
import java.util.*;

public class OutsideOfBaiern extends BonusTile {

    public boolean conflictsWith(BonusTile tile) {
        return tile instanceof OutsideOfBaiern;
    }

    public boolean isAvailableBase(TurnManager turn) {
        boolean fujaba__Success = false;
        Board board = null;
        SeatPlace seatPlace = null;
        Player player = null;
        Iterator fujaba__IterBoardToARegion = null;
        Region aRegion = null;
        try {
            fujaba__Success = false;
            JavaSDM.ensure(turn != null);
            player = turn.getPlayer();
            JavaSDM.ensure(player != null);
            seatPlace = player.getSeatPlace();
            JavaSDM.ensure(seatPlace != null);
            board = seatPlace.getBoard();
            JavaSDM.ensure(board != null);
            fujaba__Success = true;
        } catch (JavaSDMException fujaba__InternalException) {
            fujaba__Success = false;
        }
        try {
            fujaba__Success = false;
            JavaSDM.ensure(board != null);
            fujaba__Success = false;
            fujaba__IterBoardToARegion = board.iteratorOfRegion();
            while (!(fujaba__Success) && fujaba__IterBoardToARegion.hasNext()) {
                try {
                    aRegion = (Region) fujaba__IterBoardToARegion.next();
                    JavaSDM.ensure(aRegion != null);
                    JavaSDM.ensure(JavaSDM.stringCompare(aRegion.getName(), "Baiern") != 0);
                    JavaSDM.ensure(!aRegion.isPartiallyCoveredBy(player));
                    fujaba__Success = true;
                } catch (JavaSDMException fujaba__InternalException) {
                    fujaba__Success = false;
                }
            }
            JavaSDM.ensure(fujaba__Success);
            fujaba__Success = true;
        } catch (JavaSDMException fujaba__InternalException) {
            fujaba__Success = false;
        }
        if (fujaba__Success) {
            return false;
        } else {
            return true;
        }
    }
}
