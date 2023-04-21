package com.tenline.pinecone.platform.monitor.mina;

import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.osgi.framework.Bundle;
import com.tenline.pinecone.platform.model.Device;

/**
 * @author Bill
 *
 */
public abstract class AbstractMinaProtocolEncoder extends ProtocolEncoderAdapter {

    /**
	 * Protocol Bundle
	 */
    protected Bundle bundle;

    /**
	 * Protocol Logger
	 */
    protected Logger logger = Logger.getLogger(getClass());

    /**
	 * 
	 * @param bundle
	 */
    public AbstractMinaProtocolEncoder(Bundle bundle) {
        this.bundle = bundle;
    }

    /**
	 * Build Packet
	 * @param device
	 * @return
	 */
    protected abstract byte[] buildPacket(Device device);

    /**
	 * Build Packet Type
	 * @param map
	 * @return
	 */
    protected abstract byte[] buildPacketType(TreeMap<String, byte[]> map);

    /**
	 * Build Packet Data
	 * @param map
	 * @return
	 */
    protected abstract byte[] buildPacketData(TreeMap<String, byte[]> map);

    /**
	 * Build Packet Check
	 * @param bytes
	 * @return
	 */
    protected abstract byte buildPacketCheck(byte[] bytes);

    /**
	 * Transmit Packet
	 * @param packet
	 * @param output
	 */
    protected void transmitPacket(byte[] packet, ProtocolEncoderOutput output) {
        IoBuffer buffer = IoBuffer.allocate(1).setAutoExpand(true);
        buffer.put(packet);
        buffer.flip();
        output.write(buffer);
    }
}
