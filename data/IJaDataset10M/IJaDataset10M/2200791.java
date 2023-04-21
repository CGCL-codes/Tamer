package wjhk.jupload2.gui.image;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.filedata.PictureFileData;
import wjhk.jupload2.policies.UploadPolicy;

/**
 * This panel is used to preview picture, when PictureUploadPolicy (or one of
 * its inherited policy) is used. Manages the panel where pictures are
 * displayed. <BR>
 * Each time a user selects a file in the panel file, the PictureUploadPolicy
 * calls
 * {@link #setPictureFile(PictureFileData, AbstractButton, AbstractButton)}. I
 * did an attempt to store the Image generated for the Panel size into the
 * PictureFileData, to avoid to calculate the offscreenPicture each time the
 * user select the same file again. But it doesn't work: the applet quickly runs
 * out of memory, even after numerous calls of System.gc and finalize. <BR>
 * <BR>
 * This file is taken from the PictureApplet ((C) 2002 Guillaume
 * Chamberland-Larose), available here: To contact Guillaume Chamberland-Larose
 * for bugs, patches, suggestions: Please use the forums on the sourceforge web
 * page for this project, located at:
 * http://sourceforge.net/projects/picture-applet/ Updated : 2006 etienne_sf<BR>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. <BR>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. <BR>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
public class PicturePanel extends Canvas implements MouseListener, ComponentListener {

    /** A generated serialVersionUID, to avoid warning during compilation */
    private static final long serialVersionUID = -3439340009940699981L;

    private PictureFileData pictureFileData;

    /**
     * offscreenImage contains an image, that can be asked by
     * {@link PictureFileData#getImage(Canvas, boolean)}. It is used to preview
     * this picture.
     */
    private Image offscreenImage = null;

    /**
     * Indicates if the offscreen image should be calculated once and stored, to
     * avoid to calculate it again. <BR>
     * Indications: the offscreen image should be calculate only once for the
     * picturePanel on the applet, and for each display when the user ask to
     * display the fulscreen picture (by a click on the picturePanel).
     */
    private boolean hasToStoreOffscreenPicture = false;

    /**
     * The current upload policy.
     */
    protected UploadPolicy uploadPolicy;

    /**
     * Standard constructor.
     * 
     * @param hasToStoreOffscreenPicture
     * @param uploadPolicy The current upload policy
     */
    public PicturePanel(boolean hasToStoreOffscreenPicture, UploadPolicy uploadPolicy) {
        super();
        this.hasToStoreOffscreenPicture = hasToStoreOffscreenPicture;
        this.uploadPolicy = uploadPolicy;
        addMouseListener(this);
        addComponentListener(this);
    }

    /**
     * This setter is called by {@link PictureFileData} to set the picture that
     * is to be previewed.
     * 
     * @param pictureFileData The FileData for the image to be displayed. Null
     *            if no picture should be displayed.
     * @param button1 A button that will be activated or not, depending of the
     *            pictures was correctly set into the panel. May be null, if not
     *            button is to be enabled.
     * @param button2 Another button that will be activated or not. May also be
     *            null.
     */
    public void setPictureFile(PictureFileData pictureFileData, AbstractButton button1, AbstractButton button2) {
        this.pictureFileData = null;
        if (this.offscreenImage != null) {
            this.offscreenImage.flush();
            this.offscreenImage = null;
        }
        repaint();
        boolean enableButton = false;
        if (pictureFileData != null && pictureFileData.canRead()) {
            this.pictureFileData = pictureFileData;
            enableButton = true;
            calculateOffscreenImage();
            repaint();
        }
        if (button1 != null) {
            button1.setEnabled(enableButton);
        }
        if (button2 != null) {
            button2.setEnabled(enableButton);
        }
    }

    /**
     * @see java.awt.Canvas#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        if (this.offscreenImage != null) {
            int hMargin = (getWidth() - this.offscreenImage.getWidth(this)) / 2;
            int vMargin = (getHeight() - this.offscreenImage.getHeight(this)) / 2;
            g.drawImage(this.offscreenImage, hMargin, vMargin, this);
            this.offscreenImage.flush();
        } else {
            this.uploadPolicy.displayDebug("PicturePanel.paint(): offscreenImage is null", 50);
        }
    }

    /**
     * This function adds a quarter rotation to the current picture.
     * 
     * @param quarter Number of quarters (90�) the picture should rotate. 1
     *            means rotating of 90� clockwise (?). Can be negative
     *            (counterclockwise), more than 1...
     */
    public void rotate(int quarter) {
        if (this.pictureFileData != null) {
            Cursor previousCursor = this.uploadPolicy.setWaitCursor();
            this.pictureFileData.addRotation(quarter);
            this.offscreenImage.flush();
            this.offscreenImage = null;
            calculateOffscreenImage();
            repaint();
            this.uploadPolicy.setCursor(previousCursor);
        } else {
            this.uploadPolicy.displayWarn("Hum, this is really strange: there is no pictureFileData in the PicturePanel! Command is ignored.");
        }
    }

    /**
     * This method get the offscreenImage from the current pictureFileData. This
     * image is null, if pictureFileData is null. In this case, the repaint will
     * only clear the panel rectangle, on the screen.
     */
    private void calculateOffscreenImage() {
        if (this.pictureFileData == null) {
            if (this.offscreenImage != null) {
                this.offscreenImage = null;
                this.uploadPolicy.displayWarn("PicturePanel.calculateOffscreenImage(): pictureFileData is null (offscreenImage set to null");
            }
        } else if (this.offscreenImage == null) {
            this.uploadPolicy.displayDebug("PicturePanel.calculateOffscreenImage(): trying to calculate offscreenImage (PicturePanel.calculateOffscreenImage()", 30);
            try {
                this.offscreenImage = this.pictureFileData.getImage(this, this.hasToStoreOffscreenPicture);
            } catch (JUploadException e) {
                this.uploadPolicy.displayErr(e);
                this.pictureFileData = null;
                this.offscreenImage = null;
            }
        }
    }

    /**
     * Is it really useful ??
     */
    @Override
    protected void finalize() throws Throwable {
        this.uploadPolicy.displayDebug("Within PicturePanel.finalize()", 10);
        if (this.offscreenImage != null) {
            this.offscreenImage.flush();
        }
    }

    /** @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent) */
    public void mouseClicked(MouseEvent arg0) {
        if (this.pictureFileData != null) {
            this.uploadPolicy.onFileDoubleClicked(this.pictureFileData);
        }
    }

    /** @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent) */
    public void mouseEntered(MouseEvent arg0) {
    }

    /** @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent) */
    public void mouseExited(MouseEvent arg0) {
    }

    /** @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent) */
    public void mousePressed(MouseEvent arg0) {
    }

    /** @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent) */
    public void mouseReleased(MouseEvent arg0) {
    }

    /**
     * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
     */
    public void componentHidden(ComponentEvent arg0) {
    }

    /**
     * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
     */
    public void componentMoved(ComponentEvent arg0) {
    }

    /**
     * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
     */
    public void componentResized(ComponentEvent arg0) {
        this.uploadPolicy.displayDebug("Within componentResized", 10);
        if (this.offscreenImage != null) {
            this.offscreenImage.flush();
            this.offscreenImage = null;
        }
        calculateOffscreenImage();
        repaint();
    }

    /**
     * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
     */
    public void componentShown(ComponentEvent arg0) {
    }
}
