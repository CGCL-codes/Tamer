package de.engelhardt.jdso;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import de.engelhardt.jdso.control.DataControl;
import de.engelhardt.jdso.visual.Scope;

/**
 * @author Dieter
 *
 */
public class ScopeView extends ViewPart {

    public static final String ID = "de.engelhardt.mydsoapp.ScopeView";

    private Scope scope;

    private Button btStartStop;

    private Button acButton;

    private Button dcButton;

    private Button trNegButton;

    private Button trPosButton;

    private Label timeValue;

    private Scale scaleTime;

    private Label triggerValue;

    private Scale scaleTrigger;

    private Group levelGroup;

    private Scale scaleOffsetHigh;

    private Label offsetValueHigh;

    private Scale scaleOffsetLow;

    private Label offsetValueLow;

    private Image imStart;

    private Image imStop;

    private Text msg;

    private DataControl cntrl;

    /**
	 * 
	 */
    public ScopeView() {
        imStart = new Image(Display.getCurrent(), getClass().getResourceAsStream("/icons/resume_co.gif"));
        imStop = new Image(Display.getCurrent(), getClass().getResourceAsStream("/icons/terminate_co.gif"));
    }

    @Override
    public void createPartControl(Composite parent) {
        GridLayout gridLayout = new GridLayout(2, false);
        parent.setLayout(gridLayout);
        scope = new Scope(parent, SWT.BORDER);
        scope.setGridData();
        scope.initBackgroundScale();
        GridLayout grdLayout = new GridLayout(2, false);
        Composite composite1 = new Composite(parent, SWT.BORDER);
        GridData data = new GridData(SWT.BEGINNING, SWT.FILL, false, false);
        data.widthHint = 240;
        data.minimumWidth = 240;
        composite1.setLayoutData(data);
        GridLayout gridLayout1 = new GridLayout(2, false);
        composite1.setLayout(gridLayout1);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.horizontalSpan = 2;
        btStartStop = new Button(composite1, SWT.TOGGLE);
        btStartStop.setText("Start");
        btStartStop.setImage(imStart);
        btStartStop.setLayoutData(gridData);
        btStartStop.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                if (btStartStop.getSelection()) {
                    btStartStop.setText("Stop");
                    btStartStop.setImage(imStop);
                    cntrl.sendStart();
                } else {
                    btStartStop.setText("Start");
                    btStartStop.setImage(imStart);
                    cntrl.sendStop();
                }
            }
        });
        GridData gridData1 = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData1.horizontalSpan = 2;
        gridData1.verticalAlignment = SWT.CENTER;
        Group acdcGroup = new Group(composite1, SWT.BORDER);
        acdcGroup.setText("Spannung");
        acdcGroup.setLayoutData(gridData1);
        RowLayout rowLayout = new RowLayout();
        rowLayout.justify = true;
        acdcGroup.setLayout(rowLayout);
        acButton = new Button(acdcGroup, SWT.RADIO);
        acButton.setText("AC");
        acButton.setSelection(false);
        acButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                if (acButton.getSelection()) {
                    cntrl.sendACDC(false);
                }
            }
        });
        dcButton = new Button(acdcGroup, SWT.RADIO);
        dcButton.setText("DC");
        dcButton.setSelection(true);
        dcButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                if (dcButton.getSelection()) {
                    cntrl.sendACDC(true);
                }
            }
        });
        acdcGroup.pack();
        GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData2.horizontalSpan = 2;
        Group timeGroup = new Group(composite1, SWT.BORDER);
        timeGroup.setText("Time");
        timeGroup.setLayoutData(gridData2);
        timeGroup.setLayout(grdLayout);
        timeValue = new Label(timeGroup, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
        timeValue.setText("100�s");
        scaleTime = new Scale(timeGroup, SWT.HORIZONTAL | SWT.BORDER);
        scaleTime.setMaximum(15);
        scaleTime.setMinimum(1);
        scaleTime.setIncrement(1);
        scaleTime.setPageIncrement(1);
        scaleTime.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                int perspectiveValue = scaleTime.getSelection();
                switch(perspectiveValue) {
                    case 1:
                        timeValue.setText("100ns");
                        break;
                    case 2:
                        timeValue.setText("250ns");
                        break;
                    case 3:
                        timeValue.setText("500ns");
                        break;
                    case 4:
                        timeValue.setText("1�s");
                        break;
                    case 5:
                        timeValue.setText("2,5�s");
                        break;
                    case 6:
                        timeValue.setText("5�s");
                        break;
                    case 7:
                        timeValue.setText("10�s");
                        break;
                    case 8:
                        timeValue.setText("25�s");
                        break;
                    case 9:
                        timeValue.setText("50�s");
                        break;
                    case 10:
                        timeValue.setText("100�s");
                        break;
                    case 11:
                        timeValue.setText("250�s");
                        break;
                    case 12:
                        timeValue.setText("500�s");
                        break;
                    case 13:
                        timeValue.setText("1ms");
                        break;
                    case 14:
                        timeValue.setText("2,5ms");
                        break;
                    case 15:
                        timeValue.setText("5ms");
                        break;
                    default:
                        timeValue.setText("ERR");
                        break;
                }
                cntrl.sendTiming(perspectiveValue);
            }
        });
        scaleTime.setToolTipText("�ndert den Wert f�r das Timing");
        timeGroup.pack();
        GridData gridData5 = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData5.horizontalSpan = 2;
        gridData5.verticalAlignment = SWT.CENTER;
        Group trgGroup = new Group(composite1, SWT.BORDER);
        trgGroup.setText("Trigger-Flanke");
        trgGroup.setLayoutData(gridData5);
        trgGroup.setLayout(rowLayout);
        trNegButton = new Button(trgGroup, SWT.RADIO);
        trNegButton.setText("steigend");
        trNegButton.setSelection(true);
        trNegButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                if (acButton.getSelection()) {
                    cntrl.sendTriggerLevel(true);
                }
            }
        });
        trPosButton = new Button(trgGroup, SWT.RADIO);
        trPosButton.setText("fallend");
        trNegButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                if (acButton.getSelection()) {
                    cntrl.sendTriggerLevel(false);
                }
            }
        });
        GridData gridData3 = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData3.horizontalSpan = 2;
        Group triggerGroup = new Group(composite1, SWT.BORDER);
        triggerGroup.setText("Trigger");
        triggerGroup.setLayoutData(gridData3);
        triggerGroup.setLayout(grdLayout);
        triggerValue = new Label(triggerGroup, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
        triggerValue.setText("____0");
        scaleTrigger = new Scale(triggerGroup, SWT.BORDER);
        scaleTrigger.setMaximum(255);
        scaleTrigger.setMinimum(0);
        scaleTrigger.setIncrement(32);
        scaleTrigger.setPageIncrement(32);
        scaleTrigger.setSelection(128);
        scaleTrigger.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                int val = scaleTrigger.getSelection();
                triggerValue.setText(new Integer(val).toString());
                cntrl.sendTrigger(val);
            }
        });
        triggerGroup.pack();
        GridData gridData4 = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData4.horizontalSpan = 2;
        levelGroup = new Group(composite1, SWT.BORDER);
        levelGroup.setText("Offset: 128");
        levelGroup.setLayoutData(gridData4);
        levelGroup.setLayout(grdLayout);
        offsetValueHigh = new Label(levelGroup, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
        offsetValueHigh.setText("____8");
        scaleOffsetHigh = new Scale(levelGroup, SWT.BORDER);
        scaleOffsetHigh.setMaximum(15);
        scaleOffsetHigh.setMinimum(0);
        scaleOffsetHigh.setIncrement(1);
        scaleOffsetHigh.setPageIncrement(1);
        scaleOffsetHigh.setSelection(8);
        scaleOffsetHigh.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                offsetValueHigh.setText(new Integer(scaleOffsetHigh.getSelection()).toString());
                sendOffeset();
            }
        });
        offsetValueLow = new Label(levelGroup, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
        offsetValueLow.setText("____0");
        scaleOffsetLow = new Scale(levelGroup, SWT.BORDER);
        scaleOffsetLow.setMaximum(15);
        scaleOffsetLow.setMinimum(0);
        scaleOffsetLow.setIncrement(1);
        scaleOffsetLow.setPageIncrement(1);
        scaleOffsetLow.setSelection(0);
        scaleOffsetLow.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                offsetValueLow.setText(new Integer(scaleOffsetLow.getSelection()).toString());
                sendOffeset();
            }
        });
        msg = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        msg.setEditable(false);
        data = new GridData(SWT.END, SWT.END, false, false, 2, 1);
        data.widthHint = 736;
        data.heightHint = 70;
        data.minimumWidth = 640;
        data.minimumHeight = 70;
        msg.setLayoutData(data);
        cntrl = new DataControl(msg);
        cntrl.setScope(scope);
        cntrl.addMessage("Init Complete");
        cntrl.init();
    }

    @Override
    public void setFocus() {
    }

    private void sendOffeset() {
        int val = scaleOffsetHigh.getSelection() * 16 + scaleOffsetLow.getSelection();
        levelGroup.setText("Offset: " + new Integer(val).toString());
        cntrl.sendOffeset(val);
    }

    @Override
    public void dispose() {
        cntrl.dispose();
        btStartStop.dispose();
        acButton.dispose();
        btStartStop.dispose();
        dcButton.dispose();
        imStart.dispose();
        imStop.dispose();
        msg.dispose();
        offsetValueHigh.dispose();
        scaleOffsetHigh.dispose();
        scaleTime.dispose();
        scaleTrigger.dispose();
        timeValue.dispose();
        triggerValue.dispose();
        trNegButton.dispose();
        trPosButton.dispose();
        super.dispose();
    }
}
