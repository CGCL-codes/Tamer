package org.sss.eibs.design;

import java.util.ArrayList;
import java.util.Collections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.sss.eibs.design.jface.AbstractListProvider;
import org.sss.module.IModuleManager;
import org.sss.module.eibs.Datafield;
import org.sss.module.eibs.EibsFactory;
import org.sss.module.eibs.Index;
import org.sss.module.eibs.Module;
import org.sss.module.eibs.ModuleRef;
import org.sss.module.eibs.common.Constants;
import org.sss.util.ContainerUtils;

/**
 * Module编辑页面
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 709 $ $Date: 2012-04-17 05:15:40 -0400 (Tue, 17 Apr 2012) $
 */
public class ShellModuleProperty extends AbstractShell {

    private IModuleManager manager;

    private Module module;

    private ModuleRef moduleRef;

    private EList moduleList;

    private Module ownerModule;

    private ModuleRef ownerModuleRef;

    private Module contextModule;

    private Index index;

    public ShellModuleProperty(Shell parent, IModuleManager manager, TreeItem item) {
        super(parent);
        Object data = item.getData();
        if (data instanceof ModuleRef) moduleRef = (ModuleRef) data;
        module = manager.getModule(data);
        init(manager, item.getParentItem());
    }

    public ShellModuleProperty(Shell parent, IModuleManager manager, TreeItem item, ModuleRef moduleRef) {
        super(parent);
        this.moduleRef = moduleRef;
        module = manager.getModule(moduleRef);
        init(manager, item);
    }

    protected void init(IModuleManager manager, TreeItem parentItem) {
        this.manager = manager;
        if (parentItem != null) {
            Object data = parentItem.getData();
            if (data instanceof ModuleRef) ownerModuleRef = (ModuleRef) data;
            ownerModule = manager.getModule(data);
            moduleList = ownerModule.getModules();
            parentItem = parentItem.getParentItem();
            if (parentItem != null) contextModule = manager.getModule(parentItem.getData());
        }
        createContents();
        setLayout(new FillLayout());
    }

    protected void createContents() {
        setText("Properties of module");
        setSize(350, 311);
        final Composite composite = new Composite(this, SWT.NONE);
        composite.setLayout(new GridLayout());
        final TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        final TabItem tabItemInstance = new TabItem(tabFolder, SWT.NONE);
        tabItemInstance.setText("Instance");
        final Composite compositeInstance = new Composite(tabFolder, SWT.NONE);
        tabItemInstance.setControl(compositeInstance);
        final Label labelModule = new Label(compositeInstance, SWT.NONE);
        labelModule.setText("Module");
        labelModule.setBounds(90, 5, 41, 13);
        final Label labelName = new Label(compositeInstance, SWT.NONE);
        labelName.setText("Name");
        labelName.setBounds(223, 5, 35, 13);
        final Label labelContext = new Label(compositeInstance, SWT.NONE);
        labelContext.setText("Context:");
        labelContext.setBounds(10, 27, 54, 13);
        final Text textContextModule = new Text(compositeInstance, SWT.BORDER);
        textContextModule.setEditable(false);
        textContextModule.setBounds(70, 23, 80, 20);
        final Label labelOwner = new Label(compositeInstance, SWT.NONE);
        labelOwner.setText("Owner:");
        labelOwner.setBounds(10, 52, 45, 13);
        final Text textOwnerModule = new Text(compositeInstance, SWT.BORDER);
        textOwnerModule.setEditable(false);
        textOwnerModule.setBounds(70, 48, 80, 20);
        final Text textOwnerName = new Text(compositeInstance, SWT.BORDER);
        textOwnerName.setEditable(false);
        textOwnerName.setBounds(165, 48, 150, 20);
        final Label labelInstance = new Label(compositeInstance, SWT.NONE);
        labelInstance.setText("Instance:");
        labelInstance.setBounds(10, 77, 59, 13);
        final Text textInstanceModule = new Text(compositeInstance, SWT.BORDER);
        textInstanceModule.setEditable(false);
        textInstanceModule.setBounds(70, 73, 80, 20);
        textInstanceModule.setText(module.getName());
        final Text textInstanceName = new Text(compositeInstance, SWT.BORDER);
        textInstanceName.setBounds(165, 73, 150, 20);
        textInstanceName.setText(module.getName());
        textInstanceName.addVerifyListener(nameVerifier);
        final Group groupUsage = new Group(compositeInstance, SWT.NONE);
        groupUsage.setText("Usage");
        groupUsage.setBounds(70, 98, 114, 66);
        final Button radioUsageInstantiated = new Button(groupUsage, SWT.RADIO);
        radioUsageInstantiated.setText("Instantiated");
        radioUsageInstantiated.setBounds(10, 20, 89, 15);
        final Button radioUsageArgument = new Button(groupUsage, SWT.RADIO);
        radioUsageArgument.setText("Argument");
        radioUsageArgument.setBounds(10, 42, 80, 15);
        final Label labelInitSize = new Label(compositeInstance, SWT.NONE);
        labelInitSize.setText("Init size:");
        labelInitSize.setBounds(190, 123, 59, 13);
        final Spinner spinnerInitSize = new Spinner(compositeInstance, SWT.BORDER);
        spinnerInitSize.setTextLimit(3);
        spinnerInitSize.setMinimum(1);
        spinnerInitSize.setMaximum(999);
        spinnerInitSize.setBounds(255, 120, 58, 19);
        spinnerInitSize.setEnabled(false);
        final Label labelPageSize = new Label(compositeInstance, SWT.NONE);
        labelPageSize.setBounds(190, 148, 59, 13);
        labelPageSize.setText("Page size:");
        final Spinner spinnerPageSize = new Spinner(compositeInstance, SWT.BORDER);
        spinnerPageSize.setBounds(255, 145, 58, 19);
        spinnerPageSize.setTextLimit(3);
        spinnerPageSize.setMinimum(0);
        spinnerPageSize.setMaximum(999);
        spinnerPageSize.setEnabled(false);
        final Button checkList = new Button(compositeInstance, SWT.CHECK);
        checkList.setText("list");
        checkList.setBounds(260, 98, 45, 16);
        checkList.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                boolean flag = checkList.getSelection();
                spinnerInitSize.setEnabled(flag);
                spinnerPageSize.setEnabled(flag);
                if (!flag) {
                    spinnerInitSize.setSelection(0);
                    spinnerPageSize.setSelection(0);
                }
            }
        });
        final Button checkStatic = new Button(compositeInstance, SWT.CHECK);
        checkStatic.setText("static");
        checkStatic.setBounds(190, 98, 58, 16);
        final Label labelArgument = new Label(compositeInstance, SWT.NONE);
        labelArgument.setText("Argument:");
        labelArgument.setBounds(10, 174, 58, 13);
        final Combo comboArgument = new Combo(compositeInstance, SWT.DROP_DOWN | SWT.READ_ONLY);
        comboArgument.setBounds(70, 170, 245, 20);
        SelectionAdapter adapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean flag = radioUsageArgument.getSelection();
                labelArgument.setVisible(flag);
                comboArgument.setVisible(flag);
                if (flag) {
                    java.util.List<String> list = new ArrayList<String>();
                    manager.findModules(contextModule, ownerModule.getName(), module.getName(), list, "", checkList.getSelection());
                    comboArgument.setItems(list.toArray(Constants.EMPTY_STRINGS));
                } else comboArgument.setItems(Constants.EMPTY_STRINGS);
            }
        };
        radioUsageArgument.addSelectionListener(adapter);
        radioUsageInstantiated.addSelectionListener(adapter);
        checkList.addSelectionListener(adapter);
        final TabItem tabItemModule = new TabItem(tabFolder, SWT.NONE);
        tabItemModule.setText("Module");
        final Composite compositeModule = new Composite(tabFolder, SWT.NONE);
        tabItemModule.setControl(compositeModule);
        final Label labelTitle = new Label(compositeModule, SWT.NONE);
        labelTitle.setText("Title:");
        labelTitle.setBounds(10, 20, 36, 13);
        final Label lableTable = new Label(compositeModule, SWT.NONE);
        lableTable.setText("Table:");
        lableTable.setBounds(10, 50, 36, 13);
        final Text textTitle = new Text(compositeModule, SWT.BORDER);
        textTitle.setBounds(49, 16, 267, 20);
        final Text textTable = new Text(compositeModule, SWT.BORDER);
        textTable.setBounds(49, 46, 80, 20);
        textTable.addVerifyListener(tableNameVerifier);
        final Button checkTransient = new Button(compositeModule, SWT.CHECK);
        checkTransient.setBounds(153, 48, 97, 16);
        checkTransient.setText("memory only");
        final TabItem tabItemPanels = new TabItem(tabFolder, SWT.NONE);
        tabItemPanels.setText("Panels");
        final Composite compositePanels = new Composite(tabFolder, SWT.NONE);
        tabItemPanels.setControl(compositePanels);
        final List listPanels = new List(compositePanels, SWT.MULTI | SWT.BORDER);
        listPanels.setBounds(10, 10, 304, 150);
        final Label labelPanels = new Label(compositePanels, SWT.NONE);
        labelPanels.setText("Panel:");
        labelPanels.setBounds(10, 173, 64, 12);
        final Combo comboPanel = new Combo(compositePanels, SWT.READ_ONLY);
        comboPanel.setBounds(10, 196, 304, 20);
        java.util.List<String> list = new ArrayList();
        manager.findPanels(module, list, "\\");
        comboPanel.setItems(list.toArray(Constants.EMPTY_STRINGS));
        comboPanel.select(0);
        final Button buttonAddPanel = new Button(compositePanels, SWT.NONE);
        buttonAddPanel.setText("∧");
        buttonAddPanel.setBounds(115, 168, 30, 22);
        buttonAddPanel.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                String panel = comboPanel.getText();
                if (!ContainerUtils.isEmpty(panel)) {
                    boolean found = false;
                    for (String oldPanel : listPanels.getItems()) if (panel.equals(oldPanel)) found = true;
                    if (!found) {
                        listPanels.add(panel);
                        updateChanged();
                    }
                }
            }
        });
        final Button buttonRemovePanel = new Button(compositePanels, SWT.NONE);
        buttonRemovePanel.setText("∨");
        buttonRemovePanel.setBounds(200, 168, 30, 22);
        buttonRemovePanel.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                if (listPanels.getSelectionCount() > 0) listPanels.remove(listPanels.getSelectionIndex());
                updateChanged();
            }
        });
        final TabItem tabItemComments = new TabItem(tabFolder, SWT.NONE);
        tabItemComments.setText("Comments");
        final Composite compositeComments = new Composite(tabFolder, SWT.NONE);
        tabItemComments.setControl(compositeComments);
        final Label labelModuleComment = new Label(compositeComments, SWT.NONE);
        labelModuleComment.setText("Module Comments:");
        labelModuleComment.setBounds(10, 5, 122, 12);
        final Text textModuleComment = new Text(compositeComments, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        textModuleComment.setBounds(10, 22, 304, 81);
        final Label labelInstanceComment = new Label(compositeComments, SWT.NONE);
        labelInstanceComment.setText("Instance Comments:");
        labelInstanceComment.setBounds(10, 108, 122, 12);
        final Text textInstanceComment = new Text(compositeComments, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        textInstanceComment.setBounds(10, 125, 304, 88);
        final Label labelIndex = new Label(compositeInstance, SWT.NONE);
        labelIndex.setBounds(215, 199, 41, 13);
        labelIndex.setText("Index:");
        final Spinner spinnerIndex = new Spinner(compositeInstance, SWT.READ_ONLY | SWT.BORDER);
        spinnerIndex.setBounds(263, 195, 50, 20);
        final TabItem tabItemDbIndexs = new TabItem(tabFolder, SWT.NONE);
        tabItemDbIndexs.setText("DB Indexes");
        final Composite compositeDbIndexs = new Composite(tabFolder, SWT.NONE);
        tabItemDbIndexs.setControl(compositeDbIndexs);
        final Label indexFieldsLabel = new Label(compositeDbIndexs, SWT.NONE);
        indexFieldsLabel.setText("Index Fields:");
        indexFieldsLabel.setBounds(222, 10, 85, 13);
        final Label labelAvailableFields = new Label(compositeDbIndexs, SWT.NONE);
        labelAvailableFields.setText("Available Fields:");
        labelAvailableFields.setBounds(10, 10, 129, 13);
        final List listFields = new List(compositeDbIndexs, SWT.BORDER);
        listFields.setBounds(10, 29, 97, 192);
        list.clear();
        for (Datafield datafield : (java.util.List<Datafield>) module.getDatafields()) {
            if (ContainerUtils.isEmpty(datafield.getColumn())) datafield.setColumn(datafield.getName());
            list.add(datafield.getColumn());
        }
        listFields.setItems(list.toArray(Constants.EMPTY_STRINGS));
        final ListViewer listViewerIndexFields = new ListViewer(compositeDbIndexs, SWT.BORDER);
        AbstractListProvider provider = new AbstractListProvider() {

            @Override
            public Object[] getElements(Object data) {
                if (index != null) return ((java.util.List<String>) index.getFields()).toArray(Constants.EMPTY_STRINGS);
                return Constants.EMPTY_STRINGS;
            }
        };
        listViewerIndexFields.setLabelProvider(provider);
        listViewerIndexFields.setContentProvider(provider);
        final List listIndexFields = listViewerIndexFields.getList();
        listIndexFields.setBounds(222, 29, 97, 192);
        listIndexFields.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDoubleClick(MouseEvent event) {
                if (index != null && listIndexFields.getSelectionCount() > 0) {
                    index.getFields().remove(listIndexFields.getSelection()[0]);
                    listViewerIndexFields.setInput(null);
                    updateParentChanged();
                }
            }
        });
        listFields.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDoubleClick(MouseEvent event) {
                if (index != null && listFields.getSelectionCount() > 0 && !index.getFields().contains(listFields.getSelection()[0])) {
                    index.getFields().add(listFields.getSelection()[0]);
                    listViewerIndexFields.setInput(Collections.EMPTY_LIST);
                    updateParentChanged();
                }
            }
        });
        final Button buttonDelete = new Button(compositeDbIndexs, SWT.NONE);
        buttonDelete.setText("Delete");
        buttonDelete.setBounds(165, 94, 48, 22);
        final Button checkUnique = new Button(compositeDbIndexs, SWT.CHECK);
        checkUnique.setText("unique");
        checkUnique.setBounds(114, 127, 66, 16);
        checkUnique.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                if (index != null) {
                    index.setUnique(checkUnique.getSelection());
                    updateParentChanged();
                }
            }
        });
        java.util.List<String> names = new ArrayList<String>();
        for (Index index : (java.util.List<Index>) module.getIndexes()) names.add(index.getName());
        final Combo comboIndex = new Combo(compositeDbIndexs, SWT.READ_ONLY);
        comboIndex.setBounds(114, 68, 99, 20);
        comboIndex.setItems(names.toArray(Constants.EMPTY_STRINGS));
        comboIndex.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                String indexName = comboIndex.getText();
                index = null;
                for (Index index : (java.util.List<Index>) module.getIndexes()) {
                    if (index.getName().equals(indexName)) {
                        ShellModuleProperty.this.index = index;
                        listViewerIndexFields.setInput(Collections.EMPTY_LIST);
                        checkUnique.setSelection(index.isUnique());
                        break;
                    }
                }
            }
        });
        if (!names.isEmpty()) comboIndex.select(0);
        comboIndex.notifyListeners(SWT.Selection, null);
        buttonDelete.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                if (index != null && confirm("Delete this index?")) {
                    module.getIndexes().remove(index);
                    comboIndex.remove(comboIndex.getSelectionIndex());
                    updateParentChanged();
                }
            }
        });
        final Button buttonNew = new Button(compositeDbIndexs, SWT.NONE);
        buttonNew.setText("New");
        buttonNew.setBounds(114, 94, 48, 22);
        buttonNew.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                String indexName = input("New index", "Index name:", "", indexNameValidator);
                if (!ContainerUtils.isEmpty(indexName)) {
                    for (Index _index : (java.util.List<Index>) module.getIndexes()) {
                        if (_index.getName().equals(indexName)) {
                            error("This name of index is exist.");
                            return;
                        }
                    }
                    index = EibsFactory.eINSTANCE.createIndex();
                    index.setName(indexName);
                    module.getIndexes().add(index);
                    listViewerIndexFields.setInput(Collections.EMPTY_LIST);
                    comboIndex.add(indexName);
                    checkUnique.setSelection(false);
                    updateParentChanged();
                }
            }
        });
        final Composite compositeButton = new Composite(composite, SWT.NONE);
        compositeButton.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false));
        final Button buttonApply = new Button(compositeButton, SWT.NONE);
        buttonApply.setBounds(156, 0, 75, 21);
        buttonApply.setText("Apply");
        buttonApply.setEnabled(false);
        buttonApply.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (moduleRef != null) {
                    if (!manager.changeName(ownerModule, moduleRef, textInstanceName.getText())) {
                        error("Name of module is used.");
                        return;
                    }
                    moduleRef.setArgument(radioUsageArgument.getSelection());
                    if (moduleRef.isArgument()) manager.setValue(ownerModuleRef, moduleRef.getName(), comboArgument.getText());
                    moduleRef.setList(checkList.getSelection());
                    if (moduleRef.isList()) {
                        moduleRef.setInitSize(spinnerInitSize.getSelection());
                        moduleRef.setPageSize(spinnerPageSize.getSelection());
                    }
                    moduleRef.setStatic(checkStatic.getSelection());
                    moduleRef.setComments(textInstanceComment.getText());
                }
                module.setTable(textTable.getText());
                module.setTitle(textTitle.getText());
                module.setTransient(checkTransient.getSelection());
                module.setComments(textModuleComment.getText());
                if (moduleList != null) moduleList.move(spinnerIndex.getSelection(), moduleRef);
                if (!tabItemPanels.isDisposed()) {
                    module.getMainPanels().clear();
                    for (String item : listPanels.getItems()) module.getMainPanels().add(item);
                }
                resetChanged();
            }
        });
        final Button buttonClose = new Button(compositeButton, SWT.NONE);
        buttonClose.setText("Close");
        buttonClose.setBounds(244, 0, 75, 21);
        buttonClose.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                close();
                if (modified) ((ShellMain) parent).refreshModuleTree();
            }
        });
        if (moduleRef != null) {
            textInstanceName.setText(moduleRef.getName());
            setText(textInstanceComment, moduleRef.getComments());
            if (IModuleManager.MODULE_OBJECT.equalsIgnoreCase(moduleRef.getType())) {
                moduleRef.setArgument(true);
                radioUsageArgument.setEnabled(false);
                radioUsageInstantiated.setEnabled(false);
                moduleRef.setList(false);
                checkList.setEnabled(false);
            }
            radioUsageArgument.setSelection(moduleRef.isArgument());
            radioUsageInstantiated.setSelection(!moduleRef.isArgument());
            checkList.setSelection(moduleRef.isList());
            if (moduleRef.isList()) {
                spinnerInitSize.setSelection(moduleRef.getInitSize());
                spinnerPageSize.setSelection(moduleRef.getPageSize());
            }
            checkStatic.setSelection(moduleRef.isStatic());
            tabItemPanels.dispose();
        } else {
            textInstanceName.setEditable(false);
            textInstanceComment.setEditable(false);
            checkList.setEnabled(false);
            checkStatic.setEnabled(false);
            checkTransient.setEnabled(false);
            for (String panel : (java.util.List<String>) module.getMainPanels()) listPanels.add(panel);
        }
        setText(textTable, module.getTable());
        setText(textTitle, module.getTitle());
        setText(textModuleComment, module.getComments());
        checkTransient.setSelection(module.isTransient());
        checkList.notifyListeners(SWT.Selection, null);
        radioUsageArgument.notifyListeners(SWT.Selection, null);
        if (ownerModuleRef != null) {
            textOwnerModule.setText(ownerModuleRef.getType());
            textOwnerName.setText(ownerModuleRef.getName());
        } else if (ownerModule != null) {
            textOwnerModule.setText(ownerModule.getName());
            textOwnerName.setText(ownerModule.getName());
        }
        if (moduleList == null) spinnerIndex.setEnabled(false); else {
            spinnerIndex.setSelection(moduleList.indexOf(moduleRef));
            spinnerIndex.setMaximum(moduleList.size() - 1);
        }
        if (contextModule != null) {
            textContextModule.setText(contextModule.getName());
            if (radioUsageArgument.getSelection()) comboArgument.setText(manager.getValue(ownerModuleRef, moduleRef.getName()));
        } else {
            radioUsageInstantiated.setSelection(true);
            groupUsage.setEnabled(false);
            radioUsageArgument.setEnabled(false);
            radioUsageInstantiated.setEnabled(false);
        }
        super.addUpdateListener(new Widget[] { buttonApply });
    }
}
