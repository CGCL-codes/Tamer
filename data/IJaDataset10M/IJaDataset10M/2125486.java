package bagaturchess.bitboard.impl.plies.specials;

import bagaturchess.bitboard.api.IInternalMoveList;
import bagaturchess.bitboard.impl.Constants;
import bagaturchess.bitboard.impl.Fields;
import bagaturchess.bitboard.impl.Figures;
import bagaturchess.bitboard.impl.movegen.Move;
import bagaturchess.bitboard.impl.movegen.MoveInt;
import bagaturchess.bitboard.impl.zobrist.ConstantStructure;

public class Promotioning extends Fields {

    public static final long WHITE_PROMOTIONS = A8 | B8 | C8 | D8 | E8 | F8 | G8 | H8;

    public static final long BLACK_PROMOTIONS = A1 | B1 | C1 | D1 | E1 | F1 | G1 | H1;

    public static void fillCapturePromotion(final int figureID, final int figureColour, final int fromFieldID, final int[] figuresIDsPerFieldsIDs, final int dirID, final int toFieldID, final int promotionFigurePID, final long[] move) {
        move[0] = Move.MASK_EMPTY | Move.MASK_CAPTURE | Move.MASK_PROMOTION;
        move[1] = figureID;
        move[5] = dirID;
        move[6] = 0;
        move[9] = fromFieldID;
        move[10] = toFieldID;
        int capturedFigureID = figuresIDsPerFieldsIDs[toFieldID];
        if (capturedFigureID == Constants.PID_NONE) {
            throw new IllegalStateException("capturedFigureID=" + capturedFigureID);
        }
        move[11] = capturedFigureID;
        move[21] = promotionFigurePID;
    }

    public static void fillNonCapturePromotion(final int figureID, final int figureColour, final int fromFieldID, final int dirID, final int toFieldID, final int promotionFigurePID, final long[] move) {
        move[0] = Move.MASK_EMPTY | Move.MASK_PROMOTION;
        move[1] = figureID;
        move[5] = dirID;
        move[6] = 0;
        move[9] = fromFieldID;
        move[10] = toFieldID;
        move[21] = promotionFigurePID;
    }
}
