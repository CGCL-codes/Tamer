package com.google.gwt.user.client.ui;

/**
 * Implemented by {@link IndexedPanel}s that also allow insertions.
 */
public interface InsertPanel extends IndexedPanel {

    /**
   * Extends this interface with convenience methods to handle {@link IsWidget}.
   */
    interface ForIsWidget extends InsertPanel, IndexedPanel.ForIsWidget {

        void add(IsWidget w);

        void insert(IsWidget w, int beforeIndex);
    }

    /**
   * Adds a child widget to this panel.
   * 
   * @param w the child widget to be added
   */
    void add(Widget w);

    /**
   * Inserts a child widget before the specified index. If the widget is already
   * a child of this panel, it will be moved to the specified index.
   * 
   * @param w the child widget to be inserted
   * @param beforeIndex the index before which it will be inserted
   * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of
   *           range
   */
    void insert(Widget w, int beforeIndex);
}
