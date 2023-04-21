package net.sourceforge.jcpusim.ui.tabs;

import net.sourceforge.jcpusim.Core;
import net.sourceforge.jcpusim.ui.ToolBarBuilder;
import net.sourceforge.jcpusim.ui.ToolBarBuilderItem;
import net.sourceforge.jcpusim.ui.workspace.DefaultWorkspace;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

/**
 * The Problems class displays all syntax and runtime errors for the
 * user's program
 */
public class Problems implements Tab {

    public static final int PROBLEM_SEVERE = 0;

    public static final int PROBLEM_WARNING = 1;

    public static final int PROBLEM_INFO = 2;

    /** shared resources and methods */
    protected Core core;

    /** the parent CTabFolder */
    protected CTabFolder folder;

    /** the tab */
    public CTabItem cTabItem;

    /** */
    protected Composite composite;

    /** the table */
    protected Table table;

    /** line count for alternating gray background row effect */
    protected int count;

    /** names of the table columns */
    protected final String[] columnNames = { "", "Line", "Description" };

    /** */
    protected CoolBar coolBar;

    /** toolbar data, in order to be positioned on screen */
    protected final ToolBarBuilderItem[] toolBarData = { new ToolBarBuilderItem(100, ToolBarBuilderItem.TOOLBAR_NO_PARENT), new ToolBarBuilderItem(101, 100, SWT.PUSH, "Clear", true, "edittrash") };

    /**
	 * Create a new Problems tab
	 * @param core - shared resources and methods
	 * @param folder
	 */
    public Problems(Core core, CTabFolder folder) {
        this.core = core;
        this.folder = folder;
        count = 0;
        cTabItem = new CTabItem(folder, SWT.NONE);
        cTabItem.setImage(core.getIcon(core.ICON_SMALL, "important"));
        cTabItem.setText("Problems");
        composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new FormLayout());
        coolBar = new ToolBarBuilder(core, composite, toolBarData, core.getMainShellEventListener()).getCoolBar();
        table = new Table(composite, SWT.NONE);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        for (int i = 0; i < columnNames.length; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(columnNames[i]);
        }
        cTabItem.setControl(composite);
        folder.setSelection(cTabItem);
        init();
        layout();
    }

    public void init() {
        for (int i = 0; i < columnNames.length; i++) table.getColumn(i).pack();
    }

    public void layout() {
        FormData coolBarData = new FormData();
        coolBarData.left = new FormAttachment(0);
        coolBarData.right = new FormAttachment(100);
        coolBarData.top = new FormAttachment(0);
        coolBar.setLayoutData(coolBarData);
        FormData tableData = new FormData();
        tableData.left = new FormAttachment(0);
        tableData.right = new FormAttachment(100);
        tableData.top = new FormAttachment(coolBar);
        tableData.bottom = new FormAttachment(100);
        table.setLayoutData(tableData);
    }

    public void update() {
    }

    public void add(int severity, int line, String description) {
        TableItem tableItem = new TableItem(table, SWT.NONE);
        String icon = null;
        switch(severity) {
            case PROBLEM_SEVERE:
                icon = "redled";
                break;
            case PROBLEM_WARNING:
                icon = "yellowled";
                break;
            case PROBLEM_INFO:
                icon = "greenled";
                break;
        }
        tableItem.setImage(0, core.getIcon(core.ICON_SMALL, icon));
        tableItem.setText(1, String.valueOf(line));
        tableItem.setText(2, description);
        if (count++ % 2 == 1) tableItem.setBackground(new Color(core.getDisplay(), 240, 240, 240));
    }

    public void clear() {
        table.clearAll();
        table.setItemCount(0);
    }
}
