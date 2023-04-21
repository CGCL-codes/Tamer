package be.vds.jtbdive.client.view.core.search;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.filters.AggregatorDiveFilter;
import be.vds.jtbdive.client.core.filters.DiveFilter;
import be.vds.jtbdive.client.core.filters.DiveFilterHelper;
import be.vds.jtbdive.client.core.filters.DiveFilterNature;
import be.vds.jtbdive.client.core.filters.DiveFilterType;
import be.vds.jtbdive.client.core.filters.operator.AggregatorOperator;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.core.search.filterpanels.AggregatorFilterPanel;
import be.vds.jtbdive.client.view.core.search.filterpanels.DateFilterPanel;
import be.vds.jtbdive.client.view.core.search.filterpanels.DiveSiteFilterPanel;
import be.vds.jtbdive.client.view.core.search.filterpanels.DiverFilterPanel;
import be.vds.jtbdive.client.view.core.search.filterpanels.DoubleFilterPanel;
import be.vds.jtbdive.client.view.core.search.filterpanels.FilterNaturePanel;
import be.vds.jtbdive.client.view.core.search.filterpanels.IntegerFilterPanel;
import be.vds.jtbdive.client.view.core.search.filterpanels.KeyedCatalogFilterPanel;
import be.vds.jtbdive.client.view.core.search.filterpanels.StringFilterPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class DiveFiltersDialog extends PromptDialog {

    private static final long serialVersionUID = -7183329889072732335L;

    private JPanel detailPanel;

    private CardLayout detailLayout;

    private Map<String, FilterNaturePanel> filterPanels;

    private FiltersTree filtersTree;

    private I18nButton addButton;

    private I18nButton removeButton;

    private DiverManagerFacade diverManagerFacade;

    private DiveSiteManagerFacade diveSiteManagerFacade;

    public DiveFiltersDialog(Frame parentDialog, DiverManagerFacade diverManagerFacade, DiveSiteManagerFacade diveSiteManagerFacade) {
        super(parentDialog, i18n.getString("filters"), i18n.getString("filters.define.message"));
        this.diverManagerFacade = diverManagerFacade;
        this.diveSiteManagerFacade = diveSiteManagerFacade;
    }

    @Override
    protected void doBeforeInit() {
        filterPanels = new HashMap<String, FilterNaturePanel>();
    }

    @Override
    protected Component createContentPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(UIAgent.getInstance().getColorBaseBackground());
        GridBagConstraints gc = new GridBagConstraints();
        GridBagLayoutManager.addComponent(p, createFiltersTree(), gc, 0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
        GridBagLayoutManager.addComponent(p, createButtons(), gc, 1, 0, 1, 1, 0, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        GridBagLayoutManager.addComponent(p, createFilterDetails(), gc, 0, 1, 2, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        return p;
    }

    private Component createFiltersTree() {
        filtersTree = new FiltersTree();
        filtersTree.addPropertyChangeListener(createPropertyChangeListener());
        JScrollPane scroll = new JScrollPane(filtersTree);
        scroll.setPreferredSize(new Dimension(300, 200));
        scroll.setMinimumSize(new Dimension(300, 200));
        return scroll;
    }

    private PropertyChangeListener createPropertyChangeListener() {
        PropertyChangeListener l = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (FiltersTree.SELECTION_CHANGED.equals(evt.getPropertyName())) {
                    DiveFilter df = (DiveFilter) evt.getNewValue();
                    adaptFilterContent(df);
                    adaptButtons(df);
                }
            }
        };
        return l;
    }

    private void adaptButtons(DiveFilter df) {
        boolean isRootChild = df == null;
        if (!isRootChild) isRootChild = filtersTree.isRootLeaf(df);
        removeButton.setEnabled(df != null && !isRootChild);
        addButton.setEnabled(df != null && df instanceof AggregatorDiveFilter);
    }

    private void adaptFilterContent(DiveFilter filter) {
        if (filter == null || filter.getDiveFilterType() == null) {
            detailLayout.show(detailPanel, "default");
        } else {
            displayPanelForNature(filter);
        }
    }

    private void displayPanelForNature(DiveFilter filter) {
        DiveFilterNature nature = filter.getDiveFilterType().getDiveFilterNature();
        FilterNaturePanel filterPanel = filterPanels.get(nature);
        if (filterPanel == null) {
            filterPanel = createPanelForNature(nature);
            if (filterPanel != null) registerFilterPanel(filterPanel, nature.getKey());
        }
        if (filterPanel != null) {
            detailLayout.show(detailPanel, nature.getKey());
            filterPanel.setDiveFilter(filter);
        } else {
            detailLayout.show(detailPanel, "default");
        }
    }

    private FilterNaturePanel createPanelForNature(DiveFilterNature nature) {
        switch(nature) {
            case STRING:
                return createCommentComponent();
            case DOUBLE:
                return createDoubleFilterComponent();
            case INTEGER:
                return createIntegerFilterComponent();
            case AGGREGATOR:
                return createAggregatorComponent();
            case DIVER:
                return createDiverComponent();
            case DIVESITE:
                return createDiveSiteComponent();
            case KEYED_CATALOG:
                return createKeyedCatalogComponent();
            case DATE:
                return createDateComponent();
        }
        return null;
    }

    private FilterNaturePanel createDateComponent() {
        DateFilterPanel c = new DateFilterPanel();
        c.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (FilterNaturePanel.FILTER_CHANGED.equals(evt.getPropertyName())) {
                    refreshLabel((DiveFilter) evt.getNewValue());
                }
            }
        });
        return c;
    }

    private FilterNaturePanel createKeyedCatalogComponent() {
        KeyedCatalogFilterPanel c = new KeyedCatalogFilterPanel();
        c.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (FilterNaturePanel.FILTER_CHANGED.equals(evt.getPropertyName())) {
                    refreshLabel((DiveFilter) evt.getNewValue());
                }
            }
        });
        return c;
    }

    private FilterNaturePanel createDiveSiteComponent() {
        DiveSiteFilterPanel c = new DiveSiteFilterPanel();
        c.setDiveSiteManagerFacade(diveSiteManagerFacade);
        c.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (FilterNaturePanel.FILTER_CHANGED.equals(evt.getPropertyName())) {
                    refreshLabel((DiveFilter) evt.getNewValue());
                }
            }
        });
        return c;
    }

    private FilterNaturePanel createDiverComponent() {
        DiverFilterPanel c = new DiverFilterPanel();
        c.setDiverManagerFacade(diverManagerFacade);
        c.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (FilterNaturePanel.FILTER_CHANGED.equals(evt.getPropertyName())) {
                    refreshLabel((DiveFilter) evt.getNewValue());
                }
            }
        });
        return c;
    }

    private void refreshLabel(DiveFilter diveFilter) {
        filtersTree.refreshLabel(diveFilter);
    }

    private FilterNaturePanel createAggregatorComponent() {
        AggregatorFilterPanel c = new AggregatorFilterPanel();
        c.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (FilterNaturePanel.FILTER_CHANGED.equals(evt.getPropertyName())) {
                    refreshLabel((DiveFilter) evt.getNewValue());
                }
            }
        });
        return c;
    }

    private DiveFilter getSelectedFilter() {
        return filtersTree.getSelectedFilter();
    }

    private Component createButtons() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        p.setOpaque(false);
        GridBagLayoutManager.addComponent(p, addFilterButton(), gc, 0, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
        GridBagLayoutManager.addComponent(p, removeFilterButton(), gc, 0, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
        GridBagLayoutManager.addComponent(p, Box.createVerticalGlue(), gc, 0, 2, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        return p;
    }

    private Component addFilterButton() {
        addButton = new I18nButton(new AbstractAction("add") {

            private static final long serialVersionUID = -4671797616286104898L;

            @Override
            public void actionPerformed(ActionEvent e) {
                FilterTypeSelectionDialog d = new FilterTypeSelectionDialog(DiveFiltersDialog.this);
                int i = d.showDialog(300, 400);
                if (i == PromptDialog.OPTION_OK) {
                    DiveFilter df = getSelectedFilter();
                    filtersTree.addFilter(df, DiveFilterHelper.createDiveFilter(d.getFilterType()));
                }
            }
        });
        addButton.setEnabled(false);
        return addButton;
    }

    private Component removeFilterButton() {
        removeButton = new I18nButton(new AbstractAction("remove") {

            private static final long serialVersionUID = 4176849937821366706L;

            @Override
            public void actionPerformed(ActionEvent e) {
                DiveFilter df = getSelectedFilter();
                filtersTree.removeFilter(df);
            }
        });
        removeButton.setEnabled(false);
        return removeButton;
    }

    private Component createFilterDetails() {
        detailLayout = new CardLayout();
        detailPanel = new DetailPanel(detailLayout);
        detailPanel.add(createNoComponent(), "default");
        return detailPanel;
    }

    private void registerFilterPanel(FilterNaturePanel component, String key) {
        detailPanel.add(component, key);
        filterPanels.put(key, component);
    }

    private FilterNaturePanel createCommentComponent() {
        StringFilterPanel c = new StringFilterPanel();
        c.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (FilterNaturePanel.FILTER_CHANGED.equals(evt.getPropertyName())) {
                    refreshLabel((DiveFilter) evt.getNewValue());
                }
            }
        });
        return c;
    }

    private FilterNaturePanel createDoubleFilterComponent() {
        DoubleFilterPanel c = new DoubleFilterPanel();
        c.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (FilterNaturePanel.FILTER_CHANGED.equals(evt.getPropertyName())) {
                    refreshLabel((DiveFilter) evt.getNewValue());
                }
            }
        });
        return c;
    }

    private FilterNaturePanel createIntegerFilterComponent() {
        IntegerFilterPanel c = new IntegerFilterPanel();
        c.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (FilterNaturePanel.FILTER_CHANGED.equals(evt.getPropertyName())) {
                    refreshLabel((DiveFilter) evt.getNewValue());
                }
            }
        });
        return c;
    }

    private Component createNoComponent() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        return p;
    }

    public AggregatorDiveFilter getDiveFilterGroup() {
        return filtersTree.getFilters();
    }

    public void setDiveFilters(AggregatorDiveFilter filters) {
        filtersTree.setFilters(filters);
        filtersTree.expandAll();
    }

    public static void main(String[] args) {
        DiveFiltersDialog d = new DiveFiltersDialog(null, null, null);
        d.setDefaultFilters();
        d.showDialog();
    }

    public void setDefaultFilters() {
        AggregatorDiveFilter a = new AggregatorDiveFilter(DiveFilterType.AGGREGATOR);
        a.setOperator(AggregatorOperator.OR);
        setDiveFilters(a);
    }
}
