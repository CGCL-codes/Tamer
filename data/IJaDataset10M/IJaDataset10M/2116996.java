package de.sooja.framework.ui.browser;

import java.util.Date;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import de.sooja.framework.core.bookmark.model.BookmarkManager;
import de.sooja.framework.core.bookmark.model.BookmarksChangeEvent;
import de.sooja.framework.core.bookmark.model.IBookmarkChangeListener;
import de.sooja.framework.ui.UIPlugin;
import de.sooja.framework.core.application.manager.ApplicationManager;
import de.sooja.framework.core.site.manager.SiteManager;

/**
 * The Browser view.  This consists of a <code>Browser</code> control, and an
 * address bar consisting of a <code>Label</code> and a <code>Text</code> 
 * control.  This registers handling actions for the retargetable actions added 
 * by <code>SoojaActionBuilder</code> (Back, Forward, Stop, Refresh, Home).  
 * This also hooks listeners on the Browser control for status and progress
 * messages, and redirects these to the status line.
 * 
 * @since 3.0
 */
public class BrowserView extends ViewPart implements ISoojaBrowser {

    /**
   * Debug flag.  When true, status and progress messages are sent to the
   * console in addition to the status line.
   */
    private static final boolean DEBUG = false;

    private Date date;

    private Logger logger;

    private Browser browser;

    private BrowserView browserView;

    private Text location;

    private IWorkbenchWindow window;

    private SiteManager mySiteManager = ApplicationManager.getInstance().getSiteManager();

    private String initialUrl;

    private Action backAction = new Action("Back") {

        public void run() {
            browser.back();
        }
    };

    private Action forwardAction = new Action("Forward") {

        public void run() {
            browser.forward();
        }
    };

    private Action stopAction = new Action("Stop") {

        public void run() {
            browser.stop();
            getViewSite().getActionBars().getStatusLineManager().getProgressMonitor().done();
        }
    };

    private Action refreshAction = new Action("Refresh") {

        public void run() {
            browser.refresh();
        }
    };

    private BrowserWindowListener listener = new BrowserWindowListener() {

        public void registerAsActive() {
            window = UIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
            UIPlugin.setBrowserView(browserView, window);
        }

        public void setViewed() {
            date.getTime();
        }

        public Date getViewed() {
            return date;
        }

        public IWorkbenchWindow getWorkbenchWindow() {
            return window;
        }
    };

    /**
   * Constructs a new <code>BrowserView</code>.
   */
    public BrowserView() {
        window = UIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
        date = new Date();
        date.getTime();
        browserView = this;
        logger = Logger.getLogger("de.sooja.framework.ui.browser");
        initialUrl = BookmarkManager.getBookmarkManager().getStartPage();
        BookmarkManager.getBookmarkManager().addListener(new IBookmarkChangeListener() {

            public void bookmarksChanged(BookmarksChangeEvent event) {
                initialUrl = BookmarkManager.getBookmarkManager().getStartPage();
            }
        });
        UIPlugin.getDefault().addBrowserWindowListener(listener);
    }

    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site);
        if (memento != null) {
            String u = memento.getString("url");
            if (u != null) {
                initialUrl = u;
            }
        }
    }

    public void saveState(IMemento memento) {
        memento.putString("url", browser.getUrl());
    }

    public void createPartControl(Composite parent) {
        browser = createBrowser(parent, getViewSite().getActionBars());
        browser.setUrl(initialUrl);
    }

    public void loadURL(String url) {
        logger.debug("is the browser(" + this.hashCode() + ") disposed? " + browser.isDisposed());
        browser.setUrl(url);
        listener.setViewed();
    }

    public String getURL() {
        return browser.getUrl();
    }

    public void setURL(String URL) {
        location.setText(URL);
    }

    public void dispose() {
        logger.debug("dispose BrowserView: " + this.hashCode() + " in window " + window.hashCode());
        UIPlugin.setBrowserView(null, window);
        UIPlugin.getDefault().removeBrowserWindowListener(listener);
        super.dispose();
    }

    public void setFocus() {
        if (browser != null && !browser.isDisposed()) {
            logger.debug("setFocus to BrowserView: " + this.hashCode() + " in window " + window.hashCode());
            browser.setFocus();
        }
        listener.setViewed();
        UIPlugin.setBrowserView(this, window);
        mySiteManager.setSoojaSideIdByLocation(location);
    }

    private Browser createBrowser(Composite parent, final IActionBars actionBars) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        parent.setLayout(gridLayout);
        Label labelAddress = new Label(parent, SWT.NONE);
        labelAddress.setText("URL");
        location = new Text(parent, SWT.BORDER);
        GridData data = new GridData();
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        location.setLayoutData(data);
        browser = new Browser(parent, SWT.NONE);
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.horizontalSpan = 2;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        browser.setLayoutData(data);
        browser.addProgressListener(new ProgressAdapter() {

            IProgressMonitor monitor = actionBars.getStatusLineManager().getProgressMonitor();

            boolean working = false;

            int workedSoFar;

            public void changed(ProgressEvent event) {
                if (DEBUG) {
                    System.out.println("changed: " + event.current + "/" + event.total);
                }
                if (event.total == 0) return;
                if (!working) {
                    if (event.current == event.total) return;
                    monitor.beginTask("", event.total);
                    workedSoFar = 0;
                    working = true;
                }
                monitor.worked(event.current - workedSoFar);
                workedSoFar = event.current;
            }

            public void completed(ProgressEvent event) {
                if (DEBUG) {
                    System.out.println("completed: " + event.current + "/" + event.total);
                }
                monitor.done();
                working = false;
            }
        });
        browser.addStatusTextListener(new StatusTextListener() {

            IStatusLineManager status = actionBars.getStatusLineManager();

            public void changed(StatusTextEvent event) {
                if (DEBUG) {
                    System.out.println("status: " + event.text);
                }
                status.setMessage(event.text);
            }
        });
        browser.addLocationListener(new LocationAdapter() {

            public void changed(LocationEvent event) {
                if (event.top) location.setText(event.location);
                mySiteManager.setSoojaSideIdByLocation(location);
            }
        });
        browser.addTitleListener(new TitleListener() {

            public void changed(TitleEvent event) {
                setPartName(event.title);
            }
        });
        browser.addOpenWindowListener(new OpenWindowListener() {

            public void open(WindowEvent event) {
                System.out.println("neues Fenster gef�llig???");
                PopUpBrowser popBrowser = new PopUpBrowser(event.browser);
                popBrowser.run();
            }
        });
        location.addSelectionListener(new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent e) {
                browser.setUrl(location.getText());
            }
        });
        return browser;
    }

    private Browser createLocalBrowser(Composite parent, final IActionBars actionBars) {
        browser = new Browser(parent, SWT.NONE);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.horizontalSpan = 2;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        browser.setLayoutData(data);
        browser.addProgressListener(new ProgressAdapter() {

            IProgressMonitor monitor = actionBars.getStatusLineManager().getProgressMonitor();

            boolean working = false;

            int workedSoFar;

            public void changed(ProgressEvent event) {
                if (DEBUG) {
                    System.out.println("changed: " + event.current + "/" + event.total);
                }
                if (event.total == 0) return;
                if (!working) {
                    if (event.current == event.total) return;
                    monitor.beginTask("", event.total);
                    workedSoFar = 0;
                    working = true;
                }
                monitor.worked(event.current - workedSoFar);
                workedSoFar = event.current;
            }

            public void completed(ProgressEvent event) {
                if (DEBUG) {
                    System.out.println("completed: " + event.current + "/" + event.total);
                }
                monitor.done();
                working = false;
            }
        });
        browser.addStatusTextListener(new StatusTextListener() {

            IStatusLineManager status = actionBars.getStatusLineManager();

            public void changed(StatusTextEvent event) {
                if (DEBUG) {
                    System.out.println("status: " + event.text);
                }
                status.setMessage(event.text);
            }
        });
        return browser;
    }

    public boolean isBackable() {
        return browser.isBackEnabled();
    }

    public void back() {
        backAction.run();
    }

    public boolean isForwardable() {
        return browser.isForwardEnabled();
    }

    public void forward() {
        forwardAction.run();
    }

    public void stop() {
        stopAction.run();
    }

    public void refresh() {
        refreshAction.run();
    }

    public void loadStartPage() {
        browser.setUrl(BookmarkManager.getBookmarkManager().getStartPage());
    }
}
