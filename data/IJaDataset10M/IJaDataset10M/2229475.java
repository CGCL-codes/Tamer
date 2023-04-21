package net.jxta.jxtacast;

import java.io.*;
import java.util.*;
import net.jxta.discovery.*;
import net.jxta.document.*;
import net.jxta.endpoint.*;
import net.jxta.id.*;
import net.jxta.peergroup.*;
import net.jxta.pipe.*;
import net.jxta.protocol.*;
import net.jxta.rendezvous.*;

public class JxtaCast implements PipeMsgListener, Runnable {

    public static String version = "2.00";

    static final String MESSAGETYPE = "MessageType";

    static final String SENDERNAME = "JxtaTalkSenderName";

    static final String SENDERID = "SenderID";

    static final String VERSION = "FileCastVersion";

    static final String CAPTION = "Caption";

    static final String FILEKEY = "FileKey";

    static final String FILENAME = "FileName";

    static final String FILESIZE = "FileSize";

    static final String BLOCKNUM = "BlockNum";

    static final String TOTALBLOCKS = "TotalBlocks";

    static final String BLOCKSIZE = "BlockSize";

    static final String DATABLOCK = "DataBlock";

    static final String REQTOPEER = "ReqToPeer";

    static final String REQ_ANYPEER = "ReqAnyPeer";

    static final String MSG_FILE = "FILE";

    static final String MSG_FILE_ACK = "FILE_ACK";

    static final String MSG_FILE_REQ = "FILE_REQ";

    static final String MSG_FILE_REQ_RESP = "FILE_REQ_RESP";

    static final String MSG_CHAT = "CHAT";

    public static final String DELIM = "]--,',--[";

    public static boolean logEnabled;

    public int outBlockSize = 12288;

    public int outWranglerLifetime = 600000;

    public int inWranglerLifetime = 300000;

    public int timeTilReq = 60000;

    public int trailBossPeriod = 1000;

    public String fileSaveLoc;

    protected Hashtable wranglers = new Hashtable(40);

    protected Vector sendFileQueue = new Vector(10);

    protected DiscoveryService disco;

    protected PeerAdvertisement myPeer;

    protected PeerGroup group;

    protected String castName;

    protected PipeService pipeServ;

    protected InputPipe inputPipe;

    protected OutputPipe outputPipe;

    protected InputPipe privInputPipe;

    protected Vector jcListeners;

    protected Thread trailBossThread;

    /** Constructor
   *
   *  @param group - peergroup that we've joined.
   *  @param castName - name to use in the pipe advertisement ID , such as an
   *                    application name.  This permits the creation of
   *                    multiple JxtaCast channels within a single group.
   */
    public JxtaCast(PeerAdvertisement myPeer, PeerGroup group, String castName) {
        this.myPeer = myPeer;
        this.castName = new String(castName);
        setPeerGroup(group);
        fileSaveLoc = "." + File.separator;
        jcListeners = new Vector(10);
    }

    public JxtaCast() {
    }

    public void start() {
        trailBossThread = new Thread(this, "JxtaCast:TrailBoss");
        trailBossThread.start();
    }

    public void stop() {
        trailBossThread = null;
        if (inputPipe != null) {
            inputPipe.close();
        }
        if (outputPipe != null) {
            outputPipe.close();
        }
    }

    /** Return the currently joined peer group. */
    public PeerGroup getPeerGroup() {
        return group;
    }

    /** Change to a new peer group.
   *  @return true if we successfully created the pipes in the new group.
   */
    public boolean setPeerGroup(PeerGroup group) {
        boolean rc;
        if (this.group != null && group.getPeerGroupID().equals(this.group.getPeerGroupID())) return true;
        synchronized (wranglers) {
            this.group = group;
            disco = group.getDiscoveryService();
            pipeServ = group.getPipeService();
            rc = createPipes(castName);
        }
        return rc;
    }

    /** Log a debug message to the console.  Should maybe use Log4J?
   *  Have to figure out whether we can use Log4J to show our application
   *  debug messages, but suppress all the JXTA platform messages.
   */
    public static void logMsg(String msg) {
        if (logEnabled) System.out.println(msg);
    }

    /**
   *  Create an input and output pipe to handle the file transfers.
   *  Publish their advertisements so that other peers will find them.
   *
   *  @param castName - name to use in the pipe advertisement ID.
   *  @return true if successful.
   */
    protected boolean createPipes(String castName) {
        if (inputPipe != null) inputPipe.close();
        if (outputPipe != null) outputPipe.close();
        if (privInputPipe != null) privInputPipe.close();
        PipeAdvertisement pipeAdvt;
        pipeAdvt = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        byte jxtaCastID[] = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xBB, (byte) 0xBB, (byte) 0xBB, (byte) 0xBB, (byte) 0xBB, (byte) 0xBB, (byte) 0xBB, (byte) 0xBB, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA };
        String idStr = castName + "-[FileCast Pipe ID]-" + new String(jxtaCastID);
        PipeID id = (PipeID) IDFactory.newPipeID(group.getPeerGroupID(), idStr.getBytes());
        pipeAdvt.setPipeID(id);
        pipeAdvt.setName("JxtaTalkSenderName." + castName);
        pipeAdvt.setType(PipeService.PropagateType);
        try {
            disco.publish(pipeAdvt);
            disco.remotePublish(pipeAdvt);
            inputPipe = pipeServ.createInputPipe(pipeAdvt, this);
            outputPipe = pipeServ.createOutputPipe(pipeAdvt, 2000);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        pipeAdvt = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        id = (PipeID) IDFactory.newPipeID(group.getPeerGroupID());
        pipeAdvt.setPipeID(id);
        pipeAdvt.setName(getBackChannelPipeName());
        pipeAdvt.setType(PipeService.UnicastType);
        try {
            disco.publish(pipeAdvt);
            disco.remotePublish(pipeAdvt);
            privInputPipe = pipeServ.createInputPipe(pipeAdvt, this);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /** Return the name used in advertisement for our "back channel" input pipe.
   *  The string contains a known prefix that can be used for discovery,
   *  plus our peer name and ID.
   */
    public String getBackChannelPipeName() {
        String name = getBackChannelPipePrefix() + DELIM + myPeer.getName() + DELIM + myPeer.getPeerID().toString();
        return name;
    }

    /** Return the prefix used in the name of our "back channel" input pipe.
   *  This prefix can be used with advertisement discovery to narrow the
   *  discovery results to peers using JxtaCast with your application.
   */
    public String getBackChannelPipePrefix() {
        return "FileCastBackChannel." + castName;
    }

    /** Extract the peer name from the given pipe advertisement name.
   */
    public static String getPeerNameFromBackChannelPipeName(String pipeName) {
        int start = pipeName.indexOf(DELIM);
        if (start < 0) return null;
        int end = pipeName.indexOf(DELIM, start + 1);
        if (end < 0) return null;
        start += DELIM.length();
        if (start > end) return null;
        return pipeName.substring(start, end);
    }

    /** Extract the peer ID from the given pipe advertisement name.
   */
    public static String getPeerIdFromBackChannelPipeName(String pipeName) {
        int pos = pipeName.indexOf(DELIM);
        if (pos < 0) return null;
        pos = pipeName.indexOf(DELIM, ++pos);
        if (pos < 0) return null;
        return pipeName.substring(pos + DELIM.length());
    }

    /**
   *  Send a Message down the output pipe.
   */
    public synchronized void sendMessage(Message msg) {
        try {
            outputPipe.send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
   * Receive messages from the input pipe.
   *
   * @param event PipeMsgEvent the event that contains our message.
   */
    public synchronized void pipeMsgEvent(PipeMsgEvent event) {
        Message msg = event.getMessage();
        try {
            String msgType = getMsgString(msg, MESSAGETYPE);
            if (msgType == null) {
                logMsg("Error: message received with no MESSAGETYPE.");
                return;
            }
            if (msgType.equals(MSG_CHAT)) receiveChatMsg(msg); else receiveFileMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Receive a file transfer message.
   *
   * @param msg a file transfer message.
   */
    public synchronized void receiveFileMsg(Message msg) {
        try {
            String msgType = getMsgString(msg, MESSAGETYPE);
            if (msgType == null) return;
            FileWrangler wrangler = (FileWrangler) wranglers.get(getMsgString(msg, FILEKEY));
            if (wrangler == null && msgType.equals(MSG_FILE)) {
                wrangler = new InputFileWrangler(this, msg);
                wranglers.put(wrangler.key, wrangler);
            }
            if (wrangler == null) logMsg("Unable to obtain wrangler for message.  Msg type: " + msgType + "  key: " + getMsgString(msg, FILEKEY)); else wrangler.processMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Receive a chat message.
   *
   * @param msg a chat message.
   */
    public synchronized void receiveChatMsg(Message msg) {
        try {
            String sender = getMsgString(msg, SENDERNAME);
            String caption = getMsgString(msg, CAPTION);
            logMsg(sender + " : " + caption);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   *  Send a chat message out to the group members.
   *
   *  @param  text   the message text.
   */
    public synchronized void sendChatMsg(String text) {
        try {
            Message msg = new Message();
            setMsgString(msg, MESSAGETYPE, MSG_CHAT);
            setMsgString(msg, SENDERNAME, myPeer.getName());
            setMsgString(msg, SENDERID, myPeer.getPeerID().toString());
            setMsgString(msg, VERSION, version);
            setMsgString(msg, CAPTION, text);
            sendMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
   *  Send a file out to the group members.
   *
   *  @param  file       the file to send.
   *  @param  caption    description of the file (optional)
   */
    public synchronized void sendFile(File file, String caption) {
        OutputFileWrangler wrangler = new OutputFileWrangler(this, file, caption);
        sendFileQueue.add(wrangler);
    }

    /** Worker thread.  Call functions to perform time-intensive operations that
   *  would bog down the main thread.
   */
    public void run() {
        while (true) {
            try {
                Thread.sleep(trailBossPeriod);
            } catch (InterruptedException e) {
            }
            synchronized (wranglers) {
                checkFileWranglers();
                checkSendFileQueue();
            }
        }
    }

    /** Loop thru our current collection of FileWrangler objects, and call
   *  bossCheck() for each one.  This gives them a chance to perform any
   *  needed tasks.
   *
   *  We keep wranglers stored in our Hashtable for awhile after we've finished
   *  sending or receiving the file. They're available to respond to requests
   *  from other peers for file blocks that they are missing.
   *
   *  In response to the bossCheck() call, wranglers that have been inactive
   *  for a long time will remove themselves from the collection.  Input
   *  wranglers that are missing file blocks will request them.
   */
    protected void checkFileWranglers() {
        Enumeration elements = wranglers.elements();
        ;
        FileWrangler wrangler;
        while (elements.hasMoreElements()) {
            wrangler = (FileWrangler) elements.nextElement();
            wrangler.bossCheck();
        }
    }

    /**
   * Outgoing file send operations are queued by the main thread.  This function
   * is called by the worker thread.  It reads them from the queue, and triggers
   * the file load and send process.
   */
    protected void checkSendFileQueue() {
        OutputFileWrangler wrangler = null;
        if (sendFileQueue.isEmpty()) return;
        wrangler = (OutputFileWrangler) sendFileQueue.remove(0);
        if (wrangler != null) {
            wranglers.put(wrangler.key, wrangler);
            wrangler.sendFile();
            wrangler = null;
        }
    }

    /**
   * Register a JxtaCastEventListener.  Listeners are sent progress events while
   * sending and receiving files.
   */
    public synchronized void addJxtaCastEventListener(JxtaCastEventListener listener) {
        if (!jcListeners.contains(listener)) jcListeners.addElement(listener);
    }

    /**
   * Un-register a JxtaCastEventListener.
   */
    public synchronized void removeJxtaCastEventListener(JxtaCastEventListener listener) {
        jcListeners.removeElement(listener);
    }

    /**
   * Send a JxtaCastEvent to all registered listeners.
   */
    protected void sendJxtaCastEvent(JxtaCastEvent e) {
        JxtaCastEventListener listener = null;
        Enumeration elements = jcListeners.elements();
        while (elements.hasMoreElements()) {
            listener = (JxtaCastEventListener) elements.nextElement();
            listener.jxtaCastProgress(e);
        }
    }

    public static void setMsgString(Message msg, String name, String str) {
        msg.replaceMessageElement(new StringMessageElement(name, str, null));
    }

    public static String getMsgString(Message msg, String name) {
        MessageElement elem = msg.getMessageElement(name);
        if (elem == null) return null;
        return elem.toString();
    }
}

/**
* Base class for processing a file.
*
* Files are split up and sent in blocks of data.  This class gathers
* the data blocks for a file as they come in, and writes the file
* to disk when it is complete.
*
* It's ok for blocks to arrive out of order, and ok for duplicate blocks
* to arrive.
*/
abstract class FileWrangler {

    public String key;

    JxtaCast jc;

    String sender;

    String senderId;

    String filename;

    String caption = "";

    byte fdata[];

    int myBlockSize;

    int totalBlocks;

    long lastActivity;

    /** Process a file transfer message.
   */
    public abstract void processMsg(Message msg);

    /** Receive a regular 'maintainence' check-in from the TrailBoss thread.
   */
    public abstract void bossCheck();

    /** Create a unique key for this file transfer. */
    public static String composeKey(String senderId, String filename) {
        String keyStr = senderId + "+" + filename + "+" + String.valueOf(System.currentTimeMillis());
        return keyStr;
    }

    /** Send the specified block of file data out over the wire.
   *
   *  @param  blockNum  The block to send.
   *  @param  msgType   Message type: MSG_FILE or MSG_FILE_REQ_RESP.
   */
    synchronized void sendBlock(int blockNum, String msgType) {
        if (blockNum < 0 || blockNum >= totalBlocks) return;
        try {
            lastActivity = System.currentTimeMillis();
            Message msg = new Message();
            jc.setMsgString(msg, jc.MESSAGETYPE, msgType);
            jc.setMsgString(msg, jc.SENDERNAME, jc.myPeer.getName());
            jc.setMsgString(msg, jc.SENDERID, jc.myPeer.getPeerID().toString());
            jc.setMsgString(msg, jc.VERSION, jc.version);
            jc.setMsgString(msg, jc.FILEKEY, key);
            jc.setMsgString(msg, jc.FILENAME, filename);
            jc.setMsgString(msg, jc.FILESIZE, String.valueOf(fdata.length));
            if (blockNum == 0 && caption != null) jc.setMsgString(msg, jc.CAPTION, caption);
            jc.setMsgString(msg, jc.BLOCKNUM, String.valueOf(blockNum));
            jc.setMsgString(msg, jc.TOTALBLOCKS, String.valueOf(totalBlocks));
            jc.setMsgString(msg, jc.BLOCKSIZE, String.valueOf(myBlockSize));
            int bSize = myBlockSize;
            if (blockNum == totalBlocks - 1) bSize = fdata.length - (blockNum * myBlockSize);
            ByteArrayMessageElement elem = new ByteArrayMessageElement(jc.DATABLOCK, null, fdata, blockNum * myBlockSize, bSize, null);
            msg.replaceMessageElement(elem);
            jc.logMsg("Sending: " + filename + "  block: " + (blockNum + 1) + "  of: " + totalBlocks);
            jc.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
* Class for sending a file.
*
* Files are split up and sent in blocks of data.  This class loads the
* file into memory, and sends out data blocks.  It re-send blocks in response
* to requests from other peers.
*
*/
class OutputFileWrangler extends FileWrangler {

    File file;

    int blocksSent;

    /**
   * Constructor - Build a wrangler to process an outgoing file.
   *
   */
    public OutputFileWrangler(JxtaCast jc, File file, String caption) {
        this.jc = jc;
        this.file = file;
        lastActivity = System.currentTimeMillis();
        sender = jc.myPeer.getName();
        senderId = jc.myPeer.getPeerID().toString();
        filename = file.getName();
        this.caption = caption;
        key = composeKey(senderId, filename);
        blocksSent = 0;
        myBlockSize = jc.outBlockSize;
        int lastBlockSize = (int) file.length() % myBlockSize;
        totalBlocks = (int) file.length() / myBlockSize;
        if (lastBlockSize != 0) totalBlocks++;
    }

    /** Process a file transfer message.
   */
    public void processMsg(Message msg) {
        lastActivity = System.currentTimeMillis();
        String msgType = jc.getMsgString(msg, jc.MESSAGETYPE);
        if (msgType.equals(jc.MSG_FILE_ACK)) processMsgAck(msg); else if (msgType.equals(jc.MSG_FILE_REQ)) processMsgReq(msg);
    }

    /** Receive a regular 'maintainence' check-in from the TrailBoss.
   */
    public void bossCheck() {
        if (blocksSent > 0 && blocksSent < totalBlocks && System.currentTimeMillis() - lastActivity > jc.trailBossPeriod + 500) {
            jc.logMsg("bossCheck sending block.");
            sendBlock(blocksSent++, jc.MSG_FILE);
            updateProgress();
        }
        if (System.currentTimeMillis() - lastActivity > jc.outWranglerLifetime) jc.wranglers.remove(key);
    }

    /** Process a file transfer ACK message.
   *
   *  Peers will send us an ACK message when they've received a block.
   *  When we get one (from any peer), for the last block we've sent,
   *  then we can send the next block.
   */
    private void processMsgAck(Message msg) {
        int blockNum = Integer.parseInt(jc.getMsgString(msg, jc.BLOCKNUM));
        jc.logMsg("Received ACK: " + filename + "  block " + (blockNum + 1) + ", from " + jc.getMsgString(msg, jc.SENDERNAME));
        int nextBlock = blockNum + 1;
        if (nextBlock == blocksSent && nextBlock < totalBlocks) {
            blocksSent++;
            sendBlock(nextBlock, jc.MSG_FILE);
            updateProgress();
        }
    }

    /** Process a file transfer REQ message.
   *
   *  A peer has requested a block of this file.  Send it out as a
   *  REQ_RESP 'request response' message.
   */
    private void processMsgReq(Message msg) {
        int blockNum = Integer.parseInt(jc.getMsgString(msg, jc.BLOCKNUM));
        if (blockNum >= blocksSent) {
            jc.logMsg("Received " + jc.getMsgString(msg, jc.MESSAGETYPE) + ": " + filename + "  block " + (blockNum + 1) + ", from " + jc.getMsgString(msg, jc.SENDERNAME));
            sendBlock(blocksSent++, jc.MSG_FILE);
            updateProgress();
            return;
        }
        String reqToPeer = jc.getMsgString(msg, jc.REQTOPEER);
        if (reqToPeer.equals(jc.myPeer.getPeerID().toString()) || reqToPeer.equals(jc.REQ_ANYPEER)) {
            if (reqToPeer.equals(jc.REQ_ANYPEER)) jc.logMsg("Received REQ_ANYPEER: " + filename + "  block " + (blockNum + 1) + ", from " + jc.getMsgString(msg, jc.SENDERNAME)); else jc.logMsg("Received FILE_REQ: " + filename + "  block " + (blockNum + 1) + ", from " + jc.getMsgString(msg, jc.SENDERNAME));
            sendBlock(blockNum, jc.MSG_FILE_REQ_RESP);
        }
    }

    /** Start the file transfer process.
   *
   *  We read the file into memory here, instead of in the constructor,
   *  so that the operation will take place in the desired thread.  (See the
   *  JxtaCast.sendFile() method.)
   *
   *  We'll send out the file's first data block.  Additional blocks will be
   *  sent in response to acknowledgement messages from the peers, or in response
   *  to bossCheck() calls from the TrailBoss (whichever comes faster).
   *
   *  Why?  Because if we tried to send all the blocks at once, we'd overload
   *  the capabilities of the propagate pipes, and lots of messages would be
   *  dropped.  So we use the ACK in order to send blocks at a rate that can
   *  be managed.
   */
    public void sendFile() {
        blocksSent = 0;
        fdata = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(fdata, 0, fdata.length);
            fis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        sendBlock(blocksSent++, jc.MSG_FILE);
        updateProgress();
    }

    /** Notify listeners of our transmission progress.
   */
    private void updateProgress() {
        JxtaCastEvent e = new JxtaCastEvent();
        e.transType = e.SEND;
        e.filename = new String(filename);
        e.filepath = new String(jc.fileSaveLoc);
        e.sender = new String(sender);
        e.senderId = new String(senderId);
        if (caption != null) e.caption = new String(caption);
        e.percentDone = ((float) blocksSent / totalBlocks) * 100;
        jc.sendJxtaCastEvent(e);
    }
}

/**
* Class for receiving a file.
*
* Files are split up and sent in blocks of data.  This class gathers
* the data blocks for a file as they come in, and writes the file
* to disk when it is complete.
*
* It's ok for blocks to arrive out of order, and ok for duplicate blocks
* to arrive.   The wrangler can send out requests for missing blocks, and
* also provide blocks for other peers that are missing them.
*/
class InputFileWrangler extends FileWrangler {

    boolean blockIn[];

    String lastAck[];

    boolean askedOrig[];

    int blocksReceived;

    String reqLevel;

    int currReqBlock;

    long lastReqTime;

    long firstBlockTime;

    long latestBlockTime;

    long minTimeToWait;

    /**
   * Constructor - Build a wrangler to process an incoming file.
   *
   * The message used in the constructor doesn't have to be the first
   * message in the sequence.  Any will do.  The message is not
   * processed from the constructor, so be sure to call processMsg() as
   * well.
   */
    public InputFileWrangler(JxtaCast jc, Message msg) {
        this.jc = jc;
        lastActivity = System.currentTimeMillis();
        sender = jc.getMsgString(msg, jc.SENDERNAME);
        senderId = jc.getMsgString(msg, jc.SENDERID);
        key = jc.getMsgString(msg, jc.FILEKEY);
        filename = jc.getMsgString(msg, jc.FILENAME);
        blocksReceived = 0;
        totalBlocks = Integer.parseInt(jc.getMsgString(msg, jc.TOTALBLOCKS));
        myBlockSize = Integer.parseInt(jc.getMsgString(msg, jc.BLOCKSIZE));
        fdata = new byte[Integer.parseInt(jc.getMsgString(msg, jc.FILESIZE))];
        blockIn = new boolean[totalBlocks];
        lastAck = new String[totalBlocks];
        askedOrig = new boolean[totalBlocks];
        currReqBlock = 0;
        lastReqTime = System.currentTimeMillis();
        minTimeToWait = 4000;
    }

    /** Process a file transfer message.
   */
    public void processMsg(Message msg) {
        String msgType = jc.getMsgString(msg, jc.MESSAGETYPE);
        if (msgType.equals(jc.MSG_FILE) || msgType.equals(jc.MSG_FILE_REQ_RESP)) processMsgFile(msg); else if (msgType.equals(jc.MSG_FILE_ACK)) processMsgAck(msg); else if (msgType.equals(jc.MSG_FILE_REQ)) processMsgReq(msg);
    }

    /** Receive a regular 'maintainence' check-in from the TrailBoss.
   */
    public void bossCheck() {
        if (System.currentTimeMillis() - lastActivity > jc.inWranglerLifetime) jc.wranglers.remove(key);
        long timeToWait = jc.timeTilReq;
        long avgTimeTweenBlocks;
        if (blocksReceived > 1) {
            avgTimeTweenBlocks = (latestBlockTime - firstBlockTime) / blocksReceived;
            if ((avgTimeTweenBlocks * 3) < timeToWait) timeToWait = avgTimeTweenBlocks * 2;
            if (timeToWait < minTimeToWait) timeToWait = minTimeToWait;
        }
        if (blocksReceived < totalBlocks && System.currentTimeMillis() - lastReqTime > timeToWait && System.currentTimeMillis() - lastActivity > timeToWait) {
            requestNextMissingBlock();
        }
    }

    /** Process one incoming block of file data.
   */
    public void processMsgFile(Message msg) {
        lastActivity = System.currentTimeMillis();
        try {
            int blockNum = Integer.parseInt(jc.getMsgString(msg, jc.BLOCKNUM));
            String msgSender = jc.getMsgString(msg, jc.SENDERNAME);
            String msgSenderId = jc.getMsgString(msg, jc.SENDERID);
            if (blockIn[blockNum] == true) {
                if (!msgSenderId.equals(jc.myPeer.getPeerID().toString())) ;
                jc.logMsg("Duplicate block: " + filename + " block: " + (blockNum + 1));
                return;
            }
            latestBlockTime = lastActivity;
            if (blocksReceived == 0) firstBlockTime = lastActivity;
            minTimeToWait = 4000;
            if (blockNum == 0) caption = jc.getMsgString(msg, jc.CAPTION);
            jc.logMsg("From " + sender + " - " + " < " + filename + " > block: " + (blockNum + 1) + " of " + totalBlocks);
            MessageElement elem = msg.getMessageElement(jc.DATABLOCK);
            if (elem == null) return;
            byte dataBlock[] = elem.getBytes(false);
            System.arraycopy(dataBlock, 0, fdata, blockNum * myBlockSize, dataBlock.length);
            blockIn[blockNum] = true;
            blocksReceived++;
            sendAck(msg);
            if (blocksReceived == totalBlocks) {
                writeFile();
            } else if (jc.getMsgString(msg, jc.MESSAGETYPE).equals(jc.MSG_FILE_REQ_RESP)) {
                requestNextMissingBlock();
            }
            JxtaCastEvent e = new JxtaCastEvent();
            e.transType = e.RECV;
            e.filename = new String(filename);
            e.filepath = new String(jc.fileSaveLoc);
            e.senderId = new String(senderId);
            if (sender == null) sender = "<anonymous>";
            e.sender = new String(sender);
            if (caption != null) e.caption = new String(caption);
            e.percentDone = ((float) blocksReceived / totalBlocks) * 100;
            jc.sendJxtaCastEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Send an acknowledgement that we've received a file data block.
   */
    private void sendAck(Message msg) {
        try {
            Message ackMsg = new Message();
            jc.setMsgString(ackMsg, jc.MESSAGETYPE, jc.MSG_FILE_ACK);
            jc.setMsgString(ackMsg, jc.SENDERNAME, jc.myPeer.getName());
            jc.setMsgString(ackMsg, jc.SENDERID, jc.myPeer.getPeerID().toString());
            jc.setMsgString(ackMsg, jc.VERSION, jc.version);
            jc.setMsgString(ackMsg, jc.FILEKEY, jc.getMsgString(msg, jc.FILEKEY));
            jc.setMsgString(ackMsg, jc.FILENAME, filename);
            jc.setMsgString(ackMsg, jc.BLOCKNUM, jc.getMsgString(msg, jc.BLOCKNUM));
            int blockNum = Integer.parseInt(jc.getMsgString(msg, jc.BLOCKNUM));
            jc.logMsg("Sending ACK: " + filename + "  block " + (blockNum + 1));
            jc.sendMessage(ackMsg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Send a request for specific file data block.
   *
   *  @param  blockNum  the block to request.
   */
    private void sendReq(int blockNum) {
        minTimeToWait += 4000;
        try {
            Message reqMsg = new Message();
            jc.setMsgString(reqMsg, jc.MESSAGETYPE, jc.MSG_FILE_REQ);
            jc.setMsgString(reqMsg, jc.SENDERNAME, jc.myPeer.getName());
            jc.setMsgString(reqMsg, jc.SENDERID, jc.myPeer.getPeerID().toString());
            jc.setMsgString(reqMsg, jc.VERSION, jc.version);
            jc.setMsgString(reqMsg, jc.FILEKEY, key);
            jc.setMsgString(reqMsg, jc.FILENAME, filename);
            jc.setMsgString(reqMsg, jc.BLOCKNUM, String.valueOf(blockNum));
            String reqTo = "last ACK";
            if (lastAck[blockNum] != null) {
                jc.setMsgString(reqMsg, jc.REQTOPEER, lastAck[blockNum]);
                lastAck[blockNum] = null;
            } else if (!askedOrig[blockNum]) {
                jc.setMsgString(reqMsg, jc.REQTOPEER, senderId);
                askedOrig[blockNum] = true;
                reqTo = "orig sender";
            } else {
                jc.setMsgString(reqMsg, jc.REQTOPEER, jc.REQ_ANYPEER);
                reqTo = "ANYONE!";
            }
            jc.logMsg("Sending REQ to " + reqTo + ": " + filename + "  block " + (blockNum + 1));
            jc.sendMessage(reqMsg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
   * Request the next missing block of file data.  We request the block from
   * the latest peer known to have received that block.  If none are known, or
   * we've already requested from that peer and not gotten a response, we request
   * from the original sender of the file.  If we've already done that, then
   * we request from anyone.  (We hope not to have to do that, since it may
   * result in many peers responding at once with the same data block.)
   */
    private void requestNextMissingBlock() {
        while (currReqBlock < blockIn.length) {
            if (blockIn[currReqBlock] == false) {
                sendReq(currReqBlock);
                currReqBlock++;
                lastReqTime = System.currentTimeMillis();
                break;
            }
            currReqBlock++;
        }
        if (currReqBlock == blockIn.length) currReqBlock = 0;
    }

    /** Process a file transfer ACK message.
   *
   *  Peers will send out an ACK message when they've received a block.
   *  We'll keep track of the latest peer that sent an ACK for each block.
   *  If we don't get that block ourselves, we can request it from a peer
   *  that has it.
   */
    private void processMsgAck(Message msg) {
        String senderId = jc.getMsgString(msg, jc.SENDERID);
        if (senderId.equals(jc.myPeer.getPeerID().toString())) return;
        int blockNum = Integer.parseInt(jc.getMsgString(msg, jc.BLOCKNUM));
        lastAck[blockNum] = jc.getMsgString(msg, jc.SENDERID);
        jc.logMsg("Received ACK: " + filename + "  block " + (blockNum + 1) + ", from " + jc.getMsgString(msg, jc.SENDERNAME));
    }

    /** Process a file transfer REQ message.
   *
   *  A peer has requested a block of this file.  If the request is addressed
   *  to us, or to any peer, send the block out as a REQ_RESP 'request response'
   *  message.
   */
    private void processMsgReq(Message msg) {
        String reqToPeer = jc.getMsgString(msg, jc.REQTOPEER);
        if (reqToPeer == null) return;
        if (!reqToPeer.equals(jc.myPeer.getPeerID().toString()) && !reqToPeer.equals(jc.REQ_ANYPEER)) return;
        String reqSender = jc.getMsgString(msg, jc.SENDERID);
        if (reqSender == null) return;
        if (reqSender.equals(jc.myPeer.getPeerID().toString())) return;
        int blockNum = Integer.parseInt(jc.getMsgString(msg, jc.BLOCKNUM));
        if (reqToPeer.equals(jc.REQ_ANYPEER)) jc.logMsg("Received REQ_ANYPEER: " + filename + "  block " + (blockNum + 1) + ", from " + jc.getMsgString(msg, jc.SENDERNAME)); else jc.logMsg("Received FILE_REQ: " + filename + "  block " + (blockNum + 1) + ", from " + jc.getMsgString(msg, jc.SENDERNAME));
        if (blockNum > 0 && blockNum < blockIn.length && blockIn[blockNum] == true) sendBlock(blockNum, jc.MSG_FILE_REQ_RESP);
    }

    /** Write the file data to a disk file.
   */
    private void writeFile() {
        jc.logMsg("*** WRITING FILE ***   " + jc.fileSaveLoc + filename);
        try {
            FileOutputStream fos = new FileOutputStream(jc.fileSaveLoc + filename);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(fdata, 0, fdata.length);
            bos.flush();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
