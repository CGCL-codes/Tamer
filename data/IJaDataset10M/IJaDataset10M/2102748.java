package fr.excilys.gwt.devmode.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TableManager extends Composite {

    private FlexTable table;

    private int nbRows;

    private int nbColumns;

    public TableManager() {
        VerticalPanel mainPanel = new VerticalPanel();
        FlexTable table = new FlexTable();
        table.setWidget(0, 0, generateRandomLabel());
        table.addStyleName("FlexTable");
        nbColumns = 1;
        nbRows = 1;
        mainPanel.add(table);
        HorizontalPanel controlPanel = new HorizontalPanel();
        Button addColumnButon = new Button("Ajouter une colonne");
        addColumnButon.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                addColumn();
            }
        });
        controlPanel.add(addColumnButon);
        Button addRowButon = new Button("Ajouter une ligne");
        addRowButon.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                addRow();
            }
        });
        controlPanel.add(addRowButon);
        mainPanel.add(controlPanel);
        initWidget(mainPanel);
    }

    private void addRow() {
        for (int i = 0; i < nbColumns; i++) {
            table.setWidget(nbRows, i, generateRandomLabel());
        }
        nbRows++;
    }

    private void addColumn() {
        for (int i = 0; i < nbRows; i++) {
            table.setWidget(i, nbColumns, generateRandomLabel());
        }
        nbColumns++;
    }

    private Label generateRandomLabel() {
        String random = Double.toString(Math.random());
        Label label = new Label(random);
        label.addStyleName("FlexTable-ColumnLabel");
        return label;
    }
}
