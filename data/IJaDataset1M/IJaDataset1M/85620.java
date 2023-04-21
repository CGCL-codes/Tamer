package net.chanibal.hala.client;

import java.util.ArrayList;
import java.util.Date;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractForm extends Composite {

    private Button processButton;

    private Label infoLabel;

    private FlexTable flexTable;

    private ArrayList pola = new ArrayList();

    public abstract class AbstractFormField {

        String name;

        Label errWidget;

        AbstractFormField(String name) {
            this.name = name;
        }

        public abstract Widget getDataWidet();

        public Widget getNameWidet() {
            return new Label(name + ":");
        }

        public Widget getErrorWidet() {
            if (errWidget == null) {
                errWidget = new Label();
                errWidget.setStyleName("gwt-Label-Error");
            }
            return errWidget;
        }

        public void setFieldError(String err) {
            if (errWidget == null) return;
            if (err == null) {
                errWidget.setVisible(true);
            } else {
                errWidget.setText(err);
                errWidget.setVisible(true);
            }
        }

        public void remFieldError() {
            if (errWidget == null) return;
            errWidget.setVisible(false);
        }

        public abstract boolean apply();
    }

    public class TimeField extends AbstractFormField {

        HorizontalPanel horizontalPanel;

        ListBox godzina;

        ListBox minuta;

        Date time;

        TimeField(String name, Date time) {
            super(name);
            this.time = time;
        }

        public int getHour() {
            return godzina.getSelectedIndex();
        }

        public int getMinute() {
            return minuta.getSelectedIndex() * 5;
        }

        public boolean apply() {
            return true;
        }

        public Widget getDataWidet() {
            if (horizontalPanel == null) {
                horizontalPanel = new HorizontalPanel();
                godzina = new ListBox();
                horizontalPanel.add(godzina);
                for (int x = 0; x <= 24; x++) godzina.addItem(String.valueOf(x));
                godzina.setSelectedIndex(time.getHours());
                minuta = new ListBox();
                horizontalPanel.add(minuta);
                for (int x = 0; x < 60; x += 5) minuta.addItem(String.valueOf(x));
                minuta.setSelectedIndex(time.getMinutes() / 5);
            }
            return horizontalPanel;
        }
    }

    public class DataField extends AbstractFormField {

        public class DziencChangeListener implements ChangeListener {

            public void onChange(Widget sender) {
                date = getDate();
            }
        }

        public class RokChangeListener implements ChangeListener {

            public void onChange(Widget sender) {
                adjustDate();
            }
        }

        public class MiesiacChangeListener implements ChangeListener {

            public void onChange(Widget sender) {
                adjustDate();
            }
        }

        public Date date;

        int startYear;

        int cmp;

        HorizontalPanel horizontalPanel;

        ListBox rok;

        ListBox dzien;

        ListBox miesiac;

        public DataField(String name, Date date, int cmp) {
            super(name);
            this.date = date;
            startYear = date.getYear();
            if (cmp != 1) startYear -= 5;
            if (cmp == 2) startYear -= 5;
            this.cmp = cmp;
        }

        public boolean apply() {
            if (cmp == 1) {
                if (date.before(getDate())) {
                    setFieldError("Wybierz datę po " + date.toLocaleString());
                    return true;
                }
            }
            if (cmp == 2) {
                if (date.before(getDate())) {
                    setFieldError("Wybierz datę sprzed " + date.toLocaleString());
                    return true;
                }
            }
            remFieldError();
            return true;
        }

        public Date getDate() {
            return new Date(rok.getSelectedIndex() + startYear, miesiac.getSelectedIndex(), dzien.getSelectedIndex() + 1);
        }

        public void adjustDate() {
            int q = dzien.getSelectedIndex();
            dzien.clear();
            int dni = new Date(rok.getSelectedIndex() + startYear, miesiac.getSelectedIndex() + 1, 0).getDate();
            for (int x = 1; x <= dni; x++) dzien.addItem(String.valueOf(x));
            dzien.setSelectedIndex(q < dni ? q : (dni - 1));
        }

        public Widget getDataWidet() {
            if (horizontalPanel == null) {
                horizontalPanel = new HorizontalPanel();
                rok = new ListBox();
                horizontalPanel.add(rok);
                for (int x = startYear; x <= startYear + 10; x++) rok.addItem(String.valueOf(x + 1900));
                rok.setSelectedIndex(new Date().getYear() - startYear);
                rok.addChangeListener(new RokChangeListener());
                miesiac = new ListBox();
                horizontalPanel.add(miesiac);
                miesiac.addItem("Styczeń");
                miesiac.addItem("Luty");
                miesiac.addItem("Marzec");
                miesiac.addItem("Kwiecień");
                miesiac.addItem("Maj");
                miesiac.addItem("Czerwiec");
                miesiac.addItem("Lipiec");
                miesiac.addItem("Sierpień");
                miesiac.addItem("Wrzesień");
                miesiac.addItem("Październik");
                miesiac.addItem("Listopad");
                miesiac.addItem("Grudzień");
                miesiac.setSelectedIndex(date.getMonth());
                miesiac.addChangeListener(new MiesiacChangeListener());
                dzien = new ListBox();
                horizontalPanel.add(dzien);
                adjustDate();
                dzien.setSelectedIndex(date.getDate() - 1);
            }
            return horizontalPanel;
        }
    }

    private static class FormDialog extends DialogBox {

        public FormDialog(String text, AbstractForm form) {
            setText(text);
            setWidget(form);
        }
    }

    ;

    public class DataTableField extends AbstractFormField {

        ArrayList argsy = new ArrayList();

        VerticalPanel verticalPanel;

        private ListBox listBox;

        FormDialog dialog;

        public class WybierzDaty extends AbstractForm {

            DataField data = new DataField("Dzień", new Date(), 1);

            TimeField time1 = new TimeField("Od Godziny", new Date());

            TimeField time2 = new TimeField("Do Godziny", new Date());

            public WybierzDaty() {
                super("Dodaj");
                addField(data);
                addField(time1);
                addField(time2);
            }

            public void doRequest() {
                if (time1.getHour() > time2.getHour() || (time1.getHour() == time2.getHour() && time1.getMinute() > time2.getMinute())) {
                    setError("Czas do musi być puźniejszy");
                    return;
                }
                dialog.hide();
                dialog = null;
                Date d1 = data.getDate();
                Date d2 = (Date) d1.clone();
                d1.setHours(time1.getHour());
                d1.setMinutes(time1.getMinute());
                d2.setHours(time2.getHour());
                d2.setMinutes(time2.getMinute());
                addTermin(d1, d2);
            }
        }

        public void addTermin(Date from, Date to) {
            Date[] p = { from, to };
            argsy.add(p);
            listBox.addItem(from.toLocaleString() + " - " + to.toLocaleString());
        }

        public Date[][] getTerminy() {
            Date[][] d = new Date[argsy.size()][2];
            for (int x = 0; x < d.length; x++) {
                d[x] = (Date[]) argsy.get(x);
            }
            return d;
        }

        private class ButtonAddClick implements ClickListener {

            public void onClick(Widget sender) {
                AbstractForm f = new WybierzDaty();
                dialog = new FormDialog("Wybierz Termin", f);
                dialog.center();
                dialog.show();
            }
        }

        private class ButtonRemClick implements ClickListener {

            public void onClick(Widget sender) {
                int v = listBox.getSelectedIndex();
                if (v < 0) return;
                argsy.remove(v);
                listBox.removeItem(v);
            }
        }

        DataTableField(String name) {
            super(name);
        }

        public boolean apply() {
            return true;
        }

        public Widget getDataWidet() {
            if (verticalPanel == null) {
                verticalPanel = new VerticalPanel();
                listBox = new ListBox();
                verticalPanel.add(listBox);
                listBox.setVisibleItemCount(5);
                HorizontalPanel h = new HorizontalPanel();
                verticalPanel.add(h);
                h.add(new Button("Dodaj nowy", new ButtonAddClick()));
                h.add(new Button("Usuń", new ButtonRemClick()));
            }
            return verticalPanel;
        }
    }

    public class TextField extends AbstractFormField {

        String regex;

        TextBox dataWidget;

        TextField(String name, String regex) {
            super(name);
            this.regex = regex;
        }

        public boolean apply() {
            String s = dataWidget.getText();
            if (regex == null) {
                remFieldError();
                return true;
            }
            if (s == null) {
                setFieldError("To pole jest wymagane.");
                return false;
            }
            if (!s.matches(regex)) {
                setFieldError("Wpisz poprawnie dane.");
                return false;
            }
            remFieldError();
            return true;
        }

        public Widget getDataWidet() {
            if (dataWidget == null) {
                dataWidget = new TextBox();
            }
            return dataWidget;
        }

        public String getText() {
            return dataWidget.getText();
        }
    }

    public class PasswordField extends AbstractFormField {

        int minsize;

        int maxsize;

        PasswordTextBox dataWidget;

        PasswordField(String name, int minsize, int maxsize) {
            super(name);
            this.minsize = minsize;
            this.maxsize = maxsize;
        }

        public boolean apply() {
            String s = dataWidget.getText();
            if (s == null) {
                setFieldError("To pole jest wymagane.");
                return false;
            }
            if (!s.matches("[A-Za-z0-9]+")) {
                setFieldError("Wpisz same litery i cyfry.");
                return false;
            }
            if (s.length() < minsize) {
                setFieldError("Hasło za krótkie, minimum " + minsize + " liter.");
                return false;
            }
            if (s.length() > maxsize) {
                setFieldError("Hasło za długie, maksimum " + maxsize + " liter.");
                return false;
            }
            remFieldError();
            return true;
        }

        public Widget getDataWidet() {
            if (dataWidget == null) {
                dataWidget = new PasswordTextBox();
            }
            return dataWidget;
        }

        public String getText() {
            return dataWidget.getText();
        }
    }

    AbstractForm(String processname) {
        VerticalPanel verticalPanel = new VerticalPanel();
        initWidget(verticalPanel);
        flexTable = new FlexTable();
        verticalPanel.add(flexTable);
        infoLabel = new Label("Przetważanie zapytania...");
        infoLabel.setVisible(false);
        verticalPanel.add(infoLabel);
        processButton = new Button();
        processButton.addClickListener(new ProcessButtonClickListener());
        processButton.setText(processname);
        verticalPanel.add(processButton);
    }

    public void setInfo(String info) {
        infoLabel.setStyleName("gwt-Label-Message");
        infoLabel.setText(info);
        infoLabel.setVisible(true);
    }

    public void setError(String err) {
        infoLabel.setStyleName("gwt-Label-Error");
        infoLabel.setText(err);
        infoLabel.setVisible(true);
    }

    public abstract void doRequest();

    public void addField(AbstractFormField pole) {
        int i = pola.size();
        pola.add(pole);
        flexTable.setWidget(i * 2, 0, pole.getNameWidet());
        flexTable.setWidget(i * 2, 1, pole.getDataWidet());
        flexTable.setWidget(i * 2 + 1, 1, pole.getErrorWidet());
    }

    private class ProcessButtonClickListener implements ClickListener {

        public void onClick(final Widget sender) {
            String s = "";
            for (int i = 0; i < pola.size(); i++) {
                AbstractFormField p = (AbstractFormField) pola.get(i);
                if (!p.apply()) {
                    s = "Wypełnij poprawnie pole " + p.name + ".\n";
                }
            }
            if (s.length() > 0) {
                setError(s);
            } else {
                setInfo("Proszę czekać...");
                doRequest();
            }
        }
    }
}
