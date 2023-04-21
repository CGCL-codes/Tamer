package org.lwjgl.opengl;

import org.lwjgl.util.generator.opengl.GLenum;

public interface EXT_provoking_vertex {

    /** Accepted by the &lt;mode&gt; parameter of ProvokingVertexEXT: */
    int GL_FIRST_VERTEX_CONVENTION_EXT = 0x8E4D;

    int GL_LAST_VERTEX_CONVENTION_EXT = 0x8E4E;

    /**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
    int GL_PROVOKING_VERTEX_EXT = 0x8E4F;

    int GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION_EXT = 0x8E4C;

    void glProvokingVertexEXT(@GLenum int mode);
}
