package axiom.gui;

import org.lwjgl.opengl.GL11;
import axiom.util.Texture;

public class Message extends GUI_Object {

    public short x;

    public short y;

    public String text;

    public short wid;

    public short hei;

    float r, g, b, a;

    public Message() {
    }

    public void setColor(float r1, float g1, float b1, float a1) {
        r = 0;
        g = 0;
        b = 0;
        a = 1;
        r = r1;
        g = g1;
        b = b1;
        a = a1;
    }

    public Message(String data, int posx, int posy) {
        r = 0;
        g = 0;
        b = 0;
        a = 1;
        hei = 1;
        wid = 0;
        x = (short) posx;
        y = (short) posy;
        text = data;
        boolean scan = false;
        short longest_line = 0;
        int bound = 0;
        int newbound;
        while (!scan) {
            newbound = text.indexOf('\n', bound);
            if (newbound == -1) {
                if ((text.length() - bound) > longest_line) longest_line = (short) (text.length() - bound);
                scan = true;
            } else {
                if ((newbound - bound) > longest_line) longest_line = (short) (newbound - bound);
                hei++;
            }
            bound = newbound + 1;
        }
        wid = longest_line;
        x1 = x;
        y1 = y;
        x2 = wid * Font.font_w;
        y2 = hei * Font.font_h;
    }

    public void render() {
        Texture.Apply(Font.font_skin);
        GL11.glPushMatrix();
        GL11.glTranslatef(x1, y1, 0);
        GL11.glColor4f(r, g, b, a);
        for (int j = 0; j < text.length(); j++) {
            char curr = text.charAt(j);
            if (curr != '\n') {
                GL11.glTranslatef(Font.font_w, 0, 0);
                GL11.glCallList(Font.FList + (int) curr);
            }
        }
        GL11.glPopMatrix();
    }
}
