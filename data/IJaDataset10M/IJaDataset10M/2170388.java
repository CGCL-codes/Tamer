package org.jimcat.gui.perspective.boards.thumbnail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jimcat.gui.ViewControl;
import org.jimcat.gui.perspective.AbstractPerspective;
import org.jimcat.gui.perspective.boards.Board;
import org.jimcat.model.Thumbnail;

/**
 * A board showing thumbnails
 * 
 * $Id$
 * 
 * @author Herbert
 */
public class ThumbnailPerspective extends AbstractPerspective {

    /**
	 * the board used to display items
	 */
    private Board board;

    /**
	 * the slider to adjuste thumbnail size
	 */
    private JSlider slider;

    /**
	 * a item used to calculate dimensions
	 */
    private WheelListThumbnail reverenceThumbnail;

    /**
	 * @param control
	 */
    public ThumbnailPerspective(ViewControl control) {
        super(control);
        initComponents();
    }

    /**
	 * build up component hierarchie
	 */
    private void initComponents() {
        setLayout(new BorderLayout());
        ThumbnailFactory factory = new ThumbnailFactory();
        reverenceThumbnail = (WheelListThumbnail) factory.getNewItem();
        board = new Board(getViewControl(), factory);
        Dimension thumb = new Dimension(Thumbnail.MAX_THUMBNAIL_SIZE, Thumbnail.MAX_THUMBNAIL_SIZE);
        setThumbnailSize(thumb);
        add(board, BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new FlowLayout());
        slider = new JSlider();
        slider.setOpaque(false);
        slider.setMinimum(40);
        slider.setMaximum(100);
        slider.setMajorTickSpacing(1);
        slider.setFocusable(false);
        ChangeHandler handler = new ChangeHandler();
        slider.addChangeListener(handler);
        bottom.addMouseWheelListener(handler);
        bottom.add(slider);
        add(bottom, BorderLayout.SOUTH);
        slider.setValue((slider.getMinimum() + slider.getMaximum()) / 2);
    }

    /**
	 * use this methode to set Thumbnail size to specified value
	 * 
	 * @param size
	 */
    private void setThumbnailSize(Dimension size) {
        reverenceThumbnail.setGraphicSize(size);
        board.setItemSize(reverenceThumbnail.getPreferredSize());
    }

    /**
	 * react on a slider move
	 * 
	 * $Id: ThumbnailPerspective.java 760 2007-05-22 16:44:17Z 07g1t1u1 $
	 * 
	 * @author Herbert
	 */
    class ChangeHandler implements ChangeListener, MouseWheelListener {

        /**
		 * react on slider changes
		 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
		 */
        public void stateChanged(ChangeEvent e) {
            JSlider sl = (JSlider) e.getSource();
            int value = sl.getValue();
            float perc = value / (float) sl.getMaximum();
            int width = (int) (Thumbnail.MAX_THUMBNAIL_SIZE * perc);
            int height = (int) (Thumbnail.MAX_THUMBNAIL_SIZE * perc);
            setThumbnailSize(new Dimension(width, height));
            repaint();
        }

        /**
		 * react on mouse wheel changed
		 * 
		 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
		 */
        public void mouseWheelMoved(MouseWheelEvent e) {
            int value = slider.getValue();
            if (e.getWheelRotation() < 0) {
                slider.setValue(value - 5);
            } else {
                slider.setValue(value + 5);
            }
        }
    }

    /**
     * disable library view observing
     * @see org.jimcat.gui.perspective.AbstractPerspective#disablePerspective()
     */
    @Override
    protected void disablePerspective() {
        board.setActive(false);
    }

    /**
     * enalbe library view observing
     * @see org.jimcat.gui.perspective.AbstractPerspective#enablePerspective()
     */
    @Override
    protected void enablePerspective() {
        board.setActive(true);
    }
}
