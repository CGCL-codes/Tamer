package ranab.server.ftp.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import ranab.gui.GuiUtils;
import ranab.gui.TextAreaIo;
import ranab.io.IoUtils;
import ranab.io.StreamConnector;
import ranab.server.ftp.FtpConfig;

/**
 * Ip restrictor panel
 */
public class FtpIpPanel extends PluginPanel {

    private static final String SAVE_IMG = "ranab/server/ftp/gui/save.gif";

    private static final String RELOAD_IMG = "ranab/server/ftp/gui/reload.gif";

    private JTextArea mjIpTxt;

    private FtpConfig mConfig;

    private JTextField mHeaderLab;

    /**
     * Instantiate IP restrictor panel
     */
    public FtpIpPanel(FtpTree tree) {
        super(tree);
        initComponents();
    }

    /**
     * Initialize UI components
     */
    private void initComponents() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(3, 0, 0, 3);
        setLayout(new GridBagLayout());
        int yindex = -1;
        mHeaderLab = new JTextField();
        mHeaderLab.setHorizontalAlignment(JTextField.CENTER);
        mHeaderLab.setColumns(12);
        mHeaderLab.setEditable(false);
        mHeaderLab.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        mHeaderLab.setFont(new Font(null, Font.BOLD, 12));
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 1;
        add(mHeaderLab, gc);
        mjIpTxt = new JTextArea();
        JScrollPane txtPane = new JScrollPane(mjIpTxt, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        txtPane.setPreferredSize(new Dimension(150, 250));
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 1;
        add(txtPane, gc);
        JPanel btnPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton jSaveBtn = new JButton("Save", GuiUtils.createImageIcon(SAVE_IMG));
        btnPane.add(jSaveBtn);
        JButton jResetBtn = new JButton("Reload", GuiUtils.createImageIcon(RELOAD_IMG));
        btnPane.add(jResetBtn);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 1;
        add(btnPane, gc);
        jSaveBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                save();
            }
        });
        jResetBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                refresh(mConfig);
            }
        });
    }

    /**
     * Save IP data
     */
    public void save() {
        FtpConfig cfg = mConfig;
        if (cfg == null) {
            return;
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            is = TextAreaIo.getInputStream(mjIpTxt);
            os = new FileOutputStream(cfg.getIpRestrictor().getFile());
            StreamConnector sc = new StreamConnector(is, os);
            sc.connect();
            if (sc.hasException()) {
                throw sc.getException();
            }
            GuiUtils.showInformationMessage(getTree().getTopFrame(), "Saved ip restrictor file.");
            cfg.getLogger().info("Saved ip restrictor file.");
        } catch (Exception ex) {
            GuiUtils.showErrorMessage(getTree().getTopFrame(), ex.getMessage());
            cfg.getLogger().error(ex);
        } finally {
            IoUtils.close(is);
            IoUtils.close(os);
        }
        cfg.getIpRestrictor().readFile();
    }

    /**
     * Refresh ip data
     */
    public void refresh(FtpConfig cfg) {
        mjIpTxt.setText("");
        if (cfg != null) {
            mjIpTxt.setText("");
            Iterator ipRestrictorIt = cfg.getIpRestrictor().iterator();
            while (ipRestrictorIt.hasNext()) {
                mjIpTxt.append(ipRestrictorIt.next().toString());
                mjIpTxt.append("\n");
            }
            String headerStr = "";
            if (cfg.isAllowIp()) {
                headerStr = "Allow IP Listed";
            } else {
                headerStr = "Ban IP Listed";
            }
            mHeaderLab.setText(headerStr);
        }
        mConfig = cfg;
    }

    /**
     * Not displayable when server stopped
     */
    public boolean isDisplayable() {
        return mConfig != null;
    }
}
