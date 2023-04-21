package diuf.diva.hephaistk.undercity.agents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import diuf.diva.hephaistk.config.LoggingManager;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class OutputScreenBehaviour extends CyclicBehaviour {

    private static final long serialVersionUID = 5058776610212246293L;

    private ACLMessage msg = null;

    @Override
    public void action() {
        block();
        msg = myAgent.receive();
        if (msg != null) {
            String rfId = msg.getContent();
            if (rfId.equals("041523779e")) {
                try {
                    FileInputStream in = new FileInputStream(new File(""));
                    AdvancedPlayer player = new AdvancedPlayer(in);
                    player.play(5);
                } catch (JavaLayerException e) {
                    LoggingManager.getLogger().error(e);
                } catch (FileNotFoundException e) {
                    LoggingManager.getLogger().error(e);
                }
            }
        }
    }
}
