package org.gwt.formlayout.showcase.client.tutorial;

import org.gwt.formlayout.client.builder.PanelBuilder;
import org.gwt.formlayout.client.layout.CellConstraints;
import org.gwt.formlayout.client.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;

public class Basics9Item extends PanelDataListItem {

    public Basics9Item() {
        super("Basics 9 - Bounded Sizes");
    }

    @Override
    public Container getPanel() {
        TabPanel tabPanel = new TabPanel();
        TabItem tab1 = new TabItem("Jumping 1");
        TabItem tab2 = new TabItem("Jumping 2");
        TabItem tab3 = new TabItem("Stable 1");
        TabItem tab4 = new TabItem("Stable 2");
        tab1.add(buildJumping1Panel());
        tab2.add(buildJumping2Panel());
        tab3.add(buildStable1Panel());
        tab4.add(buildStable2Panel());
        tabPanel.add(tab1);
        tabPanel.add(tab2);
        tabPanel.add(tab3);
        tabPanel.add(tab4);
        return tabPanel;
    }

    private Component buildJumping1Panel() {
        FormLayout layout = new FormLayout("pref, 3dlu, [35dlu,min], 2dlu, min, 2dlu, min, 2dlu, min, ", EDITOR_ROW_SPEC);
        return buildEditorGeneralPanel(layout);
    }

    private Component buildJumping2Panel() {
        FormLayout layout = new FormLayout("pref, 3dlu, [35dlu,min], 2dlu, min, 2dlu, min, 2dlu, min, ", EDITOR_ROW_SPEC);
        return buildEditorTransportPanel(layout);
    }

    private Component buildStable1Panel() {
        FormLayout layout = new FormLayout("[50dlu,pref], 3dlu, [35dlu,min], 2dlu, min, 2dlu, min, 2dlu, min, ", EDITOR_ROW_SPEC);
        return buildEditorGeneralPanel(layout);
    }

    private Component buildStable2Panel() {
        FormLayout layout = new FormLayout("[50dlu,pref], 3dlu, [35dlu,min], 2dlu, min, 2dlu, min, 2dlu, min, ", EDITOR_ROW_SPEC);
        return buildEditorTransportPanel(layout);
    }

    private static final String EDITOR_ROW_SPEC = "3*(p, 3dlu), p";

    /**
     * Builds and returns the editor's general tab for the given layout.
     *
     * @param layout   the layout to be used
     * @return the editor's general tab
     */
    private Component buildEditorGeneralPanel(FormLayout layout) {
        layout.setColumnGroups(new int[][] { { 3, 5, 7, 9 } });
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.addLabel("File number:", cc.xy(1, 1));
        builder.add(new TextField(), cc.xyw(3, 1, 7));
        builder.addLabel("RFQ number:", cc.xy(1, 3));
        builder.add(new TextField(), cc.xyw(3, 3, 7));
        builder.addLabel("Entry date:", cc.xy(1, 5));
        builder.add(new TextField(), cc.xy(3, 5));
        builder.addLabel("Sales Person:", cc.xy(1, 7));
        builder.add(new TextField(), cc.xyw(3, 7, 7));
        return builder.getPanel();
    }

    /**
     * Builds and answer the editor's transport tab for the given layout.
     *
     * @param layout   the layout to be used
     * @return the editor's transport panel
     */
    private Component buildEditorTransportPanel(FormLayout layout) {
        layout.setColumnGroups(new int[][] { { 3, 5, 7, 9 } });
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.addLabel("Shipper:", cc.xy(1, 1));
        builder.add(new TextField(), cc.xy(3, 1));
        builder.add(new TextField(), cc.xyw(5, 1, 5));
        builder.addLabel("Consignee:", cc.xy(1, 3));
        builder.add(new TextField(), cc.xy(3, 3));
        builder.add(new TextField(), cc.xyw(5, 3, 5));
        builder.addLabel("Departure:", cc.xy(1, 5));
        builder.add(new TextField(), cc.xy(3, 5));
        builder.add(new TextField(), cc.xyw(5, 5, 5));
        builder.addLabel("Destination:", cc.xy(1, 7));
        builder.add(new TextField(), cc.xy(3, 7));
        builder.add(new TextField(), cc.xyw(5, 7, 5));
        return builder.getPanel();
    }

    @Override
    public String getDescription() {
        return "Demonstrates how to specify lower and upper bounds for column and row sizes.\n\n" + "This is useful to give components or related panels a more stable layout.\n" + "The first two tabs below jump, whereas the remaining tabs are stable.";
    }
}
