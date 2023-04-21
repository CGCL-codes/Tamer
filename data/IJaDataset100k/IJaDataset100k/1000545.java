package fi.vtt.probeframework.javaclient.protocol;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import fi.vtt.probeframework.javaclient.api.probe.*;
import fi.vtt.probeframework.javaclient.protocol.messages.Precision;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.File;

/**
 * @author Teemu Kanstr�n
 */
public class SeveralTestsInOneFile extends BaseTest {

    private ByteArrayOutputStream bos = null;

    private byte index = 1;

    @Before
    public void setup() throws Exception {
        IO.reset();
        bos = new ByteArrayOutputStream();
        IO.file = bos;
        IO.tcp = new FileOutputStream("pf-3test.data");
        BaseProbe.reset();
        PF.reset();
        Configuration.testMode = true;
    }

    @After
    public void teardown() {
        Configuration.testMode = false;
    }

    @Test
    public void oneTest() {
        PFTest test = new PFTest("test-proj", "1", "SUT", Precision.SECOND, "test-name", "test-suite");
        PF.startTest(test);
        IntProbe ip = new IntProbe(test, (byte) 1, "myintprobe");
        ip.data(0x11);
        ByteProbe bp = new ByteProbe(test, (byte) 2, "mybyteprobe");
        bp.data(new byte[] { 1, 2, 3 });
        ip.data(0x22);
        BooleanProbe boolp = new BooleanProbe(test, (byte) 3, "myboolprobe");
        boolp.data(true);
        ip.data(0x33);
        byte[] msg = bos.toByteArray();
        clearTime(msg, 45);
        clearTime(msg, 78);
        clearTime(msg, 106);
        clearTime(msg, 122);
        clearTime(msg, 146);
        clearTime(msg, 162);
        byte[] expected = expectedForTestOne();
        assertArrayEquals(expected, msg);
    }

    private byte[] expectedForTestOne() {
        byte[] expected = new byte[] { (byte) 0xff, (byte) 0xff, 0x2, 0x9, 0x74, 0x65, 0x73, 0x74, 0x2d, 0x70, 0x72, 0x6f, 0x6a, 0x1, 0x31, 0x9, 0x74, 0x65, 0x73, 0x74, 0x2d, 0x6e, 0x61, 0x6d, 0x65, 0x3, 0x53, 0x55, 0x54, 0xa, 0x74, 0x65, 0x73, 0x74, 0x2d, 0x73, 0x75, 0x69, 0x74, 0x65, 0x3, 0, 0, 0, 0, 0, 0, 1, 0x10, 0x1, 0xa, 0x6d, 0x79, 0x69, 0x6e, 0x74, 0x70, 0x72, 0x6f, 0x62, 0x65, 0x6, 0x11, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0x11, 0, 0, 0, 0, 0x10, 0x2, 0xb, 0x6d, 0x79, 0x62, 0x79, 0x74, 0x65, 0x70, 0x72, 0x6f, 0x62, 0x65, 0x9, 0x11, 0, 0x2, 0x2, 0, 0x3, 1, 2, 3, 0, 0, 0, 0, 0x11, 0, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0x22, 0, 0, 0, 0, 0x10, 0x3, 0xb, 0x6d, 0x79, 0x62, 0x6f, 0x6f, 0x6c, 0x70, 0x72, 0x6f, 0x62, 0x65, 0x1, 0x11, 0, 4, 0x3, 1, 0, 0, 0, 0, 0x11, 0, 5, 1, 0, 0, 0, 0, 0, 0, 0, 0x33, 0, 0, 0, 0 };
        return expected;
    }

    @Test
    public void twoTests() {
        oneTest();
        PFTest test = new PFTest("test-proj", "1", "SUT", Precision.SECOND, "test-name", "test-suite");
        PF.startTest(test);
        IntProbe ip = new IntProbe(test, (byte) 1, "myintprobe");
        BooleanProbe bp = new BooleanProbe(test, (byte) 3, "myboolprobe");
        bp.data(true);
        ip.data(1);
        byte[] msg = bos.toByteArray();
        byte[] expected = expectedForTestOne();
        expected = createStartMsg(expected, (byte) test.getTestId());
        clearStartTime(msg, expected);
        expected = addTestChange(expected, test.getTestId());
        clearTime(msg, 45);
        clearTime(msg, 78);
        clearTime(msg, 106);
        clearTime(msg, 122);
        clearTime(msg, 146);
        clearTime(msg, 162);
        expected = addMyIntProbeType(expected);
        expected = addMyBoolProbeType(expected);
        expected = addMyBoolProbeData(expected, true, (byte) 6);
        clearTime(msg, expected.length);
        expected = addMyIntProbeData(expected, (byte) 1, (byte) 7);
        clearTime(msg, expected.length);
        assertArrayEquals(expected, msg);
    }

    private void clearStartTime(byte[] msg, byte[] expected) {
        System.out.println("index:" + expected.length);
        msg[expected.length - 4] = 0;
        msg[expected.length - 5] = 0;
        msg[expected.length - 6] = 0;
        msg[expected.length - 7] = 0;
    }

    @Test
    public void threeTests() {
        oneTest();
        PFTest test1 = new PFTest("test-proj", "1", "SUT", Precision.SECOND, "test-name", "test-suite");
        PF.startTest(test1);
        PFTest test2 = new PFTest("test-proj", "1", "SUT", Precision.SECOND, "test-name", "test-suite");
        PF.startTest(test2);
        IntProbe ip = new IntProbe(test1, (byte) 1, "myintprobe");
        BooleanProbe bp = new BooleanProbe(test1, (byte) 3, "myboolprobe");
        bp.data(true);
        ip.data(1);
        BooleanProbe bp2 = new BooleanProbe(test2, (byte) 3, "myboolprobe");
        bp2.data(false);
        ip.data(11);
        byte[] msg = bos.toByteArray();
        byte[] expected = expectedForTestOne();
        expected = createStartMsg(expected, (byte) test1.getTestId());
        clearStartTime(msg, expected);
        expected = createStartMsg(expected, (byte) test2.getTestId());
        clearStartTime(msg, expected);
        clearTime(msg, 45);
        clearTime(msg, 78);
        clearTime(msg, 106);
        clearTime(msg, 122);
        clearTime(msg, 146);
        clearTime(msg, 162);
        expected = addTestChange(expected, test1.getTestId());
        expected = addMyIntProbeType(expected);
        expected = addMyBoolProbeType(expected);
        expected = addMyBoolProbeData(expected, true, (byte) 6);
        clearTime(msg, expected.length);
        expected = addMyIntProbeData(expected, (byte) 1, (byte) 7);
        clearTime(msg, expected.length);
        expected = addTestChange(expected, test2.getTestId());
        expected = addMyBoolProbeType(expected);
        expected = addMyBoolProbeData(expected, false, (byte) 8);
        clearTime(msg, expected.length);
        expected = addTestChange(expected, test1.getTestId());
        expected = addMyIntProbeData(expected, (byte) 11, (byte) 9);
        clearTime(msg, expected.length);
        assertArrayEquals(expected, msg);
    }

    private byte[] addTestChange(byte[] expected, int testId) {
        byte[] add = new byte[] { 0, 0, 0, (byte) testId };
        return appendBytesToMsg(expected, add);
    }

    private byte[] createStartMsg(byte[] msg, byte testId) {
        byte[] add = new byte[] { (byte) 0xff, (byte) 0xff, 0x2, 0x9, 0x74, 0x65, 0x73, 0x74, 0x2d, 0x70, 0x72, 0x6f, 0x6a, 0x1, 0x31, 0x9, 0x74, 0x65, 0x73, 0x74, 0x2d, 0x6e, 0x61, 0x6d, 0x65, 0x3, 0x53, 0x55, 0x54, 0xa, 0x74, 0x65, 0x73, 0x74, 0x2d, 0x73, 0x75, 0x69, 0x74, 0x65, 0x3, 0, 0, 0, 0, 0, 0, testId };
        return appendBytesToMsg(msg, add);
    }

    private byte[] addMyIntProbeType(byte[] msg) {
        byte[] add = new byte[] { 0x10, 0x1, 0xa, 0x6d, 0x79, 0x69, 0x6e, 0x74, 0x70, 0x72, 0x6f, 0x62, 0x65, 0x6 };
        return appendBytesToMsg(msg, add);
    }

    private byte[] appendBytesToMsg(byte[] msg, byte[] add) {
        byte[] newMsg = new byte[msg.length + add.length];
        System.arraycopy(msg, 0, newMsg, 0, msg.length);
        System.arraycopy(add, 0, newMsg, msg.length, add.length);
        return newMsg;
    }

    private byte[] addMyIntProbeData(byte[] msg, byte value, byte index) {
        byte[] add = new byte[] { 0x11, 0, index, 1, 0, 0, 0, 0, 0, 0, 0, value, 0, 0, 0, 0 };
        return appendBytesToMsg(msg, add);
    }

    private void clearTime(byte[] msg, int endIndex) {
        msg[endIndex - 1] = 0;
        msg[endIndex - 2] = 0;
        msg[endIndex - 3] = 0;
        msg[endIndex - 4] = 0;
    }

    private byte[] addMyByteProbeType(byte[] msg) {
        byte[] add = new byte[] { 0x10, 0x2, 0xb, 0x6d, 0x79, 0x62, 0x79, 0x74, 0x65, 0x70, 0x72, 0x6f, 0x62, 0x65, 0x9 };
        return appendBytesToMsg(msg, add);
    }

    private byte[] addMyByteProbeData(byte[] msg, byte v1, byte v2, byte v3) {
        byte[] add = new byte[] { 0x11, 0, index++, 0x2, 0, 0x3, v1, v2, v3, 0, 0, 0, 0 };
        return appendBytesToMsg(msg, add);
    }

    private byte[] addMyBoolProbeType(byte[] msg) {
        byte[] add = new byte[] { 0x10, 0x3, 0xb, 0x6d, 0x79, 0x62, 0x6f, 0x6f, 0x6c, 0x70, 0x72, 0x6f, 0x62, 0x65, 0x1 };
        return appendBytesToMsg(msg, add);
    }

    private byte[] addMyBoolProbeData(byte[] msg, boolean value, byte index) {
        byte v = 0;
        if (value) {
            v = 1;
        }
        byte[] add = new byte[] { 0x11, 0, index, 0x3, v, 0, 0, 0, 0 };
        return appendBytesToMsg(msg, add);
    }
}
