package org.akrogen.tkui.grammars.xul.internal.ui.menus;

import java.util.Iterator;
import java.util.Vector;
import org.akrogen.tkui.core.gui.GuiConstants;
import org.akrogen.tkui.core.internal.dom.attributes.TkuiEventAttrDescriptorImpl;
import org.akrogen.tkui.grammars.xul.XulConstants;
import org.akrogen.tkui.grammars.xul.internal.ui.XULElementImpl;
import org.akrogen.tkui.grammars.xul.ui.IXULControlElement;
import org.akrogen.tkui.grammars.xul.ui.menus.IPopup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.events.Event;

public class PopupImpl extends XULElementImpl implements IPopup {

    private static final long serialVersionUID = 3545513997853209648L;

    private static Log logger = LogFactory.getLog(PopupImpl.class);

    private Vector relatedXULControls = new Vector();

    public PopupImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) throws DOMException {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
        registerAttrDescriptor(new TkuiEventAttrDescriptorImpl(XulConstants.XUL_NAMESPACE_URI, XulConstants.ONPOPUPSHOWN_EVENT, false, GuiConstants.Show));
        registerAttrDescriptor(new TkuiEventAttrDescriptorImpl(XulConstants.XUL_NAMESPACE_URI, XulConstants.ONINPUT_EVENT, false, GuiConstants.Hide));
    }

    public String getGuiWidgetId() {
        return GuiConstants.GUI_MENUPOPUP_ID;
    }

    public void registerMenuContainer(IXULControlElement xulControlElement) {
        relatedXULControls.add(xulControlElement);
    }

    public void unregisterMenuContainer(IXULControlElement xulControlElement) {
        relatedXULControls.remove(xulControlElement);
    }

    protected void handleGUIPropertyChanged(Event evt) {
        super.handleGUIPropertyChanged(evt);
        String eventName = evt.getType();
        if (GuiConstants.ONDISPOSE_EVENT.equals(eventName)) {
            Iterator iterator = relatedXULControls.iterator();
            while (iterator.hasNext()) ((IXULControlElement) iterator.next()).setPopupMenu(null);
        }
    }
}
