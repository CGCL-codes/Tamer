package net.sf.insim4j.packetfactory.impl;

import net.sf.insim4j.packetfactory.impl.InSimHeaderFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.sf.insim4j.ResponsePacket;
import net.sf.insim4j.insim.InSimResponsePacket;
import net.sf.insim4j.insim.enums.PacketType;
import net.sf.insim4j.insim.impl.general.InSimHeader;
import net.sf.insim4j.outgauge.impl.OutGaugePacketImpl;
import net.sf.insim4j.outsim.impl.OutSimPacketImpl;
import net.sf.insim4j.packetfactory.InSimPacketFactory;
import net.sf.insim4j.packetfactory.OutGaugePacketFactory;
import net.sf.insim4j.packetfactory.OutSimPacketFactory;
import net.sf.insim4j.packetfactory.ResponsePacketFactory;
import net.sf.insim4j.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ResponsePacketFactory creates response packets from received binary data.
 *
 * @author Jiří Sotona
 *
 */
public class ResponsePacketFactoryImpl implements ResponsePacketFactory {

    /**
	 * Log for this class. NOT static. For more details see
	 * http://commons.apache.org/logging/guide.html#Obtaining%20a%20Log%20Object
	 */
    private final Logger log = LoggerFactory.getLogger(ResponsePacketFactoryImpl.class);

    private static final String PREFIX = "create";

    private static final Map<PacketType, String> factoryMethodMap;

    static {
        factoryMethodMap = new HashMap<PacketType, String>();
        factoryMethodMap.put(PacketType.ISP_ISI, PREFIX + "Init");
        factoryMethodMap.put(PacketType.ISP_VER, PREFIX + "Version");
        factoryMethodMap.put(PacketType.ISP_TINY, PREFIX + "TinyPacket");
        factoryMethodMap.put(PacketType.ISP_SMALL, PREFIX + "SmallPacket");
        factoryMethodMap.put(PacketType.ISP_STA, PREFIX + "State");
        factoryMethodMap.put(PacketType.ISP_SCH, PREFIX + "SingleChar");
        factoryMethodMap.put(PacketType.ISP_SFP, PREFIX + "StateFlagsPack");
        factoryMethodMap.put(PacketType.ISP_SCC, PREFIX + "SetCarCamera");
        factoryMethodMap.put(PacketType.ISP_CPP, PREFIX + "CameraPosition");
        factoryMethodMap.put(PacketType.ISP_ISM, PREFIX + "Multi");
        factoryMethodMap.put(PacketType.ISP_MSO, PREFIX + "MsgOut");
        factoryMethodMap.put(PacketType.ISP_III, PREFIX + "Info");
        factoryMethodMap.put(PacketType.ISP_MST, PREFIX + "MsgType");
        factoryMethodMap.put(PacketType.ISP_MTC, PREFIX + "MsgToConnection");
        factoryMethodMap.put(PacketType.ISP_MOD, PREFIX + "ScreenMode");
        factoryMethodMap.put(PacketType.ISP_VTN, PREFIX + "VoteNotify");
        factoryMethodMap.put(PacketType.ISP_RST, PREFIX + "RaceStart");
        factoryMethodMap.put(PacketType.ISP_NCN, PREFIX + "NewConn");
        factoryMethodMap.put(PacketType.ISP_CNL, PREFIX + "ConnLeave");
        factoryMethodMap.put(PacketType.ISP_CPR, PREFIX + "ConnPlayerRename");
        factoryMethodMap.put(PacketType.ISP_NPL, PREFIX + "NewPlayer");
        factoryMethodMap.put(PacketType.ISP_PLP, PREFIX + "PlayerPits");
        factoryMethodMap.put(PacketType.ISP_PLL, PREFIX + "PlayerLeaveRace");
        factoryMethodMap.put(PacketType.ISP_LAP, PREFIX + "LapTime");
        factoryMethodMap.put(PacketType.ISP_SPX, PREFIX + "SplitTime");
        factoryMethodMap.put(PacketType.ISP_PIT, PREFIX + "PitStop");
        factoryMethodMap.put(PacketType.ISP_PSF, PREFIX + "PitStopFinished");
        factoryMethodMap.put(PacketType.ISP_PLA, PREFIX + "PitLane");
        factoryMethodMap.put(PacketType.ISP_CCH, PREFIX + "CameraChange");
        factoryMethodMap.put(PacketType.ISP_PEN, PREFIX + "Penalty");
        factoryMethodMap.put(PacketType.ISP_TOC, PREFIX + "TakeOverCar");
        factoryMethodMap.put(PacketType.ISP_FLG, PREFIX + "Flag");
        factoryMethodMap.put(PacketType.ISP_PFL, PREFIX + "PlayerFlags");
        factoryMethodMap.put(PacketType.ISP_FIN, PREFIX + "RaceFinished");
        factoryMethodMap.put(PacketType.ISP_RES, PREFIX + "Result");
        factoryMethodMap.put(PacketType.ISP_REO, PREFIX + "Reorder");
        factoryMethodMap.put(PacketType.ISP_NLP, PREFIX + "NodeLap");
        factoryMethodMap.put(PacketType.ISP_MCI, PREFIX + "MultiCarInfo");
        factoryMethodMap.put(PacketType.ISP_MSX, PREFIX + "MsgExt");
        factoryMethodMap.put(PacketType.ISP_MSL, PREFIX + "MsgLocal");
        factoryMethodMap.put(PacketType.ISP_CRS, PREFIX + "CarReset");
        factoryMethodMap.put(PacketType.ISP_BFN, PREFIX + "ButtonFunction");
        factoryMethodMap.put(PacketType.ISP_AXI, PREFIX + "AutoXInfo");
        factoryMethodMap.put(PacketType.ISP_AXO, PREFIX + "AutoXObject");
        factoryMethodMap.put(PacketType.ISP_BTN, PREFIX + "Button");
        factoryMethodMap.put(PacketType.ISP_BTC, PREFIX + "ButtonClick");
        factoryMethodMap.put(PacketType.ISP_BTT, PREFIX + "ButtonType");
    }

    private final InSimHeaderFactory fHeaderFactory;

    private final InSimPacketFactory fInSimPacketFactory;

    private final OutSimPacketFactory fOutSimPacketFactory;

    private final OutGaugePacketFactory fOutGaugePacketFactory;

    private final StringUtils fStringUtils;

    @Inject
    private ResponsePacketFactoryImpl(final InSimHeaderFactory headerFactory, final InSimPacketFactory inSimPacketFactory, final OutSimPacketFactory outSimPacketFactory, final OutGaugePacketFactory outGaugePacketFactory, final StringUtils stringUtils) {
        fHeaderFactory = headerFactory;
        fInSimPacketFactory = inSimPacketFactory;
        fOutSimPacketFactory = outSimPacketFactory;
        fOutGaugePacketFactory = outGaugePacketFactory;
        fStringUtils = stringUtils;
    }

    /**
	 * Create response packet from buffer data
	 *
	 * @param buffer
	 *            data buffer
	 * @return response packet
	 * @throws IllegalArgumentException
	 * @see net.sf.insim4j.packetfactory.ResponsePacketFactory#createPacket(java.nio.ByteBuffer)
	 */
    @Override
    public ResponsePacket createPacket(final ByteBuffer buffer) {
        buffer.order(InSimHeader.BYTE_ORDER);
        ResponsePacket response = null;
        try {
            Method factoryMethod = null;
            try {
                final InSimHeader header = fHeaderFactory.createHeader(buffer);
                final PacketType type = header.getType();
                final String methodName = factoryMethodMap.get(type);
                factoryMethod = InSimPacketFactory.class.getMethod(methodName, ByteBuffer.class);
            } catch (final IllegalArgumentException iae) {
            }
            buffer.rewind();
            if (buffer.capacity() == OutGaugePacketImpl.SIZE || buffer.capacity() == OutGaugePacketImpl.SIZE - 4) {
                response = fOutGaugePacketFactory.createOutGaugePacket(buffer);
            } else if (buffer.capacity() == OutSimPacketImpl.SIZE || buffer.capacity() == OutSimPacketImpl.SIZE - 4) {
                response = fOutSimPacketFactory.createOutSimPacket(buffer);
            } else if (factoryMethod != null) {
                response = (InSimResponsePacket) factoryMethod.invoke(fInSimPacketFactory, buffer);
            } else {
                log.error("!!!!!!!!!!!!!!!!! UNKNOWN PACKET !!!!!!!!!!!!!!!!! " + fStringUtils.arrayToString(buffer.array()));
            }
        } catch (final SecurityException e) {
            log.error("Error when accessing response packet constructor.", e);
        } catch (final NoSuchMethodException e) {
            log.error("Constructor of response packet class cannot be found.", e);
        } catch (final IllegalArgumentException e) {
            log.error("Illegal argument passed to the response packet class constructor.", e);
        } catch (final IllegalAccessException e) {
            log.error("Response packet class constructor cannot be accessed.", e);
        } catch (final InvocationTargetException e) {
            log.error("Response packet class constructor throws exception.", e);
        }
        return response;
    }
}
