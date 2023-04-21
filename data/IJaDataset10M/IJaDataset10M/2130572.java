package org.jgenesis.swing;

import java.beans.*;
import org.jgenesis.swing.editors.BeanManagerEditor;

/**
 * @author root
 */
public class JGBeanNavigatorBeanInfo extends SimpleBeanInfo {

    private static BeanDescriptor getBdescriptor() {
        BeanDescriptor beanDescriptor = new BeanDescriptor(JGBeanNavigator.class, null);
        return beanDescriptor;
    }

    private static final int PROPERTY_accessibleContext = 0;

    private static final int PROPERTY_actionMap = 1;

    private static final int PROPERTY_alignmentX = 2;

    private static final int PROPERTY_alignmentY = 3;

    private static final int PROPERTY_ancestorListeners = 4;

    private static final int PROPERTY_autoscrolls = 5;

    private static final int PROPERTY_background = 6;

    private static final int PROPERTY_backgroundSet = 7;

    private static final int PROPERTY_beanManager = 8;

    private static final int PROPERTY_border = 9;

    private static final int PROPERTY_borderPainted = 10;

    private static final int PROPERTY_bounds = 11;

    private static final int PROPERTY_cancelAction = 12;

    private static final int PROPERTY_cancelVisible = 13;

    private static final int PROPERTY_colorModel = 14;

    private static final int PROPERTY_componentCount = 15;

    private static final int PROPERTY_componentListeners = 16;

    private static final int PROPERTY_componentOrientation = 17;

    private static final int PROPERTY_componentPopupMenu = 18;

    private static final int PROPERTY_components = 19;

    private static final int PROPERTY_containerListeners = 20;

    private static final int PROPERTY_cursor = 21;

    private static final int PROPERTY_cursorSet = 22;

    private static final int PROPERTY_debugGraphicsOptions = 23;

    private static final int PROPERTY_deleteAction = 24;

    private static final int PROPERTY_deleteVisible = 25;

    private static final int PROPERTY_displayable = 26;

    private static final int PROPERTY_doubleBuffered = 27;

    private static final int PROPERTY_dropTarget = 28;

    private static final int PROPERTY_editAction = 29;

    private static final int PROPERTY_editVisible = 30;

    private static final int PROPERTY_enabled = 31;

    private static final int PROPERTY_firstAction = 32;

    private static final int PROPERTY_firstVisible = 33;

    private static final int PROPERTY_floatable = 34;

    private static final int PROPERTY_focusable = 35;

    private static final int PROPERTY_focusCycleRoot = 36;

    private static final int PROPERTY_focusCycleRootAncestor = 37;

    private static final int PROPERTY_focusListeners = 38;

    private static final int PROPERTY_focusOwner = 39;

    private static final int PROPERTY_focusTraversable = 40;

    private static final int PROPERTY_focusTraversalKeysEnabled = 41;

    private static final int PROPERTY_focusTraversalPolicy = 42;

    private static final int PROPERTY_focusTraversalPolicyProvider = 43;

    private static final int PROPERTY_focusTraversalPolicySet = 44;

    private static final int PROPERTY_font = 45;

    private static final int PROPERTY_fontSet = 46;

    private static final int PROPERTY_foreground = 47;

    private static final int PROPERTY_foregroundSet = 48;

    private static final int PROPERTY_graphics = 49;

    private static final int PROPERTY_graphicsConfiguration = 50;

    private static final int PROPERTY_height = 51;

    private static final int PROPERTY_hierarchyBoundsListeners = 52;

    private static final int PROPERTY_hierarchyListeners = 53;

    private static final int PROPERTY_ignoreRepaint = 54;

    private static final int PROPERTY_inheritsPopupMenu = 55;

    private static final int PROPERTY_inputContext = 56;

    private static final int PROPERTY_inputMethodListeners = 57;

    private static final int PROPERTY_inputMethodRequests = 58;

    private static final int PROPERTY_inputVerifier = 59;

    private static final int PROPERTY_insertAction = 60;

    private static final int PROPERTY_insertVisible = 61;

    private static final int PROPERTY_insets = 62;

    private static final int PROPERTY_keyListeners = 63;

    private static final int PROPERTY_lastAction = 64;

    private static final int PROPERTY_lastVisible = 65;

    private static final int PROPERTY_layout = 66;

    private static final int PROPERTY_lightweight = 67;

    private static final int PROPERTY_locale = 68;

    private static final int PROPERTY_locationOnScreen = 69;

    private static final int PROPERTY_managingFocus = 70;

    private static final int PROPERTY_margin = 71;

    private static final int PROPERTY_maximumSize = 72;

    private static final int PROPERTY_maximumSizeSet = 73;

    private static final int PROPERTY_minimumSize = 74;

    private static final int PROPERTY_minimumSizeSet = 75;

    private static final int PROPERTY_modificationButtonsVisible = 76;

    private static final int PROPERTY_mouseListeners = 77;

    private static final int PROPERTY_mouseMotionListeners = 78;

    private static final int PROPERTY_mousePosition = 79;

    private static final int PROPERTY_mouseWheelListeners = 80;

    private static final int PROPERTY_name = 81;

    private static final int PROPERTY_nextAction = 82;

    private static final int PROPERTY_nextFocusableComponent = 83;

    private static final int PROPERTY_nextVisible = 84;

    private static final int PROPERTY_opaque = 85;

    private static final int PROPERTY_optimizedDrawingEnabled = 86;

    private static final int PROPERTY_orientation = 87;

    private static final int PROPERTY_paintingTile = 88;

    private static final int PROPERTY_parent = 89;

    private static final int PROPERTY_peer = 90;

    private static final int PROPERTY_preferredSize = 91;

    private static final int PROPERTY_preferredSizeSet = 92;

    private static final int PROPERTY_previousAction = 93;

    private static final int PROPERTY_previuosVisible = 94;

    private static final int PROPERTY_propertyChangeListeners = 95;

    private static final int PROPERTY_refreshAction = 96;

    private static final int PROPERTY_refreshVisible = 97;

    private static final int PROPERTY_registeredKeyStrokes = 98;

    private static final int PROPERTY_requestFocusEnabled = 99;

    private static final int PROPERTY_rollover = 100;

    private static final int PROPERTY_rootPane = 101;

    private static final int PROPERTY_saveAction = 102;

    private static final int PROPERTY_saveVisible = 103;

    private static final int PROPERTY_showing = 104;

    private static final int PROPERTY_toolkit = 105;

    private static final int PROPERTY_toolTipText = 106;

    private static final int PROPERTY_topLevelAncestor = 107;

    private static final int PROPERTY_transferHandler = 108;

    private static final int PROPERTY_treeLock = 109;

    private static final int PROPERTY_UIClassID = 110;

    private static final int PROPERTY_valid = 111;

    private static final int PROPERTY_validateRoot = 112;

    private static final int PROPERTY_verifyInputWhenFocusTarget = 113;

    private static final int PROPERTY_vetoableChangeListeners = 114;

    private static final int PROPERTY_visible = 115;

    private static final int PROPERTY_visibleRect = 116;

    private static final int PROPERTY_width = 117;

    private static final int PROPERTY_x = 118;

    private static final int PROPERTY_y = 119;

    private static PropertyDescriptor[] getPdescriptor() {
        PropertyDescriptor[] properties = new PropertyDescriptor[120];
        try {
            properties[PROPERTY_accessibleContext] = new PropertyDescriptor("accessibleContext", JGBeanNavigator.class, "getAccessibleContext", null);
            properties[PROPERTY_accessibleContext].setExpert(true);
            properties[PROPERTY_actionMap] = new PropertyDescriptor("actionMap", JGBeanNavigator.class, "getActionMap", "setActionMap");
            properties[PROPERTY_actionMap].setExpert(true);
            properties[PROPERTY_alignmentX] = new PropertyDescriptor("alignmentX", JGBeanNavigator.class, "getAlignmentX", "setAlignmentX");
            properties[PROPERTY_alignmentX].setExpert(true);
            properties[PROPERTY_alignmentY] = new PropertyDescriptor("alignmentY", JGBeanNavigator.class, "getAlignmentY", "setAlignmentY");
            properties[PROPERTY_alignmentY].setExpert(true);
            properties[PROPERTY_ancestorListeners] = new PropertyDescriptor("ancestorListeners", JGBeanNavigator.class, "getAncestorListeners", null);
            properties[PROPERTY_ancestorListeners].setExpert(true);
            properties[PROPERTY_autoscrolls] = new PropertyDescriptor("autoscrolls", JGBeanNavigator.class, "getAutoscrolls", "setAutoscrolls");
            properties[PROPERTY_autoscrolls].setExpert(true);
            properties[PROPERTY_background] = new PropertyDescriptor("background", JGBeanNavigator.class, "getBackground", "setBackground");
            properties[PROPERTY_background].setExpert(true);
            properties[PROPERTY_backgroundSet] = new PropertyDescriptor("backgroundSet", JGBeanNavigator.class, "isBackgroundSet", null);
            properties[PROPERTY_backgroundSet].setExpert(true);
            properties[PROPERTY_beanManager] = new PropertyDescriptor("beanManager", JGBeanNavigator.class, "getBeanManager", "setBeanManager");
            properties[PROPERTY_beanManager].setPreferred(true);
            properties[PROPERTY_beanManager].setPropertyEditorClass(BeanManagerEditor.class);
            properties[PROPERTY_border] = new PropertyDescriptor("border", JGBeanNavigator.class, "getBorder", "setBorder");
            properties[PROPERTY_border].setExpert(true);
            properties[PROPERTY_borderPainted] = new PropertyDescriptor("borderPainted", JGBeanNavigator.class, "isBorderPainted", "setBorderPainted");
            properties[PROPERTY_borderPainted].setExpert(true);
            properties[PROPERTY_bounds] = new PropertyDescriptor("bounds", JGBeanNavigator.class, "getBounds", "setBounds");
            properties[PROPERTY_bounds].setExpert(true);
            properties[PROPERTY_cancelAction] = new PropertyDescriptor("cancelAction", JGBeanNavigator.class, "getCancelAction", "setCancelAction");
            properties[PROPERTY_cancelAction].setPreferred(true);
            properties[PROPERTY_cancelVisible] = new PropertyDescriptor("cancelVisible", JGBeanNavigator.class, "isCancelVisible", "setCancelVisible");
            properties[PROPERTY_cancelVisible].setPreferred(true);
            properties[PROPERTY_colorModel] = new PropertyDescriptor("colorModel", JGBeanNavigator.class, "getColorModel", null);
            properties[PROPERTY_colorModel].setExpert(true);
            properties[PROPERTY_componentCount] = new PropertyDescriptor("componentCount", JGBeanNavigator.class, "getComponentCount", null);
            properties[PROPERTY_componentCount].setExpert(true);
            properties[PROPERTY_componentListeners] = new PropertyDescriptor("componentListeners", JGBeanNavigator.class, "getComponentListeners", null);
            properties[PROPERTY_componentListeners].setExpert(true);
            properties[PROPERTY_componentOrientation] = new PropertyDescriptor("componentOrientation", JGBeanNavigator.class, "getComponentOrientation", "setComponentOrientation");
            properties[PROPERTY_componentOrientation].setExpert(true);
            properties[PROPERTY_componentPopupMenu] = new PropertyDescriptor("componentPopupMenu", JGBeanNavigator.class, "getComponentPopupMenu", "setComponentPopupMenu");
            properties[PROPERTY_componentPopupMenu].setExpert(true);
            properties[PROPERTY_components] = new PropertyDescriptor("components", JGBeanNavigator.class, "getComponents", null);
            properties[PROPERTY_components].setExpert(true);
            properties[PROPERTY_containerListeners] = new PropertyDescriptor("containerListeners", JGBeanNavigator.class, "getContainerListeners", null);
            properties[PROPERTY_containerListeners].setExpert(true);
            properties[PROPERTY_cursor] = new PropertyDescriptor("cursor", JGBeanNavigator.class, "getCursor", "setCursor");
            properties[PROPERTY_cursor].setExpert(true);
            properties[PROPERTY_cursorSet] = new PropertyDescriptor("cursorSet", JGBeanNavigator.class, "isCursorSet", null);
            properties[PROPERTY_cursorSet].setExpert(true);
            properties[PROPERTY_debugGraphicsOptions] = new PropertyDescriptor("debugGraphicsOptions", JGBeanNavigator.class, "getDebugGraphicsOptions", "setDebugGraphicsOptions");
            properties[PROPERTY_debugGraphicsOptions].setExpert(true);
            properties[PROPERTY_deleteAction] = new PropertyDescriptor("deleteAction", JGBeanNavigator.class, "getDeleteAction", "setDeleteAction");
            properties[PROPERTY_deleteAction].setPreferred(true);
            properties[PROPERTY_deleteVisible] = new PropertyDescriptor("deleteVisible", JGBeanNavigator.class, "isDeleteVisible", "setDeleteVisible");
            properties[PROPERTY_deleteVisible].setPreferred(true);
            properties[PROPERTY_displayable] = new PropertyDescriptor("displayable", JGBeanNavigator.class, "isDisplayable", null);
            properties[PROPERTY_displayable].setExpert(true);
            properties[PROPERTY_doubleBuffered] = new PropertyDescriptor("doubleBuffered", JGBeanNavigator.class, "isDoubleBuffered", "setDoubleBuffered");
            properties[PROPERTY_doubleBuffered].setExpert(true);
            properties[PROPERTY_dropTarget] = new PropertyDescriptor("dropTarget", JGBeanNavigator.class, "getDropTarget", "setDropTarget");
            properties[PROPERTY_dropTarget].setExpert(true);
            properties[PROPERTY_editAction] = new PropertyDescriptor("editAction", JGBeanNavigator.class, "getEditAction", "setEditAction");
            properties[PROPERTY_editAction].setPreferred(true);
            properties[PROPERTY_editVisible] = new PropertyDescriptor("editVisible", JGBeanNavigator.class, "isEditVisible", "setEditVisible");
            properties[PROPERTY_editVisible].setPreferred(true);
            properties[PROPERTY_enabled] = new PropertyDescriptor("enabled", JGBeanNavigator.class, "isEnabled", "setEnabled");
            properties[PROPERTY_enabled].setPreferred(true);
            properties[PROPERTY_firstAction] = new PropertyDescriptor("firstAction", JGBeanNavigator.class, "getFirstAction", "setFirstAction");
            properties[PROPERTY_firstAction].setPreferred(true);
            properties[PROPERTY_firstVisible] = new PropertyDescriptor("firstVisible", JGBeanNavigator.class, "isFirstVisible", "setFirstVisible");
            properties[PROPERTY_firstVisible].setPreferred(true);
            properties[PROPERTY_floatable] = new PropertyDescriptor("floatable", JGBeanNavigator.class, "isFloatable", "setFloatable");
            properties[PROPERTY_floatable].setExpert(true);
            properties[PROPERTY_focusable] = new PropertyDescriptor("focusable", JGBeanNavigator.class, "isFocusable", "setFocusable");
            properties[PROPERTY_focusable].setExpert(true);
            properties[PROPERTY_focusCycleRoot] = new PropertyDescriptor("focusCycleRoot", JGBeanNavigator.class, "isFocusCycleRoot", "setFocusCycleRoot");
            properties[PROPERTY_focusCycleRoot].setExpert(true);
            properties[PROPERTY_focusCycleRootAncestor] = new PropertyDescriptor("focusCycleRootAncestor", JGBeanNavigator.class, "getFocusCycleRootAncestor", null);
            properties[PROPERTY_focusCycleRootAncestor].setExpert(true);
            properties[PROPERTY_focusListeners] = new PropertyDescriptor("focusListeners", JGBeanNavigator.class, "getFocusListeners", null);
            properties[PROPERTY_focusListeners].setExpert(true);
            properties[PROPERTY_focusOwner] = new PropertyDescriptor("focusOwner", JGBeanNavigator.class, "isFocusOwner", null);
            properties[PROPERTY_focusOwner].setExpert(true);
            properties[PROPERTY_focusTraversable] = new PropertyDescriptor("focusTraversable", JGBeanNavigator.class, "isFocusTraversable", null);
            properties[PROPERTY_focusTraversable].setExpert(true);
            properties[PROPERTY_focusTraversalKeysEnabled] = new PropertyDescriptor("focusTraversalKeysEnabled", JGBeanNavigator.class, "getFocusTraversalKeysEnabled", "setFocusTraversalKeysEnabled");
            properties[PROPERTY_focusTraversalKeysEnabled].setExpert(true);
            properties[PROPERTY_focusTraversalPolicy] = new PropertyDescriptor("focusTraversalPolicy", JGBeanNavigator.class, "getFocusTraversalPolicy", "setFocusTraversalPolicy");
            properties[PROPERTY_focusTraversalPolicy].setExpert(true);
            properties[PROPERTY_focusTraversalPolicyProvider] = new PropertyDescriptor("focusTraversalPolicyProvider", JGBeanNavigator.class, "isFocusTraversalPolicyProvider", "setFocusTraversalPolicyProvider");
            properties[PROPERTY_focusTraversalPolicyProvider].setExpert(true);
            properties[PROPERTY_focusTraversalPolicySet] = new PropertyDescriptor("focusTraversalPolicySet", JGBeanNavigator.class, "isFocusTraversalPolicySet", null);
            properties[PROPERTY_focusTraversalPolicySet].setExpert(true);
            properties[PROPERTY_font] = new PropertyDescriptor("font", JGBeanNavigator.class, "getFont", "setFont");
            properties[PROPERTY_font].setExpert(true);
            properties[PROPERTY_fontSet] = new PropertyDescriptor("fontSet", JGBeanNavigator.class, "isFontSet", null);
            properties[PROPERTY_fontSet].setExpert(true);
            properties[PROPERTY_foreground] = new PropertyDescriptor("foreground", JGBeanNavigator.class, "getForeground", "setForeground");
            properties[PROPERTY_foreground].setExpert(true);
            properties[PROPERTY_foregroundSet] = new PropertyDescriptor("foregroundSet", JGBeanNavigator.class, "isForegroundSet", null);
            properties[PROPERTY_foregroundSet].setExpert(true);
            properties[PROPERTY_graphics] = new PropertyDescriptor("graphics", JGBeanNavigator.class, "getGraphics", null);
            properties[PROPERTY_graphics].setExpert(true);
            properties[PROPERTY_graphicsConfiguration] = new PropertyDescriptor("graphicsConfiguration", JGBeanNavigator.class, "getGraphicsConfiguration", null);
            properties[PROPERTY_graphicsConfiguration].setExpert(true);
            properties[PROPERTY_height] = new PropertyDescriptor("height", JGBeanNavigator.class, "getHeight", null);
            properties[PROPERTY_height].setExpert(true);
            properties[PROPERTY_hierarchyBoundsListeners] = new PropertyDescriptor("hierarchyBoundsListeners", JGBeanNavigator.class, "getHierarchyBoundsListeners", null);
            properties[PROPERTY_hierarchyBoundsListeners].setExpert(true);
            properties[PROPERTY_hierarchyListeners] = new PropertyDescriptor("hierarchyListeners", JGBeanNavigator.class, "getHierarchyListeners", null);
            properties[PROPERTY_hierarchyListeners].setExpert(true);
            properties[PROPERTY_ignoreRepaint] = new PropertyDescriptor("ignoreRepaint", JGBeanNavigator.class, "getIgnoreRepaint", "setIgnoreRepaint");
            properties[PROPERTY_ignoreRepaint].setExpert(true);
            properties[PROPERTY_inheritsPopupMenu] = new PropertyDescriptor("inheritsPopupMenu", JGBeanNavigator.class, "getInheritsPopupMenu", "setInheritsPopupMenu");
            properties[PROPERTY_inheritsPopupMenu].setExpert(true);
            properties[PROPERTY_inputContext] = new PropertyDescriptor("inputContext", JGBeanNavigator.class, "getInputContext", null);
            properties[PROPERTY_inputContext].setExpert(true);
            properties[PROPERTY_inputMethodListeners] = new PropertyDescriptor("inputMethodListeners", JGBeanNavigator.class, "getInputMethodListeners", null);
            properties[PROPERTY_inputMethodListeners].setExpert(true);
            properties[PROPERTY_inputMethodRequests] = new PropertyDescriptor("inputMethodRequests", JGBeanNavigator.class, "getInputMethodRequests", null);
            properties[PROPERTY_inputMethodRequests].setExpert(true);
            properties[PROPERTY_inputVerifier] = new PropertyDescriptor("inputVerifier", JGBeanNavigator.class, "getInputVerifier", "setInputVerifier");
            properties[PROPERTY_inputVerifier].setExpert(true);
            properties[PROPERTY_insertAction] = new PropertyDescriptor("insertAction", JGBeanNavigator.class, "getInsertAction", "setInsertAction");
            properties[PROPERTY_insertAction].setPreferred(true);
            properties[PROPERTY_insertVisible] = new PropertyDescriptor("insertVisible", JGBeanNavigator.class, "isInsertVisible", "setInsertVisible");
            properties[PROPERTY_insertVisible].setPreferred(true);
            properties[PROPERTY_insets] = new PropertyDescriptor("insets", JGBeanNavigator.class, "getInsets", null);
            properties[PROPERTY_insets].setExpert(true);
            properties[PROPERTY_keyListeners] = new PropertyDescriptor("keyListeners", JGBeanNavigator.class, "getKeyListeners", null);
            properties[PROPERTY_keyListeners].setExpert(true);
            properties[PROPERTY_lastAction] = new PropertyDescriptor("lastAction", JGBeanNavigator.class, "getLastAction", "setLastAction");
            properties[PROPERTY_lastAction].setPreferred(true);
            properties[PROPERTY_lastVisible] = new PropertyDescriptor("lastVisible", JGBeanNavigator.class, "isLastVisible", "setLastVisible");
            properties[PROPERTY_lastVisible].setPreferred(true);
            properties[PROPERTY_layout] = new PropertyDescriptor("layout", JGBeanNavigator.class, "getLayout", "setLayout");
            properties[PROPERTY_layout].setExpert(true);
            properties[PROPERTY_lightweight] = new PropertyDescriptor("lightweight", JGBeanNavigator.class, "isLightweight", null);
            properties[PROPERTY_lightweight].setExpert(true);
            properties[PROPERTY_locale] = new PropertyDescriptor("locale", JGBeanNavigator.class, "getLocale", "setLocale");
            properties[PROPERTY_locale].setExpert(true);
            properties[PROPERTY_locationOnScreen] = new PropertyDescriptor("locationOnScreen", JGBeanNavigator.class, "getLocationOnScreen", null);
            properties[PROPERTY_locationOnScreen].setExpert(true);
            properties[PROPERTY_managingFocus] = new PropertyDescriptor("managingFocus", JGBeanNavigator.class, "isManagingFocus", null);
            properties[PROPERTY_managingFocus].setExpert(true);
            properties[PROPERTY_margin] = new PropertyDescriptor("margin", JGBeanNavigator.class, "getMargin", "setMargin");
            properties[PROPERTY_margin].setExpert(true);
            properties[PROPERTY_maximumSize] = new PropertyDescriptor("maximumSize", JGBeanNavigator.class, "getMaximumSize", "setMaximumSize");
            properties[PROPERTY_maximumSize].setExpert(true);
            properties[PROPERTY_maximumSizeSet] = new PropertyDescriptor("maximumSizeSet", JGBeanNavigator.class, "isMaximumSizeSet", null);
            properties[PROPERTY_maximumSizeSet].setExpert(true);
            properties[PROPERTY_minimumSize] = new PropertyDescriptor("minimumSize", JGBeanNavigator.class, "getMinimumSize", "setMinimumSize");
            properties[PROPERTY_minimumSize].setExpert(true);
            properties[PROPERTY_minimumSizeSet] = new PropertyDescriptor("minimumSizeSet", JGBeanNavigator.class, "isMinimumSizeSet", null);
            properties[PROPERTY_minimumSizeSet].setExpert(true);
            properties[PROPERTY_modificationButtonsVisible] = new PropertyDescriptor("modificationButtonsVisible", JGBeanNavigator.class, null, "setModificationButtonsVisible");
            properties[PROPERTY_modificationButtonsVisible].setExpert(true);
            properties[PROPERTY_mouseListeners] = new PropertyDescriptor("mouseListeners", JGBeanNavigator.class, "getMouseListeners", null);
            properties[PROPERTY_mouseListeners].setExpert(true);
            properties[PROPERTY_mouseMotionListeners] = new PropertyDescriptor("mouseMotionListeners", JGBeanNavigator.class, "getMouseMotionListeners", null);
            properties[PROPERTY_mouseMotionListeners].setExpert(true);
            properties[PROPERTY_mousePosition] = new PropertyDescriptor("mousePosition", JGBeanNavigator.class, "getMousePosition", null);
            properties[PROPERTY_mousePosition].setExpert(true);
            properties[PROPERTY_mouseWheelListeners] = new PropertyDescriptor("mouseWheelListeners", JGBeanNavigator.class, "getMouseWheelListeners", null);
            properties[PROPERTY_mouseWheelListeners].setExpert(true);
            properties[PROPERTY_name] = new PropertyDescriptor("name", JGBeanNavigator.class, "getName", "setName");
            properties[PROPERTY_name].setExpert(true);
            properties[PROPERTY_nextAction] = new PropertyDescriptor("nextAction", JGBeanNavigator.class, "getNextAction", "setNextAction");
            properties[PROPERTY_nextAction].setPreferred(true);
            properties[PROPERTY_nextFocusableComponent] = new PropertyDescriptor("nextFocusableComponent", JGBeanNavigator.class, "getNextFocusableComponent", "setNextFocusableComponent");
            properties[PROPERTY_nextFocusableComponent].setExpert(true);
            properties[PROPERTY_nextVisible] = new PropertyDescriptor("nextVisible", JGBeanNavigator.class, "isNextVisible", "setNextVisible");
            properties[PROPERTY_nextVisible].setPreferred(true);
            properties[PROPERTY_opaque] = new PropertyDescriptor("opaque", JGBeanNavigator.class, "isOpaque", "setOpaque");
            properties[PROPERTY_opaque].setExpert(true);
            properties[PROPERTY_optimizedDrawingEnabled] = new PropertyDescriptor("optimizedDrawingEnabled", JGBeanNavigator.class, "isOptimizedDrawingEnabled", null);
            properties[PROPERTY_optimizedDrawingEnabled].setExpert(true);
            properties[PROPERTY_orientation] = new PropertyDescriptor("orientation", JGBeanNavigator.class, "getOrientation", "setOrientation");
            properties[PROPERTY_orientation].setExpert(true);
            properties[PROPERTY_paintingTile] = new PropertyDescriptor("paintingTile", JGBeanNavigator.class, "isPaintingTile", null);
            properties[PROPERTY_paintingTile].setExpert(true);
            properties[PROPERTY_parent] = new PropertyDescriptor("parent", JGBeanNavigator.class, "getParent", null);
            properties[PROPERTY_parent].setExpert(true);
            properties[PROPERTY_peer] = new PropertyDescriptor("peer", JGBeanNavigator.class, "getPeer", null);
            properties[PROPERTY_peer].setExpert(true);
            properties[PROPERTY_preferredSize] = new PropertyDescriptor("preferredSize", JGBeanNavigator.class, "getPreferredSize", "setPreferredSize");
            properties[PROPERTY_preferredSize].setExpert(true);
            properties[PROPERTY_preferredSizeSet] = new PropertyDescriptor("preferredSizeSet", JGBeanNavigator.class, "isPreferredSizeSet", null);
            properties[PROPERTY_preferredSizeSet].setExpert(true);
            properties[PROPERTY_previousAction] = new PropertyDescriptor("previousAction", JGBeanNavigator.class, "getPreviousAction", "setPreviousAction");
            properties[PROPERTY_previousAction].setPreferred(true);
            properties[PROPERTY_previuosVisible] = new PropertyDescriptor("previuosVisible", JGBeanNavigator.class, "isPreviuosVisible", "setPreviuosVisible");
            properties[PROPERTY_previuosVisible].setPreferred(true);
            properties[PROPERTY_propertyChangeListeners] = new PropertyDescriptor("propertyChangeListeners", JGBeanNavigator.class, "getPropertyChangeListeners", null);
            properties[PROPERTY_propertyChangeListeners].setExpert(true);
            properties[PROPERTY_refreshAction] = new PropertyDescriptor("refreshAction", JGBeanNavigator.class, "getRefreshAction", "setRefreshAction");
            properties[PROPERTY_refreshAction].setPreferred(true);
            properties[PROPERTY_refreshVisible] = new PropertyDescriptor("refreshVisible", JGBeanNavigator.class, "isRefreshVisible", "setRefreshVisible");
            properties[PROPERTY_refreshVisible].setPreferred(true);
            properties[PROPERTY_registeredKeyStrokes] = new PropertyDescriptor("registeredKeyStrokes", JGBeanNavigator.class, "getRegisteredKeyStrokes", null);
            properties[PROPERTY_registeredKeyStrokes].setExpert(true);
            properties[PROPERTY_requestFocusEnabled] = new PropertyDescriptor("requestFocusEnabled", JGBeanNavigator.class, "isRequestFocusEnabled", "setRequestFocusEnabled");
            properties[PROPERTY_requestFocusEnabled].setExpert(true);
            properties[PROPERTY_rollover] = new PropertyDescriptor("rollover", JGBeanNavigator.class, "isRollover", "setRollover");
            properties[PROPERTY_rollover].setExpert(true);
            properties[PROPERTY_rootPane] = new PropertyDescriptor("rootPane", JGBeanNavigator.class, "getRootPane", null);
            properties[PROPERTY_rootPane].setExpert(true);
            properties[PROPERTY_saveAction] = new PropertyDescriptor("saveAction", JGBeanNavigator.class, "getSaveAction", "setSaveAction");
            properties[PROPERTY_saveAction].setPreferred(true);
            properties[PROPERTY_saveVisible] = new PropertyDescriptor("saveVisible", JGBeanNavigator.class, "isSaveVisible", "setSaveVisible");
            properties[PROPERTY_saveVisible].setPreferred(true);
            properties[PROPERTY_showing] = new PropertyDescriptor("showing", JGBeanNavigator.class, "isShowing", null);
            properties[PROPERTY_showing].setExpert(true);
            properties[PROPERTY_toolkit] = new PropertyDescriptor("toolkit", JGBeanNavigator.class, "getToolkit", null);
            properties[PROPERTY_toolkit].setExpert(true);
            properties[PROPERTY_toolTipText] = new PropertyDescriptor("toolTipText", JGBeanNavigator.class, "getToolTipText", "setToolTipText");
            properties[PROPERTY_toolTipText].setExpert(true);
            properties[PROPERTY_topLevelAncestor] = new PropertyDescriptor("topLevelAncestor", JGBeanNavigator.class, "getTopLevelAncestor", null);
            properties[PROPERTY_topLevelAncestor].setExpert(true);
            properties[PROPERTY_transferHandler] = new PropertyDescriptor("transferHandler", JGBeanNavigator.class, "getTransferHandler", "setTransferHandler");
            properties[PROPERTY_transferHandler].setExpert(true);
            properties[PROPERTY_treeLock] = new PropertyDescriptor("treeLock", JGBeanNavigator.class, "getTreeLock", null);
            properties[PROPERTY_treeLock].setExpert(true);
            properties[PROPERTY_UIClassID] = new PropertyDescriptor("UIClassID", JGBeanNavigator.class, "getUIClassID", null);
            properties[PROPERTY_UIClassID].setExpert(true);
            properties[PROPERTY_valid] = new PropertyDescriptor("valid", JGBeanNavigator.class, "isValid", null);
            properties[PROPERTY_valid].setExpert(true);
            properties[PROPERTY_validateRoot] = new PropertyDescriptor("validateRoot", JGBeanNavigator.class, "isValidateRoot", null);
            properties[PROPERTY_validateRoot].setExpert(true);
            properties[PROPERTY_verifyInputWhenFocusTarget] = new PropertyDescriptor("verifyInputWhenFocusTarget", JGBeanNavigator.class, "getVerifyInputWhenFocusTarget", "setVerifyInputWhenFocusTarget");
            properties[PROPERTY_verifyInputWhenFocusTarget].setExpert(true);
            properties[PROPERTY_vetoableChangeListeners] = new PropertyDescriptor("vetoableChangeListeners", JGBeanNavigator.class, "getVetoableChangeListeners", null);
            properties[PROPERTY_vetoableChangeListeners].setExpert(true);
            properties[PROPERTY_visible] = new PropertyDescriptor("visible", JGBeanNavigator.class, "isVisible", "setVisible");
            properties[PROPERTY_visible].setPreferred(true);
            properties[PROPERTY_visibleRect] = new PropertyDescriptor("visibleRect", JGBeanNavigator.class, "getVisibleRect", null);
            properties[PROPERTY_visibleRect].setExpert(true);
            properties[PROPERTY_width] = new PropertyDescriptor("width", JGBeanNavigator.class, "getWidth", null);
            properties[PROPERTY_width].setExpert(true);
            properties[PROPERTY_x] = new PropertyDescriptor("x", JGBeanNavigator.class, "getX", null);
            properties[PROPERTY_x].setExpert(true);
            properties[PROPERTY_y] = new PropertyDescriptor("y", JGBeanNavigator.class, "getY", null);
            properties[PROPERTY_y].setExpert(true);
        } catch (IntrospectionException e) {
        }
        return properties;
    }

    private static final int EVENT_ancestorListener = 0;

    private static final int EVENT_componentListener = 1;

    private static final int EVENT_containerListener = 2;

    private static final int EVENT_focusListener = 3;

    private static final int EVENT_hierarchyBoundsListener = 4;

    private static final int EVENT_hierarchyListener = 5;

    private static final int EVENT_inputMethodListener = 6;

    private static final int EVENT_keyListener = 7;

    private static final int EVENT_mouseListener = 8;

    private static final int EVENT_mouseMotionListener = 9;

    private static final int EVENT_mouseWheelListener = 10;

    private static final int EVENT_propertyChangeListener = 11;

    private static final int EVENT_vetoableChangeListener = 12;

    private static EventSetDescriptor[] getEdescriptor() {
        EventSetDescriptor[] eventSets = new EventSetDescriptor[13];
        try {
            eventSets[EVENT_ancestorListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "ancestorListener", javax.swing.event.AncestorListener.class, new String[] { "ancestorAdded", "ancestorMoved", "ancestorRemoved" }, "addAncestorListener", "removeAncestorListener");
            eventSets[EVENT_componentListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "componentListener", java.awt.event.ComponentListener.class, new String[] { "componentHidden", "componentMoved", "componentResized", "componentShown" }, "addComponentListener", "removeComponentListener");
            eventSets[EVENT_containerListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "containerListener", java.awt.event.ContainerListener.class, new String[] { "componentAdded", "componentRemoved" }, "addContainerListener", "removeContainerListener");
            eventSets[EVENT_focusListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "focusListener", java.awt.event.FocusListener.class, new String[] { "focusGained", "focusLost" }, "addFocusListener", "removeFocusListener");
            eventSets[EVENT_hierarchyBoundsListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "hierarchyBoundsListener", java.awt.event.HierarchyBoundsListener.class, new String[] { "ancestorMoved", "ancestorResized" }, "addHierarchyBoundsListener", "removeHierarchyBoundsListener");
            eventSets[EVENT_hierarchyListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "hierarchyListener", java.awt.event.HierarchyListener.class, new String[] { "hierarchyChanged" }, "addHierarchyListener", "removeHierarchyListener");
            eventSets[EVENT_inputMethodListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "inputMethodListener", java.awt.event.InputMethodListener.class, new String[] { "caretPositionChanged", "inputMethodTextChanged" }, "addInputMethodListener", "removeInputMethodListener");
            eventSets[EVENT_keyListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "keyListener", java.awt.event.KeyListener.class, new String[] { "keyPressed", "keyReleased", "keyTyped" }, "addKeyListener", "removeKeyListener");
            eventSets[EVENT_mouseListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "mouseListener", java.awt.event.MouseListener.class, new String[] { "mouseClicked", "mouseEntered", "mouseExited", "mousePressed", "mouseReleased" }, "addMouseListener", "removeMouseListener");
            eventSets[EVENT_mouseMotionListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "mouseMotionListener", java.awt.event.MouseMotionListener.class, new String[] { "mouseDragged", "mouseMoved" }, "addMouseMotionListener", "removeMouseMotionListener");
            eventSets[EVENT_mouseWheelListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "mouseWheelListener", java.awt.event.MouseWheelListener.class, new String[] { "mouseWheelMoved" }, "addMouseWheelListener", "removeMouseWheelListener");
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] { "propertyChange" }, "addPropertyChangeListener", "removePropertyChangeListener");
            eventSets[EVENT_vetoableChangeListener] = new EventSetDescriptor(org.jgenesis.swing.JGBeanNavigator.class, "vetoableChangeListener", java.beans.VetoableChangeListener.class, new String[] { "vetoableChange" }, "addVetoableChangeListener", "removeVetoableChangeListener");
        } catch (IntrospectionException e) {
        }
        return eventSets;
    }

    private static final int METHOD_action0 = 0;

    private static final int METHOD_add1 = 1;

    private static final int METHOD_addNotify2 = 2;

    private static final int METHOD_addPropertyChangeListener3 = 3;

    private static final int METHOD_addSeparator4 = 4;

    private static final int METHOD_applyComponentOrientation5 = 5;

    private static final int METHOD_areFocusTraversalKeysSet6 = 6;

    private static final int METHOD_bounds7 = 7;

    private static final int METHOD_checkImage8 = 8;

    private static final int METHOD_computeVisibleRect9 = 9;

    private static final int METHOD_contains10 = 10;

    private static final int METHOD_countComponents11 = 11;

    private static final int METHOD_createImage12 = 12;

    private static final int METHOD_createToolTip13 = 13;

    private static final int METHOD_createVolatileImage14 = 14;

    private static final int METHOD_deliverEvent15 = 15;

    private static final int METHOD_disable16 = 16;

    private static final int METHOD_dispatchEvent17 = 17;

    private static final int METHOD_doLayout18 = 18;

    private static final int METHOD_enable19 = 19;

    private static final int METHOD_enableInputMethods20 = 20;

    private static final int METHOD_findComponentAt21 = 21;

    private static final int METHOD_firePropertyChange22 = 22;

    private static final int METHOD_getActionForKeyStroke23 = 23;

    private static final int METHOD_getBeanSet24 = 24;

    private static final int METHOD_getBounds25 = 25;

    private static final int METHOD_getClientProperty26 = 26;

    private static final int METHOD_getComponentAt27 = 27;

    private static final int METHOD_getComponentIndex28 = 28;

    private static final int METHOD_getComponentZOrder29 = 29;

    private static final int METHOD_getConditionForKeyStroke30 = 30;

    private static final int METHOD_getDefaultLocale31 = 31;

    private static final int METHOD_getFontMetrics32 = 32;

    private static final int METHOD_getInputMap33 = 33;

    private static final int METHOD_getInsets34 = 34;

    private static final int METHOD_getListeners35 = 35;

    private static final int METHOD_getLocation36 = 36;

    private static final int METHOD_getMousePosition37 = 37;

    private static final int METHOD_getPopupLocation38 = 38;

    private static final int METHOD_getPropertyChangeListeners39 = 39;

    private static final int METHOD_getSize40 = 40;

    private static final int METHOD_getToolTipLocation41 = 41;

    private static final int METHOD_getToolTipText42 = 42;

    private static final int METHOD_gotFocus43 = 43;

    private static final int METHOD_grabFocus44 = 44;

    private static final int METHOD_handleEvent45 = 45;

    private static final int METHOD_hasFocus46 = 46;

    private static final int METHOD_hide47 = 47;

    private static final int METHOD_imageUpdate48 = 48;

    private static final int METHOD_insets49 = 49;

    private static final int METHOD_inside50 = 50;

    private static final int METHOD_invalidate51 = 51;

    private static final int METHOD_isAncestorOf52 = 52;

    private static final int METHOD_isFocusCycleRoot53 = 53;

    private static final int METHOD_isLightweightComponent54 = 54;

    private static final int METHOD_keyDown55 = 55;

    private static final int METHOD_keyUp56 = 56;

    private static final int METHOD_layout57 = 57;

    private static final int METHOD_list58 = 58;

    private static final int METHOD_locate59 = 59;

    private static final int METHOD_location60 = 60;

    private static final int METHOD_lostFocus61 = 61;

    private static final int METHOD_minimumSize62 = 62;

    private static final int METHOD_mouseDown63 = 63;

    private static final int METHOD_mouseDrag64 = 64;

    private static final int METHOD_mouseEnter65 = 65;

    private static final int METHOD_mouseExit66 = 66;

    private static final int METHOD_mouseMove67 = 67;

    private static final int METHOD_mouseUp68 = 68;

    private static final int METHOD_move69 = 69;

    private static final int METHOD_nextFocus70 = 70;

    private static final int METHOD_paint71 = 71;

    private static final int METHOD_paintAll72 = 72;

    private static final int METHOD_paintComponents73 = 73;

    private static final int METHOD_paintImmediately74 = 74;

    private static final int METHOD_postEvent75 = 75;

    private static final int METHOD_preferredSize76 = 76;

    private static final int METHOD_prepareImage77 = 77;

    private static final int METHOD_print78 = 78;

    private static final int METHOD_printAll79 = 79;

    private static final int METHOD_printComponents80 = 80;

    private static final int METHOD_putClientProperty81 = 81;

    private static final int METHOD_registerKeyboardAction82 = 82;

    private static final int METHOD_remove83 = 83;

    private static final int METHOD_removeAll84 = 84;

    private static final int METHOD_removeNotify85 = 85;

    private static final int METHOD_removePropertyChangeListener86 = 86;

    private static final int METHOD_repaint87 = 87;

    private static final int METHOD_requestDefaultFocus88 = 88;

    private static final int METHOD_requestFocus89 = 89;

    private static final int METHOD_requestFocusInWindow90 = 90;

    private static final int METHOD_resetKeyboardActions91 = 91;

    private static final int METHOD_reshape92 = 92;

    private static final int METHOD_resize93 = 93;

    private static final int METHOD_revalidate94 = 94;

    private static final int METHOD_scrollRectToVisible95 = 95;

    private static final int METHOD_setBounds96 = 96;

    private static final int METHOD_setButtonsLayout97 = 97;

    private static final int METHOD_setComponentZOrder98 = 98;

    private static final int METHOD_setDefaultLocale99 = 99;

    private static final int METHOD_setInputMap100 = 100;

    private static final int METHOD_setLocation101 = 101;

    private static final int METHOD_setSize102 = 102;

    private static final int METHOD_show103 = 103;

    private static final int METHOD_size104 = 104;

    private static final int METHOD_toString105 = 105;

    private static final int METHOD_transferFocus106 = 106;

    private static final int METHOD_transferFocusBackward107 = 107;

    private static final int METHOD_transferFocusDownCycle108 = 108;

    private static final int METHOD_transferFocusUpCycle109 = 109;

    private static final int METHOD_unregisterKeyboardAction110 = 110;

    private static final int METHOD_update111 = 111;

    private static final int METHOD_updateButtons112 = 112;

    private static final int METHOD_updateUI113 = 113;

    private static final int METHOD_validate114 = 114;

    private static MethodDescriptor[] getMdescriptor() {
        MethodDescriptor[] methods = new MethodDescriptor[115];
        try {
            methods[METHOD_action0] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("action", new Class[] { java.awt.Event.class, java.lang.Object.class }));
            methods[METHOD_action0].setDisplayName("");
            methods[METHOD_add1] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("add", new Class[] { javax.swing.Action.class }));
            methods[METHOD_add1].setDisplayName("");
            methods[METHOD_addNotify2] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("addNotify", new Class[] {}));
            methods[METHOD_addNotify2].setDisplayName("");
            methods[METHOD_addPropertyChangeListener3] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("addPropertyChangeListener", new Class[] { java.lang.String.class, java.beans.PropertyChangeListener.class }));
            methods[METHOD_addPropertyChangeListener3].setDisplayName("");
            methods[METHOD_addSeparator4] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("addSeparator", new Class[] { java.awt.Dimension.class }));
            methods[METHOD_addSeparator4].setDisplayName("");
            methods[METHOD_applyComponentOrientation5] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("applyComponentOrientation", new Class[] { java.awt.ComponentOrientation.class }));
            methods[METHOD_applyComponentOrientation5].setDisplayName("");
            methods[METHOD_areFocusTraversalKeysSet6] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("areFocusTraversalKeysSet", new Class[] { Integer.TYPE }));
            methods[METHOD_areFocusTraversalKeysSet6].setDisplayName("");
            methods[METHOD_bounds7] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("bounds", new Class[] {}));
            methods[METHOD_bounds7].setDisplayName("");
            methods[METHOD_checkImage8] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("checkImage", new Class[] { java.awt.Image.class, Integer.TYPE, Integer.TYPE, java.awt.image.ImageObserver.class }));
            methods[METHOD_checkImage8].setDisplayName("");
            methods[METHOD_computeVisibleRect9] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("computeVisibleRect", new Class[] { java.awt.Rectangle.class }));
            methods[METHOD_computeVisibleRect9].setDisplayName("");
            methods[METHOD_contains10] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("contains", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_contains10].setDisplayName("");
            methods[METHOD_countComponents11] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("countComponents", new Class[] {}));
            methods[METHOD_countComponents11].setDisplayName("");
            methods[METHOD_createImage12] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("createImage", new Class[] { java.awt.image.ImageProducer.class }));
            methods[METHOD_createImage12].setDisplayName("");
            methods[METHOD_createToolTip13] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("createToolTip", new Class[] {}));
            methods[METHOD_createToolTip13].setDisplayName("");
            methods[METHOD_createVolatileImage14] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("createVolatileImage", new Class[] { Integer.TYPE, Integer.TYPE, java.awt.ImageCapabilities.class }));
            methods[METHOD_createVolatileImage14].setDisplayName("");
            methods[METHOD_deliverEvent15] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("deliverEvent", new Class[] { java.awt.Event.class }));
            methods[METHOD_deliverEvent15].setDisplayName("");
            methods[METHOD_disable16] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("disable", new Class[] {}));
            methods[METHOD_disable16].setDisplayName("");
            methods[METHOD_dispatchEvent17] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("dispatchEvent", new Class[] { java.awt.AWTEvent.class }));
            methods[METHOD_dispatchEvent17].setDisplayName("");
            methods[METHOD_doLayout18] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("doLayout", new Class[] {}));
            methods[METHOD_doLayout18].setDisplayName("");
            methods[METHOD_enable19] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("enable", new Class[] {}));
            methods[METHOD_enable19].setDisplayName("");
            methods[METHOD_enableInputMethods20] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("enableInputMethods", new Class[] { Boolean.TYPE }));
            methods[METHOD_enableInputMethods20].setDisplayName("");
            methods[METHOD_findComponentAt21] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("findComponentAt", new Class[] { java.awt.Point.class }));
            methods[METHOD_findComponentAt21].setDisplayName("");
            methods[METHOD_firePropertyChange22] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("firePropertyChange", new Class[] { java.lang.String.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_firePropertyChange22].setDisplayName("");
            methods[METHOD_getActionForKeyStroke23] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getActionForKeyStroke", new Class[] { javax.swing.KeyStroke.class }));
            methods[METHOD_getActionForKeyStroke23].setDisplayName("");
            methods[METHOD_getBeanSet24] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getBeanSet", new Class[] { org.jgenesis.beanset.BeanSet.class }));
            methods[METHOD_getBeanSet24].setDisplayName("");
            methods[METHOD_getBounds25] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getBounds", new Class[] { java.awt.Rectangle.class }));
            methods[METHOD_getBounds25].setDisplayName("");
            methods[METHOD_getClientProperty26] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getClientProperty", new Class[] { java.lang.Object.class }));
            methods[METHOD_getClientProperty26].setDisplayName("");
            methods[METHOD_getComponentAt27] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getComponentAt", new Class[] { java.awt.Point.class }));
            methods[METHOD_getComponentAt27].setDisplayName("");
            methods[METHOD_getComponentIndex28] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getComponentIndex", new Class[] { java.awt.Component.class }));
            methods[METHOD_getComponentIndex28].setDisplayName("");
            methods[METHOD_getComponentZOrder29] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getComponentZOrder", new Class[] { java.awt.Component.class }));
            methods[METHOD_getComponentZOrder29].setDisplayName("");
            methods[METHOD_getConditionForKeyStroke30] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getConditionForKeyStroke", new Class[] { javax.swing.KeyStroke.class }));
            methods[METHOD_getConditionForKeyStroke30].setDisplayName("");
            methods[METHOD_getDefaultLocale31] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getDefaultLocale", new Class[] {}));
            methods[METHOD_getDefaultLocale31].setDisplayName("");
            methods[METHOD_getFontMetrics32] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getFontMetrics", new Class[] { java.awt.Font.class }));
            methods[METHOD_getFontMetrics32].setDisplayName("");
            methods[METHOD_getInputMap33] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getInputMap", new Class[] {}));
            methods[METHOD_getInputMap33].setDisplayName("");
            methods[METHOD_getInsets34] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getInsets", new Class[] { java.awt.Insets.class }));
            methods[METHOD_getInsets34].setDisplayName("");
            methods[METHOD_getListeners35] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getListeners", new Class[] { java.lang.Class.class }));
            methods[METHOD_getListeners35].setDisplayName("");
            methods[METHOD_getLocation36] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getLocation", new Class[] { java.awt.Point.class }));
            methods[METHOD_getLocation36].setDisplayName("");
            methods[METHOD_getMousePosition37] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getMousePosition", new Class[] { Boolean.TYPE }));
            methods[METHOD_getMousePosition37].setDisplayName("");
            methods[METHOD_getPopupLocation38] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getPopupLocation", new Class[] { java.awt.event.MouseEvent.class }));
            methods[METHOD_getPopupLocation38].setDisplayName("");
            methods[METHOD_getPropertyChangeListeners39] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getPropertyChangeListeners", new Class[] { java.lang.String.class }));
            methods[METHOD_getPropertyChangeListeners39].setDisplayName("");
            methods[METHOD_getSize40] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getSize", new Class[] { java.awt.Dimension.class }));
            methods[METHOD_getSize40].setDisplayName("");
            methods[METHOD_getToolTipLocation41] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getToolTipLocation", new Class[] { java.awt.event.MouseEvent.class }));
            methods[METHOD_getToolTipLocation41].setDisplayName("");
            methods[METHOD_getToolTipText42] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("getToolTipText", new Class[] { java.awt.event.MouseEvent.class }));
            methods[METHOD_getToolTipText42].setDisplayName("");
            methods[METHOD_gotFocus43] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("gotFocus", new Class[] { java.awt.Event.class, java.lang.Object.class }));
            methods[METHOD_gotFocus43].setDisplayName("");
            methods[METHOD_grabFocus44] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("grabFocus", new Class[] {}));
            methods[METHOD_grabFocus44].setDisplayName("");
            methods[METHOD_handleEvent45] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("handleEvent", new Class[] { java.awt.Event.class }));
            methods[METHOD_handleEvent45].setDisplayName("");
            methods[METHOD_hasFocus46] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("hasFocus", new Class[] {}));
            methods[METHOD_hasFocus46].setDisplayName("");
            methods[METHOD_hide47] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("hide", new Class[] {}));
            methods[METHOD_hide47].setDisplayName("");
            methods[METHOD_imageUpdate48] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("imageUpdate", new Class[] { java.awt.Image.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_imageUpdate48].setDisplayName("");
            methods[METHOD_insets49] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("insets", new Class[] {}));
            methods[METHOD_insets49].setDisplayName("");
            methods[METHOD_inside50] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("inside", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_inside50].setDisplayName("");
            methods[METHOD_invalidate51] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("invalidate", new Class[] {}));
            methods[METHOD_invalidate51].setDisplayName("");
            methods[METHOD_isAncestorOf52] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("isAncestorOf", new Class[] { java.awt.Component.class }));
            methods[METHOD_isAncestorOf52].setDisplayName("");
            methods[METHOD_isFocusCycleRoot53] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("isFocusCycleRoot", new Class[] { java.awt.Container.class }));
            methods[METHOD_isFocusCycleRoot53].setDisplayName("");
            methods[METHOD_isLightweightComponent54] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("isLightweightComponent", new Class[] { java.awt.Component.class }));
            methods[METHOD_isLightweightComponent54].setDisplayName("");
            methods[METHOD_keyDown55] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("keyDown", new Class[] { java.awt.Event.class, Integer.TYPE }));
            methods[METHOD_keyDown55].setDisplayName("");
            methods[METHOD_keyUp56] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("keyUp", new Class[] { java.awt.Event.class, Integer.TYPE }));
            methods[METHOD_keyUp56].setDisplayName("");
            methods[METHOD_layout57] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("layout", new Class[] {}));
            methods[METHOD_layout57].setDisplayName("");
            methods[METHOD_list58] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("list", new Class[] { java.io.PrintWriter.class, Integer.TYPE }));
            methods[METHOD_list58].setDisplayName("");
            methods[METHOD_locate59] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("locate", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_locate59].setDisplayName("");
            methods[METHOD_location60] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("location", new Class[] {}));
            methods[METHOD_location60].setDisplayName("");
            methods[METHOD_lostFocus61] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("lostFocus", new Class[] { java.awt.Event.class, java.lang.Object.class }));
            methods[METHOD_lostFocus61].setDisplayName("");
            methods[METHOD_minimumSize62] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("minimumSize", new Class[] {}));
            methods[METHOD_minimumSize62].setDisplayName("");
            methods[METHOD_mouseDown63] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("mouseDown", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseDown63].setDisplayName("");
            methods[METHOD_mouseDrag64] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("mouseDrag", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseDrag64].setDisplayName("");
            methods[METHOD_mouseEnter65] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("mouseEnter", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseEnter65].setDisplayName("");
            methods[METHOD_mouseExit66] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("mouseExit", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseExit66].setDisplayName("");
            methods[METHOD_mouseMove67] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("mouseMove", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseMove67].setDisplayName("");
            methods[METHOD_mouseUp68] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("mouseUp", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseUp68].setDisplayName("");
            methods[METHOD_move69] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("move", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_move69].setDisplayName("");
            methods[METHOD_nextFocus70] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("nextFocus", new Class[] {}));
            methods[METHOD_nextFocus70].setDisplayName("");
            methods[METHOD_paint71] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("paint", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_paint71].setDisplayName("");
            methods[METHOD_paintAll72] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("paintAll", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_paintAll72].setDisplayName("");
            methods[METHOD_paintComponents73] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("paintComponents", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_paintComponents73].setDisplayName("");
            methods[METHOD_paintImmediately74] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("paintImmediately", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_paintImmediately74].setDisplayName("");
            methods[METHOD_postEvent75] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("postEvent", new Class[] { java.awt.Event.class }));
            methods[METHOD_postEvent75].setDisplayName("");
            methods[METHOD_preferredSize76] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("preferredSize", new Class[] {}));
            methods[METHOD_preferredSize76].setDisplayName("");
            methods[METHOD_prepareImage77] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("prepareImage", new Class[] { java.awt.Image.class, Integer.TYPE, Integer.TYPE, java.awt.image.ImageObserver.class }));
            methods[METHOD_prepareImage77].setDisplayName("");
            methods[METHOD_print78] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("print", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_print78].setDisplayName("");
            methods[METHOD_printAll79] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("printAll", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_printAll79].setDisplayName("");
            methods[METHOD_printComponents80] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("printComponents", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_printComponents80].setDisplayName("");
            methods[METHOD_putClientProperty81] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("putClientProperty", new Class[] { java.lang.Object.class, java.lang.Object.class }));
            methods[METHOD_putClientProperty81].setDisplayName("");
            methods[METHOD_registerKeyboardAction82] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("registerKeyboardAction", new Class[] { java.awt.event.ActionListener.class, javax.swing.KeyStroke.class, Integer.TYPE }));
            methods[METHOD_registerKeyboardAction82].setDisplayName("");
            methods[METHOD_remove83] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("remove", new Class[] { java.awt.Component.class }));
            methods[METHOD_remove83].setDisplayName("");
            methods[METHOD_removeAll84] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("removeAll", new Class[] {}));
            methods[METHOD_removeAll84].setDisplayName("");
            methods[METHOD_removeNotify85] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("removeNotify", new Class[] {}));
            methods[METHOD_removeNotify85].setDisplayName("");
            methods[METHOD_removePropertyChangeListener86] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("removePropertyChangeListener", new Class[] { java.lang.String.class, java.beans.PropertyChangeListener.class }));
            methods[METHOD_removePropertyChangeListener86].setDisplayName("");
            methods[METHOD_repaint87] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("repaint", new Class[] { java.awt.Rectangle.class }));
            methods[METHOD_repaint87].setDisplayName("");
            methods[METHOD_requestDefaultFocus88] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("requestDefaultFocus", new Class[] {}));
            methods[METHOD_requestDefaultFocus88].setDisplayName("");
            methods[METHOD_requestFocus89] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("requestFocus", new Class[] {}));
            methods[METHOD_requestFocus89].setDisplayName("");
            methods[METHOD_requestFocusInWindow90] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("requestFocusInWindow", new Class[] {}));
            methods[METHOD_requestFocusInWindow90].setDisplayName("");
            methods[METHOD_resetKeyboardActions91] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("resetKeyboardActions", new Class[] {}));
            methods[METHOD_resetKeyboardActions91].setDisplayName("");
            methods[METHOD_reshape92] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("reshape", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_reshape92].setDisplayName("");
            methods[METHOD_resize93] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("resize", new Class[] { java.awt.Dimension.class }));
            methods[METHOD_resize93].setDisplayName("");
            methods[METHOD_revalidate94] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("revalidate", new Class[] {}));
            methods[METHOD_revalidate94].setDisplayName("");
            methods[METHOD_scrollRectToVisible95] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("scrollRectToVisible", new Class[] { java.awt.Rectangle.class }));
            methods[METHOD_scrollRectToVisible95].setDisplayName("");
            methods[METHOD_setBounds96] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("setBounds", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_setBounds96].setDisplayName("");
            methods[METHOD_setButtonsLayout97] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("setButtonsLayout", new Class[] {}));
            methods[METHOD_setButtonsLayout97].setDisplayName("");
            methods[METHOD_setComponentZOrder98] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("setComponentZOrder", new Class[] { java.awt.Component.class, Integer.TYPE }));
            methods[METHOD_setComponentZOrder98].setDisplayName("");
            methods[METHOD_setDefaultLocale99] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("setDefaultLocale", new Class[] { java.util.Locale.class }));
            methods[METHOD_setDefaultLocale99].setDisplayName("");
            methods[METHOD_setInputMap100] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("setInputMap", new Class[] { Integer.TYPE, javax.swing.InputMap.class }));
            methods[METHOD_setInputMap100].setDisplayName("");
            methods[METHOD_setLocation101] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("setLocation", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_setLocation101].setDisplayName("");
            methods[METHOD_setSize102] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("setSize", new Class[] { java.awt.Dimension.class }));
            methods[METHOD_setSize102].setDisplayName("");
            methods[METHOD_show103] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("show", new Class[] { Boolean.TYPE }));
            methods[METHOD_show103].setDisplayName("");
            methods[METHOD_size104] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("size", new Class[] {}));
            methods[METHOD_size104].setDisplayName("");
            methods[METHOD_toString105] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("toString", new Class[] {}));
            methods[METHOD_toString105].setDisplayName("");
            methods[METHOD_transferFocus106] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("transferFocus", new Class[] {}));
            methods[METHOD_transferFocus106].setDisplayName("");
            methods[METHOD_transferFocusBackward107] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("transferFocusBackward", new Class[] {}));
            methods[METHOD_transferFocusBackward107].setDisplayName("");
            methods[METHOD_transferFocusDownCycle108] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("transferFocusDownCycle", new Class[] {}));
            methods[METHOD_transferFocusDownCycle108].setDisplayName("");
            methods[METHOD_transferFocusUpCycle109] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("transferFocusUpCycle", new Class[] {}));
            methods[METHOD_transferFocusUpCycle109].setDisplayName("");
            methods[METHOD_unregisterKeyboardAction110] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("unregisterKeyboardAction", new Class[] { javax.swing.KeyStroke.class }));
            methods[METHOD_unregisterKeyboardAction110].setDisplayName("");
            methods[METHOD_update111] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("update", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_update111].setDisplayName("");
            methods[METHOD_updateButtons112] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("updateButtons", new Class[] {}));
            methods[METHOD_updateButtons112].setDisplayName("");
            methods[METHOD_updateUI113] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("updateUI", new Class[] {}));
            methods[METHOD_updateUI113].setDisplayName("");
            methods[METHOD_validate114] = new MethodDescriptor(org.jgenesis.swing.JGBeanNavigator.class.getMethod("validate", new Class[] {}));
            methods[METHOD_validate114].setDisplayName("");
        } catch (Exception e) {
        }
        return methods;
    }

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
}
