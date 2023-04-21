package eibstack;

import java.io.IOException;

/**
 *  Interface for incoming connectionless eib data (unicast) services.
 *  'incoming' means that this interface provides methods to indicate that a
 *  remote eib device wants to read or change local 'things', instead of 
 *  providing methods to read or change 'things' in a remote eib device.
 *
 *  @see OutgoingUnicastService
 */
public interface IncomingUnicastService {

    /**
     *  Registers the caller for incoming unicast services.
     *
     *  @param da  A list of remote physical addresses for which the caller registers.
     *             An empty array means to register with all possible addresses.
     *  @param l   The listener for the incoming service indications.
     *  @return    The access point for the requested registration.
     *
     *  @throws AlreadyRegisteredException if at least one of the specified addresses
     *      in 'da' is already registered by another AccessPoint. If thrown, none of the
     *      specified addresses is registered.
     */
    public AccessPoint register(int[] da, Listener l) throws AlreadyRegisteredException;

    /**
     *  Describes the interface of an access point object returned when succesfully
     *  calling the {@link #register(int[],Listener) register method}.
     */
    public interface AccessPoint {

        /**
         *  Opens an connection to a remote device with address da.
         *
         *  @param da        Physical address of remote device to connect to.
         *  @param l         The listener object for this connection.
         *  @param priority  Change default priority for this connection (default priority is low).
         *  @param hopCount  Change default hopCount for this connection (default hopCount is 6).
         *  @return          The connection object for this connection.
         *
         *  @throws IOException               if the connection could not be established.
         *  @throws IllegalArgumentException  if any of the parameters isn't adequate.

         *  @see Connection
         */
        public Connection connect(int da, Connection.Listener l) throws IOException;

        public Connection connect(int da, int pr, int hc, Connection.Listener l) throws IOException;

        /**
         *  Cancels the registrations for this access point.
         */
        public void cancel();
    }

    /**
     *  Describes the interface the user of the IncomingDataService has to provide
     *  when registering any physical addresses i.e. remote devices.
     */
    public interface Listener {

        /**
         *  Is called when a remote device has established a connection.
         *
         *  @param sa   Physical address of the remote device.
         *  @param con  The connection object of this new connection.
         *  @return     The listener object for this connection.
         *              (If null the connection will be refused.)
         *
         *  @see Connection
         */
        public Connection.Listener connected(int sa, Connection con);

        /**
         *  Called if a remote device wants to read a local property value.
         *
         *  @param sa        Pysical address of the originating device.
         *  @param pr        Inclosed priority.
         *  @param hc        Remain of the inclosed hop count.
         *  @param objIdx    The index of the eib-object.
         *  @param propID    The property-id in the specified eib-object.
         *  @param startIdx  The number of the first property element to read.
         *  @param noElems   The number of property elements to read.
         *  @return          The requested property elements or null if property elements 
         *                   do not exist or data would not fit in pdu or higher access rights required.
         */
        public byte[] readPropertyValue(int sa, int pr, int hc, int objIdx, int propID, long startIdx, int noElems);

        /**
         *  Called if a remote device wants to write a new value to a local property.
         *
         *  @param sa        Pysical address of the originating device.
         *  @param pr        Inclosed priority.
         *  @param hc        Remain of the inclosed hop count.
         *  @param objIdx    The index of the eib-object.
         *  @param propID    The property-id in the specified eib-object.
         *  @param startIdx  The number of the first property element to read.
         *  @param noElems   The number of property elements to read.
         *  @param data      The property elements to write as byte array.
         *  @return          The read back property elements after writing the above values
         *                   or null if property elements do not exist or data would not fit
         *                   in pdu or higher access rights required.
         */
        public byte[] writePropertyValue(int sa, int pr, int hc, int objIdx, int propID, long startIdx, int noElems, byte[] data);

        /**
         *  Called if a remote device wants to read the description of a local property.
         *
         *  @param sa       Pysical address of the originating device.
         *  @param pr       Inclosed priority.
         *  @param hc       Remain of the inclosed hop count.
         *  @param objIdx   The index of the eib-object.
         *  @param propID   The property-id in the specified eib-object.
         *  @param propIdx  The number of the first property element to read.
         *  @param noElems  The number of property elements to read.
         *  @return         The requested property description or null if property does not exist
         *                  (see {@link PropertyDescr}).
         */
        public PropertyDescr readPropertyDescr(int sa, int pr, int hc, int objIdx, int propID, int propIdx);
    }
}
