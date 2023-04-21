package com.gwtext.sample.showcase2.client.layout;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.ComponentMgr;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.gwtext.sample.showcase2.client.ShowcasePanel;

public class RowLayoutSample extends ShowcasePanel {

    public String getSourceUrl() {
        return "source/layout/RowLayoutSample.java.html";
    }

    public Panel getViewPanel() {
        if (panel == null) {
            panel = new Panel();
            panel.setLayout(new FitLayout());
            final Panel wrapperPanel = new Panel();
            wrapperPanel.setLayout(new RowLayout());
            wrapperPanel.setBorder(true);
            wrapperPanel.setBodyStyle("border-style:dotted;border-color:blue;");
            Panel firstPanel = new Panel();
            firstPanel.setTitle("My Height is 75px");
            firstPanel.setClosable(true);
            final Button removeGreedyButton = new Button("Remove Greedy Panel", new ButtonListenerAdapter() {

                public void onClick(Button button, EventObject e) {
                    wrapperPanel.remove("greedy");
                    button.disable();
                }
            });
            final Button showHideButton = new Button("Show / Hide Bottom Panel", new ButtonListenerAdapter() {

                public void onClick(Button button, EventObject e) {
                    Component b = ComponentMgr.getComponent("bottom");
                    b.setVisible(!b.isVisible());
                }
            });
            firstPanel.add(showHideButton);
            firstPanel.add(removeGreedyButton);
            wrapperPanel.add(firstPanel, new RowLayoutData(75));
            Panel secondPanel = new Panel();
            secondPanel.setTitle("I will take half of the free space!");
            secondPanel.setHtml(" I am greedy <br><br><br><br><br><br><br><br><br><br><br><br><br><br>" + "<br><br><br><br>...");
            secondPanel.setId("greedy");
            secondPanel.setCollapsible(true);
            secondPanel.setAutoScroll(true);
            secondPanel.setBodyStyle("margin-bottom:10px");
            wrapperPanel.add(secondPanel, new RowLayoutData("50%"));
            Panel third = new Panel();
            third.setTitle("I'll take some too");
            third.setHtml("..if you don't mind. I don't have a <tt>height</tt> in the config.<br>Btw, " + "you can resize me!<br><br><br><br><br><br><br><br><br><br><br><br><br>" + "<br><br><br><br><br>...");
            third.setId("panel3");
            third.setAutoScroll(true);
            wrapperPanel.add(third);
            Panel fourth = new Panel();
            fourth.setId("slider");
            wrapperPanel.add(fourth, new RowLayoutData(5));
            Panel fifth = new Panel();
            fifth.setTitle("Let me settle too");
            fifth.setHtml("Since there are two of us without <tt>height</tt> given, each will take 1/2 " + "of the unallocated space (which is 50%), that's why my height initially is 25%." + "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>...");
            fifth.setAutoScroll(true);
            wrapperPanel.add(fifth);
            Panel sixth = new Panel();
            sixth.setTitle("I'm the panel with height in pixels too");
            sixth.setHtml("Nothing special, 60px panel");
            sixth.setId("bottom");
            wrapperPanel.add(sixth, new RowLayoutData(60));
            panel.add(wrapperPanel);
        }
        return panel;
    }
}
