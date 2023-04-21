package interfaces.admin;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import maingps.Main_Jen;
import donnees.ExceptionAccesDonnees;
import donnees.ExceptionCreationDonnees;
import donnees.ExceptionMiseAjourDonnees;
import donnees.ExceptionSuppressionDonnees;
import donnees.Route;
import donnees.Troncon;
import donnees.TypeRoute;

public class PanelAdminModifRoute extends PanelAdmin {

    protected FrameAdmin frame;

    protected JButton boutonAnnuler;

    protected JButton boutonCreerTroncon;

    protected JButton boutonEnregistrer;

    protected JButton boutonSupprimer;

    protected JComboBox comboType;

    protected JLabel labelNom;

    protected JLabel labelType;

    protected JScrollPane scrollpaneTroncons;

    protected JPanel panelTroncons;

    boolean creation;

    /** Creates new form PanelAdminModifRoute */
    public PanelAdminModifRoute(FrameAdmin _frame) {
        frame = _frame;
        initComponents();
    }

    public String getTitle() {
        return "Modification de route";
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        labelNom = new JLabel();
        labelType = new JLabel();
        comboType = new JComboBox();
        panelTroncons = new JPanel();
        scrollpaneTroncons = new JScrollPane(panelTroncons);
        boutonCreerTroncon = new JButton();
        boutonEnregistrer = new JButton();
        boutonSupprimer = new JButton();
        boutonAnnuler = new JButton();
        labelNom.setFont(new Font("Tahoma", 1, 11));
        labelNom.setText("Nom de la route");
        labelType.setText("Type");
        for (String type : TypeRoute.getListeTypesRoutes()) {
            comboType.addItem(type);
        }
        scrollpaneTroncons.setBorder(BorderFactory.createTitledBorder("Tron�ons"));
        boutonCreerTroncon.setText("Cr�er un tron�on");
        boutonCreerTroncon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                System.out.println("modif route" + frame.getModel().getRoute());
                DialogNewTroncon dialogNewTroncon = new DialogNewTroncon(frame);
                dialogNewTroncon.setVisible(true);
                if (dialogNewTroncon.isValide()) {
                    frame.getModel().setTroncon(dialogNewTroncon.getTroncon());
                    frame.afficherCreationTroncon();
                }
            }
        });
        boutonEnregistrer.setText("Enregistrer");
        boutonEnregistrer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (comboType.getSelectedIndex() != -1) {
                    frame.getModel().getRoute().setType(TypeRoute.getTypeRouteByName((String) comboType.getSelectedItem()));
                    try {
                        if (isCreation()) {
                            frame.getModel().createRoute(frame.getModel().getRoute());
                        } else {
                            frame.getModel().updateRoute(frame.getModel().getRoute());
                        }
                        JOptionPane.showMessageDialog(frame, "Les modifications ont �t� enregistr�es", "Modification de route", JOptionPane.INFORMATION_MESSAGE);
                        Main_Jen.log.debug("Enregistrement route");
                        frame.afficherPanelPrecedent();
                    } catch (ExceptionMiseAjourDonnees e) {
                        JOptionPane.showMessageDialog(frame, "Erreur de MAJ des donn�es : " + e.getMessage(), "Modification de route", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    } catch (ExceptionCreationDonnees e) {
                        JOptionPane.showMessageDialog(frame, "Erreur de creation de donn�es : " + e.getMessage(), "Modification de route", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    } catch (ExceptionAccesDonnees e) {
                        JOptionPane.showMessageDialog(frame, "Erreur d'acc�s aux donn�es : " + e.getMessage(), "Modification de route", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Veuillez s�lectionner le type de la route", "Modification de route", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        boutonSupprimer.setText("Supprimer");
        boutonSupprimer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int retour = JOptionPane.showConfirmDialog(frame, "Confirmez-vous la suppression de la route \"" + frame.getModel().getRoute() + "\" et de tous ses tron�ons ?", "Modification de route", JOptionPane.OK_CANCEL_OPTION);
                if (retour == JOptionPane.OK_OPTION) {
                    try {
                        frame.getModel().supprimerRoute(frame.getModel().getRoute());
                        Main_Jen.log.debug("Suppression route + tous ses troncons");
                        frame.afficherPanelPrecedent();
                    } catch (ExceptionSuppressionDonnees e) {
                        JOptionPane.showMessageDialog(frame, "Erreur d'acc�s aux donn�es : " + e.getMessage(), "Modification de route", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }
        });
        boutonAnnuler.setText("Annuler");
        boutonAnnuler.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                frame.afficherPanelPrecedent();
            }
        });
        initComponentValues();
        setLayout();
    }

    public void initComponentValues() {
        System.out.println("initcomponentvalues");
        Route r = frame.getModel().getRoute();
        try {
            labelNom.setText(r.getNom());
            if (isCreation() || r.getType() == null) {
                comboType.setSelectedIndex(-1);
            } else {
                comboType.setSelectedItem(r.getType().getNom());
            }
            panelTroncons.removeAll();
            panelTroncons.setLayout(new BoxLayout(panelTroncons, BoxLayout.Y_AXIS));
            for (Troncon t : frame.getModel().getRoute().getTroncons()) {
                LinkTroncon l = new LinkTroncon(t);
                l.addMouseListener(new MouseAdapter() {

                    public void mouseClicked(MouseEvent e) {
                        Main_Jen.log.debug("click " + ((LinkTroncon) e.getSource()).getTroncon());
                        frame.getModel().setTroncon(((LinkTroncon) e.getSource()).getTroncon());
                        frame.afficherModifTroncon();
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }

                    public void mouseEntered(MouseEvent e) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }

                    public void mouseExited(MouseEvent e) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                });
                panelTroncons.add(l);
                Main_Jen.log.debug("Troncon " + t);
            }
            boutonSupprimer.setEnabled(!isCreation());
        } catch (NullPointerException e) {
        }
    }

    public void setLayout() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(71, 71, 71).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(scrollpaneTroncons, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(boutonCreerTroncon)).addGroup(layout.createSequentialGroup().addComponent(boutonEnregistrer).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(boutonSupprimer).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(boutonAnnuler)).addGroup(layout.createSequentialGroup().addComponent(labelType).addGap(29, 29, 29).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(comboType, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE))))).addGroup(layout.createSequentialGroup().addGap(39, 39, 39).addComponent(labelNom))).addContainerGap(34, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(22, 22, 22).addComponent(labelNom).addGap(27, 27, 27).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(labelType).addComponent(comboType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(17, 17, 17).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(46, 46, 46).addComponent(boutonCreerTroncon)).addGroup(layout.createSequentialGroup().addGap(14, 14, 14).addComponent(scrollpaneTroncons, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))).addGap(19, 19, 19).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(boutonEnregistrer).addComponent(boutonSupprimer).addComponent(boutonAnnuler)).addContainerGap(20, Short.MAX_VALUE)));
    }

    public boolean isCreation() {
        return creation;
    }

    public void setCreation(boolean _creation) {
        creation = _creation;
    }
}
