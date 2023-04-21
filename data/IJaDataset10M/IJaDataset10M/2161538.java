package candyfolds.config.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import candyfolds.CandyFoldsPlugin;
import candyfolds.config.Config;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.Mode;
import org.gjt.sp.jedit.OptionPane;
import org.gjt.sp.jedit.View;
import org.gjt.sp.util.Log;

public class CandyFoldsOptionPane implements OptionPane {

    private static CandyFoldsOptionPane INSTANCE;

    public static CandyFoldsOptionPane getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CandyFoldsOptionPane();
        }
        INSTANCE.reset();
        View view = jEdit.getActiveView();
        if (view != null) {
            Buffer buffer = view.getBuffer();
            if (buffer != null) {
                INSTANCE.modesCB.setSelectedItem(buffer.getMode());
                INSTANCE.setupModeConfigP();
            }
        }
        return INSTANCE;
    }

    private Config config;

    final JPanel panel = new JPanel(new BorderLayout());

    final JComboBox modesCB = new JComboBox();

    final ModeConfigPanel modeConfigP = new ModeConfigPanel();

    final JCheckBox usesDefaultModeConfigCB = new JCheckBox("Use Only " + Config.DEFAULT_MODE_CONFIG_NAME + " Configuration");

    {
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        Box box = new Box(BoxLayout.X_AXIS);
        box.add(new JLabel("Mode: "));
        box.add(box.createHorizontalStrut(3));
        box.add(modesCB);
        box.add(box.createHorizontalStrut(5));
        box.add(usesDefaultModeConfigCB);
        box.add(box.createHorizontalGlue());
        panel.add(box, BorderLayout.NORTH);
        panel.add(modeConfigP.panel);
        modesCB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ev) {
                setupModeConfigP();
            }
        });
        usesDefaultModeConfigCB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ev) {
                String modeName = getSelectedModeConfigName();
                if (modeName == null) return;
                if (usesDefaultModeConfigCB.isSelected()) {
                    config.setModeConfigToDefault(modeName);
                } else {
                    config.setModeConfigToNew(modeName);
                }
                setupModeConfigP(modeName);
            }
        });
    }

    private void setupModeConfigP() {
        setupModeConfigP(getSelectedModeConfigName());
    }

    private void setupModeConfigP(String modeName) {
        if (modeName == null) return;
        if (modeName == Config.DEFAULT_MODE_CONFIG_NAME) {
            modeConfigP.setModeConfig(config.getModeConfig(Config.DEFAULT_MODE_CONFIG_NAME));
            usesDefaultModeConfigCB.setVisible(false);
            modeConfigP.panel.setVisible(true);
        } else {
            usesDefaultModeConfigCB.setVisible(true);
            if (config.usesDefaultModeConfig(modeName)) {
                modeConfigP.panel.setVisible(false);
                usesDefaultModeConfigCB.setSelected(true);
            } else {
                modeConfigP.setModeConfig(config.getModeConfig(modeName));
                modeConfigP.panel.setVisible(true);
                usesDefaultModeConfigCB.setSelected(false);
            }
        }
    }

    private String getSelectedModeConfigName() {
        if (config == null) return null;
        Object mode = modesCB.getSelectedItem();
        if (mode == null) return null;
        String modeConfigName;
        if (mode == Config.DEFAULT_MODE_CONFIG_NAME) return Config.DEFAULT_MODE_CONFIG_NAME;
        return ((Mode) mode).getName();
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public String getName() {
        return "CandyFolds";
    }

    private void reset() {
        config = new Config();
        if (modesCB.getItemCount() == 0) {
            modesCB.addItem(Config.DEFAULT_MODE_CONFIG_NAME);
            for (Mode mode : jEdit.getModes()) modesCB.addItem(mode);
        }
        setupModeConfigP();
    }

    @Override
    public void init() {
    }

    @Override
    public void save() {
        if (config == null) return;
        CandyFoldsPlugin plugin = CandyFoldsPlugin.getInstance();
        if (plugin == null) return;
        modeConfigP.save();
        config.store();
        plugin.setConfig(config);
        reset();
    }
}
