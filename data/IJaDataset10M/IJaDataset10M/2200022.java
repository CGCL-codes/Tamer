package org.torweg.pulse.component.core.contentregistry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.torweg.pulse.annotations.Action;
import org.torweg.pulse.annotations.Groups;
import org.torweg.pulse.annotations.Permission;
import org.torweg.pulse.annotations.RequireToken;
import org.torweg.pulse.bundle.Bundle;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.service.event.XSLTOutputEvent;
import org.torweg.pulse.service.request.ServiceRequest;
import org.torweg.pulse.site.content.ContentFolderNode;
import org.torweg.pulse.site.content.ContentNode;
import org.torweg.pulse.site.content.URLContent;
import org.torweg.pulse.site.content.admin.AbstractBasicContentEditor;
import org.torweg.pulse.site.content.admin.AbstractBasicContentEditorResult;
import org.torweg.pulse.util.adminui.JSONCommunicationUtils;
import org.torweg.pulse.util.adminui.RightsCheckUtils;
import org.torweg.pulse.util.entity.Node;

/**
 * the editor for a {@code URLContent} within the administration.
 * 
 * @author Daniel Dietz
 * @version $Revision: 1453 $
 */
public class URLContentEditor extends AbstractBasicContentEditor {

    /**
	 * returns the initialised {@code URLContentEditor} for a
	 * {@code URLContent} which is determined by a given id in the request.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 * 
	 * @return an AJAX-result: the initialised {@code URLContentEditor}
	 */
    @RequireToken
    @Action(value = "initURLContentEditor")
    @Permission("editURLContent")
    @Groups(values = { "CoreAdministrator" })
    public final AbstractBasicContentEditorResult initEditor(final Bundle bundle, final ServiceRequest request) {
        AbstractBasicContentEditorResult result = buildInitResult(request);
        XSLTOutputEvent event = new XSLTOutputEvent(getConfig().getAjaxXSL());
        event.setStopEvent(true);
        request.getEventManager().addEvent(event);
        return result;
    }

    /**
	 * Returns a result to initialise editors for the
	 * pulse-website-administration.
	 * 
	 * @param request
	 *            the current {@code ServiceRequest}
	 * 
	 * @return an {@code AbstractBasicContentEditorResult}
	 */
    private AbstractBasicContentEditorResult buildInitResult(final ServiceRequest request) {
        Long id = Long.parseLong(request.getCommand().getParameter("id").getFirstValue());
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        AbstractBasicContentEditorResult result = new AbstractBasicContentEditorResult();
        URLContent urlContent = null;
        try {
            urlContent = (URLContent) s.get(URLContent.class, id);
            result.setContent(urlContent);
            tx.rollback();
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("URLContentEditor.buildInitResult.failed: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
        return result;
    }

    /**
	 * saves the basic properties of a content (name, suffix, keywords).
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 */
    @RequireToken
    @Action(value = "saveURLContent", generate = true)
    @Permission("editURLContent")
    @Groups(values = { "CoreAdministrator" })
    public final void saveEditor(final Bundle bundle, final ServiceRequest request) {
        Long id = Long.valueOf(request.getCommand().getParameter("id").getFirstValue());
        String url = null;
        if (request.getCommand().getParameter("url") != null) {
            url = request.getCommand().getParameter("url").getFirstValue();
        }
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        JSONObject error = null;
        try {
            URLContent urlContent = (URLContent) s.get(URLContent.class, id);
            error = RightsCheckUtils.checkUserAgainstLocale(error, bundle, request, urlContent.getLocale());
            if (error == null) {
                error = AbstractBasicContentEditor.setBasics(urlContent, request, s);
            }
            if (error == null) {
                if (url != null) {
                    urlContent.setUrl(url);
                }
                ContentNode contentNode = (ContentNode) s.createCriteria(ContentNode.class).add(Restrictions.sqlRestriction("{alias}.content_id=" + urlContent.getId())).uniqueResult();
                if (!contentNode.getName().equals(urlContent.getName())) {
                    contentNode.setName(urlContent.getName());
                    s.saveOrUpdate(contentNode);
                }
                urlContent.setLastModifier(request.getUser());
                s.saveOrUpdate(urlContent);
                AbstractBasicContentEditor.initHibernateSearchFix(s, urlContent);
                tx.commit();
            }
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("URLContentEditor.saveURLContent.failed: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
        if (error == null) {
            JSONCommunicationUtils.jsonSuccessMessage(request);
        } else {
            JSONCommunicationUtils.jsonErrorMessage(request, error);
        }
    }

    /**
	 * creates a copy of a {@code URLContent} specified by parameter "id"
	 * in request in a folder specified by parameter "toid" in request using the
	 * locale of the folder.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 */
    @RequireToken
    @Action(value = "copyURLContent", generate = true)
    @Permission("editURLContent")
    @Groups(values = { "CoreAdministrator" })
    public final void copyURLContent(final Bundle bundle, final ServiceRequest request) {
        Long id = Long.valueOf(request.getCommand().getParameter("id").getFirstValue());
        Long toId = Long.valueOf(request.getCommand().getParameter("toid").getFirstValue());
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        JSONObject error = null;
        URLContent copy = null;
        JSONArray expandPath = new JSONArray();
        try {
            URLContent urlContent = (URLContent) s.get(URLContent.class, id);
            ContentFolderNode folder = (ContentFolderNode) s.get(ContentFolderNode.class, toId);
            error = RightsCheckUtils.checkUserAgainstLocale(error, bundle, request, folder.getLocale());
            if (error == null) {
                copy = urlContent.createCopy(folder.getLocale(), request.getUser());
                if (!copy.getBundle().getName().equals(folder.getBundle().getName())) {
                    copy.setBundle(folder.getBundle());
                }
                ContentNode contentNode = new ContentNode(copy, copy.getBundle());
                contentNode.setLocale(folder.getLocale());
                folder.addChild(contentNode);
                s.saveOrUpdate(copy);
                s.saveOrUpdate(contentNode);
                s.saveOrUpdate(folder);
                expandPath.add(contentNode.getId());
                Node parent = folder;
                while (parent != null) {
                    expandPath.add(0, parent.getId());
                    parent = parent.getParent();
                }
                AbstractBasicContentEditor.initHibernateSearchFix(s, copy);
                tx.commit();
            }
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("URLContentEditor.copyURLContent.failed: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
        if (error == null) {
            JSONCommunicationUtils.jsonSuccessMessage(request, "expandPath", expandPath);
        } else {
            JSONCommunicationUtils.jsonErrorMessage(request, error);
        }
    }
}
