package ch.oblivion.comixviewer.rcpplugin.bindings;

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import ch.oblivion.comixviewer.rcpplugin.Activator;

/**
 * Binding strategy for {@link URL} objects.
 * @author mark.miller
 */
public class UrlBindingStrategy {

    public static UpdateValueStrategy createUrlToStringStrategy() {
        UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setConverter(new UrlToStringConverter());
        return strategy;
    }

    public static UpdateValueStrategy createStringToUrlStrategy() {
        UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setConverter(new StringToUrlConverter());
        strategy.setAfterGetValidator(new UrlValidator());
        return strategy;
    }

    static class UrlToStringConverter implements IConverter {

        @Override
        public Object convert(Object fromObject) {
            assert fromObject instanceof URL;
            URL url = (URL) fromObject;
            return url.toExternalForm();
        }

        @Override
        public Object getFromType() {
            return URL.class;
        }

        @Override
        public Object getToType() {
            return String.class;
        }
    }

    static class StringToUrlConverter implements IConverter {

        @Override
        public Object convert(Object fromObject) {
            assert fromObject instanceof String;
            String urlString = (String) fromObject;
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not convert to url " + fromObject, e));
            }
            return url;
        }

        @Override
        public Object getFromType() {
            return String.class;
        }

        @Override
        public Object getToType() {
            return URL.class;
        }
    }

    static class UrlValidator implements IValidator {

        @Override
        public IStatus validate(Object value) {
            assert value instanceof String;
            IStatus status = ValidationStatus.ok();
            try {
                new URL((String) value);
            } catch (MalformedURLException e) {
                status = ValidationStatus.error("Could not convert to url " + value, e);
            }
            return status;
        }
    }
}
