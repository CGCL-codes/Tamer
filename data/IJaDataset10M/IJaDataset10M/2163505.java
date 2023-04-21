package west.controller.westats;

import java.beans.*;

/**
 *
 * @author armnant
 */
public class WEStatsControllerBeanInfo extends SimpleBeanInfo {

    private static BeanDescriptor getBdescriptor() {
        BeanDescriptor beanDescriptor = new BeanDescriptor(west.controller.westats.WEStatsController.class, null);
        return beanDescriptor;
    }

    private static final int PROPERTY_averagingLength = 0;

    private static final int PROPERTY_averagingLengthChoices = 1;

    private static final int PROPERTY_deleteIntermediateFiles = 2;

    private static final int PROPERTY_dynamicsLevelIp1 = 3;

    private static final int PROPERTY_dynamicsLevelIp1Choices = 4;

    private static final int PROPERTY_dynamicsTimeStepNumber = 5;

    private static final int PROPERTY_maxWindSpeed = 6;

    private static final int PROPERTY_mesoGeophyFile = 7;

    private static final int PROPERTY_modelDirectory = 8;

    private static final int PROPERTY_modelDirectoryClimateStates = 9;

    private static final int PROPERTY_outputFile = 10;

    private static final int PROPERTY_statisticsLevel = 11;

    private static final int PROPERTY_windClimateTable = 12;

    private static PropertyDescriptor[] getPdescriptor() {
        PropertyDescriptor[] properties = new PropertyDescriptor[13];
        try {
            properties[PROPERTY_averagingLength] = new PropertyDescriptor("averagingLength", west.controller.westats.WEStatsController.class, "getAveragingLength", "setAveragingLength");
            properties[PROPERTY_averagingLengthChoices] = new PropertyDescriptor("averagingLengthChoices", west.controller.westats.WEStatsController.class, "getAveragingLengthChoices", null);
            properties[PROPERTY_deleteIntermediateFiles] = new PropertyDescriptor("deleteIntermediateFiles", west.controller.westats.WEStatsController.class, "isDeleteIntermediateFiles", "setDeleteIntermediateFiles");
            properties[PROPERTY_dynamicsLevelIp1] = new PropertyDescriptor("dynamicsLevelIp1", west.controller.westats.WEStatsController.class, "getDynamicsLevelIp1", "setDynamicsLevelIp1");
            properties[PROPERTY_dynamicsLevelIp1Choices] = new PropertyDescriptor("dynamicsLevelIp1Choices", west.controller.westats.WEStatsController.class, "getDynamicsLevelIp1Choices", null);
            properties[PROPERTY_dynamicsTimeStepNumber] = new PropertyDescriptor("dynamicsTimeStepNumber", west.controller.westats.WEStatsController.class, "getDynamicsTimeStepNumber", "setDynamicsTimeStepNumber");
            properties[PROPERTY_maxWindSpeed] = new PropertyDescriptor("maxWindSpeed", west.controller.westats.WEStatsController.class, "getMaxWindSpeed", "setMaxWindSpeed");
            properties[PROPERTY_mesoGeophyFile] = new PropertyDescriptor("mesoGeophyFile", west.controller.westats.WEStatsController.class, "getMesoGeophyFile", "setMesoGeophyFile");
            properties[PROPERTY_modelDirectory] = new PropertyDescriptor("modelDirectory", west.controller.westats.WEStatsController.class, "getModelDirectory", "setModelDirectory");
            properties[PROPERTY_modelDirectoryClimateStates] = new PropertyDescriptor("modelDirectoryClimateStates", west.controller.westats.WEStatsController.class, "getModelDirectoryClimateStates", null);
            properties[PROPERTY_outputFile] = new PropertyDescriptor("outputFile", west.controller.westats.WEStatsController.class, "getOutputFile", "setOutputFile");
            properties[PROPERTY_statisticsLevel] = new PropertyDescriptor("statisticsLevel", west.controller.westats.WEStatsController.class, "getStatisticsLevel", "setStatisticsLevel");
            properties[PROPERTY_windClimateTable] = new PropertyDescriptor("windClimateTable", west.controller.westats.WEStatsController.class, "getWindClimateTable", "setWindClimateTable");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private static final int EVENT_propertyChangeListener = 0;

    private static EventSetDescriptor[] getEdescriptor() {
        EventSetDescriptor[] eventSets = new EventSetDescriptor[1];
        try {
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor(west.controller.westats.WEStatsController.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] { "propertyChange" }, "addPropertyChangeListener", "removePropertyChangeListener");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return eventSets;
    }

    private static final int METHOD_addPropertyChangeListener0 = 0;

    private static final int METHOD_info1 = 1;

    private static final int METHOD_load2 = 2;

    private static final int METHOD_propertyChange3 = 3;

    private static final int METHOD_refresh4 = 4;

    private static final int METHOD_removePropertyChangeListener5 = 5;

    private static final int METHOD_run6 = 6;

    private static final int METHOD_saveAs7 = 7;

    private static MethodDescriptor[] getMdescriptor() {
        MethodDescriptor[] methods = new MethodDescriptor[8];
        try {
            methods[METHOD_addPropertyChangeListener0] = new MethodDescriptor(west.controller.AbstractController.class.getMethod("addPropertyChangeListener", new Class[] { java.lang.String.class, java.beans.PropertyChangeListener.class }));
            methods[METHOD_addPropertyChangeListener0].setDisplayName("");
            methods[METHOD_info1] = new MethodDescriptor(west.controller.westats.WEStatsController.class.getMethod("info", new Class[] {}));
            methods[METHOD_info1].setDisplayName("");
            methods[METHOD_load2] = new MethodDescriptor(west.controller.westats.WEStatsController.class.getMethod("load", new Class[] { java.lang.String.class }));
            methods[METHOD_load2].setDisplayName("");
            methods[METHOD_propertyChange3] = new MethodDescriptor(west.controller.AbstractController.class.getMethod("propertyChange", new Class[] { java.beans.PropertyChangeEvent.class }));
            methods[METHOD_propertyChange3].setDisplayName("");
            methods[METHOD_refresh4] = new MethodDescriptor(west.controller.AbstractController.class.getMethod("refresh", new Class[] {}));
            methods[METHOD_refresh4].setDisplayName("");
            methods[METHOD_removePropertyChangeListener5] = new MethodDescriptor(west.controller.AbstractController.class.getMethod("removePropertyChangeListener", new Class[] { java.lang.String.class, java.beans.PropertyChangeListener.class }));
            methods[METHOD_removePropertyChangeListener5].setDisplayName("");
            methods[METHOD_run6] = new MethodDescriptor(west.controller.westats.WEStatsController.class.getMethod("run", new Class[] {}));
            methods[METHOD_run6].setDisplayName("");
            methods[METHOD_saveAs7] = new MethodDescriptor(west.controller.westats.WEStatsController.class.getMethod("saveAs", new Class[] { java.lang.String.class }));
            methods[METHOD_saveAs7].setDisplayName("");
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
