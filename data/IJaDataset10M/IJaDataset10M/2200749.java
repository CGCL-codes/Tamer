package ro.jlsoft.lateorders;

import android.app.Activity;
import android.os.Bundle;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Vector;
import android.app.Activity;
import android.os.Bundle;
import android.util.Xml;
import android.widget.Toast;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.io.KXmlParser;
import org.kxml2.kdom.Document;
import org.xmlpull.v1.XmlPullParserException;

public class LateOrders extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String URL = "http://192.168.61.3/TestWeb/Comenzi.asmx";
        String MethodName = "ReturnArray";
        String NAMESPACE = "http://tempuri.org/";
        Order[] allOrders;
        allOrders = GetAllOrders(URL, MethodName, NAMESPACE);
        String Resultn = new String();
        String Result0 = new String();
        String Result1 = new String();
        String Result2 = new String();
        Result0 = "\n" + allOrders[0].get_name() + "  " + allOrders[0].get_client() + "\n" + allOrders[0].get_control();
        Result1 = "\n" + allOrders[1].get_name() + "  " + allOrders[1].get_client() + "\n" + allOrders[1].get_control();
        Result2 = "\n" + allOrders[2].get_name() + "  " + allOrders[2].get_client() + "\n" + allOrders[2].get_control();
        Resultn = "\n" + Result0 + "\n" + Result1 + "\n" + Result2;
        System.out.println(Resultn);
        System.out.println(Result1);
        Toast.makeText(this, "Result: " + Resultn + "\n", Toast.LENGTH_LONG).show();
    }

    public static Order[] GetAllOrders(String URL, String MethodName, String NAMESPACE) {
        SoapObject response = InvokeMethod(URL, MethodName, NAMESPACE);
        return retrieveOrdersFromSoap(response);
    }

    public static SoapObject InvokeMethod(String URL, String MethodName, String NAMESPACE) {
        SoapObject request = GetSoapObject(MethodName, NAMESPACE);
        SoapSerializationEnvelope envelope = GetEnvelope(request);
        return MakeCall(URL, envelope, NAMESPACE, MethodName);
    }

    public static Order[] retrieveOrdersFromSoap(SoapObject soap) {
        Order[] orders = new Order[soap.getPropertyCount()];
        for (int i = 0; i < orders.length; i++) {
            SoapObject pii = (SoapObject) soap.getProperty(i);
            Order passport = new Order();
            passport.set_name(pii.getProperty(0).toString());
            passport.set_surname(pii.getProperty(1).toString());
            passport.set_zone(pii.getProperty(2).toString());
            passport.set_client(pii.getProperty(3).toString());
            passport.set_control(pii.getProperty(4).toString());
            orders[i] = passport;
        }
        return orders;
    }

    public static SoapObject GetSoapObject(String MethodName, String NAMESPACE) {
        return new SoapObject(NAMESPACE, MethodName);
    }

    public static SoapSerializationEnvelope GetEnvelope(SoapObject Soap) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Soap);
        return envelope;
    }

    public static SoapObject MakeCall(String URL, SoapSerializationEnvelope Envelope, String NAMESPACE, String METHOD_NAME) {
        AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
        try {
            Envelope.addMapping(NAMESPACE, Order.ORDER_CLASS.getSimpleName(), Order.ORDER_CLASS);
            androidHttpTransport.call(NAMESPACE + METHOD_NAME, Envelope);
            SoapObject response = (SoapObject) Envelope.getResponse();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
