package fr.ign.cogit.appli.geopensim.appli.rules;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import fr.ign.cogit.appli.geopensim.appli.GeOpenSimApplication;
import fr.ign.cogit.appli.geopensim.evolution.EvolutionRule;
import fr.ign.cogit.appli.geopensim.evolution.EvolutionRuleBase;
import org.apache.log4j.Logger;

/**
 * @author Florence Curie
 *
 */
public class EditRulesFramev2 extends JFrame implements ActionListener {

    private static final long serialVersionUID = -3096219058454297601L;

    private static final Logger logger = Logger.getLogger(EditRulesFramev2.class.getName());

    private JButton boutonAjouterUnite, boutonSupprimerUnite, boutonModifierUnite, boutonFermer;

    private JButton boutonAjouterZone, boutonSupprimerZone, boutonModifierZone;

    private DefaultListModel listModelUnite, listModelZone;

    private JList listReglesUnite, listReglesZone;

    private EvolutionRuleBase configuration;

    private List<EvolutionRule> listeRulesZone, listeRulesUnite;

    public EditRulesFramev2() {
        super();
        this.setTitle("Règles d'évolution");
        this.setBounds(50, 100, 500, 400);
        this.setResizable(true);
        this.setIconImage(new ImageIcon(GeOpenSimApplication.class.getResource("/geopensim-icon.gif")).getImage());
        Container contenu = this.getContentPane();
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
            }
        });
        configuration = EvolutionRuleBase.getInstance();
        List<EvolutionRule> listRules = configuration.getRules();
        listModelUnite = new DefaultListModel();
        listModelZone = new DefaultListModel();
        listeRulesUnite = new ArrayList<EvolutionRule>();
        listeRulesZone = new ArrayList<EvolutionRule>();
        for (EvolutionRule rule : listRules) {
            if (rule.getType().equals("ZoneElementaireUrbaine")) {
                String str = makeString(rule);
                listModelZone.addElement(str);
                listeRulesZone.add(rule);
            } else if (rule.getType().equals("UniteUrbaine")) {
                String str = makeString(rule);
                listModelUnite.addElement(str);
                listeRulesUnite.add(rule);
            }
        }
        JPanel unitePanel = new JPanel(false);
        unitePanel.setLayout(new GridLayout(0, 1));
        JLabel columnLabelUnite = new JLabel(" période : (fréquence) [précondition] expression");
        Box hBoxLabelUnite = Box.createHorizontalBox();
        hBoxLabelUnite.add(columnLabelUnite);
        hBoxLabelUnite.add(Box.createHorizontalGlue());
        listReglesUnite = new JList(listModelUnite);
        if (!listeRulesUnite.isEmpty()) {
            int index = listeRulesUnite.size() - 1;
            listReglesUnite.setSelectedIndex(index);
            listReglesUnite.ensureIndexIsVisible(index);
        }
        JScrollPane defilUnite = new JScrollPane(listReglesUnite);
        defilUnite.setMinimumSize(new Dimension(500, 300));
        boutonAjouterUnite = new JButton("ajouter");
        boutonAjouterUnite.addActionListener(this);
        boutonSupprimerUnite = new JButton("supprimer");
        boutonSupprimerUnite.addActionListener(this);
        boutonModifierUnite = new JButton("modifier");
        boutonModifierUnite.addActionListener(this);
        if (listeRulesUnite.isEmpty()) {
            boutonModifierUnite.setEnabled(false);
            boutonSupprimerUnite.setEnabled(false);
        }
        Box hBoxAjoutSupprUnite = Box.createHorizontalBox();
        hBoxAjoutSupprUnite.add(boutonAjouterUnite);
        hBoxAjoutSupprUnite.add(Box.createRigidArea(new Dimension(10, 0)));
        hBoxAjoutSupprUnite.add(boutonSupprimerUnite);
        hBoxAjoutSupprUnite.add(Box.createRigidArea(new Dimension(10, 0)));
        hBoxAjoutSupprUnite.add(boutonModifierUnite);
        hBoxAjoutSupprUnite.add(Box.createHorizontalGlue());
        Box vBoxUnite = Box.createVerticalBox();
        vBoxUnite.add(hBoxLabelUnite);
        vBoxUnite.add(Box.createVerticalStrut(3));
        vBoxUnite.add(defilUnite);
        vBoxUnite.add(Box.createVerticalStrut(10));
        vBoxUnite.add(hBoxAjoutSupprUnite);
        vBoxUnite.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        unitePanel.add(vBoxUnite);
        JPanel zonePanel = new JPanel(false);
        ;
        zonePanel.setLayout(new GridLayout(0, 1));
        JLabel columnLabelZone = new JLabel(" propriété = nomRegleEvolution (période)");
        Box hBoxLabelZone = Box.createHorizontalBox();
        hBoxLabelZone.add(columnLabelZone);
        hBoxLabelZone.add(Box.createHorizontalGlue());
        listReglesZone = new JList(listModelZone);
        if (!listeRulesZone.isEmpty()) {
            int index = listeRulesZone.size() - 1;
            listReglesZone.setSelectedIndex(index);
            listReglesZone.ensureIndexIsVisible(index);
        }
        JScrollPane defilZone = new JScrollPane(listReglesZone);
        defilZone.setMinimumSize(new Dimension(500, 300));
        boutonAjouterZone = new JButton("ajouter");
        boutonAjouterZone.addActionListener(this);
        boutonSupprimerZone = new JButton("supprimer");
        boutonSupprimerZone.addActionListener(this);
        boutonModifierZone = new JButton("modifier");
        boutonModifierZone.addActionListener(this);
        if (listeRulesZone.isEmpty()) {
            boutonModifierZone.setEnabled(false);
            boutonSupprimerZone.setEnabled(false);
        }
        Box hBoxAjoutSupprZone = Box.createHorizontalBox();
        hBoxAjoutSupprZone.add(boutonAjouterZone);
        hBoxAjoutSupprZone.add(Box.createRigidArea(new Dimension(10, 0)));
        hBoxAjoutSupprZone.add(boutonSupprimerZone);
        hBoxAjoutSupprZone.add(Box.createRigidArea(new Dimension(10, 0)));
        hBoxAjoutSupprZone.add(boutonModifierZone);
        hBoxAjoutSupprZone.add(Box.createHorizontalGlue());
        Box vBoxZone = Box.createVerticalBox();
        vBoxZone.add(hBoxLabelZone);
        vBoxZone.add(Box.createVerticalStrut(3));
        vBoxZone.add(defilZone);
        vBoxZone.add(Box.createVerticalStrut(10));
        vBoxZone.add(hBoxAjoutSupprZone);
        vBoxZone.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        zonePanel.add(vBoxZone);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Unite Urbaine", unitePanel);
        tabbedPane.addTab("Zone Elementaire Urbaine", zonePanel);
        boutonFermer = new JButton("Fermer");
        boutonFermer.addActionListener(this);
        Box hBoxBoutons = Box.createHorizontalBox();
        hBoxBoutons.add(Box.createHorizontalGlue());
        hBoxBoutons.add(Box.createRigidArea(new Dimension(10, 0)));
        hBoxBoutons.add(boutonFermer);
        Box vBoxFinal = Box.createVerticalBox();
        vBoxFinal.add(tabbedPane);
        vBoxFinal.add(Box.createRigidArea(new Dimension(0, 10)));
        vBoxFinal.add(hBoxBoutons);
        vBoxFinal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenu.add(vBoxFinal, BorderLayout.CENTER);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        JFrame fenetre1 = new EditRulesFramev2();
        fenetre1.setVisible(true);
    }

    public String makeString(EvolutionRule rule) {
        String strAff = rule.getPropertyName() + " = " + rule.getNom() + " (" + rule.getStart() + " - " + rule.getEnd() + ")";
        return strAff;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(boutonFermer)) {
            setVisible(false);
            dispose();
        } else if (e.getSource().equals(boutonAjouterUnite)) {
            EvolutionRuleCreationDialog fenetre2 = new EvolutionRuleCreationDialog(this);
            EvolutionRule rule = fenetre2.getRule();
            if (rule != null) {
                rule.setType("UniteUrbaine");
                String strAffich = makeString(rule);
                logger.info(strAffich);
                int index = listModelUnite.size();
                listModelUnite.insertElementAt(strAffich, index);
                listeRulesUnite.add(rule);
                listReglesUnite.setSelectedIndex(index);
                listReglesUnite.ensureIndexIsVisible(index);
                if (listModelUnite.size() > 0) {
                    boutonSupprimerUnite.setEnabled(true);
                    boutonModifierUnite.setEnabled(true);
                }
                configuration.getRules().add(rule);
                configuration.marshall();
                boutonModifierUnite.setEnabled(true);
                boutonSupprimerUnite.setEnabled(true);
            }
        } else if (e.getSource().equals(boutonModifierUnite)) {
            int index = listReglesUnite.getSelectedIndex();
            EvolutionRuleCreationDialog fenetre2 = new EvolutionRuleCreationDialog(this, listeRulesUnite.get(index));
            EvolutionRule rule = fenetre2.getRule();
            if (rule != null) {
                rule.setType("UniteUrbaine");
                listeRulesUnite.set(index, rule);
                String strAffich = makeString(rule);
                listModelUnite.setElementAt(strAffich, index);
                configuration.setRules(listeRulesUnite);
                configuration.marshall();
            }
        } else if (e.getSource().equals(boutonSupprimerUnite)) {
            int index = listReglesUnite.getSelectedIndex();
            EvolutionRule rule = listeRulesUnite.get(index);
            listModelUnite.remove(index);
            listeRulesUnite.remove(index);
            if (listModelUnite.size() == 0) {
                boutonSupprimerUnite.setEnabled(false);
                boutonModifierUnite.setEnabled(false);
            } else {
                if (index == listModelUnite.getSize()) {
                    index--;
                }
                listReglesUnite.setSelectedIndex(index);
                listReglesUnite.ensureIndexIsVisible(index);
            }
            configuration.getRules().remove(rule);
            configuration.marshall();
        } else if (e.getSource().equals(boutonAjouterZone)) {
            EvolutionRuleCreationDialog fenetre2 = new EvolutionRuleCreationDialog(this);
            EvolutionRule rule = fenetre2.getRule();
            if (rule != null) {
                rule.setType("ZoneElementaireUrbaine");
                String strAffich = makeString(rule);
                logger.info(strAffich);
                int index = listModelZone.size();
                listModelZone.insertElementAt(strAffich, index);
                listeRulesZone.add(rule);
                listReglesZone.setSelectedIndex(index);
                listReglesZone.ensureIndexIsVisible(index);
                if (listModelZone.size() > 0) {
                    boutonSupprimerZone.setEnabled(true);
                    boutonModifierZone.setEnabled(true);
                }
                configuration.getRules().add(rule);
                configuration.marshall();
                boutonModifierZone.setEnabled(true);
                boutonSupprimerZone.setEnabled(true);
            }
        } else if (e.getSource().equals(boutonModifierZone)) {
            int index = listReglesZone.getSelectedIndex();
            EvolutionRuleCreationDialog fenetre2 = new EvolutionRuleCreationDialog(this, listeRulesZone.get(index));
            EvolutionRule rule = fenetre2.getRule();
            if (rule != null) {
                rule.setType("ZoneElementaireUrbaine");
                listeRulesZone.set(index, rule);
                String strAffich = makeString(rule);
                listModelZone.setElementAt(strAffich, index);
                configuration.setRules(listeRulesZone);
                configuration.marshall();
            }
        } else if (e.getSource().equals(boutonSupprimerZone)) {
            int index = listReglesZone.getSelectedIndex();
            EvolutionRule rule = listeRulesZone.get(index);
            listModelZone.remove(index);
            listeRulesZone.remove(index);
            if (listModelZone.size() == 0) {
                boutonSupprimerZone.setEnabled(false);
                boutonModifierZone.setEnabled(false);
            } else {
                if (index == listModelZone.getSize()) {
                    index--;
                }
                listReglesZone.setSelectedIndex(index);
                listReglesZone.ensureIndexIsVisible(index);
            }
            configuration.getRules().remove(rule);
            configuration.marshall();
        }
    }
}
