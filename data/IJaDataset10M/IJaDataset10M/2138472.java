package ucalgary.ebe.samples;

import java.util.List;
import java.util.Vector;
import javax.swing.event.EventListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import ucalgary.ebe.ci.mice.cursor.DetectMice;
import ucalgary.ebe.ci.mice.cursor.MouseCursor;
import ucalgary.ebe.ci.mice.cursor.MouseCursorData;
import ucalgary.ebe.ci.mice.events.HandleMiceEvents;
import ucalgary.ebe.ci.mice.utils.MouseUtils;
import ucalgary.ebe.ci.mice.wrapper.ManyMouseWrapper;

/**
 * This class is a relict from the beginning (i think it can be deleted) //TODO
 * @author herbiga
 *
 */
public class SwtSample {

    static Display display = null;

    static SwtSample sample = new SwtSample();

    static Shell shell;

    static int counter = 0;

    static int counter2 = 0;

    static Button button1;

    private static int rate = 15;

    private static int eventRate = 100;

    static boolean develop = true;

    static int numberOfMice;

    static List<MouseCursor> cursors;

    static List<MouseCursorData> cursorData = new Vector<MouseCursorData>();

    int currentDevice = 0;

    private EventListenerList manyMouseListeners = new EventListenerList();

    public Shell open(Display display) {
        SwtSample.display = display;
        Shell shell = new Shell(display);
        shell.setText("Multiple Mice Demo");
        GridLayout layout = new GridLayout(1, true);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        shell.setLayout(layout);
        shell.pack();
        shell.open();
        return shell;
    }

    private Button createButton(Shell shell, String text, Point location, Point size) {
        Button button = new Button(shell, SWT.PUSH);
        button.setText(text);
        button.setLocation(location);
        button.setSize(size);
        return button;
    }

    public SwtSample() {
        super();
    }

    public static void main(String[] args) {
        display = new Display();
        shell = sample.open(display);
        shell.setSize(new Point(636, 276));
        button1 = sample.createButton(shell, "Press me", new Point(20, 20), new Point(50, 25));
        button1.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event e) {
                System.out.println(++counter + "############### Mouse Down (button: " + e.button + " x: " + e.x + " y: " + e.y + ")");
            }
        });
        cursors = new DetectMice().getMice(shell, display, null, null, null);
        numberOfMice = cursors.size();
        MouseCursor temp;
        for (int i = 0; i < numberOfMice; i++) {
            temp = cursors.get(i);
            cursorData.add(i, new MouseCursorData(temp.getMouseId(), temp.getMouseShellLocation(), temp.getRotateDelta()));
        }
        Point hideLocation = shell.getLocation();
        hideLocation.y = hideLocation.y + 100;
        hideLocation.x = hideLocation.x + 100;
        MouseUtils.hideSystemMouseInShell(display, shell, cursorData, hideLocation);
        MouseUtils.getScreenSize(display);
        ManyMouseWrapper.getInstance().setMiceData(cursorData);
        display.timerExec(rate, sample.new UpdateMiceCursor());
        display.timerExec(eventRate, sample.new HandleManyMouseEvents());
        shell.addListener(SWT.KeyDown, new Listener() {

            public void handleEvent(Event e) {
                if (shell.isFocusControl()) {
                    if (e.type == SWT.KeyDown && e.keyCode == 0x1B) {
                        shell.dispose();
                    }
                }
            }
        });
        shell.setActive();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatchCIF()) {
                display.sleep();
            }
        }
        for (int i = 0; i < numberOfMice; i++) {
            temp = cursors.get(i);
            temp.dispose();
        }
        ManyMouseWrapper.stop();
    }

    private class UpdateMiceCursor implements Runnable {

        public void run() {
            for (int i = 0; i < numberOfMice; i++) {
                cursors.get(i).setMouseShellLocation(cursorData.get(i).getMouseLocation());
                display.setCursorLocation(cursorData.get(i).getHideSystemMouseLocation());
            }
            display.timerExec(rate, this);
        }
    }

    private class HandleManyMouseEvents implements Runnable {

        public void run() {
            HandleMiceEvents.handleEvents(numberOfMice, cursorData, display, manyMouseListeners, shell, new Point(5, 25));
            display.timerExec(eventRate, this);
        }
    }
}
