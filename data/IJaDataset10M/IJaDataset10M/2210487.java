package org.silicolife.gui;

import java.util.ArrayList;
import javax.swing.JToolBar;

public interface SLFrameListener<C> {

    public void slFrameCanExit(SLFrame frame, boolean canExit);

    public void slFrameToolBarVisible(SLFrame frame, JToolBar toolBar, boolean visible);

    public void slFrameSelectionChanged(SLFrame frame, ArrayList<C> selectionList);
}
