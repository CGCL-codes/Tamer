package com.melloware.jukes.gui.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.uif.application.Application;
import com.jgoodies.uif.builder.ToolBarBuilder;
import com.jgoodies.uifextras.util.UIFactory;
import com.melloware.jukes.file.tag.MusicTag;
import com.melloware.jukes.gui.tool.Actions;
import com.melloware.jukes.gui.tool.MainModule;
import com.melloware.jukes.gui.tool.Resources;
import com.melloware.jukes.gui.tool.Settings;
import com.melloware.jukes.gui.view.component.ComplexInternalFrame;
import com.melloware.jukes.gui.view.component.ComponentFactory;
import com.melloware.jukes.util.GuiUtil;
import com.melloware.jukes.util.TimeSpan;
import com.melloware.jukes.util.TimeSpanUnit;

/**
 * Th filter panel on the main Jukes window.
 * <p>
 * Copyright (c) 1999-2007 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 4.0 AZ - some modification 2009
 */
@SuppressWarnings("unchecked")
public final class FilterPanel extends ComplexInternalFrame implements ActionListener {

    private static final Log LOG = LogFactory.getLog(FilterPanel.class);

    private static final String GENRE_NONE = "<none>";

    private AbstractButton filterButton;

    private final JCheckBox newFlag;

    private final JCheckBox exactFlag;

    private final JComboBox bitrateField;

    private final JComboBox genreField;

    private final JComboBox operatorBitrate;

    private final JComboBox operatorYear;

    private final JPanel filterPanel;

    private final JScrollPane scrollPane;

    private final JTextField yearField1;

    private final JTextField yearField2;

    private final Settings settings;

    /** AZ - Artist & Album filters */
    private final JTextField artistField;

    private final JTextField albumField;

    /**
    * Default constructor
    * <p>
    * @param settings the application settings.
    */
    public FilterPanel(Settings settings) {
        super(Resources.getString("label.filter"));
        LOG.debug("Filter panel created.");
        this.settings = settings;
        setFrameIcon(Resources.FILTER_ICON);
        setToolBar(buildToolBar());
        final List genres = MusicTag.getGenreTypes();
        genres.add(0, GENRE_NONE);
        genreField = new JComboBox(genres.toArray());
        genreField.setSelectedItem(GENRE_NONE);
        genreField.setEditable(true);
        genreField.addActionListener(this);
        bitrateField = new JComboBox(Resources.BITRATES);
        bitrateField.setSelectedItem(null);
        bitrateField.addActionListener(this);
        operatorBitrate = new JComboBox(Resources.OPERATOR);
        operatorBitrate.setSelectedItem(">=");
        operatorBitrate.addActionListener(this);
        operatorYear = new JComboBox(Resources.OPERATOR);
        operatorYear.setSelectedItem(">=");
        operatorYear.addActionListener(this);
        yearField1 = new JTextField();
        (yearField1).setColumns(5);
        yearField2 = new JTextField();
        (yearField2).setColumns(5);
        newFlag = new JCheckBox(Resources.getString("label.recentadds"), false);
        newFlag.addActionListener(this);
        exactFlag = new JCheckBox(Resources.getString("label.exact"), false);
        exactFlag.addActionListener(this);
        artistField = new JTextField();
        (artistField).setColumns(5);
        albumField = new JTextField();
        (albumField).setColumns(5);
        filterPanel = new JPanel(new BorderLayout());
        filterPanel.add(buildFilter(), BorderLayout.CENTER);
        scrollPane = UIFactory.createStrippedScrollPane(filterPanel);
        this.add(scrollPane);
    }

    /**
    * When the filter is changed re apply it.
    */
    public void actionPerformed(ActionEvent aEvent) {
        if (filterButton.isSelected()) {
            LOG.debug("Filter changed");
            applyFilter();
        }
    }

    /**
    * Applies this filter to the application
    */
    public void applyFilter() {
        final MainFrame mainFrame = (MainFrame) Application.getDefaultParentFrame();
        try {
            GuiUtil.setBusyCursor(mainFrame, true);
            String query = "";
            final String artist = artistField.getText();
            if (StringUtils.isNotBlank(artist)) {
                final String artistStmt = artist.replaceAll("'", "\'\'");
                if (exactFlag.isSelected()) {
                    query = query + "and UPPER(artist.name) = '" + artistStmt.toUpperCase() + "'";
                } else {
                    query = query + "and UPPER(artist.name) LIKE '%" + artistStmt.toUpperCase() + "%'";
                }
            }
            final String album = albumField.getText();
            if (StringUtils.isNotBlank(album)) {
                final String albumStmt = album.replaceAll("'", "\'\'");
                if (exactFlag.isSelected()) {
                    query = query + "and UPPER(disc.name) = '" + albumStmt.toUpperCase() + "'";
                } else {
                    query = query + "and UPPER(disc.name) LIKE '%" + albumStmt.toUpperCase() + "%'";
                }
            }
            final String genre = (String) genreField.getSelectedItem();
            if ((StringUtils.isNotBlank(genre)) && (!StringUtils.equals(genre, GENRE_NONE))) {
                if (exactFlag.isSelected()) {
                    query = query + " and UPPER(disc.genre) = '" + genre.toUpperCase() + "'";
                } else {
                    query = query + " and UPPER(disc.genre) LIKE '%" + genre.toUpperCase() + "%'";
                }
            }
            if (StringUtils.isNotBlank(yearField1.getText())) {
                if (StringUtils.isBlank(yearField2.getText())) {
                    query = query + " and disc.year " + operatorYear.getSelectedItem().toString() + "'" + StringEscapeUtils.escapeSql(yearField1.getText()) + "'";
                } else {
                    query = query + " and disc.year between " + "'" + StringEscapeUtils.escapeSql(yearField1.getText()) + "'" + " and " + "'" + StringEscapeUtils.escapeSql(yearField2.getText()) + "'";
                }
            }
            if (bitrateField.getSelectedItem() != null) {
                query = query + " and disc.bitrate  " + operatorBitrate.getSelectedItem().toString() + StringEscapeUtils.escapeSql((String) bitrateField.getSelectedItem());
            }
            if (newFlag.isSelected()) {
                final TimeSpan timespan = new TimeSpan(System.currentTimeMillis());
                timespan.subtract(TimeSpanUnit.DAYS, this.settings.getNewFileInDays());
                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                query = query + " and disc.createdDate > '" + format.format(new Date(timespan.getTime())) + " 00:00:00'";
            }
            MainModule.SETTINGS.setFilter(query);
            mainFrame.getMainModule().refreshTree();
        } finally {
            GuiUtil.setBusyCursor(mainFrame, false);
        }
    }

    /**
    * Clears the current filter.
    */
    public void clearFilter() {
        genreField.setSelectedItem(null);
        yearField1.setText("");
        yearField2.setText("");
        artistField.setText("");
        albumField.setText("");
        bitrateField.setSelectedItem(null);
        newFlag.setSelected(false);
        exactFlag.setSelected(false);
        filterButton.setSelected(false);
        removeFilter();
    }

    /**
    * Removes the filter from the application.
    */
    public void removeFilter() {
        final MainFrame mainFrame = (MainFrame) Application.getDefaultParentFrame();
        try {
            GuiUtil.setBusyCursor(mainFrame, true);
            MainModule.SETTINGS.setFilter("");
            mainFrame.getMainModule().refreshTree();
        } finally {
            GuiUtil.setBusyCursor(mainFrame, false);
        }
    }

    /**
    * Builds the filter panel.
    * <p>
    * @return the panel used to specify filters
    */
    private JComponent buildFilter() {
        FormLayout layout = new FormLayout("3px, pref, pref, pref, pref, fill:pref:grow", "4px, p, 4px, p, 4px, p, 4px, p, 4px, p, 4px, p, 4px, p");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.addLabel(Resources.getString("label.artist") + ": ", cc.xy(2, 2));
        builder.add(artistField, cc.xyw(3, 2, 4));
        builder.addLabel(Resources.getString("label.disc") + ": ", cc.xy(2, 4));
        builder.add(albumField, cc.xyw(3, 4, 4));
        builder.addLabel(Resources.getString("label.genre") + ": ", cc.xy(2, 6));
        builder.add(genreField, cc.xyw(3, 6, 4));
        builder.addLabel(Resources.getString("label.year") + ": ", cc.xy(2, 8));
        builder.add(operatorYear, cc.xy(3, 8));
        builder.add(yearField1, cc.xy(4, 8));
        builder.add(yearField2, cc.xy(5, 8));
        builder.addLabel(Resources.getString("label.bitrate") + ": ", cc.xy(2, 10));
        builder.add(operatorBitrate, cc.xy(3, 10));
        builder.add(bitrateField, cc.xy(4, 10));
        builder.add(newFlag, cc.xyw(2, 12, 4));
        builder.add(exactFlag, cc.xyw(2, 14, 4));
        return builder.getPanel();
    }

    /**
    * Builds the toolbar for this panel.
    * <p>
    * @return the toolbar to return
    */
    private JToolBar buildToolBar() {
        final ToolBarBuilder bar = new ToolBarBuilder("Filter Toolbar");
        AbstractButton button = null;
        filterButton = ComponentFactory.createToolBarToggleButton(Actions.FILTER_APPLY_ID);
        filterButton.putClientProperty(Resources.EDITOR_COMPONENT, this);
        bar.add(filterButton);
        button = ComponentFactory.createToolBarButton(Actions.FILTER_CLEAR_ID);
        button.putClientProperty(Resources.EDITOR_COMPONENT, this);
        bar.add(button);
        button = ComponentFactory.createToolBarButton(Actions.FILTER_CLOSE_ID);
        bar.add(button);
        return bar.getToolBar();
    }
}
