package com.ivis.xprocess.ui.widgets;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import com.ivis.xprocess.core.Consumable;
import com.ivis.xprocess.core.Organization;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.ui.datawrappers.DataCacheManager;
import com.ivis.xprocess.ui.datawrappers.ElementWrapper;
import com.ivis.xprocess.ui.datawrappers.OrganizationWrapper;
import com.ivis.xprocess.ui.datawrappers.PersonWrapper;
import com.ivis.xprocess.ui.datawrappers.UnallocatedAvailabilityWrapper;
import com.ivis.xprocess.ui.datawrappers.process.ConsumableWrapper;
import com.ivis.xprocess.ui.datawrappers.project.ProjectResourceWrapper;
import com.ivis.xprocess.ui.datawrappers.project.ProjectWrapper;
import com.ivis.xprocess.ui.dialogs.CalendarDialog;
import com.ivis.xprocess.ui.inplaceediting.ViewText;
import com.ivis.xprocess.ui.preferences.managers.ColumnPreferenceChangeManager;
import com.ivis.xprocess.ui.preferences.managers.DatePreferenceChangeManager;
import com.ivis.xprocess.ui.preferences.managers.IPreferencePageListener;
import com.ivis.xprocess.ui.preferences.managers.PreferenceChangeManager.ChangedPreference;
import com.ivis.xprocess.ui.properties.DialogMessages;
import com.ivis.xprocess.ui.properties.WidgetMessages;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory;
import com.ivis.xprocess.ui.refresh.ChangeRecord;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory.ChangeEvent;
import com.ivis.xprocess.ui.tables.columns.TaskColumnWarehouse;
import com.ivis.xprocess.ui.tables.columns.definition.MoneyColumn;
import com.ivis.xprocess.ui.tables.columns.properties.AppearanceProperties;
import com.ivis.xprocess.ui.tables.columns.properties.CommonAppearanceProperties;
import com.ivis.xprocess.ui.tables.columns.properties.ViewAppearanceProperties;
import com.ivis.xprocess.ui.util.DateFormatter;
import com.ivis.xprocess.ui.util.FontAndColorManager;
import com.ivis.xprocess.ui.util.KeyUtil;
import com.ivis.xprocess.ui.util.TestHarness;
import com.ivis.xprocess.ui.util.TreeUtil;
import com.ivis.xprocess.ui.util.ViewUtil;
import com.ivis.xprocess.ui.view.providers.PricingTableContentProvider;
import com.ivis.xprocess.ui.view.providers.PricingTableLabelProvider;
import com.ivis.xprocess.ui.views.sorters.AvailabilitySorter;
import com.ivis.xprocess.util.Day;
import com.ivis.xprocess.util.Money;
import com.ivis.xprocess.util.impl.MoneyImpl;

public class PricingTable extends Composite implements IPreferencePageListener {

    private static final Logger logger = Logger.getLogger(PricingTable.class.getName());

    private ElementWrapper elementWrapper;

    private Day startOfDayColumns;

    private Composite navigationComposite;

    private Label datelabel;

    private TreeViewer pricingTreeViewer;

    private MoneyColumn[] pricingColumns;

    private TreeColumn[] treeColumns;

    private TreeEditor treeEditor;

    protected Control editorControl;

    private int column;

    private int row;

    private TreeItem item;

    private Point point;

    private boolean displayingError = false;

    private Button dateButton;

    private Button moveToThisWeekButton;

    public PricingTable(Composite parent, int style, ElementWrapper elementWrapper) {
        super(parent, style);
        this.elementWrapper = elementWrapper;
        startOfDayColumns = new Day().getPrevious(Day.DayOfWeek.MONDAY, false);
        addPropertyListeners();
        initialize();
    }

    private void initialize() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        this.setLayout(gridLayout);
        createNavigationBar();
        pricingTreeViewer = new TreeViewer(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
        GridData layoutData = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
        layoutData.horizontalSpan = 2;
        pricingTreeViewer.getTree().setLayoutData(layoutData);
        pricingTreeViewer.getTree().setHeaderVisible(true);
        pricingTreeViewer.getTree().setLinesVisible(true);
        if (elementWrapper instanceof OrganizationWrapper || elementWrapper instanceof PersonWrapper) {
            createOrganizationPricingColumns();
        }
        if (elementWrapper instanceof ConsumableWrapper) {
            createConsumablePricingColumns();
        }
        pricingTreeViewer.setContentProvider(new PricingTableContentProvider());
        pricingTreeViewer.setLabelProvider(new PricingTableLabelProvider(this));
        if (!(elementWrapper instanceof ConsumableWrapper)) {
            pricingTreeViewer.setSorter(new AvailabilitySorter());
        }
        pricingTreeViewer.setInput(this);
        addListeners();
        treeEditor = new TreeEditor(pricingTreeViewer.getTree());
        treeEditor.horizontalAlignment = SWT.LEFT;
        treeEditor.grabHorizontal = true;
        setupTestHarness();
    }

    private void addListeners() {
        pricingTreeViewer.getTree().addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
                point = new Point(e.x, e.y);
                column = -1;
                row = -1;
                Tree tree = pricingTreeViewer.getTree();
                Rectangle clientArea = tree.getClientArea();
                int index = 0;
                TreeItem[] treeItems = TreeUtil.getAllItems(tree);
                while (index < treeItems.length) {
                    boolean visible = false;
                    item = treeItems[index];
                    for (int i = 0; i < tree.getColumns().length; i++) {
                        Rectangle rect = item.getBounds(i);
                        if (rect.contains(point)) {
                            column = i;
                            row = index;
                            break;
                        }
                        if (!visible && rect.intersects(clientArea)) {
                            visible = true;
                        }
                    }
                    index++;
                }
                if (elementWrapper instanceof OrganizationWrapper || elementWrapper instanceof PersonWrapper) {
                    if ((column > 0) && (row != -1)) {
                        cellEditor(pricingTreeViewer);
                    }
                }
                if (elementWrapper instanceof ConsumableWrapper) {
                    if ((column > -1) && (row != -1)) {
                        cellEditor(pricingTreeViewer);
                    }
                }
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseUp(MouseEvent e) {
            }
        });
    }

    protected void cellEditor(TreeViewer treeViewer) {
        if (item != null) {
            TreeItem treeItem = treeViewer.getTree().getItem(point);
            if ((treeItem == null) || treeItem.getData() instanceof UnallocatedAvailabilityWrapper) {
                return;
            }
            treeViewer.getTree().showColumn(treeViewer.getTree().getColumn(column));
            treeViewer.getTree().showItem(treeItem);
            ViewText viewText = new ViewText(treeViewer.getTree(), SWT.NONE);
            viewText.setFont(FontAndColorManager.getInstance().getEditorFont());
            viewText.setText(treeItem.getText(column));
            viewText.setFocus();
            editorControl = viewText;
            treeEditor.setEditor(editorControl, treeItem, column);
            addCellListeners();
        }
    }

    private void addCellListeners() {
        editorControl.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (KeyUtil.isReturnKey(e)) {
                    saveCell();
                }
                if (e.keyCode == SWT.ESC) {
                    editorControl.dispose();
                }
            }
        });
        editorControl.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                if ((editorControl != null) && !editorControl.isDisposed() && !displayingError) {
                    saveCell();
                }
            }
        });
    }

    private void saveCell() {
        String message = validPricing();
        try {
            if (message.equals("undefined")) {
                return;
            }
            if (message.length() > 0) {
                displayingError = true;
                MessageDialog.openError(ViewUtil.getCurrentShell(), "Pricing Entry Error", message);
                displayingError = false;
                return;
            }
            if (editorControl instanceof ViewText) {
                ViewText viewText = (ViewText) editorControl;
                String txt = viewText.getText();
                txt = txt.toUpperCase();
                Money money = null;
                try {
                    money = new MoneyImpl(txt);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Unable to convert cell text - " + txt + " to Money.", e);
                    return;
                }
                if (treeEditor.getItem().getData() instanceof PersonWrapper || treeEditor.getItem().getData() instanceof OrganizationWrapper) {
                    MoneyColumn moneyColumn = getMoneyColumn(column - 1);
                    if (treeEditor.getItem().getData() instanceof PersonWrapper) {
                        PersonWrapper personWrapper = (PersonWrapper) treeEditor.getItem().getData();
                        Person person = personWrapper.getPerson();
                        ChangeEventFactory.startChangeRecording(personWrapper);
                        ChangeEventFactory.addChange(personWrapper, ChangeEvent.FIELDS_CHANGED);
                        ChangeEventFactory.addPropertyChange(personWrapper, "item pricing");
                        person.getPricing().setPriceRecordWithPrice(moneyColumn.getDay(), moneyColumn.getDay(), money);
                        ChangeEventFactory.saveChanges();
                        ChangeEventFactory.stopChangeRecording();
                        pricingTreeViewer.refresh();
                    }
                    if (treeEditor.getItem().getData() instanceof OrganizationWrapper) {
                        OrganizationWrapper organizationWrapper = (OrganizationWrapper) treeEditor.getItem().getData();
                        Organization organization = (Organization) organizationWrapper.getElement();
                        for (Person person : organization.getPersons()) {
                            PersonWrapper personWrapper = (PersonWrapper) DataCacheManager.getWrapperByElement(person);
                            ChangeEventFactory.startChangeRecording(personWrapper);
                            ChangeEventFactory.addChange(personWrapper, ChangeEvent.FIELDS_CHANGED);
                            ChangeEventFactory.addPropertyChange(personWrapper, "item pricing");
                        }
                        organization.getPricing().setPriceRecordWithPrice(moneyColumn.getDay(), moneyColumn.getDay(), money);
                        ChangeEventFactory.saveChanges();
                        ChangeEventFactory.stopChangeRecording();
                        pricingTreeViewer.refresh();
                    }
                }
                if (treeEditor.getItem().getData() instanceof ConsumableWrapper) {
                    MoneyColumn moneyColumn = getMoneyColumn(column);
                    ConsumableWrapper consumableWrapper = (ConsumableWrapper) treeEditor.getItem().getData();
                    Consumable consumable = consumableWrapper.getConsumable();
                    ChangeEventFactory.startChangeRecording(consumableWrapper);
                    ChangeEventFactory.addChange(consumableWrapper, ChangeEvent.FIELDS_CHANGED);
                    ChangeEventFactory.addPropertyChange(consumableWrapper, "item pricing");
                    consumable.getPricing().setPriceRecordWithPrice(moneyColumn.getDay(), moneyColumn.getDay(), money);
                    ChangeEventFactory.saveChanges();
                    ChangeEventFactory.stopChangeRecording();
                    pricingTreeViewer.refresh();
                }
            }
        } finally {
            editorControl.dispose();
        }
    }

    private String validPricing() {
        String message = "";
        if (editorControl instanceof ViewText) {
            ViewText viewText = (ViewText) editorControl;
            String txt = viewText.getText();
            if (txt.toLowerCase().equals("undefined")) {
                return "undefined";
            }
            txt = txt.toUpperCase();
            try {
                Money money = new MoneyImpl(txt);
                if (money.getAmount() < 0.0) {
                    message = "Please provide a positive amount";
                }
            } catch (Exception e) {
                message = "Please provide a valid money value i.e. \"25.5 USD\"";
            }
        }
        return message;
    }

    private void createNavigationBar() {
        navigationComposite = new Composite(this, SWT.NONE);
        GridData layoutData = new GridData();
        layoutData.horizontalSpan = 2;
        layoutData.verticalAlignment = SWT.CENTER;
        navigationComposite.setLayoutData(layoutData);
        GridLayout navigationCompositeGridLayout = new GridLayout();
        navigationCompositeGridLayout.numColumns = 7;
        navigationComposite.setLayout(navigationCompositeGridLayout);
        ToolBar leftNavigationToolBar = new ToolBar(navigationComposite, SWT.NONE);
        Action leftOneWeekAction = new Action("Back One Week", IAction.AS_PUSH_BUTTON) {

            @Override
            public void run() {
                moveTo(-7);
            }
        };
        leftOneWeekAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "/toolbar/navigate_left2.gif"));
        Action leftOneDayAction = new Action("Back One Day", IAction.AS_PUSH_BUTTON) {

            @Override
            public void run() {
                moveTo(-1);
            }
        };
        leftOneDayAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "/toolbar/navigate_left.gif"));
        ToolBarManager toolBarManager = new ToolBarManager(leftNavigationToolBar);
        toolBarManager.add(leftOneWeekAction);
        toolBarManager.add(leftOneDayAction);
        toolBarManager.update(true);
        datelabel = new Label(navigationComposite, SWT.CENTER);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, new DateFormatter().getDefaultLocale());
        datelabel.setText(dateFormat.format(startOfDayColumns.getJavaDate()));
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        datelabel.setLayoutData(gridData);
        dateButton = new Button(navigationComposite, SWT.FLAT);
        dateButton.setText(DialogMessages.dialog_button_dotdotdot);
        dateButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                openCalendarDialog();
            }
        });
        moveToThisWeekButton = new Button(navigationComposite, SWT.FLAT);
        moveToThisWeekButton.setText(WidgetMessages.show_this_week_button_label);
        moveToThisWeekButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                moveTo(new Day().getPrevious(Day.DayOfWeek.MONDAY, false));
            }
        });
        ToolBar rightNavigationToolBar = new ToolBar(navigationComposite, SWT.NONE);
        Action rightOneDayAction = new Action("Forward One Day", IAction.AS_PUSH_BUTTON) {

            @Override
            public void run() {
                moveTo(1);
            }
        };
        rightOneDayAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "/toolbar/navigate_right.gif"));
        Action rightOneWeekAction = new Action("Forward One Week", IAction.AS_PUSH_BUTTON) {

            @Override
            public void run() {
                moveTo(7);
            }
        };
        rightOneWeekAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "/toolbar/navigate_right2.gif"));
        Action expandAllAction = new Action("Expand All", IAction.AS_PUSH_BUTTON) {

            @Override
            public void run() {
                pricingTreeViewer.expandAll();
            }
        };
        expandAllAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "/misc/expandall.gif"));
        Action collapseAllAction = new Action("Collapse All", IAction.AS_PUSH_BUTTON) {

            @Override
            public void run() {
                pricingTreeViewer.collapseAll();
            }
        };
        collapseAllAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "/misc/collapseall.gif"));
        toolBarManager = new ToolBarManager(rightNavigationToolBar);
        toolBarManager.add(rightOneDayAction);
        toolBarManager.add(rightOneWeekAction);
        toolBarManager.add(expandAllAction);
        toolBarManager.add(collapseAllAction);
        toolBarManager.update(true);
    }

    private void moveTo(int daysToMove) {
        this.layout(false);
        startOfDayColumns = startOfDayColumns.addDays(daysToMove);
        if (elementWrapper instanceof OrganizationWrapper || elementWrapper instanceof PersonWrapper) {
            createOrganizationPricingColumns();
        }
        if (elementWrapper instanceof ConsumableWrapper) {
            createConsumablePricingColumns();
        }
        updateDateLabel();
        pricingTreeViewer.refresh();
        this.layout(true);
    }

    private void moveTo(Day day) {
        this.layout(false);
        startOfDayColumns = day;
        if (elementWrapper instanceof OrganizationWrapper || elementWrapper instanceof PersonWrapper) {
            createOrganizationPricingColumns();
        }
        if (elementWrapper instanceof ConsumableWrapper) {
            createConsumablePricingColumns();
        }
        updateDateLabel();
        pricingTreeViewer.refresh();
        this.layout(true);
    }

    private void openCalendarDialog() {
        CalendarDialog calendarDialog = new CalendarDialog(dateButton);
        calendarDialog.setTitle(DialogMessages.navigate_to_calendar_dialog_title);
        calendarDialog.setDay(startOfDayColumns);
        int button = calendarDialog.open();
        if (button == IDialogConstants.OK_ID) {
            Date returnedDate = calendarDialog.getCalendar().getTime();
            moveTo(new Day(returnedDate));
        }
    }

    private void createOrganizationPricingColumns() {
        for (TreeColumn column : pricingTreeViewer.getTree().getColumns()) {
            column.dispose();
        }
        pricingColumns = new MoneyColumn[7];
        treeColumns = new TreeColumn[8];
        TreeColumn column = new TreeColumn(pricingTreeViewer.getTree(), 0);
        column.setText("Name");
        column.setWidth(200);
        treeColumns[0] = column;
        Day dayPointer = startOfDayColumns;
        for (int i = 0; i < pricingColumns.length; i++) {
            MoneyColumn pricingColumn = new MoneyColumn();
            ViewAppearanceProperties viewAppearanceProperties = new ViewAppearanceProperties(80, true, true);
            CommonAppearanceProperties commonAppearanceProperties = TaskColumnWarehouse.getCommonProperty(pricingColumn);
            AppearanceProperties appearanceProperties = new AppearanceProperties(commonAppearanceProperties, viewAppearanceProperties);
            TaskColumnWarehouse.placeAppearancePropertyIntoWarehouse(pricingColumn, appearanceProperties);
            pricingColumn.setName("" + dayPointer);
            pricingColumn.setEditable(true);
            pricingColumn.setDay(dayPointer);
            pricingColumns[i] = pricingColumn;
            column = new TreeColumn(pricingTreeViewer.getTree(), i + 1);
            column.setText(pricingColumn.getName());
            column.setWidth(pricingColumn.getWidth());
            column.setResizable(pricingColumn.isResizable());
            treeColumns[i + 1] = column;
            dayPointer = dayPointer.addDays(1);
        }
    }

    private void createConsumablePricingColumns() {
        for (TreeColumn column : pricingTreeViewer.getTree().getColumns()) {
            column.dispose();
        }
        pricingColumns = new MoneyColumn[7];
        treeColumns = new TreeColumn[7];
        TreeColumn column;
        Day dayPointer = startOfDayColumns;
        for (int i = 0; i < pricingColumns.length; i++) {
            MoneyColumn pricingColumn = new MoneyColumn();
            ViewAppearanceProperties viewAppearanceProperties = new ViewAppearanceProperties(80, true, true);
            CommonAppearanceProperties commonAppearanceProperties = TaskColumnWarehouse.getCommonProperty(pricingColumn);
            AppearanceProperties appearanceProperties = new AppearanceProperties(commonAppearanceProperties, viewAppearanceProperties);
            TaskColumnWarehouse.placeAppearancePropertyIntoWarehouse(pricingColumn, appearanceProperties);
            pricingColumn.setName("" + dayPointer);
            pricingColumn.setEditable(true);
            pricingColumn.setDay(dayPointer);
            pricingColumns[i] = pricingColumn;
            column = new TreeColumn(pricingTreeViewer.getTree(), i);
            column.setText(pricingColumn.getName());
            column.setWidth(pricingColumn.getWidth());
            column.setResizable(pricingColumn.isResizable());
            treeColumns[i] = column;
            dayPointer = dayPointer.addDays(1);
        }
    }

    private void addPropertyListeners() {
        ColumnPreferenceChangeManager.addPreferencePageListener(this);
        DatePreferenceChangeManager.addPreferencePageListener(this);
    }

    private void updateDateLabel() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, new DateFormatter().getDefaultLocale());
        datelabel.setText(dateFormat.format(startOfDayColumns.getJavaDate()));
    }

    public ElementWrapper getElementWrapper() {
        return elementWrapper;
    }

    public MoneyColumn getMoneyColumn(int columnIndex) {
        return pricingColumns[columnIndex];
    }

    public boolean refreshEvent(final ChangeRecord changeRecord) {
        if (changeRecord.hasProperty("party availability") || changeRecord.hasProperty("item pricing") || changeRecord.hasProperty("modifyingpricing")) {
            return true;
        }
        if (changeRecord.hasChange(ChangeEvent.ELEMENT_DELETED)) {
            if (changeRecord.getElementWrapper() instanceof PersonWrapper || changeRecord.getElementWrapper() instanceof ProjectWrapper) {
                return true;
            }
        }
        if (changeRecord.hasChange(ChangeEvent.NEW_ELEMENT) && changeRecord.getElementWrapper() instanceof ProjectResourceWrapper) {
            ProjectResourceWrapper projectPersonWrapper = (ProjectResourceWrapper) changeRecord.getElementWrapper();
            Person person = projectPersonWrapper.getPerson();
            if (elementWrapper instanceof OrganizationWrapper) {
                OrganizationWrapper organizationWrapper = (OrganizationWrapper) elementWrapper;
                if (organizationWrapper.isPersonInOrganization(person)) {
                    return true;
                }
            }
        }
        if (changeRecord.getElementWrapper() instanceof PersonWrapper) {
            PersonWrapper personWrapper = (PersonWrapper) changeRecord.getElementWrapper();
            if (changeRecord.hasChange(ChangeEvent.NEW_ELEMENT) || changeRecord.hasProperty("NAME")) {
                if (elementWrapper instanceof OrganizationWrapper) {
                    OrganizationWrapper organizationWrapper = (OrganizationWrapper) elementWrapper;
                    if (personWrapper.getParentUuid().equals(organizationWrapper.getUuid())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void refresh(List<ChangeRecord> changeRecords) {
        boolean refreshAll = false;
        Set<Object> individualElementWrappersToRefresh = new HashSet<Object>();
        for (ChangeRecord changeRecord : changeRecords) {
            if (changeRecord.hasChange(ChangeEvent.VCS_UPDATE)) {
                refreshAll = true;
                break;
            }
            if (changeRecord.hasProperty("party availability") || changeRecord.hasProperty("item pricing") || changeRecord.hasProperty("modifyingpricing")) {
                individualElementWrappersToRefresh.add(changeRecord.getElementWrapper());
            }
            if (changeRecord.hasChange(ChangeEvent.ELEMENT_DELETED)) {
                if (changeRecord.getElementWrapper() instanceof PersonWrapper || changeRecord.getElementWrapper() instanceof ProjectWrapper) {
                    refreshAll = true;
                    break;
                }
            }
            if (changeRecord.getElementWrapper() instanceof PersonWrapper) {
                PersonWrapper personWrapper = (PersonWrapper) changeRecord.getElementWrapper();
                if (elementWrapper instanceof OrganizationWrapper) {
                    OrganizationWrapper organizationWrapper = (OrganizationWrapper) elementWrapper;
                    if (personWrapper.getParentUuid().equals(organizationWrapper.getUuid())) {
                        if (changeRecord.hasChange(ChangeEvent.NEW_ELEMENT)) {
                            refreshAll = true;
                            break;
                        }
                        if (changeRecord.hasProperty("NAME")) {
                            individualElementWrappersToRefresh.add(changeRecord.getElementWrapper());
                        }
                    }
                }
            }
        }
        if (refreshAll) {
            pricingTreeViewer.refresh();
        } else {
            for (Object elementWrapper : individualElementWrappersToRefresh) {
                pricingTreeViewer.refresh(elementWrapper, true);
            }
        }
    }

    public void preferencePageChanged(ChangedPreference changedPreference) {
        if (!this.isDisposed()) {
            this.layout(false);
            createConsumablePricingColumns();
            updateDateLabel();
            pricingTreeViewer.refresh();
            this.layout(true);
        }
    }

    private void setupTestHarness() {
        TestHarness.name(pricingTreeViewer.getTree(), TestHarness.PRICING_TABLE);
    }
}
