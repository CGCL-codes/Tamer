package net.sourceforge.quexec.packet.chars.producer;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import net.sourceforge.quexec.packet.chars.consumer.CharPacketConsumer;
import net.sourceforge.quexec.proc.ExecutorServiceAware;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.NDC;

public abstract class AbstractCharPacketProducer implements CharPacketProducer, ExecutorServiceAware {

    private static final Log log = LogFactory.getLog(AbstractCharPacketProducer.class);

    private Future<?> future;

    private final CountDownLatch producerTaskFinished = new CountDownLatch(1);

    private List<? extends CharPacketConsumer> consumers = null;

    private String name;

    private ExecutorService executor;

    @Override
    public final void setExecutor(ExecutorService exec) {
        this.executor = exec;
    }

    @Override
    public final void setConsumers(List<? extends CharPacketConsumer> consumers) {
        this.consumers = consumers;
    }

    @Override
    public final void setConsumer(CharPacketConsumer consumer) {
        this.consumers = Collections.singletonList(consumer);
    }

    /**
	 * Returns the list of registered consumers.
	 */
    protected final List<? extends CharPacketConsumer> getConsumers() {
        return consumers;
    }

    /**
	 * Returns the producer name used for tracing purposes.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Set the name of the char packet producer. This name is optional. If it is
	 * defined, it is used for logging purposes.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Template method for setting up internal state before the producer is started.
	 * 
	 * This method is called just before {@link #start()} is executed.
	 */
    protected abstract void prepareStart();

    /**
	 * Template method for executing the actual char stream production logic.
	 */
    protected abstract void doProduction() throws InterruptedException;

    @Override
    public final void start() {
        prepareStart();
        if (this.consumers.isEmpty()) {
            log.warn("no consumers available.");
            return;
        }
        if (this.executor == null) {
            throw new IllegalStateException("executor must be defined");
        }
        this.future = this.executor.submit(new Runnable() {

            @Override
            public void run() {
                NDC.push(name);
                try {
                    log.debug("start producer task");
                    doProduction();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    producerTaskFinished.countDown();
                    log.debug("stopped producer task.");
                    NDC.remove();
                }
            }
        });
    }

    @Override
    public final void shutdown() {
        log.debug("shutdown initiated");
        this.future.cancel(true);
        try {
            this.producerTaskFinished.await();
        } catch (InterruptedException e) {
            log.warn("interrupt occurred while waiting for producer task: " + e + " (may lead to problems if code after shutdown() assumes that" + " process connectors have already terminated)");
            Thread.currentThread().interrupt();
        }
    }

    public final void await() throws InterruptedException {
        this.producerTaskFinished.await();
    }

    public final boolean isFinished() {
        return this.producerTaskFinished.getCount() == 0;
    }

    /**
	 * Helper method for transmitting a packet to all consumers.
	 * 
	 * @param count the number of characters to be transmitted
	 * @param packetBuf the buffer which contains the characters to be transmitted
	 */
    protected final void sendPacketToConsumers(int count, char[] packetBuf) {
        Iterator<? extends CharPacketConsumer> itConsumer = getConsumers().iterator();
        while (itConsumer.hasNext()) {
            CharPacketConsumer c = itConsumer.next();
            if (!c.consume(count, packetBuf)) {
                log.debug("consumer stops consumption: " + c);
                itConsumer.remove();
            }
        }
    }

    protected final void sendPacketToConsumers(String packet) {
        sendPacketToConsumers(packet.length(), packet.toCharArray());
    }

    /**
	 * Helper method for transmitting the final packet to all consumers.
	 * 
	 * @param count the number of characters to be transmitted
	 * @param packetBuf the buffer which contains the characters to be transmitted
	 */
    protected final void sendLastPacketToConsumers(int count, char[] packetBuf) {
        for (CharPacketConsumer c : getConsumers()) {
            c.endOfPackets(count, packetBuf);
        }
    }

    protected final void sendLastPacketToConsumers(String packet) {
        sendLastPacketToConsumers(packet.length(), packet.toCharArray());
    }
}
