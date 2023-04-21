package pikes.peak.test;

import java.util.Locale;
import org.junit.Before;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.theme.FixedThemeResolver;

public abstract class PikesPeakComponentTestCase {

    protected StaticMessageSource messageSource = null;

    protected StaticMessageSource themeMessageSource = null;

    protected FixedLocaleResolver localeResolver = null;

    protected FixedThemeResolver themeResolver = null;

    protected PikesPeakTestRequest pikesPeakRequest = null;

    @Before
    public void setup() throws Exception {
        messageSource = new StaticMessageSource();
        messageSource.addMessage("Some text", Locale.ENGLISH, "Translated Text");
        themeMessageSource = new StaticMessageSource();
        themeMessageSource.addMessage("Some text", Locale.ENGLISH, "Translated Theme Text");
        localeResolver = new FixedLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        themeResolver = new FixedThemeResolver();
        themeResolver.setDefaultThemeName("myTheme");
        pikesPeakRequest = createPikesPeakTestRequest();
    }

    protected PikesPeakTestRequest createPikesPeakTestRequest() {
        return new PikesPeakTestRequest(messageSource, themeMessageSource, localeResolver, themeResolver);
    }
}
