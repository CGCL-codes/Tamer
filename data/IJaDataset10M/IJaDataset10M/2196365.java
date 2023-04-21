package com.yov.scanner.imageprocessing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener {

    private BufferedImage displayImage;

    private BufferedImage croppedImage;

    private boolean isMoved;

    private int originalX;

    private int originalY;

    private int clickX, clickY;

    private int dragX, dragY;

    private boolean isClicked, isDragged, isDragging, isFocused, isCropped;

    private int imgWidth;

    private int imgHeight;

    private String imgFileName;

    private int dragWidth, dragHeight;

    public ImagePanel(BufferedImage newImage) {
        super();
        displayImage = newImage;
        imgWidth = displayImage.getWidth();
        imgHeight = displayImage.getHeight();
        croppedImage = null;
        originalX = 0;
        originalY = 0;
        isMoved = false;
        clickX = -1;
        clickY = -1;
        dragX = -1;
        dragY = -1;
        isClicked = false;
        isDragged = false;
        isDragging = false;
        isFocused = false;
        isCropped = false;
        addMouseListener(this);
        addMouseMotionListener(this);
        this.setSize(displayImage.getWidth(), displayImage.getHeight());
        this.setMaximumSize(new Dimension(displayImage.getWidth(), displayImage.getHeight()));
        this.setMinimumSize(new Dimension(displayImage.getWidth(), displayImage.getHeight()));
        this.setPreferredSize(new Dimension(displayImage.getWidth(), displayImage.getHeight()));
    }

    public ImagePanel(String newImageFileName) {
        super();
        try {
            displayImage = ImageIO.read(new File(newImageFileName));
        } catch (Exception e) {
            System.out.println("Can not open Image File");
            e.printStackTrace();
        }
        imgWidth = displayImage.getWidth();
        imgHeight = displayImage.getHeight();
        imgFileName = newImageFileName;
        croppedImage = null;
        originalX = 0;
        originalY = 0;
        isMoved = false;
        clickX = -1;
        clickY = -1;
        dragX = -1;
        dragY = -1;
        isClicked = false;
        isDragged = false;
        isDragging = false;
        isFocused = false;
        isCropped = false;
        addMouseListener(this);
        addMouseMotionListener(this);
        this.setSize(displayImage.getWidth(), displayImage.getHeight());
        this.setMaximumSize(new Dimension(displayImage.getWidth(), displayImage.getHeight()));
        this.setMinimumSize(new Dimension(displayImage.getWidth(), displayImage.getHeight()));
        this.setPreferredSize(new Dimension(displayImage.getWidth(), displayImage.getHeight()));
    }

    public void setImage(BufferedImage newImage) {
        displayImage = newImage;
        imgWidth = displayImage.getWidth();
        imgHeight = displayImage.getHeight();
    }

    public int getImageWidth() {
        return imgWidth;
    }

    public int getImageHeight() {
        return imgHeight;
    }

    public BufferedImage getCroppedImage() {
        return croppedImage;
    }

    public boolean isCropped() {
        return isCropped;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public boolean isDragged() {
        return isDragged;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public boolean isFocused() {
        return isFocused;
    }

    public int getDragWidth() {
        return dragWidth;
    }

    public int getDragHeight() {
        return dragHeight;
    }

    public boolean setIsClicked(boolean newIsClicked) {
        boolean oldValue = isClicked;
        isClicked = newIsClicked;
        return oldValue;
    }

    public boolean setIsDragged(boolean newIsDragged) {
        boolean oldValue = isDragged;
        isDragged = newIsDragged;
        return oldValue;
    }

    public boolean setIsDragging(boolean newIsDragging) {
        boolean oldValue = isDragging;
        isDragging = newIsDragging;
        return oldValue;
    }

    public boolean setIsFocused(boolean newIsFocused) {
        boolean oldValue = isFocused;
        isFocused = newIsFocused;
        return oldValue;
    }

    @Override
    public void paint(Graphics g) {
        if (displayImage != null) {
            if (!isMoved) {
                Point originalLoc = this.getLocation();
                originalX = (int) originalLoc.getX();
                originalY = (int) originalLoc.getY();
                isMoved = true;
            }
            int dispX = 0;
            int dispY = 0;
            dragWidth = dragX - clickX;
            dragHeight = dragY - clickY;
            int paneHeight = this.getHeight();
            int paneWidth = this.getWidth();
            if (!isFocused) {
                if ((paneHeight > imgHeight) || (paneWidth > imgWidth)) {
                    dispX = (int) ((paneWidth / 2.0f) - (imgWidth / 2.0f));
                    dispY = (int) ((paneHeight / 2.0f) - (imgHeight / 2.0f));
                    g.drawImage(displayImage, dispX + originalX, dispY + originalY, null);
                } else {
                    g.drawImage(displayImage, 0, 0, null);
                }
            } else {
                if ((paneHeight > imgHeight) || (paneWidth > imgWidth)) {
                    dispX = (int) ((paneWidth / 2.0f) - (imgWidth / 2.0f));
                    dispY = (int) ((paneHeight / 2.0f) - (imgHeight / 2.0f));
                    g.drawImage(displayImage, dispX + originalX, dispY + originalY, 2 * dragWidth, 2 * dragHeight, clickX, clickY, dragX, dragY, null);
                } else {
                    g.drawImage(displayImage, 0, 0, 2 * dragWidth, 2 * dragHeight, clickX, clickY, dragX, dragY, null);
                }
                System.out.println("painted 1");
            }
            if (isClicked) {
                System.out.println(" -- Paint { isClicked } --");
                System.out.println("Draw Clicked Oval");
                g.setColor(Color.BLUE);
                g.drawOval(clickX, clickY, 3, 3);
            }
            if (isDragging) {
                g.setXORMode(Color.CYAN);
                if (dragWidth >= 0 && dragHeight >= 0) {
                    g.drawRect(clickX, clickY, dragWidth, dragHeight);
                }
                if (dragWidth < 0 && dragHeight >= 0) {
                    g.drawRect(dragX, clickY, -dragWidth, dragHeight);
                }
                if (dragWidth >= 0 && dragHeight < 0) {
                    g.drawRect(clickX, dragY, dragWidth, -dragHeight);
                }
                if (dragWidth < 0 && dragHeight < 0) {
                    g.drawRect(dragX, dragY, -dragWidth, -dragHeight);
                }
            }
            if (isDragged && !isFocused) {
                g.setXORMode(Color.WHITE);
                if (dragWidth >= 0 && dragHeight >= 0) {
                    g.drawRect(clickX, clickY, dragWidth, dragHeight);
                }
                if (dragWidth < 0 && dragHeight >= 0) {
                    g.drawRect(dragX, clickY, -dragWidth, dragHeight);
                }
                if (dragWidth >= 0 && dragHeight < 0) {
                    g.drawRect(clickX, dragY, dragWidth, -dragHeight);
                }
                if (dragWidth < 0 && dragHeight < 0) {
                    g.drawRect(dragX, dragY, -dragWidth, -dragHeight);
                }
            }
        }
    }

    public void cropImage() {
        isCropped = false;
        croppedImage = null;
        dragWidth = dragX - clickX;
        dragHeight = dragY - clickY;
        if (dragWidth >= 0 && dragHeight >= 0) {
            croppedImage = displayImage.getSubimage(clickX, clickY, dragWidth, dragHeight);
        }
        if (dragWidth < 0 && dragHeight >= 0) {
            croppedImage = displayImage.getSubimage(dragX, clickY, -dragWidth, dragHeight);
        }
        if (dragWidth >= 0 && dragHeight < 0) {
            croppedImage = displayImage.getSubimage(clickX, dragY, dragWidth, -dragHeight);
        }
        if (dragWidth < 0 && dragHeight < 0) {
            croppedImage = displayImage.getSubimage(dragX, dragY, -dragWidth, -dragHeight);
        }
        if (croppedImage != null) {
            try {
                ImageIO.write(croppedImage, "jpg", new File(imgFileName));
                isCropped = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clickX = e.getX();
        clickY = e.getY();
        isClicked = true;
        isDragging = false;
        isDragged = false;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (isDragging) {
            int exitX = e.getX();
            int exitY = e.getY();
            if (exitX < 0) {
                dragX = 0;
            }
            if (exitY < 0) {
                dragY = 0;
            }
            if (exitX >= imgWidth) {
                dragX = imgWidth - 1;
            }
            if (exitY >= imgHeight) {
                dragY = imgHeight - 1;
            }
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        clickX = e.getX();
        clickY = e.getY();
        isDragging = true;
        isDragged = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isDragging) {
            isDragging = false;
            isDragged = true;
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
        ((JPanel) e.getSource()).scrollRectToVisible(r);
        dragX = e.getX();
        dragY = e.getY();
        isDragging = true;
        isClicked = false;
        if (isDragging) {
            if (dragX >= imgWidth) {
                dragX = imgWidth - 1;
            }
            if (dragY >= imgHeight) {
                dragY = imgHeight - 1;
            }
            if (dragX < 0) {
                dragX = 0;
            }
            if (dragY < 0) {
                dragY = 0;
            }
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }
}
