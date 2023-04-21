package jimagick.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import jimagick.gui.GUI;
import jimagick.gui.imageframe.ImageFrame;
import jimagick.gui.list.ImageTable;
import jimagick.gui.list.ImgTableModel;
import jimagick.gui.tabbedpane.InfoTable;
import jimagick.gui.tabbedpane.InfoTableModel;
import jimagick.utils.ImageListCell;
import org.apache.log4j.Logger;

/**
 * The listener interface for receiving imageTableKey events. The class that is
 * interested in processing a imageTableKey event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addKeyListener<code> method. When
 * the imageTableKey event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see KeyEvent
 */
public class ImageTableKeyListener implements KeyListener {

    private static final Logger logger = Logger.getLogger(ImageTableKeyListener.class);

    /**
	 * When the enter key is pressed, this method starts the {@link ImageFrame}
	 */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            logger.debug("Typed enter");
            ImageTable table = (ImageTable) e.getSource();
            try {
                ImageFrame frame = new ImageFrame(((ImgTableModel) table.getModel()).getArrayList(), table.getSelectedRow());
                frame.addComponentListener(new ImageFrameComponentListener());
                frame.addWindowListener(new ImageFrameWindowListener());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
	 * When a key is released, this method permits to reload the
	 * {@link InfoTable} model whit the selected image.
	 */
    @Override
    public void keyReleased(KeyEvent e) {
        logger.debug("Key Relased");
        GUI.instance(false).getCatPanel().updateModelBeforeSaving();
        ImageTable table = (ImageTable) e.getSource();
        ImageListCell img = ((ImgTableModel) table.getModel()).getElement(table.getSelectedRow());
        logger.debug("Aggiornamento immagine nell'info table: " + img.getSource().getName());
        GUI.instance(false).getInfoTable().setModel(new InfoTableModel(img));
        GUI.instance(false).getCatModel().reload(img.getCategories(), img);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
