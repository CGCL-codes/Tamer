package org.peertrust.meta;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import net.jxta.edutella.util.Configurable;
import net.jxta.edutella.util.Configurator;
import net.jxta.edutella.util.Option;
import org.apache.log4j.Logger;
import org.peertrust.inference.InferenceEngine;
import org.peertrust.inference.LogicAnswer;
import org.peertrust.inference.LogicQuery;
import org.peertrust.inference.MinervaProlog;
import org.peertrust.net.AbstractFactory;
import org.peertrust.net.Answer;
import org.peertrust.net.Message;
import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;
import org.peertrust.net.Query;
import org.peertrust.net.ssl.SecureSocketFactory;
import org.peertrust.security.credentials.Credential;
import org.peertrust.security.credentials.CredentialStore;
import org.peertrust.security.credentials.x509.X509CredentialStore;
import org.peertrust.strategy.FIFOQueue;
import org.peertrust.strategy.Queue;

/**
 * $Id: MetaInterpreter.java,v 1.1 2004/08/07 12:51:56 magik Exp $
 * 
 * @author olmedilla
 * @date 05-Dec-2003 Last changed $Date: 2004/08/07 12:51:56 $ by $Author:
 *       dolmedilla $
 * @description
 */
public class MetaInterpreter implements Configurable, Runnable {

    public static final String ASSERTA = "asserta";

    private final int SLEEP_TIME = 200;

    private Queue queue;

    private InferenceEngine engine;

    private String baseFolder;

    private String entitiesFile;

    private MetaInterpreterListener metaIListener;

    private Thread metaIThread = null;

    private Hashtable entities = new Hashtable();

    private Configurator config;

    private Peer localPeer = new Peer();

    private String keystoreFile;

    private String keyPassword;

    private String storePassword;

    private AbstractFactory factory;

    private NetClient netClient;

    private CredentialStore credStore;

    private static Logger log = Logger.getLogger(MetaInterpreter.class);

    public MetaInterpreter(Queue queue, InferenceEngine engine, Configurator conf, AbstractFactory factory) {
        log.debug("Created");
        this.queue = queue;
        this.engine = engine;
        this.config = conf;
        this.factory = factory;
    }

    public void init() {
        readEntities();
        log.info("PeerName = " + localPeer.getAlias());
        engine.execute(ASSERTA + "(peerName(" + localPeer.getAlias() + "))");
        engine.execute(ASSERTA + "(debug_on)");
        try {
            credStore = new X509CredentialStore(baseFolder + keystoreFile, storePassword);
        } catch (Exception e) {
            log.error("Invalid keystore file");
        }
        Vector credentials = credStore.getCredentials();
        for (int i = 0; i < credentials.size(); i++) {
            String stringCredential = ((Credential) credentials.elementAt(i)).getStringRepresentation();
            log.debug("Adding credential string '" + stringCredential + "'");
            engine.execute(ASSERTA + "(" + stringCredential + ")");
        }
        netClient = factory.createNetClient(this.config);
        metaIListener = new MetaInterpreterListener(queue, engine, entities, config, this.factory);
        metaIThread = new Thread(this, "MetaInterpreter");
        metaIThread.start();
    }

    public void run() {
        log.debug("start");
        Thread myThread = Thread.currentThread();
        while (metaIThread == myThread) {
            processQueue();
        }
    }

    public void stop() {
        metaIThread = null;
        metaIListener.stop();
    }

    void processQueue() {
        Tree selectedTree = queue.pop();
        if (selectedTree == null) {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                log.error(localPeer.getAlias() + ": interpreter waken up", e);
            }
            return;
        }
        log.debug("PROCESSING TREE:" + selectedTree.getGoal());
        if (selectedTree.getStatus() == Tree.FAILED) {
            Answer failure = new Answer(selectedTree.getGoal(), null, Answer.FAILURE, selectedTree.getReqQueryId(), localPeer);
            log.debug("Sending answer of " + failure.getGoal() + " to " + selectedTree.getRequester().getAlias());
            sendMessage(failure, selectedTree.getRequester());
            return;
        } else if (((selectedTree.getStatus() == Tree.READY) && (selectedTree.getSubqueries().equals("[]"))) || (selectedTree.getStatus() == Tree.ANSWERED)) {
            Tree pattern = new Tree(selectedTree.getRequester(), selectedTree.getReqQueryId());
            int status;
            if (queue.search(pattern) == null) status = Answer.LAST_ANSWER; else status = Answer.ANSWER;
            Answer answer = new Answer(selectedTree.getGoal(), selectedTree.getProof(), status, selectedTree.getReqQueryId(), localPeer);
            log.debug("Sending answer " + answer.getGoal() + " to " + selectedTree.getRequester().getAlias() + " with proof " + answer.getProof());
            sendMessage(answer, selectedTree.getRequester());
            return;
        }
        LogicQuery logicQuery = new LogicQuery(selectedTree.getGoal(), selectedTree.getSubqueries(), selectedTree.getRequester().getAlias());
        LogicAnswer[] results = engine.processTree(logicQuery);
        if (results == null) {
            log.debug("results == null");
            Tree pattern = new Tree(selectedTree.getRequester(), selectedTree.getReqQueryId());
            if (queue.search(pattern) == null) {
                Tree failedTree = new Tree(selectedTree);
                failedTree.setStatus(Tree.FAILED);
                log.debug("Tree " + failedTree.getGoal() + " failed");
                queue.add(failedTree);
            }
        } else {
            log.debug("results == " + results.length);
            for (int i = 0; i < results.length; i++) {
                String delegator = results[i].getDelegator();
                if (delegator == null) {
                    log.debug("Delegator == null");
                    if (results[i].getSubgoals().equals("[]")) {
                        log.debug("subqueries == []");
                        Tree answeredTree = new Tree(selectedTree);
                        answeredTree.setGoal(results[i].getGoal());
                        answeredTree.appendProof(results[i].getProof());
                        answeredTree.setStatus(Tree.ANSWERED);
                        log.debug("Tree " + answeredTree.getGoal() + " answered");
                        queue.add(answeredTree);
                    } else {
                        Tree newTree = new Tree(results[i].getGoal(), results[i].getSubgoals(), selectedTree.getProof(), Tree.READY, selectedTree.getRequester(), selectedTree.getReqQueryId());
                        newTree.appendProof(results[i].getProof());
                        queue.add(newTree);
                    }
                } else {
                    Tree delegatedTree = new Tree(results[i].getGoal(), results[i].getSubgoals(), selectedTree.getProof(), Tree.WAITING, selectedTree.getRequester(), selectedTree.getReqQueryId(), (Peer) entities.get(delegator), results[i].getGoalExpanded());
                    delegatedTree.appendProof(results[i].getProof());
                    log.debug("Delegator == " + delegator);
                    queue.add(delegatedTree);
                    Query query = new Query(delegatedTree.getLastExpandedGoal(), localPeer, delegatedTree.getId());
                    log.debug("Sending request " + query.getGoal() + " to " + delegatedTree.getDelegator().getAlias());
                    sendMessage(query, delegatedTree.getDelegator());
                }
            }
        }
    }

    private void sendMessage(Message message, Peer destination) {
        if (message instanceof Query) log.debug("Send query to " + destination.getAddress() + ":" + destination.getPort() + " from " + message.getOrigin().getAlias()); else if (message instanceof Answer) log.debug("Send answer to " + destination.getAddress() + ":" + destination.getPort() + " from " + message.getOrigin().getAlias()); else log.error("Unknown message type");
        netClient.send(message, destination);
    }

    private void readEntities() {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(baseFolder + entitiesFile));
        } catch (FileNotFoundException e) {
            log.error("Entities file not found: " + baseFolder + entitiesFile, e);
        }
        try {
            String line;
            String[] attributes;
            while ((line = input.readLine()) != null) {
                if (line.charAt(0) != '%') {
                    attributes = line.split("\t");
                    entities.put(attributes[0], new Peer(attributes[0], attributes[1], Integer.parseInt(attributes[2])));
                }
            }
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    /**
     * @see net.jxta.edutella.util.Configurable#getOptions()
     */
    public Option[] getOptions() {
        Option baseOpt = new Option('b', "metaI.baseFolder", "Base Folder", "Folder with local files", true);
        Option addOpt = new Option('a', "metaI.address", "Address", "Local Address", true);
        Option nameOpt = new Option('n', "metaI.peerName", "Name", "Peer name", true);
        Option portOpt = new Option('p', "metaI.serverPort", "Port", "Server Port", true);
        Option entOpt = new Option('e', "metaI.entitiesFile", "Entities File", "Entities File", true);
        Option keystoreOpt = new Option('f', "metaI.keystoreFile", "Keystore File", "Keystore File", true);
        Option keypwdOpt = new Option('k', "metaI.keyPassword", "Key Password", "Key Password", true);
        keypwdOpt.setIsPassword(true);
        Option storepwdOpt = new Option('s', "metaI.storePassword", "Keystore Password", "Keystore Password", true);
        storepwdOpt.setIsPassword(true);
        return new Option[] { baseOpt, addOpt, nameOpt, portOpt, entOpt, keystoreOpt, keypwdOpt, storepwdOpt };
    }

    /**
     * @see net.jxta.edutella.util.Configurable#getPropertyPrefix()
     */
    public String getPropertyPrefix() {
        return "metaI";
    }

    public void setBaseFolder(String folder) {
        baseFolder = folder;
    }

    public String getBaseFolder() {
        return baseFolder;
    }

    public void setAddress(String address) {
        localPeer.setAddress(address);
    }

    public String getAddress() {
        return localPeer.getAddress();
    }

    public void setPeerName(String peerName) {
        localPeer.setAlias(peerName.toLowerCase());
    }

    protected String getPeerName() {
        return localPeer.getAlias();
    }

    public void setServerPort(int port) {
        localPeer.setPort(port);
    }

    public int getPort() {
        return localPeer.getPort();
    }

    public void setEntitiesFile(String file) {
        entitiesFile = file;
    }

    public String getEntitiesFile() {
        return entitiesFile;
    }

    public void setKeystoreFile(String file) {
        keystoreFile = file;
    }

    public String getKeyStoreFile() {
        return keystoreFile;
    }

    public void setKeyPassword(String pwd) {
        this.keyPassword = pwd;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setStorePassword(String pwd) {
        this.storePassword = pwd;
    }

    public String getStorePassword() {
        return storePassword;
    }

    public static void main(String[] args) {
        try {
            Configurator cf = new Configurator("trust.properties", args);
            cf.setAppInfo("Atomated Trust Negotiation Peer");
            MinervaProlog engine = new MinervaProlog(cf);
            cf.register(engine);
            MetaInterpreter metaI = new MetaInterpreter(new FIFOQueue(), engine, cf, new SecureSocketFactory());
            cf.register(metaI);
            cf.finishConfig();
            metaI.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
