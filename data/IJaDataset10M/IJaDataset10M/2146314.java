package javasean;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.datatransfer.*;
import javax.swing.plaf.BorderUIResource;
import javasean.DataModel.*;

/**
* Provides JAVASEAN subwindow for user to select the makeup of the alignment.
* Appears in the upper left corner of the main javaSean window.
* Presents choices to select a Reference, Candidates(s), alignment type (Feat Spec. Align),
* and inputs to the alignment algorithm.
*/
public class AccessionPanel extends JPanel implements ActionListener {

    private EventListenerList m_EventListenerList = new EventListenerList();

    private ActionEvent theEvent;

    private Font m_TextFont = new Font("Dialog", Font.BOLD, 12);

    private Font m_MonospacedListFont = new Font("monospaced", Font.BOLD, 14);

    private Font m_InfoFont = new Font("Dialog", Font.PLAIN, 11);

    private String m_GeneName = null;

    private JTextField m_geneNameTextField;

    private JButton m_geneNameButton;

    private JButton m_infoButton;

    private JRadioButton m_featSpecAlignRadioButton;

    private JTextField m_distLimitTextField;

    private JTextField m_exonGapTextField;

    private Vector m_accessionList, m_harvestedList;

    private JRadioButton[] m_ReferenceRadioButton;

    private JCheckBox[] m_SelectedCheckBox;

    private JTextField[] m_accStartPos;

    private JTextField[] m_accEndPos;

    private String m_addAccIDStr = null;

    private JTextField m_addAccID;

    private JButton m_addAccButton;

    private JButton m_alignButton;

    private JButton m_upgradeButton;

    private PropertyFileRW m_selPFRW;

    private String m_ACCESSION_HISTORY_PROP = null;

    private KeyStroke copyKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_COPY, 0);

    private KeyStroke cutKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_CUT, 0);

    private KeyStroke pasteKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_PASTE, 0);

    /**
* Constructor.
*/
    public AccessionPanel(String the_GeneName, Vector the_accessionList, String the_ACCESSION_HISTORY_PROP) {
        m_selPFRW = new PropertyFileRW(the_ACCESSION_HISTORY_PROP);
        reset(the_GeneName, the_accessionList);
    }

    /**
* Used to reset the list of accessions presented to the user for choices.
* Invoked by actions in JAVASEAN.
*/
    public void reset(String the_GeneName, Vector the_accessionList) {
        m_GeneName = the_GeneName;
        m_accessionList = the_accessionList;
        sortDMAccessions();
        removeAll();
        init();
    }

    /**
* Builds/Rebuilds window when DMAccessions are added or removed.
*/
    public void init() {
        setLayout(new GridBagLayout());
        JPanel qPanel = new JPanel();
        qPanel.setLayout(new GridBagLayout());
        JLabel geneLabel = new JLabel("Gene Name");
        geneLabel.setForeground(Color.black);
        geneLabel.setFont(m_TextFont);
        GridBagAdder.add(qPanel, geneLabel, 0, 0, 2, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
        m_geneNameTextField = new JTextField(m_GeneName, 8);
        m_geneNameTextField.setForeground(Color.blue.darker());
        m_geneNameTextField.setFont(m_TextFont);
        GridBagAdder.add(qPanel, m_geneNameTextField, 3, 0, 1, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
        m_geneNameButton = new JButton("New");
        m_geneNameButton.addActionListener(this);
        m_geneNameButton.setFont(m_TextFont);
        GridBagAdder.add(qPanel, m_geneNameButton, 4, 0, 1, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JLabel fsaLabel = new JLabel("Feature Specific Align");
        fsaLabel.setForeground(Color.black);
        fsaLabel.setFont(m_TextFont);
        GridBagAdder.add(qPanel, fsaLabel, 0, 1, 2, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
        m_featSpecAlignRadioButton = new JRadioButton();
        GridBagAdder.add(qPanel, m_featSpecAlignRadioButton, 3, 1, 1, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JLabel dlasLabel = new JLabel("Sequence Distance Limit  ");
        dlasLabel.setForeground(Color.black);
        dlasLabel.setFont(m_TextFont);
        GridBagAdder.add(qPanel, dlasLabel, 0, 2, 2, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
        m_distLimitTextField = new JTextField(5);
        m_distLimitTextField.setText("1000");
        m_distLimitTextField.setFont(m_TextFont);
        m_distLimitTextField.setHorizontalAlignment(JTextField.RIGHT);
        GridBagAdder.add(qPanel, m_distLimitTextField, 3, 2, 1, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JLabel egLabel = new JLabel("Maximum Exon Gap");
        egLabel.setForeground(Color.black);
        egLabel.setFont(m_TextFont);
        GridBagAdder.add(qPanel, egLabel, 0, 3, 2, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
        m_exonGapTextField = new JTextField(5);
        m_exonGapTextField.setText("30");
        m_exonGapTextField.setFont(m_TextFont);
        m_exonGapTextField.setHorizontalAlignment(JTextField.RIGHT);
        GridBagAdder.add(qPanel, m_exonGapTextField, 3, 3, 1, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
        m_infoButton = new JButton("[info]");
        m_infoButton.addActionListener(this);
        m_infoButton.setFont(m_InfoFont);
        m_infoButton.setForeground(Color.blue);
        m_infoButton.setBorder(new BorderUIResource.EmptyBorderUIResource(m_infoButton.getInsets()));
        GridBagAdder.add(qPanel, m_infoButton, 4, 3, 1, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
        GridBagAdder.add(this, qPanel, 0, 0, 5, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        Vector toBeAdded = new Vector();
        JScrollPane accListPanel = new JScrollPane();
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new GridLayout(0, 1));
        if (m_accessionList != null) {
            int listSize = m_accessionList.size();
            m_ReferenceRadioButton = new JRadioButton[listSize];
            m_SelectedCheckBox = new JCheckBox[listSize];
            m_accStartPos = new JTextField[listSize];
            m_accEndPos = new JTextField[listSize];
            ButtonGroup bg = new ButtonGroup();
            for (int i = 0; i < m_accessionList.size(); i++) {
                DMAccession ac = (DMAccession) (m_accessionList.get(i));
                JPanel acPanel = new JPanel();
                acPanel.setLayout(new FlowLayout());
                m_ReferenceRadioButton[i] = new JRadioButton();
                m_ReferenceRadioButton[i].addActionListener(this);
                bg.add(m_ReferenceRadioButton[i]);
                acPanel.add(m_ReferenceRadioButton[i]);
                m_SelectedCheckBox[i] = new JCheckBox();
                acPanel.add(m_SelectedCheckBox[i]);
                String idText = ac.getListedAs();
                if (idText == null) {
                    idText = ac.getID();
                }
                int l = 10 - idText.length();
                for (int sp = 0; sp < l; sp++) {
                    idText += " ";
                }
                int tl = 14 - ac.getMolecType().length() - idText.length();
                for (int sp = 0; sp < tl; sp++) {
                    idText += " ";
                }
                idText += ac.getMolecType();
                JLabel idLabel = new JLabel(idText);
                idLabel.setFont(m_MonospacedListFont);
                if (ac.getMolecType().equals("DNA")) {
                    idLabel.setForeground(Color.blue.darker());
                } else if ((ac.getMolecType().equals("mRNA")) || (ac.getMolecType().equals("RNA"))) {
                    idLabel.setForeground(Color.green.darker());
                }
                acPanel.add(idLabel);
                m_accStartPos[i] = new JTextField(7);
                m_accStartPos[i].setFont(m_MonospacedListFont);
                m_accStartPos[i].setHorizontalAlignment(JTextField.RIGHT);
                m_accStartPos[i].setText(new Integer(ac.getUnitDMFeatSeg().getSegStart()).toString());
                acPanel.add(m_accStartPos[i]);
                m_accEndPos[i] = new JTextField(7);
                m_accEndPos[i].setFont(m_MonospacedListFont);
                m_accEndPos[i].setHorizontalAlignment(JTextField.RIGHT);
                m_accEndPos[i].setText(new Integer(ac.getUnitDMFeatSeg().getSegEnd()).toString());
                acPanel.add(m_accEndPos[i]);
                listPanel.add(acPanel);
            }
            if (m_accessionList.size() > 0) {
                m_ReferenceRadioButton[0].setSelected(true);
                m_SelectedCheckBox[0].setSelected(false);
                m_SelectedCheckBox[0].setEnabled(false);
                Vector selList = m_selPFRW.getPropList(m_GeneName);
                boolean notUsed = true;
                if (selList.size() > 0) {
                    for (int i = 0; i < selList.size(); i++) {
                        notUsed = true;
                        String selID = (String) (selList.get(i));
                        for (int j = 0; j < m_accessionList.size(); j++) {
                            DMAccession ac = (DMAccession) (m_accessionList.get(j));
                            if (ac.getID().equals(selID)) {
                                if (i == 0) {
                                    m_ReferenceRadioButton[j].setSelected(true);
                                } else {
                                    m_SelectedCheckBox[j].setSelected(true);
                                    m_SelectedCheckBox[j].setEnabled(true);
                                }
                                notUsed = false;
                                break;
                            }
                        }
                        if (notUsed) {
                            toBeAdded.add(selID);
                        }
                    }
                } else {
                    for (int j = 0; j < m_accessionList.size(); j++) {
                        DMAccession ac = (DMAccession) (m_accessionList.get(j));
                        if ((ac.getMolecType().equals("mRNA")) || (ac.getMolecType().equals("RNA"))) {
                            m_SelectedCheckBox[j].setSelected(true);
                            m_SelectedCheckBox[j].setEnabled(true);
                        }
                    }
                }
            }
        }
        accListPanel.setViewportView(listPanel);
        accListPanel.getVerticalScrollBar().setUnitIncrement(31);
        accListPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        accListPanel.setPreferredSize(new Dimension(430, 320));
        GridBagAdder.add(this, accListPanel, 0, 1, 5, 3, 1, 2, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        JPanel addAccPanel = new JPanel();
        addAccPanel.setLayout(new FlowLayout());
        m_addAccID = new JTextField(20);
        CCPEnable(m_addAccID);
        addAccPanel.add(m_addAccID);
        if ((m_accessionList != null) && (m_accessionList.size() > 0) && (toBeAdded.size() > 0)) {
            String AddAccIDList = null;
            for (int i = 0; i < toBeAdded.size(); i++) {
                String selID = (String) (toBeAdded.get(i));
                System.out.println("NO 'SELECTED' MATCH FOR<" + selID + ">");
                if (!selID.equals(m_GeneName)) {
                    if (i == 0) {
                        AddAccIDList = selID;
                    } else {
                        AddAccIDList += "," + selID;
                    }
                }
            }
            if (AddAccIDList != null) {
                m_addAccID.setText(AddAccIDList);
            }
        }
        m_addAccButton = new JButton("Add");
        m_addAccButton.addActionListener(this);
        addAccPanel.add(m_addAccButton);
        m_alignButton = new JButton("Align");
        m_alignButton.addActionListener(this);
        addAccPanel.add(m_alignButton);
        m_upgradeButton = new JButton("Upgr");
        m_upgradeButton.addActionListener(this);
        addAccPanel.add(m_upgradeButton);
        GridBagAdder.add(this, addAccPanel, 0, 4, 5, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER);
    }

    public void updateList(String the_AccName, int the_length) {
        for (int i = 0; i < m_accessionList.size(); i++) {
            DMAccession ac = (DMAccession) (m_accessionList.get(i));
            if (ac.getID().equals(the_AccName)) {
                m_accEndPos[i].setText(new Integer(the_length).toString());
                return;
            }
        }
    }

    /**
* Sorts the accessions in descending order of size, for display in the list.
*/
    private void sortDMAccessions() {
        if (m_accessionList == null) {
            return;
        }
        int listSize = m_accessionList.size();
        for (int i = 0; i < listSize; i++) {
            DMAccession ac = (DMAccession) (m_accessionList.get(i));
            for (int j = (i + 1); j < listSize; j++) {
                DMAccession acnext = (DMAccession) (m_accessionList.get(j));
                if (acnext.getUnitDMFeatSeg().getSegLength() > ac.getUnitDMFeatSeg().getSegLength()) {
                    ac = (DMAccession) m_accessionList.set(i, acnext);
                    m_accessionList.set(j, ac);
                    ac = acnext;
                }
            }
        }
    }

    public String getGeneName() {
        return m_GeneName;
    }

    public void setGeneName(String the_GeneName) {
        m_GeneName = the_GeneName;
    }

    /**
* Returns Vector of all selected References/Candidates selected by user. 
* Reference is in zeroth position, all DMAccessions in 1 through n are Candidates.
*/
    public Vector getHarvestedDMAccessionList() {
        return m_harvestedList;
    }

    /**
* Called by JAVASEAN to provide SOSPanel with a complete list of DMAccessions
* associated with the current gene.
* SOSPanel uses this list to provide the user with a list of 'Additional DMAccessions.'
* This list does not currently require that the DMAccessions be retrieved, so the
* DMAccessions do not contain version or date information.
*/
    public Vector getAllDMAccessionList() {
        return m_accessionList;
    }

    /**
* Called by JAVASEAN to determine if the user selected a DMFeature Specific Align.
*/
    public boolean isFeatSpecAlign() {
        return m_featSpecAlignRadioButton.isSelected();
    }

    /**
* Called by JAVASEAN to determine if the integer value the user chose for the Alignment.
* Only relevant for certain alignment implementations.
*/
    public int getDistanceLimit() {
        try {
            return (new Integer(m_distLimitTextField.getText())).intValue();
        } catch (Exception ex) {
        }
        return 1000;
    }

    /**
* The maximim gap which can appear in an exon before it is considered an intron between
* two separate exons.
*/
    public int getExonGapSize() {
        try {
            return (new Integer(m_exonGapTextField.getText())).intValue();
        } catch (Exception ex) {
        }
        return 30;
    }

    /**
* Called by JAVASEAN to add an Additional DMAccession (one not included in the GeneMap File)
* to the DMAccession List.  If a user wants to add an DMAccession, the DMAccession ID is entered
* in a box on AccessionPanel and the 'ADD' button is pressed.  An actionEvent is sent to
* JAVASEAN which then invokes this method in AccessionPanel. This is handled in this manner
* as the main JAVASEAN panel may need to fill in DMAccession IDs based on previous user selections.
*/
    public void addAccEntry(DMAccession the_DMAccession) {
        boolean isAlreadyThere = false;
        for (int i = 0; i < m_accessionList.size(); i++) {
            DMAccession ac = (DMAccession) (m_accessionList.get(i));
            if (the_DMAccession.getID().equals(ac.getID())) {
                isAlreadyThere = true;
                break;
            }
        }
        if (!isAlreadyThere) {
            m_accessionList.add(the_DMAccession);
            sortDMAccessions();
            removeAll();
            init();
            revalidate();
        }
    }

    /**
* A way for JAVASEAN to remove SEANs when the 'Use Prior SEANs' menu checkbox is deselected.
* Also used to remove prior SEANs when a new one is created.
*/
    public void removeSEANs() {
        for (int i = 0; i < m_accessionList.size(); i++) {
            DMAccession ac = (DMAccession) (m_accessionList.get(i));
            if (ac.isPriorSEAN()) {
                System.out.println("AccessionPanel REMOVING <" + ac.getID() + ">");
                m_accessionList.removeElementAt(i);
                i--;
            }
        }
        sortDMAccessions();
        removeAll();
        init();
        revalidate();
    }

    /**
* Used by JAVASEAN to get the Additional DMAccession ID for processing before calling addAccEntry().
*/
    public String getAddAccID() {
        return m_addAccIDStr;
    }

    /**
* Monitors the AlignmentPanel buttons, packages the list of selected accessions before
* firing an action to JAVASEAN for further processing.
*/
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == m_alignButton) {
            m_harvestedList = harvestUserSelections();
            if (m_harvestedList != null) {
                saveMostRecentList();
                fireActionEvent("AccessionPanel:ALIGN");
            }
        } else if (o == m_upgradeButton) {
            m_harvestedList = harvestUserSelections();
            if (m_harvestedList != null) {
                fireActionEvent("AccessionPanel:UPGRADE");
            }
        } else if (o == m_geneNameButton) {
            m_GeneName = m_geneNameTextField.getText().trim();
            fireActionEvent("AccessionPanel:NEW");
        } else if (o == m_infoButton) {
            JOptionPane jop = new JOptionPane();
            String msg = "Gene Name: Use '<gene_name>.v<version>' to recall a particular version, or '<gene_name>.' for the most recent.\nDistance Limit: Alignment tool dependent value.\nMaximum Exon Gap: Largest gap size before the gap is considered an intron.\nAdd: Include Accessions in comma delimited list to above table.";
            jop.showMessageDialog(this, msg, "Accession Panel Operation", JOptionPane.INFORMATION_MESSAGE);
        } else if (o == m_addAccButton) {
            m_addAccIDStr = m_addAccID.getText();
            fireActionEvent("AccessionPanel:ADDACC");
        } else if (o instanceof JRadioButton) {
            for (int i = 0; i < m_accessionList.size(); i++) {
                if (m_ReferenceRadioButton[i].isSelected()) {
                    m_SelectedCheckBox[i].setEnabled(false);
                } else {
                    m_SelectedCheckBox[i].setEnabled(true);
                }
            }
        } else {
            String keyCommand = e.getActionCommand();
            if (keyCommand.equals("CopyKey")) {
                m_addAccID.copy();
            } else if (keyCommand.equals("CutKey")) {
                m_addAccID.cut();
            } else if (keyCommand.equals("PasteKey")) {
                m_addAccID.paste();
            }
        }
    }

    public void saveMostRecentList() {
        Vector res = new Vector();
        for (int i = 0; i < m_harvestedList.size(); i++) {
            DMAccession ac = (DMAccession) m_harvestedList.get(i);
            if (ac.isCandidate()) {
                res.add(ac.getID());
            } else if (ac.isReference()) {
                res.insertElementAt(ac.getID(), 0);
            }
        }
        if (m_selPFRW != null) {
            m_selPFRW.setPropList(m_GeneName, res);
        }
    }

    private void CCPEnable(JTextComponent the_comp) {
        the_comp.registerKeyboardAction(this, "CopyKey", copyKeyStroke, JComponent.WHEN_FOCUSED);
        the_comp.registerKeyboardAction(this, "CutKey", cutKeyStroke, JComponent.WHEN_FOCUSED);
        the_comp.registerKeyboardAction(this, "PasteKey", pasteKeyStroke, JComponent.WHEN_FOCUSED);
    }

    public void showClipboard() {
        Clipboard c = getToolkit().getSystemClipboard();
        if (c != null) {
            Transferable t = c.getContents(this);
            DataFlavor[] flavors = t.getTransferDataFlavors();
            for (int i = 0; i < flavors.length; i++) {
                try {
                    Object o = t.getTransferData(flavors[i]);
                    System.out.print("\tFlavor " + i + " gives " + o.getClass().getName());
                    if (o instanceof String) {
                        System.out.println("TEXT<" + o + ">");
                    } else {
                        System.out.println("NOT TEXT<" + o + ">");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            System.out.print("\n");
        }
    }

    /**
* Prepares the list of DMAccessions selected by the user for passing up to JAVASEAN.
* Triggered by the ALIGN button.
*/
    public Vector harvestUserSelections() {
        int newStart, newEnd;
        if (m_accessionList == null) {
            return null;
        }
        Vector retList = new Vector();
        for (int i = 0; i < m_accessionList.size(); i++) {
            DMAccession ac = (DMAccession) (m_accessionList.get(i));
            if (m_ReferenceRadioButton[i].isSelected() || m_SelectedCheckBox[i].isSelected()) {
                newStart = new Integer(m_accStartPos[i].getText()).intValue();
                newEnd = new Integer(m_accEndPos[i].getText()).intValue();
                ac.setUnitDMFeatSeg(new DMFeatSeg(newStart, newEnd));
                if (m_ReferenceRadioButton[i].isSelected()) {
                    ac.setReference(true);
                } else {
                    ac.setReference(false);
                }
                if (m_SelectedCheckBox[i].isSelected()) {
                    ac.setCandidate(true);
                } else {
                    ac.setCandidate(false);
                }
                ac.clearDMFeatureList();
                retList.add(ac);
            }
        }
        return retList;
    }

    /**
* So JAVASEAN can be told of events in AccessionPanel.
*/
    public void addActionListener(ActionListener l) {
        m_EventListenerList.add(ActionListener.class, l);
    }

    /**
* So JAVASEAN can be told of events in AccessionPanel.
*/
    public void removeActionListener(ActionListener l) {
        m_EventListenerList.remove(ActionListener.class, l);
    }

    /**
* So JAVASEAN can be told of events in AccessionPanel.
*/
    protected void fireActionEvent(String eventName) {
        Object[] list = m_EventListenerList.getListenerList();
        for (int index = list.length - 2; index >= 0; index -= 2) {
            theEvent = new ActionEvent(this, 0, eventName);
            ((ActionListener) list[index + 1]).actionPerformed(theEvent);
        }
    }

    /**
* For testing AccessionPanel in standalone mode.
*/
    public static void main(String args[]) {
        JFrame frame = new JFrame("Stock Editor");
        Container framecp = frame.getContentPane();
        AccessionPanel ap;
        String rptFile = "/users/ftpuser/sean-complete.fullrpt";
        GeneMapFileReader gmfr = new GeneMapFileReader(rptFile);
        Vector accList;
        String geneName;
        if (args.length > 0) {
            geneName = args[0];
            accList = gmfr.getDMAccessionList(geneName);
        } else {
            geneName = "Ance";
            accList = gmfr.getDMAccessionList("Ance");
        }
        ap = new AccessionPanel(geneName, accList, "./PROP/SELECTED.properties");
        framecp.add(ap);
        frame.pack();
        frame.setSize(new Dimension(600, 400));
        frame.setVisible(true);
        ap.addAccEntry(new DMAccession("AK000001", "DNA", new DMFeatSeg(1, 100)));
    }
}
