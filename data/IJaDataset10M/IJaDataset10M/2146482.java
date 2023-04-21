package serene.validation.schema.parsed.util;

import java.util.Arrays;

class LevelTop extends Level {

    LevelTop() {
        super();
        child = new LevelBottom(this);
    }

    public boolean isTopLevel() {
        return true;
    }

    public boolean isBottomLevel() {
        return false;
    }

    public Level getLevelUp() {
        throw new IllegalStateException();
    }

    public Level getLevelDown() {
        return child;
    }

    void setChild(Level child) {
        this.child = child;
    }

    public void clearAll() {
        child.clearAll();
        clear();
    }

    public String toString() {
        return "0 TOP parsedComponents " + Arrays.toString(parsedComponents) + child.toString(1);
    }

    String toString(int i) {
        return i + " TOP parsedComponents " + Arrays.toString(parsedComponents) + child.toString(i + 1);
    }
}
