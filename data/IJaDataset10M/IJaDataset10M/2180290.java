package com.cgosoft.mtgdrafttimer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Representa el objeto Draft que est� compuesto por partes m�s peque�as que
 * descienden de la clase DraftPart.
 */
public class Draft {

    private LinkedList<DraftPart> draftParts;

    private DraftPart activePart;

    private final long REVIEW_TIME_INCREASE = 15000;

    /**
	 * Para un draft normal de 3 sobres por jugador.
	 * 
	 * @param pickTimes
	 *            Tiempos para cada uno de los "picks" de cada sobre, en
	 *            milisegundos.
	 * @param numRounds
	 *            N�mero de rondas que se jugar�n.
	 * @param reviewTime
	 *            Tiempo para revisar lo que se draftea en un sobre, en
	 *            milisegundos.
	 * @param buildTime
	 *            Tiempo para la construcci�n del mazo de juego, en
	 *            milisegundos.
	 * @param roundTime
	 *            Tiempo de duraci�n para cada ronda de juego, en milisegundos.
	 */
    public Draft(HashMap<Integer, Long> pickTimes, int numRounds, long reviewTime, long buildTime, long roundTime, boolean excludeLand) {
        this.activePart = null;
        this.draftParts = new LinkedList<DraftPart>();
        @SuppressWarnings("unchecked") HashMap<Integer, Long> list1 = (HashMap<Integer, Long>) pickTimes.clone();
        @SuppressWarnings("unchecked") HashMap<Integer, Long> list2 = (HashMap<Integer, Long>) pickTimes.clone();
        @SuppressWarnings("unchecked") HashMap<Integer, Long> list3 = (HashMap<Integer, Long>) pickTimes.clone();
        this.draftParts.add(new Booster(1, list1, excludeLand));
        this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
        this.draftParts.add(new ReviewPart(1, reviewTime));
        this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
        this.draftParts.add(new Booster(2, list2, excludeLand));
        this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
        this.draftParts.add(new ReviewPart(2, reviewTime + this.REVIEW_TIME_INCREASE));
        this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
        this.draftParts.add(new Booster(3, list3, excludeLand));
        this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
        this.draftParts.add(new BuildPart(3, buildTime));
        if (numRounds > 0) {
            this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
        }
        for (int x = 1; x <= numRounds; x++) {
            this.draftParts.add(new RoundPart(x, roundTime));
            if (x != numRounds) {
                this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
            }
        }
    }

    /**
	 * Si se van a utilizar m�s de 3 sobres, aunque es poco probable que lo
	 * utilice alguna vez.
	 * 
	 * @param pickTimes
	 *            Tiempos para cada uno de los "picks" de cada sobre, en
	 *            milisegundos.
	 * @param numRounds
	 *            N�mero de rondas que se jugar�n.
	 * @param reviewTime
	 *            Tiempo para revisar lo que se draftea en un sobre, en
	 *            milisegundos.
	 * @param buildTime
	 *            Tiempo para la construcci�n del mazo de juego, en
	 *            milisegundos.
	 * @param roundTime
	 *            Tiempo de duraci�n para cada ronda de juego, en milisegundos.
	 * @param numBoosters
	 *            N�mero de sobres por jugador.
	 */
    public Draft(HashMap<Integer, Long> pickTimes, int numRounds, long reviewTime, long buildTime, long roundTime, boolean excludeLand, int numBoosters) {
        this.activePart = null;
        this.draftParts = new LinkedList<DraftPart>();
        for (int x = 1; x <= numBoosters; x++) {
            if (x != numBoosters) {
                @SuppressWarnings("unchecked") HashMap<Integer, Long> list = (HashMap<Integer, Long>) pickTimes.clone();
                this.draftParts.add(new Booster(x, list, excludeLand));
                this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
                this.draftParts.add(new ReviewPart(x, reviewTime + ((x - 1) * this.REVIEW_TIME_INCREASE)));
                this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
            } else {
                @SuppressWarnings("unchecked") HashMap<Integer, Long> list = (HashMap<Integer, Long>) pickTimes.clone();
                this.draftParts.add(new Booster(x, list, excludeLand));
                this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
                this.draftParts.add(new BuildPart(x, buildTime));
                if (numRounds > 0) {
                    this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
                }
            }
        }
        for (int y = 1; y <= numRounds; y++) {
            this.draftParts.add(new RoundPart(y, roundTime));
            if (y != numRounds) {
                this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
            }
        }
    }

    /**
	 * S�lo se cuenta el tiempo para las rondas que se indiquen
	 * 
	 * @param numRounds
	 *            N�mero de rondas que se jugar�n
	 * @param roundTime
	 *            Tiempo de cada ronda en milisegundos
	 */
    public Draft(int numRounds, long roundTime) {
        this.activePart = null;
        this.draftParts = new LinkedList<DraftPart>();
        for (int x = 1; x <= numRounds; x++) {
            this.draftParts.add(new RoundPart(x, roundTime));
            if (x != numRounds) {
                this.draftParts.add(new PausePart(PausePart.PAUSE_TIME));
            }
        }
    }

    /**
	 * Extrae un nuevo objeto DraftPart y lo asigna como parte activa.
	 * 
	 * @return Referencia al DraftPart activo.
	 */
    public DraftPart getNextPart() {
        this.activePart = (DraftPart) this.draftParts.poll();
        return this.activePart;
    }

    /**
	 * Devuelve la referencia al DraftPart activo.
	 * 
	 * @return Referencia al objeto DraftPart.
	 */
    public DraftPart getActivePart() {
        return this.activePart;
    }

    /**
	 * Devuelve la referencia al siguiente DraftPart o null si ya no quedan m�s
	 * partes.
	 * 
	 * @return Referencia al pr�ximo DraftPart.
	 */
    public DraftPart showNextPart() {
        DraftPart result;
        try {
            result = this.draftParts.getFirst();
        } catch (NoSuchElementException e) {
            result = null;
        }
        return result;
    }
}
