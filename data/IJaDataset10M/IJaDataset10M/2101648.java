package com.c4j.workbench.wtree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import com.c4j.IFragmentReference;
import com.c4j.IFragmentReferenceHolder;
import com.c4j.assembly.IAssembly;
import com.c4j.assembly.ILibrary;
import com.c4j.assembly.ILibraryReference;
import com.c4j.assembly.IMain;
import com.c4j.component.IComponent;
import com.c4j.component.IComponentPort;
import com.c4j.composition.IComposition;
import com.c4j.composition.IConnection;
import com.c4j.composition.IInstance;
import com.c4j.composition.IPublicFacetPort;
import com.c4j.composition.IPublicPort;
import com.c4j.composition.IPublicPortReference;
import com.c4j.composition.IPublicReceptaclePort;
import com.c4j.workbench.Actions;
import com.c4j.workbench.IconSet;
import com.c4j.workbench.WorkbenchIface;
import com.c4j.workbench.diagram.DiagramCreator;
import com.c4j.workspace.IContainer;
import com.c4j.workspace.IContainerElement;
import com.c4j.workspace.IFacetPort;
import com.c4j.workspace.IFolder;
import com.c4j.workspace.IJar;
import com.c4j.workspace.IPort;
import com.c4j.workspace.IReceptaclePort;
import com.c4j.workspace.IWorkspace;

@SuppressWarnings("serial")
public class WorkspaceTree extends JTree {

    private final WorkspaceTreeModelTranslator translator;

    private final DefaultTreeModel model;

    private static boolean openCloseMenue(final Object value, final boolean noSeparator, final JPopupMenu menu, final DiagramCreator creator, final JFrame frame) {
        boolean result = noSeparator;
        if (value instanceof IComposition) {
            final IComposition composition = (IComposition) value;
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.OpenDiagramAction(creator, composition));
            menu.add(new Actions.CloseDiagramAction(creator, composition));
            result = false;
            addNewComponentReferenceMenu(menu, composition);
            addNewCompositionReferenceMenu(menu, composition);
            menu.add(new Actions.NewJarAction(frame, composition));
        }
        return result;
    }

    private static boolean creationMenu(final Object value, final boolean noSeparator, final JPopupMenu menu, final WorkbenchIface.Neighborhood neighborhood, final JFrame frame, final JTree tree, final WorkspaceTreeModelTranslator translator) {
        boolean result = noSeparator;
        if (value instanceof IContainer) {
            final IContainer container = (IContainer) value;
            final IWorkspace workspace = container.getWorkspace();
            menu.add(new Actions.NewFolderAction(tree, translator, container));
            menu.add(new Actions.NewComponentAction(frame, neighborhood, workspace, container));
            menu.add(new Actions.NewCompositionAction(frame, neighborhood, workspace, container));
            menu.add(new Actions.NewAssemblyAction(tree, translator, container));
            result = false;
        }
        if (value instanceof IAssembly) {
            final IAssembly assembly = (IAssembly) value;
            menu.add(new Actions.NewLibraryAction(neighborhood.use_type(), frame, assembly));
            result = false;
        }
        if (value instanceof ILibrary) {
            final ILibrary library = (ILibrary) value;
            addNewLibraryReferenceMenu(menu, library);
            addNewComponentReferenceMenu(menu, library);
            addNewCompositionReferenceMenu(menu, library);
            result = false;
        }
        if (value instanceof IComposition) {
            final IComposition composition = (IComposition) value;
            if (!noSeparator) menu.addSeparator();
            addNewComponentInstanceMenu(frame, menu, composition);
            addNewCompositionInstanceMenu(frame, menu, composition);
            addNewConnectionMenu(menu, composition);
            menu.add(new Actions.NewPortAction(frame, neighborhood, composition));
            result = false;
        }
        if (value instanceof IComponent) {
            final IComponent component = (IComponent) value;
            if (!noSeparator) menu.addSeparator();
            addNewComponentReferenceMenu(menu, component);
            addNewCompositionReferenceMenu(menu, component);
            menu.add(new Actions.NewPortAction(frame, neighborhood, component));
            menu.add(new Actions.NewJarAction(frame, component));
            result = false;
        }
        if (value instanceof IPublicPort) {
            final IPublicPort publicPort = (IPublicPort) value;
            if (!noSeparator) menu.addSeparator();
            addNewPublicPortReferenceMenu(menu, publicPort);
            result = false;
        }
        return result;
    }

    private static boolean removeMenu(final Object value, final boolean noSeparator, final JPopupMenu menu) {
        boolean result = noSeparator;
        if (value instanceof IContainerElement) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.RemoveContainerElementAction((IContainerElement) value));
            result = false;
        }
        if (value instanceof ILibrary) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.RemoveLibraryAction((ILibrary) value));
            result = false;
        }
        if (value instanceof IFragmentReference) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.RemoveFragmentReferenceAction((IFragmentReference) value));
            result = false;
        }
        if (value instanceof ILibraryReference) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.RemoveLibraryReferenceAction((ILibraryReference) value));
            result = false;
        }
        if (value instanceof IInstance) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.RemoveInstanceAction((IInstance) value));
            result = false;
        }
        if (value instanceof IConnection) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.RemoveConnectionAction((IConnection) value));
            result = false;
        }
        if (value instanceof IPublicPort) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.RemovePortAction((IPublicPort) value));
            result = false;
        }
        if (value instanceof IComponentPort) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.RemovePortAction((IComponentPort) value));
            result = false;
        }
        if (value instanceof IPublicPortReference) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.RemovePublicPortReferenceAction((IPublicPortReference) value));
            result = false;
        }
        if (value instanceof IJar) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.RemoveJarAction((IJar) value));
            result = false;
        }
        return result;
    }

    private static boolean renameMenu(final Object value, final boolean noSeparator, final JPopupMenu menu, final JTree tree, final TreePath path) {
        boolean result = noSeparator;
        if (value instanceof IContainerElement || value instanceof ILibrary || value instanceof IInstance || value instanceof IPort) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.RenameAction(tree, path));
            result = false;
        }
        return result;
    }

    private static boolean propertiesMenu(final Object value, final boolean noSeparator, final JPopupMenu menu, final WorkbenchIface.Neighborhood neighborhood, final JFrame frame) {
        boolean result = noSeparator;
        if (value instanceof IComposition) {
            final IComposition composition = (IComposition) value;
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.CompositionPropertiesAction(frame, neighborhood, composition));
            result = false;
        }
        if (value instanceof IComponent) {
            final IComponent component = (IComponent) value;
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.ComponentPropertiesAction(frame, neighborhood, component));
            result = false;
        }
        if (value instanceof ILibrary) {
            final ILibrary library = (ILibrary) value;
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.LibraryPropertiesAction(neighborhood.use_type(), frame, library));
            result = false;
        }
        if (value instanceof IPort) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.PortPropertiesAction(frame, neighborhood, (IPort) value));
            result = false;
        }
        if (value instanceof IJar) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.JarPropertiesAction(frame, (IJar) value));
            result = false;
        }
        if (value instanceof IConnection) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.ConnectionPropertiesAction(frame, (IConnection) value));
            result = false;
        }
        if (value instanceof IPublicPortReference) {
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.PublicPortReferencePropertiesAction(frame, (IPublicPortReference) value));
            result = false;
        }
        return result;
    }

    private static boolean tasksMenu(final Object value, final boolean noSeparator, final JPopupMenu menu, final WorkbenchIface.Neighborhood neighborhood, final DiagramCreator creator, final JFrame frame) {
        final boolean result = noSeparator;
        if (value instanceof IComposition) {
            final IComposition composition = (IComposition) value;
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.SaveAction(frame, neighborhood.use_xml(), creator, composition));
            menu.add(new Actions.CleanAction(frame, neighborhood.use_cleaner(), composition));
            menu.add(new Actions.CleanThoroughAction(frame, neighborhood.use_cleaner(), composition));
            menu.add(new Actions.GenerateAction(frame, neighborhood.use_generator(), composition));
            menu.add(new Actions.GenerateEclipseAction(frame, neighborhood.use_generator(), composition));
            menu.add(new Actions.BuildAction(frame, neighborhood.use_builder(), composition));
        }
        if (value instanceof IComponent) {
            final IComponent component = (IComponent) value;
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.SaveAction(frame, neighborhood.use_xml(), null, component));
            menu.add(new Actions.CleanAction(frame, neighborhood.use_cleaner(), component));
            menu.add(new Actions.CleanThoroughAction(frame, neighborhood.use_cleaner(), component));
            menu.add(new Actions.GenerateAction(frame, neighborhood.use_generator(), component));
            menu.add(new Actions.GenerateEclipseAction(frame, neighborhood.use_generator(), component));
            menu.add(new Actions.BuildAction(frame, neighborhood.use_builder(), component));
        }
        if (value instanceof ILibrary) {
            final ILibrary library = (ILibrary) value;
            if (library.getMain() != null) {
                final IMain main = library.getMain();
                if (!noSeparator) menu.addSeparator();
                menu.add(new Actions.CleanAction(frame, neighborhood.use_cleaner(), main));
                menu.add(new Actions.CleanThoroughAction(frame, neighborhood.use_cleaner(), main));
                menu.add(new Actions.GenerateAction(frame, neighborhood.use_generator(), main));
                menu.add(new Actions.GenerateEclipseAction(frame, neighborhood.use_generator(), main));
                menu.add(new Actions.BuildAction(frame, neighborhood.use_builder(), main));
            }
        }
        if (value instanceof IAssembly) {
            final IAssembly assembly = (IAssembly) value;
            if (!noSeparator) menu.addSeparator();
            menu.add(new Actions.SaveAction(frame, neighborhood.use_xml(), null, assembly));
            menu.add(new Actions.CleanAction(frame, neighborhood.use_cleaner(), assembly));
            menu.add(new Actions.PackAction(frame, neighborhood.use_packer(), assembly));
        }
        return result;
    }

    public WorkspaceTree(final WorkbenchIface.Neighborhood neighborhood, final JFrame frame, final IWorkspace workspace, final DiagramCreator creator) {
        super();
        translator = new WorkspaceTreeModelTranslator(workspace, this);
        model = new DefaultTreeModel(translator.getNode(workspace));
        setModel(model);
        setRootVisible(false);
        setShowsRootHandles(true);
        setEditable(true);
        final WorkspaceTreeCellRenderer renderer = new WorkspaceTreeCellRenderer(translator);
        setCellRenderer(renderer);
        setCellEditor(new WorkspaceTreeCellEditor(this, translator));
        getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        ToolTipManager.sharedInstance().registerComponent(this);
        addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(final MouseEvent arg0) {
            }

            @Override
            public void mousePressed(final MouseEvent arg0) {
                if (arg0.isPopupTrigger()) {
                    final Object value;
                    final TreePath path;
                    final int row = getRowForLocation(arg0.getX(), arg0.getY());
                    if (row == -1) path = null; else path = getPathForRow(row);
                    setSelectionPath(path);
                    if (path == null) value = workspace; else value = translator.getValue((DefaultMutableTreeNode) path.getLastPathComponent());
                    final JPopupMenu menu = new JPopupMenu();
                    boolean noSeparator = true;
                    noSeparator = openCloseMenue(value, noSeparator, menu, creator, frame);
                    noSeparator = creationMenu(value, noSeparator, menu, neighborhood, frame, WorkspaceTree.this, translator);
                    noSeparator = removeMenu(value, noSeparator, menu);
                    noSeparator = renameMenu(value, noSeparator, menu, WorkspaceTree.this, path);
                    noSeparator = propertiesMenu(value, noSeparator, menu, neighborhood, frame);
                    noSeparator = tasksMenu(value, noSeparator, menu, neighborhood, creator, frame);
                    menu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
                }
            }

            @Override
            public void mouseExited(final MouseEvent arg0) {
            }

            @Override
            public void mouseEntered(final MouseEvent arg0) {
            }

            @Override
            public void mouseClicked(final MouseEvent arg0) {
            }
        });
    }

    public WorkspaceTreeModelTranslator getTranslator() {
        return translator;
    }

    @Override
    public boolean isPathEditable(final TreePath path) {
        final Object value = translator.getValue((DefaultMutableTreeNode) path.getLastPathComponent());
        return value instanceof IContainerElement || value instanceof ILibrary || value instanceof IInstance || value instanceof IPort;
    }

    private static void addNewPublicPortReferenceMenu(final JPopupMenu menu, final IPublicPort publicPort) {
        final JMenu subMenu = new JMenu("Connect With");
        menu.add(subMenu);
        final IComposition composition = publicPort.getRoot();
        if (publicPort instanceof IPublicFacetPort) for (final IInstance instance : composition.getSortedInstances()) if (instance.getFragmentReference().isResolved()) for (final IFacetPort facet : instance.getFragment().getSortedFacets()) if (facet.getType().equals(publicPort.getType()) && !publicPort.isReferencedBy(instance, facet)) subMenu.add(new Actions.NewPublicFacetPortReferenceAction((IPublicFacetPort) publicPort, instance, facet));
        if (publicPort instanceof IPublicReceptaclePort) for (final IInstance instance : composition.getSortedInstances()) if (instance.getFragmentReference().isResolved()) for (final IReceptaclePort receptacle : instance.getFragment().getSortedReceptacles()) if (receptacle.getType().equals(publicPort.getType()) && !publicPort.isReferencedBy(instance, receptacle)) subMenu.add(new Actions.NewPublicReceptaclePortReferenceAction((IPublicReceptaclePort) publicPort, instance, receptacle));
    }

    private static void addNewConnectionMenu(final JPopupMenu menu, final IComposition composition) {
        final JMenu subMenu = new JMenu("New Connection");
        menu.add(subMenu);
        for (final IInstance facetInstance : composition.getSortedInstances()) if (facetInstance.getFragmentReference().isResolved()) for (final IFacetPort facet : facetInstance.getFragment().getSortedFacets()) for (final IInstance receptacleInstance : composition.getSortedInstances()) if (receptacleInstance.getFragmentReference().isResolved()) for (final IReceptaclePort receptacle : receptacleInstance.getFragment().getSortedReceptacles()) if (facet.getType().equals(receptacle.getType()) && !composition.isConnected(facetInstance, facet, receptacleInstance, receptacle)) subMenu.add(new Actions.NewConnectionAction(composition, facetInstance, facet, receptacleInstance, receptacle));
    }

    private static enum FragmentKind {

        COMPONENT, COMPOSITION
    }

    private static int addNewInstanceMenu(final JFrame frame, final JMenu menu, final IComposition composition, final IContainer container, final FragmentKind kind) {
        int counter = 0;
        final List<JMenu> menus = new Vector<JMenu>();
        for (final IFolder folder : container.getSortedFolders()) {
            final JMenu subMenu = new JMenu(folder.getName());
            subMenu.setIcon(IconSet.FOLDER.getIcon());
            final int size = addNewInstanceMenu(frame, subMenu, composition, folder, kind);
            if (size > 0) {
                menus.add(subMenu);
                counter++;
            }
        }
        switch(kind) {
            case COMPONENT:
                if (container.getComponents().size() == 0 && menus.size() == 1) {
                    for (final IFolder folder : container.getSortedFolders()) addNewInstanceMenu(frame, menu, composition, folder, kind);
                } else {
                    for (final JMenu menuX : menus) menu.add(menuX);
                    for (final IComponent component : container.getSortedComponents()) {
                        menu.add(new Actions.NewInstanceAction(frame, composition, component, IconSet.COMPONENT_ADD.getIcon()));
                        counter++;
                    }
                }
                break;
            case COMPOSITION:
                if (container.getCompositions().size() == 0 && menus.size() == 1) {
                    for (final IFolder folder : container.getSortedFolders()) addNewInstanceMenu(frame, menu, composition, folder, kind);
                } else {
                    for (final JMenu menuX : menus) menu.add(menuX);
                    for (final IComposition compostion : container.getSortedCompositions()) {
                        menu.add(new Actions.NewInstanceAction(frame, composition, compostion, IconSet.COMPOSITION_ADD.getIcon()));
                        counter++;
                    }
                }
                break;
            default:
                break;
        }
        return counter;
    }

    private static void addNewComponentInstanceMenu(final JFrame frame, final JPopupMenu menu, final IComposition composition) {
        final JMenu subMenu = new JMenu("New Component Instance");
        menu.add(subMenu);
        addNewInstanceMenu(frame, subMenu, composition, composition.getWorkspace(), FragmentKind.COMPONENT);
    }

    private static void addNewCompositionInstanceMenu(final JFrame frame, final JPopupMenu menu, final IComposition composition) {
        final JMenu subMenu = new JMenu("New Composition Instance");
        menu.add(subMenu);
        addNewInstanceMenu(frame, subMenu, composition, composition.getWorkspace(), FragmentKind.COMPOSITION);
    }

    private static int addNewFragmentReferenceMenu(final JMenu menu, final IFragmentReferenceHolder holder, final IContainer container, final FragmentKind kind) {
        int counter = 0;
        final List<JMenu> menus = new Vector<JMenu>();
        for (final IFolder folder : container.getSortedFolders()) {
            final JMenu subMenu = new JMenu(folder.getName());
            subMenu.setIcon(IconSet.FOLDER.getIcon());
            final int size = addNewFragmentReferenceMenu(subMenu, holder, folder, kind);
            if (size > 0) {
                menus.add(subMenu);
                counter++;
            }
        }
        switch(kind) {
            case COMPONENT:
                if (container.getCompositions().size() == 0 && menus.size() == 1) {
                    for (final IFolder folder : container.getSortedFolders()) addNewFragmentReferenceMenu(menu, holder, folder, kind);
                } else {
                    for (final JMenu menuX : menus) menu.add(menuX);
                    for (final IComponent component : container.getSortedComponents()) {
                        menu.add(new Actions.NewFragmentReferenceAction(holder, component, IconSet.COMPONENT.getIcon()));
                        counter++;
                    }
                }
                break;
            case COMPOSITION:
                if (container.getCompositions().size() == 0 && menus.size() == 1) {
                    for (final IFolder folder : container.getSortedFolders()) addNewFragmentReferenceMenu(menu, holder, folder, kind);
                } else {
                    for (final JMenu menuX : menus) menu.add(menuX);
                    for (final IComposition compostion : container.getSortedCompositions()) {
                        menu.add(new Actions.NewFragmentReferenceAction(holder, compostion, IconSet.COMPOSITION.getIcon()));
                        counter++;
                    }
                }
                break;
            default:
                break;
        }
        return counter;
    }

    private static void addNewComponentReferenceMenu(final JPopupMenu menu, final IFragmentReferenceHolder holder) {
        final JMenu subMenu = new JMenu(holder instanceof ILibrary ? "Place Comopnent" : "Add Component Reference");
        menu.add(subMenu);
        addNewFragmentReferenceMenu(subMenu, holder, holder.getRoot().getWorkspace(), FragmentKind.COMPONENT);
    }

    private static void addNewCompositionReferenceMenu(final JPopupMenu menu, final IFragmentReferenceHolder holder) {
        final JMenu subMenu = new JMenu(holder instanceof ILibrary ? "Place Composition" : "Add Composition Reference");
        menu.add(subMenu);
        addNewFragmentReferenceMenu(subMenu, holder, holder.getRoot().getWorkspace(), FragmentKind.COMPOSITION);
    }

    private static void addNewLibraryReferenceMenu(final JPopupMenu menu, final ILibrary library) {
        final JMenu subMenu = new JMenu("Add Library Dependency");
        menu.add(subMenu);
        for (final ILibrary refLibrary : library.getRoot().getLibraries()) subMenu.add(new Actions.NewLibraryReferenceAction(library, refLibrary));
    }
}
