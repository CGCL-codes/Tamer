package es.java.otro.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;

public class EntryBrowserView extends ViewPart {

    public static final String ID = "otro.entry.localbrowser";

    private Composite top = null;

    private Browser browser = null;

    public EntryBrowserView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        top = new Composite(parent, SWT.NONE);
        top.setLayout(new GridLayout());
        createBrowser();
    }

    @Override
    public void setFocus() {
    }

    /**
	 * This method initializes browser	
	 *
	 */
    private void createBrowser() {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        ToolBar navBar = new ToolBar(top, SWT.None);
        browser = new Browser(top, SWT.NONE);
        browser.setLayoutData(gridData);
        navBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END));
        final ToolItem back = new ToolItem(navBar, SWT.PUSH);
        back.setText("back");
        back.setEnabled(false);
        final ToolItem forward = new ToolItem(navBar, SWT.PUSH);
        forward.setText("forward");
        forward.setEnabled(false);
        back.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                browser.back();
            }
        });
        forward.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                browser.forward();
            }
        });
        LocationListener locationListener = new LocationListener() {

            public void changed(LocationEvent event) {
                Browser browser = (Browser) event.widget;
                back.setEnabled(browser.isBackEnabled());
                forward.setEnabled(browser.isForwardEnabled());
            }

            public void changing(LocationEvent event) {
            }
        };
        browser.addLocationListener(locationListener);
    }

    public void setHTML(String html) {
        final String header = "<html><font face='arial, vardana'>";
        final String footer = "</font></html>";
        StringBuffer sb = new StringBuffer();
        sb.append(header).append(html).append(footer);
        browser.setText(sb.toString());
    }
}
