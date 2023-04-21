package net.pesahov.common.utils.decoders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import net.pesahov.common.utils.decoders.Base64Decoder.Base64CodingTable;
import net.pesahov.common.utils.decoders.Base64Decoder.Base64InputStream;
import net.pesahov.common.utils.decoders.Base64Decoder.Base64OutputStream;

/**
 * @author Pesahov Dmitry
 * @since 2.0
 */
public class PackageTest {

    /**
     * For testing.
     * @param argv
     * @throws IOException 
     */
    public static void main(String[] argv) throws IOException {
        int oldTests = 0;
        int test = 0;
        int passed = 0;
        int failed = 0;
        Base64Decoder base64Decoder = new Base64Decoder(true, Base64CodingTable.OrderedURLSafeBase64);
        DecodersChain decoder = new DecodersChain(base64Decoder);
        String[] strings = new String[] { "", "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r\nabcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r\nabcdefghijklmnopqrstuvwxyz\r\n0", "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r\nabcdefghijklmnopqrstuvwxyz\r\n0123456409\t", "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r\nabcdefghijklmnopqrstuvwxyz\r\n0123456789!@#$%^&*()_", "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r\nabcdefghijklmnopqrstuvwxyz\r\n0123456789!@#$%^&*()_+-=/\\|", "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r\nabcdefghijklmnopqrstuvwxyz\r\n0123456789!@#$%^&*()_+-=/\\|\t\0\1\2\3\4\5\6\7" };
        for (test = 0; test < strings.length; ) {
            if (testString(decoder, strings[test++], test)) passed++; else failed++;
        }
        System.out.println("\r\nSammury: Test=" + test + " Passed=" + passed + " Failed=" + failed);
        oldTests = test;
        int maxSize = 1024 * 32;
        int interations = 77;
        while (test < interations + oldTests) {
            test++;
            double rand = Math.random();
            byte[] bytes = new byte[(int) (rand * maxSize)];
            for (int i = 0; i < bytes.length; i++) bytes[i] = (byte) ((rand = Math.random()) * Byte.MAX_VALUE + (1 - rand) * Byte.MIN_VALUE);
            if (testBytes(decoder, bytes, test)) passed++; else failed++;
        }
        System.out.println("\r\nSammury: Test=" + test + " Passed=" + passed + " Failed=" + failed);
        oldTests = test;
        while (test < interations + oldTests) {
            test++;
            double rand = Math.random();
            byte[] bytes = new byte[(int) (rand * maxSize)];
            for (int i = 0; i < bytes.length; i++) bytes[i] = (byte) ((rand = Math.random()) * Byte.MAX_VALUE + (1 - rand) * Byte.MIN_VALUE);
            if (testStreams(decoder, bytes, test)) passed++; else failed++;
        }
        System.out.println("\r\nSammury: Test=" + test + " Passed=" + passed + " Failed=" + failed);
        oldTests = test;
        while (test < interations + oldTests) {
            test++;
            double rand = Math.random();
            byte[] bytes = new byte[(int) (rand * maxSize)];
            for (int i = 0; i < bytes.length; i++) bytes[i] = (byte) ((rand = Math.random()) * Byte.MAX_VALUE * 2);
            if (testStreamsV2(base64Decoder, bytes, test)) passed++; else failed++;
        }
        System.out.println("\r\nSammury: Test=" + test + " Passed=" + passed + " Failed=" + failed);
    }

    /**
     * @param decoder
     * @param string
     * @param test
     * @return
     * @throws CodingException
     */
    protected static boolean testString(Decoder decoder, String string, int test) throws CodingException {
        byte[] originalBytes = string.getBytes();
        System.out.println("Original [length=" + originalBytes.length + "]:\r\n" + string);
        byte[] encodedBytes = decoder.encode(originalBytes);
        String encoded = new String(encodedBytes);
        System.out.println("Encoded [length=" + encodedBytes.length + "]:\r\n" + encoded);
        byte[] decodedBytes = decoder.decode(encoded.getBytes());
        String decoded = new String(decodedBytes);
        System.out.println("Decoded [length=" + decodedBytes.length + "]:\r\n" + decoded);
        boolean passed = string.equals(decoded);
        System.out.println("Test[" + test + "] " + (passed ? "passed................................................................." : "failed<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"));
        return passed;
    }

    /**
     * @param decoder
     * @param bytes
     * @param test
     * @return
     * @throws CodingException
     */
    protected static boolean testBytes(Decoder decoder, byte[] original, int test) throws CodingException {
        System.out.println("Original [length=" + original.length + "]");
        byte[] encoded = decoder.encode(original);
        System.out.println("Encoded [length=" + encoded.length + "]:\r\n" + new String(encoded));
        byte[] decoded = decoder.decode(encoded);
        System.out.println("Decoded [length=" + decoded.length + "]");
        boolean passed = Arrays.equals(original, decoded);
        System.out.println("Test[" + test + "] " + (passed ? "passed................................................................." : "failed<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"));
        return passed;
    }

    /**
     * @param decoder
     * @param bytes
     * @param test
     * @return
     * @throws CodingException
     */
    protected static boolean testStreams(Decoder decoder, byte[] original, int test) throws CodingException, IOException {
        System.out.println("Original [length=" + original.length + "]");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        decoder.encode(new ByteArrayInputStream(original), output);
        byte[] encoded = output.toByteArray();
        System.out.println("Encoded [length=" + encoded.length + "]:\r\n" + new String(encoded));
        output.reset();
        decoder.decode(new ByteArrayInputStream(encoded), output);
        byte[] decoded = output.toByteArray();
        System.out.println("Decoded [length=" + decoded.length + "]");
        boolean passed = Arrays.equals(original, decoded);
        System.out.println("Test[" + test + "] " + (passed ? "passed................................................................." : "failed<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"));
        return passed;
    }

    /**
     * @param decoder
     * @param bytes
     * @param test
     * @return
     * @throws CodingException
     */
    protected static boolean testStreamsV2(Base64Decoder decoder, byte[] original, int test) throws CodingException, IOException {
        System.out.println("Original [length=" + original.length + "]");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream b64Output = new Base64OutputStream(output, decoder);
        b64Output.write(original);
        b64Output.close();
        byte[] encoded = output.toByteArray();
        System.out.println("Encoded [length=" + encoded.length + "]:\r\n" + new String(encoded));
        output = new ByteArrayOutputStream();
        Base64InputStream b64Input = new Base64InputStream(new ByteArrayInputStream(encoded), decoder);
        int b;
        while ((b = b64Input.read()) != -1) output.write(b);
        byte[] decoded = output.toByteArray();
        System.out.println("Decoded [length=" + decoded.length + "]");
        boolean passed = Arrays.equals(original, decoded);
        System.out.println("Test[" + test + "] " + (passed ? "passed................................................................." : "failed<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"));
        return passed;
    }
}
