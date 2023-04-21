package org.lwjgl.opengl;

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.*;
import java.nio.*;

public interface ATI_element_array {

    int GL_ELEMENT_ARRAY_ATI = 0x8768;

    int GL_ELEMENT_ARRAY_TYPE_ATI = 0x8769;

    int GL_ELEMENT_ARRAY_POINTER_ATI = 0x876A;

    void glElementPointerATI(@AutoType("pPointer") @GLenum int type, @Check @Const @GLubyte @GLushort @GLuint Buffer pPointer);

    void glDrawElementArrayATI(@GLenum int mode, @GLsizei int count);

    void glDrawRangeElementArrayATI(@GLenum int mode, @GLuint int start, @GLuint int end, @GLsizei int count);
}
