package org.wsmostudio.ui.editors;

import java.util.List;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.omwg.ontology.*;
import org.wsmo.common.Entity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmostudio.runtime.*;
import org.wsmostudio.ui.GUIHelper;
import org.wsmostudio.ui.Utils;
import org.wsmostudio.ui.dnd.*;
import org.wsmostudio.ui.dnd.Clipboard;
import org.wsmostudio.ui.editors.common.*;
import org.wsmostudio.ui.editors.model.RelationInstanceModel;
import org.wsmostudio.ui.views.navigator.WSMOLabelProvider;

/**
 * An editor component, subclass of WSMOEditor, capable to handle with WSMO-API 
 * Relation Instance objects.
 * This editor appears as a part of the WSMO Studio workbench. It is the default
 * editor implementation for this kind of objects and it can be replaced by 
 * extending extension points <code>org.wsmostudio.ui.editors</code> and 
 * <code>org.eclipse.ui.editors</code>.
 *
 * @author not attributable
 * @version $Revision: 1544 $ $Date: 2008-07-15 09:42:06 -0400 (Tue, 15 Jul 2008) $
 */
public class RelationInstanceEditor extends WSMOEditor {

    private NFPPanel nfpPanel;

    private RelationInstanceModel model;

    private TabFolder paramValsFolder;

    private Text relationField;

    private TreeViewer attrValsTable;

    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        nfpPanel = new NFPPanel(parent, model);
        Group supRelPanel = new Group(parent, SWT.NONE);
        supRelPanel.setLayout(new GridLayout(4, false));
        supRelPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        supRelPanel.setText("Relation");
        new Label(supRelPanel, SWT.NONE).setImage(JFaceResources.getImageRegistry().get(WsmoImageRegistry.RELATION_ICON));
        new Label(supRelPanel, SWT.NONE).setText("Identifier: ");
        relationField = new Text(supRelPanel, SWT.BORDER);
        relationField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if (model.getInstance().getRelation() != null) {
            relationField.setText(model.getInstance().getRelation().getIdentifier().toString());
        }
        Button selectRel = new Button(supRelPanel, SWT.PUSH);
        selectRel.setText("Select");
        selectRel.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                doSelectRelation();
            }
        });
        paramValsFolder = new TabFolder(parent, SWT.NONE);
        paramValsFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
        createAttrValsView(paramValsFolder);
        createContextMenu();
        initDNDTargetValues();
    }

    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
        this.model = (RelationInstanceModel) input.getAdapter(RelationInstanceModel.class);
    }

    public void dispose() {
        nfpPanel.dispose();
        super.dispose();
    }

    protected void updateFromModel() {
        nfpPanel.update();
        attrValsTable.refresh(true);
    }

    public boolean isInputEntityDead() {
        return model.getInstance().getOntology() == null || false == model.getInstance().getOntology().listRelationInstances().contains(model.getInstance());
    }

    private void createContextMenu() {
        final MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager mgr) {
                fillContextMenu(menuMgr);
            }
        });
        attrValsTable.getTree().setMenu(menuMgr.createContextMenu(attrValsTable.getTree()));
    }

    private void fillContextMenu(MenuManager menuMgr) {
        final TreeItem[] sel = attrValsTable.getTree().getSelection();
        if (sel == null || sel.length == 0 || false == GUIHelper.containsCursor(sel[0].getBounds(), attrValsTable.getTree())) {
            return;
        }
        if (sel[0].getData() instanceof Parameter) {
            menuMgr.add(new Action("Set Data Value") {

                public void run() {
                    handleSetDataValue();
                }
            });
            menuMgr.add(new Action("Set Instance Value") {

                public void run() {
                    handleSetInstanceValue();
                }
            });
            menuMgr.add(new Separator());
            Action clearAction = new Action("Clear Value") {

                public void run() {
                    handleClearValue();
                }
            };
            menuMgr.add(clearAction);
            clearAction.setEnabled(sel[0].getItemCount() > 0);
        } else {
            if (sel[0].getData() instanceof DataValue) {
                menuMgr.add(new Action("Edit Value") {

                    public void run() {
                        handleEditValue((DataValue) sel[0].getData(), (Parameter) sel[0].getParentItem().getData());
                    }
                });
                menuMgr.add(new Separator());
            } else if (sel[0].getData() instanceof Instance) {
                menuMgr.add(new Action("Edit Value") {

                    public void run() {
                        GUIHelper.openInEditor((Entity) sel[0].getData(), model.getMasterModel());
                    }
                });
                menuMgr.add(new Separator());
            }
            menuMgr.add(new Action("Clear Value") {

                public void run() {
                    handleClearValue();
                }
            });
        }
    }

    private void createAttrValsView(TabFolder parent) {
        TabItem tabItem = new TabItem(parent, SWT.NONE);
        tabItem.setText("Parameter Values");
        attrValsTable = new TreeViewer(parent, SWT.BORDER);
        attrValsTable.setContentProvider(new ParamValueProvider(model.getInstance()));
        attrValsTable.setLabelProvider(new WSMOLabelProvider() {

            public String getText(Object element) {
                if (element instanceof Parameter) {
                    return "parameter";
                }
                if (element instanceof DataValue) {
                    return Utils.normalizeSpaces(((DataValue) element).toString());
                }
                return super.getText(element, model.getInstance().getOntology());
            }
        });
        attrValsTable.setInput(model.getInstance());
        attrValsTable.expandToLevel(2);
        attrValsTable.getTree().addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent e) {
                TreeItem[] sel = attrValsTable.getTree().getSelection();
                if (sel != null && sel.length > 0) {
                    if (sel[0].getData() instanceof Parameter) {
                        if (sel[0].getExpanded()) {
                            sel[0].setExpanded(false);
                        } else {
                            attrValsTable.expandToLevel(sel[0].getData(), TreeViewer.ALL_LEVELS);
                        }
                    } else if (sel[0].getData() instanceof DataValue) {
                        handleEditValue((DataValue) sel[0].getData(), (Parameter) sel[0].getParentItem().getData());
                    } else if (sel[0].getData() instanceof Instance) {
                        GUIHelper.openInEditor((Entity) sel[0].getData(), model.getMasterModel());
                    }
                }
            }
        });
        tabItem.setControl(attrValsTable.getControl());
    }

    private void doSelectRelation() {
        WSMOChooser chooser = WSMOChooser.createRelationChooser(getSite().getShell(), WSMORuntime.getRuntime());
        Relation newSuperRelation = (Relation) chooser.open();
        if (newSuperRelation != null) {
            try {
                Ontology thisOntology = model.getInstance().getOntology();
                if (false == newSuperRelation.getOntology().equals(thisOntology)) {
                    if (false == thisOntology.listOntologies().contains(newSuperRelation.getOntology())) {
                        thisOntology.addOntology(newSuperRelation.getOntology());
                    }
                }
                model.setRelation(newSuperRelation);
            } catch (InvalidModelException ime) {
                LogManager.logError(ime);
            }
            attrValsTable.refresh();
            relationField.setText(newSuperRelation.getIdentifier().toString());
        }
    }

    private void handleEditValue(DataValue value, Parameter param) {
        DataValue newValue = DataValueEditor.createEditor(getSite().getShell(), value, null);
        if (newValue == null || newValue.equals(value)) {
            return;
        }
        doSetAttrValue(param, newValue);
        this.attrValsTable.refresh(param, true);
    }

    private void handleClearValue() {
        TreeItem[] sel = attrValsTable.getTree().getSelection();
        Parameter selectedParam = null;
        if (false == sel[0].getData() instanceof Parameter) {
            selectedParam = (Parameter) sel[0].getParentItem().getData();
        } else {
            selectedParam = (Parameter) sel[0].getData();
        }
        int pos = model.getInstance().getRelation().listParameters().indexOf(selectedParam);
        try {
            model.setParameterValue((byte) pos, null);
        } catch (InvalidModelException ime) {
            LogManager.logError(ime);
        }
    }

    private void handleSetDataValue() {
        IStructuredSelection sel = (IStructuredSelection) attrValsTable.getSelection();
        Parameter selectedParam = (Parameter) sel.getFirstElement();
        DataValue value = DataValueEditor.createEditor(getSite().getShell(), Utils.getTypesAsString(selectedParam.listTypes()));
        if (value == null) {
            return;
        }
        doSetAttrValue(selectedParam, value);
    }

    private void handleSetInstanceValue() {
        IStructuredSelection sel = (IStructuredSelection) attrValsTable.getSelection();
        Parameter selectedParam = (Parameter) sel.getFirstElement();
        WSMOChooser chooser = WSMOChooser.createInstanceChooser(getSite().getShell(), WSMORuntime.getRuntime());
        Instance newConceptType = (Instance) chooser.open();
        if (newConceptType == null) {
            return;
        }
        doSetAttrValue(selectedParam, newConceptType);
    }

    private void doSetAttrValue(Parameter selectedParam, Value value) {
        int pos = model.getInstance().getRelation().listParameters().indexOf(selectedParam);
        try {
            model.setParameterValue((byte) pos, value);
        } catch (InvalidModelException ime) {
            LogManager.logError(ime);
        }
    }

    private void initDNDTargetValues() {
        DropTarget target = new DropTarget(attrValsTable.getTree(), DND.DROP_LINK | DND.DROP_DEFAULT);
        final WSMOTransfer wsmoTransfer = WSMOTransfer.getInstance();
        target.setTransfer(new Transfer[] { wsmoTransfer });
        target.addDropListener(new DropTargetAdapter() {

            public void dragEnter(DropTargetEvent event) {
                event.detail = DND.DROP_NONE;
            }

            public void dragOver(DropTargetEvent event) {
                event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
                if (event.item != null) {
                    Object selected = event.item.getData();
                    if (selected instanceof Parameter && Clipboard.getInstance().ensureContentType(Value.class) && Clipboard.getInstance().getSize() == 1) {
                        event.detail = DND.DROP_LINK;
                    } else {
                        event.detail = DND.DROP_NONE;
                    }
                }
            }

            public void drop(DropTargetEvent event) {
                if (wsmoTransfer.isSupportedType(event.currentDataType)) {
                    if (event.item == null) {
                        return;
                    }
                    Object selected = event.item.getData();
                    if (false == selected instanceof Parameter) {
                        return;
                    }
                    byte pos = (byte) model.getInstance().getRelation().listParameters().indexOf((Parameter) selected);
                    try {
                        model.setParameterValue(pos, (Value) Clipboard.getInstance().getContent().get(0));
                    } catch (InvalidModelException ime) {
                        MessageDialog.openError(getSite().getShell(), "Error", ime.getMessage());
                        LogManager.logError(ime);
                    }
                }
            }
        });
    }
}

class ParamValueProvider implements ITreeContentProvider {

    private RelationInstance relInstance;

    ParamValueProvider(RelationInstance relInst) {
        this.relInstance = relInst;
    }

    public Object[] getChildren(Object parentElement) {
        if (parentElement == relInstance) {
            if (relInstance.getRelation() == null) {
                return null;
            }
            return relInstance.getRelation().listParameters().toArray();
        }
        if (parentElement instanceof Parameter) {
            List<Parameter> vals = relInstance.getRelation().listParameters();
            int paramPos = indexOf(vals, parentElement);
            if (paramPos != -1) {
                try {
                    Object value = relInstance.getParameterValue((byte) paramPos);
                    if (value != null) {
                        return new Object[] { value };
                    }
                } catch (Exception e) {
                    LogManager.logError(e);
                }
            }
        }
        return null;
    }

    public Object getParent(Object element) {
        return null;
    }

    public boolean hasChildren(Object element) {
        Object[] children = getChildren(element);
        return children != null && children.length > 0;
    }

    public Object[] getElements(Object inputElement) {
        if (inputElement == relInstance) {
            return getChildren(inputElement);
        }
        return null;
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    private int indexOf(List<Parameter> params, Object param) {
        for (int i = 0; i < params.size(); i++) {
            if (params.get(i) == param) {
                return i;
            }
        }
        return -1;
    }
}
