package com.aol.OpenAIM;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class Utils {

    enum ResponseType {

        CLIENTLOGIN_RESPONSE, LOGOUT_RESPONSE, STARTSESSION_RESPONSE, ENDSESSION_RESPONSE, FETCHEVENTS_RESPONSE, SENDIM_RESPONSE
    }

    ;

    private static String CLIENTLOGIN_URL = "https://api.screenname.aol.com/auth/clientLogin";

    private static String LOGOUT_URL = "https://api.screenname.aol.com/auth/logout";

    private static String STARTSESSION_URL = "http://api.oscar.aol.com/aim/startSession";

    private static String ENDSESSION_URL = "http://api.oscar.aol.com/aim/endSession";

    private static String SENDIM_URL = "http://api.oscar.aol.com/im/sendIM";

    public static String getHmacSHA256(String key, String text) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        ;
        mac.init(keySpec);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(mac.doFinal(text.getBytes()));
    }

    public static String urlEncodeStr(String s) {
        String encoded = "";
        try {
            encoded = java.net.URLEncoder.encode(s, "UTF-8").replaceAll("_", "%5F").replaceAll(",", "%2C");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoded;
    }

    public static HashMap parseResponse(InputStream is, ResponseType responseType) {
        HashMap result = new HashMap();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine;
        String body = "";
        try {
            while (((readLine = br.readLine()) != null)) {
                System.err.println(readLine);
                body += readLine + "\n";
            }
            br.close();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new ByteArrayInputStream(body.getBytes()));
            doc.getDocumentElement().normalize();
            String status = doc.getElementsByTagName("statusCode").item(0).getTextContent();
            result.put("statusCode", status);
            result.put("statusText", doc.getElementsByTagName("statusText").item(0).getTextContent());
            int statusCode = Integer.parseInt(status);
            if (statusCode == 200) {
                if (responseType == ResponseType.CLIENTLOGIN_RESPONSE) {
                    result.put("a", doc.getElementsByTagName("a").item(0).getTextContent());
                    result.put("sessionSecret", doc.getElementsByTagName("sessionSecret").item(0).getTextContent());
                    result.put("hostTime", doc.getElementsByTagName("hostTime").item(0).getTextContent());
                    result.put("expiresIn", doc.getElementsByTagName("expiresIn").item(0).getTextContent());
                } else if (responseType == ResponseType.STARTSESSION_RESPONSE) {
                    result.put("fetchBaseURL", doc.getElementsByTagName("fetchBaseURL").item(0).getTextContent());
                    result.put("aimsid", doc.getElementsByTagName("aimsid").item(0).getTextContent());
                } else if (responseType == ResponseType.FETCHEVENTS_RESPONSE) {
                    result.put("timeToNextFetch", doc.getElementsByTagName("timeToNextFetch").item(0).getTextContent());
                    result.put("fetchBaseURL", doc.getElementsByTagName("fetchBaseURL").item(0).getTextContent());
                    NodeList nl = doc.getElementsByTagName("event");
                    int len = nl.getLength();
                    if (len > 0) {
                        for (int i = 0; i < len; i++) {
                            Node eventNode = nl.item(i);
                            Node type = eventNode.getFirstChild();
                            String typeName = type.getTextContent();
                            if (typeName.equalsIgnoreCase("buddylist")) {
                                NodeList buddy = doc.getElementsByTagName("buddy");
                                int buddyNum = buddy.getLength();
                                HashMap buddylistMap = new HashMap();
                                for (int j = 0; j < buddyNum; j++) {
                                    String name = buddy.item(j).getFirstChild().getTextContent();
                                    buddylistMap.put(name, name);
                                }
                                result.put("buddylist", buddylistMap);
                            } else if (typeName.equalsIgnoreCase("im")) {
                                Node eventDataNode = eventNode.getFirstChild().getNextSibling();
                                Node sourceNode = eventDataNode.getFirstChild();
                                Node aimIdNode = sourceNode.getFirstChild();
                                Node messageNode = sourceNode.getNextSibling();
                                String aimId = aimIdNode.getTextContent();
                                String rawMessage = messageNode.getTextContent().replaceAll("\\<.*?>", "");
                                result.put("aimId", aimId);
                                result.put("newMessage", rawMessage);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static HashMap clientLogin(String userId, String pwd, String devId) {
        HashMap response = null;
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(CLIENTLOGIN_URL);
        method.addParameter("f", "xml");
        method.addParameter("devId", devId);
        method.addParameter("tokenType", "shortterm");
        method.addParameter("s", userId);
        method.addParameter("pwd", pwd);
        try {
            int returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_OK) {
                response = parseResponse(method.getResponseBodyAsStream(), ResponseType.CLIENTLOGIN_RESPONSE);
                if (response != null) {
                    String status = (String) response.get("statusCode");
                    int statusCode = Integer.parseInt(status);
                    if (statusCode == 200) {
                        String sessionSecret = (String) response.get("sessionSecret");
                        response.put("sessionKey", getHmacSHA256(pwd, sessionSecret));
                    }
                }
            } else {
                System.err.println("The Post method is not implemented by this URI");
                method.getResponseBodyAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    public static HashMap logout(String token, String sessionKey, String devId) {
        HashMap response = null;
        GetMethod method = null;
        try {
            String params = "a=" + token;
            params += "&devId=" + urlEncodeStr(devId);
            params += "&f=xml";
            String encodedParams = urlEncodeStr(params);
            String sigBase = "GET&" + urlEncodeStr(LOGOUT_URL);
            sigBase += "&" + encodedParams;
            String shaSig256 = getHmacSHA256(sessionKey, sigBase);
            params += "&sig_sha256=" + urlEncodeStr(shaSig256);
            HttpClient client = new HttpClient();
            method = new GetMethod(LOGOUT_URL + "?" + params);
            if (client.executeMethod(method) == HttpStatus.SC_OK) {
                response = parseResponse(method.getResponseBodyAsStream(), ResponseType.LOGOUT_RESPONSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    public static HashMap startSession(String token, String hostTime, String devId, String sessionKey) {
        HashMap response = null;
        GetMethod method = null;
        try {
            String params = "a=" + token;
            params += "&clientName=omp";
            params += "&clientVersion=1";
            params += "&events=" + urlEncodeStr("buddylist,im");
            params += "&f=xml";
            params += "&k=" + urlEncodeStr(devId);
            params += "&ts=" + urlEncodeStr(hostTime);
            String encodedParams = urlEncodeStr(params);
            String sigBase = "GET&" + urlEncodeStr(STARTSESSION_URL) + "&" + encodedParams;
            String shaSig256 = getHmacSHA256(sessionKey, sigBase);
            params += "&sig_sha256=" + urlEncodeStr(shaSig256);
            HttpClient client = new HttpClient();
            method = new GetMethod(STARTSESSION_URL + "?" + params);
            if (client.executeMethod(method) == HttpStatus.SC_OK) {
                response = parseResponse(method.getResponseBodyAsStream(), ResponseType.STARTSESSION_RESPONSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    public static HashMap endSession(String aimsid) {
        HashMap response = null;
        GetMethod method = null;
        try {
            String params = "aimsid=" + aimsid;
            params += "&f=xml";
            HttpClient client = new HttpClient();
            method = new GetMethod(ENDSESSION_URL + "?" + params);
            if (client.executeMethod(method) == HttpStatus.SC_OK) {
                response = parseResponse(method.getResponseBodyAsStream(), ResponseType.ENDSESSION_RESPONSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    public static HashMap fetchEvents(String fetchBaseURL, String aimsid, int timeout) {
        HashMap response = null;
        GetMethod method = null;
        try {
            String params = "&f=xml&timeout=" + timeout;
            HttpClient client = new HttpClient();
            method = new GetMethod(fetchBaseURL + params);
            if (client.executeMethod(method) == HttpStatus.SC_OK) {
                response = parseResponse(method.getResponseBodyAsStream(), ResponseType.FETCHEVENTS_RESPONSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    public static HashMap sendIM(String aimsid, String devId, String buddyName, String msg) {
        HashMap response = null;
        GetMethod method = null;
        try {
            String params = "aimsid=" + aimsid;
            params += "&f=xml";
            params += "&k=" + urlEncodeStr(devId);
            params += "&message=" + urlEncodeStr(msg);
            params += "&t=" + urlEncodeStr(buddyName);
            HttpClient client = new HttpClient();
            method = new GetMethod(SENDIM_URL + "?" + params);
            if (client.executeMethod(method) == HttpStatus.SC_OK) {
                response = parseResponse(method.getResponseBodyAsStream(), ResponseType.SENDIM_RESPONSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return response;
    }
}
