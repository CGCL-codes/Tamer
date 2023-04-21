package ucalgary.ebe.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import ucalgary.ebe.ci.mice.MultipleMice;
import ucalgary.ebe.ci.mice.utils.MouseUtils;
import ucalgary.ebe.test.event.TestCIMouseListener;
import ucalgary.ebe.test.event.TestCIMouseMoveListener;

/**
 * This is the demo application how to use the Concurrent Imput Framework 
 * @author herbiga
 *
 */
public class CIListenerTest {

    private static Shell sShell = null;

    public static void main(String[] args) {
        Display display = Display.getDefault();
        CIListenerTest thisClass = new CIListenerTest();
        thisClass.createSShell();
        CIListenerTest.sShell.open();
        MultipleMice cif = MultipleMice.getInstance(display, sShell, false);
        cif.addCIMouseListener(new TestCIMouseListener());
        cif.addCIMouseMoveListener(new TestCIMouseMoveListener());
        String[] cursorTexts = { "Sascha", "Andy" };
        RGB[] cursorColors = { MouseUtils.CC_YELLOW, MouseUtils.CC_BLUE };
        int[] rotateDirections = { SWT.LEFT, SWT.DOWN };
        cif.start(100, 100, cursorTexts, cursorColors, rotateDirections);
        sShell.setActive();
        while (!CIListenerTest.sShell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        cif.stop();
    }

    /**
	 * This method initializes sShell
	 */
    private void createSShell() {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.verticalAlignment = GridData.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.verticalSpacing = 2;
        gridLayout.horizontalSpacing = 2;
        sShell = new Shell();
        sShell.setText("MultipleMouseDemo");
        sShell.setLayout(gridLayout);
        sShell.setSize(new Point(636, 276));
        Button button = new Button(sShell, SWT.NONE);
        button.setText("Click me :o)");
        button.setLayoutData(gridData);
        button.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event e) {
                System.out.println("Event arrived");
            }
        });
        Button button2 = new Button(sShell, SWT.NONE);
        button2.setText("EXIT");
        button2.setLayoutData(gridData);
        button2.addListener(SWT.MouseUp, new Listener() {

            public void handleEvent(Event e) {
                System.exit(1);
            }
        });
    }
}
