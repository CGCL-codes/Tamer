package nuts.exts.xwork2;

import java.util.ResourceBundle;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.ResourceBundleTextProvider;
import com.opensymphony.xwork2.inject.Inject;

/**
 * This factory enables users to provide and correctly initialize a custom TextProvider.
 */
@SuppressWarnings("unchecked")
public class TextProviderFactory {

    private TextProvider textProvider;

    /**
     * @param textProvider textProvider
     */
    @Inject
    public void setTextProvider(TextProvider textProvider) {
        this.textProvider = textProvider;
    }

    protected TextProvider getTextProvider() {
        if (this.textProvider == null) {
            return new TextProviderSupport();
        } else {
            return textProvider;
        }
    }

    /**
     * @param clazz class 
     * @param provider locale provider
     * @return text provider
     */
    public TextProvider createInstance(Class clazz, LocaleProvider provider) {
        TextProvider instance = getTextProvider();
        if (instance instanceof ResourceBundleTextProvider) {
            ((ResourceBundleTextProvider) instance).setClazz(clazz);
            ((ResourceBundleTextProvider) instance).setLocaleProvider(provider);
        }
        return instance;
    }

    /**
     * @param bundle resource bundle
     * @param provider locale provider
     * @return text provider
     */
    public TextProvider createInstance(ResourceBundle bundle, LocaleProvider provider) {
        TextProvider instance = getTextProvider();
        if (instance instanceof ResourceBundleTextProvider) {
            ((ResourceBundleTextProvider) instance).setBundle(bundle);
            ((ResourceBundleTextProvider) instance).setLocaleProvider(provider);
        }
        return instance;
    }
}
