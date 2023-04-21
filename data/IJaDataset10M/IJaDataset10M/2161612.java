package org.gwt.advanced.client.datamodel;

/**
 * This is a lazy loadable hierarchical data model.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class LazyHierarchicalGridDataModel extends HierarchicalGridDataModel implements LazyLoadable {

    /**
     * Initializes the model with the preloaded piece of data set.
     *
     * @param data is a data set piece.
     */
    public LazyHierarchicalGridDataModel(Object[][] data) {
        super((Object[][]) null);
        final LazyHierarchicalGridDataModel source = this;
        setDelegate(new LazyGridDataModel(data) {

            protected void prepareEvent(EditableModelEvent event) {
                event.setSource(source);
            }
        });
    }

    /**
     * Creates a new instnace of this class and defines the handler.
     *
     * @param handler is a callback handler to be invoked on changes.
     */
    public LazyHierarchicalGridDataModel(DataModelCallbackHandler<Editable> handler) {
        super((Object[][]) null);
        final LazyHierarchicalGridDataModel source = this;
        setDelegate(new LazyGridDataModel(handler) {

            protected void prepareEvent(EditableModelEvent event) {
                event.setSource(source);
            }
        });
    }

    /** {@inheritDoc} */
    public void setTotalRowCount(int totalRowCount) {
        ((LazyLoadable) getDelegate()).setTotalRowCount(totalRowCount);
    }
}
