package com.objetdirect.gwt.umlapi.client.artifacts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.allen_sauer.gwt.log.client.Log;
import com.objetdirect.gwt.umlapi.client.artifacts.clazz.ClassPartAttributesArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.clazz.ClassPartMethodsArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.clazz.ClassPartNameArtifact;
import com.objetdirect.gwt.umlapi.client.engine.Point;
import com.objetdirect.gwt.umlapi.client.gfx.GfxManager;
import com.objetdirect.gwt.umlapi.client.gfx.GfxObject;
import com.objetdirect.gwt.umlapi.client.helpers.GWTUMLDrawerHelper;
import com.objetdirect.gwt.umlapi.client.helpers.QualityLevel;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;

/**
 * This class is an artifact used to represent a class. <br>
 * A class is divided in three {@link NodePartArtifact} :
 * <ul>
 * <li>{@link ClassPartNameArtifact} For the name and stereotype part</li>
 * <li>{@link ClassPartAttributesArtifact} For the attribute list part</li>
 * <li>{@link ClassPartMethodsArtifact} For the method list part</li>
 * </ul>
 * 
 * @author Florian Mounier (mounier-dot-florian.at.gmail'dot'com)
 */
public abstract class NodeArtifact extends BoxArtifact {

    /**
	 * /!\ Don't forget to increment the serialVersionUID if you change any of the fields above /!\
	 */
    private static final long serialVersionUID = 1L;

    protected LinkedList<NodePartArtifact> nodeParts;

    private int width;

    /** Default constructor ONLY for gwt-rpc serialization. */
    @Deprecated
    protected NodeArtifact() {
    }

    /**
	 * Constructor of NodeArtifact
	 * 
	 * @param canvas
	 *            Where the gfxObjects are displayed
	 * @param id
	 *            The artifacts's id
	 */
    public NodeArtifact(final UMLCanvas canvas, int id) {
        super(canvas, id);
        nodeParts = new LinkedList<NodePartArtifact>();
    }

    @Override
    public void edit(final GfxObject editedGfxObject) {
        for (final NodePartArtifact nodePart : nodeParts) {
            if (editedGfxObject.equals(nodePart.getGfxObject())) {
                nodePart.edit(editedGfxObject);
                return;
            }
        }
        if (editedGfxObject.equals(this.getGfxObject())) {
            Log.warn("Selecting a virtual group : this should not happen !");
            nodeParts.peek().edit(editedGfxObject);
        } else {
            GfxObject gfxObjectGroup = editedGfxObject.getGroup();
            if (gfxObjectGroup != null) {
                for (final NodePartArtifact nodePart : nodeParts) {
                    if (gfxObjectGroup.equals(nodePart.getGfxObject())) {
                        nodePart.edit(editedGfxObject);
                        return;
                    }
                }
                gfxObjectGroup = gfxObjectGroup.getGroup();
                if (gfxObjectGroup != null) {
                    for (final NodePartArtifact nodePart : nodeParts) {
                        if (gfxObjectGroup.equals(nodePart.getGfxObject())) {
                            nodePart.edit(editedGfxObject);
                            return;
                        }
                    }
                    if (gfxObjectGroup.equals(this.getGfxObject())) {
                        Log.warn("Selecting the master virtual group : this should NOT happen !");
                        nodeParts.peek().edit(editedGfxObject);
                    } else {
                        Log.warn("No editable part found");
                    }
                } else {
                    Log.warn("No editable part found");
                }
            }
        }
    }

    @Override
    public int getHeight() {
        int height = 0;
        for (final NodePartArtifact nodePart : nodeParts) {
            height += nodePart.getHeight();
        }
        return height;
    }

    /**
	 * Getter for the name
	 * 
	 * @return the name of this node
	 */
    public abstract String getName();

    @Override
    public GfxObject getOutline() {
        if (QualityLevel.IsAlmost(QualityLevel.NORMAL)) {
            final GfxObject vg = GfxManager.getPlatform().buildVirtualGroup();
            final List<Integer> widthList = new ArrayList<Integer>();
            for (final NodePartArtifact nodePart : nodeParts) {
                nodePart.computeBounds();
                widthList.add(nodePart.getWidth());
            }
            final int maxWidth = GWTUMLDrawerHelper.getMaxOf(widthList);
            width = maxWidth;
            int heightDelta = 0;
            for (final NodePartArtifact nodePart : nodeParts) {
                nodePart.setNodeWidth(maxWidth);
                final GfxObject outline = nodePart.getOutline();
                outline.addToVirtualGroup(vg);
                outline.translate(new Point(0, heightDelta));
                heightDelta += nodePart.getHeight();
            }
            return vg;
        }
        return super.getOutline();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void rebuildGfxObject() {
        for (final NodePartArtifact nodePart : nodeParts) {
            GfxManager.getPlatform().clearVirtualGroup(nodePart.getGfxObject());
        }
        GfxManager.getPlatform().clearVirtualGroup(gfxObject);
        super.rebuildGfxObject();
    }

    @Override
    public void setCanvas(final UMLCanvas canvas) {
        this.canvas = canvas;
        for (final NodePartArtifact nodePart : nodeParts) {
            nodePart.setCanvas(canvas);
        }
    }

    @Override
    public void unselect() {
        super.unselect();
        for (final NodePartArtifact nodePart : nodeParts) {
            nodePart.unselect();
        }
    }

    @Override
    protected void buildGfxObject() {
        for (final NodePartArtifact nodePart : nodeParts) {
            nodePart.initializeGfxObject().addToVirtualGroup(gfxObject);
        }
        final List<Integer> widthList = new ArrayList<Integer>();
        for (final NodePartArtifact nodePart : nodeParts) {
            nodePart.computeBounds();
            widthList.add(nodePart.getWidth());
        }
        final int maxWidth = GWTUMLDrawerHelper.getMaxOf(widthList);
        width = maxWidth;
        int heightDelta = 0;
        for (final NodePartArtifact nodePart : nodeParts) {
            nodePart.setNodeWidth(maxWidth);
            nodePart.getGfxObject().translate(new Point(0, heightDelta));
            heightDelta += nodePart.getHeight();
        }
    }

    @Override
    protected void select() {
        super.select();
        for (final NodePartArtifact nodePart : nodeParts) {
            nodePart.select();
        }
    }
}
