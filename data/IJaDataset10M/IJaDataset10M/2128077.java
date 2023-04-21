package interfaces.menu.options;

import interfaces.options.CheckBoxOptionWidget;
import interfaces.options.OptionWidget;
import interfaces.options.OptionsContainer;
import interfaces.options.TextEditorOptionWidget;
import fileHandling.ProfileHandler;
import fileHandling.language.LanguageLoader;
import fileHandling.language.interfaces.MenuText;
import fileHandling.language.interfaces.OptionsWindowText;
import logic.nodes.nodeSettings.Settings;
import main.InitGame;
import org.fenggui.Display;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import settings.CommonSettings;

public class CommonOptionsWindow extends OptionWindow {

    private OptionsContainer options;

    public CommonOptionsWindow(Display display, OptionsMenu optionsMenu) {
        super(display, optionsMenu, LanguageLoader.get(OptionsWindowText.Common), LanguageLoader.COMMON_FILE);
        options = new OptionsContainer(MAX_OPTIONS);
        addTabButton(LanguageLoader.get(MenuText.Options), options);
        makeOptions();
        applyButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                CommonSettings settings = CommonSettings.getInstance();
                Settings tempSettings = new Settings(options);
                settings.copySettings(tempSettings, true);
                ProfileHandler.changeCommonOptions(ProfileHandler.getSelectedProfile());
                InitGame.changeCommonSettings();
            }
        });
        setOptionsContainer(options);
    }

    private void makeOptions() {
        CommonSettings settings = CommonSettings.getInstance();
        String languageString = settings.getLanguageString();
        addComboBox(languageString, CommonSettings.LANGUAGE, LanguageLoader.getInstalledLanguages(), options);
        OptionWidget fov = new TextEditorOptionWidget(settings.getFOVString(), CommonSettings.FOV, LanguageLoader.get(CommonSettings.FOV));
        options.addOption(fov);
        String drawFPSString = settings.getDrawFPSString();
        OptionWidget drawFPS = new CheckBoxOptionWidget(drawFPSString, CommonSettings.DRAWFPS, LanguageLoader.get(CommonSettings.DRAWFPS), display);
        options.addOption(drawFPS);
    }
}
