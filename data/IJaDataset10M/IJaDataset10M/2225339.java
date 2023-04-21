package edu.gsbme.gyoza2d.GraphGenerator.Layout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.jgraph.graph.DefaultGraphCell;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import edu.gsbme.MMLParser2.ModelML.ModMLModel;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.MMLParser2.Vocabulary.FML;
import edu.gsbme.MMLParser2.Vocabulary.ModelML;
import edu.gsbme.MMLParser2.XPath.XPathScanner;
import edu.gsbme.gyoza2d.GraphGenerator.BasicDrawer;
import edu.gsbme.gyoza2d.GraphGenerator.GraphGenerator;
import edu.gsbme.gyoza2d.GraphGenerator.LayoutStructure;
import edu.gsbme.gyoza2d.visual.CellMLVertex;
import edu.gsbme.gyoza2d.visual.FMLContainerVertex;
import edu.gsbme.gyoza2d.visual.FMLVertex;
import edu.gsbme.gyoza2d.visual.ImportVertex;
import edu.gsbme.gyoza2d.visual.ModelMLContainerVertex;
import edu.gsbme.gyoza2d.visual.ModelMLVertex;
import edu.gsbme.gyoza2d.visual.Abstract.AbstractModelControl;
import edu.gsbme.gyoza2d.visual.Abstract.AbstractTextControl;
import edu.gsbme.gyoza2d.visual.FML.EdgeControl;
import edu.gsbme.gyoza2d.visual.FML.FaceControl;
import edu.gsbme.gyoza2d.visual.FML.GeometricEntityControl;
import edu.gsbme.gyoza2d.visual.FML.VertexControl;
import edu.gsbme.gyoza2d.visual.Import.DependentVariableControl;
import edu.gsbme.menuactiondelegate.MenuActionDelegate;

/**
 * Generates a abstract view of the MML model. This graph is currently not used.
 * @author David
 *
 */
public class AbstractViewLayout implements ILayoutAlgorithm {

    LayoutStructure structure;

    MenuActionDelegate actionDelegate;

    GraphGenerator graphGenerator;

    boolean editable = true;

    public AbstractViewLayout(LayoutStructure structure, MenuActionDelegate actionDelegate, GraphGenerator graphGenerator) {
        this.structure = structure;
        this.actionDelegate = actionDelegate;
        this.graphGenerator = graphGenerator;
    }

    /**
	 * Root Element of this graph
	 * @param pg
	 * @param parent
	 */
    private void drawModelMLElement(DefaultGraphCell pg, Element parent) {
        AbstractModelControl control = new AbstractModelControl("Model", actionDelegate);
        ModelMLVertex vtx = new ModelMLVertex(control);
        structure.add(vtx);
        if (pg != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
        NodeList geom_entity_list = XPathScanner.getScanner().searchNodeListXPath(parent, "//" + FML.geometric_entity.toString());
        AbstractTextControl geControl = null;
        FMLContainerVertex geVtx = null;
        for (int i = 0; i < geom_entity_list.getLength(); i++) {
            if (i == 0) {
                geControl = new AbstractTextControl("Geometric Entity", actionDelegate);
                geVtx = new FMLContainerVertex(geControl);
                structure.add(geVtx);
                BasicDrawer.connectCell("", vtx, geVtx, structure);
            }
            Element temp = (Element) geom_entity_list.item(i);
            drawGeometryEntity(geVtx, temp);
        }
        NodeList face_list = XPathScanner.getScanner().searchNodeListXPath(parent, "//" + FML.face.toString());
        AbstractTextControl faceControl = null;
        FMLContainerVertex faceVtx = null;
        for (int i = 0; i < face_list.getLength(); i++) {
            if (i == 0) {
                faceControl = new AbstractTextControl("Face", actionDelegate);
                faceVtx = new FMLContainerVertex(faceControl);
                structure.add(faceVtx);
                BasicDrawer.connectCell("", vtx, faceVtx, structure);
            }
            Element temp = (Element) face_list.item(i);
            drawFace(faceVtx, temp);
        }
        NodeList edge_list = XPathScanner.getScanner().searchNodeListXPath(parent, "//" + FML.edge.toString());
        AbstractTextControl edgControl = null;
        FMLContainerVertex edgVtx = null;
        for (int i = 0; i < edge_list.getLength(); i++) {
            if (i == 0) {
                edgControl = new AbstractTextControl("Edge", actionDelegate);
                edgVtx = new FMLContainerVertex(edgControl);
                structure.add(edgVtx);
                BasicDrawer.connectCell("", vtx, edgVtx, structure);
            }
            Element temp = (Element) edge_list.item(i);
            drawEdge(edgVtx, temp);
        }
        NodeList vertex_list = XPathScanner.getScanner().searchNodeListXPath(parent, "//" + FML.vertex.toString());
        AbstractTextControl vtxControl = null;
        FMLContainerVertex vtxVtx = null;
        for (int i = 0; i < vertex_list.getLength(); i++) {
            if (i == 0) {
                vtxControl = new AbstractTextControl("Edge", actionDelegate);
                vtxVtx = new FMLContainerVertex(vtxControl);
                structure.add(vtxVtx);
                BasicDrawer.connectCell("", vtx, vtxVtx, structure);
            }
            Element temp = (Element) vertex_list.item(i);
            drawVertex(vtxVtx, temp);
        }
    }

    private void drawGeometryEntity(DefaultGraphCell pg, Element parent) {
        GeometricEntityControl control = new GeometricEntityControl(parent, actionDelegate, graphGenerator);
        FMLVertex vtx = new FMLVertex(control);
        structure.add(vtx);
        if (pg != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
        Element parent_group = XPathScanner.getScanner().searchElementXPath(parent, "parent::*/parent::*");
        if (parent_group != null) {
            NodeList system_ref_list = XPathScanner.getScanner().searchNodeListXPath(parent_group, ModelML.physics_property.toString() + "/*[@" + Attributes.system_ref.toString() + "]");
            for (int i = 0; i < system_ref_list.getLength(); i++) {
                Element temp = (Element) system_ref_list.item(i);
                drawSystemRef(vtx, temp);
            }
        } else {
            System.out.println("Error : Group not found @ AbstractViewLayout.java");
        }
    }

    private void drawFace(DefaultGraphCell pg, Element parent) {
        FaceControl control = new FaceControl(parent, actionDelegate, graphGenerator);
        FMLVertex vtx = new FMLVertex(control);
        structure.add(vtx);
        if (pg != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
        Element parent_group = XPathScanner.getScanner().searchElementXPath(parent, "parent::*/parent::*");
        if (parent_group != null) {
            NodeList system_ref_list = XPathScanner.getScanner().searchNodeListXPath(parent_group, ModelML.physics_property.toString() + "/*[@" + Attributes.system_ref.toString() + "]");
            for (int i = 0; i < system_ref_list.getLength(); i++) {
                Element temp = (Element) system_ref_list.item(i);
                drawSystemRef(vtx, temp);
            }
        } else {
            System.out.println("Error : Group not found @ AbstractViewLayout.java");
        }
    }

    private void drawEdge(DefaultGraphCell pg, Element parent) {
        EdgeControl control = new EdgeControl(parent, actionDelegate, graphGenerator);
        FMLVertex vtx = new FMLVertex(control);
        structure.add(vtx);
        if (pg != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
        Element parent_group = XPathScanner.getScanner().searchElementXPath(parent, "parent::*/parent::*");
        if (parent_group != null) {
            NodeList system_ref_list = XPathScanner.getScanner().searchNodeListXPath(parent_group, ModelML.physics_property.toString() + "/*[@" + Attributes.system_ref.toString() + "]");
            for (int i = 0; i < system_ref_list.getLength(); i++) {
                Element temp = (Element) system_ref_list.item(i);
                drawSystemRef(vtx, temp);
            }
        } else {
            System.out.println("Error : Group not found @ AbstractViewLayout.java");
        }
    }

    private void drawVertex(DefaultGraphCell pg, Element parent) {
        VertexControl control = new VertexControl(parent, actionDelegate, graphGenerator);
        FMLVertex vtx = new FMLVertex(control);
        structure.add(vtx);
        if (pg != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
        Element parent_group = XPathScanner.getScanner().searchElementXPath(parent, "parent::*/parent::*");
        if (parent_group != null) {
            NodeList system_ref_list = XPathScanner.getScanner().searchNodeListXPath(parent_group, ModelML.physics_property.toString() + "/*[@" + Attributes.system_ref.toString() + "]");
            for (int i = 0; i < system_ref_list.getLength(); i++) {
                Element temp = (Element) system_ref_list.item(i);
                drawSystemRef(vtx, temp);
            }
        } else {
            System.out.println("Error : Group not found @ AbstractViewLayout.java");
        }
    }

    /**
	 * Draw A System Ref vertex and append the relevant mathamatical information as a child
	 * @param pg
	 * @param parent
	 */
    private void drawSystemRef(DefaultGraphCell pg, Element parent) {
        AbstractTextControl control = new AbstractTextControl("System : " + parent.getAttribute(Attributes.system_ref.toString()), actionDelegate);
        ModelMLContainerVertex vtx = new ModelMLContainerVertex(control);
        structure.add(vtx);
        if (parent.getTagName().equals(ModelML.system_mapping.toString())) {
            drawSystemMappingRepresentation(vtx, parent);
        } else if (parent.getTagName().equals(ModelML.import_property.toString())) {
            drawImportPropertyRepresentation(vtx, parent);
        } else {
            System.out.println("Error : tag not recognized @ AbstractViewLayout.drawSystemRef() " + parent);
        }
        if (pg != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
    }

    private void drawSystemMappingRepresentation(DefaultGraphCell pg, Element parent) {
        NodeList variable_list = parent.getElementsByTagName(ModelML.state_variable.toString());
        if (variable_list.getLength() == 0) {
            AbstractTextControl control = new AbstractTextControl("All State Variables", actionDelegate);
            ModelMLContainerVertex vtx = new ModelMLContainerVertex(control);
            structure.add(vtx);
            if (pg != null) {
                BasicDrawer.connectCell("", pg, vtx, structure);
            }
        }
        for (int i = 0; i < variable_list.getLength(); i++) {
            Element temp = (Element) variable_list.item(i);
            AbstractTextControl control = new AbstractTextControl(temp.getAttribute(Attributes.ref.toString()), actionDelegate);
            ModelMLContainerVertex vtx = new ModelMLContainerVertex(control);
            structure.add(vtx);
            if (pg != null) {
                BasicDrawer.connectCell("", pg, vtx, structure);
            }
        }
        Element condition = ModMLModel.returnConditionsTag(ModMLModel.returnVariableMappingTag(parent));
        if (condition != null) {
            NodeList condition_list = XPathScanner.getScanner().searchNodeListXPath(condition, "*");
            for (int i = 0; i < condition_list.getLength(); i++) {
                Element temp = (Element) condition_list.item(i);
                AbstractTextControl control = new AbstractTextControl(temp.getTagName() + " : " + temp.getAttribute(Attributes.ref.toString()), actionDelegate);
                ModelMLContainerVertex vtx = new ModelMLContainerVertex(control);
                structure.add(vtx);
                if (pg != null) {
                    BasicDrawer.connectCell("", pg, vtx, structure);
                }
            }
        }
    }

    /**
	 * TODO room for improvement, Need to draw ALL DE including modification, hide the specification related structure (i.e. Layer)
	 * @param pg
	 * @param parent
	 */
    private void drawImportPropertyRepresentation(DefaultGraphCell pg, Element parent) {
        AbstractTextControl control = new AbstractTextControl(parent.getAttribute(Attributes.import_ref.toString()), actionDelegate);
        CellMLVertex vtx = new CellMLVertex(control);
        structure.add(vtx);
        NodeList layer_list = ModMLModel.returnLayerList(parent);
        for (int i = 0; i < layer_list.getLength(); i++) {
            Element temp = (Element) layer_list.item(i);
            Element eq_id = ModMLModel.returnImportEquation(temp);
            control = new AbstractTextControl("Layer : " + temp.getAttribute(Attributes.name.toString()), actionDelegate);
            ModelMLVertex vtxLayer = new ModelMLVertex(control);
            BasicDrawer.connectCell("", vtx, vtxLayer, structure);
            structure.add(vtxLayer);
            control = new AbstractTextControl("Modification Equation: " + eq_id.getAttribute(Attributes.ref.toString()), actionDelegate);
            ModelMLVertex vtxEq = new ModelMLVertex(control);
            BasicDrawer.connectCell("", vtxLayer, vtxEq, structure);
            structure.add(vtxEq);
        }
        if (pg != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
    }

    private void drawImportModel(DefaultGraphCell pg, Element parent) {
        DependentVariableControl control = new DependentVariableControl(parent, actionDelegate, graphGenerator);
        ImportVertex vtx = new ImportVertex(control);
        structure.add(vtx);
        if (pg != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
    }

    private void drawDEList(DefaultGraphCell pg, Element parent) {
        DependentVariableControl control = new DependentVariableControl(parent, actionDelegate, graphGenerator);
        ImportVertex vtx = new ImportVertex(control);
        structure.add(vtx);
        if (pg != null) {
            BasicDrawer.connectCell("", pg, vtx, structure);
        }
    }

    @Override
    public Menu getLayoutMenu(Composite parent) {
        return null;
    }

    @Override
    public void run(DefaultGraphCell pg, Object parent) {
        Element tag = (Element) parent;
        if (tag.getTagName().equals(ModelML.modelml.toString())) {
            drawModelMLElement(null, tag);
        } else {
            System.out.println("AbnstractViewLayout : Tag Not yet SUpported : " + parent);
        }
    }

    @Override
    public String getLayoutName() {
        return "Abstract View Layout";
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void dispose() {
        structure = null;
        actionDelegate = null;
        graphGenerator = null;
    }
}
