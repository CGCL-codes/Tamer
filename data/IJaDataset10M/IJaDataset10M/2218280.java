package gwtmodule1.client.gui.utils;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FormTable extends FlexTable {

    public static final String STYLE_SECTION_HEADER = "FormTableSection";

    public static final String STYLE_BUTTON = "FormTableButton";

    public FormTable() {
        super();
    }

    public void addButton(String title, ClickListener listener) {
        Button b = new Button();
        b.setText(title);
        b.setStyleName(STYLE_BUTTON);
        if (listener != null) b.addClickListener(listener);
        int i = getRowCount();
        setWidget(i, 0, b);
        getFlexCellFormatter().setColSpan(i, 0, 2);
        getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_CENTER);
    }

    public void addInput(String title, Widget input) {
        int i = getRowCount();
        setText(i, 0, title + ":");
        getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        setWidget(i, 1, input);
    }

    public void addText(String title, String text) {
        int i = getRowCount();
        setText(i, 0, title + ":");
        getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        setText(i, 1, text);
    }

    public void addLine(Widget line) {
        int i = getRowCount();
        setWidget(i, 0, line);
        getFlexCellFormatter().setColSpan(i, 0, 2);
    }

    public void addSection(String title) {
        FlexTable t = new FlexTable();
        t.setStyleName(STYLE_SECTION_HEADER);
        t.setWidth("100%");
        t.setHTML(1, 0, "<hr>");
        t.setHTML(1, 1, "<nobr>" + title + "</nobr>");
        t.setHTML(1, 2, "<hr>");
        t.getCellFormatter().setWidth(0, 0, "50%");
        t.getCellFormatter().setWidth(0, 2, "50%");
        addLine(t);
    }

    public void addCreditCardNumber() {
        addInput("Credit Card Institute", new ListBox());
        addInput("Credit Card No.", new TextBox());
        addInput("Credit Card Expiration Date", new TextBox());
    }

    public void addAddress() {
        addInput("City", new TextBox());
        addInput("Street", new TextBox());
        addInput("State", new TextBox());
        addInput("ZIP", new TextBox());
    }
}
