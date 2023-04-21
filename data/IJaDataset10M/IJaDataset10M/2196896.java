package org.targol.warfocdamanager.ui.editors.cdarunner;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.targol.warfocdamanager.core.model.cda.CdaSession;

/**
 * @author Targol lagadec
 */
public class CdaSumaryLabelProvider extends LabelProvider implements ITableLabelProvider {

    private final CdaSumaryTab parent;

    /**
	 * Constructor.
	 * 
	 * @param tab Sumary tab that owns this label provider.
	 */
    public CdaSumaryLabelProvider(final CdaSumaryTab tab) {
        this.parent = tab;
    }

    @Override
    public final Image getColumnImage(final Object element, final int columnIndex) {
        return null;
    }

    @Override
    public final String getColumnText(final Object element, final int columnIndex) {
        if (element instanceof CdaSession) {
            final CdaSession sess = (CdaSession) element;
            if (columnIndex == 0) {
                return sess.getName();
            }
            if (columnIndex == this.parent.getNbCols() - 1) {
                return new Float(sess.getProgress()).toString();
            }
            return new Float(sess.getProgressForStage(this.parent.getColNames()[columnIndex])).toString();
        }
        return null;
    }
}
