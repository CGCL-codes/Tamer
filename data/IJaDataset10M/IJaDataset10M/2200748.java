package jabberSrpc;

import jabberSrpc.Base64.DecodingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import org.jivesoftware.smack.packet.IQ;
import org.xml.sax.SAXException;

public class RPCPacket extends IQ {

    public static final int REQUEST = 1;

    public static final int RESPONSE = 2;

    private String content;

    private String stringData;

    private Object object;

    private int packetType;

    private String methodName;

    public RPCPacket() {
        content = "";
    }

    public void createRequest(String from, String to, String id) {
        try {
            setPacketType(REQUEST);
            setType(IQ.Type.SET);
            setTo(to);
            setFrom(from);
            if (id != null) {
                setPacketID(id);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter steamWriter;
            steamWriter = new OutputStreamWriter(out, "UTF-8");
            XMLWriterImpl writer = new XMLWriterImpl();
            writer.setWriter(steamWriter);
            writer.startElement("methodCall");
            writer.startElement("methodName");
            writer.characters(methodName);
            writer.endElement("methodName");
            writer.startElement("params");
            writer.startElement("param");
            writer.startElement("base64");
            writer.characters(stringData);
            writer.endElement("base64");
            writer.endElement("param");
            writer.endElement("params");
            writer.endElement("methodCall");
            steamWriter.flush();
            content = out.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createResponse(String from, String to, String id) {
        try {
            setPacketType(RESPONSE);
            setType(IQ.Type.RESULT);
            setTo(to);
            setFrom(from);
            if (id != null) {
                setPacketID(id);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter steamWriter;
            steamWriter = new OutputStreamWriter(out, "UTF-8");
            XMLWriterImpl writer = new XMLWriterImpl();
            writer.setWriter(steamWriter);
            writer.startElement("methodResponse");
            writer.startElement("params");
            writer.startElement("param");
            writer.startElement("base64");
            writer.characters(stringData);
            writer.endElement("base64");
            writer.endElement("param");
            writer.endElement("params");
            writer.endElement("methodResponse");
            steamWriter.flush();
            content = out.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RPCPacket createResponseFromRequest(String data) {
        RPCPacket p = new RPCPacket();
        p.setStringData(data);
        p.createResponse(this.getTo(), this.getFrom(), this.getPacketID());
        return p;
    }

    @Override
    public String getChildElementXML() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<query xmlns=\"jabber:iq:rpc\">");
        buffer.append(content);
        buffer.append("</query>");
        return buffer.toString();
    }

    private Object ByteArray2Object(byte[] data) {
        if (data == null) return null;
        if (data.length == 0) return null;
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Object o = null;
        try {
            o = new ObjectInputStream(bais).readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return o;
    }

    public String getStringData() {
        return stringData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }

    public byte[] getByteData() {
        byte[] b = null;
        try {
            b = Base64.decode(stringData);
        } catch (DecodingException e) {
            e.printStackTrace();
        }
        return b;
    }

    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getObject() {
        object = ByteArray2Object(getByteData());
        return object;
    }
}
