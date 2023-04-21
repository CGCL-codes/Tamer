package org.lokee.punchcard.persistence;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.lokee.punchcard.PunchCardUtil;
import org.lokee.punchcard.config.CardConfig;
import org.lokee.punchcard.config.DeckConfig;
import org.lokee.punchcard.config.IPunchCardConfig;
import org.lokee.punchcard.util.ILogSeverity;
import org.lokee.punchcard.util.Utilities;

/**
 * @author CLaguerre
 * Responsible for discovering the implementation for the IPersistentManager
 */
public abstract class PersistentManagerFactory {

    private static Map<String, IAuditCardFormat> auditCardFormatMap = new HashMap<String, IAuditCardFormat>();

    private static Map<String, IAuditManager> auditManagerMap = new HashMap<String, IAuditManager>();

    private static Map<String, IPersistentManager> persistentManagerMap = new HashMap<String, IPersistentManager>();

    private static Map<String, ICardHandler> cardHandlerMap = new HashMap<String, ICardHandler>();

    /**
	 * The implementation can be card or deck specific.  This way all cards don't
	 * have to share the same persistent manager.
	 * getInstance
	 * @param punchcardConfig
	 * @return
	 * @throws PersistentException
	 */
    public static IPersistentManager getInstance(IPunchCardConfig punchcardConfig[]) throws PersistentException {
        try {
            return getPersistentManager(punchcardConfig);
        } catch (ClassNotFoundException e) {
            throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to find implementation for(" + IPersistentManager.class.getName() + ")", ILogSeverity.FATAL, e);
        } catch (IllegalAccessException e) {
            throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to access implementation for(" + IPersistentManager.class.getName() + ")", ILogSeverity.FATAL, e);
        } catch (InstantiationException e) {
            throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to instantiate implementation for(" + IPersistentManager.class.getName() + ")", ILogSeverity.FATAL, e);
        }
    }

    public static IAuditManager getAuditInstance(IPunchCardConfig punchcardConfig[]) throws PersistentException {
        try {
            return getAuditManager(punchcardConfig);
        } catch (ClassNotFoundException e) {
            throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to find implementation for(" + IPersistentManager.class.getName() + ")", ILogSeverity.FATAL, e);
        } catch (IllegalAccessException e) {
            throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to access implementation for(" + IPersistentManager.class.getName() + ")", ILogSeverity.FATAL, e);
        } catch (InstantiationException e) {
            throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to instantiate implementation for(" + IPersistentManager.class.getName() + ")", ILogSeverity.FATAL, e);
        }
    }

    public static IAuditCardFormat getAuditCardFormatInstance(IPunchCardConfig punchcardConfig[]) throws PersistentException {
        try {
            return getAuditCardFormat(punchcardConfig);
        } catch (ClassNotFoundException e) {
            throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to find implementation for(" + IPersistentManager.class.getName() + ")", ILogSeverity.FATAL, e);
        } catch (IllegalAccessException e) {
            throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to access implementation for(" + IPersistentManager.class.getName() + ")", ILogSeverity.FATAL, e);
        } catch (InstantiationException e) {
            throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to instantiate implementation for(" + IPersistentManager.class.getName() + ")", ILogSeverity.FATAL, e);
        }
    }

    /**
	 * Fetching the handler (adapter/pluging) to handle the card.
	 * The handler can be card or deck specific.  This way each card may have
	 * it's own handler.
	 * fetchCardHandler
	 * @param cardConfig
	 * @param deckConfig
	 * @return
	 * @throws PersistentException
	 */
    public static ICardHandler fetchCardHandler(CardConfig cardConfig, DeckConfig deckConfig) throws PersistentException {
        String className = fetchClassName(cardConfig, deckConfig);
        ICardHandler handler = (ICardHandler) cardHandlerMap.get(className);
        if (handler == null) {
            try {
                handler = getCardHandler(className);
                cardHandlerMap.put(className, handler);
            } catch (ClassNotFoundException e) {
                throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to find implementation for(" + className + ")", ILogSeverity.FATAL, e);
            } catch (IllegalAccessException e) {
                throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to access implementation for(" + className + ")", ILogSeverity.FATAL, e);
            } catch (InstantiationException e) {
                throw new PersistentException(IPersistentMessages.E_ERROR_GETTING_INSTANCE + ": unable to instantiate implementation for(" + className + ")", ILogSeverity.FATAL, e);
            }
        }
        return handler;
    }

    private static IPersistentManager getPersistentManager(IPunchCardConfig punchcardConfig[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = PunchCardUtil.findProperty(IPersistentManager.class.getName(), punchcardConfig, null);
        IPersistentManager persistentManager = (IPersistentManager) persistentManagerMap.get(className);
        if (persistentManager == null) {
            Class<IPersistentManager> clazz = (Class<IPersistentManager>) Class.forName(className);
            persistentManager = (IPersistentManager) clazz.newInstance();
            persistentManagerMap.put(className, persistentManager);
        }
        return persistentManager;
    }

    private static IAuditCardFormat getAuditCardFormat(IPunchCardConfig punchcardConfig[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = PunchCardUtil.findProperty(IAuditCardFormat.class.getName(), punchcardConfig, null);
        IAuditCardFormat auditCardFormat = (IAuditCardFormat) auditCardFormatMap.get(className);
        if (auditCardFormat == null) {
            Class<IAuditManager> clazz = (Class<IAuditManager>) Class.forName(className);
            auditCardFormat = (IAuditCardFormat) clazz.newInstance();
            auditCardFormatMap.put(className, auditCardFormat);
        }
        return auditCardFormat;
    }

    private static IAuditManager getAuditManager(IPunchCardConfig punchcardConfig[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = PunchCardUtil.findProperty(IAuditManager.class.getName(), punchcardConfig, null);
        if (StringUtils.isBlank(className)) {
            return null;
        }
        IAuditManager auditManager = (IAuditManager) auditManagerMap.get(className);
        if (auditManager == null) {
            Class<IAuditManager> clazz = (Class<IAuditManager>) Class.forName(className);
            auditManager = (IAuditManager) clazz.newInstance();
            auditManagerMap.put(className, auditManager);
        }
        return auditManager;
    }

    private static String fetchClassName(CardConfig cardConfig, DeckConfig deckConfig) {
        if (StringUtils.isNotBlank(cardConfig.getHandlerClassName())) {
            return cardConfig.getHandlerClassName();
        }
        return PunchCardUtil.findProperty(cardConfig.getClass().getName(), new IPunchCardConfig[] { cardConfig, deckConfig });
    }

    private static ICardHandler getCardHandler(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<ICardHandler> clazz = (Class<ICardHandler>) Class.forName(className);
        Utilities.getLogger(PersistentManagerFactory.class).debug("HandlerClassName::" + className);
        return (ICardHandler) clazz.newInstance();
    }
}
