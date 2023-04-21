package net.sourceforge.bucky.state;

public class SmashingMove extends Move {

    private Position to;

    public SmashingMove(Position from, Position to, Piece toPromoteTo, Position target) {
        super(from, target, toPromoteTo);
        this.to = to;
    }

    boolean conformsToRules(Board board) {
        if (null != board.pieceAt(target) && board.pieceAt(target).owner() == board.currentPlayer) return false;
        boolean motionIsAccepted = false;
        if (board.hasRole(from, Role.WHITE_VALIANT)) {
            int row_delta = target.row() - from.row();
            int col_distance = Math.abs(target.col() - from.col());
            if (row_delta == 1 && col_distance <= 1) motionIsAccepted = true;
        }
        if (board.hasRole(from, Role.BLACK_VALIANT)) {
            int row_delta = target.row() - from.row();
            int col_distance = Math.abs(target.col() - from.col());
            if (row_delta == -1 && col_distance <= 1) motionIsAccepted = true;
        }
        if (board.hasRole(from, Role.ZEALOUS)) {
            int row_distance = Math.abs(from.row() - to.row());
            int col_distance = Math.abs(from.col() - to.col());
            if (row_distance == 0 && col_distance != 0 || row_distance != 0 && col_distance == 0 || row_distance == col_distance) {
                int distance = Math.max(row_distance, col_distance);
                int row_direction = sign(to.row() - from.row());
                int col_direction = sign(to.col() - from.col());
                int current_row = from.row(), current_col = from.col();
                boolean all_clear = true;
                for (int i = 1; i <= distance; i++) {
                    current_row += row_direction;
                    current_col += col_direction;
                    if (null != board.squares[current_row][current_col]) {
                        all_clear = false;
                        break;
                    }
                }
                if (all_clear) {
                    current_row += row_direction;
                    current_col += col_direction;
                    if (current_row < 0 || current_row > 7 || current_col < 0 || current_col > 7 || null != board.squares[current_row][current_col]) {
                        motionIsAccepted = true;
                    }
                }
            }
        }
        return motionIsAccepted;
    }

    public Position to() {
        return to;
    }

    public Board makeMove(Board priorState) {
        return null;
    }
}
