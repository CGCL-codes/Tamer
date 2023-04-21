package com.kanteron.PacsViewer;

import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;

public class MyParser {

    MyParser() {
    }

    public Vector parseStudyUID(Object array[]) {
        Vector data = new Vector();
        for (int i = 0; i < array.length; i++) {
            String temp = (String) array[i];
            if (temp.startsWith(Messages.getString("MyParser.0"))) {
                StringTokenizer tokenizer = new StringTokenizer(temp, Messages.getString("MyParser.1"));
                String token = tokenizer.nextToken();
                System.out.println(token);
                token = tokenizer.nextToken();
                data.addElement(token);
                System.out.println(token);
            }
        }
        return data;
    }

    public Vector parseSeriesUID(Object array[]) {
        Vector data = new Vector();
        for (int i = 0; i < array.length; i++) {
            String temp = (String) array[i];
            if (temp.startsWith(Messages.getString("MyParser.2"))) {
                System.out.println(temp);
                StringTokenizer tokenizer = new StringTokenizer(temp, Messages.getString("MyParser.3"));
                String token = tokenizer.nextToken();
                System.out.println(token);
                token = tokenizer.nextToken();
                data.addElement(token);
                System.out.println(token);
            }
        }
        return data;
    }

    public Vector parseNumberSeries(Object array[]) {
        Vector data = new Vector();
        for (int i = 0; i < array.length; i++) {
            String temp = (String) array[i];
            if (temp.startsWith(Messages.getString("MyParser.4"))) {
                System.out.println(temp);
                StringTokenizer tokenizer = new StringTokenizer(temp, Messages.getString("MyParser.5"));
                String token = tokenizer.nextToken();
                System.out.println(token);
                token = tokenizer.nextToken();
                data.addElement(token);
                System.out.println(token);
            }
        }
        return data;
    }

    public Vector parseNumberImages(Object array[]) {
        Vector data = new Vector();
        for (int i = 0; i < array.length; i++) {
            String temp = (String) array[i];
            if (temp.startsWith(Messages.getString("MyParser.6"))) {
                System.out.println(temp);
                StringTokenizer tokenizer = new StringTokenizer(temp, Messages.getString("MyParser.7"));
                String token = tokenizer.nextToken();
                System.out.println(token);
                token = tokenizer.nextToken();
                data.addElement(token);
                System.out.println(token);
            }
        }
        return data;
    }

    public Vector parsePatient(Object array[]) {
        Vector data = new Vector();
        for (int i = 0; i < array.length; i++) {
            String temp = (String) array[i];
            if (temp.startsWith(Messages.getString("MyParser.8"))) {
                System.out.println(temp);
                StringTokenizer tokenizer = new StringTokenizer(temp, Messages.getString("MyParser.9"));
                String token = tokenizer.nextToken();
                System.out.println(token);
                token = tokenizer.nextToken();
                data.addElement(token);
                System.out.println(token);
            }
        }
        return data;
    }

    public Vector parseImages(Object array[]) {
        Vector data = new Vector();
        for (int i = 0; i < array.length; i++) {
            String temp = (String) array[i];
            if (temp.startsWith(Messages.getString("MyParser.10"))) {
                System.out.println(temp);
                StringTokenizer tokenizer = new StringTokenizer(temp, Messages.getString("MyParser.11"));
                String token = tokenizer.nextToken();
                System.out.println(token);
                token = tokenizer.nextToken();
                data.addElement(token);
                System.out.println(token);
            }
        }
        return data;
    }

    public Vector parseSeriesDescription(Object array[]) {
        Vector data = new Vector();
        for (int i = 0; i < array.length; i++) {
            String temp = (String) array[i];
            if (temp.startsWith(Messages.getString("MyParser.12"))) {
                System.out.println(temp);
                StringTokenizer tokenizer = new StringTokenizer(temp, Messages.getString("MyParser.13"));
                String token = tokenizer.nextToken();
                System.out.println(token);
                token = tokenizer.nextToken();
                data.addElement(token);
                System.out.println(token);
            }
        }
        return data;
    }

    public Vector parseSeriesImagesCount(Object array[]) {
        Vector data = new Vector();
        for (int i = 0; i < array.length; i++) {
            String temp = (String) array[i];
            if (temp.startsWith(Messages.getString("MyParser.14"))) {
                System.out.println(temp);
                StringTokenizer tokenizer = new StringTokenizer(temp, Messages.getString("MyParser.15"));
                String token = tokenizer.nextToken();
                System.out.println(token);
                token = tokenizer.nextToken();
                data.addElement(token);
                System.out.println(token);
            }
        }
        return data;
    }

    public Vector parseSeriesModality(Object array[]) {
        Vector data = new Vector();
        for (int i = 0; i < array.length; i++) {
            String temp = (String) array[i];
            if (temp.startsWith(Messages.getString("MyParser.16"))) {
                System.out.println(temp);
                StringTokenizer tokenizer = new StringTokenizer(temp, Messages.getString("MyParser.17"));
                String token = tokenizer.nextToken();
                System.out.println(token);
                token = tokenizer.nextToken();
                data.addElement(token);
                System.out.println(token);
            }
        }
        return data;
    }

    public static void main(String args1[]) {
    }
}
