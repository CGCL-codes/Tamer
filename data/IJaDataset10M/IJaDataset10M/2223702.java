package bean;

import com.alibaba.fastjson.JSON;

public class Tile {

    private int id;

    private int row;

    private int col;

    private int index;

    private int tile_type;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getTile_type() {
        return tile_type;
    }

    public void setTile_type(int tile_type) {
        this.tile_type = tile_type;
    }
}
