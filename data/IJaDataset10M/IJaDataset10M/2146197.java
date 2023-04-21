package org.ialbum.ui.swing;

import java.io.Serializable;
import java.util.ResourceBundle;
import java.awt.Image;
import java.awt.MediaTracker;
import javax.swing.JPanel;

public class ImageViewer extends JPanel implements Serializable {

    private static ResourceBundle myResource = ResourceBundle.getBundle(ImageViewer.class.getName());

    public static final int STATE_NO_IMAGE = 0;

    public static final int STATE_IMAGE_LOADING = 1;

    public static final int STATE_IMAGE_RESIZING = 2;

    public static final int STATE_IMAGE_READY = 3;

    public static final int STATE_IMAGE_ERROR = 99;

    private javax.swing.JLabel messageLabel;

    private Image image = null;

    private Image resizedImage = null;

    private Image loadingImage = null;

    private int state = STATE_NO_IMAGE;

    private int _x = 0;

    private int _y = 0;

    private int _previousViewHeight = -1;

    private int _previousViewWidth = -1;

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        setResizedImage(null);
        if (image == null) {
            setState(STATE_NO_IMAGE);
            repaint();
        }
        this.image = image;
    }

    public String test() {
        String text = myResource.getString("noImageMessage.text");
        return text;
    }

    private Image getResizedImage() {
        return this.resizedImage;
    }

    private void setResizedImage(Image resizedImage) {
        this.resizedImage = resizedImage;
    }

    public int getState() {
        return this.state;
    }

    private void setState(int state) {
        this.state = state;
    }

    private int get_x() {
        return _x;
    }

    private void set_x(int value) {
        _x = value;
    }

    private int get_y() {
        return _y;
    }

    private void set_y(int value) {
        _y = value;
    }

    /** Creates a new instance of ImageViewer */
    public ImageViewer() {
        super();
        setLayout(new java.awt.BorderLayout());
        messageLabel = new javax.swing.JLabel();
        add(messageLabel, java.awt.BorderLayout.CENTER);
        messageLabel.setText(null);
        messageLabel.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        evt.getComponent().getParent().dispatchEvent(evt);
    }

    private java.awt.Image getLoadingImage() {
        if (loadingImage == null) {
            String resourceName = getClass().getPackage().getName().replace('.', '/') + "/resources/loadingImage.jpg";
            java.net.URL resourceURL = getClass().getClassLoader().getSystemResource(resourceName);
            if (resourceURL != null) {
                Image image = java.awt.Toolkit.getDefaultToolkit().getImage(resourceURL);
                java.awt.MediaTracker mediaTracker;
                mediaTracker = new java.awt.MediaTracker(this);
                mediaTracker.addImage(image, 0);
                try {
                    mediaTracker.waitForAll();
                } catch (InterruptedException e) {
                }
                ;
                mediaTracker.removeImage(image);
                loadingImage = image;
            }
        }
        return loadingImage;
    }

    public void paint(java.awt.Graphics g) {
        super.paint(g);
        java.awt.Color previousColor = null;
        previousColor = g.getColor();
        int viewHeight = getSize().height;
        int viewWidth = getSize().width;
        if (getImage() != null && getResizedImage() == null) {
            if (getLoadingImage() != null) {
                g.drawImage(getLoadingImage(), viewWidth / 2, viewHeight / 2, this);
            }
            loadImage();
        }
        if ((_previousViewHeight != -1 && viewHeight != _previousViewHeight) || (_previousViewWidth != -1 && viewWidth != _previousViewWidth)) {
            String string = myResource.getString("resizingMessage.text");
            g.drawString(string, viewWidth / 3, viewHeight / 2);
            loadImage();
        }
        g.setColor(java.awt.Color.black);
        switch(getState()) {
            case STATE_NO_IMAGE:
                messageLabel.setText(myResource.getString("noImageMessage.text"));
                break;
            case STATE_IMAGE_LOADING:
                messageLabel.setText(myResource.getString("loadingMessage.text"));
                break;
            case STATE_IMAGE_RESIZING:
                messageLabel.setText(myResource.getString("resizingMessage.text"));
                break;
            case STATE_IMAGE_READY:
                messageLabel.setText(null);
                break;
            case STATE_IMAGE_ERROR:
                messageLabel.setText(myResource.getString("errorMessage.text"));
                break;
            default:
                messageLabel.setText(myResource.getString("unknownStateMessage.text") + getState());
                break;
        }
        if (getState() == STATE_IMAGE_READY) {
            g.drawImage(getResizedImage(), get_x(), get_y(), this);
        }
        g.setColor(previousColor);
    }

    private void loadImage() {
        setState(STATE_IMAGE_LOADING);
        java.awt.MediaTracker mediaTracker;
        mediaTracker = new java.awt.MediaTracker(this);
        mediaTracker.addImage(getImage(), 0);
        try {
            mediaTracker.waitForAll();
        } catch (InterruptedException e) {
        }
        ;
        boolean imageLoadingOk = true;
        if (mediaTracker.isErrorID(0)) {
            setState(STATE_IMAGE_ERROR);
            imageLoadingOk = false;
        }
        mediaTracker.removeImage(getImage());
        if (!imageLoadingOk) {
            return;
        }
        int viewHeight = getSize().height;
        int viewWidth = getSize().width;
        _previousViewHeight = viewHeight;
        _previousViewWidth = viewWidth;
        int initialHeight = getImage().getHeight(this);
        int initialWidth = getImage().getWidth(this);
        int x = 0;
        int y = 0;
        int resizedHeight = 0;
        int resizedWidth = 0;
        float heightWidthRatio = (float) ((float) initialHeight / (float) initialWidth);
        boolean ajustOnWidth = (initialHeight < initialWidth);
        if (ajustOnWidth) {
            resizedWidth = viewWidth;
            resizedHeight = (int) ((float) (heightWidthRatio * (float) resizedWidth));
            if (resizedHeight > viewHeight) {
                resizedHeight = viewHeight;
                resizedWidth = (int) ((float) ((1 / heightWidthRatio) * (float) resizedHeight));
            }
        } else {
            resizedHeight = viewHeight;
            resizedWidth = (int) ((float) ((1 / heightWidthRatio) * (float) resizedHeight));
            if (resizedWidth > viewWidth) {
                resizedWidth = viewWidth;
                resizedHeight = (int) ((float) (heightWidthRatio * (float) resizedWidth));
            }
        }
        if ((resizedHeight > initialHeight) || (resizedWidth > initialWidth)) {
            resizedHeight = initialHeight;
            resizedWidth = initialWidth;
        }
        x = (viewWidth - resizedWidth) / 2;
        y = (viewHeight - resizedHeight) / 2;
        set_x(x);
        set_y(y);
        setState(STATE_IMAGE_RESIZING);
        java.awt.Image resizedImage = getImage().getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_DEFAULT);
        setResizedImage(resizedImage);
        mediaTracker.addImage(getResizedImage(), 0);
        try {
            mediaTracker.waitForAll();
        } catch (InterruptedException e) {
        }
        ;
        mediaTracker.removeImage(getResizedImage());
        setState(STATE_IMAGE_READY);
    }
}
