package com.erickpardal.game.fpsdemo.scene;

import java.text.DecimalFormat;
import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.light.PointLight;
import com.jme.light.SimpleLightNode;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Disk;
import com.jme.scene.shape.Dome;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState.StateType;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.LensFlare;
import com.jmex.effects.LensFlareFactory;

/**
 * SkyDome provides a dynamic sky environment. It implements dynamic sun, moon, clouds
 * motion and star effect. The textures applied to the dome, the plane and the star effect
 * are preloaded to ensure smoothness of game.
 *
 * @author Yi Wang (Neakor)
 */
public class SkyDome extends Node {

    private static final long serialVersionUID = 1L;

    private Renderer renderer;

    private float radius;

    private float cycle;

    private float[] dColor;

    private float[] mColor;

    private LightState astronomyLightState;

    private SpatialTransformer astronomyAnimator;

    private PointLight sunlight;

    private LensFlare sunFlareEffect;

    private PointLight moonlight;

    private LensFlare moonFlareEffect;

    private Skybox starEffect;

    /**
	* No argument Constructor of SkyDome.
	*/
    public SkyDome() {
        this(1000, (Texture) null, (Texture) null, (Texture) null, 7200, new float[] { 0.9843f, 0.5098f, 0, 1 }, new float[] { 0.2745f, 0.3961f, 0.6196f, 1 });
    }

    /**
	* Constructor of SkyDome.
     * @param cameraFar The far plane value of the camera.
	* @param model The model of the skydome including the dome modela and the plane model.
     * @param starTexture The texture used for the star effect.
     * @param cycle The time it takes the sun to complete one rotation cycle in seconds.
     * @param dColor The color of the sunlight at dawn and dusk in the order of RGBA.
     * @param mColor The color of the moonlight during the night time.
	*/
    public SkyDome(float cameraFar, Node model, Texture starTexture, float cycle, float[] dColor, float[] mColor) {
        this.renderer = DisplaySystem.getDisplaySystem().getRenderer();
        this.radius = cameraFar / 2;
        this.cycle = cycle;
        this.dColor = dColor;
        this.mColor = mColor;
        this.initializeSkyDome(model);
        this.initializeAstronomySystem();
        this.initializeStarEffect(starTexture);
        this.setupRenderStates();
    }

    /**
	* Constructor of SkyDome.
     * @param cameraFar The far plane value of the camera.
     * @param domeTexture The texture used for the sky dome.
     * @param planeTexture The texture used for the plane under the sky dome.
     * @param starTexture The texture used for the star effect.
     * @param cycle The time it takes the sun to complete one rotation cycle in seconds.
     * @param dColor The color of the sunlight at dawn and dusk in the order of RGBA.
     * @param mColor The color of the moonlight during the night time.
	*/
    public SkyDome(float cameraFar, Texture domeTexture, Texture planeTexture, Texture starTexture, float cycle, float[] dColor, float[] mColor) {
        this.renderer = DisplaySystem.getDisplaySystem().getRenderer();
        this.radius = cameraFar / 2;
        this.cycle = cycle;
        this.dColor = dColor;
        this.mColor = mColor;
        this.initializeSkyDome(domeTexture, planeTexture);
        this.initializeAstronomySystem();
        this.initializeStarEffect(starTexture);
        this.setupRenderStates();
    }

    /**
	* Update the sky dome.
	* @param cameraPosition The current position of the camera.
	*/
    public void update(Vector3f cameraPosition) {
        this.setLocalTranslation(cameraPosition.x, cameraPosition.y - this.radius / 2, cameraPosition.z);
        float currentTime = this.getCurrentTime(false);
        this.updateSunLightColor(currentTime);
        this.updateMoonLightColor(currentTime);
        this.updateSunLensFlare(currentTime);
        this.updateMoonLensFlare(currentTime);
        this.updateStarEffect(currentTime);
    }

    /**
	* Change the sunlight color based on the given current time.
	* @param currentTime The current time.
	*/
    private void updateSunLightColor(float currentTime) {
        float[] newColor = new float[4];
        if (currentTime >= 6 && currentTime < 12) {
            for (int i = 0; i < newColor.length; i++) {
                newColor[i] = this.dColor[i] + ((1 - this.dColor[i]) / 6) * (currentTime - 6);
            }
            this.sunlight.setDiffuse(new ColorRGBA(newColor[0], newColor[1], newColor[2], newColor[3]));
        } else if (currentTime >= 12 && currentTime < 18) {
            for (int i = 0; i < newColor.length; i++) {
                newColor[i] = 1 - ((1 - this.dColor[i]) / 6) * (currentTime - 12);
            }
            this.sunlight.setDiffuse(new ColorRGBA(newColor[0], newColor[1], newColor[2], newColor[3]));
        } else if (currentTime >= 18 && currentTime < 24) {
            for (int i = 0; i < newColor.length; i++) {
                newColor[i] = this.dColor[i] - (this.dColor[i] / 6) * (currentTime - 18);
            }
            this.sunlight.setDiffuse(new ColorRGBA(newColor[0], newColor[1], newColor[2], newColor[3]));
        } else if (currentTime >= 0 && currentTime < 6) {
            for (int i = 0; i < newColor.length; i++) {
                newColor[i] = (this.dColor[i] / 6) * (currentTime - 0);
            }
            this.sunlight.setDiffuse(new ColorRGBA(newColor[0], newColor[1], newColor[2], newColor[3]));
        }
    }

    /**
	* Change the moonlight color based on the given current time.
	* @param currentTime The current time.
	*/
    private void updateMoonLightColor(float currentTime) {
        float[] newColor = new float[4];
        if (currentTime >= 6 && currentTime <= 12) {
            for (int i = 0; i < newColor.length; i++) {
                newColor[i] = this.mColor[i] - (this.mColor[i] / 6) * (currentTime - 6);
            }
            this.moonlight.setDiffuse(new ColorRGBA(newColor[0], newColor[1], newColor[2], newColor[3]));
        } else if (currentTime > 12 && currentTime <= 18) {
            for (int i = 0; i < newColor.length; i++) {
                newColor[i] = (this.mColor[i] / 6) * (currentTime - 12);
            }
            this.moonlight.setDiffuse(new ColorRGBA(newColor[0], newColor[1], newColor[2], newColor[3]));
        }
    }

    /**
	* Increase or decrease the sun's lens flare effect intensity based on the given current time.
	* The sun's maximum intensity is 1.
	* @param currentTime The current time.
	*/
    private void updateSunLensFlare(float currentTime) {
        if (currentTime >= 8 && currentTime < 12) {
            this.sunFlareEffect.setIntensity((currentTime - 8) / 4);
        } else if (currentTime >= 12 && currentTime < 16) {
            this.sunFlareEffect.setIntensity(1 - (currentTime - 12) / 4);
        } else if (currentTime >= 16 || currentTime < 8) {
            this.sunFlareEffect.setIntensity(0);
        }
    }

    /**
	* Increase or decrease the moon's lens flare effect intensity based on the given current time.
	* The moon's maximum intensity is 0.2.
	* @param currentTime The current time.
	*/
    private void updateMoonLensFlare(float currentTime) {
        if (currentTime >= 20 && currentTime < 24) {
            this.moonFlareEffect.setIntensity(((currentTime - 20) / 4) / 5);
        } else if (currentTime >= 0 && currentTime < 4) {
            this.moonFlareEffect.setIntensity((1 - (currentTime - 0) / 4) / 5);
        } else if (currentTime >= 4 || currentTime < 20) {
            this.moonFlareEffect.setIntensity(0);
        }
    }

    /**
	* Increase or decrease the alpha of all sides of star effect skybox based on the given current time.
	* @param currentTime The current time.
	*/
    private void updateStarEffect(float currentTime) {
        if (currentTime >= 20 && currentTime < 24) {
            this.starEffect.getFace(Skybox.Face.North).getDefaultColor().a = (currentTime - 20) / 4;
            this.starEffect.getFace(Skybox.Face.East).getDefaultColor().a = (currentTime - 20) / 4;
            this.starEffect.getFace(Skybox.Face.South).getDefaultColor().a = (currentTime - 20) / 4;
            this.starEffect.getFace(Skybox.Face.West).getDefaultColor().a = (currentTime - 20) / 4;
            this.starEffect.getFace(Skybox.Face.Up).getDefaultColor().a = (currentTime - 20) / 4;
        } else if (currentTime >= 0 && currentTime < 4) {
            this.starEffect.getFace(Skybox.Face.North).getDefaultColor().a = 1 - (currentTime - 0) / 4;
            this.starEffect.getFace(Skybox.Face.East).getDefaultColor().a = 1 - (currentTime - 0) / 4;
            this.starEffect.getFace(Skybox.Face.South).getDefaultColor().a = 1 - (currentTime - 0) / 4;
            this.starEffect.getFace(Skybox.Face.West).getDefaultColor().a = 1 - (currentTime - 0) / 4;
            this.starEffect.getFace(Skybox.Face.Up).getDefaultColor().a = 1 - (currentTime - 0) / 4;
        } else if (currentTime >= 4 || currentTime < 20) {
            this.starEffect.getFace(Skybox.Face.North).getDefaultColor().a = 0;
            this.starEffect.getFace(Skybox.Face.East).getDefaultColor().a = 0;
            this.starEffect.getFace(Skybox.Face.South).getDefaultColor().a = 0;
            this.starEffect.getFace(Skybox.Face.West).getDefaultColor().a = 0;
            this.starEffect.getFace(Skybox.Face.Up).getDefaultColor().a = 0;
        }
    }

    /**
	* Initialize the sky dome with the given model node.
	* @param model The model of the skydome including the dome modela and the plane model.
	*/
    private void initializeSkyDome(Node model) {
        model.setLocalTranslation(new Vector3f(0, this.radius / 2, 0));
        model.setIsCollidable(false);
        this.buildCloudsAnimation(model);
        this.setModes(model);
        this.attachChild(model);
    }

    /**
	* Initialize the sky dome which contains a dome and a plane.
     * @param domeTexture The texture used for the sky dome.
     * @param planeTexture The texture used for the plane under the sky dome.
	*/
    private void initializeSkyDome(Texture domeTexture, Texture planeTexture) {
        Node model = new Node("SkydomeModel");
        model.attachChild(this.buildDome(domeTexture));
        model.attachChild(this.buildPlane(planeTexture));
        this.buildCloudsAnimation(model);
        this.setModes(model);
        this.attachChild(model);
    }

    /**
	* Build the clouds animation which is a simple texture translation.
	* @param dome The dome of sky dome.
	*/
    private void buildCloudsAnimation(Spatial dome) {
        SpatialTransformer animator = new SpatialTransformer(1);
        animator.setObject(dome, 0, -1);
        Quaternion start = new Quaternion();
        start.fromAngleAxis(0, new Vector3f(0, 1, 0));
        animator.setRotation(0, 0, start);
        Quaternion middle = new Quaternion();
        middle.fromAngleAxis(FastMath.DEG_TO_RAD * 180, new Vector3f(0, 1, 0));
        animator.setRotation(0, this.cycle / 30, middle);
        Quaternion end = new Quaternion();
        end.fromAngleAxis(FastMath.DEG_TO_RAD * 360, new Vector3f(0, 1, 0));
        animator.setRotation(0, this.cycle / 15, end);
        animator.setActive(true);
        animator.setRepeatType(Controller.RT_WRAP);
        animator.interpolateMissing();
        dome.addController(animator);
    }

    /**
	* Build the dome as the sky.
	* @param domeTexture The texture used for the sky dome.
	*/
    private Dome buildDome(Texture domeTexture) {
        Dome dome = new Dome("Sky", new Vector3f(0, 0, 0), 64, 64, this.radius, true);
        dome.setLocalTranslation(new Vector3f(0, 0, 0));
        dome.setModelBound(new BoundingBox());
        dome.updateModelBound();
        dome.setIsCollidable(false);
        TextureState ts = this.renderer.createTextureState();
        ts.setTexture(domeTexture);
        ts.setEnabled(true);
        ts.apply();
        dome.setRenderState(ts);
        return dome;
    }

    /**
	* Build the plane under the sky dome.
	* @param planeTexture The texture used for the plane under the sky dome.
	*/
    private Disk buildPlane(Texture planeTexture) {
        Disk plane = new Disk("Ground", 64, 64, 11 * this.radius / 10);
        plane.setLocalTranslation(new Vector3f(0, 0, 0));
        Quaternion rotation = new Quaternion();
        rotation.fromAngleAxis(270 * FastMath.DEG_TO_RAD, new Vector3f(1, 0, 0));
        plane.setLocalRotation(rotation);
        plane.setModelBound(new BoundingBox());
        plane.updateModelBound();
        plane.setIsCollidable(false);
        TextureState ts = this.renderer.createTextureState();
        ts.setTexture(planeTexture);
        ts.setEnabled(true);
        ts.apply();
        plane.setRenderState(ts);
        return plane;
    }

    /**
	* Initialize the astronomy system which includes a sun system and a moon system.
	*/
    private void initializeAstronomySystem() {
        Node astronomyNode = new Node("AstronomyCenter");
        astronomyNode.setLocalTranslation(new Vector3f(0, 0, 0));
        this.astronomyLightState = this.renderer.createLightState();
        this.astronomyLightState.setTwoSidedLighting(true);
        this.buildSunSystem(astronomyNode);
        this.buildMoonSystem(astronomyNode);
        this.buildAstronomyAnimation(astronomyNode);
        this.attachChild(astronomyNode);
    }

    /**
	* Build the sun system which contains a point light outside the sky dome and a lens
	* flare effect inside the sky dome.
	* @param astronomyNode The astronomy center node.
	*/
    private void buildSunSystem(Node astronomyNode) {
        Node sunSystem = new Node("SunSystem");
        sunSystem.attachChild(this.buildSun());
        sunSystem.attachChild(this.buildSunLensFlare());
        astronomyNode.attachChild(sunSystem);
    }

    /**
	* Build the sun.
	* @return The light node contains the sun.
	*/
    private SimpleLightNode buildSun() {
        this.sunlight = new PointLight();
        this.sunlight.setEnabled(true);
        this.sunlight.setDiffuse(new ColorRGBA(0, 0, 0, 0));
        this.sunlight.setAmbient(ColorRGBA.gray);
        this.sunlight.setAttenuate(false);
        this.sunlight.setShadowCaster(true);
        SimpleLightNode sun = new SimpleLightNode("Sun", this.sunlight);
        sun.setLocalTranslation(new Vector3f(0, -20 * this.radius, 0));
        this.astronomyLightState.attach(this.sunlight);
        return sun;
    }

    /**
	* Build the sun's lens flare effect.
	* @return The light node contains the sun's lens flare effect.
	*/
    private Node buildSunLensFlare() {
        Node sunLensFlare = new Node("SunLensFlare");
        sunLensFlare.setLocalTranslation(new Vector3f(0, -95 * this.radius / 100, 0));
        TextureState[] textureStates = new TextureState[4];
        Texture[] texs = new Texture[4];
        String directory = getClass().getClassLoader().getResource("com/erickpardal/game/fpsdemo/data/textures/scene/lensflare/").getPath();
        for (int i = 0; i < textureStates.length; i++) {
            textureStates[i] = this.renderer.createTextureState();
            int flareIndex = i + 1;
            String fileName = directory + "flare" + flareIndex;
            texs[i] = TextureManager.loadTexture(fileName, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
            textureStates[i].setTexture(texs[i]);
            textureStates[i].setEnabled(true);
            textureStates[i].apply();
        }
        this.sunFlareEffect = LensFlareFactory.createBasicLensFlare("LensFlareEffect", textureStates);
        this.sunFlareEffect.setIntensity(0);
        this.sunFlareEffect.updateRenderState();
        sunLensFlare.attachChild(this.sunFlareEffect);
        return sunLensFlare;
    }

    /**
	* Build the moon system which contains a point light outside the sky dome and a lens
	* flare effect inside the sky dome.
	* @param astronomyNode The astronomy center node.
	*/
    private void buildMoonSystem(Node astronomyNode) {
        Node moonSystem = new Node("MoonSystem");
        moonSystem.attachChild(this.buildMoon());
        moonSystem.attachChild(this.buildMoonLensFlare());
        astronomyNode.attachChild(moonSystem);
    }

    /**
	* Build the moon.
	*/
    private SimpleLightNode buildMoon() {
        this.moonlight = new PointLight();
        this.moonlight.setEnabled(true);
        this.moonlight.setDiffuse(new ColorRGBA(this.mColor[0], this.mColor[1], this.mColor[2], this.mColor[3]));
        this.moonlight.setAmbient(ColorRGBA.gray);
        this.moonlight.setAttenuate(false);
        this.moonlight.setShadowCaster(true);
        SimpleLightNode moon = new SimpleLightNode("Moon", this.moonlight);
        moon.setLocalTranslation(new Vector3f(0, 20 * this.radius, 0));
        this.astronomyLightState.attach(this.moonlight);
        return moon;
    }

    /**
	* Build the moon's lens flare effect.
	* @return The light node contains the moon's lens flare effect.
	*/
    private Node buildMoonLensFlare() {
        Node moonLensFlare = new Node("MoonLensFlare");
        moonLensFlare.setLocalTranslation(new Vector3f(0, 95 * this.radius / 100, 0));
        TextureState[] textureStates = new TextureState[4];
        Texture[] texs = new Texture[4];
        String directory = "";
        for (int i = 0; i < textureStates.length; i++) {
            textureStates[i] = this.renderer.createTextureState();
            int flareIndex = i + 1;
            String fileName = directory + "flare" + flareIndex;
            texs[i] = TextureManager.loadTexture(fileName, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
            textureStates[i].setTexture(texs[i]);
            textureStates[i].setEnabled(true);
            textureStates[i].apply();
        }
        this.moonFlareEffect = LensFlareFactory.createBasicLensFlare("LensFlareEffect", textureStates);
        this.moonFlareEffect.setIntensity(0.2f);
        this.moonFlareEffect.updateRenderState();
        moonLensFlare.attachChild(this.moonFlareEffect);
        return moonLensFlare;
    }

    /**
	* Build the rotational astronomy animation.
	* @param astronomyNode The astronomy center node.
	*/
    private void buildAstronomyAnimation(Node astronomyNode) {
        this.astronomyAnimator = new SpatialTransformer(1);
        this.astronomyAnimator.setObject(astronomyNode, 0, -1);
        Quaternion midNight = new Quaternion();
        midNight.fromAngleAxis(0, new Vector3f(0, 0, 1));
        this.astronomyAnimator.setRotation(0, 0, midNight);
        Quaternion noon = new Quaternion();
        noon.fromAngleAxis(FastMath.DEG_TO_RAD * 180, new Vector3f(0, 0, 1));
        this.astronomyAnimator.setRotation(0, this.cycle / 2, noon);
        Quaternion finalMidNight = new Quaternion();
        finalMidNight.fromAngleAxis(FastMath.DEG_TO_RAD * 360, new Vector3f(0, 0, 1));
        this.astronomyAnimator.setRotation(0, this.cycle, finalMidNight);
        this.astronomyAnimator.setActive(true);
        this.astronomyAnimator.setRepeatType(Controller.RT_WRAP);
        this.astronomyAnimator.interpolateMissing();
        astronomyNode.addController(this.astronomyAnimator);
    }

    /**
	* Initialize the star effect with given star texture.
	* @param starTexture The texture used for the star effect.
	* @return The star effect skybox.
	*/
    private void initializeStarEffect(Texture starTexture) {
        this.starEffect = new Skybox("StarEffect", this.radius / 2, this.radius / 2, this.radius / 2);
        this.starEffect.setTexture(Skybox.Face.North, starTexture);
        this.starEffect.setTexture(Skybox.Face.East, starTexture);
        this.starEffect.setTexture(Skybox.Face.South, starTexture);
        this.starEffect.setTexture(Skybox.Face.West, starTexture);
        this.starEffect.setTexture(Skybox.Face.Up, starTexture);
        this.starEffect.setTexture(Skybox.Face.Down, starTexture);
        this.starEffect.setLocalTranslation(new Vector3f(0, 9 * this.radius / 10, 0));
        this.starEffect.getFace(Skybox.Face.Down).setDefaultColor(new ColorRGBA(0, 0, 0, 0));
        BlendState blend = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        blend.setBlendEnabled(true);
        blend.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        blend.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        blend.setTestEnabled(true);
        blend.setTestFunction(BlendState.TestFunction.GreaterThan);
        blend.setEnabled(true);
        this.starEffect.setRenderState(blend);
        this.starEffect.updateRenderState();
        this.starEffect.preloadTextures();
        this.attachChild(this.starEffect);
    }

    /**
	* Set up the essential render states.
	*/
    private void setupRenderStates() {
        this.setModes(this);
        ZBufferState zbuff = this.renderer.createZBufferState();
        zbuff.setWritable(false);
        zbuff.setEnabled(true);
        zbuff.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        this.setRenderState(zbuff);
        this.updateRenderState();
    }

    /**
	* Set the texture combine mode, render queue mode, and cull mode for the given spatial.
	* @param spatial The spatial needs to be set with the modes.
	*/
    private void setModes(Spatial spatial) {
        spatial.setTextureCombineMode(TextureCombineMode.Replace);
        spatial.setRenderQueueMode(Renderer.QUEUE_SKIP);
        spatial.setCullHint(CullHint.Never);
    }

    /**
	* Convert the given String representation of time into the animation time system.
	* @param currentTime The String representation of time in the form of <HH.mm>.
	* @return The converted time in animation time system.
	*/
    private float toAnimationTime(String currentTime) {
        int hour = Integer.valueOf(currentTime.substring(0, currentTime.indexOf(".")));
        int minute = Integer.valueOf(currentTime.substring(currentTime.indexOf(".") + 1, currentTime.length()));
        float totalMinute = hour * 60 + minute;
        float animationTime = (totalMinute / (24 * 60)) * this.cycle;
        return animationTime;
    }

    /**
	* Set the sky dome time with the given String. This method is typically used to
	* synchronize the sky dome time amoung all the clients over the network.
	* @param currentTime The String representation of earth time to be set.<p>
	* Note: The form of the argument has to be <HH.mm>.
	*/
    public void setCurrentTime(String currentTime) {
        float animationTime = this.toAnimationTime(currentTime);
        this.astronomyAnimator.setCurTime(animationTime);
    }

    /**
	* Retrieve the current time. Typically the minutes should not be converted if the
	* returned value is used to set a scale value.
	* @param convertMinutes True if the returned time value should be in 24 hour 60 minute system.
	* 						False if the returned time value should be in 24 hour 100 minute system.
	* @return The current time.
	*/
    public float getCurrentTime(boolean convertMinutes) {
        String rawTimeStr = String.valueOf(this.astronomyAnimator.getCurTime() / (this.cycle / 24));
        if (convertMinutes) {
            String hourStr = rawTimeStr.substring(0, rawTimeStr.lastIndexOf("."));
            int hour = Integer.valueOf(hourStr);
            DecimalFormat minuteFormatter = new DecimalFormat("#.##");
            String rawMinuteStr = "0" + rawTimeStr.substring(rawTimeStr.lastIndexOf("."), rawTimeStr.length());
            String minuteStr = minuteFormatter.format(60 * Float.valueOf(rawMinuteStr) / 100);
            float minute = Float.valueOf(minuteStr);
            if (minute == 0.6f) {
                minute = 1;
            }
            float rawEarthTime = hour + minute;
            if (rawEarthTime == 24) {
                rawEarthTime = 0.0f;
            }
            DecimalFormat timeFormatter = new DecimalFormat("##.##");
            String earthTimeStr = timeFormatter.format(rawEarthTime);
            return Float.valueOf(earthTimeStr);
        } else {
            return Float.valueOf(rawTimeStr);
        }
    }

    /**
	* Retrieve the astronomy LightState which contains the sunlight and moonlight.
	* @return The astronomy LightState.
	*/
    public LightState getLighting() {
        return this.astronomyLightState;
    }

    /**
	* Check if the current earth time is day time.
	* @return True if the current earth time is day time. False otherwise.
	*/
    public boolean isDayTime() {
        float currentEarthTime = this.getCurrentTime(true);
        return (currentEarthTime >= 6 && currentEarthTime < 18);
    }

    /**
	* Check if the current earth time is night time.
	* @return True if the current earth time is night time. False otherwise.
	*/
    public boolean isNightTime() {
        float currentEarthTime = this.getCurrentTime(true);
        return (currentEarthTime >= 18 || currentEarthTime < 6);
    }
}
