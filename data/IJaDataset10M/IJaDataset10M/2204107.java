package net.sourceforge.eclipsetrader.news.actions;

import net.sourceforge.eclipsetrader.news.NewsPlugin;
import net.sourceforge.eclipsetrader.news.views.NewsView;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class RefreshFeedAction implements IWorkbenchWindowActionDelegate, IViewActionDelegate, IPropertyChangeListener {

    private IAction action;

    private NewsView view;

    public void init(IWorkbenchWindow window) {
        NewsPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
    }

    public void init(IViewPart view) {
        if (view instanceof NewsView) this.view = (NewsView) view;
    }

    public void dispose() {
        NewsPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
    }

    public void run(IAction action) {
        if (view != null && view.getSecurity() != null) NewsPlugin.getDefault().startFeedSnapshot(view.getSecurity()); else NewsPlugin.getDefault().startFeedSnapshot();
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.action = action;
        action.setEnabled(!NewsPlugin.getDefault().getPreferenceStore().getBoolean(NewsPlugin.FEED_RUNNING));
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getProperty().equals(NewsPlugin.FEED_RUNNING)) {
            if (action != null) action.setEnabled(!NewsPlugin.getDefault().getPreferenceStore().getBoolean(NewsPlugin.FEED_RUNNING));
        }
    }
}
