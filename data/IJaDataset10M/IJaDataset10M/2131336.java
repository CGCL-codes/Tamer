package edu.vub.at.urbiflock.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.BufferType;
import edu.vub.at.objects.natives.grammar.AGSymbol;
import edu.vub.at.urbiflock.interfaces.Flockr;
import edu.vub.at.urbiflock.util.UrbiflockConstants;
import edu.vub.at.urbiflock.util.Utility;

public class FlockEditor extends Activity {

    private Flockr flockr_;

    private static final String SPINNER_TITLE = "Please pick a proximity";

    protected static final int ADD_CONSTRAINT_ITEM = 0;

    protected static final int REMOVE_CONSTRAINTS_ITEM = 1;

    private HashMap<Integer, ProfileFieldMatchProximityPanel> constraints_ = new HashMap<Integer, ProfileFieldMatchProximityPanel>();

    private ScrollView mainView_;

    private LinearLayout fieldsPanel_;

    private LinearLayout flockNamePanel_;

    private LinearLayout constraintsPanel_;

    private LinearLayout addButtonPanel_;

    private LinearLayout buttonPanel_;

    private MenuItem profileConstraintItem_;

    private MenuItem removeItem_;

    private EditText flockNameField_;

    private HashSet<ProfileFieldMatchProximityPanel> constraintPanels_ = new HashSet<ProfileFieldMatchProximityPanel>();

    private LinearLayout isFriendPanel_;

    private LinearLayout isNearbyPanel_;

    private Button saveButton_;

    private Button doneButton_;

    private Button addButton_;

    public FlockEditor() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flockr_ = MainPanel.getFlockr();
        mainView_ = new ScrollView(this);
        mainView_.setBackgroundColor(Color.parseColor(UrbiflockConstants.bgColour));
        fieldsPanel_ = Utility.getLinearLayoutVertical(this);
        flockNamePanel_ = Utility.getLinearLayoutHorizontal(this);
        flockNameField_ = new EditText(this);
        flockNameField_.setSingleLine();
        flockNameField_.setHint("new flock name");
        flockNamePanel_.addView(flockNameField_, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        constraintsPanel_ = Utility.getLinearLayoutVertical(this);
        buttonPanel_ = new LinearLayout(this);
        buttonPanel_.setOrientation(LinearLayout.HORIZONTAL);
        doneButton_ = new Button(this);
        doneButton_.setText("Done");
        doneButton_.setVisibility(View.GONE);
        saveButton_ = new Button(this);
        saveButton_.setText("Save");
        saveButton_.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
        addButton_ = new Button(this);
        addButton_.setText("Add constraint");
        addButton_.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(android.R.drawable.ic_input_add), null, null, null);
        LinearLayout addButtonLayout = new LinearLayout(this);
        LinearLayout saveButtonLayout = new LinearLayout(this);
        addButtonLayout.addView(addButton_);
        saveButtonLayout.addView(saveButton_);
        saveButtonLayout.addView(doneButton_);
        buttonPanel_.addView(addButtonLayout);
        buttonPanel_.addView(saveButtonLayout);
        isFriendPanel_ = Utility.getLinearLayoutHorizontal(this);
        isNearbyPanel_ = Utility.getLinearLayoutHorizontal(this);
        ;
        saveButton_.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                tryToSave();
            }
        });
        addButton_.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                addProfileConstraintPanel();
            }
        });
        doneButton_.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                switchToEditMode();
            }
        });
        CheckBox isFriendCheckbox_ = new CheckBox(this);
        isFriendCheckbox_.setText(" Is Friend?");
        CheckBox isNearbyCheckbox_ = new CheckBox(this);
        isNearbyCheckbox_.setText(" Is Nearby?");
        Utility.setLinearLayout(this, isFriendPanel_, new View[] { isFriendCheckbox_, isNearbyCheckbox_ });
        setContentView(mainView_);
        updateUI();
    }

    private void updateUI() {
        mainView_.removeAllViews();
        mainView_.addView(fieldsPanel_);
        fieldsPanel_.removeAllViews();
        fieldsPanel_.addView(flockNamePanel_);
        fieldsPanel_.addView(constraintsPanel_);
        constraintsPanel_.addView(isFriendPanel_);
        constraintsPanel_.addView(isNearbyPanel_);
        fieldsPanel_.addView(buttonPanel_);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        profileConstraintItem_ = menu.add(0, ADD_CONSTRAINT_ITEM, ADD_CONSTRAINT_ITEM, "add profile constraint");
        profileConstraintItem_.setIcon(android.R.drawable.ic_menu_add);
        removeItem_ = menu.add(0, REMOVE_CONSTRAINTS_ITEM, REMOVE_CONSTRAINTS_ITEM, "remove constraints");
        removeItem_.setIcon(android.R.drawable.ic_menu_delete);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Log.v("AmbientTalk", "ContextItemSelected: " + itemId);
        switch(item.getItemId()) {
            case ADD_CONSTRAINT_ITEM:
                addProfileConstraintPanel();
                return true;
            case REMOVE_CONSTRAINTS_ITEM:
                switchToRemoveMode();
                return true;
            default:
                return false;
        }
    }

    public void addProfileConstraintPanel() {
        final ProfileFieldMatchProximityPanel panel = new ProfileFieldMatchProximityPanel(this, constraintsPanel_);
        constraintsPanel_.addView(panel);
        constraintPanels_.add(panel);
    }

    public void switchToRemoveMode() {
        saveButton_.setVisibility(View.GONE);
        addButton_.setVisibility(View.GONE);
        doneButton_.setVisibility(View.VISIBLE);
        removeItem_.setVisible(false);
        removeItem_.setEnabled(false);
        profileConstraintItem_.setVisible(false);
        profileConstraintItem_.setEnabled(false);
        for (ProfileFieldMatchProximityPanel c : constraintPanels_) {
            c.toggleFields(false);
        }
    }

    public void switchToEditMode() {
        saveButton_.setVisibility(View.VISIBLE);
        addButton_.setVisibility(View.VISIBLE);
        doneButton_.setVisibility(View.GONE);
        removeItem_.setVisible(true);
        removeItem_.setEnabled(true);
        profileConstraintItem_.setVisible(true);
        profileConstraintItem_.setEnabled(true);
        for (ProfileFieldMatchProximityPanel c : constraintPanels_) {
            c.toggleFields(true);
        }
    }

    public void removePanel(ProfileFieldMatchProximityPanel panel) {
        constraintPanels_.remove(panel);
        constraintsPanel_.removeView(panel);
    }

    public void tryToSave() {
        if (flockNameField_.getText().toString().trim() == "") {
            return;
        } else {
            String fieldName;
            boolean shouldBeFriend = false;
            boolean shouldBeNearby = false;
            Vector fieldMatchers = new Vector();
            Log.v("AmbientTalk", "Number of proximities " + constraintPanels_.size());
            Iterator proximityPanelIterator = constraintPanels_.iterator();
            while (proximityPanelIterator.hasNext()) {
                LinearLayout proximityPanel = (LinearLayout) proximityPanelIterator.next();
                if (proximityPanel.equals(isFriendPanel_)) {
                    shouldBeFriend = ((CheckBox) proximityPanel.getChildAt(0)).isChecked();
                    break;
                }
                if (proximityPanel.equals(isNearbyPanel_)) {
                    shouldBeNearby = ((CheckBox) proximityPanel.getChildAt(0)).isChecked();
                    break;
                }
                ProfileFieldMatchProximityPanel fieldPanel = (ProfileFieldMatchProximityPanel) proximityPanel;
                Vector fieldMatcher = new Vector();
                fieldName = fieldPanel.getFieldName();
                fieldMatcher.add(AGSymbol.jAlloc(fieldName));
                fieldMatcher.add(fieldPanel.getFieldType());
                fieldMatcher.add(fieldPanel.getComparator());
                fieldMatcher.add(fieldPanel.getFieldValue());
                fieldMatchers.add(fieldMatcher);
            }
            flockr_.createFlockFromFieldMatchers(flockNameField_.getText().toString().trim(), fieldMatchers, shouldBeFriend, shouldBeNearby);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
