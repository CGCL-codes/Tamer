package com.objetdirect.gwt.umlapi.client.artifacts;

import com.objetdirect.gwt.umlapi.client.contextMenu.MenuBarAndTitle;
import com.objetdirect.gwt.umlapi.client.gfx.GfxManager;
import com.objetdirect.gwt.umlapi.client.gfx.GfxObject;
import com.objetdirect.gwt.umlapi.client.gfx.GfxStyle;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;

/**
 * This artifact represent a specific link between a note and any uml artifact
 * 
 * @author Florian Mounier (mounier-dot-florian.at.gmail'dot'com)
 */
public class LinkNoteArtifact extends LinkArtifact {

    private transient GfxObject line;

    /**
	 * /!\ Don't forget to increment the serialVersionUID if you change any of the fields above /!\
	 */
    private static final long serialVersionUID = 1L;

    private NoteArtifact note;

    private UMLArtifact target;

    /** Default Constructor ONLY for gwt-rpc serialization. */
    @Deprecated
    @SuppressWarnings("unused")
    private LinkNoteArtifact() {
    }

    public LinkNoteArtifact(final UMLCanvas canvas, int id, final UMLArtifact artifact1, final UMLArtifact artifact2) {
        super(canvas, id, artifact1, artifact2);
        if (artifact1 instanceof NoteArtifact) {
            note = (NoteArtifact) artifact1;
            target = artifact2;
        } else if (artifact2 instanceof NoteArtifact) {
            note = (NoteArtifact) artifact2;
            target = artifact1;
        } else {
            throw new IllegalArgumentException();
        }
        line = null;
        note.addDependency(this, target);
        target.addDependency(this, note);
    }

    @Override
    public void buildGfxObject() {
        leftPoint = note.getCenter();
        rightPoint = target.getCenter();
        line = GfxManager.getPlatform().buildLine(leftPoint, rightPoint);
        line.addToVirtualGroup(gfxObject);
        line.setStroke(ThemeManager.getTheme().getLinkNoteForegroundColor(), 1);
        line.setStrokeStyle(GfxStyle.DASH);
        gfxObject.moveToBack();
    }

    @Override
    public void edit(final GfxObject editedGfxObject) {
    }

    @Override
    public MenuBarAndTitle getRightMenu() {
        final MenuBarAndTitle rightMenu = new MenuBarAndTitle();
        rightMenu.setName("Note link");
        return rightMenu;
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    @Override
    public void removeCreatedDependency() {
        note.removeDependency(this);
        target.removeDependency(this);
    }

    @Override
    public void setCanvas(final UMLCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public String toURL() {
        return "LinkNote$<" + note.getId() + ">!<" + target.getId() + ">";
    }

    @Override
    public void unselect() {
        super.unselect();
        line.setStroke(ThemeManager.getTheme().getLinkNoteForegroundColor(), 1);
    }

    @Override
    protected void select() {
        super.select();
        line.setStroke(ThemeManager.getTheme().getLinkNoteHighlightedForegroundColor(), 2);
    }

    @Override
    public void setUpAfterDeserialization() {
        buildGfxObject();
    }
}
