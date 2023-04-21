package cn.com.androidforfun.finance.ui;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import cn.com.androidforfun.commons.util.Formats;
import cn.com.androidforfun.commons.util.GUIs;
import cn.com.androidforfun.commons.util.Logger;
import cn.com.androidforfun.finance.R;
import cn.com.androidforfun.finance.calculator.Calculator;
import cn.com.androidforfun.finance.context.ContextsActivity;
import cn.com.androidforfun.finance.data.Account;
import cn.com.androidforfun.finance.data.Detail;
import cn.com.androidforfun.finance.data.IDataProvider;
import cn.com.androidforfun.finance.data.PaymentsType;

public class DetailEditorActivity extends ContextsActivity implements android.view.View.OnClickListener {

    public static final String INTENT_MODE_CREATE = "modeCreate";

    public static final String INTENT_DETAIL = "detail";

    public static final int VIEW_MODE_INCOME_CREATE = 1;

    public static final int VIEW_MODE_OUTCOME_CREATE = 2;

    public static final int VIEW_MODE_DETAIL = 3;

    private int modeCreate;

    private int counterCreate;

    private Detail detail;

    private Detail workingDetail;

    Spinner fromEditor;

    Spinner toEditor;

    EditText dateEditor;

    EditText noteEditor;

    EditText moneyEditor;

    Button okBtn;

    Button cancelBtn;

    Button closeBtn;

    static final int TIME_DIALOG_ID = 0;

    static final int DATE_DIALOG_ID = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deteditor);
        initIntent();
        initialEditor();
    }

    /** clone a detail without id **/
    private Detail clone(Detail detail) {
        Detail d = new Detail(detail.getFrom(), detail.getTo(), detail.getDate(), detail.getMoney(), detail.getNote());
        return d;
    }

    private void initIntent() {
        Bundle bundle = getIntentExtras();
        modeCreate = bundle.getInt(INTENT_MODE_CREATE, VIEW_MODE_INCOME_CREATE);
        detail = (Detail) bundle.get(INTENT_DETAIL);
        workingDetail = clone(detail);
        if (modeCreate == VIEW_MODE_INCOME_CREATE) {
            setTitle(R.string.title_deteditor_create);
        } else if (modeCreate == VIEW_MODE_OUTCOME_CREATE) {
            setTitle(R.string.title_deteditor_create);
        } else if (modeCreate == VIEW_MODE_DETAIL) {
            setTitle(R.string.title_deteditor_create);
        }
    }

    private void initialEditor() {
        initialSpinner();
        dateEditor = (EditText) findViewById(R.id.deteditor_date);
        dateEditor.setText(Formats.normalizeDate2String(workingDetail.getDate()));
        moneyEditor = (EditText) findViewById(R.id.deteditor_money);
        moneyEditor.setText(workingDetail.getMoney() <= 0 ? "" : Formats.double2String(workingDetail.getMoney()));
        noteEditor = (EditText) findViewById(R.id.deteditor_note);
        noteEditor.setText(workingDetail.getNote());
        ImageButton cal2 = (ImageButton) findViewById(R.id.deteditor_cal2);
        Button pickDateBtn = (Button) findViewById(R.id.pickDateBtn);
        okBtn = (Button) findViewById(R.id.deteditor_ok);
        if (modeCreate == VIEW_MODE_INCOME_CREATE || modeCreate == VIEW_MODE_OUTCOME_CREATE) {
            okBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_add, 0, 0, 0);
            okBtn.setText(R.string.cact_create);
        } else {
            okBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_update, 0, 0, 0);
            okBtn.setText(R.string.cact_update);
        }
        okBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.deteditor_cancel);
        closeBtn = (Button) findViewById(R.id.deteditor_close);
        cancelBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        cal2.setOnClickListener(this);
        pickDateBtn.setOnClickListener(this);
        if (modeCreate == VIEW_MODE_DETAIL) {
            fromEditor.setEnabled(false);
            toEditor.setEnabled(false);
            dateEditor.setEnabled(false);
            moneyEditor.setEnabled(false);
            cal2.setEnabled(false);
            pickDateBtn.setEnabled(false);
            cancelBtn.setVisibility(Button.GONE);
            closeBtn.setVisibility(Button.VISIBLE);
        }
    }

    List<String> fromListId;

    List<String> toListId;

    private void initialSpinner() {
        fromEditor = (Spinner) findViewById(R.id.deteditor_from);
        toEditor = (Spinner) findViewById(R.id.deteditor_to);
        final IDataProvider idp = getContexts().getDataProvider();
        fromListId = new ArrayList<String>();
        toListId = new ArrayList<String>();
        if (modeCreate == VIEW_MODE_INCOME_CREATE || detail.getMode() == VIEW_MODE_INCOME_CREATE) {
            List<PaymentsType> inData = idp.listType("in");
            ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
            int i = 0;
            int position = 0;
            for (PaymentsType paymentsType : inData) {
                fromAdapter.add(paymentsType.getType());
                fromListId.add(paymentsType.getId());
                if (paymentsType.getId().equals(detail.getFrom())) {
                    position = i;
                }
                i++;
            }
            fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fromEditor.setAdapter(fromAdapter);
            fromEditor.setSelection(position, true);
            List<Account> accounts = idp.listAccount();
            ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
            i = 0;
            position = 0;
            for (Account account : accounts) {
                toAdapter.add(account.getName() + "-" + account.getType());
                toListId.add(account.getId());
                if (account.getId().equals(detail.getTo())) {
                    position = i;
                }
                i++;
            }
            toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            toEditor.setAdapter(toAdapter);
            toEditor.setSelection(position, true);
        }
        if (modeCreate == VIEW_MODE_OUTCOME_CREATE || detail.getMode() == VIEW_MODE_OUTCOME_CREATE) {
            List<Account> accounts = idp.listAccount();
            ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
            int i = 0;
            int position = 0;
            for (Account account : accounts) {
                fromAdapter.add(account.getName() + "-" + account.getType());
                fromListId.add(account.getId());
                if (account.getId().equals(detail.getFrom())) {
                    position = i;
                }
                i++;
            }
            fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fromEditor.setAdapter(fromAdapter);
            fromEditor.setSelection(position, true);
            List<PaymentsType> outData = idp.listType("out");
            ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
            i = 0;
            position = 0;
            for (PaymentsType paymentsType : outData) {
                toAdapter.add(paymentsType.getType());
                toListId.add(paymentsType.getId());
                if (paymentsType.getId().equals(detail.getTo())) {
                    position = i;
                }
                i++;
            }
            toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            toEditor.setAdapter(toAdapter);
            toEditor.setSelection(position, true);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.deteditor_ok:
                doOk();
                break;
            case R.id.deteditor_cancel:
                doCancel();
                break;
            case R.id.deteditor_close:
                doClose();
                break;
            case R.id.deteditor_cal2:
                doCalculator();
                break;
            case R.id.pickDateBtn:
                showDialog(DATE_DIALOG_ID);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_DIALOG_ID:
                final Calendar c = Calendar.getInstance();
                try {
                    c.setTime(Formats.normalizeString2Date(dateEditor.getText().toString()));
                } catch (ParseException e) {
                }
                return new DatePickerDialog(this, mDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id) {
            case DATE_DIALOG_ID:
                final Calendar c = Calendar.getInstance();
                try {
                    c.setTime(Formats.normalizeString2Date(dateEditor.getText().toString()));
                } catch (ParseException e) {
                }
                ((DatePickerDialog) dialog).updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            final Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateEditor.setText(Formats.normalizeDate2String(c.getTime()));
        }
    };

    private void doCalculator() {
        Intent intent = null;
        intent = new Intent(this, Calculator.class);
        intent.putExtra(Calculator.INTENT_NEED_RESULT, true);
        intent.putExtra(Calculator.INTENT_START_VALUE, moneyEditor.getText().toString());
        startActivityForResult(intent, Constants.REQUEST_CALCULATOR_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CALCULATOR_CODE && resultCode == Activity.RESULT_OK) {
            String result = data.getExtras().getString(Calculator.INTENT_RESULT_VALUE);
            try {
                double d = Double.parseDouble(result);
                if (d > 0) {
                    moneyEditor.setText(Formats.double2String(d));
                } else {
                    moneyEditor.setText("0");
                }
            } catch (Exception x) {
            }
        }
    }

    private void doOk() {
        int fromPos = fromEditor.getSelectedItemPosition();
        int toPos = toEditor.getSelectedItemPosition();
        Date date = null;
        try {
            date = Formats.normalizeString2Date(dateEditor.getText().toString());
        } catch (ParseException e) {
            Logger.e(e.getMessage(), e);
            GUIs.errorToast(this, e);
            return;
        }
        String moneystr = moneyEditor.getText().toString();
        if ("".equals(moneystr)) {
            moneyEditor.requestFocus();
            GUIs.alert(this, i18n.string(R.string.cmsg_field_empty, i18n.string(R.string.label_money)));
            return;
        }
        double money = Formats.string2Double(moneystr);
        if (money == 0) {
            GUIs.alert(this, i18n.string(R.string.cmsg_field_zero, i18n.string(R.string.label_money)));
            return;
        }
        String note = noteEditor.getText().toString();
        workingDetail.setFrom(fromListId.get(fromPos));
        workingDetail.setTo(toListId.get(toPos));
        workingDetail.setDate(date);
        workingDetail.setMoney(money);
        workingDetail.setNote(note.trim());
        workingDetail.setMode(modeCreate);
        IDataProvider idp = getContexts().getDataProvider();
        if (modeCreate == VIEW_MODE_DETAIL) {
            detail.setNote(note.trim());
            idp.updateDetail(detail.getId(), detail);
            setResult(RESULT_OK);
            GUIs.shortToast(this, i18n.string(R.string.msg_detail_updated));
            finish();
            return;
        }
        idp.newDetail(workingDetail);
        setResult(RESULT_OK);
        Account account;
        if (modeCreate == VIEW_MODE_INCOME_CREATE) {
            account = idp.findAccount(workingDetail.getTo());
            account.setInitialValue(account.getInitialValue() + workingDetail.getMoney());
            idp.updateAccount(account.getId(), account);
        }
        if (modeCreate == VIEW_MODE_OUTCOME_CREATE) {
            account = idp.findAccount(workingDetail.getFrom());
            account.setInitialValue(account.getInitialValue() - workingDetail.getMoney());
            idp.updateAccount(account.getId(), account);
        }
        workingDetail = clone(workingDetail);
        workingDetail.setMoney(0D);
        workingDetail.setNote("");
        moneyEditor.setText("");
        moneyEditor.requestFocus();
        noteEditor.setText("");
        counterCreate++;
        okBtn.setText(i18n.string(R.string.cact_create) + "(" + counterCreate + ")");
        cancelBtn.setVisibility(Button.GONE);
        closeBtn.setVisibility(Button.VISIBLE);
    }

    private void doCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void doClose() {
        setResult(RESULT_OK);
        if (modeCreate != VIEW_MODE_DETAIL) {
            GUIs.shortToast(this, i18n.string(R.string.msg_created_detail, counterCreate));
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
