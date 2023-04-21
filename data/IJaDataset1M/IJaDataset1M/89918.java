package com.cgosoft.mtgdrafttimer;

/**
 * DraftPart que representa una pausa de un tiempo determinado por las
 * preferencias de la aplicaci�n. Esta clase se utiliza para suavizar la
 * transici�n de una fase a otra.
 */
public class PausePart extends DraftPart {

    private long time;

    private long millisUntilFinished;

    public static long PAUSE_TIME = 1000;

    /**
	 * Si asignamos un nombre al PausePart.
	 * 
	 * @param time
	 *            Tiempo en milisegundos que durar� la pausa.
	 * @param partName
	 *            Nombre que tendr� el PausePart.
	 */
    public PausePart(long time, String partName) {
        super(0, DraftPart.TYPE_PAUSE, partName);
        this.time = time;
        this.millisUntilFinished = time;
    }

    /**
	 * No asignamos un nombre al PausePart.
	 * 
	 * @param time
	 *            Tiempo en milisegundos que durar� la pausa.
	 */
    public PausePart(long time) {
        super(0, DraftPart.TYPE_PAUSE);
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
