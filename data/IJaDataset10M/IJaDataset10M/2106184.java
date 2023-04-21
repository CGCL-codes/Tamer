package atg.util.service;

import java.util.logging.Level;
import atg.service.constante.AtgConstantes;
import atg.service.log.AtgLogFormatter;
import atg.service.log.AtgLogManager;

/**
 * <p>
 * Titre : Classe m�re des classes basiques du framework
 * </p>
 * <p>
 * Description : Cette classe est h�rit�e par tous les futures classes du
 * framework.
 * </p>
 * <p>
 * Copyright : FERRARI Olivier
 * </p>
 * 
 * @author FERRARI Olivier
 * @version 1.0 Ce logiciel est r�gi par la licence CeCILL soumise au droit
 *          fran�ais et respectant les principes de diffusion des logiciels
 *          libres. Vous pouvez utiliser, modifier et/ou redistribuer ce
 *          programme sous les conditions de la licence CeCILL telle que
 *          diffus�e par le CEA, le CNRS et l'INRIA sur le site
 *          http://www.cecill.info.
 * 
 * Le fait que vous puissiez acc�der � cet en-t�te signifie que vous avez pris
 * connaissance de la licence CeCILL, et que vous en avez accept� les termes.
 */
public abstract class ATGBasicStaticClass extends SecurityManager {

    private static class CurrentClassGetter extends SecurityManager {

        public String getClassName() {
            return getClassContext()[3].getName();
        }
    }

    /**
     * Contient le log associ� java.util.logging.Logger Log associ�
     */
    protected static java.util.logging.Logger logger_ = null;

    public ATGBasicStaticClass() {
    }

    /**
     * Ecriture des logs
     */
    protected static java.util.logging.Logger getLogger() {
        if (logger_ == null) logger_ = AtgLogManager.getLog(AtgConstantes.ATG_LOG_CATEGORY_SERVICEMETIER_STATIC);
        return logger_;
    }

    private static void log(Level level, String message) {
        try {
            if (getLogger() != null) {
                getLogger().logp(level, new CurrentClassGetter().getClassName(), AtgLogFormatter.getCallerMethod(), message);
            }
        } catch (Exception e) {
            java.util.logging.Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, "Impossible d'utiliser les logs", e);
        }
    }

    private static void log(Level level, String message, Throwable thrown) {
        try {
            if (getLogger() != null) {
                getLogger().logp(level, new CurrentClassGetter().getClassName(), AtgLogFormatter.getCallerMethod(), message, thrown);
            }
        } catch (Exception e) {
            java.util.logging.Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, "Impossible d'utiliser les logs", e);
        }
    }

    /**
     * Ecrit une trace de niveau 'severe'
     * 
     * @param message
     *            Message d'information
     */
    public static void logSevere(String message) {
        log(java.util.logging.Level.SEVERE, message);
    }

    /**
     * Ecrit une trace de niveau 'config'
     * 
     * @param message
     *            Message d'information
     */
    public static void logConfig(String message) {
        log(java.util.logging.Level.CONFIG, message);
    }

    /**
     * Ecrit une trace de niveau 'warning'
     * 
     * @param message
     *            Message d'information
     */
    public static void logWarning(String message) {
        log(java.util.logging.Level.WARNING, message);
    }

    /**
     * Ecrit une trace de niveau 'fine'
     * 
     * @param message
     *            Message d'information
     */
    public static void logFine(String message) {
        log(java.util.logging.Level.FINE, message);
    }

    /**
     * Ecrit une trace de niveau 'finer'
     * 
     * @param message
     *            Message d'information
     */
    public static void logFiner(String message) {
        log(java.util.logging.Level.FINER, message);
    }

    /**
     * Ecrit une trace de niveau 'finest'
     * 
     * @param message
     *            Message d'information
     */
    public static void logFinest(String message) {
        log(java.util.logging.Level.FINEST, message);
    }

    /**
     * Ecrit une trace de niveau 'info'
     * 
     * @param message
     *            Message d'information
     */
    public static void logInfo(String message) {
        log(java.util.logging.Level.INFO, message);
    }
}
