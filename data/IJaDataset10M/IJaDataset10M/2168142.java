package anima.factory.globalFactory;

import anima.component.IComponentFactory;
import anima.component.ISupports;
import anima.component.exception.ConnectorException;
import anima.connector.IAsyncConnector;
import anima.connector.IConnectorFactory;
import anima.connector.ISyncConnector;
import anima.context.IComponentContext;
import anima.factory.IGlobalFactory;
import anima.factory.exception.FactoryException;
import anima.message.IAsyncReceiver;
import anima.message.IAsyncSender;
import anima.message.IBroker;
import anima.message.IMessage;
import anima.message.IMessageFactory;
import anima.message.ISourceMessage;
import anima.message.ISyncReceiver;
import anima.message.ISyncSender;

public class GlobalFactory implements IGlobalFactory {

    IComponentContext componentContext;

    IComponentFactory componentFactory;

    IConnectorFactory connectorFactory;

    IMessageFactory messageFactory;

    IBroker broker;

    public GlobalFactory(IComponentContext componentContext) throws FactoryException {
        this.componentContext = componentContext;
        messageFactory = componentContext.createMessageFactory();
        broker = messageFactory.createBroker();
        componentFactory = componentContext.createComponentFactory();
        componentFactory.assignMessageFactory(messageFactory);
        connectorFactory = componentContext.createConnectorFactory(broker);
    }

    public IComponentFactory getComponentFactory() {
        return componentFactory;
    }

    public <T extends ISupports> T createInstance(String classId, String primaryKey) {
        return componentFactory.createInstance(classId, primaryKey);
    }

    public <T extends ISupports> T createInstance(String classId) {
        return componentFactory.createInstance(classId);
    }

    public <T extends ISupports> T createInstance(String classId, String primaryKey, String interfaceId) {
        return componentFactory.createInstance(classId, primaryKey, interfaceId);
    }

    public <T extends ISupports> void registerPrototype(Class<T> componentClass) {
        componentFactory.registerPrototype(componentClass);
    }

    public IBroker getBroker() {
        return broker;
    }

    public IMessage createMessage(String label, ISourceMessage source, Object parameters) {
        return messageFactory.createMessage(label, source, parameters);
    }

    public IMessage createMessage(String label, ISourceMessage source) {
        return messageFactory.createMessage(label, source);
    }

    public IAsyncConnector create1to1AsyncConnector(IAsyncSender sender, IAsyncReceiver receiver) throws ConnectorException {
        return connectorFactory.create1to1AsyncConnector(sender, receiver);
    }

    public ISyncConnector create1to1SyncConnector(ISyncSender sender, ISyncReceiver receiver) throws ConnectorException {
        return connectorFactory.create1to1SyncConnector(sender, receiver);
    }
}
