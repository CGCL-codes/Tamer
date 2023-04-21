package com.ebartsoft.demo.nabaztag.classes;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public final class Nabaztag {

    private static final String APIURL = "http://api.nabaztag.com/vl/FR/api.jsp";

    private DefaultHttpClient http;

    private DocumentBuilderFactory dbf;

    private DocumentBuilder db;

    private Context mContext;

    private static String mSerial;

    private static String mToken;

    public Nabaztag(Context context) {
        mContext = context;
        nabUpdateSerialAndToken(context);
    }

    private Element sendXMLRequest(String uri) throws Exception {
        if (http == null) http = new DefaultHttpClient();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = http.execute(request);
        InputStream is = response.getEntity().getContent();
        if (dbf == null) dbf = DocumentBuilderFactory.newInstance();
        if (db == null) db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);
        Element e = doc.getDocumentElement();
        e.normalize();
        return e;
    }

    public static void nabUpdateSerialAndToken(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        mSerial = pref.getString("serial", "");
        mToken = pref.getString("token", "");
    }

    public void nabWakeup() {
        try {
            String url = APIURL + "?sn=" + mSerial + "&token=" + mToken + "&action=14";
            Element root = sendXMLRequest(url);
            String nabMessage = root.getElementsByTagName("message").item(0).getFirstChild().getNodeValue();
            String nabComment = root.getElementsByTagName("comment").item(0).getFirstChild().getNodeValue();
            Toast.makeText(mContext, nabMessage + "\n" + nabComment, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    public void nabSendMessage(String message) {
        try {
            String url = APIURL + "?sn=" + mSerial + "&token=" + mToken + "&tts=" + URLEncoder.encode(message, "UTF-8");
            Element root = sendXMLRequest(url);
            String nabMessage = root.getElementsByTagName("message").item(0).getFirstChild().getNodeValue();
            String nabComment = root.getElementsByTagName("comment").item(0).getFirstChild().getNodeValue();
            Toast.makeText(mContext, nabMessage + "\n" + nabComment, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    public void nabMoveEars(int posleft, int posright) {
        try {
            String url = APIURL + "?sn=" + mSerial + "&token=" + mToken + "&posleft=" + posleft + "&=posright" + posright + "&ears=ok";
            Element root = sendXMLRequest(url);
            String nabMessage = root.getElementsByTagName("message").item(0).getFirstChild().getNodeValue();
            String nabComment = root.getElementsByTagName("comment").item(0).getFirstChild().getNodeValue();
            Toast.makeText(mContext, nabMessage + "\n" + nabComment, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    public void nabChoregraphie(String chor) {
        try {
            String url = APIURL + "?sn=" + mSerial + "&token=" + mToken + "&chor=" + chor;
            Element root = sendXMLRequest(url);
            String nabMessage = root.getElementsByTagName("message").item(0).getFirstChild().getNodeValue();
            String nabComment = root.getElementsByTagName("comment").item(0).getFirstChild().getNodeValue();
            Toast.makeText(mContext, nabMessage + "\n" + nabComment, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    public void nabGetFriendList() {
        try {
            String list = "";
            String url = APIURL + "?sn=" + mSerial + "&token=" + mToken + "&action=2";
            Element root = sendXMLRequest(url);
            NodeList nodes = root.getElementsByTagName("friend");
            int n = nodes.getLength();
            for (int i = 0; i < n; i++) {
                Node node = nodes.item(i);
                NamedNodeMap attrs = node.getAttributes();
                list += attrs.getNamedItem("name").getNodeValue() + "\n";
            }
            Toast.makeText(mContext, list, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    public void CallWebService() {
        String URL = "http://mathertel.de/AJAXEngine/S02_AJAXCoreSamples/OrteLookup.asmx";
        String NAMESPACE = "http://www.mathertel.de/OrteLookup/";
        String SOAP_ACTION = "http://www.mathertel.de/OrteLookup/OrteStartWith";
        String METHOD_NAME = "OrteStartWith";
        Object resultRequestSOAP = null;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        Toast.makeText(mContext, envelope.toString(), Toast.LENGTH_LONG).show();
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            resultRequestSOAP = envelope.getResponse();
            String results = (String) resultRequestSOAP;
            Toast.makeText(mContext, results, Toast.LENGTH_LONG).show();
        } catch (Exception aE) {
            aE.printStackTrace();
            Toast.makeText(mContext, aE.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
