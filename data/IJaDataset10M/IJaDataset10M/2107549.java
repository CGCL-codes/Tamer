package com.realtime.crossfire.jxclient.shortcuts;

import com.realtime.crossfire.jxclient.faces.Face;
import com.realtime.crossfire.jxclient.queue.CommandQueue;
import com.realtime.crossfire.jxclient.spells.Spell;
import com.realtime.crossfire.jxclient.spells.SpellListener;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Shortcut} that casts a spell.
 * @author Andreas Kirschbaum
 */
public class ShortcutSpell extends Shortcut {

    /**
     * Command prefix to "cast" a spell.
     */
    @NotNull
    private static final String CAST = "cast ";

    /**
     * Command prefix to "invoke" a spell.
     */
    @NotNull
    private static final String INVOKE = "invoke ";

    /**
     * The command queue for executing commands.
     */
    @NotNull
    private final CommandQueue commandQueue;

    /**
     * The spell to cast.
     */
    @NotNull
    private final Spell spell;

    /**
     * The command for casting the spell.
     */
    @NotNull
    private String command = CAST;

    /**
     * The {@link SpellListener} attached to {@link #spell}.
     */
    @NotNull
    private final SpellListener spellListener = new SpellListener() {

        @Override
        public void spellChanged() {
            fireModifiedEvent();
        }
    };

    /**
     * Creates a new instance.
     * @param commandQueue the command queue for executing commands
     * @param spell the spell to cast
     */
    public ShortcutSpell(@NotNull final CommandQueue commandQueue, @NotNull final Spell spell) {
        this.commandQueue = commandQueue;
        this.spell = spell;
        spell.addSpellListener(spellListener);
    }

    /**
     * Returns the spell to cast.
     * @return the spell
     */
    @NotNull
    public Spell getSpell() {
        return spell;
    }

    /**
     * Returns whether the spell should be "cast" or "invoked".
     * @return <code>true</code> for "cast", or <code>false</code> for "invoke"
     */
    public boolean isCast() {
        return command == CAST;
    }

    /**
     * Sets whether the spell should be "cast" or "invoked".
     * @param cast <code>true</code> for "cast", or <code>false</code> for
     * "invoke"
     */
    public void setCast(final boolean cast) {
        final String newCommand = cast ? CAST : INVOKE;
        if (command == newCommand) {
            return;
        }
        command = newCommand;
        fireModifiedEvent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        spell.removeSpellListener(spellListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        if (!spell.isUnknown()) {
            commandQueue.sendNcom(false, command + spell.getTag());
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getTooltipText() {
        return command + spell.getTooltipText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final ShortcutVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean displaysFace(final Face face) {
        return face.getFaceNum() == spell.getFaceNum();
    }
}
