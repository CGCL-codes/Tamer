package org.gamio.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.gamio.annotation.Create;
import org.gamio.annotation.In;
import org.gamio.annotation.Remove;
import org.gamio.annotation.Run;
import org.gamio.comm.InMsg;
import org.gamio.comm.OutMsg;
import org.gamio.logging.Log;
import org.gamio.logging.LogFactory;
import org.gamio.processor.ProcessorContext;
import org.gamio.processor.Sender;

/**
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 26 $ $Date: 2008-10-14 10:39:23 -0400 (Tue, 14 Oct 2008) $
 */
public final class HelloProcessor {

    private static final Log log = LogFactory.getLog(HelloProcessor.class);

    private static int counter = 0;

    private static Lock lock = new ReentrantLock();

    private int id = 0;

    @In
    private ProcessorContext ctx = null;

    @In
    private InMsg inMsg = null;

    @In
    private Sender sender = null;

    private static int getId() {
        lock.lock();
        try {
            return counter++;
        } finally {
            lock.unlock();
        }
    }

    @Create
    public void create() {
        id = getId();
        log.debug("Processor[", id, "] is created.");
    }

    @Run
    public void process() {
        Integer msgId = (Integer) inMsg.get("msgId");
        String content = (String) inMsg.get("content");
        log.info("msgId[", msgId, "] content[", content, "]");
        OutMsg outMsg = sender.getOutMsg();
        outMsg.put("msgId", msgId);
        content = ctx.getAttribute("content");
        outMsg.put("content", content);
        sender.send(outMsg);
    }

    @Remove
    public void remove() {
        log.debug("Processor[", id, "] is removed");
    }
}
