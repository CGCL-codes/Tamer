package com.germinus.xpression.content_editor.action;

import com.germinus.xpression.cms.lucene.ScribeContentUrl;
import com.germinus.xpression.cms.util.ManagerRegistry;
import com.germinus.xpression.groupware.util.LiferayHelperFactory;
import com.liferay.portal.struts.PortletAction;
import com.liferay.util.servlet.ServletResponseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.w3c.dom.Document;
import javax.jcr.RepositoryException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class GetLomMetadataAction extends PortletAction {

    private static final Log log = LogFactory.getLog(GetLomMetadataAction.class);

    private static final String LOM_PATH = "manifest/metadata/lom";

    private static final boolean _CHECK_METHOD_ON_PROCESS_ACTION = false;

    protected boolean isCheckMethodOnProcessAction() {
        return _CHECK_METHOD_ON_PROCESS_ACTION;
    }

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        DynaActionForm actionForm = (DynaActionForm) form;
        log.debug("Processing get lom metadata action for contentId: " + actionForm.getString("contentId"));
        HttpServletResponse response = LiferayHelperFactory.getLiferayHelper().convertToHttpServletResponse(res);
        String contentType = "text/xml;charset=UTF-8";
        String metadataXml = getMetadataXml(actionForm.getString("contentUrl"));
        ServletResponseUtil.sendFile(response, "metadata.xml", metadataXml.getBytes("UTF-8"), contentType);
        setForward(req, "/null.jsp");
    }

    @Override
    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        return null;
    }

    private String getMetadataXml(String contentUrl) throws RepositoryException, ParserConfigurationException, TransformerException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        ScribeContentUrl scribeContentUrl = new ScribeContentUrl(contentUrl);
        Document document = ManagerRegistry.getContentManager().exportXmlFromRepository(scribeContentUrl, LOM_PATH);
        return transform(document);
    }

    private static String transform(Document document) throws TransformerException {
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        StringWriter writer = new StringWriter();
        xformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }
}
