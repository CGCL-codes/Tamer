package net.wsnware.common.filters;

import net.wsnware.core.Message;
import net.wsnware.core.MessageFilter;
import net.wsnware.core.utils.MessageSourceDefault;
import net.wsnware.core.utils.Utils;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import java.util.Dictionary;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Dummy sample implementation of a Channel (MessageFilter) with Additive Noise.
 * Once the filter is added to a generic MessageSource (and some listeners are
 * added to the filter), the filter will modify and/or discard messages according to setup.
 *
 * As standard filters (since they implements MessageSource interface) the open/close state
 * will enable/disable the transmission (forwarding) of messages, moreover a restart will reset internal stats.
 * The implementation (all methods) is thread-safe.
 *
 * Additive noise may be configured and disabled, moreover you can set also the average lost-message rate.
 *
 * Noise settings may be changed throw setters and configuration.
 *
 *
 * @author  Alessandro Polo <contact@alessandropolo.name>
 * @version 0.9.1
 * @date    2011-09-12
 */
public class NoiseChannel extends MessageSourceDefault implements MessageFilter {

    public static final String ComponentPID = "wsnware.filter.noisechannel";

    public NoiseChannel() {
        super();
        initDefaults();
    }

    public NoiseChannel(String pid) {
        super(pid);
        initDefaults();
    }

    public NoiseChannel(final Logger handlerLog) {
        super(handlerLog);
        initDefaults();
    }

    private void initDefaults() {
        configuration.put(PROP_ADD_NOISE, (Boolean) true);
        configuration.put(PROP_LOSTMESSAGERATE, (Double) lostMessageRate);
        configuration.put(PROP_IMPULSERATIO, (Double) impulseRatio);
        configuration.put(PROP_STDDEV, (Double) stdDev);
    }

    @Override
    protected void setOpenedInternal(boolean opened) {
        if (opened) resetStats();
        super.setOpenedInternal(opened);
    }

    @Override
    public Message polledMessage() {
        logger.warning("A filter shouldn't be polled..");
        return null;
    }

    @Override
    public synchronized void messageReceived(Message msg, Object source) {
        if (!opened) return;
        if (this.listeners_message.isEmpty()) {
            logger.log(Level.WARNING, "No listeners registered, not forwarding message# {0}", msg.msg_id);
            return;
        }
        if (!add_noise) {
            deliverMessage(msg, source);
            return;
        }
        if (shouldBeDiscarded()) {
            logger.log(Level.FINEST, "Discarding message# {0}", msg.msg_id);
            return;
        }
        deliverMessage(addNoise(msg), source);
    }

    protected boolean add_noise = true;

    public static final String PROP_ADD_NOISE = "add_noise";

    public boolean isAddNoise() {
        return add_noise;
    }

    public synchronized void setAddNoise(boolean add_noise) {
        boolean oldAdd_noise = this.add_noise;
        this.add_noise = add_noise;
        configuration.put(PROP_ADD_NOISE, (Boolean) add_noise);
        pcs.firePropertyChange(PROP_ADD_NOISE, oldAdd_noise, add_noise);
    }

    public void resetStats() {
        lostMessageStats.clear();
    }

    protected Message addNoise(Message msg) {
        Message msgNew = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objout = new ObjectOutputStream(baos);
            objout.writeObject(msg);
            objout.close();
            byte[] buffer = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream objin = new ObjectInputStream(bais);
            msgNew = (Message) objin.readObject();
        } catch (Exception ioe) {
        }
        return msgNew;
    }

    protected double impulseRatio = 0.05;

    public static final String PROP_IMPULSERATIO = "impulse_ratio";

    public void noise_impulse(byte[] msgInRaw) {
        if (msgInRaw == null) return;
        Random randGen = new Random();
        double rand;
        double halfImpulseRatio = impulseRatio / 2.0;
        for (int i = 0; i < msgInRaw.length; i++) {
            rand = randGen.nextDouble();
            if (rand < halfImpulseRatio) msgInRaw[i] = Byte.MIN_VALUE; else msgInRaw[i] = Byte.MAX_VALUE;
        }
    }

    public double getImpulseRatio() {
        return impulseRatio;
    }

    public synchronized void setImpulseRatio(double impulseRatio) {
        double oldImpulseRatio = this.impulseRatio;
        this.impulseRatio = impulseRatio;
        configuration.put(PROP_IMPULSERATIO, (Double) impulseRatio);
        pcs.firePropertyChange(PROP_IMPULSERATIO, oldImpulseRatio, impulseRatio);
    }

    /**
     * The standard deviation used for gaussian noise (default = 10).
     */
    protected double stdDev = 10.0;

    public static final String PROP_STDDEV = "std_dev";

    public void noise_gaussian(byte[] msgInRaw) {
        if (msgInRaw == null) return;
        Random randGen = new Random();
        double tmpByte;
        double gaussian;
        for (int i = 0; i < msgInRaw.length; i++) {
            gaussian = randGen.nextGaussian();
            tmpByte = (double) msgInRaw[i] + stdDev * gaussian;
            msgInRaw[i] = (byte) tmpByte;
        }
    }

    public double getStdDev() {
        return stdDev;
    }

    public synchronized void setStdDev(double stdDev) {
        double oldStdDev = this.stdDev;
        this.stdDev = stdDev;
        configuration.put(PROP_STDDEV, (Double) stdDev);
        pcs.firePropertyChange(PROP_STDDEV, oldStdDev, stdDev);
    }

    protected double lostMessageRate = 0.0D;

    public static final String PROP_LOSTMESSAGERATE = "lost_message_rate";

    public double getLostMessageRate() {
        return lostMessageRate;
    }

    public synchronized double getLostMessageRateReal() {
        return lostMessageStats.getMean();
    }

    public synchronized void setLostMessageRate(double lostMessageRate) {
        if (lostMessageRate < 0.0D) {
            logger.log(Level.WARNING, "Invalid Rate {0}, setting 0.0D", lostMessageRate);
            lostMessageRate = 0.0D;
        } else if (lostMessageRate > 1.0D) {
            logger.log(Level.WARNING, "Invalid Rate {0}, setting 1.0D", lostMessageRate);
            lostMessageRate = 1.0D;
        }
        double oldLostMessageRate = this.lostMessageRate;
        this.lostMessageRate = lostMessageRate;
        lostMessageStats.clear();
        configuration.put(PROP_LOSTMESSAGERATE, (Double) lostMessageRate);
        pcs.firePropertyChange(PROP_LOSTMESSAGERATE, oldLostMessageRate, lostMessageRate);
    }

    SummaryStatistics lostMessageStats = new SummaryStatistics();

    public boolean shouldBeDiscarded() {
        if (lostMessageRate <= 0.0D) return false;
        if (lostMessageRate >= 1.0D) return true;
        double currentRate = lostMessageStats.getMean();
        if (currentRate < lostMessageRate) {
            lostMessageStats.addValue(1.0D);
            return false;
        } else {
            lostMessageStats.addValue(0.0D);
            return true;
        }
    }

    @Override
    public void reconfigure(Dictionary properties) {
        super.reconfigure(properties);
        if (properties == null) {
            setImpulseRatio(0.05D);
            setStdDev(10.0D);
            setLostMessageRate(0.0D);
            return;
        }
        Double impulseO = Utils.getPropDouble(properties, PROP_IMPULSERATIO, true, logger);
        if (impulseO != null) setImpulseRatio(impulseO);
        Double stddevO = Utils.getPropDouble(properties, PROP_STDDEV, true, logger);
        if (stddevO != null) setStdDev(stddevO);
        Double lostmsgO = Utils.getPropDouble(properties, PROP_LOSTMESSAGERATE, true, logger);
        if (lostmsgO != null) setLostMessageRate(lostmsgO);
    }

    public static void main(String args[]) throws Exception {
        Utils.forceLogLevel("", Level.ALL);
        NoiseChannel noiseFilter = new NoiseChannel();
        noiseFilter.addMessageListener(new net.wsnware.common.messaging.MessageListenerNull());
        noiseFilter.setLostMessageRate(0.65445F);
        int i = 501;
        while (i-- > 0) {
            noiseFilter.messageReceived(new Message(), null);
        }
        double val = noiseFilter.getLostMessageRateReal();
        noiseFilter.addMessageListener(new net.wsnware.core.MessageListener() {

            @Override
            public void messageReceived(Message msg, Object source) {
                System.out.println("Message#" + msg.msg_id);
            }

            @Override
            public void reconfigure(Dictionary properties) {
            }

            @Override
            public Dictionary getConfiguration() {
                return null;
            }
        });
        Dictionary newConf = new java.util.Hashtable();
        noiseFilter.reconfigure(newConf);
        Thread.sleep(5000);
        noiseFilter.close();
        noiseFilter.messageReceived(new Message(), null);
    }
}
