package techniques.zoom;

import java.net.SocketException;
import java.util.Date;
import fr.inria.zuist.cluster.viewer.Viewer;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

public class Tilt extends AbstractZoomTechnique {

    public static final String IN_CMD_ZOOM = "zoom";

    public static final String IN_CMD_ZORIG_ENABLED = "zoe";

    public static final String IN_CMD_ZORIG_DISABLED = "zod";

    public static final String IN_CMD_ZORIG = "zo";

    public static final String IN_CMD_GGV = "ggv";

    public static final String CMD_STOP = "stop";

    public static final String IN_CMD_STOP = CMD_STOP;

    protected OSCPortIn dataReceiver;

    protected OSCListener VICONListener;

    public Tilt(String id, ORDER o, int portIn) {
        super(id, o, false);
        try {
            this.dataReceiver = new OSCPortIn(portIn);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("Tilt object created");
    }

    @Override
    public void initListeners() {
        VICONListener = new OSCListener() {

            public void acceptMessage(Date date, OSCMessage msg) {
                Object[] parts = msg.getArguments();
                String cmd = (String) parts[0];
                if (cmd.equals(IN_CMD_ZOOM)) {
                    if (order.equals(ORDER.FIRST)) {
                        System.out.println("zoom1 " + ((Float) parts[1]).floatValue());
                        addAltitudeSample();
                    } else if (order.equals(ORDER.ZERO)) {
                        Viewer.getInstance().zeroOrderZoom(((Float) parts[1]).floatValue());
                        System.out.println("zoom0 " + ((Float) parts[1]).floatValue());
                        addAltitudeSample();
                    }
                } else if (cmd.equals(IN_CMD_ZORIG)) {
                    System.out.println("set zom origin " + ((Integer) parts[1]).intValue() + " " + ((Integer) parts[2]).intValue());
                } else if (cmd.equals(IN_CMD_STOP)) {
                    System.out.println("stop");
                } else if (cmd.equals(IN_CMD_ZORIG_ENABLED)) {
                    System.out.println("Mode: zoom to origin");
                } else if (cmd.equals(IN_CMD_ZORIG_DISABLED)) {
                    System.out.println("Mode: zoom to center of screen");
                } else if (cmd.equals(IN_CMD_GGV)) {
                    System.out.println("ggv");
                }
            }
        };
        dataReceiver.addListener(Viewer.MOVE_CAMERA, VICONListener);
        System.out.println("Listeners initialized");
    }

    @Override
    public void startListening() {
        dataReceiver.startListening();
        System.out.println("Listeners listening");
    }

    @Override
    public void stopListening() {
        dataReceiver.stopListening();
        dataReceiver.close();
    }

    @Override
    public void close() {
        dataReceiver.close();
    }
}
