package ch.squix.nataware.messaging;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.restlet.Client;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.protobuf.Message;
import ch.squix.protocol.Dataclasses.Node;

public class SimpleMessageService implements IMessageService {

    private Logger logger = LoggerFactory.getLogger(SimpleMessageService.class);

    private MessageHandlerNotifier notifier;

    private String serviceAddress;

    private Node localNode;

    private Thread listenerThread;

    private boolean keepRunning = true;

    public SimpleMessageService(String serviceAddress, Node localNode) {
        this.serviceAddress = serviceAddress;
        this.localNode = localNode;
        notifier = new MessageHandlerNotifier();
    }

    public void addMessageHandler(IMessageHandler handler) {
        notifier.addMessageHandler(handler);
    }

    public void removeMessageHandler(IMessageHandler handler) {
        notifier.removeMessageHandler(handler);
    }

    public void sendMessage(Node target, Message message) {
        try {
            Client client = new Client(Protocol.HTTP);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XMLEncoder encoder = new XMLEncoder(outputStream);
            encoder.writeObject(message);
            encoder.close();
            outputStream.flush();
            Form form = new Form();
            logger.info(outputStream.toString());
            form.add("message", outputStream.toString());
            Request request = new Request(Method.POST, serviceAddress + target.getNodeId() + "/message");
            request.setEntity(form.getWebRepresentation());
            Response response = client.handle(request);
            logger.info("Answer received: " + response.getEntity().getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessageHandler(IMessageHandler handler, Class<?> messageType) {
        notifier.addMessageHandler(handler, messageType);
    }

    public void notifyMessageHandlers(Message message) {
        notifier.notifyMessageHandlers(message);
    }

    public void startMessageListener() {
        keepRunning = true;
        listenerThread = new Thread(new Runnable() {

            public void run() {
                Client client = new Client(Protocol.HTTP);
                Request request = new Request(Method.GET, serviceAddress + localNode.getNodeId() + "/message");
                while (keepRunning) {
                    Response response = client.handle(request);
                    Representation output = response.getEntity();
                    if (output.isAvailable()) {
                        try {
                            XMLDecoder xdec = new XMLDecoder(output.getStream());
                            Message message = (Message) xdec.readObject();
                            logger.info("Received answer from REST server: \n");
                            notifier.notifyMessageHandlers(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        logger.info("Seems like noody sent us mail:-(. Ask again");
                    }
                }
            }
        });
        listenerThread.start();
    }

    public void stopMessageListener() {
        keepRunning = false;
    }
}
