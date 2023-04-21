package net.entropysoft.dashboard.plugin.dashboard.dnd;

import net.entropysoft.dashboard.plugin.Activator;
import net.entropysoft.dashboard.plugin.dashboard.gef.parts.ChartEditPart;
import net.entropysoft.dashboard.plugin.dashboard.request.DashboardVariableValueFactory;
import net.entropysoft.dashboard.plugin.dashboard.request.DashboardXYChartFactory;
import net.entropysoft.dashboard.plugin.dnd.VariableTransfer;
import net.entropysoft.dashboard.plugin.variables.IVariable;
import net.entropysoft.dashboard.plugin.variables.IVariableDescription;
import net.entropysoft.dashboard.plugin.variables.TypeUtils;
import net.entropysoft.dashboard.plugin.variables.VariableManager;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Drop listener for IVariable
 * 
 * @author cedric
 */
public class VariableDropTargetListener extends AbstractTransferDropTargetListener {

    private DashboardXYChartFactory chartFactory = new DashboardXYChartFactory();

    private DashboardVariableValueFactory dashboardVariableValueFactory = new DashboardVariableValueFactory();

    public VariableDropTargetListener(EditPartViewer viewer) {
        super(viewer, VariableTransfer.getInstance());
    }

    protected Request createTargetRequest() {
        CreateRequest request = new CreateRequest();
        request.setFactory(dashboardVariableValueFactory);
        return request;
    }

    protected void handleEnteredEditPart() {
        if (getTargetEditPart() instanceof ChartEditPart) {
            CreateRequest createRequest = (CreateRequest) getTargetRequest();
            createRequest.setFactory(chartFactory);
        }
        super.handleEnteredEditPart();
    }

    protected void handleExitingEditPart() {
        super.handleExitingEditPart();
        CreateRequest createRequest = (CreateRequest) getTargetRequest();
        createRequest.setFactory(dashboardVariableValueFactory);
    }

    protected void updateTargetRequest() {
        CreateRequest createRequest = (CreateRequest) getTargetRequest();
        createRequest.setLocation(getDropLocation());
    }

    protected void handleDragOver() {
        getCurrentEvent().detail = DND.DROP_COPY;
        super.handleDragOver();
    }

    private void setFactoryFromMenu() {
        final CreateRequest createRequest = (CreateRequest) getTargetRequest();
        createRequest.setFactory(null);
        DropTargetEvent event = getCurrentEvent();
        Display display = Activator.getStandardDisplay();
        Menu menu = new Menu(display.getActiveShell(), SWT.POP_UP);
        MenuItem item = new MenuItem(menu, SWT.PUSH);
        item.setText(Messages.VariableDropTargetListener_InsertAsText);
        item.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                createRequest.setFactory(chartFactory);
            }
        });
        item = new MenuItem(menu, SWT.PUSH);
        item.setText(Messages.VariableDropTargetListener_InsertAsDynamicText);
        item.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                createRequest.setFactory(dashboardVariableValueFactory);
            }
        });
        menu.setLocation(event.x, event.y);
        menu.setVisible(true);
        while (!menu.isDisposed() && menu.isVisible()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        menu.dispose();
    }

    /**
	 * set the factory to use with the CreateRequest
	 * 
	 */
    private void setFactory() {
        IVariable variable = (IVariable) getCurrentEvent().data;
        chartFactory.setVariable(variable);
        dashboardVariableValueFactory.setVariable(variable);
        IVariableDescription variableDescription = VariableManager.getInstance().getVariableDescription(variable, false);
        boolean isChartable = TypeUtils.isNumericType(variableDescription.getTypeAsString());
        CreateRequest createRequest = (CreateRequest) getTargetRequest();
        if (!isChartable) {
            createRequest.setFactory(dashboardVariableValueFactory);
        } else if (getTargetEditPart() instanceof ChartEditPart) {
            createRequest.setFactory(chartFactory);
        } else {
            setFactoryFromMenu();
        }
    }

    protected void handleDrop() {
        updateTargetRequest();
        updateTargetEditPart();
        setFactory();
        super.handleDrop();
    }
}
