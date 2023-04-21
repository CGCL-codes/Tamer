package at.rc.tacos.client.ui.sorterAndTooltip;

import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;
import at.rc.tacos.platform.model.VehicleDetail;

/**
 * This shows the tooltip for a vehicle
 * @author Birgit
 */
public class VehicleTooltip extends ToolTip {

    private VehicleDetail vehicleDetail;

    /**
	 * Creates a new tooltip for the vehicle view
	 * @param control the control for the tooltip to show
	 */
    public VehicleTooltip(Control control) {
        super(control);
        setShift(new Point(1, 1));
    }

    /**
	 * Returns whether or not the tooltip should be created.
	 * @param event the triggered event
	 * @return true if the tooltip should be created
	 */
    @Override
    protected boolean shouldCreateToolTip(Event event) {
        Widget hoverWidget = getTipWidget(event);
        vehicleDetail = getTaskListElement(hoverWidget);
        if (vehicleDetail != null) return true;
        return false;
    }

    @Override
    protected Composite createToolTipContentArea(Event event, Composite parent) {
        Composite composite = createToolTipContentAreaComposite(parent);
        String text = vehicleDetail.getVehicleName();
        addTitleAndLabel(composite, "Fahrzeug: ", text);
        text = vehicleDetail.getVehicleType();
        addTitleAndLabel(composite, "Fahrzeugtyp: ", text);
        if (vehicleDetail.getVehicleNotes() != null) if (!vehicleDetail.getVehicleNotes().equals("")) {
            text = vehicleDetail.getVehicleNotes();
            addTitleAndLabel(composite, "Notizen: ", text);
        }
        text = "";
        if (vehicleDetail.getDriver() != null) text = vehicleDetail.getDriver().getLastName() + " " + vehicleDetail.getDriver().getFirstName();
        if (vehicleDetail.getFirstParamedic() != null) text = text + vehicleDetail.getFirstParamedic().getLastName() + " " + vehicleDetail.getFirstParamedic().getFirstName();
        if (vehicleDetail.getSecondParamedic() != null) text = text + vehicleDetail.getSecondParamedic().getLastName() + " " + vehicleDetail.getSecondParamedic().getFirstName();
        if (!text.equalsIgnoreCase("")) addTitleAndLabel(composite, "Besatzung: ", text);
        if (vehicleDetail.isReadyForAction()) text = "ja"; else text = "nein";
        addTitleAndLabel(composite, "Einsatzbereit: ", text);
        if (vehicleDetail.isOutOfOrder()) text = "ja"; else text = "nein";
        addTitleAndLabel(composite, "Au�er Dienst: ", text);
        text = vehicleDetail.getBasicStation().getLocationName();
        addTitleAndLabel(composite, "Basisdienststelle: ", text);
        text = vehicleDetail.getCurrentStation().getLocationName();
        addTitleAndLabel(composite, "Aktuelle Dienststelle: ", text);
        if (vehicleDetail.getMobilePhone() != null) {
            text = vehicleDetail.getMobilePhone().getMobilePhoneName();
            addTitleAndLabel(composite, "Handy: ", text);
        }
        if (vehicleDetail.getLastDestinationFree() != null) if (!vehicleDetail.getLastDestinationFree().equalsIgnoreCase("")) {
            text = vehicleDetail.getLastDestinationFree();
            addTitleAndLabel(composite, "Letztes Ziel frei: ", text);
        }
        return composite;
    }

    protected void addTitleAndLabel(Composite parent, String titel, String text) {
        if (text.trim().isEmpty()) return;
        Label titelLabel = new Label(parent, SWT.NONE);
        titelLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        titelLabel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        titelLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
        titelLabel.setText(titel);
        Label textLabel = new Label(parent, SWT.NONE);
        textLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        textLabel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        textLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
        textLabel.setText(text);
    }

    /**
	 * Creates the tooltip content area for the tooltip
	 * @param parent the parent window
	 * @return the created composite
	 */
    protected Composite createToolTipContentAreaComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 5;
        gridLayout.marginHeight = 2;
        composite.setLayout(gridLayout);
        composite.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        return composite;
    }

    /**
	 * Returns the widget source for this tooltip
	 * @param event the event triggered
	 * @return the source widget
	 */
    protected Widget getTipWidget(Event event) {
        Point widgetPosition = new Point(event.x, event.y);
        Widget widget = event.widget;
        if (widget instanceof Table) {
            Table w = (Table) widget;
            return w.getItem(widgetPosition);
        }
        return widget;
    }

    /**
	 * Returns the element for this tooltip
	 * @param hoverObject the object under hover
	 * @return the element under the hover
	 */
    private VehicleDetail getTaskListElement(Object hoverObject) {
        if (hoverObject instanceof Widget) {
            Object data = ((Widget) hoverObject).getData();
            if (data != null) {
                return (VehicleDetail) data;
            }
        }
        return null;
    }

    /**
	 * Hides the tooltip window
	 */
    public void dispose() {
        hide();
    }
}
