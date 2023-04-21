package com.halozat2009osz.windows;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.halozat2009osz.ChatClient;
import com.halozat2009osz.conndata.FileTransfer;
import com.halozat2009osz.lang.LangSupport;

public class FileTransfersWindow extends WindowBase implements LangSupport {

    private static final long serialVersionUID = 1896365049768039424L;

    private ItemEventHandler eventHandler;

    private JMenu menuWindow;

    private JMenuItem miClose;

    private JPanel panelTransfers;

    private JScrollPane scrollTransfers;

    private int numPanels;

    public FileTransfersWindow(ChatClient chatClient) {
        super(chatClient);
        eventHandler = new ItemEventHandler();
        JMenuBar menuBar = new JMenuBar();
        menuWindow = new JMenu();
        menuBar.add(menuWindow);
        miClose = new JMenuItem("", KeyEvent.VK_C);
        miClose.addActionListener(eventHandler);
        menuWindow.add(miClose);
        setJMenuBar(menuBar);
        panelTransfers = new JPanel();
        panelTransfers.setLayout(new BoxLayout(panelTransfers, BoxLayout.Y_AXIS));
        scrollTransfers = new JScrollPane(panelTransfers);
        add(scrollTransfers, BorderLayout.CENTER);
        updateLanguage();
    }

    public void updateLanguage() {
        setTitle(chatClient.lang.get("FILE_TRANSFERS"));
        menuWindow.setText(chatClient.lang.get("WINDOW"));
        miClose.setText(chatClient.lang.get("CLOSE"));
        for (Component component : panelTransfers.getComponents()) {
            if (!(component instanceof FileTransferPanel)) continue;
            ((FileTransferPanel) component).updateLanguage();
        }
        updateSize();
        setMinimumSize(new Dimension(1, 1));
    }

    public synchronized void add(FileTransfer transfer, PrivateChatWindow chatWindow) {
        FileTransferPanel transferPanel = new FileTransferPanel(chatClient, chatWindow, transfer);
        transfer.setFileTransferPanel(transferPanel);
        panelTransfers.add(transferPanel);
        pack();
        validate();
        repaint();
        numPanels++;
        chatClient.eventFileTransferAdded();
    }

    public synchronized void remove(FileTransfer transfer) {
        panelTransfers.remove(transfer.getFileTransferPanel());
        pack();
        validate();
        repaint();
        numPanels--;
        chatClient.eventFileTransferRemoved();
        if (numPanels == 0) dispose();
    }

    public synchronized int getNumPanels() {
        return numPanels;
    }

    public ItemEventHandler getEventHandler() {
        return eventHandler;
    }

    public class ItemEventHandler implements ActionListener {

        @Override
        public synchronized void actionPerformed(ActionEvent evt) {
            try {
                if (evt.getSource() == miClose) {
                    dispose();
                } else {
                    for (Component component : panelTransfers.getComponents()) {
                        if (!(component instanceof FileTransferPanel)) continue;
                        ((FileTransferPanel) component).actionPerformed(evt);
                    }
                }
            } catch (Exception e) {
                chatClient.handleError(e);
            }
        }
    }
}
