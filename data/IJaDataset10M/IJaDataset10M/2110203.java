package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.eclipse.builder.common.InteractionChangeAdapter;
import com.volantis.mcs.eclipse.builder.common.InteractionFocussable;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.common.FormSection;
import com.volantis.mcs.eclipse.builder.editors.common.ListProxyContentProvider;
import com.volantis.mcs.eclipse.builder.editors.common.ProxyLabelDecorator;
import com.volantis.mcs.eclipse.builder.wizards.themes.SelectorGroupWizard;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.controls.ActionButton;
import com.volantis.mcs.interaction.BaseProxy;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.ProxyEvent;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.model.path.IndexedStep;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.PropertyStep;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.variants.theme.ThemeContentBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.themes.SelectorGroup;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.model.ThemeModel;
import com.volantis.mcs.themes.parsing.ObjectParser;
import com.volantis.mcs.themes.parsing.ObjectParserFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.Section;
import java.util.Iterator;
import java.util.List;

/**
 */
public class SelectorsSection extends FormSection implements InteractionFocussable {

    private static String RESOURCE_PREFIX = "SelectorsSection.";

    private static IStructuredContentProvider CONTENT_PROVIDER = new ListProxyContentProvider();

    private static final ObjectParser SELECTOR_PARSER = ObjectParserFactory.getDefaultInstance().createRuleSelectorParser();

    private static final CSSParserFactory CSS_PARSER_FACTORY = CSSParserFactory.getDefaultInstance();

    private static final CSSParser CSS_PARSER = CSS_PARSER_FACTORY.createLaxParser();

    private static final Image SELECTOR_IMAGE = EclipseCommonMessages.getPolicyIcon("sequenceSelector");

    private static final int COLUMN_SELECTOR = 0;

    private static final int COLUMN_ORDER = 1;

    private ThemeEditorContext context;

    private Action addAction;

    private Action deleteAction;

    private Action newAction;

    private TableViewer ruleTable;

    private ListenerList selectionListeners = new ListenerList();

    public SelectorsSection(Composite composite, int i, ThemeEditorContext context) {
        super(composite, i);
        this.context = context;
        createDisplayArea();
    }

    public void addSelectionChangeListener(ISelectionChangedListener listener) {
        selectionListeners.add(listener);
    }

    public void removeSelectionChangeListener(ISelectionChangedListener listener) {
        selectionListeners.remove(listener);
    }

    private void handleRuleSelection(SelectionChangedEvent event) {
        Object[] listeners = selectionListeners.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] != null && listeners[i] instanceof ISelectionChangedListener) {
                ((ISelectionChangedListener) listeners[i]).selectionChanged(event);
            }
        }
    }

    private void createDisplayArea() {
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        Section section = createSection(this, EditorMessages.getString(RESOURCE_PREFIX + "title"), null, Section.TWISTIE | Section.EXPANDED);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);
        Composite displayArea = new Composite(section, SWT.NONE);
        section.setClient(displayArea);
        layout = new GridLayout(2, false);
        data = new GridData(GridData.FILL_BOTH);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        displayArea.setLayout(layout);
        displayArea.setLayoutData(data);
        displayArea.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        createRuleTable(displayArea);
        createActions();
        createButtons(displayArea);
        createContextMenu();
    }

    private final InteractionChangeAdapter tableRefresher = new InteractionChangeAdapter() {

        public void interactionChangeEvent(ProxyEvent event) {
            if (event.isOriginator()) {
                refreshRuleTable();
            }
        }
    };

    public void setVariant(BeanProxy deviceTheme) {
        boolean nullType = false;
        if (deviceTheme != null) {
            Proxy type = deviceTheme.getPropertyProxy(PolicyModel.VARIANT_TYPE);
            nullType = VariantType.NULL == type.getModelObject();
        }
        Object oldInput = ruleTable.getInput();
        if (oldInput != null && oldInput instanceof Proxy) {
            Proxy oldRules = (Proxy) oldInput;
            oldRules.removeListener(tableRefresher);
            oldRules.removeDiagnosticListener(tableRefresher);
        }
        ruleTable.setInput(null);
        if (!nullType && deviceTheme != null) {
            BaseProxy content = (BaseProxy) deviceTheme.getPropertyProxy(PolicyModel.CONTENT);
            BeanProxy concreteContent = (BeanProxy) content.getConcreteProxy();
            if (concreteContent == null && !content.isReadOnly()) {
                ThemeContentBuilder contentModel = InternalPolicyFactory.getInternalInstance().createThemeContentBuilder();
                content.setModelObject(contentModel);
                concreteContent = (BeanProxy) content.getConcreteProxy();
            }
            if (concreteContent != null) {
                BeanProxy styleSheet = (BeanProxy) concreteContent.getPropertyProxy(ThemeModel.STYLE_SHEET);
                Proxy rules = styleSheet.getPropertyProxy(ThemeModel.RULES);
                ruleTable.setInput(rules);
                rules.addListener(tableRefresher, true);
                rules.addDiagnosticListener(tableRefresher);
            }
        }
    }

    private void createRuleTable(Composite parent) {
        Composite tableComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 1;
        layout.marginWidth = 1;
        tableComposite.setLayout(layout);
        tableComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
        GridData data = new GridData(GridData.FILL_BOTH);
        tableComposite.setLayoutData(data);
        Table table = new Table(tableComposite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
        data = new GridData(GridData.FILL_BOTH);
        data.heightHint = EditorMessages.getInteger(RESOURCE_PREFIX + "tableMinHeight").intValue();
        table.setLayoutData(data);
        final TableColumn sequenceColumn = new TableColumn(table, SWT.LEFT);
        sequenceColumn.setText(EditorMessages.getString(RESOURCE_PREFIX + "sequenceColumn.title"));
        final TableColumn orderColumn = new TableColumn(table, SWT.LEFT);
        orderColumn.setText(EditorMessages.getString(RESOURCE_PREFIX + "orderColumn.title"));
        table.setHeaderVisible(true);
        TableLayout tableLayout = new TableLayout() {

            public void layout(Composite c, boolean flush) {
                super.layout(c, flush);
                if (c instanceof Table) {
                    int tableWidth = c.getClientArea().width;
                    int orderWidth = orderColumn.getWidth();
                    if ((sequenceColumn.getWidth() + orderWidth) < tableWidth) {
                        sequenceColumn.setWidth(tableWidth - orderWidth);
                    }
                }
            }
        };
        tableLayout.addColumnData(new ColumnWeightData(600, 700, true));
        tableLayout.addColumnData(new ColumnWeightData(50, 50, true));
        table.setLayout(tableLayout);
        ruleTable = new TableViewer(table);
        ruleTable.setContentProvider(CONTENT_PROVIDER);
        final ILabelDecorator decorator = new ProxyLabelDecorator();
        final ITableLabelProvider labelProvider = new ITableLabelProvider() {

            public Image getColumnImage(Object o, int i) {
                return (i == COLUMN_SELECTOR) ? decorator.decorateImage(SELECTOR_IMAGE, o) : null;
            }

            public String getColumnText(Object o, int i) {
                BeanProxy ruleProxy = (BeanProxy) o;
                String returnValue = null;
                if (i == COLUMN_SELECTOR) {
                    Proxy selectorsProxy = ruleProxy.getPropertyProxy(Rule.SELECTORS);
                    Object model = selectorsProxy.getModelObject();
                    returnValue = selectorListToText((List) model);
                } else if (i == COLUMN_ORDER) {
                    ListProxy rules = (ListProxy) getSelectedStyleSheetProxy().getPropertyProxy(ThemeModel.RULES);
                    int index = rules.getItemProxyIndex(ruleProxy);
                    returnValue = String.valueOf(index + 1);
                }
                return returnValue;
            }

            public void addListener(ILabelProviderListener iLabelProviderListener) {
            }

            public void dispose() {
            }

            public boolean isLabelProperty(Object o, String s) {
                return true;
            }

            public void removeListener(ILabelProviderListener iLabelProviderListener) {
            }
        };
        ruleTable.setLabelProvider(labelProvider);
        CellEditor editors[] = new CellEditor[2];
        editors[0] = new TextCellEditor(table);
        ruleTable.setCellEditors(editors);
        ruleTable.setColumnProperties(new String[] { "selectors", "order" });
        ruleTable.setCellModifier(new ICellModifier() {

            public boolean canModify(Object o, String s) {
                return "selectors".equals(s);
            }

            public Object getValue(Object o, String s) {
                Object value = null;
                if ("selectors".equals(s)) {
                    List selectors = ruleProxyToSelectorList((BeanProxy) o);
                    value = selectorListToText(selectors);
                }
                return value;
            }

            public void modify(Object tableItem, String columnName, Object newValue) {
                if ("selectors".equals(columnName)) {
                    TableItem item = (TableItem) tableItem;
                    BeanProxy ruleProxy = (BeanProxy) item.getData();
                    String oldValue = labelProvider.getColumnText(ruleProxy, COLUMN_SELECTOR);
                    if (!oldValue.equals(newValue)) {
                        List parsed = CSS_PARSER.parseSelectorGroup((String) newValue);
                        ListProxy selectorsProxy = (ListProxy) ruleProxy.getPropertyProxy(Rule.SELECTORS);
                        Operation setList = selectorsProxy.prepareSetModelObjectOperation(parsed);
                        context.executeOperation(setList);
                    }
                }
            }
        });
        table.pack();
        ruleTable.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                handleRuleSelection(event);
            }
        });
    }

    private void createActions() {
        addAction = new Action() {

            public void run() {
                ListProxy rules = (ListProxy) getSelectedStyleSheetProxy().getPropertyProxy(ThemeModel.RULES);
                Operation addOp = rules.prepareCreateAndAddProxyItemOperation();
                context.executeOperation(addOp);
                refreshRuleTable();
            }
        };
        addAction.setText(EditorMessages.getString(RESOURCE_PREFIX + "addAction.text"));
        newAction = new Action() {

            public void run() {
                SelectorGroupWizard wizard = new SelectorGroupWizard(getShell(), context.getProject());
                wizard.open();
                SelectorGroup group = wizard.getSelectorGroup();
                if (group != null) {
                    ListProxy rules = (ListProxy) getSelectedStyleSheetProxy().getPropertyProxy(ThemeModel.RULES);
                    Operation addOp = rules.prepareCreateAndAddProxyItemOperation();
                    context.executeOperation(addOp);
                    BeanProxy newRule = (BeanProxy) rules.getItemProxy(rules.size() - 1);
                    ListProxy selectors = (ListProxy) newRule.getPropertyProxy(Rule.SELECTORS);
                    Operation setOp = selectors.prepareSetModelObjectOperation(wizard.getSelectorGroup().getSelectors());
                    context.executeOperation(setOp);
                }
            }
        };
        newAction.setText(EditorMessages.getString(RESOURCE_PREFIX + "newAction.text"));
        deleteAction = new Action() {

            public void run() {
                ListProxy rules = (ListProxy) getSelectedStyleSheetProxy().getPropertyProxy(ThemeModel.RULES);
                IStructuredSelection selection = (IStructuredSelection) ruleTable.getSelection();
                if (!selection.isEmpty()) {
                    Proxy selected = (Proxy) selection.getFirstElement();
                    Operation delOp = rules.prepareRemoveProxyItemOperation(selected);
                    context.executeOperation(delOp);
                    refreshRuleTable();
                }
            }
        };
        deleteAction.setText(EditorMessages.getString(RESOURCE_PREFIX + "deleteAction.text"));
    }

    private void createButtons(Composite parent) {
        Composite buttonComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = EditorMessages.getInteger(RESOURCE_PREFIX + "verticalSpacing").intValue();
        buttonComposite.setLayout(layout);
        buttonComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        buttonComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        addButton(buttonComposite, newAction);
        addButton(buttonComposite, addAction);
        addButton(buttonComposite, deleteAction);
    }

    private void addButton(Composite parent, Action action) {
        if (action != null) {
            ActionButton button = new ActionButton(parent, SWT.PUSH, action);
            button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        }
    }

    private void createContextMenu() {
    }

    private String selectorListToText(List selectors) {
        String text = "";
        if (selectors != null && !selectors.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            Iterator it = selectors.iterator();
            while (it.hasNext()) {
                buffer.append(SELECTOR_PARSER.objectToText(it.next()));
                if (it.hasNext()) {
                    buffer.append(",");
                }
            }
            text = buffer.toString();
        }
        return text;
    }

    private List ruleProxyToSelectorList(BeanProxy rule) {
        ListProxy selectorsProxy = (ListProxy) rule.getPropertyProxy(Rule.SELECTORS);
        return (List) selectorsProxy.getModelObject();
    }

    public void setFocus(Path path) {
        boolean seekingRules = true;
        int stepCount = path.getStepCount();
        for (int i = 0; i < stepCount && seekingRules; i++) {
            Step step = path.getStep(i);
            if (step instanceof PropertyStep) {
                PropertyStep property = (PropertyStep) step;
                if (ThemeModel.RULES.getName().equals(property.getProperty())) {
                    if ((i + 1) < stepCount) {
                        Step deviceIndexStep = path.getStep(i + 1);
                        if (deviceIndexStep instanceof IndexedStep) {
                            int deviceThemeIndex = ((IndexedStep) deviceIndexStep).getIndex();
                            ISelection newSelection = new StructuredSelection(ruleTable.getElementAt(deviceThemeIndex));
                            ruleTable.setSelection(newSelection, true);
                        }
                    }
                    seekingRules = false;
                }
            }
        }
    }

    private BeanProxy getSelectedStyleSheetProxy() {
        BeanProxy variant = context.getSelectedVariant();
        BaseProxy content = (BaseProxy) variant.getPropertyProxy(PolicyModel.CONTENT);
        BeanProxy concreteContent = (BeanProxy) content.getConcreteProxy();
        if (concreteContent == null) {
            ThemeContentBuilder contentModel = InternalPolicyFactory.getInternalInstance().createThemeContentBuilder();
            content.setModelObject(contentModel);
            concreteContent = (BeanProxy) content.getConcreteProxy();
        }
        return (BeanProxy) concreteContent.getPropertyProxy(ThemeModel.STYLE_SHEET);
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        ruleTable.getTable().setEnabled(b);
        addAction.setEnabled(b);
        deleteAction.setEnabled(b);
        newAction.setEnabled(b);
    }

    /**
     * Refreshes the rule table. If there is no input currently set on it,
     * then an attempt is made to set the input correctly (from the currently
     * selected variant).
     */
    private void refreshRuleTable() {
        if (ruleTable.getInput() == null) {
            BaseProxy contentBase = (BaseProxy) context.getSelectedVariant().getPropertyProxy(PolicyModel.CONTENT);
            Proxy concreteContent = contentBase.getConcreteProxy();
            if (concreteContent != null) {
                BeanProxy themeContent = (BeanProxy) concreteContent;
                BeanProxy styleSheet = (BeanProxy) themeContent.getPropertyProxy(ThemeModel.STYLE_SHEET);
                Proxy rules = styleSheet.getPropertyProxy(ThemeModel.RULES);
                ruleTable.setInput(rules);
                rules.addListener(tableRefresher, true);
                rules.addDiagnosticListener(tableRefresher);
            }
        }
        ruleTable.refresh();
    }
}
