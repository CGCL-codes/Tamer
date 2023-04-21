package manager.controllers;

import java.io.IOException;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class MasterIRCController extends PircBot {

    private SSLSocketFactory SSLFACTORY = null;

    private String SERVER = null;

    private int PORT = 6697;

    public MasterIRCController(String Server) {
        SERVER = Server;
    }

    public MasterIRCController(String Server, int Port) {
        SERVER = Server;
        PORT = Port;
    }

    public MasterIRCController(String Server, int Port, SSLSocketFactory SSLSockFact) throws SSLException {
        SERVER = Server;
        PORT = Port;
        SSLFACTORY = SSLSockFact;
        super.setSocketFactory(SSLFACTORY);
    }

    public void Init() throws NickAlreadyInUseException, IOException, IrcException {
        super.setVerbose(true);
        super.setName("TestBot");
        super.connect(SERVER, PORT, "RFjo4BDla8Vwp2WSx3");
    }

    @Override
    protected void onConnect() {
        System.out.println("Connected!");
    }
}
