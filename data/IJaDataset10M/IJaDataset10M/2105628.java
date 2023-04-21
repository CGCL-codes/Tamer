package org.lwjgl.opengl;

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.Alternate;
import org.lwjgl.util.generator.opengl.GLreturn;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;
import java.nio.IntBuffer;

public interface ARB_vertex_array_object {

    /**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
    int GL_VERTEX_ARRAY_BINDING = 0x85B5;

    @Reuse("GL30")
    void glBindVertexArray(@GLuint int array);

    @Reuse("GL30")
    void glDeleteVertexArrays(@AutoSize("arrays") @GLsizei int n, @Const @GLuint IntBuffer arrays);

    @Reuse("GL30")
    @Alternate("glDeleteVertexArrays")
    void glDeleteVertexArrays(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(caps, array)", keepParam = true) int array);

    @Reuse("GL30")
    void glGenVertexArrays(@AutoSize("arrays") @GLsizei int n, @OutParameter @GLuint IntBuffer arrays);

    @Reuse("GL30")
    @Alternate("glGenVertexArrays")
    @GLreturn("arrays")
    void glGenVertexArrays2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer arrays);

    @Reuse("GL30")
    boolean glIsVertexArray(@GLuint int array);
}
