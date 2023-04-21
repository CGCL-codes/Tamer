package com.xsm.gwt.widgets.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Add this Demo to your EntryPoint's Root to see the demo.
 * 
 * Scroll down in the build() method to see AxisPanel2 being used in a single
 *  expression to layout your interactive widgets (like buttons, textboxes etc.).
 * <br>
 * Interactive widgets as they come into existence are placed above the layout
 *  or in a separate method with their appropriate Listeners.
 *      
 * @author Sony Mathew
 */
public class AxisPanel2DemoPanel extends AxisPanel2 {

    private static final String EMPTY = "EMPTY";

    private SuggestBox searchByNameBox = null;

    private RadioButton searchByNameOption = null;

    private RadioButton searchTop10Option = null;

    private ListBox searchTop10Box = null;

    private FlexTable searchResultsTable = null;

    private FlexTable itemCartTable = null;

    /**
     * Build this panel.  Provide the pixel width and height to build to.
     */
    public AxisPanel2DemoPanel build(int pxWidth, int pxHeight) {
        String searchOptions = "search-options";
        searchByNameOption = new RadioButton(searchOptions, "Enter item name:", true);
        searchByNameBox = new SuggestBox(createSearchByNameOracle());
        searchByNameOption.setValue(true);
        searchByNameBox.addValueChangeHandler(new ValueChangeHandler<String>() {

            public void onValueChange(ValueChangeEvent<String> event) {
                searchByNameOption.setValue(true);
            }
        });
        List<Hyperlink> alphaLinks = new ArrayList<Hyperlink>();
        char[] alphas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (final char alpha : alphas) {
            Hyperlink alphaLink = new Hyperlink("" + alpha, "");
            alphaLink.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent e) {
                    searchByNameBox.setText(searchByNameBox.getText().trim() + alpha);
                    searchByNameOption.setValue(true);
                }
            });
            alphaLinks.add(alphaLink);
        }
        Hyperlink alphaLink = new Hyperlink("<<", "");
        alphaLink.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent e) {
                String search = searchByNameBox.getText().trim();
                if (search.length() > 0) {
                    search = search.substring(0, search.length() - 1);
                }
                searchByNameBox.setText(search);
                searchByNameOption.setValue(true);
            }
        });
        alphaLinks.add(alphaLink);
        searchTop10Option = new RadioButton(searchOptions, "Select Top 10 items:", true);
        searchTop10Box = new ListBox();
        List<String> top10 = getTop10ItemNames();
        for (String itemName : top10) {
            searchTop10Box.addItem(itemName);
        }
        final Button searchButton = new Button("Search");
        searchTop10Box.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent e) {
                searchTop10Option.setValue(true);
                searchButton.setFocus(true);
            }
        });
        searchButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent e) {
                findItems();
            }
        });
        searchResultsTable = new FlexTable();
        searchResultsTable.setWidth("200px");
        searchResultsTable.setHeight("70%");
        showEmptyResultsView();
        final Hyperlink pagePrevLink = new Hyperlink("<prev", "");
        final Hyperlink pageNextLink = new Hyperlink("next>", "");
        ClickHandler pageListener = new ClickHandler() {

            public void onClick(ClickEvent e) {
                if (e.getSource() == pagePrevLink) {
                    searchResultsPagePrevious();
                } else if (e.getSource() == pageNextLink) {
                    searchResultsPageNext();
                } else {
                    GWT.log("Error: ItemSearchPanel: Results paging click soruce not recognized", null);
                }
                refreshResultsView();
            }
        };
        pagePrevLink.addClickHandler(pageListener);
        pageNextLink.addClickHandler(pageListener);
        Image arrowImage = new Image("arrow_right.gif");
        itemCartTable = new FlexTable();
        itemCartTable.setWidth("200px");
        itemCartTable.setHeight("70%");
        showEmptyCartView();
        Hyperlink itemCartRemoveAllLink = new Hyperlink("Remove all", "");
        itemCartRemoveAllLink.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent e) {
                clearItemCartView();
                showEmptyCartView();
            }
        });
        Button checkOutButton = new Button("Check Out");
        checkOutButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent e) {
                Window.alert("You have been checked out");
            }
        });
        this.id("AxisPanel2Demo").y().idExt("Shopping").y().idExt("SearchOptions").styleTree("color", "red").css("searchOptions").cssChilds("searchOptionsChild").x("Search Options").idExt("Header").q().y().idExt("ByName").x(searchByNameOption, searchByNameBox).q().x(alphaLinks).css("AlphaLinks").q().q().x(searchTop10Option, searchTop10Box).idExt("ByTop10").q().x(searchButton).idExt("SearchButton").q().q().y().idExt("ResultsArea").styleTree("color", "green").css("resultsArea").cssDescends("resultsAreaDescend").y().idExt("Info").x("Search Results").idExt("Header").q().x("Click Cart+ to move items to cart, X to remove.").idExt("Howto").q().q().x().idExt("Details").y().idExt("YourResults").x("Your Results").idExt("Header").q().x(searchResultsTable).idExt("Table").q().x().css("PagingControls").treeStretchWidth().alignChilds(0, 0).put(pagePrevLink, new Label("page"), pageNextLink).q().q().x(arrowImage).idExt("ArrowImage").align(0, 0).q().y().idExt("YourCart").x("Your Cart").idExt("Header").q().x(itemCartTable).idExt("Table").q().x().idExt("RemoveAllLink").align(0, 0).put(itemCartRemoveAllLink).q().q().q().x(checkOutButton).idExt("CheckoutButton").q().q().q();
        return this;
    }

    /**
     * TODO: Create Oracle from real item names.
     * 
     * author Sony Mathew
     */
    private SuggestOracle createSearchByNameOracle() {
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        List<String> top10 = getTop10ItemNames();
        for (String item : top10) {
            oracle.add(item);
        }
        return oracle;
    }

    private void findItems() {
        String itemName = null;
        if (searchByNameOption.getValue()) {
            itemName = searchByNameBox.getText();
        } else if (searchTop10Option.getValue()) {
            if (searchTop10Box.getSelectedIndex() > -1) {
                itemName = getTop10ItemNames().get(searchTop10Box.getSelectedIndex());
            }
        }
        if (itemName == null || (itemName = itemName.trim()).length() <= 0) {
            Window.alert("You didn't enter a item name");
            return;
        }
        findItemsByName(itemName);
        refreshResultsView();
    }

    private void refreshResultsView() {
        clearResultsView();
        final List<String> itemNames = getSearchResultsPage();
        if (itemNames == null || itemNames.size() == 0) {
            showEmptyResultsView();
            return;
        }
        for (int i = 0; i < itemNames.size(); i++) {
            final String itemName = itemNames.get(i);
            searchResultsTable.setText(i, 0, itemName);
            Hyperlink toCartLink = new Hyperlink("Cart+", "");
            searchResultsTable.setWidget(i, 1, toCartLink);
            toCartLink.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent e) {
                    addToCart(itemName);
                }
            });
        }
        for (int i = 0; i < searchResultsTable.getRowCount(); i++) {
            if (i % 2 == 0) {
                searchResultsTable.getRowFormatter().setStylePrimaryName(i, "search-results-detail-table-row-even");
            } else {
                searchResultsTable.getRowFormatter().setStylePrimaryName(i, "search-results-detail-table-row-odd");
            }
        }
    }

    private void clearResultsView() {
        while (searchResultsTable.getRowCount() > 0) {
            searchResultsTable.removeRow(0);
        }
    }

    private void showEmptyResultsView() {
        searchResultsTable.setText(0, 0, EMPTY);
        searchResultsTable.setText(0, 1, "  ");
    }

    private void addToCart(final String itemName) {
        if (getItemIndexInCart(itemName) > -1) {
            return;
        }
        addToItemNameCart(itemName);
        removeEmptyFromCartView();
        int row = itemCartTable.getRowCount();
        if (row == 7) {
            Window.alert("Sorry, only 7 items can be selected");
            return;
        }
        Hyperlink removeLink = new Hyperlink("X", "");
        itemCartTable.setWidget(row, 0, removeLink);
        itemCartTable.setText(row, 1, itemName);
        removeLink.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent e) {
                removeFromCart(itemName);
            }
        });
        styleCartRows();
    }

    /**
     * Returns -1 if none found.
     * 
     * author Sony Mathew
     */
    private int getItemIndexInCart(String itemName) {
        for (int i = 0; i < itemCartTable.getRowCount(); i++) {
            String rowItemName = itemCartTable.getText(i, 1);
            if (rowItemName.equals(itemName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Will add back EMPTY if no rows left.
     * 
     * author Sony Mathew
     */
    private void removeFromCart(String itemName) {
        int i = getItemIndexInCart(itemName);
        if (i > -1) {
            removeFromItemNameCart(itemName);
            itemCartTable.removeRow(i);
        }
        if (itemCartTable.getRowCount() == 0) {
            showEmptyCartView();
        }
        styleCartRows();
    }

    private void removeEmptyFromCartView() {
        int i = getItemIndexInCart(EMPTY);
        if (i > -1) {
            if (itemCartTable.getRowCount() != 1) {
                GWT.log("Error: Showing EMPTY in Cart when rows exist", null);
            }
            itemCartTable.removeRow(i);
        }
    }

    private void clearItemCartView() {
        clearItemNameCart();
        while (itemCartTable.getRowCount() > 0) {
            itemCartTable.removeRow(0);
        }
    }

    private void showEmptyCartView() {
        if (itemCartTable.getRowCount() != 0) {
            GWT.log("Error: Showing EMPTY in Cart when rows exist", null);
        }
        itemCartTable.setText(0, 0, " ");
        itemCartTable.setText(0, 1, EMPTY);
    }

    private void styleCartRows() {
        for (int i = 0; i < itemCartTable.getRowCount(); i++) {
            if (i % 2 == 0) {
                itemCartTable.getRowFormatter().setStylePrimaryName(i, "search-results-cart-table-row-even");
            } else {
                itemCartTable.getRowFormatter().setStylePrimaryName(i, "search-results-cart-table-row-odd");
            }
        }
    }

    private List<String> getTop10ItemNames() {
        return Arrays.asList("HDTV", "IPod", "XBox", "PS3", "Wii", "HDMI", "PC", "IPhone", "GPhone", "iMac");
    }

    private void searchResultsPagePrevious() {
        pageNum = (pageNum > 0) ? pageNum - 1 : pageNum;
    }

    private void searchResultsPageNext() {
        pageNum++;
    }

    private void findItemsByName(String itemName) {
        searchStr = itemName;
    }

    private List<String> getSearchResultsPage() {
        if (searchStr == null || searchStr.length() == 0) {
            return Collections.emptyList();
        }
        List<String> page = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
            page.add(searchStr + "-" + i + "-" + pageNum);
        }
        return page;
    }

    private void addToItemNameCart(String itemName) {
    }

    private void removeFromItemNameCart(String itemName) {
    }

    private void clearItemNameCart() {
    }

    private int pageNum = 0;

    private String searchStr = "";
}
