package org.docflower.printforms.model.actionhandlers;

import java.io.*;
import java.io.InputStream;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import org.docflower.consts.DocFlowerConsts;
import org.docflower.model.Action;
import org.docflower.model.actionhandlers.AbstractActionHandler;
import org.docflower.page.PageManager;
import org.docflower.printforms.components.bind.PrintFormsCollectionBinding;
import org.docflower.printforms.components.pagecontrols.*;
import org.docflower.resources.ResourceManager;
import org.docflower.ui.*;
import org.docflower.util.*;
import org.docflower.xml.DOMUtils;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.*;

public class PrintAllFormsActionHandler extends AbstractActionHandler {

    private static final String REPORT_CANT_BE_GENERATED = "Unable to generate the print form";

    private Action parent;

    private String selectionProviderId;

    private boolean complete;

    public PrintAllFormsActionHandler() {
    }

    @Override
    public void init(Action parent, Element actionItemElement, Map<String, Object> params, Node baseNode, UpdateInfo updateInfo) {
        this.parent = parent;
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
            browser.addProgressListener(new ProgressListener() {

                @Override
                public void changed(ProgressEvent event) {
                }

                @Override
                public void completed(ProgressEvent event) {
                    setComplete(true);
                }
            });
            printAllForms();
        }
        return true;
    }

    @Override
    public boolean rollback(Action parent, UpdateInfo updateInfo) throws ActionHandlerException {
        return false;
    }

    public void printAllForms() {
        if (parent.getContext() instanceof PageManager) {
            PageManager pm = (PageManager) parent.getContext();
            if (pm.getChildren().get(0) instanceof PrintFormsCollectionBinding) {
                PrintFormsCollectionBinding binding = (PrintFormsCollectionBinding) pm.getChildren().get(0);
                PrintFormsCollection printFormsCollection = ((PrintFormsCollection) binding.getControl());
                List<PrintFormsCollectionItem> list = printFormsCollection.getItems();
                for (PrintFormsCollectionItem printFormsCollectionItem : list) {
                    fillBrowser(printFormsCollectionItem.getXsl(), parent.getContext().getBaseNode(), printFormsCollection.getBrowserSupport().getBrowser());
                    printFormsCollection.getBrowserSupport().printBrowserContent(printFormsCollectionItem);
                }
            }
        }
    }

    private void fillBrowser(String xsl, Node baseNode, Browser browser) {
        Transformer transformer;
        try {
            InputStream xsltInputStream = ResourceManager.getInstance().getResourceInputStream(xsl);
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                transformerFactory.setURIResolver(new XslURIResolver());
                StreamSource ss = new StreamSource(xsltInputStream);
                transformer = transformerFactory.newTransformer(ss);
            } finally {
                xsltInputStream.close();
            }
            StringWriter html = new StringWriter();
            StreamResult htmlStreamResult = new StreamResult(html);
            StringBuilder sb = new StringBuilder();
            DOMUtils.getNodeAbsoluteXPath(baseNode, null, sb);
            Document doc = (Document) (baseNode.getNodeType() == Node.DOCUMENT_NODE ? baseNode : baseNode.getOwnerDocument());
            transformer.setParameter("baseNode", sb.toString());
            transformer.transform(new DOMSource(doc), htmlStreamResult);
            setComplete(false);
            browser.setText(html.toString());
            do {
                if (!Display.getCurrent().readAndDispatch()) {
                    Display.getCurrent().sleep();
                }
            } while (!isComplete());
        } catch (Exception e) {
            browser.setText(REPORT_CANT_BE_GENERATED);
        }
    }

    public synchronized void setComplete(boolean isDone) {
        this.complete = isDone;
    }

    public synchronized boolean isComplete() {
        return complete;
    }
}
