package com.ruanko.client;

import com.ruanko.guibase.AnimatePanel;

public class MessagePanel extends ClientPanelRightBottom {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    AnimatePanel title;

    AnimatePanel content;

    public MessagePanel() {
        title = new AnimatePanel();
        title.setBounds(10, 10, ClientPanelRightBottom.SizeEx.width - 20, 20);
        title.setBackground("images\\glassboard_white.png");
        title.setText("���ա������¡���Ϣ����");
        title.setTextSize(12);
        title.offsetY = 2;
        this.add(title);
        content = new AnimatePanel();
        content.isMuti = true;
        content.offsetX = 30;
        content.offsetY = 40;
        content.setBounds(10, 30, ClientPanelRightBottom.SizeEx.width - 20, ClientPanelRightBottom.SizeEx.height - 20);
        content.setBackground("images\\glassboard_white.png");
        content.setText("");
        this.add(content);
    }

    public void receiveMsg(String s) {
        content.setText(s);
        repaint();
    }

    public void initConn() {
        Thread t = new Thread(new ClientSocket());
        t.start();
    }
}
