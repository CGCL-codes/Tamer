package CB_Core.Solver;

import java.util.ArrayList;

public class Entity {

    protected int Id;

    protected boolean IsLinks;

    public Entity(int id) {
        this.Id = id;
        IsLinks = false;
    }

    public void ReplaceTemp(Entity source, Entity dest) {
    }

    public void GetAllEntities(ArrayList<Entity> list) {
    }

    public String Berechne() {
        return "";
    }

    public String ToString() {
        return "";
    }
}
