package bagaturchess.bitboard.impl.endgame;

import bagaturchess.bitboard.api.IMaterialState;
import bagaturchess.bitboard.impl.Constants;
import bagaturchess.bitboard.impl.MoveListener;
import bagaturchess.bitboard.impl.movegen.MoveInt;

public class MaterialState implements MoveListener, IMaterialState {

    private int piecesCount;

    private int[] pidsCounts;

    public MaterialState() {
        init();
    }

    private void init() {
        piecesCount = 0;
        pidsCounts = new int[Constants.PID_MAX];
    }

    public int getPiecesCount() {
        return piecesCount;
    }

    public int[] getPIDsCounts() {
        return pidsCounts;
    }

    public void addPiece_Special(int pid, int fieldID) {
        throw new UnsupportedOperationException();
    }

    public void initially_addPiece(int pid, int fieldID) {
        added(pid);
    }

    public void postBackwardMove(int move) {
        if (MoveInt.isCapture(move)) {
            int cap_pid = MoveInt.getCapturedFigurePID(move);
            added(cap_pid);
        }
        if (MoveInt.isPromotion(move)) {
            int prom_pid = MoveInt.getPromotionFigurePID(move);
            removed(prom_pid);
            int pid = MoveInt.getFigurePID(move);
            added(pid);
        }
    }

    public void postForwardMove(int move) {
        if (MoveInt.isCapture(move)) {
            int cap_pid = MoveInt.getCapturedFigurePID(move);
            removed(cap_pid);
        }
        if (MoveInt.isPromotion(move)) {
            int prom_pid = MoveInt.getPromotionFigurePID(move);
            added(prom_pid);
            int pid = MoveInt.getFigurePID(move);
            removed(pid);
        }
    }

    public void preBackwardMove(int move) {
    }

    public void preForwardMove(int move) {
    }

    protected void added(int figurePID) {
        inc(figurePID);
    }

    protected void removed(int figurePID) {
        dec(figurePID);
    }

    private void inc(int pid) {
        piecesCount++;
        pidsCounts[pid]++;
    }

    protected void dec(int pid) {
        piecesCount--;
        pidsCounts[pid]--;
    }
}
