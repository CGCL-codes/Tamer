package com.ochafik.util.listenable;

import java.util.List;

/**
 * Interface for lists that support modification listeners.
 * @author Olivier Chafik
 * @param <T> Type of the elements of the list
 */
public interface ListenableList<T> extends ListenableCollection<T>, List<T> {
}
