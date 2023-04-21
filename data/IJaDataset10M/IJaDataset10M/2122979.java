package org.matsim.utils.vis.otfvis.opengl.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.matsim.gbl.Gbl;
import org.matsim.utils.vis.otfvis.gui.OTFHostControlBar;
import org.matsim.utils.vis.otfvis.gui.OTFVisConfig;
import org.matsim.utils.vis.otfvis.gui.PreferencesDialog;
import org.matsim.utils.vis.otfvis.opengl.queries.QueryToggleShowParking;

public class PreferencesDialog2 extends PreferencesDialog implements ItemListener {

    public PreferencesDialog2(JFrame frame, OTFVisConfig config, OTFHostControlBar mother) {
        super(frame, config, mother);
    }

    private static final long serialVersionUID = 5778562849300898138L;

    @Override
    protected void initGUI() {
        super.initGUI();
        {
            JPanel panel = new JPanel(null);
            getContentPane().add(panel);
            panel.setBorder(BorderFactory.createTitledBorder("Switches"));
            panel.setBounds(250, 130, 220, 160);
            JCheckBox SynchBox;
            if (host.isLiveHost()) {
                SynchBox = new JCheckBox("show parked vehicles");
                SynchBox.setSelected(cfg.isShowParking());
                SynchBox.addItemListener(this);
                SynchBox.setBounds(10, 20, 200, 31);
                SynchBox.setVisible(true);
                panel.add(SynchBox);
            }
            if ((host.isLiveHost()) || ((cfg.getFileVersion() >= 1) && (cfg.getFileMinorVersion() >= 4))) {
                SynchBox = new JCheckBox("show link Ids");
                SynchBox.setSelected(cfg.drawLinkIds());
                SynchBox.addItemListener(this);
                SynchBox.setBounds(10, 40, 200, 31);
                SynchBox.setVisible(true);
                panel.add(SynchBox);
            }
            SynchBox = new JCheckBox("show overlays");
            SynchBox.setSelected(cfg.drawOverlays());
            SynchBox.addItemListener(this);
            SynchBox.setBounds(10, 60, 200, 31);
            SynchBox.setVisible(true);
            panel.add(SynchBox);
            SynchBox = new JCheckBox("show time GL");
            SynchBox.setSelected(cfg.drawTime());
            SynchBox.addItemListener(this);
            SynchBox.setBounds(10, 80, 200, 31);
            panel.add(SynchBox);
            SynchBox = new JCheckBox("save jpg frames");
            SynchBox.setSelected(cfg.renderImages());
            SynchBox.addItemListener(this);
            SynchBox.setBounds(10, 100, 200, 31);
            panel.add(SynchBox);
            SynchBox = new JCheckBox("allow caching");
            SynchBox.setSelected(cfg.isCachingAllowed());
            SynchBox.addItemListener(this);
            SynchBox.setBounds(10, 120, 200, 31);
            panel.add(SynchBox);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        this.cfg = ((OTFVisConfig) Gbl.getConfig().getModule("otfvis"));
        JCheckBox source = (JCheckBox) e.getItemSelectable();
        if (source.getText().equals("show parked vehicles")) {
            cfg.setShowParking(e.getStateChange() != ItemEvent.DESELECTED);
            cfg.setShowParking(!cfg.isShowParking());
            if (host != null) {
                host.doQuery(new QueryToggleShowParking());
                host.clearCaches();
                host.redrawHandlers();
            }
        } else if (source.getText().equals("show link Ids")) {
            cfg.setDrawLinkIds(!cfg.drawLinkIds());
        } else if (source.getText().equals("show overlays")) {
            cfg.setDrawOverlays(!cfg.drawOverlays());
        } else if (source.getText().equals("save jpg frames")) {
            cfg.setRenderImages(!cfg.renderImages());
        } else if (source.getText().equals("show time GL")) {
            cfg.setDrawTime(!cfg.drawTime());
        } else if (source.getText().equals("allow caching")) {
            cfg.setCachingAllowed(!cfg.isCachingAllowed());
        }
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame frame = new JFrame();
                PreferencesDialog2 inst = new PreferencesDialog2(frame, new OTFVisConfig(), null);
                inst.setVisible(true);
            }
        });
    }
}
