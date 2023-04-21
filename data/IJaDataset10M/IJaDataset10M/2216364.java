package com.google.gwt.user.client.ui;

/**
 * A {@link TabPanel} that uses a {@link DecoratedTabBar} with rounded corners.
 * 
 * <p>
 * This widget will <em>only</em> work in quirks mode. If your application is in
 * Standards Mode, use {@link TabLayoutPanel} instead.
 * </p>
 * 
 * <p>
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-DecoratedTabPanel { the tab panel itself }</li>
 * <li>.gwt-TabPanelBottom { the bottom section of the tab panel (the deck
 * containing the widget) }</li>
 * </ul>
 * </p>
 * 
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.TabPanelExample}
 * </p>
 * 
 * @see TabLayoutPanel
 */
public class DecoratedTabPanel extends TabPanel {

    private static final String DEFAULT_STYLENAME = "gwt-DecoratedTabPanel";

    public DecoratedTabPanel() {
        setStylePrimaryName(DEFAULT_STYLENAME);
        getTabBar().setStylePrimaryName(DecoratedTabBar.STYLENAME_DEFAULT);
    }

    @Override
    protected SimplePanel createTabTextWrapper() {
        return new DecoratorPanel(DecoratedTabBar.TAB_ROW_STYLES, 1);
    }
}
