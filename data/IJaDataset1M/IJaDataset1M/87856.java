package org.jdmp.sigmen.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.jdmp.sigmen.client.carte.Carte;
import org.jdmp.sigmen.client.login.PersoCreation;
import org.jdmp.sigmen.client.login.Selection;

public class GlobalPanel extends JLayeredPane {

    /**
	 * 
	 */
    private static final long serialVersionUID = -271671214087591394L;

    private Chat chat;

    private ComposantGlobal composant;

    private JPanel panelDoc;

    private StyledDocument doc;

    public GlobalPanel() {
        super();
        setLayout(new GridLayout(2, 1));
        setBackground(Color.black);
        this.chat = new Chat();
        add(chat, 0);
        panelDoc = new JPanel();
        JTextPane texte = new JTextPane();
        texte.setBackground(Color.BLACK);
        texte.setEnabled(false);
        texte.setDisabledTextColor(Color.WHITE);
        panelDoc.setLayout(new BorderLayout());
        panelDoc.add(texte);
        add(panelDoc, 0);
        doc = texte.getStyledDocument();
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style styleRef = doc.addStyle("def", defaultStyle);
        StyleConstants.setAlignment(styleRef, StyleConstants.ALIGN_RIGHT);
        doc.setLogicalStyle(0, doc.getStyle("def"));
    }

    public void returnToSelection() {
        afficherTexte();
        println("Attente des informations du serveur...");
        composant = new Selection();
        ((Selection) composant).rechercher();
    }

    public void creationPerso() {
        setComposant(new PersoCreation());
    }

    public void setComposant(ComposantGlobal composant) {
        this.composant = composant;
        afficherComposant();
    }

    public void afficherComposant() {
        try {
            this.remove(0);
        } catch (IndexOutOfBoundsException e) {
        }
        add(composant, 0);
        if (composant instanceof Carte && getComponent(1) == chat) {
            JPanel jp = new JPanel();
            jp.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridheight = 1;
            c.gridwidth = 1;
            c.weightx = 1;
            c.weighty = 1;
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            c.ipadx = 0;
            c.ipady = 0;
            chat.setPreferredSize(new Dimension(0, 0));
            jp.add(chat, c);
            c.gridx = 1;
            c.gridy = 0;
            c.gridheight = 1;
            c.gridwidth = 1;
            c.weightx = 1;
            c.weighty = 1;
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            c.ipadx = 0;
            c.ipady = 0;
            ((Carte) composant).getMenu().setPreferredSize(new Dimension(0, 0));
            jp.add(((Carte) composant).getMenu(), c);
            add(jp);
        } else if (getComponent(1) != chat && !(composant instanceof Carte)) {
            remove(1);
            add(chat);
        }
        revalidate();
    }

    public void afficherTexte() {
        erase();
        try {
            this.remove(0);
        } catch (IndexOutOfBoundsException e) {
        }
        add(panelDoc, 0);
        if (getComponent(1) != chat) {
            remove(1);
            add(chat);
        }
    }

    public ComposantGlobal getComponent() {
        return composant;
    }

    public Chat getChat() {
        return chat;
    }

    public void print(String str, Style style) {
        try {
            doc.insertString(doc.getLength(), str, style);
        } catch (BadLocationException e) {
            Main.erreur("Erreur d'affichage de texte.", e);
        }
    }

    public void println(String str, Style style) {
        print(str + "\n", style);
    }

    public void print(String str, String style) {
        print(str, doc.getStyle("style"));
    }

    public void println(String str, String style) {
        print(str + "\n", doc.getStyle("style"));
    }

    public void print(String str) {
        print(str, doc.getStyle("def"));
    }

    public void println(String str) {
        print(str + "\n", doc.getStyle("def"));
    }

    public void erase() {
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException e) {
            Main.erreur("Erreur d'affichage de texte", e);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        float ratio = 17 / 14.f;
        int w = Main.fenetre().getContentPane().getWidth();
        int h = Main.fenetre().getContentPane().getHeight();
        if ((int) (ratio * h) > w) {
            return new Dimension(w, (int) (w / ratio));
        } else {
            return new Dimension((int) (h * ratio), h);
        }
    }

    public void goMap() {
        Carte carte = new Carte();
        composant = carte;
        carte.getMap();
        afficherComposant();
        Main.fenetre().setGlobal();
        revalidate();
    }
}
