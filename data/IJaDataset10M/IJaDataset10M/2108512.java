package org.lwjgl.opengl;

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.*;
import java.nio.*;

public interface EXT_draw_range_elements {

    int GL_MAX_ELEMENTS_VERTICES_EXT = 0x80E8;

    int GL_MAX_ELEMENTS_INDICES_EXT = 0x80E9;

    void glDrawRangeElementsEXT(@GLenum int mode, @GLuint int start, @GLuint int end, @AutoSize("pIndices") @GLsizei int count, @AutoType("pIndices") @GLenum int type, @BufferObject(BufferKind.ElementVBO) @Const @GLubyte @GLushort @GLuint Buffer pIndices);
}
