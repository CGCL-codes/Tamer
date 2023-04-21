package com.banordhessen.testpackage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Swtsample {

    private Display display;

    private Shell shell;

    private Swtsample() {
        display = new Display();
        shell = new Shell(display);
        shell.setText("GPS Track V&A");
        shell.setSize(800, 600);
        Monitor primary = display.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = shell.getBounds();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        shell.setLocation(x, y);
        Menu menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);
        MenuItem itemFile = new MenuItem(menuBar, SWT.CASCADE);
        MenuItem itemEdit = new MenuItem(menuBar, SWT.CASCADE);
        MenuItem itemAnalyse = new MenuItem(menuBar, SWT.CASCADE);
        MenuItem itemHelp = new MenuItem(menuBar, SWT.CASCADE);
        Menu file = new Menu(itemFile);
        itemFile.setText("&Datei");
        itemFile.setMenu(file);
        MenuItem itemOpen = new MenuItem(file, SWT.PUSH);
        itemOpen.setText("&GPX-Datei �ffnen...\tCtrl+O");
        itemOpen.setAccelerator(SWT.CTRL | 'O');
        itemOpen.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                System.out.println("Datei �ffnen");
                FileDialog fd = new FileDialog(shell);
                fd.open();
            }
        });
        MenuItem itemExit = new MenuItem(file, SWT.PUSH);
        itemExit.setText("&Beenden...\tCtrl+B");
        itemExit.setAccelerator(SWT.CTRL | 'B');
        itemExit.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                System.exit(0);
            }
        });
        Menu edit = new Menu(itemEdit);
        itemEdit.setText("&Bearbeiten");
        itemEdit.setMenu(edit);
        Menu analyse = new Menu(itemAnalyse);
        itemAnalyse.setText("&Auswerten");
        itemAnalyse.setMenu(analyse);
        Menu help = new Menu(itemHelp);
        itemHelp.setText("&Hilfe");
        itemHelp.setMenu(help);
        ToolBar bar = new ToolBar(shell, SWT.BORDER);
        Image image = new Image(display, 20, 20);
        for (int i = 0; i < 8; i++) {
            ToolItem item = new ToolItem(bar, SWT.PUSH);
            item.setText("ToolbarItem " + i);
            item.setImage(image);
        }
        bar.pack();
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        shell.setLayout(gridLayout);
        ToolBar toolbar = new ToolBar(shell, SWT.NONE);
        ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
        itemBack.setText("Back");
        ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
        itemForward.setText("Forward");
        ToolItem itemStop = new ToolItem(toolbar, SWT.PUSH);
        itemStop.setText("Stop");
        ToolItem itemRefresh = new ToolItem(toolbar, SWT.PUSH);
        itemRefresh.setText("Refresh");
        ToolItem itemGo = new ToolItem(toolbar, SWT.PUSH);
        itemGo.setText("Go");
        GridData data = new GridData();
        data.horizontalSpan = 3;
        toolbar.setLayoutData(data);
        Label labelAddress = new Label(shell, SWT.NONE);
        labelAddress.setText("Address");
        final Text location = new Text(shell, SWT.BORDER);
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.horizontalSpan = 2;
        data.grabExcessHorizontalSpace = true;
        location.setLayoutData(data);
        final Browser browser;
        try {
            browser = new Browser(shell, SWT.NONE);
        } catch (SWTError e) {
            System.out.println("Could not instantiate Browser: " + e.getMessage());
            return;
        }
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.horizontalSpan = 3;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        browser.setLayoutData(data);
        final Label status = new Label(shell, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        status.setLayoutData(data);
        final ProgressBar progressBar = new ProgressBar(shell, SWT.NONE);
        data = new GridData();
        data.horizontalAlignment = GridData.END;
        progressBar.setLayoutData(data);
        Listener listener = new Listener() {

            public void handleEvent(Event event) {
                ToolItem item = (ToolItem) event.widget;
                String string = item.getText();
                if (string.equals("Back")) browser.back(); else if (string.equals("Forward")) browser.forward(); else if (string.equals("Stop")) browser.stop(); else if (string.equals("Refresh")) browser.refresh(); else if (string.equals("Go")) browser.setUrl(location.getText());
            }
        };
        browser.addProgressListener(new ProgressListener() {

            public void changed(ProgressEvent event) {
                if (event.total == 0) return;
                int ratio = event.current * 100 / event.total;
                progressBar.setSelection(ratio);
            }

            public void completed(ProgressEvent event) {
                progressBar.setSelection(0);
            }
        });
        browser.addStatusTextListener(new StatusTextListener() {

            public void changed(StatusTextEvent event) {
                status.setText(event.text);
            }
        });
        browser.addLocationListener(new LocationListener() {

            public void changed(LocationEvent event) {
                if (event.top) location.setText(event.location);
            }

            public void changing(LocationEvent event) {
            }
        });
        itemBack.addListener(SWT.Selection, listener);
        itemForward.addListener(SWT.Selection, listener);
        itemStop.addListener(SWT.Selection, listener);
        itemRefresh.addListener(SWT.Selection, listener);
        itemGo.addListener(SWT.Selection, listener);
        location.addListener(SWT.DefaultSelection, new Listener() {

            public void handleEvent(Event e) {
                browser.setUrl(location.getText());
            }
        });
        createGUI();
        shell.open();
        while (!shell.isDisposed()) if (!display.readAndDispatch()) display.sleep();
    }

    private void createGUI() {
    }

    public static void main(String[] args) {
        new Swtsample();
    }
}
