package graphics;

import static javax.media.opengl.GL.*;
import java.io.*;
import javax.media.opengl.*;
import com.sun.opengl.util.texture.*;

public class SkyBox implements Resource {

    private Texture[] textures = new Texture[6];

    /**
     * Rendert die SkyBox
     * 
     * Beim Rendern einer SkyBox sollte die Beleuchtung ausgeschaltet sein. Es
     * wird die Cube-Klasse verwendet, da sich der Betrachter jedoch
     * normalerweise innerhalb der SkyBox befindet, sind die Texturen gegenueber
     * einem normalen Wuerfel spiegelverkehrt.
     * 
     * @param pipeline
     */
    public void render(GL pipeline) {
        textures[0].bind();
        pipeline.glBegin(GL_QUADS);
        Cube.bareTop(pipeline);
        pipeline.glEnd();
        textures[1].bind();
        pipeline.glBegin(GL_QUADS);
        Cube.bareBottom(pipeline);
        pipeline.glEnd();
        textures[2].bind();
        pipeline.glBegin(GL_QUADS);
        Cube.bareSide1(pipeline);
        pipeline.glEnd();
        textures[3].bind();
        pipeline.glBegin(GL_QUADS);
        Cube.bareSide2(pipeline);
        pipeline.glEnd();
        textures[4].bind();
        pipeline.glBegin(GL_QUADS);
        Cube.bareSide3(pipeline);
        pipeline.glEnd();
        textures[5].bind();
        pipeline.glBegin(GL_QUADS);
        Cube.bareSide4(pipeline);
        pipeline.glEnd();
    }

    public void cleanup() {
    }

    public void initialize() {
        try {
            for (int i = 0; i < textures.length; i++) {
                textures[i] = TextureIO.newTexture(new File("terragen/gebirge" + (i + 1) + ".png"), false);
                textures[i].enable();
            }
        } catch (GLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
