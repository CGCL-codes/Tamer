package com.realtime.crossfire.jxclient.gui.list;

import com.realtime.crossfire.jxclient.faces.FacesManager;
import com.realtime.crossfire.jxclient.gui.gui.GUIElement;
import com.realtime.crossfire.jxclient.gui.gui.GUIElementListener;
import com.realtime.crossfire.jxclient.gui.gui.TooltipManager;
import com.realtime.crossfire.jxclient.gui.item.GUIItemKnowledgeType;
import com.realtime.crossfire.jxclient.gui.item.ItemPainter;
import com.realtime.crossfire.jxclient.gui.label.AbstractLabel;
import com.realtime.crossfire.jxclient.items.ItemView;
import com.realtime.crossfire.jxclient.knowledge.KnowledgeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author nicolas
 */
public class GUIKnowledgeTypeList extends GUIItemList {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link TooltipManager} to update.
     */
    @NotNull
    private final TooltipManager tooltipManager;

    /**
     * The {@link GUIElementListener} to notify.
     */
    @NotNull
    private final GUIElementListener elementListener;

    /**
     * The base name for created elements.
     */
    @NotNull
    private final String name;

    /**
     * The {@link ItemView} to use.
     */
    @NotNull
    private final ItemView itemView;

    @NotNull
    private final KnowledgeManager knowledgeManager;

    /**
     * The {@link ItemPainter} for painting the icon.
     */
    @NotNull
    private final ItemPainter itemPainter;

    /**
     * The {@link FacesManager} to use.
     */
    @NotNull
    private final FacesManager facesManager;

    /**
     * Creates a new instance.
     * @param tooltipManager the tooltip manager to update
     * @param elementListener the element listener to notify
     * @param name the name of this element
     * @param cellWidth the width of cells
     * @param cellHeight the height of cells
     * @param itemView the item view to monitor
     * @param currentItem the label to update with information about the
     * selected item.
     * @param itemPainter the item painter for painting the icon
     * @param facesManager the faces manager to use
     */
    public GUIKnowledgeTypeList(@NotNull final TooltipManager tooltipManager, @NotNull final GUIElementListener elementListener, @NotNull final String name, final int cellWidth, final int cellHeight, @NotNull final ItemView itemView, @Nullable final AbstractLabel currentItem, @NotNull final KnowledgeManager knowledgeManager, @NotNull final ItemPainter itemPainter, @NotNull final FacesManager facesManager) {
        super(tooltipManager, elementListener, name, cellWidth, cellHeight, itemView, currentItem, new GUIItemKnowledgeType(tooltipManager, elementListener, name + "_template", itemPainter, -1, facesManager, knowledgeManager, itemView, cellHeight));
        this.tooltipManager = tooltipManager;
        this.elementListener = elementListener;
        this.name = name;
        this.itemView = itemView;
        this.knowledgeManager = knowledgeManager;
        this.itemPainter = itemPainter;
        this.facesManager = facesManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void selectionChanged(final int selectedIndex) {
        if (knowledgeManager == null) {
            return;
        }
        knowledgeManager.filterType(selectedIndex);
        super.selectionChanged(selectedIndex);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected GUIElement newItem(final int index) {
        return new GUIItemKnowledgeType(tooltipManager, elementListener, name + index, itemPainter, index, facesManager, knowledgeManager, itemView, 0);
    }
}
