package eu.haslgruebler.darsens.host.scenario;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Logger;
import eu.haslgruebler.darsens.conf.DarSensConstants;
import eu.haslgruebler.darsens.conf.DefaultBehaviour;
import eu.haslgruebler.darsens.conf.Level;
import eu.haslgruebler.darsens.service.Message;
import eu.haslgruebler.darsens.service.Services;
import eu.haslgruebler.darsens.service.interaction.pojo.InteractionDiffNumber;
import eu.haslgruebler.darsens.service.interaction.pojo.InteractionTree;
import eu.haslgruebler.darsens.service.message.pojo.BluePrint;
import eu.haslgruebler.darsens.service.message.pojo.DataMessage;

/**
 * @author Michael Haslgrübler, uni-michael@haslgruebler.eu
 *
 */
public class ScenarioBehaviour extends DefaultBehaviour {

    private boolean once;

    private static Logger logger = Logger.getLogger(DarSensConstants.DarSens);

    /**
	 * @param services
	 */
    public ScenarioBehaviour(Services services) {
        super(services);
        once = true;
    }

    public void setupLogger() {
        logger.setLevel(java.util.logging.Level.FINE);
        Handler[] hs = logger.getHandlers();
        logger.setLevel(java.util.logging.Level.FINE);
        for (int i = 0; i < hs.length; i++) {
            hs[i].setLevel(java.util.logging.Level.FINE);
        }
        logger.log(java.util.logging.Level.FINEST, "update");
        once = false;
    }

    public void log(byte level, String msg) {
        if (once) setupLogger();
        logger.log(translatelog(level), msg);
    }

    private String getFolder(byte type) {
        switch(type) {
            case BluePrint.GET_CLASSIFICATION_MODEL:
                return "CL";
            case BluePrint.GET_FEATURES_MATRIX:
                return "FE";
            case BluePrint.GET_FILTER_MATRIX:
                return "FI";
        }
        return "CL";
    }

    private String readFile(BluePrint msg) {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("util" + File.separator + "scenario" + File.separator + msg.getId().intValue() + File.separator + getFolder(msg.getCommand()) + File.separator + msg.getLevel() + "-" + msg.getName())));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line.trim());
                sb.append('\n');
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public boolean beforeRecvMessage(Message msg) {
        if (DarSensConstants.DEBUG) services.getBehaviour().log(Level.FINEST, msg.toString());
        if (msg.getReceiver().asLong() != services.getAddress().asLong() && msg.getReceiver().asLong() != 0) return true;
        switch(msg.getMessageType()) {
            case DataMessage.MSGTYPE:
                break;
            case InteractionDiffNumber.MSGTYPE:
                return true;
            case InteractionTree.MSGTYPE:
                if (((InteractionTree) msg).getType() == InteractionTree.REQUEST_CAPABILITES) return true;
                if (DarSensConstants.DEBUG) services.getBehaviour().log(Level.WARNING, msg.toString());
                break;
            case BluePrint.MSGTYPE:
                switch(((BluePrint) msg).getCommand()) {
                    case BluePrint.GET_CLASSIFICATION_MODEL:
                    case BluePrint.GET_FEATURES_MATRIX:
                    case BluePrint.GET_FILTER_MATRIX:
                        try {
                            if (DarSensConstants.DEBUG) services.getBehaviour().log(Level.WARNING, " reading file");
                            services.getCommunicationService().openStreamConnection(msg.getSender());
                            DataOutputStream dos = services.getCommunicationService().getStreamConnection(msg.getSender()).openDataOutputStream();
                            String data = readFile((BluePrint) msg);
                            if (DarSensConstants.DEBUG) services.getBehaviour().log(Level.WARNING, " reading file done");
                            dos.writeUTF(data.trim());
                            dos.flush();
                            dos.close();
                            if (DarSensConstants.DEBUG) services.getBehaviour().log(Level.WARNING, " sending file done");
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        break;
                    case BluePrint.CLOSE_STREAMCONNECTION:
                        try {
                            services.getCommunicationService().closeStreamConnection(msg.getSender());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return false;
    }

    public void afterRecvMessage(Message msg) {
        super.afterRecvMessage(msg);
    }

    public void afterSendMessage(Message msg) {
        super.afterSendMessage(msg);
    }

    public boolean beforeSendMessage(Message msg) {
        return super.beforeSendMessage(msg);
    }

    private java.util.logging.Level translatelog(byte level) {
        switch(level) {
            case Level.CONFIG:
                return java.util.logging.Level.CONFIG;
            case Level.FINE:
                return java.util.logging.Level.FINE;
            case Level.FINER:
                return java.util.logging.Level.FINER;
            case Level.FINEST:
                return java.util.logging.Level.FINEST;
            case Level.WARNING:
                return java.util.logging.Level.WARNING;
            case Level.SEVERE:
                return java.util.logging.Level.SEVERE;
            case Level.INFO:
                return java.util.logging.Level.INFO;
            case Level.ALL:
                return java.util.logging.Level.ALL;
            case Level.NONE:
                return java.util.logging.Level.OFF;
            default:
                break;
        }
        return java.util.logging.Level.INFO;
    }
}
