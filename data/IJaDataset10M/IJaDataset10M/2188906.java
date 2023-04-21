package com.itbs.aimcer.gui;

import com.itbs.aimcer.bean.ContactWrapper;
import com.itbs.aimcer.bean.GroupWrapper;
import com.itbs.aimcer.gui.userlist.ListRenderer;
import java.awt.*;

/**
 * @author Alex Rass
 * @since Sep 11, 2004
 */
public class UTestListRenderer extends UTestFrameTest {

    public void testRenderer() throws InterruptedException {
        ListRenderer lr = new ListRenderer();
        Component comp;
        ContactWrapper bw;
        bw = ContactWrapper.create("blah", null);
        comp = lr.getListCellRendererComponent(null, bw, 1, false, false);
        add(comp);
        bw.getStatus().setOnline(true);
        comp = lr.getListCellRendererComponent(null, bw, 1, false, false);
        add(comp);
        bw.getStatus().setAway(true);
        comp = lr.getListCellRendererComponent(null, bw, 1, false, false);
        add(comp);
        comp = lr.getListCellRendererComponent(null, bw, 1, true, false);
        add(comp);
        GroupWrapper gw = GroupWrapper.create("group1");
        comp = lr.getListCellRendererComponent(null, gw, 1, false, false);
        add(comp);
        comp = lr.getListCellRendererComponent(null, gw, 1, true, false);
        add(comp);
        gw.swapShrunk();
        comp = lr.getListCellRendererComponent(null, gw, 1, false, false);
        add(comp);
        window.setVisible(true);
        Thread.sleep(10000);
    }
}
