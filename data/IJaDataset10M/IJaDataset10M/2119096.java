package com.melloware.jukes.gui.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.uif.AbstractDialog;
import com.jgoodies.uif.util.Resizer;
import com.jgoodies.uifextras.panel.HeaderPanel;
import com.melloware.jukes.gui.tool.Resources;

/**
 * Provides statistics about the application from Hibernate and other sources.
 * <p>
 * Copyright (c) 1999-2007 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 4.0
 */
public final class StatisticsDialog extends AbstractDialog {

    private JButton buttonClose;

    private JComponent buttonBar;

    /**
    * Constructs a default about dialog using the given owner.
    * @param owner the dialog's owner
    */
    public StatisticsDialog(Frame owner) {
        super(owner);
    }

    /**
    * Builds and returns the dialog's system tab.
    * @return the system tab component
    */
    protected JComponent buildApplicationTab() {
        final JPanel table = new PropertySheetApplicationStats();
        int width = Sizes.dialogUnitXAsPixel(300, table);
        table.setPreferredSize(Resizer.DEFAULT.fromWidth(width));
        table.setBorder(Borders.DIALOG_BORDER);
        return table;
    }

    /**
    * Builds and answers the dialog's content.
    * @return the dialog's content with tabbed pane and button bar
    */
    @Override
    protected JComponent buildContent() {
        final JPanel content = new JPanel(new BorderLayout());
        content.add(buildTabbedPane(), BorderLayout.CENTER);
        buttonClose = createCancelButton();
        buttonClose.setText(Resources.getString("label.Close"));
        buttonClose.setEnabled(true);
        buttonBar = ButtonBarFactory.buildRightAlignedBar(buttonClose);
        content.add(buttonBar, BorderLayout.SOUTH);
        return content;
    }

    /**
    * Builds and returns the dialog's system tab.
    * @return the system tab component
    */
    protected JComponent buildDatabaseTab() {
        final JPanel table = new PropertySheetHibernateStats();
        int width = Sizes.dialogUnitXAsPixel(220, table);
        table.setPreferredSize(Resizer.DEFAULT.fromWidth(width));
        table.setBorder(Borders.DIALOG_BORDER);
        return table;
    }

    /**
    * Builds and returns the dialog's header.
    * @return the dialog's header component
    */
    @Override
    protected JComponent buildHeader() {
        return new HeaderPanel(Resources.getString("label.Statistics"), Resources.getString("label.StatisticsMessage"), Resources.STATS_LARGE_ICON);
    }

    /**
    * Builds and returns the dialog's tabbed pane.
    * @return the dialog's tabbed pane component
    */
    protected JComponent buildTabbedPane() {
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(Resources.getString("label.Application"), buildApplicationTab());
        tabbedPane.add(Resources.getString("label.Database"), buildDatabaseTab());
        return tabbedPane;
    }

    /**
    * Resizes the given component to give it a quadratic aspect ratio.
    * @param component the component to be resized
    */
    @Override
    protected void resizeHook(JComponent component) {
        Resizer.ONE2ONE.resizeDialogContent(component);
    }
}
