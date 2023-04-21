package org.lwjgl.opengl;

import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLintptr;
import org.lwjgl.util.generator.opengl.GLsizeiptr;

public interface APPLE_flush_buffer_range {

    /**
	 * Accepted by the &lt;pname&gt; parameter of BufferParameteriAPPLE and
	 * GetBufferParameteriv:
	 */
    int GL_BUFFER_SERIALIZED_MODIFY_APPLE = 0x8A12;

    int GL_BUFFER_FLUSHING_UNMAP_APPLE = 0x8A13;

    void glBufferParameteriAPPLE(@GLenum int target, @GLenum int pname, int param);

    void glFlushMappedBufferRangeAPPLE(@GLenum int target, @GLintptr long offset, @GLsizeiptr long size);
}
