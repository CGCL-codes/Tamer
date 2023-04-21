package org.jcompany.persistence;

import org.apache.log4j.Logger;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostLoadEventListener;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.event.PreDeleteEventListener;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreLoadEventListener;
import org.hibernate.event.PreUpdateEventListener;
import org.hibernate.event.def.DefaultPostLoadEventListener;
import org.hibernate.event.def.DefaultPreLoadEventListener;
import org.jcompany.commons.PlcException;
import org.jcompany.persistence.hibernate.PlcBaseHibernateListener;

/**
 * Configura listeners eventos
 *  
 * @author Pedro Henrique
 *
 */
public class PlcBaseManager {

    protected static Logger log = Logger.getLogger(PlcBaseManager.class);

    /**
     * jCompany 3.0 Declara listeners para todos os eventos, para aplica��o de l�gicas de seguran�a
     * @param nome, F�brica
     * @param cfg, Annotation Configuration que se� adicionado os listeners
     */
    protected void configuraListeners(String nome, AnnotationConfiguration cfg) throws PlcException {
        log.debug("############### Entrou em configuraListeners");
        PlcBaseHibernateListener baseHibernateListener = getListener();
        PreInsertEventListener[] preInsertL = { baseHibernateListener };
        cfg.getEventListeners().setPreInsertEventListeners(preInsertL);
        PreLoadEventListener[] preLoadL = { baseHibernateListener, new DefaultPreLoadEventListener() };
        cfg.getEventListeners().setPreLoadEventListeners(preLoadL);
        PreUpdateEventListener[] preUpdateL = { baseHibernateListener };
        cfg.getEventListeners().setPreUpdateEventListeners(preUpdateL);
        PreDeleteEventListener[] preDeleteL = { baseHibernateListener };
        cfg.getEventListeners().setPreDeleteEventListeners(preDeleteL);
        PostInsertEventListener[] postInsertL = { baseHibernateListener };
        cfg.getEventListeners().setPostInsertEventListeners(postInsertL);
        PostLoadEventListener[] postLoadL = { baseHibernateListener, new DefaultPostLoadEventListener() };
        cfg.getEventListeners().setPostLoadEventListeners(postLoadL);
        PostUpdateEventListener[] postUpdateL = { baseHibernateListener };
        cfg.getEventListeners().setPostUpdateEventListeners(postUpdateL);
        PostDeleteEventListener[] postDeleteL = { baseHibernateListener };
        cfg.getEventListeners().setPostDeleteEventListeners(postDeleteL);
        configuraListenersApos(nome, cfg, baseHibernateListener);
    }

    /**
	 * M�todo DP Template Method para registro de novos Listeners em classes descendentes.
	 * @since jCompany 3.0
	 * @param nome Nome da F�brica
	 * @param cfg Configuration Hibernate
	 * @param baseHibernateListener Listener default do jcompany
	 */
    protected void configuraListenersApos(String nome, AnnotationConfiguration cfg, PlcBaseHibernateListener baseHibernateListener) throws PlcException {
    }

    protected PlcBaseHibernateListener listener = null;

    /**
	   * @since jCompany 3.1
	   * Pseudo-ID. Classe que implementa eventos de Listener. Deve ser sobreposto para especializa��es do Listener
	   */
    protected PlcBaseHibernateListener getListener() {
        if (listener == null) listener = new PlcBaseHibernateListener();
        return listener;
    }
}
