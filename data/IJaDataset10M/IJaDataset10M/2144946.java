package ebe.manymouse.events;

import java.util.List;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Display;
import ebe.manymouse.cursor.MouseCursorData;
import ebe.manymouse.jni.ManyMouseEvent;
import ebe.manymouse.utils.MouseUtils;

/**
 * This class is responsible to read and handle the mice events. It is called through the HandleManyMouseEvents
 * thread
 * @author herbiga
 * @see ebe.manymouse.MultipleMice
 * @see ebe.manymouse.MultipleMice.HandleManyMouseEvents
 */
public class HandleMiceEvents {

    /**
	 * Reads the event queues and handle this event
	 * @param numberOfMice
	 * @param cursorData
	 * @param display
	 */
    public static void handleEvents(int numberOfMice, List<MouseCursorData> cursorData, Display display) {
        int handle;
        int message;
        int wParam;
        int lParam;
        int mouse_x, mouse_y;
        ManyMouseEvent mouseEvent;
        for (int i = 0; i < numberOfMice; i++) {
            mouseEvent = cursorData.get(i).getNextEvent();
            if (mouseEvent == null) {
                continue;
            } else {
                mouse_x = cursorData.get(i).getMouseLocation().x;
                mouse_y = cursorData.get(i).getMouseLocation().y;
                handle = MouseUtils.getHandle(display, mouse_x, mouse_y);
                if (handle == -1) continue;
                switch(mouseEvent.type) {
                    case ManyMouseEvent.BUTTON:
                        lParam = mouse_y;
                        lParam = lParam << 16;
                        lParam = lParam + mouse_x;
                        if (mouseEvent.value == 0) {
                            wParam = 0;
                            message = OS.WM_LBUTTONUP;
                        } else {
                            wParam = 1;
                            message = OS.WM_LBUTTONDOWN;
                        }
                        OS.SendMessage(handle, message, wParam, lParam);
                        sendEventsToWidget(handle, message, lParam, display);
                        break;
                    default:
                        System.out.println("Unknown event!");
                        break;
                }
            }
        }
    }

    /**
	 * Sends the system events to the OS (depending on the widget) 
	 * @param handle
	 * @param mouseClickType
	 * @param lParam
	 * @param display
	 */
    private static void sendEventsToWidget(int handle, int mouseClickType, int lParam, Display display) {
        System.out.println("sendEventsCall");
        switch(MouseUtils.getWidgetType(display, handle)) {
            case MouseUtils.BUTTON_WIDGET:
                if (mouseClickType == OS.WM_LBUTTONDOWN) {
                    OS.SendMessage(handle, MouseUtils.BM_SETSTATE, 1, 0);
                } else if (mouseClickType == OS.WM_LBUTTONUP) {
                    OS.SendMessage(handle, MouseUtils.BM_SETSTATE, 0, 0);
                } else {
                    System.out.println("wroooooong");
                }
                break;
            case MouseUtils.SHELL_WIDGET:
                System.out.println("recognized shell");
                break;
            default:
                System.out.println("wrong");
                break;
        }
    }
}
