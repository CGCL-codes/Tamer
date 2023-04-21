package com.jsu.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.apache.log4j.Logger;
import com.jsu.util.JudgeConfig;

/**
 * @author: Luo Qiang
 * Last Modified: 2010-2-4
 */
public class StreamHandler {

    private static final Logger log = Logger.getLogger(StreamHandler.class);

    public static void write(OutputStream os, String content) {
        try {
            PrintWriter out = new PrintWriter(new BufferedOutputStream(os));
            out.write(content);
            out.close();
        } catch (Exception e) {
            log.warn("---- write to outputStream catches a IOException, outputStream not available ----");
        }
    }

    public static String read(String fileName) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            StringBuffer sb = new StringBuffer();
            String line = new String();
            while ((line = in.readLine()) != null) {
                sb.append(line.trim());
                sb.append("\n");
            }
            in.close();
            return sb.toString();
        } catch (IOException ioe) {
            log.warn("---- read file by fileName catches a IOException, file not found ----");
            return null;
        }
    }

    public static String read(File file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            StringBuffer sb = new StringBuffer();
            String line = new String();
            while ((line = in.readLine()) != null) {
                sb.append(line.trim());
                sb.append("\n");
            }
            in.close();
            return sb.toString();
        } catch (IOException ioe) {
            log.warn("---- read file by file catches a IOException, file not found ----");
            return null;
        }
    }

    public static boolean write(String fileName, String content) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            out.write(content);
            out.close();
            return true;
        } catch (IOException ioe) {
            log.warn("---- write file by fileName catches a IOException ----");
            ioe.printStackTrace();
            return false;
        }
    }

    public static boolean write(File file, String content) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.write(content);
            out.close();
            return true;
        } catch (IOException ioe) {
            log.warn("---- write file by file catches a IOException ----");
            ioe.printStackTrace();
            return false;
        }
    }

    /***********************  inputs and outputs files getters ************************/
    public static File[] getInputFiles(Integer problemId) {
        File file = new File(JudgeConfig.getValue("JUDGE_BASE") + "data\\" + problemId.toString() + "\\inputs");
        log.info("---- load " + file.getAbsolutePath() + " ----");
        if (file.isDirectory()) return file.listFiles();
        return null;
    }

    public static String[] getInputFilesName(Integer problemId) {
        File file = new File(JudgeConfig.getValue("JUDGE_BASE") + "data\\" + problemId.toString() + "\\inputs");
        log.info("---- load " + file.getAbsolutePath() + " ----");
        if (file.isDirectory()) return file.list();
        return null;
    }

    public static File[] getOutputFiles(Integer problemId) {
        File file = new File(JudgeConfig.getValue("JUDGE_BASE") + "data\\" + problemId.toString() + "\\outputs");
        log.info("---- load " + file.getAbsolutePath() + " ----");
        if (file.isDirectory()) return file.listFiles();
        return null;
    }

    public static String[] getOutputFilesName(Integer problemId) {
        File file = new File(JudgeConfig.getValue("JUDGE_BASE") + "data\\" + problemId.toString() + "\\outputs");
        log.info("---- load " + file.getAbsolutePath() + " ----");
        if (file.isDirectory()) return file.list();
        return null;
    }
}
