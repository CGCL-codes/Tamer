package org.netbeans.modules.rtfcopypaste.utils;

import org.netbeans.modules.rtfcopypaste.options.SpinnerFontModel;

public class CurrentCopyPasteProfile {

    private static String currentCopyPasteProfile = null;

    private static Integer currentCopyPasteFontSize = null;

    private static String currentCopyPasteOption = null;

    public static String getCurrentCopyPasteProfile() {
        if (currentCopyPasteProfile == null) {
            currentCopyPasteProfile = DefaultEditorProfileManager.getDefault().getCurrentFontAndColorsProfile();
        }
        return currentCopyPasteProfile;
    }

    public static void setCurrentCopyPasteProfile(String currentCopyPasteProfile) {
        CurrentCopyPasteProfile.currentCopyPasteProfile = currentCopyPasteProfile;
    }

    public static Integer getCurrentCopyPasteFontSize() {
        if (currentCopyPasteFontSize == null) {
            currentCopyPasteFontSize = (Integer) new SpinnerFontModel().getValue();
        }
        return currentCopyPasteFontSize;
    }

    public static void setCurrentCopyPasteFontSize(Integer currentCopyPasteFontSize) {
        CurrentCopyPasteProfile.currentCopyPasteFontSize = currentCopyPasteFontSize;
    }

    public static String getCurrentCopyPasteOption() {
        if (currentCopyPasteOption == null) {
            return "HIGHTLIGHTS";
        }
        return currentCopyPasteOption;
    }

    public static void setCurrentCopyPasteOption(String currentCopyPasteOption) {
        CurrentCopyPasteProfile.currentCopyPasteOption = currentCopyPasteOption;
    }
}
