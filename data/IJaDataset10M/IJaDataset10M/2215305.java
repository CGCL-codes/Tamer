package hu.szig.AliceEngine.utils.network;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.lwjgl.util.vector.Vector3f;
import hu.szig.AliceEngine.modell.Mesh;
import hu.szig.AliceEngine.modell.Modell;
import hu.szig.AliceEngine.utils.Create;
import hu.szig.AliceEngine.utils.My3DWindow.Renderable;

public class MultiScene implements Renderable, IUdpMessageHandler {

    Map<String, Modell> guest = new HashMap<String, Modell>();

    Map<String, Modell> home = new HashMap<String, Modell>();

    Mesh enemyMesh;

    UdpClient connection;

    public MultiScene(String host, int port) throws IOException {
        connection = new UdpClient(this, host, port);
        System.out.println("Build UDP connection for Multi Gaming.");
        System.out.println("host: " + host);
        System.out.println("port: " + port);
        enemyMesh = new Mesh(new File("ship_01.obj"));
    }

    public void registerModell(Modell akt) {
        home.put(akt.name, akt);
    }

    protected static String vectorToString(Vector3f v) {
        return "" + v.x + ";" + v.y + ";" + v.z;
    }

    protected void processPosition(String[] msg) {
        Modell akt = guest.get(msg[1]);
        if (akt == null) {
            System.out.println(msg[1] + " added.");
            akt = new Modell("BodyOf" + msg[1]);
            akt.setMesh(enemyMesh);
            setColor(akt);
            guest.put(msg[1], akt);
        }
        akt.trans.setData(msg, 2);
    }

    private void setColor(Modell akt) {
        for (int len = akt.getMesh().faceGroups.size() - 1; len > 0; len--) akt.setColor(len, Create.RandomColor());
    }

    protected void sendPosition(Entry<String, Modell> akt) {
        String msg = "position " + akt.getKey() + " " + vectorToString(akt.getValue().trans.position) + " " + vectorToString(akt.getValue().trans.up) + " " + vectorToString(akt.getValue().trans.forward);
        try {
            connection.send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(InetSocketAddress source, byte[] bytes, int length) {
        String msg = new String(bytes, 0, length);
        try {
            String[] pieces = msg.split(" ");
            if (pieces.length > 0 && pieces[0].equals("position")) {
                String hostID = pieces[1];
                if (home.containsKey(hostID)) return;
                processPosition(pieces);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render() {
        for (Modell akt : guest.values()) akt.render();
        for (Entry<String, Modell> akt : home.entrySet()) sendPosition(akt);
    }
}
