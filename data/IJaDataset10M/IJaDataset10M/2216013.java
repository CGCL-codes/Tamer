package xmage.turbine.render;

import java.io.PrintStream;

/**
 * Interface implemented by renderer information classes.
 */
public interface IRenderInfo {

    /**
	 * Return renderer vendor name.
	 *
	 * @return renderer vendor name
	 */
    String getVendor();

    /**
	 * Return renderer name.
	 *
	 * @return renderer name
	 */
    String getRenderer();

    /**
	 * Return renderer version number.
	 *
	 * @return renderer version number
	 */
    String getVersion();

    /**
	 * Get additional renderer information.
	 *
	 * <p>
	 * In case of OpenGL-based renderer this method returns list of extensions.
	 * Other renderers might return other data here.
	 * </p>
	 *
	 * @return additional renderer information
	 */
    String getAdditionalInfo();

    /**
	 * Get number of lights supported by the renderer.
	 *
	 * @return number of lights supported by the renderer
	 */
    int getMaxLights();

    /**
	 * Get number of bits for red color component.
	 *
	 * @return number of bits for red color component
	 */
    int getRedBits();

    /**
	 * Get number of bits for green color component.
	 *
	 * @return number of bits for green color component
	 */
    int getGreenBits();

    /**
	 * Get number of bits for blue color component.
	 *
	 * @return number of bits for blue color component
	 */
    int getBlueBits();

    /**
	 * Get number of bits for alpha component.
	 *
	 * @return number of bits for alpha component
	 */
    int getAlphaBits();

    /**
	 * Get depth buffer precision.
	 *
	 * @return depth buffer precision
	 */
    int getDepthBits();

    /**
	 * Get stencil buffer capacity.
	 *
	 * @return stencil buffer capacity
	 */
    int getStencilBits();

    /**
	 * Get number of bits for red color component in accumulation buffer.
	 *
	 * @return number of bits for red color component in accumulation buffer
	 */
    int getAccumRedBits();

    /**
	 * Get number of bits for red color component in accumulation buffer.
	 *
	 * @return number of bits for red color component in accumulation buffer
	 */
    int getAccumGreenBits();

    /**
	 * Get number of bits for red color component in accumulation buffer.
	 *
	 * @return number of bits for red color component in accumulation buffer
	 */
    int getAccumBlueBits();

    /**
	 * Get number of bits for alpha component in accumulation buffer.
	 *
	 * @return number of bits for alpha component in accumulation buffer
	 */
    int getAccumAlphaBits();

    /**
	 * Get number of texture units available.
	 *
	 * @return number of texture units available
	 */
    int getNumTextureUnits();

    /**
	 * Write renderer information to standard output.
	 */
    void dumpInfo();

    /**
	 * Write renderer information to specified stream.
	 *
	 * @param out stream to write information to
	 */
    void dumpInfo(PrintStream out);
}
