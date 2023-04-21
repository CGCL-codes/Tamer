package mortiforo;

import java.util.ResourceBundle;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * @author fjleon
 *
 * Allows ResourceBundles to be used directly from
 * Freemarker templates, or returns the used locale
 */
public class ResourceBundleWrapper implements TemplateHashModel {

    private ResourceBundle bundle;

    public ResourceBundleWrapper(ResourceBundle bundle) {
        super();
        this.bundle = bundle;
    }

    public TemplateModel get(String arg0) throws TemplateModelException {
        if (arg0.equals("locale")) {
            return new SimpleScalar(bundle.getLocale().toString());
        }
        return new SimpleScalar(bundle.getString(arg0));
    }

    public boolean isEmpty() throws TemplateModelException {
        return !(bundle.getKeys().hasMoreElements());
    }
}
