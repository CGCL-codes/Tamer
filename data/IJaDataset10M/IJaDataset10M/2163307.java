package org.omwg.ui.actions;

import java.util.Iterator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.omwg.jface.dialogs.CommitDialog;
import org.omwg.jface.viewers.TreeLabelProvider;
import org.omwg.ontology.Attribute;
import org.omwg.ontology.Instance;
import org.omwg.ontology.Ontology;
import org.omwg.ontology.Parameter;
import org.omwg.ontology.RelationInstance;
import org.omwg.ui.editors.text.MappingTreeEditor;
import org.omwg.ui.editors.text.WsmlTreeEditor;
import org.omwg.ui.models.Model;
import org.omwg.ui.models.TreeModel;
import org.omwg.ui.models.WsmlTreeModel;
import org.omwg.ui.part.MappingMultiPageEditor;
import org.omwg.ui.part.WsmlMultiPageEditor;
import org.omwg.ui.plugin.UIPlugin;
import org.omwg.ui.views.TableView;
import org.omwg.ui.views.TreeView;
import org.omwg.ui.views.properties.NFPSource;
import org.omwg.versioning.VersionIdentifier;
import org.omwg.versioning.Versioning;
import org.wsmo.common.Entity;
import org.wsmo.common.Namespace;

public class ActionFactory {

    public static Action newOntology() {
        Action a = new Action() {

            public void run() {
                runWith("Ontology");
            }
        };
        a.setText("New Ontology");
        a.setToolTipText("New Ontology");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/ontology.gif"));
        return a;
    }

    public static Action newNamespace() {
        Action a = new Action() {

            public void run() {
                runWith("Namespace");
            }
        };
        a.setText("New Namespace");
        a.setToolTipText("New Namespace");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/namespace.gif"));
        return a;
    }

    public static Action rename() {
        Action a = new Action() {

            public void run() {
                ISelection selection = getViewer().getSelection();
                Object obj = new Object();
                if (!selection.isEmpty()) {
                    obj = ((IStructuredSelection) selection).getFirstElement();
                }
                String title = "no title";
                title = TreeLabelProvider.getName(obj);
                rename(obj, title);
            }
        };
        a.setText("Rename");
        a.setToolTipText("Action 2 tooltip");
        return a;
    }

    public static Action delete() {
        Action a = new Action() {

            public void run() {
                ISelection selection = getViewer().getSelection();
                Iterator it = ((IStructuredSelection) selection).iterator();
                while (it.hasNext()) {
                    ((Model) getViewer().getContentProvider()).removeNode(it.next());
                }
            }
        };
        a.setText("Delete");
        a.setToolTipText("Action 2 tooltip");
        a.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
        return a;
    }

    public static Action commit() {
        Action a = new Action() {

            public void run() {
                ISelection selection = getViewer().getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                if (obj instanceof NFPSource) {
                    obj = ((NFPSource) obj).getNFPHolder();
                }
                if (!(obj instanceof Ontology)) {
                    return;
                }
                Ontology ont = (Ontology) obj;
                String vid = Versioning.getVersionIdentifier(ont).getID();
                if (vid == null || vid.equals("")) {
                    vid = "1.0";
                }
                CommitDialog dialog = new CommitDialog(getViewer().getControl().getShell(), "latest changes", vid, Versioning.getChangeLog(ont), null);
                dialog.open();
                String comment = dialog.getValue();
                String version = dialog.getVersion();
                if (comment == null) {
                    return;
                }
                try {
                    ((WsmlTreeModel) getViewer().getContentProvider()).commit(obj, comment, version);
                } catch (Exception e) {
                    MessageDialog.openWarning(getViewer().getControl().getShell(), "Warning", "This version has already been committed");
                }
            }
        };
        a.setText("Commit...");
        a.setToolTipText("Commit...");
        return a;
    }

    public static Action drop() {
        Action a = new Action() {

            public void run() {
                ISelection selection = getViewer().getSelection();
                final Object obj = ((IStructuredSelection) selection).getFirstElement();
                ((WsmlTreeModel) getViewer().getContentProvider()).drop(obj);
            }
        };
        a.setText("Drop");
        a.setToolTipText("Drop");
        return a;
    }

    public static Action newConcept() {
        Action a = new Action() {

            public void run() {
                runWith("Concept");
            }
        };
        a.setText("New Concept");
        a.setToolTipText("Action 2 tooltip");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/concept.gif"));
        return a;
    }

    public static Action newAttribute() {
        Action a = new Action() {

            public void run() {
                runWith("Attribute");
            }
        };
        a.setText("New Attribute");
        a.setToolTipText("Action 2 tooltip");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/attribute.gif"));
        return a;
    }

    public static Action newRelation() {
        Action a = new Action() {

            public void run() {
                runWith("Relation");
            }
        };
        a.setText("New Relation");
        a.setToolTipText("Action 2 tooltip");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/relation.gif"));
        return a;
    }

    public static Action newParameter() {
        Action a = new Action() {

            public void run() {
                runWith("Parameter");
            }
        };
        a.setText("New Parameter");
        a.setToolTipText("Action 2 tooltip");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/parameter.gif"));
        return a;
    }

    public static Action newInstance() {
        Action a = new Action() {

            public void run() {
                runWith("Instance");
            }
        };
        a.setText("New Instance");
        a.setToolTipText("Action 2 tooltip");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/instance.gif"));
        return a;
    }

    public static Action newAttributeValue() {
        Action a = new Action() {

            public void run() {
                runWith("Attribute Value");
            }
        };
        a.setText("New Attribute Value");
        a.setToolTipText("Action 2 tooltip");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/attribute.gif"));
        return a;
    }

    public static Action newRelationInstance() {
        Action a = new Action() {

            public void run() {
                runWith("RelationInstance");
            }
        };
        a.setText("New Relation Instance");
        a.setToolTipText("Action 2 tooltip");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/relation.gif"));
        return a;
    }

    public static Action newParameterValue() {
        Action a = new Action() {

            public void run() {
                runWith("Parameter Value");
            }
        };
        a.setText("New Parameter Value");
        a.setToolTipText("Action 2 tooltip");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/parameter.gif"));
        return a;
    }

    public static Action newAxiom() {
        Action a = new Action() {

            public void run() {
                runWith("Axiom");
            }
        };
        a.setText("New Axiom");
        a.setToolTipText("Action 2 tooltip");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/axiom.gif"));
        return a;
    }

    public static Action setCardinalities() {
        Action a = new Action() {

            public void run() {
                ISelection selection = getViewer().getSelection();
                Object obj = new Object();
                if (!selection.isEmpty()) {
                    obj = ((IStructuredSelection) selection).getFirstElement();
                }
                String title = "no title";
                title = TreeLabelProvider.getName(obj);
                Entity e = ((NFPSource) obj).getNFPHolder();
                addSpinner((Attribute) e, title, ((Attribute) e).getMinCardinality(), ((Attribute) e).getMaxCardinality());
            }
        };
        a.setText("Set Cardinalities");
        a.setToolTipText("Action 2 tooltip");
        return a;
    }

    public static Action setConstraining() {
        Action a = new Action() {

            public void run() {
                Object o = ((StructuredSelection) getViewer().getSelection()).getFirstElement();
                if (o instanceof NFPSource) o = ((NFPSource) o).getNFPHolder();
                ((WsmlTreeModel) getViewer().getContentProvider()).toggleConstraining(o);
            }
        };
        a.setText("Constraining");
        a.setToolTipText("Constraining");
        return a;
    }

    public static Action setReflexive() {
        Action a = new Action() {

            public void run() {
                Attribute a = (Attribute) ((NFPSource) ((StructuredSelection) getViewer().getSelection()).getFirstElement()).getNFPHolder();
                ((WsmlTreeModel) getViewer().getContentProvider()).setReflexive(a, !a.isReflexive());
            }
        };
        a.setText("Reflexive");
        a.setToolTipText("Reflexive");
        a.setChecked(false);
        return a;
    }

    public static Action setSymmetric() {
        Action a = new Action() {

            public void run() {
                Attribute a = (Attribute) ((NFPSource) ((StructuredSelection) getViewer().getSelection()).getFirstElement()).getNFPHolder();
                ((WsmlTreeModel) getViewer().getContentProvider()).setSymmetric(a, !a.isSymmetric());
            }
        };
        a.setText("Symmetric");
        a.setToolTipText("Symmetric");
        a.setChecked(false);
        return a;
    }

    public static Action setTransitive() {
        Action a = new Action() {

            public void run() {
                Attribute a = (Attribute) ((NFPSource) ((StructuredSelection) getViewer().getSelection()).getFirstElement()).getNFPHolder();
                ((WsmlTreeModel) getViewer().getContentProvider()).setTransitive(a, !a.isTransitive());
            }
        };
        a.setText("Transitive");
        a.setToolTipText("Transitive");
        a.setChecked(false);
        return a;
    }

    public static Action setDefault() {
        Action a = new Action() {

            public void run() {
                Namespace n = (Namespace) (((StructuredSelection) getViewer().getSelection()).getFirstElement());
                ((WsmlTreeModel) getViewer().getContentProvider()).setDefault(n);
            }
        };
        a.setText("As Default Namespace");
        a.setToolTipText("As Default Namespace");
        return a;
    }

    public static Action newGGMediator() {
        Action a = new Action() {

            public void run() {
                runWith("ggMediator");
            }
        };
        a.setText("GG Mediator");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/mediator.gif"));
        return a;
    }

    public static Action newOOMediator() {
        Action a = new Action() {

            public void run() {
                runWith("ooMediator");
            }
        };
        a.setText("OO Mediator");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/mediator.gif"));
        return a;
    }

    public static Action newWWMediator() {
        Action a = new Action() {

            public void run() {
                runWith("wwMediator");
            }
        };
        a.setText("WW Mediator");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/mediator.gif"));
        return a;
    }

    public static Action newWGMediator() {
        Action a = new Action() {

            public void run() {
                runWith("wgMediator");
            }
        };
        a.setText("WG Mediator");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/mediator.gif"));
        return a;
    }

    public static Action newAnnotation() {
        Action a = new Action() {

            public void run() {
                runWith("Annotation");
            }
        };
        a.setText("New Annotation");
        a.setToolTipText("New Annotation");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/axiom.gif"));
        return a;
    }

    public static Action newMappingRule() {
        Action a = new Action() {

            public void run() {
                runWith("MappingRule");
            }
        };
        a.setText("New Mapping Rule");
        a.setToolTipText("New Mapping Rule");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/axiom.gif"));
        return a;
    }

    public static Action newClassCondition() {
        Action a = new Action() {

            public void run() {
                runWith("Class Condition");
            }
        };
        a.setText("New Class Condition");
        a.setToolTipText("New Class Condition");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/concept.gif"));
        return a;
    }

    public static Action newInstanceCondition() {
        Action a = new Action() {

            public void run() {
                runWith("Instance Condition");
            }
        };
        a.setText("New Instance Condition");
        a.setToolTipText("New Instance Condition");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/concept.gif"));
        return a;
    }

    public static Action newRelationCondition() {
        Action a = new Action() {

            public void run() {
                runWith("Relation Condition");
            }
        };
        a.setText("New Relation Condition");
        a.setToolTipText("New Relation Condition");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/concept.gif"));
        return a;
    }

    public static Action newAttributeCondition() {
        Action a = new Action() {

            public void run() {
                runWith("Attribute Condition");
            }
        };
        a.setText("New Attribute Condition");
        a.setToolTipText("New Attribute Condition");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/concept.gif"));
        return a;
    }

    public static Action changeValue() {
        Action a = new Action() {

            public void run() {
                ISelection selection = getViewer().getSelection();
                Object obj = new Object();
                if (!selection.isEmpty()) {
                    obj = ((IStructuredSelection) selection).getFirstElement();
                }
                Control c = getViewer().getControl();
                Item[] i = new Item[0];
                if (c instanceof Tree) {
                    i = ((Tree) c).getSelection();
                }
                String title = "no title";
                if (i.length > 0) {
                    title = i[0].getText();
                }
                rename(obj, title);
            }
        };
        a.setText("Change Value");
        a.setToolTipText("Change Value");
        a.setImageDescriptor(UIPlugin.imageDescriptorFromPlugin("org.omwg.dome", "img/concept.gif"));
        return a;
    }

    public static void runWith(final String NFPHolder) {
        ISelection selection = getViewer().getSelection();
        Object parent;
        if (((IStructuredSelection) selection).getFirstElement() == null) {
            parent = getViewer().getInput();
        } else {
            parent = ((IStructuredSelection) selection).getFirstElement();
        }
        String child = "New " + NFPHolder;
        TreeModel m = (TreeModel) getViewer().getContentProvider();
        Object newNode = m.addNode(NFPHolder, parent);
        if (newNode instanceof Entity) {
            m.rename(newNode, child);
        }
        StructuredSelection sel = new StructuredSelection(newNode);
        if (NFPHolder.equals("Instance") || NFPHolder.equals("RelationInstance")) {
            ((WsmlTreeEditor) getViewer().getInput()).tableViewer.setSelection(sel);
        } else {
            ((TreeViewer) getViewer()).setExpandedState(parent, true);
            (getViewer()).setSelection(sel);
        }
        if (!(newNode instanceof Parameter)) {
            rename(newNode, child);
        }
    }

    public static void rename(final Object node, String newName) {
        final StructuredViewer currentViewer = getCurrentViewer(node, newName);
        IStructuredContentProvider cp = ((IStructuredContentProvider) currentViewer.getContentProvider());
        Composite c = (Composite) currentViewer.getControl();
        Item[] items = new Item[0];
        ControlEditor editor = new ControlEditor(c);
        Rectangle bounds = new Rectangle(0, 0, 0, 0);
        if (currentViewer instanceof TableView) {
            c = ((TableView) currentViewer).getTable();
            items = ((Table) c).getSelection();
            bounds = ((TableItem) items[0]).getBounds(0);
            editor = new TableEditor((Table) c);
        }
        if (currentViewer instanceof TreeView) {
            c = ((TreeView) currentViewer).getTree();
            items = ((Tree) c).getSelection();
            if (items.length > 0) {
                bounds = ((TreeItem) items[0]).getBounds();
            }
            editor = new TreeEditor((Tree) c);
        }
        Control oldEditor = editor.getEditor();
        if (oldEditor != null) oldEditor.dispose();
        Item item;
        try {
            item = items[0];
        } catch (Exception e) {
            return;
        }
        final Text text = new Text(c, SWT.BORDER | SWT.COLOR_TITLE_FOREGROUND | SWT.FLAT | SWT.MAX);
        text.setText(newName);
        text.pack();
        text.setEditable(true);
        text.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
                    confirm(text, currentViewer, node);
                }
                if (e.keyCode == SWT.ESC) {
                    text.dispose();
                }
            }
        });
        text.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                confirm(text, currentViewer, node);
            }
        });
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = false;
        editor.minimumWidth = bounds.width + 10;
        editor.minimumHeight = 20;
        if (editor instanceof TableEditor) {
            ((TableEditor) editor).setEditor(text, (TableItem) item, 0);
        }
        if (editor instanceof TreeEditor) {
            ((TreeEditor) editor).setEditor(text, (TreeItem) item, 0);
        }
        text.setFocus();
        text.selectAll();
    }

    private static void addSpinner(final Attribute attribute, final String newName, int minValue, int maxValue) {
        final StructuredViewer currentViewer = getCurrentViewer(attribute, newName);
        final Tree tree = (Tree) currentViewer.getControl();
        Item item = tree.getSelection()[0];
        TreeEditor editor = new TreeEditor(tree);
        Control oldEditor = editor.getEditor();
        final Composite composite = new Composite(tree, SWT.NONE);
        GridLayout layout = new GridLayout(3, false);
        layout.marginHeight = 0;
        layout.horizontalSpacing = 0;
        layout.marginWidth = 2;
        composite.setLayout(layout);
        composite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        GridData minData = new GridData();
        GridData maxData = new GridData();
        Text text = new Text(composite, SWT.FLAT | SWT.MAX);
        text.setText(newName);
        text.setEnabled(false);
        text.setEditable(false);
        text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        text.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        text.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING));
        text.pack();
        final Spinner min = new Spinner(composite, SWT.BORDER);
        min.setToolTipText("Minimal Cardinality");
        min.setSize(20, 10);
        min.setMaximum(Integer.MAX_VALUE);
        min.setSelection(minValue);
        minData.verticalAlignment = GridData.FILL;
        min.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING));
        min.setFocus();
        final Spinner max = new Spinner(composite, SWT.BORDER);
        min.setToolTipText("Maximal Cardinality");
        max.setLayoutData(maxData);
        max.setMaximum(Integer.MAX_VALUE);
        max.setSelection(maxValue);
        composite.setFocus();
        composite.traverse(1);
        final FocusListener fl = new FocusListener() {

            public void focusGained(FocusEvent e) {
                ((WsmlTreeModel) currentViewer.getContentProvider()).setCardinalities((Attribute) attribute, min.getSelection(), max.getSelection());
                tree.removeFocusListener(this);
                composite.dispose();
                tree.redraw();
            }

            public void focusLost(FocusEvent e) {
            }
        };
        tree.addFocusListener(fl);
        KeyListener kl = new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
                    tree.removeFocusListener(fl);
                    ((WsmlTreeModel) currentViewer.getContentProvider()).setCardinalities((Attribute) attribute, min.getSelection(), max.getSelection());
                    composite.dispose();
                    tree.redraw();
                }
                if (e.keyCode == SWT.ESC) {
                    tree.removeFocusListener(fl);
                    composite.dispose();
                }
            }
        };
        min.addKeyListener(kl);
        max.addKeyListener(kl);
        editor.horizontalAlignment = SWT.LEFT;
        editor.verticalAlignment = SWT.TOP;
        editor.minimumWidth = ((TreeItem) item).getBounds().width + 150;
        editor.minimumHeight = 16;
        editor.setEditor(composite, (TreeItem) item, 0);
    }

    private static void confirm(Control control, StructuredViewer currentViewer, Object node) {
        String s = "";
        if (control instanceof Text) {
            s = ((Text) control).getText();
        }
        try {
            ((Model) currentViewer.getContentProvider()).rename(node, s);
        } catch (RuntimeException e) {
            e.printStackTrace();
            MessageDialog.openWarning(UIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), "Illegal Argument", e.getLocalizedMessage());
            return;
        }
        control.dispose();
        currentViewer.setSelection(new StructuredSelection(node));
    }

    private static StructuredViewer getViewer() {
        IEditorPart part = UIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (part instanceof WsmlMultiPageEditor) {
            IEditorPart page = ((WsmlMultiPageEditor) part).getEditor(0);
            if (page instanceof WsmlTreeEditor) {
                return ((WsmlTreeEditor) page).getViewer();
            }
        }
        if (part instanceof MappingMultiPageEditor) {
            IEditorPart page = ((MappingMultiPageEditor) part).getEditor(0);
            if (page instanceof MappingTreeEditor) {
                return ((MappingTreeEditor) page).getViewer();
            }
        }
        return null;
    }

    private static StructuredViewer getCurrentViewer(final Object node, String newName) {
        StructuredViewer currentViewer;
        Entity entity = null;
        if (node instanceof NFPSource) {
            entity = ((NFPSource) node).getNFPHolder();
        }
        if (newName.equals("New Instance") || newName.equals("New RelationInstance") || entity instanceof Instance || entity instanceof RelationInstance) {
            currentViewer = ((WsmlTreeEditor) getViewer().getInput()).tableViewer;
        } else {
            currentViewer = getViewer();
        }
        return currentViewer;
    }
}
