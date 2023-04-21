package org.nightlabs.connection.rxtx.config;

import gnu.io.CommPortIdentifier;
import java.util.ArrayList;
import java.util.List;
import org.nightlabs.connection.Connection;
import org.nightlabs.connection.config.ConnectionCf;
import org.nightlabs.connection.rxtx.SerialConnection;

/**
 * This class defines settings used to configure a serial port (RS232).
 *
 * @author Marco Schulze
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 * @version 1.0
 */
public class SerialConnectionCf extends ConnectionCf {

    private static final long serialVersionUID = 1L;

    private int baudRate = -1;

    private int dataBits = -1;

    private int stopBits = -1;

    private char parity = 'X';

    private boolean dtrOn = true;

    private boolean rtsOn = true;

    public static List<CommPortIdentifier> getSerialPortIdentifiers() {
        List<CommPortIdentifier> identifiers = PortMan.getPortIdentifiers();
        ArrayList<CommPortIdentifier> res = new ArrayList<CommPortIdentifier>(identifiers.size());
        for (CommPortIdentifier cpi : identifiers) {
            if (CommPortIdentifier.PORT_SERIAL == cpi.getPortType()) res.add(cpi);
        }
        return res;
    }

    public SerialConnectionCf() {
    }

    @Override
    public void init() {
        super.init();
        if (getAddress() == null) setAddress("/dev/ttyS0");
        if (baudRate == -1) setBaudRate(57600);
        if (dataBits == -1) setDataBits(8);
        if (stopBits == -1) setStopBits(1);
        if (parity == 'X') setParity('N');
    }

    @Override
    public Class<? extends Connection> getConnectionClass() {
        return SerialConnection.class;
    }

    public static final int[] BAUD_RATE = { 9600, 19200, 38400, 57600, 115200 };

    public static final int[] DATA_BITS = { 5, 6, 7, 8 };

    public static final int[] STOP_BITS = { 1, 2 };

    public static final char PARITY_NONE = 'N';

    public static final char PARITY_ODD = 'O';

    public static final char PARITY_EVEN = 'E';

    public static final char PARITY_MARK = 'M';

    public static final char PARITY_SPACE = 'S';

    public static final char[] PARITY = { 'N', 'O', 'E', 'M', 'S' };

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public char getParity() {
        return parity;
    }

    public void setParity(char parity) {
        if (parity != 'N' && parity != 'O' && parity != 'E' && parity != 'M' && parity != 'S') throw new IllegalArgumentException("parity invalid! Must be one of: 'N' (None), 'E' (Even), 'O' (Odd), 'M' (Mark), 'S' (Space)");
        this.parity = parity;
    }

    public boolean isDtrOn() {
        return dtrOn;
    }

    public void setDtrOn(boolean dtrOn) {
        this.dtrOn = dtrOn;
    }

    public boolean isRtsOn() {
        return rtsOn;
    }

    public void setRtsOn(boolean rtsOn) {
        this.rtsOn = rtsOn;
    }
}
