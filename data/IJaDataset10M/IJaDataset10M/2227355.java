package org.dinopolis.gpstool.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.dinopolis.gpstool.GpsylonKeyConstants;
import org.dinopolis.gpstool.plugin.WriteImagePlugin;
import org.dinopolis.gpstool.util.ExtensionFileFilter;
import org.dinopolis.util.Debug;
import org.dinopolis.util.Resources;
import org.dinopolis.util.servicediscovery.ServiceDiscovery;

/**
 * This frame is able to display one image and allows to save it.
 *
 * @author Christof Dallermassl
 * @version $Revision: 750 $
 */
public class ImageFrame extends JFrame implements ActionListener, GpsylonKeyConstants {

    Resources resources_;

    public static final String COMMAND_CLOSE = "close";

    public static final String COMMAND_SAVE = "save";

    ImageIcon image_icon_;

    BufferedImage image_;

    JLabel image_label_;

    JFileChooser file_chooser_;

    ServiceDiscovery service_discovery_;

    Object[] plugins_;

    public ImageFrame(Resources resources, String frame_title, BufferedImage image, ServiceDiscovery service_discovery) {
        resources_ = resources;
        service_discovery_ = service_discovery;
        setTitle(frame_title);
        Container content_pane = getContentPane();
        JPanel south_panel = new JPanel();
        image_icon_ = new ImageIcon(image);
        image_label_ = new JLabel(image_icon_);
        JScrollPane scroll_pane = new JScrollPane(image_label_);
        content_pane.add(scroll_pane, BorderLayout.CENTER);
        JButton close_button = new JButton(resources_.getString(KEY_LOCALIZE_CLOSE_BUTTON));
        close_button.setActionCommand(COMMAND_CLOSE);
        close_button.addActionListener(this);
        south_panel.add(close_button);
        JButton save_button = new JButton(resources_.getString(KEY_LOCALIZE_SAVE_BUTTON));
        save_button.setActionCommand(COMMAND_SAVE);
        save_button.addActionListener(this);
        south_panel.add(save_button);
        content_pane.add(south_panel, BorderLayout.SOUTH);
        pack();
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
            }
        });
        plugins_ = service_discovery_.getServices(org.dinopolis.gpstool.plugin.WriteImagePlugin.class);
        if (Debug.DEBUG) Debug.println("plugin", "plugins for writing image detected: " + Debug.objectToString(plugins_));
        if (plugins_.length == 0) {
            save_button.setEnabled(false);
        }
    }

    public void setImage(BufferedImage image) {
        image_ = image;
        image_icon_.setImage(image);
        repaint();
    }

    public BufferedImage getImage() {
        return (image_);
    }

    /**
 * Action Listener Method
 * 
 * @param event the action event
 */
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals(COMMAND_CLOSE)) {
            dispose();
            setVisible(false);
            return;
        }
        if (event.getActionCommand().equals(COMMAND_SAVE)) {
            saveAs();
            return;
        }
    }

    protected void saveAs() {
        if (file_chooser_ == null) {
            file_chooser_ = new JFileChooser();
            file_chooser_.setAcceptAllFileFilterUsed(false);
            file_chooser_.setMultiSelectionEnabled(false);
            file_chooser_.setFileHidingEnabled(false);
            ExtensionFileFilter filter;
            WriteImagePlugin plugin;
            String[] extensions;
            boolean plugin_found = false;
            for (int plugin_count = 0; plugin_count < plugins_.length; plugin_count++) {
                plugin = (WriteImagePlugin) plugins_[plugin_count];
                if (plugin != null) {
                    filter = new ExtensionFileFilter();
                    extensions = plugin.getContentFileExtensions();
                    for (int extension_count = 0; extension_count < extensions.length; extension_count++) filter.addExtension(extensions[extension_count]);
                    filter.setDescription(plugin.getContentDescription());
                    filter.setAuxiliaryObject(plugin);
                    file_chooser_.addChoosableFileFilter(filter);
                    plugin_found = true;
                }
            }
            if (!plugin_found) {
                System.err.println("ERROR: no plugin found!");
                return;
            }
        }
        int result = file_chooser_.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                ExtensionFileFilter filter = (ExtensionFileFilter) file_chooser_.getFileFilter();
                WriteImagePlugin plugin = (WriteImagePlugin) filter.getAuxiliaryObject();
                File file = file_chooser_.getSelectedFile();
                String format = ((ExtensionFileFilter) file_chooser_.getFileFilter()).getExtension(file);
                if (format == null) {
                    format = plugin.getContentFileExtensions()[0];
                    file = new File(file.getPath() + "." + format);
                }
                plugin.write(image_label_, new FileOutputStream(file));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
