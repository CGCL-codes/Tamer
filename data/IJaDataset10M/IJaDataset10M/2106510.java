package com.moogleapps.hello1.client;

import com.google.gwt.accounts.client.AuthSubStatus;
import com.google.gwt.accounts.client.User;
import com.google.gwt.gdata.client.finance.FinanceService;
import com.google.gwt.gdata.client.finance.PortfolioEntry;
import com.google.gwt.gdata.client.finance.PortfolioFeed;
import com.google.gwt.gdata.client.finance.PortfolioFeedCallback;
import com.google.gwt.gdata.client.impl.CallErrorException;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * The following example demonstrates how to retrieve a list of a
 * user's portfolios.
 */
public class FinanceRetrievePortfoliosDemo extends GDataDemo {

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
                return new FinanceRetrievePortfoliosDemo();
            }

            @Override
            public String getDescription() {
                return "<p>This sample uses the portfolio feed to retrieve a " + "list of all of the authenticated user's portfolios. The title " + "and ID of each portfolio entry is printed.</p>";
            }

            @Override
            public String getName() {
                return "Finance - Retrieving all portfolios";
            }
        };
    }

    private FinanceService service;

    private FlexTable mainPanel;

    private final String scope = "http://finance.google.com/finance/feeds/";

    /**
   * Setup the Finance service and create the main content panel.
   * If the user is not logged on to Finance display a message,
   * otherwise start the demo by retrieving the user's portfolios.
   */
    public FinanceRetrievePortfoliosDemo() {
        service = FinanceService.newInstance("HelloGData_Finance_RetrievePortfoliosDemo_v1.0");
        mainPanel = new FlexTable();
        initWidget(mainPanel);
        if (User.getStatus(scope) == AuthSubStatus.LOGGED_IN) {
            getPortfolios("http://finance.google.com/finance/feeds/default/portfolios");
        } else {
            showStatus("You are not logged on to Google Finance.", true);
        }
    }

    /**
   * Retrieve the portfolios feed using the Finance service and
   * the portfolios feed uri. In GData all get, insert, update
   * and delete methods always receive a callback defining
   * success and failure handlers.
   * Here, the failure handler displays an error message while the
   * success handler calls showData to display the portfolio entries.
   * 
   * @param portfoliosFeedUri The uri of the portfolios feed
   */
    private void getPortfolios(String portfoliosFeedUri) {
        showStatus("Loading portfolios feed...", false);
        service.getPortfolioFeed(portfoliosFeedUri, new PortfolioFeedCallback() {

            public void onFailure(CallErrorException caught) {
                showStatus("An error occurred while retrieving the portfolios " + "feed: " + caught.getMessage(), true);
            }

            public void onSuccess(PortfolioFeed result) {
                PortfolioEntry[] entries = result.getEntries();
                if (entries.length == 0) {
                    showStatus("You have no portfolios.", false);
                } else {
                    showData(entries);
                }
            }
        });
    }

    /**
  * Displays a set of Finance portfolio entries in a tabular 
  * fashion with the help of a GWT FlexTable widget. The data fields 
  * Title and ID are displayed.
  * 
  * @param entries The Finance portfolio entries to display.
  */
    private void showData(PortfolioEntry[] entries) {
        mainPanel.clear();
        String[] labels = new String[] { "Title", "ID" };
        mainPanel.insertRow(0);
        for (int i = 0; i < labels.length; i++) {
            mainPanel.addCell(0);
            mainPanel.setWidget(0, i, new Label(labels[i]));
            mainPanel.getFlexCellFormatter().setStyleName(0, i, "hm-tableheader");
        }
        for (int i = 0; i < entries.length; i++) {
            PortfolioEntry entry = entries[i];
            int row = mainPanel.insertRow(i + 1);
            mainPanel.addCell(row);
            mainPanel.setWidget(row, 0, new Label(entry.getTitle().getText()));
            mainPanel.addCell(row);
            mainPanel.setWidget(row, 1, new Label(entry.getId().getValue()));
        }
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
