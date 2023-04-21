package it.unibo.deis.interaction.ws.stil.utils;

import java.security.SecureRandom;

public class UUIDFactory {

    private SecureRandom m_securerandom;

    private UUIDFactory() {
        m_securerandom = new SecureRandom();
    }

    private static UUIDFactory THIS = null;

    public static synchronized UUIDFactory getInstance() {
        if (THIS == null) THIS = new UUIDFactory();
        return (THIS);
    }

    public synchronized UUID newUUID() {
        byte[] l_buffer = null;
        long l_msb = 0;
        long l_lsb = 0;
        l_buffer = new byte[16];
        m_securerandom.nextBytes(l_buffer);
        l_buffer[6] &= 0x0f;
        l_buffer[6] |= 0x40;
        l_buffer[8] &= 0x3f;
        l_buffer[8] |= 0x80;
        l_buffer[10] |= 0x80;
        for (int l_i = 0; l_i < 8; l_i++) {
            l_msb = (l_msb << 8) | (l_buffer[l_i + 0] & 0xff);
            l_lsb = (l_lsb << 8) | (l_buffer[l_i + 8] & 0xff);
        }
        return (new UUID(l_msb, l_lsb));
    }
}
