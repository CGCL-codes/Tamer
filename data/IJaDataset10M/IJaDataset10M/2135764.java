package ch.HaagWeirich.Agenda;

import java.sql.ResultSet;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import ch.rgw.tools.*;
import ch.rgw.tools.JdbcLink.Stm;
import javax.swing.JTextPane;

/**
 * @author Gerry
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@SuppressWarnings("serial")
public class Grenzen extends JDialog {

    static String Version = "1.1.1";

    private javax.swing.JPanel jContentPane = null;

    private AgendaEntry tMorgen;

    private AgendaEntry tAbend;

    private JLabel jLabel = null;

    private JTextField inpMorgen = null;

    private JLabel jLabel1 = null;

    private JTextField inpAbend = null;

    private JButton jButton = null;

    private JTextField inpMorgenText = null;

    private JTextField inpAbendText = null;

    private JTextPane inpMorgenGrund = null;

    private JTextPane inpAbendGrund = null;

    private JLabel lbTitel = null;

    private JLabel jLabel3 = null;

    private JLabel jLabel4 = null;

    private JLabel jLabel5 = null;

    private JLabel jLabel6 = null;

    /**
	 * This is the default constructor
	 */
    public Grenzen(AgendaEntry mine) {
        super();
        try {
            initialize(mine);
        } catch (Exception ex) {
            ExHandler.handle(ex);
        }
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize(AgendaEntry tx) throws Exception {
        this.setTitle("Sperrzeiten einstellen");
        this.setSize(418, 288);
        this.setContentPane(getJContentPane());
        TimeTool tt = tx.getStartTime();
        String sql = "SELECT * FROM agnTermine WHERE Tag='" + tt.toString(TimeTool.DATE_COMPACT) + "' AND TerminTyp='" + AgendaEntry.TerminTypes[AgendaEntry.RESERVIERT] + "' AND BeiWem='" + tx.BeiWem + "' AND deleted=0 ORDER BY Beginn";
        lbTitel.setText("Gesperrte Zeitr�ume f�r " + tx.BeiWem + " am " + tx.toString(TimeTool.DATE_GER));
        Stm stm = Agenda.j.getStatement();
        ResultSet res = stm.query(sql);
        while ((res != null && (res.next() == true))) {
            AgendaEntry ae = Agenda.termin.getInstance();
            ae.fetch(res);
            if (ae.Beginn == 0) {
                tMorgen = ae;
                inpMorgenGrund.setText(ae.Grund);
                inpMorgenText.setText(ae.Personalien);
                tt = ae.getStartTime();
                tt.addMinutes(ae.Dauer);
                inpMorgen.setText(tt.toString(TimeTool.TIME_SMALL));
            }
            if (ae.Beginn + ae.Dauer == 1439) {
                tAbend = ae;
                inpAbendGrund.setText(ae.Grund);
                inpAbendText.setText(ae.Personalien);
                tt = ae.getStartTime();
                inpAbend.setText(tt.toString(TimeTool.TIME_SMALL));
            }
        }
        res.close();
        Agenda.j.releaseStatement(stm);
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel6 = new JLabel();
            jLabel5 = new JLabel();
            jLabel4 = new JLabel();
            jLabel3 = new JLabel();
            lbTitel = new JLabel();
            jLabel1 = new JLabel();
            jLabel = new JLabel();
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(null);
            jLabel.setBounds(18, 47, 74, 23);
            jLabel.setText("00:00 bis");
            jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
            jLabel1.setBounds(325, 48, 74, 23);
            jLabel1.setText("bis 23:59");
            lbTitel.setBounds(70, 14, 286, 21);
            lbTitel.setText("Gesperrte Zeitr�ume");
            jLabel3.setBounds(18, 86, 77, 15);
            jLabel3.setText("Text");
            jLabel3.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
            jLabel4.setBounds(243, 86, 65, 15);
            jLabel4.setText("Text");
            jLabel4.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
            jLabel5.setBounds(18, 129, 96, 18);
            jLabel5.setText("Erl�uterung");
            jLabel5.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
            jLabel6.setBounds(246, 129, 96, 18);
            jLabel6.setText("Erl�uterung");
            jLabel6.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
            jContentPane.add(jLabel, null);
            jContentPane.add(getInpMorgen(), null);
            jContentPane.add(jLabel1, null);
            jContentPane.add(getInpAbend(), null);
            jContentPane.add(getJButton(), null);
            jContentPane.add(getInpMorgenText(), null);
            jContentPane.add(getInpAbendText(), null);
            jContentPane.add(getInpMorgenGrund(), null);
            jContentPane.add(getInpAbendGrund(), null);
            jContentPane.add(lbTitel, null);
            jContentPane.add(jLabel3, null);
            jContentPane.add(jLabel4, null);
            jContentPane.add(jLabel5, null);
            jContentPane.add(jLabel6, null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes inpMorgen	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getInpMorgen() {
        if (inpMorgen == null) {
            inpMorgen = new JTextField();
            inpMorgen.setBounds(102, 47, 74, 23);
        }
        return inpMorgen;
    }

    /**
	 * This method initializes inpAbend	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getInpAbend() {
        if (inpAbend == null) {
            inpAbend = new JTextField();
            inpAbend.setBounds(241, 48, 74, 23);
        }
        return inpAbend;
    }

    /**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setBounds(162, 214, 85, 23);
            jButton.setText("OK");
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (tMorgen != null) {
                        TimeTool tt = tMorgen.getStartTime();
                        String morgen = inpMorgen.getText();
                        tt.set(morgen);
                        tMorgen.setEndTime(tt);
                        tMorgen.setPersonalia(inpMorgenText.getText());
                        tMorgen.Grund = inpMorgenGrund.getText();
                        tMorgen.flush();
                    }
                    if (tAbend != null) {
                        TimeTool tt = tAbend.getStartTime();
                        String abend = inpAbend.getText();
                        tt.set(abend);
                        tAbend.setStartTime(tt);
                        tAbend.Dauer = 1439 - tAbend.Beginn;
                        tAbend.Grund = inpAbendGrund.getText();
                        tAbend.setPersonalia(inpAbendText.getText());
                        tAbend.flush();
                    }
                    dispose();
                }
            });
        }
        return jButton;
    }

    /**
	 * This method initializes inpMorgenText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getInpMorgenText() {
        if (inpMorgenText == null) {
            inpMorgenText = new JTextField();
            inpMorgenText.setBounds(18, 104, 158, 18);
        }
        return inpMorgenText;
    }

    /**
	 * This method initializes inpAbendText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getInpAbendText() {
        if (inpAbendText == null) {
            inpAbendText = new JTextField();
            inpAbendText.setBounds(243, 103, 158, 18);
        }
        return inpAbendText;
    }

    /**
	 * This method initializes inpMorgenGrund	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
    private JTextPane getInpMorgenGrund() {
        if (inpMorgenGrund == null) {
            inpMorgenGrund = new JTextPane();
            inpMorgenGrund.setBounds(18, 151, 158, 53);
        }
        return inpMorgenGrund;
    }

    /**
	 * This method initializes inpAbendGrund	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
    private JTextPane getInpAbendGrund() {
        if (inpAbendGrund == null) {
            inpAbendGrund = new JTextPane();
            inpAbendGrund.setBounds(244, 151, 158, 53);
        }
        return inpAbendGrund;
    }
}
