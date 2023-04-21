package org.chess.quasimodo.domain.logic;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;

public class PawnStructure {

    private final List<Integer> pawnOffsets = new ArrayList<Integer>();

    public void addPawn(int index) {
        Assert.isTrue(index > -1 && index < 64, "Pawn index must be within 0-63 range, not " + index);
        pawnOffsets.add(index);
    }

    public List<PawnStructure> connectedStructures() {
        List<PawnStructure> structures = new ArrayList<PawnStructure>();
        return structures;
    }

    public List<Integer> getIsolaniPawns() {
        List<Integer> isolanies = new ArrayList<Integer>();
        boolean is_isolani;
        for (Integer outerOffset : pawnOffsets) {
            is_isolani = true;
            if (outerOffset != null) {
                for (Integer innerOffset : pawnOffsets) {
                    if (innerOffset != null && innerOffset != outerOffset && Math.abs(innerOffset & 7 - outerOffset & 7) == 1) {
                        is_isolani = false;
                        break;
                    }
                }
            }
            if (is_isolani) {
                isolanies.add(outerOffset);
            }
        }
        return isolanies;
    }
}
