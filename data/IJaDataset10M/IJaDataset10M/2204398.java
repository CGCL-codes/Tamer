package it.chesslab.chessboard;

/** 
 * @author Romano Ghetti 
 */
final class Path {

    /**  */
    private final int direction;

    /**  */
    private final Square[] squares;

    /**  */
    Path(int direction, Square[] squares) {
        this.direction = direction;
        this.squares = squares;
    }

    /**  */
    final int getDirection() {
        return this.direction;
    }

    /**  */
    final int getLength() {
        return this.squares.length;
    }

    /**  */
    final Square getSquare(int i) {
        return this.squares[i];
    }

    /**  */
    final Square getFirstPieceSquare() {
        for (int i = 0; i < this.squares.length; i++) {
            Square square = this.squares[i];
            if (!square.isEmpty()) {
                return square;
            }
        }
        return null;
    }

    /**  */
    final boolean isOpen(int toSquare) {
        for (int i = 0; i < this.squares.length; i++) {
            if (this.squares[i].getIndex() == toSquare) {
                return true;
            } else if (!this.squares[i].isEmpty()) {
                return false;
            }
        }
        return false;
    }

    /**  */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        for (int i = 0; i < this.squares.length; i++) {
            builder.append(this.squares[i]);
            if (i < this.squares.length - 1) {
                builder.append(',');
            }
        }
        builder.append(')');
        return builder.toString();
    }
}
