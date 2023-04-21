package motudo.screen.domain;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import com.google.inject.Inject;

public class ProgressUpdateServiceImpl implements IWidgetUpdateService {

    private final INextTimerService ntimer;

    private static final int TIME_UPDATE = 3000;

    private int count;

    private Display display;

    @Inject
    public ProgressUpdateServiceImpl(INextTimerService aNtimer) {
        this.ntimer = aNtimer;
    }

    private void updateLabel(Label label) {
        int mins = ntimer.getNextMins();
        String result = String.format(" %dm", mins);
        label.setText(result);
    }

    @Override
    public void setTarget(Composite parent) {
        if (display == null) {
            parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
            RowLayout rowLayout = new RowLayout();
            rowLayout.marginTop = 0;
            rowLayout.marginLeft = 0;
            rowLayout.marginBottom = 0;
            parent.setLayout(rowLayout);
            final ProgressBar pbl = new ProgressBar(parent, SWT.NULL);
            pbl.setLayoutData(new RowData(50, 12));
            pbl.setMaximum(100);
            pbl.setMinimum(0);
            pbl.setSelection((int) ntimer.getProgressPercent());
            final Label label = new Label(parent, SWT.NULL);
            label.setLayoutData(new RowData(50, 12));
            label.setToolTipText("[Minutes]");
            label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
            label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
            updateLabel(label);
            display = Display.getCurrent();
            display.timerExec(TIME_UPDATE, new Runnable() {

                @Override
                public void run() {
                    count++;
                    pbl.setSelection((int) ntimer.getProgressPercent());
                    updateLabel(label);
                    Display.getCurrent().timerExec(TIME_UPDATE, this);
                }
            });
        }
    }
}
