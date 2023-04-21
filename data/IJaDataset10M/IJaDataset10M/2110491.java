package org.jsemantic.services.r4spring.impl;

import java.util.Collection;
import java.util.Map;
import org.jsemantic.core.knowledgedbfactory.KnowledgeDBFactory;
import org.jsemantic.core.session.SemanticSession;
import org.jsemantic.core.session.exception.SemanticException;
import org.jsemantic.core.session.factory.SemanticSessionFactory;
import org.jsemantic.jservice.core.component.exception.ComponentException;
import org.jsemantic.jservice.core.service.exception.ServiceException;
import org.jsemantic.jservice.core.service.skeletal.AbstractManagedService;
import org.jsemantic.services.r4spring.R4SpringService;

public abstract class R4SpringServiceSkeletal extends AbstractManagedService implements R4SpringService {

    private SemanticSessionFactory semanticSessionFactory = null;

    @Override
    protected void startService() throws ServiceException {
    }

    @Override
    protected void stopService() throws ServiceException {
    }

    @Override
    protected void release() throws ComponentException {
        this.semanticSessionFactory = null;
    }

    public void setSemanticSessionFactory(SemanticSessionFactory semanticSessionFactory) {
        this.semanticSessionFactory = semanticSessionFactory;
    }

    public void setKnowledgeDBFactory(KnowledgeDBFactory knowledgeFactory) {
        this.semanticSessionFactory.setKnowledgeDBFactory(knowledgeFactory);
    }

    public void setSessionVariables(Map<?, ?> sessionVariables) {
        this.semanticSessionFactory.setSessionVariables(sessionVariables);
    }

    public Collection<?> execute(Object facts) {
        SemanticSession session = getInstance();
        Collection<?> results = null;
        try {
            results = session.execute(facts);
        } catch (SemanticException e) {
        } catch (Exception e) {
        } finally {
            if (session != null) {
                session.dispose();
                session = null;
            }
        }
        return results;
    }

    public Collection<?> execute(Collection<?> facts) {
        SemanticSession session = getInstance();
        Collection<?> results = null;
        try {
            results = session.execute(facts);
        } catch (SemanticException e) {
        } catch (Exception e) {
        } finally {
            if (session != null) {
                session.dispose();
                session = null;
            }
        }
        return results;
    }

    protected SemanticSession getInstance() {
        return this.semanticSessionFactory.getInstance();
    }
}
