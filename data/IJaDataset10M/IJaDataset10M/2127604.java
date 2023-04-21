package com.fh.auge.internal;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IControlCreator;
import org.eclipse.jface.fieldassist.TextControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.domainlanguage.money.Money;
import com.domainlanguage.time.CalendarDate;
import com.domainlanguage.time.CalendarInterval;
import com.fh.auge.core.Gain;
import com.fh.auge.core.Position;
import com.fh.auge.core.Security;
import com.fh.auge.core.currency.ICurrencyManager;
import com.fh.auge.core.tradelog.Trade;

public abstract class TradePanel extends Composite {

    private DecoratedField commentField;

    enum Validator {

        REQUIRED, INT, MONEY
    }

    Map<String, String> errors = new HashMap<String, String>();

    Map<DecoratedField, Validator[]> validators = new HashMap<DecoratedField, Validator[]>();

    protected Label nameLabel = null;

    protected Text nameText = null;

    protected Label sharesLabel = null;

    protected Label sharesText = null;

    protected Label buyDateLabel = null;

    protected Label buyDate = null;

    private Label investedCapitalText = null;

    protected Text commentText = null;

    private FieldDecoration errorFieldIndicator;

    private Trade trade;

    ICurrencyManager currenyManager;

    private DecoratedField sellDateField;

    private DateTime sellDateTime;

    private DecoratedField returnedField;

    private Text returnedText;

    private Label returnedCapitalCombo;

    public TradePanel(Composite parent, int style, ICurrencyManager currenyManager) {
        super(parent, style);
        this.currenyManager = currenyManager;
        initialize();
    }

    private void initialize() {
        this.setLayout(PanelHelper.createLayout(3));
        FieldDecoration requiredFieldIndicator = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED);
        errorFieldIndicator = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
        Group g = new Group(this, SWT.NONE);
        g.setText("Open");
        g.setLayout(PanelHelper.createLayout(2));
        g.setLayoutData(PanelHelper.getGridDataField(3));
        new Label(g, SWT.NONE).setText("Shares Owned:");
        sharesText = new Label(g, SWT.RIGHT);
        new Label(g, SWT.NONE).setText("Date:");
        buyDate = new Label(g, SWT.NONE);
        new Label(g, SWT.NONE).setText("Invested Capital:");
        investedCapitalText = new Label(g, SWT.RIGHT);
        Listener listener = new Listener() {

            public void handleEvent(Event e) {
                String string = e.text;
                char[] chars = new char[string.length()];
                string.getChars(0, chars.length, chars, 0);
                for (int i = 0; i < chars.length; i++) {
                    if (!('0' <= chars[i] && chars[i] <= '9' || chars[i] == '.' || chars[i] == ',')) {
                        e.doit = false;
                        return;
                    }
                }
            }
        };
        Group g1 = new Group(this, SWT.NONE);
        g1.setText("Close");
        g1.setLayout(PanelHelper.createLayout(3));
        g1.setLayoutData(PanelHelper.getGridDataField(3));
        new Label(g1, SWT.NONE).setText("Date:");
        sellDateField = new DecoratedField(g1, SWT.BORDER | SWT.RIGHT, new IControlCreator() {

            public Control createControl(Composite parent, int style) {
                return new DateTime(parent, style);
            }
        });
        GridData gridDataField = PanelHelper.getGridDataField(1);
        gridDataField.grabExcessHorizontalSpace = false;
        gridDataField.horizontalAlignment = GridData.BEGINNING;
        sellDateField.getLayoutControl().setLayoutData(gridDataField);
        sellDateTime = (DateTime) sellDateField.getControl();
        sellDateTime.setHours(12);
        sellDateField.addFieldDecoration(requiredFieldIndicator, SWT.TOP | SWT.LEFT, true);
        sellDateField.addFieldDecoration(errorFieldIndicator, SWT.BOTTOM | SWT.LEFT, false);
        sellDateField.hideDecoration(errorFieldIndicator);
        new Label(g1, SWT.NONE).setText("");
        new Label(g1, SWT.NONE).setText("Returned Capital:");
        returnedField = new DecoratedField(g1, SWT.BORDER | SWT.RIGHT, new TextControlCreator());
        returnedText = (Text) returnedField.getControl();
        returnedField.getLayoutControl().setLayoutData(PanelHelper.getGridDataField(1));
        ((GridData) returnedField.getLayoutControl().getLayoutData()).verticalAlignment = GridData.FILL;
        returnedField.addFieldDecoration(errorFieldIndicator, SWT.BOTTOM | SWT.LEFT, false);
        returnedField.addFieldDecoration(requiredFieldIndicator, SWT.TOP | SWT.LEFT, true);
        returnedField.hideDecoration(errorFieldIndicator);
        returnedText.addModifyListener(new FieldModifyListener(returnedField, "Returned Capital"));
        validators.put(returnedField, new Validator[] { Validator.REQUIRED, Validator.MONEY });
        returnedCapitalCombo = new Label(g1, SWT.NONE);
        returnedText.addListener(SWT.Verify, listener);
        new Label(this, SWT.NONE).setText("");
        new Label(this, SWT.NONE).setText("");
        new Label(this, SWT.NONE).setText("");
        Label l = new Label(this, SWT.NONE);
        l.setText("Comment:");
        commentField = new DecoratedField(this, SWT.BORDER | SWT.LEFT | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP, new TextControlCreator());
        commentText = (Text) commentField.getControl();
        GridData d = PanelHelper.getGridDataField(2);
        d.verticalAlignment = GridData.FILL;
        d.grabExcessVerticalSpace = true;
        commentField.getLayoutControl().setLayoutData(d);
        commentText.addModifyListener(new FieldModifyListener(commentField, "Comment"));
        commentField.addFieldDecoration(errorFieldIndicator, SWT.BOTTOM | SWT.LEFT, false);
        commentField.hideDecoration(errorFieldIndicator);
    }

    public boolean canCommit() {
        return validate(returnedField) == null;
    }

    public void commit() throws Exception {
        trade.setComment(getComment());
        trade.setReturned(getReturnedCapital());
        trade.setHoldInterval(getInterval());
        trade.setGain(Gain.create(trade.getInvested(), trade.getReturned()));
    }

    private CalendarInterval getInterval() {
        return CalendarInterval.inclusive(trade.getHoldInterval().start(), getDate(sellDateTime));
    }

    private String getComment() {
        return commentText.getText();
    }

    public boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public String getErrorMessage() {
        StringBuffer buf = new StringBuffer();
        String del = "";
        for (String msg : errors.values()) {
            buf.append(del);
            buf.append(msg);
            del = ", ";
        }
        return buf.toString();
    }

    public static void main(String[] args) throws ParseException {
        ICurrencyManager currenyManager = new ICurrencyManager() {

            public List<Currency> getCurrencies() {
                Currency[] list = new Currency[] { Currency.getInstance("EUR"), Currency.getInstance("USD") };
                return Arrays.asList(list);
            }

            public Currency getDefaultCurrency() {
                return Currency.getInstance("EUR");
            }
        };
        Display display = Display.getDefault();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout(SWT.VERTICAL));
        TradePanel panel = new TradePanel(shell, SWT.NONE, currenyManager) {

            @Override
            protected void errorsChanged() {
            }
        };
        Position p = Position.investCapital(new Security("APPLE INC.", "aaa", "APC.F", Currency.getInstance("EUR")), Money.euros(1122.25), 15, new Date());
        Trade t = Trade.create(p);
        t.setReturned(Money.euros(1300.25));
        panel.setTrade(t);
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private static CalendarDate getDate(DateTime el) {
        return CalendarDate.date(el.getYear(), el.getMonth(), el.getDay());
    }

    public void setTrade(Trade data) {
        this.trade = data;
        if (data != null) {
            if (data.getShares() != 0) sharesText.setText("" + data.getShares());
            if (data.getInvested() != null) {
                investedCapitalText.setText(FormatHolder.getInstance().getMoneyFormat().format(data.getInvested()));
            }
            if (data.getHoldInterval() != null) {
                buyDate.setText(FormatHolder.getInstance().format(data.getHoldInterval().start()));
                sellDateTime.setYear(data.getHoldInterval().end().breachEncapsulationOf_year());
                sellDateTime.setMonth(data.getHoldInterval().end().breachEncapsulationOf_month());
                sellDateTime.setDay(data.getHoldInterval().end().breachEncapsulationOf_day());
            }
            if (data.getComment() != null) {
                commentText.setText(data.getComment());
            }
            if (data.getReturned() != null) {
                returnedText.setText(FormatHolder.getInstance().getMoneyInputFormat().format(data.getReturned().breachEncapsulationOfAmount()));
            }
            returnedCapitalCombo.setText(FormatHolder.getInstance().getCurrencySymbol(data.getInvested().breachEncapsulationOfCurrency()));
        }
        layout(true);
    }

    protected int getShares() throws Exception {
        return Integer.parseInt(sharesText.getText());
    }

    protected Money getReturnedCapital() throws Exception {
        return Money.valueOf(FormatHolder.getInstance().getMoneyInputFormat().parse(returnedText.getText()).doubleValue(), trade.getInvested().breachEncapsulationOfCurrency());
    }

    protected abstract void errorsChanged();

    class FieldModifyListener implements ModifyListener {

        DecoratedField field;

        String name;

        public FieldModifyListener(DecoratedField field, String name) {
            super();
            this.field = field;
            this.name = name;
        }

        public void modifyText(ModifyEvent e) {
            String msg = validate(field);
            if (msg != null) {
                field.showDecoration(errorFieldIndicator);
                errors.put(name, name + msg);
            } else {
                field.hideDecoration(errorFieldIndicator);
                errors.remove(name);
            }
            errorsChanged();
        }
    }

    protected String validate(DecoratedField field) {
        Text source = (Text) field.getControl();
        String msg = null;
        Validator[] validators = this.validators.get(field);
        if (validators != null) {
            for (Validator validator : validators) {
                if (validator == Validator.REQUIRED) {
                    if (isEmpty(source.getText())) {
                        msg = " required";
                        break;
                    }
                } else if (validator == Validator.INT) {
                    if (!isEmpty(source.getText())) {
                        try {
                            int t = Integer.parseInt(source.getText());
                            if (t <= 0) msg = " must be greater than 0";
                        } catch (NumberFormatException e1) {
                            msg = " must be an Integer";
                        }
                        break;
                    }
                } else if (validator == Validator.MONEY) {
                    if (!isEmpty(source.getText())) {
                        try {
                            double d = FormatHolder.getInstance().getMoneyInputFormat().parse(source.getText()).doubleValue();
                            if (d <= 0) msg = " must be greater than 0";
                        } catch (ParseException ex) {
                            msg = " must be a Double";
                        }
                        break;
                    }
                }
            }
        }
        return msg;
    }
}
