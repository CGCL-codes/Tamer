package com.cgosoft.mtgdrafttimer;

/**
 * La clase RoundPart representa la parte del draft en la que se juega una
 * ronda.
 */
public class RoundPart extends DraftPart {

    private long time;

    private long millisUntilFinished;

    /**
	 * Si le asignamos un nombre al RoundPart.
	 * 
	 * @param partId
	 *            Identificador del RoundPart.
	 * @param time
	 *            Tiempo que durar� la ronda de juego.
	 * @param partName
	 *            Nombre del RoundPart.
	 */
    public RoundPart(int partId, long time, String partName) {
        super(partId, DraftPart.TYPE_ROUND, partName);
        this.time = time;
        this.millisUntilFinished = time;
    }

    /**
	 * No le asignamos un nombre al RoundPart.
	 * 
	 * @param partId
	 *            Identificador del RoundPart.
	 * @param time
	 *            Tiempo que durar� la ronda de juego.
	 */
    public RoundPart(int partId, long time) {
        super(partId, DraftPart.TYPE_ROUND, "");
        this.time = time;
        this.millisUntilFinished = time;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMillisUntilFinished() {
        return millisUntilFinished;
    }
}
