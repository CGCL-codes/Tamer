package org.designerator.common.interfaces;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

public interface GuiProvider {

    public void createControl(Composite parent, final IProcessListener messageListener);
}
