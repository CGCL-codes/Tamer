package org.monet.modelling.ui.providers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.monet.modelling.ui.IPropertyCategory;
import org.monet.modelling.ui.IPropertyElement;
import org.monet.modelling.ui.UIElement;

public class TreeContentProvider implements ITreeContentProvider, PropertyChangeListener {

    private Viewer viewer;

    private IAdapterManager adapterManager;

    public static final Object[] EMPTY_ARRAY = new Object[0];

    public TreeContentProvider(IAdapterManager adapterManager) {
        this.adapterManager = adapterManager;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        IPropertyCategory propertyCategory = (IPropertyCategory) this.adapterManager.loadAdapter(inputElement, IPropertyCategory.class.getName());
        if (propertyCategory == null) return EMPTY_ARRAY;
        return propertyCategory.getChildren();
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        return getElements(parentElement);
    }

    @Override
    public Object getParent(Object element) {
        IPropertyElement propertyElement = (IPropertyElement) this.adapterManager.loadAdapter(element, IPropertyElement.class.getName());
        if (propertyElement == null) return null;
        return propertyElement.getParent();
    }

    @Override
    public boolean hasChildren(Object element) {
        return getChildren(element).length > 0;
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.viewer = viewer;
        IPropertyCategory oldPropertyCategory = (oldInput != null) ? (IPropertyCategory) this.adapterManager.loadAdapter(oldInput, IPropertyCategory.class.getName()) : null;
        IPropertyCategory newPropertyCategory = (newInput != null) ? (IPropertyCategory) this.adapterManager.loadAdapter(newInput, IPropertyCategory.class.getName()) : null;
        if (oldPropertyCategory != null) removeListenerFrom(oldPropertyCategory);
        if (newPropertyCategory != null) addListenerTo(newPropertyCategory);
    }

    @Override
    public void dispose() {
        Object object = this.viewer.getInput();
        IPropertyCategory propertyCategory = (IPropertyCategory) this.adapterManager.loadAdapter(object, IPropertyCategory.class.getName());
        if (propertyCategory != null) removeListenerFrom(propertyCategory);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Object source = event.getSource();
        if (source instanceof IPropertyElement) updatePropertyElement((IPropertyElement) source);
        if (source instanceof IPropertyCategory) updateCategoryElement((IPropertyCategory) source);
    }

    private void removeListenerFrom(IPropertyCategory propertyCategory) {
        for (Object object : propertyCategory.getChildren()) {
            UIElement uielement = (UIElement) this.adapterManager.loadAdapter(object, UIElement.class.getName());
            System.out.println("uielement: " + uielement.toString());
            uielement.removePropertyChangeListener(this);
        }
    }

    private void addListenerTo(IPropertyCategory propertyCategory) {
        propertyCategory.addPropertyChangeListener(this);
        for (Object object : propertyCategory.getChildren()) {
            UIElement uiElement = (UIElement) this.adapterManager.loadAdapter(object, UIElement.class.getName());
            uiElement.addPropertyChangeListener(this);
        }
    }

    private void updateCategoryElement(IPropertyCategory propertyCategory) {
        System.out.println("se actualiza un categoryElement");
    }

    private void updatePropertyElement(IPropertyElement propertyElement) {
        TreeViewer treeViewer = (TreeViewer) this.viewer;
        treeViewer.update(propertyElement.getAdaptable(), new String[] { "value" });
    }
}
