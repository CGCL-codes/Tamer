package thesis;

import java.util.ArrayList;

public class Vergelijker_volledigeTekstAangepast extends Vergelijker {

    public ArrayList vergelijkTekst(DTDocument document) {
        ArrayList result = new ArrayList();
        String inhoud = document.getInhoud();
        result = Gegevensbank.getGegevensbank().zoekGewijzigdVoorkomen(inhoud, null);
        return result;
    }
}
