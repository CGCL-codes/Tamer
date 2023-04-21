package eu.fortaleza.object;

import java.io.IOException;
import org.lwjgl.opengl.GL11;
import eu.fortaleza.ResourceOpengLFactory;
import eu.fortaleza.interfaces.IGenericMapItem;
import eu.fortaleza.interfaces.ILocalMap;
import eu.fortaleza.interfaces.IsoConstants;
import eu.fortaleza.lwjgl.Texture;
import eu.fortaleza.lwjgl.TextureLoader.TextureFilter;

public class ColumnBasic implements IGenericMapItem, IsoConstants {

    protected Texture tex, tex2;

    public ColumnBasic() {
        if (tex == null) try {
            tex = ResourceOpengLFactory.getInstance().getTextureLoader().getTexture("sprites/castle/wall_side_with_border.png", TextureFilter.NearestFilteredTexture);
            tex2 = ResourceOpengLFactory.getInstance().getTextureLoader().getTexture("sprites/brik.png", TextureFilter.NearestFilteredTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(ILocalMap map, float xx, float yy, float zz, int x, int y) {
        GL11.glPushMatrix();
        GL11.glTranslatef(xx, yy, zz);
        float z = def_z * (def_column_multiply - 1);
        drawRoof(z);
        drawColumnBody(z);
        GL11.glPopMatrix();
    }

    protected void drawColumnBody(float z) {
        int x, y;
        float tx = 0;
        float ty = 0;
        float tx2 = 0;
        float ty2 = 0;
        tx2 = tex.getWidth();
        ty2 = tex.getHeight();
        tex.bind();
        int s = 0;
        GL11.glBegin(GL11.GL_QUADS);
        x = def_wit + s;
        y = def_hei + s;
        float z2 = 0;
        GL11.glNormal3f(1.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(tx2, ty);
        GL11.glVertex3f(x, 0, z);
        GL11.glTexCoord2f(tx2, ty2);
        GL11.glVertex3f(x, 0, z2);
        GL11.glTexCoord2f(tx, ty2);
        GL11.glVertex3f(x, y, z2);
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex3f(x, y, z);
        GL11.glNormal3f(-1.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex3f(0, 0, z);
        GL11.glTexCoord2f(tx2, ty);
        GL11.glVertex3f(0, y, z);
        GL11.glTexCoord2f(tx2, ty2);
        GL11.glVertex3f(0, y, z2);
        GL11.glTexCoord2f(tx, ty2);
        GL11.glVertex3f(0, 0, z2);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glTexCoord2f(tx, ty2);
        GL11.glVertex3f(0, y, z2);
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex3f(0, y, z);
        GL11.glTexCoord2f(tx2, ty);
        GL11.glVertex3f(x, y, z);
        GL11.glTexCoord2f(tx2, ty2);
        GL11.glVertex3f(x, y, z2);
        GL11.glNormal3f(0.0f, -1.0f, 0.0f);
        GL11.glTexCoord2f(tx2, ty2);
        GL11.glVertex3f(0, 0, z2);
        GL11.glTexCoord2f(tx, ty2);
        GL11.glVertex3f(x, 0, z2);
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex3f(x, 0, z);
        GL11.glTexCoord2f(tx2, ty);
        GL11.glVertex3f(0, 0, z);
        GL11.glEnd();
    }

    @Override
    public int getZ() {
        return def_column_multiply * def_z - def_z;
    }

    protected void drawRoof(float z) {
        tex2.bind();
        GL11.glBegin(GL11.GL_QUADS);
        int s = 2;
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(0, 0, z);
        GL11.glTexCoord2f(tex2.getWidth(), 0);
        GL11.glVertex3f(def_wit + s, 0, z);
        GL11.glTexCoord2f(tex2.getWidth(), tex2.getHeight());
        GL11.glVertex3f(def_wit + s, def_hei + s, z);
        GL11.glTexCoord2f(0, tex2.getHeight());
        GL11.glVertex3f(0, def_hei + s, z);
        GL11.glEnd();
    }
}
