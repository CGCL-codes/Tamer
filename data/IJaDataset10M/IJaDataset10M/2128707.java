package canvas.view.locator;

import canvas.XCanvasGlobal;
import canvas.view.filechooser.ImageFileChangedHandler;
import canvas.view.filechooser.ImageFileFilter;
import canvas.view.helper.ActionInitiator;
import canvas.view.helper.ImageHelper;
import canvas.view.persistence.PointPO;
import canvas.view.persistence.RectanglePO;
import canvas.view.persistence.XResizableImageWidgetPO;
import canvas.view.widget.XObjectScene;
import canvas.view.widget.XResizableImageWidget;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Isaac
 */
public class XResizableImageWidgetLocator implements XWidgetLocator {

    public void locate(XObjectScene objectScene, Point location, Rectangle bounds) {
        objectScene.setXWidgetLocator(null);
        JFileChooser chooser = initImageFileChooser();
        int option = chooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
            File file = chooser.getSelectedFile();
            if (file.canRead()) {
                BufferedImage bimg = ImageHelper.readImage(file);
                if (bimg != null) {
                    XResizableImageWidgetPO widgetPO = new XResizableImageWidgetPO();
                    widgetPO.setImageBase64Text(ImageHelper.toBase64Text(bimg));
                    if (bounds != null) {
                        widgetPO.setPreferredBounds(new RectanglePO(bounds));
                    }
                    widgetPO.setPreferredLocation(new PointPO(location));
                    XResizableImageWidget imageWidget = widgetPO.createWidget(objectScene);
                    ActionInitiator.initDefaultAction(objectScene, objectScene.getMainLayer(), objectScene.getConnLayer(), imageWidget);
                } else {
                    JOptionPane.showMessageDialog(null, "Not an image.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Can't read this file:" + file);
            }
        }
        objectScene.setXWidgetLocator(null);
    }

    private static JFileChooser initImageFileChooser() {
        ImageFileChangedHandler selectedImageFileChangedHandler = new ImageFileChangedHandler();
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(XCanvasGlobal.IMAGE_FILE_PATH));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new ImageFileFilter());
        chooser.setAccessory(selectedImageFileChangedHandler);
        chooser.addPropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, selectedImageFileChangedHandler);
        return chooser;
    }
}
