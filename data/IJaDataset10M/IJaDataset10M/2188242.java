package com.wangyu001.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadUtil {

    public static final int NONE = 0;

    public static final int DATA_HEADER = 1;

    public static final int FIELD_DATA = 2;

    public static final int FILE_DATA = 3;

    private int totalBytes;

    /**
     * 在读取二进制数据文件时需要注意,在用文本形式按行读入数据时,因为readLine()
     * 不能读入回车换行符,按行读入数据会丢失二进制数据中的"0A"和"0D"(十六进制)字
     * 符.所以将响应实体放入一个二进制字节数组中,然后在字节数组中对文件数据进行定 位,这样就不会丢失数据.
     */
    private byte[] requestMessage;

    private String requestMessageStr;

    private String contontType = "";

    private String fieldName = "";

    private String fieldValue = "";

    private String boundary = "";

    private String lastBoundary = "";

    private int fileLength = 0;

    private String fileName = "";

    private Hashtable formFields = new Hashtable();

    private int callBackFlag = 0;

    public UploadUtil(HttpServletRequest req, HttpServletResponse res, String saveFileName) {
        try {
            doPost(req, res, saveFileName);
        } catch (ServletException e) {
            setCallBackFlag(-1);
        } catch (IOException e) {
            setCallBackFlag(-1);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res, String saveFileName) throws ServletException, IOException {
        this.totalBytes = req.getContentLength();
        this.requestMessage = new byte[this.totalBytes];
        this.contontType = req.getContentType();
        int pos = this.contontType.indexOf("boundary=");
        if (pos != -1) {
            pos += "boundary=".length();
            this.boundary = "--" + this.contontType.substring(pos);
            this.lastBoundary = this.boundary + "--";
        }
        int state = this.NONE;
        DataInputStream in = new DataInputStream(req.getInputStream());
        in.readFully(this.requestMessage);
        in.close();
        this.requestMessageStr = new String(this.requestMessage);
        BufferedReader buffer = new BufferedReader(new StringReader(this.requestMessageStr));
        String readLine = "";
        while (true) {
            readLine = buffer.readLine();
            if (readLine == null || readLine.equalsIgnoreCase(this.lastBoundary)) break;
            switch(state) {
                case NONE:
                    if (readLine.startsWith(this.boundary)) {
                        state = this.DATA_HEADER;
                    }
                    break;
                case DATA_HEADER:
                    pos = readLine.indexOf("filename=");
                    if (pos == -1) {
                        pos = readLine.indexOf("name=");
                        pos += "name=".length() + 1;
                        readLine = readLine.substring(pos);
                        fieldName = readLine.substring(0, readLine.length() - 1);
                        state = this.FIELD_DATA;
                    } else {
                        String temp = readLine;
                        pos = readLine.indexOf("filename=") + "filename=".length() + 1;
                        readLine = readLine.substring(pos, readLine.length() - 1);
                        pos = readLine.lastIndexOf("\\");
                        fileName = readLine.substring(pos + 1);
                        pos = this.byteIndexOf(this.requestMessage, temp, 0);
                        this.requestMessage = this.subBytes(this.requestMessage, pos + temp.getBytes().length + 2, this.requestMessage.length);
                        readLine = buffer.readLine();
                        File file = new File(saveFileName);
                        this.fileName = saveFileName;
                        DataOutputStream fileOut = new DataOutputStream(new FileOutputStream(file));
                        this.requestMessage = this.subBytes(this.requestMessage, readLine.getBytes().length + 4, this.requestMessage.length);
                        pos = this.byteIndexOf(this.requestMessage, this.boundary, 0);
                        this.requestMessage = this.subBytes(this.requestMessage, 0, pos - 2);
                        fileOut.write(this.requestMessage);
                        this.fileLength = this.requestMessage.length;
                        state = this.FILE_DATA;
                    }
                    break;
                case FIELD_DATA:
                    this.fieldValue = buffer.readLine();
                    formFields.put(this.fieldName, this.fieldValue);
                    state = this.NONE;
                    break;
                case FILE_DATA:
                    while ((!readLine.startsWith(this.boundary)) && (!readLine.startsWith(this.lastBoundary))) readLine = buffer.readLine();
                    if (readLine.startsWith(this.boundary)) state = this.DATA_HEADER;
                    break;
            }
        }
        setCallBackFlag(1);
    }

    /**
     * @param b 要搜索的字节数组
     * @param s 要查找的字符串
     * @param start 搜索的起始位置
     * @return 如果找到返回s的第一个字节在字节数组中的下标，否则返回-1
     */
    private int byteIndexOf(byte[] b, String s, int start) {
        return this.byteIndexOf(b, s.getBytes(), start);
    }

    /**
     * @param b 要搜索的字节数组
     * @param s 要查找的字节数组
     * @param start 搜索的起始位置
     * @return 如果找到返回s的第一个字节在字节数组中的下标，否则返回-1
     */
    private int byteIndexOf(byte[] b, byte[] s, int start) {
        int i;
        if (s.length == 0) return 0;
        int max = b.length - s.length;
        if (max < 0) return -1; else if (start > max) return -1; else if (start < 0) start = 0;
        search: for (i = start; i < max; i++) {
            if (b[i] == s[0]) {
                int k = 1;
                while (k < s.length) {
                    if (b[k + i] != s[k]) continue search;
                    k++;
                }
                return i;
            }
        }
        return -1;
    }

    /**
     * 在一个字节数组中提取一个字节数组
     */
    private byte[] subBytes(byte[] b, int from, int end) {
        byte[] result = new byte[end - from];
        System.arraycopy(b, from, result, 0, end - from);
        return result;
    }

    /**
     * 在一个字节数组中提取一个字符串
     */
    private String subBytesToString(byte[] b, int from, int end) {
        return new String(this.subBytes(b, from, end));
    }

    public int getCallBackFlag() {
        return callBackFlag;
    }

    public void setCallBackFlag(int callBackFlag) {
        this.callBackFlag = callBackFlag;
    }
}
