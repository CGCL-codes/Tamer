package com.novusradix.JavaPop.Client.Tools;

import com.novusradix.JavaPop.Client.Client;
import com.novusradix.JavaPop.Messaging.Tools.Fungus;
import java.awt.Point;

/**
 *
 * @author gef
 */
public class FungusTool extends Tool {

    public FungusTool(ToolGroup tg, Client c) {
        super(tg, c);
    }

    @Override
    public void PrimaryDown(Point p) {
        client.sendMessage(new Fungus(p));
    }

    public void PrimaryUp(Point p) {
    }

    public String getIconName() {
        return "/com/novusradix/JavaPop/icons/Fungus.png";
    }

    public String getToolTip() {
        return "Fungus";
    }
}
