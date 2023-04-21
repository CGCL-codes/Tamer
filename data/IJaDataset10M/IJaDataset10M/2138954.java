package net.sf.bbarena.model.event.game;

import net.sf.bbarena.model.Arena;
import net.sf.bbarena.model.pitch.Ball;
import net.sf.bbarena.model.pitch.Pitch;
import net.sf.bbarena.model.team.Player;
import net.sf.bbarena.model.util.Concat;
import net.sf.bbarena.model.util.Pair;

public class PickUpBallEvent extends BallEvent {

    private static final long serialVersionUID = 8477756281960807889L;

    private long _playerId = 0;

    private Ball _ball = null;

    private Player _player = null;

    public PickUpBallEvent(int ballId, long playerId) {
        super(ballId);
        _playerId = playerId;
    }

    @Override
    public void doEvent(Arena arena) {
        _arena = arena;
        _player = arena.getPlayerManager().getPlayer(_playerId);
        _ball = getBall(arena);
        arena.getPitch().ballPickUp(_ball, _player);
    }

    @Override
    public void undoEvent() {
        Pitch pitch = _arena.getPitch();
        pitch.ballLose(_ball, _player);
    }

    @Override
    public String getString() {
        return Concat.buildLog(getClass(), new Pair("ballId", getBallId()), new Pair("playerId", _playerId));
    }

    public long getPlayerId() {
        return _playerId;
    }
}
