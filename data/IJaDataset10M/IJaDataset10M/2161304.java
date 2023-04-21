package org.eaasyst.eaa.apps.de;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.apps.ListApplicationBase;
import org.eaasyst.eaa.data.DataConnector;
import org.eaasyst.eaa.data.impl.DocumentDabFactory;
import org.eaasyst.eaa.data.impl.DocumentUserDabFactory;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.syst.data.persistent.Document;
import org.eaasyst.eaa.syst.data.transients.ApplicationActionSpecification;
import org.eaasyst.eaa.syst.data.transients.ColumnSpecification;
import org.eaasyst.eaa.utils.StringUtils;

/**
 * <p>This application displays a list of document users.</p>
 * 
 * @version 2.5
 * @author Jeff Chilton
 */
public class DocumentUserList extends ListApplicationBase {

    /**
	 * <p>Constructs a new "DocumentUserList" object.</p>
	 * 
	 * @since Eaasy Street 2.5
	 */
    public DocumentUserList() {
        super();
        className = StringUtils.computeClassName(getClass());
        setApplTitleKey("title.document.user.list");
        setFormName("documentUserList");
        setInsertApplication("org.eaasyst.eaa.apps.de.DocumentUserEntry");
        setKeyFieldName("id");
        setKeyLength(0);
        setDynamic(true);
        setAccessBeanFactory(new DocumentUserDabFactory());
        List colSpecs = new ArrayList();
        ApplicationActionSpecification actionSpec = new ApplicationActionSpecification("Link", ApplicationActionSpecification.ACTION_TYPE_PUSH, "org.eaasyst.eaa.apps.de.DocumentUserEdit");
        ColumnSpecification colSpec = new ColumnSpecification("label.userId", false, ColumnSpecification.CONTENT_TYPE_DATA, "userId", actionSpec);
        colSpecs.add(colSpec);
        colSpec = new ColumnSpecification("label.name", false, ColumnSpecification.CONTENT_TYPE_DATA, "userName");
        colSpecs.add(colSpec);
        colSpec = new ColumnSpecification("label.authority", false, ColumnSpecification.CONTENT_TYPE_DATA, "displayAuthority");
        colSpecs.add(colSpec);
        actionSpec = new ApplicationActionSpecification("Link", ApplicationActionSpecification.ACTION_TYPE_PUSH, "org.eaasyst.eaa.apps.de.DocumentUserDelete");
        colSpec = new ColumnSpecification("label.remove", "center", false, ColumnSpecification.CONTENT_TYPE_ICON, "remove", actionSpec);
        colSpecs.add(colSpec);
        setColumnSpecifications(colSpecs);
    }

    /**
	 * <p>Called by the <code>Controller</code> whenever the application is
	 * invoked through either navigation or an application request.</p>
	 * 
	 * @param req
	 * the <code>HttpServletRequest</code> object
	 * @since Eaasy Street 2.5
	 */
    public void initialize(HttpServletRequest req) {
        EaasyStreet.logTrace("[In] DocumentUserList.initialize()");
        super.initialize(req);
        HttpSession ses = req.getSession();
        int index = getIndex(ses, true);
        Document doc = getDocument(req.getParameter(Constants.RPK_RECORD_KEY));
        ses.setAttribute("document", doc);
        ses.setAttribute(Constants.SAK_APPL_TITLE_ARGS + index, new String[] { doc.getTitle() });
        EaasyStreet.logTrace("[Out] DocumentUserList.initialize()");
    }

    /**
	 * <p>Returns the requested document.</p>
	 * 
	 * @param key
	 * the key to the requested document
	 * @return the requested document
	 * @since Eaasy Street 2.5
	 */
    private static Document getDocument(String key) {
        EaasyStreet.logTrace("[In] DocumentUserList.getDocument()");
        Document doc = new Document();
        DataConnector dc = new DataConnector(new DocumentDabFactory().getAccessBean("browse"));
        int keyValue = StringUtils.intValue(key);
        Map parameters = new HashMap();
        parameters.put(DataConnector.RECORD_KEY_PARAMETER, new Integer(keyValue));
        dc.setParameters(parameters);
        dc.setCommand(DataConnector.READ_COMMAND);
        dc.execute();
        if (dc.getResponseCode() == 0) {
            doc = (Document) dc.getExecutionResults();
        } else {
            EaasyStreet.logError("Error obtaining document; response code=" + dc.getResponseCode() + "; response=" + dc.getResponseString());
        }
        EaasyStreet.logTrace("[Out] DocumentUserList.getDocument()");
        return doc;
    }
}
