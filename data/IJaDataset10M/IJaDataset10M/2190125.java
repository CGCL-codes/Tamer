package org.fudaa.fudaa.taucomac;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuGridLayout;
import com.memoire.bu.BuTextField;
import org.fudaa.dodico.corba.taucomac.SCorrod;
import org.fudaa.dodico.corba.taucomac.STubes;

/**
 * Onglet des Tubes.
 *
 * @version      $Revision: 1.7 $ $Date: 2006-09-19 15:08:57 $ by $Author: deniger $
 * @author       Jean-Yves Riou
 */
public class TaucomacTubes extends TaucomacMerePanParm implements ActionListener, FocusListener {

    STubes parametresLocal = null;

    JPanel pnHaut;

    JPanel pnHautG;

    JPanel pnHautD;

    JPanel pnMil;

    JPanel pnMilG;

    JPanel pnMilC;

    JPanel pnMilD;

    JPanel pnBas;

    JLabel la_Diam;

    JLabel la_Young;

    JLabel la_Dpara;

    JLabel la_Dperp;

    JLabel la_EpTete;

    JLabel la_EpPied;

    JLabel unit_Diam;

    JLabel unit_Young;

    JLabel unit_Dpara;

    JLabel unit_Dperp;

    JLabel unit_EpTete;

    JLabel unit_EpPied;

    BuTextField tf_Diam;

    BuTextField tf_Young;

    BuTextField tf_Dpara;

    BuTextField tf_Dperp;

    BuTextField tf_EpTete;

    BuTextField tf_EpPied;

    JLabel la_Tete;

    JLabel la_BUTete;

    JLabel la_unitTete;

    JLabel la_Pied;

    JLabel la_BUPied;

    JLabel la_unitPied;

    JLabel la_z1;

    JLabel la_z2;

    JLabel la_z3;

    JLabel unit_z1;

    JLabel unit_z2;

    JLabel unit_z3;

    BuTextField tf_z1;

    BuTextField tf_z2;

    BuTextField tf_z3;

    JLabel unit_Ep1;

    JLabel unit_Ep2;

    JLabel unit_Ep3;

    BuTextField tf_Ep1;

    BuTextField tf_Ep2;

    BuTextField tf_Ep3;

    public int nbMaxCorrodes_ = 3;

    JComboBox listDiam1;

    JComboBox listDiam2;

    JComboBox listDiam3;

    JLabel la_Comb1;

    JLabel la_Comb2;

    JLabel la_Comb3;

    JLabel la_Combtit1;

    JLabel la_Combtit2;

    JLabel la_Combtit3;

    public TaucomacTubes(final BuCommonInterface _appli) {
        super();
        new TaucomacLireDiam();
        listDiam1 = new JComboBox(TaucomacLireDiam.valDiam1);
        listDiam1.setPreferredSize(new Dimension(80, 80));
        listDiam2 = new JComboBox(TaucomacLireDiam.valDiam2);
        listDiam2.setPreferredSize(new Dimension(80, 80));
        listDiam3 = new JComboBox(TaucomacLireDiam.valDiam3);
        listDiam3.setPreferredSize(new Dimension(80, 80));
        listDiam1.addActionListener(this);
        listDiam2.addActionListener(this);
        listDiam3.addActionListener(this);
        pnHautG = new JPanel();
        final BuGridLayout loCal2 = new BuGridLayout();
        loCal2.setColumns(3);
        loCal2.setHgap(5);
        loCal2.setVgap(10);
        loCal2.setHfilled(true);
        loCal2.setCfilled(true);
        pnHautG.setLayout(loCal2);
        pnHautG.setBorder(new EmptyBorder(5, 20, 5, 20));
        la_Diam = new JLabel("  Diam�tre des tubes (0<D<50000 mm) ", SwingConstants.RIGHT);
        la_Young = new JLabel(" Module d'Young (0<E<5000000 MPa) ", SwingConstants.RIGHT);
        tf_Diam = BuTextField.createDoubleField();
        tf_Young = BuTextField.createDoubleField();
        unit_Diam = new JLabel(" mm ", SwingConstants.LEFT);
        unit_Young = new JLabel(" t/m2 ", SwingConstants.LEFT);
        placeUnit(unit_Diam, 4);
        placeUnit(unit_Young, 8);
        tf_Diam.setColumns(6);
        int n = 0;
        n = 0;
        placeCompn(pnHautG, la_Diam, 0, n);
        n++;
        placeCompn(pnHautG, tf_Diam, 0, n);
        n++;
        placeCompn(pnHautG, unit_Diam, 0, n);
        n++;
        placeCompn(pnHautG, la_Young, 0, n);
        n++;
        placeCompn(pnHautG, tf_Young, 0, n);
        n++;
        placeCompn(pnHautG, unit_Young, 0, n);
        pnHautD = new JPanel();
        final BuGridLayout loCal3 = new BuGridLayout();
        loCal3.setColumns(3);
        loCal3.setHgap(5);
        loCal3.setVgap(5);
        loCal3.setHfilled(true);
        loCal3.setCfilled(true);
        pnHautD.setLayout(loCal3);
        pnHautD.setBorder(new EmptyBorder(5, 40, 5, 50));
        la_Combtit1 = new JLabel("Catalogues", SwingConstants.RIGHT);
        la_Combtit2 = new JLabel("de", SwingConstants.CENTER);
        la_Combtit3 = new JLabel("Diametres", SwingConstants.LEFT);
        la_Comb1 = new JLabel("Com. 1", SwingConstants.CENTER);
        la_Comb2 = new JLabel("Com. 2", SwingConstants.CENTER);
        la_Comb3 = new JLabel("Perso.", SwingConstants.CENTER);
        n = 0;
        placeCompn(pnHautD, la_Combtit1, 0, n);
        n++;
        placeCompn(pnHautD, la_Combtit2, 0, n);
        n++;
        placeCompn(pnHautD, la_Combtit3, 0, n);
        n++;
        placeCompn(pnHautD, la_Comb1, 0, n);
        n++;
        placeCompn(pnHautD, la_Comb2, 0, n);
        n++;
        placeCompn(pnHautD, la_Comb3, 0, n);
        n++;
        placeCompn(pnHautD, listDiam1, 0, n);
        n++;
        placeCompn(pnHautD, listDiam2, 0, n);
        n++;
        placeCompn(pnHautD, listDiam3, 0, n);
        n++;
        pnHaut = new JPanel();
        final BuGridLayout loCal4 = new BuGridLayout();
        loCal4.setColumns(2);
        loCal4.setHgap(5);
        loCal4.setVgap(5);
        loCal4.setHfilled(true);
        loCal4.setCfilled(true);
        pnHaut.setLayout(loCal4);
        pnHaut.setBorder(new EmptyBorder(5, 5, 5, 5));
        n = 0;
        placeCompn(pnHaut, pnHautG, 0, n);
        n++;
        placeCompn(pnHaut, pnHautD, 0, n);
        n++;
        pnMil = new JPanel();
        final BuGridLayout loCal5 = new BuGridLayout();
        loCal5.setColumns(3);
        loCal5.setHgap(5);
        loCal5.setVgap(10);
        loCal5.setHfilled(true);
        loCal5.setCfilled(true);
        pnMil.setLayout(loCal5);
        pnMil.setBorder(new EmptyBorder(40, 5, 5, 5));
        la_Dpara = new JLabel("Distance entre axes dans la direction de l'action (amarrage/accostage) ", SwingConstants.RIGHT);
        la_Dperp = new JLabel("Distance entre axes dans la direction perpendiculaire de l'action ", SwingConstants.RIGHT);
        tf_Dpara = BuTextField.createDoubleField();
        tf_Dperp = BuTextField.createDoubleField();
        unit_Dpara = new JLabel(" mm ", SwingConstants.LEFT);
        unit_Dperp = new JLabel(" mm ", SwingConstants.LEFT);
        placeUnit(unit_Dpara, 4);
        placeUnit(unit_Dperp, 4);
        tf_Dpara.setColumns(6);
        n = 0;
        placeCompn(pnMil, la_Dpara, 1, n);
        n++;
        placeCompn(pnMil, tf_Dpara, 1, n);
        n++;
        placeCompn(pnMil, unit_Dpara, 1, n);
        n++;
        placeCompn(pnMil, la_Dperp, 1, n);
        n++;
        placeCompn(pnMil, tf_Dperp, 1, n);
        n++;
        placeCompn(pnMil, unit_Dperp, 1, n);
        pnBas = new JPanel();
        final BuGridLayout loCal6 = new BuGridLayout();
        loCal6.setColumns(5);
        loCal6.setHgap(5);
        loCal6.setVgap(10);
        loCal6.setHfilled(true);
        loCal6.setCfilled(true);
        pnBas.setLayout(loCal6);
        pnBas.setBorder(new EmptyBorder(30, 50, 5, 10));
        la_Tete = new JLabel("Epaisseur corrodee en tete des pieux ", SwingConstants.RIGHT);
        ;
        la_z1 = new JLabel("Epaisseur corrodee  a la cote        ", SwingConstants.RIGHT);
        la_z2 = new JLabel("Epaisseur corrodee  a la cote        ", SwingConstants.RIGHT);
        la_z3 = new JLabel("Epaisseur corrodee  a la cote        ", SwingConstants.RIGHT);
        la_Pied = new JLabel("Epaisseur corrodee au pied des pieux ", SwingConstants.RIGHT);
        la_BUTete = new JLabel("", SwingConstants.RIGHT);
        tf_z1 = BuTextField.createDoubleField();
        tf_z1.setHorizontalAlignment(SwingConstants.RIGHT);
        tf_z2 = BuTextField.createDoubleField();
        tf_z2.setHorizontalAlignment(SwingConstants.RIGHT);
        tf_z3 = BuTextField.createDoubleField();
        tf_z3.setHorizontalAlignment(SwingConstants.RIGHT);
        la_BUPied = new JLabel("", SwingConstants.RIGHT);
        la_unitTete = new JLabel("", SwingConstants.LEFT);
        unit_z1 = new JLabel(" m ", SwingConstants.LEFT);
        unit_z2 = new JLabel(" m ", SwingConstants.LEFT);
        unit_z3 = new JLabel(" m ", SwingConstants.LEFT);
        la_unitPied = new JLabel("", SwingConstants.LEFT);
        tf_EpTete = BuTextField.createDoubleField();
        tf_EpTete.setHorizontalAlignment(SwingConstants.RIGHT);
        tf_Ep1 = BuTextField.createDoubleField();
        tf_Ep1.setHorizontalAlignment(SwingConstants.RIGHT);
        tf_Ep2 = BuTextField.createDoubleField();
        tf_Ep2.setHorizontalAlignment(SwingConstants.RIGHT);
        tf_Ep3 = BuTextField.createDoubleField();
        tf_Ep3.setHorizontalAlignment(SwingConstants.RIGHT);
        tf_EpPied = BuTextField.createDoubleField();
        tf_EpPied.setHorizontalAlignment(SwingConstants.RIGHT);
        unit_EpTete = new JLabel(" mm", SwingConstants.LEFT);
        unit_Ep1 = new JLabel(" mm", SwingConstants.LEFT);
        unit_Ep2 = new JLabel(" mm", SwingConstants.LEFT);
        unit_Ep3 = new JLabel(" mm", SwingConstants.LEFT);
        unit_EpPied = new JLabel(" mm", SwingConstants.LEFT);
        placeUnit(unit_z1, 1);
        placeUnit(unit_z2, 1);
        placeUnit(unit_z3, 1);
        placeUnit(unit_EpTete, 4);
        placeUnit(unit_Ep1, 4);
        placeUnit(unit_Ep2, 4);
        placeUnit(unit_Ep3, 4);
        placeUnit(unit_EpPied, 4);
        tf_EpTete.setColumns(6);
        tf_z1.setColumns(6);
        tf_Ep1.setColumns(6);
        n = 0;
        placeCompn(pnBas, la_Tete, 0, n);
        n++;
        placeCompn(pnBas, la_BUTete, 0, n);
        n++;
        placeCompn(pnBas, la_unitTete, 0, n);
        n++;
        placeCompn(pnBas, tf_EpTete, 0, n);
        n++;
        placeCompn(pnBas, unit_EpTete, 0, n);
        n++;
        placeCompn(pnBas, la_z1, 1, n);
        n++;
        placeCompn(pnBas, tf_z1, 1, n);
        n++;
        placeCompn(pnBas, unit_z1, 1, n);
        n++;
        placeCompn(pnBas, tf_Ep1, 1, n);
        n++;
        placeCompn(pnBas, unit_Ep1, 1, n);
        n++;
        placeCompn(pnBas, la_z2, 1, n);
        n++;
        placeCompn(pnBas, tf_z2, 1, n);
        n++;
        placeCompn(pnBas, unit_z2, 1, n);
        n++;
        placeCompn(pnBas, tf_Ep2, 1, n);
        n++;
        placeCompn(pnBas, unit_Ep2, 1, n);
        n++;
        placeCompn(pnBas, la_z3, 1, n);
        n++;
        placeCompn(pnBas, tf_z3, 1, n);
        n++;
        placeCompn(pnBas, unit_z3, 1, n);
        n++;
        placeCompn(pnBas, tf_Ep3, 1, n);
        n++;
        placeCompn(pnBas, unit_Ep3, 1, n);
        n++;
        placeCompn(pnBas, la_Pied, 1, n);
        n++;
        placeCompn(pnBas, la_BUPied, 1, n);
        n++;
        placeCompn(pnBas, la_unitPied, 1, n);
        n++;
        placeCompn(pnBas, tf_EpPied, 1, n);
        n++;
        placeCompn(pnBas, unit_EpPied, 1, n);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(40, 10, 40, 10));
        this.add(pnHaut, BorderLayout.NORTH);
        this.add(pnMil, BorderLayout.CENTER);
        this.add(pnBas, BorderLayout.SOUTH);
        setParametres(parametresLocal);
    }

    public void focusLost(final FocusEvent e) {
        final Object src = e.getSource();
        if (src instanceof JTextField) {
        }
    }

    public void focusGained(final FocusEvent e) {
    }

    public void actionPerformed(final ActionEvent e) {
        final Object src = e.getSource();
        System.out.println("action performed  " + e + " " + src);
        if (src instanceof JComboBox) {
            final JComboBox cb = (JComboBox) e.getSource();
            final String reselect = (String) cb.getSelectedItem();
            tf_Diam.setValue(reselect);
        }
        parametresLocal = getParametres();
        setVisible(true);
    }

    public void setParametres(final STubes parametresProjet_) {
        System.out.println("*********");
        parametresLocal = parametresProjet_;
        if (parametresLocal != null) {
            if (parametresLocal.diametre >= 0) {
                tf_Diam.setValue(new Double(parametresLocal.diametre));
            } else {
                tf_Diam.setValue("");
            }
            if (parametresLocal.young >= 0) {
                tf_Young.setValue(new Double(parametresLocal.young));
            } else {
                tf_Young.setValue("");
            }
            if (parametresLocal.distAction >= 0) {
                tf_Dpara.setValue(new Double(parametresLocal.distAction));
            } else {
                tf_Dpara.setValue("");
            }
            if (parametresLocal.distPerpend >= 0) {
                tf_Dperp.setValue(new Double(parametresLocal.distPerpend));
            } else {
                tf_Dperp.setValue("");
            }
            if (parametresLocal.epCorrTet >= 0) {
                tf_EpTete.setValue(new Double(parametresLocal.epCorrTet));
            } else {
                tf_EpTete.setValue("");
            }
            if (parametresLocal.epCorrPied >= 0) {
                tf_EpPied.setValue(new Double(parametresLocal.epCorrPied));
            } else {
                tf_EpPied.setValue("");
            }
            if (parametresLocal.corrTubes[0].z >= 0) {
                tf_z1.setValue(new Double(parametresLocal.corrTubes[0].z));
            } else {
                tf_z1.setValue("");
            }
            if (parametresLocal.corrTubes[0].ep >= 0) {
                tf_Ep1.setValue(new Double(parametresLocal.corrTubes[0].ep));
            } else {
                tf_Ep1.setValue("");
            }
            if (parametresLocal.corrTubes[1].z >= 0) {
                tf_z2.setValue(new Double(parametresLocal.corrTubes[1].z));
            } else {
                tf_z2.setValue("");
            }
            if (parametresLocal.corrTubes[1].ep >= 0) {
                tf_Ep2.setValue(new Double(parametresLocal.corrTubes[1].ep));
            } else {
                tf_Ep2.setValue("");
            }
            if (parametresLocal.corrTubes[2].z >= 0) {
                tf_z3.setValue(new Double(parametresLocal.corrTubes[2].z));
            } else {
                tf_z3.setValue("");
            }
            if (parametresLocal.corrTubes[2].ep >= 0) {
                tf_Ep3.setValue(new Double(parametresLocal.corrTubes[2].ep));
            } else {
                tf_Ep3.setValue("");
            }
        }
    }

    public STubes getParametres() {
        if (parametresLocal == null) {
            parametresLocal = new STubes();
            parametresLocal.corrTubes = new SCorrod[nbMaxCorrodes_];
            System.out.println(" parametresLocal = " + parametresLocal);
            for (int i = 0; i < nbMaxCorrodes_; i++) {
                parametresLocal.corrTubes[i] = new SCorrod();
            }
        }
        String changed = "";
        double tmpD = 0.;
        String tmpS = "";
        final String blanc = "";
        tmpD = parametresLocal.diametre;
        tmpS = tf_Diam.getText();
        if (tmpS.equals(blanc)) {
            parametresLocal.diametre = -999.;
        } else {
            parametresLocal.diametre = Double.parseDouble(tmpS);
        }
        if (parametresLocal.diametre != tmpD) {
            changed += "diametre|";
        }
        tmpD = parametresLocal.young;
        tmpS = (tf_Young.getText());
        if (tmpS.equals(blanc)) {
            parametresLocal.young = -999.;
        } else {
            parametresLocal.young = Double.parseDouble(tmpS);
        }
        if (parametresLocal.young != tmpD) {
            changed += "young|";
        }
        tmpD = parametresLocal.distAction;
        tmpS = (tf_Dpara.getText());
        if (tmpS.equals(blanc)) {
            parametresLocal.distAction = -999.;
        } else {
            parametresLocal.distAction = Double.parseDouble(tmpS);
        }
        if (parametresLocal.distAction != tmpD) {
            changed += "distAction|";
        }
        tmpD = parametresLocal.distPerpend;
        tmpS = (tf_Dperp.getText());
        if (tmpS.equals(blanc)) {
            parametresLocal.distPerpend = -999.;
        } else {
            parametresLocal.distPerpend = Double.parseDouble(tmpS);
        }
        if (parametresLocal.distPerpend != tmpD) {
            changed += "distPerpend|";
        }
        tmpD = parametresLocal.epCorrTet;
        tmpS = (tf_EpTete.getText());
        if (tmpS.equals(blanc)) {
            parametresLocal.epCorrTet = -999.;
        } else {
            parametresLocal.epCorrTet = Double.parseDouble(tmpS);
        }
        if (parametresLocal.epCorrTet != tmpD) {
            changed += "epCorrTet|";
        }
        tmpD = parametresLocal.epCorrPied;
        tmpS = (tf_EpPied.getText());
        if (tmpS.equals(blanc)) {
            parametresLocal.epCorrPied = -999.;
        } else {
            parametresLocal.epCorrPied = Double.parseDouble(tmpS);
        }
        if (parametresLocal.epCorrPied != tmpD) {
            changed += "epCorrPied|";
        }
        tmpD = parametresLocal.corrTubes[0].z;
        tmpS = (tf_z1.getText());
        if (tmpS.equals(blanc)) {
            parametresLocal.corrTubes[0].z = -999.;
        } else {
            parametresLocal.corrTubes[0].z = Double.parseDouble(tmpS);
        }
        if (parametresLocal.corrTubes[0].z != tmpD) {
            changed += "corrTubes[0].z|";
        }
        tmpD = parametresLocal.corrTubes[0].ep;
        tmpS = (tf_Ep1.getText());
        if (tmpS.equals(blanc)) {
            parametresLocal.corrTubes[0].ep = -999.;
        } else {
            parametresLocal.corrTubes[0].ep = Double.parseDouble(tmpS);
        }
        if (parametresLocal.corrTubes[0].ep != tmpD) {
            changed += "corrTubes[0].ep|";
        }
        tmpD = parametresLocal.corrTubes[1].z;
        tmpS = (tf_z2.getText());
        if (tmpS.equals(blanc)) {
            parametresLocal.corrTubes[1].z = -999.;
        } else {
            parametresLocal.corrTubes[1].z = Double.parseDouble(tmpS);
        }
        if (parametresLocal.corrTubes[1].z != tmpD) {
            changed += "corrTubes[1].z|";
        }
        tmpD = parametresLocal.corrTubes[1].ep;
        tmpS = (tf_Ep2.getText());
        if (tmpS.equals(blanc)) {
            parametresLocal.corrTubes[1].ep = -999.;
        } else {
            parametresLocal.corrTubes[1].ep = Double.parseDouble(tmpS);
        }
        if (parametresLocal.corrTubes[1].ep != tmpD) {
            changed += "corrTubes[1].ep|";
        }
        tmpD = parametresLocal.corrTubes[2].z;
        tmpS = (tf_z3.getText());
        if (tmpS.equals(blanc)) {
            parametresLocal.corrTubes[2].z = -999.;
        } else {
            parametresLocal.corrTubes[2].z = Double.parseDouble(tmpS);
        }
        if (parametresLocal.corrTubes[2].z != tmpD) {
            changed += "corrTubes[2].z|";
        }
        tmpD = parametresLocal.corrTubes[2].ep;
        tmpS = (tf_Ep3.getText());
        if (tmpS.equals(blanc)) {
            parametresLocal.corrTubes[2].ep = -999.;
        } else {
            parametresLocal.corrTubes[2].ep = Double.parseDouble(tmpS);
        }
        if (parametresLocal.corrTubes[2].ep != tmpD) {
            changed += "corrTubes[2].ep|";
        }
        int n = 0;
        for (int i = 0; i < 3; i++) {
            if (parametresLocal.corrTubes[i].z != -999) {
                if (parametresLocal.corrTubes[i].ep != -999) {
                    n++;
                }
            }
        }
        parametresLocal.nbCorrod = n;
        return parametresLocal;
    }

    public void majlook(final int _style, final int _phase, final int _unit, final int _nTubes) {
        final int phase_ = _phase;
        final int nTubes_ = _nTubes;
        if (phase_ == 0) {
            la_Dpara.setVisible(false);
            tf_Dpara.setVisible(false);
            unit_Dperp.setVisible(false);
            la_Dperp.setVisible(false);
            unit_Dpara.setVisible(false);
            tf_Dperp.setVisible(false);
        } else {
            la_Dpara.setVisible(true);
            tf_Dpara.setVisible(true);
            unit_Dperp.setVisible(true);
            la_Dperp.setVisible(true);
            unit_Dpara.setVisible(true);
            tf_Dperp.setVisible(true);
            if (nTubes_ < 2) {
                la_Dpara.setVisible(false);
                tf_Dpara.setVisible(false);
                unit_Dperp.setVisible(false);
                la_Dperp.setVisible(false);
                unit_Dpara.setVisible(false);
                tf_Dperp.setVisible(false);
            } else {
                if (nTubes_ == 3) {
                    la_Dperp.setVisible(false);
                    unit_Dperp.setVisible(false);
                    tf_Dperp.setVisible(false);
                }
            }
        }
    }
}
