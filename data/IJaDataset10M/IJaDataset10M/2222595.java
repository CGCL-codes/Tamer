package wood.model.effect;

import wood.model.entity.Entity;
import wood.model.map.Map.Tile;
import wood.model.tileobject.AbstractTileObjectVisitor;
import wood.model.tileobject.TileObject;
import wood.model.tileobject.TileObjectVisitor;

public abstract class Effect extends TileObject {

    public Effect(Tile tile) {
        super(tile);
    }

    private static final long serialVersionUID = 1572519364009986670L;

    public abstract void visitEntity(Entity e);

    public void dissipate() {
    }

    public abstract Entity getEntity();
}
