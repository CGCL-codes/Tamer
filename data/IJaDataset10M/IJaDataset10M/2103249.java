package org.xith3d.scenegraph.primitives;

import org.jagatoo.opengl.enums.TextureFormat;
import org.openmali.FastMath;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loaders.texture.TextureLoader.FlipMode;
import org.xith3d.render.BackgroundRenderPass;
import org.xith3d.render.BaseRenderPassConfig;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.Geometry;
import org.xith3d.scenegraph.GroupNode;
import org.xith3d.scenegraph.PolygonAttributes;
import org.xith3d.scenegraph.RenderingAttributes;
import org.xith3d.scenegraph.StaticTransform;
import org.xith3d.scenegraph.Texture;
import org.xith3d.scenegraph.View.CameraMode;

/**
 * A sky box is background node intended to display the sky with a far away
 * appearance but without consuming massive computational resources. It is drawn
 * with the depth buffer disabled, so all objects which are drawn after it will
 * be drawn in front. Even though it respects View's angle, it will always be
 * drawn as if the View (camera) were in its center regardless of the actual
 * position.
 * <p>
 * This implementation creates a sphere to provide a more realistic sky than
 * SkyBox. The downside to using a sphere is that you need a panoramic picture
 * to use as the Texture. This implementation also uses GeoSphere geometry so
 * that it doesn't create a ridiculous number of triangles.
 * <p>
 * This class ignores the default setting for pickable and sets itself to NOT
 * pickable.  You must explicitly call setPickable( true ) on SkyGeoSphere
 * nodes regardless of the default setting.
 * <p>
 * Originally inspired by William Denniss's SkyBox.
 * 
 * @author Kevin Finley (aka horati)
 */
public class SkyGeoHemisphere extends BackgroundRenderPass {

    public static final int DEFAULT_SKY_SPLITS = 5;

    public static final float X_ROTATION = FastMath.PI * 1.5f;

    private static Texture getTextureOrNull(String texture) {
        if (texture == null) return (null);
        Texture tex = TextureLoader.getInstance().getTexture(texture, (FlipMode) null, TextureFormat.RGB, Texture.MipmapMode.BASE_LEVEL, true, false, false);
        tex.enableAutoFreeLocalData();
        return (tex);
    }

    public static <G extends GroupNode> G createSkyGeoHemisphereGroup(int frequency, Texture texture, G group) {
        GeoSphere hemisphere = new GeoSphere(5f, frequency, Geometry.COORDINATES | Geometry.TEXTURE_COORDINATES, false, 2, true);
        hemisphere.getGeometry().setOptimization(Geometry.Optimization.USE_DISPLAY_LISTS);
        StaticTransform.rotateX(hemisphere, FastMath.PI_HALF);
        StaticTransform.rotateZ(hemisphere, FastMath.PI);
        Appearance appearance = hemisphere.getAppearance(true);
        RenderingAttributes renderingAttributes = new RenderingAttributes();
        renderingAttributes.setDepthBufferWriteEnabled(false);
        appearance.setRenderingAttributes(renderingAttributes);
        PolygonAttributes polygonAttributes = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_FRONT);
        appearance.setPolygonAttributes(polygonAttributes);
        appearance.setTexture(texture);
        hemisphere.setAppearance(appearance);
        group.addChild(hemisphere);
        group.setPickableRecursive(false);
        return (group);
    }

    public static BranchGroup createSkyGeoHemisphereGroup(int frequency, Texture texture) {
        return (createSkyGeoHemisphereGroup(frequency, texture, new BranchGroup()));
    }

    public static final BranchGroup createSkyGeoHemisphereGroup(int frequency, String textureName) {
        return (createSkyGeoHemisphereGroup(frequency, getTextureOrNull(textureName)));
    }

    public static final BranchGroup createSkyGeoHemisphereGroup(Texture texture) {
        return (createSkyGeoHemisphereGroup(SkyGeoHemisphere.DEFAULT_SKY_SPLITS, texture));
    }

    public static final BranchGroup createSkyGeoHemisphereGroup(String textureName) {
        return (createSkyGeoHemisphereGroup(SkyGeoHemisphere.DEFAULT_SKY_SPLITS, textureName));
    }

    public SkyGeoHemisphere(int frequency, Texture texture) {
        super(createSkyGeoHemisphereGroup(frequency, texture), new BaseRenderPassConfig(CameraMode.VIEW_FIXED_POSITION));
    }

    public SkyGeoHemisphere(int frequency, String textureName) {
        this(frequency, getTextureOrNull(textureName));
    }

    public SkyGeoHemisphere(Texture texture) {
        this(SkyGeoHemisphere.DEFAULT_SKY_SPLITS, texture);
    }

    public SkyGeoHemisphere(String textureName) {
        this(SkyGeoHemisphere.DEFAULT_SKY_SPLITS, textureName);
    }
}
