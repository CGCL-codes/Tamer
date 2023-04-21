package org.rapla.mobile.android.activity;

import java.util.ArrayList;
import java.util.Arrays;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentFormater;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.framework.RaplaContextException;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.os.LoadAllocatablesAsyncTask;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;
import org.rapla.mobile.android.widget.adapter.AllocatableAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

/**
 * The allocatable details screen allows the user to book an allocatable of the
 * previously selected allocatable category. A booked allocatable can be
 * assigned to any number of appointments.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class AllocatableDetailsActivity extends BaseActivity {

    private ListView allocatableListView;

    private static final int DIALOG_CONFIRM_UNDO_BOOKING = 1;

    private static final int DIALOG_ASSIGN_APPOINTMENTS = 2;

    private AsyncTask<?, ?, ?> runningTask;

    public static final String INTENT_STRING_ALLOCATABLE_CATEGORY_ELEMENT_KEY = "element_key";

    private SelectedAllocatableActionHandler selectedAllocatableActionHandler = new SelectedAllocatableActionHandler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.allocatable_details_list);
        this.setTitle(R.string.titlebar_title_allocatable_list);
        this.allocatableListView = (ListView) findViewById(R.id.allocatable_details_list);
        this.allocatableListView.setEmptyView(findViewById(android.R.id.empty));
        this.allocatableListView.setOnItemClickListener(new AllocatableListItemClickedListener());
        this.registerForContextMenu(this.allocatableListView);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.runningTask != null) {
            this.runningTask.cancel(true);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.allocatable_details_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            this.selectedAllocatableActionHandler.handleListItem(info.position);
            menu.setHeaderTitle(R.string.options);
            if (this.selectedAllocatableActionHandler.isChecked()) {
                menu.add(Menu.NONE, R.string.option_delete, 1, R.string.options_undo_booking);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.string.option_delete) {
            this.showDialog(DIALOG_CONFIRM_UNDO_BOOKING);
        }
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case DIALOG_CONFIRM_UNDO_BOOKING:
                dialog = this.createDialogConfirmUndoBooking(builder);
                break;
            case DIALOG_ASSIGN_APPOINTMENTS:
                dialog = this.createDialogAssignAppointments(builder);
                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id) {
            case DIALOG_CONFIRM_UNDO_BOOKING:
                break;
            case DIALOG_ASSIGN_APPOINTMENTS:
                this.prepareDialogAssignAppointments(dialog);
                break;
        }
    }

    private void prepareDialogAssignAppointments(Dialog dialog) {
        ListView list = ((AlertDialog) dialog).getListView();
        list.clearChoices();
        Appointment[] allAppointments = this.getSelectedReservation().getAppointments();
        Appointment[] assignedAppointments = new Appointment[0];
        if (this.getSelectedReservation().hasAllocated(this.selectedAllocatableActionHandler.currentAllocatable)) {
            assignedAppointments = this.getSelectedReservation().getAppointmentsFor(this.selectedAllocatableActionHandler.currentAllocatable);
        }
        ArrayList<Appointment> assignedAppointmentsList = new ArrayList<Appointment>(Arrays.asList(assignedAppointments));
        for (int i = 0; i < allAppointments.length; i++) {
            Appointment appointment = allAppointments[i];
            if (assignedAppointmentsList.contains(appointment)) {
                list.setItemChecked(i, true);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.refreshListView();
    }

    /**
	 * Initially create dialog for confirming undoing a booking
	 * 
	 * @param Alert
	 *            Dialog Builder to build the dialog
	 * @return Composed dialog
	 */
    public AlertDialog createDialogConfirmUndoBooking(AlertDialog.Builder builder) {
        DialogInterface.OnClickListener listener = new AllocatableUndoBookingDialogListener();
        builder.setMessage(R.string.allocatable_confirm_undo_booking).setPositiveButton(R.string.yes, listener).setNegativeButton(R.string.cancel, listener);
        return builder.create();
    }

    /**
	 * Initially create dialog for assigning appointments
	 * 
	 * @param Alert
	 *            Dialog Builder to build the dialog
	 * @return Composed dialog
	 */
    public AlertDialog createDialogAssignAppointments(AlertDialog.Builder builder) {
        Appointment[] appointments = this.getSelectedReservation().getAppointments();
        CharSequence[] items = new CharSequence[appointments.length];
        for (int i = 0; i < appointments.length; i++) {
            try {
                items[i] = ((AppointmentFormater) this.getRaplaContext().lookup(AppointmentFormater.ROLE)).getSummary(appointments[i]);
            } catch (RaplaContextException e) {
                ExceptionDialogFactory.getInstance().create(this, R.string.exception_rapla_context_lookup, AllocatableListActivity.class).show();
            }
        }
        builder.setTitle(R.string.allocatable_assign_appointments_dialog_title).setPositiveButton(R.string.apply, new AllocatableAssignAppointmentsDialogListener()).setMultiChoiceItems(items, null, null);
        return builder.create();
    }

    /**
	 * Refresh list view by retrieving the latest data from the selected
	 * reservation
	 */
    public void refreshListView() {
        this.runningTask = new LoadAllocatablesAsyncTask(this, this.getSelectedReservation(), this.allocatableListView, this.getFacade(), this.getIntent().getStringExtra(INTENT_STRING_ALLOCATABLE_CATEGORY_ELEMENT_KEY), ExceptionDialogFactory.getInstance(), LoadDataProgressDialogFactory.getInstance(), AllocatableListActivity.class).execute();
    }

    /**
	 * The list adapter cannot be kept as an instance attribute as for some
	 * reason, as soon as passing the reference to the async task for loading
	 * allocatables, the reference gets lost. So always retrieve it on the fly
	 * from the list view.
	 * 
	 * @return Casted list adapter of list view
	 */
    public AllocatableAdapter getListAdapter() {
        return (AllocatableAdapter) this.allocatableListView.getAdapter();
    }

    public AllocatableListItemCheckboxListener createAllocatableListItemCheckboxListener(int listItemIndex) {
        return new AllocatableListItemCheckboxListener(listItemIndex);
    }

    /**
	 * AllocatableUndoBookingDialogListener
	 * 
	 * This class handles the dialog started from the context menu for undoing
	 * booking an allocatable
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
    public class AllocatableUndoBookingDialogListener implements DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                selectedAllocatableActionHandler.undoBooking();
                dialog.dismiss();
            } else {
                dialog.dismiss();
            }
        }
    }

    /**
	 * SelectedAllocatableActionHandler
	 * 
	 * This class handles all actions related to an allocatable selected from
	 * the list view.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
    public class SelectedAllocatableActionHandler {

        private Allocatable currentAllocatable;

        private int listItemIndex;

        public void handleListItem(int i) {
            this.resetAttributes();
            this.listItemIndex = i;
            this.currentAllocatable = this.getAllocatableByListItemIndex(this.listItemIndex);
        }

        private void resetAttributes() {
            this.currentAllocatable = null;
            this.listItemIndex = -1;
        }

        private Allocatable getAllocatableByListItemIndex(int listItemIndex) {
            return ((AllocatableAdapter) allocatableListView.getAdapter()).getItem(listItemIndex);
        }

        public void book() {
            getSelectedReservation().addAllocatable(this.currentAllocatable);
        }

        public void undoBooking() {
            this.uncheckCheckbox();
            getSelectedReservation().removeAllocatable(this.currentAllocatable);
            refreshListView();
        }

        private void uncheckCheckbox() {
            CheckBox checkbox = this.getCheckbox();
            checkbox.setChecked(false);
            checkbox.setEnabled(true);
        }

        public boolean isChecked() {
            return this.getCheckbox().isChecked();
        }

        private CheckBox getCheckbox() {
            View listItemView = allocatableListView.getChildAt(this.listItemIndex);
            CheckBox checkbox = (CheckBox) listItemView.findViewById(R.id.allocatable_details_list_item_checkbox);
            return checkbox;
        }
    }

    /**
	 * AllocatableListItemCheckboxListener
	 * 
	 * This class handles the checkbox logic of a list item
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
    public class AllocatableListItemCheckboxListener implements OnCheckedChangeListener {

        private int listItemIndex;

        public AllocatableListItemCheckboxListener(int listItemIndex) {
            this.listItemIndex = listItemIndex;
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                buttonView.setEnabled(false);
                selectedAllocatableActionHandler.handleListItem(this.listItemIndex);
                selectedAllocatableActionHandler.book();
                showDialog(DIALOG_ASSIGN_APPOINTMENTS);
            }
        }
    }

    /**
	 * AllocatableListItemClickedListener
	 * 
	 * This class handles list item clicks. If the checkbox of the list item is
	 * checked, the dialog for assigning appointments to this allocatable is
	 * being started. Otherwise, no action is executed.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
    public class AllocatableListItemClickedListener implements OnItemClickListener {

        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            CompoundButton checkBox = (CompoundButton) arg1.findViewById(R.id.allocatable_details_list_item_checkbox);
            if (checkBox.isChecked()) {
                selectedAllocatableActionHandler.handleListItem(position);
                showDialog(DIALOG_ASSIGN_APPOINTMENTS);
            }
        }
    }

    /**
	 * This class handles the dialog for assigning appointments to an
	 * allocatable. If no appointment is selected, the allocatable will be
	 * assigned to all appointments.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 */
    public class AllocatableAssignAppointmentsDialogListener implements OnClickListener {

        public void onClick(DialogInterface dialog, int which) {
            ListView list = ((AlertDialog) dialog).getListView();
            ReservationImpl reservation = getSelectedReservation();
            Appointment[] allAppointments = reservation.getAppointments();
            ArrayList<Appointment> restrictedAppointments = new ArrayList<Appointment>();
            ArrayList<Appointment> rejectedAppointments = new ArrayList<Appointment>();
            for (int i = 0; i < allAppointments.length; i++) {
                if (list.isItemChecked(i)) {
                    if (getFacade().hasPermissionToAllocate(allAppointments[i], selectedAllocatableActionHandler.currentAllocatable)) {
                        restrictedAppointments.add(allAppointments[i]);
                    } else {
                        rejectedAppointments.add(allAppointments[i]);
                        list.setItemChecked(i, false);
                    }
                }
            }
            Appointment[] restrictedAppointmentArray = new Appointment[restrictedAppointments.size()];
            restrictedAppointments.toArray(restrictedAppointmentArray);
            if (allAppointments.length > restrictedAppointmentArray.length) {
                reservation.setRestriction(selectedAllocatableActionHandler.currentAllocatable, restrictedAppointmentArray);
            } else {
                reservation.setRestriction(selectedAllocatableActionHandler.currentAllocatable, null);
            }
            if (rejectedAppointments.size() == allAppointments.length) {
                Toast.makeText(AllocatableDetailsActivity.this, R.string.no_permission_to_allocate_all, Toast.LENGTH_LONG).show();
            } else if (rejectedAppointments.size() > 0) {
                Toast.makeText(AllocatableDetailsActivity.this, String.format(getString(R.string.no_persmission_to_allocate_for_x_of_y_appointments), rejectedAppointments.size(), allAppointments.length), Toast.LENGTH_LONG).show();
            }
            refreshListView();
        }
    }
}
