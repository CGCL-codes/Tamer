package icm.unicore.iademo2.plugins.iademo;

import java.awt.Rectangle;
import java.io.*;
import java.net.*;
import javax.net.ssl.SSLSocket;
import org.unicore.Vsite;
import org.unicore.upl.*;
import org.unicore.AJOIdentifier;
import com.pallas.unicore.extensions.Usite;
import com.pallas.unicore.connection.UnicoreSSLSocketFactory;
import com.pallas.unicore.resourcemanager.ResourceManager;
import com.pallas.unicore.utility.UserMessages;
import icm.unicore.iademo2.protocol.*;

public class ConnectionHandler implements ICommunicationProvider {

    private Usite usite;

    private Vsite vsite;

    private Socket socket;

    ObjectOutputStream oos;

    ObjectInputStream ois;

    private IADemoProtocol2 protoUtils;

    /** Creates a new instance of ConnectionHandler */
    private ConnectionHandler(Usite usite, Vsite vsite) {
        this.usite = usite;
        this.vsite = vsite;
        protoUtils = new IADemoProtocol2();
    }

    private boolean init() {
        try {
            URL url = usite.getAddress();
            String usiteAddress = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
            socket = (new UnicoreSSLSocketFactory(ResourceManager.getUser(usite))).createSocket(usiteAddress);
            socket.setSoTimeout(60000);
            Object[] gatewayCert = ((SSLSocket) socket).getSession().getPeerCertificates();
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            ConsignJob consignJob = new ConsignJob(new AJOIdentifier(IIMagic.II_MAGIC), vsite, ResourceManager.getUser(usite), null, null, null, false, false);
            oos.writeObject(consignJob);
            Object rawReply = ois.readObject();
            if (!(rawReply instanceof ConsignJobReply)) {
                UserMessages.error("Can't connect to Vsite. Unknown reply");
                return false;
            }
            ConsignJobReply reply = (ConsignJobReply) rawReply;
            if (reply.getLastEntry().getReturnCode() < 0) {
                UserMessages.error("Can't connect to Vsite. Returned reason: \n" + reply.getLastEntry().getComment());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static ConnectionHandler getConnectionHandler(Usite usite, Vsite vsite) {
        ConnectionHandler handler = new ConnectionHandler(usite, vsite);
        if (handler.init()) return handler;
        return null;
    }

    public ImageData getImageData() throws IOException {
        protoUtils.buffer[0] = protoUtils.A_GET_IMAGE_DATA;
        protoUtils.writeBuffer(oos);
        protoUtils.readBuffer(ois);
        ImageData id = new ImageData();
        id.description = ois.readUTF();
        id.dimensions = new Rectangle(0, 0, protoUtils.buffer[1], protoUtils.buffer[2]);
        return id;
    }

    public void endSession() throws IOException {
        protoUtils.buffer[0] = protoUtils.A_END_SESSION;
        protoUtils.writeBuffer(oos);
        ois.close();
        oos.close();
        socket.close();
    }

    public ImageContent getJpegImage(Rectangle dim, float q) throws IOException {
        protoUtils.buffer[0] = protoUtils.A_GET_JPEG_IMAGE;
        protoUtils.buffer[1] = dim.width;
        protoUtils.buffer[2] = dim.height;
        protoUtils.buffer[3] = dim.x;
        protoUtils.buffer[4] = dim.y;
        protoUtils.quality = q;
        protoUtils.writeBuffer(oos);
        protoUtils.readBuffer(ois);
        int extraSize = ois.readInt();
        ImageContent ic = new ImageContent();
        ic.dimensions = new Rectangle(protoUtils.buffer[3], protoUtils.buffer[4], protoUtils.buffer[1], protoUtils.buffer[2]);
        ic.content = new byte[extraSize];
        ois.readFully(ic.content);
        return ic;
    }

    public ImageContent getPngImage(Rectangle dim) throws IOException {
        protoUtils.buffer[0] = protoUtils.A_GET_PNG_IMAGE;
        protoUtils.buffer[1] = dim.width;
        protoUtils.buffer[2] = dim.height;
        protoUtils.buffer[3] = dim.x;
        protoUtils.buffer[4] = dim.y;
        protoUtils.writeBuffer(oos);
        protoUtils.readBuffer(ois);
        int extraSize = ois.readInt();
        ImageContent ic = new ImageContent();
        ic.dimensions = new Rectangle(protoUtils.buffer[3], protoUtils.buffer[4], protoUtils.buffer[1], protoUtils.buffer[2]);
        ic.content = new byte[extraSize];
        ois.readFully(ic.content);
        return ic;
    }
}
