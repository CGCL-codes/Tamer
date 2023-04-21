package client.gui.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import megamek.client.ui.AWT.MechView;
import megamek.client.ui.AWT.UnitFailureDialog;
import megamek.client.ui.AWT.UnitLoadingDialog;
import megamek.common.Entity;
import megamek.common.MechFileParser;
import megamek.common.MechSummary;
import megamek.common.MechSummaryCache;
import megamek.common.MechSummaryComparator;
import megamek.common.loaders.EntityLoadingException;
import client.MWClient;
import client.gui.CMainFrame;
import client.gui.MechInfo;
import common.CampaignData;
import common.util.SpringLayoutHelper;
import common.util.UnitUtils;

public class RepodSelectorDialog extends JFrame implements ActionListener, KeyListener, ListSelectionListener, Runnable, WindowListener, ItemListener {

    /**
     * 
     */
    private static final long serialVersionUID = -6467246609231845514L;

    private static final int KEY_TIMEOUT = 1000;

    private CMainFrame clientgui;

    private MechSummary[] mechsCurrent;

    private UnitLoadingDialog unitLoadingDialog;

    private StringBuilder m_sbSearch = new StringBuilder();

    private long m_nLastSearch = 0;

    private JPanel pParams = new JPanel();

    DefaultListModel defaultModel = null;

    ListSelectionModel listSelectionModel = null;

    JList mechList = null;

    JScrollPane listScrollPane = null;

    JScrollPane leftScrollPane = null;

    JScrollPane rightScrollPane = null;

    private JButton bRepod = new JButton("Repod");

    private JButton bCancel = new JButton("Close");

    private JButton bRandom = new JButton("Random");

    private JTextArea mechViewLeft = null;

    private JTextArea mechViewRight = null;

    private JPanel pUpper = new JPanel();

    private JPanel pPreview = new JPanel();

    private MWClient mwclient = null;

    private TreeMap<String, String> chassieList = new TreeMap<String, String>();

    private String unitId = "";

    private boolean global = false;

    public RepodSelectorDialog(CMainFrame cl, UnitLoadingDialog uld, MWClient mwclient, String chassieList, String unitId) {
        super("Repod Selector");
        clientgui = cl;
        unitLoadingDialog = uld;
        this.mwclient = mwclient;
        StringTokenizer ST = new StringTokenizer(chassieList, "#");
        while (ST.hasMoreElements()) {
            String tempstr = ST.nextToken();
            if (tempstr.equals("GLOBAL")) {
                global = true;
            } else if (tempstr.contains(".")) {
                String chassieMods = ST.nextToken();
                this.chassieList.put(tempstr, chassieMods);
            }
        }
        this.unitId = unitId;
        mechViewLeft = new JTextArea(22, 29);
        mechViewRight = new JTextArea(22, 34);
        defaultModel = new DefaultListModel();
        mechList = new JList(defaultModel);
        listSelectionModel = mechList.getSelectionModel();
        mechList.setVisibleRowCount(22);
        listSelectionModel.addListSelectionListener(this);
        listScrollPane = new JScrollPane(mechList);
        leftScrollPane = new JScrollPane(mechViewLeft);
        rightScrollPane = new JScrollPane(mechViewRight);
        listScrollPane.setAlignmentX(LEFT_ALIGNMENT);
        listScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        leftScrollPane.setAlignmentX(LEFT_ALIGNMENT);
        leftScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.setAlignmentX(LEFT_ALIGNMENT);
        rightScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mechList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mechViewLeft.setFont(new Font("Monospaced", Font.PLAIN, 11));
        mechViewRight.setFont(new Font("Monospaced", Font.PLAIN, 11));
        mechList.setFont(new Font("Monospaced", Font.PLAIN, 11));
        pPreview = new MechInfo(mwclient);
        pPreview.setVisible(false);
        pPreview.setMinimumSize(new Dimension(86, 74));
        pPreview.setMaximumSize(new Dimension(86, 74));
        pUpper.setLayout(new FlowLayout(FlowLayout.CENTER));
        pUpper.add(pParams);
        pUpper.add(pPreview);
        JPanel textBoxSpring = new JPanel(new SpringLayout());
        textBoxSpring.add(listScrollPane);
        textBoxSpring.add(leftScrollPane);
        textBoxSpring.add(rightScrollPane);
        SpringLayoutHelper.setupSpringGrid(textBoxSpring, 1, 3);
        JPanel buttonHolder = new JPanel();
        buttonHolder.add(bRepod);
        buttonHolder.add(bRandom);
        buttonHolder.add(bCancel);
        getRootPane().setDefaultButton(bCancel);
        JPanel springHolder = new JPanel(new SpringLayout());
        springHolder.add(pUpper);
        springHolder.add(textBoxSpring);
        springHolder.add(buttonHolder);
        SpringLayoutHelper.setupSpringGrid(springHolder, 3, 1);
        getContentPane().add(springHolder);
        clearMechPreview();
        setSize(785, 560);
        setResizable(false);
        bRandom.setEnabled(Boolean.parseBoolean(mwclient.getserverConfigs("RandomRepodAllowed")));
        bRepod.setEnabled(!Boolean.parseBoolean(mwclient.getserverConfigs("RandomRepodOnly")));
        mechList.addListSelectionListener(this);
        mechList.addKeyListener(this);
        bRepod.addActionListener(this);
        bCancel.addActionListener(this);
        bRandom.addActionListener(this);
        addWindowListener(this);
    }

    public void run() {
        filterMechs();
        sortMechs();
        unitLoadingDialog.setVisible(false);
        final Map<String, String> hFailedFiles = MechSummaryCache.getInstance().getFailedFiles();
        if ((hFailedFiles != null) && (hFailedFiles.size() > 0)) {
            new UnitFailureDialog(clientgui, hFailedFiles);
        }
        try {
            mechList.setSelectedIndex(0);
        } catch (Exception e) {
            mechList.setSelectedIndex(-1);
        }
        pPreview.setVisible(true);
        setVisible(true);
        mechList.requestFocus();
    }

    private void filterMechs() {
        Vector<MechSummary> vMechs = new Vector<MechSummary>(1, 1);
        MechSummary[] mechs = MechSummaryCache.getInstance().getAllMechs();
        if (mechs == null) {
            System.err.println("No units to filter!");
            return;
        }
        int x = 0;
        try {
            for (; x < mechs.length; x++) {
                String model = UnitUtils.getMechSummaryFileName(mechs[x]);
                if ((chassieList.get(model) != null) && !vMechs.contains(mechs[x].getModel())) {
                    vMechs.addElement(mechs[x]);
                }
            }
        } catch (Exception ex) {
            CampaignData.mwlog.errLog(ex);
            System.err.println("mechs size: " + mechs.length + " x: " + x);
        }
        mechsCurrent = new MechSummary[vMechs.size()];
        vMechs.copyInto(mechsCurrent);
        sortMechs();
    }

    private void sortMechs() {
        Arrays.sort(mechsCurrent, new MechSummaryComparator(0));
        defaultModel.clear();
        for (int x = 0; x < mechsCurrent.length; x++) {
            defaultModel.add(x, formatMech(mechsCurrent[x]));
        }
        repaint();
    }

    private void searchFor(String search) {
        for (int i = 0; i < mechsCurrent.length; i++) {
            if (mechsCurrent[i].getName().toLowerCase().startsWith(search)) {
                mechList.setSelectedIndex(i);
                mechList.ensureIndexIsVisible(i);
                break;
            }
        }
    }

    @Override
    public void setVisible(boolean show) {
        setLocationRelativeTo(null);
        super.setVisible(show);
        pack();
    }

    private String formatMech(MechSummary ms) {
        String result = makeLength(ms.getModel(), 12) + " " + makeLength(ms.getChassis(), 10) + " " + makeLength("" + ms.getTons(), 3) + " " + makeLength("" + ms.getBV(), 5);
        String chassieMods = chassieList.get(UnitUtils.getMechSummaryFileName(ms));
        StringTokenizer mods = new StringTokenizer(chassieMods, "$");
        result += " " + makeLength(mods.nextToken() + mwclient.moneyOrFluMessage(true, true, -1), 5);
        result += " " + makeLength(mods.nextToken() + "cp", 7);
        result += " " + makeLength(mods.nextToken() + mwclient.moneyOrFluMessage(false, true, -1), 5);
        return result;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bCancel) {
            dispose();
        }
        if (ae.getSource() == bRepod) {
            try {
                MechSummary ms = mechsCurrent[mechList.getSelectedIndex()];
                String unitFile = UnitUtils.getMechSummaryFileName(ms);
                if (global) {
                    mwclient.sendChat(MWClient.CAMPAIGN_PREFIX + "c repod#" + unitId + "#GLOBAL#" + unitFile);
                } else {
                    mwclient.sendChat(MWClient.CAMPAIGN_PREFIX + "c repod#" + unitId + "#" + unitFile);
                }
                Thread.sleep(125);
                dispose();
            } catch (Exception ex) {
                CampaignData.mwlog.errLog(ex);
            }
        }
        if (ae.getSource() == bRandom) {
            try {
                if (global) {
                    mwclient.sendChat(MWClient.CAMPAIGN_PREFIX + "c repod#" + unitId + "#GLOBAL#RANDOM");
                } else {
                    mwclient.sendChat(MWClient.CAMPAIGN_PREFIX + "c repod#" + unitId + "#RANDOM");
                }
                Thread.sleep(125);
                dispose();
            } catch (Exception ex) {
                CampaignData.mwlog.errLog(ex);
            }
        }
    }

    /**
     * for compliance with ListSelectionListener
     */
    public void valueChanged(ListSelectionEvent event) {
        int selected = mechList.getSelectedIndex();
        if (selected == -1) {
            clearMechPreview();
            return;
        }
        MechSummary ms = mechsCurrent[selected];
        try {
            Entity entity = new MechFileParser(ms.getSourceFile(), ms.getEntryName()).getEntity();
            previewMech(entity);
        } catch (EntityLoadingException ex) {
            System.out.println("Unable to load mech: " + ms.getSourceFile() + ": " + ms.getEntryName() + ": " + ex.getMessage());
            CampaignData.mwlog.errLog(ex);
            clearMechPreview();
            return;
        }
    }

    public void itemStateChanged(ItemEvent ie) {
        Object currSelection = mechList.getSelectedValue();
        sortMechs();
        filterMechs();
        mechList.setSelectedValue(currSelection, true);
    }

    void clearMechPreview() {
        mechViewLeft.setEditable(false);
        mechViewRight.setEditable(false);
        mechViewLeft.setText("");
        mechViewRight.setText("");
        previewMech(null);
    }

    void previewMech(Entity entity) {
        Entity currEntity = entity;
        boolean populateTextFields = true;
        if (entity == null) {
            try {
                currEntity = UnitUtils.createOMG();
                populateTextFields = false;
            } catch (Exception e) {
            }
        }
        MechView mechView = null;
        try {
            mechView = new MechView(currEntity, true);
        } catch (Exception e) {
            populateTextFields = false;
        }
        mechViewLeft.setEditable(false);
        mechViewRight.setEditable(false);
        if (populateTextFields && (mechView != null)) {
            mechViewLeft.setText(mechView.getMechReadoutBasic());
            mechViewRight.setText(mechView.getMechReadoutLoadout());
        } else {
            mechViewLeft.setText("No unit selected");
            mechViewRight.setText("No unit selected");
        }
        mechViewLeft.setCaretPosition(0);
        mechViewRight.setCaretPosition(0);
        try {
            ((MechInfo) pPreview).setUnit(currEntity);
            ((MechInfo) pPreview).setImageVisible(true);
            pPreview.paint(pPreview.getGraphics());
        } catch (Exception ex) {
        }
    }

    private static final String SPACES = "                        ";

    private String makeLength(String s, int nLength) {
        if (s.length() == nLength) {
            return s;
        } else if (s.length() > nLength) {
            return s.substring(0, nLength - 2) + "..";
        } else {
            return s + SPACES.substring(0, nLength - s.length());
        }
    }

    public void keyReleased(java.awt.event.KeyEvent ke) {
    }

    public void keyPressed(java.awt.event.KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            ActionEvent event = new ActionEvent(bCancel, ActionEvent.ACTION_PERFORMED, "");
            actionPerformed(event);
        }
        long curTime = System.currentTimeMillis();
        if (curTime - m_nLastSearch > KEY_TIMEOUT) {
            m_sbSearch = new StringBuilder();
        }
        m_nLastSearch = curTime;
        m_sbSearch.append(ke.getKeyChar());
        searchFor(m_sbSearch.toString().toLowerCase());
    }

    public void keyTyped(java.awt.event.KeyEvent ke) {
    }

    public void windowActivated(java.awt.event.WindowEvent windowEvent) {
    }

    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
    }

    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        dispose();
    }

    public void windowDeactivated(java.awt.event.WindowEvent windowEvent) {
    }

    public void windowDeiconified(java.awt.event.WindowEvent windowEvent) {
    }

    public void windowIconified(java.awt.event.WindowEvent windowEvent) {
    }

    public void windowOpened(java.awt.event.WindowEvent windowEvent) {
    }
}
