package com.ncs.crm.client.data;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;

public class ServerCountLabel extends Label {

    private String contents = "";

    private int total = 0;

    public ServerCountLabel() {
        setAlign(Alignment.CENTER);
        setHeight(20);
        setContents(contents);
        addShowContextMenuHandler(new ShowContextMenuHandler() {

            public void onShowContextMenu(ShowContextMenuEvent event) {
                event.cancel();
            }
        });
    }

    public void incrementAndUpdate(int t, int start, int end) {
        System.out.println("运行 incrementAndUpdate ...");
        this.total = t;
        contents = "总记录数: <b>" + total + "</b> , 当前: <b>" + start + "</b> 至 <b>" + end + "</b>";
        this.setContents(contents);
        setBackgroundColor("yellow");
        new Timer() {

            public void run() {
                setBackgroundColor("ffffff");
            }
        }.schedule(1000);
    }

    public void incrementAndUpdate2(int start, int end) {
        System.out.println("运行 incrementAndUpdate2 ...");
        if (total < end) {
            total = end;
        }
        contents = "总记录数: <b>" + total + "</b> , 当前: <b>" + start + "</b> 至 <b>" + end + "</b>";
        this.setContents(contents);
        setBackgroundColor("yellow");
        new Timer() {

            public void run() {
                setBackgroundColor("ffffff");
            }
        }.schedule(1000);
    }

    public void incrementAndUpdate3(int start, int end) {
        if (total < end) {
            total = end;
        }
        contents = "总员工数: <b>" + total + "</b> , 当前有: <b>" + start + "</b> 至 <b>" + end + "</b> 位员工";
        this.setContents(contents);
        setBackgroundColor("yellow");
        new Timer() {

            public void run() {
                setBackgroundColor("ffffff");
            }
        }.schedule(1000);
    }
}
