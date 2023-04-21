package com.richclientgui.toolbox.samples.progressIndicator;

import static com.richclientgui.toolbox.samples.images.SampleToolBoxImageRegistry.getImage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import com.richclientgui.toolbox.progressIndicator.ImageSequencer;
import com.richclientgui.toolbox.samples.images.SampleToolBoxImageRegistry;

public class ImageSequencerDemo {

    static boolean toggle = true;

    static double current = 0;

    static int duration = 1000;

    static int i = 0;

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = getShell(display);
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public static Shell getShell(Display display) {
        final Shell shell = new Shell(display);
        shell.setText("Progress indicators");
        shell.setLayout(new GridLayout());
        new ImageSequencerDemo(shell);
        shell.setLocation(Display.getDefault().getBounds().width / 2 - 100, Display.getDefault().getBounds().height / 2 - 50);
        shell.open();
        shell.pack();
        shell.setSize(230, 300);
        return shell;
    }

    public ImageSequencerDemo(Shell chell) {
        chell.setLocation(200, 200);
        final Composite comp = new Composite(chell, SWT.NONE);
        comp.setLayout(new GridLayout(4, true));
        comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        comp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        final ImageSequencer seq = new ImageSequencer(comp, SWT.NONE, new Image[] { getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_A_1), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_A_2), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_A_3), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_A_4), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_A_5), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_A_6), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_A_7), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_A_8) }, 250, true);
        seq.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
        seq.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        final ImageSequencer seqB = new ImageSequencer(comp, SWT.NONE, new Image[] { getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_B_1), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_B_2), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_B_3), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_B_4), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_B_5), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_B_6), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_B_7), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_B_8) }, 250, true);
        seqB.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
        seqB.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        final ImageSequencer seqD = new ImageSequencer(comp, SWT.NONE, new Image[] { getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_1), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_2), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_3), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_4), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_5), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_6), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_7), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_8), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_9), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_10), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_11), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_D_12) }, 150, true);
        seqD.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
        seqD.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        final ImageSequencer seqC = new ImageSequencer(comp, SWT.NONE, new Image[] { getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_C_1), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_C_2), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_C_3), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_C_4), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_C_5), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_C_6), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_C_7), getImage(SampleToolBoxImageRegistry.IMG_INDICATOR_C_8) }, 150, true);
        seqC.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
        seqC.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        final Label vert = new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL);
        final GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gd.horizontalSpan = 4;
        vert.setLayoutData(gd);
        final Composite buttonPad = new Composite(comp, SWT.NONE);
        final GridData gdPad = new GridData(SWT.FILL, SWT.FILL, true, true);
        gdPad.horizontalSpan = 4;
        buttonPad.setLayoutData(gdPad);
        buttonPad.setLayout(new GridLayout(3, false));
        buttonPad.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        final Button g = new Button(buttonPad, SWT.PUSH);
        g.setText("Stop");
        g.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                seq.stopSequence();
                seqB.stopSequence();
                seqC.stopSequence();
                seqD.stopSequence();
            }
        });
        final Button gs = new Button(buttonPad, SWT.PUSH);
        gs.setText("Start");
        gs.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                seq.startSequence();
                seqB.startSequence();
                seqC.startSequence();
                seqD.startSequence();
            }
        });
        final Button gs1 = new Button(buttonPad, SWT.PUSH);
        gs1.setText("Reset");
        gs1.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                seq.resetSequence();
                seqB.resetSequence();
                seqC.resetSequence();
                seqD.resetSequence();
            }
        });
    }
}
