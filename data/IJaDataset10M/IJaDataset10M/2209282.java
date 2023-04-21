package wood.model.decal;

import wood.model.map.Map.Tile;
import wood.model.tileobject.TileObject;
import wood.model.tileobject.TileObjectVisitor;

public class Decal extends TileObject {

    private static final long serialVersionUID = 8058754164732753954L;

    public Decal(Tile tile) {
        super(tile);
    }

    public void accept(TileObjectVisitor tileObjectVisitor) {
    }

    public boolean blocks(TileObject tileObject) {
        return tileObject instanceof Decal;
    }
}
