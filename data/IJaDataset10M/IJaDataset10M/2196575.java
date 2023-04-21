package net.jomper.cm.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class SerialisedTools {

    static final int UnitSize = 1024 * 900;

    public static byte[] group(List<SerialisedUnit> units, int length) {
        ByteBuffer t = ByteBuffer.allocate(length);
        System.out.println("buildup:length:>" + t.limit() + ":" + length);
        System.out.println("buildup:t:>" + t.toString());
        for (SerialisedUnit unit : units) {
            t.put(unit.getBytes());
            System.out.println("buildup:unit:>" + t.toString());
        }
        return t.array();
    }

    public static List<SerialisedUnit> split(byte[] bytes) {
        List<SerialisedUnit> units = new ArrayList<SerialisedUnit>();
        ByteBuffer o = ByteBuffer.wrap(bytes).asReadOnlyBuffer();
        System.out.println("limit:>" + o.limit());
        int length = o.limit();
        int blockCount = Math.round((float) length / SerialisedTools.UnitSize);
        if (blockCount == 0) blockCount = 1;
        int t = 0;
        System.out.println("blockCount:>" + blockCount);
        for (int i = 0; i < blockCount; i++) {
            System.out.println("o:>" + o.toString());
            ByteBuffer buffer;
            if (o.remaining() > SerialisedTools.UnitSize) {
                buffer = ByteBuffer.allocate(SerialisedTools.UnitSize);
            } else {
                buffer = ByteBuffer.allocate(o.remaining());
            }
            o.get(buffer.array());
            System.out.println("unitBuffer:>" + buffer.limit());
            System.out.println(buffer.toString());
            t += buffer.remaining();
            units.add(new SerialisedUnit(i, buffer.array()));
        }
        System.out.println("all:>" + t);
        return units;
    }
}
