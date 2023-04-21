package ru.nsu.ccfit.pm.econ.engine.data;

import java.util.Date;
import ru.nsu.ccfit.pm.econ.common.engine.data.IUGameTime;

/**
 * IUGameTime implementation for engine
 * 
 * @author pupatenko
 * 
 * @see IUGameTime
 */
public class GameTimeEngine implements IUGameTime {

    private Date time;

    private int turnNumber;

    private boolean turnFinished;

    public GameTimeEngine(Date time, int turnNumber, boolean turnFinished) {
        this.time = time;
        this.turnNumber = turnNumber;
        this.turnFinished = turnFinished;
    }

    public GameTimeEngine(IUGameTime toCopy) {
        this.time = new Date(toCopy.getTime().getTime());
        this.turnNumber = toCopy.getTurnNumber();
        this.turnFinished = toCopy.isTurnFinished();
    }

    @Override
    public Date getTime() {
        return time;
    }

    @Override
    public int getTurnNumber() {
        return turnNumber;
    }

    @Override
    public boolean isTurnFinished() {
        return turnFinished;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public void setTurnFinished(boolean turnFinished) {
        this.turnFinished = turnFinished;
    }
}
