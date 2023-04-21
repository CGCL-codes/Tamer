package com.google.gwt.ricordo.client.view;

import java.util.List;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.ricordo.client.presenter.AnnotationPresenter;
import com.google.gwt.ricordo.shared.VariableDetailsLight;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnnotationView extends RicordoView implements AnnotationPresenter.Display {

    private final Button searchButton = new Button("Search");

    private final BackToMainButton backToMainButton = new BackToMainButton();

    private final Button addButton = new Button("Create new RDF model");

    private ResultsPanel resultsPanel;

    private final FlexTable searchTable = new FlexTable();

    private final TextBox modelText = new TextBox();

    public AnnotationView() {
        super(TitlePanel.ANNOT_TITLE);
        contentDetailsPanel.add(discriptionPanel());
        contentDetailsPanel.add(createSearchPanel());
        contentDetailsPanel.add(createResultsPanel());
    }

    private VerticalPanel discriptionPanel() {
        VerticalPanel discriptionPanel = new VerticalPanel();
        discriptionPanel.setStyleName("pannel-Border");
        discriptionPanel.setWidth("100%");
        Label discriptionLabel = new Label("This application supports annotations of Virtual Physiological Human data and models (VPHDM)s.");
        discriptionPanel.add(discriptionLabel);
        return discriptionPanel;
    }

    private VerticalPanel createSearchPanel() {
        VerticalPanel searchPanel = new VerticalPanel();
        searchPanel.setStyleName("pannel-Border");
        searchPanel.setWidth("100%");
        searchTable.getColumnFormatter().setWidth(0, "150em");
        searchTable.setWidget(0, 0, new Label("Model url"));
        searchTable.setWidget(0, 1, modelText);
        modelText.setWidth("400");
        searchTable.setWidget(1, 1, createButtonPanel());
        searchPanel.add(searchTable);
        return searchPanel;
    }

    public HorizontalPanel createButtonPanel() {
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(10);
        buttonPanel.add(searchButton);
        buttonPanel.add(backToMainButton);
        return buttonPanel;
    }

    public void setSearchButtonStatus(boolean status) {
        if (status) {
            searchButton.setText("Search");
        } else {
            searchButton.setText("Searching...");
        }
        searchButton.setEnabled(status);
    }

    private VerticalPanel createResultsPanel() {
        resultsPanel = new ResultsPanel();
        resultsPanel.setResultTableSize(new String[] { "80px", "500px" });
        return resultsPanel;
    }

    public String getModelText() {
        return modelText.getText();
    }

    public void setModelText(String modelTextString) {
        modelText.setText(modelTextString);
    }

    public HasClickHandlers getSearchButton() {
        return searchButton;
    }

    public HasClickHandlers getAddButton() {
        return addButton;
    }

    public BackToMainButton getBackToMainButton() {
        return backToMainButton;
    }

    public HasClickHandlers getList() {
        return resultsPanel.getResultsTable();
    }

    public void clearResultPanel() {
        resultsPanel.setResultsLabel("");
        resultsPanel.clearResultTable();
    }

    public void setData(List<VariableDetailsLight> data) {
        resultsPanel.setResultsLabel("");
        resultsPanel.clearResultTable();
        if (data.isEmpty()) {
            return;
        }
        resultsPanel.addResultRow(0, new String[] { "Index", "Variable URL" });
        for (int i = 0; i < data.size(); ++i) {
            resultsPanel.addResultRow(i + 1, data.get(i).getDisplayContent());
        }
        resultsPanel.applyResultTableStyles();
    }

    public void setNoResultsLabel(List<VariableDetailsLight> data) {
        if (data.isEmpty()) {
            resultsPanel.setResultsLabel("No results found for this query");
            return;
        }
    }

    public int getClickedRow(ClickEvent event) {
        int selectedRow = -1;
        HTMLTable.Cell cell = resultsPanel.getResultsTable().getCellForEvent(event);
        if (cell != null) {
            if (cell.getCellIndex() > 0) {
                selectedRow = cell.getRowIndex();
            }
        }
        return selectedRow;
    }

    public Widget asWidget() {
        return this;
    }
}
