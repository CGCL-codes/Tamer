package org.lwjgl.opengl;

public interface AMD_depth_clamp_separate {

    /**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 * and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
    int GL_DEPTH_CLAMP_NEAR_AMD = 0x901E, GL_DEPTH_CLAMP_FAR_AMD = 0x901F;
}
