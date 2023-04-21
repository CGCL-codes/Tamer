package panoramic.geometry;

import panoramic.textures.DefaultTextures;
import panoramic.textures.TextureManager;
import panoramic.IRenderable;
import rctgl.park.RCTGLStatics;
import java.util.*;
import javax.media.opengl.GL;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;

public class PanoPolygon implements IRenderable {

    private int m_textureID = DefaultTextures.UNDEFINED;

    private List<PanoVertex2> m_textureCoords = new ArrayList<PanoVertex2>();

    private List<PanoVertex3> m_spaceCoords = new ArrayList<PanoVertex3>();

    private PanoRGB m_rgb = null;

    private PanoRGBA m_rgba = null;

    private boolean m_needsAlpha = false;

    public void setNeedsAlpha(boolean val) {
        m_needsAlpha = val;
    }

    public void setTextureID(int texID) {
        m_textureID = texID;
    }

    public boolean isTextureDefined() {
        return (m_textureID != DefaultTextures.UNDEFINED);
    }

    public void setRGB(PanoRGB rgb) {
        m_rgba = null;
        m_rgb = new PanoRGB();
        m_rgb = rgb;
    }

    public void setRGBA(PanoRGBA rgba) {
        m_rgb = null;
        m_rgba = new PanoRGBA();
        m_rgba = rgba;
    }

    public void addVertex(PanoVertex3 spaceCoord) {
        m_spaceCoords.add(spaceCoord);
    }

    public void addVertex(PanoVertex3 spaceCoord, PanoVertex2 texCoord) {
        m_textureCoords.add(texCoord);
        m_spaceCoords.add(spaceCoord);
    }

    public void render(GL gl) {
        TextureManager texMan = TextureManager.getInstance();
        Texture tex = null;
        tex = texMan.getTexture(m_textureID);
        if (tex != null) {
            if (m_needsAlpha) {
                gl.glDisable(GL.GL_BLEND);
                gl.glEnable(GL.GL_ALPHA_TEST);
                gl.glAlphaFunc(GL.GL_LESS, 0.4f);
            } else {
                gl.glEnable(GL.GL_BLEND);
                gl.glAlphaFunc(GL.GL_GEQUAL, 1.0f);
            }
            tex.bind();
            TextureCoords tc = tex.getImageTexCoords();
            if (m_rgb != null) {
                gl.glColor4d(m_rgb.getR(), m_rgb.getG(), m_rgb.getB(), 1.0d);
            } else if (m_rgba != null) {
                gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
                gl.glAlphaFunc(GL.GL_GREATER, 0.0f);
                gl.glColor4d(m_rgba.getR(), m_rgba.getG(), m_rgba.getB(), m_rgba.getA());
            }
            gl.glBegin(gl.GL_QUADS);
            for (int i = 0; i < m_spaceCoords.size(); i++) {
                PanoVertex3 vx = m_spaceCoords.get(i);
                PanoVertex2 tx = m_textureCoords.get(i);
                gl.glTexCoord2d(tx.getX(), tx.getY());
                gl.glVertex3d(vx.getX(), vx.getY(), vx.getZ());
            }
            if (m_needsAlpha) {
                gl.glEnable(GL.GL_BLEND);
                gl.glDisable(GL.GL_ALPHA_TEST);
            }
        } else {
            gl.glColor3d(m_rgb.getR(), m_rgb.getG(), m_rgb.getB());
            gl.glBegin(gl.GL_QUADS);
            for (int i = 0; i < m_spaceCoords.size(); i++) {
                PanoVertex3 vx = m_spaceCoords.get(i);
                gl.glVertex3d(vx.getX(), vx.getY(), vx.getZ());
            }
        }
        gl.glEnd();
    }
}
