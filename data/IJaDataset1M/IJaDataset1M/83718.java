package org.zkoss.gwt.client.testzk;

import java.util.Date;
import org.zkoss.gwt.client.Utils;
import org.zkoss.gwt.client.zk.Event;
import org.zkoss.gwt.client.zk.EventListener;
import org.zkoss.gwt.client.zk.Events;
import org.zkoss.gwt.client.zul.wgt.Button;
import org.zkoss.gwt.client.zul.wgt.Label;
import org.zkoss.gwt.client.zul.wnd.Window;
import com.google.gwt.user.client.ui.Widget;

public class Testcase1 extends TestcaseWrapper {

    public Widget getInstance() {
        final Window win1 = new Window();
        win1.setTitle(" outer Window!!");
        win1.setBorder("normal");
        final Button changeTitleBtn = new Button();
        ;
        changeTitleBtn.setLabel("Click Me!!!");
        final EventListener onClick = new EventListener() {

            public void onEvent(Event event) {
                win1.setTitle("Btn Clicked" + new Date());
            }
        };
        changeTitleBtn.addEventListener(Events.ON_CLICK, onClick);
        final Button unListenBtn = new Button();
        unListenBtn.setLabel("unlisten TEST");
        unListenBtn.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event event) {
                boolean r = changeTitleBtn.removeEventListener("onClick", onClick);
                if (!r) {
                    changeTitleBtn.addEventListener(Events.ON_CLICK, onClick);
                }
            }
        });
        final Button gwtBtn = new Button();
        gwtBtn.setLabel("gwt native Object TEST");
        gwtBtn.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event event) {
                Button b = (Button) Utils.getGwtWidget(gwtBtn.getZkWidget());
                b.setLabel("get GWT Object from JS!");
            }
        });
        Window win = new Window();
        win.setTitle("This is our First Window!!");
        win.setBorder("normal");
        Label label = new Label();
        label.setValue("Second ZK Widget!!!");
        win.add(label);
        win1.add(changeTitleBtn);
        win1.add(unListenBtn);
        win1.add(gwtBtn);
        win1.add(win);
        return win1;
    }
}
