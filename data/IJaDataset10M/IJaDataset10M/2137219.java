package com.smssalama.i18n;

public abstract class ScreenTitles implements Localizable {

    public static final ScreenTitles screenTitles() {
        return (ScreenTitles) LocalizableFactory.createLocalizedObject(ScreenTitles.class);
    }

    public abstract String setupLanguage();

    public abstract String setup();

    public abstract String home();

    public abstract String signIn();

    public abstract String settings();

    public abstract String contactEditor();

    public abstract String contactEditorNew();

    public abstract String contactView();

    public abstract String messages();

    public abstract String messageEditor();

    public abstract String messageEditorNew();

    public abstract String contacts();

    public abstract String help();
}
