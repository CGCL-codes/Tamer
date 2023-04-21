package game.database.map.vo;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class Map {

    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private int widthInTiles;

    private int heightInTiles;

    private int sizeTile;

    private int startTileHeroPosX;

    private int startTileHeroPosY;

    private int position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidthInTiles() {
        return widthInTiles;
    }

    public void setWidthInTiles(int widthInTiles) {
        this.widthInTiles = widthInTiles;
    }

    public int getHeightInTiles() {
        return heightInTiles;
    }

    public void setHeightInTiles(int heightInTiles) {
        this.heightInTiles = heightInTiles;
    }

    public int getSizeTile() {
        return sizeTile;
    }

    public void setSizeTile(int sizeTile) {
        this.sizeTile = sizeTile;
    }

    public int getStartTileHeroPosX() {
        return startTileHeroPosX;
    }

    public void setStartTileHeroPosX(int startTileHeroPosX) {
        this.startTileHeroPosX = startTileHeroPosX;
    }

    public int getStartTileHeroPosY() {
        return startTileHeroPosY;
    }

    public void setStartTileHeroPosY(int startTileHeroPosY) {
        this.startTileHeroPosY = startTileHeroPosY;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Id: " + getId() + " - Name: " + getName();
    }
}
