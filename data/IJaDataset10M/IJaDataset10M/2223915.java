package com.tenline.pinecone.platform.monitor.mina;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.serial.SerialAddress;
import org.apache.mina.transport.serial.SerialConnector;
import org.apache.mina.transport.serial.SerialAddress.DataBits;
import org.apache.mina.transport.serial.SerialAddress.FlowControl;
import org.apache.mina.transport.serial.SerialAddress.Parity;
import org.apache.mina.transport.serial.SerialAddress.StopBits;
import org.osgi.framework.Bundle;
import com.tenline.pinecone.platform.model.Device;
import com.tenline.pinecone.platform.monitor.BundleHelper;
import com.tenline.pinecone.platform.monitor.IEndpoint;

/**
 * @author Bill
 *
 */
public class MinaSerialEndpoint implements IEndpoint {

    /**
	 * Logger
	 */
    private Logger logger = Logger.getLogger(getClass());

    /**
	 * Concurrent Executor Service
	 */
    private ExecutorService executor;

    /**
	 * MINA IoConnector
	 */
    private IoConnector connector;

    /**
	 * MINA Session
	 */
    private IoSession session;

    /**
	 * MINA Handler
	 */
    private MinaHandler handler;

    /**
	 * MINA Protocol Codec Factory
	 */
    private MinaProtocolCodecFactory factory;

    /**
	 * Serial Ports
	 */
    private static ArrayList<String> serialPorts = new ArrayList<String>();

    /**
	 * 
	 */
    public MinaSerialEndpoint() {
        connector = new SerialConnector();
    }

    @Override
    public void close() {
        removePort();
        CloseFuture future = session.close(true);
        future.awaitUninterruptibly();
        if (future.isClosed()) {
            connector.dispose();
            executor.shutdown();
            factory.close();
            handler.close();
            logger.info("Close Endpoint");
        }
    }

    @Override
    public void initialize(Device device) {
        try {
            Bundle bundle = BundleHelper.getBundle(device);
            executor = Executors.newCachedThreadPool();
            connector.getFilterChain().addLast("executor", new ExecutorFilter(executor));
            factory = new MinaProtocolCodecFactory();
            factory.initialize(bundle);
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));
            handler = new MinaHandler();
            handler.initialize(factory.getBuilder());
            handler.getMapping().put(device, null);
            connector.setHandler(handler);
            ConnectFuture future = connector.connect(new SerialAddress(getPort(bundle), Integer.valueOf(bundle.getHeaders().get("Baud-Rate").toString()), getDataBits(Integer.valueOf(bundle.getHeaders().get("Data-Bits").toString())), getStopBits(Integer.valueOf(bundle.getHeaders().get("Stop-Bits").toString())), getParity(bundle.getHeaders().get("Parity").toString()), getFlowControl(bundle.getHeaders().get("Flow-Control").toString())));
            future.awaitUninterruptibly();
            if (future.isConnected()) {
                session = future.getSession();
                logger.info("Initialize Endpoint");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Get Port
	 * @param bundle
	 * @return
	 */
    private String getPort(Bundle bundle) {
        String port = bundle.getHeaders().get("Port").toString();
        if (port != null) {
            try {
                if (CommPortIdentifier.getPortIdentifier(port) != null) {
                    if (!serialPorts.contains(port)) serialPorts.add(port); else port = matchPort();
                }
            } catch (NoSuchPortException e) {
                port = matchPort();
            }
        } else {
            port = matchPort();
        }
        return port;
    }

    /**
	 * Match Port
	 * @return
	 */
    private String matchPort() {
        Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements()) {
            String port = ((CommPortIdentifier) ports.nextElement()).getName();
            if (!serialPorts.contains(port)) {
                serialPorts.add(port);
                return port;
            }
        }
        return null;
    }

    /**
	 * Remove Port
	 */
    private void removePort() {
        serialPorts.remove(((SerialAddress) session.getRemoteAddress()).getName());
    }

    /**
	 * Get Data Bits
	 * @param dataBits
	 * @return
	 */
    private DataBits getDataBits(int dataBits) {
        switch(dataBits) {
            case 5:
                return DataBits.DATABITS_5;
            case 6:
                return DataBits.DATABITS_6;
            case 7:
                return DataBits.DATABITS_7;
            case 8:
                return DataBits.DATABITS_8;
            default:
                return null;
        }
    }

    /**
	 * Get Stop Bits
	 * @param stopBits
	 * @return
	 */
    private StopBits getStopBits(int stopBits) {
        switch(stopBits) {
            case 1:
                return StopBits.BITS_1;
            case 2:
                return StopBits.BITS_2;
            default:
                return StopBits.BITS_1_5;
        }
    }

    /**
	 * Get Parity
	 * @param parity
	 * @return
	 */
    private Parity getParity(String parity) {
        if (parity.toLowerCase().equals("odd")) {
            return Parity.ODD;
        } else if (parity.toLowerCase().equals("even")) {
            return Parity.EVEN;
        } else if (parity.toLowerCase().equals("mark")) {
            return Parity.MARK;
        } else if (parity.toLowerCase().equals("space")) {
            return Parity.SPACE;
        } else {
            return Parity.NONE;
        }
    }

    /**
	 * Get Flow Control
	 * @param flowControl
	 * @return
	 */
    private FlowControl getFlowControl(String flowControl) {
        if (flowControl.toLowerCase().equals("rtscts_in")) {
            return FlowControl.RTSCTS_IN;
        } else if (flowControl.toLowerCase().equals("rtscts_out")) {
            return FlowControl.RTSCTS_OUT;
        } else if (flowControl.toLowerCase().equals("xonxoff_in")) {
            return FlowControl.XONXOFF_IN;
        } else if (flowControl.toLowerCase().equals("xonxoff_out")) {
            return FlowControl.XONXOFF_OUT;
        } else {
            return FlowControl.NONE;
        }
    }
}
