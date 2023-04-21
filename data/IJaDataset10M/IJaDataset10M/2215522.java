package org.torweg.pulse.component.store;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.annotations.Action;
import org.torweg.pulse.annotations.Permission;
import org.torweg.pulse.annotations.Action.Security;
import org.torweg.pulse.component.store.model.StoreContent;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.service.event.NotFoundEvent;
import org.torweg.pulse.service.request.Command;
import org.torweg.pulse.service.request.ServiceRequest;
import org.torweg.pulse.site.content.AbstractBasicContent;
import org.torweg.pulse.site.content.AbstractContentDisplayer;
import org.torweg.pulse.site.content.ContentGroup;
import org.torweg.pulse.site.content.ContentResult;
import org.torweg.pulse.site.map.SitemapNode;

/**
 * is used to display {@code StoreContent}s.
 * 
 * @author Thomas Weber
 * @version $Revision: 1546 $
 * @see StoreContent
 * @see ContentGroup
 */
public class StoreContentDisplayer extends AbstractContentDisplayer {

    /**
	 * the logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreContentDisplayer.class);

    /**
	 * loads a given {@code StoreContent}, either by the {@code Command}'s
	 * <em>sitemapID</em> (see: {@link Command#getSitemapID()}) or by the
	 * {@code <em>contentId</em>} {@code Parameter}, while the {@code
	 * <em>contentId</em>} {@code Parameter} takes precedence over the {@code
	 * Command}'s <em>sitemapId</em>.
	 * <p>
	 * If the <em>sitemapID</em> denotes a {@code SitemapNode} without a {@code
	 * View}, the first {@code CMSContent} in the ancestor axis is loaded
	 * instead.
	 * </p>
	 * 
	 * @param request
	 *            the current {@code ServiceRequest}
	 * @return a {@code StoreContentDisplayerResult} for the requested {@code
	 *         StoreContent}
	 * @see StoreContentDisplayerResult
	 */
    @Action(value = "displayStore", security = Security.NEVER)
    @Permission("displayStore")
    public final ContentResult displayStore(final ServiceRequest request) {
        Command command = request.getCommand();
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        try {
            AbstractBasicContent content = chooseContent(command, s);
            if (content == null) {
                LOGGER.warn("No content found for '{}'.", command);
                request.getEventManager().addEvent(new NotFoundEvent());
                return null;
            }
            if ((!content.getSuffix().equals(command.getSuffix())) || (command.getHttpParameter("contentId") != null)) {
                prepareRedirect(request, content);
                tx.commit();
                return null;
            }
            content.initLazyFields();
            ContentResult result;
            if (content instanceof StoreContent) {
                StoreContent sc = (StoreContent) content;
                result = new StoreContentDisplayerResult(sc, request);
            } else if (content instanceof ContentGroup) {
                ContentGroup scg = (ContentGroup) content;
                List<SitemapNode> children = getChildrenForContentGroup(request, s);
                result = new StoreContentDisplayerResult(scg, children, request);
            } else {
                tx.commit();
                return null;
            }
            tx.commit();
            return result;
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException(e);
        } finally {
            s.close();
        }
    }
}
