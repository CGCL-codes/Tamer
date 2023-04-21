package org.osll.tictactoe.transport.corba.server;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.osll.tictactoe.game.Game;
import org.osll.tictactoe.transport.corba.service.Control;
import org.osll.tictactoe.transport.corba.service.ControlPOATie;

public class GameServer implements Runnable {

    private static int count = 0;

    private String serviceName = null;

    private ORB orb = null;

    public GameServer(Game controller) {
        super();
        String args[] = new String[5];
        args[0] = "";
        args[1] = "-ORBInitialPort";
        args[2] = "1050";
        args[3] = "-ORBInitialHost";
        args[4] = "127.0.0.1";
        orb = ORB.init(args, null);
        POA rootpoa = null;
        try {
            rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        } catch (InvalidName e) {
            e.printStackTrace();
        }
        try {
            rootpoa.the_POAManager().activate();
        } catch (AdapterInactive e) {
            e.printStackTrace();
        }
        ControlServant servant = new ControlServant(controller);
        ControlPOATie tie = new ControlPOATie(servant, rootpoa);
        Control href = tie._this(orb);
        org.omg.CORBA.Object objRef = null;
        NamingContextExt ncRef = null;
        try {
            objRef = orb.resolve_initial_references("NameService");
            ncRef = NamingContextExtHelper.narrow(objRef);
        } catch (InvalidName e) {
            e.printStackTrace();
        }
        serviceName = "TicTacToeGameServer" + count;
        count++;
        NameComponent path[] = null;
        try {
            path = ncRef.to_name(serviceName);
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName e1) {
            e1.printStackTrace();
        }
        try {
            ncRef.rebind(path, href);
        } catch (NotFound e) {
            e.printStackTrace();
        } catch (CannotProceed e) {
            e.printStackTrace();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        orb.run();
    }

    public String getServiceName() {
        return serviceName;
    }
}
