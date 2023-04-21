package org.kku.gui.source;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.layout.*;
import javax.swing.*;

public class RefreshDetailBuilder {

    private JPanel panel;

    private PanelBuilder builder;

    private CellConstraints cc;

    public RefreshDetailBuilder(JPanel panel) {
        this.panel = panel;
        init();
    }

    private void init() {
        String columns;
        String rows;
        FormLayout layout;
        columns = "16dlu, p:grow";
        rows = "";
        layout = new FormLayout(columns, rows);
        panel.setLayout(layout);
        cc = new CellConstraints();
        builder = new PanelBuilder(panel, layout);
    }

    public void addGap() {
        addGap("12dlu");
    }

    public void addGap(String gap) {
        builder.appendRow(gap);
        builder.nextLine();
    }

    public void addSeparator() {
        builder.appendRow("p");
        builder.setColumnSpan(builder.getColumnCount());
        builder.add(new JSeparator());
        builder.setColumnSpan(1);
        builder.nextLine();
        addGap();
    }

    public void add(JComponent component) {
        builder.appendRow("p");
        builder.setColumn(2);
        builder.add(component);
        builder.nextLine();
        addGap();
    }
}
