package com.wateray.ipassbook.ui.widget;

import java.awt.Component;
import javax.swing.JFrame;
import com.wateray.ipassbook.commom.Constant;
import com.wateray.ipassbook.util.ImageManager;

/**
 * @author wateray
 * 
 */
public class CFrame extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3172874700605827735L;

    /**
	 * Default constructor
	 */
    public CFrame() {
        super();
        setIconImage(ImageManager.getImage(Constant.APP_ICON));
    }

    /**
	 * Default constructor
	 */
    public CFrame(String title) {
        super(title);
        setIconImage(ImageManager.getImage(Constant.APP_ICON));
    }

    /**
	 * Instantiates a new custom frame.
	 * 
	 * @param title
	 * @param width
	 * @param height
	 * @param owner
	 */
    public CFrame(String title, int width, int height, Component owner) {
        super(title);
        setSize(width, height);
        setLocationRelativeTo(owner);
        setIconImage(ImageManager.getImage(Constant.APP_ICON));
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }
}
