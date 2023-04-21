package pt.igeo.snig.mig.editor.ui.migFrame.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Computes MD5 value from either a string or a file
 * @author Jos� Pedro Dias
 * @since $ version $
 */
public class Md5Util {

    /** holds a MessageDigest once used the first time */
    private static MessageDigest algorithm = null;

    /**
	 * Computes MD5 hex String from byte[] 
	 * @param input
	 * @return String version of the hexadecimal value of a byte array
	 * @throws NoSuchAlgorithmException happens when MD5 is not supported by the JRE
	 */
    public static String getMd5(byte[] input) throws NoSuchAlgorithmException {
        if (algorithm == null) {
            algorithm = MessageDigest.getInstance("MD5");
        }
        algorithm.reset();
        algorithm.update(input);
        byte messageDigest[] = algorithm.digest();
        BigInteger number = new BigInteger(1, messageDigest);
        String md5val = number.toString(16);
        int prefixZeros = 32 - md5val.length();
        for (int i = 0; i < prefixZeros; ++i) {
            md5val = "0" + md5val;
        }
        return md5val.toUpperCase();
    }

    /**
	 * Computes MD5 hex String from a given String
	 * @param input
	 * @return String version of the hexadecimal value of a String
	 * @throws NoSuchAlgorithmException
	 */
    public static String getMd5(String input) throws NoSuchAlgorithmException {
        return getMd5(input.getBytes());
    }

    /**
	 * Computes MD5 hex String from a given file name
	 * @param fileName file name to compute MD5 from
	 * @return String version of the hexadecimal value of a file
	 * @throws FileNotFoundException 
	 * @throws NoSuchAlgorithmException
	 */
    public static String getMd5FromFile(String fileName) throws FileNotFoundException, NoSuchAlgorithmException {
        try {
            InputStream in = new FileInputStream(new File(fileName));
            byte[] data = new byte[in.available()];
            in.read(data);
            return getMd5(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    /**
	 * If called with 1 argument prints its MD5 digest
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                System.out.println(Md5Util.getMd5FromFile(args[0]));
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }
}
