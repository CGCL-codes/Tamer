package ui.quicksettings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import ui.navigation.XYZDisplayComp;
import ui.opengl.GLView;
import backend.global.AvoGlobal;

public class QuickSettings extends Composite {

    static Combo cSnap;

    static Combo dSnap;

    public QuickSettings(Composite parent, int style) {
        super(parent, style);
        this.setLayout(new RowLayout(SWT.HORIZONTAL));
        FontData fd = new FontData();
        fd.setHeight(8);
        fd.setName("Verdana");
        Font f = new Font(this.getDisplay(), fd);
        Label lUnits = new Label(this, SWT.BOLD);
        lUnits.setFont(f);
        lUnits.setText(" Units:");
        Combo cUnits = new Combo(this, SWT.READ_ONLY);
        cUnits.setItems(new String[] { "inches", "mils", "meters", "mm" });
        cUnits.setFont(f);
        cUnits.select(2);
        cUnits.setBackground(this.getBackground());
        Label lSnap = new Label(this, SWT.BOLD);
        lSnap.setFont(f);
        lSnap.setText(" Snap:");
        cSnap = new Combo(this, SWT.READ_ONLY);
        cSnap.setItems(new String[] { "Off", "0.25x", "0.5x", "1.0x" });
        cSnap.setFont(f);
        cSnap.select(2);
        cSnap.setBackground(this.getBackground());
        cSnap.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                int selIndex = cSnap.getSelectionIndex();
                switch(selIndex) {
                    case (0):
                        {
                            AvoGlobal.snapEnabled = false;
                            break;
                        }
                    case (1):
                        {
                            AvoGlobal.snapEnabled = true;
                            AvoGlobal.snapSize = 0.25;
                            break;
                        }
                    case (2):
                        {
                            AvoGlobal.snapEnabled = true;
                            AvoGlobal.snapSize = 0.5;
                            break;
                        }
                    case (3):
                        {
                            AvoGlobal.snapEnabled = true;
                            AvoGlobal.snapSize = 1.0;
                            break;
                        }
                    default:
                        {
                            System.out.println("no snap selected in quickSettings?? ignoring change.");
                            break;
                        }
                }
            }
        });
        Label lDebug = new Label(this, SWT.BOLD);
        lDebug.setFont(f);
        lDebug.setText(" View:");
        dSnap = new Combo(this, SWT.READ_ONLY);
        dSnap.setItems(new String[] { "Normal", "Debug" });
        dSnap.setFont(f);
        dSnap.select(0);
        dSnap.setBackground(this.getBackground());
        dSnap.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                int selIndex = dSnap.getSelectionIndex();
                switch(selIndex) {
                    case (0):
                        {
                            AvoGlobal.DEBUG_MODE = false;
                            AvoGlobal.glView.updateGLView = true;
                            break;
                        }
                    case (1):
                        {
                            AvoGlobal.DEBUG_MODE = true;
                            AvoGlobal.glView.updateGLView = true;
                            break;
                        }
                    default:
                        {
                            System.out.println("no view selected in quickSettings?? ignoring change.");
                            break;
                        }
                }
            }
        });
    }
}
