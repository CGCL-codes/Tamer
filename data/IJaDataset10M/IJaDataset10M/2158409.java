package enlishem;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.devil.IL;
import org.lwjgl.devil.ILU;
import org.lwjgl.devil.ILUT;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.GL;

public class Texture {

    private int width;

    private int height;

    private int imageID;

    private int glID;

    public Texture(int imageID) {
        this.imageID = imageID;
        glID = ILUT.ilutGLBindTexImage();
        width = 32;
        height = 32;
    }

    public Texture(String fileName) {
        IntBuffer buf = BufferUtils.createIntBuffer(1);
        IL.ilGenImages(buf);
        imageID = buf.get();
        IL.ilBindImage(imageID);
        IL.ilLoadImage(fileName);
        ILU.iluFlipImage();
        glID = ILUT.ilutGLBindTexImage();
        width = 32;
        height = 32;
    }

    public void delete() {
        IntBuffer buf = BufferUtils.createIntBuffer(1);
        buf.put(imageID);
        IL.ilDeleteImages(buf);
    }

    public Texture mirrorTexture() {
        IL.ilBindImage(IL.ilCloneCurImage());
        ILU.iluMirror();
        return new Texture(IL.ilGetInteger(IL.IL_ACTIVE_IMAGE));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getGLID() {
        return glID;
    }

    public void bind() {
        GL.glBindTexture(GL11.GL_TEXTURE_2D, glID);
    }
}
