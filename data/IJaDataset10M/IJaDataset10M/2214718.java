package org.apache.camel.quickfix;

import org.apache.camel.CamelContext;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.Service;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.util.ObjectHelper;
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.FieldNotFound;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.UnsupportedMessageType;
import java.io.InputStream;

/**
 * QuickfixEndpoint is the commpon class for quickfix endpoints
 * <p/>
 * Usage example:
 * <p/>
 * from("quickfix-server:acceptor.cfg[?params]").to("someBean", "someMethod").to("quickfix-client:initiator.cfg[?params]");
 * <p/>
 *
 * @author Anton Arhipov
 * @see org.apache.camel.quickfix.QuickfixInitiator
 * @see org.apache.camel.quickfix.QuickfixAcceptor
 */
public abstract class QuickfixEndpoint extends DefaultEndpoint implements Service {

    private static final Logger log = Logger.getLogger(QuickfixEndpoint.class);

    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            public void run() {
                try {
                    stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private boolean strict;

    private SessionID sessionID;

    private String configuration;

    private LogFactory logFactory;

    private MessageStoreFactory messageStoreFactory;

    private Processor processor = new QuickfixProcessor();

    private QuickfixApplication application;

    public QuickfixEndpoint(String uri, CamelContext context, String configuration) {
        super(uri, context);
        this.configuration = configuration;
    }

    public boolean isSingleton() {
        return true;
    }

    public void onMessage(Message message) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        Exchange exchange = createExchange(message);
        try {
            processor.process(exchange);
        } catch (FieldNotFound e) {
            throw e;
        } catch (IncorrectDataFormat e) {
            throw e;
        } catch (IncorrectTagValue e) {
            throw e;
        } catch (UnsupportedMessageType e) {
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected exception encountered in onMessage()", e);
            throw e;
        } catch (Throwable e) {
            log.error("Unexpected exception encountered in onMessage()", e);
            throw new RuntimeException("Unexpected exception encountered in onMessage()", e);
        }
    }

    public Exchange createExchange(Message message) {
        setExchangePattern(ExchangePattern.InOut);
        Exchange answer = createExchange();
        answer.getIn().setBody(message);
        answer.getOut().setBody(message);
        return answer;
    }

    public Producer createProducer() throws Exception {
        return new QuickfixProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        this.processor = processor;
        return new QuickfixConsumer(this, processor);
    }

    public SessionID getSessionID() {
        return sessionID;
    }

    public void setSessionID(SessionID sessionID) {
        this.sessionID = sessionID;
    }

    public Session getSession() {
        return Session.lookupSession(sessionID);
    }

    public void stop() throws Exception {
        Session session = Session.lookupSession(sessionID);
        if (session != null) {
            session.disconnect();
        }
    }

    public void start() throws Exception {
        Resource configResource = getResource();
        InputStream inputStream = configResource.getInputStream();
        ObjectHelper.notNull(inputStream, "Could not load " + configuration);
        SessionSettings settings = new SessionSettings(inputStream);
        MessageStoreFactory storeFactory = createMessageStoreFactory(settings);
        LogFactory logFactory = createLogFactory(settings);
        createApplication();
        start(application, storeFactory, settings, logFactory);
    }

    protected abstract void start(Application application, MessageStoreFactory storeFactory, SessionSettings settings, LogFactory logFactory) throws ConfigError;

    private void createApplication() {
        if (application == null) {
            application = new QuickfixApplication(this);
        } else {
            application.setEndpoint(this);
        }
    }

    private LogFactory createLogFactory(SessionSettings settings) {
        if (this.logFactory == null) {
            if (!strict) {
                logFactory = new ScreenLogFactory(settings);
            } else {
                throw new RuntimeException("The strict option is switched on. " + "You should either inject the required logging factory via spring context, " + "or specify the logging factory parameters via endpoint URI");
            }
        }
        return logFactory;
    }

    private MessageStoreFactory createMessageStoreFactory(SessionSettings settings) {
        if (this.messageStoreFactory == null) {
            if (!strict) {
                messageStoreFactory = new FileStoreFactory(settings);
            } else {
                throw new RuntimeException("The strict option is switched on. " + "You should either inject the required logging factory via spring context, " + "or specify the logging factory parameters via endpoint URI");
            }
        }
        return messageStoreFactory;
    }

    public void setLogFactory(LogFactory logFactory) {
        this.logFactory = logFactory;
    }

    public void setMessageStoreFactory(MessageStoreFactory messageStoreFactory) {
        this.messageStoreFactory = messageStoreFactory;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    private Resource getResource() {
        ResourceLoader loader = new DefaultResourceLoader();
        return loader.getResource(this.configuration);
    }
}
