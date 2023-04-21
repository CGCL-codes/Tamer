package net.aa3sd.SMT.ui;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import org.apache.log4j.Logger;

/**
 * Drag and drop support for images onto/off of JButton and JLabel
 * To use: 
 * <code>
 		JLabel lblImage = new JLabel(imageIcon);
		lblImage.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JComponent component = (JComponent) mouseEvent.getSource();
				TransferHandler handler = component.getTransferHandler();
				handler.exportAsDrag(component, mouseEvent, TransferHandler.COPY);
			}
		} );
 </code>
 * 
 * @author Paul J. Morris
 *
 */
public class ButtonImageTransferHandler extends TransferHandler implements Transferable {

    private static final long serialVersionUID = -7810066828881970832L;

    private static final Logger log = Logger.getLogger(ButtonImageTransferHandler.class);

    private static final DataFlavor uriList = new DataFlavor("text/uri-list; class=java.lang.String; charset=Unicode", null);

    private static final DataFlavor flavors[] = { DataFlavor.imageFlavor, uriList };

    private Image image;

    private ArrayList<ImageChangeListener> imageChangeListeners;

    /**
     * Default constructor. 
     */
    public ButtonImageTransferHandler() {
        imageChangeListeners = new ArrayList<ImageChangeListener>();
    }

    public void registerImageChangeListener(ImageChangeListener listener) {
        imageChangeListeners.add(listener);
    }

    private void notifyListeners() {
        Iterator<ImageChangeListener> i = imageChangeListeners.iterator();
        while (i.hasNext()) {
            i.next().imageChanged();
        }
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        boolean result = false;
        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(flavor)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return image;
        }
        return null;
    }

    @Override
    public int getSourceActions(JComponent component) {
        return TransferHandler.COPY;
    }

    @Override
    public boolean canImport(JComponent component, DataFlavor[] transferFlavors) {
        boolean result = false;
        if ((component instanceof JLabel) || (component instanceof AbstractButton)) {
            for (int i = 0; i < transferFlavors.length; i++) {
                for (int j = 0; j < flavors.length; j++) {
                    if (transferFlavors[i].equals(flavors[j])) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    @Override
    protected Transferable createTransferable(JComponent component) {
        image = null;
        Transferable result = null;
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            Icon icon = label.getIcon();
            if (icon instanceof ImageIcon) {
                image = ((ImageIcon) icon).getImage();
                result = this;
            }
        } else if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            Icon icon = button.getIcon();
            if (icon instanceof ImageIcon) {
                image = ((ImageIcon) icon).getImage();
                result = this;
            }
        }
        return result;
    }

    @Override
    public boolean importData(JComponent component, Transferable transferable) {
        boolean success = false;
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            if (transferable.isDataFlavorSupported(flavors[0])) {
                try {
                    image = (Image) transferable.getTransferData(flavors[0]);
                    ImageIcon icon = new ImageIcon(image);
                    label.setIcon(icon);
                    success = true;
                } catch (UnsupportedFlavorException e) {
                    log.error(e.getMessage());
                } catch (IOException e1) {
                    log.error(e1);
                }
            } else if (transferable.isDataFlavorSupported(flavors[1])) {
                try {
                    String stream = (String) transferable.getTransferData(flavors[1]);
                    image = ImageIO.read(new URL(stream));
                    ImageIcon icon = new ImageIcon(image);
                    label.setIcon(icon);
                    label.invalidate();
                    success = true;
                } catch (UnsupportedFlavorException e) {
                    log.error(e.getMessage());
                } catch (IOException e1) {
                    log.error(e1);
                }
            } else {
                log.debug("Trying to drop an transportable of an unsuported flavor: " + Arrays.toString(transferable.getTransferDataFlavors()));
            }
        } else if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            if (transferable.isDataFlavorSupported(flavors[0])) {
                try {
                    image = (Image) transferable.getTransferData(flavors[0]);
                    ImageIcon icon = new ImageIcon(image);
                    button.setIcon(icon);
                    success = true;
                } catch (UnsupportedFlavorException e) {
                    log.error(e.getMessage());
                } catch (IOException e1) {
                    log.error(e1);
                }
            } else {
                log.debug("Trying to drop an transportable of an unsuported flavor: " + Arrays.toString(transferable.getTransferDataFlavors()));
            }
        }
        if (success) {
            notifyListeners();
        }
        return success;
    }
}
