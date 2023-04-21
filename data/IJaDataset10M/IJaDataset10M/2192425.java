package recoveryAgents;

import it.unibo.is.communication.data.IUdpMessage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import structure.MulticastOperator;
import structure.NameServiceControl;

/**
 * Agente che ha il compito di inviare un messaggio in multicast all'inizializzazione per la verifica 
 * se esiste un master in rete oppure no. Se entro un timeout non riceve risposta si autoelegge master 
 */
public class CheckLifeAgent extends Thread implements IDiscoveryAgent {

    private Lock _lock = new ReentrantLock();

    private Condition _waitForMessage = _lock.newCondition();

    private Condition _waitForFirstMessage = _lock.newCondition();

    private boolean _hasMessageArrived;

    private boolean _hasFirstMessageArrived;

    private MulticastOperator _multicastOp;

    private String _message;

    private String _firstMessage;

    private String _masterMessage;

    private String _messageSended;

    private NameServiceControl _nameServiceControl;

    private UdpMulticastListenerForDiscovery _udpListener;

    private boolean _stopListener;

    private int _timeToSendKeepalive;

    /**
	 * Costruttore con parametri
	 * @param nameservice � l'oggetto preposto a controllare gli agenti 
	 * @param port � la porta a cui si connette la socket multicast
	 */
    public CheckLifeAgent(NameServiceControl nameservice, int port) {
        _hasMessageArrived = false;
        _hasFirstMessageArrived = false;
        _nameServiceControl = nameservice;
        _message = "are you alive?_" + _nameServiceControl.getEntityName();
        _firstMessage = "hello_" + _nameServiceControl.getEntityName() + "_portTcp_" + _nameServiceControl.getPortTcpToReplica();
        _masterMessage = "I'm master_" + _nameServiceControl.getEntityName();
        _stopListener = false;
        _timeToSendKeepalive = 1000;
        try {
            InetAddress addressGroup = InetAddress.getByName(_nameServiceControl.getRecoveryMulticastGroup());
            _multicastOp = new MulticastOperator("CheckLiFeAgent", port, addressGroup);
            _multicastOp.beginCommunication(5000);
            _multicastOp.println("starts at port " + _multicastOp.getPort() + " with multicast address " + _multicastOp.getAddressGroup().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        println("created!");
    }

    /**
	 * Quando il tempo di attesa della risposta � esaurito comunica  al NameService che � master
	 */
    public void endOfTime() {
        _lock.lock();
        try {
            println("timeout scaduto..sono master");
            _nameServiceControl.setCurrentState(ExecutionState.MASTER_WAIT_SLAVE);
            _nameServiceControl.setIdentity("master");
            _hasFirstMessageArrived = true;
            _hasMessageArrived = true;
            _waitForMessage.signal();
            _waitForFirstMessage.signal();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            _lock.unlock();
        }
    }

    /**
	 * UdpMulticastListener notifica la ricezione del primo messaggio
	 * @param message � il messaggio udp ricevuto
	 */
    public void receiveFirstMessage(IUdpMessage message) {
        _lock.lock();
        try {
            println("primo messaggio ricevuto..");
            _hasFirstMessageArrived = true;
            if (message.getPayload().startsWith("hello")) {
                _nameServiceControl.setCurrentState(ExecutionState.MASTER_WORKING_WITH_SLAVE);
                _nameServiceControl.setIdentity("master");
                String ipAddress = message.getSenderHostName();
                String[] args = message.getPayload().split("_");
                int port = Integer.parseInt(args[args.length - 1]);
                _nameServiceControl.slaveConnected(ipAddress, port);
                _multicastOp.sendMessage(_masterMessage);
                _messageSended = _masterMessage;
                _hasMessageArrived = true;
                _waitForMessage.signal();
                println("..sono master");
                _stopListener = true;
            } else if (message.getPayload().startsWith("I'm master")) {
                _nameServiceControl.setCurrentState(ExecutionState.SLAVE_WORKING);
                _nameServiceControl.setIdentity("slave");
                println("..sono slave");
            }
            _waitForFirstMessage.signal();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            _lock.unlock();
        }
    }

    /**
	 * UdpMulticastListener notifica la ricezione dei messaggi successivi
	 * @param message � il messaggio udp ricevuto
	 */
    public void receiveMessage(IUdpMessage message) {
        _lock.lock();
        try {
            println("risposta di keepalive ricevuta..");
            _waitForMessage.signal();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            _lock.unlock();
        }
    }

    /**
	 * Notifica al NameServiceControl che il non ha pi� inviato messaggi e la socket � 
	 * andata in timeoutException
	 */
    public void masterDead() {
        _lock.lock();
        println("master offline");
        _multicastOp.endCommunication();
        _hasMessageArrived = true;
        _waitForMessage.signal();
        _nameServiceControl.setCurrentState(ExecutionState.SLAVE_TO_MASTER);
        _nameServiceControl.deadMaster();
        _lock.unlock();
    }

    /**
	 * Restituisce il contenuto del messaggio appena inviato
	 * @return String � il messaggio appena inviato
	 */
    public String getMessageSended() {
        return _messageSended;
    }

    /**
	 * Indica se il listener deve essere fermato oppure no
	 * @return boolean vale <code>true</code> se il listener deve essere fermato, <code>false</code> 
	 * altrimenti
	 */
    public boolean listenerStop() {
        return _stopListener;
    }

    public void run() {
        _lock.lock();
        try {
            _multicastOp.sendMessage(_firstMessage);
            _messageSended = _firstMessage;
            println("inviato messaggio di hello");
            _udpListener = new UdpMulticastListenerForDiscovery(this, "checkLife", _multicastOp);
            _udpListener.start();
            while (!_hasFirstMessageArrived || !_hasMessageArrived) {
                if (!_hasFirstMessageArrived) {
                    println("sospeso in attesa di ricevere il primo messaggio");
                    _waitForFirstMessage.await();
                }
                if (!_nameServiceControl.isMaster()) {
                    Thread.sleep(_timeToSendKeepalive);
                    _multicastOp.sendMessage(_message);
                    _messageSended = _message;
                    println("inviato messaggio di keepalive");
                }
                if (!_hasMessageArrived) {
                    println("sospeso in attesa di ricevere il successivo messaggio");
                    _waitForMessage.await();
                }
            }
            println("termino");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            _lock.unlock();
        }
    }

    /**
	 * Scrive a video il messaggio con un determinato formato
	 * @param message � il messaggio da scrivere a video
	 */
    private void println(String message) {
        System.out.println("CheckLiFeAgent: " + message);
    }
}
