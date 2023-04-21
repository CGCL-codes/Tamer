package boogiepants.instruments;

import java.awt.AWTEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.awt.event.KeyEvent;
import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnBehaviorPost;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOr;
import boogiepants.display.KnowsSelected;
import boogiepants.util.Util;

public class KeyBehavior extends Behavior {

    private static int deleteKey;

    private WakeupCondition condition;

    private HashMap<Integer, KeyListener> map = new HashMap<Integer, KeyListener>();

    private InstrumentManager manager;

    public KeyBehavior(KnowsSelected knows) {
        manager = InstrumentManager.getInstance();
        if (Util.isMac()) {
            deleteKey = KeyEvent.VK_BACK_SPACE;
        } else {
            deleteKey = KeyEvent.VK_DELETE;
        }
        map.put(deleteKey, new DeleteListener(knows));
    }

    @Override
    public void initialize() {
        condition = new WakeupOr(new WakeupCriterion[] { new WakeupOnElapsedFrames(0), new WakeupOnAWTEvent(AWTEvent.KEY_EVENT_MASK) });
        wakeupOn(condition);
        System.out.println("initialized");
    }

    @Override
    public void processStimulus(Enumeration criteria) {
        if (manager.isEditMode()) {
            while (criteria.hasMoreElements()) {
                WakeupCriterion w = (WakeupCriterion) criteria.nextElement();
                if (w instanceof WakeupOnAWTEvent) {
                    KeyEvent kevt = (KeyEvent) ((WakeupOnAWTEvent) w).getAWTEvent()[0];
                    int kevtcode = kevt.getKeyCode();
                    int kevttype = kevt.getID();
                    System.out.println("key event code " + kevtcode + " == delete " + deleteKey + "?");
                    if (kevttype == KeyEvent.KEY_PRESSED && map.containsKey(kevtcode)) {
                        map.get(kevtcode).keyPressed(kevt);
                        kevt.consume();
                    }
                }
            }
        }
        wakeupOn(condition);
    }
}
