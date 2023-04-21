package net;

import java.net.DatagramPacket;
import java.net.InetAddress;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GimmiState implements NetMessage {

    @Override
    public DatagramPacket getMsgPacket(InetAddress dst, int port) {
        DocumentBuilder db;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document msg = db.newDocument();
            Element root = msg.createElement(GameNetMessages.khetMsg);
            msg.appendChild(root);
            root.setAttribute("type", GameNetMessages.gimmiState);
            String msgXML = GameNetMessages.getMsg(msg);
            if (msgXML == null) return null;
            byte[] buf = msgXML.getBytes();
            return new DatagramPacket(buf, buf.length, dst, port);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
