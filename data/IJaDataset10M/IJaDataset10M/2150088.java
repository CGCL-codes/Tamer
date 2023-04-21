package org.docflower.dbfmediator.rcp.model.actionhandlers;

import java.io.InputStream;
import java.util.Map;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamSource;
import org.docflower.consts.DocFlowerConsts;
import org.docflower.model.Action;
import org.docflower.model.actionhandlers.AbstractActionHandler;
import org.docflower.resources.ResourceManager;
import org.docflower.ui.UpdateInfo;
import org.docflower.util.*;
import org.w3c.dom.*;

public class ExportDBFFolderActionHandler extends AbstractActionHandler {

    private String xslt;

    private Document targerDoc;

    @Override
    public void init(Action parent, Element actionItemElement, Map<String, Object> params, Node baseNode, UpdateInfo updateInfo) {
        xslt = (String) params.get(DocFlowerConsts.ATTR_XSLT);
        targerDoc = (Document) params.get(DocFlowerConsts.ATTR_DOC);
    }

    @Override
    public boolean handle(Action parent, UpdateInfo updateInfo) throws ActionHandlerException {
        Transformer transformer;
        try {
            InputStream xsltInputStream = ResourceManager.getInstance().getResourceInputStream(xslt);
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                transformerFactory.setURIResolver(new XslURIResolver());
                transformer = transformerFactory.newTransformer((new StreamSource(xsltInputStream)));
            } finally {
                xsltInputStream.close();
            }
            Document data = parent.getContext().getDataModel().getData();
            transformer.transform(new DOMSource(data), new DOMResult(targerDoc));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ActionHandlerException("Can't export the data to DBF", e);
        }
    }

    @Override
    public boolean rollback(Action parent, UpdateInfo updateInfo) throws ActionHandlerException {
        return false;
    }
}
