package org.opennms.netmgt.provision.detector.simple.response;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Donald Desloge
 */
public class MultilineOrientedResponse {

    private BufferedReader m_in;

    private List<String> m_responseList;

    public MultilineOrientedResponse() {
        setResponseList(new ArrayList<String>());
    }

    public void addLine(String line) {
        getResponseList().add(line);
    }

    public void receive(BufferedReader in) {
        m_in = in;
    }

    public boolean startsWith(String prefix) {
        for (String line : getResponseList()) {
            if (!line.startsWith(prefix)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param beginRange
     * @param endRange
     * @return
     */
    public boolean expectedCodeRange(int beginCodeRange, int endCodeRange) {
        for (String line : getResponseList()) {
            if (!validateCodeRange(getCode(line), beginCodeRange, endCodeRange)) {
                return false;
            }
        }
        return true;
    }

    private String getCode(String firstResponseLine) {
        String codeString = firstResponseLine.substring(0, 3);
        return codeString;
    }

    public boolean containedInHTTP(String pattern, String url, boolean isCheckCode, int maxRetCode) {
        try {
            String response = getEntireResponse(m_in);
            System.out.printf("Checking http response, pattern: %s  URL: %s  isCheckCode: %s  MaxRetCode: %s\n", pattern, url, isCheckCode, maxRetCode);
            if (response != null && response.contains(pattern)) {
                System.out.println("Return from server was: " + response);
                if (isCheckCode) {
                    if (("/".equals(url)) || (isCheckCode == false)) {
                        maxRetCode = 600;
                    }
                    StringTokenizer t = new StringTokenizer(response);
                    t.nextToken();
                    String codeString = t.nextToken();
                    if (validateCodeRange(codeString, 99, maxRetCode)) {
                        System.out.println("RetCode Passed");
                        return true;
                    }
                } else {
                    System.out.println("isAServer");
                    return true;
                }
            }
        } catch (SocketException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    /**
     * @return
     * @throws IOException 
     */
    private String getEntireResponse(BufferedReader in) throws IOException {
        char[] cbuf = new char[1024];
        int chars = 0;
        StringBuffer response = new StringBuffer();
        try {
            while ((chars = in.read(cbuf, 0, 1024)) != -1) {
                String line = new String(cbuf, 0, chars);
                response.append(line);
            }
        } catch (java.net.SocketTimeoutException timeoutEx) {
            if (timeoutEx.bytesTransferred > 0) {
                String line = new String(cbuf, 0, timeoutEx.bytesTransferred);
                response.append(line);
            }
        }
        return response.toString();
    }

    /**
     * @param codeString
     * @return
     */
    private boolean validateCodeRange(String codeString, int beginCodeRange, int endCodeRange) {
        try {
            int code = Integer.parseInt(codeString);
            return (code >= beginCodeRange && code <= endCodeRange);
        } catch (Exception e) {
            return false;
        }
    }

    public String toString() {
        return getResponseList().isEmpty() ? "MultilineOrientedResponse" : String.format("Response: %s", getResponseList().toArray());
    }

    public void setResponseList(List<String> responseList) {
        m_responseList = responseList;
    }

    public List<String> getResponseList() {
        return m_responseList;
    }
}
