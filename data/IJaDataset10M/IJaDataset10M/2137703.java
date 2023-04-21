package sequime.AlignmentBuilder;

/**
 * 
 * @author Charlotta Schaerfe, Fabian Aicheler
 *
 * compute local alignment with Smith-Waterman-algorithm
 * 
 * S_(i,j) = maximum(S_(i-1,j) + gapPenalty, 
 * 					 S_(i,j-1) + gapPenalty, 
 * 					 S_(i-1,j-1) + matchScore or mismatchScore,
 * 					 0)
 * 
 * Score: maximum of all S_(i,j)
 *
 */
public class SmithWaterman extends SmithWatermanInit {

    int[][] mat = null;

    private void traceback(int i, int j) {
        int pos = mat[i][j];
        char a = getSequence1().charAt(i);
        char b = getSequence2().charAt(j);
        if ((i == 0) && (j == 0)) {
            return;
        }
        if ((i >= 1) && (j >= 1)) if (mat[i - 1][j - 1] + match(a, b) == pos) {
            setAligned1(a + getAligned1());
            setAligned2(b + getAligned2());
            if (mat[i - 1][j - 1] == 0) {
                setBegin1(i);
                setBegin2(j);
                return;
            }
            traceback(i - 1, j - 1);
            return;
        }
        if (j >= 1) if (mat[i][j - 1] + getGapPenalty() == pos) {
            setAligned1("-" + getAligned1());
            setAligned2(b + getAligned2());
            if (mat[i][j - 1] == 0) {
                setBegin1(i);
                setBegin2(j);
                return;
            }
            traceback(i, j - 1);
            return;
        }
        if (i >= 1) if (mat[i - 1][j] + getGapPenalty() == pos) {
            setAligned1(a + getAligned1());
            setAligned2("-" + getAligned2());
            if (mat[i - 1][j] == 0) {
                setBegin1(i);
                setBegin2(j);
                return;
            }
            traceback(i - 1, j);
        }
    }

    private void traceback(int i, int j, SMatrix matrix) {
        int pos = mat[i][j];
        char a = getSequence1().charAt(i);
        char b = getSequence2().charAt(j);
        if ((i == 0) && (j == 0)) {
            return;
        }
        if ((i >= 1) && (j >= 1)) if (mat[i - 1][j - 1] + subMatch(a, b, matrix) == pos) {
            setAligned1(a + getAligned1());
            setAligned2(b + getAligned2());
            if (mat[i - 1][j - 1] == 0) {
                setBegin1(i);
                setBegin2(j);
                return;
            }
            traceback(i - 1, j - 1, matrix);
            return;
        }
        if (j >= 1) if (mat[i][j - 1] + getGapPenalty() == pos) {
            setAligned1("-" + getAligned1());
            setAligned2(b + getAligned2());
            if (mat[i][j - 1] == 0) {
                setBegin1(i);
                setBegin2(j);
                return;
            }
            traceback(i, j - 1, matrix);
            return;
        }
        if (i >= 1) if (mat[i - 1][j] + getGapPenalty() == pos) {
            setAligned1(a + getAligned1());
            setAligned2("-" + getAligned2());
            if (mat[i - 1][j] == 0) {
                setBegin1(i);
                setBegin2(j);
                return;
            }
            traceback(i - 1, j, matrix);
        }
    }

    /**
	 * compute a local alignment using the Smith-Waterman-algorithm
	 * fill computation matrix, get score and all alignments via traceback
	 */
    public void compute() {
        setSequence1("-" + getSequence1());
        setSequence2("-" + getSequence2());
        String seq1 = getSequence1();
        String seq2 = getSequence2();
        mat = new int[seq1.length()][seq2.length()];
        for (int i = 0; i < seq1.length(); i++) {
            mat[i][0] = 0;
        }
        for (int j = 0; j < seq2.length(); j++) {
            mat[0][j] = 0;
        }
        for (int j = 1; j < seq2.length(); j++) {
            for (int i = 1; i < seq1.length(); i++) {
                mat[i][j] = maximum(mat[i - 1][j - 1] + match(seq1.charAt(i), seq2.charAt(j)), mat[i - 1][j] + getGapPenalty(), mat[i][j - 1] + getGapPenalty(), 0);
                if (mat[i][j] >= getScore()) {
                    setScore(mat[i][j]);
                    setEnd1(i);
                    setEnd2(j);
                }
            }
        }
        setScore(mat[getEnd1()][getEnd2()]);
        setAligned1("");
        setAligned2("");
        traceback(getEnd1(), getEnd2());
    }

    /**
	 * default constructor
	 */
    public SmithWaterman() {
        super();
    }

    /**
	 * overloaded constructor
	 * @param seq1, seq2 the sequences to align
	 */
    public SmithWaterman(String seq1, String seq2) {
        super();
        setSequence1(seq1);
        setSequence2(seq2);
    }

    @Override
    public void compute(SMatrix matrix) {
        setSequence1("-" + getSequence1());
        setSequence2("-" + getSequence2());
        String seq1 = getSequence1();
        String seq2 = getSequence2();
        mat = new int[seq1.length()][seq2.length()];
        for (int i = 0; i < seq1.length(); i++) {
            mat[i][0] = 0;
        }
        for (int j = 0; j < seq2.length(); j++) {
            mat[0][j] = 0;
        }
        for (int j = 1; j < seq2.length(); j++) {
            for (int i = 1; i < seq1.length(); i++) {
                mat[i][j] = maximum(mat[i - 1][j - 1] + subMatch(seq1.charAt(i), seq2.charAt(j), matrix), mat[i - 1][j] + getGapPenalty(), mat[i][j - 1] + getGapPenalty(), 0);
                if (mat[i][j] >= getScore()) {
                    setScore(mat[i][j]);
                    setEnd1(i);
                    setEnd2(j);
                }
            }
        }
        setScore(mat[getEnd1()][getEnd2()]);
        setAligned1("");
        setAligned2("");
        traceback(getEnd1(), getEnd2(), matrix);
    }
}
