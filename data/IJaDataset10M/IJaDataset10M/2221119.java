package com.moogleapps.hello1.client;

import com.google.gwt.accounts.client.AuthSubStatus;
import com.google.gwt.accounts.client.User;
import com.google.gwt.gdata.client.atom.Text;
import com.google.gwt.gdata.client.finance.FinanceService;
import com.google.gwt.gdata.client.finance.PortfolioData;
import com.google.gwt.gdata.client.finance.PortfolioEntry;
import com.google.gwt.gdata.client.finance.PortfolioEntryCallback;
import com.google.gwt.gdata.client.impl.CallErrorException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The following example demonstrates how to create a portfolio.
 */
public class FinanceCreatePortfolioDemo extends GDataDemo {

    /**
   * This method is used by the main sample app to obtain
   * information on this sample and a sample instance.
   * 
   * @return An instance of this demo.
   */
    public static GDataDemoInfo init() {
        return new GDataDemoInfo() {

            @Override
            public GDataDemo createInstance() {
                return new FinanceCreatePortfolioDemo();
            }

            @Override
            public String getDescription() {
                return "<p>This sample demonstrates how to create and insert " + "a new portfolio. The portfolio feed post URL " + "(http://finance.google.com/finance/feeds/default/portfolios) " + "is used to insert a new portfolio entry for the authenticated " + "user.</p>";
            }

            @Override
            public String getName() {
                return "Finance - Creating a portfolio";
            }
        };
    }

    private FinanceService service;

    private FlexTable mainPanel;

    private final String scope = "http://finance.google.com/finance/feeds/";

    /**
   * Setup the Finance service and create the main content panel.
   * If the user is not logged on to Finance display a message,
   * otherwise start the demo by creating a portfolio.
   */
    private FinanceCreatePortfolioDemo() {
        service = FinanceService.newInstance("HelloGData_Finance_CreatePortfolioDemo_v1.0");
        mainPanel = new FlexTable();
        initWidget(mainPanel);
        if (User.getStatus(scope) == AuthSubStatus.LOGGED_IN) {
            Button startButton = new Button("Create a portfolio");
            startButton.addClickListener(new ClickListener() {

                public void onClick(Widget sender) {
                    createPortfolio("http://finance.google.com/finance/feeds/default/portfolios");
                }
            });
            mainPanel.setWidget(0, 0, startButton);
        } else {
            showStatus("You are not logged on to Google Finance.", true);
        }
    }

    /**
   * Create a portfolio by inserting a portfolio entry into
   * a portfolio feed.
   * Set the portfolio's title to an arbitrary string. Here
   * we prefix the title with 'GWT-Finance-Client' so that
   * we can identify which portfolios were created by this demo.
   * The new portfolio is created with currency code set to USD.
   * On success and failure, display a status message.
   * 
   * @param portfolioFeedUri The uri of the portfolio feed into which
   * to insert the portfolio entry
   */
    private void createPortfolio(String portfolioFeedUri) {
        showStatus("Creating portfolio...", false);
        PortfolioEntry entry = PortfolioEntry.newInstance();
        entry.setTitle(Text.newInstance());
        entry.getTitle().setText("GWT-Finance-Client - inserted portfolio");
        PortfolioData data = PortfolioData.newInstance();
        data.setCurrencyCode("USD");
        entry.setPortfolioData(data);
        service.insertPortfolioEntry(portfolioFeedUri, entry, new PortfolioEntryCallback() {

            public void onFailure(CallErrorException caught) {
                showStatus("An error occurred while creating a portfolio: " + caught.getMessage(), true);
            }

            public void onSuccess(PortfolioEntry result) {
                showStatus("Created a portfolio.", false);
            }
        });
    }

    /**
   * Displays a status message to the user.
   * 
   * @param message The message to display.
   * @param isError Indicates whether the status is an error status.
   */
    private void showStatus(String message, boolean isError) {
        mainPanel.clear();
        mainPanel.insertRow(0);
        mainPanel.addCell(0);
        Label msg = new Label(message);
        if (isError) {
            msg.setStylePrimaryName("hm-error");
        }
        mainPanel.setWidget(0, 0, msg);
    }
}
