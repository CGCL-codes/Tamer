package org.gerhardb.jibs.viewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import org.gerhardb.jibs.Jibs;
import org.gerhardb.lib.scroller.Scroller;
import org.gerhardb.lib.swing.SwingUtils;

/**
 *
 * @author  Gerhard Beck
 */
public class PicInfoDialogBase extends javax.swing.JDialog {

    PicInfoExif myPicInfoExif;

    private JButton myCloseBtn = new JButton(Jibs.getString("ok"));

    protected boolean isSlideShowRunning;

    Scroller myScroller;

    public PicInfoDialogBase(Frame owner, Scroller scroller) {
        super(owner, Jibs.getString("PicInfoDialog.1"), true);
        this.myScroller = scroller;
    }

    public void display(File file, JComponent picturePanel, int displayInfoWidth, int displayInfoHeight, boolean showDisplayInfo) {
        this.myPicInfoExif = new PicInfoExif(file, picturePanel, displayInfoWidth, displayInfoHeight, showDisplayInfo, this.myScroller);
        this.isSlideShowRunning = this.myScroller.isSlideShowRunning();
        this.myScroller.stopSlideShow();
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        class Dismiss extends AbstractAction {

            Dismiss() {
                super("dismiss");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        }
        this.myCloseBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                close();
            }
        });
        Action dismiss = new Dismiss();
        String actionID = "dismiss";
        this.myCloseBtn.getActionMap().put(actionID, dismiss);
        InputMap map = this.myCloseBtn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke("CANCEL"), actionID);
        map.put(KeyStroke.getKeyStroke("ENTER"), actionID);
        this.setSize(new Dimension(600, 430));
        this.getContentPane().add(setUpScreen());
        setUpMenus();
        this.setSize(640, 480);
        SwingUtils.centerOnScreen(this);
        this.myCloseBtn.requestFocus();
        System.out.println("PicInfoDialogBase.setVisible()");
        this.setVisible(true);
        System.out.println("PicInfoDialogBase returning");
    }

    @SuppressWarnings("static-method")
    protected JButton[] getToolBarButtons() {
        return new JButton[0];
    }

    private Component setUpScreen() {
        JToolBar btnRows = new JToolBar();
        btnRows.setFloatable(false);
        btnRows.add(this.myCloseBtn);
        JButton[] toolBarBtns = getToolBarButtons();
        for (int i = 0; i < toolBarBtns.length; i++) {
            btnRows.add(toolBarBtns[i]);
        }
        JPanel rtnMe = this.myPicInfoExif.makeBorderPanel();
        rtnMe.add(btnRows, BorderLayout.SOUTH);
        return new JScrollPane(rtnMe);
    }

    private void setUpMenus() {
        JMenuItem menuFileClose = new JMenuItem(Jibs.getString("close"));
        menuFileClose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PicInfoDialogBase.this.dispose();
            }
        });
        JMenu menuFile = new JMenu(Jibs.getString("file"));
        menuFile.add(menuFileClose);
        JMenuBar menu = new JMenuBar();
        menu.add(menuFile);
        this.setJMenuBar(menu);
    }

    protected void close() {
        setVisible(false);
        dispose();
        if (this.isSlideShowRunning) {
            this.myScroller.startSlideShow(ViewerPreferences.continuousShow());
        } else {
            this.myScroller.stopSlideShow();
        }
    }
}
