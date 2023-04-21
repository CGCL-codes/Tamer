package tcg.scada.sim.iecsim.datastore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import tcg.scada.iec.EIecApduFormat;
import tcg.scada.iec.EIecApduUFormatType;
import tcg.scada.iec.EIecTypeId;
import tcg.scada.iec.IecApdu;
import tcg.scada.iec.EIecAsduSequenceType;
import tcg.scada.iec.EIecCauseOfTransmission;
import tcg.scada.iec.IecAsdu;
import tcg.scada.iec.IecInfoElement;
import tcg.scada.iec.IecInfoObject;
import tcg.common.util.FifoQueue;

public class IecServerThread {

    private static final int MAX_INFO_OBJECT_ARRAY = 1;

    private static final short ERROR_THRESHOLD = 3;

    private static final int DEF_THREADSLEEP = 50;

    private static final int MAX_UNACK_MESSAGES = 200;

    private static final long MAX_UNACK_TIMEOUT = 60000;

    private Socket sock_ = null;

    private IecServer parent_ = null;

    private String remoteAddr_ = null;

    private Logger logger_ = null;

    private InputStream in_ = null;

    private OutputStream out_ = null;

    private boolean isDataEnabled_ = false;

    FifoQueue<IecApdu> sendingQueue_ = new FifoQueue<IecApdu>();

    ArrayList<IecMessage> unAckMessages_ = new ArrayList<IecMessage>();

    private int sendSequenceNo_ = 1;

    private int recvSequenceNo_ = 0;

    private Thread sendingThread_ = null;

    private Thread receivingThread_ = null;

    private boolean keepSending_ = false;

    private boolean keepReceiving_ = false;

    IecServerThread(IecServer parent, Socket socket) {
        parent_ = parent;
        sock_ = socket;
        if (sock_ != null && sock_.isConnected()) {
            remoteAddr_ = sock_.getInetAddress().getHostAddress() + ":" + sock_.getPort();
        }
        logger_ = parent_.logger_;
    }

    public synchronized void start() {
        if (sock_ == null) return;
        NDC.push(remoteAddr_);
        logger_.info("Client thread " + remoteAddr_ + " is started.");
        parent_.registerClient(this);
        if (in_ == null || out_ == null) {
            try {
                in_ = sock_.getInputStream();
                out_ = sock_.getOutputStream();
            } catch (IOException ioe) {
                logger_.error("Can not get input/output streams. Terminating current thread.");
                NDC.pop();
                return;
            }
        }
        if (sendingThread_ != null && (!sendingThread_.isAlive() || !keepSending_)) {
            sendingThread_ = null;
        }
        if (sendingThread_ == null) {
            keepSending_ = true;
            sendingThread_ = new Thread(new SendingThread());
            sendingThread_.start();
        }
        if (receivingThread_ != null && (!receivingThread_.isAlive() || !keepReceiving_)) {
            receivingThread_ = null;
        }
        if (receivingThread_ == null) {
            keepReceiving_ = true;
            receivingThread_ = new Thread(new ReceivingThread());
            receivingThread_.start();
        }
        NDC.pop();
    }

    /**
	 * Stop the client service thread. 
	 * This will stop the internal sending and receiving thread.
	 */
    public synchronized void stop() {
        NDC.push(remoteAddr_);
        stopReceivingThread();
        stopSendingThread();
        __stop();
        NDC.pop();
    }

    /**
	 * Internal version of stop() which is not synchronized.
	 * This allows internal thread to call the stop() without causing deadlock!
	 */
    private void __stop() {
        try {
            sock_.close();
        } catch (IOException ioe) {
        }
        logger_.info("Client thread " + remoteAddr_ + " ends run");
        parent_.unregisterClient(this);
        in_ = null;
        out_ = null;
    }

    private void stopReceivingThread() {
        if (receivingThread_ != null) {
            keepReceiving_ = false;
            try {
                sendingThread_.join(1000);
            } catch (InterruptedException inte) {
            }
            receivingThread_ = null;
        }
    }

    private void stopSendingThread() {
        if (sendingThread_ != null) {
            keepSending_ = false;
            try {
                sendingThread_.join(1000);
            } catch (InterruptedException inte) {
            }
            sendingThread_ = null;
        }
    }

    public boolean isRunning() {
        if (sendingThread_ != null && keepSending_ && receivingThread_ != null && keepReceiving_) return true;
        return false;
    }

    public String getRemoteAddress() {
        return remoteAddr_;
    }

    public void addApdu(IecApdu apdu) {
        synchronized (sendingQueue_) {
            sendingQueue_.push(apdu);
        }
    }

    public void addUnackMessage(int sequenceNo) {
        IecMessage msg = new IecMessage(sequenceNo);
        synchronized (unAckMessages_) {
            unAckMessages_.add(msg);
        }
    }

    public void recvMessageAck(int sequenceNo) {
        synchronized (unAckMessages_) {
            int pos = -1;
            for (int i = 0; i < unAckMessages_.size(); i++) {
                if (unAckMessages_.get(i).sequenceNumber == sequenceNo) {
                    pos = i;
                    break;
                }
            }
            for (int i = 0; i <= pos; i++) {
                unAckMessages_.remove(0);
            }
        }
    }

    private boolean getRequest(IecApdu request) throws IOException {
        int result = 0;
        byte[] start_byte = new byte[1];
        start_byte[0] = 0;
        try {
            do {
                result = in_.read(start_byte, 0, 1);
                if (result == 0) {
                    return false;
                } else if (result == -1) {
                    return false;
                }
            } while (start_byte[0] != IecApdu.START_BYTE);
        } catch (IOException ioe) {
            logger_.error("Cannot read from socket. IOException: " + ioe.getMessage());
            throw ioe;
        }
        byte[] asdu_len = new byte[1];
        asdu_len[0] = 0;
        try {
            result = in_.read(asdu_len, 0, 1);
            if (result == -1) return false;
        } catch (IOException ioe) {
            logger_.error("Cannot read from socket. IOException: " + ioe.getMessage());
            throw ioe;
        }
        if (asdu_len[0] == 0) {
            logger_.error("ASDU Length can not be 0 bytes.");
            return false;
        }
        byte[] messages = new byte[asdu_len[0] + 2];
        messages[0] = start_byte[0];
        messages[1] = asdu_len[0];
        int nread = 0, toread = asdu_len[0];
        try {
            while (nread < toread) {
                result = in_.read(messages, nread + 2, toread - nread);
                if (result == -1) break;
                nread += result;
            }
        } catch (IOException ioe) {
            logger_.error("Cannot read from socket. IOException: " + ioe.getMessage());
            throw ioe;
        }
        printBuffer(logger_, messages, "REQUEST", 10);
        if (nread != toread) {
            logger_.error("Length of ASDU message is not as expected. Received: " + nread + ". Expected: " + toread);
            return false;
        }
        try {
            if (!request.parse(messages)) {
                logger_.error("Can not parse APDU!");
                return false;
            }
        } catch (Exception ex) {
            logger_.error("Can not parse APDU: " + ex.getMessage());
            return false;
        }
        if (request.format == EIecApduFormat.I_FORMAT && request.asdu.commonAddress != parent_.commonAddress_) {
            logger_.warn("Invalid common address: " + request.asdu.commonAddress + ". My common address is " + parent_.commonAddress_ + ". " + "For now, I will ignore this error.");
        }
        recvSequenceNo_ = request.sendSequenceNumber;
        if (request.format == EIecApduFormat.I_FORMAT || request.format == EIecApduFormat.S_FORMAT) {
            recvMessageAck(request.recvSequenceNumber);
        }
        return true;
    }

    private boolean handleRequest(IecApdu request, List<IecApdu> responses) {
        if (request.format == EIecApduFormat.I_FORMAT) {
            if (request.asdu.typeId == EIecTypeId.C_CS_NA_1) {
                logger_.info("Received clock synchronization command (C_CS_NA_1). At the moment we will just ignore it.");
                if (!isDataEnabled_) {
                    logger_.warn("Data transfer is not enabled. It will be ignored.");
                    return false;
                }
                if (request.asdu.causeOfTransmission.value() != EIecCauseOfTransmission.ACTIVATION.value()) {
                    logger_.warn("Cause of Transmission is not ACTIVATION (6). Maybe we should ignore this message?");
                    logger_.warn("For now, we will just process it as if the correct COT is set.");
                }
                logger_.info("Sending clock synchronization confirm (C_CS_NA_1, COT=7).");
                IecApdu response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_CONFIRM;
                responses.add(response);
            } else if (request.asdu.typeId == EIecTypeId.C_IC_NA_1) {
                int interroGroup = 0;
                try {
                    interroGroup = request.asdu.infoObjects.get(0).infoElements[0].getIntValue();
                } catch (Exception ex) {
                }
                logger_.info("Received interrogation command (C_IC_NA_1). Interro Group: " + interroGroup);
                if (!isDataEnabled_) {
                    logger_.warn("Data transfer is not enabled. It will be ignored.");
                    return false;
                }
                if (request.asdu.causeOfTransmission.value() != EIecCauseOfTransmission.ACTIVATION.value()) {
                    logger_.warn("Cause of Transmission is not ACTIVATION (6). Maybe we should ignore this message?");
                    logger_.warn("For now, we will just process it as if the correct COT is set.");
                }
                logger_.info("Sending interrogation command confirm (C_IC_NA_1, COT=7).");
                IecApdu response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_CONFIRM;
                responses.add(response);
                Iterator<DataPoint> it = null;
                DataPoint dp = null;
                Collection<DataPoint> dpList = null;
                EIecCauseOfTransmission cot = EIecCauseOfTransmission.INTERROGATED_STATION;
                if (interroGroup == 1) {
                    cot = EIecCauseOfTransmission.INTERROGATED_GROUP1;
                } else if (interroGroup == 2) {
                    cot = EIecCauseOfTransmission.INTERROGATED_GROUP2;
                } else if (interroGroup == 3) {
                    cot = EIecCauseOfTransmission.INTERROGATED_GROUP3;
                } else if (interroGroup == 4) {
                    cot = EIecCauseOfTransmission.INTERROGATED_GROUP4;
                }
                if (interroGroup == 0 || interroGroup == 1) {
                    dpList = parent_.data_.getAllDIPoints();
                    logger_.info("Sending all DI datapoints (as single value). Number of datapoints: " + dpList.size());
                    it = dpList.iterator();
                    while (it.hasNext()) {
                        response = new IecApdu();
                        response.format = EIecApduFormat.I_FORMAT;
                        response.asdu.typeId = EIecTypeId.M_SP_TB_1;
                        response.asdu.commonAddress = request.asdu.commonAddress;
                        response.asdu.sequenceType = EIecAsduSequenceType.SEQUENCE_OF_OBJECTS;
                        response.asdu.causeOfTransmission = cot;
                        while (response.asdu.infoObjects.size() < MAX_INFO_OBJECT_ARRAY && it.hasNext()) {
                            dp = it.next();
                            try {
                                IecInfoObject infoObject = new IecInfoObject();
                                infoObject.objectAddress = dp.getAddress();
                                infoObject.timeTag = dp.getTimestamp();
                                infoObject.infoElements = new IecInfoElement[1];
                                infoObject.infoElements[0] = new IecInfoElement();
                                infoObject.infoElements[0].setQuality(dp.getQuality());
                                infoObject.infoElements[0].setBooleanValue(dp.getBooleanValue());
                                response.asdu.infoObjects.add(infoObject);
                            } catch (Exception ex) {
                                logger_.error("Can not create IEC message for datapoint: " + dp.getName());
                                logger_.error("Exception: " + ex.getMessage());
                            }
                        }
                        responses.add(response);
                    }
                }
                if (interroGroup == 0 || interroGroup == 2) {
                    dpList = parent_.data_.getAllDDIPoints();
                    logger_.info("Sending all DDI datapoints (as double value). Number of datapoints: " + dpList.size());
                    it = dpList.iterator();
                    while (it.hasNext()) {
                        response = new IecApdu();
                        response.format = EIecApduFormat.I_FORMAT;
                        response.asdu.typeId = EIecTypeId.M_DP_TB_1;
                        response.asdu.commonAddress = request.asdu.commonAddress;
                        response.asdu.sequenceType = EIecAsduSequenceType.SEQUENCE_OF_OBJECTS;
                        response.asdu.causeOfTransmission = cot;
                        while (response.asdu.infoObjects.size() < MAX_INFO_OBJECT_ARRAY && it.hasNext()) {
                            dp = it.next();
                            try {
                                IecInfoObject infoObject = new IecInfoObject();
                                infoObject.objectAddress = dp.getAddress();
                                infoObject.timeTag = dp.getTimestamp();
                                infoObject.infoElements = new IecInfoElement[1];
                                infoObject.infoElements[0] = new IecInfoElement();
                                infoObject.infoElements[0].setQuality(dp.getQuality());
                                infoObject.infoElements[0].setIntValue(dp.getIntValue());
                                response.asdu.infoObjects.add(infoObject);
                            } catch (Exception ex) {
                                logger_.error("Can not create IEC message for datapoint: " + dp.getName());
                                logger_.error("Exception: " + ex.getMessage());
                            }
                        }
                        responses.add(response);
                    }
                }
                if (interroGroup == 0 || interroGroup == 3) {
                    dpList = parent_.data_.getAllTDIPoints();
                    logger_.info("Sending all TDI datapoints (as measured scaled value). Number of datapoints: " + dpList.size());
                    it = dpList.iterator();
                    while (it.hasNext()) {
                        response = new IecApdu();
                        response.format = EIecApduFormat.I_FORMAT;
                        response.asdu.typeId = EIecTypeId.M_ME_TE_1;
                        response.asdu.commonAddress = request.asdu.commonAddress;
                        response.asdu.sequenceType = EIecAsduSequenceType.SEQUENCE_OF_OBJECTS;
                        response.asdu.causeOfTransmission = cot;
                        while (response.asdu.infoObjects.size() < MAX_INFO_OBJECT_ARRAY && it.hasNext()) {
                            dp = it.next();
                            try {
                                IecInfoObject infoObject = new IecInfoObject();
                                infoObject.objectAddress = dp.getAddress();
                                infoObject.timeTag = dp.getTimestamp();
                                infoObject.infoElements = new IecInfoElement[1];
                                infoObject.infoElements[0] = new IecInfoElement();
                                infoObject.infoElements[0].setQuality(dp.getQuality());
                                infoObject.infoElements[0].setIntValue(dp.getIntValue());
                                response.asdu.infoObjects.add(infoObject);
                            } catch (Exception ex) {
                                logger_.error("Can not create IEC message for datapoint: " + dp.getName());
                                logger_.error("Exception: " + ex.getMessage());
                            }
                        }
                        responses.add(response);
                    }
                    dpList = parent_.data_.getAllAIPoints();
                    logger_.info("Sending all AI datapoints (as measured scaled value). Number of datapoints: " + dpList.size());
                    it = dpList.iterator();
                    while (it.hasNext()) {
                        response = new IecApdu();
                        response.format = EIecApduFormat.I_FORMAT;
                        response.asdu.typeId = EIecTypeId.M_ME_TE_1;
                        response.asdu.commonAddress = request.asdu.commonAddress;
                        response.asdu.sequenceType = EIecAsduSequenceType.SEQUENCE_OF_OBJECTS;
                        response.asdu.causeOfTransmission = cot;
                        while (response.asdu.infoObjects.size() < MAX_INFO_OBJECT_ARRAY && it.hasNext()) {
                            dp = it.next();
                            try {
                                IecInfoObject infoObject = new IecInfoObject();
                                infoObject.objectAddress = dp.getAddress();
                                infoObject.timeTag = dp.getTimestamp();
                                infoObject.infoElements = new IecInfoElement[1];
                                infoObject.infoElements[0] = new IecInfoElement();
                                infoObject.infoElements[0].setQuality(dp.getQuality());
                                infoObject.infoElements[0].setIntValue(dp.getIntValue());
                                response.asdu.infoObjects.add(infoObject);
                            } catch (Exception ex) {
                                logger_.error("Can not create IEC message for datapoint: " + dp.getName());
                                logger_.error("Exception: " + ex.getMessage());
                            }
                        }
                        responses.add(response);
                    }
                    dpList = parent_.data_.getAllUTCPoints();
                    logger_.info("Sending all UTC datapoints (as measured scaled value). Number of datapoints: " + dpList.size());
                    it = dpList.iterator();
                    while (it.hasNext()) {
                        response = new IecApdu();
                        response.format = EIecApduFormat.I_FORMAT;
                        response.asdu.typeId = EIecTypeId.M_ME_TE_1;
                        response.asdu.commonAddress = request.asdu.commonAddress;
                        response.asdu.sequenceType = EIecAsduSequenceType.SEQUENCE_OF_OBJECTS;
                        response.asdu.causeOfTransmission = cot;
                        while (response.asdu.infoObjects.size() < MAX_INFO_OBJECT_ARRAY && it.hasNext()) {
                            dp = it.next();
                            try {
                                IecInfoObject infoObject = new IecInfoObject();
                                infoObject.objectAddress = dp.getAddress();
                                infoObject.timeTag = dp.getTimestamp();
                                infoObject.infoElements = new IecInfoElement[1];
                                infoObject.infoElements[0] = new IecInfoElement();
                                infoObject.infoElements[0].setQuality(dp.getQuality());
                                infoObject.infoElements[0].setIntValue(dp.getIntValue());
                                response.asdu.infoObjects.add(infoObject);
                            } catch (Exception ex) {
                                logger_.error("Can not create IEC message for datapoint: " + dp.getName());
                                logger_.error("Exception: " + ex.getMessage());
                            }
                        }
                        responses.add(response);
                    }
                }
                if (interroGroup == 0 || interroGroup == 4) {
                    dpList = parent_.data_.getAllMIPoints();
                    logger_.info("Sending all MI datapoints (as integrated total). Number of datapoints: " + dpList.size());
                    it = dpList.iterator();
                    while (it.hasNext()) {
                        response = new IecApdu();
                        response.format = EIecApduFormat.I_FORMAT;
                        response.asdu.typeId = EIecTypeId.M_IT_TB_1;
                        response.asdu.commonAddress = request.asdu.commonAddress;
                        response.asdu.sequenceType = EIecAsduSequenceType.SEQUENCE_OF_OBJECTS;
                        response.asdu.causeOfTransmission = cot;
                        while (response.asdu.infoObjects.size() < MAX_INFO_OBJECT_ARRAY && it.hasNext()) {
                            dp = it.next();
                            try {
                                IecInfoObject infoObject = new IecInfoObject();
                                infoObject.objectAddress = dp.getAddress();
                                infoObject.timeTag = dp.getTimestamp();
                                infoObject.infoElements = new IecInfoElement[1];
                                infoObject.infoElements[0] = new IecInfoElement();
                                infoObject.infoElements[0].setQuality(dp.getQuality());
                                infoObject.infoElements[0].setIntValue(dp.getIntValue());
                                response.asdu.infoObjects.add(infoObject);
                            } catch (Exception ex) {
                                logger_.error("Can not create IEC message for datapoint: " + dp.getName());
                                logger_.error("Exception: " + ex.getMessage());
                            }
                        }
                        responses.add(response);
                    }
                }
                logger_.info("Sending interrogation command end (C_IC_NA_1, COT=10).");
                response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_TERMINATE;
                responses.add(response);
            } else if (request.asdu.typeId == EIecTypeId.C_CI_NA_1) {
                logger_.info("Received counter interrogation command (C_CI_NA_1).");
                if (!isDataEnabled_) {
                    logger_.warn("Data transfer is not enabled. It will be ignored.");
                    return false;
                }
                if (request.asdu.causeOfTransmission.value() != EIecCauseOfTransmission.ACTIVATION.value()) {
                    logger_.warn("Cause of Transmission is not ACTIVATION (6). Maybe we should ignore this message?");
                    logger_.warn("For now, we will just process it as if the correct COT is set.");
                }
                logger_.info("Sending counter interrogation command confirm (C_CI_NA_1, COT=7).");
                IecApdu response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_CONFIRM;
                responses.add(response);
                Iterator<DataPoint> it = null;
                DataPoint dp = null;
                Collection<DataPoint> dpList = null;
                dpList = parent_.data_.getAllMIPoints();
                logger_.info("Sending all MI datapoints. Number of datapoints: " + dpList.size());
                it = dpList.iterator();
                while (it.hasNext()) {
                    response = new IecApdu();
                    response.format = EIecApduFormat.I_FORMAT;
                    response.asdu.typeId = EIecTypeId.M_IT_TB_1;
                    response.asdu.commonAddress = request.asdu.commonAddress;
                    response.asdu.sequenceType = EIecAsduSequenceType.SEQUENCE_OF_OBJECTS;
                    response.asdu.causeOfTransmission = EIecCauseOfTransmission.INTERROGATED_STATION;
                    while (response.asdu.infoObjects.size() < MAX_INFO_OBJECT_ARRAY && it.hasNext()) {
                        dp = it.next();
                        try {
                            IecInfoObject infoObject = new IecInfoObject();
                            infoObject.objectAddress = dp.getAddress();
                            infoObject.timeTag = dp.getTimestamp();
                            infoObject.infoElements = new IecInfoElement[1];
                            infoObject.infoElements[0] = new IecInfoElement();
                            infoObject.infoElements[0].setQuality(dp.getQuality());
                            infoObject.infoElements[0].setIntValue(dp.getIntValue());
                            response.asdu.infoObjects.add(infoObject);
                        } catch (Exception ex) {
                            logger_.error("Can not create IEC message for datapoint: " + dp.getName());
                            logger_.error("Exception: " + ex.getMessage());
                        }
                    }
                    responses.add(response);
                }
                logger_.info("Sending counter interrogation command end (C_CI_NA_1, COT=10).");
                response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_TERMINATE;
                responses.add(response);
            } else if (request.asdu.typeId == EIecTypeId.C_SC_NA_1) {
                int address = request.asdu.infoObjects.get(0).objectAddress;
                boolean value = false;
                try {
                    value = request.asdu.infoObjects.get(0).infoElements[0].getBooleanValue();
                    logger_.info("Received single command (C_SC_NA_1). " + "Address: " + address + ". Value: " + value + ".");
                } catch (Exception ex) {
                    logger_.info("Received single command (C_SC_NA_1). " + "Address: " + address + ". Invalid value!");
                    logger_.error("Invalid single command value. Exception: " + ex.getMessage());
                    return false;
                }
                @SuppressWarnings("unused") int qualifier = request.asdu.infoObjects.get(0).infoElements[0].getQualifier();
                if (!isDataEnabled_) {
                    logger_.warn("Data transfer is not enabled. It will be ignored.");
                    return false;
                }
                if (request.asdu.causeOfTransmission.value() != EIecCauseOfTransmission.ACTIVATION.value()) {
                    logger_.warn("Cause of Transmission is not ACTIVATION (6). Maybe we should ignore this message?");
                    logger_.warn("For now, we will just process it as if the correct COT is set.");
                }
                logger_.info("Sending single command confirm (C_SC_NA_1, COT=7).");
                IecApdu response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_CONFIRM;
                responses.add(response);
                parent_.parent_.receiveControl(address, value);
                logger_.info("Sending single command end (C_SC_NA_1, COT=10).");
                response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_TERMINATE;
                responses.add(response);
            } else if (request.asdu.typeId == EIecTypeId.C_DC_NA_1) {
                int address = request.asdu.infoObjects.get(0).objectAddress;
                int value = 0;
                try {
                    value = request.asdu.infoObjects.get(0).infoElements[0].getIntValue();
                    logger_.info("Received double command (C_DC_NA_1). " + "Address: " + address + ". Value: " + value + ".");
                } catch (Exception ex) {
                    logger_.info("Received double command (C_DC_NA_1). " + "Address: " + address + ". Invalid value!");
                    logger_.error("Invalid double command value. Exception: " + ex.getMessage());
                    return false;
                }
                @SuppressWarnings("unused") int qualifier = request.asdu.infoObjects.get(0).infoElements[0].getQualifier();
                if (!isDataEnabled_) {
                    logger_.warn("Data transfer is not enabled. It will be ignored.");
                    return false;
                }
                if (request.asdu.causeOfTransmission.value() != EIecCauseOfTransmission.ACTIVATION.value()) {
                    logger_.warn("Cause of Transmission is not ACTIVATION (6). Maybe we should ignore this message?");
                    logger_.warn("For now, we will just process it as if the correct COT is set.");
                }
                logger_.info("Sending double command confirm (C_DC_NA_1, COT=7).");
                IecApdu response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_CONFIRM;
                responses.add(response);
                boolean boolVal = ((value & 0x01) > 0);
                parent_.parent_.receiveControl(address, boolVal);
                boolVal = ((value & 0x02) > 0);
                parent_.parent_.receiveControl(address, boolVal);
                logger_.info("Sending double command end (C_DC_NA_1, COT=10).");
                response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_TERMINATE;
                responses.add(response);
            } else if (request.asdu.typeId == EIecTypeId.C_SE_NA_1) {
                int address = request.asdu.infoObjects.get(0).objectAddress;
                int value = 0;
                try {
                    value = request.asdu.infoObjects.get(0).infoElements[0].getIntValue();
                    logger_.info("Received set point - normalized (C_SE_NA_1). " + "Address: " + address + ". Value: " + value + ".");
                } catch (Exception ex) {
                    logger_.info("Received set point - normalized (C_SE_NA_1). " + "Address: " + address + ". Invalid value!");
                    logger_.error("Invalid set point (normalized) value. Exception: " + ex.getMessage());
                    return false;
                }
                @SuppressWarnings("unused") int qualifier = request.asdu.infoObjects.get(0).infoElements[0].getQualifier();
                if (!isDataEnabled_) {
                    logger_.warn("Data transfer is not enabled. It will be ignored.");
                    return false;
                }
                if (request.asdu.causeOfTransmission.value() != EIecCauseOfTransmission.ACTIVATION.value()) {
                    logger_.warn("Cause of Transmission is not ACTIVATION (6). Maybe we should ignore this message?");
                    logger_.warn("For now, we will just process it as if the correct COT is set.");
                }
                logger_.info("Sending set point, normalized, command confirm (C_SE_NA_1, COT=7).");
                IecApdu response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_CONFIRM;
                responses.add(response);
                logger_.info("Sending set point, normalized, command end (C_SE_NA_1, COT=10).");
                response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_TERMINATE;
                responses.add(response);
            } else if (request.asdu.typeId == EIecTypeId.C_SE_NB_1) {
                int address = request.asdu.infoObjects.get(0).objectAddress;
                int value = 0;
                try {
                    value = request.asdu.infoObjects.get(0).infoElements[0].getIntValue();
                    logger_.info("Received set point - scaled (C_SE_NB_1). " + "Address: " + address + ". Value: " + value + ".");
                } catch (Exception ex) {
                    logger_.info("Received set point - scaled (C_SE_NB_1). " + "Address: " + address + ". Invalid value!");
                    logger_.error("Invalid set point (scaled) value. Exception: " + ex.getMessage());
                    return false;
                }
                @SuppressWarnings("unused") int qualifier = request.asdu.infoObjects.get(0).infoElements[0].getQualifier();
                if (!isDataEnabled_) {
                    logger_.warn("Data transfer is not enabled. It will be ignored.");
                    return false;
                }
                if (request.asdu.causeOfTransmission.value() != EIecCauseOfTransmission.ACTIVATION.value()) {
                    logger_.warn("Cause of Transmission is not ACTIVATION (6). Maybe we should ignore this message?");
                    logger_.warn("For now, we will just process it as if the correct COT is set.");
                }
                logger_.info("Sending set point, scaled, command confirm (C_SE_NB_1, COT=7).");
                IecApdu response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_CONFIRM;
                responses.add(response);
                parent_.parent_.receiveControl(address, value);
                logger_.info("Sending set point, scaled, command end (C_SE_NB_1, COT=10).");
                response = new IecApdu();
                response.format = EIecApduFormat.I_FORMAT;
                response.asdu = new IecAsdu(request.asdu);
                response.asdu.causeOfTransmission = EIecCauseOfTransmission.ACTIVATION_TERMINATE;
                responses.add(response);
            } else {
                logger_.warn("Received I_FORMAT with ASDU type " + request.asdu.typeId.toString() + ". Ignored!");
            }
        } else if (request.format == EIecApduFormat.U_FORMAT) {
            if (request.uFormatType == EIecApduUFormatType.STARTDT_ACT) {
                logger_.info("Received STARTDT_ACT. Data transfer will be enabled.");
                isDataEnabled_ = true;
                logger_.info("Sending STARTDT_CON. Data transfer has been enabled.");
                IecApdu response = new IecApdu();
                response.format = EIecApduFormat.U_FORMAT;
                response.uFormatType = EIecApduUFormatType.STARTDT_CON;
                responses.add(response);
            } else if (request.uFormatType == EIecApduUFormatType.STARTDT_CON) {
                logger_.warn("Received STARTDT_CON message. Ignored!");
            } else if (request.uFormatType == EIecApduUFormatType.STOPDT_ACT) {
                logger_.info("Received STOPDT_ACT. Data transfer will be disabled.");
                isDataEnabled_ = false;
                logger_.info("Received STOPDT_CON. Data transfer has been disabled.");
                IecApdu response = new IecApdu();
                response.format = EIecApduFormat.U_FORMAT;
                response.uFormatType = EIecApduUFormatType.STOPDT_CON;
                responses.add(response);
            } else if (request.uFormatType == EIecApduUFormatType.STOPDT_CON) {
                logger_.warn("Received STOPDT_CON message. Ignored!");
            } else if (request.uFormatType == EIecApduUFormatType.TESTFR_ACT) {
                logger_.info("Received TESTFR_ACT. Sending TESTFR_CON response.");
                IecApdu response = new IecApdu();
                response.format = EIecApduFormat.U_FORMAT;
                response.uFormatType = EIecApduUFormatType.TESTFR_CON;
                responses.add(response);
            } else if (request.uFormatType == EIecApduUFormatType.TESTFR_CON) {
                logger_.warn("Received TESTFR_CON message. Ignored!");
            }
        } else if (request.format == EIecApduFormat.S_FORMAT) {
            logger_.info("Received acknowledgment message (S_FORMAT). " + "Acknowledged sequence number: " + request.recvSequenceNumber);
        }
        return true;
    }

    private boolean sendResponse(IecApdu response) throws IOException {
        response.sendSequenceNumber = sendSequenceNo_++;
        response.recvSequenceNumber = recvSequenceNo_;
        byte[] buffer = null;
        try {
            buffer = response.build();
            if (buffer == null) {
                logger_.error("Can not build APDU!");
            }
        } catch (Exception ex) {
            logger_.error("Can not build APDU: " + ex.getMessage());
        }
        if (buffer == null || buffer.length == 0) {
            return false;
        }
        printBuffer(logger_, buffer, "RESPONSE", 10);
        try {
            out_.write(buffer);
            out_.flush();
            return true;
        } catch (IOException ioe) {
            logger_.error("Cannot write to socket. IOException: " + ioe.getMessage());
            throw ioe;
        }
    }

    private static void printBuffer(Logger logger, byte[] data, String prefix, int numCols) {
        int size = data.length;
        int numRows = 0;
        if ((size % numCols) == 0) {
            numRows = (int) (size / numCols);
        } else {
            numRows = (int) (size / numCols) + 1;
        }
        int idx = 0;
        for (int i = 0; i < numRows && idx < data.length; i++) {
            String tmpString = prefix + " [" + String.format("%1$-2s", Integer.toHexString(i)) + "] ";
            for (int j = 0; j < numCols && idx < data.length; j++) {
                tmpString += String.format("%1$-3s", Integer.toHexString(data[idx++] & 0x00ff));
            }
            logger.debug(tmpString);
        }
    }

    class ReceivingThread implements Runnable {

        public void run() {
            NDC.push(parent_.strContext_);
            NDC.push(remoteAddr_);
            NDC.push("RECV");
            logger_.info("Starting receiving thread...");
            IecApdu request = new IecApdu();
            int errorCounter = 0;
            boolean status = false;
            long timestamp = Calendar.getInstance().getTimeInMillis();
            while (keepReceiving_ && errorCounter < ERROR_THRESHOLD && parent_.isRunning() && sock_.isConnected()) {
                if ((Calendar.getInstance().getTimeInMillis() - timestamp) > IecServer.DEF_IDLE_TIMEOUT) {
                    logger_.info("Receiving thread has timed out. Stopping...");
                    keepReceiving_ = false;
                    break;
                }
                request.reset();
                status = false;
                try {
                    status = getRequest(request);
                    errorCounter = 0;
                } catch (IOException ioe) {
                    errorCounter++;
                    try {
                        Thread.sleep(DEF_THREADSLEEP);
                    } catch (InterruptedException ie) {
                    }
                    ;
                    continue;
                }
                if (!status) {
                    if (!keepReceiving_) break;
                    try {
                        Thread.sleep(DEF_THREADSLEEP);
                    } catch (InterruptedException ie) {
                    }
                    ;
                    continue;
                }
                timestamp = Calendar.getInstance().getTimeInMillis();
                if (parent_.replyConnection_) {
                    List<IecApdu> responses = new ArrayList<IecApdu>();
                    handleRequest(request, responses);
                    synchronized (sendingQueue_) {
                        for (int i = 0; i < responses.size(); i++) {
                            sendingQueue_.push(responses.get(i));
                        }
                    }
                } else {
                    logger_.info("Reply disabled. No reply.");
                }
                if (!keepReceiving_) break;
                try {
                    Thread.sleep(DEF_THREADSLEEP);
                } catch (InterruptedException ie) {
                }
                ;
            }
            if (keepReceiving_) {
                if (errorCounter >= ERROR_THRESHOLD) {
                    logger_.warn("Receiving thread error counter has reached ERROR_THRESHOLD. It will be terminated.");
                } else if (!parent_.isRunning()) {
                    logger_.warn("Listening server has stopped. Receiving thread will be terminated.");
                } else if (sock_.isConnected()) {
                    logger_.warn("Socket connection has been closed. Receiving thread will be terminated.");
                }
            }
            keepReceiving_ = false;
            logger_.info("Receiving thread has terminated.");
            stopSendingThread();
            __stop();
            NDC.pop();
            NDC.pop();
            NDC.pop();
        }
    }

    class SendingThread implements Runnable {

        public void run() {
            NDC.push(parent_.strContext_);
            NDC.push(remoteAddr_);
            NDC.push("SEND");
            logger_.info("Starting sending thread...");
            int errorCounter = 0;
            while (keepSending_ && errorCounter < ERROR_THRESHOLD && parent_.isRunning() && sock_.isConnected()) {
                if (unAckMessages_.size() > 0) {
                    long datediff = Calendar.getInstance().getTimeInMillis() - unAckMessages_.get(0).timestamp.getTime();
                    if (datediff > MAX_UNACK_TIMEOUT) {
                        logger_.warn("Oldest unacknowledged message has timed out (" + MAX_UNACK_TIMEOUT + " msec). " + "Sending thread will terminate.");
                        keepSending_ = false;
                        break;
                    }
                }
                if (unAckMessages_.size() > MAX_UNACK_MESSAGES) {
                    logger_.warn("No of unacknowledged messages has reached MAX_UNACK_MESSAGES (" + MAX_UNACK_MESSAGES + "). " + "Sending thread will terminate.");
                    keepSending_ = false;
                    break;
                }
                List<IecApdu> responses = new ArrayList<IecApdu>();
                synchronized (sendingQueue_) {
                    while (sendingQueue_.hasEntry()) {
                        responses.add(sendingQueue_.pop());
                    }
                }
                for (int i = 0; i < responses.size() && keepSending_ && errorCounter < ERROR_THRESHOLD; i++) {
                    try {
                        sendResponse(responses.get(i));
                        errorCounter = 0;
                    } catch (IOException ioe) {
                        errorCounter++;
                    }
                }
                if (!keepSending_ || errorCounter >= ERROR_THRESHOLD) break;
                try {
                    Thread.sleep(DEF_THREADSLEEP);
                } catch (InterruptedException ie) {
                }
                ;
            }
            if (keepSending_) {
                if (errorCounter >= ERROR_THRESHOLD) {
                    logger_.warn("Sending thread error counter has reached ERROR_THRESHOLD. It will be terminated.");
                } else if (!parent_.isRunning()) {
                    logger_.warn("Listening server has stopped. Sending thread will be terminated.");
                } else if (sock_.isConnected()) {
                    logger_.warn("Socket connection has been closed. Sending thread will be terminated.");
                }
            }
            keepSending_ = false;
            logger_.info("Sending thread has terminated.");
            stopReceivingThread();
            __stop();
            NDC.pop();
            NDC.pop();
            NDC.pop();
        }
    }

    class IecMessage {

        public int sequenceNumber;

        public Date timestamp;

        public IecMessage(int seqNum) {
            sequenceNumber = seqNum;
            timestamp = new Date();
        }
    }
}
