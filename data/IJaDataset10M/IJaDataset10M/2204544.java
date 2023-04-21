package org.az.gsm.web.client.scaffold.ioc;

import org.az.gsm.web.client.scaffold.ScaffoldDesktopApp;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value = { ScaffoldModule.class })
public interface DesktopInjector extends Ginjector {

    ScaffoldDesktopApp getScaffoldApp();
}
