package jelb.common;

import java.util.ArrayList;

public class ActorList extends ArrayList<Mob> {

    private static final long serialVersionUID = 1L;

    public ActorList() {
        super();
    }

    public Mob getMob(int id) {
        for (Mob a : this) {
            if (a.getId().toInt() == id) {
                return a;
            }
        }
        return null;
    }

    public Actor getActor(String name) {
        for (Mob a : this) {
            if (a instanceof Actor && name.equalsIgnoreCase(((Actor) a).getName())) {
                return (Actor) a;
            }
        }
        return null;
    }

    public String toString() {
        String s = "";
        for (Mob a : this) {
            s += a.toString() + " ";
        }
        return s;
    }
}
