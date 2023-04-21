package org.sourceforge.jvb3d;

import java.awt.GraphicsConfiguration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import javax.media.j3d.Canvas3D;
import org.sourceforge.jvb3d.Model.IModelNetwork;
import org.sourceforge.jvb3d.Model.Model;
import org.sourceforge.jvb3d.Network.ClientFacade;
import org.sourceforge.jvb3d.Network.NetworkClientFacade;
import org.sourceforge.jvb3d.Network.NetworkClientFacadeHolder;
import org.sourceforge.jvb3d.Network.NetworkServerFacade;
import org.sourceforge.jvb3d.Network.NetworkServerFacadeHolder;
import org.sourceforge.jvb3d.Network.ServerFacade;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * @author Develop
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConsoleClient {

    static NetworkClientFacade networkFacade;

    static Model modelInterface;

    static boolean terminate = false;

    public static void main(String[] args) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        modelInterface = new Model(canvas3D, false);
        NetworkClientFacadeHolder.setNetworkFacade(new ClientFacade());
        networkFacade = NetworkClientFacadeHolder.getNetworkFacade();
        networkFacade.setModelInterface(modelInterface);
        String avatar = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (!terminate) {
                String command = in.readLine();
                if (command.equalsIgnoreCase("quit")) terminate = true; else if (command.equalsIgnoreCase("updateall")) {
                    System.out.println("sending all updates");
                    networkFacade.sendAllUpdate();
                } else if (command.startsWith("update ")) {
                    int pos = command.indexOf(" ");
                    System.out.println("sending update for: " + command.substring(pos + 1));
                    networkFacade.sendUpdate(command.substring(pos + 1));
                } else if (command.startsWith("join ")) {
                    int pos = command.indexOf(" ");
                    int lastpos = command.lastIndexOf(" ");
                    System.out.println("joining: " + command.substring(pos + 1, lastpos) + " on " + command.substring(lastpos + 1));
                    avatar = networkFacade.join(new InetSocketAddress(command.substring(pos + 1, lastpos), Integer.parseInt(command.substring(lastpos + 1))));
                    modelInterface.createLocalPlayer(avatar);
                    System.out.println("joined as: " + avatar);
                } else if (command.equalsIgnoreCase("floodupdates")) {
                    System.out.println("flooding server with updates");
                    for (int i = 0; i < 10; i++) networkFacade.sendUpdate(avatar);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
