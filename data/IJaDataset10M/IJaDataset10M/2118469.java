package be.vds.jtbdive.model.divecomputer.uwatec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import be.vds.jtbdive.exceptions.TransferException;
import be.vds.jtbdive.external.com.DataComInterface;
import be.vds.jtbdive.model.Dive;
import be.vds.jtbdive.model.divecomputer.DiveComputerDataParser;
import be.vds.jtbdive.model.divecomputer.DiveComputerDataParserListener;

public class UwatecDiveComputerDataParser implements DiveComputerDataParser {

    private static final int TIME_OUT = 2000;

    private static final Logger logger = Logger.getLogger(UwatecDiveComputerDataParser.class);

    private DataComInterface dataComInterface;

    private boolean orderedData;

    private int[] binaries;

    private Set<DiveComputerDataParserListener> diveComputerDataParserListeners = new HashSet<DiveComputerDataParserListener>();

    @Override
    public void setOrderedData(boolean orderedData) {
        this.orderedData = orderedData;
    }

    private List<Dive> convertToDives(int[] binaries) {
        UwatecData aladinData = new UwatecData(binaries);
        UwatecDiveComputerAdapter adapter = new UwatecDiveComputerAdapter();
        List<Dive> dives = new ArrayList<Dive>();
        for (UwatecLogEntry logEntry : aladinData.getLogbook()) {
            dives.add(adapter.adapt(logEntry));
        }
        Collections.sort(dives, new Dive.DiveDateComparator());
        adapter.addProfileToDives(dives, aladinData);
        return dives;
    }

    @Override
    public List<Dive> read() throws TransferException {
        binaries = null;
        if (orderedData) {
            return readWithoutFormat();
        }
        return readWithFormat();
    }

    private List<Dive> readWithFormat() throws TransferException {
        try {
            try {
                notifyDiveComputerDataParserListeners("opening the port...");
                logger.debug("opening port");
                dataComInterface.open();
                logger.debug("opening opened");
                notifyDiveComputerDataParserListeners("port opened...");
            } catch (Exception e1) {
                logger.error(e1.getMessage());
                throw new TransferException("Error while opening the port");
            }
            int[] read_data = new int[2046];
            InputStream is = dataComInterface.getInputStream();
            int byteRead = 0;
            int timeout = 0;
            for (int i = 0, t = 0; i < 4 && timeout < TIME_OUT; t++) {
                int streamTimer = 0;
                while (is.available() == 0) {
                    if (streamTimer == 600000) {
                        try {
                            dataComInterface.close();
                            logger.info("streamTimer timeout reached");
                            throw new TransferException("time out reached");
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            throw new TransferException("Error while closing the port");
                        }
                    }
                    streamTimer++;
                }
                byteRead = is.read();
                if (byteRead != 0) {
                    logger.debug(byteRead);
                }
                if (i < 3) {
                    if (byteRead == 85) {
                        i++;
                    } else {
                        i = 0;
                    }
                } else if (byteRead == 0) {
                    i++;
                } else {
                    i = 0;
                }
                timeout++;
            }
            if (timeout == TIME_OUT) {
                try {
                    dataComInterface.close();
                    logger.info("timeout reached");
                    throw new TransferException("time out reached");
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    throw new TransferException("Error while closing the port");
                }
            }
            for (int j = 0; j < read_data.length; j++) {
                byteRead = is.read();
                read_data[j] = byteRead;
            }
            read_data = reorder_bytes(read_data, read_data.length);
            checkSum(read_data);
            try {
                dataComInterface.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new TransferException("Error while closing the port");
            }
            binaries = read_data;
            List<Dive> d = convertToDives(read_data);
            notifyDiveComputerDataParserListeners("done...");
            logger.info("reading finished");
            return d;
        } catch (IOException e) {
            e.printStackTrace();
            throw new TransferException("IO Exception");
        }
    }

    private List<Dive> readWithoutFormat() {
        try {
            try {
                dataComInterface.open();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            int[] read_data = new int[2046];
            InputStream is = dataComInterface.getInputStream();
            OutputStream os = dataComInterface.getOutputStream();
            int byteRead = 0;
            int timeout = 0;
            for (int i = 0, t = 0; i < 4 && timeout < TIME_OUT; t++) {
                for (int j = 0; j < read_data.length; j++) {
                    byteRead = is.read();
                    read_data[j] = byteRead;
                }
                checkSum(read_data);
                try {
                    dataComInterface.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return convertToDives(read_data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkSum(int[] read_data) {
        int checksum = read_data[2045] * 256 + read_data[2044];
        int sum = 0x1fe;
        for (int i = 0; i < read_data.length - 2; i++) {
            sum += read_data[i];
        }
        sum = sum % 65536;
        if (sum != checksum) {
            System.out.println("Not good checksum");
        }
    }

    private int[] reorder_bytes(int[] buf, int len) {
        int j;
        for (int i = 0; i < len; i++) {
            j = (buf[i] & 0x01) << 7;
            j += (buf[i] & 0x02) << 5;
            j += (buf[i] & 0x04) << 3;
            j += (buf[i] & 0x08) << 1;
            j += (buf[i] & 0x10) >> 1;
            j += (buf[i] & 0x20) >> 3;
            j += (buf[i] & 0x40) >> 5;
            j += (buf[i] & 0x80) >> 7;
            buf[i] = j;
        }
        return buf;
    }

    @Override
    public void setDataComInterface(DataComInterface dataComInterface) {
        this.dataComInterface = dataComInterface;
    }

    @Override
    public int[] getBinaries() {
        return binaries;
    }

    @Override
    public void addDiveComputerDataParserListener(DiveComputerDataParserListener diveComputerDataParserListener) {
        this.diveComputerDataParserListeners.add(diveComputerDataParserListener);
    }

    private void notifyDiveComputerDataParserListeners(String message) {
        for (DiveComputerDataParserListener listener : diveComputerDataParserListeners) {
            listener.notified(message);
        }
    }
}
