package skycastle.easyui;

/**
 * A view of a collection, such as a Collection class, or a javabean with properties.
 *
 * There can be different collection views, for example depending on number of elements in the collection,
 * and wether all the elements are the same type or not.
 *
 * @author Hans H�ggstr�m
 */
public interface CollectionView extends UiView {

    void addSelectionListener(final UiSelectionListener uiSelectionListener);
}
