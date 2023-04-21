package de.sonivis.tool.core.datamodel.extension.proxy;

import de.sonivis.tool.core.datamodel.extension.RevisionElement;
import de.sonivis.tool.core.datamodel.extension.TemplateTalkPage;
import de.sonivis.tool.core.datamodel.extension.TemplateTalkPageLink;

/**
 * Proxy interface for {@link TemplateTalkPageLink} entities.
 * 
 * @param <REV>
 *            Sub-type of {@link IRevisionElement} representing a narrowed {@link RevisionElement}
 *            entity.
 * @param <T>
 *            Sub-type of {@link ITemplateTalkPage} representing a narrowed {@link TemplateTalkPage}
 *            entity.
 * 
 * @author Andreas Erber
 * @version $Revision$, $Date$
 */
public interface ITemplateTalkPageLink<REV extends IRevisionElement, T extends ITemplateTalkPage> extends ITalkPageLink<REV, T> {
}
