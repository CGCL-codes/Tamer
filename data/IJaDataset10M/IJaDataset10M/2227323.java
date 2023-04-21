package jsattrak.utilities;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.LayerList;
import java.awt.Point;
import jsattrak.gui.JSatTrak;
import name.gano.worldwind.geom.ECIRadialGrid;
import name.gano.worldwind.layers.Earth.EcefTimeDepRenderableLayer;

/**
 *
 * @author sgano
 */
public interface J3DEarthComponent {

    public boolean getTerrainProfileEnabled();

    public String getTerrainProfileSat();

    public double getTerrainProfileLongSpan();

    public void setTerrainProfileSat(String terrainProfileSat);

    public void setTerrainProfileLongSpan(double terrainProfileLongSpan);

    public void setTerrainProfileEnabled(boolean enabled);

    public boolean isViewModeECI();

    public void setViewModeECI(boolean viewModeECI);

    public JSatTrak getApp();

    public WorldWindow getWwd();

    public String getDialogTitle();

    public int getWwdWidth();

    public int getWwdHeight();

    public Point getWwdLocationOnScreen();

    public LayerList getLayerList();

    public void setOrbitFarClipDistance(double clipDist);

    public void setOrbitNearClipDistance(double clipDist);

    public double getOrbitFarClipDistance();

    public double getOrbitNearClipDistance();

    public boolean isModelViewMode();

    public void setModelViewMode(boolean modelViewMode);

    public String getModelViewString();

    public void setModelViewString(String modelViewString);

    public double getModelViewNearClip();

    public void setModelViewNearClip(double modelViewNearClip);

    public double getModelViewFarClip();

    public void setModelViewFarClip(double modelViewFarClip);

    public void resetWWJdisplay();

    public boolean isSmoothViewChanges();

    public void setSmoothViewChanges(boolean smoothViewChanges);

    public void setSunShadingOn(boolean useSunShading);

    public boolean isSunShadingOn();

    public void setAmbientLightLevel(int level);

    public int getAmbientLightLevel();

    public boolean isLensFlareEnabled();

    public void setLensFlare(boolean enabled);

    public EcefTimeDepRenderableLayer getEcefTimeDepRenderableLayer();

    public ECIRadialGrid getEciRadialGrid();
}
