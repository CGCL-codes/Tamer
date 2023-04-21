package com.tresys.slide.plugin.views;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import com.tresys.slide.plugin.SLIDEPlugin;
import com.tresys.slide.plugin.SLIDEImageRegistry;
import com.tresys.slide.plugin.builder.SLIDEProjectNature;
import com.tresys.slide.plugin.editors.ISLIDEEditor;
import com.tresys.slide.plugin.editors.ModuleEditor;
import com.tresys.slide.plugin.editors.ModuleEditorInput;
import com.tresys.slide.plugin.views.policyexplorer.PolicyExplorerView;
import com.tresys.slide.utility.policyxmlparser.Component;
import com.tresys.slide.utility.policyxmlparser.Interface;
import com.tresys.slide.utility.policyxmlparser.Layer;
import com.tresys.slide.utility.policyxmlparser.Module;
import com.tresys.slide.utility.policyxmlparser.Template;

public class ModuleTreeView extends ViewPart implements IResourceChangeListener, ISelectionChangedListener {

    private TreeViewer viewer;

    private IWorkbenchPage page;

    private IPartListener2 listener;

    private Action doubleClickAction;

    private IProject currProject;

    public static String VIEW_ID = SLIDEPlugin.PLUGIN_ID + ".plugin.views.ModuleTreeView";

    public void setFocus() {
        viewer.getControl().setFocus();
    }

    public void init(IViewSite site) throws PartInitException {
        super.init(site);
        page = site.getPage();
        if (page.getActiveEditor() != null && page.getActiveEditor() instanceof ISLIDEEditor) {
            currProject = ((ISLIDEEditor) site.getPage().getActiveEditor()).getProject();
        } else {
            IViewReference[] views = page.getViewReferences();
            for (int i = 0; i < views.length; i++) {
                if (views[i].getView(false) instanceof PolicyExplorerView) {
                    ((PolicyExplorerView) views[i].getView(false)).getTreeViewer().addSelectionChangedListener(ModuleTreeView.this);
                    Object selection = ((IStructuredSelection) ((PolicyExplorerView) views[i].getView(false)).getTreeViewer().getSelection()).getFirstElement();
                    if (selection instanceof IResource) {
                        this.currProject = ((IResource) selection).getProject();
                    } else if (selection instanceof Component) {
                        this.currProject = ((Component) selection).getProject();
                    }
                }
            }
        }
        listener = new IPartListener2() {

            PolicyExplorerView explorer = null;

            public void partInputChanged(IWorkbenchPartReference partRef) {
            }

            public void partVisible(IWorkbenchPartReference partRef) {
            }

            public void partHidden(IWorkbenchPartReference partRef) {
            }

            public void partOpened(IWorkbenchPartReference partRef) {
            }

            public void partDeactivated(IWorkbenchPartReference partRef) {
            }

            public void partClosed(IWorkbenchPartReference partRef) {
                if (explorer != null) explorer.getTreeViewer().removeSelectionChangedListener(ModuleTreeView.this);
                explorer = null;
            }

            public void partBroughtToTop(IWorkbenchPartReference partRef) {
            }

            public void partActivated(IWorkbenchPartReference partRef) {
                IWorkbenchPart part = partRef.getPart(false);
                if (part instanceof ISLIDEEditor) {
                    setCurrProject(((ISLIDEEditor) part).getProject());
                } else if (part instanceof PolicyExplorerView) {
                    if (explorer == part) return;
                    if (explorer != null) explorer.getTreeViewer().removeSelectionChangedListener(ModuleTreeView.this);
                    explorer = (PolicyExplorerView) part;
                    explorer.getTreeViewer().addSelectionChangedListener(ModuleTreeView.this);
                }
            }
        };
        page.addPartListener(listener);
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }

    public void init(IViewSite site, IMemento memento) throws PartInitException {
        init(site);
    }

    public void dispose() {
        super.dispose();
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        page.removePartListener(listener);
    }

    /**
	 * Get a new label provider for the outline
	 * @return the label provider for Policies
	 */
    ILabelProvider getLabelProvider() {
        return new ILabelProvider() {

            public boolean isLabelProperty(Object element, String property) {
                return true;
            }

            public void removeListener(ILabelProviderListener listener) {
            }

            public void dispose() {
            }

            public void addListener(ILabelProviderListener listener) {
            }

            public String getText(Object element) {
                if (element instanceof String) {
                    return (String) element;
                } else if (element instanceof Module) {
                    return element.toString() + " (" + ((Module) element).getNumFilteredChildren() + ")";
                } else if (element instanceof Layer) {
                    return element.toString() + " (" + ((Layer) element).getNumVisChildren() + ")";
                } else {
                    return element.toString();
                }
            }

            public Image getImage(Object element) {
                if (element instanceof Module) return SLIDEPlugin.getDefault().registry.Get(SLIDEImageRegistry.MODULE_ICON); else if (element instanceof Interface) return SLIDEPlugin.getDefault().registry.Get(SLIDEImageRegistry.INTERFACE_ICON); else if (element instanceof Template) return SLIDEPlugin.getDefault().registry.Get(SLIDEImageRegistry.TEMPLATE_ICON); else if (element instanceof Layer) return SLIDEPlugin.getDefault().registry.Get(SLIDEImageRegistry.LAYER_ICON);
                return null;
            }
        };
    }

    /**
	 * Get a new content provider for the outline
	 * @return The content provider for the tree
	 */
    ITreeContentProvider getContentProvider() {
        return new ITreeContentProvider() {

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            public void dispose() {
            }

            public Object[] getElements(Object inputElement) {
                Object theArray[] = null;
                try {
                    Map layers = null;
                    if (currProject != null && currProject.isOpen() && currProject.hasNature(SLIDEProjectNature.NATURE_ID)) layers = ((SLIDEProjectNature) currProject.getNature(SLIDEProjectNature.NATURE_ID)).getLayerMap();
                    if (layers != null) {
                        theArray = layers.values().toArray();
                        Arrays.sort(theArray);
                        return theArray;
                    }
                } catch (Exception ce) {
                    ce.printStackTrace();
                }
                return new String[] {};
            }

            public boolean hasChildren(Object element) {
                if (element instanceof Layer) {
                    return !((Layer) element).getModules().isEmpty();
                } else if (element instanceof Module) {
                    return (((Module) element).GetInterfaces().size() + ((Module) element).GetTemplates().size() > 0);
                }
                return false;
            }

            public Object getParent(Object element) {
                if (element instanceof Module) {
                    return ((Module) element).getLayer();
                } else if (element instanceof Interface) {
                    return ((Interface) element).getParent();
                } else if (element instanceof Template) {
                    return ((Template) element).getParent();
                }
                return null;
            }

            public Object[] getChildren(Object parentElement) {
                if (parentElement instanceof Layer) {
                    Object[] children = ((Layer) parentElement).getModules().values().toArray();
                    Arrays.sort(children);
                    return children;
                } else if (parentElement instanceof Module) {
                    Vector children = new Vector(((Module) parentElement).GetInterfaces().values());
                    children.addAll(((Module) parentElement).GetTemplates().values());
                    Object[] returnval = children.toArray();
                    Arrays.sort(returnval);
                    return returnval;
                }
                return new Object[] {};
            }
        };
    }

    public void createPartControl(Composite parent) {
        final Composite composite = new Composite(parent, SWT.FILL);
        viewer = new TreeViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(getContentProvider());
        viewer.setLabelProvider(getLabelProvider());
        viewer.setInput(getViewSite());
        MenuManager manager = new MenuManager();
        manager.createContextMenu(composite);
        setUpActions();
        hookDoubleClickAction();
        createHover();
        hookContextMenu();
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        new Label(composite, SWT.NONE).setText("Filter");
        final Text interfaceName = new Text(composite, SWT.BORDER);
        interfaceName.addMouseTrackListener(new MouseTrackListener() {

            public void mouseHover(MouseEvent e) {
                interfaceName.setToolTipText("Filter Interface List");
            }

            public void mouseExit(MouseEvent e) {
                interfaceName.setToolTipText("");
            }

            public void mouseEnter(MouseEvent e) {
            }
        });
        GridData treeData = new GridData(SWT.FILL, SWT.FILL, true, true);
        treeData.horizontalSpan = 2;
        viewer.getTree().setLayoutData(treeData);
        interfaceName.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        interfaceName.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                viewer.addFilter(new ViewerFilter() {

                    public boolean select(Viewer viewer, Object parentElement, Object element) {
                        if (element instanceof Component) {
                            return ((Component) element).Display(interfaceName.getText());
                        }
                        return false;
                    }
                });
            }
        });
    }

    private final void hookContextMenu() {
        MenuManager menuMgr = new MenuManager();
        menuMgr.add(new Separator(org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS));
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu("com.tresys.test", menuMgr, viewer);
    }

    public void resourceChanged(IResourceChangeEvent event) {
        try {
            if (event.getType() == IResourceChangeEvent.POST_BUILD) if (event.getDelta().getResource() == currProject) viewer.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                doubleClickAction.run();
            }
        });
    }

    /**
	 * Set all all the actions that are defined in this view
	 *
	 */
    private void setUpActions() {
        doubleClickAction = new Action() {

            public void run() {
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                int line = 0;
                Module m_Module = null;
                if (obj instanceof Module) {
                    m_Module = (Module) obj;
                } else if (obj instanceof Interface) {
                    Interface currInterface = (Interface) obj;
                    m_Module = currInterface.getParent();
                    line = currInterface.getLine();
                } else if (obj instanceof Template) {
                    Template currTemplate = (Template) obj;
                    m_Module = currTemplate.getParent();
                    line = currTemplate.getLine();
                }
                if (!m_Module.getIFFile().exists()) return;
                ModuleEditorInput input = new ModuleEditorInput(m_Module, ModuleEditorInput.IF_EDITOR_INDEX);
                try {
                    ModuleEditor editor = (ModuleEditor) IDE.openEditor(page, input, ModuleEditor.ID);
                    HashMap map = new HashMap();
                    map.put(IMarker.LINE_NUMBER, new Integer(line));
                    IMarker marker = m_Module.getIFFile().createMarker(IMarker.TEXT);
                    marker.setAttributes(map);
                    editor.displayMarker(marker);
                    marker.delete();
                } catch (CoreException ce) {
                    ce.printStackTrace();
                }
            }
        };
    }

    /**
	 * Create a hover manager for the viewer and install it
	 */
    private void createHover() {
        TreeHoverManager hoverManager = new TreeHoverManager(new IInformationControlCreator() {

            public IInformationControl createInformationControl(Shell parent) {
                return new DefaultInformationControl(parent);
            }
        });
        hoverManager.install(viewer.getControl());
    }

    /**
	 * Adds the given selection change listener from this tree viewer. 
	 * Has no affect if an identical listener is not registered. 
	 * 
	 * @param listener  a selection changed listener
	 */
    public void AddSelectionListener(ISelectionChangedListener listener) {
        viewer.addSelectionChangedListener(listener);
    }

    /**
	 * Removes the given selection change listener from this tree viewer. 
	 * Has no affect if an identical listener is not registered. 
	 * 
	 * @param listener  a selection changed listener
	 */
    public void RemoveSelectionListener(ISelectionChangedListener listener) {
        viewer.removeSelectionChangedListener(listener);
    }

    public void selectionChanged(SelectionChangedEvent event) {
        Object selection = ((IStructuredSelection) event.getSelection()).getFirstElement();
        if (selection instanceof Component) {
            setCurrProject(((Component) selection).getProject());
        } else if (selection instanceof IResource) {
            setCurrProject(((IResource) selection).getProject());
        }
    }

    private void setCurrProject(IProject currProject) {
        if (this.currProject != currProject) {
            this.currProject = currProject;
            viewer.refresh();
        }
    }

    public IProject getProject() {
        return currProject;
    }
}
