package net.sourceforge.jmodbus;

/**
 * Interface for Modbus transport mechanisims.  This is the interface
 * that will be implemented by classes representing the transports for the
 * different flavours of Modbus.  Examples are ModbusRTU, ModbusASCII
 * and ModbusTCP.
 *
 * Classes implementing this interface are reponsible for the framing 
 * and deframing of Modbus messages from ther transport media and the 
 * actual sending and recieving of data.
 *
 * @author Kelvin Proctor
 */
public interface ModbusTransport {

    /**
     * Method to send a Modbus frame via the transport media.  The return 
     * status of the function indicates if the transmission sucedded.
     *
     * @author Kelvin Proctor
     *
     * @param msg The Modbus Message to be sent.
     * @return    Transmission sucess flag, to indicate if the transmission
     *            was sucessful.
     */
    public boolean sendFrame(ModbusMessage msg);

    /**
     * Method to receive a Modbus frame via the transport media.  The return 
     * value indicates the length of the frame.  This method will block until
     * the comminication path is terminated or a frame is sucessfully received.
     *
     * @author Kelvin Proctor
     *
     * @param msg The Modbus Message object for received data to be written into
     * @return    Receive sucess flag, to indicate if the receive was sucessful.
     */
    public boolean receiveFrame(ModbusMessage msg);
}
