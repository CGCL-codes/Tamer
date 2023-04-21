package de.hpi.eworld.editor.routesandtrips;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QBoxLayout;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QBoxLayout.Direction;
import de.hpi.eworld.core.ModelManager;
import de.hpi.eworld.core.ResourceLoader;
import de.hpi.eworld.exporter.sumo.data.SumoManager;
import de.hpi.eworld.exporter.sumo.data.SumoRoute;
import de.hpi.eworld.exporter.sumo.data.SumoTrip;
import de.hpi.eworld.exporter.sumo.data.SumoVehicleType;
import de.hpi.eworld.model.db.data.Edge;
import de.hpi.eworld.networkview.NetworkView;
import de.hpi.eworld.networkview.model.ViewItem;
import de.hpi.eworld.networkview.model.WayItem;

/**
 * Provides a dialog, which enables the user to create trips in eWorld.
 * @author Nico Naumann
 */
public class CreateTripDialog extends QDialog {

    /**
	 * This signal is emitted, when the editing of the current trip is finished.
	 */
    public Signal1<SumoTrip> finishedSignal = new Signal1<SumoTrip>();

    /**
	 * A line edit to set the id for the current trip.
	 */
    private QLineEdit idText;

    /**
	 * A spinbox to set the departing time for the current trip.
	 */
    private QSpinBox depTimeSpin;

    /**
	 * A line edit to set the starting edge for the current trip.
	 */
    private QLineEdit fromEdgeEdit;

    /**
	 * A button, which can be used to select the starting edge.
	 */
    private QPushButton fromEdgeSelectButton;

    /**
	 * A line edit to set the ending edge for the current trip.
	 */
    private QLineEdit toEdgeEdit;

    /**
	 * A button, which can be used to select the ending edge.
	 */
    private QPushButton toEdgeSelectButton;

    /**
	 * A combo box to select the vehicle type for the current trip.
	 */
    private QComboBox typeCombo;

    /**
	 * A spinbox to select the duration for the current trip.
	 */
    private QSpinBox periodEdit;

    /**
	 * a spinbox to select the number of cars for the current trip.
	 */
    private QSpinBox repnoEdit;

    /**
	 * A reference to the network view.
	 */
    private NetworkView networkView;

    /**
	 * The current added trip.
	 */
    private SumoTrip currentTrip;

    /**
	 * This is used as a textedit pointer for the slot onNetworkViewItemClicked
	 */
    private QLineEdit targetLineEdit;

    /**
	 * Constructor
	 * @param parent The parent widget.
	 * @param nv A reference to the network view
	 */
    public CreateTripDialog(QWidget parent, NetworkView nv) {
        super(parent);
        this.setWindowTitle("Create new Trip");
        setWindowFlags(WindowType.Dialog);
        setWindowIcon(ResourceLoader.getInstance().createIconFromLocalResource(getClass().getClassLoader(), "route.png"));
        setModal(true);
        this.networkView = nv;
        QGridLayout resultLayout = new QGridLayout();
        QLabel tripLabel = new QLabel("Please enter the properties for the new trip:");
        QLabel idLabel = new QLabel("Id:");
        QLabel depTimeLabel = new QLabel("Start Time:");
        QLabel fromNodeLabel = new QLabel("From Edge:");
        QLabel toNodeLabel = new QLabel("To Edge:");
        QLabel vTypeLabel = new QLabel("Vehicle Type:");
        QLabel periodLabel = new QLabel("Emit interval:");
        QLabel repnoLabel = new QLabel("Replication number:");
        idText = new QLineEdit("Trip" + new Random().nextInt());
        depTimeSpin = new QSpinBox();
        ModelManager mm = ModelManager.getInstance();
        depTimeSpin.setMinimum(mm.getSimulationStartTime());
        depTimeSpin.setMaximum(mm.getSimulationEndTime());
        fromEdgeEdit = new QLineEdit();
        fromEdgeEdit.setEnabled(false);
        fromEdgeSelectButton = new QPushButton("Select");
        fromEdgeSelectButton.pressed.connect(this, "onFromNodeSelect()");
        toEdgeEdit = new QLineEdit();
        toEdgeEdit.setEnabled(false);
        toEdgeSelectButton = new QPushButton("Select");
        toEdgeSelectButton.pressed.connect(this, "onToNodeSelect()");
        typeCombo = new QComboBox();
        SumoManager sm = SumoManager.getInstance();
        List<SumoVehicleType> vTypes = sm.getVehicleTypes();
        for (SumoVehicleType v : vTypes) {
            typeCombo.addItem(v.getId());
        }
        periodEdit = new QSpinBox();
        periodEdit.setMinimum(1);
        periodEdit.setMaximum(100000);
        periodEdit.setValue(1000);
        repnoEdit = new QSpinBox();
        repnoEdit.setValue(5);
        repnoEdit.setMinimum(0);
        repnoEdit.setMaximum(10000);
        QPushButton bOk = new QPushButton("OK");
        bOk.pressed.connect(this, "onOkButton()");
        bOk.setDefault(true);
        QPushButton bCancel = new QPushButton("Cancel");
        bCancel.pressed.connect(this, "onCancelButton()");
        QBoxLayout okCancelLayout = new QBoxLayout(Direction.RightToLeft);
        okCancelLayout.addWidget(bCancel, 0, AlignmentFlag.AlignRight);
        okCancelLayout.addWidget(bOk, 1, AlignmentFlag.AlignRight);
        resultLayout.addWidget(tripLabel, 0, 0, 1, 3);
        resultLayout.addWidget(idLabel, 1, 0, 1, 1);
        resultLayout.addWidget(depTimeLabel, 2, 0, 1, 1);
        resultLayout.addWidget(fromNodeLabel, 3, 0, 1, 1);
        resultLayout.addWidget(toNodeLabel, 4, 0, 1, 1);
        resultLayout.addWidget(vTypeLabel, 5, 0, 1, 1);
        resultLayout.addWidget(periodLabel, 6, 0, 1, 1);
        resultLayout.addWidget(repnoLabel, 7, 0, 1, 1);
        resultLayout.addWidget(idText, 1, 1, 1, 2);
        resultLayout.addWidget(depTimeSpin, 2, 1, 1, 2);
        resultLayout.addWidget(fromEdgeEdit, 3, 1, 1, 1);
        resultLayout.addWidget(fromEdgeSelectButton, 3, 2, 1, 1);
        resultLayout.addWidget(toEdgeEdit, 4, 1, 1, 1);
        resultLayout.addWidget(toEdgeSelectButton, 4, 2, 1, 1);
        resultLayout.addWidget(typeCombo, 5, 1, 1, 2);
        resultLayout.addWidget(periodEdit, 6, 1, 1, 2);
        resultLayout.addWidget(repnoEdit, 7, 1, 1, 2);
        resultLayout.addLayout(okCancelLayout, 8, 0, 1, 3);
        resultLayout.setColumnStretch(0, 1);
        resultLayout.setColumnStretch(1, 5);
        resultLayout.setColumnStretch(2, 3);
        this.setLayout(resultLayout);
    }

    /**
	 * Constructor
	 * @param parent The parent widget 
	 * @param nv A reference to the network view
	 * @param trip The trip to edit.
	 */
    public CreateTripDialog(QWidget parent, NetworkView nv, SumoTrip trip) {
        this(parent, nv);
        this.idText.setText(trip.getId());
        this.depTimeSpin.setValue(trip.getDepTime());
        this.fromEdgeEdit.setText(trip.getFromEdgeId());
        this.toEdgeEdit.setText(trip.getToEdgeId());
        this.periodEdit.setValue(trip.getPeriod());
        this.repnoEdit.setValue(trip.getRepNumber());
        this.typeCombo.setCurrentIndex(typeCombo.findText(trip.getVType().getId()));
        currentTrip = trip;
        this.idText.setEnabled(false);
    }

    /**
	 * Updates the starting node text field.
	 */
    @SuppressWarnings("unused")
    private void onFromNodeSelect() {
        parentWidget().setVisible(false);
        this.setVisible(false);
        this.setEnabled(false);
        this.targetLineEdit = fromEdgeEdit;
        networkView.viewItemClicked.connect(this, "onViewItemClicked(ViewItem)");
    }

    /**
	 * Updates the ending node text field.
	 */
    @SuppressWarnings("unused")
    private void onToNodeSelect() {
        parentWidget().setVisible(false);
        this.setVisible(false);
        this.setEnabled(false);
        this.targetLineEdit = toEdgeEdit;
        networkView.viewItemClicked.connect(this, "onViewItemClicked(ViewItem)");
    }

    /**
	 * If a new trip is added, the signal finishedSignal is emitted with this trip.
	 * If a trip is edited, this trip will be updated.
	 */
    @SuppressWarnings("unused")
    private void onOkButton() {
        if (idText.text().equals("") || fromEdgeEdit.text().equals("") || toEdgeEdit.text().equals("")) {
            QMessageBox.warning(this, "Missing information", "Please fill out all neccessary fields (Id, From- and To-Edge)");
            return;
        }
        if (currentTrip == null) {
            SumoTrip result = new SumoTrip(depTimeSpin.value(), fromEdgeEdit.text(), idText.text(), toEdgeEdit.text());
            result.setPeriod(periodEdit.value());
            result.setRepNumber(repnoEdit.value());
            result.setVType(SumoManager.getInstance().getVehicleTypeById(typeCombo.currentText()));
            finishedSignal.emit(result);
            finishedSignal.disconnect();
        } else {
            currentTrip.setData(depTimeSpin.value(), fromEdgeEdit.text(), idText.text(), toEdgeEdit.text());
            currentTrip.setPeriod(periodEdit.value());
            currentTrip.setRepNumber(repnoEdit.value());
            currentTrip.setVType(SumoManager.getInstance().getVehicleTypeById(typeCombo.currentText()));
        }
        this.close();
        this.dispose();
    }

    /**
	 * Closes this dialog.
	 */
    @SuppressWarnings("unused")
    private void onCancelButton() {
        this.close();
    }

    /**
	 * Gets invoked, when a ViewItem is clicked in the network view.
	 * @param v The ViewItem, which was clicked.
	 */
    public void onViewItemClicked(ViewItem v) {
        if (!(v instanceof WayItem)) return;
        Edge e = ((WayItem) v).getPreviouslySelectedEdge();
        if (e != null) {
            targetLineEdit.setText(e.getInternalID());
            this.networkView.viewItemClicked.disconnect(this);
            targetLineEdit = null;
            this.setEnabled(true);
            this.setVisible(true);
            parentWidget().setVisible(true);
        }
    }

    public void onTypeButtonSelect() {
        System.out.println("Pressed Vehicle Add Button");
        AddVehicleDialog addVehicleDlg = new AddVehicleDialog(new ArrayList<SumoRoute>(), this);
        addVehicleDlg.show();
    }
}
