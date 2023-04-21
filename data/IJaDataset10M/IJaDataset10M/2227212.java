package org.vikamine.kernel;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Manages resources/strings for the vikamine kernel.
 * 
 * @author atzmueller
 */
public class KernelResources {

    private ResourceBundle i18N;

    private static KernelResources instance;

    private KernelResources() {
        super();
    }

    private KernelResources(Locale locale) {
        this();
        i18N = ResourceBundle.getBundle("resources.messages.VKMKernelMessages", locale);
    }

    public static void createInstance(Locale locale) {
        if (instance != null) {
            throw new IllegalStateException("Instance has been already created!");
        }
        instance = new KernelResources(locale);
    }

    public static synchronized KernelResources getInstance() {
        if (instance == null) {
            createInstance(Locale.getDefault());
        }
        return instance;
    }

    public ResourceBundle getI18N() {
        return i18N;
    }
}
