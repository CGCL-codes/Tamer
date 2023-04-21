package com.realtime.crossfire.jxclient.skin.factory;

import com.realtime.crossfire.jxclient.gui.button.ButtonImages;
import com.realtime.crossfire.jxclient.gui.button.GUITextButton;
import com.realtime.crossfire.jxclient.gui.commandlist.CommandList;
import com.realtime.crossfire.jxclient.gui.gui.AbstractGUIElement;
import com.realtime.crossfire.jxclient.gui.gui.GUIElementListener;
import com.realtime.crossfire.jxclient.gui.gui.TooltipManager;
import java.awt.Color;
import java.awt.Font;
import org.jetbrains.annotations.NotNull;

/**
 * A factory class to create "textbutton" instances.
 * @author Andreas Kirschbaum
 */
public class TextButtonFactory {

    /**
     * The images comprising the "up" button state.
     */
    @NotNull
    private final ButtonImages up;

    /**
     * The images comprising the "down" button state.
     */
    @NotNull
    private final ButtonImages down;

    /**
     * The font to use.
     */
    @NotNull
    private final Font font;

    /**
     * The text color.
     */
    @NotNull
    private final Color color;

    /**
     * The text color when selected.
     */
    @NotNull
    private final Color colorSelected;

    /**
     * Creates a new instance.
     * @param up the images comprising the "up" button state
     * @param down the images comprising the "down" button state
     * @param font the font to use
     * @param color the text color
     * @param colorSelected the text color when selected
     */
    public TextButtonFactory(@NotNull final ButtonImages up, @NotNull final ButtonImages down, @NotNull final Font font, @NotNull final Color color, @NotNull final Color colorSelected) {
        this.up = up;
        this.down = down;
        this.font = font;
        this.color = color;
        this.colorSelected = colorSelected;
    }

    /**
     * Creates a new text button.
     * @param tooltipManager the tooltip manager to update
     * @param elementListener the element listener to notify
     * @param name the name of this element
     * @param text the button text
     * @param autoRepeat whether the button should autorepeat while being
     * pressed
     * @param commandList the commands to execute when the button is elected
     * @return the new text button
     */
    @NotNull
    public AbstractGUIElement newTextButton(@NotNull final TooltipManager tooltipManager, @NotNull final GUIElementListener elementListener, @NotNull final String name, @NotNull final String text, final boolean autoRepeat, @NotNull final CommandList commandList) {
        return new GUITextButton(tooltipManager, elementListener, name, up, down, text, font, color, colorSelected, autoRepeat, commandList);
    }
}
