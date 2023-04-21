package org.mobicents.slee.container.management.console.client.pages;

import org.mobicents.slee.container.management.console.client.activity.ActivityCard;
import org.mobicents.slee.container.management.console.client.common.CardControl;
import org.mobicents.slee.container.management.console.client.common.SmartTabPage;

public class ActivityPage extends SmartTabPage {

    private CardControl cardControl = new CardControl();

    public ActivityPage() {
        initWidget(cardControl);
    }

    public static SmartTabPageInfo getInfo() {
        return new SmartTabPageInfo("<image src='images/activity.context.gif' /> Activities", "Activities") {

            protected SmartTabPage createInstance() {
                return new ActivityPage();
            }
        };
    }

    public void onInit() {
        ActivityCard activityBrowserCard = new ActivityCard();
        cardControl.add(activityBrowserCard, "<image align='absbottom' src='images/activity.context.gif' /> Browse Activities", true);
        cardControl.selectTab(0);
        cardControl.setWidth("100%");
    }

    public void onHide() {
        cardControl.onHide();
    }

    public void onShow() {
        cardControl.onShow();
    }
}
