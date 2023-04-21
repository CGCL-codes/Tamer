package br.com.sysmap.crux.core.client.screen.factory;

import br.com.sysmap.crux.core.client.declarative.TagEvent;
import br.com.sysmap.crux.core.client.declarative.TagEvents;
import br.com.sysmap.crux.core.client.event.bind.BlurEvtBind;
import br.com.sysmap.crux.core.client.event.bind.FocusEvtBind;
import br.com.sysmap.crux.core.client.screen.InterfaceConfigException;
import br.com.sysmap.crux.core.client.screen.WidgetFactory.WidgetFactoryContext;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
public interface HasAllFocusHandlersFactory<T extends HasAllFocusHandlers> {

    @TagEvents({ @TagEvent(FocusEvtBind.class), @TagEvent(BlurEvtBind.class) })
    void processEvents(WidgetFactoryContext<T> context) throws InterfaceConfigException;
}
