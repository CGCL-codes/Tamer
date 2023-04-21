package com.hummer.service.view.main.content;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.hummer.service.AppController;
import com.hummer.service.data.base.BaseDataFilter;
import com.hummer.service.data.base.BaseDataFilterDefinition;
import com.hummer.service.data.base.BaseDataObjectDefinition;
import com.hummer.service.data.factory.DataObjectFactory;
import com.hummer.service.enums.EnumDataTypes;
import com.hummer.service.enums.EnumMenuItemAction;
import com.hummer.service.view.main.AppView;

/**
 * Provides a filter panel and a <i>BrowseContentPlain</i> connected to it.
 * @author gernot.hummer
 *
 */
public class BrowseContentFiltered extends JPanel {

    private static final long serialVersionUID = -1166033414860350621L;

    private BaseDataObjectDefinition def;

    private EnumMenuItemAction standardAction;

    private ArrayList<BaseDataFilter> filters;

    private JPanel filtersPanel;

    private BrowseContentPlain bc;

    private ArrayList<String> filterItemNames;

    /**
     * @param def
     * @param standardAction
     */
    public BrowseContentFiltered(BaseDataObjectDefinition def, EnumMenuItemAction standardAction) {
        this.setLayout(new BorderLayout());
        this.def = def;
        this.standardAction = standardAction;
        this.filters = new ArrayList<BaseDataFilter>();
        this.filterItemNames = new ArrayList<String>();
        this.filtersPanel = new JPanel();
        this.bc = new BrowseContentPlain(def, standardAction);
        this.add(this.filtersPanel, BorderLayout.NORTH);
        this.add(this.bc, BorderLayout.CENTER);
        this.init();
    }

    /**
     * initialize the view
     */
    public void init() {
        this.refreshFilters();
    }

    /**
     * Ensure all filtes a properly loaded.
     */
    public void refreshFilters() {
        this.filtersPanel.removeAll();
        for (BaseDataFilter filter : this.filters) {
            DataObjectFactory.getInstance().getFilter(filter);
            this.filtersPanel.add(buildFilterPanel(filter));
        }
        this.validate();
    }

    /**
     * Builds the visual representation of a filter.
     * @param filter
     * @return a <i>JPanel</i> containing the visual representation of a filter.
     */
    private JPanel buildFilterPanel(BaseDataFilter filter) {
        JPanel filterPanel = new JPanel(new FlowLayout());
        JLabel filterLabel = new JLabel(DataObjectFactory.getInstance().getLanguageContent(AppController.getInstance().getActiveLanguage(), filter.getName()));
        String[] selection = new String[filter.getShowValues().size()];
        int i = 0;
        for (String value : filter.getShowValues()) {
            selection[i] = value;
            i++;
        }
        JComboBox jField = new JComboBox(selection);
        jField.addItemListener(new ChangeListener(filter));
        filterPanel.add(filterLabel);
        filterPanel.add(jField);
        return filterPanel;
    }

    /**
     * Add a filter handing in all filter information.
     * 
     * @param filterName
     * @param targetField
     * @param sourceTable
     * @param showValueColumn
     * @param showType
     * @param valueColumn
     * @param valueType
     */
    public void addFilter(String filterName, String targetField, String sourceTable, String showValueColumn, EnumDataTypes showType, String valueColumn, EnumDataTypes valueType) {
        BaseDataFilterDefinition def = new BaseDataFilterDefinition(sourceTable, showValueColumn, showType, valueColumn, valueType);
        this.filters.add(new BaseDataFilter(filterName, def));
        this.filterItemNames.add(targetField);
    }

    /**
     * Add a custom filter
     * 
     * @param targetField
     * @param filter
     */
    public void addFilter(String targetField, BaseDataFilter filter) {
        this.filters.add(filter);
        this.filterItemNames.add(targetField);
    }

    private class ChangeListener implements ItemListener {

        private BaseDataFilter parentFilter;

        public ChangeListener(BaseDataFilter parentFilter) {
            this.parentFilter = parentFilter;
        }

        @Override
        public void itemStateChanged(ItemEvent arg0) {
            parentFilter.setActiveItem(arg0.getItem());
            AppView.getInstance().getFilterCache().put(parentFilter.getName(), arg0.getItem());
            remove(bc);
            bc = new BrowseContentPlain(def, standardAction, filters, filterItemNames);
            add(bc, BorderLayout.CENTER);
            validate();
        }
    }
}
