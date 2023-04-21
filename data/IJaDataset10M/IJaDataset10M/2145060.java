package fr.itris.glips.extension.jwidget.trends.runtime.configuration;

import org.w3c.dom.*;
import java.util.*;
import java.util.prefs.*;
import java.awt.*;
import fr.itris.glips.extension.jwidget.trends.runtime.controller.*;
import fr.itris.glips.extension.jwidget.trends.runtime.model.database.HistoryField;
import fr.itris.glips.extension.jwidget.trends.runtime.model.database.HistoryManager;
import fr.itris.glips.extension.jwidget.trends.runtime.model.database.HistoryTable;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.colorsblinkings.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.config.*;

/**
 * the class providing all the configuration values for a curve
 * @author ITRIS, Jordi SUC
 */
public class TrendsCurveConfiguration {

    /**
	 * the id of the preferences
	 */
    protected static final String colorPrefId = "Color", displayScalePrefId = "DisplayScale", dashesPrefId = "Dashes", thicknessPrefId = "Thickness", interpolationTypePrefId = "InterpolationType", pointsTypePrefId = "PointsType";

    /**
	 * the controller
	 */
    private TrendsRuntimeController controller;

    /**
	 * the configuration object
	 */
    private TrendsConfiguration configuration;

    /**
	 * the curve element
	 */
    private Element curveElement;

    /**
	 * the listener to the changes of one of the properties
	 */
    private Set<CurvesConfigurationChangeListener> listeners = new HashSet<CurvesConfigurationChangeListener>();

    /**
	 * the tag name
	 */
    private String tagName;

    /**
	 * the history field corresponding to this tag name
	 */
    private HistoryField historyField;

    /**
	 * whether the tag name corresponds to an enumerated tag
	 */
    private boolean isEnumeratedTag;

    /**
	 * the list of the enumerated tag values, if this tag is an enumerated one
	 */
    private LinkedList<String> enumeratedTagValues = new LinkedList<String>();

    /**
	 * the color of the curve
	 */
    private Color color;

    /**
	 * the thickness of the curve
	 */
    private int thickness;

    /**
	 * the color of the curve when it is in an invalid state
	 */
    private Color colorForInvalid;

    /**
	 * the thickness of the curve when it is in an invalid state
	 */
    private int thicknessForInvalid;

    /**
	 * the stroke object
	 */
    private Stroke stroke;

    /**
	 * the stroke for invalid object
	 */
    private Stroke strokeForInvalid;

    /**
	 * the label
	 */
    private String label;

    /**
	 * the dashes 
	 */
    private String dashes;

    /**
	 * the point type
	 */
    private String pointType;

    /**
	 * the interpolation type
	 */
    private String interpolationType;

    /**
	 * the dashes for the invalid state
	 */
    private String dashesForInvalid;

    /**
	 * whether the scale for this curve should be displayed
	 */
    private boolean displayScale;

    /**
	 * the minimum value
	 */
    private TrendsDataLimit minLimit;

    /**
	 * the maximum value
	 */
    private TrendsDataLimit maxLimit;

    /**
	 * the constructor of the class
	 * @param controller the trends runtime controller
	 * @param configuration the configuration object
	 * @param curveElement the curve element
	 */
    public TrendsCurveConfiguration(TrendsRuntimeController controller, TrendsConfiguration configuration, Element curveElement) {
        this.controller = controller;
        this.configuration = configuration;
        this.curveElement = curveElement;
        computeStaticValues();
        computeValues();
    }

    /**
	 * computes the values (that could not change) that are specified in the jwidget element
	 */
    protected void computeStaticValues() {
        tagName = curveElement.getAttribute("tag");
        isEnumeratedTag = (curveElement.getNodeName().equals("rtda:stateCurve"));
        SVGPicture picture = controller.getJwidgetRuntime().getPicture();
        ConfigurationDocument confDoc = picture.getMainDisplay().getPictureManager().getConfigurationDocument(picture);
        if (confDoc != null) {
            if (isEnumeratedTag) {
                enumeratedTagValues.clear();
                LinkedList<String> tagValues = confDoc.getChildValues(tagName);
                if (tagValues != null) {
                    enumeratedTagValues.addAll(tagValues);
                }
            }
        }
    }

    /**
	 * computing the data base values
	 */
    public void computeDatabaseValues() {
        SVGPicture picture = controller.getJwidgetRuntime().getPicture();
        ConfigurationDocument confDoc = picture.getMainDisplay().getPictureManager().getConfigurationDocument(picture);
        if (confDoc != null) {
            Element tagElement = confDoc.getElement(tagName);
            if (tagElement != null) {
                String historyManagerName = tagElement.getAttribute("historymanager");
                String historyTableName = tagElement.getAttribute("historytable");
                String historyTagName = tagElement.getAttribute("historytagname");
                HistoryManager historyManager = controller.getModel().getHistoryManager(historyManagerName);
                if (historyManager != null) {
                    HistoryTable historyTable = historyManager.getHistoryTable(historyTableName);
                    if (historyTable != null) {
                        historyField = new HistoryField(historyTable, tagName, historyTagName);
                    }
                }
            }
        }
    }

    /**
	 * retrieves and computes all the values that are specified in the jwidget element
	 */
    protected void computeValues() {
        String colorString = PreferencesStore.getPreference(getPreferencesNodeId(), colorPrefId);
        if (colorString == null || colorString.equals("")) {
            colorString = curveElement.getAttribute("color");
        }
        color = ColorsAndBlinkingsToolkit.getColor(colorString);
        if (color == null) {
            color = Color.white;
        }
        try {
            String thicknessString = PreferencesStore.getPreference(getPreferencesNodeId(), thicknessPrefId);
            if (thicknessString == null || thicknessString.equals("")) {
                thicknessString = curveElement.getAttribute("thickness");
            }
            thickness = Integer.parseInt(thicknessString);
        } catch (Exception ex) {
        }
        colorForInvalid = ColorsAndBlinkingsToolkit.getColor(curveElement.getAttribute("colorForInvalid"));
        try {
            thicknessForInvalid = Integer.parseInt(curveElement.getAttribute("thicknessForInvalid"));
        } catch (Exception ex) {
        }
        label = curveElement.getAttribute("curveLabel");
        dashes = PreferencesStore.getPreference(getPreferencesNodeId(), dashesPrefId);
        if (dashes == null || dashes.equals("")) {
            dashes = curveElement.getAttribute("dash");
        }
        pointType = PreferencesStore.getPreference(getPreferencesNodeId(), pointsTypePrefId);
        if (pointType == null || pointType.equals("")) {
            pointType = curveElement.getAttribute("point");
        }
        interpolationType = PreferencesStore.getPreference(getPreferencesNodeId(), interpolationTypePrefId);
        if (interpolationType == null || interpolationType.equals("")) {
            interpolationType = curveElement.getAttribute("interpolation");
        }
        dashesForInvalid = curveElement.getAttribute("dashForInvalid");
        String displayScaleString = PreferencesStore.getPreference(getPreferencesNodeId(), displayScalePrefId);
        if (displayScaleString == null || displayScaleString.equals("")) {
            displayScaleString = curveElement.getAttribute("graduations");
        }
        try {
            displayScale = Boolean.parseBoolean(displayScaleString);
        } catch (Exception ex) {
        }
        computeStroke();
        float[] dashArray = getDashArray(dashesForInvalid);
        if (dashArray != null) {
            strokeForInvalid = new BasicStroke(thicknessForInvalid, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, dashArray, 0);
        } else {
            strokeForInvalid = new BasicStroke(thicknessForInvalid);
        }
        minLimit = new TrendsDataLimit(controller, tagName, curveElement.getAttribute("tagMin"), true);
        maxLimit = new TrendsDataLimit(controller, tagName, curveElement.getAttribute("tagMax"), true);
    }

    /**
	 * computes the strokes
	 */
    protected void computeStroke() {
        float[] dashArray = getDashArray(dashes);
        if (dashArray != null) {
            stroke = new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, dashArray, 0);
        } else {
            stroke = new BasicStroke(thickness);
        }
    }

    /**
	 * adds a new curves configuration change listener
	 * @param listener a new curves configuration change listener
	 */
    public void addCurvesConfigurationChangeListener(CurvesConfigurationChangeListener listener) {
        listeners.add(listener);
    }

    /**
	 * remove the curves configuration change listener
	 * @param listener the curves configuration change listener
	 */
    public void removeCurvesConfigurationChangeListener(CurvesConfigurationChangeListener listener) {
        listeners.remove(listener);
    }

    /**
	 * notifies that a change occured on the style properties of the curve
	 */
    protected void notifyStyleChanged() {
        for (CurvesConfigurationChangeListener listener : new HashSet<CurvesConfigurationChangeListener>(listeners)) {
            listener.curveStyleChanged();
        }
    }

    /**
	 * notifies that a change occured on the color property of the curve
	 */
    protected void notifyColorChanged() {
        for (CurvesConfigurationChangeListener listener : new HashSet<CurvesConfigurationChangeListener>(listeners)) {
            listener.curveColorChanged();
        }
    }

    /**
	 * notifies that a change occured on the scale properties of the curve
	 */
    protected void notifyScaleChanged() {
        for (CurvesConfigurationChangeListener listener : new HashSet<CurvesConfigurationChangeListener>(listeners)) {
            listener.curveScaleChanged();
        }
        configuration.computeCurrentTrendsCurveConfiguration();
    }

    /**
	 * @return the label of this curve
	 */
    public String getLabel() {
        return label;
    }

    /**
	 * @param label the new label
	 */
    public void setLabel(String label) {
        this.label = label;
        notifyStyleChanged();
    }

    /**
	 * @return the tag name
	 */
    public String getTagName() {
        return tagName;
    }

    /**
	 * @return the history field
	 */
    public HistoryField getHistoryField() {
        return historyField;
    }

    /**
	 * @return whether the values of the tag are recorded in the history manager
	 */
    public boolean isRecordedInHistoryManager() {
        return historyField != null;
    }

    /**
	 * @return the list of the enumerated tag values, if this tag is an enumerated one
	 */
    public LinkedList<String> getEnumeratedTagValues() {
        return enumeratedTagValues;
    }

    /**
	 * @return whether the tag  is an enumerated one
	 */
    public boolean isEnumeratedTag() {
        return isEnumeratedTag;
    }

    /**
	 * @return the minimum value for the tag
	 */
    public double getMinValue() {
        if (isEnumeratedTag) {
            return 0;
        } else {
            return minLimit.getValue();
        }
    }

    /**
	 * @return the maximum value for the tag
	 */
    public double getMaxValue() {
        if (isEnumeratedTag) {
            return enumeratedTagValues.size() - 1;
        } else {
            return maxLimit.getValue();
        }
    }

    /**
	 * @return the preferences node id for this curve
	 */
    public String getPreferencesNodeId() {
        return controller.getJwidgetRuntime().getPicture().getAnimActionsHandler().getViewPath() + "::" + getTagName();
    }

    /**
	 * @return whether the scale of this curve should be displayed
	 */
    public boolean displayScale() {
        return displayScale;
    }

    /**
	 * @param displayScale whether the scale of this curve should be displayed
	 */
    public void setDisplayScale(boolean displayScale) {
        this.displayScale = displayScale;
        notifyScaleChanged();
        PreferencesStore.setPreference(getPreferencesNodeId(), displayScalePrefId, Boolean.toString(displayScale));
    }

    /**
	 * @return the color of the curve
	 */
    public Color getColor() {
        return color;
    }

    /**
	 * sets the new color for the curve
	 * @param color a color
	 */
    public void setColor(Color color) {
        this.color = color;
        notifyStyleChanged();
        notifyColorChanged();
        PreferencesStore.setPreference(getPreferencesNodeId(), colorPrefId, ColorsAndBlinkingsToolkit.getColorString(color));
    }

    /**
	 * @return the stroke object
	 */
    public Stroke getStroke() {
        return stroke;
    }

    /**
	 * @return the stroke object
	 */
    public Stroke getStrokeForInvalid() {
        return strokeForInvalid;
    }

    /**
	 * @return the thickness of the curve
	 */
    public int getThickness() {
        return thickness;
    }

    /**
	 * @param thickness the thickness
	 */
    public void setThickness(int thickness) {
        this.thickness = thickness;
        computeStroke();
        notifyStyleChanged();
        PreferencesStore.setPreference(getPreferencesNodeId(), thicknessPrefId, thickness + "");
    }

    /**
	 * @return the dash of the curve
	 */
    public String getDash() {
        return dashes;
    }

    /**
	 * @param dash the new dash
	 */
    public void setDash(String dash) {
        this.dashes = dash;
        computeStroke();
        notifyStyleChanged();
        PreferencesStore.setPreference(getPreferencesNodeId(), dashesPrefId, dash);
    }

    /**
	 * @return the point type
	 */
    public String getPoint() {
        return pointType;
    }

    /**
	 * @param pointType the new point type
	 */
    public void setPoint(String pointType) {
        this.pointType = pointType;
        notifyStyleChanged();
        PreferencesStore.setPreference(getPreferencesNodeId(), pointsTypePrefId, pointType);
    }

    /**
	 * @return the interpolation type
	 */
    public String getInterpolation() {
        return interpolationType;
    }

    /**
	 * @param interpolationType the new interpolation type
	 */
    public void setInterpolationType(String interpolationType) {
        this.interpolationType = interpolationType;
        notifyStyleChanged();
        PreferencesStore.setPreference(getPreferencesNodeId(), interpolationTypePrefId, interpolationType);
    }

    /**
	 * @return the color of the curve when it is in an invalid state
	 */
    public Color getColorForInvalid() {
        return colorForInvalid;
    }

    /**
	 * @param colorForInvalid the color of the curve when it is in an invalid state
	 */
    public void setColorForInvalid(Color colorForInvalid) {
        this.colorForInvalid = colorForInvalid;
        notifyStyleChanged();
    }

    /**
	 * @return the thickness of the curve when it is in an invalid state
	 */
    public int getThicknessForInvalid() {
        return thicknessForInvalid;
    }

    /**
	 * @param thicknessForInvalid the thickness of the curve when it is in an invalid state
	 */
    public void setThicknessForInvalid(int thicknessForInvalid) {
        this.thicknessForInvalid = thicknessForInvalid;
        notifyStyleChanged();
    }

    /**
	 * @return the dash of the curve when it is in an invalid state
	 */
    public String getDashForInvalid() {
        return dashesForInvalid;
    }

    /**
	 * @param dashForInvalid the dash of the curve when it is in an invalid state
	 */
    public void setDashForInvalid(String dashForInvalid) {
        this.dashesForInvalid = dashForInvalid;
        notifyStyleChanged();
    }

    /**
	 * @return the configuration object
	 */
    public TrendsConfiguration getConfiguration() {
        return configuration;
    }

    /**
	 * reinitializes all the properties of the curve
	 *
	 */
    public void reinitialize() {
        try {
            Preferences pref = PreferencesStore.getPreferenceNode(getPreferencesNodeId());
            pref.clear();
        } catch (Exception ex) {
        }
        computeValues();
        notifyScaleChanged();
        notifyStyleChanged();
        notifyColorChanged();
    }

    /**
	 * computes the dash array corresponding to the given string
	 * @param dashString the string of the dash
	 * @return the dash array corresponding to the given string
	 */
    public static float[] getDashArray(String dashString) {
        float[] dashesArray = null;
        String stVal = "";
        if (dashString != null) {
            stVal = dashString.trim();
        }
        LinkedList<Float> dashes = new LinkedList<Float>();
        String val = "";
        float dash = 0.0f;
        int pos = 0;
        while (stVal.length() > 0) {
            pos = stVal.indexOf(" ");
            if (pos != -1) {
                val = stVal.substring(0, pos);
                stVal = stVal.substring(pos + 1, stVal.length());
            } else {
                val = stVal;
                stVal = "";
            }
            try {
                dash = Float.parseFloat(val);
            } catch (Exception ex) {
                dash = Float.NaN;
            }
            if (!Float.isNaN(dash)) {
                dashes.add(dash);
            }
        }
        if (dashes.size() != 0) {
            dashesArray = new float[dashes.size()];
            int i = 0;
            for (float dsh : dashes) {
                dashesArray[i++] = dsh;
            }
        }
        return dashesArray;
    }
}
