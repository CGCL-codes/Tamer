package xbrowser.screen;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import xbrowser.XProjectConstants;
import xbrowser.XRepository;
import xbrowser.util.XPluginObject;
import xbrowser.widgets.*;

public class XPluginManagerLayout extends XFrame {

    public XPluginManagerLayout() {
        setTitle(XRepository.getResourceManager().getProperty(this, "Title"));
        XPluginInfoTable plugin_table = new XPluginInfoTable();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(plugin_table), BorderLayout.CENTER);
        getContentPane().add(getButtonsPanel(plugin_table), BorderLayout.SOUTH);
        setIconImage(buildImageIcon(this, "image.FrameIcon").getImage());
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        pack();
        Dimension size = new Dimension(570, 350);
        setSize(size);
    }

    private JPanel getButtonsPanel(XPluginInfoTable plugin_table) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btn_close = buildButton(new CloseAction());
        pnl.add(buildButton(new InstallPluginAction()));
        pnl.add(buildButton(plugin_table.startPluginAction));
        pnl.add(buildButton(plugin_table.stopPluginAction));
        pnl.add(buildButton(plugin_table.uninstallPluginAction));
        pnl.add(buildButton(plugin_table.pluginInfoAction));
        pnl.add(btn_close);
        getRootPane().setDefaultButton(btn_close);
        return pnl;
    }

    private void copyFile(File src_file, File dest_file) {
        InputStream src_stream = null;
        OutputStream dest_stream = null;
        try {
            int b;
            src_stream = new BufferedInputStream(new FileInputStream(src_file));
            dest_stream = new BufferedOutputStream(new FileOutputStream(dest_file));
            while ((b = src_stream.read()) != -1) dest_stream.write(b);
        } catch (Exception e) {
            XRepository.getLogger().warning(this, "Error on copying the plugin file!");
            XRepository.getLogger().warning(this, e);
        } finally {
            try {
                src_stream.close();
                dest_stream.close();
            } catch (Exception ex2) {
            }
        }
    }

    private class InstallPluginAction extends XDefaultAction {

        public InstallPluginAction() {
            super(XPluginManagerLayout.this, "InstallPlugin", null);
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser file_chooser = XRepository.getComponentBuilder().getPluginFileChooser();
            if (file_chooser.showOpenDialog(XPluginManagerLayout.this) == JFileChooser.APPROVE_OPTION) {
                File selected_file = file_chooser.getSelectedFile();
                if ((selected_file != null) && (selected_file.exists())) {
                    File temp_out_file = new File(XProjectConstants.TEMP_DIR + selected_file.getName());
                    File out_file = new File(XProjectConstants.PLUGINS_DIR + selected_file.getName());
                    if (temp_out_file.exists()) temp_out_file.delete();
                    copyFile(selected_file, temp_out_file);
                    if (out_file.exists()) out_file.delete();
                    copyFile(temp_out_file, out_file);
                    XRepository.getPluginManager().addPlugin(new XPluginObject(out_file.getName()));
                    temp_out_file.delete();
                }
            }
        }
    }

    private class CloseAction extends XDefaultAction {

        public CloseAction() {
            super(XPluginManagerLayout.this, "Close", null);
        }

        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }
}
