package org.lwjgl.opengl;

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLvoid;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public interface APPLE_texture_range {

    /**
	 * Accepted by the <pname> parameters of TexParameteri, TexParameterf,
	 * TexParameteriv, TexParameterfv, GetTexParameteriv, and
	 * GetTexParameterfv:
	 */
    int GL_TEXTURE_STORAGE_HINT_APPLE = 0x85BC;

    /**
	 * Accepted by the <param> parameters of TexParameteri, TexParameterf,
	 * TexParameteriv, and TexParameterfv:
	 */
    int GL_STORAGE_PRIVATE_APPLE = 0x85BD;

    int GL_STORAGE_CACHED_APPLE = 0x85BE;

    int GL_STORAGE_SHARED_APPLE = 0x85BF;

    /**
	 * Accepted by the <pname> parameters of GetTexParameteriv and
	 * GetTexParameterfv:
	 */
    int GL_TEXTURE_RANGE_LENGTH_APPLE = 0x85B7;

    /** Accepted by the <pname> parameters of GetTexParameterPointerv: */
    int GL_TEXTURE_RANGE_POINTER_APPLE = 0x85B8;

    void glTextureRangeAPPLE(@GLenum int target, @AutoSize("pointer") @GLsizei int length, @GLvoid ByteBuffer pointer);

    void glGetTexParameterPointervAPPLE(@GLenum int target, @GLenum int pname, @Result @GLvoid Buffer params);
}
