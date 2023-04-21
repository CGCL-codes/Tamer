package net.sf.rcpforms.widgetwrapper.wrapper.advanced.tree;

import net.sf.rcpforms.widgetwrapper.wrapper.advanced.event.IRCPTreeInputChangedListener;
import org.eclipse.jface.viewers.TreeViewer;

public interface IRCPAdvancedTreeViewer {

    public TreeViewer asJFaceViewer();

    public void addTreeInputChangedListener(final IRCPTreeInputChangedListener listener);

    public IRCPTreeInputChangedListener[] getTreeInputChangedListeners();

    public void removeTreeInputChangedListener(final IRCPTreeInputChangedListener listener);
}
