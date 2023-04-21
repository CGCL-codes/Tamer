package takatuka.drivers.interfaces;

import takatuka.drivers.radio.*;

/**
 * 
 * Description:
 * <p>
 *
 * Currently ISerial extends IRadio and does not change anything.
 * In future, we might have to add some extra things, hence this
 * interface is used to be on the safe side. We do not want to change user
 * application is serial interface is extended.
 *
 * </p> 
 * @author Edgar Oswald, Arthur Wahl
 * @version 1.0
 */
public interface ISerial {

    /**
     * Start the serial. By default the serial is Switched Off.
     * This function should have only synchronized implementations.
     */
    public void start();

    /**
     * Send a packet. Note that this should have only synchronized implementations.
     *
     * @param packet
     * @throws java.io.IOException
     */
    public void send(Packet packet) throws java.io.IOException;

    /**
     *
     * The function waits for a packet to be recieved. In case a packet is received
     * before the call of this function and is already saved in TakaTuka received packet
     * queue then the function returns that packet immediately. Otherwise the function
     * waits (block execution) until a packet is received.
     *
     * This function should have only synchronized implementations.
     *
     *
     * @param packet used by the function to return data.
     */
    public void receive(Packet packet);

    /**
     * If a packet is received
     * before the call of this function and is already saved in TakaTuka received packet
     * queue then the function returns that packet immediately. Otherwise, it wait for given
     * number of milliseconds to receive a packet. In case the TakaTuka
     * received packet queue is empty and no packet is received during the waiting period
     * then the function returns a null.
     * This function should have only synchronized implementations.
     *
     * WARNING: This function provides new data inside packet object only when the return
     * value is true. Otherwise, the packet remains unchanged.
     *
     * @param packet used by the function to return data
     * @param milliseconds wait for given number of milliseconds for the packet
     * @return false if the packet has not new data.
     */
    public boolean receive(Packet packet, int milliseconds);

    /**
     * create a packet with an empty byte array of maximum payload size. The
     * destination address in the packet created is set to Broadcast.
     * The maximum packet size is fixed at 21 and cannot be changed.
     *
     * @return returns a newly created packet
     */
    public Packet makePacket();

    /**
     * make packet with the given size of payload.
     * The destination address in the packet created is set to Broadcast.
     * @param sizeOfPacket
     * @return returns a newly create packet
     */
    public Packet makePacket(int sizeOfPacket);

    /**
     * return size of max payload allowed by the link layer.
     * This function is used by makePacket to create
     * packet of maximum payload length.
     * In case of serial packet this size is fixed at 21 bytes
     * and not changeable at the moment.
     * @return the maximum payload
     */
    public int maxPayloadLength();
}
