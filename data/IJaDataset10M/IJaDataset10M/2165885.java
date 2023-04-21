package org.mobicents.slee.container.management.console.client.common;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * @author Stefano Zappaterra
 * 
 */
public class CardControl extends Composite implements TabListener, CommonControl {

    private boolean firstTabSelectionEvent = true;

    private TabPanel tabPanel = new TabPanel();

    private Card selectedCard = null;

    public CardControl() {
        super();
        tabPanel.addTabListener(this);
        initWidget(tabPanel);
    }

    public void add(Card card, String tabText, boolean asHTML) {
        tabPanel.add(card, tabText, asHTML);
    }

    public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
        Card newSelectedCard = (Card) tabPanel.getWidget(tabIndex);
        if (newSelectedCard == null) return;
        if (newSelectedCard == selectedCard) return;
        if (selectedCard != null) selectedCard.onUnselect();
        newSelectedCard.onSelect();
        if (firstTabSelectionEvent) firstTabSelectionEvent = false; else newSelectedCard.onShow();
        selectedCard = newSelectedCard;
    }

    public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
        return true;
    }

    public void selectTab(int index) {
        tabPanel.selectTab(index);
    }

    public void onHide() {
        if (selectedCard != null) selectedCard.onHide();
    }

    public void onInit() {
    }

    public void onShow() {
        if (selectedCard != null) selectedCard.onShow();
    }
}
