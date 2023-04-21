package cc.w3d.jawos.jinn.xuid.xuidGenerator.engines.xeCompactUidGenerator;

import java.util.Date;
import java.util.Random;
import cc.w3d.jawos.jinn.xuid.xuidGenerator.core.structure.Uid;
import cc.w3d.jawos.jinn.xuid.xuidGenerator.engine.IXUidGeneratorEngine;
import cc.w3d.jawos.jinn.xuid.xuidGenerator.engines.xeCompactUidGenerator.structure.UidImpl;

public class XECompactUidGenerator implements IXUidGeneratorEngine {

    protected static final int TO_BYTE_ARRAY_DEFAULT_SIZE = (Long.SIZE / Byte.SIZE);

    protected static final int TO_STRING_MAX_SIZE = 12;

    private static final long CYCLIC_TIME_MASK = 13l * 365l * 24l * 60l * 60l * 10l;

    private static long genVal() {
        int iTime = (int) (((new Date().getTime() / 100l) % CYCLIC_TIME_MASK) + Integer.MIN_VALUE);
        int iRand = new Random().nextInt();
        long lRand = ((long) iRand) << 32l;
        long lTime = ((long) iTime) & 0x00000000FFFFFFFFl;
        return lTime | lRand;
    }

    public IXUidGeneratorEngine getClone() {
        return new XECompactUidGenerator();
    }

    public Uid generate() {
        UidImpl r = new UidImpl();
        r.setData(genVal());
        return r;
    }

    public Uid recover(long id) {
        UidImpl r = new UidImpl();
        r.setData(id);
        return r;
    }

    public Uid recover(String id) {
        return new UidImpl(id);
    }

    public Uid recover(byte[] id) {
        return new UidImpl(id);
    }

    public int getToByteArrayDefaultSize() {
        return TO_BYTE_ARRAY_DEFAULT_SIZE;
    }

    public int getToStringMaxSize() {
        return TO_STRING_MAX_SIZE;
    }

    public Object getEngineConfigurationManager() {
        return null;
    }

    public void comitTransaction(Object transaction) {
    }

    public Object initTransaction() {
        return null;
    }

    public void rollbackTransaction(Object transaction) {
    }
}
