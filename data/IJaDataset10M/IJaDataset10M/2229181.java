package com.ojt.balance;

import org.apache.log4j.Logger;
import com.ojt.balance.kern.DEConnector;
import com.ojt.balance.kern.SerialDataListener;
import com.ojt.connector.ConnectionDescriptor;
import com.ojt.connector.Rs232ConnectionDescriptor;
import java.util.Enumeration;

/**
 * @author FMo
 * @since 20 avr. 2009 (FMo) : Cr�ation
 */
public class BalanceProtocolHandler implements SerialDataListener {

    private final BalanceDecoder decoder;

    private final BalanceDriverImpl driver;

    private final DEConnector connector;

    private final Logger logger = Logger.getLogger(getClass());

    public BalanceProtocolHandler(final BalanceDecoder decoder, final BalanceDriverImpl driver) {
        if ((decoder == null) || (driver == null)) {
            throw new IllegalArgumentException();
        }
        this.decoder = decoder;
        this.driver = driver;
        this.connector = new DEConnector();
        connector.addListener(this);
    }

    @Override
    public void dataReceived(final String frame) {
        try {
            notify(decoder.decode(frame));
        } catch (final Exception ex) {
            logger.error("FMo >> impossible de d�coder la trame", ex);
        }
    }

    private void notify(final BalanceFrame decodedFrame) {
        driver.frameReceived(decodedFrame);
    }

    public void start() {
        connector.start();
    }

    public void stop() {
        connector.stop();
    }

    public void setConnectionDescriptor(final ConnectionDescriptor desc) {
        if (desc instanceof Rs232ConnectionDescriptor) {
            connector.setConnectionDescriptor((Rs232ConnectionDescriptor) desc);
        }
    }

    public ConnectionDescriptor getConnectionDescriptor() {
        return connector.getConnectionDescriptor();
    }

    public Enumeration getAvalablePorts() {
        return connector.getAvalablePorts();
    }
}
