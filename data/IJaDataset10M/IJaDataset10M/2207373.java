package fr.kb.frais.vue.action.creation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import fr.kb.frais.control.util.ActionIconAdapter;
import fr.kb.frais.control.util.VerifNombreAdapter;
import fr.kb.frais.modele.object.Trajet;
import fr.kb.frais.vue.components.action.ActionIcon;
import fr.kb.frais.vue.util.Constant;

public class TrajetVueCreation extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private CardLayout cardLayout;

    private JPanel card;

    private JPanel nombre;

    private JPanel trajet;

    private JTextField text;

    private List<Trajet> trajets;

    private Map<Integer, JTextField[]> map;

    private static InternalCreation vue;

    private int nombreTrajet;

    private String action;

    private JLabel verify;

    public TrajetVueCreation() {
    }

    public TrajetVueCreation(InternalCreation v, String action) {
        super();
        vue = v;
        this.action = action;
        buildGUI();
    }

    private void buildGUI() {
        setLayout(new BorderLayout());
        card = new JPanel();
        cardLayout = new CardLayout();
        card.setLayout(cardLayout);
        initNombre();
        initTrajet();
        card.add(nombre, "NOMBRE");
        card.add(trajet, "TRAJET");
        this.verify = new JLabel();
        verify.setForeground(Color.red);
        add(BorderLayout.NORTH, verify);
        add(BorderLayout.CENTER, card);
    }

    public void initTrajet() {
        trajets = new ArrayList<Trajet>();
        trajet = new JPanel(new BorderLayout());
        JPanel trajetPanel = new JPanel(new GridLayout(nombreTrajet, 1));
        map = new HashMap<Integer, JTextField[]>();
        for (int i = 0; i < nombreTrajet; i++) {
            JPanel t = new JPanel(new GridLayout(1, 3));
            JPanel paneDepart = new JPanel();
            JLabel labelAller = new JLabel("D�part : ");
            JTextField aller = new JTextField(7);
            labelAller.setLabelFor(aller);
            paneDepart.add(labelAller);
            paneDepart.add(aller);
            JPanel paneArrivee = new JPanel();
            JLabel labelArrivee = new JLabel("Arriv�e : ");
            JTextField arrivee = new JTextField(7);
            labelArrivee.setLabelFor(arrivee);
            paneArrivee.add(labelArrivee);
            paneArrivee.add(arrivee);
            JPanel paneTarif = new JPanel();
            JLabel labelTarif = new JLabel("Tarif : ");
            JTextField tarif = new JTextField(3);
            labelTarif.setLabelFor(tarif);
            tarif.addKeyListener(new VerifNombreAdapter(tarif));
            paneTarif.add(labelTarif);
            paneTarif.add(tarif);
            map.put(i, new JTextField[] { aller, arrivee, tarif });
            t.add(paneDepart);
            t.add(paneArrivee);
            t.add(paneTarif);
            trajetPanel.add(t);
        }
        ActionIcon suivant = new ActionIcon(Constant.suivant);
        suivant.addMouseListener(new ActionIconAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Map<JTextField, String> verif = verify();
                setVerify(verif, e);
                if (verif.size() == 0) {
                    for (int i = 0; i < nombreTrajet; i++) {
                        JTextField[] tab = map.get(i);
                        String depart = tab[0].getText();
                        String arrivee = tab[1].getText();
                        Double tarif = Double.parseDouble(tab[2].getText());
                        Trajet t = new Trajet(depart, arrivee, tarif);
                        trajets.add(t);
                    }
                    if (vue.getMois().getTrajets() == null) {
                        vue.getMois().setTrajets(trajets);
                    } else {
                        vue.getMois().addAllTrajets(trajets);
                    }
                    if (action == null) {
                        vue.changeVue(InternalCreation.PERSONNE);
                    } else {
                        vue.dispose();
                    }
                }
            }
        });
        ActionIcon precedent = new ActionIcon(Constant.precedent);
        precedent.addMouseListener(new ActionIconAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                showNombre();
            }
        });
        JPanel p1 = new JPanel();
        p1.add(precedent);
        p1.add(suivant);
        trajet.add(BorderLayout.CENTER, trajetPanel);
        trajet.add(BorderLayout.SOUTH, p1);
        card.add(trajet, "TRAJET");
        vue.pack();
    }

    private void initNombre() {
        nombre = new JPanel(new BorderLayout());
        JPanel p = new JPanel();
        text = new JTextField(2);
        text.addKeyListener(new VerifNombreAdapter(text));
        JLabel label = new JLabel("choisir nombre de trajets : ");
        label.setLabelFor(text);
        p.add(label);
        p.add(text);
        ActionIcon suivant = new ActionIcon(Constant.suivant);
        suivant.addMouseListener(new ActionIconAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Map<JTextField, String> result = new HashMap<JTextField, String>();
                setVerify(result, e);
                if (text.getText() != null && text.getText() != "") {
                    try {
                        int avant = nombreTrajet;
                        nombreTrajet = Integer.parseInt(text.getText());
                        if (avant != nombreTrajet) initTrajet();
                        cardLayout.show(card, "TRAJET");
                    } catch (Exception ex) {
                        result.put(text, "Il faut remplir le champ Nombre");
                        setVerify(result, e);
                    }
                } else {
                    result.put(text, "Il faut remplir le champ Nombre");
                    setVerify(result, e);
                }
            }
        });
        JPanel p1 = new JPanel();
        p1.add(suivant);
        nombre.add(BorderLayout.CENTER, p);
        nombre.add(BorderLayout.SOUTH, p1);
        vue.pack();
    }

    private Map<JTextField, String> verify() {
        Map<JTextField, String> verify = new HashMap<JTextField, String>();
        for (int i = 0; i < nombreTrajet; i++) {
            JTextField[] tab = map.get(i);
            String depart = tab[0].getText();
            String arrivee = tab[1].getText();
            if (depart.equals("") || depart == null) {
                verify.put(tab[0], "Il faut remplir le champ Ville de d�part ligne " + (i + 1));
            }
            if (arrivee.equals("") || arrivee == null) {
                verify.put(tab[1], "Il faut remplir le champ Ville d'arriv�e ligne " + (i + 1));
            }
            try {
                Double.parseDouble(tab[2].getText());
            } catch (Exception e) {
                verify.put(tab[2], "Il faut remplir le champ Tarif ligne " + (i + 1));
            }
        }
        return verify;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public void setCardLayout(CardLayout cardLayout) {
        this.cardLayout = cardLayout;
    }

    public JPanel getCard() {
        return card;
    }

    public void setCard(JPanel card) {
        this.card = card;
    }

    public JPanel getNombre() {
        return nombre;
    }

    public void setNombre(JPanel nombre) {
        this.nombre = nombre;
    }

    public JPanel getTrajet() {
        return trajet;
    }

    public void setTrajet(JPanel trajet) {
        this.trajet = trajet;
    }

    public JTextField getText() {
        return text;
    }

    public void setText(JTextField text) {
        this.text = text;
    }

    public List<Trajet> getTrajets() {
        return trajets;
    }

    public void setTrajets(List<Trajet> trajets) {
        this.trajets = trajets;
    }

    public Map<Integer, JTextField[]> getMap() {
        return map;
    }

    public void setMap(Map<Integer, JTextField[]> map) {
        this.map = map;
    }

    public int getNombreTrajet() {
        return nombreTrajet;
    }

    public void setNombreTrajet(int nombreTrajet) {
        this.nombreTrajet = nombreTrajet;
    }

    public static InternalCreation getVue() {
        return vue;
    }

    public static void setVue(InternalCreation vue) {
        TrajetVueCreation.vue = vue;
    }

    public void showTrajet() {
        cardLayout.show(card, "TRAJET");
    }

    public void showNombre() {
        cardLayout.show(card, "NOMBRE");
    }

    public void setVerify(Map<JTextField, String> list, MouseEvent e) {
        if (list.size() > 0) {
            JPopupMenu menu = new JPopupMenu();
            for (JTextField t : list.keySet()) {
                String s = list.get(t);
                final JTextField text = t;
                JMenuItem item = new JMenuItem(s, new ImageIcon(Constant.cheminImage + "attention.png"));
                item.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        text.requestFocus();
                    }
                });
                menu.add(item);
            }
            menu.show((Component) e.getSource(), 0, 0);
            menu.setVisible(true);
        }
    }
}
