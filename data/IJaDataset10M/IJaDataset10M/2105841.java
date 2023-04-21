package Vistes.Tasques;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import Domini.*;
import Errors.*;
import Vistes.Eines.*;
import Vistes.*;
import com.borland.jbcl.layout.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Frederic P�rez Ordeig
 * @version 1.0
 */
public class Frame1 extends JFrame {

    JPanel contentPane;

    JMenuBar jMenuBar1 = new JMenuBar();

    JMenu jMenuFile = new JMenu();

    JMenuItem jMenuFileExit = new JMenuItem();

    JMenu jMenuHelp = new JMenu();

    JMenuItem jMenuHelpAbout = new JMenuItem();

    JToolBar jToolBar = new JToolBar();

    JButton jButton1 = new JButton();

    JButton jButton2 = new JButton();

    JButton jButton3 = new JButton();

    ImageIcon image1;

    ImageIcon image2;

    ImageIcon image3;

    JLabel statusBar = new JLabel();

    BorderLayout borderLayout1 = new BorderLayout();

    JButton jButton4 = new JButton();

    JTabbedPane jTabbedPane1 = new JTabbedPane();

    pnlPlantilles pnlPlan = new pnlPlantilles();

    pnlPrincipal pnlPrincipal = new pnlPrincipal();

    pnlPrincipal pnlPrincipal1 = new pnlPrincipal();

    public Frame1() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        image1 = new ImageIcon(Vistes.Tasques.Frame1.class.getResource("openFile.gif"));
        image2 = new ImageIcon(Vistes.Tasques.Frame1.class.getResource("closeFile.gif"));
        image3 = new ImageIcon(Vistes.Tasques.Frame1.class.getResource("help.gif"));
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(548, 380));
        this.setTitle("Frame Title");
        statusBar.setText(" ");
        jMenuFile.setText("File");
        jMenuFileExit.setText("Exit");
        jMenuFileExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuFileExit_actionPerformed(e);
            }
        });
        jMenuHelp.setText("Help");
        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jMenuHelpAbout_actionPerformed(e);
            }
        });
        jButton1.setIcon(image1);
        jButton1.setToolTipText("Open File");
        jButton2.setIcon(image2);
        jButton2.setToolTipText("Close File");
        jButton3.setIcon(image3);
        jButton3.setToolTipText("Help");
        jButton4.setText("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton4_actionPerformed(e);
            }
        });
        jToolBar.add(jButton1);
        jToolBar.add(jButton2);
        jToolBar.add(jButton3);
        jMenuFile.add(jMenuFileExit);
        jMenuHelp.add(jMenuHelpAbout);
        jMenuBar1.add(jMenuFile);
        jMenuBar1.add(jMenuHelp);
        this.setJMenuBar(jMenuBar1);
        contentPane.add(jToolBar, BorderLayout.NORTH);
        contentPane.add(statusBar, BorderLayout.CENTER);
        contentPane.add(jButton4, BorderLayout.SOUTH);
        contentPane.add(jTabbedPane1, BorderLayout.WEST);
        jTabbedPane1.add(pnlPlan, "pnlPlan");
        jTabbedPane1.add(pnlPrincipal1, "pnlPrincipal1");
    }

    public void jMenuFileExit_actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
        Frame1_AboutBox dlg = new Frame1_AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.show();
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            jMenuFileExit_actionPerformed(null);
        }
    }

    void jButton4_actionPerformed(ActionEvent e) {
        dlgPlantilla dlgplan = new dlgPlantilla();
        dlgplan.show();
    }
}
