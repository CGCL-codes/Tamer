package bagaturchess.bitboard.tests.pawnstructure.passers;

import bagaturchess.bitboard.impl.Figures;
import bagaturchess.bitboard.tests.pawnstructure.Test;

public class Passers4 extends Test {

    @Override
    public String getFEN() {
        return "4k3/8/1P6/8/8/8/8/4K3 w";
    }

    @Override
    public void validate() {
        validatePassers(Figures.COLOUR_WHITE, B6, 5);
        validatePassers(Figures.COLOUR_BLACK, 0L, 0);
        validateUnstoppablePassers(Figures.COLOUR_WHITE, B6);
        validateUnstoppablePassers(Figures.COLOUR_BLACK, 0L);
    }
}
