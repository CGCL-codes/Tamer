package mylittlestudent.controller;

import java.util.*;
import javax.microedition.lcdui.*;
import mylittlestudent.controller.interfaces.*;

/**
 * der Zeitmesser
 */
class Timer extends Thread implements ITimer {

    /**
     * Anzahl an Millisekunden, die der Zeitmesser nichts tut, nachdem er alles erledigt hat.
     */
    private static final int WAIT_MILLISECONDS = 100;

    /**
     * Intervall zwischen Event-Behandlungen
     */
    private int eventInterval = 60000;

    /**
     * Z�hler, der zum Abz�hlen von Timer-L�ufen verwendet wird, um Event-Intervalle zu erzeugen.
     */
    private int eventIntervalCount = 0;

    private Vector observers = new Vector();

    private volatile boolean end = false;

    private Object lockOberservers = new Object();

    /**
     * Gibt das Intervall zwischen zwei Event-Ausl�sungen zur�ck.
     * @return Timer-Intervall in Millisekunden
     */
    public int getEventInterval() {
        return eventInterval;
    }

    public void setEventInterval(int milliseconds) {
        eventInterval = milliseconds;
    }

    /**
     * Thread beenden
     */
    public void endThread() {
        end = true;
    }

    /**
     * einen m�glichen Observer (View) abmelden
     * @param view der View, der m�glicherweise den Timer observiert
     */
    public void unregisterViewObserver(Displayable view) {
        if (!(view instanceof ITimerObserver)) {
            return;
        }
        ITimerObserver unregisterObserver = (ITimerObserver) view;
        this.unregisterObserver(unregisterObserver);
    }

    /**
     * einen Auftrag eines Observers abmelden
     * @param observer der Observer/Beobachter
     * @param ID die Auftrags-ID
     */
    public void unregisterObserver(ITimerObserver observer, int ID) {
        synchronized (lockOberservers) {
            int observersSize = observers.size();
            for (int observerIndex = 0; observerIndex < observersSize; observerIndex++) {
                ObserverInfo currentObserver = (ObserverInfo) observers.elementAt(observerIndex);
                if (currentObserver.observer == observer && currentObserver.ID == ID) {
                    observers.removeElement(currentObserver);
                    return;
                }
            }
        }
    }

    /**
     * einen Observer abmelden
     * @param observer der Observer/Beobachter
     */
    public void unregisterObserver(ITimerObserver observer) {
        synchronized (lockOberservers) {
            for (int observerIndex = 0; observerIndex < observers.size(); observerIndex++) {
                ObserverInfo currentObserver = (ObserverInfo) observers.elementAt(observerIndex);
                if (currentObserver.observer == observer) {
                    observers.removeElement(currentObserver);
                }
            }
        }
    }

    /**
     * einen Observer registrieren
     * @param observer der Observer/Beobachter
     * @param waitMilliseconds die Zeitspanne, nach deren Ablauf der Observer informiert werden m�chte
     */
    public void registerObserver(ITimerObserver observer, int waitMilliseconds) {
        long endTime = System.currentTimeMillis() + waitMilliseconds;
        ObserverInfo observerInfo = new ObserverInfo(observer, endTime, 0);
        observers.addElement(observerInfo);
    }

    /**
     * einen Observer registrieren
     * @param observer der Beobachter, der �ber den Ablauf einer Zeitspanne informiert werden m�chte
     * @param waitMilliseconds die Zeitspanne in Millisekunden, nach deren Ablauf der Beobachter informiert werden m�chte
     * @param ID Eine Kennzahl, f�r die Zuordnung von Warteauftrag zu Zweck.
     * Dies wird gebraucht, falls man den Timer f�r verschiedene Zwecke warten l�sst.
     * Z.B. k�nnte man den Timer wegen zwei verschiedenen Animationen warten lassen.  
     * Damit man diese dann bei dem Aufruf von timeElapsed() au�einanderhalten kann, gibt es die ID.
     */
    public void registerObserver(ITimerObserver observer, int waitMilliseconds, int ID) {
        long endTime = System.currentTimeMillis() + waitMilliseconds;
        ObserverInfo observerInfo = new ObserverInfo(observer, endTime, ID);
        observers.addElement(observerInfo);
    }

    /**
     * die Thread-Funktion
     */
    public void run() {
        try {
            while (!end) {
                sleep(WAIT_MILLISECONDS);
                if (eventIntervalCount >= eventInterval) {
                    if (!GameController.getInstance().getRandomEventsDisabled()) GameController.getInstance().getActionFacade().performAction(ActionTypes.RUN_RANDOM_EVENT);
                    if (!GameController.getInstance().getRegularEventsDisabled()) GameController.getInstance().getActionFacade().performAction(ActionTypes.RUN_REGULAR_EVENT);
                    eventIntervalCount = 0;
                } else {
                    eventIntervalCount += WAIT_MILLISECONDS;
                }
                synchronized (lockOberservers) {
                    long currentTime = System.currentTimeMillis();
                    for (int observerIndex = 0; observerIndex < observers.size(); observerIndex++) {
                        ObserverInfo currentObserver = (ObserverInfo) observers.elementAt(observerIndex);
                        if (currentTime > currentObserver.endTime) {
                            currentObserver.observer.timeElapsed(currentObserver.ID);
                            observers.removeElement(currentObserver);
                            observerIndex--;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private class ObserverInfo {

        public ITimerObserver observer;

        public long endTime = 0;

        public int ID = 0;

        public ObserverInfo(ITimerObserver observer, long endTime, int ID) {
            this.observer = observer;
            this.endTime = endTime;
            this.ID = ID;
        }
    }
}
