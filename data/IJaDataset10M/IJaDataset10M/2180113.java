package ch.articlefox.localization;

import java.util.HashMap;
import ch.articlefox.Languages;

/**
 * The german dictionary.
 * 
 * @author Lukas Blunschi
 */
public class German extends AbstractDictionary {

    public German() {
        phase2shortphasename = new HashMap<Integer, String>();
        phase2shortphasename.put(0, "O");
        phase2shortphasename.put(1, "R");
        phase2shortphasename.put(2, "T");
        phase2shortphasename.put(3, "KT");
        phase2shortphasename.put(4, "L");
        phase2shortphasename.put(5, "A");
        phase2shortphasename.put(6, "F");
        langId2langname = new HashMap<Integer, String>();
        langId2langname.put(Languages.L_GERMAN, "Deutsch");
        langId2langname.put(Languages.L_FRENCH, "Französisch");
        langId2langname.put(Languages.L_ITALIAN, "Italienisch");
        langId2langname.put(Languages.L_ENGLISH, "Englisch");
        langId2langname.put(Languages.L_SWEDISH, "Schwedisch");
        langId2langname.put(Languages.L_SPANISH, "Spanisch");
        langId2langname.put(Languages.L_RUSSIAN, "Russisch");
        langId2langname.put(Languages.L_ARABIC, "Arabisch");
    }

    public String getTaskName(Integer phase, Integer language) {
        if (phase == 0) {
            return "Originaltext in " + langId2langname.get(language) + " erstellen";
        } else if (phase == 1) {
            return langId2langname.get(language) + "er Text revidieren";
        } else if (phase == 2) {
            return "Text ins " + langId2langname.get(language) + " übersetzen";
        } else if (phase == 3) {
            return langId2langname.get(language) + "er Text korrigieren";
        } else if (phase == 4) {
            return "Layouten";
        } else if (phase == 5) {
            return "Akzeptieren";
        } else if (phase == 6) {
            return "Fertig";
        } else {
            throw new RuntimeException("Unknown phase: " + phase + "!");
        }
    }

    public String getLanguageCode() {
        return "de";
    }

    public String login() {
        return "Login";
    }

    public String logout() {
        return "Logout";
    }

    public String go() {
        return "Los";
    }

    public String download() {
        return "Download";
    }

    public String add() {
        return "Hinzufügen";
    }

    public String delete() {
        return "Löschen";
    }

    public String save() {
        return "Speichern";
    }

    public String welcome() {
        return "Willkommen";
    }

    public String hello() {
        return "Hallo";
    }

    public String myTasks() {
        return "Meine Aufgaben";
    }

    public String activeArticles() {
        return "Aktive Artikel";
    }

    public String todaysArticles() {
        return "Artikel von heute";
    }

    public String dailyOverview() {
        return "Tagesübersicht";
    }

    public String articlesByCategory() {
        return "Artikel nach Kategorie";
    }

    public String users() {
        return "Benutzer";
    }

    public String categories() {
        return "Kategorien";
    }

    public String stats() {
        return "Statistiken";
    }

    public String articles() {
        return "Artikel Planung";
    }

    public String createArticle() {
        return "Artikel Erstellen";
    }

    public String createTemplate() {
        return "Vorlage Erstellen";
    }

    public String deleteArticle() {
        return "Artikel Löschen";
    }

    public String date() {
        return "Datum";
    }

    public String category() {
        return "Kategorie";
    }

    public String article() {
        return "Artikel";
    }

    public String createText() {
        return "Originaltext Erstellen";
    }

    public String reviseText() {
        return "Text Revidieren";
    }

    public String translateOrSummariesText() {
        return "Übersetzen und/oder Zusammenfassen";
    }

    public String correctText() {
        return "Korrigieren";
    }

    public String layout() {
        return "Layouten";
    }

    public String accept() {
        return "Akzeptieren";
    }

    public String done() {
        return "Erledigt";
    }

    public String yes() {
        return "Ja";
    }

    public String no() {
        return "Nein";
    }

    public String user() {
        return "Benutzer";
    }

    public String showCompleted() {
        return "Erfüllte Aufgaben Anzeigen";
    }

    public String showNotCompleted() {
        return "Noch Nicht Erfüllte Aufgaben Anzeigen";
    }

    public String noArticles() {
        return "Du hast keine nicht erfüllten Aufgaben. Judihui!";
    }

    public String count() {
        return "Anzahl";
    }

    public String username() {
        return "Benutzername";
    }

    public String password() {
        return "Passwort";
    }

    public String isAdmin() {
        return "Ist Admin";
    }

    public String isEditor() {
        return "Ist Redaktor";
    }

    public String nickname() {
        return "Vulgo";
    }

    public String firstname() {
        return "Vorname";
    }

    public String lastname() {
        return "Nachname";
    }

    public String language() {
        return "Sprache";
    }

    public String address() {
        return "Adresse";
    }

    public String email() {
        return "Email";
    }

    public String mobile() {
        return "Handy";
    }

    public String picture() {
        return "Bild";
    }

    public String show() {
        return "Anzeigen";
    }

    public String name() {
        return "Name";
    }

    public String from() {
        return "Von";
    }

    public String to() {
        return "Bis";
    }

    public String showArticleLinks() {
        return "Artikel Links Anzeigen";
    }

    public String showTotalsOnly() {
        return "Nur Totale Anzeigen";
    }

    public String total() {
        return "Total";
    }

    public String id() {
        return "Id";
    }

    public String description() {
        return "Beschreibung";
    }

    public String publicationDate() {
        return "Veröffentlichungs Datum";
    }

    public String numcharsTitle() {
        return "Anzahl Zeichen im Titel";
    }

    public String numcharsLead() {
        return "Anzahl Zeichen im Lead";
    }

    public String numcharsText() {
        return "Anzahl Zeichen im Text";
    }

    public String location() {
        return "Ort";
    }

    public String reqtime() {
        return "Geschätzte Zeit in Minuten";
    }

    public String editTasks() {
        return "Aufgaben Bearbeiten";
    }

    public String notifyUsers() {
        return "Benachrichtigung senden";
    }

    public String tasks() {
        return "Aufgaben";
    }

    public String notdone() {
        return "Nicht erledigt";
    }

    public String title() {
        return "Titel";
    }

    public String lead() {
        return "Lead";
    }

    public String text() {
        return "Text";
    }

    public String attachments() {
        return "Anhänge";
    }

    public String characters() {
        return "Zeichen";
    }

    public String uploadFile() {
        return "Datei Hochladen";
    }

    public String forward() {
        return "Artikel Weitergeben";
    }

    public String backward() {
        return "Artikel zurückgeben";
    }

    public String error() {
        return "Fehler";
    }

    public String undefined() {
        return "Undefiniert";
    }

    public String notificationSuccessful(String receiverStr) {
        return "Die Benachrichtigung wurde erfolgreich an " + receiverStr + " verschickt.";
    }

    public String notificationFailed(String receiverStr, String problemDesc) {
        return "Beim Verschicken der Benachrichtigung an " + receiverStr + " ist ein Problem aufgetreten: " + problemDesc;
    }

    public String emailTaskWaitingSubject() {
        return "Etwas zu tun";
    }

    public String emailTaskWaitingBody(String nickname, String articleDesc, String href) {
        StringBuffer buf = new StringBuffer();
        buf.append("Hallo ").append(nickname).append(",\n");
        buf.append("\n");
        buf.append("Eine Aufgabe für den Artikel '").append(articleDesc).append("' wartet auf dich:\n");
        buf.append("\n");
        buf.append(href + "\n");
        buf.append("\n");
        buf.append("Vielen Dank und viele Grüsse,\n");
        buf.append("Articlefox");
        return buf.toString();
    }

    public String templates() {
        return "Vorlagen";
    }

    public String template() {
        return "Vorlage";
    }

    public String texts() {
        return "Texte";
    }
}
