package org.tranche.server.logs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Iterator;
import org.tranche.security.SecurityUtil;
import org.tranche.security.Signature;
import org.tranche.hash.BigHash;
import org.tranche.time.TimeUtil;
import org.tranche.util.DevUtil;
import org.tranche.util.IOUtil;
import org.tranche.commons.RandomUtil;
import org.tranche.util.TempFileUtil;
import org.tranche.util.TrancheTestCase;

/**
 *
 * @author Bryan E. Smith - bryanesmith@gmail.com
 */
public class LogEntryTest extends TrancheTestCase {

    /**
     * Going to write a file. Then we'll intentionally destroy an entry
     * and make sure they other entries are fine!
     */
    public void testChecksumNegative() throws Exception {
        String ip = "127.127.127.127";
        LogEntries entries = new LogEntries();
        entries.add(LogEntry.logGetData(TimeUtil.getTrancheTimestamp(), ip, DevUtil.getRandomBigHash(123)));
        entries.add(LogEntry.logGetMetaData(TimeUtil.getTrancheTimestamp(), ip, DevUtil.getRandomBigHash(456)));
        entries.add(LogEntry.logGetConfiguration(TimeUtil.getTrancheTimestamp(), ip));
        entries.add(LogEntry.logGetNonce(TimeUtil.getTrancheTimestamp(), ip));
        entries.add(LogEntry.logSetConfiguration(TimeUtil.getTrancheTimestamp(), ip, DevUtil.getBogusSignature()));
        entries.add(LogEntry.logSetData(TimeUtil.getTrancheTimestamp(), ip, DevUtil.getRandomBigHash(789), DevUtil.getBogusSignature()));
        entries.add(LogEntry.logSetMetaData(TimeUtil.getTrancheTimestamp(), ip, DevUtil.getRandomBigHash(012), DevUtil.getBogusSignature()));
        assertEquals("Expecting 7 entries.", 7, entries.size());
        LogWriter writer = null;
        LogReader reader = null;
        File logFile = TempFileUtil.createTemporaryFile(".log-entry-test.log");
        File badLogFile = null;
        try {
            writer = new LogWriter(logFile);
            Iterator<LogEntry> it = entries.iterator();
            while (it.hasNext()) {
                writer.writeEntry(it.next());
            }
            writer.close();
            int count = 0;
            reader = new LogReader(logFile);
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
            reader.close();
            assertEquals("Expecting 7 entries, found " + count, 7, count);
            badLogFile = TempFileUtil.createTemporaryFile("log-entry-test.bad.log");
            byte[] bytes = IOUtil.getBytes(logFile);
            int positionForBadByte = 10;
            byte[] badBytes = new byte[bytes.length];
            System.arraycopy(bytes, 0, badBytes, 0, positionForBadByte);
            byte badByte = 0;
            while (badByte == 0 && badByte == bytes[positionForBadByte]) {
                badByte = (byte) (RandomUtil.getInt(256) - 128);
            }
            badBytes[positionForBadByte] = badByte;
            System.arraycopy(bytes, positionForBadByte + 1, badBytes, positionForBadByte + 1, bytes.length - positionForBadByte - 1);
            assertEquals("Expecting same num bytes", bytes.length, badBytes.length);
            IOUtil.setBytes(badBytes, badLogFile);
            reader = new LogReader(badLogFile);
            count = 0;
            while (reader.hasNext()) {
                LogEntry trash = reader.next();
                count++;
            }
            assertEquals("Expecting 6 entries, found " + count, 6, count);
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            IOUtil.safeDelete(logFile);
            if (badLogFile != null) {
                IOUtil.safeDelete(badLogFile);
            }
        }
    }

    /**
     * This signature is good for nothing.
     */
    private Signature getFauxSignature() throws Exception {
        DevUtil.getDevUser();
        String algorithm = SecurityUtil.getSignatureAlgorithm(DevUtil.getDevPrivateKey());
        byte[] bytes = new byte[256];
        RandomUtil.getBytes(bytes);
        byte[] sig = SecurityUtil.sign(new ByteArrayInputStream(bytes), DevUtil.getDevPrivateKey(), algorithm);
        return new Signature(sig, algorithm, DevUtil.getDevAuthority());
    }

    private int getSmallProjectSize() {
        return RandomUtil.getInt(1024) + 256;
    }

    private BigHash createBigHashForProjectOfSize(int size) {
        byte[] data = new byte[size];
        RandomUtil.getBytes(data);
        return new BigHash(data);
    }
}
