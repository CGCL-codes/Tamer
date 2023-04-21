package net.sf.timelogng.ui.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import net.sf.timelogng.model.Client;
import net.sf.timelogng.ui.model.Model;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class ClientView extends AbstractView {

    public static final String ID = "net.sf.timelogng.ui.view.ClientView";

    private TableViewer viewer;

    private PropertyChangeListener clientsPropertyChangeListener = new ClientsPropertyChangeListener();

    private PropertyChangeListener timesheetPropertyChangeListener = new TimesheetPropertyChangeListener();

    @Override
    protected Viewer doCreatePartControl(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setLabelProvider(new WorkbenchLabelProvider());
        Model.getInstance().addPropertyChangeListener(Model.CLIENTS, clientsPropertyChangeListener);
        Model.getInstance().addPropertyChangeListener(Model.TIMESHEET, timesheetPropertyChangeListener);
        setInput(Model.getInstance().getClients());
        createSelectionProvider();
        addSelectionListeners();
        return viewer;
    }

    private void createSelectionProvider() {
        getSite().setSelectionProvider(viewer);
    }

    private void addSelectionListeners() {
        getSite().getPage().addSelectionListener(ClientSelectionHolder.getInstance());
    }

    @Override
    public void dispose() {
        Model.getInstance().removePropertyChangeListener(clientsPropertyChangeListener);
        Model.getInstance().removePropertyChangeListener(timesheetPropertyChangeListener);
        getSite().getPage().removeSelectionListener(ClientSelectionHolder.getInstance());
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    public void setInput(final Set<Client> clients) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                viewer.setInput(clients);
                viewer.refresh();
            }
        });
    }

    public void refresh() {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                viewer.refresh(false);
            }
        });
    }

    class ClientsPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent arg0) {
            refresh();
        }
    }

    class TimesheetPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent arg0) {
            setInput(Model.getInstance().getClients());
        }
    }
}
