package br.unb.unbiquitous.ubiquitos.uos.test.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import br.unb.unbiquitous.ubiquitos.uos.adaptabitilyEngine.Gateway;
import br.unb.unbiquitous.ubiquitos.uos.application.UOSMessageContext;
import br.unb.unbiquitous.ubiquitos.uos.driverManager.UosDriver;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpDriver;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpService;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceCall;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceResponse;

/**
 * 
 * This is a simple stream driver to test the channel feature.
 * It just do like a chat app where it take a msg and return the response for that request by the channel.
 * 
 * @author Lucas Lins
 *
 */
public class StreamDriver implements UosDriver {

    private static final String MESSAGE_KEY = "message";

    private static final String CHANNELS_KEY = "channels";

    private static final Logger logger = Logger.getLogger(StreamDriver.class);

    /**
	 * Receives the request and starts the threaded chat manager.
	 * 
	 * @param serviceCall
	 * @param serviceResponse
	 * @param messageContext
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void chatService(ServiceCall serviceCall, ServiceResponse serviceResponse, UOSMessageContext messageContext) {
        logger.debug("Handling StreamDriver.chatService Call");
        Map parameters = serviceCall.getParameters();
        int channels = (Integer) parameters.get(CHANNELS_KEY);
        logger.debug("Caller DeviceName : " + messageContext.getCallerDevice().getNetworkDeviceName());
        Map responseMap = new HashMap();
        responseMap.put(MESSAGE_KEY, "CHAT STARTING...");
        for (int i = 0; i < channels; i++) {
            (new ChatServiceThreaded(i, messageContext)).start();
        }
        serviceResponse.setResponseData(responseMap);
    }

    /**
	 * The threaded class that will manager the data the will be received by the channel and generates the
	 * response for it.
	 *  
	 * @author Lucas Lins
	 *
	 */
    private class ChatServiceThreaded extends Thread {

        private int MAX_NOT_READY_TRIES = 10;

        private int NOT_READY_SLEEP_TIME = 100;

        private int channel;

        private UOSMessageContext msgContext;

        public ChatServiceThreaded(int channel, UOSMessageContext msgContext) {
            this.channel = channel;
            this.msgContext = msgContext;
        }

        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(msgContext.getDataInputStream(channel)));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(msgContext.getDataOutputStream(channel)));
                int notReadyCount = 0;
                while (true) {
                    if (reader.ready()) {
                        int available = msgContext.getDataInputStream(channel).available();
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < available; i++) {
                            builder.append((char) reader.read());
                        }
                        logger.debug("CHANNEL_DRIVER[" + channel + "]: RECEBIDO MSG: [" + builder.toString() + "]");
                        String msgRetorno = "CHANNEL_DRIVER[" + channel + "]: STREAM SERVICE RECEBEU: {" + builder.toString() + "}";
                        logger.debug("CHANNEL_DRIVER[" + channel + "]: ENVIANDO MSG: [" + msgRetorno + "]");
                        writer.write(msgRetorno);
                        writer.flush();
                    } else {
                        notReadyCount++;
                    }
                    if (notReadyCount > MAX_NOT_READY_TRIES) {
                        Thread.sleep(NOT_READY_SLEEP_TIME);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public UpDriver getDriver() {
        UpDriver driver = new UpDriver();
        driver.setName("StreamDriver");
        List<UpService> services = new ArrayList<UpService>();
        UpService listDrivers = new UpService();
        listDrivers.setName("chatService");
        Map<String, UpService.ParameterType> listDriversParameters = new HashMap<String, UpService.ParameterType>();
        listDriversParameters.put(MESSAGE_KEY, UpService.ParameterType.MANDATORY);
        listDrivers.setParameters(listDriversParameters);
        services.add(listDrivers);
        driver.setServices(services);
        return driver;
    }

    @Override
    public void init(Gateway gateway, String instanceId) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public List<UpDriver> getParent() {
        return null;
    }
}
