package net.cioppino.fita.user.views.trainingbook;

import net.cioppino.fita.core.model.ParentObject;
import net.cioppino.fita.core.model.Score;
import net.cioppino.fita.core.model.ScoreCard;
import net.cioppino.fita.core.model.Training;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class TrainingBookViewContentProvider implements ITreeContentProvider {

    private TreeViewer viewer;

    /**
	 * Stellt fest, ob ein Knoten Kindelemente hat
	 */
    public boolean hasChildren(Object element) {
        if (element instanceof ScoreCard) {
            return false;
        } else if (element instanceof ParentObject) return ((ParentObject) element).hasChildren();
        return false;
    }

    /**
	 * Liefert das Elternelement eines Knotens
	 */
    public Object getParent(Object element) {
        if (element instanceof Training) {
            return ((Training) element).getParent();
        } else if (element instanceof ScoreCard) {
            return ((ScoreCard) element).getParent();
        } else if (element instanceof Score) {
            return ((Score) element).getParent();
        }
        return null;
    }

    @Override
    public void dispose() {
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof ScoreCard) {
            return null;
        } else if (parentElement instanceof ParentObject) {
            return ((ParentObject) parentElement).getChildren();
        }
        return null;
    }

    @Override
    public Object[] getElements(Object parent) {
        if (parent instanceof ScoreCard) {
            return null;
        }
        return getChildren(parent);
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.viewer = (TreeViewer) viewer;
    }

    public TreeViewer getViewer() {
        return viewer;
    }
}
