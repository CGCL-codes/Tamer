package de.sonivis.tool.core.datamodel.extension.proxy;

import de.sonivis.tool.core.datamodel.extension.RevisionElement;
import de.sonivis.tool.core.datamodel.extension.Term;
import de.sonivis.tool.core.datamodel.extension.TermPartOfRevision;

/**
 * Proxy interface for a {@link TermPartOfRevision} entity.
 * 
 * @param <TERM>
 *            Sub-type of {@link ITerm} representing a narrowed {@link Term} entity.
 * @param <REV>
 *            Sub-type of {@link IRevisionElement} representing a narrowed {@link RevisionElement}
 *            entity.
 * @author Andreas Erber
 * @version $Revision: 1416 $, $Date: 2010-01-28 06:46:22 -0500 (Thu, 28 Jan 2010) $
 */
public interface ITermPartOfRevision<TERM extends ITerm, REV extends IRevisionElement> extends IPartOf<TERM, REV> {
}
