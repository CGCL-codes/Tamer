package xmage.turbine.render;

import xmage.turbine.NodeConfig;

/**
 * This class contains all configuration options for Turbine renderers.
 *
 * <p>
 * Not all renderers support all features so some settings may be ignored
 * by them.
 * </p>
 */
public class RenderConfig extends NodeConfig {

    RenderConfig() {
    }

    private IRenderer renderer = null;

    public void setIRenderer(IRenderer renderer) {
        this.renderer = renderer;
    }

    /** Texturing on/off. Enabled by default. */
    public static final int TEXTURING = 0;

    /** Lighting on/off. Enabled by default. */
    public static final int LIGHTING = 1;

    /** Wireframe rendering on/off. Disabled by default. */
    public static final int WIREFRAME = 2;

    /** Backface culling on/off. Enabled by default. */
    public static final int BACKFACE_CULLING = 10;

    /** Use vertex array on/off. Enabled by default. */
    public static final int USE_VERTEX_ARRAYS = 20;

    /** Use compiled vertex arrays on/off. Enabled by default. */
    public static final int USE_COMPILED_VERTEX_ARRAYS = 21;

    public boolean texturing = true;

    public boolean lighting = true;

    public boolean wireframe = false;

    public boolean backfaceCulling = true;

    public boolean useVertArrays = true;

    public boolean useCompiledVertArrays = true;

    /**
	 * Set renderer configuration option.
	 *
	 * @param flag option to set
	 * @param value value to set it to
	 */
    public void setFlag(int flag, boolean value) {
        switch(flag) {
            case TEXTURING:
                texturing = value;
                reconfigure();
                return;
            case LIGHTING:
                lighting = value;
                reconfigure();
                return;
            case WIREFRAME:
                wireframe = value;
                reconfigure();
                return;
            case BACKFACE_CULLING:
                backfaceCulling = value;
                reconfigure();
                return;
            case USE_VERTEX_ARRAYS:
                useVertArrays = value;
                reconfigure();
                return;
            case USE_COMPILED_VERTEX_ARRAYS:
                useCompiledVertArrays = value;
                reconfigure();
                return;
            default:
                throw new IllegalArgumentException("Invalid flag: " + flag);
        }
    }

    private void reconfigure() {
        if (renderer != null) {
            renderer.reconfigure();
        }
    }

    /**
	 * 
	 * Return renderer configuration option
	 *
	 * @param flag option to return
	 * @return its value
	 */
    public boolean getFlag(int flag) {
        switch(flag) {
            case TEXTURING:
                return texturing;
            case LIGHTING:
                return lighting;
            case WIREFRAME:
                return wireframe;
            case BACKFACE_CULLING:
                return backfaceCulling;
            case USE_VERTEX_ARRAYS:
                return useVertArrays;
            case USE_COMPILED_VERTEX_ARRAYS:
                return useCompiledVertArrays;
            default:
                throw new IllegalArgumentException("Invalid flag: " + flag);
        }
    }

    @Override
    public void setMagnificationMode(int magMode) {
        super.setMagnificationMode(magMode);
        reconfigure();
    }

    @Override
    public void setMinificationMode(int minMode) {
        super.setMinificationMode(minMode);
        reconfigure();
    }
}
