package org.yawlfoundation.yawl.editor.actions.net;

import org.yawlfoundation.yawl.editor.YAWLEditor;
import org.yawlfoundation.yawl.editor.swing.menu.MenuUtilities;
import org.yawlfoundation.yawl.editor.foundations.ResourceLoader;
import org.yawlfoundation.yawl.editor.specification.SpecificationUndoManager;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class NetBackgroundImageAction extends YAWLSelectedNetAction {

    /**
   *
   */
    private static final long serialVersionUID = 1L;

    {
        putValue(Action.SHORT_DESCRIPTION, " Set the net background image. ");
        putValue(Action.NAME, "Set Net Background Image...");
        putValue(Action.LONG_DESCRIPTION, "Set the net background image.");
        putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_I));
        putValue(Action.SMALL_ICON, getPNGIcon("picture"));
        putValue(Action.ACCELERATOR_KEY, MenuUtilities.getAcceleratorKeyStroke("shift B"));
    }

    public NetBackgroundImageAction() {
    }

    public void actionPerformed(ActionEvent event) {
        JFileChooser chooser = new JFileChooser("Select Background Image for Net");
        chooser.setFileFilter(new ImageFilter());
        int result = chooser.showOpenDialog(YAWLEditor.getInstance());
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                String path = chooser.getSelectedFile().getCanonicalPath();
                ImageIcon bgImage = ResourceLoader.getExternalImageAsIcon(path);
                if (bgImage != null) {
                    bgImage.setDescription(path);
                    getGraph().setBackgroundImage(bgImage);
                    SpecificationUndoManager.getInstance().setDirty(true);
                }
            } catch (IOException ioe) {
            }
        }
    }

    public class ImageFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals("tiff") || extension.equals("tif") || extension.equals("gif") || extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        public String getDescription() {
            return "Image Files";
        }

        private String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');
            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }
    }
}
