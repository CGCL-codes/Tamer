package tr.view.future.screen;

import au.com.thinkingrock.tr.resource.Icons;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.logging.Logger;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import tr.view.Window;

/**
 * Top component for future item.
 *
 * @author <a href="mailto:jimoore@netspace.net.au">Jeremy Moore</a>
 */
public final class FutureTopComponent extends Window implements LookupListener {

    private static final Logger LOG = Logger.getLogger("tr.view.future");

    private static final String PREFERRED_ID = "FutureTopComponent";

    private static FutureTopComponent instance;

    private FuturePanel panel;

    private Lookup.Result result;

    private FutureTopComponent() {
        setName(NbBundle.getMessage(FutureTopComponent.class, "CTL_FutureTopComponent"));
        setToolTipText(NbBundle.getMessage(FutureTopComponent.class, "TTT_FutureTopComponent"));
        setIcon(Icons.SomedayMaybe.getImage());
        initComponents();
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }

    @Override
    protected void componentOpened() {
        super.componentOpened();
        if (panel == null) {
            removeAll();
            panel = new FuturePanel();
            add(panel, BorderLayout.CENTER);
        }
        panel.initModel(null);
        FuturesTopComponent rtc = FuturesTopComponent.findInstance();
        result = rtc.getLookup().lookup(new Lookup.Template(FutureNode.class));
        result.addLookupListener(this);
        result.allInstances();
    }

    protected void componentClosed() {
        super.componentClosed();
        result.removeLookupListener(this);
        result = null;
    }

    protected void componentDeactivated() {
        panel.deactivate();
        super.componentDeactivated();
    }

    public synchronized void resultChanged(LookupEvent lookupEvent) {
        LOG.info("Starting");
        if (panel == null) return;
        Collection collection = result.allInstances();
        if (collection.isEmpty()) {
            panel.initModel(null);
            LOG.info("null");
        } else {
            FutureNode node = (FutureNode) collection.iterator().next();
            panel.initModel(node.future);
            LOG.info("initialising model");
        }
    }

    /** Start editing if possible. */
    public void edit() {
        if (panel == null) return;
        requestActive();
        panel.edit();
    }

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized FutureTopComponent getDefault() {
        if (instance == null) {
            instance = new FutureTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the FutureTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized FutureTopComponent findInstance() {
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.future");
    }
}
