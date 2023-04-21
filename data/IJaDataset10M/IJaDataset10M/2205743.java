package org.jgenesis.swing;

import java.beans.*;
import org.jgenesis.swing.editors.BeanManagerEditor;
import org.jgenesis.swing.editors.EditableModeEditor;

/**
 * @author root
 */
public class JGDateTextFieldBeanInfo extends SimpleBeanInfo {

    private static BeanDescriptor getBdescriptor() {
        BeanDescriptor beanDescriptor = new BeanDescriptor(JGDateTextField.class, null);
        return beanDescriptor;
    }

    private static final int PROPERTY_accessibleContext = 0;

    private static final int PROPERTY_action = 1;

    private static final int PROPERTY_actionCommand = 2;

    private static final int PROPERTY_actionListeners = 3;

    private static final int PROPERTY_actionMap = 4;

    private static final int PROPERTY_actions = 5;

    private static final int PROPERTY_alignmentX = 6;

    private static final int PROPERTY_alignmentY = 7;

    private static final int PROPERTY_allowEmpty = 8;

    private static final int PROPERTY_allowInvalidDate = 9;

    private static final int PROPERTY_ancestorListeners = 10;

    private static final int PROPERTY_autoscrolls = 11;

    private static final int PROPERTY_autoTab = 12;

    private static final int PROPERTY_background = 13;

    private static final int PROPERTY_backgroundSet = 14;

    private static final int PROPERTY_beanManager = 15;

    private static final int PROPERTY_border = 16;

    private static final int PROPERTY_bounds = 17;

    private static final int PROPERTY_caret = 18;

    private static final int PROPERTY_caretColor = 19;

    private static final int PROPERTY_caretListeners = 20;

    private static final int PROPERTY_caretPosition = 21;

    private static final int PROPERTY_colorModel = 22;

    private static final int PROPERTY_columns = 23;

    private static final int PROPERTY_component = 24;

    private static final int PROPERTY_componentCount = 25;

    private static final int PROPERTY_componentListeners = 26;

    private static final int PROPERTY_componentOrientation = 27;

    private static final int PROPERTY_componentPopupMenu = 28;

    private static final int PROPERTY_components = 29;

    private static final int PROPERTY_containerListeners = 30;

    private static final int PROPERTY_cursor = 31;

    private static final int PROPERTY_cursorSet = 32;

    private static final int PROPERTY_datePattern = 33;

    private static final int PROPERTY_debugGraphicsOptions = 34;

    private static final int PROPERTY_disabledTextColor = 35;

    private static final int PROPERTY_displayable = 36;

    private static final int PROPERTY_document = 37;

    private static final int PROPERTY_doubleBuffered = 38;

    private static final int PROPERTY_dragEnabled = 39;

    private static final int PROPERTY_dropTarget = 40;

    private static final int PROPERTY_editableMode = 41;

    private static final int PROPERTY_enabled = 42;

    private static final int PROPERTY_fieldName = 43;

    private static final int PROPERTY_focusable = 44;

    private static final int PROPERTY_focusAccelerator = 45;

    private static final int PROPERTY_focusCycleRoot = 46;

    private static final int PROPERTY_focusCycleRootAncestor = 47;

    private static final int PROPERTY_focusGainSelect = 48;

    private static final int PROPERTY_focusListeners = 49;

    private static final int PROPERTY_focusOwner = 50;

    private static final int PROPERTY_focusTraversable = 51;

    private static final int PROPERTY_focusTraversalKeys = 52;

    private static final int PROPERTY_focusTraversalKeysEnabled = 53;

    private static final int PROPERTY_focusTraversalPolicy = 54;

    private static final int PROPERTY_focusTraversalPolicyProvider = 55;

    private static final int PROPERTY_focusTraversalPolicySet = 56;

    private static final int PROPERTY_font = 57;

    private static final int PROPERTY_fontSet = 58;

    private static final int PROPERTY_foreground = 59;

    private static final int PROPERTY_foregroundSet = 60;

    private static final int PROPERTY_graphics = 61;

    private static final int PROPERTY_graphicsConfiguration = 62;

    private static final int PROPERTY_height = 63;

    private static final int PROPERTY_hierarchyBoundsListeners = 64;

    private static final int PROPERTY_hierarchyListeners = 65;

    private static final int PROPERTY_highlighter = 66;

    private static final int PROPERTY_horizontalAlignment = 67;

    private static final int PROPERTY_horizontalVisibility = 68;

    private static final int PROPERTY_ignoreRepaint = 69;

    private static final int PROPERTY_inheritsPopupMenu = 70;

    private static final int PROPERTY_inputContext = 71;

    private static final int PROPERTY_inputMethodListeners = 72;

    private static final int PROPERTY_inputMethodRequests = 73;

    private static final int PROPERTY_inputVerifier = 74;

    private static final int PROPERTY_insets = 75;

    private static final int PROPERTY_keyListeners = 76;

    private static final int PROPERTY_keymap = 77;

    private static final int PROPERTY_layout = 78;

    private static final int PROPERTY_lightweight = 79;

    private static final int PROPERTY_locale = 80;

    private static final int PROPERTY_locationOnScreen = 81;

    private static final int PROPERTY_managingFocus = 82;

    private static final int PROPERTY_margin = 83;

    private static final int PROPERTY_maximumSize = 84;

    private static final int PROPERTY_maximumSizeSet = 85;

    private static final int PROPERTY_maxLength = 86;

    private static final int PROPERTY_minimumSize = 87;

    private static final int PROPERTY_minimumSizeSet = 88;

    private static final int PROPERTY_mouseListeners = 89;

    private static final int PROPERTY_mouseMotionListeners = 90;

    private static final int PROPERTY_mousePosition = 91;

    private static final int PROPERTY_mouseWheelListeners = 92;

    private static final int PROPERTY_name = 93;

    private static final int PROPERTY_navigationFilter = 94;

    private static final int PROPERTY_nextFocusableComponent = 95;

    private static final int PROPERTY_opaque = 96;

    private static final int PROPERTY_optimizedDrawingEnabled = 97;

    private static final int PROPERTY_paintingTile = 98;

    private static final int PROPERTY_parent = 99;

    private static final int PROPERTY_passwordEnable = 100;

    private static final int PROPERTY_peer = 101;

    private static final int PROPERTY_preferredScrollableViewportSize = 102;

    private static final int PROPERTY_preferredSize = 103;

    private static final int PROPERTY_preferredSizeSet = 104;

    private static final int PROPERTY_propertyChangeListeners = 105;

    private static final int PROPERTY_registeredKeyStrokes = 106;

    private static final int PROPERTY_requestFocusEnabled = 107;

    private static final int PROPERTY_rootPane = 108;

    private static final int PROPERTY_scrollableTracksViewportHeight = 109;

    private static final int PROPERTY_scrollableTracksViewportWidth = 110;

    private static final int PROPERTY_scrollOffset = 111;

    private static final int PROPERTY_selectedText = 112;

    private static final int PROPERTY_selectedTextColor = 113;

    private static final int PROPERTY_selectionColor = 114;

    private static final int PROPERTY_selectionEnd = 115;

    private static final int PROPERTY_selectionStart = 116;

    private static final int PROPERTY_showing = 117;

    private static final int PROPERTY_showValidationDialogs = 118;

    private static final int PROPERTY_text = 119;

    private static final int PROPERTY_toolkit = 120;

    private static final int PROPERTY_toolTipText = 121;

    private static final int PROPERTY_topLevelAncestor = 122;

    private static final int PROPERTY_transferHandler = 123;

    private static final int PROPERTY_treeLock = 124;

    private static final int PROPERTY_UI = 125;

    private static final int PROPERTY_UIClassID = 126;

    private static final int PROPERTY_valid = 127;

    private static final int PROPERTY_validateRoot = 128;

    private static final int PROPERTY_verifyInputWhenFocusTarget = 129;

    private static final int PROPERTY_vetoableChangeListeners = 130;

    private static final int PROPERTY_visible = 131;

    private static final int PROPERTY_visibleRect = 132;

    private static final int PROPERTY_width = 133;

    private static final int PROPERTY_x = 134;

    private static final int PROPERTY_y = 135;

    private static PropertyDescriptor[] getPdescriptor() {
        PropertyDescriptor[] properties = new PropertyDescriptor[136];
        try {
            properties[PROPERTY_accessibleContext] = new PropertyDescriptor("accessibleContext", JGDateTextField.class, "getAccessibleContext", null);
            properties[PROPERTY_accessibleContext].setExpert(true);
            properties[PROPERTY_action] = new PropertyDescriptor("action", JGDateTextField.class, "getAction", "setAction");
            properties[PROPERTY_action].setExpert(true);
            properties[PROPERTY_actionCommand] = new PropertyDescriptor("actionCommand", JGDateTextField.class, null, "setActionCommand");
            properties[PROPERTY_actionCommand].setExpert(true);
            properties[PROPERTY_actionListeners] = new PropertyDescriptor("actionListeners", JGDateTextField.class, "getActionListeners", null);
            properties[PROPERTY_actionListeners].setExpert(true);
            properties[PROPERTY_actionMap] = new PropertyDescriptor("actionMap", JGDateTextField.class, "getActionMap", "setActionMap");
            properties[PROPERTY_actionMap].setExpert(true);
            properties[PROPERTY_actions] = new PropertyDescriptor("actions", JGDateTextField.class, "getActions", null);
            properties[PROPERTY_actions].setExpert(true);
            properties[PROPERTY_alignmentX] = new PropertyDescriptor("alignmentX", JGDateTextField.class, "getAlignmentX", "setAlignmentX");
            properties[PROPERTY_alignmentX].setExpert(true);
            properties[PROPERTY_alignmentY] = new PropertyDescriptor("alignmentY", JGDateTextField.class, "getAlignmentY", "setAlignmentY");
            properties[PROPERTY_alignmentY].setExpert(true);
            properties[PROPERTY_allowEmpty] = new PropertyDescriptor("allowEmpty", JGDateTextField.class, "isAllowEmpty", "setAllowEmpty");
            properties[PROPERTY_allowEmpty].setPreferred(true);
            properties[PROPERTY_allowInvalidDate] = new PropertyDescriptor("allowInvalidDate", JGDateTextField.class, "isAllowInvalidDate", "setAllowInvalidDate");
            properties[PROPERTY_allowInvalidDate].setPreferred(true);
            properties[PROPERTY_ancestorListeners] = new PropertyDescriptor("ancestorListeners", JGDateTextField.class, "getAncestorListeners", null);
            properties[PROPERTY_ancestorListeners].setExpert(true);
            properties[PROPERTY_autoscrolls] = new PropertyDescriptor("autoscrolls", JGDateTextField.class, "getAutoscrolls", "setAutoscrolls");
            properties[PROPERTY_autoscrolls].setExpert(true);
            properties[PROPERTY_autoTab] = new PropertyDescriptor("autoTab", JGDateTextField.class, "isAutoTab", "setAutoTab");
            properties[PROPERTY_autoTab].setPreferred(true);
            properties[PROPERTY_background] = new PropertyDescriptor("background", JGDateTextField.class, "getBackground", "setBackground");
            properties[PROPERTY_background].setExpert(true);
            properties[PROPERTY_backgroundSet] = new PropertyDescriptor("backgroundSet", JGDateTextField.class, "isBackgroundSet", null);
            properties[PROPERTY_backgroundSet].setExpert(true);
            properties[PROPERTY_beanManager] = new PropertyDescriptor("beanManager", JGDateTextField.class, "getBeanManager", "setBeanManager");
            properties[PROPERTY_beanManager].setPreferred(true);
            properties[PROPERTY_beanManager].setPropertyEditorClass(BeanManagerEditor.class);
            properties[PROPERTY_border] = new PropertyDescriptor("border", JGDateTextField.class, "getBorder", "setBorder");
            properties[PROPERTY_border].setExpert(true);
            properties[PROPERTY_bounds] = new PropertyDescriptor("bounds", JGDateTextField.class, "getBounds", "setBounds");
            properties[PROPERTY_bounds].setExpert(true);
            properties[PROPERTY_caret] = new PropertyDescriptor("caret", JGDateTextField.class, "getCaret", "setCaret");
            properties[PROPERTY_caret].setExpert(true);
            properties[PROPERTY_caretColor] = new PropertyDescriptor("caretColor", JGDateTextField.class, "getCaretColor", "setCaretColor");
            properties[PROPERTY_caretColor].setExpert(true);
            properties[PROPERTY_caretListeners] = new PropertyDescriptor("caretListeners", JGDateTextField.class, "getCaretListeners", null);
            properties[PROPERTY_caretListeners].setExpert(true);
            properties[PROPERTY_caretPosition] = new PropertyDescriptor("caretPosition", JGDateTextField.class, "getCaretPosition", "setCaretPosition");
            properties[PROPERTY_caretPosition].setExpert(true);
            properties[PROPERTY_colorModel] = new PropertyDescriptor("colorModel", JGDateTextField.class, "getColorModel", null);
            properties[PROPERTY_colorModel].setExpert(true);
            properties[PROPERTY_columns] = new PropertyDescriptor("columns", JGDateTextField.class, "getColumns", "setColumns");
            properties[PROPERTY_columns].setExpert(true);
            properties[PROPERTY_component] = new IndexedPropertyDescriptor("component", JGDateTextField.class, null, null, "getComponent", null);
            properties[PROPERTY_component].setExpert(true);
            properties[PROPERTY_componentCount] = new PropertyDescriptor("componentCount", JGDateTextField.class, "getComponentCount", null);
            properties[PROPERTY_componentCount].setExpert(true);
            properties[PROPERTY_componentListeners] = new PropertyDescriptor("componentListeners", JGDateTextField.class, "getComponentListeners", null);
            properties[PROPERTY_componentListeners].setExpert(true);
            properties[PROPERTY_componentOrientation] = new PropertyDescriptor("componentOrientation", JGDateTextField.class, "getComponentOrientation", "setComponentOrientation");
            properties[PROPERTY_componentOrientation].setExpert(true);
            properties[PROPERTY_componentPopupMenu] = new PropertyDescriptor("componentPopupMenu", JGDateTextField.class, "getComponentPopupMenu", "setComponentPopupMenu");
            properties[PROPERTY_componentPopupMenu].setExpert(true);
            properties[PROPERTY_components] = new PropertyDescriptor("components", JGDateTextField.class, "getComponents", null);
            properties[PROPERTY_components].setExpert(true);
            properties[PROPERTY_containerListeners] = new PropertyDescriptor("containerListeners", JGDateTextField.class, "getContainerListeners", null);
            properties[PROPERTY_containerListeners].setExpert(true);
            properties[PROPERTY_cursor] = new PropertyDescriptor("cursor", JGDateTextField.class, "getCursor", "setCursor");
            properties[PROPERTY_cursor].setExpert(true);
            properties[PROPERTY_cursorSet] = new PropertyDescriptor("cursorSet", JGDateTextField.class, "isCursorSet", null);
            properties[PROPERTY_cursorSet].setExpert(true);
            properties[PROPERTY_datePattern] = new PropertyDescriptor("datePattern", JGDateTextField.class, "getDatePattern", "setDatePattern");
            properties[PROPERTY_datePattern].setPreferred(true);
            properties[PROPERTY_debugGraphicsOptions] = new PropertyDescriptor("debugGraphicsOptions", JGDateTextField.class, "getDebugGraphicsOptions", "setDebugGraphicsOptions");
            properties[PROPERTY_debugGraphicsOptions].setExpert(true);
            properties[PROPERTY_disabledTextColor] = new PropertyDescriptor("disabledTextColor", JGDateTextField.class, "getDisabledTextColor", "setDisabledTextColor");
            properties[PROPERTY_disabledTextColor].setExpert(true);
            properties[PROPERTY_displayable] = new PropertyDescriptor("displayable", JGDateTextField.class, "isDisplayable", null);
            properties[PROPERTY_displayable].setExpert(true);
            properties[PROPERTY_document] = new PropertyDescriptor("document", JGDateTextField.class, "getDocument", "setDocument");
            properties[PROPERTY_document].setPreferred(true);
            properties[PROPERTY_doubleBuffered] = new PropertyDescriptor("doubleBuffered", JGDateTextField.class, "isDoubleBuffered", "setDoubleBuffered");
            properties[PROPERTY_doubleBuffered].setExpert(true);
            properties[PROPERTY_dragEnabled] = new PropertyDescriptor("dragEnabled", JGDateTextField.class, "getDragEnabled", "setDragEnabled");
            properties[PROPERTY_dragEnabled].setExpert(true);
            properties[PROPERTY_dropTarget] = new PropertyDescriptor("dropTarget", JGDateTextField.class, "getDropTarget", "setDropTarget");
            properties[PROPERTY_dropTarget].setExpert(true);
            properties[PROPERTY_editableMode] = new PropertyDescriptor("editableMode", JGDateTextField.class, "getEditableMode", "setEditableMode");
            properties[PROPERTY_editableMode].setPreferred(true);
            properties[PROPERTY_editableMode].setPropertyEditorClass(EditableModeEditor.class);
            properties[PROPERTY_enabled] = new PropertyDescriptor("enabled", JGDateTextField.class, "isEnabled", "setEnabled");
            properties[PROPERTY_enabled].setPreferred(true);
            properties[PROPERTY_fieldName] = new PropertyDescriptor("fieldName", JGDateTextField.class, "getFieldName", "setFieldName");
            properties[PROPERTY_fieldName].setPreferred(true);
            properties[PROPERTY_focusable] = new PropertyDescriptor("focusable", JGDateTextField.class, "isFocusable", "setFocusable");
            properties[PROPERTY_focusable].setExpert(true);
            properties[PROPERTY_focusAccelerator] = new PropertyDescriptor("focusAccelerator", JGDateTextField.class, "getFocusAccelerator", "setFocusAccelerator");
            properties[PROPERTY_focusAccelerator].setExpert(true);
            properties[PROPERTY_focusCycleRoot] = new PropertyDescriptor("focusCycleRoot", JGDateTextField.class, "isFocusCycleRoot", "setFocusCycleRoot");
            properties[PROPERTY_focusCycleRoot].setExpert(true);
            properties[PROPERTY_focusCycleRootAncestor] = new PropertyDescriptor("focusCycleRootAncestor", JGDateTextField.class, "getFocusCycleRootAncestor", null);
            properties[PROPERTY_focusCycleRootAncestor].setExpert(true);
            properties[PROPERTY_focusGainSelect] = new PropertyDescriptor("focusGainSelect", JGDateTextField.class, "isFocusGainSelect", "setFocusGainSelect");
            properties[PROPERTY_focusGainSelect].setPreferred(true);
            properties[PROPERTY_focusListeners] = new PropertyDescriptor("focusListeners", JGDateTextField.class, "getFocusListeners", null);
            properties[PROPERTY_focusListeners].setExpert(true);
            properties[PROPERTY_focusOwner] = new PropertyDescriptor("focusOwner", JGDateTextField.class, "isFocusOwner", null);
            properties[PROPERTY_focusOwner].setExpert(true);
            properties[PROPERTY_focusTraversable] = new PropertyDescriptor("focusTraversable", JGDateTextField.class, "isFocusTraversable", null);
            properties[PROPERTY_focusTraversable].setExpert(true);
            properties[PROPERTY_focusTraversalKeys] = new IndexedPropertyDescriptor("focusTraversalKeys", JGDateTextField.class, null, null, "getFocusTraversalKeys", "setFocusTraversalKeys");
            properties[PROPERTY_focusTraversalKeys].setExpert(true);
            properties[PROPERTY_focusTraversalKeysEnabled] = new PropertyDescriptor("focusTraversalKeysEnabled", JGDateTextField.class, "getFocusTraversalKeysEnabled", "setFocusTraversalKeysEnabled");
            properties[PROPERTY_focusTraversalKeysEnabled].setExpert(true);
            properties[PROPERTY_focusTraversalPolicy] = new PropertyDescriptor("focusTraversalPolicy", JGDateTextField.class, "getFocusTraversalPolicy", "setFocusTraversalPolicy");
            properties[PROPERTY_focusTraversalPolicy].setExpert(true);
            properties[PROPERTY_focusTraversalPolicyProvider] = new PropertyDescriptor("focusTraversalPolicyProvider", JGDateTextField.class, "isFocusTraversalPolicyProvider", "setFocusTraversalPolicyProvider");
            properties[PROPERTY_focusTraversalPolicyProvider].setExpert(true);
            properties[PROPERTY_focusTraversalPolicySet] = new PropertyDescriptor("focusTraversalPolicySet", JGDateTextField.class, "isFocusTraversalPolicySet", null);
            properties[PROPERTY_focusTraversalPolicySet].setExpert(true);
            properties[PROPERTY_font] = new PropertyDescriptor("font", JGDateTextField.class, "getFont", "setFont");
            properties[PROPERTY_font].setExpert(true);
            properties[PROPERTY_fontSet] = new PropertyDescriptor("fontSet", JGDateTextField.class, "isFontSet", null);
            properties[PROPERTY_fontSet].setExpert(true);
            properties[PROPERTY_foreground] = new PropertyDescriptor("foreground", JGDateTextField.class, "getForeground", "setForeground");
            properties[PROPERTY_foreground].setExpert(true);
            properties[PROPERTY_foregroundSet] = new PropertyDescriptor("foregroundSet", JGDateTextField.class, "isForegroundSet", null);
            properties[PROPERTY_foregroundSet].setExpert(true);
            properties[PROPERTY_graphics] = new PropertyDescriptor("graphics", JGDateTextField.class, "getGraphics", null);
            properties[PROPERTY_graphics].setExpert(true);
            properties[PROPERTY_graphicsConfiguration] = new PropertyDescriptor("graphicsConfiguration", JGDateTextField.class, "getGraphicsConfiguration", null);
            properties[PROPERTY_graphicsConfiguration].setExpert(true);
            properties[PROPERTY_height] = new PropertyDescriptor("height", JGDateTextField.class, "getHeight", null);
            properties[PROPERTY_height].setExpert(true);
            properties[PROPERTY_hierarchyBoundsListeners] = new PropertyDescriptor("hierarchyBoundsListeners", JGDateTextField.class, "getHierarchyBoundsListeners", null);
            properties[PROPERTY_hierarchyBoundsListeners].setExpert(true);
            properties[PROPERTY_hierarchyListeners] = new PropertyDescriptor("hierarchyListeners", JGDateTextField.class, "getHierarchyListeners", null);
            properties[PROPERTY_hierarchyListeners].setExpert(true);
            properties[PROPERTY_highlighter] = new PropertyDescriptor("highlighter", JGDateTextField.class, "getHighlighter", "setHighlighter");
            properties[PROPERTY_highlighter].setExpert(true);
            properties[PROPERTY_horizontalAlignment] = new PropertyDescriptor("horizontalAlignment", JGDateTextField.class, "getHorizontalAlignment", "setHorizontalAlignment");
            properties[PROPERTY_horizontalAlignment].setExpert(true);
            properties[PROPERTY_horizontalVisibility] = new PropertyDescriptor("horizontalVisibility", JGDateTextField.class, "getHorizontalVisibility", null);
            properties[PROPERTY_horizontalVisibility].setExpert(true);
            properties[PROPERTY_ignoreRepaint] = new PropertyDescriptor("ignoreRepaint", JGDateTextField.class, "getIgnoreRepaint", "setIgnoreRepaint");
            properties[PROPERTY_ignoreRepaint].setExpert(true);
            properties[PROPERTY_inheritsPopupMenu] = new PropertyDescriptor("inheritsPopupMenu", JGDateTextField.class, "getInheritsPopupMenu", "setInheritsPopupMenu");
            properties[PROPERTY_inheritsPopupMenu].setExpert(true);
            properties[PROPERTY_inputContext] = new PropertyDescriptor("inputContext", JGDateTextField.class, "getInputContext", null);
            properties[PROPERTY_inputContext].setExpert(true);
            properties[PROPERTY_inputMethodListeners] = new PropertyDescriptor("inputMethodListeners", JGDateTextField.class, "getInputMethodListeners", null);
            properties[PROPERTY_inputMethodListeners].setExpert(true);
            properties[PROPERTY_inputMethodRequests] = new PropertyDescriptor("inputMethodRequests", JGDateTextField.class, "getInputMethodRequests", null);
            properties[PROPERTY_inputMethodRequests].setExpert(true);
            properties[PROPERTY_inputVerifier] = new PropertyDescriptor("inputVerifier", JGDateTextField.class, "getInputVerifier", "setInputVerifier");
            properties[PROPERTY_inputVerifier].setExpert(true);
            properties[PROPERTY_insets] = new PropertyDescriptor("insets", JGDateTextField.class, "getInsets", null);
            properties[PROPERTY_insets].setExpert(true);
            properties[PROPERTY_keyListeners] = new PropertyDescriptor("keyListeners", JGDateTextField.class, "getKeyListeners", null);
            properties[PROPERTY_keyListeners].setExpert(true);
            properties[PROPERTY_keymap] = new PropertyDescriptor("keymap", JGDateTextField.class, "getKeymap", "setKeymap");
            properties[PROPERTY_keymap].setExpert(true);
            properties[PROPERTY_layout] = new PropertyDescriptor("layout", JGDateTextField.class, "getLayout", "setLayout");
            properties[PROPERTY_layout].setExpert(true);
            properties[PROPERTY_lightweight] = new PropertyDescriptor("lightweight", JGDateTextField.class, "isLightweight", null);
            properties[PROPERTY_lightweight].setExpert(true);
            properties[PROPERTY_locale] = new PropertyDescriptor("locale", JGDateTextField.class, "getLocale", "setLocale");
            properties[PROPERTY_locale].setExpert(true);
            properties[PROPERTY_locationOnScreen] = new PropertyDescriptor("locationOnScreen", JGDateTextField.class, "getLocationOnScreen", null);
            properties[PROPERTY_locationOnScreen].setExpert(true);
            properties[PROPERTY_managingFocus] = new PropertyDescriptor("managingFocus", JGDateTextField.class, "isManagingFocus", null);
            properties[PROPERTY_managingFocus].setExpert(true);
            properties[PROPERTY_margin] = new PropertyDescriptor("margin", JGDateTextField.class, "getMargin", "setMargin");
            properties[PROPERTY_margin].setExpert(true);
            properties[PROPERTY_maximumSize] = new PropertyDescriptor("maximumSize", JGDateTextField.class, "getMaximumSize", "setMaximumSize");
            properties[PROPERTY_maximumSize].setExpert(true);
            properties[PROPERTY_maximumSizeSet] = new PropertyDescriptor("maximumSizeSet", JGDateTextField.class, "isMaximumSizeSet", null);
            properties[PROPERTY_maximumSizeSet].setExpert(true);
            properties[PROPERTY_maxLength] = new PropertyDescriptor("maxLength", JGDateTextField.class, "getMaxLength", "setMaxLength");
            properties[PROPERTY_maxLength].setPreferred(true);
            properties[PROPERTY_minimumSize] = new PropertyDescriptor("minimumSize", JGDateTextField.class, "getMinimumSize", "setMinimumSize");
            properties[PROPERTY_minimumSize].setExpert(true);
            properties[PROPERTY_minimumSizeSet] = new PropertyDescriptor("minimumSizeSet", JGDateTextField.class, "isMinimumSizeSet", null);
            properties[PROPERTY_minimumSizeSet].setExpert(true);
            properties[PROPERTY_mouseListeners] = new PropertyDescriptor("mouseListeners", JGDateTextField.class, "getMouseListeners", null);
            properties[PROPERTY_mouseListeners].setExpert(true);
            properties[PROPERTY_mouseMotionListeners] = new PropertyDescriptor("mouseMotionListeners", JGDateTextField.class, "getMouseMotionListeners", null);
            properties[PROPERTY_mouseMotionListeners].setExpert(true);
            properties[PROPERTY_mousePosition] = new PropertyDescriptor("mousePosition", JGDateTextField.class, "getMousePosition", null);
            properties[PROPERTY_mousePosition].setExpert(true);
            properties[PROPERTY_mouseWheelListeners] = new PropertyDescriptor("mouseWheelListeners", JGDateTextField.class, "getMouseWheelListeners", null);
            properties[PROPERTY_mouseWheelListeners].setExpert(true);
            properties[PROPERTY_name] = new PropertyDescriptor("name", JGDateTextField.class, "getName", "setName");
            properties[PROPERTY_name].setExpert(true);
            properties[PROPERTY_navigationFilter] = new PropertyDescriptor("navigationFilter", JGDateTextField.class, "getNavigationFilter", "setNavigationFilter");
            properties[PROPERTY_navigationFilter].setExpert(true);
            properties[PROPERTY_nextFocusableComponent] = new PropertyDescriptor("nextFocusableComponent", JGDateTextField.class, "getNextFocusableComponent", "setNextFocusableComponent");
            properties[PROPERTY_nextFocusableComponent].setExpert(true);
            properties[PROPERTY_opaque] = new PropertyDescriptor("opaque", JGDateTextField.class, "isOpaque", "setOpaque");
            properties[PROPERTY_opaque].setExpert(true);
            properties[PROPERTY_optimizedDrawingEnabled] = new PropertyDescriptor("optimizedDrawingEnabled", JGDateTextField.class, "isOptimizedDrawingEnabled", null);
            properties[PROPERTY_optimizedDrawingEnabled].setExpert(true);
            properties[PROPERTY_paintingTile] = new PropertyDescriptor("paintingTile", JGDateTextField.class, "isPaintingTile", null);
            properties[PROPERTY_paintingTile].setExpert(true);
            properties[PROPERTY_parent] = new PropertyDescriptor("parent", JGDateTextField.class, "getParent", null);
            properties[PROPERTY_parent].setExpert(true);
            properties[PROPERTY_passwordEnable] = new PropertyDescriptor("passwordEnable", JGDateTextField.class, "isPasswordEnable", "setPasswordEnable");
            properties[PROPERTY_passwordEnable].setPreferred(true);
            properties[PROPERTY_peer] = new PropertyDescriptor("peer", JGDateTextField.class, "getPeer", null);
            properties[PROPERTY_peer].setExpert(true);
            properties[PROPERTY_preferredScrollableViewportSize] = new PropertyDescriptor("preferredScrollableViewportSize", JGDateTextField.class, "getPreferredScrollableViewportSize", null);
            properties[PROPERTY_preferredScrollableViewportSize].setExpert(true);
            properties[PROPERTY_preferredSize] = new PropertyDescriptor("preferredSize", JGDateTextField.class, "getPreferredSize", "setPreferredSize");
            properties[PROPERTY_preferredSize].setExpert(true);
            properties[PROPERTY_preferredSizeSet] = new PropertyDescriptor("preferredSizeSet", JGDateTextField.class, "isPreferredSizeSet", null);
            properties[PROPERTY_preferredSizeSet].setExpert(true);
            properties[PROPERTY_propertyChangeListeners] = new PropertyDescriptor("propertyChangeListeners", JGDateTextField.class, "getPropertyChangeListeners", null);
            properties[PROPERTY_propertyChangeListeners].setExpert(true);
            properties[PROPERTY_registeredKeyStrokes] = new PropertyDescriptor("registeredKeyStrokes", JGDateTextField.class, "getRegisteredKeyStrokes", null);
            properties[PROPERTY_registeredKeyStrokes].setExpert(true);
            properties[PROPERTY_requestFocusEnabled] = new PropertyDescriptor("requestFocusEnabled", JGDateTextField.class, "isRequestFocusEnabled", "setRequestFocusEnabled");
            properties[PROPERTY_requestFocusEnabled].setExpert(true);
            properties[PROPERTY_rootPane] = new PropertyDescriptor("rootPane", JGDateTextField.class, "getRootPane", null);
            properties[PROPERTY_rootPane].setExpert(true);
            properties[PROPERTY_scrollableTracksViewportHeight] = new PropertyDescriptor("scrollableTracksViewportHeight", JGDateTextField.class, "getScrollableTracksViewportHeight", null);
            properties[PROPERTY_scrollableTracksViewportHeight].setExpert(true);
            properties[PROPERTY_scrollableTracksViewportWidth] = new PropertyDescriptor("scrollableTracksViewportWidth", JGDateTextField.class, "getScrollableTracksViewportWidth", null);
            properties[PROPERTY_scrollableTracksViewportWidth].setExpert(true);
            properties[PROPERTY_scrollOffset] = new PropertyDescriptor("scrollOffset", JGDateTextField.class, "getScrollOffset", "setScrollOffset");
            properties[PROPERTY_scrollOffset].setExpert(true);
            properties[PROPERTY_selectedText] = new PropertyDescriptor("selectedText", JGDateTextField.class, "getSelectedText", null);
            properties[PROPERTY_selectedText].setExpert(true);
            properties[PROPERTY_selectedTextColor] = new PropertyDescriptor("selectedTextColor", JGDateTextField.class, "getSelectedTextColor", "setSelectedTextColor");
            properties[PROPERTY_selectedTextColor].setExpert(true);
            properties[PROPERTY_selectionColor] = new PropertyDescriptor("selectionColor", JGDateTextField.class, "getSelectionColor", "setSelectionColor");
            properties[PROPERTY_selectionColor].setExpert(true);
            properties[PROPERTY_selectionEnd] = new PropertyDescriptor("selectionEnd", JGDateTextField.class, "getSelectionEnd", "setSelectionEnd");
            properties[PROPERTY_selectionEnd].setExpert(true);
            properties[PROPERTY_selectionStart] = new PropertyDescriptor("selectionStart", JGDateTextField.class, "getSelectionStart", "setSelectionStart");
            properties[PROPERTY_selectionStart].setExpert(true);
            properties[PROPERTY_showing] = new PropertyDescriptor("showing", JGDateTextField.class, "isShowing", null);
            properties[PROPERTY_showing].setExpert(true);
            properties[PROPERTY_showValidationDialogs] = new PropertyDescriptor("showValidationDialogs", JGDateTextField.class, "isShowValidationDialogs", "setShowValidationDialogs");
            properties[PROPERTY_showValidationDialogs].setPreferred(true);
            properties[PROPERTY_text] = new PropertyDescriptor("text", JGDateTextField.class, "getText", "setText");
            properties[PROPERTY_text].setExpert(true);
            properties[PROPERTY_toolkit] = new PropertyDescriptor("toolkit", JGDateTextField.class, "getToolkit", null);
            properties[PROPERTY_toolkit].setExpert(true);
            properties[PROPERTY_toolTipText] = new PropertyDescriptor("toolTipText", JGDateTextField.class, "getToolTipText", "setToolTipText");
            properties[PROPERTY_toolTipText].setExpert(true);
            properties[PROPERTY_topLevelAncestor] = new PropertyDescriptor("topLevelAncestor", JGDateTextField.class, "getTopLevelAncestor", null);
            properties[PROPERTY_topLevelAncestor].setExpert(true);
            properties[PROPERTY_transferHandler] = new PropertyDescriptor("transferHandler", JGDateTextField.class, "getTransferHandler", "setTransferHandler");
            properties[PROPERTY_transferHandler].setExpert(true);
            properties[PROPERTY_treeLock] = new PropertyDescriptor("treeLock", JGDateTextField.class, "getTreeLock", null);
            properties[PROPERTY_treeLock].setExpert(true);
            properties[PROPERTY_UI] = new PropertyDescriptor("UI", JGDateTextField.class, "getUI", "setUI");
            properties[PROPERTY_UI].setExpert(true);
            properties[PROPERTY_UIClassID] = new PropertyDescriptor("UIClassID", JGDateTextField.class, "getUIClassID", null);
            properties[PROPERTY_UIClassID].setExpert(true);
            properties[PROPERTY_valid] = new PropertyDescriptor("valid", JGDateTextField.class, "isValid", null);
            properties[PROPERTY_valid].setExpert(true);
            properties[PROPERTY_validateRoot] = new PropertyDescriptor("validateRoot", JGDateTextField.class, "isValidateRoot", null);
            properties[PROPERTY_validateRoot].setExpert(true);
            properties[PROPERTY_verifyInputWhenFocusTarget] = new PropertyDescriptor("verifyInputWhenFocusTarget", JGDateTextField.class, "getVerifyInputWhenFocusTarget", "setVerifyInputWhenFocusTarget");
            properties[PROPERTY_verifyInputWhenFocusTarget].setExpert(true);
            properties[PROPERTY_vetoableChangeListeners] = new PropertyDescriptor("vetoableChangeListeners", JGDateTextField.class, "getVetoableChangeListeners", null);
            properties[PROPERTY_vetoableChangeListeners].setExpert(true);
            properties[PROPERTY_visible] = new PropertyDescriptor("visible", JGDateTextField.class, "isVisible", "setVisible");
            properties[PROPERTY_visible].setExpert(true);
            properties[PROPERTY_visibleRect] = new PropertyDescriptor("visibleRect", JGDateTextField.class, "getVisibleRect", null);
            properties[PROPERTY_visibleRect].setExpert(true);
            properties[PROPERTY_width] = new PropertyDescriptor("width", JGDateTextField.class, "getWidth", null);
            properties[PROPERTY_width].setExpert(true);
            properties[PROPERTY_x] = new PropertyDescriptor("x", JGDateTextField.class, "getX", null);
            properties[PROPERTY_x].setExpert(true);
            properties[PROPERTY_y] = new PropertyDescriptor("y", JGDateTextField.class, "getY", null);
            properties[PROPERTY_y].setExpert(true);
        } catch (IntrospectionException e) {
        }
        return properties;
    }

    private static final int EVENT_actionListener = 0;

    private static final int EVENT_ancestorListener = 1;

    private static final int EVENT_caretListener = 2;

    private static final int EVENT_componentListener = 3;

    private static final int EVENT_containerListener = 4;

    private static final int EVENT_focusListener = 5;

    private static final int EVENT_hierarchyBoundsListener = 6;

    private static final int EVENT_hierarchyListener = 7;

    private static final int EVENT_inputMethodListener = 8;

    private static final int EVENT_keyListener = 9;

    private static final int EVENT_mouseListener = 10;

    private static final int EVENT_mouseMotionListener = 11;

    private static final int EVENT_mouseWheelListener = 12;

    private static final int EVENT_propertyChangeListener = 13;

    private static final int EVENT_vetoableChangeListener = 14;

    private static EventSetDescriptor[] getEdescriptor() {
        EventSetDescriptor[] eventSets = new EventSetDescriptor[15];
        try {
            eventSets[EVENT_actionListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "actionListener", java.awt.event.ActionListener.class, new String[] { "actionPerformed" }, "addActionListener", "removeActionListener");
            eventSets[EVENT_ancestorListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "ancestorListener", javax.swing.event.AncestorListener.class, new String[] { "ancestorAdded", "ancestorMoved", "ancestorRemoved" }, "addAncestorListener", "removeAncestorListener");
            eventSets[EVENT_caretListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "caretListener", javax.swing.event.CaretListener.class, new String[] { "caretUpdate" }, "addCaretListener", "removeCaretListener");
            eventSets[EVENT_componentListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "componentListener", java.awt.event.ComponentListener.class, new String[] { "componentHidden", "componentMoved", "componentResized", "componentShown" }, "addComponentListener", "removeComponentListener");
            eventSets[EVENT_containerListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "containerListener", java.awt.event.ContainerListener.class, new String[] { "componentAdded", "componentRemoved" }, "addContainerListener", "removeContainerListener");
            eventSets[EVENT_focusListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "focusListener", java.awt.event.FocusListener.class, new String[] { "focusGained", "focusLost" }, "addFocusListener", "removeFocusListener");
            eventSets[EVENT_hierarchyBoundsListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "hierarchyBoundsListener", java.awt.event.HierarchyBoundsListener.class, new String[] { "ancestorMoved", "ancestorResized" }, "addHierarchyBoundsListener", "removeHierarchyBoundsListener");
            eventSets[EVENT_hierarchyListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "hierarchyListener", java.awt.event.HierarchyListener.class, new String[] { "hierarchyChanged" }, "addHierarchyListener", "removeHierarchyListener");
            eventSets[EVENT_inputMethodListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "inputMethodListener", java.awt.event.InputMethodListener.class, new String[] { "caretPositionChanged", "inputMethodTextChanged" }, "addInputMethodListener", "removeInputMethodListener");
            eventSets[EVENT_keyListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "keyListener", java.awt.event.KeyListener.class, new String[] { "keyPressed", "keyReleased", "keyTyped" }, "addKeyListener", "removeKeyListener");
            eventSets[EVENT_mouseListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "mouseListener", java.awt.event.MouseListener.class, new String[] { "mouseClicked", "mouseEntered", "mouseExited", "mousePressed", "mouseReleased" }, "addMouseListener", "removeMouseListener");
            eventSets[EVENT_mouseMotionListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "mouseMotionListener", java.awt.event.MouseMotionListener.class, new String[] { "mouseDragged", "mouseMoved" }, "addMouseMotionListener", "removeMouseMotionListener");
            eventSets[EVENT_mouseWheelListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "mouseWheelListener", java.awt.event.MouseWheelListener.class, new String[] { "mouseWheelMoved" }, "addMouseWheelListener", "removeMouseWheelListener");
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] { "propertyChange" }, "addPropertyChangeListener", "removePropertyChangeListener");
            eventSets[EVENT_vetoableChangeListener] = new EventSetDescriptor(org.jgenesis.swing.JGDateTextField.class, "vetoableChangeListener", java.beans.VetoableChangeListener.class, new String[] { "vetoableChange" }, "addVetoableChangeListener", "removeVetoableChangeListener");
        } catch (IntrospectionException e) {
        }
        return eventSets;
    }

    private static final int METHOD_action0 = 0;

    private static final int METHOD_add1 = 1;

    private static final int METHOD_addKeymap2 = 2;

    private static final int METHOD_addNotify3 = 3;

    private static final int METHOD_addPropertyChangeListener4 = 4;

    private static final int METHOD_applyComponentOrientation5 = 5;

    private static final int METHOD_areFocusTraversalKeysSet6 = 6;

    private static final int METHOD_bounds7 = 7;

    private static final int METHOD_checkImage8 = 8;

    private static final int METHOD_computeVisibleRect9 = 9;

    private static final int METHOD_contains10 = 10;

    private static final int METHOD_copy11 = 11;

    private static final int METHOD_countComponents12 = 12;

    private static final int METHOD_createImage13 = 13;

    private static final int METHOD_createToolTip14 = 14;

    private static final int METHOD_createVolatileImage15 = 15;

    private static final int METHOD_cut16 = 16;

    private static final int METHOD_deliverEvent17 = 17;

    private static final int METHOD_disable18 = 18;

    private static final int METHOD_dispatchEvent19 = 19;

    private static final int METHOD_doLayout20 = 20;

    private static final int METHOD_enable21 = 21;

    private static final int METHOD_enableInputMethods22 = 22;

    private static final int METHOD_findComponentAt23 = 23;

    private static final int METHOD_firePropertyChange24 = 24;

    private static final int METHOD_getActionForKeyStroke25 = 25;

    private static final int METHOD_getBounds26 = 26;

    private static final int METHOD_getClientProperty27 = 27;

    private static final int METHOD_getComponentAt28 = 28;

    private static final int METHOD_getComponentZOrder29 = 29;

    private static final int METHOD_getConditionForKeyStroke30 = 30;

    private static final int METHOD_getDefaultLocale31 = 31;

    private static final int METHOD_getFontMetrics32 = 32;

    private static final int METHOD_getInputMap33 = 33;

    private static final int METHOD_getInsets34 = 34;

    private static final int METHOD_getKeymap35 = 35;

    private static final int METHOD_getListeners36 = 36;

    private static final int METHOD_getLocation37 = 37;

    private static final int METHOD_getMousePosition38 = 38;

    private static final int METHOD_getPopupLocation39 = 39;

    private static final int METHOD_getPropertyChangeListeners40 = 40;

    private static final int METHOD_getScrollableBlockIncrement41 = 41;

    private static final int METHOD_getScrollableUnitIncrement42 = 42;

    private static final int METHOD_getSize43 = 43;

    private static final int METHOD_getText44 = 44;

    private static final int METHOD_getToolTipLocation45 = 45;

    private static final int METHOD_getToolTipText46 = 46;

    private static final int METHOD_gotFocus47 = 47;

    private static final int METHOD_grabFocus48 = 48;

    private static final int METHOD_handleEvent49 = 49;

    private static final int METHOD_hasFocus50 = 50;

    private static final int METHOD_hide51 = 51;

    private static final int METHOD_imageUpdate52 = 52;

    private static final int METHOD_insets53 = 53;

    private static final int METHOD_inside54 = 54;

    private static final int METHOD_invalidate55 = 55;

    private static final int METHOD_isAncestorOf56 = 56;

    private static final int METHOD_isFocusCycleRoot57 = 57;

    private static final int METHOD_isLightweightComponent58 = 58;

    private static final int METHOD_keyDown59 = 59;

    private static final int METHOD_keyUp60 = 60;

    private static final int METHOD_layout61 = 61;

    private static final int METHOD_list62 = 62;

    private static final int METHOD_loadKeymap63 = 63;

    private static final int METHOD_locate64 = 64;

    private static final int METHOD_location65 = 65;

    private static final int METHOD_lostFocus66 = 66;

    private static final int METHOD_minimumSize67 = 67;

    private static final int METHOD_modelToView68 = 68;

    private static final int METHOD_mouseDown69 = 69;

    private static final int METHOD_mouseDrag70 = 70;

    private static final int METHOD_mouseEnter71 = 71;

    private static final int METHOD_mouseExit72 = 72;

    private static final int METHOD_mouseMove73 = 73;

    private static final int METHOD_mouseUp74 = 74;

    private static final int METHOD_move75 = 75;

    private static final int METHOD_moveCaretPosition76 = 76;

    private static final int METHOD_nextFocus77 = 77;

    private static final int METHOD_paint78 = 78;

    private static final int METHOD_paintAll79 = 79;

    private static final int METHOD_paintComponents80 = 80;

    private static final int METHOD_paintImmediately81 = 81;

    private static final int METHOD_paste82 = 82;

    private static final int METHOD_postActionEvent83 = 83;

    private static final int METHOD_postEvent84 = 84;

    private static final int METHOD_preferredSize85 = 85;

    private static final int METHOD_prepareImage86 = 86;

    private static final int METHOD_print87 = 87;

    private static final int METHOD_printAll88 = 88;

    private static final int METHOD_printComponents89 = 89;

    private static final int METHOD_putClientProperty90 = 90;

    private static final int METHOD_read91 = 91;

    private static final int METHOD_registerKeyboardAction92 = 92;

    private static final int METHOD_remove93 = 93;

    private static final int METHOD_removeAll94 = 94;

    private static final int METHOD_removeKeymap95 = 95;

    private static final int METHOD_removeNotify96 = 96;

    private static final int METHOD_removePropertyChangeListener97 = 97;

    private static final int METHOD_repaint98 = 98;

    private static final int METHOD_replaceSelection99 = 99;

    private static final int METHOD_requestDefaultFocus100 = 100;

    private static final int METHOD_requestFocus101 = 101;

    private static final int METHOD_requestFocusInWindow102 = 102;

    private static final int METHOD_resetKeyboardActions103 = 103;

    private static final int METHOD_reshape104 = 104;

    private static final int METHOD_resize105 = 105;

    private static final int METHOD_revalidate106 = 106;

    private static final int METHOD_scrollRectToVisible107 = 107;

    private static final int METHOD_select108 = 108;

    private static final int METHOD_selectAll109 = 109;

    private static final int METHOD_setBounds110 = 110;

    private static final int METHOD_setComponentZOrder111 = 111;

    private static final int METHOD_setDefaultLocale112 = 112;

    private static final int METHOD_setInputMap113 = 113;

    private static final int METHOD_setLocation114 = 114;

    private static final int METHOD_setSize115 = 115;

    private static final int METHOD_show116 = 116;

    private static final int METHOD_size117 = 117;

    private static final int METHOD_toString118 = 118;

    private static final int METHOD_transferFocus119 = 119;

    private static final int METHOD_transferFocusBackward120 = 120;

    private static final int METHOD_transferFocusDownCycle121 = 121;

    private static final int METHOD_transferFocusUpCycle122 = 122;

    private static final int METHOD_unregisterKeyboardAction123 = 123;

    private static final int METHOD_update124 = 124;

    private static final int METHOD_updateUI125 = 125;

    private static final int METHOD_validate126 = 126;

    private static final int METHOD_viewToModel127 = 127;

    private static final int METHOD_write128 = 128;

    private static MethodDescriptor[] getMdescriptor() {
        MethodDescriptor[] methods = new MethodDescriptor[129];
        try {
            methods[METHOD_action0] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("action", new Class[] { java.awt.Event.class, java.lang.Object.class }));
            methods[METHOD_action0].setDisplayName("");
            methods[METHOD_add1] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("add", new Class[] { java.awt.Component.class }));
            methods[METHOD_add1].setDisplayName("");
            methods[METHOD_addKeymap2] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("addKeymap", new Class[] { java.lang.String.class, javax.swing.text.Keymap.class }));
            methods[METHOD_addKeymap2].setDisplayName("");
            methods[METHOD_addNotify3] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("addNotify", new Class[] {}));
            methods[METHOD_addNotify3].setDisplayName("");
            methods[METHOD_addPropertyChangeListener4] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("addPropertyChangeListener", new Class[] { java.lang.String.class, java.beans.PropertyChangeListener.class }));
            methods[METHOD_addPropertyChangeListener4].setDisplayName("");
            methods[METHOD_applyComponentOrientation5] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("applyComponentOrientation", new Class[] { java.awt.ComponentOrientation.class }));
            methods[METHOD_applyComponentOrientation5].setDisplayName("");
            methods[METHOD_areFocusTraversalKeysSet6] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("areFocusTraversalKeysSet", new Class[] { Integer.TYPE }));
            methods[METHOD_areFocusTraversalKeysSet6].setDisplayName("");
            methods[METHOD_bounds7] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("bounds", new Class[] {}));
            methods[METHOD_bounds7].setDisplayName("");
            methods[METHOD_checkImage8] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("checkImage", new Class[] { java.awt.Image.class, Integer.TYPE, Integer.TYPE, java.awt.image.ImageObserver.class }));
            methods[METHOD_checkImage8].setDisplayName("");
            methods[METHOD_computeVisibleRect9] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("computeVisibleRect", new Class[] { java.awt.Rectangle.class }));
            methods[METHOD_computeVisibleRect9].setDisplayName("");
            methods[METHOD_contains10] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("contains", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_contains10].setDisplayName("");
            methods[METHOD_copy11] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("copy", new Class[] {}));
            methods[METHOD_copy11].setDisplayName("");
            methods[METHOD_countComponents12] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("countComponents", new Class[] {}));
            methods[METHOD_countComponents12].setDisplayName("");
            methods[METHOD_createImage13] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("createImage", new Class[] { java.awt.image.ImageProducer.class }));
            methods[METHOD_createImage13].setDisplayName("");
            methods[METHOD_createToolTip14] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("createToolTip", new Class[] {}));
            methods[METHOD_createToolTip14].setDisplayName("");
            methods[METHOD_createVolatileImage15] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("createVolatileImage", new Class[] { Integer.TYPE, Integer.TYPE, java.awt.ImageCapabilities.class }));
            methods[METHOD_createVolatileImage15].setDisplayName("");
            methods[METHOD_cut16] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("cut", new Class[] {}));
            methods[METHOD_cut16].setDisplayName("");
            methods[METHOD_deliverEvent17] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("deliverEvent", new Class[] { java.awt.Event.class }));
            methods[METHOD_deliverEvent17].setDisplayName("");
            methods[METHOD_disable18] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("disable", new Class[] {}));
            methods[METHOD_disable18].setDisplayName("");
            methods[METHOD_dispatchEvent19] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("dispatchEvent", new Class[] { java.awt.AWTEvent.class }));
            methods[METHOD_dispatchEvent19].setDisplayName("");
            methods[METHOD_doLayout20] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("doLayout", new Class[] {}));
            methods[METHOD_doLayout20].setDisplayName("");
            methods[METHOD_enable21] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("enable", new Class[] {}));
            methods[METHOD_enable21].setDisplayName("");
            methods[METHOD_enableInputMethods22] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("enableInputMethods", new Class[] { Boolean.TYPE }));
            methods[METHOD_enableInputMethods22].setDisplayName("");
            methods[METHOD_findComponentAt23] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("findComponentAt", new Class[] { java.awt.Point.class }));
            methods[METHOD_findComponentAt23].setDisplayName("");
            methods[METHOD_firePropertyChange24] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("firePropertyChange", new Class[] { java.lang.String.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_firePropertyChange24].setDisplayName("");
            methods[METHOD_getActionForKeyStroke25] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getActionForKeyStroke", new Class[] { javax.swing.KeyStroke.class }));
            methods[METHOD_getActionForKeyStroke25].setDisplayName("");
            methods[METHOD_getBounds26] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getBounds", new Class[] { java.awt.Rectangle.class }));
            methods[METHOD_getBounds26].setDisplayName("");
            methods[METHOD_getClientProperty27] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getClientProperty", new Class[] { java.lang.Object.class }));
            methods[METHOD_getClientProperty27].setDisplayName("");
            methods[METHOD_getComponentAt28] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getComponentAt", new Class[] { java.awt.Point.class }));
            methods[METHOD_getComponentAt28].setDisplayName("");
            methods[METHOD_getComponentZOrder29] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getComponentZOrder", new Class[] { java.awt.Component.class }));
            methods[METHOD_getComponentZOrder29].setDisplayName("");
            methods[METHOD_getConditionForKeyStroke30] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getConditionForKeyStroke", new Class[] { javax.swing.KeyStroke.class }));
            methods[METHOD_getConditionForKeyStroke30].setDisplayName("");
            methods[METHOD_getDefaultLocale31] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getDefaultLocale", new Class[] {}));
            methods[METHOD_getDefaultLocale31].setDisplayName("");
            methods[METHOD_getFontMetrics32] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getFontMetrics", new Class[] { java.awt.Font.class }));
            methods[METHOD_getFontMetrics32].setDisplayName("");
            methods[METHOD_getInputMap33] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getInputMap", new Class[] {}));
            methods[METHOD_getInputMap33].setDisplayName("");
            methods[METHOD_getInsets34] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getInsets", new Class[] { java.awt.Insets.class }));
            methods[METHOD_getInsets34].setDisplayName("");
            methods[METHOD_getKeymap35] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getKeymap", new Class[] { java.lang.String.class }));
            methods[METHOD_getKeymap35].setDisplayName("");
            methods[METHOD_getListeners36] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getListeners", new Class[] { java.lang.Class.class }));
            methods[METHOD_getListeners36].setDisplayName("");
            methods[METHOD_getLocation37] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getLocation", new Class[] { java.awt.Point.class }));
            methods[METHOD_getLocation37].setDisplayName("");
            methods[METHOD_getMousePosition38] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getMousePosition", new Class[] { Boolean.TYPE }));
            methods[METHOD_getMousePosition38].setDisplayName("");
            methods[METHOD_getPopupLocation39] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getPopupLocation", new Class[] { java.awt.event.MouseEvent.class }));
            methods[METHOD_getPopupLocation39].setDisplayName("");
            methods[METHOD_getPropertyChangeListeners40] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getPropertyChangeListeners", new Class[] { java.lang.String.class }));
            methods[METHOD_getPropertyChangeListeners40].setDisplayName("");
            methods[METHOD_getScrollableBlockIncrement41] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getScrollableBlockIncrement", new Class[] { java.awt.Rectangle.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_getScrollableBlockIncrement41].setDisplayName("");
            methods[METHOD_getScrollableUnitIncrement42] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getScrollableUnitIncrement", new Class[] { java.awt.Rectangle.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_getScrollableUnitIncrement42].setDisplayName("");
            methods[METHOD_getSize43] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getSize", new Class[] { java.awt.Dimension.class }));
            methods[METHOD_getSize43].setDisplayName("");
            methods[METHOD_getText44] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getText", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_getText44].setDisplayName("");
            methods[METHOD_getToolTipLocation45] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getToolTipLocation", new Class[] { java.awt.event.MouseEvent.class }));
            methods[METHOD_getToolTipLocation45].setDisplayName("");
            methods[METHOD_getToolTipText46] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("getToolTipText", new Class[] { java.awt.event.MouseEvent.class }));
            methods[METHOD_getToolTipText46].setDisplayName("");
            methods[METHOD_gotFocus47] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("gotFocus", new Class[] { java.awt.Event.class, java.lang.Object.class }));
            methods[METHOD_gotFocus47].setDisplayName("");
            methods[METHOD_grabFocus48] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("grabFocus", new Class[] {}));
            methods[METHOD_grabFocus48].setDisplayName("");
            methods[METHOD_handleEvent49] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("handleEvent", new Class[] { java.awt.Event.class }));
            methods[METHOD_handleEvent49].setDisplayName("");
            methods[METHOD_hasFocus50] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("hasFocus", new Class[] {}));
            methods[METHOD_hasFocus50].setDisplayName("");
            methods[METHOD_hide51] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("hide", new Class[] {}));
            methods[METHOD_hide51].setDisplayName("");
            methods[METHOD_imageUpdate52] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("imageUpdate", new Class[] { java.awt.Image.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_imageUpdate52].setDisplayName("");
            methods[METHOD_insets53] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("insets", new Class[] {}));
            methods[METHOD_insets53].setDisplayName("");
            methods[METHOD_inside54] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("inside", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_inside54].setDisplayName("");
            methods[METHOD_invalidate55] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("invalidate", new Class[] {}));
            methods[METHOD_invalidate55].setDisplayName("");
            methods[METHOD_isAncestorOf56] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("isAncestorOf", new Class[] { java.awt.Component.class }));
            methods[METHOD_isAncestorOf56].setDisplayName("");
            methods[METHOD_isFocusCycleRoot57] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("isFocusCycleRoot", new Class[] { java.awt.Container.class }));
            methods[METHOD_isFocusCycleRoot57].setDisplayName("");
            methods[METHOD_isLightweightComponent58] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("isLightweightComponent", new Class[] { java.awt.Component.class }));
            methods[METHOD_isLightweightComponent58].setDisplayName("");
            methods[METHOD_keyDown59] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("keyDown", new Class[] { java.awt.Event.class, Integer.TYPE }));
            methods[METHOD_keyDown59].setDisplayName("");
            methods[METHOD_keyUp60] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("keyUp", new Class[] { java.awt.Event.class, Integer.TYPE }));
            methods[METHOD_keyUp60].setDisplayName("");
            methods[METHOD_layout61] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("layout", new Class[] {}));
            methods[METHOD_layout61].setDisplayName("");
            methods[METHOD_list62] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("list", new Class[] { java.io.PrintWriter.class, Integer.TYPE }));
            methods[METHOD_list62].setDisplayName("");
            methods[METHOD_loadKeymap63] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("loadKeymap", new Class[] { javax.swing.text.Keymap.class, Class.forName("[Ljavax.swing.text.JTextComponent$KeyBinding;"), Class.forName("[Ljavax.swing.Action;") }));
            methods[METHOD_loadKeymap63].setDisplayName("");
            methods[METHOD_locate64] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("locate", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_locate64].setDisplayName("");
            methods[METHOD_location65] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("location", new Class[] {}));
            methods[METHOD_location65].setDisplayName("");
            methods[METHOD_lostFocus66] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("lostFocus", new Class[] { java.awt.Event.class, java.lang.Object.class }));
            methods[METHOD_lostFocus66].setDisplayName("");
            methods[METHOD_minimumSize67] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("minimumSize", new Class[] {}));
            methods[METHOD_minimumSize67].setDisplayName("");
            methods[METHOD_modelToView68] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("modelToView", new Class[] { Integer.TYPE }));
            methods[METHOD_modelToView68].setDisplayName("");
            methods[METHOD_mouseDown69] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("mouseDown", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseDown69].setDisplayName("");
            methods[METHOD_mouseDrag70] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("mouseDrag", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseDrag70].setDisplayName("");
            methods[METHOD_mouseEnter71] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("mouseEnter", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseEnter71].setDisplayName("");
            methods[METHOD_mouseExit72] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("mouseExit", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseExit72].setDisplayName("");
            methods[METHOD_mouseMove73] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("mouseMove", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseMove73].setDisplayName("");
            methods[METHOD_mouseUp74] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("mouseUp", new Class[] { java.awt.Event.class, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_mouseUp74].setDisplayName("");
            methods[METHOD_move75] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("move", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_move75].setDisplayName("");
            methods[METHOD_moveCaretPosition76] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("moveCaretPosition", new Class[] { Integer.TYPE }));
            methods[METHOD_moveCaretPosition76].setDisplayName("");
            methods[METHOD_nextFocus77] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("nextFocus", new Class[] {}));
            methods[METHOD_nextFocus77].setDisplayName("");
            methods[METHOD_paint78] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("paint", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_paint78].setDisplayName("");
            methods[METHOD_paintAll79] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("paintAll", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_paintAll79].setDisplayName("");
            methods[METHOD_paintComponents80] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("paintComponents", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_paintComponents80].setDisplayName("");
            methods[METHOD_paintImmediately81] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("paintImmediately", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_paintImmediately81].setDisplayName("");
            methods[METHOD_paste82] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("paste", new Class[] {}));
            methods[METHOD_paste82].setDisplayName("");
            methods[METHOD_postActionEvent83] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("postActionEvent", new Class[] {}));
            methods[METHOD_postActionEvent83].setDisplayName("");
            methods[METHOD_postEvent84] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("postEvent", new Class[] { java.awt.Event.class }));
            methods[METHOD_postEvent84].setDisplayName("");
            methods[METHOD_preferredSize85] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("preferredSize", new Class[] {}));
            methods[METHOD_preferredSize85].setDisplayName("");
            methods[METHOD_prepareImage86] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("prepareImage", new Class[] { java.awt.Image.class, Integer.TYPE, Integer.TYPE, java.awt.image.ImageObserver.class }));
            methods[METHOD_prepareImage86].setDisplayName("");
            methods[METHOD_print87] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("print", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_print87].setDisplayName("");
            methods[METHOD_printAll88] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("printAll", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_printAll88].setDisplayName("");
            methods[METHOD_printComponents89] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("printComponents", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_printComponents89].setDisplayName("");
            methods[METHOD_putClientProperty90] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("putClientProperty", new Class[] { java.lang.Object.class, java.lang.Object.class }));
            methods[METHOD_putClientProperty90].setDisplayName("");
            methods[METHOD_read91] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("read", new Class[] { java.io.Reader.class, java.lang.Object.class }));
            methods[METHOD_read91].setDisplayName("");
            methods[METHOD_registerKeyboardAction92] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("registerKeyboardAction", new Class[] { java.awt.event.ActionListener.class, javax.swing.KeyStroke.class, Integer.TYPE }));
            methods[METHOD_registerKeyboardAction92].setDisplayName("");
            methods[METHOD_remove93] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("remove", new Class[] { java.awt.Component.class }));
            methods[METHOD_remove93].setDisplayName("");
            methods[METHOD_removeAll94] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("removeAll", new Class[] {}));
            methods[METHOD_removeAll94].setDisplayName("");
            methods[METHOD_removeKeymap95] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("removeKeymap", new Class[] { java.lang.String.class }));
            methods[METHOD_removeKeymap95].setDisplayName("");
            methods[METHOD_removeNotify96] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("removeNotify", new Class[] {}));
            methods[METHOD_removeNotify96].setDisplayName("");
            methods[METHOD_removePropertyChangeListener97] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("removePropertyChangeListener", new Class[] { java.lang.String.class, java.beans.PropertyChangeListener.class }));
            methods[METHOD_removePropertyChangeListener97].setDisplayName("");
            methods[METHOD_repaint98] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("repaint", new Class[] { java.awt.Rectangle.class }));
            methods[METHOD_repaint98].setDisplayName("");
            methods[METHOD_replaceSelection99] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("replaceSelection", new Class[] { java.lang.String.class }));
            methods[METHOD_replaceSelection99].setDisplayName("");
            methods[METHOD_requestDefaultFocus100] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("requestDefaultFocus", new Class[] {}));
            methods[METHOD_requestDefaultFocus100].setDisplayName("");
            methods[METHOD_requestFocus101] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("requestFocus", new Class[] {}));
            methods[METHOD_requestFocus101].setDisplayName("");
            methods[METHOD_requestFocusInWindow102] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("requestFocusInWindow", new Class[] {}));
            methods[METHOD_requestFocusInWindow102].setDisplayName("");
            methods[METHOD_resetKeyboardActions103] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("resetKeyboardActions", new Class[] {}));
            methods[METHOD_resetKeyboardActions103].setDisplayName("");
            methods[METHOD_reshape104] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("reshape", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_reshape104].setDisplayName("");
            methods[METHOD_resize105] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("resize", new Class[] { java.awt.Dimension.class }));
            methods[METHOD_resize105].setDisplayName("");
            methods[METHOD_revalidate106] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("revalidate", new Class[] {}));
            methods[METHOD_revalidate106].setDisplayName("");
            methods[METHOD_scrollRectToVisible107] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("scrollRectToVisible", new Class[] { java.awt.Rectangle.class }));
            methods[METHOD_scrollRectToVisible107].setDisplayName("");
            methods[METHOD_select108] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("select", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_select108].setDisplayName("");
            methods[METHOD_selectAll109] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("selectAll", new Class[] {}));
            methods[METHOD_selectAll109].setDisplayName("");
            methods[METHOD_setBounds110] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("setBounds", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE }));
            methods[METHOD_setBounds110].setDisplayName("");
            methods[METHOD_setComponentZOrder111] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("setComponentZOrder", new Class[] { java.awt.Component.class, Integer.TYPE }));
            methods[METHOD_setComponentZOrder111].setDisplayName("");
            methods[METHOD_setDefaultLocale112] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("setDefaultLocale", new Class[] { java.util.Locale.class }));
            methods[METHOD_setDefaultLocale112].setDisplayName("");
            methods[METHOD_setInputMap113] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("setInputMap", new Class[] { Integer.TYPE, javax.swing.InputMap.class }));
            methods[METHOD_setInputMap113].setDisplayName("");
            methods[METHOD_setLocation114] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("setLocation", new Class[] { Integer.TYPE, Integer.TYPE }));
            methods[METHOD_setLocation114].setDisplayName("");
            methods[METHOD_setSize115] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("setSize", new Class[] { java.awt.Dimension.class }));
            methods[METHOD_setSize115].setDisplayName("");
            methods[METHOD_show116] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("show", new Class[] { Boolean.TYPE }));
            methods[METHOD_show116].setDisplayName("");
            methods[METHOD_size117] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("size", new Class[] {}));
            methods[METHOD_size117].setDisplayName("");
            methods[METHOD_toString118] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("toString", new Class[] {}));
            methods[METHOD_toString118].setDisplayName("");
            methods[METHOD_transferFocus119] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("transferFocus", new Class[] {}));
            methods[METHOD_transferFocus119].setDisplayName("");
            methods[METHOD_transferFocusBackward120] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("transferFocusBackward", new Class[] {}));
            methods[METHOD_transferFocusBackward120].setDisplayName("");
            methods[METHOD_transferFocusDownCycle121] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("transferFocusDownCycle", new Class[] {}));
            methods[METHOD_transferFocusDownCycle121].setDisplayName("");
            methods[METHOD_transferFocusUpCycle122] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("transferFocusUpCycle", new Class[] {}));
            methods[METHOD_transferFocusUpCycle122].setDisplayName("");
            methods[METHOD_unregisterKeyboardAction123] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("unregisterKeyboardAction", new Class[] { javax.swing.KeyStroke.class }));
            methods[METHOD_unregisterKeyboardAction123].setDisplayName("");
            methods[METHOD_update124] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("update", new Class[] { java.awt.Graphics.class }));
            methods[METHOD_update124].setDisplayName("");
            methods[METHOD_updateUI125] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("updateUI", new Class[] {}));
            methods[METHOD_updateUI125].setDisplayName("");
            methods[METHOD_validate126] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("validate", new Class[] {}));
            methods[METHOD_validate126].setDisplayName("");
            methods[METHOD_viewToModel127] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("viewToModel", new Class[] { java.awt.Point.class }));
            methods[METHOD_viewToModel127].setDisplayName("");
            methods[METHOD_write128] = new MethodDescriptor(org.jgenesis.swing.JGDateTextField.class.getMethod("write", new Class[] { java.io.Writer.class }));
            methods[METHOD_write128].setDisplayName("");
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
