package org.mtmi.ui.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.mtmi.midi.MidiManager;
import org.mtmi.tuio.TUIOManager;
import org.mtmi.ui.MouseInteractionManager;
import org.mtmi.ui.scenes.ColorManager;
import org.mtmi.ui.scenes.FontManager;
import org.mtmi.ui.scenes.DemoScene;
import org.mtmi.ui.scenes.MidiScene;

public class Main {

    static int[] circle(int r, int offsetX, int offsetY) {
        int[] polygon = new int[8 * r + 4];
        for (int i = 0; i < 2 * r + 1; i++) {
            int x = i - r;
            int y = (int) Math.sqrt(r * r - x * x);
            polygon[2 * i] = offsetX + x;
            polygon[2 * i + 1] = offsetY + y;
            polygon[8 * r - 2 * i - 2] = offsetX + x;
            polygon[8 * r - 2 * i - 1] = offsetY - y;
        }
        return polygon;
    }

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        shell.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
        DemoScene scene = new DemoScene(shell);
        MouseInteractionManager mia = new MouseInteractionManager(scene);
        scene.addMouseListener(mia);
        TUIOManager mainTUIOManager = TUIOManager.getInstance();
        mainTUIOManager.init(scene);
        MidiManager midiManager = MidiManager.getInstance();
        midiManager.init();
        shell.addListener(SWT.KeyDown, new Listener() {

            public void handleEvent(Event e) {
                if (e.character == SWT.ESC) {
                    shell.dispose();
                }
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        ColorManager.getInstance().dispose();
        FontManager.getInstance().dispose();
        mainTUIOManager.dispose();
        MidiManager.getInstance().dispose();
        display.dispose();
    }
}
