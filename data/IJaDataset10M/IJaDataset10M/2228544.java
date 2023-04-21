package net.mufly.client;

import net.mufly.client.configuration.ConfigurationRemote;
import net.mufly.client.core.ApplicationParameters;
import net.mufly.client.core.DefaultCallback;
import net.mufly.client.ui.MessagePanel;
import net.mufly.client.ui.SummaryPanel;
import net.mufly.client.ui.account.AccountManager;
import net.mufly.client.ui.filter.FilterPanel;
import net.mufly.client.ui.filter.GridFilter;
import net.mufly.client.ui.grid.TransactionGrid;
import net.mufly.client.ui.summary.SummaryTab;
import net.mufly.client.ui.table.TransactionTable;
import net.mufly.client.ui.tag.TagManager;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SliderField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Mufly implements EntryPoint {

    private static final String mainWidget = "muflyMainWindow";

    private MuflyConstants constants;

    private TabPanel tabPanel;

    private DockPanel dock;

    private FilterPanel filterPanel;

    private TransactionTable transTable;

    private SummaryPanel summaryPanel;

    private MessagePanel messagePanel;

    private AccountManager accountManager;

    private TagManager tagManager;

    private SummaryTab summaryTab;

    private TotalStatusBar totalButBar;

    private ContentPanel gridPanel;

    private ContentPanel chartPanel;

    private FormPanel formPanel;

    private GridFilter gridFilter;

    private TransactionGrid tranGrid;

    private Viewport viewport;

    private BasePagingLoader<PagingLoadResult<BeanModel>> loader;

    public TransactionTable getTransactionTable() {
        return this.transTable;
    }

    public SummaryPanel getSummaryPanel() {
        return this.summaryPanel;
    }

    public FilterPanel getFilterPanel() {
        return this.filterPanel;
    }

    public TagManager getTagManager() {
        return this.tagManager;
    }

    public AccountManager getAccountManager() {
        return this.accountManager;
    }

    public MuflyConstants getConstants() {
        return this.constants;
    }

    public TransactionGrid getTransactionGrid() {
        return this.tranGrid;
    }

    public GridFilter getGridFilter() {
        return this.gridFilter;
    }

    public void onModuleLoad() {
        GXT.setDefaultTheme(Theme.BLUE, true);
        ApplicationParameters.getInstance().setApplication(this);
        constants = GWT.create(MuflyConstants.class);
        createMainPanel();
        gridFilter = new GridFilter();
        viewport = new Viewport();
        buildMainLayout(viewport);
        tranGrid = new TransactionGrid(gridFilter, gridPanel, formPanel);
        RootPanel.get(Mufly.mainWidget).add(viewport);
        messagePanel.setStatusMessage(constants.init());
        ConfigurationRemote.Util.getInstance().initServerConfiguration(new DefaultCallback() {

            public void onSuccess(Object result) {
                messagePanel.setStatusMessage((String) result);
                filterPanel.setEnabled(true, false, false, false, true, true);
                GXT.hideLoadingPanel("loading");
            }
        });
    }

    public void displayErrorMessage(String errorMessage) {
    }

    private void createMainPanel() {
        dock = new DockPanel();
        accountManager = new AccountManager();
        tagManager = new TagManager();
        filterPanel = new FilterPanel();
        transTable = new TransactionTable();
        summaryPanel = new SummaryPanel();
        messagePanel = new MessagePanel();
        summaryTab = new SummaryTab();
        dock.setSpacing(4);
        dock.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
        dock.add(filterPanel, DockPanel.NORTH);
        dock.add(summaryPanel, DockPanel.SOUTH);
        dock.add(transTable, DockPanel.CENTER);
        tabPanel = new TabPanel();
        tabPanel.setAutoWidth(true);
        tabPanel.setPlain(true);
        TabItem dockItem = new TabItem();
        dockItem.setText(constants.transMngmnt());
        dockItem.add(dock);
        TabItem summaryItem = new TabItem();
        summaryItem.setText(constants.summary());
        summaryItem.add(summaryTab);
        summaryItem.addListener(Events.Select, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent be) {
                summaryTab.totalMonth().update();
                summaryTab.totalAccount().update();
            }
        });
        TabItem accountItem = new TabItem();
        accountItem.setText(constants.accMngmnt());
        accountItem.add(accountManager);
        TabItem tagItem = new TabItem();
        tagItem.setText(constants.tagMngmnt());
        tagItem.add(tagManager);
        TabItem newTagItem = new TabItem();
        newTagItem.setText("New Tag Manager");
        tabPanel.add(dockItem);
        tabPanel.add(summaryItem);
        tabPanel.add(accountItem);
        tabPanel.add(tagItem);
        tabPanel.add(newTagItem);
    }

    private void buildMainLayout(Viewport viewport) {
        LayoutContainer innerRight = new LayoutContainer();
        LayoutContainer innerLeft = new LayoutContainer();
        LayoutContainer appHeading = new LayoutContainer();
        appHeading.addText("Mufly v.0.3");
        appHeading.addStyleName("appHeading");
        gridPanel = new ContentPanel();
        gridPanel.setHeading("Transaction List");
        formPanel = new FormPanel();
        formPanel.setHeading("Tag Cloud????");
        chartPanel = new ContentPanel();
        chartPanel.setHeading("Account Cloud????");
        BorderLayoutData inNorth = new BorderLayoutData(LayoutRegion.NORTH, 320);
        inNorth.setMinSize(320);
        inNorth.setMaxSize(600);
        inNorth.setSplit(true);
        BorderLayoutData inCenter = new BorderLayoutData(LayoutRegion.CENTER);
        inCenter.setMargins(new Margins(5, 0, 0, 0));
        inCenter.setFloatable(true);
        innerRight.setLayout(new BorderLayout());
        innerRight.add(formPanel, inNorth);
        innerRight.add(chartPanel, inCenter);
        BorderLayoutData north = new BorderLayoutData(LayoutRegion.NORTH, 30);
        BorderLayoutData outCenter = new BorderLayoutData(LayoutRegion.CENTER);
        outCenter.setMargins(new Margins(5));
        BorderLayoutData east = new BorderLayoutData(LayoutRegion.EAST, 400);
        east.setMinSize(400);
        east.setMaxSize(600);
        east.setSplit(true);
        east.setMargins(new Margins(5, 5, 5, 0));
        viewport.setLayout(new BorderLayout());
        viewport.add(appHeading, north);
        viewport.add(gridPanel, outCenter);
        viewport.add(innerRight, east);
    }

    public class TotalStatusBar {

        private Html total;

        public TotalStatusBar(ContentPanel cp) {
            total = new Html(constants.total());
            cp.setBottomComponent(total);
        }
    }

    ;
}
