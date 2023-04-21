package l1j.server.server.serverpackets;

import java.util.logging.Logger;
import l1j.server.server.Opcodes;

public class S_HouseMap extends ServerBasePacket {

    private static Logger _log = Logger.getLogger(S_HouseMap.class.getName());

    private static final String S_HOUSEMAP = "[S] S_HouseMap";

    private byte[] _byte = null;

    public S_HouseMap(int objectId, String house_number) {
        buildPacket(objectId, house_number);
    }

    private void buildPacket(int objectId, String house_number) {
        int number = Integer.valueOf(house_number);
        writeC(Opcodes.S_OPCODE_HOUSEMAP);
        writeD(objectId);
        writeD(number);
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
            _byte = getBytes();
        }
        return _byte;
    }

    @Override
    public String getType() {
        return S_HOUSEMAP;
    }
}
