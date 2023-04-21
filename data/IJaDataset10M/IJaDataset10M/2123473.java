package com.release.ldap.helper;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

public class ParseReqBody extends java.lang.Object implements Serializable {

    private int m_StdBufSize = 1024;

    private ServletInputStream In;

    private String delimitor = null;

    private String filename = null;

    /**
	 * @param req 
	 * @throws IOException
	 */
    public ParseReqBody(HttpServletRequest req) throws IOException {
        delimitor = req.getContentType();
        System.out.println("delimitor outside if: " + delimitor);
        In = req.getInputStream();
        System.out.println("delimitor: " + delimitor);
        if (delimitor.indexOf("boundary=") != -1) {
            delimitor = delimitor.substring(delimitor.indexOf("boundary=") + 9, delimitor.length());
            delimitor = "--" + delimitor;
            System.out.println("delimitor inside if: " + delimitor);
        }
    }

    private String readLine() {
        byte buffer[] = new byte[m_StdBufSize];
        try {
            int noData = In.readLine(buffer, 0, buffer.length);
            if (noData != -1) return new String(buffer, 0, noData, "ISO-8859-1");
        } catch (Exception E) {
            E.printStackTrace();
        }
        return null;
    }

    /**
	 * @return
	 */
    public String getFilename() {
        return filename;
    }

    /**
	 * @return
	 */
    public String getParameterValue() {
        return new String(getParameter());
    }

    /**
	 * @return
	 */
    public String getNextParameter() {
        try {
            String LineIn = null, paramName = null;
            while ((LineIn = readLine()) != null) {
                if (LineIn.indexOf("name=") != -1) {
                    int c1 = LineIn.indexOf("name=");
                    int c2 = LineIn.indexOf("\"", c1 + 6);
                    paramName = LineIn.substring(c1 + 6, c2);
                    if (LineIn.indexOf("filename=") != -1) {
                        c1 = LineIn.indexOf("filename=");
                        c2 = LineIn.indexOf("\"", c1 + 10);
                        filename = LineIn.substring(c1 + 10, c2);
                        if (filename.lastIndexOf("\\") != -1) {
                            filename = filename.substring(filename.lastIndexOf("\\") + 1);
                        }
                        if (filename.length() == 0) filename = null;
                    }
                    LineIn = readLine();
                    if (LineIn.indexOf("Content-Type") != -1) {
                        LineIn = readLine();
                    }
                    return paramName;
                }
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
        return null;
    }

    /**
	 * @return
	 */
    public byte[] getParameter() {
        byte bigByte1[] = new byte[m_StdBufSize];
        int bigByte1Length = 0;
        byte bigByte2[];
        try {
            int noData = 0;
            while (true) {
                byte buffer[] = new byte[m_StdBufSize];
                noData = In.readLine(buffer, 0, buffer.length);
                if (bigByte1Length == 0) {
                    bigByte1 = null;
                    bigByte1 = new byte[noData];
                    System.arraycopy(buffer, 0, bigByte1, 0, noData);
                    bigByte1Length += noData;
                } else {
                    bigByte2 = new byte[bigByte1Length];
                    System.arraycopy(bigByte1, 0, bigByte2, 0, bigByte1Length);
                    bigByte1 = null;
                    bigByte1 = new byte[bigByte1Length + noData];
                    System.arraycopy(bigByte2, 0, bigByte1, 0, bigByte1Length);
                    System.arraycopy(buffer, 0, bigByte1, bigByte1Length, noData);
                    bigByte1Length += noData;
                    bigByte2 = null;
                }
                String sDelim = new String(buffer, 0, noData);
                if (sDelim.indexOf(delimitor) != -1) {
                    bigByte1Length -= sDelim.length();
                    bigByte2 = new byte[bigByte1Length];
                    System.arraycopy(bigByte1, 0, bigByte2, 0, bigByte1Length);
                    return bigByte2;
                }
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
        return null;
    }
}
