package basic;

import alice.cartago.*;
import alice.simpa.*;

public class Receiver extends Agent {

    @ACTIVITY
    void main() {
        try {
            SensorId sid = getSensor("s0");
            listen(sid);
            log("listening...");
            Perception p = sense(sid, "new_msg", 10000);
            Msg msg = (Msg) p.getContent(0);
            log(">> " + msg.getContentAsString());
            tell(msg.getSenderId(), new Msg("could be better."), msg.getId(), 500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
