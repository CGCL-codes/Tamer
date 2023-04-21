package de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import de.fraunhofer.isst.axbench.ResourceManager;
import de.fraunhofer.isst.axbench.Session;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.elements.Model;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.AbstractConnection;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.AbstractArchitectureModel;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.DataElement;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Function;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Port;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.SubComponent;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.XORComponent;
import de.fraunhofer.isst.axbench.axlang.elements.featuremodel.Feature;
import de.fraunhofer.isst.axbench.axlang.elements.mappings.A2RMapping;
import de.fraunhofer.isst.axbench.axlang.elements.mappings.AbstractF2ArchitectureMapping;
import de.fraunhofer.isst.axbench.axlang.elements.transactionmodel.Activation;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLangDefinition.AXLKeyword;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLangElementUtilities;
import de.fraunhofer.isst.axbench.axlang.utilities.ReferenceKind;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;
import de.fraunhofer.isst.axbench.axlang.utilities.ValidType;
import de.fraunhofer.isst.axbench.editors.axlmultipage.AXLMultiPageEditor;
import de.fraunhofer.isst.axbench.editors.axlmultipage.axleditor.actions.ViewAction;
import de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor.actions.DeleteAction;
import de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor.actions.DummyAction;
import de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor.actions.ExpandAction;
import de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor.actions.NewChildAction;
import de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor.actions.NewReferenceAction;
import de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor.actions.RenameAction;
import de.fraunhofer.isst.axbench.utilities.AXBenchProjectsPreferences;
import de.fraunhofer.isst.axbench.utilities.AXBenchProjectsPreferences.SimpleProjectPreferences;
import de.fraunhofer.isst.axbench.utilities.Constants;
import de.fraunhofer.isst.axbench.utilities.FeatureInSelection;
import de.fraunhofer.isst.axbench.utilities.IAXLangElementNode;
import de.fraunhofer.isst.axbench.utilities.StructureNode;
import de.fraunhofer.isst.axbench.views.AbstractAXLView;
import de.fraunhofer.isst.axbench.views.IAXLView;

/**
 * @brief the editable view tree one instance for every multipageeditor
 * @author skaegebein
 * @author ekleinod
 * @version 0.9.0
 * @since 0.8.0
 */
public class AXLTreeEditor extends AbstractAXLView {

    private boolean activatedummy = false;

    protected IAXLangElement actualselectedelement = null;

    protected boolean errorinrename = false;

    private LinkedHashMap<Role, NewChildAction> new_child = null;

    private LinkedHashMap<Role, NewReferenceAction> new_reference = null;

    protected DeleteAction delete_child = null;

    private ExpandAction expand_action = null;

    private DummyAction dummyaction = null;

    private RenameAction rename = null;

    private MenuManager lowLevel = null;

    private MenuManager secondlevel_new_child = null;

    private MenuManager secondlevel_new_reference = null;

    private MenuManager secondlevel_showin = null;

    /**
     * @param multipageeditor
     */
    public AXLTreeEditor(AXLMultiPageEditor multipageeditor) {
        super(multipageeditor);
    }

    /**
     * @brief initialize all actions for the menu.
     */
    private void initializeActions() {
        new_child = new LinkedHashMap<Role, NewChildAction>();
        for (int i = 0; i < Role.values().length; i++) {
            Role role = Role.values()[i];
            new_child.put(role, new NewChildAction(role.name(), role));
            new_child.get(role).init(this);
        }
        new_reference = new LinkedHashMap<Role, NewReferenceAction>();
        for (int i = 0; i < Role.values().length; i++) {
            Role role = Role.values()[i];
            new_reference.put(role, new NewReferenceAction(role.name(), role));
            new_reference.get(role).init(this);
        }
        delete_child = new DeleteAction("Delete");
        delete_child.init(this);
        rename = new RenameAction("Rename...");
        rename.init(this);
        expand_action = new ExpandAction("Expand");
        expand_action.init(this);
        dummyaction = new DummyAction("Dummy Action");
        dummyaction.init(this);
    }

    /**
     * @brief initialize the menu managers.
     */
    private void initializeMenuManagers() {
        lowLevel = new MenuManager("LowLevel");
        lowLevel.setRemoveAllWhenShown(true);
        lowLevel.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                handleLowLevelMenuToShow(manager);
            }
        });
        String sImageID = "actions" + ResourceManager.PATHSEPARATOR + "editableView" + ResourceManager.PATHSEPARATOR;
        String id = "new_child";
        sImageID = sImageID + id;
        Image im = ResourceManager.get(sImageID);
        secondlevel_new_child = new MenuManager("New Child", ImageDescriptor.createFromImage(im), "theNewChildID");
        secondlevel_new_child.setRemoveAllWhenShown(true);
        secondlevel_new_child.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                handleSecondLevelNewChildMenuToShow(manager);
            }
        });
        sImageID = "actions" + ResourceManager.PATHSEPARATOR + "editableView" + ResourceManager.PATHSEPARATOR;
        id = "new_reference";
        sImageID = sImageID + id;
        im = ResourceManager.get(sImageID);
        secondlevel_new_reference = new MenuManager("New Reference", ImageDescriptor.createFromImage(im), "theNewReferenceID");
        secondlevel_new_reference.setRemoveAllWhenShown(true);
        secondlevel_new_reference.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                handleSecondLevelNewReferenceMenuToShow(manager);
            }
        });
        secondlevel_showin = new MenuManager("Show In");
        secondlevel_showin.setRemoveAllWhenShown(true);
        secondlevel_showin.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                handleSecondLevelShowInMenuToShow(manager);
            }
        });
    }

    /**
     * @brief handles the view of the low level menu.
     * @param manager
     */
    protected void handleLowLevelMenuToShow(IMenuManager manager) {
        manager.add(secondlevel_new_child);
        manager.add(secondlevel_new_reference);
        manager.add(new Separator());
        manager.add(delete_child);
        manager.add(new Separator());
        manager.add(rename);
        manager.add(new Separator());
        manager.add(expand_action);
        manager.add(new Separator());
        manager.add(secondlevel_showin);
        if (activatedummy) {
            manager.add(dummyaction);
        }
        checkEnableStatusOfActions();
    }

    /**
     * @brief handles the view of the second level menu (new child).
     * @param manager
     */
    protected void handleSecondLevelNewChildMenuToShow(IMenuManager manager) {
        Object selectedElement = ((IStructuredSelection) getTreeViewer().getSelection()).getFirstElement();
        if (selectedElement instanceof IAXLangElementNode) {
            IAXLangElement element = ((IAXLangElementNode) selectedElement).getIAXLangElement();
            if (element.isClockElement() || EditableViewConstants.getInstance().getNoneditableelements().contains(element.getClassName())) {
                if (element.getClassName().equals("XORComponent")) {
                    manager.add(new_child.get(Role.SUBCOMPONENT));
                }
                return;
            }
            if (element.getClass().equals(SubComponent.class)) {
                if (element.isXOR()) {
                    return;
                }
            }
            for (int i = 0; i < ReferenceKind.values().length; i++) {
                if (ReferenceKind.values()[i].name().equals("CHILD")) {
                    Object[] types = element.getValidElementTypes(ReferenceKind.values()[i]).toArray();
                    for (int r = 0; r < types.length; r++) {
                        ValidType type = (ValidType) types[r];
                        if (isThereIsAConstraint(element, type)) {
                            continue;
                        }
                        manager.add(new_child.get(type.getRole()));
                    }
                }
            }
        }
        if (selectedElement instanceof StructureNode) {
            StructureNode node = (StructureNode) selectedElement;
            Role role = node.getKind().getPossibleRole();
            if (role != null) {
                if (role.equals(Role.DATAELEMENT)) {
                    manager.add(new_child.get(Role.SIGNAL));
                    manager.add(new_child.get(Role.OPERATION));
                } else {
                    manager.add(new_child.get(role));
                }
            }
        }
    }

    /**
     * @brief checks if there is a constraint defined. e.g. the user can't add signals to a local component (only to the
     *        application model)
     * @param element
     * @param type
     * @return
     */
    private boolean isThereIsAConstraint(IAXLangElement element, ValidType type) {
        if (type.getRole().equals(Role.SIGNAL) || type.getRole().equals(Role.OPERATION)) {
            if (element instanceof AbstractArchitectureModel) {
                return true;
            }
            return false;
        } else if (type.getRole().equals(Role.BEHAVIOR)) {
            if (element.getChild(Role.BEHAVIOR) != null) {
                return true;
            }
        } else if (type.getRole().equals(Role.EXECUTE)) {
            if (element.getChild(Role.EXECUTE) != null) {
                return true;
            }
        } else if (element.getClass().equals(Function.class) && type.getRole().equals(Role.FUNCTION)) {
            if (!element.isXOR()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @brief handles the view of the second level menu (new reference).
     * @param manager
     */
    protected void handleSecondLevelNewReferenceMenuToShow(IMenuManager manager) {
        Object selectedElement = ((IStructuredSelection) getTreeViewer().getSelection()).getFirstElement();
        if (selectedElement instanceof IAXLangElementNode) {
            IAXLangElement element = ((IAXLangElementNode) selectedElement).getIAXLangElement();
            if (element.isClockElement() || EditableViewConstants.getInstance().getNoneditableelements().contains(element.getClassName())) {
                return;
            }
            if (element.getClass().getSuperclass().equals(AbstractConnection.class)) {
                return;
            }
            if (element.getClass().equals(SubComponent.class)) {
                if (element.isXOR()) {
                    return;
                }
            }
            if (EditableViewConstants.getInstance().getSpecialreferenceactionsmenu().containsKey(element.getClass())) {
                manager.add(new_reference.get(EditableViewConstants.getInstance().getSpecialreferenceactionsmenu().get(element.getClass())));
                return;
            }
            for (int i = 0; i < ReferenceKind.values().length; i++) {
                if (ReferenceKind.values()[i].name().equals("REFERENCE")) {
                    Object[] types = element.getValidElementTypes(ReferenceKind.values()[i]).toArray();
                    for (int r = 0; r < types.length; r++) {
                        ValidType type = (ValidType) types[r];
                        manager.add(new_reference.get(type.getRole()));
                    }
                }
            }
        }
        if (selectedElement instanceof StructureNode) {
            StructureNode node = (StructureNode) selectedElement;
            Role role = node.getKind().getPossibleRole();
            if (role != null) {
                System.err.println("TODO?-Reference:" + role.name());
            }
        }
    }

    protected void handleSecondLevelShowInMenuToShow(IMenuManager manager) {
        ArrayList<String> viewids = this.getMymultipageeditor().getAxlEditor().getViewids();
        ArrayList<ViewAction> viewactions = this.getMymultipageeditor().getAxlEditor().getViewactions();
        for (int i = 0; i < viewids.size(); i++) {
            boolean isVisible = false;
            for (int r = 0; r < PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences().length; r++) {
                IViewReference ref = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences()[r];
                if (viewids.get(i) == ref.getId()) {
                    isVisible = true;
                    break;
                }
            }
            if (isVisible) {
                viewactions.get(i).setEnabled(false);
            }
        }
        for (int i = 0; i < viewactions.size(); i++) {
            manager.add(viewactions.get(i));
        }
    }

    /**
     * @brief checks if the action has to be enabled or not.
     */
    protected void checkEnableStatusOfActions() {
        delete_child.setEnabled(true);
        rename.setEnabled(true);
        Object selectedElement = ((IStructuredSelection) getTreeViewer().getSelection()).getFirstElement();
        if (!isElementEditable(selectedElement)) {
            rename.setEnabled(false);
        }
        if (selectedElement instanceof IAXLangElementNode) {
            secondlevel_new_reference.setVisible(false);
            secondlevel_new_child.setVisible(false);
            IAXLangElement element = ((IAXLangElementNode) selectedElement).getIAXLangElement();
            if (!Constants.getInstance().checkIfElementIsInFile(element, getMymultipageeditor()) && !getMymultipageeditor().isCurrentUsedModel()) {
                delete_child.setEnabled(false);
                rename.setEnabled(false);
                return;
            }
            if (element.isClockElement() || EditableViewConstants.getInstance().getNoneditableelements().contains(element.getClassName())) {
                if (element.isClockElement()) {
                    delete_child.setEnabled(false);
                }
                rename.setEnabled(false);
                secondlevel_new_reference.setVisible(false);
                if (element.getClassName().equals("XORComponent") || element.getClassName().equals("HWXORComponent")) {
                    rename.setEnabled(true);
                    secondlevel_new_child.setVisible(true);
                }
                return;
            }
            if (element.getClass().equals(AbstractConnection.class)) {
                rename.setEnabled(false);
            }
            if (element.getParent() != null && element.getParent().isXOR()) {
                Role role = null;
                if (element.getParent().getClass().equals(Feature.class)) {
                    role = Role.FEATURE;
                } else if (element.getParent().getClass().equals(XORComponent.class)) {
                    role = Role.SUBCOMPONENT;
                } else if (element.getParent().getClass().equals(Function.class)) {
                    role = Role.FUNCTION;
                } else if (element.getParent().getChildren(role).size() <= 2) {
                    delete_child.setEnabled(false);
                }
            }
            if (element.getClass().equals(SubComponent.class)) {
                if (element.isXOR()) {
                    delete_child.setEnabled(false);
                    rename.setEnabled(false);
                    secondlevel_new_reference.setVisible(false);
                    secondlevel_new_child.setVisible(false);
                    return;
                }
            }
            if (element.getParent() != null && (element.getParent().getClass().equals(XORComponent.class))) {
                if (!element.getClass().equals(SubComponent.class)) {
                    secondlevel_new_reference.setVisible(false);
                    secondlevel_new_child.setVisible(false);
                    return;
                }
            }
            for (int i = 0; i < ReferenceKind.values().length; i++) {
                if (ReferenceKind.values()[i].name().equals("REFERENCE") || ReferenceKind.values()[i].name().equals("PATH") && EditableViewConstants.getInstance().getSpecialreferenceactionsmenu().containsKey(element.getClass())) {
                    Object[] types = element.getValidElementTypes(ReferenceKind.values()[i]).toArray();
                    for (int r = 0; r < types.length; r++) {
                        secondlevel_new_reference.setVisible(true);
                    }
                }
            }
            secondlevel_new_child.setVisible(false);
            for (int i = 0; i < ReferenceKind.values().length; i++) {
                if (ReferenceKind.values()[i].name().equals("CHILD")) {
                    Object[] types = element.getValidElementTypes(ReferenceKind.values()[i]).toArray();
                    for (int r = 0; r < types.length; r++) {
                        secondlevel_new_child.setVisible(true);
                    }
                }
            }
            if (element.equals(Session.getCurrentElement())) {
                delete_child.setEnabled(false);
            }
        } else if (selectedElement instanceof StructureNode) {
            delete_child.setEnabled(false);
            secondlevel_new_reference.setVisible(false);
            secondlevel_new_child.setVisible(false);
            StructureNode node = (StructureNode) selectedElement;
            Role role = node.getKind().getPossibleRole();
            if (role != null) {
                if (node.getParent() != null) {
                    if (node.getAXLElementParent().isClockElement()) {
                        return;
                    }
                    if (node.getParent().getClass().equals(XORComponent.class)) {
                        if (!role.equals(Role.SUBCOMPONENT)) {
                            return;
                        }
                    }
                }
                if (node.getParent() instanceof IAXLangElementNode) {
                    if (Constants.getInstance().checkIfElementIsInFile(((IAXLangElementNode) node.getParent()).getIAXLangElement(), getMymultipageeditor())) {
                        secondlevel_new_child.setVisible(true);
                    }
                } else {
                    secondlevel_new_child.setVisible(true);
                }
            }
        } else if (selectedElement instanceof FeatureInSelection) {
            delete_child.setEnabled(false);
            secondlevel_new_reference.setVisible(false);
            secondlevel_new_child.setVisible(false);
        }
    }

    /**
     * @brief initialize the constraints, where no action can appear.
     */
    private void initializeConstraints() {
        EditableViewConstants.getInstance().getNoneditableelements();
    }

    @Override
    public void init(IViewSite site) throws PartInitException {
        super.init(site);
    }

    /**
     * @brief Creates the part control of the view.
     * @param parent parent composite
     */
    @Override
    public void createPartControl(Composite parent) {
        Tree theTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        theTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        theTree.setLinesVisible(true);
        theTree.setHeaderVisible(true);
        TreeColumn colElementInstance = new TreeColumn(theTree, SWT.LEFT);
        colElementInstance.setText("Model Element");
        colElementInstance.setAlignment(SWT.LEFT);
        colElementInstance.setWidth(500);
        TreeColumn detailsInstance = new TreeColumn(theTree, SWT.LEFT);
        detailsInstance.setText("Details");
        detailsInstance.setAlignment(SWT.LEFT);
        detailsInstance.setWidth(200);
        setTreeViewer(new TreeViewer(theTree));
        getTreeViewer().addFilter(IAXLView.filter);
        getTreeViewer().setContentProvider(new EditableViewContentProvider(this));
        getTreeViewer().setLabelProvider(new EditableViewLabelProvider(this));
        EditableViewConstants.getInstance().setLabelprovider((EditableViewLabelProvider) getTreeViewer().getLabelProvider());
        TreeViewerColumn column1 = this.buildUpToolTipAndIncludeForColumn(colElementInstance);
        TreeViewerColumn column2 = new TreeViewerColumn(getTreeViewer(), detailsInstance);
        column2.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                String text = "";
                Object theElement = element;
                if (element instanceof IAXLangElementNode) {
                    theElement = ((IAXLangElementNode) element).getIAXLangElement();
                }
                if (theElement instanceof IAXLangElement) {
                    if (theElement instanceof Port) {
                        boolean bFurther = false;
                        for (DataElement theDataElement : ((Port) theElement).getDataElements()) {
                            if (bFurther) {
                                text += ",";
                            }
                            text += theDataElement.getIdentifier();
                            bFurther = true;
                        }
                    }
                }
                return text;
            }
        });
        ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(column1.getViewer()) {

            @Override
            protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
                return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION || event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
            }
        };
        TreeViewerEditor.create(getTreeViewer(), actSupport, ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);
        column1.setEditingSupport(new EditingSupport(getTreeViewer()) {

            TextCellEditor editor = null;

            IContentProposalProvider contentProposalProvider = null;

            @Override
            protected void setValue(Object element, Object value) {
                if (value == null) {
                    return;
                }
                if (element instanceof IAXLangElementNode) {
                    IAXLangElement node = ((IAXLangElementNode) element).getIAXLangElement();
                    changeEditorContent(node, String.valueOf(value));
                } else {
                    try {
                        throw new InvalidParameterException("Not expected:" + element);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected Object getValue(Object element) {
                if (element instanceof IAXLangElementNode) {
                    if (((IAXLangElementNode) element).getIAXLangElement() != null) {
                        return ((IAXLangElementNode) element).getIAXLangElement().getIdentifier();
                    }
                }
                return getLabelProvider().getText(element);
            }

            @Override
            protected CellEditor getCellEditor(Object element) {
                if (editor == null) {
                    contentProposalProvider = new SimpleContentProposalProvider(new String[] { "no suggestion" });
                    editor = new EditableViewContentProposal((Composite) getTreeViewer().getControl(), contentProposalProvider, KeyStroke.getInstance(SWT.CTRL, 32), null);
                    editor.setValidator(new ICellEditorValidator() {

                        public String isValid(Object value) {
                            errorinrename = false;
                            String newText = String.valueOf(value);
                            if (newText.equals(getLabelProvider().getText(actualselectedelement))) {
                                if (newText.contains(" ")) {
                                    newText = newText.substring(newText.indexOf(" ") + 1, newText.length());
                                }
                            }
                            String errormessage = null;
                            if (newText.trim().length() == 0) {
                                errormessage = "The identifier of an element must no be a empty string.";
                            }
                            if (newText.length() > 0) {
                                if (!newText.substring(0, 1).matches("[a-zA-Z]") && !newText.substring(0, 1).equals("_")) {
                                    errormessage = "The identifier of an element have to start with a letter or with \'_\'";
                                }
                            }
                            if (Pattern.compile("\\W").matcher(newText).find() && !newText.contains("_")) {
                                errormessage = "No non-word-characters (e.g. \'ß\') allowed for the identifier of an element.";
                            }
                            for (int i = 0; i < AXLKeyword.values().length; i++) {
                                if (newText.equals(AXLKeyword.values()[i].getAXL())) {
                                    errormessage = "It's a AXL keyword!";
                                }
                            }
                            if (actualselectedelement.getUID().contains(".")) {
                                Set<Entry<Role, Collection<IAXLangElement>>> entries = Session.getCurrentElement().getDescendants().entrySet();
                                for (Entry<Role, Collection<IAXLangElement>> entry : entries) {
                                    Object[] o = entry.getValue().toArray();
                                    for (int i = 0; i < o.length; i++) {
                                        IAXLangElement axlelement = (IAXLangElement) o[i];
                                        if (!axlelement.equals(actualselectedelement)) {
                                            String uidcompare = actualselectedelement.getUID().substring(0, actualselectedelement.getUID().lastIndexOf("."));
                                            if (axlelement.getIdentifier().equals(newText) && axlelement.getUID().equals((uidcompare + "." + newText).toLowerCase())) {
                                                errormessage = "An element with the same identifier also exists in the same hierarchie!(" + axlelement.getUID() + "-AXLElement:" + axlelement.getClassName() + ")";
                                            }
                                        }
                                    }
                                }
                            }
                            if (Session.getForbiddenIdentifiers().contains(newText)) {
                                errormessage = "The desired identifier " + newText + " also exists inside pure::variants as a unique name.";
                            }
                            if (errormessage != null) {
                                errorinrename = true;
                            }
                            getMymultipageeditor().getEditorSite().getActionBars().getStatusLineManager().setErrorMessage(errormessage);
                            return null;
                        }
                    });
                }
                return editor;
            }

            @Override
            protected boolean canEdit(Object element) {
                if (element instanceof IAXLangElementNode) {
                    Session.fireRefreshForbiddenIdentifierList(AXLTreeEditor.this, ((IAXLangElementNode) element).getIAXLangElement());
                }
                return isElementEditable(element);
            }
        });
        getTreeViewer().setAutoExpandLevel(2);
        getTreeViewer().getControl().addMouseListener(new MouseListener() {

            public void mouseUp(MouseEvent e) {
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseDoubleClick(MouseEvent e) {
            }
        });
        getTreeViewer().getControl().addKeyListener(new KeyListener() {

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.keyCode == 127) {
                    checkEnableStatusOfActions();
                    delete_child.run();
                }
                if (e.stateMask == SWT.CTRL && e.keyCode == 122) {
                    getMymultipageeditor().getAxlEditor().performUndo();
                }
                if (e.stateMask == SWT.CTRL && e.keyCode == 121) {
                }
            }
        });
        super.createPartControl(parent);
        initializeActions();
        initializeMenuManagers();
        initializeConstraints();
        createContextMenu();
    }

    @Override
    public void setInput(Object theInput) {
        super.setInput(theInput);
        setPartName("EV: " + Session.getCurrentElement().getIdentifier());
    }

    @Override
    public void hookPageSelection() {
    }

    @Override
    public void hookContextMenu() {
    }

    /**
     * @brief check the new value and perhaps changes the editor content.
     * @param node
     * @param value
     */
    public void changeEditorContent(IAXLangElement node, String value) {
        if (!errorinrename) {
            String theValue = value;
            String wholetext = getLabelProvider().getText(node);
            String[] gabstring = wholetext.split(node.getIdentifier());
            if (gabstring.length > 1) {
                theValue = extendedStringCheck(node, value);
            } else if (gabstring.length > 0) {
                if (theValue.contains(gabstring[0])) {
                    theValue = theValue.replace(gabstring[0], "");
                }
            }
            if (!node.getIdentifier().equals(value)) {
                node.setIdentifier(theValue);
                if (node.getClass().equals(XORComponent.class)) {
                    AXLangElementUtilities.getInstance().searchForComponentOrXORSubComponent(node, AbstractArchitectureModel.class, XORComponent.class, Role.COMPONENT, Role.SUBCOMPONENT, Role.COMPONENTTYPE).setIdentifier(theValue);
                } else getContentProvider().getTreeItem(((IStructuredSelection) getTreeViewer().getSelection()).getFirstElement()).setText(value);
                expand_action.updateAXBench(node);
                if (node instanceof Model) {
                    Session.fireSyncPointFileChanged(node, null, false);
                }
            }
        } else {
            getMymultipageeditor().getEditorSite().getActionBars().getStatusLineManager().setErrorMessage(null);
        }
    }

    /**
     * @brief if the gabstring.length>1 it's possible that the value look like 'processortype processortype' or 'Arduino
     *        Arduino'. This is a special case and it is treated so.
     * @param node
     * @param value
     * @return
     */
    private String extendedStringCheck(IAXLangElement node, String value) {
        return value;
    }

    /**
     * @brief Reacts on a change of the session element (currently only on saves inside the axleditor).
     * @param objCaller calling object
     */
    @Override
    public void elementChanged(Object objCaller) {
        if (!objCaller.equals(this) && !getMymultipageeditor().isIncluded()) {
            if (!objCaller.equals("INTERN_REFRESH") && Session.getCurrentmultipage().equals(getMymultipageeditor()) && !getTreeViewer().getControl().isDisposed()) {
                collectEditorInfos(objCaller);
                if (getMymultipageeditor().getAxlEditor().getMy_operations().getAxlEditorElement() == null) {
                    setPartName("No element");
                    getTreeViewer().getControl().setRedraw(false);
                    getTreeViewer().getTree().deselectAll();
                    getTreeViewer().setInput(null);
                    getTreeViewer().getControl().setRedraw(true);
                } else if (Session.isAxlcurrentinputsaved() || this.isReactonchange()) {
                    getContentProvider().getNodes().clear();
                    handleUpdate(false);
                    setPartName("EV: " + Session.getCurrentElement().getIdentifier());
                }
                IFile theEditorFile = ((IFileEditorInput) this.getMymultipageeditor().getEditorInput()).getFile();
                if (!theEditorFile.exists()) {
                    return;
                }
                if (Constants.getInstance().containsModel(theEditorFile.getRawLocation().toFile())) {
                    String path = (String) AXBenchProjectsPreferences.getInstance().getSimpleProjectPreferencesValue(theEditorFile.getProject(), SimpleProjectPreferences.MODELFILE.getQualifier());
                    String mypath = Constants.getInstance().correctedPath(theEditorFile.getRawLocation().toFile(), theEditorFile.getProject());
                    if (path != null && !path.equals(mypath)) {
                        AXBenchProjectsPreferences.getInstance().setSimpleProjectsPreferencesValue(theEditorFile.getProject(), SimpleProjectPreferences.MODELFILE.getQualifier(), Constants.getInstance().correctedPath(theEditorFile.getRawLocation().toFile(), theEditorFile.getProject()));
                        if (!getMymultipageeditor().isCurrentUsedModel()) {
                            try {
                                throw new InvalidParameterException("Not expected!" + getMymultipageeditor().getTitle());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            getMymultipageeditor().setCurrentUsedModel(true);
                        }
                    }
                }
            }
        } else {
        }
    }

    @Override
    public void selectionChanged(SelectionChangedEvent theChangeEvent) {
        getMymultipageeditor().setSelection(theChangeEvent.getSelection());
        if (Session.getCurrentmultipage().equals(getMymultipageeditor())) {
            ISelection theSelection = theChangeEvent.getSelection();
            if (theSelection.isEmpty()) {
                Session.getCurrenttexteditor().resetHighlightRange();
            } else {
                if (((IStructuredSelection) theSelection).getFirstElement() instanceof IAXLangElementNode) {
                    IAXLangElement axlElement = ((IAXLangElementNode) ((IStructuredSelection) theSelection).getFirstElement()).getIAXLangElement();
                    handleSelection(axlElement);
                } else if (((IStructuredSelection) theSelection).getFirstElement() instanceof IAXLangElement) {
                    IAXLangElement axlElement = (IAXLangElement) ((IStructuredSelection) theSelection).getFirstElement();
                    handleSelection(axlElement);
                } else {
                    Session.getCurrenttexteditor().resetHighlightRange();
                }
            }
        }
    }

    /**
     * @brief checks if the element is in this file (include files...).
     * @param axlElement
     */
    private void handleSelection(IAXLangElement axlElement) {
        if (Constants.getInstance().checkIfElementIsInFile(axlElement, getMymultipageeditor())) {
            try {
                IDocument theDocument = Session.getCurrenttexteditor().getDocumentProvider().getDocument(Session.getCurrenttexteditor().getEditorInput());
                getContentProvider().handleCorrectSelection(axlElement, theDocument);
            } catch (Exception e) {
                Session.getCurrenttexteditor().resetHighlightRange();
            }
        } else {
        }
    }

    /**
     * @brief creates the context menu, without considering the parent context menu.
     */
    private void createContextMenu() {
        Menu theMenu = lowLevel.createContextMenu(getTreeViewer().getControl());
        getTreeViewer().getControl().setMenu(theMenu);
    }

    /**
     * @brief check if the element is editable
     * @param element
     * @return true||false
     */
    protected boolean isElementEditable(Object element) {
        if (element instanceof StructureNode) {
            return false;
        } else if (element instanceof IAXLangElementNode) {
            IAXLangElement axlelement = ((IAXLangElementNode) element).getIAXLangElement();
            if (!Constants.getInstance().checkIfElementIsInFile(axlelement, getMymultipageeditor()) && !getMymultipageeditor().isCurrentUsedModel()) {
                return false;
            }
            if (axlelement.isClockElement()) {
                return false;
            }
            if (axlelement.getClass().equals(AbstractConnection.class)) {
                return false;
            }
            if (axlelement.getClass().equals(Activation.class)) {
                return false;
            }
            if (axlelement.getParent() != null && (axlelement.getParent().getClass().equals(AbstractF2ArchitectureMapping.class) || axlelement.getParent().getClass().equals(A2RMapping.class))) {
                return false;
            }
            if (EditableViewConstants.getInstance().getNoneditableelements().contains(axlelement.getClassName()) || (axlelement.getParent() != null && EditableViewConstants.getInstance().getNoneditableelements().contains(axlelement.getParent().getClassName()) && !(axlelement.getClassName().equals("HWSubComponent") || axlelement.getClassName().equals("SubComponent")))) {
                if (axlelement.getClass().equals(XORComponent.class)) {
                    actualselectedelement = axlelement;
                    return true;
                }
                return false;
            }
            if (axlelement.getClass().equals(SubComponent.class)) {
                if (axlelement.isXOR()) {
                    return false;
                }
            }
            actualselectedelement = axlelement;
            return true;
        } else if (element instanceof FeatureInSelection) {
            return false;
        } else {
            return false;
        }
    }

    /**
     * @return the new_reference
     */
    public LinkedHashMap<Role, NewReferenceAction> getNew_reference() {
        return new_reference;
    }

    /**
     * @return the new_child
     */
    public LinkedHashMap<Role, NewChildAction> getNew_child() {
        return new_child;
    }

    /**
     * @brief handles the included tree view and the visibility of the items. workaround, it's very expensive because of
     *        the expand and the collapse but perhapse there is another way to collect the informations from in to
     *        outwards.
     */
    public void handleIncludedTreeView() {
        if (Session.isIncluded() && getTreeViewer() != null && !getTreeViewer().getControl().isDisposed()) {
            getTreeViewer().getControl().setRedraw(false);
            TreePath[] paths = getTreeViewer().getExpandedTreePaths();
            getTreeViewer().expandAll();
            getTreeViewer().collapseAll();
            getTreeViewer().setExpandedTreePaths(paths);
            getTreeViewer().getControl().setRedraw(true);
        }
    }

    /**
     * @brief this method is necessary for the creation of all iaxlangelementnodes for the OpenInTreeEditor action.
     */
    public void handleNonIncludedTreeView() {
        if (!Session.isIncluded() && getTreeViewer() != null && !getTreeViewer().getControl().isDisposed()) {
            getTreeViewer().getControl().setRedraw(false);
            TreePath[] paths = getTreeViewer().getExpandedTreePaths();
            getTreeViewer().expandAll();
            getTreeViewer().collapseAll();
            getTreeViewer().setExpandedTreePaths(paths);
            getTreeViewer().getControl().setRedraw(true);
        }
    }

    /**
     * @brief handles to show the error at the topnode. needed to prevent expensive new draws.
     * @param iserror
     */
    public void handleError(boolean iserror) {
        try {
            if (iserror) {
                if (this.getTreeViewer().getTree().getItem(0) != null) {
                    this.getTreeViewer().getTree().getItem(0).setImage(ResourceManager.get(AXLMultiPageEditor.MODELIMGPATH + "_error"));
                }
            } else {
                if (this.getTreeViewer().getTree().getItem(0) != null) {
                    this.getTreeViewer().getTree().getItem(0).setImage(ResourceManager.get(AXLMultiPageEditor.MODELIMGPATH));
                }
            }
        } catch (Exception ex) {
        }
    }
}
