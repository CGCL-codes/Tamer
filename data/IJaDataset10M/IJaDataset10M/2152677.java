package west.controller.geogeo;

import java.beans.*;

/**
 *
 * @author armnant
 */
public class GengeoControllerBeanInfo extends SimpleBeanInfo {

    private static BeanDescriptor getBdescriptor() {
        BeanDescriptor beanDescriptor = new BeanDescriptor(west.controller.geogeo.GengeoController.class, null);
        return beanDescriptor;
    }

    private static final int PROPERTY_aggregType = 0;

    private static final int PROPERTY_aggregTypeChoices = 1;

    private static final int PROPERTY_defaultDBDirname = 2;

    private static final int PROPERTY_filterMask = 3;

    private static final int PROPERTY_filterSoil = 4;

    private static final int PROPERTY_filterTopo = 5;

    private static final int PROPERTY_filterTypeMaskChoices = 6;

    private static final int PROPERTY_filterTypeSoilChoices = 7;

    private static final int PROPERTY_filterTypeTopoChoices = 8;

    private static final int PROPERTY_filterTypeVegChoices = 9;

    private static final int PROPERTY_filterVeg = 10;

    private static final int PROPERTY_fullscan = 11;

    private static final int PROPERTY_gridFilename = 12;

    private static final int PROPERTY_logfile = 13;

    private static final int PROPERTY_maskNdx = 14;

    private static final int PROPERTY_maxOverFactor = 15;

    private static final int PROPERTY_minOverFactor = 16;

    private static final int PROPERTY_outputFilename = 17;

    private static final int PROPERTY_soilNdx = 18;

    private static final int PROPERTY_topoDBDirname = 19;

    private static final int PROPERTY_topoNdx = 20;

    private static final int PROPERTY_vegDBDirname = 21;

    private static final int PROPERTY_vegNdx = 22;

    private static final int PROPERTY_verboseLevel = 23;

    private static final int PROPERTY_verboseLevelChoices = 24;

    private static PropertyDescriptor[] getPdescriptor() {
        PropertyDescriptor[] properties = new PropertyDescriptor[25];
        try {
            properties[PROPERTY_aggregType] = new PropertyDescriptor("aggregType", west.controller.geogeo.GengeoController.class, "getAggregType", "setAggregType");
            properties[PROPERTY_aggregTypeChoices] = new PropertyDescriptor("aggregTypeChoices", west.controller.geogeo.GengeoController.class, "getAggregTypeChoices", null);
            properties[PROPERTY_defaultDBDirname] = new PropertyDescriptor("defaultDBDirname", west.controller.geogeo.GengeoController.class, "getDefaultDBDirname", "setDefaultDBDirname");
            properties[PROPERTY_filterMask] = new PropertyDescriptor("filterMask", west.controller.geogeo.GengeoController.class, "getFilterMask", "setFilterMask");
            properties[PROPERTY_filterSoil] = new PropertyDescriptor("filterSoil", west.controller.geogeo.GengeoController.class, "getFilterSoil", "setFilterSoil");
            properties[PROPERTY_filterTopo] = new PropertyDescriptor("filterTopo", west.controller.geogeo.GengeoController.class, "getFilterTopo", "setFilterTopo");
            properties[PROPERTY_filterTypeMaskChoices] = new PropertyDescriptor("filterTypeMaskChoices", west.controller.geogeo.GengeoController.class, "getFilterTypeMaskChoices", null);
            properties[PROPERTY_filterTypeSoilChoices] = new PropertyDescriptor("filterTypeSoilChoices", west.controller.geogeo.GengeoController.class, "getFilterTypeSoilChoices", null);
            properties[PROPERTY_filterTypeTopoChoices] = new PropertyDescriptor("filterTypeTopoChoices", west.controller.geogeo.GengeoController.class, "getFilterTypeTopoChoices", null);
            properties[PROPERTY_filterTypeVegChoices] = new PropertyDescriptor("filterTypeVegChoices", west.controller.geogeo.GengeoController.class, "getFilterTypeVegChoices", null);
            properties[PROPERTY_filterVeg] = new PropertyDescriptor("filterVeg", west.controller.geogeo.GengeoController.class, "getFilterVeg", "setFilterVeg");
            properties[PROPERTY_fullscan] = new PropertyDescriptor("fullscan", west.controller.geogeo.GengeoController.class, "getFullscan", "setFullscan");
            properties[PROPERTY_gridFilename] = new PropertyDescriptor("gridFilename", west.controller.geogeo.GengeoController.class, "getGridFilename", "setGridFilename");
            properties[PROPERTY_logfile] = new PropertyDescriptor("logfile", west.controller.geogeo.GengeoController.class, "getLogfile", "setLogfile");
            properties[PROPERTY_maskNdx] = new PropertyDescriptor("maskNdx", west.controller.geogeo.GengeoController.class, "getMaskNdx", "setMaskNdx");
            properties[PROPERTY_maxOverFactor] = new PropertyDescriptor("maxOverFactor", west.controller.geogeo.GengeoController.class, "getMaxOverFactor", "setMaxOverFactor");
            properties[PROPERTY_minOverFactor] = new PropertyDescriptor("minOverFactor", west.controller.geogeo.GengeoController.class, "getMinOverFactor", "setMinOverFactor");
            properties[PROPERTY_outputFilename] = new PropertyDescriptor("outputFilename", west.controller.geogeo.GengeoController.class, "getOutputFilename", "setOutputFilename");
            properties[PROPERTY_soilNdx] = new PropertyDescriptor("soilNdx", west.controller.geogeo.GengeoController.class, "getSoilNdx", "setSoilNdx");
            properties[PROPERTY_topoDBDirname] = new PropertyDescriptor("topoDBDirname", west.controller.geogeo.GengeoController.class, "getTopoDBDirname", "setTopoDBDirname");
            properties[PROPERTY_topoNdx] = new PropertyDescriptor("topoNdx", west.controller.geogeo.GengeoController.class, "getTopoNdx", "setTopoNdx");
            properties[PROPERTY_vegDBDirname] = new PropertyDescriptor("vegDBDirname", west.controller.geogeo.GengeoController.class, "getVegDBDirname", "setVegDBDirname");
            properties[PROPERTY_vegNdx] = new PropertyDescriptor("vegNdx", west.controller.geogeo.GengeoController.class, "getVegNdx", "setVegNdx");
            properties[PROPERTY_verboseLevel] = new PropertyDescriptor("verboseLevel", west.controller.geogeo.GengeoController.class, "getVerboseLevel", "setVerboseLevel");
            properties[PROPERTY_verboseLevelChoices] = new PropertyDescriptor("verboseLevelChoices", west.controller.geogeo.GengeoController.class, "getVerboseLevelChoices", null);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private static final int EVENT_propertyChangeListener = 0;

    private static EventSetDescriptor[] getEdescriptor() {
        EventSetDescriptor[] eventSets = new EventSetDescriptor[1];
        try {
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor(west.controller.geogeo.GengeoController.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] { "propertyChange" }, "addPropertyChangeListener", "removePropertyChangeListener");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return eventSets;
    }

    private static final int METHOD_addPropertyChangeListener0 = 0;

    private static final int METHOD_load1 = 1;

    private static final int METHOD_propertyChange2 = 2;

    private static final int METHOD_refresh3 = 3;

    private static final int METHOD_removePropertyChangeListener4 = 4;

    private static final int METHOD_run5 = 5;

    private static final int METHOD_saveAs6 = 6;

    private static MethodDescriptor[] getMdescriptor() {
        MethodDescriptor[] methods = new MethodDescriptor[7];
        try {
            methods[METHOD_addPropertyChangeListener0] = new MethodDescriptor(west.controller.AbstractController.class.getMethod("addPropertyChangeListener", new Class[] { java.lang.String.class, java.beans.PropertyChangeListener.class }));
            methods[METHOD_addPropertyChangeListener0].setDisplayName("");
            methods[METHOD_load1] = new MethodDescriptor(west.controller.geogeo.GengeoController.class.getMethod("load", new Class[] { java.lang.String.class }));
            methods[METHOD_load1].setDisplayName("");
            methods[METHOD_propertyChange2] = new MethodDescriptor(west.controller.AbstractController.class.getMethod("propertyChange", new Class[] { java.beans.PropertyChangeEvent.class }));
            methods[METHOD_propertyChange2].setDisplayName("");
            methods[METHOD_refresh3] = new MethodDescriptor(west.controller.AbstractController.class.getMethod("refresh", new Class[] {}));
            methods[METHOD_refresh3].setDisplayName("");
            methods[METHOD_removePropertyChangeListener4] = new MethodDescriptor(west.controller.AbstractController.class.getMethod("removePropertyChangeListener", new Class[] { java.lang.String.class, java.beans.PropertyChangeListener.class }));
            methods[METHOD_removePropertyChangeListener4].setDisplayName("");
            methods[METHOD_run5] = new MethodDescriptor(west.controller.geogeo.GengeoController.class.getMethod("run", new Class[] {}));
            methods[METHOD_run5].setDisplayName("");
            methods[METHOD_saveAs6] = new MethodDescriptor(west.controller.geogeo.GengeoController.class.getMethod("saveAs", new Class[] { java.lang.String.class }));
            methods[METHOD_saveAs6].setDisplayName("");
        } catch (Exception e) {
        }
        return methods;
    }

    private static java.awt.Image iconColor16 = null;

    private static java.awt.Image iconColor32 = null;

    private static java.awt.Image iconMono16 = null;

    private static java.awt.Image iconMono32 = null;

    private static String iconNameC16 = null;

    private static String iconNameC32 = null;

    private static String iconNameM16 = null;

    private static String iconNameM32 = null;

    private static final int defaultPropertyIndex = -1;

    private static final int defaultEventIndex = -1;

    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     * 
     * @return BeanDescriptor describing the editable
     * properties of this bean.  May return null if the
     * information should be obtained by automatic analysis.
     */
    public BeanDescriptor getBeanDescriptor() {
        return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     * 
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean.  May return null if the
     * information should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will
     * belong to the IndexedPropertyDescriptor subclass of PropertyDescriptor.
     * A client of getPropertyDescriptors can use "instanceof" to check
     * if a given PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     * 
     * @return  An array of EventSetDescriptors describing the kinds of 
     * events fired by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public EventSetDescriptor[] getEventSetDescriptors() {
        return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     * 
     * @return  An array of MethodDescriptors describing the methods 
     * implemented by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public MethodDescriptor[] getMethodDescriptors() {
        return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are 
     * customizing the bean.
     * @return  Index of default property in the PropertyDescriptor array
     * 		returned by getPropertyDescriptors.
     * <P>	Returns -1 if there is no default property.
     */
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will
     * mostly commonly be used by human's when using the bean. 
     * @return Index of default event in the EventSetDescriptor array
     *		returned by getEventSetDescriptors.
     * <P>	Returns -1 if there is no default event.
     */
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }

    /**
     * This method returns an image object that can be used to
     * represent the bean in toolboxes, toolbars, etc.   Icon images
     * will typically be GIFs, but may in future include other formats.
     * <p>
     * Beans aren't required to provide icons and may return null from
     * this method.
     * <p>
     * There are four possible flavors of icons (16x16 color,
     * 32x32 color, 16x16 mono, 32x32 mono).  If a bean choses to only
     * support a single icon we recommend supporting 16x16 color.
     * <p>
     * We recommend that icons have a "transparent" background
     * so they can be rendered onto an existing background.
     *
     * @param  iconKind  The kind of icon requested.  This should be
     *    one of the constant values ICON_COLOR_16x16, ICON_COLOR_32x32, 
     *    ICON_MONO_16x16, or ICON_MONO_32x32.
     * @return  An image object representing the requested icon.  May
     *    return null if no suitable icon is available.
     */
    public java.awt.Image getIcon(int iconKind) {
        switch(iconKind) {
            case ICON_COLOR_16x16:
                if (iconNameC16 == null) return null; else {
                    if (iconColor16 == null) iconColor16 = loadImage(iconNameC16);
                    return iconColor16;
                }
            case ICON_COLOR_32x32:
                if (iconNameC32 == null) return null; else {
                    if (iconColor32 == null) iconColor32 = loadImage(iconNameC32);
                    return iconColor32;
                }
            case ICON_MONO_16x16:
                if (iconNameM16 == null) return null; else {
                    if (iconMono16 == null) iconMono16 = loadImage(iconNameM16);
                    return iconMono16;
                }
            case ICON_MONO_32x32:
                if (iconNameM32 == null) return null; else {
                    if (iconMono32 == null) iconMono32 = loadImage(iconNameM32);
                    return iconMono32;
                }
            default:
                return null;
        }
    }
}
