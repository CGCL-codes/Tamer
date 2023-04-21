package gestalt.candidates.rendertotexture;

import javax.media.opengl.GL;
import gestalt.Gestalt;

public class BufferInfo {

    public static final int SECONDARY = 0;

    public static final int TERTIARY = 1;

    public static final int QUATERNARY = 2;

    public int framebuffer_object = 0;

    public int renderbuffer_depth = 0;

    public int renderbuffer_stencil = Gestalt.UNDEFINED;

    public int renderbuffer_color = Gestalt.UNDEFINED;

    public int framebuffer_object_MULTISAMPLE = Gestalt.UNDEFINED;

    public int texture = 0;

    public int attachment_point = GL.GL_COLOR_ATTACHMENT0_EXT;

    public int[] additional_textures = null;

    public int[] additional_attachment_points = null;

    public static BufferInfo getBufferInfoMultipleTexture(int theNumberOfAdditionalTexture) {
        final BufferInfo myBufferInfo = new BufferInfo();
        myBufferInfo.additional_textures = new int[theNumberOfAdditionalTexture];
        myBufferInfo.additional_attachment_points = new int[theNumberOfAdditionalTexture];
        for (int i = 0; i < myBufferInfo.additional_textures.length; i++) {
            myBufferInfo.additional_textures[i] = Gestalt.UNDEFINED;
            myBufferInfo.additional_attachment_points[i] = GL.GL_COLOR_ATTACHMENT1_EXT + i;
        }
        return myBufferInfo;
    }
}
