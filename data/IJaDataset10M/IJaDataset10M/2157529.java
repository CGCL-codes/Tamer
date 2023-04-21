package hoplugins.seriesstats.ui;

import hoplugins.Commons;
import hoplugins.SeriesStats;
import hoplugins.seriesstats.RatingsPanel;
import plugins.ITabellenVerlaufEintrag;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * DOCUMENT ME!
 *
 * @author Mirtillo To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RatingPanelTab extends JPanel implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3199982683174903143L;

    private JComboBox JCBDate1;

    private JComboBox JCBDate2;

    private JComboBox JCBTeam1;

    private JComboBox JCBTeam2;

    private RatingsPanel RatingRatings;

    /**
     * Creates a new RatingPanelTab object.
     */
    public RatingPanelTab() {
        JPanel jPanel1 = Commons.getModel().getGUI().createImagePanel();
        GridBagLayout jPanel1Layout = new GridBagLayout();
        jPanel1Layout.columnWidths = new int[] { 1, 1, 1, 1, 1, 1 };
        jPanel1Layout.rowHeights = new int[] { 1, 1, 1, 1, 1, 1 };
        jPanel1Layout.columnWeights = new double[] { 0.05, 0.2, 0.01, 0.01, 0.2, 0.05 };
        jPanel1Layout.rowWeights = new double[] { 0.01, 0.01, 0.01, 0.05, 0.001, 0.25 };
        jPanel1.setLayout(jPanel1Layout);
        this.JCBTeam1 = new JComboBox();
        this.JCBTeam1.setMaximumRowCount(8);
        this.JCBTeam1.setPreferredSize(new Dimension(200, 25));
        this.JCBTeam1.addActionListener(this);
        jPanel1.add(this.JCBTeam1, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.JCBTeam2 = new JComboBox();
        this.JCBTeam2.setMaximumRowCount(8);
        this.JCBTeam2.setPreferredSize(new Dimension(200, 25));
        this.JCBTeam2.addActionListener(this);
        jPanel1.add(this.JCBTeam2, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.JCBDate1 = new JComboBox();
        this.JCBDate1.setMaximumRowCount(8);
        this.JCBDate1.setPreferredSize(new Dimension(200, 25));
        this.JCBDate1.addActionListener(this);
        jPanel1.add(this.JCBDate1, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.JCBDate2 = new JComboBox();
        this.JCBDate2.setMaximumRowCount(8);
        this.JCBDate2.setPreferredSize(new Dimension(200, 25));
        this.JCBDate2.addActionListener(this);
        jPanel1.add(this.JCBDate2, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.RatingRatings = new RatingsPanel(Commons.getModel());
        jPanel1.add(this.RatingRatings.getPanel(), new GridBagConstraints(1, 4, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        setLayout(new BorderLayout());
        add(jPanel1, BorderLayout.CENTER);
        RefreshTeamBox();
    }

    /**
     * DOCUMENT ME!
     *
     * @param event
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(JCBTeam1) || event.getSource().equals(JCBTeam2)) {
            updateDateCombos();
            refreshPanelBewertung();
        }
        if ((event.getSource().equals(JCBDate1) || event.getSource().equals(JCBDate2)) && (JCBDate1.getItemCount() > 1) && (JCBDate2.getItemCount() > 1)) {
            refreshPanelBewertung();
        }
    }

    /**
     * TODO Missing Method Documentation
     */
    private void RefreshTeamBox() {
        try {
            this.JCBTeam1.removeActionListener(this);
            this.JCBTeam2.removeActionListener(this);
            this.JCBTeam1.removeAllItems();
            this.JCBTeam2.removeAllItems();
            int ligaId = Commons.getModel().getXtraDaten().getLeagueLevelUnitID();
            int season = Commons.getModel().getBasics().getSeason();
            System.out.println(ligaId + " " + season);
            ITabellenVerlaufEintrag[] dummy = Commons.getModel().getSpielplan(ligaId, season).getVerlauf().getEintraege();
            Vector<String> data = new Vector<String>();
            for (int i = 0; i < dummy.length; i++) {
                data.add(new String(dummy[i].getTeamName()));
            }
            Collections.sort(data);
            for (Enumeration<String> e = data.elements(); e.hasMoreElements(); ) {
                String TeamName = e.nextElement();
                this.JCBTeam1.addItem(TeamName);
                this.JCBTeam2.addItem(TeamName);
            }
            this.JCBTeam1.addActionListener(this);
            this.JCBTeam2.addActionListener(this);
        } catch (Exception e) {
            if (true) {
                SeriesStats.getIDB().append("---ooo---");
                SeriesStats.getIDB().append(e);
            }
        }
    }

    /**
     * TODO Missing Method Documentation
     */
    private void refreshPanelBewertung() {
        System.out.println("Repainting Called");
    }

    /**
     * TODO Missing Method Documentation
     */
    private void updateDateCombos() {
        String d1 = "";
        String d2 = "";
        if ((JCBDate1.getItemCount() > 0) && (JCBDate2.getItemCount() > 0)) {
            d1 = JCBDate1.getSelectedItem().toString();
            d2 = JCBDate2.getSelectedItem().toString();
        }
        if ((JCBTeam1.getItemCount() > 0) && (JCBTeam2.getItemCount() > 0)) {
            JCBDate1.removeAllItems();
            JCBDate2.removeAllItems();
            Vector<?> tmd = new Vector<Object>();
            for (int i = 0; (tmd != null) && (i < tmd.size()); i++) {
                Vector<?> v = (Vector<?>) tmd.elementAt(i);
                JCBDate1.addItem(v.elementAt(0));
            }
            JCBDate1.addItem("(" + Commons.getModel().getLanguageString("Durchschnitt") + ")");
            JCBDate1.addItem("(" + Commons.getModel().getLanguageString("Minimal") + ")");
            JCBDate1.addItem("(" + Commons.getModel().getLanguageString("Maximal") + ")");
            tmd = new Vector<Object>();
            for (int i = 0; (tmd != null) && (i < tmd.size()); i++) {
                Vector<?> v = (Vector<?>) tmd.elementAt(i);
                JCBDate2.addItem(v.elementAt(0));
            }
            JCBDate2.addItem("(" + Commons.getModel().getLanguageString("Durchschnitt") + ")");
            JCBDate2.addItem("(" + Commons.getModel().getLanguageString("Maximal") + ")");
            JCBDate2.addItem("(" + Commons.getModel().getLanguageString("Minimal") + ")");
            if ((JCBDate1.getItemCount() > 0) && (JCBDate2.getItemCount() > 0)) {
                JCBDate1.setSelectedItem(d1);
                JCBDate2.setSelectedItem(d2);
            }
        }
    }
}
