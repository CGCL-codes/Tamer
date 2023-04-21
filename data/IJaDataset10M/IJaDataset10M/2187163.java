package net.sourceforge.jcoupling.peer;

import net.sourceforge.jcoupling.JCouplingException;
import net.sourceforge.jcoupling.JCouplingTestFramework;
import net.sourceforge.jcoupling.factory.IntegratorFactory;
import net.sourceforge.jcoupling.wca.*;
import net.sourceforge.jcoupling.bus.server.ConfigurationException;
import net.sourceforge.jcoupling.bus.server.NotFoundException;
import net.sourceforge.jcoupling.bus.server.PermissionException;
import net.sourceforge.jcoupling.bus.server.callout.RequestController;
import net.sourceforge.jcoupling.bus.dao.DaoFactoryType;
import net.sourceforge.jcoupling.peer.destination.*;
import net.sourceforge.jcoupling.peer.interaction.InteractionSignal;
import net.sourceforge.jcoupling.peer.interaction.ResponseContainer;
import net.sourceforge.jcoupling.peer.interaction.MessageBusServerImpl;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.sql.SQLException;

public class TestTimeDecoupled extends JCouplingTestFramework {

    private Sender _sender;

    private Receiver _receiver;

    private Address _address;

    private Topic _topic;

    private Channel _channel;

    private static Random random = new Random(new Date().getTime() + 5);

    private static final String PASSWORD = "Password";

    public void setUp() throws TransportException, ConfigurationException, URISyntaxException, PermissionException, NotFoundException {
        MessageBusServerImpl messageServer = new MessageBusServerImpl();
        RequestController controller = new RequestController(DaoFactoryType.In_Memory, messageServer.getBusController());
        IntegratorFactory factory = new LocalIntegratorFactory(messageServer, controller);
        _decoupleFactory = factory;
        Integrator integrator = _decoupleFactory.createIntegrator();
        _sender = new Sender(integrator);
        _receiver = new Receiver(integrator);
        _address = _receiver.createAddress(new URI("test:address" + random.nextInt()), null, PASSWORD);
        _topic = _receiver.createTopic(new URI("test:topic" + random.nextInt()), null, null);
        _receiver.subscribe(_topic, null);
        _channel = _receiver.createChannel(new URI("test:channel" + random.nextInt()), null, null);
    }

    public void testBlockingSendAddress() throws JCouplingException, TimeoutException, SQLException {
        Destination destination = _address;
        runBlockingSend(destination);
    }

    public void testBlockingSendTopic() throws JCouplingException, TimeoutException, SQLException {
        Destination destination = _topic;
        runBlockingSend(destination);
    }

    public void testBlockingSendChannel() throws JCouplingException, TimeoutException, SQLException {
        Destination destination = _channel;
        runBlockingSend(destination);
    }

    public void testNonBlockingSendAddress() throws JCouplingException, ExecutionException, InterruptedException {
        Destination destination = _address;
        runNonBlockingSend(destination);
    }

    public void testNonBlockingSendTopic() throws JCouplingException, ExecutionException, InterruptedException {
        Destination destination = _topic;
        runNonBlockingSend(destination);
    }

    public void testNonBlockingSendChannel() throws JCouplingException, ExecutionException, InterruptedException {
        Destination destination = _channel;
        runNonBlockingSend(destination);
    }

    public void testReturnDestinations() throws NotFoundException, TransportException {
        Set<Subscribable> mySubscriptions = _receiver.getSubscriptions();
        assertEquals(2, mySubscriptions.size());
        _receiver.returnDestinations();
        assertEquals(0, _receiver.getSubscriptions().size());
    }

    private void runNonBlockingSend(Destination destination) throws ExecutionException, InterruptedException, JCouplingException {
        Message messageTo = new Message();
        messageTo.setContent("testing");
        Future<ResponseContainer> futureSend = _sender.nonBlockingSend(messageTo, destination, TimeCoupling.Decoupled);
        ResponseContainer responseContainer = futureSend.get();
        assertEquals(InteractionSignal.MSG_BUFFERED, responseContainer.getHeadResponse().getValue());
        SimpleProcessor processor = new SimpleProcessor();
        Future<? extends Message> future = _receiver.nonBlockingReceive(destination, processor);
        Message message = future.get();
        assertEquals(responseContainer.getMessageID(), message.getID());
    }

    private void runBlockingSend(Destination destination) throws JCouplingException, NotFoundException, PermissionException, TimeoutException, SQLException {
        Message message = new Message();
        message.setContent("testing");
        WCAChannel channel = new WCAChannel(destination.toString(), MemoryCommunicationAdapter.getInstance(), TimeCoupling.Coupled, SpaceCoupling.SendReceive, false, null, null);
        System.out.println("$$ here ");
        ResponseContainer responseContainer = _sender.blockingSend(message, channel, TimeCoupling.Decoupled);
        assertEquals(InteractionSignal.MSG_BUFFERED, responseContainer.getHeadResponse().getValue());
        Message messageBk = _receiver.blockingReceive(destination, null).get(0);
        assertEquals(responseContainer.getMessageID(), messageBk.getID());
    }
}
