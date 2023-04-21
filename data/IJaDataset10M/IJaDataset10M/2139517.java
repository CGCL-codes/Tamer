package mygame;

import com.jme3.math.Vector3f;
import com.jme3.network.base.DefaultClient;
import com.jme3.network.kernel.tcp.SocketConnector;
import com.jme3.network.message.StreamDataMessage;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author crazysaem
 */
public class bamc_client {

    private SocketConnector tcpconnector = null;

    private InetAddress ipadress = null;

    private DefaultClient client = null;

    private bamc_messageListener messageListener = null;

    private StreamDataMessage message = null;

    public bamc_client(byte ip1, byte ip2, byte ip3, byte ip4, World world, DrawPlayers drawplayers) {
        try {
            byte[] ip = new byte[4];
            ip[0] = ip1;
            ip[1] = ip2;
            ip[2] = ip3;
            ip[3] = ip4;
            message = new StreamDataMessage();
            ipadress = InetAddress.getByAddress(ip);
            tcpconnector = new SocketConnector(ipadress, 1337);
            messageListener = new bamc_messageListener(world, drawplayers);
            client = new DefaultClient("BAMC", 1, tcpconnector, null);
            client.addMessageListener(messageListener);
            client.start();
        } catch (IOException ex) {
            Logger.getLogger(bamc_client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SendPlayerData(Vector3f pos, Vector3f dir) {
        byte[] id = ByteIntConvert.intToByteArray(client.getId());
        byte[] x = ByteFloatConvert.floatToByteArray(pos.x);
        byte[] y = ByteFloatConvert.floatToByteArray(pos.y);
        byte[] z = ByteFloatConvert.floatToByteArray(pos.z);
        byte[] data = new byte[29];
        data[0] = 1;
        data[1] = id[0];
        data[2] = id[1];
        data[3] = id[2];
        data[4] = id[3];
        data[5] = x[0];
        data[6] = x[1];
        data[7] = x[2];
        data[8] = x[3];
        data[9] = y[0];
        data[10] = y[1];
        data[11] = y[2];
        data[12] = y[3];
        data[13] = z[0];
        data[14] = z[1];
        data[15] = z[2];
        data[16] = z[3];
        x = ByteFloatConvert.floatToByteArray(dir.x);
        y = ByteFloatConvert.floatToByteArray(dir.y);
        z = ByteFloatConvert.floatToByteArray(dir.z);
        data[17] = x[0];
        data[18] = x[1];
        data[19] = x[2];
        data[20] = x[3];
        data[21] = y[0];
        data[22] = y[1];
        data[23] = y[2];
        data[24] = y[3];
        data[25] = z[0];
        data[26] = z[1];
        data[27] = z[2];
        data[28] = z[3];
        message.setData(data);
        client.send(message);
    }

    public void SendBlockData(int x, int y, int z, byte type) {
        byte[] data = new byte[14];
        byte[] bx = ByteIntConvert.intToByteArray(x);
        byte[] by = ByteIntConvert.intToByteArray(y);
        byte[] bz = ByteIntConvert.intToByteArray(z);
        data[0] = 2;
        data[1] = bx[0];
        data[2] = bx[1];
        data[3] = bx[2];
        data[4] = bx[3];
        data[5] = by[0];
        data[6] = by[1];
        data[7] = by[2];
        data[8] = by[3];
        data[9] = bz[0];
        data[10] = bz[1];
        data[11] = bz[2];
        data[12] = bz[3];
        data[13] = type;
        message.setData(data);
        client.send(message);
    }
}
