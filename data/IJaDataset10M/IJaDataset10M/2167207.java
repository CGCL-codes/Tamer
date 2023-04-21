package org.lwjgl.opengl;

public interface APPLE_rgb_422 {

    /**
	 * Accepted by the &lt;format&gt; parameter of DrawPixels, ReadPixels, TexImage1D,
	 * TexImage2D, GetTexImage, TexImage3D, TexSubImage1D, TexSubImage2D,
	 * TexSubImage3D, GetHistogram, GetMinmax, ConvolutionFilter1D,
	 * ConvolutionFilter2D, GetConvolutionFilter, SeparableFilter2D,
	 * GetSeparableFilter, ColorTable, GetColorTable:
	 */
    int GL_RGB_422_APPLE = 0x8A1F;

    /**
	 * Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels, TexImage1D,
	 * TexImage2D, GetTexImage, TexImage3D, TexSubImage1D, TexSubImage2D,
	 * TexSubImage3D, GetHistogram, GetMinmax, ConvolutionFilter1D,
	 * ConvolutionFilter2D, GetConvolutionFilter, SeparableFilter2D,
	 * GetSeparableFilter, ColorTable, GetColorTable:
	 */
    int GL_UNSIGNED_SHORT_8_8_APPLE = 0x85BA;

    int GL_UNSIGNED_SHORT_8_8_REV_APPLE = 0x85BB;
}
