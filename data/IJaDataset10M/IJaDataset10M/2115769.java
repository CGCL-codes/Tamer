package mediathek.filme;

import mediathek.Funktionen;
import mediathek.Konstanten;
import mediathek.daten.Daten;

public class BeobTimerFilmeLaden implements TimerListener {

    private Daten daten;

    public BeobTimerFilmeLaden(Daten ddaten) {
        daten = ddaten;
    }

    @Override
    public void ping() {
        --daten.neuLadenIn;
        if (daten.neuLadenIn <= 0) {
            resetTimer();
            if (Funktionen.getUpdateFilme(daten) == Konstanten.UPDATE_FILME_AUTO) {
                daten.filmeLaden.filmeImportServer();
            }
        }
    }

    @Override
    public void reset() {
        resetTimer();
    }

    private synchronized void resetTimer() {
        daten.neuLadenIn = Konstanten.NEU_LADEN_IN;
    }
}
