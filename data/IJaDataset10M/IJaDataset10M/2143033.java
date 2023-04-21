package com.netprogress.abcprogress.qform.util;

import javax.swing.event.EventListenerList;
import java.util.EventListener;

public class SmartEventListenerList extends EventListenerList {

    public void fireSmartEvent(SmartEvent e) {
        Object[] list = getListenerList();
        for (int j = list.length - 2; j >= 0; j -= 2) {
            if (list[j] == e.getListenerClass()) {
                e.notifyListener((EventListener) list[j + 1]);
            }
        }
    }
}
