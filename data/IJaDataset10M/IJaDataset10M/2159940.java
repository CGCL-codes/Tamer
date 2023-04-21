package de.fmf.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

public class URLConnectionReader implements Runnable {

    File baseDownloadFolder = new File("c:\\Dokumente und Einstellungen\\fma\\Desktop\\download");

    private String url;

    private ArrayList<String> links = new ArrayList<String>();

    private ArrayList<String> pix = new ArrayList<String>();

    public static void main(String[] args) {
        URLConnectionReader x = new URLConnectionReader("http://fhg.dacash.com/gals/mature/4/1");
        ArrayList<String> xxx = x.analyzeUrlForLinks();
        for (Iterator<String> iterator = xxx.iterator(); iterator.hasNext(); ) {
            String s = iterator.next();
            if (s.endsWith("jpg")) {
                System.out.println(x.url + s);
                new UrlReader(false, x.url + s, new File(x.baseDownloadFolder, x.url.substring(x.url.lastIndexOf('/')) + System.currentTimeMillis()));
            }
        }
    }

    public URLConnectionReader(String url) {
        this.url = url;
    }

    private ArrayList<String> analyzeUrlForLinks() {
        Thread t = new Thread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return links;
    }

    private ArrayList<String> analyzeUrlForPix() {
        Thread t = new Thread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pix;
    }

    @Override
    public void run() {
        StringBuffer content = new StringBuffer();
        try {
            URL yahoo = new URL(url);
            URLConnection yc = yahoo.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("");
        if (content.toString().contains("href") || content.toString().contains("HREF")) {
            try {
                String[] xxx = content.toString().replace("HREF", "href").split("href");
                for (int i = 1; i < xxx.length; i++) {
                    String string = xxx[i];
                    string = string.substring(string.indexOf('"') + 1);
                    string = string.substring(0, string.indexOf('"'));
                    if (!links.contains(string)) links.add(string);
                    System.out.println(string);
                }
            } catch (Exception e) {
            }
        }
    }
}
