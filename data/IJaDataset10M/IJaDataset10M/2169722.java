package com.ecmdeveloper.plugin.diagrams.editors;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import com.ecmdeveloper.plugin.diagrams.model.ClassDiagramNote;
import com.ecmdeveloper.plugin.diagrams.util.IconFiles;
import com.ecmdeveloper.plugin.diagrams.Activator;

/**
 * 
 * @author Ricardo.Belfor
 *
 */
public class ClassDiagramEditorPaletteFactory {

    public static PaletteRoot createPalette() {
        PaletteRoot palette = new PaletteRoot();
        palette.add(createControlGroup(palette));
        palette.add(createComponentsDrawer());
        return palette;
    }

    private static PaletteContainer createControlGroup(PaletteRoot root) {
        PaletteGroup controlGroup = new PaletteGroup("Controls");
        ToolEntry tool = new PanningSelectionToolEntry();
        controlGroup.add(tool);
        root.setDefaultEntry(tool);
        controlGroup.add(new MarqueeToolEntry());
        return controlGroup;
    }

    private static PaletteContainer createComponentsDrawer() {
        PaletteDrawer componentsDrawer = new PaletteDrawer("Diagram Elements");
        ImageDescriptor noteImageSmall = Activator.getImageDescriptor(IconFiles.NOTE);
        ImageDescriptor noteImageLarge = Activator.getImageDescriptor(IconFiles.NOTE_LARGE);
        CombinedTemplateCreationEntry noteCreationEntry = new CombinedTemplateCreationEntry("Diagram Note", "Note", new SimpleFactory(ClassDiagramNote.class), noteImageSmall, noteImageLarge);
        componentsDrawer.add(noteCreationEntry);
        return componentsDrawer;
    }
}
