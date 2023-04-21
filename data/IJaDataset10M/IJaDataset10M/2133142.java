package org.digitall.projects.elsuri.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import org.digitall.common.systemmanager.ChangePassword;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicConfig;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.html.BrowserLauncher;
import org.digitall.lib.icons.IconTypes;

public class MainPanel extends BasicPanel {

    private JFrame parent;

    private ImageIcon dateContainerImage = IconTypes.getIcon("iconos/ui/desktoppanel/datecontainer.png");

    private ImageIcon btnContainerCenterImage = IconTypes.getIcon("iconos/ui/desktoppanel/btncontainercenter.png");

    private ImageIcon btnContainerRightImage = IconTypes.getIcon("iconos/ui/desktoppanel/btncontainerright.png");

    private ImageIcon btnContainerLeftImage = IconTypes.getIcon("iconos/ui/desktoppanel/btncontainerleft.png");

    private ImageIcon rightDownImage = IconTypes.getIcon("iconos/ui/desktoppanel/rightdown.png");

    private ImageIcon rightUpImage = IconTypes.getIcon("iconos/ui/desktoppanel/rightup.png");

    private ImageIcon topLeftImage = IconTypes.getIcon("iconos/ui/desktoppanel/leftup.png");

    private ImageIcon btnPlayerImage = IconTypes.getIcon("iconos/ui/desktoppanel/btnplayer.png");

    private ImageIcon btnNextImage = IconTypes.getIcon("iconos/ui/desktoppanel/btnnext.png");

    private ImageIcon btnPreviousImage = IconTypes.getIcon("iconos/ui/desktoppanel/btnprevious.png");

    private ImageIcon btnExitImage = IconTypes.getIcon("iconos/ui/desktoppanel/btnexit.png");

    private ImageIcon btnMinimizeImage = IconTypes.getIcon("iconos/ui/desktoppanel/btnminimize.png");

    private BasicPanel jpLeft = new BasicPanel();

    private BasicPanel jpRight = new BasicPanel();

    private BasicPanel jpCenter = new BasicPanel();

    private BorderLayout blMain = new BorderLayout();

    private BorderLayout blCenter = new BorderLayout();

    private BorderLayout blCentralPanel = new BorderLayout();

    private BorderLayout blRight = new BorderLayout();

    private BorderLayout blLeft = new BorderLayout();

    private GridBagLayout gblBtnContainer = new GridBagLayout();

    private GridBagLayout gblTopRight = new GridBagLayout();

    private GridBagLayout gblBottomRight = new GridBagLayout();

    private GridBagLayout gblTopLeft = new GridBagLayout();

    private GridBagLayout gblBottomLeft = new GridBagLayout();

    private BasicButton btnStickyNotes = new BasicButton(IconTypes.stickynotes_32x32);

    private BasicButton btnPlayer = new BasicButton(btnPlayerImage);

    private BasicButton btnMain = new BasicButton(IconTypes.main_32x32);

    private BasicButton btnPrevious = new BasicButton(btnPreviousImage);

    private BasicButton btnNext = new BasicButton(btnNextImage);

    private BasicPanel jpCentralPanel = new BasicPanel();

    private BasicPanel jpTopRight = new BasicPanel();

    private BasicPanel jpDesktopButtons = new BasicPanel();

    private BasicPanel jpTopLeft = new BasicPanel();

    private BasicPanel jpBottomRight = new BasicPanel();

    private BasicLabel lblDateContainer = Environment.lblCalendar;

    private BasicPanel jpBtnContainer = new BasicPanel();

    private BasicPanel jpBottomLeft = new BasicPanel();

    private BasicPanel jpButtonsContainer = new BasicPanel();

    private BasicLabel lblCenterContainer = new BasicLabel(btnContainerCenterImage);

    private BasicLabel lblRightContainer = new BasicLabel(btnContainerRightImage);

    private BasicLabel lblLeftContainer = new BasicLabel(btnContainerLeftImage);

    private BasicButton btnExit = new BasicButton(btnExitImage);

    private BasicButton btnMinimize = new BasicButton(btnMinimizeImage);

    private BoxLayout blButtonsContainer;

    private BasicLabel lblLeftFiller = new BasicLabel();

    private BasicLabel lblBottomRight = new BasicLabel(rightDownImage);

    private BasicLabel lblTopRight = new BasicLabel(rightUpImage);

    private BasicButton lblTopLeft = new BasicButton(topLeftImage);

    private BasicButton btnTaxes = new BasicButton(IconTypes.taxes_32x32);

    private BasicButton btnCashflow = new BasicButton(IconTypes.cashflow_32x32);

    private BasicButton btnResources = new BasicButton(IconTypes.resources_32x32);

    private BasicButton btnAssets = new BasicButton(IconTypes.assets_32x32);

    private BasicButton btnReports = new BasicButton(IconTypes.reports_32x32);

    private BasicButton btnGaia = new BasicButton(IconTypes.gaia_32x32);

    private Separator lblSeparator1 = new Separator();

    private Separator lblSeparator2 = new Separator();

    private Separator lblSeparator3 = new Separator();

    private Separator lblSeparator4 = new Separator();

    private Separator lblSeparator5 = new Separator();

    private Separator lblSeparator6 = new Separator();

    public MainPanel(JFrame _parent) {
        try {
            parent = _parent;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(blMain);
        this.setBounds(new Rectangle(10, 10, 800, 100));
        this.setSize(new Dimension(924, 100));
        jpLeft.setPreferredSize(new Dimension(150, 10));
        jpLeft.setLayout(blLeft);
        jpLeft.setOpaque(false);
        jpRight.setPreferredSize(new Dimension(150, 10));
        jpRight.setLayout(blRight);
        jpRight.setOpaque(false);
        jpCenter.setLayout(blCenter);
        jpCenter.setOpaque(false);
        btnStickyNotes.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnStickyNotes_actionPerformed(e);
            }
        });
        jpCentralPanel.setLayout(blCentralPanel);
        jpCentralPanel.setOpaque(false);
        lblDateContainer.setIcon(dateContainerImage);
        lblDateContainer.setHorizontalAlignment(SwingConstants.CENTER);
        lblDateContainer.setPreferredSize(new Dimension(365, 36));
        lblDateContainer.setSize(new Dimension(359, 26));
        lblDateContainer.setIconTextGap(0);
        lblDateContainer.setVerticalAlignment(SwingConstants.TOP);
        lblDateContainer.setHorizontalTextPosition(SwingConstants.CENTER);
        jpBtnContainer.setLayout(gblBtnContainer);
        jpBtnContainer.setPreferredSize(new Dimension(114, 33));
        jpBtnContainer.setOpaque(false);
        jpBottomRight.setLayout(gblBottomRight);
        jpBottomRight.setOpaque(false);
        jpBottomRight.setMaximumSize(new Dimension(75, 32));
        jpBottomRight.setMinimumSize(new Dimension(75, 32));
        jpBottomRight.setPreferredSize(new Dimension(85, 32));
        jpBottomLeft.setPreferredSize(new Dimension(70, 32));
        jpBottomLeft.setLayout(gblBottomLeft);
        jpBottomLeft.setOpaque(false);
        btnPlayer.setHorizontalAlignment(SwingConstants.CENTER);
        btnPlayer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnPlayer_actionPerformed(e);
            }
        });
        btnMain.setRolloverEnabled(true);
        btnMain.setRolloverIcon(IconTypes.main_ro_32x32);
        btnMain.setHorizontalAlignment(SwingConstants.CENTER);
        btnMain.setMaximumSize(new Dimension(70, 15));
        btnMain.setPreferredSize(new Dimension(70, 15));
        btnMain.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnMain_actionPerformed(e);
            }
        });
        btnPrevious.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnPrevious_actionPerformed(e);
            }
        });
        btnNext.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnNext_actionPerformed(e);
            }
        });
        lblCenterContainer.setHorizontalAlignment(SwingConstants.CENTER);
        lblCenterContainer.setPreferredSize(new Dimension(234, 24));
        lblRightContainer.setPreferredSize(new Dimension(80, 24));
        lblLeftContainer.setHorizontalAlignment(SwingConstants.RIGHT);
        lblLeftContainer.setPreferredSize(new Dimension(80, 24));
        btnExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnExit_actionPerformed(e);
            }
        });
        btnMinimize.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnMinimize_actionPerformed(e);
            }
        });
        jpButtonsContainer.setBounds(new Rectangle(29, 44, 394, 20));
        jpButtonsContainer.setPreferredSize(new Dimension(10, 51));
        jpButtonsContainer.setOpaque(false);
        blButtonsContainer = new BoxLayout(jpButtonsContainer, BoxLayout.X_AXIS);
        jpButtonsContainer.setLayout(blButtonsContainer);
        jpButtonsContainer.setSize(new Dimension(577, 51));
        jpButtonsContainer.setMaximumSize(new Dimension(82, 51));
        lblLeftFiller.setMaximumSize(new Dimension(20, 22));
        lblLeftFiller.setMinimumSize(new Dimension(20, 22));
        lblLeftFiller.setPreferredSize(new Dimension(0, 22));
        lblBottomRight.setIconTextGap(0);
        lblBottomRight.setPreferredSize(new Dimension(85, 29));
        lblBottomRight.setHorizontalAlignment(SwingConstants.LEFT);
        lblTopLeft.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                lblTopLeft_actionPerformed(e);
            }
        });
        btnTaxes.setRolloverEnabled(true);
        btnTaxes.setRolloverIcon(IconTypes.taxes_ro_32x32);
        btnTaxes.setHorizontalAlignment(SwingConstants.CENTER);
        btnTaxes.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnTaxes_actionPerformed(e);
            }
        });
        btnCashflow.setRolloverEnabled(true);
        btnCashflow.setRolloverIcon(IconTypes.cashflow_ro_32x32);
        btnCashflow.setHorizontalAlignment(SwingConstants.CENTER);
        btnCashflow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnCashflow_actionPerformed(e);
            }
        });
        btnResources.setRolloverEnabled(true);
        btnResources.setRolloverIcon(IconTypes.resources_ro_32x32);
        btnResources.setHorizontalAlignment(SwingConstants.CENTER);
        btnResources.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnResources_actionPerformed(e);
            }
        });
        btnAssets.setRolloverEnabled(true);
        btnAssets.setRolloverIcon(IconTypes.assets_ro_32x32);
        btnAssets.setHorizontalAlignment(SwingConstants.CENTER);
        btnAssets.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnAssets_actionPerformed(e);
            }
        });
        btnReports.setRolloverEnabled(true);
        btnReports.setRolloverIcon(IconTypes.reports_ro_32x32);
        btnReports.setHorizontalAlignment(SwingConstants.CENTER);
        btnReports.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnReports_actionPerformed(e);
            }
        });
        btnGaia.setRolloverEnabled(true);
        btnGaia.setRolloverIcon(IconTypes.gaia_ro_32x32);
        btnGaia.setHorizontalAlignment(SwingConstants.CENTER);
        btnGaia.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnGaia_actionPerformed(e);
            }
        });
        jpTopRight.setLayout(gblTopRight);
        jpTopRight.setOpaque(false);
        jpTopRight.setMinimumSize(new Dimension(150, 68));
        jpTopRight.setPreferredSize(new Dimension(150, 68));
        jpTopRight.setSize(new Dimension(150, 68));
        jpTopRight.setMaximumSize(new Dimension(150, 68));
        jpTopRight.setBounds(new Rectangle(0, 0, 150, 68));
        jpDesktopButtons.setOpaque(false);
        jpTopLeft.setLayout(gblTopLeft);
        jpTopLeft.setOpaque(false);
        jpLeft.add(jpBottomLeft, BorderLayout.SOUTH);
        jpTopLeft.add(lblTopLeft, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 25, 0, 0), 0, 0));
        jpLeft.add(jpTopLeft, BorderLayout.CENTER);
        this.add(jpLeft, BorderLayout.WEST);
        jpDesktopButtons.add(btnPrevious, null);
        jpDesktopButtons.add(btnNext, null);
        jpRight.add(jpBottomRight, BorderLayout.SOUTH);
        jpTopRight.add(btnExit, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 35, 0, 0), 0, 0));
        jpTopRight.add(btnMinimize, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(-6, 0, 0, 0), 0, 0));
        jpTopRight.add(lblTopRight, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 25), 0, 0));
        jpRight.add(jpTopRight, BorderLayout.CENTER);
        this.add(jpRight, BorderLayout.EAST);
        jpCenter.add(btnStickyNotes, BorderLayout.EAST);
        jpCentralPanel.add(jpBtnContainer, BorderLayout.CENTER);
        jpCentralPanel.add(lblDateContainer, BorderLayout.NORTH);
        jpButtonsContainer.add(lblLeftFiller, null);
        jpButtonsContainer.add(btnTaxes, null);
        jpButtonsContainer.add(lblSeparator1, null);
        jpButtonsContainer.add(btnCashflow, null);
        jpButtonsContainer.add(lblSeparator2, null);
        jpButtonsContainer.add(btnResources, null);
        jpButtonsContainer.add(lblSeparator3, null);
        jpButtonsContainer.add(btnAssets, null);
        jpButtonsContainer.add(lblSeparator5, null);
        jpButtonsContainer.add(btnReports, null);
        jpButtonsContainer.add(lblSeparator6, null);
        jpButtonsContainer.add(btnGaia, null);
        jpBtnContainer.add(jpButtonsContainer, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(-22, 0, 8, 0), 450, 0));
        jpBtnContainer.add(lblCenterContainer, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 0, 0), 0, 0));
        jpBtnContainer.add(lblRightContainer, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(12, 0, 0, 0), 0, 0));
        jpBtnContainer.add(lblLeftContainer, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(12, 0, 0, 0), 0, 0));
        jpCenter.add(btnMain, BorderLayout.WEST);
        jpCenter.add(jpCentralPanel, BorderLayout.CENTER);
        this.add(jpCenter, BorderLayout.CENTER);
        btnMain.setToolTipText(Environment.MODULE_MAIN);
        btnTaxes.setToolTipText(Environment.MODULE_TAXES);
        btnCashflow.setToolTipText(Environment.MODULE_CASHFLOW);
        btnResources.setToolTipText(Environment.MODULE_RESOURCES);
        btnAssets.setToolTipText(Environment.MODULE_ASSETS);
        btnReports.setToolTipText(Environment.MODULE_REPORTS);
        btnGaia.setToolTipText(Environment.MODULE_GAIA);
        btnStickyNotes.setToolTipText(Environment.MODULE_STICKYNOTES);
        lblTopLeft.setToolTipText("http://www.digitallsh.com.ar");
        btnExit.setToolTipText("Salir");
        btnMinimize.setToolTipText("Minimizar - (F11 Pantalla completa)");
        lblDateContainer.setForeground(Color.black);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        GradientPaint gradient = new GradientPaint(0, 0, BasicConfig.PANEL_GRADIENT_START_COLOR, 0, h, BasicConfig.PANEL_GRADIENT_END_COLOR, false);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, w, h);
    }

    private void btnExit_actionPerformed(ActionEvent e) {
        if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == ActionEvent.SHIFT_MASK) {
            ChangePassword pass = new ChangePassword(Environment.sessionUser);
            pass.setModal(true);
            pass.setVisible(true);
        } else {
            closeApplication();
        }
    }

    public void closeApplication() {
        Advisor.closeApplication();
    }

    private void btnAssets_actionPerformed(ActionEvent e) {
        Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_ASSETS));
    }

    private void btnCashflow_actionPerformed(ActionEvent e) {
        Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_CASHFLOW));
    }

    private void btnGaia_actionPerformed(ActionEvent e) {
        Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_GAIA));
    }

    private void btnMain_actionPerformed(ActionEvent e) {
        Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_MAIN));
    }

    private void btnReports_actionPerformed(ActionEvent e) {
        Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_REPORTS));
    }

    private void btnResources_actionPerformed(ActionEvent e) {
        Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_RESOURCES));
    }

    private void btnStickyNotes_actionPerformed(ActionEvent e) {
        Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_STICKYNOTES));
    }

    private void btnTaxes_actionPerformed(ActionEvent e) {
        Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_TAXES));
    }

    private void btnPrevious_actionPerformed(ActionEvent e) {
    }

    private void btnNext_actionPerformed(ActionEvent e) {
    }

    private void btnPlayer_actionPerformed(ActionEvent e) {
    }

    private void lblTopLeft_actionPerformed(ActionEvent e) {
        BrowserLauncher.openURL("http://www.digitallsh.com.ar/");
    }

    private void btnMinimize_actionPerformed(ActionEvent e) {
        parent.setState(JFrame.ICONIFIED);
    }

    private class Separator extends BasicLabel {

        private int width = 15;

        public Separator() {
            super(IconTypes.getIcon("iconos/ui/desktoppanel/btnseparator.png"));
            setPreferredSize(new Dimension(width, 24));
            setSize(new Dimension(width, 24));
            setMinimumSize(new Dimension(width, 24));
            setMaximumSize(new Dimension(width, 24));
        }
    }
}
