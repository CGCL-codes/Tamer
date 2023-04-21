package net.sourceforge.hlm.gui.managers;

public interface DisplayManager extends DisplayObserver {

    PreferencesManager getPreferencesManager();

    void setLanguage(String language);

    void setUseCustomLayouts(boolean useCustomLayouts);

    void setUseVerbalAndOr(boolean useVerbalAndOr);

    ColorManager getColorManager();

    FontManager getFontManager();
}
