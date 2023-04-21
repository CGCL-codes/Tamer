package csel.model.areaeffect;

import csel.model.GameGlobals;
import csel.model.entity.Entity;

public interface AreaEffect extends java.io.Serializable {

    public void attach(GameGlobals gb);

    public void enter(Entity e);

    public void leave(Entity e);
}
