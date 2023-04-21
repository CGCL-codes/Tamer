package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import utils.Resources;

@SuppressWarnings("serial")
public class JClavier extends JPanel {

    protected JPanel[] jp_ligne;

    protected JTouche[] jb_touches;

    protected JTouche jt_backspace;

    protected JTouche jt_enter;

    protected JTouche jt_specialChar;

    protected JTouche jt_maj, jt_space, jt_dico, jt_options;

    static final String CAR_STANDARD = "\";:.,-'!?@azertyuiopqsdfghjklmwxcvbn";

    static final String CAR_SPECIAL = "<>|789+-()\\°_456*/[]§^~123%={}#&0€$£";

    static final String CAR_ACCENT = "àäâãå█éèëêç█ïî█ñ█ùüû██öõ█ÿ█████æœ███";

    static final int LONGUEUR_LIGNE = CAR_STANDARD.length();

    static final int LIGNES = 4;

    static final Dimension TOUCHES_STANDARD = new Dimension(60, 50);

    /**
	 * Constructeur
	 */
    public JClavier() {
        super(new GridLayout(LIGNES, 1));
        ActionListener action = new Event_JClavier(this);
        this.jp_ligne = new JPanel[LIGNES];
        for (int i = 0; i < LIGNES; i++) this.jp_ligne[i] = new JPanel();
        this.jb_touches = new JTouche[CAR_STANDARD.length()];
        for (int i = 0; i < CAR_STANDARD.length(); i++) this.jb_touches[i] = new JTouche(Character.toString(CAR_STANDARD.charAt(i)), SwingConstants.CENTER, JTouche.TOUCHE_STANDARD);
        this.jt_backspace = new JTouche(Resources.getImageIcon("images/backspace.png", getClass()), SwingConstants.CENTER, JTouche.TOUCHE_ACTION);
        this.jt_enter = new JTouche(Resources.getImageIcon("images/enter.png", getClass()), SwingConstants.CENTER, JTouche.TOUCHE_ACTION);
        this.jt_specialChar = new JTouche(".,:;@+-/", SwingConstants.CENTER, JTouche.TOUCHE_ACTION);
        this.jt_maj = new JTouche(Resources.getImageIcon("images/maj_unlock.png", getClass()), SwingConstants.CENTER, JTouche.TOUCHE_ACTION_WITH_LOCK);
        this.jt_space = new JTouche(" ", SwingConstants.CENTER, JTouche.TOUCHE_ACTION);
        this.jt_dico = new JTouche("Dico", SwingConstants.CENTER, JTouche.TOUCHE_ACTION_WITH_LOCK);
        this.jt_options = new JTouche(Resources.getImageIcon("images/options.png", getClass()), JTouche.TOUCHE_ACTION_WITH_LOCK);
        for (int i = 0; i < CAR_STANDARD.length(); i++) this.jb_touches[i].setPreferredSize(TOUCHES_STANDARD);
        this.jt_backspace.setPreferredSize(new Dimension(110, 50));
        this.jt_enter.setPreferredSize(new Dimension(110, 50));
        this.jt_specialChar.setPreferredSize(new Dimension(110, 50));
        this.jt_maj.setPreferredSize(new Dimension(90, 50));
        this.jt_space.setPreferredSize(new Dimension(120, 50));
        this.jt_dico.setPreferredSize(new Dimension(95, 50));
        this.jt_options.setPreferredSize(new Dimension(50, 50));
        this.jt_maj.addActionListener(action);
        this.jt_specialChar.addActionListener(action);
        for (int i = 0; i < LIGNES; i++) this.add(this.jp_ligne[i]);
        for (int i = 0; i < 10; i++) this.jp_ligne[0].add(this.jb_touches[i]);
        this.jp_ligne[0].add(this.jt_backspace);
        for (int i = 10; i < 20; i++) this.jp_ligne[1].add(this.jb_touches[i]);
        this.jp_ligne[1].add(this.jt_enter);
        for (int i = 20; i < 30; i++) this.jp_ligne[2].add(this.jb_touches[i]);
        this.jp_ligne[2].add(this.jt_specialChar);
        this.jp_ligne[3].add(this.jt_maj);
        for (int i = 30; i < 36; i++) this.jp_ligne[3].add(this.jb_touches[i]);
        this.jp_ligne[3].add(this.jt_space);
        this.jp_ligne[3].add(this.jt_dico);
        this.jp_ligne[3].add(this.jt_options);
    }
}

class Event_JClavier implements ActionListener {

    JClavier jp;

    /**
	 * Constructeur
	 * @param jp le clavier 
	 */
    public Event_JClavier(JClavier jp) {
        this.jp = jp;
    }

    /**
	 * Appelé lors de l'appui simple sur une touche
	 * Affiche le caractère ou traite la touche spéciale
	 */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        Object obj = arg0.getSource();
        if (obj instanceof JTouche) {
            JTouche jt = (JTouche) obj;
            if (jt == jp.jt_maj) {
                if (jp.jt_maj.isEnfonce()) {
                    for (int i = 0; i < JClavier.LONGUEUR_LIGNE; i++) jp.jb_touches[i].setText(jp.jb_touches[i].getText().toUpperCase());
                    if (jp.jt_maj.isLock()) jp.jt_maj.setIcon(Resources.getImageIcon("images/maj_lock.png", getClass())); else jp.jt_maj.setIcon(Resources.getImageIcon("images/maj_unlock.png", getClass()));
                } else {
                    for (int i = 0; i < JClavier.LONGUEUR_LIGNE; i++) jp.jb_touches[i].setText(jp.jb_touches[i].getText().toLowerCase());
                    jp.jt_maj.setIcon(Resources.getImageIcon("images/maj_unlock.png", getClass()));
                }
            } else if (jt == jp.jt_specialChar) {
                if (jp.jt_specialChar.getText().contains("abcd")) {
                    if (jp.jt_maj.isEnfonce()) for (int i = 0; i < JClavier.LONGUEUR_LIGNE; i++) jp.jb_touches[i].setText(Character.toString(JClavier.CAR_STANDARD.toUpperCase().charAt(i))); else for (int i = 0; i < JClavier.LONGUEUR_LIGNE; i++) jp.jb_touches[i].setText(Character.toString(JClavier.CAR_STANDARD.toLowerCase().charAt(i)));
                    for (int i = 0; i < JClavier.LONGUEUR_LIGNE; i++) jp.jb_touches[i].setEnabled(true);
                    jp.jt_specialChar.setText(".,:;@+-/");
                } else if (jp.jt_specialChar.getText().contains("@")) {
                    if (jp.jt_maj.isEnfonce()) for (int i = 0; i < JClavier.LONGUEUR_LIGNE; i++) jp.jb_touches[i].setText(Character.toString(JClavier.CAR_SPECIAL.toUpperCase().charAt(i))); else for (int i = 0; i < JClavier.LONGUEUR_LIGNE; i++) jp.jb_touches[i].setText(Character.toString(JClavier.CAR_SPECIAL.toLowerCase().charAt(i)));
                    for (int i = 0; i < JClavier.LONGUEUR_LIGNE; i++) jp.jb_touches[i].setEnabled(true);
                    jp.jt_specialChar.setText("éèàù");
                } else {
                    if (jp.jt_maj.isEnfonce()) for (int i = 0; i < JClavier.LONGUEUR_LIGNE; i++) jp.jb_touches[i].setText(Character.toString(JClavier.CAR_ACCENT.toUpperCase().charAt(i))); else for (int i = 0; i < JClavier.LONGUEUR_LIGNE; i++) jp.jb_touches[i].setText(Character.toString(JClavier.CAR_ACCENT.toLowerCase().charAt(i)));
                    for (int i = 0; i < JClavier.LONGUEUR_LIGNE; i++) if (jp.jb_touches[i].getText().equals("█")) jp.jb_touches[i].setEnabled(false);
                    jp.jt_specialChar.setText("abcd");
                }
            }
        }
    }
}
