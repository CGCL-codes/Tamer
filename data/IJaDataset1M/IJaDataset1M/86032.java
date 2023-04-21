package edu.gsbme.gyoza2d.GraphGenerator.Layout.FML;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.jgraph.graph.DefaultGraphCell;
import edu.gsbme.MMLParser2.FML.Search.FMLSearch.FMLSource;
import edu.gsbme.MMLParser2.FML.VirtualTree.cTreeNodes;
import edu.gsbme.MMLParser2.Vocabulary.FML;
import edu.gsbme.gyoza2d.GraphGenerator.BasicDrawer;
import edu.gsbme.gyoza2d.GraphGenerator.GraphGenerator;
import edu.gsbme.gyoza2d.GraphGenerator.LayoutStructure;
import edu.gsbme.gyoza2d.GraphGenerator.Layout.ILayoutAlgorithm;
import edu.gsbme.gyoza2d.visual.FMLVertex;
import edu.gsbme.gyoza2d.visual.HDF5Vertex;
import edu.gsbme.gyoza2d.visual.FML.MeshControl;
import edu.gsbme.gyoza2d.visual.FML.MeshDomainControl;
import edu.gsbme.gyoza2d.visual.FML.MeshTopologyControl;
import edu.gsbme.menuactiondelegate.MenuActionDelegate;

/**
 * Takes in a VirtualFMLTree and construct a mesh layout that can span across HDF5 or
 * XML Format
 * @author David
 *
 */
public class MeshLayout implements ILayoutAlgorithm {

    LayoutStructure structure;

    MenuActionDelegate actionDelegate;

    GraphGenerator graphGenerator;

    boolean editable = true;

    public MeshLayout(LayoutStructure structure, MenuActionDelegate actionDelegate, GraphGenerator graphGenerator) {
        this.structure = structure;
        this.actionDelegate = actionDelegate;
        this.graphGenerator = graphGenerator;
    }

    @Override
    public Menu getLayoutMenu(Composite parent) {
        return null;
    }

    @Override
    public String getLayoutName() {
        return null;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void run(DefaultGraphCell pg, Object parent) {
        if (parent instanceof cTreeNodes) {
            cTreeNodes node = (cTreeNodes) parent;
            if (node.getFML_ID().equals(FML.mesh.toString())) {
                parseMeshElement(pg, node);
            } else if (node.getFML_ID().equals(FML.vertex_list.toString()) || node.getFML_ID().equals(FML.edge_list.toString()) || node.getFML_ID().equals(FML.face_list.toString()) || node.getFML_ID().equals(FML.element_list.toString())) {
                parseMeshTopologyElement(pg, node);
            }
        }
    }

    private void parseMeshElement(DefaultGraphCell pg, cTreeNodes current_node) {
        MeshControl control = new MeshControl(current_node, actionDelegate, graphGenerator);
        DefaultGraphCell vtx = null;
        if (current_node.getSource() == FMLSource.XML) {
            if (current_node.isInterchangeNode()) {
                vtx = new HDF5Vertex(control);
                structure.add(vtx);
            } else {
                vtx = new FMLVertex(control);
                structure.add(vtx);
            }
        } else if (current_node.getSource() == FMLSource.HDF5) {
            vtx = new HDF5Vertex(control);
            structure.add(vtx);
        } else {
        }
        if (pg != null && vtx != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
        cTreeNodes[] children = current_node.getChildren();
        for (int i = 0; i < children.length; i++) {
            parseMeshTopologyElement(vtx, children[i]);
        }
    }

    private void parseMeshTopologyElement(DefaultGraphCell pg, cTreeNodes current_node) {
        MeshTopologyControl control = new MeshTopologyControl(current_node, actionDelegate, graphGenerator);
        DefaultGraphCell vtx = null;
        if (current_node.getSource() == FMLSource.XML) {
            if (current_node.isInterchangeNode()) {
                vtx = new HDF5Vertex(control);
                structure.add(vtx);
            } else {
                vtx = new FMLVertex(control);
                structure.add(vtx);
            }
        } else if (current_node.getSource() == FMLSource.HDF5) {
            vtx = new HDF5Vertex(control);
            structure.add(vtx);
        } else {
        }
        if (pg != null && vtx != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
    }

    private void parseMeshDomainElement(DefaultGraphCell pg, cTreeNodes current_node) {
        MeshDomainControl control = new MeshDomainControl(current_node, actionDelegate, graphGenerator);
        DefaultGraphCell vtx = null;
        if (current_node.getSource() == FMLSource.XML) {
            vtx = new FMLVertex(control);
            structure.add(vtx);
        } else if (current_node.getSource() == FMLSource.HDF5) {
            vtx = new HDF5Vertex(control);
            structure.add(vtx);
        } else {
        }
        if (pg != null && vtx != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
    }

    @Override
    public void dispose() {
        structure = null;
        actionDelegate = null;
        graphGenerator = null;
    }
}
