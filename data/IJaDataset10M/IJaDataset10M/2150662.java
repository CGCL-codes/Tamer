package br.unb.unbiquitous.ubiquitos.uos.connectivityTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import br.unb.unbiquitous.ubiquitos.uos.context.UOSApplicationContext;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceCall;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceCall.ServiceType;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceResponse;

public class TestActiveStreamTCP extends TestCase {

    private static Logger logger = Logger.getLogger(TestActiveStreamTCP.class);

    protected UOSApplicationContext applicationContext;

    private static final int TIME_BETWEEN_TESTS = 500;

    private static final int TIME_TO_LET_BE_FOUND = 25000;

    protected static long currentTest = 0;

    private Object lock = Object.class;

    private boolean isOnTest = false;

    private int activeChannels;

    private static final int max_receive_tries = 30;

    @Override
    protected synchronized void setUp() throws Exception {
        synchronized (lock) {
            if (isOnTest) {
                System.out.println("====== Waiting Lock Release (" + lock.hashCode() + ") ======");
                lock.wait();
            }
            System.out.println("====== Locked (" + lock.hashCode() + ") " + isOnTest + "  ======");
            isOnTest = true;
        }
        logger.info("\n");
        logger.info("============== Teste : " + currentTest++ + " ========================== Begin");
        logger.info("\n");
        applicationContext = new UOSApplicationContext();
        applicationContext.init("br/unb/unbiquitous/ubiquitos/uos/connectivityTest/propertiesTCP");
    }

    @Override
    protected synchronized void tearDown() throws Exception {
        applicationContext.tearDown();
        logger.info("============== Teste : " + (currentTest - 1) + " ========================== End");
        Thread.sleep(TIME_BETWEEN_TESTS);
        synchronized (lock) {
            if (!isOnTest) {
                System.out.println("====== Waiting Lock Release (" + lock.hashCode() + ") ======");
                lock.wait();
            }
            System.out.println("====== UnLocked (" + lock.hashCode() + ") " + isOnTest + "  ======");
            isOnTest = false;
            lock.notify();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testTCPConsumesStreamUDP() throws Exception {
        Thread.sleep(TIME_TO_LET_BE_FOUND);
        logger.info("---------------------- testTCPConsumesStreamUDP BEGIN ---------------------- ");
        logger.info("Trying to consume the chat service from the Device Driver from the UDP machine");
        int channels = 5;
        ServiceCall serviceCall = new ServiceCall();
        serviceCall.setDriver("StreamDriver");
        serviceCall.setService("chatService");
        serviceCall.setInstanceId("streamDriverIdUDPDevice");
        serviceCall.setChannelType("Ethernet:TCP");
        serviceCall.setServiceType(ServiceType.STREAM);
        serviceCall.setChannels(channels);
        Map parameters = new HashMap();
        parameters.put("message", "testMessage");
        parameters.put("channels", channels);
        serviceCall.setParameters(parameters);
        ServiceResponse response = applicationContext.getGateway().callService(this.applicationContext.getDeviceManager().retrieveDevice("ProxyDevice"), serviceCall);
        assertNotNull(response);
        if (response != null && (response.getError() == null || response.getError().isEmpty())) {
            logger.info("Stream Service OK! ");
            logger.info("Let's see what we got: ");
            Map<String, String> mapa = response.getResponseData();
            logger.info("Returned encapsulated" + " : " + mapa.get("message"));
            if (mapa.get("message") == null) {
                return;
            }
            Thread.sleep(4000);
            activeChannels = channels;
            for (int i = 0; i < channels; i++) {
                ChatThreaded chatChannel = new ChatThreaded(i, response.getMessageContext().getDataInputStream(i), response.getMessageContext().getDataOutputStream(i));
                chatChannel.start();
            }
            logger.debug("waiting");
            while (activeChannels > 0) {
                logger.debug("probe : " + activeChannels);
                Thread.sleep(1000);
            }
            logger.debug("fim");
        } else {
            logger.error("Not possible to consume chat service from the UDP machine");
        }
        logger.info("---------------------- testTCPConsumesStreamUDP END ---------------------- ");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testTCPConsumesStreamBluetooth() throws Exception {
        Thread.sleep(TIME_TO_LET_BE_FOUND);
        logger.info("---------------------- testTCPConsumesStreamBluetooth BEGIN ---------------------- ");
        logger.info("Trying to consume the chat service from the Device Driver from the Bluetooth machine");
        int channels = 5;
        ServiceCall serviceCall = new ServiceCall();
        serviceCall.setDriver("StreamDriver");
        serviceCall.setService("chatService");
        serviceCall.setInstanceId("streamDriverIdBluetoothDevice");
        serviceCall.setChannelType("Ethernet:TCP");
        serviceCall.setServiceType(ServiceType.STREAM);
        serviceCall.setChannels(channels);
        Map parameters = new HashMap();
        parameters.put("message", "testMessage");
        parameters.put("channels", channels);
        serviceCall.setParameters(parameters);
        ServiceResponse response = applicationContext.getGateway().callService(this.applicationContext.getDeviceManager().retrieveDevice("ProxyDevice"), serviceCall);
        assertNotNull(response);
        if (response != null && (response.getError() == null || response.getError().isEmpty())) {
            logger.info("Stream Service OK! ");
            logger.info("Let's see what we got: ");
            Map<String, String> mapa = response.getResponseData();
            logger.info("Returned encapsulated" + " : " + mapa.get("message"));
            if (mapa.get("message") == null) {
                return;
            }
            Thread.sleep(4000);
            activeChannels = channels;
            for (int i = 0; i < channels; i++) {
                ChatThreaded chatChannel = new ChatThreaded(i, response.getMessageContext().getDataInputStream(i), response.getMessageContext().getDataOutputStream(i));
                chatChannel.start();
            }
            logger.debug("waiting");
            while (activeChannels > 0) {
                logger.debug("probe : " + activeChannels);
                Thread.sleep(1000);
            }
            logger.debug("fim");
        } else {
            logger.error("Not possible to consume chat service from the Bluetooth machine");
        }
        logger.info("---------------------- testTCPConsumesStreamBluetooth END ---------------------- ");
    }

    private synchronized void finalizeChannel() {
        activeChannels--;
    }

    private class ChatThreaded extends Thread {

        private int channelNumber;

        private InputStream in;

        private OutputStream out;

        public ChatThreaded(int channelNumber, InputStream in, OutputStream out) {
            this.channelNumber = channelNumber;
            this.in = in;
            this.out = out;
        }

        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                for (int j = 0; j < 10; j++) {
                    String msg = "CHANNEL[" + channelNumber + "]: MSG DE TESTE DO CHAT " + j;
                    logger.debug("CHANNEL[" + channelNumber + "]: ENVIANDO MSG: [" + msg + "]");
                    writer.write(msg);
                    writer.flush();
                    Thread.sleep(1000);
                    for (int trie = 0; trie < max_receive_tries; trie++) {
                        if (reader.ready()) {
                            int available = in.available();
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < available; i++) {
                                builder.append((char) reader.read());
                            }
                            logger.debug("CHANNEL[" + channelNumber + "]: RECEBIDO MSG: [" + builder.toString() + "]");
                            break;
                        }
                        Thread.sleep(300);
                    }
                }
                finalizeChannel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.debug("finalize :" + activeChannels);
        }
    }
}
