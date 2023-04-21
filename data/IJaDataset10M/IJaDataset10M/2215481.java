package it.unibo.deis.uniboEnv_p2p.application.utils;

import java.io.*;
import org.apache.log4j.Logger;

public class Redirecting {

    protected static Logger logger = Logger.getLogger(Redirecting.class);

    /**
	   * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
    public static void trasferisci_a_byte_file_binario(DataInputStream src, DataOutputStream dest) throws IOException {
        int buffer = 0;
        int read = 0;
        try {
            while ((buffer = src.read()) >= 0) {
                dest.write(buffer);
                read++;
            }
            dest.flush();
        } catch (EOFException e) {
            System.out.println("Problemi, i seguenti: ");
            e.printStackTrace();
        }
    }

    /**
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
    public static void trasferisci_a_byte_file_binario_withError(DataInputStream src, DataOutputStream dest) throws IOException {
        int buffer = 0;
        int read = 0;
        try {
            while ((buffer = src.read()) >= 0) {
                dest.write(buffer);
                read++;
            }
            dest.flush();
        } catch (EOFException e) {
            System.out.println("Problemi, i seguenti: ");
            e.printStackTrace();
        }
    }

    /**
	 * Leggo esattamente lenght byte di input
	 * @param src
	 * @param dest
	 * @param flenght
	 * @throws IOException
	 */
    public static void trasferisci_a_byte_file_binario(DataInputStream src, DataOutputStream dest, long flength) throws IOException {
        int buffer = 0;
        int offset = 0;
        try {
            int read = 0;
            while ((buffer = src.read()) >= 0) {
                dest.write(buffer);
                read++;
                if (read == flength) break;
            }
            dest.flush();
        } catch (EOFException e) {
            System.out.println("Problemi, i seguenti: ");
            e.printStackTrace();
        }
    }

    /**
	   * Trasferisci con codifica Base64
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
    public static void trasferisci_File2Stream(FileInputStream src, DataOutputStream dest) throws IOException {
        Base64.InputStream b64is = null;
        int buffer = 0;
        b64is = new Base64.InputStream(src, Base64.ENCODE);
        try {
            while ((buffer = b64is.read()) >= 0) {
                dest.write(buffer);
            }
            dest.flush();
        } catch (EOFException e) {
            System.out.println("Problemi, i seguenti: ");
            e.printStackTrace();
        }
    }

    public static void trasferisci_Stream2File(DataInputStream src, FileOutputStream dest) throws IOException {
        Base64.OutputStream b64os = null;
        int buffer = 0;
        b64os = new Base64.OutputStream(dest, Base64.DECODE);
        try {
            while ((buffer = src.read()) >= 0) {
                b64os.write(buffer);
            }
            b64os.flush();
        } catch (EOFException e) {
            System.out.println("Problemi, i seguenti: ");
            e.printStackTrace();
        }
    }

    public static void trasferisciOggetto(OutputStream outS, Object inObj) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(outS);
            out.writeObject(inObj);
            out.flush();
        } catch (IOException e) {
            logger.info("Redirecting > Error on Object Transfert");
            e.printStackTrace();
        }
    }

    public static Object leggiOggetto(InputStream srcS) {
        ObjectInputStream in = null;
        Object outObj = null;
        try {
            in = new ObjectInputStream(srcS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outObj = in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outObj;
    }

    public static void StringPusching(String inputString, DataOutputStream out) {
        char[] cBuf = null;
        int index = 0;
        int lenght = 40;
        StringReader stReader = new StringReader(inputString);
        try {
            while ((stReader.read(cBuf, index, lenght)) >= 0) {
                index += lenght;
                out.writeUTF(cBuf.toString());
            }
            out.flush();
        } catch (EOFException e) {
            System.out.println("Problemi, i seguenti: ");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
