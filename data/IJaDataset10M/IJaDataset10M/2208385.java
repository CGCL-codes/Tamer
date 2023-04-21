package mazerunner;

import java.awt.CardLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.*;
import java.awt.Robot;
import java.awt.AWTException;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import org.newdawn.easyogg.OggClip;

/**
 *
 * @author  timothyb89
 */
public class MainGameWindow extends javax.swing.JFrame {

    /** Creates new form MainGameWindow */
    public OggClip clip = null;

    public MainGameWindow() {
        initComponents();
        try {
            clip = new OggClip("music.ogg");
        } catch (LineUnavailableException lue) {
            JOptionPane.showMessageDialog(null, "The audio device is unavailable.");
            clip.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Please click on the text box found at the \nbottom of the screen to enable keyboard control.");
        int msg = JOptionPane.showConfirmDialog(null, "Would you like to enable sound (this can be changed later)", "Enable Music and Sound", 0, 2);
        if (msg == 0) System.setProperty("mazerunner_audio", "true");
        if (msg == 1) System.setProperty("mazerunner_audio", "false");
        if (msg == 0) {
            clip.loop();
        }
        StatsWindow stats = new StatsWindow();
        stats.setLocation(this.getX() + 678, this.getY());
        stats.setVisible(true);
        stats.setSize(170, 450);
    }

    /** Just a boring method to shorten the JOptionPane code...
     */
    private void warning(String text, String title) {
        JOptionPane.showMessageDialog(null, text, title, 2);
    }

    private void initComponents() {
        map1 = new javax.swing.JPanel();
        smile = new javax.swing.JLabel();
        bPipe1Pane = new javax.swing.JPanel();
        bPipe1 = new javax.swing.JLabel();
        wall2 = new javax.swing.JPanel();
        rPipe1 = new javax.swing.JLabel();
        popupmenu = new javax.swing.JPopupMenu();
        exit1 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        stats = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        mapA1 = new maps.mapA();
        map2 = new maps.map2();
        nextButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exit = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        musicmenu = new javax.swing.JMenu();
        musstart = new javax.swing.JMenuItem();
        musstop = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        enableDebugMenu = new javax.swing.JCheckBoxMenuItem();
        runCmd = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        submitComment = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        map1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 6, true));
        map1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent evt) {
                map1MouseMoved(evt);
            }
        });
        map1.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                map1KeyPressed(evt);
            }
        });
        smile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mazerunner/images/smiley.png")));
        bPipe1Pane.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bPipe1PaneMouseEntered(evt);
            }
        });
        bPipe1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mazerunner/images/pipes/black-half-bottom.png")));
        javax.swing.GroupLayout bPipe1PaneLayout = new javax.swing.GroupLayout(bPipe1Pane);
        bPipe1Pane.setLayout(bPipe1PaneLayout);
        bPipe1PaneLayout.setHorizontalGroup(bPipe1PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(bPipe1));
        bPipe1PaneLayout.setVerticalGroup(bPipe1PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(bPipe1));
        wall2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 15));
        javax.swing.GroupLayout wall2Layout = new javax.swing.GroupLayout(wall2);
        wall2.setLayout(wall2Layout);
        wall2Layout.setHorizontalGroup(wall2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
        wall2Layout.setVerticalGroup(wall2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 411, Short.MAX_VALUE));
        rPipe1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mazerunner/images/pipes/red-half-top.png")));
        javax.swing.GroupLayout map1Layout = new javax.swing.GroupLayout(map1);
        map1.setLayout(map1Layout);
        map1Layout.setHorizontalGroup(map1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(map1Layout.createSequentialGroup().addGroup(map1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(map1Layout.createSequentialGroup().addContainerGap().addComponent(rPipe1)).addGroup(map1Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(smile))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(wall2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(bPipe1Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(353, Short.MAX_VALUE)));
        map1Layout.setVerticalGroup(map1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(map1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(map1Layout.createSequentialGroup().addContainerGap().addComponent(smile).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(rPipe1)).addComponent(wall2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(bPipe1Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        popupmenu.getAccessibleContext().setAccessibleParent(this);
        exit1.setText("Exit");
        popupmenu.add(exit1);
        jMenuItem1.setText("Item");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        popupmenu.add(jMenuItem1);
        stats.setLayout(null);
        jLabel1.setFont(new java.awt.Font("Dialog", 3, 24));
        jLabel1.setText("Stats");
        stats.add(jLabel1);
        jLabel1.setBounds(50, 10, 70, 20);
        jLabel2.setText("Score:");
        stats.add(jLabel2);
        jLabel2.setBounds(10, 50, 40, 15);
        stats.add(jSeparator1);
        jSeparator1.setBounds(0, 40, 170, 10);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mazerunner/images/coins/coin-green-small.png")));
        jLabel3.setText(":");
        stats.add(jLabel3);
        jLabel3.setBounds(10, 70, 30, 30);
        jLabel4.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel4.setText("0");
        stats.add(jLabel4);
        jLabel4.setBounds(40, 80, 120, 15);
        jLabel5.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel5.setText("0");
        stats.add(jLabel5);
        jLabel5.setBounds(50, 50, 110, 15);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MazeRunner 0.64");
        setResizable(false);
        jPanel1.setLayout(new java.awt.CardLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Window"));
        mapA1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mapA1MouseClicked(evt);
            }
        });
        jPanel1.add(mapA1, "map1");
        jPanel1.add(map2, "map2");
        nextButton.setText("Next Map");
        nextButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        jMenu1.setText("File");
        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jMenu1.add(exit);
        jMenuItem3.setText("Test Map");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);
        jMenuBar1.add(jMenu1);
        musicmenu.setText("Music");
        musstart.setText("Start");
        musstart.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                musstartActionPerformed(evt);
            }
        });
        musicmenu.add(musstart);
        musstop.setText("Stop");
        musstop.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                musstopActionPerformed(evt);
            }
        });
        musicmenu.add(musstop);
        jMenuBar1.add(musicmenu);
        jMenu2.setText("Options");
        enableDebugMenu.setText("Enable Debug");
        enableDebugMenu.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                enableDebugMenuItemStateChanged(evt);
            }
        });
        enableDebugMenu.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                enableDebugMenuPropertyChange(evt);
            }
        });
        enableDebugMenu.addVetoableChangeListener(new java.beans.VetoableChangeListener() {

            public void vetoableChange(java.beans.PropertyChangeEvent evt) throws java.beans.PropertyVetoException {
                enableDebugMenuVetoableChange(evt);
            }
        });
        jMenu2.add(enableDebugMenu);
        runCmd.setText("Run command...");
        runCmd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runCmdActionPerformed(evt);
            }
        });
        jMenu2.add(runCmd);
        jMenuBar1.add(jMenu2);
        jMenu3.setText("Help");
        submitComment.setText("Submit Comment/Feature Request");
        submitComment.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitCommentActionPerformed(evt);
            }
        });
        jMenu3.add(submitComment);
        jMenuItem2.setText("About");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);
        jMenuBar1.add(jMenu3);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(nextButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(nextButton).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        maps.mlevel1runner mlevel1 = new maps.mlevel1runner();
        mlevel1.setVisible(true);
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        clip.stop();
        AboutWindow aboutWindow = new AboutWindow();
        aboutWindow.setLocation(this.getX() + 50, this.getY() + 50);
        aboutWindow.setVisible(true);
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        String card = System.getProperty("mrunner_card");
        CardLayout cl = (CardLayout) (jPanel1.getLayout());
        cl.show(jPanel1, card);
        System.setProperty("mrunner_card", null);
        clip.stop();
        clip.close();
        try {
            clip = new OggClip(System.getProperty("mazerunner_audiofile"));
            clip.loop();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void mapA1MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getButton() == 3) {
            popupmenu.setVisible(true);
            popupmenu.setLocation(evt.getXOnScreen(), evt.getYOnScreen());
        }
    }

    private void submitCommentActionPerformed(java.awt.event.ActionEvent evt) {
        chatWindow cw = new chatWindow();
        cw.setVisible(true);
    }

    /** In-game Scripting System ********************************************************************
     * This is all of the code for the in-game scripting system.                                    * 
     * This collects commands and sets the system variable for the mazerunner command and does some *
     *    basic processing of them. (Most of the processing will be done in a map itself, thus the  *
     *    system variable)                                                                          *
     ***********************************************************************************************/
    private void chkCmdAndRun(String cmd) {
        if (cmd != null) {
            if (cmd.contains("music off")) {
                clip.stop();
                JOptionPane.showMessageDialog(null, "Music has been disabled.", "Music Disabled", 2);
            } else if (runcmd.contains("music on")) {
                warning("Please use 'music play' or 'music loop' ", "Invalid Command");
            } else if (cmd.contains("cheat")) {
                cheatWindow cw = new cheatWindow();
                cw.setVisible(true);
            } else if (cmd.contains("exit")) System.exit(0); else if (cmd.contains("music loop")) {
                try {
                    clip = new OggClip(System.getProperty("mazerunner_audiofile"));
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                clip.loop();
                warning("The music system has been started in a loop.", "Music Loop Started");
            } else if (cmd.contains("music play")) {
                try {
                    clip = new OggClip(System.getProperty("mazerunner_audiofile"));
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                clip.play();
                warning("The music system has been started and is playing the audio one time.", "Audio system started.");
            }
        }
    }

    public String mon = "music on";

    public String moff = "music off";

    public String runcmd = null;

    private void runCmdActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane optionpane = new JOptionPane();
        runcmd = optionpane.showInputDialog(null, "Enter a Command", "Run Command", 3);
        optionpane.setLocation(75, 75);
        optionpane.setBounds(75, 75, 250, 75);
        int chkCmd = 0;
        if (runcmd != null) {
            chkCmd = JOptionPane.showConfirmDialog(null, "You entered " + runcmd + ". Is this correct?");
            if (chkCmd == 0) {
                System.setProperty("mazerunner_cmd", runcmd);
                chkCmdAndRun(runcmd);
            } else if (chkCmd == 1) {
                chkCmd = 0;
                runCmdActionPerformed(null);
            }
        }
    }

    private void enableDebugMenuItemStateChanged(java.awt.event.ItemEvent evt) {
        String isDbgEnabled = null;
        boolean isDebugEnabled = enableDebugMenu.getState();
        if (isDebugEnabled) isDbgEnabled = "true";
        if (!isDebugEnabled) isDbgEnabled = "false";
        System.setProperty("mazerunner_dbg", isDbgEnabled);
    }

    private void enableDebugMenuPropertyChange(java.beans.PropertyChangeEvent evt) {
    }

    private void enableDebugMenuVetoableChange(java.beans.PropertyChangeEvent evt) throws java.beans.PropertyVetoException {
    }

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String card = System.getProperty("mrunner_card");
        if (card == "card1" || card == null) {
            JOptionPane.showMessageDialog(null, "You have not passed the current level");
        } else {
            CardLayout cl = (CardLayout) (jPanel1.getLayout());
            cl.show(jPanel1, card);
            System.setProperty("mrunner_card", null);
            clip.stop();
            clip.close();
            try {
                clip = new OggClip(System.getProperty("mazerunner_audiofile"));
                clip.loop();
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean hitWWall = false;

    public int x = 0;

    public int y = 0;

    private void map1KeyPressed(java.awt.event.KeyEvent evt) {
    }

    private void bPipe1PaneMouseEntered(java.awt.event.MouseEvent evt) {
    }

    private void musstopActionPerformed(java.awt.event.ActionEvent evt) {
        clip.stop();
    }

    private void musstartActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            clip = new OggClip(System.getProperty("mazerunner_audiofile"));
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        clip.loop();
    }

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    /** Sets the location for the image. 
     * Also check to see if the movement was more then 15 pixels, and
     * if it was, the image will not move (cheating prevention)
     */
    private void map1MouseMoved(java.awt.event.MouseEvent evt) {
    }

    private javax.swing.JLabel bPipe1;

    private javax.swing.JPanel bPipe1Pane;

    private javax.swing.JCheckBoxMenuItem enableDebugMenu;

    private javax.swing.JMenuItem exit;

    private javax.swing.JMenuItem exit1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenu jMenu3;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JMenuItem jMenuItem3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JPanel map1;

    private maps.map2 map2;

    private maps.mapA mapA1;

    private javax.swing.JMenu musicmenu;

    private javax.swing.JMenuItem musstart;

    private javax.swing.JMenuItem musstop;

    private javax.swing.JButton nextButton;

    private javax.swing.JPopupMenu popupmenu;

    private javax.swing.JLabel rPipe1;

    private javax.swing.JMenuItem runCmd;

    private javax.swing.JLabel smile;

    private javax.swing.JPanel stats;

    private javax.swing.JMenuItem submitComment;

    private javax.swing.JPanel wall2;
}
