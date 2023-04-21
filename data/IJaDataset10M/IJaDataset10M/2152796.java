package realtimetrading.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import realtimetrading.gui.book.BookGui;
import realtimetrading.gui.exchange.ContractGui;
import realtimetrading.gui.graphquote.GQgui;
import realtimetrading.market.Market;

/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class MarketGui extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private JToolBar jAgentBar;

    private JButton jBookWatcherButton;

    private JPanel jPanel1;

    private JMenuItem jMenuLocalDataItem;

    private JMenuItem jMenuQMFinecoItem;

    private JMenu jMenu1;

    private JMenuBar jMenuBar;

    private JDesktopPane jDesktopPane;

    private JButton jButtonGraph;

    private JButton jButtonContractList;

    private JButton jButtonReset;

    private JPanel CommandPanel;

    private JToggleButton jToggleButtonPlay;

    private BookGui bookGui;

    private ContractGui contractGui;

    private GQgui gqGui;

    private static Market MARKET;

    /**
	 * Auto-generated main method to display this JFrame
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MarketGui inst = new MarketGui();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    /**
	 * Costruttore 
	 */
    public MarketGui() {
        super();
        initGUI();
        this.setVisible(true);
    }

    public void setMarket(Market M) {
        this.MARKET = M;
    }

    /**
	 * Inizializza l'interfaccia grafica
	 */
    private void initGUI() {
        try {
            this.setPreferredSize(new java.awt.Dimension(682, 529));
            this.setSize(682, 529);
            this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
            this.setTitle("RTT - Real Time Trading");
            this.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent evt) {
                    thisWindowClosing(evt);
                }
            });
            getContentPane().add(getJAgentBar(), BorderLayout.EAST);
            getContentPane().add(getJDesktopPane(), BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Barra deli agenti
	 * @return
	 */
    private JToolBar getJAgentBar() {
        if (jAgentBar == null) {
            jAgentBar = new JToolBar();
            jAgentBar.setOrientation(SwingConstants.VERTICAL);
            jAgentBar.add(getJPanel1());
        }
        return jAgentBar;
    }

    private JButton getJBookWatcherButton() {
        if (jBookWatcherButton == null) {
            jBookWatcherButton = new JButton();
            FlowLayout jButton1Layout = new FlowLayout();
            jBookWatcherButton.setLayout(jButton1Layout);
            jBookWatcherButton.setText("BookWatcher");
            jBookWatcherButton.setSize(34, 26);
            jBookWatcherButton.setPreferredSize(new java.awt.Dimension(104, 28));
            jBookWatcherButton.setHorizontalTextPosition(SwingConstants.CENTER);
            jBookWatcherButton.setFont(new java.awt.Font("Segoe UI", 0, 9));
            jBookWatcherButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    jBookWatcherButtonActionPerformed(evt);
                }
            });
        }
        return jBookWatcherButton;
    }

    private JDesktopPane getJDesktopPane() {
        if (jDesktopPane == null) {
            jDesktopPane = new JDesktopPane();
            jDesktopPane.setPreferredSize(new java.awt.Dimension(639, 333));
            jDesktopPane.setDoubleBuffered(true);
            jDesktopPane.setBackground(Color.DARK_GRAY);
        }
        return jDesktopPane;
    }

    private void jBookWatcherButtonActionPerformed(ActionEvent evt) {
        getJDesktopPane().add(getBookGui());
        getBookGui().toFront();
    }

    private BookGui getBookGui() {
        if (bookGui == null) {
            bookGui = new BookGui(MARKET.BOOK);
        }
        return bookGui;
    }

    private void thisWindowClosing(WindowEvent evt) {
        System.out.println("RTT Close event");
        MARKET.exitTrading();
        JInternalFrame[] allFrame = getJDesktopPane().getAllFrames();
        for (int i = 0; i < allFrame.length; i++) allFrame[i].dispose();
    }

    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            FlowLayout jPanel1Layout = new FlowLayout();
            jPanel1Layout.setHgap(10);
            jPanel1Layout.setVgap(3);
            jPanel1Layout.setAlignment(FlowLayout.LEFT);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1.setPreferredSize(new java.awt.Dimension(120, 40));
            jPanel1.setSize(120, 40);
            jPanel1.add(getCommandPanel());
            jPanel1.add(getJBookWatcherButton());
            jPanel1.add(getJButtonContractList());
            jPanel1.add(getJButtonGraph());
        }
        return jPanel1;
    }

    private JToggleButton getJToggleButtonPlay() {
        if (jToggleButtonPlay == null) {
            jToggleButtonPlay = new JToggleButton();
            jToggleButtonPlay.setText("P");
            jToggleButtonPlay.setFont(new java.awt.Font("AlArabiya", 0, 9));
            jToggleButtonPlay.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    jToggleButtonPlayActionPerformed(evt);
                }
            });
        }
        return jToggleButtonPlay;
    }

    private void jToggleButtonPlayActionPerformed(ActionEvent evt) {
        if (getJToggleButtonPlay().isSelected()) {
            MARKET.startTrading();
        } else MARKET.stopTrading();
    }

    public void refresh() {
        getBookGui().refresh();
        getContractGui().refresh();
        getGQgui().refresh();
    }

    private JPanel getCommandPanel() {
        if (CommandPanel == null) {
            CommandPanel = new JPanel();
            GridLayout CommandPanelLayout = new GridLayout(1, 1);
            CommandPanelLayout.setColumns(1);
            CommandPanelLayout.setHgap(5);
            CommandPanelLayout.setVgap(5);
            CommandPanel.setLayout(CommandPanelLayout);
            CommandPanel.setPreferredSize(new java.awt.Dimension(104, 28));
            CommandPanel.add(getJToggleButtonPlay());
            CommandPanel.add(getJButtonReset());
        }
        return CommandPanel;
    }

    private JButton getJButtonReset() {
        if (jButtonReset == null) {
            jButtonReset = new JButton();
            jButtonReset.setText("R");
            jButtonReset.setFont(new java.awt.Font("AlArabiya", 0, 9));
        }
        return jButtonReset;
    }

    private JButton getJButtonContractList() {
        if (jButtonContractList == null) {
            jButtonContractList = new JButton();
            jButtonContractList.setText("ContractList");
            jButtonContractList.setPreferredSize(new java.awt.Dimension(104, 28));
            jButtonContractList.setHorizontalTextPosition(SwingConstants.CENTER);
            jButtonContractList.setFont(new java.awt.Font("Segoe UI", 0, 9));
            jButtonContractList.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    jButtonContractListActionPerformed(evt);
                }
            });
        }
        return jButtonContractList;
    }

    private void jButtonContractListActionPerformed(ActionEvent evt) {
        getJDesktopPane().add(getContractGui());
        getContractGui().toFront();
    }

    private ContractGui getContractGui() {
        if (contractGui == null) {
            contractGui = new ContractGui(MARKET.CONTRACT);
        }
        return contractGui;
    }

    private JButton getJButtonGraph() {
        if (jButtonGraph == null) {
            jButtonGraph = new JButton();
            jButtonGraph.setText("Graph");
            jButtonGraph.setPreferredSize(new java.awt.Dimension(104, 28));
            jButtonGraph.setHorizontalTextPosition(SwingConstants.CENTER);
            jButtonGraph.setFont(new java.awt.Font("Segoe UI", 0, 9));
            jButtonGraph.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    jButtonGraphActionPerformed(evt);
                }
            });
        }
        return jButtonGraph;
    }

    private GQgui getGQgui() {
        if (gqGui == null) {
            gqGui = new GQgui(MARKET.CONTRACT);
        }
        return gqGui;
    }

    private void jButtonGraphActionPerformed(ActionEvent evt) {
        getJDesktopPane().add(getGQgui());
        getContractGui().toFront();
    }
}
