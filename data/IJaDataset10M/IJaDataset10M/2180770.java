package pl.edu.mimuw.mas.editor.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.mimuw.mas.bezier.ControlPoints;
import pl.edu.mimuw.mas.chart.Area;

/**
 * Implementacja widgetu do obsługi siatki kontrolnej.
 */
public class ControlPointsWidget extends Composite {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final int SPINNER_MIN = 3;

    private static final int SPINNER_MAX = 20;

    private static final int SPINNER_DEFAULT = 5;

    private Composite up;

    private ControlPointsCanvas canvas;

    private ControlPoints cp;

    private Area selection;

    private Spinner rowsSpinner;

    private Spinner columnsSpinner;

    private Label rowsSpinnerLabel;

    private Label columnsSpinnerLabel;

    private Label separator;

    private Label info;

    private Button createButton;

    private Button clearButton;

    public ControlPointsWidget(Composite arg0, int arg1) {
        super(arg0, arg1);
        cp = null;
        this.setLayout(new GridLayout());
        up = new Composite(this, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.marginLeft = 30;
        up.setLayout(gridLayout);
        rowsSpinnerLabel = new Label(up, SWT.NONE);
        rowsSpinnerLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        rowsSpinnerLabel.setText("Wiersze:");
        rowsSpinner = new Spinner(up, SWT.BORDER);
        rowsSpinner.setMinimum(SPINNER_MIN);
        rowsSpinner.setMaximum(SPINNER_MAX);
        rowsSpinner.setSelection(SPINNER_DEFAULT);
        new Label(up, SWT.NONE);
        columnsSpinnerLabel = new Label(up, SWT.NONE);
        columnsSpinnerLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        columnsSpinnerLabel.setText("Kolumny:");
        columnsSpinner = new Spinner(up, SWT.BORDER);
        columnsSpinner.setMinimum(SPINNER_MIN);
        columnsSpinner.setMaximum(SPINNER_MAX);
        columnsSpinner.setSelection(SPINNER_DEFAULT);
        createButton = new Button(up, SWT.NONE);
        createButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));
        createButton.setText("Stwórz nową siatkę!");
        createButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                createControlPoints();
            }
        });
        separator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        clearButton = new Button(this, SWT.NONE);
        clearButton.setText("Odznacz wszystkie punkty");
        clearButton.setVisible(false);
        clearButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                unselectAll();
            }
        });
        info = new Label(this, SWT.NONE);
        canvas = new ControlPointsCanvas(this, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        canvas.setVisible(false);
        canvas.addListener(SWT.Modify | SWT.DefaultSelection, new Listener() {

            @Override
            public void handleEvent(Event e) {
                selectArea((Area) e.data, true);
                canvas.redraw();
            }
        });
        canvas.addListener(SWT.Modify, new Listener() {

            @Override
            public void handleEvent(Event e) {
                selectArea((Area) e.data, false);
                canvas.redraw();
            }
        });
    }

    public void setSelection(Area selection) {
        this.selection = selection;
        clearButton.setVisible(false);
        canvas.setVisible(false);
    }

    private void createControlPoints() {
        int rows = rowsSpinner.getSelection();
        int columns = columnsSpinner.getSelection();
        log.info("Tworzę nową siatkę punktów kontrolnych z " + rows + " wierszami i " + columns + " kolumnami, dla terenu " + "((" + selection.getArea()[0] + ", " + selection.getArea()[1] + "), " + "(" + selection.getArea()[2] + ", " + selection.getArea()[3] + "))");
        cp = new ControlPoints(columns, rows, selection);
        canvas.setControlPoints(cp);
        canvas.redraw();
        info.setText(cp.getInfo());
        clearButton.setVisible(true);
        canvas.setVisible(true);
        Event e = new Event();
        e.data = cp;
        notifyListeners(SWT.Modify, e);
    }

    private void unselectAll() {
        int col = (int) cp.getColumnsCount();
        int row = (int) cp.getRowsCount();
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                cp.unselect(i, j);
            }
        }
        canvas.redraw();
    }

    private void selectArea(Area a, boolean eraseOld) {
        if (eraseOld) {
            cp.selectNone();
        }
        for (int i = a.getArea()[0]; i <= a.getArea()[2]; i++) {
            for (int j = a.getArea()[1]; j <= a.getArea()[3]; j++) {
                cp.toggle(i, j);
            }
        }
    }
}
