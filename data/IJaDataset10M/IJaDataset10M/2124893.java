package de.usd.nova.compiler;

import java.util.Iterator;
import java.util.Vector;
import antlr.RecognitionException;

/**
 * nimmt Fehlermeldungen des Compilers auf und sammelt sie.
 * diese k�nnen hinterher aufbereitet abgerufen werden.
 * Dient zum einen zum Darstellen der Fehler in der IDE
 * Zum anderen soll es beim automatisierten Testen unterst�tzen
 * zum jetzigen Zeitpunkt werden nur Error vollst�ndig verwertet
 * Warnings werden nur aufgenommen, ohne dass sie von der IDE 
 * ausgewertet werden
 * @author JOetting
 */
public class Logger {

    private static boolean fatalError = false;

    private static boolean error = false;

    private static boolean warning = false;

    public static final int LEXER = 1;

    public static final int PARSER = 2;

    public static final int SEMANTICANALYSE = 3;

    public static final int TRANSFORMER = 4;

    public static final int ASSEMBLIERER = 5;

    public static final int COMPILER = 6;

    public static final int GENERAL = 7;

    private static int nrError = 0;

    private static int nrWarning = 0;

    private static int firstErrorType = 0;

    private static Vector errorCollection = new Vector();

    private static Vector warningCollection = new Vector();

    private static boolean debug = true;

    /**
	 * gibt Nachrichten in die Console aus, wenn der Debug-Modus aktiviert ist
	 */
    public static void printDebuggingLine(String message) {
        if (debug) System.out.println(message);
    }

    /**
	 * nimmt Fehler auf
	 * @param ex
	 */
    public static void reportError(RecognitionException ex) {
        errorCollection.add(ex);
        error = true;
        nrError++;
    }

    /**
	* nimmt Fehler auf
	* teilweise werden Fehler gefiltert
	* das Kriterium daf�r lautet:existiert schon ein Fehler mit Positionsangabe, 
	* wird ein Fehler ohne Positionsangabe nicht angezeigt
	* Fehler ohne Positionsangabe sind in der Regel Folgefehler im sp�teren Compilerstadium,
	* mit dem der Nutzer wenig anzufangen wei�.
	* Ist es der erste Fehler, wird er trotzdem angezeigt, damit der Programmierer einen Anhaltspunkt hat
	* eigentlich kann ein solcher Fehler aber nicht der erste sein. 
	* @param ex
	*/
    public static void reportError(RecognitionException ex, int component) {
        if (ex.getLine() == -1) {
            if (errorOcurred()) return;
        }
        errorCollection.add(ex);
        error = true;
        nrError++;
        if (!errorOcurred()) firstErrorType = component;
    }

    /**
	* nimmt Fehler auf
	* @param ex
	*/
    public static void reportError(java.lang.String s) {
        errorCollection.add(s);
        error = true;
        nrError++;
    }

    /**
	* nimmt Fehler auf
	* @param ex
	*/
    public static void reportError(java.lang.String s, int component) {
        errorCollection.add(s);
        error = true;
        nrError++;
        if (!errorOcurred()) firstErrorType = component;
    }

    /**
	* nimmt Fehler auf
	* @param ex
	*/
    public static void reportWarning(java.lang.String s) {
        getWarningCollection().add(s);
        warning = true;
        nrWarning++;
    }

    /**
	* nimmt Fehler auf
	* @param ex
	*/
    public static void reportWarning(java.lang.String s, int component) {
        getWarningCollection().add(s);
        warning = true;
        nrWarning++;
        if (!errorOcurred()) firstErrorType = component;
    }

    /**
	* gibt alle Fehler und Warnungen aus
	*/
    public static void printLogs() {
        Iterator i = errorCollection.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            System.out.println("Fehler :" + o);
        }
        i = warningCollection.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            System.out.println("Fehler :" + o);
        }
        System.out.println("Anzahl Fehler : " + nrError);
        System.out.println("Anzahl Warnings : " + nrWarning);
    }

    public static boolean isFatalError() {
        return fatalError;
    }

    public static boolean isError() {
        return error;
    }

    public static boolean isWarning() {
        return warning;
    }

    public static int getNrError() {
        return nrError;
    }

    public static int getNrWarning() {
        return nrWarning;
    }

    public static Vector getExceptionCollection() {
        return errorCollection;
    }

    public static int getFirstErrorType() {
        return firstErrorType;
    }

    /**
	 * @return gibt true zur�ck, wenn es einen Fehler gibt, false sonst
	 */
    public static boolean hasError() {
        if (errorCollection.isEmpty()) return false; else return true;
    }

    /**
	 * gibt den ersten Fehler zur�ck
	 * null, wenn es keinen Fehler gibt
	 */
    public static Object getFirstError() {
        if (errorCollection.isEmpty()) return null;
        return errorCollection.elementAt(0);
    }

    /**
	 * ermittelt, ob bereits ein Fehler aufgetreten ist	 * 
	 * @return true, wenn Fehler bereits aufgetreten ist
	 */
    private static boolean errorOcurred() {
        if (firstErrorType == 0) return false; else return true;
    }

    public static Vector getWarningCollection() {
        return warningCollection;
    }

    /**
	 * geht alle Arten von Fehlern gesammelt zur�ck
	 * @return Error und Warnings in einem Vector
	 */
    public static Vector getErrorsAndWarnings() {
        Vector errorAndWarnings = new Vector(errorCollection);
        errorAndWarnings.add(warningCollection);
        return errorAndWarnings;
    }

    public static void resetLogger() {
        nrError = 0;
        nrWarning = 0;
        fatalError = false;
        error = false;
        warning = false;
        firstErrorType = 0;
        errorCollection.clear();
        warningCollection.clear();
    }
}
