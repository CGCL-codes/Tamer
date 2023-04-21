package com.umc.gui.content;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.parserapplications.filterbuilder.layouts.NullLayoutManager;
import com.umc.collector.Publisher;
import com.umc.gui.GuiController;
import com.umc.helper.Statistic;
import com.umc.helper.UMCConstants;
import com.umc.helper.UMCLanguage;

/**
 * @author Administrator
 */
public class ScanStatusPanel extends JPanel {

    private static final long serialVersionUID = -3471259981606467365L;

    public static final int STATUS_WORKING = 1;

    public static final int STATUS_DONE = 2;

    public static final int STATUS_NOT_USED = 3;

    private Color color_working = Color.white;

    private Color color_idle = Color.gray;

    private JLabel statusCollecotor;

    private JLabel labelCollecotor;

    private JLabel statusPublischer;

    private JLabel labelPublischer;

    private JLabel statusBasicInfos;

    private JLabel labelBasicInfos;

    private JLabel statusMG;

    private JLabel labelMG;

    private JLabel statusUG;

    private JLabel labelUG;

    private JLabel statusSG;

    private JLabel labelSG;

    private JLabel statusUG_XML;

    private JLabel labelUG_XML;

    private JLabel statusBackgroundworker;

    private JLabel labelBackgroundworker;

    private JLabel statusMovieFiles;

    private JLabel labelMovieFiles;

    private JLabel statusWarnings;

    private JLabel labelWarnings;

    private JLabel statusHelperTable;

    private JLabel labelHelperTable;

    private JLabel statusHTML;

    private JLabel labelHTML;

    private JLabel statusPDF;

    private JLabel labelPDF;

    private JLabel statusFrontend;

    private JLabel labelFrontend;

    private JLabel labelResult;

    private int fileCount = 0;

    private String actualStep = "";

    private boolean scanning = false;

    private int initialJobCount = 0;

    public ScanStatusPanel() {
        initComponents();
        Runnable testRunnable = new Runnable() {

            public void run() {
                while (true && Publisher.getInstance().getJobCount() > 0) {
                    repaint();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException exc) {
                    }
                }
            }
        };
        Thread aThread = new Thread(testRunnable);
        aThread.start();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintFrame(g2);
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new java.awt.Font("Helvetica", Font.BOLD, 30));
        if (scanning) g2.drawString(fileCount + " / " + initialJobCount, 20, 70); else g2.drawString(fileCount + "", 20, 70);
        g2.setFont(new java.awt.Font("Helvetica", Font.BOLD, 20));
        g2.drawString(actualStep, 265, 65);
        g2.setFont(new java.awt.Font("Helvetica", Font.BOLD, 10));
        g2.drawString("[Terminator 1]", 20, 140);
        g2.drawString("[Terminator 2]", 20, 160);
        g2.drawString("[Der Herr Der Ringe]", 20, 180);
        g2.drawString("[Stirb Langsam 4.0]", 20, 200);
        g2.drawString("[Underworld]", 20, 220);
    }

    public void paintFrame(Graphics2D g2) {
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(5, 5, 200, 100, 10, 10);
        g2.setColor(UMCConstants.guiColor);
        g2.fillRoundRect(7, 7, 196, 96, 10, 10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(5, 7, 200, 10, 10, 10);
        g2.fillRect(5, 8, 200, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Count", 10, 16);
        g2.setFont(new java.awt.Font("HELVETICA", Font.PLAIN, 11));
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(210, 5, getWidth() - 221, 100, 10, 10);
        g2.setColor(UMCConstants.guiColor);
        g2.fillRoundRect(212, 7, getWidth() - 225, 96, 10, 10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(210, 7, getWidth() - 221, 10, 10, 10);
        g2.fillRect(210, 8, getWidth() - 221, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Actual Step:", 215, 16);
        g2.setFont(new java.awt.Font("HELVETICA", Font.PLAIN, 11));
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(5, 110, getWidth() - 16, getHeight() - 315, 10, 10);
        g2.setColor(UMCConstants.guiColor);
        g2.fillRoundRect(7, 112, getWidth() - 21, getHeight() - 319, 10, 10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(5, 112, getWidth() - 16, 10, 10, 10);
        g2.fillRect(5, 113, getWidth() - 16, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Scanning...", 10, 120);
        g2.setFont(new java.awt.Font("HELVETICA", Font.PLAIN, 11));
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(5, getHeight() - 200, getWidth() - 16, 187, 10, 10);
        g2.setColor(UMCConstants.guiColor);
        g2.fillRoundRect(7, getHeight() - 198, getWidth() - 21, 191, 10, 10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(5, getHeight() - 198, getWidth() - 16, 10, 10, 10);
        g2.fillRect(5, getHeight() - 197, getWidth() - 16, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Warnings:", 10, getHeight() - 190);
        g2.setFont(new java.awt.Font("HELVETICA", Font.PLAIN, 11));
    }

    private void initComponents() {
        ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + "/resources/Gui/done.gif");
        statusCollecotor = new JLabel(icon);
        statusCollecotor.setEnabled(false);
        statusPublischer = new JLabel(icon);
        statusPublischer.setEnabled(false);
        statusBasicInfos = new JLabel(icon);
        statusBasicInfos.setEnabled(false);
        statusMG = new JLabel(icon);
        statusMG.setEnabled(false);
        statusUG = new JLabel(icon);
        statusUG.setEnabled(false);
        statusSG = new JLabel(icon);
        statusSG.setEnabled(false);
        statusUG_XML = new JLabel(icon);
        statusUG_XML.setEnabled(false);
        statusBackgroundworker = new JLabel(icon);
        statusBackgroundworker.setEnabled(false);
        statusMovieFiles = new JLabel(icon);
        statusMovieFiles.setEnabled(false);
        statusWarnings = new JLabel(icon);
        statusWarnings.setEnabled(false);
        statusHelperTable = new JLabel(icon);
        statusHelperTable.setEnabled(false);
        statusHTML = new JLabel(icon);
        statusHTML.setEnabled(false);
        statusPDF = new JLabel(icon);
        statusPDF.setEnabled(false);
        statusFrontend = new JLabel(icon);
        statusFrontend.setEnabled(false);
        labelCollecotor = new JLabel(UMCLanguage.getText("gui.scan.status.collector"));
        labelCollecotor.setForeground(color_idle);
        labelPublischer = new JLabel(UMCLanguage.getText("gui.scan.status.publisher"));
        labelPublischer.setForeground(color_idle);
        labelBasicInfos = new JLabel(UMCLanguage.getText("gui.scan.status.basicinfo"));
        labelBasicInfos.setForeground(color_idle);
        labelMG = new JLabel(UMCLanguage.getText("gui.scan.status.mg"));
        labelMG.setForeground(color_idle);
        labelUG = new JLabel(UMCLanguage.getText("gui.scan.status.ug"));
        labelUG.setForeground(color_idle);
        labelSG = new JLabel(UMCLanguage.getText("gui.scan.status.sg"));
        labelSG.setForeground(color_idle);
        labelUG_XML = new JLabel(UMCLanguage.getText("gui.scan.status.ug_xml"));
        labelUG_XML.setForeground(color_idle);
        labelBackgroundworker = new JLabel(UMCLanguage.getText("gui.scan.status.backgroundworker"));
        labelBackgroundworker.setForeground(color_idle);
        labelMovieFiles = new JLabel(UMCLanguage.getText("gui.scan.status.moviefile"));
        labelMovieFiles.setForeground(color_idle);
        labelWarnings = new JLabel(UMCLanguage.getText("gui.scan.status.warnings"));
        labelWarnings.setForeground(color_idle);
        labelHelperTable = new JLabel(UMCLanguage.getText("gui.scan.status.helpertable"));
        labelHelperTable.setForeground(color_idle);
        labelHTML = new JLabel(UMCLanguage.getText("gui.scan.status.html"));
        labelHTML.setForeground(color_idle);
        labelPDF = new JLabel(UMCLanguage.getText("gui.scan.status.pdf"));
        labelPDF.setForeground(color_idle);
        labelFrontend = new JLabel(UMCLanguage.getText("gui.scan.status.frontend"));
        labelFrontend.setForeground(color_idle);
        labelResult = new JLabel("  ");
        setBackground(new Color(40, 41, 59, 255));
        setLayout(new NullLayoutManager());
        JLabel jl = new JLabel();
        jl.setLocation(225, 40);
        jl.setIcon(new ImageIcon(System.getProperty("user.dir") + UMCConstants.fileSeparator + "resources" + UMCConstants.fileSeparator + "Gui" + UMCConstants.fileSeparator + "loading.gif"));
        add(jl);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
        flowLayout.setVgap(0);
        flowLayout.setHgap(4);
        JPanel panelCollector = new JPanel(flowLayout);
        panelCollector.setOpaque(false);
        panelCollector.add(statusCollecotor);
        panelCollector.add(labelCollecotor);
        JPanel panelPublischer = new JPanel(flowLayout);
        panelPublischer.setOpaque(false);
        panelPublischer.add(statusPublischer);
        panelPublischer.add(labelPublischer);
        JPanel panelBasicInfos = new JPanel(flowLayout);
        panelBasicInfos.setOpaque(false);
        panelBasicInfos.add(statusBasicInfos);
        panelBasicInfos.add(labelBasicInfos);
        JPanel panelMG = new JPanel(flowLayout);
        panelMG.setOpaque(false);
        panelMG.add(statusMG);
        panelMG.add(labelMG);
        JPanel panelUG = new JPanel(flowLayout);
        panelUG.setOpaque(false);
        panelUG.add(statusUG);
        panelUG.add(labelUG);
        JPanel panelSG = new JPanel(flowLayout);
        panelSG.setOpaque(false);
        panelSG.add(statusSG);
        panelSG.add(labelSG);
        JPanel panelUG_XML = new JPanel(flowLayout);
        panelUG_XML.setOpaque(false);
        panelUG_XML.add(statusUG_XML);
        panelUG_XML.add(labelUG_XML);
        JPanel panelBackgroundworker = new JPanel(flowLayout);
        panelBackgroundworker.setOpaque(false);
        panelBackgroundworker.add(statusBackgroundworker);
        panelBackgroundworker.add(labelBackgroundworker);
        JPanel panelMovieFiles = new JPanel(flowLayout);
        panelMovieFiles.setOpaque(false);
        panelMovieFiles.add(statusMovieFiles);
        panelMovieFiles.add(labelMovieFiles);
        JPanel panelWarnings = new JPanel(flowLayout);
        panelWarnings.setOpaque(false);
        panelWarnings.add(statusWarnings);
        panelWarnings.add(labelWarnings);
        JPanel panelHelperTable = new JPanel(flowLayout);
        panelHelperTable.setOpaque(false);
        panelHelperTable.add(statusHelperTable);
        panelHelperTable.add(labelHelperTable);
        JPanel panelHTML = new JPanel(flowLayout);
        panelHTML.setOpaque(false);
        panelHTML.add(statusHTML);
        panelHTML.add(labelHTML);
        JPanel panelPDF = new JPanel(flowLayout);
        panelPDF.setOpaque(false);
        panelPDF.add(statusPDF);
        panelPDF.add(labelPDF);
        JPanel panelFrontend = new JPanel(flowLayout);
        panelFrontend.setOpaque(false);
        panelFrontend.add(statusFrontend);
        panelFrontend.add(labelFrontend);
        JPanel panelResult = new JPanel(flowLayout);
        panelResult.setOpaque(false);
        panelResult.add(labelResult);
    }

    /**
	 * Increments a variable which will be paintet in the 'count' frame
	 * or assigns the given value and paints it.
	 */
    public void incrementCounter(Integer value) {
        if (value != null) fileCount = value; else fileCount++;
        repaint();
    }

    /**
	 * Resets the counter
	 */
    public void resetCounter() {
        fileCount = 0;
        repaint();
    }

    /**
	 * The given text will be painted in the 'actual step' frame
	 * @param s
	 */
    public void setActualStep(String s) {
        actualStep = s;
        repaint();
    }

    /**
	 * Sets the scanning flag.
	 * @param b
	 */
    public void setScanning(boolean b) {
        scanning = b;
        initialJobCount = Publisher.getInstance().getJobCount();
    }

    public void setStatusCollecotor(int status) {
        if (status == STATUS_WORKING) {
            labelCollecotor.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelCollecotor.setForeground(color_idle);
            statusCollecotor.setEnabled(true);
        }
    }

    public void setStatusPublischer(int status) {
        if (status == STATUS_WORKING) {
            labelPublischer.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelPublischer.setForeground(color_idle);
            statusPublischer.setEnabled(true);
        }
    }

    public void setStatusBasicInfos(int status) {
        if (status == STATUS_WORKING) {
            labelBasicInfos.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelBasicInfos.setForeground(color_idle);
            statusBasicInfos.setEnabled(true);
        }
    }

    public void setStatusMG(int status) {
        if (status == STATUS_WORKING) {
            labelMG.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelMG.setForeground(color_idle);
            statusMG.setEnabled(true);
        }
    }

    public void setStatusUG(int status) {
        if (status == STATUS_WORKING) {
            labelUG.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelUG.setForeground(color_idle);
            statusUG.setEnabled(true);
        }
    }

    public void setStatusSG(int status) {
        if (status == STATUS_WORKING) {
            labelSG.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelSG.setForeground(color_idle);
            statusSG.setEnabled(true);
        }
    }

    public void setStatusUG_XML(int status) {
        if (status == STATUS_WORKING) {
            labelUG_XML.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelUG_XML.setForeground(color_idle);
            statusUG_XML.setEnabled(true);
        }
    }

    public void setStatusBackgroundworker(int status) {
        if (status == STATUS_WORKING) {
            labelBackgroundworker.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelBackgroundworker.setForeground(color_idle);
            statusBackgroundworker.setEnabled(true);
        }
    }

    public void setStatusMovieFiles(int status) {
        if (status == STATUS_WORKING) {
            labelMovieFiles.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelMovieFiles.setForeground(color_idle);
            statusMovieFiles.setEnabled(true);
        }
    }

    public void setStatusHelperTable(int status) {
        if (status == STATUS_WORKING) {
            labelHelperTable.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelHelperTable.setForeground(color_idle);
            statusHelperTable.setEnabled(true);
        }
    }

    public void setStatusHTML(int status) {
        if (status == STATUS_WORKING) {
            labelHTML.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelHTML.setForeground(color_idle);
            statusHTML.setEnabled(true);
        } else if (status == STATUS_NOT_USED) {
            labelHTML.setForeground(color_idle);
            labelHTML.setText("<html><s>" + labelHTML.getText());
            statusHTML.setEnabled(true);
        }
    }

    public void setStatusPDF(int status) {
        if (status == STATUS_WORKING) {
            labelPDF.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelPDF.setForeground(color_idle);
            statusPDF.setEnabled(true);
        } else if (status == STATUS_NOT_USED) {
            labelPDF.setForeground(color_idle);
            labelPDF.setText("<html><s>" + labelPDF.getText());
            statusPDF.setEnabled(true);
        }
    }

    public void setStatusFrontend(int status) {
        if (status == STATUS_WORKING) {
            labelFrontend.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelFrontend.setForeground(color_idle);
            statusFrontend.setEnabled(true);
        }
    }

    public void setStatusWarnings(int status) {
        if (status == STATUS_WORKING) {
            labelWarnings.setForeground(color_working);
        } else if (status == STATUS_DONE) {
            labelWarnings.setForeground(color_idle);
            statusWarnings.setEnabled(true);
        }
    }

    public void setStatusResult(String s) {
    }

    public void setProgressBar(int min, int max) {
    }

    public void updateProgressBar(int status) {
    }

    public void updateProgressBar() {
    }
}
