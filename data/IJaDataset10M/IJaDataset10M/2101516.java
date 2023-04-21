package de.wilanthaou.songbookcreator.ui.view.adapter;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import de.wilanthaou.songbookcreator.ui.view.event.SelectionChangedSupport;

/**
 * Helper class to wire a {@link JList} selection to
 * a {@link SelectionChangedSupport}.
 * @author Alexander Metzner
 * @version $Revision: 1.3 $
 *
 */
public final class JListToSelectionChangedAdapter {

    /**
	 * Wires a {@link SelectionChangedSupport} to notify registerd listeners on selection changes from a {@link JList}. 
	 * @param <T> the type of objects in the list
	 * @param list the {@link JList}
	 * @param support the {@link SelectionChangedSupport}
	 */
    @SuppressWarnings("unchecked")
    public static <T> void wireSelectionChangedSupport2JList(final JList list, final SelectionChangedSupport<T> support) {
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                Collection<T> c = new ArrayList<T>();
                c.add((T) list.getSelectedValue());
                support.fireSelectionChanged(c);
            }
        });
    }
}
