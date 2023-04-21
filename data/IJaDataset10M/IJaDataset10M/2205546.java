package net.scharlie.lumberjack4logs.io;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Level;

/**
 * Die Klasse <code>LogLevelHelper</code> laedt die Klasse mit den log4j-Levels
 * und stellt einen String-2-Level-Konverter bereit.
 * 
 * @author Bernd Scharlemann
 */
public class LogLevelHelper {

    /**
     * Name der System-Property, um eine andere Level-Klasse zu setzen als
     * <code>org.apache.log4j.Level</code>.
     */
    public static final String LEVELCLASS_PROP_NAME = "net.scharlie.lumberjack4logs.levelclass";

    /**
     * Level-Klasse.
     */
    private static Class<?> sLevelClass;

    /**
     * Methode, um einen String in ein log4j-Level umzuwandeln.
     */
    private static Method sString2LevelMethod;

    /**
     * Pure static class.
     */
    private LogLevelHelper() {
    }

    /**
     * Antworte die zu verwendende Level-Klasse.
     * 
     * @return Level-Klasse
     */
    public static Class<?> getLevelClass() {
        if (sLevelClass == null) {
            String aClassName = null;
            Class<?> aClass = null;
            try {
                aClassName = System.getProperty(LEVELCLASS_PROP_NAME);
                if (aClassName != null) {
                    aClass = Class.forName(aClassName);
                }
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (aClass == null) {
                aClass = Level.class;
                aClassName = aClass.getName() + " (default)";
            }
            System.out.println("INFO:\t-D" + LEVELCLASS_PROP_NAME + "=" + aClassName);
            sLevelClass = aClass;
        }
        return sLevelClass;
    }

    /**
     * Wandle einen String in einen Log-Level um.
     * 
     * @param pLevelName
     *            Name des log4j-Levels
     * 
     * @return Log-Level
     */
    public static Level string2Level(final String pLevelName) {
        Level aLevel = null;
        if (pLevelName != null && pLevelName.length() > 0) {
            try {
                if (sString2LevelMethod == null) {
                    sString2LevelMethod = getLevelClass().getMethod("toLevel", new Class[] { String.class });
                }
                aLevel = (Level) sString2LevelMethod.invoke(null, new Object[] { pLevelName });
            } catch (final NoSuchMethodException e) {
                e.printStackTrace();
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            } catch (final InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (aLevel == null) {
            aLevel = Level.TRACE;
        }
        return aLevel;
    }
}
