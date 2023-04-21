package virtuallab;

public class Tile {

    private int row;

    private int col;

    private java.awt.Point position;

    private String filePath;

    public Tile(int row, int col, int x, int y) {
        this.row = row;
        this.col = col;
        this.position = new java.awt.Point(x, y);
        this.filePath = Integer.toString(this.row) + "/" + Integer.toString(this.row) + "_" + Integer.toString(this.col) + ".jpg";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public java.awt.Point getPosition() {
        return position;
    }

    public String getFilePath() {
        return filePath;
    }
}
