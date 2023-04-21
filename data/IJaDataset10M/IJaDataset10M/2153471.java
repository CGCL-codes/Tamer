package com.exedosoft.plat.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.exedosoft.plat.bo.DOApplication;

public class ReadTxtFile {

    public ReadTxtFile() {
    }

    void copyDir(DOApplication dop) {
        URL url = DOGlobals.class.getResource("/globals.xml");
        String fullFilePath = url.getPath();
        String prefix = fullFilePath.substring(0, fullFilePath.toLowerCase().indexOf("web-inf"));
        try {
            StringUtil.copyDirectiory(prefix + dop.getName(), prefix + "exedo/baseproject/");
            File indexFile = new File(prefix + dop.getName() + "/index.jsp");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(indexFile), "utf-8"));
            StringBuffer sb = new StringBuffer();
            while (true) {
                String aLine = in.readLine();
                if (aLine == null) {
                    break;
                }
                System.out.println(aLine);
                if (aLine.indexOf("pane_dorgauth.pml") != -1) {
                    aLine = aLine.replace("pane_dorgauth", "pane_aaaaa" + dop.getName());
                }
                sb.append(aLine).append("\n");
            }
            in.close();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indexFile), "utf-8"));
            out.append(sb.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(prefix);
    }

    public static void convertGBK2UTF4File(String filePath) {
        File indexFile = new File(filePath);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(indexFile), "gbk"));
            StringBuffer sb = new StringBuffer();
            while (true) {
                String aLine = in.readLine();
                if (aLine == null) {
                    break;
                }
                sb.append(aLine).append("\n");
            }
            in.close();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indexFile), "utf-8"));
            out.append(sb.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        File indexFile = new File("c:\\mydb.script");
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(indexFile), "utf-8"));
        StringBuffer sb = new StringBuffer();
        while (true) {
            String aLine = in.readLine();
            if (aLine == null) {
                break;
            }
            sb.append(aLine).append(";").append("\n");
        }
        in.close();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indexFile), "utf-8"));
        out.append(sb.toString());
        out.flush();
        out.close();
    }
}
