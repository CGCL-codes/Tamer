package com.xinsdd.client.frame.main;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.xinsdd.client.command.BaseCommand;
import com.xinsdd.client.common.Contacts;
import com.xinsdd.client.frame.RichFrame;
import com.xinsdd.client.frame.components.MessagePanel;
import com.xinsdd.client.frame.main.panel.BookCatalogAndContentPanel;
import com.xinsdd.client.frame.main.panel.BookEndPanel;
import com.xinsdd.client.frame.main.panel.BookFrontPanel;
import com.xinsdd.client.frame.main.panel.LockPanel;
import com.xinsdd.client.frame.main.panel.ReaderPanel;
import com.xinsdd.client.frame.main.panel.SubjectsPanel;
import com.xinsdd.client.frame.main.panel.TopInfoPanel;

/**
 * 新书点读主界面
 */
public class MainFrame extends RichFrame {

    @Override
    public void disposeFrameOnShowFront() {
        msgPanel.close();
    }

    /** 屏幕宽度 */
    static Dimension screenSize = null;

    /** 软件宽度 */
    public static int softWidth = 1006;

    /** 软件高度 */
    public static int softHeight = 738;

    static {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (screenSize.width < 1024 || screenSize.height < 768) {
            softWidth = screenSize.width;
            softHeight = screenSize.height;
        }
    }

    /** 左边广告宽度 */
    private int xAdWidthL = 30;

    /** 右边广告宽度 */
    private int xAdWidthR = 30;

    /** x起点坐标，因为预先留广告的缘故 */
    public int xStart = (screenSize.width - softWidth) / 2;

    public int yStart = (screenSize.height - softHeight) / 2;

    /** x终点坐标，因为预先留广告的缘故 */
    private int xEnd = screenSize.width - xAdWidthR;

    private int yEnd = screenSize.height - 0;

    /** 按钮 */
    JLabel close, min;

    public JLabel lock;

    JLabel chinese, natives, story, english, math, cover, catalog, set, coverBottom, exit, front, next;

    JPanel bgPanel;

    public MessagePanel msgPanel, userIsNoMoney;

    public LockPanel lockPanel;

    public ReaderPanel leftPanel;

    SubjectsPanel subjectsPanel;

    public TopInfoPanel topInfoPanel;

    public BookFrontPanel bookFontPanel;

    public BookEndPanel bookEndPanel;

    public BookCatalogAndContentPanel bookCatalogPanel;

    /** 主页面广告 */
    String adMainUrl = null;

    /** 目录页面广告 */
    public String adCatalogUrl = null;

    public BaseCommand bc = new BaseCommand();

    @Override
    public void setBg(ImageIcon img) {
        setLayout(null);
        BufferedImage bfBG = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bfBG.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, bfBG.getWidth(), bfBG.getHeight());
        ImageIcon imgBg = new ImageIcon(bfBG);
        JLabel frameBg = new JLabel();
        frameBg.setIcon(imgBg);
        frameBg.setBounds(0, 0, imgBg.getIconWidth(), imgBg.getIconHeight());
        this.getLayeredPane().add(frameBg, new Integer(Integer.MIN_VALUE));
        setBackground(Color.BLACK);
        bgPanel.setBounds(xStart, yStart, softWidth, softHeight);
        bgPanel.setLayout(null);
        ((JPanel) getContentPane()).setOpaque(false);
        getLayeredPane().setOpaque(false);
        this.getLayeredPane().add(bgPanel, new Integer(Integer.MIN_VALUE + 1));
        int imgWidthFT = 0;
        int imgHeightFT = 0;
        ImageIcon mainBg = rm.getImageIconOfPng("main/bg");
        JLabel frameFT = new JLabel();
        imgWidthFT = softWidth;
        imgHeightFT = softHeight;
        mainBg.setImage(mainBg.getImage().getScaledInstance(imgWidthFT, imgHeightFT, Image.SCALE_DEFAULT));
        frameFT.setIcon(mainBg);
        frameFT.setBounds(0, 0, imgWidthFT, imgHeightFT);
        bgPanel.add(frameFT);
    }

    /**
	 * 初始化   
	 * @param data 初始数据
	 * 2011-7-3 下午02:27:23
	 */
    public void init(Map data) {
        setUndecorated(true);
        String title = Contacts.TITLE;
        int width = 1024;
        int height = 768;
        init(title, width, height);
        setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        setBaseUI("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        addTray(rm.getImage("tray"));
        this.setIconImage(rm.getImage("tray"));
        setAlwaysOnTop(true);
        bgPanel = new JPanel();
        subjectsPanel = new SubjectsPanel(this);
        bgPanel.add(subjectsPanel);
        leftPanel = new ReaderPanel(this);
        bgPanel.add(leftPanel);
        Rectangle msgRt = new Rectangle();
        msgRt.x = xStart;
        msgRt.y = 0;
        msgRt.width = softWidth;
        msgRt.height = softHeight;
        msgPanel = new MessagePanel(msgRt);
        this.getLayeredPane().add(msgPanel, new Integer(Integer.MAX_VALUE));
        lockPanel = new LockPanel(this);
        this.getLayeredPane().add(lockPanel, new Integer(Integer.MAX_VALUE - 1));
        lockPanel.bgImg = rm.getImageIconOfPng("main/lockBg");
        lockPanel.password = data.get("password").toString();
        bookCatalogPanel = BookCatalogAndContentPanel.getInstance(this);
        bgPanel.add(bookCatalogPanel);
        bookFontPanel = new BookFrontPanel(this);
        bgPanel.add(bookFontPanel);
        bookFontPanel.setVisible(true);
        bookEndPanel = new BookEndPanel(this);
        bgPanel.add(bookEndPanel);
        bookEndPanel.setVisible(false);
        topInfoPanel = new TopInfoPanel(this);
        bgPanel.add(topInfoPanel);
        setButton();
        addButtonListener();
        setBg(null);
        setData(data);
        setVisible(false);
        setVisible(true);
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice screen = environment.getDefaultScreenDevice();
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_WINDOWS) {
                    Robot robot = null;
                    try {
                        robot = new Robot(screen);
                    } catch (AWTException e1) {
                        e1.printStackTrace();
                    }
                    robot.keyPress(KeyEvent.VK_0);
                    robot.keyRelease(KeyEvent.VK_0);
                    return true;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) return true;
                return false;
            }
        });
        this.addWindowStateListener(new WindowStateListener() {

            @Override
            public void windowStateChanged(WindowEvent e) {
                System.out.println("状态监听!");
                if (getState() == 1 && lockPanel.isLocked) {
                    setState(0);
                }
            }
        });
    }

    public int USER_IS_NOMONEY = 1;

    public int USER_IS_NOMONEY_FOR_10DAY_AGO = 2;

    /** 显示没有钱 */
    public void showNoMoney(int type) {
        Rectangle msgRt = new Rectangle();
        msgRt.x = xStart;
        msgRt.y = 0;
        msgRt.width = softWidth;
        msgRt.height = softHeight;
        userIsNoMoney = new MessagePanel(msgRt, true) {

            @Override
            public void onClick() {
                try {
                    Runtime.getRuntime().exec("explorer http://www.xinsdd.com/account_center.html");
                    onMin();
                    this.setVisible(false);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
        this.getLayeredPane().add(userIsNoMoney, new Integer(Integer.MAX_VALUE));
        ImageIcon imgIcon = null;
        if (type == USER_IS_NOMONEY) imgIcon = rm.getImageIconOfPng("main/userIsNoMoney"); else if (type == USER_IS_NOMONEY_FOR_10DAY_AGO) imgIcon = rm.getImageIconOfPng("main/userIsNoMoneyFor10DayAgo");
        userIsNoMoney.setMsg(imgIcon);
        if (type == USER_IS_NOMONEY_FOR_10DAY_AGO) userIsNoMoney.autoClose();
    }

    /***
	 * 显示数据   
	 */
    private void setData(Map data) {
        lockPanel.password = data.get("password").toString();
        topInfoPanel.setData(data);
        adMainUrl = data.get("ad1").toString();
        adCatalogUrl = data.get("ad2").toString();
        System.out.println(data.get("ad"));
        List subjectsList = (List) data.get("subjects");
        subjectsPanel.setData(subjectsList);
        adMainPanel = new JPanel();
        adMainPanel.setOpaque(false);
        adMainPanel.setBounds(xStart + 130, yStart + 95, softWidth - 140, softHeight - 105);
        adMainPanel.setVisible(true);
        getContentPane().add(adMainPanel);
        ImageIcon adMainImg = new ImageIcon(adMainUrl);
        JLabel adMainLabel = new JLabel();
        if (adMainImg != null) {
            adMainImg.setImage(adMainImg.getImage().getScaledInstance(adMainPanel.getWidth(), adMainPanel.getHeight(), Image.SCALE_DEFAULT));
            adMainLabel.setBounds(0, 0, adMainPanel.getWidth(), adMainPanel.getHeight());
            adMainLabel.setIcon(adMainImg);
            adMainPanel.add(adMainLabel);
        }
        adMainLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        setVisible(true);
    }

    public JPanel adMainPanel;

    /**
	 * 添加LABLE   
	 */
    public void addLabel(String name, int x, int y, int width, int height) {
        JLabel label = new JLabel(name);
        label.setBounds(xStart + x, yStart + y, width, height);
        label.setForeground(Contacts.labColor);
        this.add(label);
    }

    /** 添加按钮 */
    private void setButton() {
        close = addButtonLable(bgPanel, "main/close", softWidth - 45, 10);
        min = addButtonLable(bgPanel, "main/min", softWidth - 75, 10);
        lock = addButtonLable(bgPanel, "main/unLock", softWidth - 105, 10);
        subjectsPanel.setLayout(new FlowLayout());
        subjectsPanel.setForeground(Color.CYAN);
    }

    /** 按钮事件 */
    private void addButtonListener() {
        close.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (lockPanel.isLocked) {
                    if (bookFontPanel.isVisible()) lockPanel.tempHide = bookFontPanel;
                    if (bookEndPanel.isVisible()) lockPanel.tempHide = bookEndPanel;
                    if (bookCatalogPanel.isVisible()) lockPanel.tempHide = bookCatalogPanel;
                    LockPanel.DO_WHAT = LockPanel.DO_WHAT_CLOSE;
                    lockPanel.initComponents();
                } else {
                    System.exit(0);
                }
            }
        });
        min.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (lockPanel.isLocked) {
                    if (bookFontPanel.isVisible()) lockPanel.tempHide = bookFontPanel;
                    if (bookEndPanel.isVisible()) lockPanel.tempHide = bookEndPanel;
                    if (bookCatalogPanel.isVisible()) lockPanel.tempHide = bookCatalogPanel;
                    LockPanel.DO_WHAT = LockPanel.DO_WHAT_MIN;
                    lockPanel.initComponents();
                } else {
                    setVisible(false);
                }
            }
        });
        lock.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (bookFontPanel.isVisible()) lockPanel.tempHide = bookFontPanel;
                if (bookEndPanel.isVisible()) lockPanel.tempHide = bookEndPanel;
                if (bookCatalogPanel.isVisible()) lockPanel.tempHide = bookCatalogPanel;
                lockPanel.DO_WHAT = lockPanel.DO_WHAT_NONE;
                lockPanel.initComponents();
            }
        });
    }
}
