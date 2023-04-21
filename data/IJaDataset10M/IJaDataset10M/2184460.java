package rsm2b.mouse;

import java.util.ArrayList;

/**
 * Manage the multiple mouse.
 * It can occurs that if an object takes the focus, this class does not listen to the mice anymore.
 *
 */
public class MouseManager extends Thread {

    int nbMice = 0;

    boolean initialized = false;

    public MouseManager() {
        start();
        while (!initialized) {
        }
    }

    Mouse[] mouseArray = new Mouse[10];

    public Mouse[] getMouseArray() {
        return mouseArray;
    }

    public ArrayList<Mouse> getMouseList() {
        ArrayList<Mouse> mouseList = new ArrayList<Mouse>();
        for (Mouse mouse : mouseArray) {
            if (mouse != null) mouseList.add(mouse);
        }
        return mouseList;
    }

    @Override
    public void run() {
        nbMice = ManyMouse.Init();
        System.out.println("ManyMouse.Init() reported " + nbMice + ".");
        for (int i = 0; i < nbMice; i++) {
            System.out.println("Mouse #" + i + ": " + ManyMouse.DeviceName(i));
            System.out.println();
            mouseArray[i] = new Mouse();
        }
        ManyMouseEvent event = new ManyMouseEvent();
        initialized = true;
        for (; ; ) {
            if (!ManyMouse.PollEvent(event)) try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } else {
                switch(event.type) {
                    case ManyMouseEvent.RELMOTION:
                        if (event.item == 0) {
                            mouseArray[event.device].moveRelative(event.value, 0);
                        }
                        if (event.item == 1) {
                            mouseArray[event.device].moveRelative(0, event.value);
                        }
                        break;
                    case ManyMouseEvent.BUTTON:
                        boolean pressed = (event.value == 0) ? false : true;
                        mouseArray[event.device].setButtonState(event.item, pressed);
                        break;
                    case ManyMouseEvent.SCROLL:
                        if (event.item == 0) {
                            if (event.value > 0) mouseArray[event.device].mouseWheelUp(); else mouseArray[event.device].mouseWheelDown();
                        } else {
                            if (event.value > 0) mouseArray[event.device].mouseWheelRight(); else mouseArray[event.device].mouseWheelLeft();
                        }
                        break;
                    case ManyMouseEvent.DISCONNECT:
                        mouseArray[event.device].setConnected(false);
                        System.out.print("disconnect");
                        break;
                }
            }
        }
    }
}
