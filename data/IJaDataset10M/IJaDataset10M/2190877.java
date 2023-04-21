package pl.edu.mimuw.mas.editor.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import pl.edu.mimuw.mas.bezier.ControlPoints;

/**
 * Implementacja widgetu do podnoszenia punktów kontrolnych.
 */
public class JackWidget extends Composite {

    private static final float DELTA_CONST = 0.1f;

    private Label label;

    private FloatScale scale;

    public JackWidget(Composite c, int mask) {
        super(c, mask);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginBottom = 0;
        layout.marginTop = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        this.setLayout(layout);
        label = new Label(this, SWT.NONE);
        label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
        scale = new FloatScale(this, SWT.NONE);
        scale.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent arg0) {
                updateLabel();
            }
        });
        scale.addMouseListener(new MouseListener() {

            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent e) {
            }

            @Override
            public void mouseUp(MouseEvent e) {
                float shift = scale.getFloatSelection();
                resetSelection();
                shiftControlPoints(shift);
            }
        });
        scale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
        resetSelection();
    }

    public void setControlPoints(ControlPoints cp) {
        int[] area = cp.getArea().getArea();
        int rowsCount = Math.abs(area[0] - area[2]);
        int columnsCount = Math.abs(area[1] - area[3]);
        float delta = DELTA_CONST * (rowsCount + columnsCount);
        scale.setFloatMaximum(delta);
        scale.setFloatMinimum(-delta);
    }

    public void updateLabel() {
        label.setText("Wysokość: " + String.format("%.2f", scale.getFloatSelection()));
        this.layout();
    }

    public void resetSelection() {
        setSelection(0f);
    }

    public void setSelection(float selection) {
        scale.setFloatSelection(selection);
        updateLabel();
    }

    private void shiftControlPoints(float shift) {
        Event e = new Event();
        e.data = new Float(shift);
        notifyListeners(SWT.Modify, e);
    }
}
