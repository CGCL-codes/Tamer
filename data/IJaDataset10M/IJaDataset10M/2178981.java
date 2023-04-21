package org.nomadpim.core.internal.entity;

import java.util.List;
import org.nomadpim.core.util.event.IExtendedContainerChangeListener;

public interface IDataStore<T> extends IExtendedContainerChangeListener<T> {

    List<T> restore();
}
