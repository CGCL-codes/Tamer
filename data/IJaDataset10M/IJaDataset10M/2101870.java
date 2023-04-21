package gumbo.core.struct;

import gumbo.core.bean.ExposableBean;
import gumbo.core.bean.ExposerBean;
import gumbo.core.bean.UpdateBean;
import gumbo.core.bean.UpdateEvent;
import gumbo.core.bean.annotation.Exposes;
import gumbo.core.bean.annotation.Mutates;
import gumbo.core.bean.annotation.Observes;
import gumbo.core.bean.impl.ExposerBeanImpl;
import gumbo.core.bean.impl.UpdateBeanImpl;
import gumbo.core.util.CoreUtils;
import gumbo.core.util.AssertUtils;
import gumbo.core.util.EventPhase;
import gumbo.core.util.EventPhase.Describe;
import gumbo.core.util.EventPhase.Prescribe;
import java.beans.PropertyChangeListener;
import java.util.Collection;

/**
 * Wraps a Collection as an UpdateBean. In general, exposers and views are
 * UpdateBeans. Default equality is based on identity (i.e. equality is
 * undefined for a Collection). Specific subclass types (Set, List) must
 * override equality so that it is based on state.
 * @param <E> Collection element type.
 */
public class CollectionBean<E> extends CollectionWrapper<E> implements ExposerBean {

    /**
	 * Creates an instance.
	 * @param target Shared exposed wrapper target. Never null.
	 */
    public CollectionBean(Collection<E> target) {
        super(target);
        if (target instanceof UpdateBean) {
            _beanHelper = (UpdateBean) target;
        } else {
            _beanHelper = new UpdateBeanImpl.IdentityEquality();
        }
    }

    /**
	 * Called by subclass mutators before an update. If there are host or
	 * exposed update listeners an UpdateEvent is fired. Otherwise nothing
	 * happens.
	 */
    protected void doBeforeUpdate() {
        _exposerHelper.doBeforeUpdate();
    }

    /**
	 * Called by subclass mutators after an update. If there are host or exposed
	 * update listeners an UpdateEvent is fired. Otherwise nothing happens.
	 */
    protected void doAfterUpdate() {
        _exposerHelper.doAfterUpdate();
    }

    @Override
    protected CollectionBean<E> getWrapperTarget() {
        return (CollectionBean<E>) super.getWrapperTarget();
    }

    @Exposes(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public IteratorBean<E> iterator() {
        AssertUtils.assertNotDisposed(this);
        IteratorBean<E> exposer = new IteratorBean<E>(super.iterator());
        addExposedBean(exposer);
        return exposer;
    }

    @Mutates(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public boolean add(E element) {
        AssertUtils.assertNotDisposed(this);
        doBeforeUpdate();
        boolean changed = super.add(element);
        if (changed) {
            doAfterUpdate();
        }
        return changed;
    }

    @Mutates(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public boolean addAll(Collection<? extends E> c) {
        AssertUtils.assertNotDisposed(this);
        doBeforeUpdate();
        boolean changed = super.addAll(c);
        if (changed) {
            doAfterUpdate();
        }
        return changed;
    }

    @Mutates(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public void clear() {
        AssertUtils.assertNotDisposed(this);
        doBeforeUpdate();
        boolean changed = !isEmpty();
        clear();
        if (changed) {
            doAfterUpdate();
        }
    }

    @Mutates(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public boolean remove(Object o) {
        AssertUtils.assertNotDisposed(this);
        doBeforeUpdate();
        boolean changed = super.remove(o);
        if (changed) {
            doAfterUpdate();
        }
        return changed;
    }

    @Mutates(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public boolean removeAll(Collection<?> c) {
        AssertUtils.assertNotDisposed(this);
        doBeforeUpdate();
        boolean changed = super.removeAll(c);
        if (changed) {
            doAfterUpdate();
        }
        return changed;
    }

    @Mutates(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public boolean retainAll(Collection<?> c) {
        AssertUtils.assertNotDisposed(this);
        doBeforeUpdate();
        boolean changed = super.retainAll(c);
        if (changed) {
            doAfterUpdate();
        }
        return changed;
    }

    @Observes(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public boolean contains(Object elem) {
        return super.contains(elem);
    }

    @Observes(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public boolean containsAll(Collection<?> c) {
        return super.containsAll(c);
    }

    @Observes(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Observes(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public int size() {
        return super.size();
    }

    @Observes(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public Object[] toArray() {
        return super.toArray();
    }

    @Observes(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public <T> T[] toArray(T[] a) {
        return super.toArray(a);
    }

    @Override
    public void addExposedBean(ExposableBean exposed) {
        _exposerHelper.addExposedBean(exposed);
    }

    @Override
    public void removeExposedBean(Object exposed) {
        _exposerHelper.removeExposedBean(exposed);
    }

    @Override
    public void fireExposedUpdateEvent(Describe phase) {
        _exposerHelper.fireExposedUpdateEvent(phase);
    }

    @Override
    public boolean hasExposedBeanListeners(Prescribe phase, String propertyId) {
        return _exposerHelper.hasExposedBeanListeners(phase, propertyId);
    }

    @Override
    public UpdateEvent getExposedUpdateEvent(Describe phase) {
        return _exposerHelper.getExposedUpdateEvent(phase);
    }

    @Override
    public void addBeanListener(PropertyChangeListener listener, EventPhase.Prescribe phase, String propertyId) {
        AssertUtils.assertNotDisposed(this);
        _beanHelper.addBeanListener(listener, phase, propertyId);
    }

    @Override
    public void fireUpdateEvent(UpdateEvent event) {
        AssertUtils.assertNotDisposed(this);
        _beanHelper.fireUpdateEvent(event);
    }

    @Override
    public boolean hasBeanListeners() {
        AssertUtils.assertNotDisposed(this);
        return _beanHelper.hasBeanListeners();
    }

    @Override
    public boolean hasBeanListeners(EventPhase.Prescribe phase, String propertyId) {
        AssertUtils.assertNotDisposed(this);
        return _beanHelper.hasBeanListeners(phase, propertyId);
    }

    @Override
    public void removeBeanListener(PropertyChangeListener listener, EventPhase.Prescribe phase, String propertyId) {
        AssertUtils.assertNotDisposed(this);
        _beanHelper.removeBeanListener(listener, phase, propertyId);
    }

    @Override
    public void removeBeanListener(PropertyChangeListener listener) {
        AssertUtils.assertNotDisposed(this);
        _beanHelper.removeBeanListener(listener);
    }

    @Observes(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Observes(props = CollectionBean.BEAN_COLLECTION)
    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    protected void implDispose() {
        super.implDispose();
        _exposerHelper = CoreUtils.dispose(_exposerHelper);
        if (_beanHelper != getWrapperTarget()) {
            CoreUtils.dispose(_beanHelper);
        }
        _beanHelper = null;
    }

    private static final long serialVersionUID = 1L;

    private UpdateBean _beanHelper;

    private ExposerBeanImpl _exposerHelper = new ExposerBeanImpl.IdentityEquality(BEAN_COLLECTION, this);

    /**
	 * Bean property ID for updates to the collection membership (additions,
	 * removals). A "before" event is always sent. An "after" event is ONLY sent
	 * if the collection changes (e.g. mutator returns "true"). Does not reflect
	 * updates to the member objects themselves. Unless otherwise noted, the
	 * before and after event type is UpdateEvent.
	 */
    public static final String BEAN_COLLECTION = "Collection";
}
