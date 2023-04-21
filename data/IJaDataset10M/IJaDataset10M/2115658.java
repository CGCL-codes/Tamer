package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockWatcher implements EntryPoint {

    private static final int REFRESH_INTERVAL = 5000;

    private VerticalPanel mainPanel = new VerticalPanel();

    private FlexTable stocksFlexTable = new FlexTable();

    private HorizontalPanel addPanel = new HorizontalPanel();

    private TextBox newSymbolTextBox = new TextBox();

    private Button addStockButton = new Button("Add");

    private Label lastUpdatedLabel = new Label();

    private Label errorMsgLabel = new Label();

    private ArrayList<String> stocks = new ArrayList<String>();

    private StockPriceServiceAsync stockPriceSvc;

    /**
	 * This is the entry point method.
	 */
    public void onModuleLoad() {
        stocksFlexTable.setText(0, 0, "Symbol");
        stocksFlexTable.setText(0, 1, "Price");
        stocksFlexTable.setText(0, 2, "Change");
        stocksFlexTable.setText(0, 3, "Remove");
        stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
        stocksFlexTable.addStyleName("watchList");
        stocksFlexTable.setCellPadding(6);
        addPanel.add(newSymbolTextBox);
        addPanel.add(addStockButton);
        errorMsgLabel.setStyleName("errorMessage");
        errorMsgLabel.setVisible(false);
        mainPanel.add(errorMsgLabel);
        mainPanel.add(stocksFlexTable);
        mainPanel.add(addPanel);
        mainPanel.add(lastUpdatedLabel);
        addPanel.addStyleName("addPanel");
        RootPanel.get("stockList").add(mainPanel);
        newSymbolTextBox.setFocus(true);
        Timer refreshTimer = new Timer() {

            @Override
            public void run() {
                refreshWatchList();
            }
        };
        refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
        addStockButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                addStock();
            }
        });
        newSymbolTextBox.addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                    addStock();
                }
            }
        });
        refreshWatchList();
    }

    /**
	   * Generate random stock prices.
	   */
    private void refreshWatchList() {
        if (stockPriceSvc == null) {
            stockPriceSvc = GWT.create(StockPriceService.class);
        }
        AsyncCallback<StockPrice[]> callback = new AsyncCallback<StockPrice[]>() {

            public void onFailure(Throwable caught) {
                String details = caught.getMessage();
                if (caught instanceof DelistedException) {
                    details = "Company '" + ((DelistedException) caught).getSymbol() + "' was delisted";
                }
                errorMsgLabel.setText("Error: " + details);
                errorMsgLabel.setVisible(true);
            }

            public void onSuccess(StockPrice[] result) {
                updateTable(result);
            }
        };
        stockPriceSvc.getPrices(stocks.toArray(new String[0]), callback);
    }

    /**
	   * Update the Price and Change fields all the rows in the stock table.
	   *
	   * @param prices Stock data for all rows.
	   */
    private void updateTable(StockPrice[] prices) {
        for (int i = 0; i < prices.length; i++) {
            updateTable(prices[i]);
        }
        lastUpdatedLabel.setText("Last update : " + DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
        errorMsgLabel.setVisible(false);
    }

    /**
	   * Update a single row in the stock table.
	   *
	   * @param price Stock data for a single row.
	   */
    private void updateTable(StockPrice price) {
        if (!stocks.contains(price.getSymbol())) {
            return;
        }
        int row = stocks.indexOf(price.getSymbol()) + 1;
        String priceText = NumberFormat.getFormat("#,##0.00").format(price.getPrice());
        NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
        String changeText = changeFormat.format(price.getChange());
        String changePercentText = changeFormat.format(price.getChangePercent());
        stocksFlexTable.setText(row, 1, priceText);
        Label changeWidget = (Label) stocksFlexTable.getWidget(row, 2);
        changeWidget.setText(changeText + " (" + changePercentText + "%)");
        String changeStyleName = "noChange";
        if (price.getChangePercent() < -0.1f) {
            changeStyleName = "negativeChange";
        } else if (price.getChangePercent() > 0.1f) {
            changeStyleName = "positiveChange";
        }
        changeWidget.setStyleName(changeStyleName);
    }

    private void addStock() {
        final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
        newSymbolTextBox.setFocus(true);
        if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) {
            Window.alert("'" + symbol + "' is not a valid symbol.");
            newSymbolTextBox.selectAll();
            return;
        }
        newSymbolTextBox.setText("");
        if (stocks.contains(symbol)) return;
        int row = stocksFlexTable.getRowCount();
        stocks.add(symbol);
        stocksFlexTable.setText(row, 0, symbol);
        stocksFlexTable.setWidget(row, 2, new Label());
        stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");
        Button removeStockButton = new Button("x");
        removeStockButton.addStyleDependentName("remove");
        removeStockButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                int removedIndex = stocks.indexOf(symbol);
                stocks.remove(removedIndex);
                stocksFlexTable.removeRow(removedIndex + 1);
            }
        });
        stocksFlexTable.setWidget(row, 3, removeStockButton);
    }
}
