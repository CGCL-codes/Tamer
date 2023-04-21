package org.jqc;

import java.util.Collection;
import org.apache.commons.logging.Log;
import org.jqc.QcCustomization.TABLE_NAME;
import org.jqc.comwrapper.ObjectWrapper;
import org.jqc.comwrapper.VarWrapper;
import org.jqc.comwrapper.WrappedComException;
import org.qctools4j.exception.QcException;
import org.qctools4j.utils.LoggerFactory;

public abstract class AbstractQcFactory<I extends AbstractQcItem> extends AbstractQcFilteredCollection<Integer, I> {

    private static final Log LOG = LoggerFactory.getLog(AbstractQcFactory.class);

    private final QcCustomization customization;

    private final QcCustomizationFields customFields;

    protected AbstractQcFactory(final QcProjectConnectedSession context, final ObjectWrapper objectWrapper) {
        super(context, objectWrapper);
        customization = context.getCustomization();
        customFields = customization.getFields();
    }

    public Iterable<QcCustomizationField> getFields() {
        return customFields.getFields(getTableName());
    }

    public QcCustomizationField getCustomizationField(final String fldName) {
        return customFields.getField(getTableName(), fldName);
    }

    protected abstract TABLE_NAME getTableName();

    public I create() throws QcException {
        LOG.debug("creating item");
        final ObjectWrapper invoke = addItem(new VarWrapper());
        return createWrapper(invoke);
    }

    protected ObjectWrapper addItem(final VarWrapper wrapper) {
        return collectionContainer.invoke("addItem", wrapper).getValue();
    }

    public interface ItemOperation<I> {

        public void use(I item) throws QcException;
    }

    public void useItem(final Integer key, final ItemOperation<I> op) throws QcException {
        try {
            final I item = getItem(key);
            item.lock();
            op.use(item);
            item.post();
        } catch (final WrappedComException e) {
            throw new QcException(e);
        }
    }

    @Override
    public Collection<I> getItems(final Collection<Integer> p) {
        return super.getItems(p);
    }

    @Override
    public I getItem(final Integer key) {
        LOG.debug("fetching item with id " + key.toString());
        final ObjectWrapper invoke = collectionContainer.invoke("item", key).getValue();
        return createWrapper(invoke);
    }

    public void removeItem(final int id) {
        collectionContainer.invoke("removeItem", id);
    }

    public boolean remove(final I t) {
        if (!t.isNew()) {
            removeItem(t.getId());
            return true;
        } else {
            LOG.warn("tried to remove a new item");
            return false;
        }
    }
}
