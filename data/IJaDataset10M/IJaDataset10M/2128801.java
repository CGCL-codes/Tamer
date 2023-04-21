package org.jdmp.sigmen.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.jdmp.sigmen.messages.Constantes;
import org.jdmp.sigmen.messages.Convert;
import org.jdmp.sigmen.messages.Constantes.Envoi;

public class Chat extends JComponent implements KeyListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3895566306136275268L;

    private JTextField tf;

    private StyledDocument doc;

    private long ping;

    private int pHisto;

    private int pHistoMP;

    private List<String> histo = new ArrayList<String>();

    private List<String> histoMP = new ArrayList<String>();

    private JTextPane texte;

    public Chat() {
        super();
        setLayout(new GridBagLayout());
        tf = new JTextField();
        tf.addKeyListener(this);
        texte = new JTextPane();
        JScrollPane sp = new JScrollPane(texte);
        texte.setEditable(false);
        doc = texte.getStyledDocument();
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        def = doc.addStyle("normal", def);
        def = doc.addStyle("normal_expe", def);
        StyleConstants.setBold(def, true);
        def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        def = doc.addStyle("info", def);
        StyleConstants.setForeground(def, Color.green);
        def = doc.addStyle("infoImportant", def);
        StyleConstants.setBold(def, true);
        def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        def = doc.addStyle("infoIta", def);
        StyleConstants.setForeground(def, Color.green);
        StyleConstants.setItalic(def, true);
        def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        def = doc.addStyle("erreur", def);
        StyleConstants.setForeground(def, Color.red);
        def = doc.addStyle("erreurImportant", def);
        StyleConstants.setBold(def, true);
        def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        def = doc.addStyle("prive", def);
        StyleConstants.setForeground(def, Color.blue);
        def = doc.addStyle("priveImportant", def);
        StyleConstants.setBold(def, true);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 9;
        c.weightx = 100;
        c.weighty = 100;
        c.fill = GridBagConstraints.BOTH;
        add(sp, c);
        c.gridy = 9;
        c.gridheight = 1;
        c.weighty = 0;
        add(tf, c);
    }

    public synchronized void traiterReception(byte[] data) {
        int type = Convert.toInt(data);
        byte[] msg = new byte[data.length - 4];
        System.arraycopy(data, 4, msg, 0, msg.length);
        switch(type) {
            case Constantes.Chat.NORMAL:
                afficherNormal(msg);
                break;
            case Constantes.Chat.INFO:
                afficherInfo(msg);
                break;
            case Constantes.Chat.ERREUR:
                afficherErreur(msg);
                break;
            case Constantes.Chat.ERREUR_SIGNE:
                afficherErreurSignee(msg);
                break;
            case Constantes.Chat.MP:
                afficherPrive(msg);
                break;
            case Constantes.Chat.LISTE_CONNECTES:
                afficherListeConnectes(msg);
                break;
            case Constantes.Chat.UNKNOWN_COMMAND:
                afficherErreurCommande(msg);
                break;
            default:
                Main.erreur("Type de message du serveur inconnu (" + type + ").");
                break;
        }
    }

    public void traiterEnvoi() {
        String msg = tf.getText();
        tf.setText(null);
        pHisto = histo.size();
        if (histo.size() == 0 || !msg.equals(histo.get(histo.size() - 1))) {
            histo.add(msg);
            pHisto++;
        }
        if (msg.equalsIgnoreCase("/ping") || msg.toLowerCase().startsWith("/ping ")) {
            ping = new Date().getTime();
            Main.sender().send(Envoi.PING);
        } else {
            Main.sender().send(Envoi.CHAT, msg.getBytes(Constantes.UTF8));
        }
    }

    public void afficherNormal(byte[] data) {
        int size = Convert.toInt(data);
        byte[] tab = new byte[size];
        System.arraycopy(data, 4, tab, 0, size);
        String nom = new String(tab, Constantes.UTF8);
        tab = new byte[data.length - size - 4];
        System.arraycopy(data, 4 + size, tab, 0, tab.length);
        String message = new String(tab, Constantes.UTF8);
        print(nom, "normal_expe");
        println(" : " + message, "normal");
    }

    public void afficherErreurCommande(byte[] data) {
        print("Erreur : commande inconnue : ", "erreur");
        println(new String(data, Constantes.UTF8), "erreurImportant");
    }

    public void afficherErreur(byte[] data) {
        println(new String(data, Constantes.UTF8), "erreur");
    }

    public void afficherInfo(byte[] data) {
        println(new String(data, Constantes.UTF8), "info");
    }

    public void afficherErreurSignee(byte[] data) {
        int size = Convert.toInt(data);
        byte[] tab = new byte[size];
        System.arraycopy(data, 4, tab, 0, size);
        String nom = new String(tab, Constantes.UTF8);
        tab = new byte[data.length - size - 4];
        System.arraycopy(data, 4 + size, tab, 0, tab.length);
        String message = new String(tab, Constantes.UTF8);
        print(nom, "erreurImportant");
        println(" : " + message, "erreur");
    }

    public void afficherPrive(byte[] data) {
        int size = Convert.toInt(data);
        if (size < 0) {
            print("A ", "prive");
            size = -size;
        } else {
            print("De ", "prive");
        }
        byte[] tab = new byte[size];
        System.arraycopy(data, 4, tab, 0, size);
        String nom = new String(tab, Constantes.UTF8);
        pHistoMP = histoMP.size();
        if (histoMP.size() == 0 || !histoMP.get(histoMP.size() - 1).equals(nom)) {
            histoMP.add(nom);
            pHistoMP++;
        }
        print(nom, "priveImportant");
        tab = new byte[data.length - size - 4];
        System.arraycopy(data, 4 + size, tab, 0, tab.length);
        println(" : " + new String(tab, Constantes.UTF8), "prive");
    }

    public void afficherListeConnectes(byte[] data) {
        int n = Convert.toInt(data);
        byte[] liste = new byte[data.length - 4];
        System.arraycopy(data, 4, liste, 0, liste.length);
        println("Il y a " + n + " utilisateur(s) connecté(s) : " + new String(liste, Constantes.UTF8), "info");
    }

    public void print(String str, Style style) {
        try {
            doc.insertString(doc.getLength(), str, style);
        } catch (BadLocationException e) {
            Main.erreur("Erreur d'affichage de texte.", e);
        }
    }

    public void print(String str, String style) {
        print(str, doc.getStyle(style));
        texte.setCaretPosition(doc.getLength());
    }

    public void println(String str, Style style) {
        print(str + "\n", style);
    }

    public void println(String str, String style) {
        print(str + "\n", style);
    }

    public void ping() {
        ping = new Date().getTime() - ping;
        println("Ping : " + ping + "ms", "info");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                traiterEnvoi();
                break;
            case KeyEvent.VK_UP:
                if (pHisto != 0) {
                    pHisto--;
                    tf.setText(histo.get(pHisto));
                }
                break;
            case KeyEvent.VK_DOWN:
                if (pHisto < histo.size() - 1) {
                    pHisto++;
                    tf.setText(histo.get(pHisto));
                } else if (pHisto == histo.size() - 1) {
                    pHisto++;
                    tf.setText(null);
                }
                break;
            case KeyEvent.VK_PAGE_UP:
                if (pHistoMP != 0) {
                    pHistoMP--;
                    tf.setText("/w " + histoMP.get(pHistoMP));
                }
                break;
            case KeyEvent.VK_PAGE_DOWN:
                if (pHistoMP < histoMP.size() - 1) {
                    pHistoMP++;
                    tf.setText("/w " + histoMP.get(pHistoMP));
                } else if (pHisto == histoMP.size() - 1) {
                    pHistoMP++;
                    tf.setText(null);
                }
                break;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, 0);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
