package net.lagerwey.dreambox.skin;

import net.lagerwey.dreambox.Utils;
import net.lagerwey.xml.XmlNode;

public class Screen extends SkinElement {

    public Screen(Skin skin, XmlNode node) {
        super(skin, node);
    }

    public int[] findPosition(int[] position) {
        int[] screenPosition = Utils.parseXY(this.getNode().getAttribute("position"));
        for (int i = 0; i < position.length; i++) {
            position[i] += screenPosition[i];
        }
        return position;
    }
}
