package plugins.moc;

import plugins.IPlugin;
import xmpp.messaging.base.Message;
import xmpp.queue.IMessageQueue;

public class PluginMocThrowsExceptions implements IPlugin {

    @Override
    public boolean canProcess(Message msg) {
        return true;
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public void processMessage(Message msg) {
        throw new RuntimeException();
    }

    @Override
    public void setTransport(IMessageQueue queue) {
    }
}
