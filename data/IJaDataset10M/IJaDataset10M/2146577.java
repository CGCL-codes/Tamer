package peakmlviewer.view;

import peakmlviewer.*;
import org.eclipse.swt.widgets.*;

/**
 * 
 */
public abstract class View extends Composite {

    public View(MainWnd mainwnd, Composite parent, int style) {
        super(parent, style);
        this.mainwnd = mainwnd;
    }

    public abstract void update(int event);

    public MainWnd getMainWnd() {
        return mainwnd;
    }

    protected MainWnd mainwnd;
}
