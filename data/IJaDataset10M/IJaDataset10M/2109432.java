package graphics;

import game.Universe;
import java.io.File;
import javax.media.opengl.GL;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import common.Math3D;
import common.Point3D;

public class FacingTex extends GraphicalObject {

    Texture mtexture;

    double radius = 1;

    private String textureFile;

    @Override
    public void setParameter(String name, String args[]) {
        if (name.equals("file")) {
            textureFile = args[0];
        }
        if (name.equals("radius")) {
            this.setRadius(Double.parseDouble(args[0]));
        }
        super.setParameter(name, args);
    }

    @Override
    public void initialize(GL gl) {
        super.initialize(gl);
        try {
            File file;
            file = new File(textureFile);
            mtexture = TextureIO.newTexture(file, true);
        } catch (Exception e) {
            System.out.println("Error failed to read texture");
            e.printStackTrace();
        }
    }

    @Override
    public void prerender(GL gl) {
        super.prerender(gl);
        Point3D camera = Universe.getCamera().getPosition();
        Point3D face = Math3D.subPoint(camera, position.getCenter());
        Point3D up = new Point3D(0, 1, 0);
        Point3D left = Math3D.getXProduct(face, up);
        up = Math3D.getXProduct(face, left);
        face.normalize();
        up.normalize();
        left.normalize();
        position.getFace().copy(face);
        position.getUp().copy(left);
        position.getLeft().copy(up);
    }

    @Override
    public void render(GL gl) {
        gl.glPushAttrib(GL.GL_ENABLE_BIT);
        gl.glDisable(GL.GL_LIGHTING);
        super.render(gl);
        gl.glPopAttrib();
    }

    @Override
    public void postrender(GL gl) {
        gl.glPushAttrib(GL.GL_ENABLE_BIT);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glDisable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_BLEND);
        super.postrender(gl);
        gl.glColor3d(1, 1, 1);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        mtexture.enable();
        mtexture.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-0.5f, 0.5f, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(0.5f, 0.5f, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(0.5f, -0.5f, 0);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-0.5f, -0.5f, 0);
        gl.glEnd();
        mtexture.disable();
        gl.glPopAttrib();
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
