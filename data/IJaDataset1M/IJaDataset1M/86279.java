package com.ssd.mdaworks.classdiagram.classDiagram.diagram.part;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;

/**
 * @generated
 */
public class ClassdiagramPaletteFactory {

    /**
	 * @generated
	 */
    public void fillPalette(PaletteRoot paletteRoot) {
        paletteRoot.add(createDiagramNodes1Group());
        paletteRoot.add(createChildNodes2Group());
        paletteRoot.add(createLinks3Group());
    }

    /**
	 * Creates "Diagram Nodes" palette tool group
	 * @generated
	 */
    private PaletteContainer createDiagramNodes1Group() {
        PaletteGroup paletteContainer = new PaletteGroup(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.DiagramNodes1Group_title);
        paletteContainer.add(createMClass1CreationTool());
        paletteContainer.add(createMPackage2CreationTool());
        paletteContainer.add(createEAnnotation3CreationTool());
        return paletteContainer;
    }

    /**
	 * Creates "Child Nodes" palette tool group
	 * @generated
	 */
    private PaletteContainer createChildNodes2Group() {
        PaletteGroup paletteContainer = new PaletteGroup(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.ChildNodes2Group_title);
        paletteContainer.setDescription(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.ChildNodes2Group_desc);
        paletteContainer.add(createMAttribute1CreationTool());
        paletteContainer.add(createMOperation2CreationTool());
        paletteContainer.add(createCreateAnnotationdetails3CreationTool());
        return paletteContainer;
    }

    /**
	 * Creates "Links" palette tool group
	 * @generated
	 */
    private PaletteContainer createLinks3Group() {
        PaletteGroup paletteContainer = new PaletteGroup(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.Links3Group_title);
        paletteContainer.setDescription(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.Links3Group_desc);
        paletteContainer.add(createMAnnotationreference1CreationTool());
        paletteContainer.add(createAssociation2CreationTool());
        paletteContainer.add(createAggregation3CreationTool());
        paletteContainer.add(createGeneralization4CreationTool());
        return paletteContainer;
    }

    /**
	 * @generated
	 */
    private ToolEntry createMClass1CreationTool() {
        List types = new ArrayList(2);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MClass_2001);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MClass_3004);
        NodeToolEntry entry = new NodeToolEntry(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.MClass1CreationTool_title, com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.MClass1CreationTool_desc, types);
        entry.setSmallIcon(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.getImageDescriptor(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MClass_2001));
        entry.setLargeIcon(entry.getSmallIcon());
        return entry;
    }

    /**
	 * @generated
	 */
    private ToolEntry createMPackage2CreationTool() {
        List types = new ArrayList(2);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MPackage_2002);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MPackage_3005);
        NodeToolEntry entry = new NodeToolEntry(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.MPackage2CreationTool_title, com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.MPackage2CreationTool_desc, types);
        entry.setSmallIcon(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.getImageDescriptor(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MPackage_2002));
        entry.setLargeIcon(entry.getSmallIcon());
        return entry;
    }

    /**
	 * @generated
	 */
    private ToolEntry createEAnnotation3CreationTool() {
        List types = new ArrayList(2);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MAnnotation_3003);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MAnnotation_2003);
        NodeToolEntry entry = new NodeToolEntry(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.EAnnotation3CreationTool_title, com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.EAnnotation3CreationTool_desc, types);
        entry.setSmallIcon(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.getImageDescriptor(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MAnnotation_3003));
        entry.setLargeIcon(entry.getSmallIcon());
        return entry;
    }

    /**
	 * @generated
	 */
    private ToolEntry createMAttribute1CreationTool() {
        List types = new ArrayList(1);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MAttribute_3001);
        NodeToolEntry entry = new NodeToolEntry(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.MAttribute1CreationTool_title, com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.MAttribute1CreationTool_desc, types);
        entry.setSmallIcon(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.getImageDescriptor(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MAttribute_3001));
        entry.setLargeIcon(entry.getSmallIcon());
        return entry;
    }

    /**
	 * @generated
	 */
    private ToolEntry createMOperation2CreationTool() {
        List types = new ArrayList(1);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MOperation_3002);
        NodeToolEntry entry = new NodeToolEntry(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.MOperation2CreationTool_title, com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.MOperation2CreationTool_desc, types);
        entry.setSmallIcon(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.getImageDescriptor(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MOperation_3002));
        entry.setLargeIcon(entry.getSmallIcon());
        return entry;
    }

    /**
	 * @generated
	 */
    private ToolEntry createCreateAnnotationdetails3CreationTool() {
        List types = new ArrayList(1);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MStringToStringMapEntry_3006);
        NodeToolEntry entry = new NodeToolEntry(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.CreateAnnotationdetails3CreationTool_title, com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.CreateAnnotationdetails3CreationTool_desc, types);
        entry.setSmallIcon(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.getImageDescriptor(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MStringToStringMapEntry_3006));
        entry.setLargeIcon(entry.getSmallIcon());
        return entry;
    }

    /**
	 * @generated
	 */
    private ToolEntry createMAnnotationreference1CreationTool() {
        List types = new ArrayList(1);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MAnnotationReferences_4004);
        LinkToolEntry entry = new LinkToolEntry(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.MAnnotationreference1CreationTool_title, com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.MAnnotationreference1CreationTool_desc, types);
        entry.setSmallIcon(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.getImageDescriptor(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MAnnotationReferences_4004));
        entry.setLargeIcon(entry.getSmallIcon());
        return entry;
    }

    /**
	 * @generated
	 */
    private ToolEntry createAssociation2CreationTool() {
        List types = new ArrayList(1);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MReference_4001);
        LinkToolEntry entry = new LinkToolEntry(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.Association2CreationTool_title, com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.Association2CreationTool_desc, types);
        entry.setSmallIcon(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.getImageDescriptor(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MReference_4001));
        entry.setLargeIcon(entry.getSmallIcon());
        return entry;
    }

    /**
	 * @generated
	 */
    private ToolEntry createAggregation3CreationTool() {
        List types = new ArrayList(1);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MReference_4002);
        LinkToolEntry entry = new LinkToolEntry(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.Aggregation3CreationTool_title, com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.Aggregation3CreationTool_desc, types);
        entry.setSmallIcon(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.getImageDescriptor(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MReference_4002));
        entry.setLargeIcon(entry.getSmallIcon());
        return entry;
    }

    /**
	 * @generated
	 */
    private ToolEntry createGeneralization4CreationTool() {
        List types = new ArrayList(1);
        types.add(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MClassMSuperTypes_4003);
        LinkToolEntry entry = new LinkToolEntry(com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.Generalization4CreationTool_title, com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.Messages.Generalization4CreationTool_desc, types);
        entry.setSmallIcon(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.getImageDescriptor(com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes.MClassMSuperTypes_4003));
        entry.setLargeIcon(entry.getSmallIcon());
        return entry;
    }

    /**
	 * @generated
	 */
    private static class NodeToolEntry extends ToolEntry {

        /**
		 * @generated
		 */
        private final List elementTypes;

        /**
		 * @generated
		 */
        private NodeToolEntry(String title, String description, List elementTypes) {
            super(title, description, null, null);
            this.elementTypes = elementTypes;
        }

        /**
		 * @generated
		 */
        public Tool createTool() {
            Tool tool = new UnspecifiedTypeCreationTool(elementTypes);
            tool.setProperties(getToolProperties());
            return tool;
        }
    }

    /**
	 * @generated
	 */
    private static class LinkToolEntry extends ToolEntry {

        /**
		 * @generated
		 */
        private final List relationshipTypes;

        /**
		 * @generated
		 */
        private LinkToolEntry(String title, String description, List relationshipTypes) {
            super(title, description, null, null);
            this.relationshipTypes = relationshipTypes;
        }

        /**
		 * @generated
		 */
        public Tool createTool() {
            Tool tool = new UnspecifiedTypeConnectionTool(relationshipTypes);
            tool.setProperties(getToolProperties());
            return tool;
        }
    }
}
