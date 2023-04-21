package alice.cartago.examples.roomsworld;

import alice.cartago.*;

/**
 *
 * @author michelepiunti
 */
@ARTIFACT_MANUAL(outports = { @OUTPORT(name = "link") })
public class roomsConsole extends Artifact {

    /** Creates a new instance of console */
    public roomsConsole() {
    }

    @OPERATION
    void print(Object msg) {
        try {
            triggerOp("link", new Op("print", "" + msg));
        } catch (Exception ex) {
            log("linkop print mst to GUI failed.");
        }
    }

    @OPERATION
    void print(Object msg1, Object msg2) {
        String msg = "" + msg1 + msg2;
        try {
            triggerOp("link", new Op("print", msg));
        } catch (Exception ex) {
            log("linkop print mst to GUI failed.");
        }
    }

    @OPERATION
    void print(Object msg1, Object msg2, Object msg3) {
        String msg = "" + msg1 + msg2 + msg3;
        try {
            triggerOp("link", new Op("print", msg));
        } catch (Exception ex) {
            log("linkop print mst to GUI failed.");
        }
    }

    @OPERATION
    void err(String msg) {
        System.err.println(msg);
    }
}
