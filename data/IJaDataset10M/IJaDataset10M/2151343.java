package PuzzlePieces;

public class SudokuRow extends SudokuLine {

    private SudokuRow leftRow;

    private SudokuRow rightRow;

    public SudokuRow(int row) {
        super(row);
    }

    public void setLeftRow(SudokuRow left) {
        leftRow = left;
    }

    public void setRightRow(SudokuRow right) {
        rightRow = right;
    }

    public SudokuRow getLeftRow() {
        return leftRow;
    }

    public SudokuRow getRightRow() {
        return rightRow;
    }
}
