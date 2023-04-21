package it.f2.gestRip.ui;

import it.f2.gestRip.model.BinScheda;
import it.f2.gestRip.ui.VcDlgDetailScheda.mode;
import it.f2.gestRip.ui.messages.Messages;
import it.f2.gestRip.util.JDBCComboBoxModel;
import it.f2.util.ui.WindowUtil;
import it.f2.util.ui.cmb.TypeCmb;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Dimension;
import java.sql.Connection;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import org.apache.log4j.Logger;

public class VcPnlApparecchio extends JPanel {

    private static final long serialVersionUID = 1L;

    private mode modality = null;

    private BinScheda scheda = null;

    private JLabel lblAppar1 = null;

    private JComboBox cmbModello = null;

    private JLabel lblModello = null;

    private JComboBox cmbMarca = null;

    private JLabel lblMarca = null;

    private JComboBox cmbTipoAppa = null;

    private JLabel lblTipoAppa = null;

    private JTextField txlSerial = null;

    private JLabel lblSerial = null;

    private JLabel lblAccessoriCons = null;

    private JScrollPane scpAccessoriCons = null;

    private JTextPane txpAccessoriCons = null;

    private JScrollPane scpStatoGenerale = null;

    private JTextPane txpStatoGenerale = null;

    private JLabel lblStatoGenerale = null;

    private JLabel lblDifettoSegnalato = null;

    private JScrollPane scpDifettoSegnalato = null;

    private JTextPane txpDifettoSegnalato = null;

    private JLabel lblNonConformita = null;

    private JScrollPane scpNonConformita = null;

    private JTextPane txpNonConformita = null;

    private JComboBox cmbTipoRip = null;

    private JLabel lblTipoRip = null;

    private JComboBox cmbStato = null;

    private JLabel lblStatoRip = null;

    private JLabel lblDatiAcquisto = null;

    private JTextField txfDANumero = null;

    private JDateChooser txfDAData = null;

    private JLabel lblTipoDa = null;

    private JLabel lblNumDa = null;

    private JLabel lblDataDa = null;

    private JComboBox cmbTipoDA = null;

    private JButton btnAddModello = null;

    private VcDlgDetailScheda parent = null;

    private JButton btnDelDataDA = null;

    private Connection con = null;

    /**
	 * This is the default constructor
	 */
    public VcPnlApparecchio(mode modality, BinScheda scheda, VcDlgDetailScheda parent, Connection con) {
        super();
        Logger.getRootLogger().debug("VcPnlApparecchio constructor...");
        this.modality = modality;
        this.scheda = scheda;
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
        lblDataDa = new JLabel();
        lblDataDa.setBounds(new Rectangle(15, 275, 67, 16));
        lblDataDa.setText(Messages.getString("VcPnlApparecchio.lblPurchasingData"));
        lblDataDa.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNumDa = new JLabel();
        lblNumDa.setBounds(new Rectangle(14, 245, 67, 16));
        lblNumDa.setText(Messages.getString("VcPnlApparecchio.lblPurchasingNum"));
        lblNumDa.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTipoDa = new JLabel();
        lblTipoDa.setBounds(new Rectangle(22, 213, 58, 16));
        lblTipoDa.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTipoDa.setText(Messages.getString("VcPnlApparecchio.lblPurchasingType"));
        lblDatiAcquisto = new JLabel();
        lblDatiAcquisto.setBounds(new Rectangle(18, 180, 208, 16));
        lblDatiAcquisto.setText(Messages.getString("VcPnlApparecchio.lblPurchasing"));
        lblStatoRip = new JLabel();
        lblStatoRip.setBounds(new Rectangle(15, 98, 171, 16));
        lblStatoRip.setText(Messages.getString("VcPnlApparecchio.lblState"));
        lblTipoRip = new JLabel();
        lblTipoRip.setBounds(new Rectangle(13, 27, 146, 16));
        lblTipoRip.setText(Messages.getString("VcPnlApparecchio.lblRepairsType"));
        this.setSize(new Dimension(818, 572));
        lblNonConformita = new JLabel();
        lblNonConformita.setBounds(new Rectangle(363, 357, 189, 16));
        lblNonConformita.setText(Messages.getString("VcPnlApparecchio.lblNonComplilance"));
        lblDifettoSegnalato = new JLabel();
        lblDifettoSegnalato.setBounds(new Rectangle(10, 342, 299, 16));
        lblDifettoSegnalato.setText(Messages.getString("VcPnlApparecchio.lblDefectReported"));
        lblStatoGenerale = new JLabel();
        lblStatoGenerale.setBounds(new Rectangle(363, 250, 231, 16));
        lblStatoGenerale.setText(Messages.getString("VcPnlApparecchio.lblGeneralStatement"));
        lblAccessoriCons = new JLabel();
        lblAccessoriCons.setBounds(new Rectangle(363, 156, 194, 16));
        lblAccessoriCons.setText(Messages.getString("VcPnlApparecchio.lblAccessoriesDelivered"));
        lblSerial = new JLabel();
        lblSerial.setBounds(new Rectangle(363, 105, 246, 16));
        lblSerial.setText(Messages.getString("VcPnlApparecchio.lblSerial"));
        lblTipoAppa = new JLabel();
        lblTipoAppa.setBounds(new Rectangle(363, 60, 130, 16));
        lblTipoAppa.setText(Messages.getString("VcPnlApparecchio.lblEqpType"));
        lblMarca = new JLabel();
        lblMarca.setBounds(new Rectangle(505, 60, 102, 16));
        lblMarca.setText(Messages.getString("VcPnlApparecchio.lblBrand"));
        lblModello = new JLabel();
        lblModello.setBounds(new Rectangle(645, 60, 112, 16));
        lblModello.setText(Messages.getString("VcPnlApparecchio.lblModel"));
        lblAppar1 = new JLabel();
        lblAppar1.setBounds(new Rectangle(363, 27, 195, 16));
        lblAppar1.setText(Messages.getString("VcPnlApparecchio.lblEqpReceived"));
        setLayout(null);
        add(lblAppar1, null);
        add(getCmbModello(), null);
        add(lblModello, null);
        add(getCmbMarca(), null);
        add(lblMarca, null);
        add(getCmbTipoAppa(), null);
        add(lblTipoAppa, null);
        add(getTxlSerial(), null);
        add(lblSerial, null);
        add(lblAccessoriCons, null);
        add(getScpAccessoriCons(), null);
        add(getScpStatoGenerale(), null);
        add(lblStatoGenerale, null);
        add(lblDifettoSegnalato, null);
        add(getScpDifettoSegnalato(), null);
        add(lblNonConformita, null);
        add(getScpNonConformita(), null);
        this.add(getCmbTipoRip(), null);
        this.add(lblTipoRip, null);
        this.add(getCmbStato(), null);
        this.add(lblStatoRip, null);
        this.add(lblDatiAcquisto, null);
        this.add(getTxfDANumero(), null);
        this.add(getTxfDAData(), null);
        this.add(lblTipoDa, null);
        this.add(lblNumDa, null);
        this.add(lblDataDa, null);
        this.add(getCmbTipoDA(), null);
        this.add(getBtnAddModello(), null);
        this.add(getBtnDelDataDA(), null);
    }

    /**
	 * This method initializes cmbModello	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JComboBox getCmbModello() {
        if (cmbModello == null) {
            cmbModello = new JComboBox();
            cmbModello.setBounds(new Rectangle(645, 75, 130, 25));
            loadCmbModello();
            if (modality == mode.view) {
                cmbModello.setEnabled(false);
            }
            cmbModello.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    int val = Integer.parseInt(((TypeCmb) cmbModello.getSelectedItem()).getValue());
                    scheda.setIdModelli(val);
                }
            });
        }
        return cmbModello;
    }

    private void loadCmbModello() {
        String qry = "";
        int tipoApp = 0;
        try {
            tipoApp = Integer.parseInt(((TypeCmb) cmbTipoAppa.getSelectedItem()).getValue());
        } catch (NullPointerException e) {
            tipoApp = 0;
        }
        int marca = 0;
        try {
            marca = Integer.parseInt(((TypeCmb) cmbMarca.getSelectedItem()).getValue());
        } catch (NullPointerException e) {
            marca = 0;
        }
        if (tipoApp == 0 && marca == 0) {
            qry = "select id,nome,flagAttivo from modelli";
        } else if (tipoApp != 0 && marca != 0) {
            qry = "select id,nome,flagAttivo from modelli" + " where idTipoApp = " + tipoApp + " and idMarchi = " + marca;
        } else if (tipoApp != 0 && marca == 0) {
            qry = "select id,nome,flagAttivo from modelli" + " where idTipoApp = " + tipoApp;
        } else if (tipoApp == 0 && marca != 0) {
            qry = "select id,nome,flagAttivo from modelli" + " where idMarchi = " + marca;
        }
        getCmbModello().setModel(new JDBCComboBoxModel(con, qry, scheda.getIdModelli() + "", "S"));
    }

    /**
	 * This method initializes txfMarca	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JComboBox getCmbMarca() {
        if (cmbMarca == null) {
            cmbMarca = new JComboBox();
            cmbMarca.setBounds(new Rectangle(505, 75, 130, 25));
            String qry = "select id,nome,flagAttivo from marchi order by nome";
            cmbMarca.setModel(new JDBCComboBoxModel(con, qry, scheda.getIdMarchi() + "", "S"));
            cmbMarca.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    int val = Integer.parseInt(((TypeCmb) cmbMarca.getSelectedItem()).getValue());
                    scheda.setIdMarchi(val);
                    loadCmbModello();
                }
            });
            if (modality == mode.view) {
                cmbMarca.setEnabled(false);
            }
        }
        return cmbMarca;
    }

    /**
	 * This method initializes cmbTipoAppa	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JComboBox getCmbTipoAppa() {
        if (cmbTipoAppa == null) {
            cmbTipoAppa = new JComboBox();
            cmbTipoAppa.setBounds(new Rectangle(363, 75, 130, 25));
            String qry = "select id,nome,flagAttivo from tipoapparecchiature";
            cmbTipoAppa.setModel(new JDBCComboBoxModel(con, qry, scheda.getIdTipoApparecchiature() + "", "S"));
            if (modality == mode.view) {
                cmbTipoAppa.setEnabled(false);
            }
            cmbTipoAppa.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    int val = Integer.parseInt(((TypeCmb) cmbTipoAppa.getSelectedItem()).getValue());
                    scheda.setIdTipoApparecchiature(val);
                    loadCmbModello();
                }
            });
        }
        return cmbTipoAppa;
    }

    /**
	 * This method initializes txlSerial	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getTxlSerial() {
        if (txlSerial == null) {
            txlSerial = new JTextField();
            txlSerial.setBounds(new Rectangle(363, 122, 273, 25));
            txlSerial.setText(scheda.getSerial());
            if (modality == mode.view) {
                txlSerial.setEditable(false);
            }
            txlSerial.addFocusListener(new java.awt.event.FocusAdapter() {

                public void focusLost(java.awt.event.FocusEvent e) {
                    scheda.setSerial(txlSerial.getText());
                }
            });
        }
        return txlSerial;
    }

    /**
	 * This method initializes scpAccessoriCons	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getScpAccessoriCons() {
        if (scpAccessoriCons == null) {
            scpAccessoriCons = new JScrollPane();
            scpAccessoriCons.setBounds(new Rectangle(363, 171, 445, 75));
            scpAccessoriCons.setViewportView(getTxpAccessoriCons());
        }
        return scpAccessoriCons;
    }

    /**
	 * This method initializes txpAccessoriCons	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
    private JTextPane getTxpAccessoriCons() {
        if (txpAccessoriCons == null) {
            txpAccessoriCons = new JTextPane();
            txpAccessoriCons.setText(scheda.getAccessoriConsegnati());
            if (modality == mode.view) {
                txpAccessoriCons.setEditable(false);
            }
            txpAccessoriCons.addFocusListener(new java.awt.event.FocusAdapter() {

                public void focusLost(java.awt.event.FocusEvent e) {
                    scheda.setAccessoriConsegnati(txpAccessoriCons.getText());
                }
            });
        }
        return txpAccessoriCons;
    }

    /**
	 * This method initializes scpStatoGenerale	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getScpStatoGenerale() {
        if (scpStatoGenerale == null) {
            scpStatoGenerale = new JScrollPane();
            scpStatoGenerale.setBounds(new Rectangle(363, 265, 445, 75));
            scpStatoGenerale.setViewportView(getTxpStatoGenerale());
        }
        return scpStatoGenerale;
    }

    /**
	 * This method initializes txpStatoGenerale	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
    private JTextPane getTxpStatoGenerale() {
        if (txpStatoGenerale == null) {
            txpStatoGenerale = new JTextPane();
            txpStatoGenerale.setText(scheda.getStatoGenerale());
            if (modality == mode.view) {
                txpStatoGenerale.setEditable(false);
            }
            txpStatoGenerale.addFocusListener(new java.awt.event.FocusAdapter() {

                public void focusLost(java.awt.event.FocusEvent e) {
                    scheda.setStatoGenerale(txpStatoGenerale.getText());
                }
            });
        }
        return txpStatoGenerale;
    }

    /**
	 * This method initializes scpDifettoSegnalato	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getScpDifettoSegnalato() {
        if (scpDifettoSegnalato == null) {
            scpDifettoSegnalato = new JScrollPane();
            scpDifettoSegnalato.setBounds(new Rectangle(9, 360, 329, 90));
            scpDifettoSegnalato.setViewportView(getTxpDifettoSegnalato());
        }
        return scpDifettoSegnalato;
    }

    /**
	 * This method initializes txpDifettoSegnalato	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
    private JTextPane getTxpDifettoSegnalato() {
        if (txpDifettoSegnalato == null) {
            txpDifettoSegnalato = new JTextPane();
            txpDifettoSegnalato.setText(scheda.getDifettoSegnalato());
            if (modality == mode.view) {
                txpDifettoSegnalato.setEditable(false);
            }
            txpDifettoSegnalato.addFocusListener(new java.awt.event.FocusAdapter() {

                public void focusLost(java.awt.event.FocusEvent e) {
                    scheda.setDifettoSegnalato(txpDifettoSegnalato.getText());
                }
            });
        }
        return txpDifettoSegnalato;
    }

    /**
	 * This method initializes scpNonConformita	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getScpNonConformita() {
        if (scpNonConformita == null) {
            scpNonConformita = new JScrollPane();
            scpNonConformita.setBounds(new Rectangle(363, 375, 445, 75));
            scpNonConformita.setViewportView(getTxpNonConformita());
        }
        return scpNonConformita;
    }

    /**
	 * This method initializes txpNonConformita	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
    private JTextPane getTxpNonConformita() {
        if (txpNonConformita == null) {
            txpNonConformita = new JTextPane();
            txpNonConformita.setText(scheda.getNonConformita());
            if (modality == mode.view) {
                txpNonConformita.setEditable(false);
            }
            txpNonConformita.addFocusListener(new java.awt.event.FocusAdapter() {

                public void focusLost(java.awt.event.FocusEvent e) {
                    scheda.setNonConformita(txpNonConformita.getText());
                }
            });
        }
        return txpNonConformita;
    }

    /**
	 * This method initializes cmbTipoRip	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getCmbTipoRip() {
        if (cmbTipoRip == null) {
            cmbTipoRip = new JComboBox();
            String qry = "select id,nomeTipoRip,flagAttivo from tiporiparazione";
            cmbTipoRip.setModel(new JDBCComboBoxModel(con, qry, scheda.getIdTipoRiparazione() + "", "S"));
            cmbTipoRip.setBounds(new Rectangle(13, 42, 185, 25));
            if (modality == mode.view) {
                cmbTipoRip.setEnabled(false);
            }
            cmbTipoRip.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    int val = Integer.parseInt(((TypeCmb) cmbTipoRip.getSelectedItem()).getValue());
                    scheda.setIdTipoRiparazione(val);
                }
            });
        }
        return cmbTipoRip;
    }

    /**
	 * This method initializes cmbStato	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JComboBox getCmbStato() {
        if (cmbStato == null) {
            cmbStato = new JComboBox();
            cmbStato.setBounds(new Rectangle(14, 114, 184, 25));
            String qry = "select id,nomeStato,flagAttivo from anastati";
            cmbStato.setModel(new JDBCComboBoxModel(con, qry, scheda.getIdStato() + "", "S"));
            if (modality == mode.view) {
                cmbStato.setEnabled(false);
            }
            cmbStato.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    int val = Integer.parseInt(((TypeCmb) cmbStato.getSelectedItem()).getValue());
                    scheda.setIdStato(val);
                }
            });
        }
        return cmbStato;
    }

    /**
	 * This method initializes txfDANumero	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getTxfDANumero() {
        if (txfDANumero == null) {
            txfDANumero = new JTextField();
            txfDANumero.setBounds(new Rectangle(83, 240, 180, 25));
            txfDANumero.setText(scheda.getNumDatiAcq());
            if (modality == mode.view) {
                txfDANumero.setEditable(false);
            }
            txfDANumero.addFocusListener(new java.awt.event.FocusAdapter() {

                public void focusLost(java.awt.event.FocusEvent e) {
                    scheda.setNumDatiAcq(txfDANumero.getText());
                }
            });
        }
        return txfDANumero;
    }

    /**
	 * This method initializes txfDAData	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JDateChooser getTxfDAData() {
        if (txfDAData == null) {
            txfDAData = new JDateChooser();
            txfDAData.setBounds(new Rectangle(83, 272, 154, 25));
            txfDAData.setDate(scheda.getDataDatiAcq());
            if (modality == mode.view) {
                txfDAData.setEnabled(false);
            }
            txfDAData.addPropertyChangeListener("date", new java.beans.PropertyChangeListener() {

                public void propertyChange(java.beans.PropertyChangeEvent e) {
                    if (txfDAData.getDate() == null) scheda.setDataDatiAcq(null); else scheda.setDataDatiAcq(new java.sql.Date(txfDAData.getDate().getTime()));
                }
            });
        }
        return txfDAData;
    }

    /**
	 * This method initializes cmbTipoDA	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getCmbTipoDA() {
        if (cmbTipoDA == null) {
            cmbTipoDA = new JComboBox();
            String qry = "select id,tipo from tpodatiacquisto";
            cmbTipoDA.setModel(new JDBCComboBoxModel(con, qry, scheda.getIdTipoDatiAcq() + ""));
            cmbTipoDA.setBounds(new Rectangle(83, 208, 180, 25));
            if (modality == mode.view) {
                cmbTipoDA.setEnabled(false);
            }
            cmbTipoDA.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    int val = Integer.parseInt(((TypeCmb) cmbTipoDA.getSelectedItem()).getValue());
                    scheda.setIdTipoDatiAcq(val);
                }
            });
        }
        return cmbTipoDA;
    }

    /**
	 * This method initializes btnAddModello	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBtnAddModello() {
        if (btnAddModello == null) {
            btnAddModello = new JButton();
            btnAddModello.setBounds(new Rectangle(776, 75, 25, 25));
            btnAddModello.setIcon(new ImageIcon(getClass().getResource("/it/f2/gestRip/ui/img/edit_add.png")));
            btnAddModello.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    openDlgInsertModello();
                }
            });
            if (modality == mode.view) {
                btnAddModello.setEnabled(false);
            }
        }
        return btnAddModello;
    }

    private void openDlgInsertModello() {
        VcDlgInsertModello dlg = new VcDlgInsertModello(parent, this, scheda.getIdMarchi() + "", scheda.getIdTipoApparecchiature() + "", con);
        WindowUtil.centerWindow(dlg);
        dlg.setVisible(true);
    }

    public void inserisciNuovoModello(int idAppa, int idMarca, int idModello, String nomeModello) {
        selectCmbValue(getCmbMarca(), idMarca);
        selectCmbValue(getCmbTipoAppa(), idAppa);
        if (!selectCmbValue(getCmbModello(), idModello)) {
            TypeCmb c = new TypeCmb();
            c.setValue(idModello + "");
            c.setDesc(nomeModello);
            getCmbModello().addItem(c);
            selectCmbValue(getCmbModello(), idModello);
        }
    }

    public boolean selectCmbValue(JComboBox cmb, int val) {
        boolean selezionato = false;
        for (int i = 0; i < cmb.getItemCount(); i++) {
            TypeCmb c = (TypeCmb) cmb.getItemAt(i);
            if (c.getValue().equals(val + "")) {
                cmb.setSelectedIndex(i);
                selezionato = true;
            }
        }
        return selezionato;
    }

    /**
	 * This method initializes btnDelDataDA	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBtnDelDataDA() {
        if (btnDelDataDA == null) {
            btnDelDataDA = new JButton();
            btnDelDataDA.setBounds(new Rectangle(238, 272, 25, 25));
            btnDelDataDA.setIcon(new ImageIcon(getClass().getResource("/it/f2/gestRip/ui/img/edit_remove.png")));
            if (modality == mode.view) {
                btnDelDataDA.setEnabled(false);
            }
            btnDelDataDA.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    getTxfDAData().setDate(null);
                }
            });
        }
        return btnDelDataDA;
    }
}
