package my.application.ui;

import my.application.constants.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author mgeisinger
 *
 */
public class UITest {

    private Display display;

    private Shell shell;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new UITest();
    }

    public UITest() {
        display = new Display();
        shell = new Shell(display);
        shell.setText(Constants.WINDOW_MAIN_CAPTION);
        shell.setSize(Constants.WINDOW_MAIN_WIDTH, Constants.WINDOW_MAIN_HEIGHT);
        createGUI();
        shell.setLayout(new FillLayout());
        Composite composite = new Composite(shell, SWT.NONE);
        RowLayout rl = new RowLayout();
        composite.setLayout(rl);
        RowData data = new RowData();
        data.height = NumbersPanel.height;
        data.width = NumbersPanel.width;
        for (int i = 1; i < 7; i++) {
            NumbersPanel panel = new NumbersPanel(composite, SWT.BORDER, 1 + ((i - 1) * 2));
            panel.setLayoutData(data);
        }
        data = new RowData();
        data.height = NumbersPanel.height;
        data.width = NumbersPanel.width;
        for (int i = 1; i < 7; i++) {
            NumbersPanel panel = new NumbersPanel(composite, SWT.BORDER, 2 + ((i - 1) * 2));
            panel.setLayoutData(data);
        }
        shell.layout();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    /**
	 * 
	 */
    private void createGUI() {
        createMenu();
    }

    /**
	 * 
	 */
    private void createMenu() {
        Menu menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);
        MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
        Menu menu = new Menu(item);
        item.setText("&Datei");
        item.setMenu(menu);
        item = new MenuItem(menu, SWT.PUSH);
        item.setText("&Laden\tCtrl+L");
        item.setAccelerator(SWT.CTRL | 'L');
        item.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
            }
        });
        item = new MenuItem(menu, SWT.SEPARATOR);
        item = new MenuItem(menu, SWT.PUSH);
        item.setText("&Ende\tCtrl+E");
        item.setAccelerator(SWT.CTRL | 'E');
        item.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                System.out.println("Cancel");
                exit();
            }
        });
    }

    /**
	 * 
	 */
    private void exit() {
        shell.setVisible(false);
        shell.dispose();
    }

    /**
	 * 
	 */
    private void createToolbar() {
        ToolBar toolBar = new ToolBar(shell, SWT.NONE);
        ToolItem ti;
        ti = new ToolItem(toolBar, SWT.PUSH);
        ti.setToolTipText("Programm beenden");
        ti.setText("Ende");
        ti.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                exit();
            }
        });
    }

    private void createComponents() {
    }
}
