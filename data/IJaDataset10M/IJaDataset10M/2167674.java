package br.com.sysmap.crux.core.client.screen.factory;

import br.com.sysmap.crux.core.client.declarative.TagEvent;
import br.com.sysmap.crux.core.client.declarative.TagEvents;
import br.com.sysmap.crux.core.client.event.bind.ValueChangeEvtBind;
import br.com.sysmap.crux.core.client.screen.InterfaceConfigException;
import br.com.sysmap.crux.core.client.screen.WidgetFactory.WidgetFactoryContext;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
public interface HasValueChangeHandlersFactory<T extends HasValueChangeHandlers<?>> {

    @TagEvents({ @TagEvent(ValueChangeEvtBind.class) })
    void processEvents(WidgetFactoryContext<T> context) throws InterfaceConfigException;
}
