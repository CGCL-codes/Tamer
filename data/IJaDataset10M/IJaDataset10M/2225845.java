package sk.tuke.ess.editor.base.components.logger;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Logger aplikácie. Spravuje informačné, varovné a chybové hlásenia.
 *
 * @author Ján Čabala
 */
public class Logger {

    /**
     * Zoznam správ typu {@link Log}
     */
    private List<Log> logs;

    /**
     * Odkaz na aktuálnu inštanciu loggera
     */
    private static Logger instance;

    private boolean debugMode;

    private List<LoggerListener> loggerListenerList;

    /**
     * Vytvorý novú inštanciu loggera. Pre správny beh aplikácie je potrebné
     * vytvoriť ju len raz.
     */
    private Logger() {
        logs = new ArrayList<Log>();
        debugMode = false;
        loggerListenerList = new ArrayList<LoggerListener>();
    }

    /**
     * Pridá nové hlásenie
     *
     * @param typ typ hlásenia (informácia, varovanie alebo chyba)
     * @param sprava text správy
     */
    private void addLog(LogTyp typ, String sprava) {
        Log newLog = new Log(new Date(), typ, sprava);
        logs.add(newLog);
        updateListeners(newLog);
        System.out.println(newLog);
    }

    private void updateListeners(Log log) {
        for (LoggerListener loggerListener : loggerListenerList) {
            loggerListener.logAdded(log);
        }
    }

    public void addLoggerListener(LoggerListener loggerListener) {
        loggerListenerList.add(loggerListener);
    }

    /**
     * Pridá nové varovanie
     *
     * @param sprava text varovania
     */
    public void addWarning(String sprava) {
        addLog(Logger.LogTyp.Varovanie, sprava);
    }

    /**
     * Pridá nové varovanie s možnosťou formátovania textu správy.
     * Pre informácie o formátovaní textu pozrite {@link java.util.Formatter}
     *
     * @param spravaFormat formátový reťazec (text správy)
     * @param args argumenty formátového reťazca
     */
    public void addWarning(String spravaFormat, Object... args) {
        addWarning(String.format(spravaFormat, args));
    }

    /**
     * Pridá nové chybové hlásenie
     *
     * @param sprava text srávy
     */
    public void addError(String sprava) {
        addLog(Logger.LogTyp.Chyba, sprava);
    }

    /**
     * Pridá nové chybové hlásenie s možnosťou formátovania textu správy.
     * Pre informácie o formátovaní textu pozrite {@link java.util.Formatter}
     *
     * @param spravaFormat formátový reťazec (text správy)
     * @param args argumenty formátového reťazca
     */
    public void addError(String spravaFormat, Object... args) {
        addError(String.format(spravaFormat, args));
    }

    /**
     * Pridá nové informačné hlásenie
     *
     * @param sprava text správy
     */
    public void addInfo(String sprava) {
        addLog(Logger.LogTyp.Informacia, sprava);
    }

    /**
     * Pridá nové informačné hlásenie s možnosťou formátovania textu správy.
     * Pre informácie o formátovaní textu pozrite {@link java.util.Formatter}
     *
     * @param spravaFormat formátový reťazec (text správy)
     * @param args argumenty formátového reťazca
     */
    public void addInfo(String spravaFormat, Object... args) {
        addInfo(String.format(spravaFormat, args));
    }

    /**
     * Pridá nové ladiace hlásenie
     *
     * @param sprava text správy
     */
    public void addDebug(String sprava) {
        addLog(LogTyp.Debug, sprava);
    }

    /**
     * Pridá nové ladiace hlásenie s možnosťou formátovania textu správy.
     * Pre informácie o formátovaní textu pozrite {@link java.util.Formatter}
     *
     * @param spravaFormat  formátový reťazec (text správy)
     * @param args argumenty formátového reťazca
     */
    public void addDebug(String spravaFormat, Object... args) {
        addDebug(String.format(spravaFormat, args));
    }

    /**
     * Pridá ladiace hlásenie spolu s výnimkou
     *
     * @param e výnimka
     * @param sprava  sprava
     */
    public void addException(Throwable e, String sprava) {
        addDebug("%s (%s: %s)", sprava, e.getClass(), e.getMessage());
        for (StackTraceElement ste : e.getStackTrace()) {
            addDebug(ste.toString());
        }
    }

    /**
     * Pridá ladiace hlásenie spolu s výnimkov s možnosťou formatovania textu
     * @param e výnimka
     * @param spravaFormat  formátový reťazec (text správy)
     * @param args argumenty formátového reťazca
     */
    public void addException(Throwable e, String spravaFormat, Object... args) {
        addException(e, String.format(spravaFormat, args));
    }

    /**
     * Vracia počet celkový počet hlásení
     *
     * @return počet hlásení
     */
    public int getLogsCount() {
        return logs.size();
    }

    /**
     * Vracia hlásenie podľa jeho indexu v zozname hlásení
     *
     * @param index index hlásenia
     * @return objekt hlásenia
     */
    public Log getLog(int index) {
        return logs.get(index);
    }

    /**
     * Zmaže všetky doterajšie hlásenia
     */
    public void clear() {
        logs.clear();
    }

    /**
     * Uloží hlásenia do súboru
     *
     * @param file súbor do ktorého budú uložené hlásenia
     */
    public void save(File file) {
        try {
            String ls = System.getProperty("line.separator");
            FileWriter fw = new FileWriter(file, true);
            fw.write(String.format("%s%sLog aplikácie BP (%s)%s%s%s", ls, ls, new SimpleDateFormat("dd.MM.yyyy").format(new Date()), ls, "------------------------------", ls));
            for (Log log : logs) {
                fw.write(String.format("%s%s - %-10s >> %s", ls, new SimpleDateFormat("HH:mm:ss.SSSS").format(log.date), log.typ, log.getSpravaWithoutHTMLTags()));
            }
            fw.flush();
            addInfo("Log bol uložený do súboru <b>%s</b>", file.getPath());
        } catch (Exception ex) {
            addError("Nepodarilo sa uložiť log do súboru <b>%s</b>", file.getPath());
        }
    }

    /**
     * Vracia aktuálnu inštanciu loggera
     *
     * @return aktuálna inštancia loggera
     */
    public static Logger getLogger() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        addDebug("Debug %s", debugMode ? "on" : "off");
    }

    /**
     * Reprezentuje hlásenie
     */
    public class Log {

        /**
         * Dátum a čas kedy bolo hlásenie pridané
         */
        public Date date;

        /**
         * Typ hlásenia
         */
        public LogTyp typ;

        /**
         * Text hlásenia
         */
        public String sprava;

        /**
         * Vytorí nové hlásenie
         *
         * @param date dátum a čas hlásenia (popísanej situácie)
         * @param typ typ hlásenia
         * @param sprava text hlásenia
         */
        public Log(Date date, LogTyp typ, String sprava) {
            this.date = date;
            this.typ = typ;
            this.sprava = sprava;
        }

        public String getSpravaWithoutHTMLTags() {
            return sprava.replaceAll("\\<[^>]*>", "");
        }

        @Override
        public String toString() {
            return String.format("%1$td.%1$tm.%1$tY  %1$tH:%1$tM:%1$tS.%1$tL - %2$s >>>> %3$s", date, typ, getSpravaWithoutHTMLTags());
        }
    }

    /**
     * Typ hlásenia
     */
    public enum LogTyp {

        /**
         * Informačné hlásenie
         */
        Informacia, /**
         * Varovanie, menej závažná chyba alebo upozornenie na dôležitú situáciu
         */
        Varovanie, /**
         * Chybové hlásenie
         */
        Chyba, /**
         * Ladiace hlásenie
         */
        Debug
    }

    public interface LoggerListener {

        public void logAdded(Log log);
    }
}
