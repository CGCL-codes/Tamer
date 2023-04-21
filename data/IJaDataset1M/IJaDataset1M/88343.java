package com.googlecode.gwtgl.wrapper;

import com.google.gwt.typedarrays.client.Float32Array;
import com.googlecode.gwtgl.binding.WebGLRenderingContext;
import com.googlecode.gwtgl.binding.WebGLUniformLocation;
import com.googlecode.gwtgl.wrapper.enums.ClearFlag;
import com.googlecode.gwtgl.wrapper.enums.DataType;
import com.googlecode.gwtgl.wrapper.enums.DepthComparisonFunction;
import com.googlecode.gwtgl.wrapper.enums.GLCapability;
import com.googlecode.gwtgl.wrapper.enums.GLError;
import com.googlecode.gwtgl.wrapper.enums.PrimitiveRenderingMode;
import com.googlecode.gwtgl.wrapper.enums.TextureMagFilter;
import com.googlecode.gwtgl.wrapper.enums.TextureMinFilter;
import com.googlecode.gwtgl.wrapper.enums.TextureTarget;
import com.googlecode.gwtgl.wrapper.enums.TextureWrapMode;

/**
 * WebGLWrapper (in combination with the other wrapper classes) represents a lightweight wrapper around the WebGL Binding.
 * Its goal is to provide:
 * - more type safety (use of enums instead of numeric constants)
 * - easier management of common use-cases (e.g. setting an image to a texture)
 * 
 * WebGLWrapper is not:
 * - a complete design pattern aware object oriented API for WebGL
 * - a complete 3D engine
 * 
 * @author Steffen Schäfer
 * 
 */
public class WebGLWrapper {

    final WebGLRenderingContext glContext;

    /**
	 * create wrapper for the given {@link WebGLRenderingContext}
	 * @param glContext {@link WebGLRenderingContext} that will be wrapped
	 */
    public WebGLWrapper(WebGLRenderingContext glContext) {
        this.glContext = glContext;
    }

    /**
	 * Clear color, depth and stencil buffers to preset values
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#clear(int)}
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glClear.xml"
	 */
    public void clear() {
        clear(ClearFlag.COLOR_BUFFER, ClearFlag.DEPTH_BUFFER, ClearFlag.STENCIL_BUFFER);
    }

    /**
	 * Clear specified buffers to preset values
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#clear(int)}
	 * @param clearFlags Specify buffers to be cleared by providing any number of {@link ClearFlag}s
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glClear.xml"
	 */
    public void clear(ClearFlag... clearFlags) {
        int mask = 0;
        for (ClearFlag flag : clearFlags) {
            mask = mask | flag.getIntValue();
        }
        glContext.clear(mask);
    }

    /**
	 * Specify clear values for the color buffers
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#clearColor(float, float, float, float)}
	 * @param red the red value used when the color buffers are cleared
	 * @param green the green value used when the color buffers are cleared
	 * @param blue the blue value used when the color buffers are cleared
	 * @param alpha the alpha value used when the color buffers are cleared
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glClearColor.xml"
	 */
    public void clearColor(float red, float green, float blue, float alpha) {
        glContext.clearColor(red, green, blue, alpha);
    }

    /**
	 * Specify the clear value for the depth buffer
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#clearDepth(float)}
	 * @param depthValue depth value used when the depth buffer is cleared. The initial value is 1.
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glClearDepthf.xml"
	 */
    public void clearDepth(float depthValue) {
        glContext.clearDepth(depthValue);
    }

    /**
	 * Disables a {@link GLCapability}
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#disable(int)}
	 * @param capability {@link GLCapability}
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glEnable.xml"
	 */
    public void disable(GLCapability capability) {
        glContext.disable(capability.getIntValue());
    }

    /**
	 * Enables a {@link GLCapability}
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#enable(int)}
	 * @param capability {@link GLCapability}
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glEnable.xml"
	 */
    public void enable(GLCapability capability) {
        glContext.enable(capability.getIntValue());
    }

    /**
	 * Enable a generic vertex attribute array
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#enableVertexAttribArray(int)}
	 * @param index Specifies the index of the generic vertex attribute to be enabled
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glEnableVertexAttribArray.xml"
	 */
    public void enableVertexAttribArray(int index) {
        glContext.enableVertexAttribArray(index);
    }

    /**
	 * Disbale a generic vertex attribute array
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#disableVertexAttribArray(int)}
	 * @param index Specifies the index of the generic vertex attribute to be disabled
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glEnableVertexAttribArray.xml"
	 */
    public void disableVertexAttribArray(int index) {
        glContext.disableVertexAttribArray(index);
    }

    /**
	 * Define an array of generic vertex attribute data
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#vertexAttribPointer(int, int, int, boolean, int, int)}
	 * @param index Specifies the index of the generic vertex attribute to be modified
	 * @param size Specifies the number of components per generic vertex attribute. Must be 1, 2, 3, or 4. The initial value is 4.
	 * @param type Specifies the {@link DataType} of each component in the array
	 * @param normalized Specifies whether fixed-point data values should be normalized (true) or converted directly as fixed-point values (false) when they are accessed
	 * @param stride Specifies the byte offset between consecutive generic vertex attributes. If stride is 0, the generic vertex attributes are understood to be tightly packed in the array. The initial value is 0.
	 * @param offset
	 */
    public void vertexAttribPointer(int index, int size, DataType type, boolean normalized, int stride, int offset) {
        glContext.vertexAttribPointer(index, size, type.getIntValue(), normalized, stride, offset);
    }

    /**
	 * Specify the value used for depth buffer comparisons
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#depthFunc(int)}
	 * @param depthComparisonFunction depth comparison function
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glDepthFunc.xml"
	 */
    public void depthFunc(DepthComparisonFunction depthComparisonFunction) {
        glContext.depthFunc(depthComparisonFunction.getIntValue());
    }

    /**
	 * Render primitives from array data
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#drawArrays(int, int, int)}
	 * @param primitiveRenderingMode Specifies what kind of primitives to render - {@link PrimitiveRenderingMode}
	 * @param first Specifies the starting index in the enabled arrays
	 * @param count Specifies the number of indices to be rendered
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glDrawArrays.xml"
	 */
    public void drawArrays(PrimitiveRenderingMode primitiveRenderingMode, int first, int count) {
        glContext.drawArrays(primitiveRenderingMode.getIntValue(), first, count);
    }

    /**
	 * Render primitives using the currently bound index array.
	 * Wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#drawElements(int, int, int, int)}
	 * @param primitiveRenderingMode Specifies what kind of primitives to render.
	 * @param count Number of elements to be rendered
	 * @param offset Offset.
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glDrawElements.xml"
	 */
    public void drawElements(PrimitiveRenderingMode primitiveRenderingMode, int count, int offset) {
        glContext.drawElements(primitiveRenderingMode.getIntValue(), count, WebGLRenderingContext.UNSIGNED_BYTE, offset);
    }

    /**
	 * Wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#uniformMatrix2fv(WebGLUniformLocation location, boolean transpose, Float32Array value)}
	 * @param location Specifies the location of the uniform value to be modified
	 * @param transpose Specifies whether to transpose the matrix as the values are loaded into the uniform variable
	 * @param values Specifies an array of values that will be used to update the specified uniform variable
	 */
    public void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose, float[] values) {
        glContext.uniformMatrix2fv(location, transpose, Float32Array.create(values));
    }

    /**
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#uniformMatrix3fv(WebGLUniformLocation location, boolean transpose, Float32Array value)}
	 * @param location Specifies the location of the uniform value to be modified
	 * @param transpose Specifies whether to transpose the matrix as the values are loaded into the uniform variable
	 * @param values Specifies an array of values that will be used to update the specified uniform variable
	 */
    public void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose, float[] values) {
        glContext.uniformMatrix3fv(location, transpose, Float32Array.create(values));
    }

    /**
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#uniformMatrix4fv(WebGLUniformLocation location, boolean transpose, Float32Array value)}
	 * @param location Specifies the location of the uniform value to be modified
	 * @param transpose Specifies whether to transpose the matrix as the values are loaded into the uniform variable
	 * @param values Specifies an array of values that will be used to update the specified uniform variable
	 */
    public void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose, float[] values) {
        glContext.uniformMatrix4fv(location, transpose, Float32Array.create(values));
    }

    /**
	 * Select active texture unit
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#activeTexture(int)}
	 * @param textureUnit Specifies which texture unit to make active. First texture unit is 0. The number of texture units is implementation dependent, but must be at least two.
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glActiveTexture.xml"
	 */
    public void activeTexture(int textureUnit) {
        glContext.activeTexture(WebGLRenderingContext.TEXTURE0 + textureUnit);
    }

    /**
	 * Generate a complete set of mipmaps for a texture object
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#generateMipmap(int)}
	 * @param textureTarget Specifies the {@link TextureTarget} of the texture object whose mipmaps will be generated
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glGenerateMipmap.xml"
	 */
    public void generateMipmap(TextureTarget textureTarget) {
        glContext.generateMipmap(textureTarget.getIntValue());
    }

    /**
	 * Specify the value of a uniform variable for the current program object
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#uniform1f(WebGLUniformLocation, float)}
	 * @param location Specifies the location of the uniform variable to be modified
	 * @param v0 value 0 to be used for the specified uniform variable
	 */
    public void uniform1f(WebGLUniformLocation location, float v0) {
        glContext.uniform1f(location, v0);
    }

    /**
	 * Specify the value of a uniform variable for the current program object
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#uniform1i(WebGLUniformLocation, int)}
	 * @param location Specifies the location of the uniform variable to be modified
	 * @param v0 value 0 to be used for the specified uniform variable
	 */
    public void uniform1i(WebGLUniformLocation location, int v0) {
        glContext.uniform1i(location, v0);
    }

    /**
	 * Specify the value of a uniform variable for the current program object
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#uniform2f(WebGLUniformLocation, float, float)}
	 * @param location Specifies the location of the uniform variable to be modified
	 * @param v0 value 0 to be used for the specified uniform variable
	 * @param v1 value 1 to be used for the specified uniform variable
	 */
    public void uniform2f(WebGLUniformLocation location, float v0, float v1) {
        glContext.uniform2f(location, v0, v1);
    }

    /**
	 * Specify the value of a uniform variable for the current program object
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#uniform2i(WebGLUniformLocation, int, int)}
	 * @param location Specifies the location of the uniform variable to be modified
	 * @param v0 value 0 to be used for the specified uniform variable
	 * @param v1 value 1 to be used for the specified uniform variable
	 */
    public void uniform2i(WebGLUniformLocation location, int v0, int v1) {
        glContext.uniform2i(location, v0, v1);
    }

    /**
	 * Specify the value of a uniform variable for the current program object
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#uniform3f(WebGLUniformLocation, float, float, float)}
	 * @param location Specifies the location of the uniform variable to be modified
	 * @param v0 value 0 to be used for the specified uniform variable
	 * @param v1 value 1 to be used for the specified uniform variable
	 * @param v2 value 2 to be used for the specified uniform variable
	 */
    public void uniform3f(WebGLUniformLocation location, float v0, float v1, float v2) {
        glContext.uniform3f(location, v0, v1, v2);
    }

    /**
	 * Specify the value of a uniform variable for the current program object
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#uniform3i(WebGLUniformLocation, int, int, int)}
	 * @param location Specifies the location of the uniform variable to be modified
	 * @param v0 value 0 to be used for the specified uniform variable
	 * @param v1 value 1 to be used for the specified uniform variable
	 * @param v2 value 2 to be used for the specified uniform variable
	 */
    public void uniform3i(WebGLUniformLocation location, int v0, int v1, int v2) {
        glContext.uniform3i(location, v0, v1, v2);
    }

    /**
	 * Specify the value of a uniform variable for the current program object
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#uniform4f(WebGLUniformLocation, float, float, float, float)}
	 * @param location Specifies the location of the uniform variable to be modified
	 * @param v0 value 0 to be used for the specified uniform variable
	 * @param v1 value 1 to be used for the specified uniform variable
	 * @param v2 value 2 to be used for the specified uniform variable
	 * @param v3 value 3 to be used for the specified uniform variable
	 */
    public void uniform4f(WebGLUniformLocation location, float v0, float v1, float v2, float v3) {
        glContext.uniform4f(location, v0, v1, v2, v3);
    }

    /**
	 * Specify the value of a uniform variable for the current program object
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#uniform4i(WebGLUniformLocation, int, int, int, int)}
	 * @param location Specifies the location of the uniform variable to be modified
	 * @param v0 value 0 to be used for the specified uniform variable
	 * @param v1 value 1 to be used for the specified uniform variable
	 * @param v2 value 2 to be used for the specified uniform variable
	 * @param v3 value 3 to be used for the specified uniform variable
	 */
    public void uniform4i(WebGLUniformLocation location, int v0, int v1, int v2, int v3) {
        glContext.uniform4i(location, v0, v1, v2, v3);
    }

    /**
	 * Force execution of GL commands in finite time
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glFlush.xml"
	 */
    public void flush() {
        glContext.flush();
    }

    /**
	 * Return error information
	 * @return {@link GLError}
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glGetError.xml"
	 */
    public GLError getError() {
        return GLError.getByIntValue(glContext.getError());
    }

    /**
	 * Get {@link TextureWrapMode} for texture wrap S
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#getTexParameteri(int, int)} 
	 * @param target {@link TextureTarget}
	 * @return {@link TextureWrapMode}
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glGetTexParameter.xml"
	 */
    public TextureWrapMode getTextureWrapS(TextureTarget target) {
        int val = glContext.getTexParameteri(target.getIntValue(), WebGLRenderingContext.TEXTURE_WRAP_S);
        return TextureWrapMode.getByIntValue(val);
    }

    /**
	 * Set {@link TextureWrapMode} for texture wrap S
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#texParameteri(int, int, int)} 
	 * @param target {@link TextureTarget}
	 * @param textureWrapMode {@link TextureWrapMode}
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glTexParameter.xml"
	 */
    public void setTextureWrapS(TextureTarget target, TextureWrapMode textureWrapMode) {
        glContext.texParameteri(target.getIntValue(), WebGLRenderingContext.TEXTURE_WRAP_S, textureWrapMode.getIntValue());
    }

    /**
	 * Get {@link TextureWrapMode} for texture wrap T
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#getTexParameteri(int, int)}
	 * @param target {@link TextureTarget}
	 * @return {@link TextureWrapMode}
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glGetTexParameter.xml"
	 */
    public TextureWrapMode getTextureWrapT(TextureTarget target) {
        int val = glContext.getTexParameteri(target.getIntValue(), WebGLRenderingContext.TEXTURE_WRAP_T);
        return TextureWrapMode.getByIntValue(val);
    }

    /**
	 * Set {@link TextureWrapMode} for texture wrap T
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#texParameteri(int, int, int)}
	 * @param target {@link TextureTarget}
	 * @param textureWrapMode
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glTexParameter.xml"
	 */
    public void setTextureWrapT(TextureTarget target, TextureWrapMode textureWrapMode) {
        glContext.texParameteri(target.getIntValue(), WebGLRenderingContext.TEXTURE_WRAP_T, textureWrapMode.getIntValue());
    }

    /**
	 * Set {@link TextureMinFilter}
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#texParameteri(int, int, int)}
	 * @param target {@link TextureTarget}
	 * @param textureMinFilter
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glTexParameter.xml"
	 */
    public void setTextureMinFilter(TextureTarget target, TextureMinFilter textureMinFilter) {
        glContext.texParameteri(target.getIntValue(), WebGLRenderingContext.TEXTURE_MIN_FILTER, textureMinFilter.getIntValue());
    }

    /**
	 * Get {@link TextureMinFilter}
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#getTexParameteri(int, int)}
	 * @param target {@link TextureTarget}
	 * @return {@link TextureMinFilter}
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glGetTexParameter.xml"
	 */
    public TextureMinFilter getTextureMinFilter(TextureTarget target) {
        int val = glContext.getTexParameteri(target.getIntValue(), WebGLRenderingContext.TEXTURE_MIN_FILTER);
        return TextureMinFilter.getByIntValue(val);
    }

    /**
	 * Set {@link TextureMagFilter}
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#texParameteri(int, int, int)}
	 * @param target {@link TextureTarget}
	 * @param textureMagFilter
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glTexParameter.xml"
	 */
    public void setTextureMagFilter(TextureTarget target, TextureMagFilter textureMagFilter) {
        glContext.texParameteri(target.getIntValue(), WebGLRenderingContext.TEXTURE_MAG_FILTER, textureMagFilter.getIntValue());
    }

    /**
	 * Get {@link TextureMagFilter}
	 * wrapps {@link com.googlecode.gwtgl.binding.WebGLRenderingContext#getTexParameteri(int, int)}
	 * @param target {@link TextureTarget}
	 * @return {@link TextureMagFilter}
	 * @see "http://www.khronos.org/opengles/sdk/docs/man/glGetTexParameter.xml"
	 */
    public TextureMagFilter getTextureMagFilter(TextureTarget target) {
        int val = glContext.getTexParameteri(target.getIntValue(), WebGLRenderingContext.TEXTURE_MAG_FILTER);
        return TextureMagFilter.getByIntValue(val);
    }
}
