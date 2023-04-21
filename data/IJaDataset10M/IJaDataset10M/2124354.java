package com.cleangwt.client.ext;

import java.util.List;
import com.cleangwt.client.ext.layout.HLayout;
import com.cleangwt.client.ext.layout.kit.HGap;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

/**
 *
 * @author Jess
 * @date 2011/10/26
 */
public class Marquee extends Composite {

    private AbsolutePanel pnlImpl = new AbsolutePanel();

    private HLayout pnlMarueeDisplay = new HLayout();

    private Timer timer = new Timer() {

        @Override
        public void run() {
            if (pnlImpl.getWidgetLeft(pnlMarueeDisplay) < pnlImpl.getElement().getClientWidth()) {
                showNextSlice();
            } else {
                showNextMessage();
            }
        }
    };

    private int step = 5;

    private int speed = 300;

    /**
	 *
	 */
    public Marquee() {
        initWidget(pnlImpl);
        pnlImpl.add(pnlMarueeDisplay, 0, 0);
        pnlImpl.setSize("100%", "30px");
    }

    /**
	 *
	 * @param runMessages
	 */
    public void setMessages(List<String> runMessages) {
        reset();
        if (runMessages != null && runMessages.size() > 0) {
            for (String message : runMessages) {
                HTML html = new HTML(message);
                html.setWordWrap(false);
                pnlMarueeDisplay.add(new HGap(60));
                pnlMarueeDisplay.add(html);
            }
            pnlImpl.setWidgetPosition(pnlMarueeDisplay, -pnlMarueeDisplay.getElement().getOffsetWidth(), 0);
            timer.scheduleRepeating(speed);
        }
    }

    /**
	 *
	 */
    private void reset() {
        timer.cancel();
    }

    /**
	 *
	 */
    private void showNextMessage() {
        try {
            pnlImpl.setWidgetPosition(pnlMarueeDisplay, -pnlMarueeDisplay.getElement().getClientWidth(), 0);
        } catch (Exception e) {
            reset();
        }
    }

    /**
	 *
	 */
    private void showNextSlice() {
        pnlImpl.setWidgetPosition(pnlMarueeDisplay, pnlImpl.getWidgetLeft(pnlMarueeDisplay) + step, 0);
    }
}
