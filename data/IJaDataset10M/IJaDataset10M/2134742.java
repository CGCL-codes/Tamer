package freemind.extensions;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.MessageFormat;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import freemind.main.Tools;
import freemind.modes.NodeAdapter;
import freemind.view.mindmapview.MapView;

/**
 * @author foltin
 *
 */
public class ExportHook extends ModeControllerHookAdapter {

    private MapView view;

    /**
     * @param type
     * @param description
     * @return
     */
    protected File chooseFile(String type, String description) {
        Container component = getController().getFrame().getContentPane();
        JFileChooser chooser = null;
        chooser = new JFileChooser();
        File mmFile = getController().getMap().getFile();
        if (mmFile != null) {
            String proposedName = mmFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + "." + type;
            chooser.setSelectedFile(new File(proposedName));
        }
        chooser.addChoosableFileFilter(new ImageFilter(type, description));
        int returnVal = chooser.showSaveDialog(component);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File chosenFile = chooser.getSelectedFile();
        String ext = Tools.getExtension(chosenFile.getName());
        if (!Tools.safeEqualsIgnoreCase(ext, type)) {
            chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + "." + type);
        }
        if (chosenFile.exists()) {
            String overwriteText = MessageFormat.format(getController().getText("file_already_exists"), new Object[] { chosenFile.toString() });
            int overwriteMap = JOptionPane.showConfirmDialog(component, overwriteText, overwriteText, JOptionPane.YES_NO_OPTION);
            if (overwriteMap != JOptionPane.YES_OPTION) {
                return null;
            }
        }
        return chosenFile;
    }

    public static class ImageFilter extends FileFilter {

        private String type;

        private final String description;

        public ImageFilter(String type, String description) {
            this.type = type;
            this.description = description;
        }

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = Tools.getExtension(f.getName());
            return Tools.safeEqualsIgnoreCase(extension, type);
        }

        public String getDescription() {
            return description == null ? type : description;
        }
    }

    public BufferedImage createBufferedImage() {
        view = getController().getView();
        if (view == null) return null;
        NodeAdapter root = (NodeAdapter) getController().getMap().getRoot();
        Rectangle innerBounds = view.getInnerBounds(root.getViewer());
        BufferedImage myImage = (BufferedImage) view.createImage(view.getWidth(), view.getHeight());
        Graphics g = myImage.getGraphics();
        g.clipRect(innerBounds.x, innerBounds.y, innerBounds.width, innerBounds.height);
        view.print(g);
        myImage = myImage.getSubimage(innerBounds.x, innerBounds.y, innerBounds.width, innerBounds.height);
        return myImage;
    }
}
