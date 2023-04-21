package org.moyoman.client.reference;

import org.moyoman.comm.client.CommandExecutor;
import org.moyoman.comm.client.MoyomanPlayer;
import org.moyoman.comm.client.Player;
import org.moyoman.framework.ServerConfig;
import org.moyoman.module.Mode;
import org.moyoman.util.Color;
import org.moyoman.util.GameRecord;
import org.moyoman.util.Handicap;
import org.moyoman.util.IllegalMoveException;
import org.moyoman.util.InternalErrorException;
import org.moyoman.util.MoveDescriptor;
import org.moyoman.util.NoSuchDataException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Provides an implementation of <code>ValidatedPlayer</code> to allow both
 * mouse input and computer play. The two play modes are toggled by
 * <code>setInteractive(boolean)</code>. Interactive as true indicates mouse
 * player mode; false indicates computer player mode.
 *
 * @author Jeffrey M. Thompson
 * @version v0.15
 *
 * @since v0.15
 */
public class InteractiveComputerPlayer extends MoyomanPlayer implements MouseListener {

    /** Mouse player. */
    private MousePlayer _mousePlayer;

    /** Flag indicating whether this is in interactive mode. */
    private boolean _isInteractive;

    /**
     * Construct this object with the given parameters.
     *
     * @param owner The owner.
     * @param color The color of the player.
     * @param handicap The handicap of the game.
     * @param mode Computer player mode.
     * @param commandExecutor Command executor.
     * @param isInteractive Flag indicating whether this is in interactive mode
     *        initially.
     *
     * @throws InternalErrorException Thrown if the operation fails for any
     *         reason.
     */
    public InteractiveComputerPlayer(IMoyoman owner, Color color, Handicap handicap, Mode mode, CommandExecutor commandExecutor, boolean isInteractive) throws InternalErrorException {
        super(color, handicap, mode, commandExecutor);
        _isInteractive = isInteractive;
        _mousePlayer = new MousePlayer(owner, color, handicap);
    }

    /**
     * Return identifying information about this player.
     *
     * @since v0.15
     */
    public String getIdentifyingInfo() {
        String str = "Interactive Computer Player: " + super.getIdentifyingInfo();
        return str;
    }

    /**
     * Set the flag which indicates whether this is in interactive mode.
     */
    public void setInteractive(boolean isInteractive) {
        _isInteractive = isInteractive;
    }

    /**
     * Make a move. The derived class must implement this method.
     *
     * @param md The MoveDescriptor which describes the move to make.
     *
     * @throws IllegalMoveException Thrown if the move is illegal.
     * @throws InternalErrorException Thrown if the operation fails for any
     *         reason.
     */
    public void derivedMakeMove(MoveDescriptor md) throws IllegalMoveException, InternalErrorException {
        super.derivedMakeMove(md);
        _mousePlayer.derivedMakeMove(md);
    }

    /**
     * Get a move from the player. The derived class must implement this
     * method.
     *
     * @return A MoveDescriptor object which describes the move.
     *
     * @throws IllegalMoveException Thrown if the move is illegal.
     * @throws InternalErrorException Thrown if the operation fails for any
     *         reason.
     */
    public MoveDescriptor derivedRequestMove() throws IllegalMoveException, InternalErrorException {
        MoveDescriptor md;
        if (_isInteractive) {
            md = _mousePlayer.derivedRequestMove();
            CommandExecutor.get().getSuggestedMove(getId());
            super.derivedMakeMove(md);
        } else {
            md = super.derivedRequestMove();
            _mousePlayer.derivedMakeMove(md);
        }
        return md;
    }

    /**
     * Respond to a mouse click.
     *
     * @since v0.15
     */
    public void mouseClicked(MouseEvent event) {
        _mousePlayer.mouseClicked(event);
    }

    /**
     * Implement the mouse listener inteface.
     *
     * @param event A mouse event.
     *
     * @since v0.15
     */
    public void mouseEntered(MouseEvent event) {
    }

    /**
     * Implement the mouse listener inteface.
     *
     * @param event A mouse event.
     *
     * @since v0.15
     */
    public void mouseExited(MouseEvent event) {
    }

    /**
     * Implement the mouse listener inteface.
     *
     * @param event A mouse event.
     *
     * @since v0.15
     */
    public void mousePressed(MouseEvent event) {
    }

    /**
     * Implement the mouse listener inteface.
     *
     * @param event A mouse event.
     *
     * @since v0.15
     */
    public void mouseReleased(MouseEvent event) {
    }
}
