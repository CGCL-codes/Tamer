package uk.azdev.openfire.net;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import uk.azdev.openfire.net.messages.IMessage;

public class MessageSendingServerEmulator extends AbstractServerEmulator {

    private List<IMessage> messagesToSend;

    public MessageSendingServerEmulator() throws IOException {
        super();
        messagesToSend = new LinkedList<IMessage>();
    }

    public void addMessageToSend(IMessage message) {
        messagesToSend.add(message);
    }

    @Override
    public void doWork(SocketChannel channel) throws IOException {
        ChannelReader reader = new ChannelReader(channel);
        reader.skipOpeningStatement();
        ChannelWriter writer = new ChannelWriter(channel);
        writeMessages(writer);
    }

    private void writeMessages(ChannelWriter writer) throws IOException {
        for (IMessage message : messagesToSend) {
            writer.writeMessage(message);
        }
    }
}
