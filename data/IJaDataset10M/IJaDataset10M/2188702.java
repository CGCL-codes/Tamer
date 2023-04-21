package net.sf.kerner.utils.collections.list;

import java.util.List;
import net.sf.kerner.utils.collections.CollectionView;
import net.sf.kerner.utils.collections.Filter;

public interface ListView<T> extends CollectionView<T>, List<T> {

    ListView<T> getView(Filter<T> filter);

    ListView<T> getView(ListFilter<T> filter);
}
