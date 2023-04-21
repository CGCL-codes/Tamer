package de.bielefeld.uni.cebitec.contigadjacencyvisualization;

import de.bielefeld.uni.cebitec.contigadjacencygraph.LayoutGraph;
import de.bielefeld.uni.cebitec.contigadjacencyvisualization.local.CAGWindow;
import de.bielefeld.uni.cebitec.contigadjacencyvisualization.local.CagController;
import de.bielefeld.uni.cebitec.contigadjacencyvisualization.local.CagCreator;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//de.bielefeld.uni.cebitec.contigadjacencyvisualization//ContigAdjacencyGraphLocalExplorer//EN", autostore = false)
public final class ContigAdjacencyGraphLocalExplorerTopComponent extends TopComponent implements ChangeListener {

    private static ContigAdjacencyGraphLocalExplorerTopComponent instance;

    private static final String PREFERRED_ID = "ContigAdjacencyGraphLocalExplorerTopComponent";

    private CagController viewController;

    public ContigAdjacencyGraphLocalExplorerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ContigAdjacencyGraphLocalExplorerTopComponent.class, "CTL_ContigAdjacencyGraphLocalExplorerTopComponent"));
        setToolTipText(NbBundle.getMessage(ContigAdjacencyGraphLocalExplorerTopComponent.class, "HINT_ContigAdjacencyGraphLocalExplorerTopComponent"));
        ContigAdjacencyPropertiesTopComponent captc = (ContigAdjacencyPropertiesTopComponent) WindowManager.getDefault().findTopComponent("ContigAdjacencyPropertiesTopComponent");
        if (captc != null) {
            captc.addChangeListener(this);
        } else {
            Logger.getLogger(ContigAdjacencyGraphTopComponent.class.getName()).warning("Could not register a change listener.");
        }
        viewController = new CagController();
        jScrollPane1.getViewport().add(viewController.getContigView());
        jScrollPane1.setVisible(true);
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));
    }

    private javax.swing.JScrollPane jScrollPane1;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized ContigAdjacencyGraphLocalExplorerTopComponent getDefault() {
        if (instance == null) {
            instance = new ContigAdjacencyGraphLocalExplorerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ContigAdjacencyGraphLocalExplorerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ContigAdjacencyGraphLocalExplorerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ContigAdjacencyGraphLocalExplorerTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ContigAdjacencyGraphLocalExplorerTopComponent) {
            return (ContigAdjacencyGraphLocalExplorerTopComponent) win;
        }
        Logger.getLogger(ContigAdjacencyGraphLocalExplorerTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        ContigAdjacencyPropertiesTopComponent captc = (ContigAdjacencyPropertiesTopComponent) WindowManager.getDefault().findTopComponent("ContigAdjacencyPropertiesTopComponent");
        if (captc != null) {
            captc.open();
        }
    }

    @Override
    public void componentClosed() {
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof ContigAdjacencyPropertiesTopComponent) {
            ContigAdjacencyPropertiesTopComponent captc = (ContigAdjacencyPropertiesTopComponent) e.getSource();
            this.setLayoutGraph(captc.getCurrentLayoutGraph());
        }
    }

    private void setLayoutGraph(LayoutGraph layoutGraph) {
        viewController.setLayoutGraph(layoutGraph);
        this.invalidate();
        this.revalidate();
    }
}
