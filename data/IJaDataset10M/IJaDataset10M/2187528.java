package ie.tippinst.mjp;

public class EliminationSolver implements Solver {

    @Override
    public int Solve(int unsolvedCells) {
        for (int c = 0; c < MyPuzzle.width; c++) {
            for (int r = 0; r < MyPuzzle.height; r++) {
                if (MyPuzzle[c][r].isClue() || MyPuzzle[c][r].isSolved()) break;
                for (int i = 0; i < MyPuzzle.height; i++) {
                    if (MyPuzzle[c][i].value != 0) MyPuzzle[c][r].remPossible(MyPuzzle[c][i].getValue());
                }
            }
            for (int j = 0; j < MyPuzzle.height; j++) {
                if (MyPuzzle[j][r].value != 0) MyPuzzle[c][r].remPossible(MyPuzzle[j][r].getValue());
            }
            int cStart = c - (c % MyPuzzle.bWidth);
            int rStart = r - (r % MyPuzzle.bHeight);
            for (int x = c; x < (c + MyPuzzle.bWidth); x++) {
                for (int y = r; y < (r + MyPuzzle.bHeight); y++) {
                    if (MyPuzzle[x][y].value != 0) MyPuzzle[c][r].remPossible(MyPuzzle[x][y].getValue());
                }
            }
            if (MyPuzzle[c][r].possibles.size() == 1) {
                MyPuzzle[c][r].setValue();
                MyPuzzle[c][r].setSolved(true);
                unsolvedCells--;
            }
        }
        return unsolvedCells;
    }
}
