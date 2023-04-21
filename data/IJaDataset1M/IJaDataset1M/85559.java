package com.triplea.rolap.apitest;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.Vector;
import java.util.Random;
import java.security.MessageDigest;

public class MainTest {

    private static Logger log = Logger.getLogger(MainTest.class.getName());

    private static Properties props = new Properties();

    private static NumberFormat nfTime = NumberFormat.getNumberInstance();

    public static String getProp(String key) {
        return props.getProperty(key);
    }

    public static void main(String args[]) {
        PropertyConfigurator.configure("testunit/TestUnit.log4j");
        nfTime.setMaximumFractionDigits(3);
        try {
            File file = new File("testunit/TestUnit.properties");
            props.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        log.info("Test Unit : started.");
        try {
            BaseTest test;
            String sid = getSID();
            test = new ServerDatabasesHandlerTest();
            makeTest(test, sid, 1);
            test = new ServerInfoHandlerTest();
            makeTest(test, sid, 1);
            test = new ServerLoadHandlerTest();
            makeTest(test, sid, 1);
            test = new ServerLoginHandlerTest();
            makeTest(test, sid, 1);
            String sidForLogout = getSID();
            test = new ServerLogoutHandlerTest();
            makeTest(test, sidForLogout, 1);
            test = new ServerSaveHandlerTest();
            makeTest(test, sid, 1);
            test = new DatabaseCubesHandlerTest();
            makeTest(test, sid, 1);
            test = new DatabaseCreateHandlerTest();
            makeTest(test, sid, 1);
            test = new DatabaseDestroyHandlerTest();
            makeTest(test, sid, 1);
            test = new DatabaseDimensionsHandlerTest();
            makeTest(test, sid, 1);
            test = new DatabaseInfoHandlerTest();
            makeTest(test, sid, 1);
            test = new DatabaseLoadHandlerTest();
            makeTest(test, sid, 1);
            test = new DatabaseRenameHandlerTest();
            makeTest(test, sid, 1);
            test = new DatabaseSaveHandlerTest();
            makeTest(test, sid, 1);
            test = new DatabaseUnloadHandlerTest();
            makeTest(test, sid, 1);
            test = new DimensionClearHandlerTest();
            makeTest(test, sid, 1);
            test = new DimensionCreateHandlerTest();
            makeTest(test, sid, 1);
            test = new DimensionCubesHandlerTest();
            makeTest(test, sid, 1);
            test = new DimensionDestroyHandlerTest();
            makeTest(test, sid, 1);
            test = new DimensionElementHandlerTest();
            makeTest(test, sid, 1);
            test = new DimensionElementsHandlerTest();
            makeTest(test, sid, 1);
            test = new DimensionInfoHandlerTest();
            makeTest(test, sid, 1);
            test = new DimensionRenameHandlerTest();
            makeTest(test, sid, 1);
            test = new ElementAppendHandlerTest();
            makeTest(test, sid, 1);
            test = new ElementCreateHandlerTest();
            makeTest(test, sid, 1);
            test = new ElementDestroyHandlerTest();
            makeTest(test, sid, 1);
            test = new ElementInfoHandlerTest();
            makeTest(test, sid, 1);
            test = new ElementMoveHandlerTest();
            makeTest(test, sid, 1);
            test = new ElementRenameHandlerTest();
            makeTest(test, sid, 1);
            test = new ElementReplaceHandlerTest();
            makeTest(test, sid, 1);
            test = new CubeClearHandlerTest();
            makeTest(test, sid, 1);
            test = new CubeCreateHandlerTest();
            makeTest(test, sid, 1);
            test = new CubeDestroyHandlerTest();
            makeTest(test, sid, 1);
            test = new CubeInfoHandlerTest();
            makeTest(test, sid, 1);
            test = new CubeLoadHandlerTest();
            makeTest(test, sid, 1);
            test = new CubeRenameHandlerTest();
            makeTest(test, sid, 1);
            test = new CubeSaveHandlerTest();
            makeTest(test, sid, 1);
            test = new CubeUnloadHandlerTest();
            makeTest(test, sid, 1);
            test = new CellAreaHandlerTest();
            makeTest(test, sid, 1);
            test = new CellCopyHandlerTest();
            makeTest(test, sid, 1);
            test = new CellExportHandlerTest();
            makeTest(test, sid, 1);
            test = new CellReplaceHandlerTest();
            makeTest(test, sid, 1);
            test = new CellReplaceBulkHandlerTest();
            makeTest(test, sid, 1);
            test = new CellValueHandlerTest();
            makeTest(test, sid, 1);
            test = new CellValuesHandlerTest();
            makeTest(test, sid, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void makeTest(BaseTest test, String sid, int iterationsCount) {
        log.info("Test Unit : " + test.getClass().getSimpleName());
        boolean passed = true;
        long startTime = System.currentTimeMillis();
        try {
            for (int i = 0; i < iterationsCount; i++) {
                passed = test.makeTest(sid);
                log.info(".");
            }
        } catch (Exception e) {
            passed = false;
            log.info("----------Failed !" + e.getMessage());
        }
        long finishTime = System.currentTimeMillis();
        log.info((passed ? "Passed" : "----------Failed !"));
        log.info("Time spent: " + nfTime.format(((double) (finishTime - startTime)) / 1000.0D) + " sec");
        if (iterationsCount > 1) {
            log.info("\t\t (" + nfTime.format(((double) (finishTime - startTime)) / 1000.0D / (double) iterationsCount) + " sec/iteration)");
        }
    }

    public static String getSID() throws IOException {
        String query = "/server/login?user=" + MainTest.getProp("user") + "&extern_password=" + MainTest.getProp("extern_password") + "&password=" + getMD5(MainTest.getProp("extern_password"));
        Vector<String> res = MainTest.sendRequest(query);
        String vals[] = MainTest.getList(res.get(0), ';');
        return vals[0];
    }

    private static String getMD5(String phrase) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(phrase.getBytes());
            return asHexString(md.digest());
        } catch (Exception e) {
        }
        return "";
    }

    private static final String asHexString(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            result.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
        }
        return result.toString();
    }

    public static Vector<String> sendRequest(String request) throws IOException {
        URL url = new URL(props.getProperty("url") + request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "text/xml");
        int code = connection.getResponseCode();
        if (code == 400) {
            Vector<String> responce = readResponce(connection.getErrorStream());
            dumpResponce(responce);
            throw new IOException(responce != null ? responce.get(0) : "");
        }
        return readResponce(connection.getInputStream());
    }

    private static void dumpResponce(Vector<String> responce) {
        for (String line : responce) log.error("\t" + line);
    }

    private static Vector<String> readResponce(InputStream input) throws IOException {
        Vector<String> responce = new Vector<String>();
        BufferedReader rd = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = rd.readLine()) != null) {
            responce.add(line);
        }
        rd.close();
        return responce;
    }

    public static String[] getList(String value, char div) {
        Vector<String> vector = new Vector<String>();
        StringBuffer buf = new StringBuffer();
        if (value != null) {
            for (int i = 0; i < value.length(); i++) {
                if (value.substring(i, i + 1).equals(Character.toString(div))) {
                    vector.add(buf.toString());
                    buf = new StringBuffer();
                } else {
                    buf.append(value.charAt(i));
                }
            }
        }
        if (buf.length() != 0) {
            vector.add(buf.toString());
        }
        String[] list = new String[vector.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = vector.get(i);
        }
        return list;
    }

    public static String generateRandomName(int length) {
        StringBuffer alphabet = new StringBuffer();
        alphabet.append("abcdefghijklmnopqrstuvwxyz");
        alphabet.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        alphabet.append("0123456789");
        Random rnd = new Random();
        StringBuffer name = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int index = (int) (rnd.nextDouble() * alphabet.length());
            name.append(alphabet.charAt(index));
        }
        return name.toString();
    }
}
