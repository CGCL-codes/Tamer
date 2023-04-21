package carregadorOBJ;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.media.opengl.GL;
import org.jouvieje.math.Vector3f;
import org.jouvieje.opengl.IGL;
import org.jouvieje.opengl.IGLApi;

public class ModelLoaderOBJ {

    protected GLModel model1;

    public void init(IGLApi glApi, String path, String caminhoCompleto, boolean masked) {
        IGL gl = glApi.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glShadeModel(GL.GL_SMOOTH);
        String OBJpath = path + ".obj";
        String MTLpath = path + ".mtl";
        try {
            FileInputStream r_path1 = new FileInputStream(OBJpath);
            BufferedReader b_read1 = new BufferedReader(new InputStreamReader(r_path1));
            model1 = new GLModel(b_read1, true, MTLpath, glApi, caminhoCompleto, masked);
            r_path1.close();
            b_read1.close();
        } catch (Exception e) {
            System.out.println("LOADING ERROR" + e);
        }
    }

    public void draw(IGL gl, Vector3f cor) {
        gl.glPushMatrix();
        model1.opengldraw(gl, cor);
        gl.glPopMatrix();
    }
}
