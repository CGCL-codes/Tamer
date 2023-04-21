package org.jdeluxe.testing;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.custom.*;

/**
 * The Class Test.
 */
public class Test {

    /** The display. */
    private Display display;

    /** The shell. */
    private Shell shell;

    /** The shell layout. */
    private FormLayout shellLayout;

    /** The composite scroller. */
    private ScrolledComposite compositeScroller;

    /** The data scroller. */
    private FormData dataScroller;

    /** The composite main. */
    private Composite compositeMain;

    /** The layout main. */
    private FormLayout layoutMain;

    /** The composite inner. */
    private Composite compositeInner;

    /** The layout inner. */
    private FormLayout layoutInner;

    /** The data inner. */
    private FormData dataInner;

    /** The label1. */
    private Label label1;

    /** The data label1. */
    private FormData dataLabel1;

    /** The label2. */
    private Label label2;

    /** The data label2. */
    private FormData dataLabel2;

    /** The label3. */
    private Label label3;

    /** The data label3. */
    private FormData dataLabel3;

    /**
	 * Instantiates a new test.
	 */
    public Test() {
        display = new Display();
        Color background = new Color(display, 204, 204, 255);
        shell = new Shell(display);
        shell.setSize(640, 480);
        shellLayout = new FormLayout();
        shellLayout.marginHeight = 5;
        shellLayout.marginWidth = 5;
        shell.setLayout(shellLayout);
        compositeScroller = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.BORDER);
        compositeMain = new Composite(compositeScroller, SWT.NONE);
        compositeScroller.setContent(compositeMain);
        layoutMain = new FormLayout();
        layoutMain.marginHeight = 0;
        layoutMain.marginWidth = 0;
        compositeMain.setLayout(layoutMain);
        dataScroller = new FormData();
        dataScroller.top = new FormAttachment(0, 5);
        dataScroller.left = new FormAttachment(0, 5);
        dataScroller.right = new FormAttachment(35, -5);
        dataScroller.bottom = new FormAttachment(100, -5);
        compositeScroller.setLayoutData(dataScroller);
        label1 = new Label(compositeMain, SWT.WRAP | SWT.BORDER);
        label1.setText("1. This is a test.");
        dataLabel1 = new FormData();
        dataLabel1.top = new FormAttachment(0, 5);
        dataLabel1.left = new FormAttachment(0, 5);
        label1.setLayoutData(dataLabel1);
        compositeInner = new Composite(compositeMain, SWT.BORDER);
        compositeInner.setBackground(background);
        layoutInner = new FormLayout();
        layoutInner.marginHeight = 0;
        layoutInner.marginWidth = 0;
        compositeInner.setLayout(layoutInner);
        dataInner = new FormData();
        dataInner.top = new FormAttachment(label1, 5);
        dataInner.left = new FormAttachment(0, 5);
        compositeInner.setLayoutData(dataInner);
        label2 = new Label(compositeInner, SWT.WRAP | SWT.BORDER);
        label2.setText("2. This is a test. This is a test. This is a test. This is" + "a test. This is a test. This is a test.");
        label2.setBackground(background);
        dataLabel2 = new FormData();
        dataLabel2.top = new FormAttachment(0, 5);
        dataLabel2.left = new FormAttachment(0, 5);
        dataLabel2.bottom = new FormAttachment(100, -5);
        label2.setLayoutData(dataLabel2);
        label3 = new Label(compositeMain, SWT.WRAP | SWT.BORDER);
        label3.setText("3. This is a test. This is a test. This is a test. This is" + "a test. This is a test. This is a test.");
        dataLabel3 = new FormData();
        dataLabel3.top = new FormAttachment(compositeInner, 5);
        dataLabel3.left = new FormAttachment(0, 5);
        dataLabel3.bottom = new FormAttachment(100, -5);
        label3.setLayoutData(dataLabel3);
        compositeScroller.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event e) {
                int width = compositeScroller.getClientArea().width - 10;
                Control[] children = compositeMain.getChildren();
                for (int i = 0; i < children.length; i++) {
                    FormData data = (FormData) children[i].getLayoutData();
                    data.width = width;
                    if (children[i] instanceof Composite) {
                        Composite c = (Composite) children[i];
                        int width2 = c.getClientArea().width - 10;
                        Control[] children2 = c.getChildren();
                        for (int j = 0; j < children2.length; j++) {
                            data = (FormData) children2[j].getLayoutData();
                            data.width = width2;
                        }
                    }
                }
                compositeMain.setSize(compositeMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        background.dispose();
        display.dispose();
    }

    /**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
    public static void main(String[] args) {
        Test test = new Test();
    }
}
