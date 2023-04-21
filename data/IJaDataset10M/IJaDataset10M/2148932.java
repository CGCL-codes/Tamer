package com.melloware.jukes.gui.view.editor;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.uif.action.ActionManager;
import com.jgoodies.uifextras.panel.GradientBackgroundPanel;
import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;
import com.melloware.jukes.db.orm.Catalog;
import com.melloware.jukes.gui.tool.Actions;
import com.melloware.jukes.gui.tool.Resources;

/**
 * This panel is displayed after a successful application startup to welcome
 * the user. It provides access to actions that are most useful at the
 * application start.
 * <p>
 * Copyright (c) 1999-2007 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 4.0
 */
public final class WelcomePanel extends GradientBackgroundPanel implements Editor {

    private static final Log LOG = LogFactory.getLog(WelcomePanel.class);

    /**
     * Constructs a <code>WelcomePanel</code>.
     */
    public WelcomePanel() {
        super(false);
        add(buildForeground());
        LOG.debug("Welcome Panel created.");
    }

    public Class getDomainClass() {
        return Catalog.class;
    }

    public JToolBar getHeaderToolBar() {
        return null;
    }

    public Icon getIcon() {
        return null;
    }

    public Object getModel() {
        return null;
    }

    public String getTitle() {
        return Resources.getString("application.name");
    }

    public JToolBar getToolBar() {
        return null;
    }

    public void setModel(Object m) {
    }

    public void activate() {
    }

    public void deactivate() {
    }

    /**
     * Builds and answers the foreground.
     */
    private JComponent buildForeground() {
        final JLabel selectLbl = new JLabel(Resources.getString("label.Selectoptions"));
        JTaskPane taskPane = new JTaskPane();
        JScrollPane scroll = new JScrollPane(taskPane);
        scroll.setBorder(null);
        JTaskPaneGroup catalogGroup = new JTaskPaneGroup();
        catalogGroup.setTitle(Resources.getString("menu.catalog"));
        catalogGroup.setToolTipText(Resources.getString("label.Catalogbasedtasks"));
        catalogGroup.setSpecial(true);
        catalogGroup.setIcon(Resources.NAVIGATOR_ICON);
        catalogGroup.add(ActionManager.get(Actions.DISC_FINDER_ID));
        catalogGroup.add(ActionManager.get(Actions.DISC_ADD_ID));
        catalogGroup.add(ActionManager.get(Actions.DISC_REMOVER_ID));
        catalogGroup.add(ActionManager.get(Actions.SEARCH_ID));
        catalogGroup.add(ActionManager.get(Actions.PREFERENCES_ID));
        taskPane.add(catalogGroup);
        JTaskPaneGroup alsoGroup = new JTaskPaneGroup();
        alsoGroup.setTitle(Resources.getString("label.seealso"));
        alsoGroup.setToolTipText(Resources.getString("label.Othertasks"));
        alsoGroup.setSpecial(true);
        alsoGroup.setIcon(Resources.getIcon("help.icon"));
        alsoGroup.add(ActionManager.get(Actions.HELP_CONTENTS_ID));
        alsoGroup.add(ActionManager.get(Actions.HELP_ABOUT_DIALOG_ID));
        alsoGroup.add(ActionManager.get(Actions.HELP_CONTACT_ID));
        alsoGroup.add(ActionManager.get(Actions.HELP_FORUMS_ID));
        alsoGroup.add(ActionManager.get(Actions.HELP_DONATE_ID));
        taskPane.add(alsoGroup);
        taskPane.add(new JLabel());
        FormLayout layout = new FormLayout("9dlu, left:pref:grow", "b:pref, c:pref, t:pref, 9dlu, pref, 6dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.getPanel().setOpaque(false);
        builder.setBorder(Borders.DLU14_BORDER);
        CellConstraints cc = new CellConstraints();
        builder.add(selectLbl, cc.xyw(1, 3, 2));
        builder.add(scroll, cc.xyw(1, 5, 2));
        return builder.getPanel();
    }
}
