package de.sonivis.tool.bibsonomyconnector.datamodel.extension.proxy;

import de.sonivis.tool.bibsonomyconnector.datamodel.extension.TagElement;
import de.sonivis.tool.bibsonomyconnector.datamodel.extension.TaggedBy;
import de.sonivis.tool.core.datamodel.ContentElement;
import de.sonivis.tool.core.datamodel.extension.proxy.IGroupedBy;
import de.sonivis.tool.core.datamodel.proxy.IContentElement;

/**
 * Proxy interface for {@link TaggedBy} entities.
 * 
 * @param <C>
 *            Sub-type of {@link IContentElement} representing a narrowed
 *            {@link ContentElement} entity.
 * @param <T>
 *            Sub-type of {@link ITagElement} representing a narrowed
 *            {@link TagElement} entity.
 * 
 * @author Andreas Erber
 * @version $Revision$, $Date$
 */
public interface ITaggedBy<C extends IContentElement, T extends ITagElement> extends IGroupedBy<C, T> {
}
