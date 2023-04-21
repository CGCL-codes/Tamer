package fw4ex_client.exercise.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import fw4ex_client.data.ExercisePathItemSet;
import fw4ex_client.data.interfaces.IExercise;
import fw4ex_client.data.interfaces.IExercisePathItem;

public class ExerciseListContentProvider implements ITreeContentProvider, IStructuredContentProvider {

    private IExercisePathItem tree;

    private ExerciseListView view;

    public ExerciseListContentProvider(ExerciseListView v) {
        this.view = v;
        this.tree = new ExercisePathItemSet();
    }

    public IExercisePathItem getTree() {
        return tree;
    }

    public void setTree(IExercisePathItem tree) {
        this.tree = tree;
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement == null) {
            return null;
        }
        if (parentElement instanceof IExercise) {
            return new Object[0];
        }
        IExercisePathItem parent = (IExercisePathItem) parentElement;
        Object[] res = new Object[parent.size()];
        int j = 0;
        for (int i = 0; i < parent.size(); i++) {
            IExercisePathItem child = parent.get(i);
            if (child != null) {
                res[j] = parent.get(i);
                j++;
            }
        }
        return res;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof IExercisePathItem) {
            return tree.getParent((IExercisePathItem) element);
        }
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof IExercise) {
            return false;
        }
        if (element instanceof IExercisePathItem) {
            return ((IExercisePathItem) element).size() > 0;
        }
        return false;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement.equals(view.getViewSite())) {
            return getChildren(tree);
        }
        return getChildren(inputElement);
    }

    public void initialize() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
