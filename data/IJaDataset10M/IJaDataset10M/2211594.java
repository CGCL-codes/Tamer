package au.gov.qld.dnr.dss.v1.ui.result;

import au.gov.qld.dnr.dss.v1.framework.Framework;
import au.gov.qld.dnr.dss.v1.framework.interfaces.ResourceManager;
import org.swzoo.log2.core.*;
import au.gov.qld.dnr.dss.v1.view.graph.GraphProperties;
import au.gov.qld.dnr.dss.v1.view.graph.BarGraphAppearance;
import au.gov.qld.dnr.dss.v1.view.graph.Settings;
import au.gov.qld.dnr.dss.v1.view.graph.ResultModel;
import au.gov.qld.dnr.dss.v1.util.opd.interfaces.*;
import au.gov.qld.dnr.dss.v1.util.window.*;
import au.gov.qld.dnr.dss.v1.util.opd.worker.*;
import au.gov.qld.dnr.dss.v1.ui.result.interfaces.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import friendless.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 * @author John Farrell (butchered by Maddog mag@netstorm.net.au)
 */
public class PolarSettingsPanel extends JPanel {

    /** Cancel notification. */
    Cancel cancel = null;

    /** the state being edited **/
    protected GraphProperties state;

    protected JTabbedPane tabPane;

    /** the panel containing the Cancel, OK, Apply buttons **/
    protected JPanel buttonPanel;

    protected JButton cancelButton, ok, apply;

    /** where the results of this edit should be written back to **/
    protected GraphPropertiesReceiver writeBack;

    /** Appearance Editor. */
    PolarAppearancePanel appearanceEditor;

    /** Colour edit panel. */
    ColourEditPanel colourEditor;

    /** Root frame. */
    protected Frame frame;

    public PolarSettingsPanel(Cancel cancel, GraphPropertiesReceiver writeBack) {
        setCancel(cancel);
        setWriteBack(writeBack);
        appearanceEditor = new PolarAppearancePanel();
        colourEditor = new ColourEditPanel();
        tabPane = new JTabbedPane();
        String appearanceTabText = resources.getProperty("dss.gui.result.view.properties.tab.appearance", "APPEARANCE");
        String coloursTabText = resources.getProperty("dss.gui.result.view.properties.tab.normal.colours", "COLOURS");
        tabPane.addTab(appearanceTabText, null, appearanceEditor, getAppearanceTabToolTip());
        tabPane.addTab(coloursTabText, null, colourEditor, getColoursTabToolTip());
        buttonPanel = new JPanel(new HCodeLayout("f", 4));
        buttonPanel.add("", cancelButton = new JButton(resources.getProperty("dss.gui.button.dismiss.label", "DISMISS")));
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                cancel();
            }
        });
        buttonPanel.add("x", new JPanel());
        buttonPanel.add(apply = new JButton(resources.getProperty("dss.gui.button.apply.label", "APPLY")));
        apply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                dispatchApply();
            }
        });
        buttonPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);
        add(tabPane, BorderLayout.CENTER);
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
        appearanceEditor.setFrame(frame);
        colourEditor.setFrame(frame);
    }

    void cancel() {
        if (cancel != null) {
            LogTools.trace(logger, 25, "PolarSettingsPanel.cancel() - Cancelling");
            cancel.cancel();
        } else {
            LogTools.warn(logger, "PolarSettingsPanel.cancel() - Oops, cancel reference is null.");
        }
    }

    /**
     * Process an apply.
     */
    void dispatchApply() {
        Container root = WindowUtil.getRoot(this);
        DispatchableLocker locker;
        if (root == null || !(root instanceof Window)) locker = new NullLocker(); else locker = new WindowLocker((Window) root);
        Framework.getGlobalManager().getDispatcher().addItem(new DefaultWorker("PolarSettingsPanel: Apply properties", locker, new Runnable() {

            public void run() {
                apply();
            }
        }));
    }

    /**
     * Provide the tooltip for the appearance tab pane.
     *
     * @return the tooltip.
     */
    public String getAppearanceTabToolTip() {
        return resources.getProperty("dss.gui.result.view.polar.tab.tooltip.appearance", "APPEARANCE OF THE POLAR GRAPH");
    }

    /**
     * Provide the tooltip for the normal colours tab pane.
     *
     * @return the tooltip.
     */
    public String getColoursTabToolTip() {
        return resources.getProperty("dss.gui.result.view.polar.tab.tooltip.colours.normal", "COLOURS OF THE POLAR GRAPH");
    }

    public void setState(GraphProperties state) {
        LogTools.trace(logger, 25, "PolarSettingsPanel.setState()");
        state = (GraphProperties) state.clone();
        this.state = state;
        appearanceEditor.setModel(state.polarAppearance);
        colourEditor.setModel(state.polarColours);
    }

    public void setWriteBack(GraphPropertiesReceiver writeBack) {
        this.writeBack = writeBack;
    }

    public void setCancel(Cancel cancel) {
        this.cancel = cancel;
    }

    public void apply() {
        LogTools.trace(logger, 25, "PolarSettingsPanel.apply() - setState((GraphProperties)state.clone());");
        writeBack.setState((GraphProperties) state.clone());
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource manager. */
    ResourceManager resources = Framework.getGlobalManager().getResourceManager();
}
