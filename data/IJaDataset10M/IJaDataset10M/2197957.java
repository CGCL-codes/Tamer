package pt.adrz.clipx;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * novo texto
 * Class that will caught clipboard events, if it is a String, that 
 * String will be stored in a LinkedList
 * @author adriano
 *
 */
public class ClipManager implements FlavorListener, ClipboardOwner, ActionListener, MouseListener, KeyListener {

    /**
	 * SyS clipboard
	 */
    private Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

    /**
	 * GUI of the application
	 */
    private ClipGUI gui;

    private PopupMenu popupMenu;

    private TrayIcon trayIcon;

    private SystemTray sysTray = SystemTray.getSystemTray();

    MenuItem item01;

    MenuItem item02;

    MenuItem item03;

    MenuItem mItemAbout;

    MenuItem mItemExit;

    private final String mItemAboutStr = "About";

    private final String mItemExitStr = "Exit";

    private final String toolTipStr = "ClipX";

    /**
	 * Constructor - Get and then Set the contents in clipboard in order
	 * to get the ownership.
	 */
    public ClipManager() {
        clip.setContents(clip.getContents(null), this);
        clip.addFlavorListener(this);
        gui = new ClipGUI(this);
        this.iniSysTray();
        this.gui.addKeyListener(this);
    }

    /**
	 * initializated system tray icon resources
	 */
    private void iniSysTray() {
        item01 = new MenuItem("item01");
        item02 = new MenuItem("item02");
        item03 = new MenuItem("item03");
        mItemAbout = new MenuItem(mItemAboutStr);
        mItemAbout.setEnabled(false);
        mItemExit = new MenuItem(mItemExitStr);
        item01.addActionListener(this);
        item02.addActionListener(this);
        item03.addActionListener(this);
        mItemAbout.addActionListener(this);
        mItemExit.addActionListener(this);
        popupMenu = new PopupMenu();
        popupMenu.add(item01);
        popupMenu.add(item02);
        popupMenu.add(item03);
        popupMenu.add(mItemAbout);
        popupMenu.add(mItemExit);
        popupMenu.addActionListener(this);
        trayIcon = new TrayIcon(new ImageIcon("img/mainIcon.gif", "ClipX").getImage());
        trayIcon.setPopupMenu(popupMenu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(this);
        trayIcon.addMouseListener(this);
        trayIcon.setToolTip(toolTipStr);
        try {
            sysTray.add(trayIcon);
        } catch (AWTException eAWT) {
        }
        trayIcon.displayMessage("ClipX", "clipboard Strings will be Saved ...", MessageType.INFO);
    }

    /**
	 * Clipboard has new data
	 */
    @Override
    public void flavorsChanged(FlavorEvent e) {
        clip.removeFlavorListener(this);
        String strClip = null;
        Transferable tf = null;
        try {
            tf = clip.getContents(this);
            if (tf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    strClip = (String) tf.getTransferData(DataFlavor.stringFlavor);
                    System.out.println("Data from clipboard = " + strClip);
                    if (!this.gui.getList().getModel().getItems().contains(strClip)) {
                        gui.getList().getModel().addElementTo(strClip, 0);
                        this.setClipboard(strClip);
                        this.gui.getEditTA().setText(strClip);
                    } else {
                        System.out.println("that Clipboard String already exists");
                        this.setClipboard(strClip);
                    }
                } catch (IOException eIO) {
                    System.out.println("IO error!");
                } catch (UnsupportedFlavorException eUFE) {
                    System.out.println("flavor not supported");
                }
            } else {
                System.out.println("not a String");
                gui.getEditTA().setText("<<< Clipboard Dont have a String item >>>");
            }
        } catch (IllegalStateException eISE) {
            System.out.println("cannot get contents from clipboard");
        }
        try {
            clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            clip.setContents(clip.getContents(null), this);
        } catch (IllegalStateException eISE) {
            System.out.println("cannot set contents in clipboard");
        } catch (HeadlessException eHE) {
            System.out.println("Cannot get clipboard");
        }
        clip.addFlavorListener(this);
    }

    /**
	 * Replace the clipboard with the given string
	 * @param text String to be stored in the clipboard
	 */
    public void setClipboard(String text) {
        clip.removeFlavorListener(this);
        StringSelection ss = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, this);
        clip.addFlavorListener(this);
    }

    @Override
    public void lostOwnership(Clipboard arg0, Transferable arg1) {
    }

    /**
	 * Every event from systray menu is processed here
	 */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println(e.getActionCommand());
            if (e.getActionCommand().equals(mItemExitStr)) {
                System.exit(0);
            }
        } catch (NullPointerException eNULL) {
            System.out.println("event is null in Action Performed");
        } catch (Exception eEX) {
            System.out.println("unknow exception in Action Performed");
        }
    }

    /**
	 * 
	 * Process the event when a single left mouse click is done over systray ClipX icon
	 */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            System.out.println("main Event");
            gui.setVisible(true);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        System.out.println("keypressed = " + arg0.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }
}
