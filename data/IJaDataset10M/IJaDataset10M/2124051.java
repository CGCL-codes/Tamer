package net.sf.raptor.ui.dnd;

import org.eclipse.jface.viewers.ISelection;

public interface DropTargetInterpreter {

    public void setData(ISelection itemsToDrop, Object target);
}
