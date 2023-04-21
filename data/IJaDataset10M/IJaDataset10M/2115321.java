package org.gromurph.javascore.gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import org.gromurph.javascore.*;
import org.gromurph.javascore.exception.RatingOutOfBoundsException;
import org.gromurph.javascore.model.AbstractDivision;
import org.gromurph.javascore.model.Division;
import org.gromurph.javascore.model.DivisionList;
import org.gromurph.javascore.model.Entry;
import org.gromurph.javascore.model.EntryList;
import org.gromurph.javascore.model.Regatta;
import org.gromurph.javascore.model.SailId;
import org.gromurph.javascore.model.SubDivision;
import org.gromurph.javascore.model.ratings.Rating;
import org.gromurph.util.*;

/**
 * The Entry class handles information related to a entry in a race, the combination of a boat, its crew, skipper
 **/
public class PanelEntry extends BaseEditor implements ActionListener {

    static ResourceBundle res = JavaScoreProperties.getResources();

    Entry fEntry;

    Regatta fRegatta;

    public PanelEntry() {
        this(null, null);
    }

    public PanelEntry(BaseEditorContainer parent, Entry startObj) {
        super(parent);
        addFields();
        setObject(startObj);
    }

    public void setRegatta(Regatta inR) {
        fRegatta = inR;
        fPanelSubDivisions.setRegatta(inR);
    }

    public void setVisible(boolean v) {
        if (v && !isVisible()) {
            updateFields();
        }
        super.setVisible(v);
    }

    private static Regatta sTestRegatta = null;

    private Regatta getRegatta() {
        if (fRegatta == null) {
            if (sTestRegatta != null) return sTestRegatta; else return null;
        } else {
            return fRegatta;
        }
    }

    private static DivisionList sDivList = null;

    /**
	 * returns the message that should be shown to confirm a deletion The question will start with:
	 * "Are you sure you want to delete this item?" And this footnote will be added to the end of the question
	 * 
	 * Expect subclasses to override this to add additional information
	 */
    public String getConfirmDeleteFootnote() {
        return res.getString("EntryMessageDeleteEntry");
    }

    public Entry getEntry() {
        return fEntry;
    }

    public void setObject(BaseObjectModel inObj) {
        if (fEntry != inObj) {
            fTableCrew.saveChanges();
            fTableCrew.editingStopped(null);
            if (fEntry != null) fEntry.removePropertyChangeListener(this);
            fEntry = (Entry) inObj;
            if (isVisible() && fEntry != null) fEntry.addPropertyChangeListener(this);
            DivisionList dlist = null;
            if (getRegatta() == null) {
                dlist = DivisionList.getMasterList();
            } else {
                dlist = getRegatta().getDivisions();
            }
            if (sDivList == null || !sDivList.equals(dlist)) {
                sDivList = dlist;
                Vector<Division> combo = new Vector<Division>();
                combo.add(AbstractDivision.NONE);
                combo.addAll(dlist);
                fComboDivision.setModel(new DefaultComboBoxModel(combo));
            }
            fPanelSubDivisions.setEntry(fEntry);
            super.setObject(fEntry);
            if (fEntry.getDivision().isOneDesign()) {
                fTextSail.requestFocus();
            } else {
                ((FocusListener) fPanelRating).focusGained(null);
            }
        }
    }

    public void restore(BaseObjectModel a, BaseObjectModel b) {
        if (a == b) return;
        Entry active = (Entry) a;
        Entry backup = (Entry) b;
        active.getBoat().setName(backup.getBoat().getName());
        active.setSailId(backup.getBoat().getSailId());
        try {
            active.setDivision(backup.getDivision());
        } catch (Exception ignore) {
        }
        active.getSkipper().setFirst(backup.getSkipper().getFirst());
        active.getSkipper().setLast(backup.getSkipper().getLast());
        active.getSkipper().setSailorId(backup.getSkipper().getSailorId());
        active.getCrew().setFirst(backup.getCrew().getFirst());
        active.getCrew().setLast(backup.getCrew().getLast());
        active.getCrew().setSailorId(backup.getCrew().getSailorId());
        active.setBow(backup.getBow());
        active.setMnaNumber(backup.getMnaNumber());
        active.setRsaNumber(backup.getRsaNumber());
        active.setClub(backup.getClub());
        super.restore(active, backup);
    }

    public void enableFields(boolean b) {
        fTextBow.setEnabled(b);
        fTextSail.setEnabled(b);
        fTextName.setEnabled(b);
        fTableCrew.setEnabled(b);
        fTextRsa.setEnabled(b);
        fTextMna.setEnabled(b);
        fTextClub.setEnabled(b);
        fPanelSubDivisions.setEnabled(b);
    }

    public void updateFields() {
        boolean showbow = false;
        boolean showSubs = false;
        if (getRegatta() != null) {
            showbow = getRegatta().isUseBowNumbers();
            showSubs = (getRegatta().getNumSubDivisions() > 0);
        }
        if (showSubs) fPanelSubDivisions.updateCheckBoxes();
        fPanelSubDivisions.setVisible(showSubs);
        fLabelSubDivisions.setVisible(showSubs);
        fTextBow.setVisible(showbow);
        fLabelBow.setVisible(showbow);
        if (fEntry != null) {
            fTextBow.setText(fEntry.getBow().toString());
            fTextName.setText(fEntry.getBoat().getName());
            fTextSail.setText(fEntry.getBoat().getSailId().toString());
            fTextMna.setText(fEntry.getMnaNumber());
            fTextRsa.setText(fEntry.getRsaNumber());
            fTextClub.setText(fEntry.getClub());
            fComboDivision.setSelectedItem(fEntry.getDivision());
            fPanelSubDivisions.setVisible(showSubs);
            enableFields(true);
            fTableCrew.saveChanges();
        } else {
            fTextName.setText(EMPTY);
            fTextBow.setText(EMPTY);
            fTextSail.setText(EMPTY);
            fTextMna.setText(EMPTY);
            fTextRsa.setText(EMPTY);
            fTextClub.setText(EMPTY);
            fComboDivision.setSelectedItem(null);
            fPanelSubDivisions.setVisible(false);
            enableFields(false);
        }
        fLabelMisc1.setText(" " + JavaScoreProperties.getInstance().getProperty(JavaScoreProperties.RSA_PROPERTY) + " ");
        fLabelMisc2.setText(" " + JavaScoreProperties.getInstance().getProperty(JavaScoreProperties.MNA_PROPERTY) + " ");
        updateRatingPanel();
    }

    public void vetoableChange(PropertyChangeEvent de) throws PropertyVetoException {
        de.getSource();
        updateFields();
    }

    public void propertyChange(PropertyChangeEvent ev) {
        try {
            vetoableChange(ev);
        } catch (Exception e) {
        }
    }

    JComboBox fComboDivision;

    JLabel fLabelBow;

    JLabel fLabelRating;

    PanelEntrySubDivisions fPanelSubDivisions;

    JPanel fPanelRatingHolder;

    BaseEditor fPanelRating;

    JTextFieldSelectAll fTextSail;

    JTextFieldSelectAll fTextBow;

    JTextFieldSelectAll fTextName;

    JTableCrew fTableCrew;

    private class CrewList extends AbstractTableModel {

        public int getRowCount() {
            if (fEntry == null) return 0;
            return 2 + fEntry.getNumCrew();
        }

        public int getColumnCount() {
            return 4;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (fEntry == null) return "<null>";
            if (rowIndex <= fEntry.getNumCrew()) {
                Person crew = getPerson(rowIndex);
                switch(columnIndex) {
                    case 0:
                        if (rowIndex == 0) return "Skipper"; else return "Crew " + Integer.toString(rowIndex);
                    case 1:
                        return crew.getLast();
                    case 2:
                        return crew.getFirst();
                    case 3:
                        return crew.getSailorId();
                    default:
                        return "<ciob>";
                }
            } else if (columnIndex == 0) {
                return "*";
            } else {
                return "";
            }
        }

        private Person getPerson(int rowIndex) {
            if (fEntry == null) return null;
            if (rowIndex == 0) {
                return fEntry.getSkipper();
            } else if (rowIndex > 0) {
                return fEntry.getCrew(rowIndex - 1);
            } else {
                return null;
            }
        }

        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            if (fEntry == null) return;
            Person crew = getPerson(rowIndex);
            switch(columnIndex) {
                case 1:
                    crew.setLast((String) value);
                    break;
                case 2:
                    crew.setFirst((String) value);
                    break;
                case 3:
                    crew.setSailorId((String) value);
                    break;
                default:
                    break;
            }
        }

        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        public String getColumnName(int column) {
            switch(column) {
                case 0:
                    return "";
                case 1:
                    return res.getString("EntryLabelLastName");
                case 2:
                    return res.getString("EntryLabelFirstName");
                case 3:
                    return res.getString("EntryLabelSailorId");
                default:
                    return "<ciob>";
            }
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return (columnIndex > 0);
        }
    }

    private class SelectionModelSkipCol0 extends DefaultListSelectionModel {

        public SelectionModelSkipCol0() {
            super();
        }

        int not0(int col) {
            return (col == 0) ? 1 : col;
        }

        public void setAnchorSelectionIndex(int anchorIndex) {
            super.setAnchorSelectionIndex(not0(anchorIndex));
        }

        public void setLeadSelectionIndex(int anchorIndex) {
            super.setLeadSelectionIndex(not0(anchorIndex));
        }

        public void setSelectionInterval(int i0, int i1) {
            super.setSelectionInterval(not0(i0), not0(i1));
        }
    }

    private static final int COLWIDTH_NUM = 45;

    private static final int COLWIDTH_LAST = 90;

    private static final int COLWIDTH_FIRST = 80;

    private static final int COLWIDTH_ISAFID = 60;

    private static final int TABLE_WIDTH = 300;

    private static final int TABLE_HEIGHT = 70;

    public class JTableCrew extends JTable {

        public JTableCrew(ArrayList crewlist) {
            super();
            setModel(new CrewList());
            setColumnSelectionAllowed(false);
            setRowSelectionAllowed(true);
            getColumnModel().setSelectionModel(new SelectionModelSkipCol0());
            DefaultTableCellRenderer rend = new DefaultTableCellRenderer();
            rend.setBackground(Color.lightGray);
            rend.setMaximumSize(new Dimension(50, rend.getPreferredSize().height));
            getColumnModel().getColumn(0).setCellRenderer(rend);
            rend = new DefaultTableCellRenderer();
            rend.setBackground(new Color(240, 240, 240));
            getColumnModel().getColumn(1).setCellRenderer(rend);
            getColumnModel().getColumn(2).setCellRenderer(rend);
            getColumnModel().getColumn(3).setCellRenderer(rend);
            setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            getColumnModel().getColumn(0).setPreferredWidth(COLWIDTH_NUM);
            getColumnModel().getColumn(1).setPreferredWidth(COLWIDTH_LAST);
            getColumnModel().getColumn(2).setPreferredWidth(COLWIDTH_FIRST);
            getColumnModel().getColumn(3).setPreferredWidth(COLWIDTH_ISAFID);
        }

        /**
		 * catches lingering changes if user leaves without tabbing out of last cell
		 */
        public void saveChanges() {
            int row = getEditingRow();
            int col = getEditingColumn();
            if (row >= 0 || col >= 0) {
                try {
                    JTextField c = (JTextField) getEditorComponent();
                    String lastText = c.getText();
                    setValueAt(lastText, row, col);
                } catch (ClassCastException e) {
                    editingStopped(null);
                }
            }
        }
    }

    JTextFieldSelectAll fTextRsa;

    JTextFieldSelectAll fTextMna;

    JTextFieldSelectAll fTextClub;

    JLabel fLabelSubDivisions;

    JLabel fLabelMisc1;

    JLabel fLabelMisc2;

    public void addFields() {
        HelpManager.getInstance().registerHelpTopic(this, "entries");
        setLayout(new GridBagLayout());
        setGridBagInsets(new java.awt.Insets(2, 2, 2, 2));
        int row = 0;
        gridbagAdd(new JLabel(res.getString("GenDivision")), 0, row, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        JPanel divPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        gridbagAdd(divPanel, 1, row, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
        fComboDivision = new JComboBox();
        fComboDivision.setName("fComboDivision");
        fComboDivision.setToolTipText(res.getString("EntryClassToolTip"));
        HelpManager.getInstance().registerHelpTopic(fComboDivision, "entries.fComboDivision");
        divPanel.add(fComboDivision);
        fPanelRatingHolder = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        divPanel.add(fPanelRatingHolder);
        fLabelRating = new JLabel(BLANK + res.getString("EntryLabelRating") + BLANK);
        fPanelRatingHolder.add(fLabelRating);
        fLabelRating.setVisible(false);
        row++;
        gridbagAdd(new JLabel(res.getString("GenSailNum")), 0, row, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        JPanel sailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        gridbagAdd(sailPanel, 1, row, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
        fTextSail = new JTextFieldSelectAll(8);
        fTextSail.setName("fTextSail");
        fTextSail.setToolTipText(res.getString("GenSailToolTip"));
        HelpManager.getInstance().registerHelpTopic(fTextSail, "entries.fTextSail");
        sailPanel.add(fTextSail);
        fLabelBow = new JLabel(res.getString("GenBowNum"));
        fTextSail.setName("fTextSail");
        sailPanel.add(fLabelBow);
        fTextBow = new JTextFieldSelectAll(4);
        fTextBow.setName("fTextBow");
        fTextBow.setToolTipText(res.getString("GenBowToolTip"));
        HelpManager.getInstance().registerHelpTopic(fTextBow, "entries.fTextBow");
        sailPanel.add(fTextBow);
        row++;
        gridbagAdd(new JLabel(res.getString("GenBoatName")), 0, row, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        fTextName = new JTextFieldSelectAll(21);
        fTextName.setName("fTextName");
        fTextName.setToolTipText(res.getString("GenBoatNameToolTip"));
        HelpManager.getInstance().registerHelpTopic(fTextName, "entries.fTextName");
        gridbagAdd(fTextName, 1, row, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
        row++;
        gridbagAdd(new JLabel(res.getString("EntryLabelCrew")), 0, row, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        fTableCrew = new JTableCrew(null);
        fTableCrew.setName("fTableCrew");
        fTableCrew.setToolTipText(res.getString("TableCrewToolTip"));
        JScrollPane scrollCrew = new JScrollPane(fTableCrew);
        scrollCrew.setMinimumSize(new Dimension(TABLE_WIDTH + 20, TABLE_HEIGHT + 20));
        scrollCrew.setPreferredSize(new Dimension(TABLE_WIDTH + 20, TABLE_HEIGHT + 70));
        gridbagAdd(scrollCrew, 1, row, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH);
        HelpManager.getInstance().registerHelpTopic(fTableCrew, "entries.fTableCrew");
        row++;
        String rsa = JavaScoreProperties.getInstance().getProperty(JavaScoreProperties.RSA_PROPERTY);
        fLabelMisc1 = new JLabel(rsa);
        gridbagAdd(fLabelMisc1, 0, row, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        JPanel panelNums = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        gridbagAdd(panelNums, 1, row, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
        fTextRsa = new JTextFieldSelectAll(8);
        panelNums.add(fTextRsa);
        fTextRsa.setToolTipText(res.getString("PrefsLabelMisc1ToolTip"));
        fTextRsa.setName("fTextRsa");
        HelpManager.getInstance().registerHelpTopic(fTextRsa, "entries.fTextRsa");
        rsa = " " + JavaScoreProperties.getInstance().getProperty(JavaScoreProperties.MNA_PROPERTY) + " ";
        fLabelMisc2 = new JLabel(rsa);
        panelNums.add(fLabelMisc2);
        fTextMna = new JTextFieldSelectAll(8);
        fTextMna.setName("fTextMna");
        panelNums.add(fTextMna);
        fTextMna.setToolTipText(res.getString("PrefsLabelMisc2ToolTip"));
        HelpManager.getInstance().registerHelpTopic(fTextMna, "entries.fTextMna");
        row++;
        gridbagAdd(new JLabel(res.getString("GenClub")), 0, row, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        fTextClub = new JTextFieldSelectAll(21);
        fTextClub.setName("fTextClub");
        fTextClub.setToolTipText(res.getString("GenClubToolTip"));
        HelpManager.getInstance().registerHelpTopic(fTextClub, "entries.fTextClub");
        gridbagAdd(fTextClub, 1, row, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
        row++;
        fLabelSubDivisions = new JLabel(res.getString("EntryLabelSubDivisionPanel"));
        gridbagAdd(fLabelSubDivisions, 0, row, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
        fPanelSubDivisions = new PanelEntrySubDivisions();
        gridbagAdd(this, fPanelSubDivisions, 1, row, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
    }

    private boolean started = false;

    public void start() {
        if (!started) {
            System.out.println("starting ");
            initializeListeners();
        }
        started = true;
    }

    public void initializeListeners() {
        fComboDivision.addActionListener(this);
        fTextSail.addActionListener(this);
        fTextBow.addActionListener(this);
        fTextName.addActionListener(this);
        fTextMna.addActionListener(this);
        fTextRsa.addActionListener(this);
        fTextClub.addActionListener(this);
        if (fEntry != null) fEntry.addPropertyChangeListener(this);
        fPanelSubDivisions.start();
        fTextName.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                System.out.println("fTextName.propertyChange old=" + ((e.getOldValue() == null) ? "null" : e.getOldValue().toString()) + ", new=" + ((e.getNewValue() == null) ? "null" : e.getNewValue().toString()));
            }
        });
    }

    public void stop() {
        if (started) {
            started = false;
            System.out.println("    stopping ");
            fComboDivision.removeActionListener(this);
            fTextSail.removeActionListener(this);
            fTextBow.removeActionListener(this);
            fTextName.removeActionListener(this);
            fTextMna.removeActionListener(this);
            fTextRsa.removeActionListener(this);
            fTextClub.removeActionListener(this);
            if (fEntry != null) fEntry.removePropertyChangeListener(this);
            fPanelSubDivisions.stop();
        }
    }

    public void exitOK() {
        super.exitOK();
        fTableCrew.saveChanges();
        JavaScore.backgroundSave();
        JavaScore.subWindowClosing();
    }

    public void actionPerformed(ActionEvent event) {
        lastField = "";
        Object object = event.getSource();
        if (object == fTextSail) fTextSail_actionPerformed(); else if (object == fTextBow) fTextBow_actionPerformed(); else if (object == fTextName) fTextName_actionPerformed(); else if (object == fTextMna) fTextMna_actionPerformed(); else if (object == fTextRsa) fTextRsa_actionPerformed(); else if (object == fTextClub) fTextClub_actionPerformed(); else if (object == fComboDivision) fComboDivision_actionPerformed();
        if (getEditorParent() != null) getEditorParent().eventOccurred(this, event);
    }

    void updateRatingPanel() {
        if (fEntry == null) {
            fPanelRatingHolder.setVisible(false);
            return;
        }
        Division div = fEntry.getDivision();
        if (div.isOneDesign()) {
            if (fPanelRating != null) {
                fPanelRating.setVisible(false);
                fLabelRating.setVisible(false);
            }
        } else {
            Rating newRtg = fEntry.getRating();
            Class newEditorClass = newRtg.getEditor();
            if ((fPanelRating == null) || !fPanelRating.getClass().equals(newEditorClass)) {
                if (fPanelRating != null) {
                    fPanelRating.stop();
                    fPanelRatingHolder.remove(fPanelRating);
                }
                try {
                    fPanelRating = (BaseEditor) newEditorClass.newInstance();
                    fPanelRatingHolder.add(fPanelRating);
                    fPanelRating.start();
                    fPanelRatingHolder.revalidate();
                    fPanelRatingHolder.setVisible(true);
                } catch (IllegalAccessException e) {
                    Util.showError(e, true);
                } catch (InstantiationException e) {
                    Util.showError(e, true);
                }
            }
            fPanelRating.setObject(newRtg);
            try {
                ((PanelRatingDouble) fPanelRating).setDivision(div);
            } catch (Exception e) {
            }
        }
    }

    void fComboDivision_actionPerformed() {
        Division div = (Division) fComboDivision.getSelectedItem();
        if (div != null && fEntry != null && !div.equals(fEntry.getDivision())) {
            try {
                fEntry.setDivision(div);
            } catch (RatingOutOfBoundsException ex) {
                String message = MessageFormat.format(res.getString("EntryMessageRatingOutOfBounds"), new Object[] { fEntry.getRating().toString(), div.toString() });
                int changeRating = JOptionPane.showConfirmDialog(this, message, res.getString("EntryMessageRatingOutOfBoundsTitle"), JOptionPane.YES_NO_OPTION);
                if (changeRating == JOptionPane.YES_OPTION) {
                    try {
                        fEntry.setRating((Rating) div.getMinRating().clone());
                        fEntry.setDivision(div);
                    } catch (RatingOutOfBoundsException canthappen) {
                    }
                } else {
                    return;
                }
            }
            updateRatingPanel();
            if (!div.isOneDesign()) {
                ((FocusListener) fPanelRating).focusGained(null);
            }
        }
    }

    String lastField = "";

    void fTextName_actionPerformed() {
        lastField = Entry.BOATNAME_PROPERTY;
        fEntry.setBoatName(fTextName.getText());
    }

    boolean editingSail = false;

    void fTextSail_actionPerformed() {
        lastField = Entry.SAILID_PROPERTY;
        String sail = fTextSail.getText();
        if (sail.equals(fEntry.getBoat().getSailId().toString())) return;
        if (!editingSail) {
            editingSail = true;
            if (getRegatta() != null) showDuplicateMessage(sail);
        }
        fEntry.setSailId(new SailId(fTextSail.getText()));
        editingSail = false;
    }

    private void showDuplicateMessage(String num) {
        EntryList eList = getRegatta().getAllEntries().findId(num);
        if (eList != null) eList.remove(fEntry);
        if (eList != null && eList.size() > 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(MessageFormat.format(res.getString("EntryMessageNumberInUse"), new Object[] { num }));
            sb.append(NEWLINE);
            for (int i = 0; i < eList.size(); i++) {
                sb.append("  ");
                sb.append(eList.get(i).toString());
                sb.append(NEWLINE);
            }
            JOptionPane.showMessageDialog(this, sb.toString(), res.getString("EntryTitleInUse"), JOptionPane.WARNING_MESSAGE);
        }
    }

    boolean editingBow = false;

    void fTextBow_actionPerformed() {
        String bow = fTextBow.getText();
        if (bow.equals(fEntry.getBow())) return;
        if (!editingBow) {
            editingBow = true;
            if (getRegatta().isUseBowNumbers()) {
                showDuplicateMessage(bow);
            }
        }
        fEntry.setBow(fTextBow.getText());
        editingBow = false;
    }

    void fTextMna_actionPerformed() {
        fEntry.setMnaNumber(fTextMna.getText());
    }

    void fTextRsa_actionPerformed() {
        fEntry.setRsaNumber(fTextRsa.getText());
    }

    void fTextClub_actionPerformed() {
        fEntry.setClub(fTextClub.getText());
    }

    public static void main(String[] args) {
        Division div = DivisionList.getMasterList().get(1);
        Division div2 = DivisionList.getMasterList().get(2);
        Entry e = new Entry();
        try {
            e.setDivision(div);
        } catch (RatingOutOfBoundsException ex) {
        }
        e.getBoat().setName("blue baby");
        e.setSailId(new SailId("1234"));
        SubDivision s2 = new SubDivision("Blue", div);
        s2.setMonopoly(true);
        s2.setScoreSeparately(false);
        SubDivision s3 = new SubDivision("Green", div2);
        s3.setMonopoly(false);
        s3.setScoreSeparately(true);
        SubDivision s1 = new SubDivision("White", DivisionList.getMasterList().get(3));
        s1.setMonopoly(false);
        s1.setScoreSeparately(false);
        sTestRegatta = new Regatta();
        sTestRegatta.addDivision(div);
        sTestRegatta.addDivision(div2);
        sTestRegatta.addEntry(e);
        sTestRegatta.addSubDivision(s1);
        sTestRegatta.addSubDivision(s2);
        sTestRegatta.addSubDivision(s3);
        Entry b = new Entry();
        b.getBoat().setName("S&M");
        b.setSkipper(new Person("Sandy", "Grosvenor"));
        b.addCrew(new Person("Barbara", "Vosbury"));
        b.addCrew(new Person("Idarae", "Prothero"));
        b.setSailId(new SailId("USA 1044"));
        PanelEntry pb = new PanelEntry();
        pb.setRegatta(sTestRegatta);
        pb.setObject(b);
        pb.start();
        JOptionPane.showConfirmDialog(null, pb);
        System.exit(0);
    }
}
