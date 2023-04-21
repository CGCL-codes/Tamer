package org.docflower.sylvia.rcp.model.actionhandlers;

import java.util.Map;
import org.docflower.consts.DocFlowerConsts;
import org.docflower.model.Action;
import org.docflower.model.actionhandlers.AbstractActionHandler;
import org.docflower.ui.*;
import org.docflower.util.ActionHandlerException;
import org.docflower.xml.DOMUtils;
import org.eclipse.swt.browser.Browser;
import org.mozilla.interfaces.*;
import org.w3c.dom.*;

public class PrintCurrentFormActionHandler extends AbstractActionHandler {

    private String selectionProviderId;

    @Override
    public void init(Action parent, Element actionItemElement, Map<String, Object> params, Node baseNode, UpdateInfo updateInfo) {
        if (actionItemElement != null) {
            selectionProviderId = DOMUtils.getAttrValue(actionItemElement, DocFlowerConsts.ATTR_SELECTION_PROVIDER_ID);
        } else if (params != null) {
            selectionProviderId = (String) params.get(DocFlowerConsts.ATTR_SELECTION_PROVIDER_ID);
        } else {
            selectionProviderId = null;
        }
    }

    @Override
    public boolean handle(Action parent, UpdateInfo updateInfo) throws ActionHandlerException {
        if ((selectionProviderId != null) && (selectionProviderId.length() > 0) && (parent.getContext() instanceof ISelectionsManager)) {
            Browser browser = (Browser) ((ISelectionsManager) parent.getContext()).getSelected(selectionProviderId);
            nsIWebBrowser webBrowser = (nsIWebBrowser) browser.getWebBrowser();
            if (webBrowser == null) {
                System.out.println("Could not get the nsIWebBrowser from the Browser widget");
                return false;
            }
            nsIInterfaceRequestor ir = (nsIInterfaceRequestor) webBrowser.queryInterface(nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID);
            nsIWebBrowserPrint print = (nsIWebBrowserPrint) ir.getInterface(nsIWebBrowserPrint.NS_IWEBBROWSERPRINT_IID);
            nsIPrintSettings settings = print.getGlobalPrintSettings();
            settings.setPrintSilent(false);
            print.print(settings, null);
        }
        return true;
    }

    @Override
    public boolean rollback(Action parent, UpdateInfo updateInfo) throws ActionHandlerException {
        return false;
    }
}
