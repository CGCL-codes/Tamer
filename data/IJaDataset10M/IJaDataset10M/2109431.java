package org.cerny.fun.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Fail messages provider
 * 
 * @author cerny
 * @version 6|9|08
 * 
 */
public class FailMessage implements IConstants {

    private static final String FAIL_MSGS_FILE = "/failMsgs.txt";

    private static final String WINDOWS_DELIMITER = "\r\n";

    private static final String EOL = "\r\n";

    private static final String LINUX_DELIMITER = "\n";

    /**
     * @return String message
     */
    public static String getMsgFileNotFound() {
        Iterator<String> iter = FailMessage.getData(FAIL_MSGS_FILE);
        iter.next();
        iter.next();
        String data = iter.next();
        return data + (EOL + EOL);
    }

    /**
     * @return String message
     */
    public static String getMsgHeaderParse() {
        Iterator<String> iter = FailMessage.getData(FAIL_MSGS_FILE);
        iter.next();
        String data = iter.next();
        return data + (EOL + EOL);
    }

    /**
     * @return String message
     */
    public static String getMsgHostNotFound() {
        Iterator<String> iter = FailMessage.getData(FAIL_MSGS_FILE);
        String data = iter.next();
        return data + (EOL + EOL);
    }

    /**
     * return Message iterator
     * (public for external testing)
     */
    public static Iterator<String> getData(String file) {
        InputStream is = FailMessage.class.getResourceAsStream(file);
        Scanner scanner = new Scanner(is).useDelimiter("\\s*###\\s*");
        List<String> dataList = new ArrayList<String>();
        while (scanner.hasNext()) {
            dataList.add(scanner.next().replaceAll(LINUX_DELIMITER, WINDOWS_DELIMITER));
        }
        return dataList.iterator();
    }

    /**
     * send message to the stream
     * @param clientOutStream
     */
    public static void sendHeaderParse(OutputStream clientOutStream) {
        if (clientOutStream != null) {
            try {
                clientOutStream.write(FailMessage.getMsgHeaderParse().getBytes(ENCODING), 0, FailMessage.getMsgHeaderParse().getBytes(ENCODING).length);
            } catch (Exception e) {
                CZLogger.logger.info("sendHeaderParse Error: stream closed");
            }
        }
    }

    /**
     * send message to the stream
     * @param clientOutStream
     */
    public static String sendHostNotFound(OutputStream clientOutStream) {
        if (clientOutStream != null) {
            try {
                clientOutStream.write(FailMessage.getMsgHostNotFound().getBytes(ENCODING), 0, FailMessage.getMsgHostNotFound().getBytes(ENCODING).length);
                return FailMessage.getMsgHostNotFound();
            } catch (Exception e) {
                CZLogger.logger.info("sendHostNotFound Error: stream closed");
            }
        }
        return null;
    }

    /**
     * send message to the stream
     * @param clientOutStream
     */
    public static String sendFileNotFound(OutputStream clientOutStream) {
        if (clientOutStream != null) {
            try {
                clientOutStream.write(FailMessage.getMsgFileNotFound().getBytes(ENCODING), 0, FailMessage.getMsgHostNotFound().getBytes(ENCODING).length);
                return FailMessage.getMsgFileNotFound();
            } catch (Exception e) {
                CZLogger.logger.info("sendHostNotFound Error: stream closed");
            }
        }
        return null;
    }
}
