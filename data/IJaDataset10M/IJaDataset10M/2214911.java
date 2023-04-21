package org.jcrpg.ui;

import java.io.File;
import java.util.HashMap;
import org.jcrpg.apps.Jcrpg;
import org.jcrpg.threed.J3DCore;
import org.jcrpg.threed.engine.ui.ZoomingQuad;
import com.ardor3d.image.Texture;
import com.ardor3d.image.Texture.MinificationFilter;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.state.TextureState;
import com.ardor3d.scenegraph.shape.Quad;
import com.ardor3d.util.TextureManager;
import com.ardor3d.util.resource.ResourceLocatorTool;
import com.ardor3d.util.resource.URLResourceSource;

public class UIImageCache {

    public static HashMap<String, TextureState> imageCache = new HashMap<String, TextureState>();

    public static Quad getImage(String filePath, boolean alpha, float sizeMul) {
        if (J3DCore.SETTINGS.DISABLE_DDS && filePath.toLowerCase().endsWith("dds")) {
            filePath = filePath.substring(0, filePath.length() - 3) + "png";
        }
        TextureState q = imageCache.get(filePath);
        if (q == null) {
            try {
                Texture texture = TextureManager.load(ResourceLocatorTool.locateResource(UIBase.RES_TYPE_UI, filePath), MinificationFilter.BilinearNearestMipMap, true);
                TextureState state = new TextureState();
                state.setTexture(texture, 0);
                q = state;
                imageCache.put(filePath, q);
            } catch (Exception ex) {
                ex.printStackTrace();
                q = null;
            }
        }
        Quad quad = new Quad(filePath, 1f * sizeMul, 1f * sizeMul);
        if (alpha) quad.setRenderState(J3DCore.getInstance().uiBase.hud.hudAS);
        quad.setRenderState(q);
        quad.setModelBound(new com.ardor3d.bounding.BoundingBox());
        quad.updateModelBound();
        quad.setTranslation(new Vector3(0, 0, 0));
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.finest("UIImageCache LOADED " + filePath);
        return quad;
    }

    public static Quad getImage(String filePath, boolean alpha, double sizeX, double sizeY) {
        return getImage(UIBase.RES_TYPE_UI, filePath, alpha, sizeX, sizeY);
    }

    public static Quad getImage(String resType, String filePath, boolean alpha, double sizeX, double sizeY) {
        if (J3DCore.SETTINGS.DISABLE_DDS && filePath.toLowerCase().endsWith("dds")) {
            filePath = filePath.substring(0, filePath.length() - 3) + "png";
        }
        TextureState q = imageCache.get(filePath);
        if (q == null) {
            try {
                Texture texture = TextureManager.load(ResourceLocatorTool.locateResource(resType, filePath), MinificationFilter.BilinearNearestMipMap, true);
                TextureState state = new TextureState();
                state.setTexture(texture, 0);
                q = state;
                imageCache.put(filePath, q);
            } catch (Exception ex) {
                ex.printStackTrace();
                q = null;
            }
        }
        Quad quad = new Quad(filePath, 1f * sizeX, 1f * sizeY);
        if (alpha) quad.setRenderState(J3DCore.getInstance().uiBase.hud.hudAS);
        quad.setRenderState(q);
        quad.setModelBound(new com.ardor3d.bounding.BoundingBox());
        quad.updateModelBound();
        quad.setTranslation(new Vector3(0, 0, 0));
        if (filePath.startsWith(".")) {
        }
        return quad;
    }

    public static Quad getImageDirectlyFromFile(String filePath, boolean alpha, double sizeX, double sizeY) {
        if (J3DCore.SETTINGS.DISABLE_DDS && filePath.toLowerCase().endsWith("dds")) {
            filePath = filePath.substring(0, filePath.length() - 3) + "png";
        }
        TextureState q = imageCache.get(filePath);
        if (q == null) {
            try {
                Texture texture = TextureManager.load(new URLResourceSource(new File(filePath).toURI().toURL()), MinificationFilter.BilinearNearestMipMap, true);
                TextureState state = new TextureState();
                state.setTexture(texture, 0);
                q = state;
                imageCache.put(filePath, q);
            } catch (Exception ex) {
                ex.printStackTrace();
                q = null;
            }
        }
        Quad quad = new Quad(filePath, 1f * sizeX, 1f * sizeY);
        if (alpha) quad.setRenderState(J3DCore.getInstance().uiBase.hud.hudAS);
        quad.setRenderState(q);
        quad.setModelBound(new com.ardor3d.bounding.BoundingBox());
        quad.updateModelBound();
        quad.setTranslation(new Vector3(0, 0, 0));
        if (filePath.startsWith(".")) {
        }
        return quad;
    }

    public static ZoomingQuad getImageZoomingQuad(String filePath, boolean alpha, double sizeX, double sizeY) {
        if (J3DCore.SETTINGS.DISABLE_DDS && filePath.toLowerCase().endsWith("dds")) {
            filePath = filePath.substring(0, filePath.length() - 3) + "png";
        }
        TextureState q = imageCache.get(filePath);
        if (q == null) {
            try {
                Texture texture = TextureManager.load(new URLResourceSource(new File(filePath).toURI().toURL()), MinificationFilter.BilinearNearestMipMap, true);
                TextureState state = new TextureState();
                state.setTexture(texture, 0);
                q = state;
                imageCache.put(filePath, q);
            } catch (Exception ex) {
                ex.printStackTrace();
                q = null;
            }
        }
        ZoomingQuad quad = new ZoomingQuad(filePath, 1f * sizeX, 1f * sizeY);
        if (alpha) quad.setRenderState(J3DCore.getInstance().uiBase.hud.hudAS);
        quad.setRenderState(q);
        quad.setModelBound(new com.ardor3d.bounding.BoundingBox());
        quad.updateModelBound();
        quad.setTranslation(new Vector3(0, 0, 0));
        return quad;
    }
}
