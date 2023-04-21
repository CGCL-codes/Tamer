package com.htdsoft.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import com.htdsoft.TableModel.CompteTableModel;
import com.htdsoft.exception.BusinessException;
import com.htdsoft.generic.Erreurs;
import com.htdsoft.generic.GBC;
import com.htdsoft.noyau.Compte;
import com.htdsoft.noyau.DaoCompte;

@SuppressWarnings("serial")
public class ActionCompte extends AbstractAction {

    private JButton Enregistrer;

    private List<Compte> allcompte;

    private JPanel panel;

    private JPanel panelTous;

    private JPanel panelFeur;

    private JPanel panelClient;

    private JPanel panelAchat;

    private JPanel panelVente;

    private JPanel panelTreso;

    private JTabbedPane tabbedPane;

    private DaoCompte daocompte;

    public ActionCompte(String nom, JPanel panel) {
        super(nom);
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent event) {
        panel.removeAll();
        createPanelTous();
        createPanelFeur();
        createPanelClient();
        createPanelAchat();
        createPanelVente();
        createPanelTreso();
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Fournisseurs", panelFeur);
        tabbedPane.addTab("Clients", panelClient);
        tabbedPane.addTab("Achat", panelAchat);
        tabbedPane.addTab("Vente", panelVente);
        tabbedPane.addTab("Tr�sorerie", panelTreso);
        tabbedPane.addTab("Tous", panelTous);
        tabbedPane.setBackgroundAt(0, Color.CYAN);
        tabbedPane.setBackgroundAt(1, Color.BLUE);
        tabbedPane.setBackgroundAt(2, Color.PINK);
        tabbedPane.setBackgroundAt(3, Color.GRAY);
        tabbedPane.setBackgroundAt(3, Color.GREEN);
        tabbedPane.setBackgroundAt(4, Color.RED);
        panel.add(tabbedPane);
        panel.updateUI();
    }

    public void createPanelTous() {
        daocompte = new DaoCompte();
        allcompte = null;
        panelTous = new JPanel();
        panelTous.setLayout(new GridBagLayout());
        JLabel Titre = null;
        try {
            allcompte = daocompte.getAllComptes();
        } catch (BusinessException be) {
            Erreurs.Warning(be);
        }
        allcompte.remove(0);
        Titre = new JLabel("Liste de tous les Comptes");
        final JTable TableDetail = new JTable(new CompteTableModel(allcompte));
        TableDetail.getTableHeader().setReorderingAllowed(false);
        final JScrollPane scrollPane = new JScrollPane(TableDetail);
        TableDetail.setPreferredScrollableViewportSize(new Dimension(500, 500));
        final GBC affTableDetail = new GBC(0, 1).setInsets(10, 0, 20, 0).setAnchor(GBC.CENTER);
        resizeCol(TableDetail);
        Enregistrer = new JButton("Enregistrer");
        Enregistrer.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
                EnregistreModifCompte(TableDetail);
            }
        });
        panelTous.add(Enregistrer, new GBC(0, 2).setAnchor(GBC.CENTER));
        panelTous.add(scrollPane, affTableDetail);
        panelTous.add(Titre, new GBC(0, 0).setInsets(0, 0, 20, 0).setAnchor(GBC.CENTER));
        panel.updateUI();
    }

    public void createPanelFeur() {
        daocompte = new DaoCompte();
        allcompte = null;
        panelFeur = new JPanel();
        try {
            allcompte = daocompte.getAllComptesByNumero("401");
        } catch (BusinessException be) {
            Erreurs.Warning(be);
        }
        JLabel Titre = new JLabel("Liste des Comptes Fournisseur");
        final JTable TableDetail = new JTable(new CompteTableModel(allcompte));
        TableDetail.getTableHeader().setReorderingAllowed(false);
        final JScrollPane scrollPane = new JScrollPane(TableDetail);
        TableDetail.setPreferredScrollableViewportSize(new Dimension(500, 500));
        final GBC affTableDetail = new GBC(0, 1).setInsets(10, 0, 20, 0).setAnchor(GBC.CENTER);
        resizeCol(TableDetail);
        Enregistrer = new JButton("Enregistrer");
        Enregistrer.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
                EnregistreModifCompte(TableDetail);
            }
        });
        panelFeur.setLayout(new GridBagLayout());
        panelFeur.add(Titre, new GBC(0, 0).setInsets(0, 0, 20, 0).setAnchor(GBC.CENTER));
        panelFeur.add(scrollPane, affTableDetail);
        panelFeur.add(Enregistrer, new GBC(0, 2).setAnchor(GBC.CENTER));
        panel.updateUI();
    }

    public void createPanelClient() {
        daocompte = new DaoCompte();
        allcompte = null;
        panelClient = new JPanel();
        panelClient.setLayout(new GridBagLayout());
        try {
            allcompte = daocompte.getAllComptesByNumero("411");
        } catch (BusinessException be) {
            Erreurs.Warning(be);
        }
        JLabel Titre = null;
        Titre = new JLabel("Liste des Comptes Client");
        final JTable TableDetail = new JTable(new CompteTableModel(allcompte));
        TableDetail.getTableHeader().setReorderingAllowed(false);
        final JScrollPane scrollPane = new JScrollPane(TableDetail);
        TableDetail.setPreferredScrollableViewportSize(new Dimension(500, 500));
        final GBC affTableDetail = new GBC(0, 1).setInsets(10, 0, 20, 0).setAnchor(GBC.CENTER);
        resizeCol(TableDetail);
        Enregistrer = new JButton("Enregistrer");
        Enregistrer.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
                EnregistreModifCompte(TableDetail);
            }
        });
        panelClient.add(Enregistrer, new GBC(0, 2).setAnchor(GBC.CENTER));
        panelClient.add(scrollPane, affTableDetail);
        panelClient.add(Titre, new GBC(0, 0).setInsets(0, 0, 20, 0).setAnchor(GBC.CENTER));
        panel.updateUI();
    }

    public void createPanelAchat() {
        daocompte = new DaoCompte();
        allcompte = null;
        panelAchat = new JPanel();
        panelAchat.setLayout(new GridBagLayout());
        try {
            allcompte = daocompte.getAllComptesAchat();
        } catch (BusinessException be) {
            Erreurs.Warning(be);
        }
        JLabel Titre = null;
        Titre = new JLabel("Liste des Comptes Achat");
        final JTable TableDetail = new JTable(new CompteTableModel(allcompte));
        TableDetail.getTableHeader().setReorderingAllowed(false);
        final JScrollPane scrollPane = new JScrollPane(TableDetail);
        TableDetail.setPreferredScrollableViewportSize(new Dimension(500, 500));
        final GBC affTableDetail = new GBC(0, 1).setInsets(10, 0, 20, 0).setAnchor(GBC.CENTER);
        resizeCol(TableDetail);
        Enregistrer = new JButton("Enregistrer");
        Enregistrer.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
                EnregistreModifCompte(TableDetail);
            }
        });
        panelAchat.add(Enregistrer, new GBC(0, 2).setAnchor(GBC.CENTER));
        panelAchat.add(scrollPane, affTableDetail);
        panelAchat.add(Titre, new GBC(0, 0).setInsets(0, 0, 20, 0).setAnchor(GBC.CENTER));
        panel.updateUI();
    }

    public void createPanelVente() {
        daocompte = new DaoCompte();
        allcompte = null;
        panelVente = new JPanel();
        panelVente.setLayout(new GridBagLayout());
        try {
            allcompte = daocompte.getAllComptesVente();
        } catch (BusinessException be) {
            Erreurs.Warning(be);
        }
        JLabel Titre = null;
        Titre = new JLabel("Liste des Comptes de Ventes");
        final JTable TableDetail = new JTable(new CompteTableModel(allcompte));
        TableDetail.getTableHeader().setReorderingAllowed(false);
        final JScrollPane scrollPane = new JScrollPane(TableDetail);
        TableDetail.setPreferredScrollableViewportSize(new Dimension(500, 500));
        final GBC affTableDetail = new GBC(0, 1).setInsets(10, 0, 20, 0).setAnchor(GBC.CENTER);
        resizeCol(TableDetail);
        Enregistrer = new JButton("Enregistrer");
        Enregistrer.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
                EnregistreModifCompte(TableDetail);
            }
        });
        panelVente.add(Enregistrer, new GBC(0, 2).setAnchor(GBC.CENTER));
        panelVente.add(scrollPane, affTableDetail);
        panelVente.add(Titre, new GBC(0, 0).setInsets(0, 0, 20, 0).setAnchor(GBC.CENTER));
        panel.updateUI();
    }

    public void createPanelTreso() {
        daocompte = new DaoCompte();
        allcompte = null;
        panelTreso = new JPanel();
        panelTreso.setLayout(new GridBagLayout());
        try {
            allcompte = daocompte.getAllComptesTreso();
        } catch (BusinessException be) {
            Erreurs.Warning(be);
        }
        JLabel Titre = null;
        Titre = new JLabel("Liste des Comptes de Tr�sorerie");
        final JTable TableDetail = new JTable(new CompteTableModel(allcompte));
        TableDetail.getTableHeader().setReorderingAllowed(false);
        final JScrollPane scrollPane = new JScrollPane(TableDetail);
        TableDetail.setPreferredScrollableViewportSize(new Dimension(500, 500));
        final GBC affTableDetail = new GBC(0, 1).setInsets(10, 0, 20, 0).setAnchor(GBC.CENTER);
        resizeCol(TableDetail);
        Enregistrer = new JButton("Enregistrer");
        Enregistrer.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
                EnregistreModifCompte(TableDetail);
            }
        });
        panelTreso.add(Enregistrer, new GBC(0, 2).setAnchor(GBC.CENTER));
        panelTreso.add(scrollPane, affTableDetail);
        panelTreso.add(Titre, new GBC(0, 0).setInsets(0, 0, 20, 0).setAnchor(GBC.CENTER));
        panel.updateUI();
    }

    public void EnregistreModifCompte(JTable TableDetail) {
        Compte unCompte = new Compte();
        DaoCompte daoCompte = new DaoCompte();
        for (int i = 0; i < TableDetail.getRowCount(); i++) {
            try {
                unCompte = daoCompte.getCompteByLibelle(TableDetail.getValueAt(i, 1).toString());
            } catch (BusinessException be) {
                Erreurs.Warning(be);
            }
            try {
                unCompte.setAchat(new Boolean(TableDetail.getValueAt(i, 2).toString()));
            } catch (BusinessException be) {
                Erreurs.Warning(be);
            }
            try {
                unCompte.setVente(new Boolean(TableDetail.getValueAt(i, 3).toString()));
            } catch (BusinessException be) {
                Erreurs.Warning(be);
            }
            try {
                unCompte.setTresorerie(new Boolean(TableDetail.getValueAt(i, 4).toString()));
            } catch (BusinessException be) {
                Erreurs.Warning(be);
            }
            try {
                daoCompte.modifieCompte(unCompte);
            } catch (BusinessException be) {
                Erreurs.Warning(be);
            }
        }
    }

    public void resizeCol(JTable TableDetail) {
        TableColumn column = null;
        for (int i = 0; i < TableDetail.getColumnCount(); i++) {
            column = TableDetail.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(60);
            }
            if (i == 1) {
                column.setPreferredWidth(300);
            } else {
                column.setPreferredWidth(30);
            }
        }
    }
}
