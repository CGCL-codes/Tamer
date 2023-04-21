package com.realtime.crossfire.jxclient.main;

import com.realtime.crossfire.jxclient.guistate.ClientSocketState;
import com.realtime.crossfire.jxclient.guistate.GuiStateListener;
import com.realtime.crossfire.jxclient.guistate.GuiStateManager;
import com.realtime.crossfire.jxclient.queue.CommandQueue;
import com.realtime.crossfire.jxclient.server.crossfire.CrossfireServerConnection;
import com.realtime.crossfire.jxclient.server.crossfire.CrossfireUpdateItemListener;
import org.jetbrains.annotations.NotNull;

/**
 * Tracks received Crossfire messages and resets the "output-count" setting
 * whenever a player logs in.
 * @author Andreas Kirschbaum
 */
public class OutputCountTracker {

    /**
     * The {@link CrossfireServerConnection} to track.
     */
    @NotNull
    private final CrossfireServerConnection server;

    /**
     * The {@link CommandQueue} for sending commands.
     */
    @NotNull
    private final CommandQueue commandQueue;

    /**
     * The {@link CrossfireUpdateItemListener} to receive item updates.
     */
    @NotNull
    private final CrossfireUpdateItemListener crossfireUpdateItemListener = new CrossfireUpdateItemListener() {

        @Override
        public void delinvReceived(final int tag) {
        }

        @Override
        public void delitemReceived(@NotNull final int[] tags) {
        }

        @Override
        public void addItemReceived(final int location, final int tag, final int flags, final int weight, final int faceNum, @NotNull final String name, @NotNull final String namePl, final int anim, final int animSpeed, final int nrof, final int type) {
        }

        @Override
        public void playerReceived(final int tag, final int weight, final int faceNum, @NotNull final String name) {
            commandQueue.sendNcom(true, 1, "output-count 1");
        }

        @Override
        public void upditemReceived(final int flags, final int tag, final int valLocation, final int valFlags, final int valWeight, final int valFaceNum, @NotNull final String valName, @NotNull final String valNamePl, final int valAnim, final int valAnimSpeed, final int valNrof) {
        }
    };

    /**
     * The {@link GuiStateListener} for detecting established or dropped
     * connections.
     */
    @NotNull
    private final GuiStateListener guiStateListener = new GuiStateListener() {

        @Override
        public void start() {
            server.removeCrossfireUpdateItemListener(crossfireUpdateItemListener);
        }

        @Override
        public void metaserver() {
            server.removeCrossfireUpdateItemListener(crossfireUpdateItemListener);
        }

        @Override
        public void preConnecting(@NotNull final String serverInfo) {
        }

        @Override
        public void connecting(@NotNull final String serverInfo) {
            server.addCrossfireUpdateItemListener(crossfireUpdateItemListener);
        }

        @Override
        public void connecting(@NotNull final ClientSocketState clientSocketState) {
        }

        @Override
        public void connected() {
        }

        @Override
        public void connectFailed(@NotNull final String reason) {
        }
    };

    /**
     * Creates a new instance.
     * @param guiStateManager the gui state manager to track
     * @param server the crossfire server connection to track
     * @param commandQueue the command queue for sending commands
     */
    public OutputCountTracker(@NotNull final GuiStateManager guiStateManager, @NotNull final CrossfireServerConnection server, @NotNull final CommandQueue commandQueue) {
        this.server = server;
        this.commandQueue = commandQueue;
        guiStateManager.addGuiStateListener(guiStateListener);
    }
}
