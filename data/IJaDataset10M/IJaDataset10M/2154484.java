package thesis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlagiTecController {

    private ArrayList $resultaat_standaard;

    private ArrayList $resultaat_standaard_met_primaire_winnowing_filter;

    private ArrayList $resultaat_volledige_tekst_letterlijk;

    private ArrayList $resultaat_volledige_tekst_aangepast;

    private ArrayList $resultaat_woordfrequenties;

    public PlagiTecController() {
        Settings.leesSettingsIn();
    }

    public boolean leesBestaandeTekstbestandenIn() {
        return PlagiTec.leesBestaandeTekstbestandenIn();
    }

    public boolean leesBestaandeTekstbestandenIn(String path) {
        return PlagiTec.leesBestaandeTekstbestandenIn(path);
    }

    public boolean verwijderDocumenten(String path) {
        return Gegevensbank.getGegevensbank().verwijderDocumenten(path);
    }

    public boolean startControleren(int vergelijkingsmethode, String pad) throws IOException {
        File locatie = new File(pad);
        if (!locatie.exists()) {
            return false;
        }
        if (vergelijkingsmethode == 1) {
            startControlerenStandaard(pad);
        } else {
            if (vergelijkingsmethode == 2) {
                startControlerenStandaardMetVoorbereidendeFilter(pad);
            } else {
                if (vergelijkingsmethode == 3) {
                    startControlerenVolledigeTekstLetterlijk(pad);
                } else {
                    if (vergelijkingsmethode == 4) {
                        startControlerenVolledigeTekstAangepast(pad);
                    } else {
                        if (vergelijkingsmethode == 5) {
                            startControlerenWoordfrequenties(pad);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
	 * Start het controleren van het bestand waarvan het pad als argument wordt meegegeven.
	 * @param pad
	 * 		  Het pad van het te controleren bestand.
	 * @throws IOException
	 */
    private void startControlerenStandaard(String pad) throws IOException {
        ArrayList res = PlagiTec.controleer_standaard(pad);
        setResultaatStandaard(res);
    }

    private void startControlerenStandaardMetVoorbereidendeFilter(String pad) throws IOException {
        ArrayList res = PlagiTec.controleer_standaard_met_voorbereidende_filter(pad);
        setResultaat_standaard_met_voorbereidende_filter(res);
    }

    private void startControlerenVolledigeTekstLetterlijk(String pad) throws IOException {
        ArrayList res = PlagiTec.controleer_volledigetekstletterlijk(pad);
        setResultaatVolledigeTekstLetterlijk(res);
    }

    private void startControlerenVolledigeTekstAangepast(String pad) throws IOException {
        ArrayList res = PlagiTec.controleer_volledigetekstaangepast(pad);
        setResultaatVolledigeTekstAangepast(res);
    }

    private void startControlerenWoordfrequenties(String pad) throws IOException {
        ArrayList res = PlagiTec.controleer_woordfrequenties(pad);
        setResultaat_woordfrequenties(res);
    }

    public void setResultaatStandaard(ArrayList resultaat) {
        $resultaat_standaard = resultaat;
    }

    public void setResultaat_standaard_met_voorbereidende_filter(ArrayList resultaat) {
        $resultaat_standaard_met_primaire_winnowing_filter = resultaat;
    }

    public void setResultaatVolledigeTekstAangepast(ArrayList resultaat) {
        $resultaat_volledige_tekst_aangepast = resultaat;
    }

    public void setResultaatVolledigeTekstLetterlijk(ArrayList resultaat) {
        $resultaat_volledige_tekst_letterlijk = resultaat;
    }

    public void setResultaat_woordfrequenties(ArrayList resultaat_woordfrequenties) {
        $resultaat_woordfrequenties = resultaat_woordfrequenties;
    }

    public ArrayList getResultaatStandaard() {
        return $resultaat_standaard;
    }

    public ArrayList getResultaat_standaard_met_primaire_winnowing_filter() {
        return $resultaat_standaard_met_primaire_winnowing_filter;
    }

    public ArrayList getResultaatVolledigeTekstAangepast() {
        return $resultaat_volledige_tekst_aangepast;
    }

    public ArrayList getResultaatVolledigeTekstLetterlijk() {
        return $resultaat_volledige_tekst_letterlijk;
    }

    public ArrayList getResultaat_woordfrequenties() {
        return $resultaat_woordfrequenties;
    }

    public String getNaam() {
        return Settings.getNAAM();
    }

    public int getNbDocumentsInDatabase() {
        return Gegevensbank.getGegevensbank().getNbDocumentsInDatabase();
    }

    public boolean resetGegevensbank() {
        return Gegevensbank.getGegevensbank().resetGegevensbank();
    }
}
