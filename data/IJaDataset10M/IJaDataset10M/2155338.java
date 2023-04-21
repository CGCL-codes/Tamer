package org.regilo.content.handlers;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.regilo.content.model.Container;
import org.regilo.content.model.INodeProvider;
import org.regilo.content.model.Node;
import org.regilo.content.model.parsers.RemoveContentParser;
import org.regilo.core.model.WebSiteConnector;
import org.regilo.core.ui.editors.RegiloEditor;
import org.regilo.core.xmlrpc.BrowserFactory;
import org.regilo.core.xmlrpc.Callback;
import org.regilo.core.xmlrpc.Method;
import org.regilo.core.xmlrpc.Methods;
import org.regilo.core.xmlrpc.RegiloBrowser;

public class RemoveNodeHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        RegiloEditor editor = (RegiloEditor) HandlerUtil.getActiveEditorChecked(event);
        WebSiteConnector web = (WebSiteConnector) editor.getDrupalSite().getConnector(WebSiteConnector.TYPE);
        INodeProvider nodeProvider = (INodeProvider) Platform.getAdapterManager().getAdapter(web, INodeProvider.class);
        Container container = nodeProvider.getContainer();
        IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
        if (selection.getFirstElement() instanceof Node) {
            Node node = (Node) selection.getFirstElement();
            if (MessageDialog.openConfirm(window.getShell(), "Confirm Delete", "Do You really want to delete node: '" + node.getTitle() + "' ?. This operation cannot be undone!")) {
                RemoveContentParser removeContentParser = new RemoveContentParser(node, container);
                Method method = Methods.getInstance().getMethod("org.regilo.methods.content.delete");
                try {
                    RegiloBrowser browser = BrowserFactory.getBrowser(web);
                    List<Object> arguments = new ArrayList<Object>();
                    arguments.add(node.getNid());
                    browser.execute(method, arguments, removeContentParser, new Callback() {

                        public void call() {
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
}
