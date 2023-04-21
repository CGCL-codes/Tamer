package game.visualizations;

import game.gui.GraphCanvas;
import game.gui.Controls;
import game.data.GlobalData;
import game.utils.Colors;
import game.models.Models;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import java.awt.*;

/**
 * This class contains all settings for Classification3D. It is a singleton.
 * <p/>
 * It's content is modified by setter methods from Classifcation3DPanel and from the application as well (show spots, show axis).
 * <p/>
 * Most of the setters invoke some other methods, so encapsualted appearances get refreshed.
 */
public class Classification3DvisualConfiguration {

    public static final int CLASSMEMBERSHIP_LEVELS = 10;

    public static final int COLORINGMODE_SINGLECOLOR = 0;

    public static final int COLORINGMODE_SCALE = 1;

    public static final int COLORINGMODE_USERCOLORS = 2;

    public static final int COLORINGMODE_RANDOMCOLORS = 3;

    private static float colorAmbientMax = 0.5f;

    private static float colorDiffuseMax = 0.5f;

    private float shininess = 0;

    private double cutoffBelow;

    private double cutoffAbove;

    private float coreTransparency;

    private float cutoffTransparency;

    private int coloringMode;

    private float colorModelR;

    private float colorModelG;

    private float colorModelB;

    private float colorR[];

    private float colorG[];

    private float colorB[];

    private boolean backFaceCulling;

    private boolean showAxis = true;

    private boolean showSpots = true;

    private int res_x = 50;

    private int res_y = 50;

    private int res_z = 50;

    private double klobouk = 0;

    private double maxSpotSize = 0.05;

    private Appearance[] appearances;

    private Appearance[] appearancesBorders;

    private Appearance[] spotAppearances;

    private static Classification3DvisualConfiguration singleton;

    /**
     * Inits the singleton with application values.
     *
     * @param gc
     */
    public static void initSingleton(GraphCanvas gc) {
        Classification3DvisualConfiguration visualConfiguration = getVisualConfiguration();
        for (int i = 0; i < Models.getInstance().getModelsNumber(); i++) {
            if (Controls.getInstance().getGnet() == Models.getInstance().getModel(i)) {
                visualConfiguration.setModelColor(Colors.color[i % GlobalData.getInstance().getONumber()]);
                break;
            }
        }
        visualConfiguration.recreateSpotAppearances(gc);
        visualConfiguration.setShowAxis(gc.isCoord());
        visualConfiguration.setShowSpots(GraphCanvas.getInstance().isPointsEnabled());
        visualConfiguration.setKlobouk(0.1);
    }

    /**
     * Singleton getter.
     *
     * @return
     */
    public static synchronized Classification3DvisualConfiguration getVisualConfiguration() {
        if (singleton != null) return singleton; else {
            singleton = createDefault();
            return singleton;
        }
    }

    /**
     * Static creator of the singleton.
     *
     * @return
     */
    private static Classification3DvisualConfiguration createDefault() {
        Classification3DvisualConfiguration cfg = new Classification3DvisualConfiguration();
        cfg.cutoffBelow = 1.0f;
        cfg.cutoffAbove = 1.0f;
        cfg.coreTransparency = 0.0f;
        cfg.cutoffTransparency = 0.9f;
        cfg.coloringMode = COLORINGMODE_SCALE;
        cfg.backFaceCulling = false;
        cfg.appearances = new Appearance[CLASSMEMBERSHIP_LEVELS];
        cfg.appearancesBorders = new Appearance[CLASSMEMBERSHIP_LEVELS];
        cfg.createAppearances();
        cfg.colorR = new float[CLASSMEMBERSHIP_LEVELS];
        cfg.colorG = new float[CLASSMEMBERSHIP_LEVELS];
        cfg.colorB = new float[CLASSMEMBERSHIP_LEVELS];
        cfg.colorR[0] = (float) 5 / 255;
        cfg.colorG[0] = (float) 5 / 255;
        cfg.colorB[0] = (float) 5 / 255;
        cfg.colorR[1] = (float) 10 / 255;
        cfg.colorG[1] = (float) 10 / 255;
        cfg.colorB[1] = (float) 10 / 255;
        cfg.colorR[2] = (float) 2 / 255;
        cfg.colorG[2] = (float) 14 / 255;
        cfg.colorB[2] = (float) 0 / 255;
        cfg.colorR[3] = (float) 0 / 255;
        cfg.colorG[3] = (float) 19 / 255;
        cfg.colorB[3] = (float) 1 / 255;
        cfg.colorR[4] = (float) 0 / 255;
        cfg.colorG[4] = (float) 0 / 255;
        cfg.colorB[4] = (float) 24 / 255;
        cfg.colorR[5] = (float) 0 / 255;
        cfg.colorG[5] = (float) 1 / 255;
        cfg.colorB[5] = (float) 36 / 255;
        cfg.colorR[6] = (float) 59 / 255;
        cfg.colorG[6] = (float) 0 / 255;
        cfg.colorB[6] = (float) 0 / 255;
        cfg.colorR[7] = (float) 71 / 255;
        cfg.colorG[7] = (float) 0 / 255;
        cfg.colorB[7] = (float) 0 / 255;
        cfg.colorR[8] = (float) 107 / 255;
        cfg.colorG[8] = (float) 0 / 255;
        cfg.colorB[8] = (float) 0 / 255;
        cfg.colorR[9] = (float) 227 / 255;
        cfg.colorG[9] = (float) 0 / 255;
        cfg.colorB[9] = (float) 0 / 255;
        return cfg;
    }

    /**
     * @param responseLevel A value between (including) 1 and Classification3Dvisual.CLASSMEMBERSHIP_LEVELS
     * @return
     */
    public Appearance getAppearance(int responseLevel) {
        return appearances[responseLevel - 1];
    }

    /**
     * @param responseLevel A value between (including) 1 and Classification3Dvisual.CLASSMEMBERSHIP_LEVELS
     * @return
     */
    public Appearance getBorderAppearance(int responseLevel) {
        return appearancesBorders[responseLevel - 1];
    }

    public float getColorR(int i) {
        return colorR[i];
    }

    public float getColorG(int i) {
        return colorG[i];
    }

    public float getColorB(int i) {
        return colorB[i];
    }

    public void setColorR(int i, float colorR) {
        this.colorR[i] = colorR;
    }

    public void setColorG(int i, float colorG) {
        this.colorG[i] = colorG;
    }

    public void setColorB(int i, float colorB) {
        this.colorB[i] = colorB;
    }

    public void setCutoffBelow(double cutoffBelow) {
        if (this.cutoffBelow != cutoffBelow) {
            this.cutoffBelow = cutoffBelow;
            updateAllTransparencyLevels();
        }
    }

    public void setCutoffAbove(double cutoffAbove) {
        if (this.cutoffAbove != cutoffAbove) {
            this.cutoffAbove = cutoffAbove;
            updateAllTransparencyLevels();
        }
    }

    public void setCoreTransparency(float coreTransparency) {
        if (this.coreTransparency != coreTransparency) {
            this.coreTransparency = coreTransparency;
            updateAllTransparencyLevels();
        }
    }

    public void setCutoffTransparency(float cutoffTransparency) {
        if (this.cutoffTransparency != cutoffTransparency) {
            this.cutoffTransparency = cutoffTransparency;
            updateAllTransparencyLevels();
        }
    }

    public void setColoringMode(int coloringMode) {
        if (this.coloringMode != coloringMode) {
            this.coloringMode = coloringMode;
            recreateAllColors();
        }
    }

    public void setBackFaceCulling(boolean backFaceCulling) {
        if (this.backFaceCulling != backFaceCulling) {
            this.backFaceCulling = backFaceCulling;
            for (int i = 0; i < appearances.length; i++) {
                PolygonAttributes pa = appearances[i].getPolygonAttributes();
                if (!backFaceCulling) {
                    pa.setCullFace(PolygonAttributes.CULL_NONE);
                    pa.setBackFaceNormalFlip(true);
                } else {
                    pa.setCullFace(PolygonAttributes.CULL_BACK);
                    pa.setBackFaceNormalFlip(false);
                }
                pa = appearancesBorders[i].getPolygonAttributes();
                if (!backFaceCulling) {
                    pa.setCullFace(PolygonAttributes.CULL_NONE);
                    pa.setBackFaceNormalFlip(true);
                } else {
                    pa.setCullFace(PolygonAttributes.CULL_BACK);
                    pa.setBackFaceNormalFlip(false);
                }
            }
        }
    }

    public double getCutoffBelow() {
        return cutoffBelow;
    }

    public double getCutoffAbove() {
        return cutoffAbove;
    }

    public float getCoreTransparency() {
        return coreTransparency;
    }

    public float getCutoffTransparency() {
        return cutoffTransparency;
    }

    public int getColoringMode() {
        return coloringMode;
    }

    public boolean isBackFaceCulling() {
        return backFaceCulling;
    }

    public int getRes_x() {
        return res_x;
    }

    public int getRes_y() {
        return res_y;
    }

    public int getRes_z() {
        return res_z;
    }

    public double getKlobouk() {
        return this.klobouk;
    }

    public double getMaxSpotSize() {
        return maxSpotSize;
    }

    public boolean isShowAxis() {
        return showAxis;
    }

    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
    }

    public boolean isShowSpots() {
        return showSpots;
    }

    public void setShowSpots(boolean showSpots) {
        if (this.showSpots != showSpots) {
            this.showSpots = showSpots;
            for (Appearance spotAppearance : spotAppearances) {
                if (showSpots) {
                    spotAppearance.getLineAttributes().setPatternMask(0xffff);
                } else {
                    spotAppearance.getLineAttributes().setPatternMask(0);
                }
            }
        }
    }

    void setModelColor(Color modelColor) {
        if (modelColor != null) {
            this.colorModelR = (float) modelColor.getRed() / 255;
            this.colorModelG = (float) modelColor.getGreen() / 255;
            this.colorModelB = (float) modelColor.getBlue() / 255;
        }
        recreateAllColors();
    }

    public void setKlobouk(double klobouk) {
        this.klobouk = klobouk;
    }

    public Appearance getSpotAppearance(int outputIndex) {
        return spotAppearances[outputIndex];
    }

    /**
     * Creates all appearances based on current configuration.
     */
    private void createAppearances() {
        for (int i = 0; i < appearances.length; i++) {
            float responseLevel = computeResponseLevel(i);
            appearances[i] = new Appearance();
            appearancesBorders[i] = new Appearance();
            appearances[i].setCapability(Appearance.ALLOW_MATERIAL_READ);
            appearancesBorders[i].setCapability(Appearance.ALLOW_MATERIAL_READ);
            Material material = new Material();
            material.setAmbientColor(computeAmbientColor(responseLevel, i));
            material.setSpecularColor(new Color3f(0.5f, 0.5f, 0.5f));
            material.setDiffuseColor(computeDiffuseColor(responseLevel, i));
            material.setEmissiveColor(0, 0, 0);
            material.setShininess(shininess);
            material.setCapability(Material.ALLOW_COMPONENT_WRITE);
            appearances[i].setMaterial(material);
            appearancesBorders[i].setMaterial(material);
            PolygonAttributes pa = new PolygonAttributes();
            appearances[i].setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
            appearancesBorders[i].setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
            appearances[i].setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
            appearancesBorders[i].setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
            appearances[i].setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
            appearancesBorders[i].setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
            pa.setCapability(PolygonAttributes.ALLOW_CULL_FACE_WRITE);
            pa.setCapability(PolygonAttributes.ALLOW_NORMAL_FLIP_WRITE);
            if (!backFaceCulling) {
                pa.setCullFace(PolygonAttributes.CULL_NONE);
                pa.setBackFaceNormalFlip(true);
            } else {
                pa.setCullFace(PolygonAttributes.CULL_BACK);
                pa.setBackFaceNormalFlip(false);
            }
            appearances[i].setPolygonAttributes(pa);
            appearancesBorders[i].setPolygonAttributes(pa);
            updateTransparency(i);
            updateBorderTransparency(i);
        }
    }

    /**
     * Updates transparency of a appIndex layer's appearance..
     *
     * @param appIndex
     */
    private void updateTransparency(int appIndex) {
        float responseLevel = computeResponseLevel(appIndex);
        float transparency = computeTransparency(responseLevel);
        if (transparency == 0) {
            appearances[appIndex].setTransparencyAttributes(null);
        } else {
            TransparencyAttributes ta = appearances[appIndex].getTransparencyAttributes();
            if (ta == null) {
                ta = new TransparencyAttributes(TransparencyAttributes.NICEST, transparency);
                ta.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
                appearances[appIndex].setTransparencyAttributes(ta);
            } else {
                ta.setTransparency(transparency);
            }
        }
    }

    /**
     * Updates transparency of a appIndex layer's appearance..
     *
     * @param appIndex
     */
    private void updateBorderTransparency(int appIndex) {
        float responseLevel = computeResponseLevel(appIndex);
        float transparency = computeTransparency(responseLevel);
        if (transparency == 0) {
            appearancesBorders[appIndex].setTransparencyAttributes(null);
        } else {
            TransparencyAttributes ta = appearancesBorders[appIndex].getTransparencyAttributes();
            if (ta == null) {
                ta = new TransparencyAttributes(TransparencyAttributes.NICEST, transparency);
                ta.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
                appearancesBorders[appIndex].setTransparencyAttributes(ta);
            } else {
                ta.setTransparency(transparency);
            }
        }
    }

    /**
     * Computes response level.
     *
     * @param appIndex
     * @return
     */
    private static float computeResponseLevel(int appIndex) {
        return (float) (appIndex + 1) / CLASSMEMBERSHIP_LEVELS;
    }

    /**
     * Computes transparency for given responseLevel.
     *
     * @param responseLevel
     * @return Returns coreTransparency if the level belongs to core and cutoffTransparency if the level belongs to cutoff.
     */
    private float computeTransparency(float responseLevel) {
        float f;
        if (isCore(responseLevel)) return coreTransparency; else f = cutoffTransparency;
        return f;
    }

    /**
     * Determines wheter the responseLevel belongs to core or cut-off.
     *
     * @param responseLevel
     * @return True if responseLevel belongs to core.
     */
    private boolean isCore(float responseLevel) {
        return (responseLevel >= (float) cutoffBelow) && (responseLevel <= (float) cutoffAbove);
    }

    /**
     * Updates all transparency levels based on current configuration.
     */
    private void updateAllTransparencyLevels() {
        for (int i = 0; i < appearances.length; i++) {
            updateTransparency(i);
            updateBorderTransparency(i);
        }
    }

    /**
     * Refreshes colors in all Appearances.
     */
    public void recreateAllColors() {
        for (int i = 0; i < appearances.length; i++) {
            Appearance appearance = appearances[i];
            float responseLevel = computeResponseLevel(i);
            appearance.getMaterial().setAmbientColor(computeAmbientColor(responseLevel, i));
            appearance.getMaterial().setDiffuseColor(computeDiffuseColor(responseLevel, i));
        }
    }

    /**
     * Computes ambient color for responseLevel's appearance.
     *
     * @param responseLevel
     * @param i
     * @return
     */
    private Color3f computeAmbientColor(float responseLevel, int i) {
        switch(coloringMode) {
            case COLORINGMODE_SINGLECOLOR:
                return new Color3f(colorAmbientMax * colorModelR, colorAmbientMax * colorModelG, colorAmbientMax * colorModelB);
            case COLORINGMODE_SCALE:
                return new Color3f(colorAmbientMax * colorModelR * responseLevel, colorAmbientMax * colorModelG * responseLevel, colorAmbientMax * colorModelB * responseLevel);
            case COLORINGMODE_USERCOLORS:
                return new Color3f(colorR[i], colorG[i], colorB[i]);
            case COLORINGMODE_RANDOMCOLORS:
                return new Color3f((float) Math.random(), (float) Math.random(), (float) Math.random());
            default:
                return null;
        }
    }

    /**
     * Computes diffuse color for responseLevel's appearance.
     *
     * @param responseLevel
     * @param i
     * @return
     */
    private Color3f computeDiffuseColor(float responseLevel, int i) {
        switch(coloringMode) {
            case COLORINGMODE_SINGLECOLOR:
                return new Color3f(colorDiffuseMax * colorModelR, colorDiffuseMax * colorModelG, colorDiffuseMax * colorModelB);
            case COLORINGMODE_SCALE:
                return new Color3f(colorDiffuseMax * colorModelR * responseLevel, colorDiffuseMax * colorModelG * responseLevel, colorDiffuseMax * colorModelB * responseLevel);
            case COLORINGMODE_USERCOLORS:
                return new Color3f(colorR[i], colorG[i], colorB[i]);
            case COLORINGMODE_RANDOMCOLORS:
                return new Color3f((float) Math.random(), (float) Math.random(), (float) Math.random());
            default:
                return null;
        }
    }

    /**
     * Recreates all the appearances, that are used by data spots.
     *
     * @param gc
     */
    private void recreateSpotAppearances(GraphCanvas gc) {
        spotAppearances = new Appearance[GlobalData.getInstance().getONumber()];
        for (int i = 0; i < spotAppearances.length; i++) {
            spotAppearances[i] = new Appearance();
            spotAppearances[i].setCapability(Appearance.ALLOW_LINE_ATTRIBUTES_READ);
            LineAttributes la = new LineAttributes();
            la.setLinePattern(LineAttributes.PATTERN_USER_DEFINED);
            la.setCapability(LineAttributes.ALLOW_WIDTH_WRITE);
            la.setCapability(LineAttributes.ALLOW_PATTERN_WRITE);
            la.setPatternMask(0xffff);
            spotAppearances[i].setLineAttributes(la);
            spotAppearances[i].setColoringAttributes(new ColoringAttributes((float) Colors.color[i].getRed() / 255, (float) Colors.color[i].getGreen() / 255, (float) Colors.color[i].getBlue() / 255, ColoringAttributes.FASTEST));
        }
    }
}
