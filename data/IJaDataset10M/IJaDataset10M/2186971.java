package com.googlecode.sarasvati.hib;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.event.ExecutionEventType;
import com.googlecode.sarasvati.event.ExecutionListener;
import com.googlecode.sarasvati.impl.BaseEngine;
import com.googlecode.sarasvati.load.GraphLoader;
import com.googlecode.sarasvati.load.GraphLoaderImpl;
import com.googlecode.sarasvati.load.GraphValidator;

public class HibEngine extends BaseEngine {

    protected Session session;

    protected HibGraphFactory factory;

    protected HibGraphRepository repository;

    /**
   * This constructor can be used in cases when the session will be set in later
   * or when performing operations that don't require a session, such as adding
   * global execution listeners. The HibEngine is created with the default
   * application context.
   * <p>
   * Each application context has it's own set of global listeners.
   *
   * This allows different applications running the same JVM to
   * have different sets of listeners without having to add
   * them at the process level.
   */
    public HibEngine() {
        super(DEFAULT_APPLICATION_CONTEXT);
    }

    /**
   * This constructor can be used in cases when the session will be set in later
   * or when performing operations that don't require a session, such as adding
   * global execution listeners. The HibEngine is created with the given
   * application context.
   * <p>
   * Each application context has it's own set of global listeners.
   *
   * This allows different applications running the same JVM to
   * have different sets of listeners without having to add
   * them at the process level.
   */
    public HibEngine(final String applicationContext) {
        super(applicationContext);
    }

    /**
   * Creates a new HibEngine with the default application context.
   * <p>
   * Each application context has it's own set of global listeners.
   *
   * This allows different applications running the same JVM to
   * have different sets of listeners without having to add
   * them at the process level.
   *
   * @param session The hibernate session.
   */
    public HibEngine(final Session session) {
        this(DEFAULT_APPLICATION_CONTEXT, session);
    }

    /**
   * Creates a new HibEngine with the given application context.
   * <p>
   * Each application context has it's own set of global listeners.
   *
   * This allows different applications running the same JVM to
   * have different sets of listeners without having to add
   * them at the process level.
   *
   * @param session The hibernate session.
   */
    public HibEngine(final String applicationContext, final Session session) {
        super(applicationContext);
        this.session = session;
        this.factory = new HibGraphFactory(session);
        this.repository = new HibGraphRepository(session);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(final Session session) {
        this.session = session;
        this.factory = new HibGraphFactory(session);
        this.repository = new HibGraphRepository(session);
    }

    @Override
    public HibGraphRepository getRepository() {
        return repository;
    }

    @Override
    public HibGraphFactory getFactory() {
        return factory;
    }

    @Override
    public GraphLoader<HibGraph> getLoader() {
        return getLoader(null);
    }

    @Override
    public GraphLoader<HibGraph> getLoader(final GraphValidator validator) {
        return new GraphLoaderImpl<HibGraph>(getFactory(), getRepository(), validator);
    }

    @Override
    public void addExecutionListener(final GraphProcess process, final Class<? extends ExecutionListener> listenerClass, final ExecutionEventType... eventTypes) {
        if (eventTypes == null || eventTypes.length == 0 || listenerClass == null) {
            return;
        }
        int eventTypeMask = 0;
        for (final ExecutionEventType eventType : eventTypes) {
            if (eventType != null) {
                eventTypeMask |= eventType.getEventType();
            }
        }
        if (eventTypeMask != 0) {
            final String type = listenerClass.getName();
            boolean updated = false;
            for (final HibProcessListener hibListener : ((HibGraphProcess) process).getListeners()) {
                if (type.equals(hibListener.getType())) {
                    hibListener.setEventTypeMask(eventTypeMask);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                final HibProcessListener hibListener = new HibProcessListener(type, eventTypeMask, process);
                session.save(hibListener);
            }
        }
        super.addExecutionListener(process, listenerClass, eventTypes);
    }

    @Override
    public void removeExecutionListener(final GraphProcess process, final Class<? extends ExecutionListener> listenerClass, final ExecutionEventType... eventTypes) {
        if (listenerClass == null) {
            return;
        }
        final int removeMask = ExecutionEventType.invertMask(ExecutionEventType.toMask(eventTypes));
        for (final HibProcessListener hibListener : ((HibGraphProcess) process).getListeners()) {
            if (listenerClass.getName().equals(hibListener.getType())) {
                if (eventTypes == null || eventTypes.length == 0) {
                    session.delete(hibListener);
                } else {
                    final int newMask = hibListener.getEventTypeMask() & removeMask;
                    if (newMask == 0) {
                        session.delete(hibListener);
                    } else {
                        hibListener.setEventTypeMask(newMask);
                    }
                }
            }
        }
        super.removeExecutionListener(process, listenerClass, eventTypes);
    }

    @Override
    public HibEngine newEngine() {
        final HibEngine engine = new HibEngine(applicationContext);
        engine.session = session;
        engine.factory = factory;
        engine.repository = repository;
        return engine;
    }

    @SuppressWarnings("unchecked")
    public List<ArcToken> getActiveArcTokens(final HibTokenSet tokenSet) {
        final String hql = "select token from HibArcToken token inner join token.tokenSetMemberships as setMember " + "where token.completeDate is null and setMember.tokenSet = :tokenSet";
        final Query query = session.createQuery(hql).setEntity("tokenSet", tokenSet);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<NodeToken> getActiveNodeTokens(final HibTokenSet tokenSet) {
        final String hql = "select token from HibNodeToken token inner join token.tokenSetMemberships as setMember " + "where token.completeDate is null and setMember.tokenSet = :tokenSet";
        final Query query = session.createQuery(hql).setEntity("tokenSet", tokenSet);
        return query.list();
    }

    public static void addToConfiguration(final AnnotationConfiguration config, final boolean enableCaching) {
        config.addAnnotatedClass(HibArc.class);
        config.addAnnotatedClass(HibArcToken.class);
        config.addAnnotatedClass(HibGraph.class);
        config.addAnnotatedClass(HibGraphListener.class);
        config.addAnnotatedClass(HibProcessListener.class);
        config.addAnnotatedClass(HibNode.class);
        config.addAnnotatedClass(HibNodeRef.class);
        config.addAnnotatedClass(HibNodeToken.class);
        config.addAnnotatedClass(HibGraphProcess.class);
        config.addAnnotatedClass(HibPropertyNode.class);
        config.addAnnotatedClass(HibCustomNodeWrapper.class);
        config.addAnnotatedClass(HibTokenSet.class);
        config.addAnnotatedClass(HibArcTokenSetMember.class);
        config.addAnnotatedClass(HibNodeTokenSetMember.class);
        config.addAnnotatedClass(HibNodeType.class);
        config.addAnnotatedClass(HibTokenSetMemberAttribute.class);
        config.addAnnotatedClass(HibExternal.class);
        if (enableCaching) {
            config.setCacheConcurrencyStrategy(HibGraph.class.getName(), "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraph.class.getName() + ".nodes", "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraph.class.getName() + ".arcs", "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraph.class.getName() + ".listeners", "read-write");
            config.setCacheConcurrencyStrategy(HibGraphListener.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibNode.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibNodeRef.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibArc.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibExternal.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibGraphProcess.class.getName(), "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraphProcess.class.getName() + ".listeners", "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraphProcess.class.getName() + ".activeArcTokens", "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraphProcess.class.getName() + ".activeNodeTokens", "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraphProcess.class.getName() + ".executionQueue", "read-write");
            config.setCacheConcurrencyStrategy(HibProcessListener.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibNodeToken.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibArcToken.class.getName(), "read-write");
        }
    }
}
