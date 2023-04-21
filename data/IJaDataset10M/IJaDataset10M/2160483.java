package net.chanibal.hala.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Kielnci extends Composite implements AsyncCallback, ClickListener {

    private Label rachunekLabel;

    private Label label_3;

    private Label loginLabel;

    private Grid grid;

    private Label label_1;

    private Label label;

    private VerticalPanel verticalPanel;

    private Kielnci selfKielnci = this;

    public Kielnci() {
        verticalPanel = new VerticalPanel();
        initWidget(verticalPanel);
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.add(new Button("Odświerz", new ClickListener() {

            public void onClick(Widget sender) {
                label_1.setText("Pobieranie bazy danych");
                Server.Util.getInstance().getKlienci(selfKielnci);
            }
        }));
        label = new Label("Tabela Klientów");
        verticalPanel.add(label);
        label_1 = new Label("Pobieranie bazy danych");
        label_1.setStyleName("gwt-Label-Message");
        verticalPanel.add(label_1);
        grid = new Grid();
        grid.setStyleName("dane");
        verticalPanel.add(grid);
        grid.resize(10, 3);
        loginLabel = new Label("login");
        grid.setWidget(0, 0, loginLabel);
        label_3 = new Label("Imię i nazwisko");
        grid.setWidget(0, 1, label_3);
        rachunekLabel = new Label("Saldo");
        grid.setWidget(0, 2, rachunekLabel);
        Server.Util.getInstance().getKlienci(this);
    }

    public void onFailure(Throwable caught) {
        label_1.setText(caught.getLocalizedMessage());
        label_1.setStyleName("gwt-Label-Error");
    }

    public void onSuccess(Object result) {
        String[][] s = (String[][]) result;
        grid.resize(s.length + 1, 3);
        for (int x = 0; x < s.length; x++) {
            Label l = new Label(s[x][0]);
            l.setStyleName("link");
            l.addClickListener(this);
            grid.setWidget(x + 1, 0, l);
            l = new Label(s[x][1]);
            grid.setWidget(x + 1, 1, l);
            l = new Label(s[x][2]);
            grid.setWidget(x + 1, 2, l);
        }
        label_1.setText("");
    }

    public void onClick(Widget sender) {
        new Klient(((Label) sender).getText()).show();
    }
}
