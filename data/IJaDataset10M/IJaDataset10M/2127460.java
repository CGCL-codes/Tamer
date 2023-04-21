package br.com.mobilit.test.exemple;

import javax.microedition.midlet.MIDletStateChangeException;
import br.com.mobilit.components.i18n.exception.I18nException;
import br.com.mobilit.components.properties.excepetion.PropertieException;
import br.com.mobilit.control.ApplicationMIDlet;
import br.com.mobilit.test.exemple.screen.ScreenFactory;
import br.com.mobilit.test.exemple.service.GlobalWeatherService;
import br.com.mobilit.test.exemple.service.QuoteOfTheDayService;
import br.com.mobilit.test.exemple.service.SettingsService;
import br.com.mobilit.test.exemple.service.TranslatorService;
import br.com.mobilit.test.exemple.service.MainService;

public class MobServicesMIDlet extends ApplicationMIDlet {

    public static final String PT_LANGUAGE = "pt";

    public static final String EN_LANGUAGE = "en";

    public static final String PT_LANGUAGE_FILE = "/stringTable_pt";

    public static final String EN_LANGUAGE_FILE = "/stringTable_en";

    public static final int MAIN_SERVICE = 1;

    public static final int GLOBAL_WEATHER_SERVICE = 2;

    public static final int TRANSLATOR_SERVICE = 3;

    public static final int QUOTE_OF_THE_DAY_SERVICE = 4;

    public static final int SETTINGS_SERVICE = 5;

    private static final int NUMBER_OF_SERVICES = 6;

    private static final String LOCALE_PROPERTIE = "microedition.locale";

    private MainService mainService;

    private TranslatorService translatorService;

    private GlobalWeatherService globalWeatherService;

    private QuoteOfTheDayService quoteOfTheDayService;

    private SettingsService settingsService;

    private boolean isFirstRun = true;

    public MobServicesMIDlet() {
    }

    protected void pauseApp() {
    }

    private void loadLanguage() {
        try {
            String language = System.getProperty(LOCALE_PROPERTIE);
            System.out.println(language);
            if (language.startsWith(PT_LANGUAGE)) {
                moSOAFramework.setLanguage(PT_LANGUAGE);
                moSOAFramework.loudLanguageTerms(PT_LANGUAGE_FILE);
            } else {
                moSOAFramework.setLanguage(EN_LANGUAGE);
                moSOAFramework.loudLanguageTerms(EN_LANGUAGE_FILE);
            }
            moSOAFramework.loudProperties("/properties");
        } catch (I18nException e) {
            e.printStackTrace();
        } catch (PropertieException e) {
            e.printStackTrace();
        }
    }

    private void initApplation() {
        this.loadLanguage();
        this.initialize(NUMBER_OF_SERVICES);
        this.mainService = new MainService(MAIN_SERVICE, this.serviceControl, this.screenManager, this);
        this.translatorService = new TranslatorService(TRANSLATOR_SERVICE, this.serviceControl, this.screenManager, this);
        this.globalWeatherService = new GlobalWeatherService(GLOBAL_WEATHER_SERVICE, this.serviceControl, this.screenManager, this);
        this.quoteOfTheDayService = new QuoteOfTheDayService(QUOTE_OF_THE_DAY_SERVICE, this.serviceControl, this.screenManager, this);
        this.settingsService = new SettingsService(SETTINGS_SERVICE, this.serviceControl, this.screenManager, this);
        this.serviceControl.add(MAIN_SERVICE, this.mainService);
        this.serviceControl.add(TRANSLATOR_SERVICE, this.translatorService);
        this.serviceControl.add(GLOBAL_WEATHER_SERVICE, this.globalWeatherService);
        this.serviceControl.add(QUOTE_OF_THE_DAY_SERVICE, this.quoteOfTheDayService);
        this.serviceControl.add(SETTINGS_SERVICE, this.settingsService);
        this.serviceControl.changeService(MAIN_SERVICE);
    }

    protected void startApp() throws MIDletStateChangeException {
        if (this.isFirstRun) {
            this.initApplation();
            this.isFirstRun = false;
        } else {
            String temp = System.getProperty(LOCALE_PROPERTIE);
            if (!temp.startsWith(moSOAFramework.getLanguage())) {
                this.loadLanguage();
                ScreenFactory.getINSTANCE().reloadScreens();
            }
        }
    }

    /**
	 * This method should exit the midlet.
	 */
    public void exitApp() {
        getDisplayAPP().setCurrent(null);
        try {
            destroyApp(true);
        } catch (MIDletStateChangeException e) {
            e.printStackTrace();
        }
        notifyDestroyed();
    }

    public void destroyApp(boolean arg0) throws MIDletStateChangeException {
    }
}
