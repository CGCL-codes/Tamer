package com.c4j.workbench.wtree;

import static java.lang.String.format;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import com.c4j.IFragmentReference;
import com.c4j.assembly.IAssembly;
import com.c4j.assembly.ILibrary;
import com.c4j.assembly.ILibraryReference;
import com.c4j.component.IComponent;
import com.c4j.composition.IComposition;
import com.c4j.composition.IConnection;
import com.c4j.composition.IInstance;
import com.c4j.composition.IPublicPortReference;
import com.c4j.workbench.IconSet;
import com.c4j.workspace.IFacetPort;
import com.c4j.workspace.IFolder;
import com.c4j.workspace.IReceptaclePort;

@SuppressWarnings("serial")
public class WorkspaceTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final Color ERROR_COLOR = new Color(255, 0, 0);

    private final WorkspaceTreeModelTranslator translator;

    public WorkspaceTreeCellRenderer(final WorkspaceTreeModelTranslator translator) {
        this.translator = translator;
    }

    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        final Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        final Object newValue;
        if (value instanceof DefaultMutableTreeNode) {
            newValue = translator.getValue((DefaultMutableTreeNode) value);
            if (newValue instanceof ILibraryReference) {
                final ILibraryReference libraryReference = (ILibraryReference) newValue;
                setText(libraryReference.getReference().getReferenceString());
                if (!libraryReference.getReference().isResolved()) setForeground(ERROR_COLOR);
            } else if (newValue instanceof IFragmentReference) {
                final IFragmentReference reference = (IFragmentReference) newValue;
                if (reference.getFragmentReference().isResolved()) setText(reference.getFragment().getName()); else {
                    setText(reference.getFragmentReference().getReferenceString());
                    setForeground(ERROR_COLOR);
                }
            } else if (newValue instanceof IConnection) {
                final IConnection connection = (IConnection) newValue;
                setText(connection.getFacetReference().getInstanceReference().getReferenceString() + "." + connection.getFacetReference().getPortReference().getReferenceString() + "->" + connection.getReceptacleReference().getInstanceReference().getReferenceString() + "." + connection.getReceptacleReference().getPortReference().getReferenceString());
                if (!connection.getFacetReference().getInstanceReference().isResolved() || !connection.getFacetReference().getPortReference().isResolved() || !connection.getReceptacleReference().getInstanceReference().isResolved() || !connection.getReceptacleReference().getPortReference().isResolved()) setForeground(ERROR_COLOR);
            } else if (newValue instanceof IPublicPortReference) {
                final IPublicPortReference portRef = (IPublicPortReference) newValue;
                setText(portRef.getInstanceReference().getReferenceString() + "." + portRef.getPortReference().getReferenceString());
                if (!portRef.getInstanceReference().isResolved() || !portRef.getPortReference().isResolved()) setForeground(ERROR_COLOR);
            } else if (newValue instanceof IInstance) {
                if (!((IInstance) newValue).getFragmentReference().isResolved()) setForeground(ERROR_COLOR);
            }
        } else newValue = value;
        setIcon(IconSet.getIcon(newValue));
        if (newValue instanceof IFolder) setToolTipText("This is a folder."); else if (newValue instanceof IComponent) setToolTipText("This is a component."); else if (newValue instanceof IComposition) setToolTipText("This is a composition."); else if (newValue instanceof IAssembly) setToolTipText("This is an assembly."); else if (newValue instanceof ILibrary) setToolTipText("This is a library."); else if (newValue instanceof ILibraryReference) setToolTipText(format("This is reference to the library ‘%s’.", ((ILibraryReference) newValue).getReference().getReferenceString())); else if (newValue instanceof IInstance && !((IInstance) newValue).getFragmentReference().isResolved()) setToolTipText(format("This is an instance of the unresolvable fragment ‘%s’.", ((IInstance) newValue).getFragmentReference().getReferenceString())); else if (newValue instanceof IInstance && ((IInstance) newValue).getFragment() instanceof IComponent) setToolTipText(format("This is an instance of the component ‘%s’.", ((IInstance) newValue).getFragmentReference().getReferenceString())); else if (newValue instanceof IInstance && ((IInstance) newValue).getFragment() instanceof IComposition) setToolTipText(format("This is an instance of the composition ‘%s’.", ((IInstance) newValue).getFragmentReference().getReferenceString())); else if (newValue instanceof IFragmentReference && ((IFragmentReference) newValue).getFragment() instanceof IComponent) setToolTipText(format("This is a reference to the component ‘%s’.", ((IFragmentReference) newValue).getFragmentReference().getReferenceString())); else if (newValue instanceof IFragmentReference && ((IFragmentReference) newValue).getFragment() instanceof IComposition) setToolTipText(format("This is a reference to the composition ‘%s’.", ((IFragmentReference) newValue).getFragmentReference().getReferenceString())); else if (newValue instanceof IFacetPort) setToolTipText("This is a facet."); else if (newValue instanceof IReceptaclePort) setToolTipText("This is a receptacle."); else if (newValue instanceof IConnection) setToolTipText("This is a connection."); else if (newValue instanceof IPublicPortReference) setToolTipText("This is a public port reference.");
        return component;
    }
}
