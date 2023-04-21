package abbot.swt.examples;

import java.util.Formatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This is the sample app for the demo described at
 * <url>http://www.sys-con.com/java/articleprint.cfm?id=1940</url> that
 * provides a means to convert Celsius temperatures to Fahrenheit.
 * 
 * @author Gary Johnston
 * @author Kevin Dale
 */
public class CelsiusConverter {

    public static final String SHELL_TITLE = "°C to °F";

    public static final String PRECISION_ITEM_TEXT = "Precision";

    public static final String BUTTON_TEXT = "Convert";

    public static final String CELSIUS_LABEL = "Celsius:";

    public static final String FAHRENHEIT_LABEL = "Fahrenheit:";

    public static final String ERROR = "**error**";

    private static final int PRECISION_MAX = 4;

    private double celsius;

    private int precision;

    private Shell shell;

    private Text celsiusText;

    private Label fahrenheitLabel;

    /**
	 * Constructs a {@link CelsiusConverter}.
	 */
    public CelsiusConverter(Display display) {
        createShell(display);
        createMenuBar();
        createContents();
        update();
    }

    public void open() {
        shell.pack();
        shell.open();
    }

    public void close() {
        if (shell != null && !shell.isDisposed()) {
            shell.close();
            shell = null;
        }
    }

    private void createShell(Display display) {
        shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setLayout(new GridLayout(2, false));
        shell.setText(SHELL_TITLE);
    }

    private void createMenuBar() {
        Menu bar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(bar);
        MenuItem precisionItem = new MenuItem(bar, SWT.CASCADE);
        precisionItem.setText(PRECISION_ITEM_TEXT);
        Menu precisionMenu = new Menu(precisionItem);
        precisionItem.setMenu(precisionMenu);
        SelectionListener listener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                precision = ((Integer) event.widget.getData()).intValue();
                update();
            }
        };
        for (int i = 0; i <= PRECISION_MAX; i++) {
            MenuItem item = new MenuItem(precisionMenu, SWT.RADIO);
            item.setText(Integer.toString(i));
            item.setData(Integer.valueOf(i));
            item.setSelection(i == precision);
            item.addSelectionListener(listener);
        }
    }

    private void createContents() {
        Label label = new Label(shell, SWT.NONE);
        label.setText(CELSIUS_LABEL);
        celsiusText = new Text(shell, SWT.BORDER);
        GridData data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        data.minimumWidth = getWidth(celsiusText);
        celsiusText.setLayoutData(data);
        celsiusText.setText(format(celsius, false));
        celsiusText.setData(CELSIUS_LABEL);
        label = new Label(shell, SWT.NONE);
        label.setText(FAHRENHEIT_LABEL);
        fahrenheitLabel = new Label(shell, SWT.None);
        fahrenheitLabel.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        fahrenheitLabel.setData(FAHRENHEIT_LABEL);
        Button button = new Button(shell, SWT.PUSH);
        button.setText(BUTTON_TEXT);
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                update();
            }
        });
    }

    private int getWidth(Control control) {
        Point extent = null;
        GC gc = new GC(control);
        try {
            gc.setFont(control.getFont());
            extent = gc.textExtent("-9999.9999°");
        } finally {
            gc.dispose();
        }
        if (extent != null) return extent.x;
        return SWT.DEFAULT;
    }

    private void update() {
        try {
            celsius = Double.parseDouble(celsiusText.getText());
            double fahrenheit = 9.0 * celsius / 5.0 + 32.0;
            fahrenheitLabel.setText(format(fahrenheit));
        } catch (NumberFormatException ignored) {
            celsius = Double.NaN;
            fahrenheitLabel.setText(ERROR);
        }
    }

    private String format(double value) {
        return format(value, true);
    }

    private String format(double value, boolean decorate) {
        StringBuilder builder = new StringBuilder();
        Formatter formatter = new Formatter(builder);
        formatter.format("%%.%df", precision);
        String format = builder.toString();
        builder.setLength(0);
        formatter.format(format, value);
        if (decorate) builder.append('°');
        return builder.toString();
    }

    public static void main(String[] argv) {
        Display display = Display.getDefault();
        CelsiusConverter converter = new CelsiusConverter(display);
        converter.open();
        while (!converter.shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.close();
    }
}
