package org.isistan.flabot.edit.ucmeditor;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.isistan.flabot.FlabotPlugin;
import org.isistan.flabot.coremodel.ComponentRole;
import org.isistan.flabot.coremodel.CoremodelFactory;
import org.isistan.flabot.coremodel.Note;
import org.isistan.flabot.coremodel.PathNode;
import org.isistan.flabot.edit.editor.editparts.NoteConnectionEditPart;
import org.isistan.flabot.edit.editormodel.EditormodelFactory;
import org.isistan.flabot.edit.editormodel.NodeVisualModel;
import org.isistan.flabot.edit.editormodel.VisualModel;
import org.isistan.flabot.messages.Messages;

final class UCMEditorPaletteFactory {

    private UCMEditorPaletteFactory() {
    }

    /** Preference ID used to persist the palette location. */
    private static final String PALETTE_DOCK_LOCATION = "UCMEditorPaletteFactory.Location";

    /** Preference ID used to persist the palette size. */
    private static final String PALETTE_SIZE = "UCMEditorPaletteFactory.Size";

    /** Preference ID used to persist the flyout palette's state. */
    private static final String PALETTE_STATE = "UCMEditorPaletteFactory.State";

    /** Create the "UCM" drawer. */
    private static PaletteContainer createModelsDrawer() {
        PaletteDrawer componentsDrawer = new PaletteDrawer(Messages.getString("org.isistan.flabot.edit.ucmeditor.UCMEditorPaletteFactory.paletteName"));
        ToolEntry tool = new CreationToolEntry(Messages.getString("org.isistan.flabot.edit.ucmeditor.UCMEditorPaletteFactory.componentItem"), Messages.getString("org.isistan.flabot.edit.ucmeditor.UCMEditorPaletteFactory.componentDescription"), new CreationFactory() {

            public Object getNewObject() {
                NodeVisualModel visual = EditormodelFactory.eINSTANCE.createNodeVisualModel();
                visual.setSemanticModel(CoremodelFactory.eINSTANCE.createComponentRole());
                return visual;
            }

            public Object getObjectType() {
                return ComponentRole.class;
            }
        }, ImageDescriptor.createFromFile(FlabotPlugin.class, "icons/role.gif"), ImageDescriptor.createFromFile(FlabotPlugin.class, "icons/role.gif"));
        tool.setToolClass(ComponentCreationTool.class);
        componentsDrawer.add(tool);
        tool = new CreationToolEntry(Messages.getString("org.isistan.flabot.edit.ucmeditor.UCMEditorPaletteFactory.pathNodeItem"), Messages.getString("org.isistan.flabot.edit.ucmeditor.UCMEditorPaletteFactory.pathNodeDescription"), new CreationFactory() {

            public Object getNewObject() {
                NodeVisualModel visual = EditormodelFactory.eINSTANCE.createNodeVisualModel();
                return visual;
            }

            public Object getObjectType() {
                return PathNode.class;
            }
        }, ImageDescriptor.createFromFile(FlabotPlugin.class, "icons/path24x24.gif"), ImageDescriptor.createFromFile(FlabotPlugin.class, "icons/path24x24.gif"));
        tool.setToolProperty(CreationTool.PROPERTY_UNLOAD_WHEN_FINISHED, false);
        tool.setToolClass(PathCreationTool.class);
        componentsDrawer.add(tool);
        componentsDrawer.add(new PaletteSeparator());
        tool = new CombinedTemplateCreationEntry(Messages.getString("org.isistan.flabot.edit.ucmeditor.UCMEditorPaletteFactory.noteItem"), Messages.getString("org.isistan.flabot.edit.ucmeditor.UCMEditorPaletteFactory.noteDescription"), VisualModel.class, new CreationFactory() {

            public Object getNewObject() {
                NodeVisualModel visual = EditormodelFactory.eINSTANCE.createNodeVisualModel();
                Note note = CoremodelFactory.eINSTANCE.createNote();
                visual.setSemanticModel(note);
                return visual;
            }

            public Object getObjectType() {
                return Note.class;
            }
        }, ImageDescriptor.createFromFile(FlabotPlugin.class, "icons/note.gif"), ImageDescriptor.createFromFile(FlabotPlugin.class, "icons/note.gif"));
        componentsDrawer.add(tool);
        tool = new ConnectionCreationToolEntry(Messages.getString("org.isistan.flabot.edit.ucmeditor.UCMEditorPaletteFactory.noteConnectionItem"), Messages.getString("org.isistan.flabot.edit.ucmeditor.UCMEditorPaletteFactory.noteConnectionDescription"), new CreationFactory() {

            public Object getNewObject() {
                return EditormodelFactory.eINSTANCE.createConnectionVisualModel();
            }

            public Object getObjectType() {
                return NoteConnectionEditPart.NOTE_CONNECTION;
            }
        }, ImageDescriptor.createFromFile(FlabotPlugin.class, "icons/note_link.gif"), ImageDescriptor.createFromFile(FlabotPlugin.class, "icons/note_link.gif"));
        componentsDrawer.add(tool);
        return componentsDrawer;
    }

    /**
	 * Creates the PaletteRoot and adds all palette elements.
	 * Use this factory method to create a new palette for your graphical editor.
	 * @return a new PaletteRoot
	 */
    static PaletteRoot createPalette() {
        PaletteRoot palette = new PaletteRoot();
        palette.add(createToolsGroup(palette));
        palette.add(createModelsDrawer());
        return palette;
    }

    /**
	 * Return a FlyoutPreferences instance used to save/load the preferences of a flyout palette.
	 */
    static FlyoutPreferences createPalettePreferences() {
        return new FlyoutPreferences() {

            private IPreferenceStore getPreferenceStore() {
                return FlabotPlugin.getDefault().getPreferenceStore();
            }

            public int getDockLocation() {
                return getPreferenceStore().getInt(PALETTE_DOCK_LOCATION);
            }

            public int getPaletteState() {
                return getPreferenceStore().getInt(PALETTE_STATE);
            }

            public int getPaletteWidth() {
                return getPreferenceStore().getInt(PALETTE_SIZE);
            }

            public void setDockLocation(int location) {
                getPreferenceStore().setValue(PALETTE_DOCK_LOCATION, location);
            }

            public void setPaletteState(int state) {
                getPreferenceStore().setValue(PALETTE_STATE, state);
            }

            public void setPaletteWidth(int width) {
                getPreferenceStore().setValue(PALETTE_SIZE, width);
            }
        };
    }

    /** Create the "Tools" group. */
    private static PaletteContainer createToolsGroup(PaletteRoot palette) {
        PaletteGroup toolGroup = new PaletteGroup(Messages.getString("org.isistan.flabot.edit.ucmeditor.UCMEditorPaletteFactory.paletteGroupName"));
        ToolEntry tool = new PanningSelectionToolEntry();
        toolGroup.add(tool);
        palette.setDefaultEntry(tool);
        toolGroup.add(new MarqueeToolEntry());
        toolGroup.add(new PaletteSeparator());
        return toolGroup;
    }
}
