package org.openhealthexchange.openpixpdq.ihe;

import org.apache.log4j.Logger;
import org.openhealthexchange.openpixpdq.ihe.audit.IheAuditTrail;
import org.openhealthexchange.openpixpdq.ihe.impl_v2.hl7.HL7Util;
import org.openhealthexchange.openpixpdq.ihe.log.IMesaLogger;
import org.openhealthexchange.openpixpdq.ihe.log.IMessageStoreLogger;
import org.openhealthexchange.openpixpdq.ihe.log.MessageStore;
import org.openhealthexchange.openpixpdq.ihe.log.IMesaLogger;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.PipeParser;
import com.misyshealthcare.connect.net.IConnectionDescription;

/**
 * This class implements a number of useful methods shared by all
 * IHE Actors.
 * 
 * @author Jim Firby
 * @version 1.0 - Nov 20, 2005
 */
public class HL7Actor {

    private IMesaLogger mesaLogger = null;

    private IMessageStoreLogger storeLogger = null;

    private IheAuditTrail auditTrail = null;

    private IConnectionDescription connection = null;

    /**
	 * Creates a new HL7 Actor.
	 * 
	 */
    HL7Actor(IConnectionDescription connection) {
        this.connection = connection;
    }

    /**
	 * Creates a new HL7 Actor
	 * 
	 */
    public HL7Actor(IConnectionDescription connection, IheAuditTrail auditTrail) {
        this.auditTrail = auditTrail;
        this.connection = connection;
    }

    /**
	 *  Starts this actor. It must be called once for each actor when the program starts. 
	 */
    public void start() {
        if (auditTrail != null) auditTrail.start();
    }

    /**
	 * Stops this actor. It must be called once for each actor just before the program quits. 
	 */
    public void stop() {
        if (auditTrail != null) auditTrail.stop();
    }

    /**
	 * Returns a useful name for this Actor so that it can be put into
	 * debugging and logging messages.
	 * 
	 * @returns a useful name for this Actor
	 */
    public String getName() {
        if (connection != null) {
            return connection.getDescription();
        } else {
            return "unnamed";
        }
    }

    /**
	 * Logs an HL7 message and description to the error log.
	 * 
	 * @param message the HL7 message
	 * @param description a description of the problem with the message
	 */
    public void logHL7MessageError(Logger log, Message message, String description) {
        if (mesaLogger != null) {
            mesaLogger.writeString(description);
        } else {
            log.error(description);
            try {
                log.error(PipeParser.encode(message, new EncodingCharacters('|', "^~\\&")));
            } catch (HL7Exception e) {
                log.error("Error reporting HL7 error", e);
            }
        }
    }

    /**
	 * Checks whether this actor has a MESA test logger.
	 * 
	 * @return True if there is a defined MESA test logger.
	 */
    boolean hasMesaLogger() {
        return (mesaLogger != null);
    }

    /**
   * Gets the MESA test logger.
   * 
   * @return The MESA test logger.
   */
    public IMesaLogger getMesaLogger() {
        return mesaLogger;
    }

    /**
	 * Sets the custom logger for MESA test messages.  This is
	 * only used by MESA testing programs.
	 * 
	 * @param stream The logger for MESA messages
	 */
    public void setMesaLogger(IMesaLogger logger) {
        mesaLogger = logger;
    }

    /**
    * Gets the Message Store logger.
    *  
    * @return The Message Store logger.
    */
    public IMessageStoreLogger getStoreLogger() {
        return storeLogger;
    }

    /**
	 * Sets the logger to store message. 
	 * 
	 * @param storeLogger the storeLogger to set
	 */
    public void setStoreLogger(IMessageStoreLogger storeLogger) {
        this.storeLogger = storeLogger;
    }

    /**
	 * Initiates a <code>MessageStore</code> instance, and log the 
	 * initial message, either in-bound message or out-bound message.
	 * 
	 * @param message the initial message to log
	 * @param isInbound whether the message is an in-bound message or out-bound message 
	 * @return a <code>MessageStore</code>
	 * @throws HL7Exception
	 */
    public MessageStore initMessageStore(Message message, boolean isInbound) throws HL7Exception {
        if (storeLogger == null) return null;
        MessageStore ret = new MessageStore();
        if (message != null) {
            String encodedMessage = HL7Util.encodeMessage(message);
            if (isInbound) ret.setInMessage(encodedMessage); else ret.setOutMessage(encodedMessage);
        }
        return ret;
    }

    /**
	 * Persists the <code>MessageStore</code> log, and save the return message
	 * which could be either in-bound or out-bound.
	 * 
	 * @param message the last message to save and log
	 * @param isInbound whether the message is an in-bound message or out-bound message 
	 * @param msgStore the <code>MessageStore</code> instance to hold the log data
	 * @throws HL7Exception if the message could not be encoded 
	 */
    public void saveMessageStore(Message message, boolean isInbound, MessageStore msgStore) throws HL7Exception {
        if (msgStore == null || storeLogger == null) return;
        if (message != null) {
            String encodedMessage = HL7Util.encodeMessage(message);
            if (isInbound) msgStore.setInMessage(encodedMessage); else msgStore.setOutMessage(encodedMessage);
        }
        storeLogger.saveLog(msgStore);
    }

    /**
	 * Gets the Audit Trail of this actor.
	 * 
	 * @return the auditTrail
	 */
    public IheAuditTrail getAuditTrail() {
        return auditTrail;
    }

    /**
	 * Gets the <code>IConnectionDescription</code> of this actor.
	 * 
	 * @return the connection
	 */
    public IConnectionDescription getConnection() {
        return connection;
    }
}
