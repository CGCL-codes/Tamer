package org.lwjgl.opengl;

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLvoid;
import java.nio.IntBuffer;
import com.sun.mirror.type.PrimitiveType;

public interface AMD_multi_draw_indirect {

    void glMultiDrawArraysIndirectAMD(@GLenum int mode, @BufferObject(BufferKind.IndirectBO) @Check("4 * primcount") @NullTerminated @Const @GLvoid(PrimitiveType.Kind.INT) IntBuffer indirect, @GLsizei int primcount, @GLsizei int stride);

    void glMultiDrawElementsIndirectAMD(@GLenum int mode, @GLenum int type, @BufferObject(BufferKind.IndirectBO) @Check("5 * primcount") @NullTerminated @Const @GLvoid(PrimitiveType.Kind.INT) IntBuffer indirect, @GLsizei int primcount, @GLsizei int stride);
}
