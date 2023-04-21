package com.xinsdd.client.frame.main.panel;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import com.xinsdd.client.common.Contacts;
import com.xinsdd.client.frame.components.BgJpanel;
import com.xinsdd.client.frame.components.ChangeButton;
import com.xinsdd.client.frame.components.RichFlashPanel;
import com.xinsdd.client.resources.ResManager;

/**
 * 书籍封面
 * 修改内容：创建类
 * @author: yxl
 * @version 1.0
 */
public class BookFrontPanel extends MainBasePanel {

    public BookFrontPanel(JFrame parent) {
        super(parent);
        setBounds(0, 80, this.parent.softWidth, this.parent.softHeight - 74);
        setOpaque(false);
        this.setVisible(false);
    }

    public BgJpanel leftPanel, buttomPanel;

    public RichFlashPanel rightPanel;

    /** 目录数据 */
    public List catalogData = null;

    ChangeButton catalogBtn, coverLastBtn, exitBtn, nextBtn;

    /** 设置数据 
	 * @throws IOException */
    public void setData(List catalogs) throws IOException {
        this.setVisible(false);
        catalogData = catalogs;
        removeAll();
        initComponents();
        Rectangle bounds = new Rectangle();
        bounds.x = 0;
        bounds.y = 1;
        bounds.width = rightPanel.getWidth() - Contacts.BOOK_RIGHTPAGE_RIGHT_SPACE;
        bounds.height = rightPanel.getHeight() - 2;
        rightPanel.bounds = bounds;
        addListener();
        String resourceName = "m.res";
        parent.msgPanel.setMsg("正在准备资源...");
        String path = ResManager.getSwfFromResFile(ReaderPanel.resRootPath + resourceName);
        if (path != null) {
            rightPanel.loadFile(path);
        } else {
            System.out.println("未找到封面！");
        }
        parent.msgPanel.close();
    }

    /**
	 * 描述：添加事件   
	 * 2011-7-26 下午10:11:11
	 */
    private void addListener() {
        catalogBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                parent.bookCatalogPanel.setData(catalogData);
                parent.bookCatalogPanel.setVisible(true);
                parent.bookCatalogPanel.catlogScroll.setVisible(true);
                parent.bookCatalogPanel.catlogLabel.setVisible(true);
                setVisible(false);
            }
        });
        coverLastBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                parent.bookEndPanel.setVisible(true);
                parent.bookEndPanel.setData(catalogData);
                setVisible(false);
            }
        });
        nextBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                parent.bookCatalogPanel.setData(catalogData);
                parent.bookCatalogPanel.setVisible(true);
                setVisible(false);
            }
        });
        exitBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                parent.bookCatalogPanel.setVisible(false);
                parent.bookFontPanel.setVisible(false);
                parent.bookEndPanel.setVisible(false);
                parent.leftPanel.setVisible(true);
                parent.adMainPanel.setVisible(true);
            }
        });
    }

    /** 初始化组件 */
    private void initComponents() {
        parent.leftPanel.setVisible(false);
        setLayout(null);
        buttomPanel = new BgJpanel(parent.rm.getImageIconOfPng("main/buttomBook"));
        rightPanel = new RichFlashPanel();
        rightPanel.bgImg = parent.rm.getImageIconOfPng("main/rightBook");
        rightPanel.setBounds(this.getWidth() / 2, 20, this.getWidth() / 2 - 40, this.getHeight() - 90);
        buttomPanel.setBounds(0, this.getHeight() - 40, this.getWidth(), 40);
        buttomPanel.setBackground(Color.WHITE);
        rightPanel.setBackground(new java.awt.Color(255, 51, 255));
        rightPanel.setLayout(null);
        add(rightPanel);
        add(buttomPanel);
        buttomPanel.setLayout(null);
        catalogBtn = new ChangeButton();
        catalogBtn.setBounds(20, 2, 80, 30);
        catalogBtn.setBgIcon(parent.rm.getImageIconOfPng("main/catalogBtn"));
        catalogBtn.setActivityBgIcon(parent.rm.getImageIconOfPng("main/catalogBtnActivity"));
        buttomPanel.add(catalogBtn);
        coverLastBtn = new ChangeButton();
        coverLastBtn.setBounds(120, 2, 80, 30);
        coverLastBtn.setBgIcon(parent.rm.getImageIconOfPng("main/coverLastBtn"));
        coverLastBtn.setActivityBgIcon(parent.rm.getImageIconOfPng("main/coverLastBtnActivity"));
        buttomPanel.add(coverLastBtn);
        exitBtn = new ChangeButton();
        exitBtn.setBounds(210, 2, 100, 30);
        exitBtn.setBgIcon(parent.rm.getImageIconOfPng("main/exitBtn"));
        exitBtn.setActivityBgIcon(parent.rm.getImageIconOfPng("main/exitBtnActivity"));
        buttomPanel.add(exitBtn);
        nextBtn = new ChangeButton();
        nextBtn.setBounds(630, 2, 100, 30);
        nextBtn.setBgIcon(parent.rm.getImageIconOfPng("main/nextBtn"));
    }
}
