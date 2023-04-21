package com.mindquarry.persistence.jcr;

import javax.jcr.RepositoryException;
import org.springmodules.jcr.JcrSessionFactory;
import com.mindquarry.common.init.InitializationException;
import com.mindquarry.persistence.api.Configuration;
import com.mindquarry.persistence.api.SessionFactory;
import com.mindquarry.persistence.jcr.cmds.CommandProcessor;
import com.mindquarry.persistence.jcr.model.Model;
import com.mindquarry.persistence.jcr.query.QueryResolver;
import com.mindquarry.persistence.jcr.trafo.TransformationManager;

/**
 * This class is the entry point for the entire jcr persistence component.
 * Threads define the context for Sessions instances. Therefore each session 
 * is hold within a ThreadLocal field.
 * Furthermore this class creates and all subcomponents and 
 * manages access to them.
 *    
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Persistence implements SessionFactory {

    public static final String ROLE = Persistence.class.getName();

    private JcrSessionFactory targetSessionFactory_;

    private Model model_;

    private QueryResolver queryResolver_;

    private CommandProcessor commandProcessor_;

    private TransformationManager transformationManager_;

    private ThreadLocal<Session> currentSession_;

    public Persistence() {
        currentSession_ = new ThreadLocal<Session>();
    }

    public void setTargetSessionFactory(JcrSessionFactory targetSessionFactory) {
        targetSessionFactory_ = targetSessionFactory;
    }

    private Configuration configuration_;

    private void configure() {
        clear();
        model_ = Model.buildFromClazzes(configuration_.getClasses());
        transformationManager_ = new TransformationManager(model_, this);
        transformationManager_.initialize();
        queryResolver_ = new QueryResolver();
        queryResolver_.initialize(configuration_);
        commandProcessor_ = new CommandProcessor(this);
    }

    public void addConfiguration(Configuration configuration) {
        if (configuration_ == null) {
            configuration_ = configuration;
        } else {
            configuration_.getClasses().addAll(configuration.getClasses());
            configuration_.getNamedQueries().putAll(configuration.getNamedQueries());
        }
        configure();
    }

    public QueryResolver getQueryResolver() {
        if (queryResolver_ == null) throw new InitializationException("query resolver is requested " + "though persistence component is not yet configured");
        return queryResolver_;
    }

    public CommandProcessor getCommandProcessor() {
        if (commandProcessor_ == null) throw new InitializationException("CommandProcessor is requested " + "though persistence component is not yet configured");
        return commandProcessor_;
    }

    public TransformationManager getTransformationManager() {
        if (transformationManager_ == null) throw new InitializationException("TransformationManager is " + "requested though persistence component " + "is not yet configured");
        return transformationManager_;
    }

    public Model getModel() {
        if (model_ == null) throw new InitializationException("Model is " + "requested though persistence component " + "is not yet configured");
        return model_;
    }

    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    public Session currentSession() {
        if (currentSession_.get() == null) {
            currentSession_.set(buildJcrSession());
        }
        return currentSession_.get();
    }

    public void clear() {
        currentSession_.set(null);
    }

    private Session buildJcrSession() {
        try {
            return new Session(this, targetSessionFactory_.getSession());
        } catch (RepositoryException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }
}
