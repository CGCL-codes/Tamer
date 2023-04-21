package fi.tuska.jalkametri;

import static fi.tuska.jalkametri.Common.KEY_ORIGINAL;
import java.util.Calendar;
import java.util.Date;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import fi.tuska.jalkametri.data.Drink;
import fi.tuska.jalkametri.data.DrinkEvent;
import fi.tuska.jalkametri.data.DrinkSelection;
import fi.tuska.jalkametri.data.DrinkSize;
import fi.tuska.jalkametri.data.IconName;
import fi.tuska.jalkametri.gui.DrinkSizeSelector;
import fi.tuska.jalkametri.gui.IconPickerDialog;
import fi.tuska.jalkametri.gui.IconView;
import fi.tuska.jalkametri.util.LogUtil;
import fi.tuska.jalkametri.util.NumberUtil;
import fi.tuska.jalkametri.util.ObjectCallback;
import fi.tuska.jalkametri.util.TimeUtil;

/**
 * Selects the drink details. This activity can be used as part of the drink
 * selecting path, or it can be fired up directly with the known drink
 * details.
 * 
 * <p>
 * The full drink selecting path is SelectDrinkCategoryActivity -
 * SelectDrinkTypeActivity - SelectDrinkSizeActivity -
 * EditDrinkDetailsActivity.
 * 
 * @author Tuukka Haapasalo
 */
public class EditDrinkDetailsActivity extends JalkametriDBActivity {

    private static final String KEY_OK_BUTTON_TITLE = "ok_button_title";

    private static final String KEY_SELECTED_DRINK_SELECTION = "selected_drink_selection";

    private static final String KEY_SHOW_TIME_PICKER = "show_time_picker";

    private static final String KEY_SHOW_SIZE_ICON_EDIT = "show_size_icon_edit";

    private static final String KEY_SHOW_SIZE_SELECTION = "show_size_selection";

    private static final String TAG = "SelectDrinkDetailsActivity";

    private static final int DIALOG_SELECT_ICON = 1;

    private static final int DIALOG_SELECT_SIZE_ICON = 2;

    private EditText nameEdit;

    private EditText strengthEdit;

    private EditText commentEdit;

    private TimePicker timePicker;

    private DatePicker datePicker;

    private View dateEditorArea;

    private TextView dateEditText;

    private IconView iconView;

    private boolean showTimeSelection = true;

    private DrinkSizeSelector drinkSizeSelector;

    private DrinkSelection selection;

    private long originalID = 0;

    private boolean dateVisible = false;

    private final ObjectCallback<IconName> iconNameCallback = new ObjectCallback<IconName>() {

        @Override
        public void objectSelected(IconName icon) {
            showStatus("Selecting icon " + icon.getIcon());
            iconView.setIcon(icon);
        }
    };

    public EditDrinkDetailsActivity() {
        super(R.string.title_edit_drink_details);
    }

    /**
     * @return true if the drink selection was OK (all required information
     * was found); false if the selection could not be prepared.
     */
    public static boolean prepareForDrinkSelection(Context parent, Intent intent, DrinkSelection sel) {
        Resources res = parent.getResources();
        if (sel.getDrink() == null) {
            Toast.makeText(parent, res.getText(R.string.msg_drink_not_set), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sel.getSize() == null) {
            Toast.makeText(parent, res.getText(R.string.msg_drink_size_not_set), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sel.getTime() == null) {
            Date time = new Date();
            sel.setTime(time);
        }
        LogUtil.d(TAG, "Preparing to edit details for %s", sel);
        intent.putExtra(KEY_SELECTED_DRINK_SELECTION, sel);
        intent.putExtra(KEY_OK_BUTTON_TITLE, res.getString(R.string.action_drink));
        intent.putExtra(KEY_SHOW_TIME_PICKER, true);
        intent.putExtra(KEY_SHOW_SIZE_ICON_EDIT, false);
        intent.putExtra(KEY_SHOW_SIZE_SELECTION, true);
        return true;
    }

    public static void prepareForDrinkEventModification(Context parent, Intent intent, DrinkEvent event, boolean showTime, boolean showSizeSelection, boolean showSizeIconEdit) {
        Resources res = parent.getResources();
        LogUtil.d(TAG, "Preparing to edit details for %s", event);
        intent.putExtra(KEY_SELECTED_DRINK_SELECTION, event);
        intent.putExtra(KEY_ORIGINAL, new Long(event.getIndex()));
        intent.putExtra(KEY_OK_BUTTON_TITLE, res.getString(R.string.action_ok));
        intent.putExtra(KEY_SHOW_TIME_PICKER, showTime);
        intent.putExtra(KEY_SHOW_SIZE_ICON_EDIT, showSizeIconEdit);
        intent.putExtra(KEY_SHOW_SIZE_SELECTION, showSizeSelection);
    }

    public static void prepareForDrinkModification(Context parent, Intent intent, Drink drink) {
        Resources res = parent.getResources();
        LogUtil.d(TAG, "Preparing to edit details for %s", drink);
        DrinkEvent event = new DrinkEvent(drink, new DrinkSize(), TimeUtil.getCurrentTime());
        intent.putExtra(KEY_SELECTED_DRINK_SELECTION, event);
        intent.putExtra(KEY_ORIGINAL, new Long(drink.getIndex()));
        intent.putExtra(KEY_OK_BUTTON_TITLE, res.getString(R.string.action_ok));
        intent.putExtra(KEY_SHOW_TIME_PICKER, false);
        intent.putExtra(KEY_SHOW_SIZE_ICON_EDIT, false);
        intent.putExtra(KEY_SHOW_SIZE_SELECTION, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.drink_details);
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        selection = (DrinkSelection) extras.get(KEY_SELECTED_DRINK_SELECTION);
        assert selection != null;
        originalID = extras.getLong(KEY_ORIGINAL);
        showTimeSelection = extras.getBoolean(KEY_SHOW_TIME_PICKER);
        if (!showTimeSelection) {
            View v = findViewById(R.id.time_edit_area);
            v.setVisibility(View.GONE);
        }
        boolean showSizeIconEdit = extras.getBoolean(KEY_SHOW_SIZE_ICON_EDIT);
        boolean showSizeSelection = extras.getBoolean(KEY_SHOW_SIZE_SELECTION);
        nameEdit = (EditText) findViewById(R.id.name_edit);
        strengthEdit = (EditText) findViewById(R.id.strength_edit);
        commentEdit = (EditText) findViewById(R.id.comment_edit);
        timePicker = (TimePicker) findViewById(R.id.time_edit);
        timePicker.setIs24HourView(true);
        iconView = (IconView) findViewById(R.id.icon);
        datePicker = (DatePicker) findViewById(R.id.date_edit);
        dateEditorArea = findViewById(R.id.date_edit_area);
        dateEditText = (TextView) findViewById(R.id.date_edit_text);
        Button okButton = (Button) findViewById(R.id.drink_button);
        String okTitle = extras.getString(KEY_OK_BUTTON_TITLE);
        if (okTitle != null) {
            okButton.setText(okTitle);
        }
        initializeComponents();
        drinkSizeSelector = new DrinkSizeSelector(this, adapter, showSizeSelection, showSizeIconEdit, DIALOG_SELECT_SIZE_ICON);
        drinkSizeSelector.initializeComponents(selection.getSize());
        tryToHideSoftKeyboard(nameEdit);
        invalidateView();
    }

    @Override
    protected void onPause() {
        updateSelectionFromUI();
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUIFromSelection();
    }

    @Override
    public void onBackPressed() {
        updateSelectionFromUI();
        super.onBackPressed();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        {
            DrinkSelection sel = (DrinkSelection) savedInstanceState.get(KEY_SELECTED_DRINK_SELECTION);
            if (sel != null) {
                this.selection = sel;
            }
        }
        updateUIFromSelection();
        updateDateEditorShown();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateSelectionFromUI();
        outState.putSerializable(KEY_SELECTED_DRINK_SELECTION, selection);
    }

    @Override
    public void updateUI() {
    }

    private void initializeComponents() {
        updateDateEditorShown();
        dateVisible = true;
        toggleDateVisible(null);
    }

    public boolean isModifying() {
        return originalID > 0;
    }

    public void toggleDateVisible(View v) {
        Resources res = getResources();
        if (dateVisible) {
            showStatus("Hiding date picker...");
            datePicker.setVisibility(View.GONE);
            dateEditText.setText(res.getText(R.string.drink_detail_date_hidden));
        } else {
            showStatus("Showing date picker...");
            datePicker.setVisibility(View.VISIBLE);
            dateEditText.setText(res.getText(R.string.drink_detail_date));
        }
        dateVisible = !dateVisible;
    }

    public void updateDateEditorShown() {
        dateEditorArea.setVisibility(isModifying() && showTimeSelection ? View.VISIBLE : View.GONE);
    }

    public void onOKPressed(View okButton) {
        updateSelectionFromUI();
        setResult(RESULT_OK, DrinkActivities.createDrinkSelectionResult(selection, originalID));
        finish();
    }

    public void onClickIcon(View v) {
        showStatus("Selecting icon...");
        showDialog(DIALOG_SELECT_ICON);
    }

    public void updateUIFromSelection() {
        assert selection != null;
        {
            Drink drink = selection.getDrink();
            assert drink != null;
            nameEdit.setText(drink.getName());
            strengthEdit.setText(NumberUtil.toString(drink.getStrength(), prefs.getLocale()));
            String icon = drink.getIcon();
            iconView.setIcon(icon);
            commentEdit.setText(drink.getComment());
        }
        {
            DrinkSize size = selection.getSize();
            drinkSizeSelector.setDrinkSize(size, false);
        }
        {
            Date drinkTime = selection.getTime();
            assert drinkTime != null;
            Calendar cal = Calendar.getInstance();
            cal.setTime(drinkTime);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
            datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }
    }

    public void updateSelectionFromUI() {
        {
            Drink drink = selection.getDrink();
            assert drink != null;
            drink.setName(nameEdit.getText().toString());
            drink.setStrength(NumberUtil.readFloat(strengthEdit.getText().toString(), prefs.getLocale()));
            String icon = iconView.getIcon().getIcon();
            drink.setIcon(icon);
            drink.setComment(commentEdit.getText().toString());
        }
        {
            DrinkSize size = drinkSizeSelector.getDrinkSize();
            selection.setSize(size);
        }
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            if (isModifying()) {
                cal.set(Calendar.YEAR, datePicker.getYear());
                cal.set(Calendar.MONTH, datePicker.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
            }
            cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
            cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
            Calendar now = Calendar.getInstance();
            if (!isModifying()) {
                if (cal.after(now)) {
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    assert !cal.after(now);
                }
            }
            selection.setTime(cal.getTime());
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
            case DIALOG_SELECT_ICON:
                dialog = new IconPickerDialog(this, iconNameCallback);
                return dialog;
            case DIALOG_SELECT_SIZE_ICON:
                dialog = new IconPickerDialog(this, drinkSizeSelector.getSetSizeIconCallback());
                return dialog;
        }
        return super.onCreateDialog(id);
    }
}
