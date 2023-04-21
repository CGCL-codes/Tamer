package de.mpicbg.buchholz.phenofam.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;
import com.google.gwt.visualization.client.visualizations.Table.Options.Policy;
import de.mpicbg.buchholz.phenofam.client.PhenoFamDataProvider.CalculatedDataAcceptor;

public class EnrichmentPanel extends Composite implements ClickListener, AvailableListsListener, CalculatedDataAcceptor, ChangeListener {

    private NumberFormat integerNumberFormat = NumberFormat.getFormat("0");

    private NumberFormat decimalNumberFormat = NumberFormat.getFormat("0.00");

    private NumberFormat scientificNumberFormat = NumberFormat.getFormat("0.00E0");

    private Table table = new Table();

    private DataTable dataTable;

    private ResultData data;

    private int pageSize = 20;

    private Double maxPValueFilter = 0.01;

    private Double maxPValueCorrectedFilter;

    private Integer minGenesFilter = 3;

    private String domainFilter;

    private final TextBox minGenesCountFilterBox = new TextBox();

    private final TextBox maxPValueFilterBox = new TextBox();

    private final TextBox maxPValueCorrectedFilterBox = new TextBox();

    private final TextBox domainFilterBox = new TextBox();

    private final Button applyFilterButton = new Button("Filter");

    private final Button downloadButton = new Button("All results");

    private final Button searchButton = new Button("Search");

    private final Button searchAndFilterButton = new Button("Search & Filter");

    private final ListBox databasesListBox = new ListBox(false);

    private UserDataPanel userDataPanel;

    public EnrichmentPanel(UserDataPanel userDataPanel) {
        this.userDataPanel = userDataPanel;
        VerticalPanel panel = new VerticalPanel();
        panel.setWidth("800px");
        panel.setVisible(false);
        initWidget(panel);
        Panel filtersPanel = createFiltersPanel();
        panel.add(filtersPanel);
        panel.setCellHeight(filtersPanel, "1em");
        panel.setCellHorizontalAlignment(filtersPanel, HasHorizontalAlignment.ALIGN_LEFT);
        panel.setCellVerticalAlignment(filtersPanel, HasVerticalAlignment.ALIGN_TOP);
        panel.setCellHeight(filtersPanel, "1.5em");
        Panel searchPanel = createSearchPanel();
        panel.add(searchPanel);
        panel.setCellHeight(searchPanel, "1em");
        panel.setCellHorizontalAlignment(searchPanel, HasHorizontalAlignment.ALIGN_LEFT);
        panel.setCellVerticalAlignment(searchPanel, HasVerticalAlignment.ALIGN_TOP);
        panel.setCellHeight(searchPanel, "1.5em");
        panel.add(table);
        table.setWidth("800px");
        table.addSelectHandler(new SelectHandler() {

            public void onSelect(SelectEvent event) {
                callSelectionListener();
            }
        });
    }

    public void setUserDataPanel(UserDataPanel userDataPanel) {
        this.userDataPanel = userDataPanel;
    }

    private void callSelectionListener() {
        JsArray<Selection> selections = table.getSelections();
        List<String> pfamIds = new ArrayList<String>(selections.length());
        for (int i = 0; i < selections.length(); i++) {
            pfamIds.add(dataTable.getValueString(selections.get(i).getRow(), 0));
        }
        userDataPanel.displayDomains(data.getRef(), pfamIds);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Double getMaxPValueFilter() {
        return maxPValueFilter;
    }

    public void setMaxPValueFilter(Double maxPValueFilter) {
        this.maxPValueFilter = maxPValueFilter;
    }

    public Double getMaxPValueCorrectedFilter() {
        return maxPValueCorrectedFilter;
    }

    public void setMaxPValueCorrectedFilter(Double maxPValueCorrectedFilter) {
        this.maxPValueCorrectedFilter = maxPValueCorrectedFilter;
    }

    public Integer getMinGenesFilter() {
        return minGenesFilter;
    }

    public void setMinGenesFilter(Integer minGenesFilter) {
        this.minGenesFilter = minGenesFilter;
    }

    public String getDomainFilter() {
        return domainFilter;
    }

    public void setDomainFilter(String domainFilter) {
        this.domainFilter = domainFilter;
    }

    public void drawData() {
        getWidget().setVisible(data != null);
        userDataPanel.setVisible(false);
        if (data == null || databasesListBox.getSelectedIndex() == -1) return;
        String featureDatabaseName = databasesListBox.getValue(databasesListBox.getSelectedIndex());
        List<ResultDataEntity> list = null;
        for (Map.Entry<FeatureDatabase, List<ResultDataEntity>> entry : data.getData().entrySet()) {
            if (entry.getKey().getName().equals(featureDatabaseName)) {
                list = entry.getValue();
                break;
            }
        }
        if (list == null) return;
        dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Feature");
        dataTable.addColumn(ColumnType.NUMBER, "Genes");
        dataTable.addColumn(ColumnType.NUMBER, "Median");
        dataTable.addColumn(ColumnType.NUMBER, "P-Value");
        dataTable.addColumn(ColumnType.NUMBER, "Corrected");
        dataTable.addColumn(ColumnType.NUMBER, "<span title=\"Measure of the overlap between two distributions; it can take values between 0 and 1. Both extreme values represent complete separation of the distributions, while a &rho; of 0.5 represents complete overlap.\">&rho;</span>");
        dataTable.addColumn(ColumnType.STRING, "Description");
        dataTable.addColumn(ColumnType.STRING, "InterPro");
        int row = 0;
        for (ResultDataEntity entity : list) {
            if (maxPValueFilter != null && entity.getPValue() > maxPValueFilter) continue;
            if (maxPValueCorrectedFilter != null && entity.getPValueCorrected() > maxPValueCorrectedFilter) continue;
            if (minGenesFilter != null && entity.getInputIdsCount() < minGenesFilter) continue;
            if (domainFilter != null && !entity.getFeature().getLabel().matches(domainFilter) && !entity.getFeature().getAcc().matches(domainFilter)) continue;
            dataTable.addRow();
            dataTable.setCell(row, 0, entity.getFeature().getAcc(), Util.createHref(entity.getFeature().getFeatureDatabase().getUrl(), entity.getFeature().getAcc(), "_feature_database_window"), null);
            dataTable.setCell(row, 1, entity.getInputIdsCount(), integerNumberFormat.format(entity.getInputIdsCount()), null);
            dataTable.setCell(row, 2, entity.getMedian(), decimalNumberFormat.format(entity.getMedian()), null);
            dataTable.setCell(row, 3, entity.getPValue(), scientificNumberFormat.format(entity.getPValue()), null);
            dataTable.setCell(row, 4, entity.getPValueCorrected(), scientificNumberFormat.format(entity.getPValueCorrected()), null);
            dataTable.setCell(row, 5, entity.getRho(), decimalNumberFormat.format(entity.getRho()), null);
            dataTable.setValue(row, 6, entity.getFeature().getLabel());
            List<String> ipros = entity.getFeature().getInterpros();
            dataTable.setCell(row, 7, ipros.isEmpty() ? null : ipros.get(0), Util.createHref("http://www.ebi.ac.uk/interpro/IEntry?ac=", ipros, ", ", "_interpro_window"), null);
            row++;
        }
        Options options = Options.create();
        options.setPage(Policy.ENABLE);
        options.setAllowHtml(true);
        options.setPageSize(pageSize);
        options.setShowRowNumber(true);
        options.setSort(Policy.ENABLE);
        JsArray<Selection> emptyArray = JavaScriptObject.createArray().cast();
        table.setSelections(emptyArray);
        table.draw(dataTable, options);
        getWidget().setVisible(true);
    }

    private void downloadData() {
        if (!GWT.isClient()) return;
        StringBuilder buf = new StringBuilder();
        buf.append("Database\tProtein Feature\tGenes\tMedian\tP-Value\tCorrected\tRho\tFeature Label\tInterpro ID\r\n");
        for (Map.Entry<FeatureDatabase, List<ResultDataEntity>> entry : data.getData().entrySet()) {
            for (ResultDataEntity row : entry.getValue()) {
                buf.append(entry.getKey().getName()).append('\t');
                buf.append(row.getFeature().getAcc()).append('\t');
                buf.append(row.getInputIdsCount()).append('\t');
                buf.append(row.getMedian()).append('\t');
                buf.append(row.getPValue()).append('\t');
                buf.append(row.getPValueCorrected()).append('\t');
                buf.append(row.getRho()).append('\t');
                buf.append(row.getFeature().getLabel()).append('\t');
                buf.append(row.getFeature().getInterpros().toString()).append("\r\n");
            }
        }
        ButtonElement b = DOM.getElementById("dd").cast();
        b.setValue(buf.toString());
        b.click();
    }

    public void showData(ResultData resultData) {
        this.data = resultData;
        String featureDatabaseName = Pfam.FEATURE_DB.getName();
        int selectedIdx = databasesListBox.getSelectedIndex();
        if (selectedIdx != -1) featureDatabaseName = databasesListBox.getValue(selectedIdx);
        databasesListBox.clear();
        if (data != null) {
            Set<FeatureDatabase> databases = data.getData().keySet();
            int i = 0;
            selectedIdx = 0;
            for (FeatureDatabase db : databases) {
                databasesListBox.addItem(db.getName());
                if (db.getName().equals(featureDatabaseName)) selectedIdx = i;
                i++;
            }
            databasesListBox.setSelectedIndex(selectedIdx);
        }
        drawData();
    }

    public void onClick(Widget sender) {
        if (sender == applyFilterButton) applyFilters(true, false); else if (sender == searchButton) applyFilters(false, true); else if (sender == searchAndFilterButton) applyFilters(true, true); else if (sender == downloadButton) downloadData();
    }

    public void onChange(Widget sender) {
        if (sender == databasesListBox) {
            drawData();
        }
    }

    private void applyFilters(boolean applyValueFilters, boolean applyDomainFilter) {
        setMinGenesFilter(null);
        setMaxPValueFilter(null);
        setMaxPValueCorrectedFilter(null);
        setDomainFilter(null);
        if (applyValueFilters) {
            try {
                String text = minGenesCountFilterBox.getText().trim();
                if (text.length() != 0) setMinGenesFilter((int) integerNumberFormat.parse(text));
            } catch (NumberFormatException e) {
                minGenesCountFilterBox.setText("");
            }
            try {
                String text = maxPValueFilterBox.getText().trim();
                if (text.length() != 0) setMaxPValueFilter(scientificNumberFormat.parse(text));
            } catch (NumberFormatException e) {
                maxPValueFilterBox.setText("");
            }
            try {
                String text = maxPValueCorrectedFilterBox.getText().trim();
                if (text.length() != 0) setMaxPValueCorrectedFilter(scientificNumberFormat.parse(text));
            } catch (NumberFormatException e) {
                maxPValueCorrectedFilterBox.setText("");
            }
        }
        if (applyDomainFilter) {
            String text = domainFilterBox.getText().trim();
            if (text.length() != 0) {
                text = text.replace("\\", "\\\\").replace("+", "\\+").replace("|", "\\|").replace("{", "\\{").replace("}", "\\}").replace("[", "\\[").replace("]", "\\]").replace("(", "\\(").replace(")", "\\)").replace("^", "\\^").replace("$", "\\$").replace(".", "\\.").replace("#", "\\#").replace("*", "\\*").replace("?", "\\?");
                setDomainFilter(".*" + text + ".*");
            }
        }
        drawData();
    }

    private Panel createFiltersPanel() {
        FlowPanel parametersPanel = new FlowPanel();
        parametersPanel.addStyleName("parameters-panel");
        Label label = new InlineLabel("Min. number of genes:");
        parametersPanel.add(label);
        if (minGenesFilter != null) minGenesCountFilterBox.setText(integerNumberFormat.format((double) minGenesFilter));
        minGenesCountFilterBox.setWidth("2em");
        minGenesCountFilterBox.setMaxLength(3);
        minGenesCountFilterBox.addStyleName("limit-text-box");
        parametersPanel.add(minGenesCountFilterBox);
        label = new InlineLabel("and max. P-Value:");
        parametersPanel.add(label);
        if (maxPValueFilter != null) maxPValueFilterBox.setText(scientificNumberFormat.format(maxPValueFilter));
        maxPValueFilterBox.setText("0.01");
        maxPValueFilterBox.setWidth("4em");
        maxPValueFilterBox.setMaxLength(10);
        maxPValueFilterBox.addStyleName("limit-text-box");
        parametersPanel.add(maxPValueFilterBox);
        label = new InlineLabel("and max. P-Value (corrected):");
        parametersPanel.add(label);
        if (maxPValueCorrectedFilter != null) maxPValueCorrectedFilterBox.setText(scientificNumberFormat.format(maxPValueCorrectedFilter));
        maxPValueCorrectedFilterBox.setWidth("4em");
        maxPValueCorrectedFilterBox.setMaxLength(10);
        maxPValueCorrectedFilterBox.addStyleName("limit-text-box");
        parametersPanel.add(maxPValueCorrectedFilterBox);
        applyFilterButton.addStyleName("refresh-button");
        parametersPanel.add(applyFilterButton);
        applyFilterButton.addClickListener(this);
        downloadButton.addStyleName("download-button");
        parametersPanel.add(downloadButton);
        downloadButton.addClickListener(this);
        return parametersPanel;
    }

    private Panel createSearchPanel() {
        FlowPanel searchPanel = new FlowPanel();
        searchPanel.addStyleName("parameters-panel");
        Label label = new InlineLabel("Search for a Pfam ID:");
        searchPanel.add(label);
        if (domainFilter != null) domainFilterBox.setText(domainFilter);
        domainFilterBox.setWidth("10em");
        domainFilterBox.setMaxLength(45);
        domainFilterBox.addStyleName("search-text-box");
        searchPanel.add(domainFilterBox);
        searchButton.addStyleName("search-button");
        searchPanel.add(searchButton);
        searchButton.addClickListener(this);
        searchAndFilterButton.addStyleName("search-filter-button");
        searchPanel.add(searchAndFilterButton);
        searchAndFilterButton.addClickListener(this);
        label = new InlineLabel("Select a database: ");
        label.addStyleName("database-selector-label");
        searchPanel.add(label);
        databasesListBox.addChangeListener(this);
        databasesListBox.addStyleName("database-selector-list");
        searchPanel.add(databasesListBox);
        return searchPanel;
    }

    public void availableListsUpdated(Collection<DataRef> lists) {
        if (data != null && !lists.contains(data.getRef())) showData(null);
    }

    public void onFailure(Throwable caught) {
    }

    public void onSuccess(ResultData resultData) {
        showData(resultData);
    }
}
