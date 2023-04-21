package net.sf.doolin.gui.view.support;

import net.sf.doolin.gui.view.GUIView;
import net.sf.doolin.gui.view.GUIViewListener;

/**
 * Adapter for a {@link GUIViewListener} whose all methods do nothing.
 * 
 * @author Damien Coraboeuf
 * 
 * 
 * @param <V>
 *            Type of object for the view
 */
public class GUIViewAdapter<V> implements GUIViewListener<V> {

    @Override
    public void onViewClosed(GUIView<V> view) {
    }

    @Override
    public void onViewInit(GUIView<V> view) {
    }

    @Override
    public void onViewShown(GUIView<V> view) {
    }
}
