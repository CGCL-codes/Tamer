package org.jsresources.apps.filterdesign.filter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import org.jsresources.apps.jmvp.manager.RM;
import org.jsresources.apps.jmvp.model.ModelEvent;
import org.jsresources.apps.jmvp.presenter.Presenter;
import org.jsresources.apps.jmvp.view.swing.JInternalFrameView;
import org.jsresources.apps.filterdesign.Debug;

public class FilterInternalFrameView extends JInternalFrameView implements PropertyChangeListener {

    private static final String sm_sUnnamedTitle = RM.getResourceString("Filter.UnnamedTitle");

    public FilterInternalFrameView(Presenter presenter) {
        super(presenter);
        presenter.addPropertyChangeListener(this);
        setTitle("---");
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        display();
    }

    public FilterInternalFramePresenter getFilterInternalFramePresenter() {
        return (FilterInternalFramePresenter) getPresenter();
    }

    private void display() {
        displayTitle();
    }

    private void displayTitle() {
        FilterModel filter = null;
        filter = (FilterModel) getPresenter().getModel();
        if (filter != null) {
            File file = filter.getFile();
            String sTitle = null;
            if (file != null) {
                sTitle = file.getPath();
            } else {
                sTitle = sm_sUnnamedTitle;
            }
            setTitle(sTitle + (filter.isModified() ? " *" : ""));
        }
    }

    public void modelChanged(ModelEvent event) {
        if (Debug.getTraceDataNotification()) {
            Debug.out("FilterInternalFrameView.modelChanged(): called with " + event);
        }
        displayTitle();
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(Presenter.MODEL_PROPERTY)) {
            displayTitle();
        }
    }
}
