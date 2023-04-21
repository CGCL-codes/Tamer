package org.xith3d.loaders.models.util.meta;

import org.openmali.vecmath2.AxisAngle3f;
import org.xith3d.loaders.models.ModelLoader;

/**
 * Contains the meta data that can be associated with a model.
 * 
 * @author Andrew Hanson (aka Patheros)
 */
public class ModelMetaData {

    protected static class Resource {

        protected String name;

        protected Type type = Type.relative;

        protected static enum Type {

            base, relative
        }
    }

    protected static class LoadingFlags {

        protected boolean lightNodes;

        protected boolean fogNodes;

        protected boolean backgroundNodes;

        protected boolean behaviorNodes;

        protected boolean viewGroups;

        protected boolean soundNodes;

        protected boolean useDisplayLists;

        public int getFlags() {
            int retVal = 0;
            retVal |= lightNodes ? ModelLoader.LOAD_LIGHT_NODES : 0;
            retVal |= fogNodes ? ModelLoader.LOAD_FOG_NODES : 0;
            retVal |= viewGroups ? ModelLoader.LOAD_CAMERAS : 0;
            retVal |= soundNodes ? ModelLoader.LOAD_SOUND_NODES : 0;
            return retVal;
        }

        public void setFlag(int loadFlags) {
            lightNodes = (loadFlags & ModelLoader.LOAD_LIGHT_NODES) > 0;
            fogNodes = (loadFlags & ModelLoader.LOAD_FOG_NODES) > 0;
            viewGroups = (loadFlags & ModelLoader.LOAD_CAMERAS) > 0;
            soundNodes = (loadFlags & ModelLoader.LOAD_SOUND_NODES) > 0;
        }
    }

    private Resource resource = new Resource();

    private AxisAngle3f rotation;

    private float scaling;

    private LoadingFlags loadingFlags = new LoadingFlags();

    /**
     * Sets the name of the resource to create a URL from.
     * 
     * @param resourceName
     */
    void setResourceName(String resourceName) {
        this.resource.name = resourceName;
    }

    /**
     * @return the name of the resource to create a URL from.
     */
    public final String getResourceName() {
        return (resource.name);
    }

    void setResourceRefrenceBase() {
        this.resource.type = Resource.Type.base;
    }

    public final boolean isResourceRefrenceBase() {
        return (resource.type == Resource.Type.base);
    }

    void setResourceRefrenceRelative() {
        this.resource.type = Resource.Type.relative;
    }

    public final boolean isResourceRefrenceRelative() {
        return (resource.type == Resource.Type.relative);
    }

    public void setRotation(AxisAngle3f rotation) {
        this.rotation = rotation;
    }

    public final AxisAngle3f getRotation() {
        return (rotation);
    }

    void setScaling(float scaling) {
        this.scaling = scaling;
    }

    public final float getScaling() {
        return (scaling);
    }

    public final LoadingFlags getLoadingFlags() {
        return (loadingFlags);
    }
}
