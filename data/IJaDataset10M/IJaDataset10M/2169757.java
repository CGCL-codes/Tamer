package org.mobicents.slee.container.management.console.client.pages;

import org.mobicents.slee.container.management.console.client.common.CardControl;
import org.mobicents.slee.container.management.console.client.common.SmartTabPage;
import org.mobicents.slee.container.management.console.client.sleestate.SleeStateCard;

public class SleePage extends SmartTabPage {

    private SleeStateCard sleeStateCard;

    private CardControl cardControl = new CardControl();

    public SleePage() {
        initWidget(cardControl);
    }

    public static SmartTabPageInfo getInfo() {
        return new SmartTabPageInfo("<image src='images/slee.gif' /> JAIN SLEE ", "SLEE Management") {

            protected SmartTabPage createInstance() {
                return new SleePage();
            }
        };
    }

    public void onHide() {
        cardControl.onHide();
    }

    public void onShow() {
        cardControl.onShow();
    }

    public void onInit() {
        sleeStateCard = new SleeStateCard();
        cardControl.onInit();
        cardControl.add(sleeStateCard, "<image align='absbottom' src='images/sleestate.gif' /> JAIN SLEE State", true);
        cardControl.setWidth("100%");
        cardControl.selectTab(0);
    }
}
