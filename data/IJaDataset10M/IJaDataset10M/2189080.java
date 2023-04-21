package bagaturchess.bitboard.impl1.movegen;

import bagaturchess.bitboard.api.IInternalMoveList;
import bagaturchess.bitboard.impl.Constants;
import bagaturchess.bitboard.impl.Fields;
import bagaturchess.bitboard.impl.plies.checking.BlackPawnsChecks;
import bagaturchess.bitboard.impl.plies.specials.Promotioning;
import bagaturchess.bitboard.impl1.plies.Enpassanting;

public class BlackPawnMovesGen extends BlackPawnsChecks {

    static final int[][] attacksValidDirs = ALL_BLACK_PAWN_ATTACKS_VALID_DIRS;

    static final int[][] nonattacksValidDirs = ALL_BLACK_PAWN_NONATTACKS_VALID_DIRS;

    static final int[][][] attacksFieldIDs = ALL_BLACK_PAWN_ATTACKS_DIRS_WITH_FIELD_IDS;

    static final int[][][] nonattacksFieldIDs = ALL_BLACK_PAWN_NONATTACKS_DIRS_WITH_FIELD_IDS;

    static final long[][][] attacksBitboards = ALL_BLACK_PAWN_ATTACKS_DIRS_WITH_BITBOARDS;

    static final long[][][] nonattacksBitboards = ALL_BLACK_PAWN_NONATTACKS_DIRS_WITH_BITBOARDS;

    public static final void genAllMoves(final int fromFieldID, final int[] figuresIDsPerFieldsIDs, final int enpassantEnemyPawnFieldID, final IInternalMoveList list) {
        int[] validDirIDs = attacksValidDirs[fromFieldID];
        int[][] dirs_ids = attacksFieldIDs[fromFieldID];
        int size = validDirIDs.length;
        for (int i = 0; i < size; i++) {
            final int dirID = validDirIDs[i];
            final int toFieldID = dirs_ids[dirID][0];
            final int targetPID = figuresIDsPerFieldsIDs[toFieldID];
            if (targetPID == Constants.PID_NONE) {
                if (enpassantEnemyPawnFieldID != -1) {
                    int enemyFieldID = Enpassanting.ADJOINING_FILE_FIELD_ID_AT_CAPTURE[Constants.COLOUR_BLACK][fromFieldID][dirID];
                    if (enemyFieldID == enpassantEnemyPawnFieldID) {
                        list.reserved_add(MoveInt.createEnpassant(Constants.PID_B_PAWN, fromFieldID, toFieldID, dirID, Constants.PID_W_PAWN));
                    }
                } else {
                }
            } else {
                if (Constants.hasSameColour(Constants.PID_B_PAWN, targetPID)) {
                } else {
                    final long toBitboard = Fields.ALL_A1H1[toFieldID];
                    if ((toBitboard & Promotioning.BLACK_PROMOTIONS) != NUMBER_0) {
                        int cap_pid = figuresIDsPerFieldsIDs[toFieldID];
                        list.reserved_add(MoveInt.createCapturePromotion(fromFieldID, toFieldID, cap_pid, Constants.PID_B_QUEEN));
                        list.reserved_add(MoveInt.createCapturePromotion(fromFieldID, toFieldID, cap_pid, Constants.PID_B_ROOK));
                        list.reserved_add(MoveInt.createCapturePromotion(fromFieldID, toFieldID, cap_pid, Constants.PID_B_BISHOP));
                        list.reserved_add(MoveInt.createCapturePromotion(fromFieldID, toFieldID, cap_pid, Constants.PID_B_KNIGHT));
                    } else {
                        list.reserved_add(MoveInt.createCapture(Constants.PID_B_PAWN, fromFieldID, toFieldID, targetPID));
                    }
                }
                continue;
            }
        }
        validDirIDs = nonattacksValidDirs[fromFieldID];
        dirs_ids = nonattacksFieldIDs[fromFieldID];
        size = validDirIDs.length;
        for (int i = 0; i < size; i++) {
            final int dirID = validDirIDs[i];
            final int toFieldID = dirs_ids[dirID][0];
            final int targetPID = figuresIDsPerFieldsIDs[toFieldID];
            if (targetPID == Constants.PID_NONE) {
                final long toBitboard = Fields.ALL_A1H1[toFieldID];
                if ((toBitboard & Promotioning.BLACK_PROMOTIONS) != NUMBER_0) {
                    list.reserved_add(MoveInt.createPromotion(fromFieldID, toFieldID, Constants.PID_B_QUEEN));
                    list.reserved_add(MoveInt.createPromotion(fromFieldID, toFieldID, Constants.PID_B_ROOK));
                    list.reserved_add(MoveInt.createPromotion(fromFieldID, toFieldID, Constants.PID_B_BISHOP));
                    list.reserved_add(MoveInt.createPromotion(fromFieldID, toFieldID, Constants.PID_B_KNIGHT));
                } else {
                    list.reserved_add(MoveInt.createNonCapture(Constants.PID_B_PAWN, fromFieldID, toFieldID));
                }
                continue;
            } else {
                break;
            }
        }
    }

    public static final void genCapturePromotionMoves(final int fromFieldID, final int[] figuresIDsPerFieldsIDs, final IInternalMoveList list) {
        int[] validDirIDs = attacksValidDirs[fromFieldID];
        int[][] dirs_ids = attacksFieldIDs[fromFieldID];
        int size = validDirIDs.length;
        for (int i = 0; i < size; i++) {
            final int dirID = validDirIDs[i];
            final int toFieldID = dirs_ids[dirID][0];
            final int targetPID = figuresIDsPerFieldsIDs[toFieldID];
            if (targetPID == Constants.PID_NONE) {
            } else {
                if (Constants.hasSameColour(Constants.PID_B_PAWN, targetPID)) {
                } else {
                    final long toBitboard = Fields.ALL_A1H1[toFieldID];
                    if ((toBitboard & Promotioning.BLACK_PROMOTIONS) != NUMBER_0) {
                        int cap_pid = figuresIDsPerFieldsIDs[toFieldID];
                        list.reserved_add(MoveInt.createCapturePromotion(fromFieldID, toFieldID, cap_pid, Constants.PID_B_QUEEN));
                        list.reserved_add(MoveInt.createCapturePromotion(fromFieldID, toFieldID, cap_pid, Constants.PID_B_ROOK));
                        list.reserved_add(MoveInt.createCapturePromotion(fromFieldID, toFieldID, cap_pid, Constants.PID_B_BISHOP));
                        list.reserved_add(MoveInt.createCapturePromotion(fromFieldID, toFieldID, cap_pid, Constants.PID_B_KNIGHT));
                    } else {
                        list.reserved_add(MoveInt.createCapture(Constants.PID_B_PAWN, fromFieldID, toFieldID, targetPID));
                    }
                }
                continue;
            }
        }
        validDirIDs = nonattacksValidDirs[fromFieldID];
        dirs_ids = nonattacksFieldIDs[fromFieldID];
        size = validDirIDs.length;
        for (int i = 0; i < size; i++) {
            final int dirID = validDirIDs[i];
            final int toFieldID = dirs_ids[dirID][0];
            final int targetPID = figuresIDsPerFieldsIDs[toFieldID];
            if (targetPID == Constants.PID_NONE) {
                final long toBitboard = Fields.ALL_A1H1[toFieldID];
                if ((toBitboard & Promotioning.BLACK_PROMOTIONS) != NUMBER_0) {
                    list.reserved_add(MoveInt.createPromotion(fromFieldID, toFieldID, Constants.PID_B_QUEEN));
                    list.reserved_add(MoveInt.createPromotion(fromFieldID, toFieldID, Constants.PID_B_ROOK));
                    list.reserved_add(MoveInt.createPromotion(fromFieldID, toFieldID, Constants.PID_B_BISHOP));
                    list.reserved_add(MoveInt.createPromotion(fromFieldID, toFieldID, Constants.PID_B_KNIGHT));
                } else {
                }
                continue;
            } else {
                break;
            }
        }
    }
}
