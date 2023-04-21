package com.edgenius.image.template;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import jf.exam.paint.PaintCanvas;
import jf.exam.paint.util.Config;
import com.edgenius.registry.IRegistrable;
import com.edgenius.registry.IRegistryNode;
import com.edgenius.registry.RootRegistryNode;

/**
 * Each class instance is corresponding each template
 * Creation date: (2001-12-19 10:40:29)
 * @author: Steve Ni
 */
public class Timage implements Cloneable, IRegistrable {

    private String name;

    private int leftX = 0;

    private int leftY = 0;

    private int oldLeftX = 0;

    private int oldLeftY = 0;

    private int width = 0;

    private int height = 0;

    private boolean activeFlag = false;

    private int imgWidth = 0;

    private int imgHeight = 0;

    private Image oldCanvas = null;

    private Image passiveImg = null;

    private IRegistryNode registerNode;

    private PaintCanvas canvas;

    TemplEvent templEvent = null;

    public class TemplEvent {

        private int changeStatus = jf.exam.paint.util.Config.TemplDragNone;

        private int changeDir = -1;

        private int mouseX = -1;

        private int mouseY = -1;

        public int getChangeDir() {
            return changeDir;
        }

        public void setChangeDir(int newChangeDir) {
            changeDir = newChangeDir;
        }

        public int getChangeStatus() {
            return changeStatus;
        }

        public void setChangeStatus(int newChangeStatus) {
            changeStatus = newChangeStatus;
        }

        public int getX() {
            return mouseX;
        }

        public int getY() {
            return mouseY;
        }

        public void setX(int newX) {
            mouseX = newX;
        }

        public void setY(int newY) {
            mouseY = newY;
        }
    }

    /**
	 * Templ constructor comment.
	 */
    public Timage(String name, Image img) {
        super();
        passiveImg = img;
        imgWidth = passiveImg.getWidth(null);
        imgHeight = passiveImg.getHeight(null);
        this.name = name;
    }

    /**
	* Creation date: (2001-12-19 11:18:14)
	*/
    public Object clone() {
        Timage tp = null;
        try {
            tp = (Timage) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return tp;
    }

    /**
	 * Creation date: (2001-12-19 10:49:50)
	 * @param g java.awt.Graphics
	 */
    public void drawActive(Graphics g) {
        if (!activeFlag) {
            width += 10;
            height += 10;
            leftX -= 5;
            leftY -= 5;
        }
        canvas = (PaintCanvas) getRootNode().lookup(Config.REG_PaintCanvas);
        if (oldCanvas == null) {
            oldCanvas = canvas.createImage(Config.CanvasW, Config.CanvasH);
            oldCanvas.getGraphics().drawImage(canvas.getCanvasImage(), 0, 0, canvas);
        }
        g.drawImage(oldCanvas, 0, 0, canvas);
        Image activeImg = canvas.createImage(width, height);
        Graphics activeG = activeImg.getGraphics();
        activeG.setColor(java.awt.Color.red);
        for (int i = 0; i < 4; i++) activeG.drawRect(getBorder()[i].x, getBorder()[i].y, getBorder()[i].width, getBorder()[i].height);
        activeG.drawImage(passiveImg, 5, 5, width - 10, height - 10, canvas);
        activeG.drawLine(20, 5, 20, 20);
        activeG.drawLine(5, 20, 20, 20);
        activeG.dispose();
        g.drawImage(activeImg, leftX, leftY, width, height, canvas);
        activeFlag = true;
    }

    /**
	 * Creation date: (2001-12-19 10:49:50)
	 * @param g java.awt.Graphics
	 */
    public void drawPassive(Graphics g) {
        if (activeFlag) {
            leftX += 5;
            leftY += 5;
            width -= 10;
            height -= 10;
        }
        canvas = (PaintCanvas) getRootNode().lookup(Config.REG_PaintCanvas);
        if (oldCanvas == null) {
            oldCanvas = canvas.createImage(Config.CanvasW, Config.CanvasH);
            oldCanvas.getGraphics().drawImage(canvas.getCanvasImage(), 0, 0, canvas);
        }
        g.drawImage(oldCanvas, 0, 0, canvas);
        g.drawImage(passiveImg, leftX, leftY, width, height, canvas);
        activeFlag = false;
    }

    /**
	 * Creation date: (2001-12-19 10:49:50)
	 * @param g java.awt.Graphics
	 */
    public void drawPassive(Graphics g, int x, int y, int w, int h) {
        leftX = x;
        leftY = y;
        width = w;
        height = h;
        canvas = (PaintCanvas) getRootNode().lookup(Config.REG_PaintCanvas);
        g.drawImage(passiveImg, x, y, w, h, canvas);
    }

    /**
		 * Creation date: (2001-12-25 14:45:06)
		 * @return java.awt.Rectangle
		 */
    private java.awt.Rectangle[] getBorder() {
        Rectangle[] arrRect = new Rectangle[4];
        arrRect[0] = new Rectangle(0, 0, width - 1, 4);
        arrRect[1] = new Rectangle(0, height - 5, width - 1, 4);
        arrRect[2] = new Rectangle(0, 0, 4, height);
        arrRect[3] = new Rectangle(width - 5, 0, 4, height);
        return arrRect;
    }

    /**
	 * Creation date: (2001-12-21 10:43:45)
	 * @return int
	 */
    public int getHeight() {
        return height;
    }

    /**
	 * Creation date: (2001-12-27 11:24:01)
	 * @return int
	 */
    public int getImgHeight() {
        return imgHeight;
    }

    /**
	 * Creation date: (2001-12-27 11:24:01)
	 * @return int
	 */
    public int getImgWidth() {
        return imgWidth;
    }

    /**
	 * Creation date: (2001-12-21 10:43:45)
	 * @return int
	 */
    public int getLeftX() {
        return leftX;
    }

    /**
	 * Creation date: (2001-12-21 10:43:45)
	 * @return int
	 */
    public int getLeftY() {
        return leftY;
    }

    /**
	 * Creation date: (2001-12-25 16:28:33)
	 * @return jf.exam.paint.graphics.tools.Templ.TemplEvent
	 */
    public TemplEvent getTemplEvent() {
        if (templEvent == null) templEvent = new TemplEvent();
        return templEvent;
    }

    /**
	 * Creation date: (2001-12-21 10:43:45)
	 * @return int
	 */
    public int getWidth() {
        return width;
    }

    /**
	 * Creation date: (2001-12-20 15:05:12)
	 * @param x int
	 * @param y int
	 */
    public boolean isInside(int x, int y) {
        if (x > leftX && x < (leftX + width) && y > leftY && y < (leftY + height)) return true; else return false;
    }

    /**
	 * Creation date: (2001-12-25 14:52:41)
	 * @return boolean
	 * @param y int
	 */
    public int isInsideBorder(int x, int y) {
        int x1, y1, w1, h1;
        for (int i = 0; i < 4; i++) {
            x1 = getBorder()[i].x + leftX;
            y1 = getBorder()[i].y + leftY;
            w1 = getBorder()[i].width;
            h1 = getBorder()[i].height;
            if (x >= x1 && x < (x1 + w1) && y >= y1 && y <= (y1 + h1)) return i;
        }
        return -1;
    }

    /**
	 * Creation date: (2001-12-25 14:52:41)
	 * @return boolean
	 * @param y int
	 */
    public boolean isInsideRestore(int x, int y) {
        if (x >= leftX + 5 && x < (leftX + 20) && y >= leftY + 5 && y <= (leftY + 20)) return true; else return false;
    }

    /**
	* Creation date: (2001-12-21 10:43:45)
	* @param newHeight int
	*/
    public void setHeight(int newHeight) {
        height = newHeight;
    }

    /**
	 * Creation date: (2001-12-27 11:24:01)
	 * @param newImgHeight int
	 */
    void setImgHeight(int newImgHeight) {
        imgHeight = newImgHeight;
    }

    /**
	 * Creation date: (2001-12-27 11:24:01)
	 * @param newImgWidth int
	 */
    void setImgWidth(int newImgWidth) {
        imgWidth = newImgWidth;
    }

    /**
	 * Creation date: (2001-12-21 10:43:45)
	 * @param newLeftX int
	 */
    public void setLeftX(int newLeftX) {
        leftX = newLeftX;
    }

    /**
	 * Creation date: (2001-12-21 10:43:45)
	 * @param newLeftY int
	 */
    public void setLeftY(int newLeftY) {
        leftY = newLeftY;
    }

    /**
	 * Creation date: (2001-12-25 16:28:33)
	 * @param newTemplEvent jf.exam.paint.graphics.tools.Templ.TemplEvent
	 */
    public void setTemplEvent(TemplEvent newTemplEvent) {
        templEvent = newTemplEvent;
    }

    /**
	 * Creation date: (2001-12-21 10:43:45)
	 * @param newWidth int
	 */
    public void setWidth(int newWidth) {
        width = newWidth;
    }

    /**
	 * Creation date: (2001-12-21 10:43:45)
	 * @param newWidth int
	 */
    public void destroyBackImage() {
        this.oldCanvas = null;
    }

    public String getRegisterName() {
        return name;
    }

    public IRegistryNode getRegisterNode() {
        return registerNode;
    }

    public RootRegistryNode getRootNode() {
        IRegistryNode node = getRegisterNode();
        while (!node.isRoot()) {
            node = (node).getParentNode();
        }
        return (RootRegistryNode) node;
    }

    public void setRegisterNode(IRegistryNode regNode) {
        registerNode = regNode;
    }
}
