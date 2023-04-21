package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
public class CoShapePageItemSetColumnGridDerivedUndoCommand extends CoShapePageItemUndoCommand {

    protected boolean m_isDerived;

    protected CoImmutableColumnGridIF m_columnGrid;

    public CoShapePageItemSetColumnGridDerivedUndoCommand(String name, CoShapePageItemIF target) {
        super(name, target);
        m_isDerived = m_target.getColumnGrid().isDerived();
        if (m_isDerived) {
            m_columnGrid = null;
        } else {
            m_columnGrid = m_target.getColumnGrid();
        }
    }

    public boolean doRedo() {
        m_target.setDerivedColumnGrid(!m_isDerived);
        return true;
    }

    public boolean doUndo() {
        m_target.setDerivedColumnGrid(m_isDerived);
        if (m_columnGrid != null) {
            m_target.getMutableColumnGrid().set(m_columnGrid.getColumnCount(), m_columnGrid.getColumnSpacing(), m_columnGrid.getLeftMargin(), m_columnGrid.getTopMargin(), m_columnGrid.getRightMargin(), m_columnGrid.getBottomMargin());
        }
        return true;
    }
}
