package org.nomadpim.core.ui.views;

import java.util.List;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PlatformUI;
import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.entity.IEntityContainer;
import org.nomadpim.core.entity.ISpace;
import org.nomadpim.core.entity.IType;
import org.nomadpim.core.util.event.IContainerChangeListener;
import org.nomadpim.core.util.event.PropertyChangeEvent;

public class TableContentProvider implements IStructuredContentProvider {

    private final class Refresher implements IContainerChangeListener<IEntity> {

        public void objectChanged(IEntity o, final PropertyChangeEvent event) {
            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                public void run() {
                    viewer.refresh(event.getSource());
                }
            });
        }

        public void objectAdded(IEntity o) {
            objects.add(o);
            refresh();
        }

        private void refresh() {
            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                public void run() {
                    viewer.refresh(false);
                }
            });
        }

        public void objectRemoved(IEntity o) {
            objects.remove(o);
            refresh();
        }
    }

    private StructuredViewer viewer;

    private IType type;

    private List<IEntity> objects;

    private Refresher refresher;

    private ISpace space;

    public TableContentProvider(ISpace space, IType type) {
        this.type = type;
        this.refresher = new Refresher();
        this.space = space;
        getContainer().addListener(refresher);
        objects = getContainer().get();
    }

    private IEntityContainer getContainer() {
        return space.getContainer(type);
    }

    public void dispose() {
        getContainer().removeListener(refresher);
    }

    public Object[] getElements(Object o) {
        return objects.toArray();
    }

    public void inputChanged(Viewer viewer, Object arg1, Object arg2) {
        this.viewer = (StructuredViewer) viewer;
    }
}
