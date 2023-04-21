package it.f2.gestRip.ui;

import it.f2.gestRip.ui.messages.Messages;
import it.f2.gestRip.util.JDBCComboBoxModel;
import it.f2.util.ui.cmb.TypeCmb;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import org.apache.log4j.Logger;

public class VcDlgInsertModello extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JComboBox cmbTipoAppa = null;

    private String marca = null;

    private String tipoAppa = null;

    private JLabel lblTipoAppa = null;

    private JLabel lblMarca = null;

    private JComboBox cmbMarca = null;

    private JLabel lblModello = null;

    private JTextField txfModello = null;

    private JButton btnOk = null;

    private JButton btnCanc = null;

    private JLabel lbDescMod = null;

    private JTextField txfDescMod = null;

    private VcPnlApparecchio parent = null;

    private Connection con = null;

    /**
	 * @param owner
	 */
    public VcDlgInsertModello(JDialog owner, VcPnlApparecchio parent, String marca, String tipoAppa, Connection con) {
        super(owner, true);
        Logger.getRootLogger().debug("VcDlgInsertModello constructor...");
        this.marca = marca;
        this.tipoAppa = tipoAppa;
        this.parent = parent;
        this.con = con;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(370, 233);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setTitle(Messages.getString("VcDlgInsertModello.titleInsModel"));
        this.setContentPane(getJContentPane());
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            lbDescMod = new JLabel();
            lbDescMod.setBounds(new Rectangle(2, 121, 135, 16));
            lbDescMod.setHorizontalAlignment(SwingConstants.RIGHT);
            lbDescMod.setText(Messages.getString("VcDlgInsertModello.lblDesc"));
            lblModello = new JLabel();
            lblModello.setBounds(new Rectangle(3, 92, 135, 16));
            lblModello.setHorizontalAlignment(SwingConstants.RIGHT);
            lblModello.setText(Messages.getString("VcDlgInsertModello.lblModel"));
            lblMarca = new JLabel();
            lblMarca.setBounds(new Rectangle(3, 62, 135, 16));
            lblMarca.setHorizontalAlignment(SwingConstants.RIGHT);
            lblMarca.setText(Messages.getString("VcDlgInsertModello.lblBrand"));
            lblTipoAppa = new JLabel();
            lblTipoAppa.setBounds(new Rectangle(4, 31, 135, 16));
            lblTipoAppa.setHorizontalAlignment(SwingConstants.RIGHT);
            lblTipoAppa.setText(Messages.getString("VcDlgInsertModello.lblEqpType"));
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getCmbTipoAppa(), null);
            jContentPane.add(lblTipoAppa, null);
            jContentPane.add(lblMarca, null);
            jContentPane.add(getCmbMarca(), null);
            jContentPane.add(lblModello, null);
            jContentPane.add(getTxfModello(), null);
            jContentPane.add(getBtnOk(), null);
            jContentPane.add(getBtnCanc(), null);
            jContentPane.add(lbDescMod, null);
            jContentPane.add(getTxfDescMod(), null);
            jContentPane.add(cmbTipoAppa, null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes cmbTipoAppa	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getCmbTipoAppa() {
        if (cmbTipoAppa == null) {
            cmbTipoAppa = new JComboBox();
            cmbTipoAppa.setBounds(new Rectangle(145, 27, 190, 25));
            String qry = "select id,nome,flagAttivo from tipoapparecchiature";
            cmbTipoAppa.setModel(new JDBCComboBoxModel(con, qry, tipoAppa, "S"));
        }
        return cmbTipoAppa;
    }

    /**
	 * This method initializes cmbMarca	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getCmbMarca() {
        if (cmbMarca == null) {
            cmbMarca = new JComboBox();
            cmbMarca.setBounds(new Rectangle(145, 58, 190, 25));
            String qry = "select id,nome,flagAttivo from marchi";
            cmbMarca.setModel(new JDBCComboBoxModel(con, qry, marca, "S"));
        }
        return cmbMarca;
    }

    /**
	 * This method initializes txfModello	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getTxfModello() {
        if (txfModello == null) {
            txfModello = new JTextField();
            txfModello.setBounds(new Rectangle(145, 89, 190, 25));
        }
        return txfModello;
    }

    /**
	 * This method initializes btnOk	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBtnOk() {
        if (btnOk == null) {
            btnOk = new JButton();
            btnOk.setIcon(new ImageIcon(getClass().getResource("/it/f2/gestRip/ui/img/button_ok.png")));
            btnOk.setBounds(new Rectangle(95, 163, 85, 25));
            btnOk.setText(Messages.getString("VcDlgInsertModello.btnOk"));
            btnOk.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    inserisci();
                }
            });
        }
        return btnOk;
    }

    private void inserisci() {
        try {
            Logger.getRootLogger().debug("Inserisci...");
            String modello = getTxfModello().getText();
            String descModello = getTxfDescMod().getText();
            int idMarca = 0;
            int idAppa = 0;
            try {
                idMarca = Integer.parseInt(((TypeCmb) getCmbMarca().getSelectedItem()).getValue());
            } catch (NullPointerException e) {
            }
            try {
                idAppa = Integer.parseInt(((TypeCmb) getCmbTipoAppa().getSelectedItem()).getValue());
            } catch (NullPointerException e) {
            }
            if (modello == null || modello.equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(getParent(), Messages.getString("VcDlgInsertModello.msgErrModel"), Messages.getString("VcDlgInsertModello.msgTitleInfo"), JOptionPane.INFORMATION_MESSAGE);
            } else if (idMarca == 0 || idAppa == 0) {
                JOptionPane.showMessageDialog(getParent(), Messages.getString("VcDlgInsertModello.msgErrEqpBrand"), Messages.getString("VcDlgInsertModello.msgTitleInfo"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                int id = 0;
                Statement smtpMod = con.createStatement();
                String qryMod = "select id from modelli " + "where idMarchi = " + idMarca + " " + "and idTipoApp = " + idAppa + " " + "and nome = '" + modello + "'";
                ResultSet rsMod = smtpMod.executeQuery(qryMod);
                while (rsMod.next()) {
                    id = rsMod.getInt(1);
                }
                rsMod.close();
                smtpMod.close();
                if (id > 0) {
                    JOptionPane.showMessageDialog(getParent(), Messages.getString("VcDlgInsertModello.msgErrModelExist"), Messages.getString("VcDlgInsertModello.msgTitleInfo"), JOptionPane.INFORMATION_MESSAGE);
                    parent.inserisciNuovoModello(idAppa, idMarca, id, modello);
                    this.setVisible(false);
                    this.dispose();
                } else {
                    Statement smtpId = con.createStatement();
                    String qryId = "select max(id) from modelli";
                    ResultSet rsId = smtpId.executeQuery(qryId);
                    while (rsId.next()) {
                        id = rsId.getInt(1);
                    }
                    rsId.close();
                    smtpId.close();
                    id++;
                    Statement smtpIns = con.createStatement();
                    String ins = "insert into modelli " + "(id,nome,descModello,idMarchi,idTipoApp,flagAttivo) " + "values(" + id + ",'" + modello + "','" + descModello + "'," + idMarca + "," + idAppa + ",'S') ";
                    smtpIns.executeUpdate(ins);
                    smtpIns.close();
                    parent.inserisciNuovoModello(idAppa, idMarca, id, modello);
                    this.setVisible(false);
                    this.dispose();
                }
            }
        } catch (SQLException e) {
            Logger.getRootLogger().error("Exception in Inserisci \n" + e + "\n");
        }
    }

    /**
	 * This method initializes btnCanc	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBtnCanc() {
        if (btnCanc == null) {
            btnCanc = new JButton();
            btnCanc.setBounds(new Rectangle(189, 163, 85, 25));
            btnCanc.setText(Messages.getString("VcDlgInsertModello.btnCanc"));
            btnCanc.setIcon(new ImageIcon(getClass().getResource("/it/f2/gestRip/ui/img/button_cancel.png")));
            btnCanc.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                    dispose();
                }
            });
        }
        return btnCanc;
    }

    /**
	 * This method initializes txfDescMod	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getTxfDescMod() {
        if (txfDescMod == null) {
            txfDescMod = new JTextField();
            txfDescMod.setBounds(new Rectangle(145, 120, 190, 25));
        }
        return txfDescMod;
    }
}
