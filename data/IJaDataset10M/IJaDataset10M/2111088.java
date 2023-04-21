package net.sf.rcpforms.widgetwrapper.wrapper.advanced.tree.check;

import java.util.ArrayList;
import net.sf.rcpforms.widgetwrapper.wrapper.advanced.tree.check.CheckableTreeNode.Checkstate;

public class CheckListenerList implements CheckListener {

    private ArrayList<CheckListener> m_listeners;

    private CheckListener[] m_arrayCache;

    public CheckListenerList() {
        super();
        m_listeners = new ArrayList<CheckListener>();
        m_arrayCache = null;
    }

    public void addCheckListener(final CheckListener listener) {
        m_arrayCache = null;
        m_listeners.add(listener);
    }

    public void removeCheckListener(final CheckListener o) {
        m_arrayCache = null;
        m_listeners.remove(o);
    }

    public void removeAllCheckListeners() {
        m_listeners.clear();
        m_arrayCache = null;
    }

    public void fireListeners(final CheckableTreeNode checkableTreeNode, final Checkstate newState, final Checkstate oldState) {
        if (m_arrayCache == null) {
            m_arrayCache = m_listeners.toArray(new CheckListener[m_listeners.size()]);
        }
        for (int index = 0; index < m_arrayCache.length; index++) {
            try {
                m_arrayCache[index].checkChanged(checkableTreeNode, newState, oldState);
            } catch (final Exception e) {
                e.printStackTrace();
            } catch (final Error e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void checkChanged(final CheckableTreeNode checkableTreeNode, final Checkstate newState, final Checkstate oldState) {
        fireListeners(checkableTreeNode, newState, oldState);
    }
}
