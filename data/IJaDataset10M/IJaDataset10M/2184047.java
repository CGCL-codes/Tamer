package com.dukesoftware.demos.swt;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;

public class SWTBrowser {

    Display display = new Display();

    Shell shell = new Shell(display);

    Text textLocation;

    Browser browser;

    Label labelStatus;

    public SWTBrowser() {
        shell.setLayout(new GridLayout());
        ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
        final ToolBarManager manager = new ToolBarManager(toolBar);
        Composite compositeLocation = new Composite(shell, SWT.NULL);
        compositeLocation.setLayout(new GridLayout(3, false));
        compositeLocation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Label labelAddress = new Label(compositeLocation, SWT.NULL);
        labelAddress.setText("Address");
        textLocation = new Text(compositeLocation, SWT.SINGLE | SWT.BORDER);
        textLocation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Button buttonGo = new Button(compositeLocation, SWT.NULL);
        browser = new Browser(shell, SWT.BORDER);
        browser.setLayoutData(new GridData(GridData.FILL_BOTH));
        Composite compositeStatus = new Composite(shell, SWT.NULL);
        compositeStatus.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        compositeStatus.setLayout(new GridLayout(2, false));
        labelStatus = new Label(compositeStatus, SWT.NULL);
        labelStatus.setText("Ready");
        labelStatus.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final ProgressBar progressBar = new ProgressBar(compositeStatus, SWT.SMOOTH);
        Listener openURLListener = new Listener() {

            public void handleEvent(Event event) {
                browser.setUrl(textLocation.getText());
            }
        };
        buttonGo.addListener(SWT.Selection, openURLListener);
        textLocation.addListener(SWT.DefaultSelection, openURLListener);
        final Action actionBackward = new Action("&Backword") {

            public void run() {
                browser.back();
            }
        };
        actionBackward.setEnabled(false);
        final Action actionForward = new Action("&Forward") {

            public void run() {
                browser.forward();
            }
        };
        actionForward.setEnabled(false);
        Action actionStop = new Action("&Stop") {

            public void run() {
                browser.stop();
            }
        };
        Action actionRefresh = new Action("&Refresh") {

            public void run() {
                browser.refresh();
            }
        };
        Action actionHome = new Action("&Home") {

            public void run() {
                browser.setUrl("http://www.eclipse.org");
            }
        };
        manager.add(actionBackward);
        manager.add(actionForward);
        manager.add(actionStop);
        manager.add(actionRefresh);
        manager.add(actionHome);
        manager.update(true);
        toolBar.pack();
        browser.addLocationListener(new LocationListener() {

            public void changing(LocationEvent event) {
                textLocation.setText(event.location);
            }

            public void changed(LocationEvent event) {
                actionBackward.setEnabled(browser.isBackEnabled());
                actionForward.setEnabled(browser.isForwardEnabled());
                manager.update(false);
            }
        });
        browser.addProgressListener(new ProgressListener() {

            public void changed(ProgressEvent event) {
                progressBar.setMaximum(event.total);
                progressBar.setSelection(event.current);
            }

            public void completed(ProgressEvent event) {
                progressBar.setSelection(0);
            }
        });
        browser.addStatusTextListener(new StatusTextListener() {

            public void changed(StatusTextEvent event) {
                labelStatus.setText(event.text);
            }
        });
        browser.addTitleListener(new TitleListener() {

            public void changed(TitleEvent event) {
                shell.setText(event.title + " - powered by SWT");
            }
        });
        initialize(display, browser);
        shell.setSize(500, 400);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    static void initialize(final Display display, Browser browser) {
        browser.addOpenWindowListener(new OpenWindowListener() {

            public void open(WindowEvent event) {
                Shell shell = new Shell(display);
                shell.setText("New Window");
                shell.setLayout(new FillLayout());
                Browser browser = new Browser(shell, SWT.NONE);
                initialize(display, browser);
                event.browser = browser;
            }
        });
        browser.addVisibilityWindowListener(new VisibilityWindowListener() {

            public void hide(WindowEvent event) {
                Browser browser = (Browser) event.widget;
                Shell shell = browser.getShell();
                shell.setVisible(false);
            }

            public void show(WindowEvent event) {
                Browser browser = (Browser) event.widget;
                Shell shell = browser.getShell();
                if (event.location != null) shell.setLocation(event.location);
                if (event.size != null) {
                    Point size = event.size;
                    shell.setSize(shell.computeSize(size.x, size.y));
                }
                shell.open();
            }
        });
        browser.addCloseWindowListener(new CloseWindowListener() {

            public void close(WindowEvent event) {
                Browser browser = (Browser) event.widget;
                Shell shell = browser.getShell();
                shell.close();
            }
        });
    }

    public static void main(String[] args) {
        new SWTBrowser();
    }
}
