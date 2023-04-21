package net.confex.views;

import net.confex.application.ToolbarLayout;
import net.confex.utils.ImageResource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class WebBrowserView extends ConfexView {

    public static final String VIEW_ID = "net.confex.view.WebBrowserView";

    private ToolItem back;

    private ToolItem forward;

    private Combo combo;

    private Browser browser;

    public WebBrowserView() {
    }

    private static final String TAG_URL = "url";

    private IMemento memento;

    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        this.memento = memento;
    }

    public void saveState(IMemento memento) {
        if (browser.getUrl() != null && !browser.getUrl().trim().equals("")) {
            memento.putString(TAG_URL, browser.getUrl());
        }
    }

    /**
     * Restores the state of the receiver to the state described in the specified memento.
     *
     * @param memento the memento
     * @since 2.0
     */
    protected void restoreState(IMemento memento) {
        browser.setUrl(memento.getString(TAG_URL));
    }

    public void setTitlePartName(String title) {
        setPartName(title);
    }

    public void init(IViewSite site) throws PartInitException {
        if (site.getSecondaryId() != null) {
            String partName = "WebBrowserView";
            if (partName.length() != 0) setPartName(partName);
        }
        super.init(site);
    }

    public void createPartControl(Composite parent) {
        Composite content = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
        content.setLayout(gridLayout);
        Composite toolbarComp = new Composite(content, SWT.NONE);
        toolbarComp.setLayout(new ToolbarLayout());
        toolbarComp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
        createToolbar(toolbarComp);
        createLocationBar(toolbarComp);
        contributeToActionBars();
        browser = new Browser(content, SWT.NONE);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        browser.addLocationListener(new LocationListener() {

            public void changed(LocationEvent event) {
                combo.setText(event.location);
            }

            public void changing(LocationEvent event) {
            }
        });
        if (memento != null) {
            restoreState(memento);
            memento = null;
        }
    }

    public void addPartListener(IPartListener listener) {
        getSite().getWorkbenchWindow().getPartService().addPartListener(listener);
    }

    public void removePartListener(IPartListener listener) {
        getSite().getWorkbenchWindow().getPartService().removePartListener(listener);
    }

    private ToolBar createToolbar(Composite parent) {
        ToolBar toolbar = new ToolBar(parent, SWT.FLAT);
        back = new ToolItem(toolbar, SWT.NONE);
        back.setImage(ImageResource.getImage(ImageResource.IMG_ELCL_NAV_BACKWARD));
        back.setHotImage(ImageResource.getImage(ImageResource.IMG_CLCL_NAV_BACKWARD));
        back.setDisabledImage(ImageResource.getImage(ImageResource.IMG_DLCL_NAV_BACKWARD));
        back.setToolTipText("Back");
        back.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                back();
            }
        });
        forward = new ToolItem(toolbar, SWT.NONE);
        forward.setImage(ImageResource.getImage(ImageResource.IMG_ELCL_NAV_FORWARD));
        forward.setHotImage(ImageResource.getImage(ImageResource.IMG_CLCL_NAV_FORWARD));
        forward.setDisabledImage(ImageResource.getImage(ImageResource.IMG_DLCL_NAV_FORWARD));
        forward.setToolTipText("Forward");
        forward.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                forward();
            }
        });
        ToolItem stop = new ToolItem(toolbar, SWT.NONE);
        stop.setImage(ImageResource.getImage(ImageResource.IMG_ELCL_NAV_STOP));
        stop.setHotImage(ImageResource.getImage(ImageResource.IMG_CLCL_NAV_STOP));
        stop.setDisabledImage(ImageResource.getImage(ImageResource.IMG_DLCL_NAV_STOP));
        stop.setToolTipText("Stop");
        stop.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                stop();
            }
        });
        ToolItem refresh = new ToolItem(toolbar, SWT.NONE);
        refresh.setImage(ImageResource.getImage(ImageResource.IMG_ELCL_NAV_REFRESH));
        refresh.setHotImage(ImageResource.getImage(ImageResource.IMG_CLCL_NAV_REFRESH));
        refresh.setDisabledImage(ImageResource.getImage(ImageResource.IMG_DLCL_NAV_REFRESH));
        refresh.setToolTipText("Refresh");
        refresh.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                refresh();
            }
        });
        return toolbar;
    }

    private ToolBar createLocationBar(Composite parent) {
        combo = new Combo(parent, SWT.DROP_DOWN);
        combo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent we) {
                try {
                    if (combo.getSelectionIndex() != -1) setUrl(combo.getItem(combo.getSelectionIndex()));
                } catch (Exception e) {
                }
            }
        });
        combo.addListener(SWT.DefaultSelection, new Listener() {

            public void handleEvent(Event e) {
                setUrl(combo.getText());
            }
        });
        ToolBar toolbar = new ToolBar(parent, SWT.FLAT);
        ToolItem go = new ToolItem(toolbar, SWT.NONE);
        go.setImage(ImageResource.getImage(ImageResource.IMG_ELCL_NAV_GO));
        go.setHotImage(ImageResource.getImage(ImageResource.IMG_CLCL_NAV_GO));
        go.setDisabledImage(ImageResource.getImage(ImageResource.IMG_DLCL_NAV_GO));
        go.setToolTipText("Go");
        go.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                if (browser.getUrl().length() != 0 && combo.indexOf(browser.getUrl()) == -1) combo.add(browser.getUrl());
                setUrl(combo.getText());
            }
        });
        return toolbar;
    }

    /**
     * Navigate to the next session history item. Convenience method that calls
     * the underlying SWT browser.
     * 
     * @return <code>true</code> if the operation was successful and
     *         <code>false</code> otherwise
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the
     *                wrong thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     * @see #back
     */
    public boolean forward() {
        if (browser == null) return false;
        return browser.forward();
    }

    /**
     * Navigate to the previous session history item. Convenience method that
     * calls the underlying SWT browser.
     * 
     * @return <code>true</code> if the operation was successful and
     *         <code>false</code> otherwise
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the
     *                wrong thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     * @see #forward
     */
    public boolean back() {
        if (browser == null) return false;
        return browser.back();
    }

    /**
     * Returns <code>true</code> if the receiver can navigate to the previous
     * session history item, and <code>false</code> otherwise. Convenience
     * method that calls the underlying SWT browser.
     * 
     * @return the receiver's back command enabled state
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * @see #back
     */
    public boolean isBackEnabled() {
        if (browser == null) return false;
        return browser.isBackEnabled();
    }

    /**
     * Returns <code>true</code> if the receiver can navigate to the next
     * session history item, and <code>false</code> otherwise. Convenience
     * method that calls the underlying SWT browser.
     * 
     * @return the receiver's forward command enabled state
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
     *                thread that created the receiver</li>
     *                </ul>
     * @see #forward
     */
    public boolean isForwardEnabled() {
        if (browser == null) return false;
        return browser.isForwardEnabled();
    }

    /**
     * Stop any loading and rendering activity. Convenience method that calls
     * the underlying SWT browser.
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the
     *                wrong thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     */
    public void stop() {
        if (browser != null) browser.stop();
    }

    /**
     * Refresh the current page. Convenience method that calls the underlying
     * SWT browser.
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_THREAD_INVALID_ACCESS when called from the
     *                wrong thread</li>
     *                <li>ERROR_WIDGET_DISPOSED when the widget has been
     *                disposed</li>
     *                </ul>
     */
    public void refresh() {
        if (browser != null) browser.refresh();
    }

    public void setUrl(String url) {
        browser.setUrl(url);
    }

    public void setPage(String page) {
        browser.setText(page);
    }

    public void dispose() {
        super.dispose();
    }

    public void setFocus() {
        browser.setFocus();
    }

    public Combo getCombo() {
        return combo;
    }
}
