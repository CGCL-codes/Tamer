package com.softaspects.jsf.support.base;

import com.softaspects.jsf.component.table.Definitions;
import com.softaspects.jsf.component.base.ComponentDefinitions;
import com.softaspects.jsf.component.base.BaseComponentConsts;
import com.softaspects.jsf.component.listmodel.ListSelectionModel;
import com.softaspects.jsf.support.util.ReverseHashMap;
import com.softaspects.jsf.support.util.StringUtils;
import com.softaspects.jsf.support.SupportDefinitions;
import com.softaspects.jsf.support.beans.BeanUtils;
import com.softaspects.jsf.support.components.container.LayoutConsts;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.util.ArrayList;
import java.util.Iterator;

public class SupportUtils implements SupportDefinitions {

    private static ReverseHashMap fAlignsMap = new ReverseHashMap();

    private static ReverseHashMap fIframeAlignsMap = new ReverseHashMap();

    private static ReverseHashMap fValignsMap = new ReverseHashMap();

    private static ReverseHashMap fSelectionsMap = new ReverseHashMap();

    private static ReverseHashMap fKindOfImagesMap = new ReverseHashMap();

    private static ReverseHashMap fOrientationsMap = new ReverseHashMap();

    private static ReverseHashMap fOrientationConstantsMap = new ReverseHashMap();

    private static ReverseHashMap fScopesMap = new ReverseHashMap();

    private static ReverseHashMap fBorderConstraintsMap = new ReverseHashMap();

    private static ReverseHashMap fBorderLayoutTypeMap = new ReverseHashMap();

    private static ReverseHashMap fLayoutDirectionsMap = new ReverseHashMap();

    private static ReverseHashMap fBooleansMap = new ReverseHashMap();

    private static ReverseHashMap fScrollingMap = new ReverseHashMap();

    private static ReverseHashMap fOverFlowMap = new ReverseHashMap();

    private static ReverseHashMap fTableRulesMap = new ReverseHashMap();

    private static ReverseHashMap fFormMethodsMap = new ReverseHashMap();

    private static ReverseHashMap fTargetWindowMap = new ReverseHashMap();

    private static void initTarget() {
    }

    private static void initAligns() {
        fAlignsMap.put(new Integer(ComponentDefinitions.ALIGN_LEFT), BaseComponentConsts.LEFT_VALUE);
        fAlignsMap.put(new Integer(ComponentDefinitions.ALIGN_RIGHT), BaseComponentConsts.RIGHT_VALUE);
        fAlignsMap.put(new Integer(ComponentDefinitions.ALIGN_CENTER), ALIGN_CENTER_STR);
    }

    private static void initValigns() {
        fValignsMap.put(new Integer(ComponentDefinitions.VALIGN_MIDDLE), BaseComponentConsts.MIDDLE_VALUE);
        fValignsMap.put(new Integer(ComponentDefinitions.VALIGN_TOP), BaseComponentConsts.TOP_VALUE);
        fValignsMap.put(new Integer(ComponentDefinitions.VALIGN_BOTTOM), BaseComponentConsts.BOTTOM_VALUE);
        fValignsMap.put(new Integer(ComponentDefinitions.VALIGN_BASELINE), VALIGN_BASELINE_STR);
    }

    private static void initIframeAligns() {
        fIframeAlignsMap.put(new Integer(ComponentDefinitions.IFRAME_ALIGN_LEFT), BaseComponentConsts.LEFT_VALUE);
        fIframeAlignsMap.put(new Integer(ComponentDefinitions.IFRAME_ALIGN_RIGHT), BaseComponentConsts.RIGHT_VALUE);
        fIframeAlignsMap.put(new Integer(ComponentDefinitions.IFRAME_ALIGN_TOP), BaseComponentConsts.TOP_VALUE);
        fIframeAlignsMap.put(new Integer(ComponentDefinitions.IFRAME_ALIGN_BASELINE), IFRAME_ALIGN_BASELINE_STR);
        fIframeAlignsMap.put(new Integer(ComponentDefinitions.IFRAME_ALIGN_BOTTOM), BaseComponentConsts.BOTTOM_VALUE);
        fIframeAlignsMap.put(new Integer(ComponentDefinitions.IFRAME_ALIGN_MIDDLE), BaseComponentConsts.MIDDLE_VALUE);
    }

    private static void initOrientations() {
        fOrientationsMap.put(Boolean.TRUE, ORIENTATION_X_NAME);
        fOrientationsMap.put(Boolean.FALSE, ORIENTATION_Y_NAME);
    }

    private static void initOrientationConstants() {
        fOrientationConstantsMap.put(new Short((short) 1), ORIENTATION_X_NAME);
        fOrientationConstantsMap.put(new Short((short) 2), ORIENTATION_Y_NAME);
    }

    private static void initScopes() {
        fScopesMap.put(new Integer(PageContext.REQUEST_SCOPE), SCOPE_REQUEST);
        fScopesMap.put(new Integer(PageContext.SESSION_SCOPE), SCOPE_SESSION);
        fScopesMap.put(new Integer(PageContext.APPLICATION_SCOPE), SCOPE_APPLICTION);
        fScopesMap.put(new Integer(PageContext.PAGE_SCOPE), SCOPE_PAGE);
    }

    private static void initSelections() {
        fSelectionsMap.put(new Integer(ListSelectionModel.SELECTION_SINGLE), SELECTION_SINGLE_STR);
        fSelectionsMap.put(new Integer(ListSelectionModel.SELECTION_SINGLE_INTERVAL), SELECTION_SINGLE_INTERVAL_STR);
        fSelectionsMap.put(new Integer(ListSelectionModel.SELECTION_MULTIPLE_INTERVAL), SELECTION_MULTIPLE_INTERVAL_STR);
    }

    private static void initBorderConstraints() {
        fBorderConstraintsMap.put(new Integer(LayoutConsts.BORDER_WEST_VALUE), BORDER_WEST_NAME);
        fBorderConstraintsMap.put(new Integer(LayoutConsts.BORDER_EAST_VALUE), BORDER_EAST_NAME);
        fBorderConstraintsMap.put(new Integer(LayoutConsts.BORDER_SOUTH_VALUE), BORDER_SOUTH_NAME);
        fBorderConstraintsMap.put(new Integer(LayoutConsts.BORDER_NORTH_VALUE), BORDER_NORTH_NAME);
        fBorderConstraintsMap.put(new Integer(LayoutConsts.BORDER_CENTER_VALUE), BORDER_CENTER_NAME);
    }

    private static void initKindsOfImages() {
        fKindOfImagesMap.put(Boolean.FALSE, IMAGES_BIG_NAME);
        fKindOfImagesMap.put(Boolean.TRUE, IMAGES_SMALL_NAME);
    }

    private static void initBorderLayoutTypes() {
        fBorderLayoutTypeMap.put(new Integer(LayoutConsts.BORDER_LAYOUT_EAST_WEST), BORDER_LAYOUT_TYPE_EAST_WEST_NAME);
        fBorderLayoutTypeMap.put(new Integer(LayoutConsts.BORDER_LAYOUT_NORTH_SOUTH), BORDER_LAYOUT_TYPE_NORTH_SOUTH_NAME);
    }

    private static void initLayoutDirections() {
        fLayoutDirectionsMap.put(new Integer(LayoutConsts.LAYOUT_DIRECTION_HORIZONTAL), LAYOUT_DIRECTION_HORIZONTAL_NAME);
        fLayoutDirectionsMap.put(new Integer(LayoutConsts.LAYOUT_DIRECTION_VERTICAL), LAYOUT_DIRECTION_VERTICAL_NAME);
    }

    private static void initBooleans() {
        fBooleansMap.put(new Integer(ComponentDefinitions.BOOLEAN_FALSE), BOOLEAN_FALSE);
        fBooleansMap.put(new Integer(ComponentDefinitions.BOOLEAN_TRUE), BOOLEAN_TRUE);
    }

    private static void initScrollings() {
        fScrollingMap.put(new Integer(ComponentDefinitions.SCROLLING_AUTO), SCROLLING_AUTO_NAME);
        fScrollingMap.put(new Integer(ComponentDefinitions.SCROLLING_NO), SCROLLING_NO_NAME);
        fScrollingMap.put(new Integer(ComponentDefinitions.SCROLLING_YES), SCROLLING_YES_NAME);
    }

    private static void initOverflows() {
        fOverFlowMap.put(new Integer(ComponentDefinitions.OVERFLOW_AUTO), OVERFLOW_AUTO_NAME);
        fOverFlowMap.put(new Integer(ComponentDefinitions.OVERFLOW_SCROLL), OVERFLOW_SCROLL_NAME);
        fOverFlowMap.put(new Integer(ComponentDefinitions.OVERFLOW_VISIBLE), OVERFLOW_VISIBLE_NAME);
        fOverFlowMap.put(new Integer(ComponentDefinitions.OVERFLOW_HIDDEN), OVERFLOW_HIDDEN_NAME);
    }

    private static void initRulesMap() {
        fTableRulesMap.put(new Integer(Definitions.RULES_ALL_VALUE), RULES_ALL_NAME);
        fTableRulesMap.put(new Integer(Definitions.RULES_COLS_VALUE), RULES_COLS_NAME);
        fTableRulesMap.put(new Integer(Definitions.RULES_GROUPS_VALUE), RULES_GROUPS_NAME);
        fTableRulesMap.put(new Integer(Definitions.RULES_NONE_VALUE), RULES_NONE_NAME);
        fTableRulesMap.put(new Integer(Definitions.RULES_ROWS_VALUE), RULES_ROWS_NAME);
    }

    private static void initFormMethodsMap() {
        fFormMethodsMap.put(new Integer(ComponentDefinitions.FORM_METHOD_GET_VALUE), FORM_METHOD_GET_NAME);
        fFormMethodsMap.put(new Integer(ComponentDefinitions.FORM_METHOD_POST_VALUE), FORM_METHOD_POST_NAME);
    }

    static {
        initTarget();
        initAligns();
        initValigns();
        initIframeAligns();
        initScopes();
        initSelections();
        initOrientations();
        initOrientationConstants();
        initKindsOfImages();
        initBorderConstraints();
        initBorderLayoutTypes();
        initLayoutDirections();
        initBooleans();
        initScrollings();
        initOverflows();
        initRulesMap();
        initFormMethodsMap();
    }

    public static ReverseHashMap getAlignsMap() {
        return fAlignsMap;
    }

    public static ReverseHashMap getIframeAlignsMap() {
        return fIframeAlignsMap;
    }

    public static ReverseHashMap getValignsMap() {
        return fValignsMap;
    }

    public static ReverseHashMap getSelectionsMap() {
        return fSelectionsMap;
    }

    public static ReverseHashMap getKindOfImagesMap() {
        return fKindOfImagesMap;
    }

    public static ReverseHashMap getOrientationsMap() {
        return fOrientationsMap;
    }

    public static ReverseHashMap getOrientationConstantsMap() {
        return fOrientationConstantsMap;
    }

    public static ReverseHashMap getScopesMap() {
        return fScopesMap;
    }

    public static ReverseHashMap getBorderConstraintsMap() {
        return fBorderConstraintsMap;
    }

    public static ReverseHashMap getBorderLayoutTypeMap() {
        return fBorderLayoutTypeMap;
    }

    public static ReverseHashMap getLayoutDirectionsMap() {
        return fLayoutDirectionsMap;
    }

    public static ReverseHashMap getBooleansMap() {
        return fBooleansMap;
    }

    public static ReverseHashMap getScrollingMap() {
        return fScrollingMap;
    }

    public static ReverseHashMap getOverFlowMap() {
        return fOverFlowMap;
    }

    public static ReverseHashMap getTableRulesMap() {
        return fTableRulesMap;
    }

    public static ReverseHashMap getFormMethodsMap() {
        return fFormMethodsMap;
    }

    public static ReverseHashMap getTargetWindowMap() {
        return fTargetWindowMap;
    }

    private static boolean simpleLoginCheck(PageContext context) {
        ReverseHashMap map = (ReverseHashMap) context.getAttribute("MAP", PageContext.APPLICATION_SCOPE);
        return map != null;
    }

    private static boolean checkSessionId(ReverseHashMap map, String id) {
        Object object = map.getValue(id);
        return object != null;
    }

    public static boolean checkLogin(PageContext context, HttpServletRequest request) {
        if (!simpleLoginCheck(context)) return false;
        ReverseHashMap map = (ReverseHashMap) context.getAttribute("MAP", PageContext.APPLICATION_SCOPE);
        String id = request.getSession(true).getId();
        return checkSessionId(map, id);
    }

    public static boolean checkLogin(String id, PageContext context, String destination, boolean encodeUrl) throws java.io.IOException {
        boolean ind1 = simpleLoginCheck(context);
        if (!ind1) {
            redirect(context, destination, encodeUrl);
            return false;
        }
        ReverseHashMap map = (ReverseHashMap) context.getAttribute("MAP", PageContext.APPLICATION_SCOPE);
        boolean ind2 = checkSessionId(map, id);
        if (!ind2) {
            redirect(context, destination, encodeUrl);
            return false;
        }
        return true;
    }

    public static void redirect(PageContext context, String destination, boolean encodeUrl) throws java.io.IOException {
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        String dest;
        if (encodeUrl) {
            dest = response.encodeURL(destination);
        } else {
            dest = destination;
        }
        response.sendRedirect(dest);
    }

    public static boolean checkLogin(String id, PageContext context) {
        boolean ind1 = simpleLoginCheck(context);
        System.out.println("Simple login check : " + ind1);
        if (!ind1) return false;
        ReverseHashMap map = (ReverseHashMap) context.getAttribute("MAP", PageContext.APPLICATION_SCOPE);
        boolean ind2 = checkSessionId(map, id);
        System.out.println("Check session id : " + ind2);
        return ind2;
    }

    public static int getTargetCode(String aTarget) {
        validateOnNull(aTarget, "Target");
        Object result = fTargetWindowMap.getKey(aTarget.trim().toLowerCase());
        if (result == null) {
            sendMessageNotSupported(aTarget, "Target");
        }
        return ((Integer) result).intValue();
    }

    public static int getAlignCode(String aAlign) {
        validateOnNull(aAlign, BaseComponentConsts.ALIGN_PROPERTY);
        Object result = fAlignsMap.getKey(aAlign.trim().toLowerCase());
        if (result == null) {
            try {
                return Integer.parseInt(aAlign);
            } catch (NumberFormatException e) {
            }
            sendMessageNotSupported(aAlign, BaseComponentConsts.ALIGN_PROPERTY);
        }
        return ((Integer) result).intValue();
    }

    public static String getAlignValue(int aAlign) {
        return (aAlign < 0) ? null : (String) fAlignsMap.getValue(new Integer(aAlign));
    }

    public static int getIframeAlignCode(String aAlign) {
        validateOnNull(aAlign, BaseComponentConsts.ALIGN_PROPERTY);
        Object result = fIframeAlignsMap.getKey(aAlign.trim().toLowerCase());
        if (result == null) {
            sendMessageNotSupported(aAlign, BaseComponentConsts.ALIGN_PROPERTY);
        }
        return ((Integer) result).intValue();
    }

    public static String getIframeAlignValue(int aAlign) {
        return (aAlign < 0) ? null : (String) fIframeAlignsMap.getValue(new Integer(aAlign));
    }

    public static int getValignCode(String aValign) {
        validateOnNull(aValign, BaseComponentConsts.VALIGN_PROPERTY);
        Object result = fValignsMap.getKey(aValign.trim().toLowerCase());
        if (result == null) {
            try {
                return Integer.parseInt(aValign);
            } catch (NumberFormatException e) {
            }
            sendMessageNotSupported(aValign, BaseComponentConsts.VALIGN_PROPERTY);
        }
        return ((Integer) result).intValue();
    }

    public static String getValignValue(int aValign) {
        return (aValign < 0) ? null : (String) fValignsMap.getValue(new Integer(aValign));
    }

    public static boolean getOrientationCode(String aOrientation) {
        validateOnNull(aOrientation, BaseComponentConsts.ORIENTATION_PROPERTY);
        Object result = fOrientationsMap.getKey(aOrientation.trim().toLowerCase());
        if (result == null) {
            sendMessageNotSupported(aOrientation, BaseComponentConsts.ORIENTATION_PROPERTY);
        }
        return ((Boolean) result).booleanValue();
    }

    public static short getOrientationConstantCode(String aOrientation) {
        validateOnNull(aOrientation, BaseComponentConsts.ORIENTATION_PROPERTY);
        try {
            return Short.parseShort(aOrientation);
        } catch (NumberFormatException e) {
            Object result = fOrientationConstantsMap.getKey(aOrientation.trim().toLowerCase());
            if (result == null) {
                sendMessageNotSupported(aOrientation, BaseComponentConsts.ORIENTATION_PROPERTY);
            }
            return ((Short) result).shortValue();
        }
    }

    public static String getOrientationValue(boolean aOrientation) {
        return (String) fOrientationsMap.getValue((aOrientation) ? Boolean.TRUE : Boolean.FALSE);
    }

    public static boolean getKindOfImagesCode(String aValue) {
        validateOnNull(aValue, "Kind of images");
        Object result = fKindOfImagesMap.getKey(aValue.trim().toLowerCase());
        if (result == null) {
            sendMessageNotSupported(aValue, "Kind of images");
        }
        return ((Boolean) result).booleanValue();
    }

    public static String getKindOfImagesValue(boolean aCode) {
        return (String) fKindOfImagesMap.get((aCode) ? Boolean.TRUE : Boolean.FALSE);
    }

    public static int getSelectionCode(String aSelection) {
        validateOnNull(aSelection, "Selection");
        try {
            return Integer.parseInt(aSelection);
        } catch (NumberFormatException e) {
            Object result = fSelectionsMap.getKey(aSelection.trim().toLowerCase());
            if (result == null) {
                sendMessageNotSupported(aSelection, "Selection");
            }
            return ((Integer) result).intValue();
        }
    }

    public static String getSelectionValue(int aSelection) {
        return (aSelection < 0) ? null : (String) fSelectionsMap.getValue(new Integer(aSelection));
    }

    public static int getBorderConstraintCode(String aBorderConstraint) {
        validateOnNull(aBorderConstraint, "Border constraint");
        Object result = fBorderConstraintsMap.getKey(aBorderConstraint.trim().toLowerCase());
        if (result == null) {
            try {
                return Integer.parseInt(aBorderConstraint);
            } catch (NumberFormatException e) {
            }
            sendMessageNotSupported(aBorderConstraint, "Border constraint");
        }
        return ((Integer) result).intValue();
    }

    public static String getBorderConstraintValue(int aBorderConstraint) {
        return (aBorderConstraint < 0) ? null : (String) fBorderConstraintsMap.getValue(new Integer(aBorderConstraint));
    }

    public static int getBorderLayoutTypeCode(String aBorderLayoutType) {
        validateOnNull(aBorderLayoutType, "Border layout type");
        Object result = fBorderLayoutTypeMap.getKey(aBorderLayoutType.trim().toLowerCase());
        if (result == null) {
            try {
                return Integer.parseInt(aBorderLayoutType);
            } catch (NumberFormatException e) {
            }
            sendMessageNotSupported(aBorderLayoutType, "Border layout type");
        }
        return ((Integer) result).intValue();
    }

    public static String getBorderLayoutTypeValue(int aBorderLayoutType) {
        return (aBorderLayoutType < 0) ? null : (String) fBorderLayoutTypeMap.getValue(new Integer(aBorderLayoutType));
    }

    public static int getLayoutDirectionCode(String aDirection) {
        validateOnNull(aDirection, "Layout direction");
        Object result = fLayoutDirectionsMap.getKey(aDirection.trim().toLowerCase());
        if (result == null) {
            sendMessageNotSupported(aDirection, "Layout direction");
        }
        return ((Integer) result).intValue();
    }

    public static String getLayoutDirectionValue(int aDirection) {
        return (aDirection < 0) ? null : (String) fLayoutDirectionsMap.getValue(new Integer(aDirection));
    }

    public static int getScopeCode(String aScope) {
        validateOnNull(aScope, "Scope");
        Object result = fScopesMap.getKey(aScope.trim().toLowerCase());
        if (result == null) {
            sendMessageNotSupported(aScope, "Scope");
        }
        return ((Integer) result).intValue();
    }

    public static String getScopeValue(int aScope) {
        return (aScope < 0) ? null : (String) fScopesMap.getValue(new Integer(aScope));
    }

    public static int getBooleanCode(String aBoolean) {
        validateOnNull(aBoolean, "Boolean");
        Object result = fBooleansMap.getKey(aBoolean.trim().toLowerCase());
        if (result == null) {
            sendMessageNotSupported(aBoolean, "Boolean");
        }
        return ((Integer) result).intValue();
    }

    public static String getBooleanValue(int aBoolean) {
        return (aBoolean < 0) ? null : (String) fBooleansMap.getValue(new Integer(aBoolean));
    }

    public static boolean getBoolean(int aBoolean) {
        if (!fBooleansMap.containsKey(new Integer(aBoolean))) {
            sendMessageNotSupported(String.valueOf(aBoolean), "Boolean");
        }
        return (aBoolean == ComponentDefinitions.BOOLEAN_TRUE);
    }

    public static int getScrollingCode(String aScrolling) {
        validateOnNull(aScrolling, "Scrolling");
        Object result = fScrollingMap.getKey(aScrolling.trim().toLowerCase());
        if (result == null) {
            sendMessageNotSupported(aScrolling, "Scrolling");
        }
        return ((Integer) result).intValue();
    }

    public static String getScrollingValue(int aScrolling) {
        return (aScrolling < 0) ? null : (String) fScrollingMap.getValue(new Integer(aScrolling));
    }

    public static int getOverflowCode(String aOverflow) {
        validateOnNull(aOverflow, "Overflow");
        Object result = fOverFlowMap.getKey(aOverflow.trim().toLowerCase());
        if (result == null) {
            sendMessageNotSupported(aOverflow, "Overflow");
        }
        return ((Integer) result).intValue();
    }

    public static String getOverflowValue(int aOverflow) {
        return (aOverflow < 0) ? null : (String) fOverFlowMap.getValue(new Integer(aOverflow));
    }

    public static String getRulesValue(int aRules) {
        return (aRules < 0) ? null : (String) fTableRulesMap.getValue(new Integer(aRules));
    }

    public static int getRulesCode(String aRules) {
        validateOnNull(aRules, BaseComponentConsts.VALIGN_PROPERTY);
        Object result = fTableRulesMap.getKey(aRules.trim().toLowerCase());
        if (result == null) {
            sendMessageNotSupported(aRules, "Rules");
        }
        return ((Integer) result).intValue();
    }

    public static String getFormMethodValue(int aMethod) {
        return (aMethod < 0) ? null : (String) fFormMethodsMap.getValue(new Integer(aMethod));
    }

    public static int getFormMethodCode(String aMethod) {
        validateOnNull(aMethod, "Form Method");
        Object result = fFormMethodsMap.getKey(aMethod.trim().toLowerCase());
        if (result == null) {
            sendMessageNotSupported(aMethod, "Form Method");
        }
        return ((Integer) result).intValue();
    }

    public static void validateBooleanValue(String aValue) {
        String value = aValue.toUpperCase();
        if (!value.equals("TRUE") && !value.equals("FALSE")) {
            throw new IllegalArgumentException("Boolean value must be 'true' or 'false'");
        }
    }

    private static void sendMessageNotSupported(String aValue, String aName) {
        throw new IllegalArgumentException("SupportUtils: " + aName + " value \'" + aValue + "\' not supported.");
    }

    public static String getTargetValue(int aTarget) {
        return (aTarget < 0) ? null : (String) fTargetWindowMap.getValue(new Integer(aTarget));
    }

    public static void validateOnNull(String aValue, String aName) {
        if (aValue == null) {
            throw new IllegalArgumentException("SupportUtils: " + aName + " can't be null.");
        }
    }

    public static void setComponentModifiedProperties(ArrayList aModifiedPropertiesNamesList, ArrayList aModifiedPropertiesValuesList, Object anObject) {
        Iterator namesIterator = aModifiedPropertiesNamesList.iterator();
        Iterator valuesIterator = aModifiedPropertiesValuesList.iterator();
        while (namesIterator.hasNext()) {
            String propertyName = (String) namesIterator.next();
            Object propertyValue = valuesIterator.next();
            BeanUtils.invokeMethod(anObject, StringUtils.getSetterMethod(propertyName), new Object[] { propertyValue });
        }
    }
}
