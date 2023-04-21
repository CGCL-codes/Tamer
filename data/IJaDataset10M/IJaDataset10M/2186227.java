package jp.ekasi.pms.ui.grid.providor;

import java.util.ArrayList;
import java.util.Collection;
import jp.ekasi.pms.model.util.ModelAdapterFactory;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * @author sumari.rutao@gamil.com
 */
public class PmsAdapterFactoryContentProvider extends ModelAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {

    protected ComposedAdapterFactory parentAdapterFactory;

    protected IChangeNotifier changeNotifier = new ChangeNotifier();

    protected Collection<Object> supportedTypes = new ArrayList<Object>();

    /**
	 * �R���X�g���N�^.<br>
	 */
    public PmsAdapterFactoryContentProvider() {
        supportedTypes.add(ITreeItemContentProvider.class);
    }

    protected ProjectTreeContentPorvidor projectTreeContentPorvidor;

    @Override
    public Adapter createProjectAdapter() {
        if (projectTreeContentPorvidor == null) {
            projectTreeContentPorvidor = new ProjectTreeContentPorvidor(this);
        }
        return projectTreeContentPorvidor;
    }

    protected TaskTreeContentsProvidor taskTreeContentsProvidor;

    @Override
    public Adapter createTaskAdapter() {
        if (taskTreeContentsProvidor == null) {
            taskTreeContentsProvidor = new TaskTreeContentsProvidor(this);
        }
        return taskTreeContentsProvidor;
    }

    @Override
    public boolean isFactoryForType(Object type) {
        return true;
    }

    public ComposeableAdapterFactory getRootAdapterFactory() {
        return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
    }

    public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
        this.parentAdapterFactory = parentAdapterFactory;
    }

    public void addListener(INotifyChangedListener notifyChangedListener) {
        changeNotifier.addListener(notifyChangedListener);
    }

    public void removeListener(INotifyChangedListener notifyChangedListener) {
        changeNotifier.removeListener(notifyChangedListener);
    }

    public void fireNotifyChanged(Notification notification) {
        changeNotifier.fireNotifyChanged(notification);
        if (parentAdapterFactory != null) {
            parentAdapterFactory.fireNotifyChanged(notification);
        }
    }

    public void dispose() {
        if (projectTreeContentPorvidor != null) projectTreeContentPorvidor.dispose();
        if (taskTreeContentsProvidor != null) taskTreeContentsProvidor.dispose();
    }
}
