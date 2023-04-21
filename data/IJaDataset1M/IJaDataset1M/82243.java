package pelletQuest.resources;

import org.newdawn.slick.*;

public class SequenceData {

    private String name;

    private String sheet;

    private int u, v;

    private int length;

    public SequenceData(String name, String sheet, int u, int v, int length) {
        this.name = name;
        this.sheet = sheet;
        this.u = u;
        this.v = v;
        this.length = length;
    }

    public String getSetName() {
        return name;
    }

    public Image getImage(int index) {
        int increment = index;
        if (index >= length) {
            increment = length - 1;
        }
        return GraphicsManager.getSprite(sheet, u + increment, v);
    }

    public int getLength() {
        return length;
    }
}
