package org.wsmostudio.ui.editors.common;

import java.util.*;
import org.eclipse.jface.action.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.wsmo.common.*;
import org.wsmo.factory.WsmoFactory;
import org.wsmostudio.runtime.WSMORuntime;
import org.wsmostudio.ui.GUIHelper;
import org.wsmostudio.ui.editors.model.TopEntityModel;

/**
 * A common purpose GUI component, designed to maintain a set of Namespace (part of 
 * WSMO-API) definitions within a certain WSMO object.
 * It is a sub-component of all WSMO editors whose target input objects can
 * contain namespace definitions (i.e. WSMO-API TopEntity objects). 
 *
 * @author not attributable
 * @version $Revision: 1227 $ $Date: 2007-07-19 09:08:32 -0400 (Thu, 19 Jul 2007) $
 */
public class NamespacesPanel {

    private Text defNSField;

    private Composite mainPanel;

    private Table nsTable;

    private TableEditor editor;

    private TopEntityModel model;

    private boolean dirtyContent = false;

    public NamespacesPanel(Composite parent, TopEntityModel model) {
        this(parent, true, model);
    }

    public NamespacesPanel(Composite parent, boolean needsBorder, TopEntityModel uiModel) {
        this.model = uiModel;
        mainPanel = (needsBorder) ? new Group(parent, SWT.NONE) : new Composite(parent, SWT.FLAT);
        mainPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if (needsBorder) {
            ((Group) mainPanel).setText("Namespaces");
        }
        GridLayout nsLayout = new GridLayout(1, false);
        nsLayout.verticalSpacing = 0;
        nsLayout.marginHeight = 0;
        mainPanel.setLayout(nsLayout);
        Composite defNSPanel = new Composite(mainPanel, SWT.NONE);
        defNSPanel.setLayout(new GridLayout(2, false));
        defNSPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(defNSPanel, SWT.NONE).setText("Default : ");
        defNSField = new Text(defNSPanel, SWT.SINGLE | SWT.BORDER);
        if (model.getTopEntity().getDefaultNamespace() != null && model.getTopEntity().getDefaultNamespace().getIRI() != null) {
            defNSField.setText(model.getTopEntity().getDefaultNamespace().getIRI().toString());
        }
        defNSField.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                dirtyContent = true;
                model.setChanged();
            }
        });
        defNSField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createTable(mainPanel);
        createContextMenu();
    }

    private void createTable(Composite parent) {
        nsTable = new Table(parent, SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.SINGLE | SWT.BORDER);
        nsTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        editor = new TableEditor(nsTable);
        nsTable.setLinesVisible(true);
        nsTable.setHeaderVisible(true);
        TableColumn column1 = new TableColumn(nsTable, SWT.NONE);
        column1.setWidth(100);
        column1.setText("Prefixes");
        column1.setAlignment(SWT.CENTER);
        TableColumn column2 = new TableColumn(nsTable, SWT.NONE);
        column2.setText("URIs");
        column2.setWidth(400);
        for (Namespace nsEntry : model.getTopEntity().listNamespaces()) {
            TableItem nsItem = new TableItem(nsTable, SWT.NONE);
            nsItem.setText(0, nsEntry.getPrefix());
            nsItem.setText(1, nsEntry.getIRI().toString());
            nsItem.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_BLUE));
        }
        nsTable.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent e) {
                Control oldEditor = editor.getEditor();
                if (oldEditor != null) {
                    oldEditor.dispose();
                }
                TableItem sel = nsTable.getSelection()[0];
                int index = -1;
                if (sel.getBounds(0).contains(e.x, e.y)) {
                    index = 0;
                } else if (sel.getBounds(1).contains(e.x, e.y)) {
                    index = 1;
                } else {
                    return;
                }
                doEditCell(sel, index);
            }
        });
        editor.horizontalAlignment = SWT.TRAIL;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        nsTable.addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent e) {
                Control oldEditor = editor.getEditor();
                if (oldEditor == null) {
                    return;
                }
                TableItem[] sel = nsTable.getSelection();
                if (sel.length == 0) {
                    return;
                }
                if (editor.getItem() != sel[0]) {
                    oldEditor.dispose();
                }
            }
        });
    }

    private void doEditCell(TableItem cell, final int column) {
        if (cell == null) {
            return;
        }
        final Text newEditor = new Text(nsTable, SWT.SINGLE);
        newEditor.setText(cell.getText(column));
        newEditor.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                Text text = (Text) editor.getEditor();
                editor.getItem().setText(column, text.getText());
                dirtyContent = true;
                model.setChanged();
            }
        });
        newEditor.selectAll();
        newEditor.setFocus();
        editor.setEditor(newEditor, cell, column);
        newEditor.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                if ((e.widget != null) && (!e.widget.isDisposed())) {
                    e.widget.dispose();
                }
            }
        });
        newEditor.addListener(SWT.Traverse, new Listener() {

            public void handleEvent(Event event) {
                if ((event.character == SWT.ESC) || (event.character == SWT.CR)) {
                    newEditor.dispose();
                    event.doit = false;
                }
            }
        });
    }

    private void createContextMenu() {
        final MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager mgr) {
                fillContextMenu(menuMgr);
            }
        });
        nsTable.setMenu(menuMgr.createContextMenu(nsTable));
    }

    private void fillContextMenu(MenuManager menuMgr) {
        if (false == GUIHelper.containsCursor(nsTable)) {
            return;
        }
        menuMgr.add(new Action("Add") {

            public void run() {
                handleAddAction();
            }
        });
        final TableItem[] sel = nsTable.getSelection();
        final Point mousePosRelative = nsTable.toControl(Display.getCurrent().getCursorLocation());
        if (sel != null && sel.length > 0 && (GUIHelper.containsCursor(sel[0].getBounds(0), nsTable) || GUIHelper.containsCursor(sel[0].getBounds(1), nsTable))) {
            menuMgr.add(new Separator());
            menuMgr.add(new Action("Edit") {

                public void run() {
                    handleEditAction((sel[0].getBounds(1).x > mousePosRelative.x) ? 0 : 1);
                }
            });
            menuMgr.add(new Separator());
            menuMgr.add(new Action("Remove") {

                public void run() {
                    handleRemoveAction();
                }
            });
        }
    }

    private void handleEditAction(int column) {
        TableItem[] sel = nsTable.getSelection();
        doEditCell(sel[0], column);
    }

    private void handleRemoveAction() {
        TableItem[] sel = nsTable.getSelection();
        sel[0].dispose();
        dirtyContent = true;
        model.setChanged();
    }

    private void handleAddAction() {
        TableItem nsItem = new TableItem(nsTable, SWT.NONE);
        nsItem.setText(0, "new");
        nsItem.setText(1, "");
        nsItem.setForeground(nsTable.getDisplay().getSystemColor(SWT.COLOR_BLUE));
        nsTable.setSelection(new TableItem[] { nsItem });
        nsTable.showSelection();
        dirtyContent = true;
        doEditCell(nsItem, 0);
        model.setChanged();
    }

    public void dispose() {
        mainPanel.dispose();
        defNSField.dispose();
    }

    public Control getControl() {
        return mainPanel;
    }

    public void doUpdate() throws Exception {
        if (dirtyContent == false) {
            return;
        }
        WsmoFactory factory = WSMORuntime.getRuntime().getWsmoFactory();
        if (defNSField.getText().trim().length() == 0) {
            model.setDefaultNamespace(null);
        } else if (model.getTopEntity().getDefaultNamespace() == null || false == model.getTopEntity().getDefaultNamespace().getIRI().toString().equals(defNSField.getText().trim())) {
            try {
                IRI defNSRef = factory.createIRI(defNSField.getText().trim());
                model.setDefaultNamespace(defNSRef);
            } catch (Exception ex) {
                throw new Exception("Unable to parse default namespace: \n" + ex.getMessage(), ex);
            }
        }
        Set<Namespace> newNSs = new HashSet<Namespace>();
        TableItem[] items = nsTable.getItems();
        for (int i = 0; i < items.length; i++) {
            String prefix = items[i].getText(0).trim();
            if (prefix.length() == 0 || items[i].getText(1).trim().length() == 0) {
                continue;
            }
            if (WSMORuntime.getRuntime().getWsmlParser().listKeywords().contains(prefix)) {
                throw new Exception("Invalid namespace prefix '" + prefix + "' - a reserved WSML keyword.");
            }
            try {
                IRI targetIRI = factory.createIRI(items[i].getText(1).trim());
                newNSs.add(factory.createNamespace(prefix, targetIRI));
            } catch (Exception ex) {
                throw new Exception("Unable to parse namespace at row " + i + ": \n" + ex.getMessage(), ex);
            }
        }
        Set<Namespace> oldNSs = new HashSet<Namespace>(model.getTopEntity().listNamespaces());
        for (Namespace tempNS : oldNSs) {
            if (false == newNSs.contains(tempNS)) {
                model.removeNamespace(tempNS);
            }
        }
        for (Namespace tempNS : newNSs) {
            if (false == oldNSs.contains(tempNS)) {
                model.addNamespace(tempNS);
            }
        }
        dirtyContent = false;
    }

    public void reloadNSInfo() {
        if (dirtyContent == true) {
            return;
        }
        String defNSUpdateText = "";
        if (model.getTopEntity().getDefaultNamespace() != null && model.getTopEntity().getDefaultNamespace().getIRI() != null) {
            defNSUpdateText = model.getTopEntity().getDefaultNamespace().getIRI().toString();
        }
        if (false == defNSUpdateText.equals(defNSField.getText())) {
            defNSField.setText(defNSUpdateText);
        }
        Set<Namespace> toDoSourceNSs = new HashSet<Namespace>(model.getTopEntity().listNamespaces());
        TableItem[] items = nsTable.getItems();
        for (int i = 0; i < items.length; i++) {
            String nsLocal = items[i].getText(0);
            Namespace sourceNS = model.getTopEntity().findNamespace(nsLocal);
            if (sourceNS == null || (sourceNS.getIRI() == null && items[i].getText(1).trim().length() > 0) || (sourceNS.getIRI() != null && false == sourceNS.getIRI().toString().equals(items[i].getText(1)))) {
                items[i].dispose();
            } else {
                toDoSourceNSs.remove(sourceNS);
            }
        }
        for (Namespace newNS : toDoSourceNSs) {
            TableItem nsItem = new TableItem(nsTable, SWT.NONE);
            nsItem.setText(0, newNS.getPrefix());
            nsItem.setText(1, newNS.getIRI().toString());
            nsItem.setForeground(nsTable.getDisplay().getSystemColor(SWT.COLOR_BLUE));
        }
    }
}
