package bagaturchess.bitboard.impl.plies.checking;

import bagaturchess.bitboard.impl.Bits;
import bagaturchess.bitboard.impl.plies.OfficerPlies;

public class OfficerChecks extends OfficerPlies {

    public static int[][][] CHECK_MIDDLE_FIELDS_IDS = new int[Bits.PRIME_67][Bits.PRIME_67][];

    public static int[][][] CHECK_MIDDLE_FIELDS_DIR_ID = new int[Bits.PRIME_67][Bits.PRIME_67][];

    public static int[][][] CHECK_MIDDLE_FIELDS_SEQS = new int[Bits.PRIME_67][Bits.PRIME_67][];

    public static long[][][] CHECK_MIDDLE_FIELDS_BITBOARDS = new long[Bits.PRIME_67][Bits.PRIME_67][];

    public static long[][][] FIELDS_PATH1 = new long[Bits.PRIME_67][Bits.PRIME_67][];

    public static long[][][] FIELDS_PATH2 = new long[Bits.PRIME_67][Bits.PRIME_67][];

    public static long[][][] FIELDS_WHOLE_PATH = new long[Bits.PRIME_67][Bits.PRIME_67][];

    static {
        genAll_Dynamic();
    }

    public static void initPair_Dynamic(int fromFieldID, int toFieldID) {
        final int[] fromFieldValidDirIDs = ALL_OFFICER_VALID_DIRS[fromFieldID];
        final int[][] middleFieldIDs = ALL_OFFICER_DIRS_WITH_FIELD_IDS[fromFieldID];
        long[][] middleFieldBitboards = ALL_OFFICER_DIRS_WITH_BITBOARDS[fromFieldID];
        final int size1 = fromFieldValidDirIDs.length;
        for (int i = 0; i < size1; i++) {
            int dirID1 = fromFieldValidDirIDs[i];
            int[] middleFieldIDsByDir = middleFieldIDs[dirID1];
            long[] middleFieldBitboardsByDir = middleFieldBitboards[dirID1];
            long path1 = 0L;
            for (int seq1 = 0; seq1 < middleFieldIDsByDir.length; seq1++) {
                int middleFieldID = middleFieldIDsByDir[seq1];
                long middleFieldBitboard = middleFieldBitboardsByDir[seq1];
                final int[] middleFieldValidDirIDs = ALL_OFFICER_VALID_DIRS[middleFieldID];
                final int[][] toFieldIDs = ALL_OFFICER_DIRS_WITH_FIELD_IDS[middleFieldID];
                final long[][] toFieldBitboards = ALL_OFFICER_DIRS_WITH_BITBOARDS[middleFieldID];
                final int size2 = middleFieldValidDirIDs.length;
                for (int j = 0; j < size2; j++) {
                    int dirID2 = middleFieldValidDirIDs[j];
                    int[] cur_toFieldIDsByDir = toFieldIDs[dirID2];
                    long[] endFieldBitboardsByDir = toFieldBitboards[dirID2];
                    long path2 = 0L;
                    for (int seq2 = 0; seq2 < cur_toFieldIDsByDir.length; seq2++) {
                        int cur_toFieldID = cur_toFieldIDsByDir[seq2];
                        long endFieldBitboard = endFieldBitboardsByDir[seq2];
                        if ((path1 & endFieldBitboard) != 0L || (cur_toFieldID == fromFieldID)) {
                            break;
                        }
                        if (toFieldID == cur_toFieldID) {
                            if (CHECK_MIDDLE_FIELDS_IDS[fromFieldID][toFieldID] == null || CHECK_MIDDLE_FIELDS_IDS[fromFieldID][toFieldID].length <= 5) {
                                CHECK_MIDDLE_FIELDS_IDS[fromFieldID][toFieldID] = extendArray(CHECK_MIDDLE_FIELDS_IDS[fromFieldID][toFieldID], middleFieldID);
                                CHECK_MIDDLE_FIELDS_DIR_ID[fromFieldID][toFieldID] = extendArray(CHECK_MIDDLE_FIELDS_DIR_ID[fromFieldID][toFieldID], dirID1);
                                CHECK_MIDDLE_FIELDS_SEQS[fromFieldID][toFieldID] = extendArray(CHECK_MIDDLE_FIELDS_SEQS[fromFieldID][toFieldID], seq1);
                                CHECK_MIDDLE_FIELDS_BITBOARDS[fromFieldID][toFieldID] = extendArray(CHECK_MIDDLE_FIELDS_BITBOARDS[fromFieldID][toFieldID], middleFieldBitboard);
                                if (PATHS[fromFieldID][middleFieldID] != path1) {
                                    throw new IllegalStateException();
                                }
                                if (PATHS[middleFieldID][toFieldID] != path2) {
                                    throw new IllegalStateException();
                                }
                                FIELDS_PATH1[fromFieldID][toFieldID] = extendArray(FIELDS_PATH1[fromFieldID][toFieldID], path1);
                                FIELDS_PATH2[fromFieldID][toFieldID] = extendArray(FIELDS_PATH2[fromFieldID][toFieldID], path2);
                                FIELDS_WHOLE_PATH[fromFieldID][toFieldID] = extendArray(FIELDS_WHOLE_PATH[fromFieldID][toFieldID], path1 | path2);
                            } else {
                                throw new IllegalStateException();
                            }
                            break;
                        } else {
                            path2 |= endFieldBitboard;
                        }
                    }
                }
                path1 |= middleFieldBitboard;
            }
        }
    }

    private static long[] extendArray(long[] source, long el) {
        long[] result = null;
        if (source != null) {
            result = new long[source.length + 1];
            System.arraycopy(source, 0, result, 0, source.length);
            result[source.length] = el;
        } else {
            result = new long[1];
            result[0] = el;
        }
        return result;
    }

    private static int[] extendArray(int[] source, int el) {
        int[] result = null;
        if (source != null) {
            result = new int[source.length + 1];
            System.arraycopy(source, 0, result, 0, source.length);
            result[source.length] = el;
        } else {
            result = new int[1];
            result[0] = el;
        }
        return result;
    }

    public static void genAll_Dynamic() {
        for (int i = 0; i < ALL_ORDERED_A1H1.length; i++) {
            int fromID = get67IDByBitboard(ALL_ORDERED_A1H1[i]);
            for (int j = 0; j < ALL_ORDERED_A1H1.length; j++) {
                int toID = get67IDByBitboard(ALL_ORDERED_A1H1[j]);
                if (fromID != toID) {
                    initPair_Dynamic(fromID, toID);
                }
            }
        }
    }

    public static String testChecks(int fromFieldID, int toFieldID) {
        String result = "Officer checks from " + getFieldSign_UC(fromFieldID) + " to " + getFieldSign_UC(toFieldID) + " -> ";
        int[] fields = CHECK_MIDDLE_FIELDS_IDS[fromFieldID][toFieldID];
        long[] fieldBoards = CHECK_MIDDLE_FIELDS_BITBOARDS[fromFieldID][toFieldID];
        if (fields == null && fieldBoards != null) {
            throw new IllegalStateException();
        } else if (fields != null && fieldBoards == null) {
            throw new IllegalStateException();
        } else if (fields != null) {
            if (fields.length != fieldBoards.length) {
                throw new IllegalStateException();
            } else {
                for (int i = 0; i < fields.length; i++) {
                    if (ALL_A1H1[fields[i]] != fieldBoards[i]) {
                        throw new IllegalStateException("\r\n" + Bits.toBinaryStringMatrix(fieldBoards[i]));
                    }
                }
            }
        }
        if (fields == null) {
            result += "NO";
        } else if (fields.length > 6) {
            throw new IllegalStateException();
        } else {
            for (int i = 0; i < fields.length; i++) {
                result += getFieldSign_UC(fields[i]) + ", ";
            }
        }
        if (fields != null) {
            result += "\r\n";
            for (int i = 0; i < fields.length; i++) {
                result += Bits.toBinaryStringMatrix(FIELDS_WHOLE_PATH[fromFieldID][toFieldID][i]) + "\r\n";
            }
        }
        return result;
    }

    public static void testAll() {
        for (int i = 0; i < ALL_ORDERED_A1H1.length; i++) {
            int fromID = get67IDByBitboard(ALL_ORDERED_A1H1[i]);
            for (int j = 0; j < ALL_ORDERED_A1H1.length; j++) {
                int toID = get67IDByBitboard(ALL_ORDERED_A1H1[j]);
                System.out.println(testChecks(fromID, toID));
            }
        }
    }

    public static void main(String[] args) {
        int from = get67IDByBitboard(F5);
        int to = get67IDByBitboard(E8);
        System.out.println(testChecks(from, to));
    }
}
