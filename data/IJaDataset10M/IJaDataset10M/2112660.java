package wood.view.draw;

public class DrawableID {

    private final String name;

    private final int id;

    public DrawableID(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        boolean ret = false;
        if (this == o) ret = true; else if (o != null && o instanceof DrawableID) {
            DrawableID dID = (DrawableID) o;
            ret = name.equals(dID.name) && id == dID.id;
        }
        return ret;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return getName() + " " + getID();
    }
}
