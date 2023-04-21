package org.ufacekit.ui.swing.core.controls;

import java.util.Collection;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.ufacekit.ui.core.UIFactory;
import org.ufacekit.ui.core.controls.UIComposite;
import org.ufacekit.ui.core.controls.UITree;
import org.ufacekit.ui.core.controls.UITable.TableBindingInfo;
import org.ufacekit.ui.core.controls.internal.InternalUITree;
import org.ufacekit.ui.core.form.UIForm;
import org.ufacekit.ui.swing.core.internal.SwingControl;
import org.ufacekit.ui.swing.databinding.swing.SwingObservables;
import org.ufacekit.ui.swing.jface.viewers.TreeViewer;
import org.ufacekit.ui.viewers.IElementComparer;
import org.ufacekit.ui.viewers.ISelection;
import org.ufacekit.ui.viewers.ISelectionChangedListener;
import org.ufacekit.ui.viewers.ViewerComparator;
import org.ufacekit.ui.viewers.ViewerFilter;
import org.ufacekit.ui.viewers.databinding.ObservableListContentProvider;
import org.ufacekit.ui.viewers.databinding.ObservableListTreeContentProvider;
import org.ufacekit.ui.viewers.databinding.TreeStructureAdvisor;
import org.ufacekit.ui.viewers.databinding.ViewersObservables;

/**
 * JFace implementation of {@link UITree}
 * 
 * @param <ModelElement>
 *            the model element of used shown in the viewer
 * @since 1.0
 * 
 */
public class SwingTree<ModelElement> extends SwingControl<JScrollPane, JTree> implements InternalUITree<ModelElement> {

    private Binding binding;

    private TreeViewer<ModelElement, Object> viewer;

    private boolean isSelectionBound;

    /**
	 * Create a new instance of {@link TreeViewer} and wrap it
	 * 
	 * @param factory
	 *            the factory the widget created with
	 * @param parent
	 *            the parent composite the control is create on
	 * @param uiInfo
	 *            info to layout and define the control
	 * @since 1.0
	 */
    public SwingTree(UIFactory<?> factory, UIComposite parent, UITree.TreeUIInfo<ModelElement> uiInfo) {
        this(factory, parent, new JTree(), uiInfo);
    }

    /**
	 * Wrap an existing {@link TreeViewer}
	 * 
	 * @param tree
	 *            the control to wrap
	 * @param uiInfo
	 *            info to layout and define the control
	 * @since 1.0
	 */
    private SwingTree(UIFactory<?> factory, UIComposite parent, JTree tree, final UITree.TreeUIInfo<ModelElement> uiInfo) {
        super(factory, parent, new JScrollPane(tree), tree, uiInfo);
        this.viewer = new TreeViewer<ModelElement, Object>(tree);
        this.viewer.setLabelProvider(uiInfo.getLabelProvider());
    }

    public void internalBind(UIForm form, DataBindingContext ctx, IObservable modelObservable, BindingInfo bindingInfo) {
        TreeBindingInfo info = (TreeBindingInfo) bindingInfo;
        this.isSelectionBound = info.getList() != null;
        IObservableFactory factory = info.getObservableFactory();
        TreeStructureAdvisor advisor = info.getStructureAdvisor();
        ObservableListTreeContentProvider<ModelElement, Object> cp = new ObservableListTreeContentProvider<ModelElement, Object>(factory, advisor);
        this.viewer.setContentProvider(cp);
        if (((TreeBindingInfo) bindingInfo).getList() != null) {
            if (binding != null) {
                internalUnbind();
            }
            this.viewer.setInput(((TableBindingInfo) bindingInfo).getList());
            binding = ctx.bindValue(ViewersObservables.observeSingleSelection(SwingObservables.getRealm(), this.viewer), (IObservableValue) modelObservable, null, null);
        } else {
            this.viewer.setInput(modelObservable);
        }
    }

    public void internalUnbind() {
        if (isSelectionBound) {
            if (binding != null && !binding.isDisposed()) {
                binding.dispose();
            }
        } else {
            ((ObservableListContentProvider<ModelElement, Object>) this.viewer.getContentProvider()).dispose();
        }
    }

    public IObservableValue getSelectionObservable() {
        return ViewersObservables.observeSingleSelection(SwingObservables.getRealm(), this.viewer);
    }

    /**
	 * @return the tree viewer wrapped
	 * @since 1.0
	 */
    TreeViewer<ModelElement, Object> getTreeViewer() {
        return this.viewer;
    }

    public void addSelectionChangedListener(ISelectionChangedListener<ModelElement> listener) {
        this.viewer.addSelectionChangedListener(listener);
    }

    public void removeSelectionChangedListener(ISelectionChangedListener<ModelElement> listener) {
        this.viewer.removeSelectionChangedListener(listener);
    }

    public IObservableValue getValidationStatus() {
        return binding.getValidationStatus();
    }

    public ISelection<ModelElement> getSelection() {
        return this.viewer.getSelection();
    }

    public void setSelection(ISelection<ModelElement> selection) {
        this.viewer.setSelection(selection);
    }

    public ViewerComparator<ModelElement> getComparator() {
        return this.viewer.getComparator();
    }

    public <T> IElementComparer<T> getComparer() {
        return this.viewer.getComparer();
    }

    public Collection<ViewerFilter<ModelElement>> getFilters() {
        return this.viewer.getFilters();
    }

    public void setFilters(Collection<ViewerFilter<ModelElement>> filters) {
        this.viewer.setFilters(filters);
    }

    public void setComparator(ViewerComparator<ModelElement> comparator) {
        this.viewer.setComparator(comparator);
    }

    public <T> void setComparer(IElementComparer<T> comparer) {
        this.viewer.setComparer(comparer);
    }

    public UIComposite getParent() {
        return (UIComposite) super.getParent();
    }

    public void update(Object element, int action) {
    }
}
