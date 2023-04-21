package de.jost_net.OBanToo.Dtaus;

/**
 * Exception des DTAUS-Parsers
 * 
 * @author Heiner Jostkleigrewe
 */
public class DtausException extends Exception {

    private static final long serialVersionUID = 790050704552032876L;

    public static final String A_AUSFUEHRUNGSDATUM_FEHLERHAFT = "Ausf�hrungsdatum fehlerhaft";

    public static final String A_SATZLAENGENFELD_FEHLERHAFT = "Satzl�ngenfeld des A-Satzes fehlerhaft: ";

    public static final String A_SATZART_FEHLERHAFT = "Satzart des A-Satzes fehlerhaft: ";

    public static final String A_GUTSCHRIFT_LASTSCHRIFT_FEHLERHAFT = "Gutschrift/Lastschrift-Kennzeichen des A-Satzes fehlerhaft: ";

    public static final String A_BLZ_FEHLERHAFT = "Bankleitzahl des A-Satzes fehlerhaft: ";

    public static final String A_AUFTRAGGEBER_FEHLERHAFT = "L�nge des Auftraggebers fehlerhaft (=0 oder >27)";

    public static final String A_DATEIERSTELLUNGSDATUM_FEHLERHAFT = "Dateierstellungsdatum des A-Satzes ist fehlerhaft: ";

    public static final String A_KONTO_FEHLERHAFT = "Konto des A-Satzes fehlerhaft: ";

    public static final String A_REFERENZ_FEHLERHAFT = "Referenz des A-Satzes nicht numerisch: ";

    public static final String A_WAEHRUNGSKENNZEICHEN_FEHLERHAFT = "W�hrungskennzeichen des A-Satzes fehlerhaft: ";

    public static final String C_SATZLAENGE_FEHLERHAFT = "Satzlaengenfeld des C-Satzes fehlerhaft: ";

    public static final String C_SATZART_FEHLERHAFT = "Satzart des C-Satzes fehlerhaft: ";

    public static final String C_BLZERSTBETEILIGT_FEHLERHAFT = "Bankleitzahl des erstbeteiligten Institutes fehlerhaft: ";

    public static final String C_BLZENDBEGUENSTIGT_FEHLERHAFT = "Bankleitzahl des endbeg�nstigten Institutes fehlerhaft: ";

    public static final String C_KONTONUMMER_FEHLERHAFT = "Kontonummer fehlerhaft: ";

    public static final String C_NAME_EMPFAENGER = "Name des Zahlungsempf�ngers/Zahlungspflichtigen ung�ltig.";

    public static final String C_NAME_EMPFAENGER2 = "Name(2) des Zahlungsempf�ngers/Zahlungspflichtigen ung�ltig.";

    public static final String C_NAME_ABSENDER = "Name des Absenders ung�ltig.";

    public static final String C_NAME_ABSENDER2 = "Name(2) des Absenders ung�ltig.";

    public static final String C_INTERNEKUNDENNUMMER_FEHLERHAFT = "Interne Kundennummer fehlerhaft: ";

    public static final String C_TEXTSCHLUESSEL_FEHLERHAFT = "Textschluessel fehlerhaft: ";

    public static final String C_ERSTBEAUFTRAGTESINSTITUT_FEHLERHAFT = "Erstbeauftragtes Institut fehlerhaft: ";

    public static final String C_KONTOAUFTRAGGEBER_FEHLERHAFT = "Konto Auftraggeber fehlerhaft: ";

    public static final String C_BETRAG_FEHLERHAFT = "Betrag fehlerhaft: ";

    public static final String C_WAEHRUNGSKENNZEICHEN_FEHLERHAFT = "W�hrungskennzeichen fehlerhaft: ";

    public static final String C_ERWEITERUNGSZEICHEN_FEHLERHAFT = "Erweiterungszeichen fehlerhaft: ";

    public static final String C_ERWEITERUNG_FEHLERHAFT = "Erweiterung fehlerhaft: ";

    public static final String C_VERWENDUNGSZWECK_FEHLERHAFT = "Verwendungszweck fehlerhaft (L�nge > 27) oder mehr als 13 St�ck.";

    public static final String E_SATZLAENGENFELD_FEHLERHAFT = "Satzl�ngenfeld des E-Satzes fehlerhaft: ";

    public static final String E_SATZART_FEHLERHAFT = "Satzart des E-Satzes fehlerhaft: ";

    public static final String E_ANZAHL_CSAETZE_FEHLERHAFT = "Anzahl der C-S�tze im E-Satz fehlerhaft: ";

    public static final String E_SUMME_BETRAEGE_FEHLERHAFT = "Summe der Betr�ge im E-Satz fehlerhaft: ";

    public static final String SATZLAENGE_FEHLERHAFT = "Satzl�nge fehlerhaft: ";

    public static final String UNGUELTIGES_ZEICHEN = "Ung�ltiges Zeichen: ";

    public static final String UNGUELTIGE_LOGISCHE_DATEI = "Ung�ltige Logische Datei: ";

    public DtausException(String text) {
        super(text);
    }

    public DtausException(String text, String value) {
        super(text + value);
    }
}
