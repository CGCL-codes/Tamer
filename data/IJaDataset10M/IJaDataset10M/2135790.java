package com.googlecode.jrename.gui.list;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import com.googlecode.jrename.bean.FileRenamer;
import com.googlecode.jrename.gui.Resources;
import com.googlecode.jrename.gui.action.OpenButton;
import com.googlecode.jrename.gui.action.PreviewButton;
import com.googlecode.jrename.gui.action.PreviewCheckBox;
import com.googlecode.jrename.gui.drop.FileDrop;
import com.googlecode.jrename.gui.list.model.CenteredCellRenderer;
import com.googlecode.jrename.gui.list.model.MatchingCellRenderer;
import com.googlecode.jrename.gui.list.model.MiddleCellRenderer;
import com.googlecode.jrename.gui.list.model.RenamerTableModel;
import com.googlecode.jrename.gui.list.model.ZebraJTable;
import com.googlecode.jrename.gui.option.OptionPane;
import com.googlecode.jrename.gui.option.generic.OptionCheckBox;
import com.googlecode.jrename.ui.RenameMediator;
import net.miginfocom.swing.MigLayout;

public class RenamerFileList extends OptionPane {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNS = { " ", " ", Resources.get("table.column.title.current"), Resources.get("table.column.title.resulting") };

    private RenameMediator med;

    private JTable table;

    private RenamerTableModel model;

    private JScrollPane scrollPane;

    public RenamerFileList(ActionListener al, RenameMediator m) {
        super(new MigLayout("fillx", "0[]0", "0[]5[]5[]0"), "title.list");
        setPreferredSize(new Dimension((int) getPreferredSize().getWidth(), 220));
        med = m;
        med.registerFileList(this);
        model = new RenamerTableModel(null, COLUMNS);
        table = new ZebraJTable(model);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setColumnSelectionAllowed(false);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false);
        new FileDrop(table, false, new FileDrop.Listener() {

            public void filesDropped(java.io.File[] files) {
                med.drop(files);
            }
        });
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        table.getColumnModel().getColumn(0).setCellRenderer(new MatchingCellRenderer());
        table.getColumnModel().getColumn(0).setMaxWidth(22);
        table.getColumnModel().getColumn(1).setCellRenderer(new CenteredCellRenderer());
        table.getColumnModel().getColumn(1).setMaxWidth(22);
        table.getColumnModel().getColumn(2).setCellRenderer(new MiddleCellRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new MiddleCellRenderer());
        scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setWheelScrollingEnabled(true);
        add(new OpenButton(al, m), "split, sg 1");
        add(new OptionCheckBox(al, m, "selection.recursive", "Recursive", false), "wrap");
        add(new PreviewButton(al, m), "split, sg 1");
        add(new PreviewCheckBox(al, m), "wrap");
        add(scrollPane, "growx");
    }

    @Override
    public void setEnabled(boolean b) {
        table.setEnabled(b);
    }

    public void setData(ArrayList<FileRenamer> files) {
        model.setRows(files);
        scrollPane.setViewportView(table);
        repaint();
    }
}
