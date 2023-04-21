package org.gwt.formlayout.showcase.client.tutorial;

import org.gwt.formlayout.client.layout.CellConstraints;
import org.gwt.formlayout.client.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.ui.Widget;

public class Basics3Item extends PanelDataListItem {

    public Basics3Item() {
        super("Basics 3 - Growing");
    }

    @Override
    public Container getPanel() {
        TabPanel tabPanel = new TabPanel();
        TabItem tab1 = new TabItem("All");
        TabItem tab2 = new TabItem("Half");
        TabItem tab3 = new TabItem("Percent Mixed");
        TabItem tab4 = new TabItem("Percent 33/67");
        tab1.add(buildAllPanel());
        tab2.add(buildHalfPanel());
        tab3.add(buildPercentPanel());
        tab4.add(buildPercent2Panel());
        tabPanel.add(tab1);
        tabPanel.add(tab2);
        tabPanel.add(tab3);
        tabPanel.add(tab4);
        return tabPanel;
    }

    private Widget buildPercent2Panel() {
        FormLayout layout = new FormLayout("pref:grow(0.33), 6px, pref:grow(0.67)", "pref, 12px, pref");
        LayoutContainer panel = new LayoutContainer(layout);
        CellConstraints cc = new CellConstraints();
        panel.add(new LabelField("Gets 33% of the space"), cc.xy(1, 1));
        panel.add(new LabelField("Gets 67% of the space"), cc.xy(3, 1));
        panel.add(new TextField(), cc.xy(1, 3));
        panel.add(new TextField(), cc.xy(3, 3));
        return panel;
    }

    private Widget buildPercentPanel() {
        FormLayout layout = new FormLayout("pref, 6px, 0:grow(0.25), 6px, 0:grow(0.75)", "pref, 12px, pref");
        LayoutContainer panel = new LayoutContainer(layout);
        CellConstraints cc = new CellConstraints();
        panel.add(new LabelField("Fixed"), cc.xy(1, 1));
        panel.add(new LabelField("Gets 25% of extra space"), cc.xy(3, 1));
        panel.add(new LabelField("Gets 75% of extra space"), cc.xy(5, 1));
        panel.add(new TextField(), cc.xy(1, 3));
        panel.add(new TextField(), cc.xy(3, 3));
        panel.add(new TextField(), cc.xy(5, 3));
        return panel;
    }

    private Widget buildHalfPanel() {
        FormLayout layout = new FormLayout("pref, 6px, 0:grow, 6px, 0:grow", "pref, 12px, pref");
        LayoutContainer panel = new LayoutContainer(layout);
        CellConstraints cc = new CellConstraints();
        panel.add(new LabelField("Fixed"), cc.xy(1, 1));
        panel.add(new LabelField("Gets half of extra space"), cc.xy(3, 1));
        panel.add(new LabelField("gets half of extra space"), cc.xy(5, 1));
        panel.add(new TextField(), cc.xy(1, 3));
        panel.add(new TextField(), cc.xy(3, 3));
        panel.add(new TextField(), cc.xy(5, 3));
        return panel;
    }

    private Widget buildAllPanel() {
        FormLayout layout = new FormLayout("pref, 6px, pref:grow", "pref, 12px, pref");
        LayoutContainer panel = new LayoutContainer(layout);
        CellConstraints cc = new CellConstraints();
        panel.add(new LabelField("Fixed"), cc.xy(1, 1));
        panel.add(new LabelField("Gets all extra space"), cc.xy(3, 1));
        panel.add(new TextField(), cc.xy(1, 3));
        panel.add(new TextField(), cc.xy(3, 3));
        return panel;
    }

    @Override
    public String getDescription() {
        return "Demonstrates the FormLayout growing options:\n" + "none, default, weighted.";
    }
}
