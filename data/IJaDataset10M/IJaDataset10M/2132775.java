package net.sourceforge.mezzo.editor.internal;

import org.eclipse.xtext.ui.common.service.UIPluginModule;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.Map;
import java.util.HashMap;

/**
 * Generated
 */
public class MezzoActivator extends AbstractUIPlugin {

    private Map<String, Injector> injectors = new HashMap<String, Injector>();

    private static MezzoActivator INSTANCE;

    public Injector getInjector(String languageName) {
        return injectors.get(languageName);
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        INSTANCE = this;
        injectors.put("net.sourceforge.mezzo.editor.Mezzo", Guice.createInjector(new net.sourceforge.mezzo.editor.MezzoUiModule(), createUIPluginModule()));
    }

    public static MezzoActivator getInstance() {
        return INSTANCE;
    }

    protected UIPluginModule createUIPluginModule() {
        return new UIPluginModule(this);
    }
}
