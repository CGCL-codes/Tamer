package br.com.sysmap.crux.gwt.client;

import br.com.sysmap.crux.core.client.declarative.DeclarativeFactory;
import br.com.sysmap.crux.core.client.declarative.TagAttribute;
import br.com.sysmap.crux.core.client.declarative.TagAttributeDeclaration;
import br.com.sysmap.crux.core.client.declarative.TagAttributes;
import br.com.sysmap.crux.core.client.declarative.TagAttributesDeclaration;
import br.com.sysmap.crux.core.client.screen.InterfaceConfigException;
import br.com.sysmap.crux.core.client.screen.factory.HasAnimationFactory;
import br.com.sysmap.crux.core.client.screen.factory.HasCloseHandlersFactory;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Represents a PopupPanelFactory
 * @author Thiago Bustamante
 *
 */
@DeclarativeFactory(id = "popupPanel", library = "gwt")
public class PopupPanelFactory extends PanelFactory<PopupPanel> implements HasAnimationFactory<PopupPanel>, HasCloseHandlersFactory<PopupPanel> {

    @Override
    public PopupPanel instantiateWidget(Element element, String widgetId) {
        String autoHideStr = getProperty(element, "autoHide");
        boolean autoHide = false;
        if (autoHideStr != null && autoHideStr.length() > 0) {
            autoHide = Boolean.parseBoolean(autoHideStr);
        }
        String modalStr = getProperty(element, "modal");
        boolean modal = false;
        if (modalStr != null && modalStr.length() > 0) {
            modal = Boolean.parseBoolean(modalStr);
        }
        return new PopupPanel(autoHide, modal);
    }

    @Override
    @TagAttributes({ @TagAttribute(value = "previewingAllNativeEvents", type = Boolean.class), @TagAttribute(value = "autoHideOnHistoryEventsEnabled", type = Boolean.class), @TagAttribute("glassStyleName"), @TagAttribute(value = "glassEnabled", type = Boolean.class) })
    @TagAttributesDeclaration({ @TagAttributeDeclaration(value = "modal", type = Boolean.class), @TagAttributeDeclaration(value = "autoHide", type = Boolean.class) })
    public void processAttributes(WidgetFactoryContext<PopupPanel> context) throws InterfaceConfigException {
        super.processAttributes(context);
    }
}
