package com.realtime.crossfire.jxclient.commands;

import com.realtime.crossfire.jxclient.gui.log.Buffer;
import com.realtime.crossfire.jxclient.gui.misc.JXCWindowRenderer;
import com.realtime.crossfire.jxclient.server.crossfire.CrossfireServerConnection;
import org.jetbrains.annotations.NotNull;

/**
 * Implements the command "clear". It clears the active message window.
 * @author Andreas Kirschbaum
 */
public class ClearCommand extends AbstractCommand {

    /**
     * The {@link JXCWindowRenderer} to affect.
     */
    @NotNull
    private final JXCWindowRenderer windowRenderer;

    /**
     * Creates a new instance.
     * @param windowRenderer the window renderer to affect
     * @param crossfireServerConnection the connection instance
     */
    public ClearCommand(@NotNull final JXCWindowRenderer windowRenderer, @NotNull final CrossfireServerConnection crossfireServerConnection) {
        super("clear", crossfireServerConnection);
        this.windowRenderer = windowRenderer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean allArguments() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull final String args) {
        if (args.length() != 0) {
            drawInfoError("The clear command does not take arguments.");
            return;
        }
        final Buffer buffer = windowRenderer.getActiveMessageBuffer();
        if (buffer == null) {
            drawInfoError("No active text window.");
            return;
        }
        buffer.clear();
    }
}
