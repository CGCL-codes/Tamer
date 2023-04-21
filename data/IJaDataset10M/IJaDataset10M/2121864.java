package org.bionote.om.dao;

import org.bionote.om.IPage;
import org.bionote.om.IRevision;

/**
 * @author mbreese
 *
 */
public interface RevisionDAO {

    public abstract IRevision findRevision(final IPage page, final int id);

    public abstract void save(IRevision pageRevision);
}
