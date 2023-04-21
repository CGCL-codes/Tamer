package us.gibb.dev.gwt.demo.client.inject;

import us.gibb.dev.gwt.command.CommandEventBus;
import us.gibb.dev.gwt.demo.client.HelloPresenter;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(InjectorConfig.class)
public interface Injector extends Ginjector {

    HelloPresenter getHelloPresenter();

    CommandEventBus getEventBus();
}
