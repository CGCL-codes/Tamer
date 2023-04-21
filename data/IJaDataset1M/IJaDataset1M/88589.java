package de.sonivis.tool.core.datamodel.extension.proxy;

import de.sonivis.tool.core.datamodel.extension.RevisionElement;
import de.sonivis.tool.core.datamodel.extension.UserTalkPage;
import de.sonivis.tool.core.datamodel.extension.UserTalkPageLink;

/**
 * Proxy interface for a {@link UserTalkPageLink} entity.
 * 
 * @param <REV>
 *            Sub-type of {@link IRevisionElement} representing a narrowed {@link RevisionElement}
 *            entity.
 * @param <U>
 *            Sub-type of {@link IUserTalkPage} representing a narrowed {@link UserTalkPage} entity.
 * 
 * @author Andreas Erber
 * @version $Revision$, $Date$
 */
public interface IUserTalkPageLink<REV extends IRevisionElement, U extends IUserTalkPage> extends ITalkPageLink<REV, U> {
}
