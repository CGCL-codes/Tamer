package org.jactr.eclipse.production.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.Viewer;
import org.jactr.tools.analysis.production.relationships.IRelationship;

public class PositiveFilter extends AbstractRelationshipFilter {

    /**
   * Logger definition
   */
    private static final transient Log LOGGER = LogFactory.getLog(PositiveFilter.class);

    public PositiveFilter() {
        setEnabled(true);
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (!_enabled) return true;
        if (!(element instanceof IRelationship)) return true;
        IRelationship rel = (IRelationship) element;
        return rel.getScore() <= 0;
    }
}
