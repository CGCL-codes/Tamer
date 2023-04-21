package ti.plato.ui.views.internal.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IBasicPropertyConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import ti.mcore.u.PluginUtil;
import ti.plato.ui.views.console.ConsolePlugin;
import ti.plato.ui.views.console.IConsole;
import ti.plato.ui.views.console.IConsoleConstants;
import ti.plato.ui.views.console.IConsoleListener;
import ti.plato.ui.views.console.IConsoleManager;
import ti.plato.ui.views.console.IConsolePageParticipant;
import ti.plato.ui.views.console.IConsoleView;
import ti.plato.ui.views.console.TextConsoleViewer;
import ti.plato.ui.views.console.constants.Constants;
import ti.plato.ui.views.console.feedback.IFeedback;

/**
 * Page book console view.
 * 
 * @since 3.0
 */
public class ConsoleView extends PageBookView implements IConsoleView, IConsoleListener, IPropertyChangeListener, IPartListener2 {

    /**
	 * Whether this console is pinned.
	 */
    private boolean fPinned = false;

    private boolean fRunscript = false;

    /**
	 * Stack of consoles in MRU order
	 */
    private List fStack = new ArrayList();

    /**
	 * The console being displayed, or <code>null</code> if none
	 */
    private IConsole fActiveConsole = null;

    /**
	 * Map of consoles to dummy console parts (used to close pages)
	 */
    private Map fConsoleToPart;

    /**
	 * Map of consoles to array of page participants
	 */
    private Map fConsoleToPageParticipants;

    /**
	 * Map of parts to consoles
	 */
    private Map fPartToConsole;

    /**
	 * Whether this view is active
	 */
    private boolean fActive = false;

    private CancelAction fCancelAction = null;

    private RunScriptAction fRunScriptAction = null;

    private PinConsoleAction fPinAction = null;

    private ConsoleDropDownAction fDisplayConsoleAction = null;

    private OpenConsoleAction fOpenConsoleAction = null;

    private boolean fScrollLock;

    private boolean isAvailable() {
        return getPageBook() != null && !getPageBook().isDisposed();
    }

    public void propertyChange(PropertyChangeEvent event) {
        Object source = event.getSource();
        if (source instanceof IConsole && event.getProperty().equals(IBasicPropertyConstants.P_TEXT)) {
            if (source.equals(getConsole())) {
                updateTitle();
            }
        }
    }

    public void partClosed(IWorkbenchPart part) {
        super.partClosed(part);
    }

    public IConsole getConsole() {
        return fActiveConsole;
    }

    public TextConsoleViewer getTextConsoleViewer() {
        IPage page = getCurrentPage();
        if (page instanceof IOConsolePage) {
            TextConsoleViewer viewer = ((IOConsolePage) page).getViewer();
            return viewer;
        }
        return null;
    }

    protected void showPageRec(PageRec pageRec) {
        if (fActiveConsole != null && pageRec.page != getDefaultPage() && fPinned && fConsoleToPart.size() > 1) {
            IConsole console = (IConsole) fPartToConsole.get(pageRec.part);
            if (!fStack.contains(console)) {
                fStack.add(console);
            }
            return;
        }
        IConsole recConsole = (IConsole) fPartToConsole.get(pageRec.part);
        if (recConsole != null && recConsole.equals(fActiveConsole)) {
            return;
        }
        super.showPageRec(pageRec);
        fActiveConsole = recConsole;
        IConsole tos = null;
        if (!fStack.isEmpty()) {
            tos = (IConsole) fStack.get(0);
        }
        if (tos != null && !tos.equals(fActiveConsole)) {
            deactivateParticipants(tos);
        }
        if (fActiveConsole != null && !fActiveConsole.equals(tos)) {
            fStack.remove(fActiveConsole);
            fStack.add(0, fActiveConsole);
            activateParticipants(fActiveConsole);
        }
        updateTitle();
        IPage page = getCurrentPage();
        if (page instanceof IOConsolePage) {
            ((IOConsolePage) page).setAutoScroll(!fScrollLock);
        }
    }

    /**
	 * Activates the participants fot the given console, if any.
	 * 
	 * @param console
	 */
    private void activateParticipants(IConsole console) {
        if (console != null && fActive) {
            IConsolePageParticipant[] participants = getParticipants(console);
            if (participants != null) {
                for (int i = 0; i < participants.length; i++) {
                    participants[i].activated();
                }
            }
        }
    }

    /**
	 * Returns a stack of consoles in the view in MRU order.
	 * 
	 * @return a stack of consoles in the view in MRU order
	 */
    protected List getConsoleStack() {
        return fStack;
    }

    /**
	 * Updates the view title based on the active console
	 */
    protected void updateTitle() {
    }

    protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
        IConsole console = (IConsole) fPartToConsole.get(part);
        IConsolePageParticipant[] participants = (IConsolePageParticipant[]) fConsoleToPageParticipants.remove(console);
        for (int i = 0; i < participants.length; i++) {
            IConsolePageParticipant participant = participants[i];
            participant.dispose();
        }
        IPage page = pageRecord.page;
        page.dispose();
        pageRecord.dispose();
        console.removePropertyChangeListener(this);
        fPartToConsole.remove(part);
        fConsoleToPart.remove(console);
        if (fPartToConsole.isEmpty()) {
            fActiveConsole = null;
        }
    }

    /**
	 * Returns the page participants registered for the given console, or <code>null</code>
	 * 
	 * @param console
	 * @return registered page participants or <code>null</code>
	 */
    private IConsolePageParticipant[] getParticipants(IConsole console) {
        return (IConsolePageParticipant[]) fConsoleToPageParticipants.get(console);
    }

    protected PageRec doCreatePage(IWorkbenchPart dummyPart) {
        ConsoleWorkbenchPart part = (ConsoleWorkbenchPart) dummyPart;
        IConsole console = part.getConsole();
        IPageBookViewPage page = console.createPage(this);
        initPage(page);
        page.createControl(getPageBook());
        console.addPropertyChangeListener(this);
        IConsolePageParticipant[] participants = ((ConsoleManager) getConsoleManager()).getPageParticipants(console);
        fConsoleToPageParticipants.put(console, participants);
        for (int i = 0; i < participants.length; i++) {
            IConsolePageParticipant participant = participants[i];
            participant.init(page, console);
        }
        PageRec rec = new PageRec(dummyPart, page);
        return rec;
    }

    protected boolean isImportant(IWorkbenchPart part) {
        return part instanceof ConsoleWorkbenchPart;
    }

    public void dispose() {
        super.dispose();
        getViewSite().getPage().removePartListener((IPartListener2) this);
        ConsoleManager consoleManager = (ConsoleManager) ConsolePlugin.getDefault().getConsoleManager();
        consoleManager.removeConsoleListener(this);
        consoleManager.registerConsoleView(this);
        ConsolePlugin.getDefault().getConsoleManager().removeConsoles(new IConsole[] { openedConsole });
    }

    /**
	 * Returns the console manager.
	 * 
     * @return the console manager
     */
    private IConsoleManager getConsoleManager() {
        return ConsolePlugin.getDefault().getConsoleManager();
    }

    protected IPage createDefaultPage(PageBook book) {
        MessagePage page = new MessagePage();
        page.createControl(getPageBook());
        initPage(page);
        return page;
    }

    public void consolesAdded(final IConsole[] consoles) {
        if (openedConsole.getName().compareTo(consoles[0].getName()) != 0) return;
        if (!isAvailable()) {
            return;
        }
        Runnable r = new Runnable() {

            public void run() {
                for (int i = 0; i < consoles.length; i++) {
                    if (!isAvailable()) {
                        continue;
                    }
                    IConsole console = consoles[i];
                    IConsole[] allConsoles = getConsoleManager().getConsoles();
                    for (int j = 0; j < allConsoles.length; j++) {
                        IConsole registered = allConsoles[j];
                        if (!registered.equals(console)) {
                            continue;
                        }
                        ConsoleWorkbenchPart part = new ConsoleWorkbenchPart(console, getSite());
                        fConsoleToPart.put(console, part);
                        fPartToConsole.put(part, console);
                        partActivated(part);
                        extensionFeedback.setAction(console, fCancelAction);
                        break;
                    }
                }
            }
        };
        asyncExec(r);
    }

    public void consolesRemoved(final IConsole[] consoles) {
        if (isAvailable()) {
            Runnable r = new Runnable() {

                public void run() {
                    for (int i = 0; i < consoles.length; i++) {
                        if (isAvailable()) {
                            IConsole console = consoles[i];
                            extensionFeedback.removeAction(console);
                            fStack.remove(console);
                            ConsoleWorkbenchPart part = (ConsoleWorkbenchPart) fConsoleToPart.get(console);
                            if (part != null) {
                                partClosed(part);
                            }
                            if (getConsole() == null) {
                                IConsole[] available = getConsoleManager().getConsoles();
                                if (available.length > 0) {
                                    display(available[available.length - 1]);
                                } else {
                                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(ConsoleView.this);
                                }
                            }
                        }
                    }
                }
            };
            asyncExec(r);
        }
    }

    /**
	 * Constructs a console view
	 */
    public ConsoleView() {
        super();
        fConsoleToPart = new HashMap();
        fPartToConsole = new HashMap();
        fConsoleToPageParticipants = new HashMap();
        ConsoleManager consoleManager = (ConsoleManager) ConsolePlugin.getDefault().getConsoleManager();
        consoleManager.registerConsoleView(this);
    }

    protected void createActions() {
        fPinAction = new PinConsoleAction(this);
        fPinAction.setEnabled(false);
        fRunScriptAction = new RunScriptAction(this, extensionFeedback);
        fCancelAction = new CancelAction(this, extensionFeedback);
    }

    protected void configureToolBar(IToolBarManager mgr) {
        mgr.add(new Separator(IConsoleConstants.LAUNCH_GROUP));
        mgr.add(new Separator(IConsoleConstants.OUTPUT_GROUP));
        mgr.add(new Separator("fixedGroup"));
        if (fPinAction != null) mgr.add(fPinAction);
        if (fRunScriptAction != null) mgr.add(fRunScriptAction);
        if (fDisplayConsoleAction != null) mgr.add(fDisplayConsoleAction);
        if (fOpenConsoleAction != null) {
            mgr.add(fOpenConsoleAction);
        }
        if (fCancelAction != null) {
            mgr.add(fCancelAction);
        }
    }

    public void display(IConsole console) {
        if (fPinned && fActiveConsole != null) {
            return;
        }
        if (console.equals(fActiveConsole)) {
            return;
        }
        ConsoleWorkbenchPart part = (ConsoleWorkbenchPart) fConsoleToPart.get(console);
        if (part != null) {
            partActivated(part);
        }
    }

    public void setPinned(boolean pin) {
        fPinned = pin;
    }

    public void setRunscript(boolean runscript) {
        fRunscript = runscript;
    }

    public boolean isRunscript() {
        return fRunscript;
    }

    public Action getCancelAction() {
        return fCancelAction;
    }

    public boolean isPinned() {
        return fPinned;
    }

    protected IWorkbenchPart getBootstrapPart() {
        return null;
    }

    /**
	 * Registers the given runnable with the display
	 * associated with this view's control, if any.
	 * 
	 * @see org.eclipse.swt.widgets.Display#asyncExec(java.lang.Runnable)
	 */
    public void asyncExec(Runnable r) {
        if (isAvailable()) {
            getPageBook().getDisplay().asyncExec(r);
        }
    }

    /**
	 * Creates this view's underlying viewer and actions.
	 * Hooks a pop-up menu to the underlying viewer's control,
	 * as well as a key listener. When the delete key is pressed,
	 * the <code>REMOVE_ACTION</code> is invoked. Hooks help to
	 * this view. Subclasses must implement the following methods
	 * which are called in the following order when a view is
	 * created:<ul>
	 * <li><code>createViewer(Composite)</code> - the context
	 *   menu is hooked to the viewer's control.</li>
	 * <li><code>createActions()</code></li>
	 * <li><code>configureToolBar(IToolBarManager)</code></li>
	 * <li><code>getHelpContextId()</code></li>
	 * </ul>
	 * @see IWorkbenchPart#createPartControl(Composite)
	 */
    private IFeedback extensionFeedback = null;

    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        IFeedback[] extensions = (IFeedback[]) PluginUtil.getExecutableExtensions(Constants.feedbackExtensionPointID, IFeedback.class);
        if (extensions.length != 1) {
            return;
        }
        extensionFeedback = extensions[0];
        if (getViewSite().getSecondaryId() != null) setPartName(getViewSite().getSecondaryId());
        createActions();
        IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
        configureToolBar(tbm);
        updateForExistingConsoles();
        getViewSite().getActionBars().updateActionBars();
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IConsoleHelpContextIds.CONSOLE_VIEW);
        getViewSite().getPage().addPartListener((IPartListener2) this);
    }

    /**
	 * Initialize for existing consoles
	 */
    private IConsole openedConsole = null;

    private void updateForExistingConsoles() {
        openedConsole = extensionFeedback.onGetNewConsole();
        ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { openedConsole });
        IConsoleManager manager = getConsoleManager();
        consolesAdded(new IConsole[] { openedConsole });
        manager.addConsoleListener(this);
    }

    public void warnOfContentChange(IConsole console) {
        IWorkbenchPart part = (IWorkbenchPart) fConsoleToPart.get(console);
        if (part != null) {
            IWorkbenchSiteProgressService service = (IWorkbenchSiteProgressService) part.getSite().getAdapter(IWorkbenchSiteProgressService.class);
            if (service != null) {
                service.warnOfContentChange();
            }
        }
    }

    public Object getAdapter(Class key) {
        Object adpater = super.getAdapter(key);
        if (adpater == null) {
            IConsole console = getConsole();
            if (console != null) {
                IConsolePageParticipant[] participants = (IConsolePageParticipant[]) fConsoleToPageParticipants.get(console);
                if (participants != null) {
                    for (int i = 0; i < participants.length; i++) {
                        IConsolePageParticipant participant = participants[i];
                        adpater = participant.getAdapter(key);
                        if (adpater != null) {
                            return adpater;
                        }
                    }
                }
            }
        }
        return adpater;
    }

    public void partActivated(IWorkbenchPartReference partRef) {
        if (isThisPart(partRef)) {
            fActive = true;
            activateParticipants(fActiveConsole);
        }
    }

    public void partBroughtToTop(IWorkbenchPartReference partRef) {
    }

    public void partClosed(IWorkbenchPartReference partRef) {
    }

    public void partDeactivated(IWorkbenchPartReference partRef) {
        if (isThisPart(partRef)) {
            fActive = false;
            deactivateParticipants(fActiveConsole);
        }
    }

    protected boolean isThisPart(IWorkbenchPartReference partRef) {
        if (partRef instanceof IViewReference) {
            IViewReference viewRef = (IViewReference) partRef;
            if (viewRef.getId().equals(getViewSite().getId())) {
                String secId = viewRef.getSecondaryId();
                String mySec = null;
                if (getSite() instanceof IViewSite) {
                    mySec = ((IViewSite) getSite()).getSecondaryId();
                }
                if (mySec == null) {
                    return secId == null;
                }
                return mySec.equals(secId);
            }
        }
        return false;
    }

    /**
	 * Deactivates participants for the given console, if any.
	 * 
	 * @param console console to deactivate
	 */
    private void deactivateParticipants(IConsole console) {
        if (console != null) {
            IConsolePageParticipant[] participants = getParticipants(console);
            if (participants != null) {
                for (int i = 0; i < participants.length; i++) {
                    participants[i].deactivated();
                }
            }
        }
    }

    public void partOpened(IWorkbenchPartReference partRef) {
    }

    public void partHidden(IWorkbenchPartReference partRef) {
    }

    public void partVisible(IWorkbenchPartReference partRef) {
    }

    public void partInputChanged(IWorkbenchPartReference partRef) {
    }

    public void setScrollLock(boolean scrollLock) {
        fScrollLock = scrollLock;
        IPage page = getCurrentPage();
        if (page instanceof IOConsolePage) {
            ((IOConsolePage) page).setAutoScroll(!scrollLock);
        }
    }

    public boolean getScrollLock() {
        return fScrollLock;
    }

    public void pin(IConsole console) {
        if (console == null) {
            setPinned(false);
        } else {
            if (isPinned()) {
                setPinned(false);
            }
            display(console);
            setPinned(true);
        }
    }
}
