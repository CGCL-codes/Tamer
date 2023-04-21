package test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Label;

public class CropTest extends Shell {

    private Group cropGroup;

    private Spinner cropTop;

    private Spinner cropLeft;

    private Spinner cropRight;

    private Spinner cropBottom;

    /**
	 * Launch the application.
	 * @param args
	 */
    public static void main(String args[]) {
        try {
            Display display = Display.getDefault();
            CropTest shell = new CropTest(display);
            shell.open();
            shell.layout();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Create the shell.
	 * @param display
	 */
    public CropTest(Display display) {
        super(display, SWT.SHELL_TRIM);
        setLayout(new GridLayout(1, false));
        {
            Composite composite = new Composite(this, SWT.NONE);
            composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
            composite.setLayout(new GridLayout(1, false));
            {
                createCropGroup(composite);
            }
        }
        createContents();
    }

    public void createCropGroup(Composite composite) {
        cropGroup = new Group(composite, SWT.NONE);
        cropGroup.setText("Crop Options");
        cropGroup.setLayout(new GridLayout(3, false));
        cropGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        new Label(cropGroup, SWT.NONE);
        {
            Composite topComp = new Composite(cropGroup, SWT.NONE);
            topComp.setLayout(new GridLayout(2, false));
            {
                Label lblTop = new Label(topComp, SWT.NONE);
                lblTop.setText("Top:");
            }
            {
                cropTop = new Spinner(topComp, SWT.BORDER);
            }
        }
        new Label(cropGroup, SWT.NONE);
        {
            Composite leftComp = new Composite(cropGroup, SWT.NONE);
            leftComp.setLayout(new GridLayout(1, false));
            {
                Label left = new Label(leftComp, SWT.NONE);
                left.setText("Left:");
            }
            {
                cropLeft = new Spinner(leftComp, SWT.BORDER);
            }
        }
        {
            Composite screenComp = new Composite(cropGroup, SWT.NONE);
            screenComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
            screenComp.setLayout(new GridLayout(1, false));
        }
        {
            Composite rightComp = new Composite(cropGroup, SWT.NONE);
            rightComp.setLayout(new GridLayout(1, false));
            {
                Label lblRight = new Label(rightComp, SWT.NONE);
                lblRight.setText("Right:");
            }
            {
                cropRight = new Spinner(rightComp, SWT.BORDER);
            }
        }
        new Label(cropGroup, SWT.NONE);
        {
            Composite bottomComp = new Composite(cropGroup, SWT.NONE);
            bottomComp.setLayout(new GridLayout(2, false));
            {
                Label lblBottom = new Label(bottomComp, SWT.NONE);
                lblBottom.setText("Bottom:");
            }
            {
                cropBottom = new Spinner(bottomComp, SWT.BORDER);
            }
        }
        new Label(cropGroup, SWT.NONE);
    }

    /**
	 * Create contents of the shell.
	 */
    protected void createContents() {
        setText("SWT Application");
        setSize(312, 269);
    }

    @Override
    protected void checkSubclass() {
    }

    public Group getCrop() {
        return cropGroup;
    }
}
