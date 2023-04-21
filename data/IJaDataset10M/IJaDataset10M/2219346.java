package marten.age.widget.obsolete;

import java.awt.Font;
import java.util.HashMap;
import java.util.Set;
import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import org.lwjgl.opengl.GL11;

public class DebugBox extends BasicSceneGraphChild {

    private HashMap<String, Object> objects = new HashMap<String, Object>();

    private static final long serialVersionUID = 1L;

    BitmapFont font = FontCache.getFont(new Font("Courier New", Font.BOLD, 14));

    public void addObject(String key, Object o) {
        objects.put(key, o);
    }

    public void removeObject(String key) {
        objects.remove(key);
    }

    public void render() {
        GL11.glPushMatrix();
        Set<String> keys = objects.keySet();
        for (String key : keys) {
            Object o = objects.get(key);
            BitmapString string = new BitmapString(font, key + ": " + o.toString());
            string.render();
            GL11.glTranslated(0.0, font.getSize(), 0.0);
        }
        GL11.glPopMatrix();
    }
}
