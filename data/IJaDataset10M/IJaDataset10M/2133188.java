package com.hisham.creditcard.skipjack.util;

import java.io.*;
import java.util.*;

/**
 *
 * @author  jon_brown
 */
public class SjPaymentHelper {

    private static int datePos = -1;

    private static int orderNumberPos = -1;

    private static int amountPos = -1;

    private static int cctypePos = -1;

    private static int ccnumPos = -1;

    private static int typePos = -1;

    private static int cunamePos = -1;

    private static int detailsPos = -1;

    private static int totalNumFields = 0;

    /** Creates a new instance of SjHelper */
    public SjPaymentHelper() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                printUsage();
                System.exit(1);
            }
            String infilename = args[0];
            String outfilename = args[1];
            createSjPaymentFile(infilename, outfilename);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

    private static void createSjPaymentFile(String infilename, String outfilename) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(infilename);
        BufferedReader reader = new BufferedReader(fr);
        FileWriter fw = new FileWriter(outfilename);
        PrintWriter writer = new PrintWriter(fw);
        String line = reader.readLine();
        setColumnPositions(line);
        while ((line = reader.readLine()) != null) {
            String[] data = line.split("\",\"");
            for (int k = 0; k < data.length; k++) {
                data[k] = trimQuotes(data[k]);
            }
            try {
                if (data.length >= 7) {
                    writer.println(data[orderNumberPos] + "\t" + data[datePos] + "\t" + data[amountPos] + "\t" + data[cctypePos] + "\t" + data[ccnumPos] + "\t" + data[typePos] + "\t" + data[cunamePos] + "\t" + data[detailsPos]);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                System.out.println("orderNumberPos: " + orderNumberPos);
                System.out.println("datePos: " + datePos);
                System.out.println("amountPos: " + amountPos);
                System.out.println("cctypePos: " + cctypePos);
                System.out.println("ccnumPos: " + ccnumPos);
                System.out.println("typePos: " + typePos);
                System.out.println("cunamePos: " + cunamePos);
                System.out.println("detailsPos: " + detailsPos);
                int index = 0;
                while (index < data.length) {
                    if (data[index] != null) {
                        System.out.println("Data[" + index + "] = " + data[index]);
                    } else {
                        System.out.println("Data[" + index + "] is null");
                    }
                    index++;
                }
            }
        }
        reader.close();
        writer.close();
    }

    public static SjPayment[] getSjPayments(String infilename) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(infilename);
        BufferedReader reader = new BufferedReader(fr);
        ArrayList<SjPayment> sjps = new ArrayList<SjPayment>();
        String line = reader.readLine();
        setColumnPositions(line);
        SjPayment sjp;
        while ((line = reader.readLine()) != null) {
            sjp = new SjPayment();
            String[] data = line.split("\",\"");
            for (int k = 0; k < data.length; k++) {
                data[k] = trimQuotes(data[k]);
            }
            try {
                if (data.length >= 7) {
                    sjp.orderNumber = data[orderNumberPos];
                    sjp.date = data[datePos];
                    sjp.amount = data[amountPos];
                    sjp.cctype = data[cctypePos];
                    sjp.ccnum = data[ccnumPos];
                    sjp.type = data[typePos];
                    sjp.cuname = data[cunamePos];
                    sjp.details = data[detailsPos];
                    sjps.add(sjp);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                System.out.println("orderNumber: " + orderNumberPos);
                System.out.println("datePos: " + datePos);
                System.out.println("amountPos: " + amountPos);
                System.out.println("cctypePos: " + cctypePos);
                System.out.println("ccnumPos: " + ccnumPos);
                System.out.println("typePos: " + typePos);
                System.out.println("cunamePos: " + cunamePos);
                System.out.println("detailsPos: " + detailsPos);
                int index = 0;
                while (index < data.length) {
                    if (data[index] != null) {
                        System.out.println("Data[" + index + "] = " + data[index]);
                    } else {
                        System.out.println("Data[" + index + "] is null");
                    }
                    index++;
                }
            }
        }
        reader.close();
        SjPayment[] result = new SjPayment[sjps.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (SjPayment) sjps.get(i);
        }
        return result;
    }

    private static void setColumnPositions(String line) {
        StringTokenizer st = new StringTokenizer(line, ",");
        int i = 0;
        while (st.hasMoreTokens()) {
            String name = st.nextToken();
            if (name.equalsIgnoreCase("\"Transaction Date\"")) {
                datePos = i;
            } else if (name.equalsIgnoreCase("\"Transaction Amount\"")) {
                amountPos = i;
            } else if (name.equalsIgnoreCase("\"Credit Card Type\"")) {
                cctypePos = i;
            } else if (name.equalsIgnoreCase("\"Credit Card Last 5\"")) {
                ccnumPos = i;
            } else if (name.equalsIgnoreCase("\"Transaction Status\"")) {
                typePos = i;
            } else if (name.equalsIgnoreCase("\"Customer Name\"")) {
                cunamePos = i;
            } else if (name.equalsIgnoreCase("\"Item Details\"")) {
                detailsPos = i;
            } else if (name.equalsIgnoreCase("\"Order Number\"")) {
                orderNumberPos = i;
            } else {
            }
            i++;
            totalNumFields++;
        }
        if ((datePos == -1) || (amountPos == -1) || (cctypePos == -1) || (ccnumPos == -1) || (typePos == -1) || (cunamePos == -1) || (detailsPos == -1)) {
            System.out.println("This file does not include all of the required fields.");
            System.out.println("The required fields are as follows:");
            System.out.println("Transaction Date, Transaction Amount, Credit Card Type, Credit Card Last 5, Transaction Status, Customer Name, Item Details");
            System.out.println("The order of these fields does not matter, although the names must be correct.");
            System.exit(1);
        }
    }

    private static String trimQuotes(String s) {
        while (s.startsWith("\"")) {
            s = s.substring(1);
        }
        while (s.endsWith("\"")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    private static void printUsage() {
        System.out.println("java SjHelper <infile> <outfile>");
    }
}
