package org.lwjgl.opengl;

public interface EXT_pixel_buffer_object extends ARB_buffer_object {

    /**
	 * Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 * BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData,
	 * GetBufferParameteriv, and GetBufferPointerv:
	 */
    int GL_PIXEL_PACK_BUFFER_EXT = 0x88EB;

    int GL_PIXEL_UNPACK_BUFFER_EXT = 0x88EC;

    /**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
    int GL_PIXEL_PACK_BUFFER_BINDING_EXT = 0x88ED;

    int GL_PIXEL_UNPACK_BUFFER_BINDING_EXT = 0x88EF;
}
