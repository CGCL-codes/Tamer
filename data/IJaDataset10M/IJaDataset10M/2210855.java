package gamePlay;

import java.io.FileInputStream;
import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class LevelMain {

    PBMLoader pbmLoader = new PBMLoader();

    TXTLoader txtLoader = new TXTLoader();

    boolean loadLevel = true;

    boolean drawLevel = true;

    int data[][];

    int[] tPos;

    Texture back = null;

    public int[][] loadLevel(int levelNr) {
        try {
            back = TextureLoader.getTexture("PNG", new FileInputStream("./DATA/Levels/Level" + levelNr + "/Level_" + levelNr + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        data = pbmLoader.read("./DATA/Levels/Level" + levelNr + "/Level_Structure_" + levelNr + ".pbm");
        tPos = txtLoader.getStartPosition("./DATA/Levels/Level" + levelNr + "/Start_Point_" + levelNr + ".txt");
        loadLevel = false;
        return data;
    }

    public void drawLevel() {
        Color.white.bind();
        back.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(1024, 0);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(1024, 1024);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(0, 1024);
        GL11.glEnd();
    }
}
