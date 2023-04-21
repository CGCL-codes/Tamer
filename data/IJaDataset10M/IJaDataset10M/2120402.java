package com.realtime.crossfire.jxclient.gui;

import com.realtime.crossfire.jxclient.account.CharacterInformation;
import com.realtime.crossfire.jxclient.account.CharacterInformationListener;
import com.realtime.crossfire.jxclient.account.CharacterModel;
import com.realtime.crossfire.jxclient.gui.gui.ActivatableGUIElement;
import com.realtime.crossfire.jxclient.gui.gui.GUIElementListener;
import com.realtime.crossfire.jxclient.gui.gui.TooltipManager;
import com.realtime.crossfire.jxclient.gui.scrollable.GUIScrollable;
import com.realtime.crossfire.jxclient.skin.skin.Extent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Transparency;
import org.jetbrains.annotations.NotNull;

/**
 * A character displaying GUI element.
 * @author Nicolas Weeger
 */
public class GUICharacter extends ActivatableGUIElement implements GUIScrollable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1;

    /**
     * Character model to display items from.
     */
    private final CharacterModel characterModel;

    /**
     * Index of the item to display in {@link #characterModel}.
     */
    private int index;

    /**
     * The font to use.
     */
    @NotNull
    private final Font font;

    /**
     * If set, paint the element in "selected" state.
     */
    private boolean selected = false;

    /**
     * All listeners to the entry itself.
     */
    @NotNull
    private final CharacterInformationListener characterInformationListener = new CharacterInformationListener() {

        /** {@inheritDoc} */
        @Override
        public void informationChanged() {
            setChanged();
            updateTooltip();
        }
    };

    /**
     * Creates a new instance.
     * @param tooltipManager the tooltip manager to update
     * @param elementListener the element listener to notify
     * @param name the name of this element
     * @param w the width for drawing this element to screen
     * @param h the height for drawing this element to screen
     * @param font the font to use
     * @param defaultIndex the initial metaserver index
     * @param characterModel the character model to monitor
     */
    public GUICharacter(@NotNull final TooltipManager tooltipManager, @NotNull final GUIElementListener elementListener, @NotNull final String name, final int w, final int h, @NotNull final Font font, final int defaultIndex, final CharacterModel characterModel) {
        super(tooltipManager, elementListener, name, new Extent(0, 0, w, h), Transparency.TRANSLUCENT);
        this.characterModel = characterModel;
        this.font = font;
        index = defaultIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void activeChanged() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(@NotNull final Graphics2D g2) {
        final CharacterInformation character = characterModel.getEntry(index);
        g2.setBackground(new Color(0, 0, 0, 0.0f));
        g2.clearRect(0, 0, getWidth(), getHeight());
        g2.setFont(font);
        g2.setColor(isActive() || selected ? Color.RED : Color.GRAY);
        g2.drawString(character == null ? "" : character.getName(), 0, font.getSize() + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canScroll(final int distance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void scroll(final int distance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetScroll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns the index of this element.
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of this element.
     * @param index the index
     */
    public void setIndex(final int index) {
        if (this.index == index) {
            return;
        }
        characterModel.removeCharacterInformationListener(index, characterInformationListener);
        this.index = index;
        characterModel.addCharacterInformationListener(index, characterInformationListener);
        setChanged();
        updateTooltip();
    }

    /**
     * Updates the tooltip text.
     */
    private void updateTooltip() {
        final CharacterInformation characterInformation = characterModel.getEntry(index);
        setTooltipText(characterInformation == null ? null : characterInformation.getName());
    }

    /**
     * Sets the selected state.
     * @param selected whether this element should drawn as "selected"
     */
    public void setSelected(final boolean selected) {
        if (this.selected == selected) {
            return;
        }
        this.selected = selected;
        setChanged();
    }
}
