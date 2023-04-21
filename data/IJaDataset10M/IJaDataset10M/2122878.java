package com.softaspects.jsf.component.container;

import com.softaspects.jsf.support.SupportDefinitions;
import com.softaspects.jsf.component.base.BaseComponentConsts;
import java.util.*;

public class SupportUtils implements SupportDefinitions {

    private static Set<String> fAligns = new HashSet<String>();

    private static Set<String> fIframeAligns = new HashSet<String>();

    private static Set<String> fValigns = new HashSet<String>();

    private static Set<String> fSelections = new HashSet<String>();

    private static Set<String> fKindOfImages = new HashSet<String>();

    private static Set<String> fOrientations = new HashSet<String>();

    private static Set<String> fOrientationConstants = new HashSet<String>();

    private static Set<String> fScopes = new HashSet<String>();

    private static Set<String> fBorderConstraints = new HashSet<String>();

    private static Set<String> fBorderLayoutType = new HashSet<String>();

    private static Set<String> fLayoutDirections = new HashSet<String>();

    private static Set<String> fBooleans = new HashSet<String>();

    private static Set<String> fScrolling = new HashSet<String>();

    private static Set<String> fOverFlow = new HashSet<String>();

    private static Set<String> fTableRules = new HashSet<String>();

    private static Set<String> fFormMethods = new HashSet<String>();

    private static Set<String> fTargetWindow = new HashSet<String>();

    private static void initTarget() {
    }

    private static void initAligns() {
        fAligns.add(BaseComponentConsts.LEFT_VALUE);
        fAligns.add(BaseComponentConsts.RIGHT_VALUE);
        fAligns.add(BaseComponentConsts.CENTER_VALUE);
    }

    private static void initValigns() {
        fValigns.add(BaseComponentConsts.MIDDLE_VALUE);
        fValigns.add(BaseComponentConsts.TOP_VALUE);
        fValigns.add(BaseComponentConsts.BOTTOM_VALUE);
        fValigns.add(VALIGN_BASELINE_STR);
    }

    private static void initIframeAligns() {
        fIframeAligns.add(BaseComponentConsts.LEFT_VALUE);
        fIframeAligns.add(BaseComponentConsts.RIGHT_VALUE);
        fIframeAligns.add(BaseComponentConsts.TOP_VALUE);
        fIframeAligns.add(IFRAME_ALIGN_BASELINE_STR);
        fIframeAligns.add(BaseComponentConsts.BOTTOM_VALUE);
        fIframeAligns.add(BaseComponentConsts.MIDDLE_VALUE);
    }

    private static void initOrientations() {
        fOrientations.add(ORIENTATION_X_NAME);
        fOrientations.add(ORIENTATION_Y_NAME);
    }

    private static void initOrientationConstants() {
        fOrientationConstants.add(ORIENTATION_X_NAME);
        fOrientationConstants.add(ORIENTATION_Y_NAME);
    }

    private static void initScopes() {
        fScopes.add(SCOPE_REQUEST);
        fScopes.add(SCOPE_SESSION);
        fScopes.add(SCOPE_APPLICTION);
        fScopes.add(SCOPE_PAGE);
    }

    private static void initSelections() {
        fSelections.add(SELECTION_SINGLE_STR);
        fSelections.add(SELECTION_SINGLE_INTERVAL_STR);
        fSelections.add(SELECTION_MULTIPLE_INTERVAL_STR);
    }

    private static void initBorderConstraints() {
        fBorderConstraints.add(BORDER_WEST_NAME);
        fBorderConstraints.add(BORDER_EAST_NAME);
        fBorderConstraints.add(BORDER_SOUTH_NAME);
        fBorderConstraints.add(BORDER_NORTH_NAME);
        fBorderConstraints.add(BORDER_CENTER_NAME);
    }

    private static void initKindsOfImages() {
        fKindOfImages.add(IMAGES_BIG_NAME);
        fKindOfImages.add(IMAGES_SMALL_NAME);
    }

    private static void initBorderLayoutTypes() {
        fBorderLayoutType.add(BORDER_LAYOUT_TYPE_EAST_WEST_NAME);
        fBorderLayoutType.add(BORDER_LAYOUT_TYPE_NORTH_SOUTH_NAME);
    }

    private static void initLayoutDirections() {
        fLayoutDirections.add(LAYOUT_DIRECTION_HORIZONTAL_NAME);
        fLayoutDirections.add(LAYOUT_DIRECTION_VERTICAL_NAME);
    }

    private static void initBooleans() {
        fBooleans.add(BOOLEAN_FALSE);
        fBooleans.add(BOOLEAN_TRUE);
    }

    private static void initScrollings() {
        fScrolling.add(SCROLLING_AUTO_NAME);
        fScrolling.add(SCROLLING_NO_NAME);
        fScrolling.add(SCROLLING_YES_NAME);
    }

    private static void initOverflows() {
        fOverFlow.add(OVERFLOW_AUTO_NAME);
        fOverFlow.add(OVERFLOW_SCROLL_NAME);
        fOverFlow.add(OVERFLOW_VISIBLE_NAME);
        fOverFlow.add(OVERFLOW_HIDDEN_NAME);
    }

    private static void initRules() {
        fTableRules.add(RULES_ALL_NAME);
        fTableRules.add(RULES_COLS_NAME);
        fTableRules.add(RULES_GROUPS_NAME);
        fTableRules.add(RULES_NONE_NAME);
        fTableRules.add(RULES_ROWS_NAME);
    }

    private static void initFormMethods() {
        fFormMethods.add(FORM_METHOD_GET_NAME);
        fFormMethods.add(FORM_METHOD_POST_NAME);
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
        initRules();
        initFormMethods();
    }

    public static boolean isRuleValid(String rule) {
        return fTableRules.contains(rule);
    }

    public static boolean isAlignValid(String align) {
        return fAligns.contains(align);
    }

    public static boolean isValignValid(String valign) {
        return fValigns.contains(valign);
    }

    public static boolean isBorderConstraintValid(String borderConstraint) {
        return fBorderConstraints.contains(borderConstraint);
    }
}
