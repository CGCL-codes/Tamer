package org.monet.backmobile.dialog;

import org.monet.backmobile.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TaskSelectAndInsertDialog extends AlertDialog implements DialogInterface.OnClickListener, OnCheckedChangeListener {

    private TextView selectorTextView;

    private Spinner elementsView;

    private CheckBox notesEnablerView;

    private EditText notesView;

    private int selectorTextViewTextId;

    public TaskSelectAndInsertDialog(Context context) {
        super(context);
    }

    public TaskSelectAndInsertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public TaskSelectAndInsertDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View mainView = getLayoutInflater().inflate(R.layout.task_dialog_select_and_insert, null);
        this.setView(mainView);
        this.selectorTextView = (TextView) mainView.findViewById(R.id.task_dialog_selector_text);
        this.selectorTextView.setText(this.selectorTextViewTextId);
        this.elementsView = (Spinner) mainView.findViewById(R.id.task_dialog_selector);
        this.notesEnablerView = (CheckBox) mainView.findViewById(R.id.task_dialog_add_note);
        this.notesEnablerView.setOnCheckedChangeListener(this);
        this.notesView = (EditText) mainView.findViewById(R.id.task_dialog_note);
        this.setButton(Dialog.BUTTON_POSITIVE, this.getContext().getString(android.R.string.ok), this);
        this.setButton(Dialog.BUTTON_NEGATIVE, this.getContext().getString(android.R.string.cancel), this);
        super.onCreate(savedInstanceState);
    }

    public void setSelectorText(int resId) {
        this.selectorTextViewTextId = resId;
    }

    @Override
    public void onClick(DialogInterface dialoginterface, int button) {
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundbutton, boolean checked) {
        if (checked) notesView.setVisibility(View.VISIBLE); else notesView.setVisibility(View.GONE);
    }
}
