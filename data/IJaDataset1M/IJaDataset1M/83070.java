package com.goodow.web.view.wave.client;

import com.goodow.web.view.wave.client.panel.WavePanel;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import java.util.logging.Logger;

public class WaveTest extends WavePanel {

    private static final Logger logger = Logger.getLogger(WaveTest.class.getName());

    private boolean start = false;

    private int x1;

    private int x2;

    JsArray<Touch> touches = null;

    private boolean scheduled;

    private boolean isStart = false;

    public WaveTest() {
        HTMLPanel hp = new HTMLPanel("");
        hp.setWidth("100%");
        hp.setHeight("700px");
        setWaveContent(hp);
        hp.addDomHandler(new TouchStartHandler() {

            @Override
            public void onTouchStart(final TouchStartEvent event) {
                JsArray<Touch> toucheStart = event.getTouches();
                if (toucheStart.length() >= 2) {
                    logger.info("touch start:" + toucheStart.length());
                    isStart = true;
                    Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {

                        @Override
                        public boolean execute() {
                            if (touches != null && isStart && !scheduled) {
                                scheduled = true;
                                Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                                    @Override
                                    public void execute() {
                                        scheduled = false;
                                        printLog(touches);
                                    }
                                });
                            }
                            if (!isStart) {
                                logger.info("Scheduler end:" + isStart);
                            }
                            return isStart;
                        }
                    }, 15);
                }
            }
        }, TouchStartEvent.getType());
        hp.addDomHandler(new TouchMoveHandler() {

            @Override
            public void onTouchMove(final TouchMoveEvent event) {
                touches = event.getTouches();
            }
        }, TouchMoveEvent.getType());
        hp.addDomHandler(new TouchEndHandler() {

            @Override
            public void onTouchEnd(final TouchEndEvent event) {
                touches = null;
                logger.info("strat end:");
                isStart = false;
            }
        }, TouchEndEvent.getType());
    }

    private void printLog(final JsArray<Touch> touches) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < touches.length(); i++) {
            Touch touch = touches.get(i);
            sb.append("{touch-id:" + touch.getIdentifier());
            sb.append(";touch-pageX:" + touch.getPageX());
            sb.append(";touch-pageY:" + touch.getPageY());
            sb.append(";touch-clientX:" + touch.getClientX());
            sb.append(";touch-clientY:" + touch.getClientY());
            sb.append(";touch-screenX:" + touch.getScreenX());
            sb.append(";touch-screenY:" + touch.getScreenY());
            sb.append("}");
        }
        logger.info(sb.toString());
    }
}
