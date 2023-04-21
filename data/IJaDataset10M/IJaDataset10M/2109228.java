package bagaturchess.bitboard.impl.eval.pawns.model;

import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.bitboard.impl.Board;

public class ModelBuilder {

    static PawnsModel tmp = new PawnsModel();

    public static PawnsModel build(IBitBoard bitboard) {
        tmp.rebuild(bitboard);
        return tmp;
    }
}
