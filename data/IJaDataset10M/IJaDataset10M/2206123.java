package org.sodeja.swing.context;

import org.sodeja.swing.frame.ApplicationFrame;
import org.sodeja.swing.locale.LocaleProvider;
import org.sodeja.swing.localization.LocalizationFactory;
import org.sodeja.swing.resource.ResourceProvider;

public interface ApplicationContext {

    public LocaleProvider getLocaleProvider();

    public ResourceProvider getResourceProvider();

    public LocalizationFactory getLocalizationFactory();

    public ApplicationFrame<?> getRootFrame();
}
