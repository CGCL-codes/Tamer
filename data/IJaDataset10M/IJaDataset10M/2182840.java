package net.sourceforge.jarcassone.game;

/**
 *
 * @author Clemens Alexander Schulz
 */
public class Board {

    private PlayingField[][] field;

    public Board() {
        field = new PlayingField[11][11];
        initBoard();
    }

    public PlayingField[][] getField() {
        return field;
    }

    public void setField(int x, int y, PlayingField newField) {
        if (newField != null) {
            if (x < 11 && x >= 0 && y < 11 && y >= 0) {
                if (field[x][y] == null) {
                    field[x][y] = newField;
                } else {
                    throw new IllegalArgumentException("You can only place a new field on an empty field!");
                }
            } else {
                throw new IllegalArgumentException("Specify valid coordinates!");
            }
        } else {
            throw new IllegalArgumentException("Specify a valid new field!");
        }
    }

    private void initBoard() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                field[i][j] = new PlayingField();
            }
        }
        field[5][5] = new PlayingField(PlayingFieldType.city, PlayingFieldType.street, PlayingFieldType.grassland, PlayingFieldType.street, false, false);
    }
}
