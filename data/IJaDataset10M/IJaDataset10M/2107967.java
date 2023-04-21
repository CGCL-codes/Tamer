package com.ruanko.client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.ruanko.guibase.AnimatePanel;

public class MainPanel extends ClientPanelLeft {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private AnimatePanel Pinyin, Category, Hot, Songnum;

    int w, h;

    public MainPanel() {
        w = SizeEx.width;
        h = SizeEx.height;
        Pinyin = new AnimatePanel();
        Pinyin.setBackground("images\\pinyin.png");
        Pinyin.setBounds(w / 10, h / 10, w * 35 / 100, h * 35 / 100);
        Pinyin.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                Pinyin.offsetY = 10;
            }

            public void mouseReleased(MouseEvent e) {
                Pinyin.offsetY = 5;
                ClientControl.PanelControl(PanelControl.PinyinClk, null);
            }
        });
        Category = new AnimatePanel();
        Category.setBackground("images\\categray.png");
        Category.setBounds(w / 10, h * 55 / 100, w * 35 / 100, h * 35 / 100);
        Category.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                Category.offsetY = 10;
            }

            public void mouseReleased(MouseEvent e) {
                Category.offsetY = 5;
                ClientControl.PanelControl(PanelControl.CategoryClk, null);
            }
        });
        Songnum = new AnimatePanel();
        Songnum.setBackground("images\\number.png");
        Songnum.setBounds(w * 55 / 100, h / 10, w * 35 / 100, h * 35 / 100);
        Songnum.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                Songnum.offsetY = 10;
            }

            public void mouseReleased(MouseEvent e) {
                Songnum.offsetY = 5;
                ClientControl.PanelControl(PanelControl.SongnumClk, null);
            }
        });
        Hot = new AnimatePanel();
        Hot.setBackground("images\\hot.png");
        Hot.setBounds(w * 55 / 100, h * 55 / 100, w * 35 / 100, h * 35 / 100);
        Hot.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                Hot.offsetY = 10;
            }

            public void mouseReleased(MouseEvent e) {
                Hot.offsetY = 5;
                ClientControl.PanelControl(PanelControl.HotClk, null);
            }
        });
        this.add(Pinyin);
        this.add(Category);
        this.add(Songnum);
        this.add(Hot);
    }
}
