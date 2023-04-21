package de.sonivis.tool.core.datamodel.extension.proxy;

import de.sonivis.tool.core.datamodel.extension.ProjectTalkPage;
import de.sonivis.tool.core.datamodel.extension.ProjectTalkPageLink;
import de.sonivis.tool.core.datamodel.extension.RevisionElement;

/**
 * Proxy interface for a {@link ProjectTalkPageLink} entity.
 * 
 * @param <REV>
 *            Sub-type of {@link IRevisionElement} representing a narrowed {@link RevisionElement}
 *            entity.
 * @param <P>
 *            Sub-type of {@link IProjectTalkPage} representing a narrowed {@link ProjectTalkPage}
 *            entity.
 * 
 * @author Andreas Erber
 * @version $Revision$, $Date$
 */
public interface IProjectTalkPageLink<REV extends IRevisionElement, P extends IProjectTalkPage> extends ITalkPageLink<REV, P> {
}
