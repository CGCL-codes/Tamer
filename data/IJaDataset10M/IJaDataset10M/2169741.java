package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import outils.regles.Regle;
import outils.regles.RegleEnsemble;
import outils.regles.RegleLocution;
import outils.regles.RegleSigne;
import outils.regles.RegleSymbole;
import outils.regles.RulesToolKit;
import nat.ConfigNat;
import net.sf.saxon.Controller;
import net.sf.saxon.event.Emitter;
import net.sf.saxon.event.MessageEmitter;

/**
 * Fenêtre de sélection et de visualisation des règles d'abrégé.
 * @author bruno
 * @since 2.0
 * @see outils.HyphenationToolkit
 */
public class ConfAbrege extends JFrame implements ActionListener, ItemListener {

    /** Textual contents */
    static Language texts = new Language("ConfAbrege");

    /** Pour la sérialisation (non utilisé) */
    private static final long serialVersionUID = 1L;

    /** JTable contenant les règles */
    private JTable table;

    /** Modèle pour la table */
    private TableModeleAbr tm;

    /** ScrollPane de la table*/
    private JScrollPane jsp;

    /** Label contenant l'adresse et la description du dictionnaire */
    private JLabel lDico;

    /** adresse du dictionnaire */
    private String dicoName;

    /** bouton pour charger un nouveau dictionnaire */
    private JButton btOuvrir = new JButton(texts.getText("open"), new ImageIcon("ui/icon/document-open.png"));

    /** bouton pour créer un nouveau dictionnaire vierge*/
    private JButton btNouveau = new JButton(texts.getText("new"), new ImageIcon("ui/icon/document-new.png"));

    /** bouton lançant la vérification le test de coupure sur le contenu de {@link #jtfTest}*/
    private JButton btVerif = new JButton(texts.getText("test"), new ImageIcon("ui/icon/applications-development.png"));

    /** bouton enregistrant le dictionnaire*/
    private JButton btEnregistrer = new JButton(texts.getText("save"), new ImageIcon("ui/icon/document-save.png"));

    /** bouton enregistrant le dictionnaire sous un nouveau nom*/
    private JButton btEnregistrerSous = new JButton(texts.getText("saveas"), new ImageIcon("ui/icon/document-save-as.png"));

    /** bouton annulant les changements et fermant la fenêtre*/
    private JButton btAnnuler = new JButton(texts.getText("cancel"), new ImageIcon("ui/icon/exit.png"));

    /** bouton enregistrant le dictionnaire sous un nouveau nom*/
    private JButton btReference = new JButton(texts.getText("ref"), new ImageIcon("ui/icon/accessories-dictionary.png"));

    private JButton btDeselect = new JButton(texts.getText("unselect"), new ImageIcon("ui/icon/check.png"));

    /** JTextField contenant la chaine à tester */
    private JTextField jtfTest = new JTextField(17);

    /** JTextArea contenant le déroulement du test de coupure */
    private JTextArea jtaReponse = new JTextArea(10, 35);

    /** Label pour {@link #jtaReponse}*/
    private JLabel lJtaReponse = new JLabel(texts.getText("detailslabel"));

    /** JTextArea contenant le résultat du test de coupure */
    private JTextField jtfReponse = new JTextField(17);

    /** JLabel aide à la saisie*/
    private JLabel lAide = new JLabel(texts.getText("help"));

    /** JLabel pour le */
    private JLabel lJcbRegles = new JLabel(texts.getText("ruleslabel"));

    /** JCombobox de filtre sur les règles */
    private JComboBox jcbRegles = new JComboBox();

    /** JCheckBox affichant les détails du test de coupure */
    private JCheckBox jcbDetailCoup = new JCheckBox(texts.getText("detail"));

    /** indique si des modifications n'ont pas été enregistrées*/
    private boolean modif = false;

    /** Tableau des ponctuations possibles en fin de mot */
    protected String[] ponctuationFin = { "”", "’", ",", ".", ":", ";", "!", "?", "»", "…", ")", "]", "}", "\"", "*" };

    /** TAbleau des ponctuations possibles en début de mot */
    protected String[] ponctuationDebut = { "¡", "¿", "«", "“", "‘", "(", "[", "{", "\"", "*" };

    /** caractère délimiteur de mots */
    protected char espace = ' ';

    /**donnees des règles */
    private ArrayList<ArrayList<Object>> donnees = new ArrayList<ArrayList<Object>>();

    /** 
	 * Constructeur 
	 * <p>Initialise les éléments d'interface graphique et construit la page</p>
	 * <p>Récupère notamment les règles d'abrégé en utilisant {@link outils.regles.RulesToolKit#getRules(String)}
	 */
    public ConfAbrege() {
        super(texts.getText("rulesselection"));
        if (!(new File(ConfigNat.getCurrentConfig().getRulesFrG2()).exists())) {
            JOptionPane.showMessageDialog(this, texts.getText("bugmsg1") + texts.getText("bugmsg2") + ConfigNat.getCurrentConfig().getDicoCoup() + " " + texts.getText("bugmsg3") + texts.getText("bugmsg4"), texts.getText("bugmsg5"), JOptionPane.ERROR_MESSAGE);
            donnees = creerDonneesTable(new File(ConfigNat.getCurrentConfig().getRulesFrG2Perso()).toURI().toString());
        } else {
            donnees = creerDonneesTable(new File(ConfigNat.getCurrentConfig().getRulesFrG2Perso()).toURI().toString());
        }
        tm = new TableModeleAbr(donnees);
        table = new JTable(tm);
        table.setAutoCreateRowSorter(true);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        TableColumnModel modelesColonnes = table.getColumnModel();
        TableColumn modelColonne = modelesColonnes.getColumn(0);
        modelColonne.setMinWidth(200);
        modelColonne = modelesColonnes.getColumn(1);
        modelColonne.setMaxWidth(50);
        modelColonne.setMinWidth(50);
        table.setDefaultRenderer(Regle.class, new BrailleTableCellRenderer());
        Context cTable = new Context("", "Table", "table", texts);
        new ContextualHelp(table, cTable);
        jsp = new JScrollPane(table);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setMinimumSize(new Dimension(500, 400));
        dicoName = ConfigNat.getCurrentConfig().getRulesFrG2Perso();
        String commentaire = "";
        if (ConfigNat.getCurrentConfig().getIsSysConfig()) {
            commentaire = texts.getText("dictlabel1");
        }
        lDico = new JLabel(texts.getText("dictlabel2") + dicoName + "</i> " + commentaire + "</h3></html>");
        Font fBraille;
        try {
            fBraille = Font.createFont(Font.PLAIN, new File("ui/fontes/DejaVuSans.ttf"));
            fBraille = fBraille.deriveFont(Font.PLAIN, 12);
        } catch (Exception e) {
            System.err.println(texts.getText("unknownpolice"));
            fBraille = new Font("DejaVu Sans", Font.PLAIN, 12);
        }
        JLabel lJtfTest = new JLabel(texts.getText("word"));
        lJtfTest.setLabelFor(jtfTest);
        lJtfTest.setDisplayedMnemonic('m');
        lJtfTest.setDisplayedMnemonicIndex(0);
        Context cjtfTest = new Context("m", "TextField", "word", texts);
        new ContextualHelp(jtfTest, cjtfTest);
        JLabel lReponse = new JLabel(texts.getText("answerlabel"));
        lReponse.setLabelFor(jtfReponse);
        lReponse.setDisplayedMnemonic('r');
        lJtfTest.setDisplayedMnemonicIndex(0);
        Context cjtfReponse = new Context("r", "TextField", "answer", texts);
        new ContextualHelp(jtfReponse, cjtfReponse);
        jtfReponse.setFont(fBraille);
        JScrollPane jsPres = new JScrollPane(jtaReponse);
        jsPres.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsPres.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jcbDetailCoup.addItemListener(this);
        jcbDetailCoup.setMnemonic('d');
        Context cjcbDetailCoup = new Context("d", "TextField", "detail", texts);
        new ContextualHelp(jcbDetailCoup, cjcbDetailCoup);
        btVerif.addActionListener(this);
        jcbDetailCoup.setMnemonic('d');
        Context cbtVerif = new Context("t", "Button", "test", texts);
        new ContextualHelp(btVerif, cbtVerif);
        btVerif.setMnemonic('t');
        lJtaReponse.setDisplayedMnemonic('l');
        lJtaReponse.setLabelFor(jtaReponse);
        Context cjtaReponse = new Context("l", "TextArea", "details", texts);
        new ContextualHelp(jtaReponse, cjtaReponse);
        jtaReponse.setEditable(false);
        jtaReponse.setFont(fBraille);
        jtfReponse.setEditable(false);
        lJtaReponse.setEnabled(false);
        btDeselect.addActionListener(this);
        Context cbtDeselect = new Context("o", "Button", "unselect", texts);
        new ContextualHelp(btDeselect, cbtDeselect);
        btDeselect.setMnemonic('o');
        btOuvrir.addActionListener(this);
        Context cbtOuvrir = new Context("c", "Button", "open", texts);
        new ContextualHelp(btOuvrir, cbtOuvrir);
        btOuvrir.setMnemonic('c');
        btNouveau.addActionListener(this);
        Context cbtNouveau = new Context("v", "Button", "new", texts);
        new ContextualHelp(btNouveau, cbtNouveau);
        btNouveau.setMnemonic('v');
        btEnregistrer.addActionListener(this);
        Context cbtEnregistrer = new Context("s", "Button", "save", texts);
        new ContextualHelp(btEnregistrer, cbtEnregistrer);
        btEnregistrer.setMnemonic('s');
        btReference.addActionListener(this);
        Context cbtReference = new Context("é", "Button", "ref", texts);
        new ContextualHelp(btReference, cbtReference);
        btReference.setMnemonic('é');
        if (ConfigNat.getCurrentConfig().getRulesFrG2Perso().equals(ConfigNat.getCurrentConfig().getRulesFrG2())) {
            btReference.setEnabled(false);
        }
        if (ConfigNat.getCurrentConfig().getIsSysConfig()) {
            btEnregistrer.setEnabled(false);
            btEnregistrerSous.setEnabled(false);
        }
        if (new File(dicoName).equals(new File(ConfigNat.getCurrentConfig().getRulesFrG2()))) {
            btEnregistrer.setEnabled(false);
        }
        btEnregistrerSous.addActionListener(this);
        Context cbtEnregistrerSous = new Context("n", "Button", "saveas", texts);
        new ContextualHelp(btEnregistrerSous, cbtEnregistrerSous);
        btEnregistrerSous.setMnemonic('n');
        btAnnuler.addActionListener(this);
        Context cbtAnnuler = new Context("q", "Button", "cancel", texts);
        new ContextualHelp(btAnnuler, cbtAnnuler);
        btAnnuler.setMnemonic('q');
        btAnnuler.setDisplayedMnemonicIndex(0);
        lJcbRegles.setDisplayedMnemonic('g');
        lJcbRegles.setLabelFor(jcbRegles);
        Context cJcbRegles = new Context("", "ComboBox", "rules", texts);
        String[] rulesList = new String[] { texts.getText("rulesall"), texts.getText("rulesloc"), texts.getText("rulessigns"), texts.getText("rulessymb"), texts.getText("rulessignrules"), texts.getText("rulessymbrules"), texts.getText("rulesgenrules"), texts.getText("rulesgeneralcaserules") };
        jcbRegles = new JComboBox(rulesList);
        new ContextualHelp(jcbRegles, cJcbRegles);
        jcbRegles.addItemListener(this);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 3, 3);
        GridBagLayout gblTest = new GridBagLayout();
        JPanel pTest = new JPanel(gblTest);
        pTest.setBorder(BorderFactory.createLineBorder(Color.gray));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gblTest.setConstraints(lJtfTest, gbc);
        pTest.add(lJtfTest);
        gbc.gridx++;
        gblTest.setConstraints(jtfTest, gbc);
        pTest.add(jtfTest);
        gbc.gridx++;
        gblTest.setConstraints(btVerif, gbc);
        pTest.add(btVerif);
        gbc.gridx++;
        gbc.insets = new Insets(3, 30, 3, 3);
        gblTest.setConstraints(lJtaReponse, gbc);
        pTest.add(lJtaReponse);
        gbc.gridy++;
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.gridheight = 3;
        gblTest.setConstraints(jsPres, gbc);
        pTest.add(jsPres);
        gbc.gridx = 0;
        gbc.gridheight = 1;
        gblTest.setConstraints(lReponse, gbc);
        pTest.add(lReponse);
        gbc.gridx++;
        gblTest.setConstraints(jtfReponse, gbc);
        pTest.add(jtfReponse);
        gbc.gridx++;
        gblTest.setConstraints(jcbDetailCoup, gbc);
        pTest.add(jcbDetailCoup);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        gblTest.setConstraints(lAide, gbc);
        pTest.add(lAide);
        gbc.gridy++;
        gbc.gridwidth = 1;
        gblTest.setConstraints(lJcbRegles, gbc);
        pTest.add(lJcbRegles);
        gbc.gridx++;
        gblTest.setConstraints(jcbRegles, gbc);
        pTest.add(jcbRegles);
        gbc.gridx++;
        gblTest.setConstraints(btDeselect, gbc);
        pTest.add(btDeselect);
        JPanel cp = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        cp.setLayout(gbl);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbl.setConstraints(lDico, gbc);
        gbc.gridwidth = 3;
        gbc.gridy++;
        gbl.setConstraints(pTest, gbc);
        gbc.gridy++;
        gbc.gridwidth = 4;
        gbl.setConstraints(jsp, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy++;
        JPanel pBoutons = new JPanel();
        GridBagLayout gblBt = new GridBagLayout();
        pBoutons.setLayout(gblBt);
        gbl.setConstraints(pBoutons, gbc);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 3, 3, 3);
        gblBt.setConstraints(btEnregistrer, gbc);
        gbc.gridx++;
        gblBt.setConstraints(btEnregistrerSous, gbc);
        gbc.insets = new Insets(10, 150, 3, 3);
        gbc.gridx++;
        gblBt.setConstraints(btAnnuler, gbc);
        JPanel pBtAction = new JPanel(new GridBagLayout());
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 3, 3, 3);
        pBtAction.add(btOuvrir, gbc);
        gbc.gridy++;
        pBtAction.add(btNouveau, gbc);
        gbc.gridy++;
        pBtAction.add(btReference, gbc);
        pBoutons.add(btEnregistrer);
        pBoutons.add(btEnregistrerSous);
        pBoutons.add(btAnnuler);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cp.add(lDico, gbc);
        gbc.gridy++;
        cp.add(pTest, gbc);
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 3;
        cp.add(jsp, gbc);
        gbc.gridx += 2;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        cp.add(pBtAction, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        cp.add(pBoutons, gbc);
        JScrollPane scrollRes = new JScrollPane(cp);
        scrollRes.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollRes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollRes);
        pack();
        Dimension dimEcran = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(getWidth(), dimEcran.width), Math.min(getHeight(), dimEcran.height));
        setVisible(true);
    }

    /**
	 * Crée les données pour la JTable {@link #table} contenues dans le 
	 * fichier dictionnaire de règles <code>dico</code> à partir du fichier de règles de référence
	 * @param dico adresse du dictionnaire de règle à charger
	 * @return les données pour la table {@link #table}
	 * @see outils.HyphenationToolkit#getRules(String)
	 */
    private ArrayList<ArrayList<Object>> creerDonneesTable(String dico) {
        ArrayList<Regle> liste = RulesToolKit.getRules(dico);
        ArrayList<Regle> listeRef = RulesToolKit.getRules(ConfigNat.getCurrentConfig().getRulesFrG2());
        ArrayList<ArrayList<Object>> d = new ArrayList<ArrayList<Object>>();
        for (int i = 0; i < listeRef.size(); i++) {
            Regle regle = listeRef.get(i);
            Boolean actif = new Boolean(true);
            try {
                actif = new Boolean(liste.contains(regle)) && liste.get(listeRef.indexOf(regle)).isActif();
            } catch (IndexOutOfBoundsException iobe) {
                liste.add(regle);
                regle.setActif(true);
            }
            ArrayList<Object> a = new ArrayList<Object>();
            a.add(regle);
            a.add(actif);
            d.add(a);
        }
        return d;
    }

    /**
	 * Réalise l'abréviation de la chaine <code>mot</code> en utilisant
	 * la configuration courante d'abrégé
	 * @param mot la chaine à abréger
	 */
    private void verifie(String mot) {
        boolean ok = true;
        if (modif) {
            ok = JOptionPane.showConfirmDialog(this, texts.getText("unsaved1") + texts.getText("unsaved2") + texts.getText("unsaved3"), texts.getText("unsaved3"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION;
        }
        if (ok) {
            try {
                DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
                DocumentBuilder constructeur = fabrique.newDocumentBuilder();
                Document doc = constructeur.newDocument();
                doc.setXmlVersion("1.1");
                doc.setXmlStandalone(true);
                ligneLit(mot, doc);
                TransformerFactory transformFactory = TransformerFactory.newInstance();
                StreamSource styleSource = new StreamSource(new File(ConfigNat.getCurrentConfig().getXSL()));
                Transformer transform = transformFactory.newTransformer(styleSource);
                transform.setParameter("debug", jcbDetailCoup.isSelected());
                transform.setParameter("dbg", jcbDetailCoup.isSelected());
                transform.setOutputProperty(OutputKeys.METHOD, "text");
                DOMSource in = new DOMSource(doc);
                StringWriter swResu = new StringWriter();
                StreamResult out = new StreamResult(swResu);
                Controller control = (Controller) transform;
                control.setMessageEmitter(new MessageEmitter());
                Emitter mesgEm = (Emitter) control.getMessageEmitter();
                StringWriter swMesg = new StringWriter();
                mesgEm.setWriter(swMesg);
                transform.transform(in, out);
                if (jcbDetailCoup.isSelected()) {
                    jtaReponse.setText(swMesg.getBuffer().toString());
                }
                jtfReponse.setText(swResu.getBuffer().toString());
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Convertit une ligne littéraire au format interne
	 * Ajoute l'élément racine créé au document
	 * @param doc Le document xml à construire 
	 * @param ligne la ligne à convertir
	 */
    private void ligneLit(String ligne, Document doc) {
        Element lit = doc.createElement("lit");
        Element d = doc.createElement("doc");
        Element phr = doc.createElement("phrase");
        int i = 0;
        int j = 0;
        String[] mots = null;
        ligne = ligne.replace("...", "…");
        ligne = ligne.replace("\t", "" + espace);
        ligne = ligne.replace("\n", "");
        ligne = ligne.replace(" ", "" + espace);
        if (ligne != null && ligne.length() > 0) {
            mots = ligne.split("" + espace);
            if (mots.length == 0) {
                mots = new String[1];
                mots[0] = ligne;
            }
        }
        if ((mots != null) && !(mots.length == 1 && mots[0] == "" + espace)) {
            while (i < mots.length) {
                boolean suivant = false;
                if (mots[i].equals("-")) {
                    Element e = doc.createElement(texts.getText("punctuation"));
                    e.setTextContent("-");
                    lit.appendChild(e);
                    suivant = true;
                } else {
                    j = 0;
                    while (j < ponctuationDebut.length) {
                        if (mots[i].startsWith(ponctuationDebut[j]) || mots[i] == ponctuationDebut[j]) {
                            Element e = doc.createElement(texts.getText("punctuation"));
                            e.setTextContent("" + mots[i].charAt(0));
                            lit.appendChild(e);
                            if (mots[i].length() > 1) {
                                mots[i] = mots[i].substring(1, mots[i].length());
                                j = 0;
                            } else {
                                suivant = true;
                            }
                        }
                        j++;
                    }
                }
                j = 0;
                if (!suivant) {
                    ArrayList<Element> ponctfin = new ArrayList<Element>();
                    while (j < ponctuationFin.length) {
                        if (mots[i].endsWith(ponctuationFin[j]) || mots[i] == ponctuationFin[j]) {
                            Element e = doc.createElement(texts.getText("punctuation"));
                            e.setTextContent("" + mots[i].charAt(mots[i].length() - 1));
                            ponctfin.add(e);
                            mots[i] = mots[i].substring(0, mots[i].length() - 1);
                            j = 0;
                        } else {
                            j++;
                        }
                    }
                    Element m = doc.createElement(texts.getText("word"));
                    m.setTextContent(mots[i].replace("&", "&amp;").replace("<", "&lt;"));
                    lit.appendChild(m);
                    int nbPonct = ponctfin.size();
                    for (j = nbPonct - 1; j >= 0; j--) {
                        lit.appendChild(ponctfin.get(j));
                    }
                }
                i++;
            }
            i = 0;
            phr.appendChild(lit);
            d.appendChild(phr);
            doc.appendChild(d);
        }
    }

    /**
	 * ferme la fenêtre
	 * <p>Réalise des tests et intéragit avec l'utilisateur 
	 * pour valider les modifs/changements de fichier</p> 
	 */
    private void quitter() {
        if (modif) {
            boolean sauv = JOptionPane.showConfirmDialog(this, texts.getText("savemsg1"), texts.getText("savemsg2"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION;
            if (sauv) {
                if (dicoName.equals("")) {
                    enregistrerSous();
                } else {
                    enregistrer();
                }
            }
        }
        if (!modif && !dicoName.equals(ConfigNat.getCurrentConfig().getRulesFrG2Perso())) {
            boolean change = JOptionPane.showConfirmDialog(this, texts.getText("changemsg1"), texts.getText("changemsg2"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION;
            if (change) {
                ConfigNat.getCurrentConfig().setRulesFrG2Perso(dicoName);
            }
            ConfigNat.getCurrentConfig().setRulesFrG2Perso(ConfigNat.getCurrentConfig().getRulesFrG2Perso());
        }
        RulesToolKit.writeRules(RulesToolKit.getRules(new File(ConfigNat.getCurrentConfig().getRulesFrG2Perso()).toURI().toString()));
        dispose();
        System.err.println(new File(ConfigNat.getCurrentConfig().getRulesFrG2Perso()).toURI().toString());
    }

    /**
	 * Charge un nouveau dictionnaire
	 *
	 */
    private void chargerDico() {
        JFileChooser jfc = new JFileChooser();
        FiltreFichier ff = new FiltreFichier(new String[] { "xml" }, texts.getText("filterfile"));
        jfc.addChoosableFileFilter(ff);
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setFileFilter(ff);
        File f = new File("./xsl/dicts/");
        jfc.setCurrentDirectory(f);
        jfc.setApproveButtonText(texts.getText("chosefilebutton"));
        jfc.setDialogTitle(texts.getText("selectfilemsg"));
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            donnees = creerDonneesTable(jfc.getSelectedFile().toURI().toString());
            tm.setDataVector(donnees);
            majTable();
            lDico.setText(texts.getText("dictmsg1") + jfc.getSelectedFile().getAbsolutePath() + " " + texts.getText("dictmsg2"));
            dicoName = jfc.getSelectedFile().getAbsolutePath();
            setModif(false);
        }
        if (new File(dicoName).equals(new File(ConfigNat.getCurrentConfig().getRulesFrG2())) || ConfigNat.getCurrentConfig().getIsSysConfig()) {
            btEnregistrer.setEnabled(false);
        } else {
            btEnregistrer.setEnabled(true);
        }
    }

    /**
	 * Enregistre le fichier de règles à une nouvelle adresse et le charge dans NAT
	 * @return faux si pb lors de l'enregistrement
	 */
    private boolean enregistrerSous() {
        boolean retour = true;
        JFileChooser jfc = new JFileChooser();
        FiltreFichier ff = new FiltreFichier(new String[] { "xml" }, texts.getText("filterfile"));
        jfc.addChoosableFileFilter(ff);
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setFileFilter(ff);
        File f = new File(ConfigNat.getUserTempFolder() + texts.getText("rules"));
        jfc.setCurrentDirectory(f);
        jfc.setApproveButtonText(texts.getText("chosefilebutton"));
        jfc.setDialogTitle(texts.getText("selectfilemsg"));
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            dicoName = jfc.getSelectedFile().getAbsolutePath();
            if (!dicoName.endsWith(".xml")) {
                dicoName += ".xml";
            }
            lDico.setText(texts.getText("dictmsg1") + dicoName + " " + texts.getText("dictmsg2"));
            retour = enregistrer();
            if (retour) {
                btEnregistrer.setEnabled(true);
            }
        }
        return retour;
    }

    /**
	 * Enregistre le dictionnaire et le charge dans NAT
	 * @return faux si pb lors de l'enregistrement
	 */
    private boolean enregistrer() {
        boolean retour = true;
        ArrayList<Regle> liste = new ArrayList<Regle>();
        for (int i = 0; i < donnees.size(); i++) {
            Regle r = ((Regle) donnees.get(i).get(0));
            r.setActif(((Boolean) (donnees.get(i).get(1))).booleanValue());
            liste.add((Regle) donnees.get(i).get(0));
        }
        if (!RulesToolKit.saveRuleFile(liste, dicoName)) {
            JOptionPane.showMessageDialog(this, texts.getText("cantsave1"), texts.getText("cantsave2"), JOptionPane.ERROR_MESSAGE);
            retour = false;
        } else {
            setModif(false);
        }
        return retour;
    }

    /**
	 * Charge un dictionnaire vierge
	 */
    private void nouveauDico() {
        donnees = creerDonneesTable(ConfigNat.getCurrentConfig().getRulesFrG2());
        tm.setDataVector(donnees);
        lDico.setText(texts.getText("dictmsg1") + texts.getText("dictmsg2") + " " + texts.getText("dictmsg3"));
        dicoName = "";
        majTable();
        btEnregistrer.setEnabled(false);
        setModif(true);
    }

    /**
	 * Charge le fichier de référence
	 */
    private void chargeReference() {
        nouveauDico();
        dicoName = ConfigNat.getCurrentConfig().getRulesFrG2();
        ConfigNat.getCurrentConfig().setRulesFrG2Perso(dicoName);
        setModif(false);
        String commentaire = " " + texts.getText("dictlabel1");
        lDico.setText(texts.getText("dictmsg1") + "<i>" + dicoName + "</i>" + commentaire + "</h3></html>");
    }

    /**
	 * Méthode redéfinie de ActionListener
	 * <p>Gère les actions sur les boutons</p>
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btVerif) {
            verifie(jtfTest.getText());
        } else if (ae.getSource() == btOuvrir) {
            chargerDico();
        } else if (ae.getSource() == btNouveau) {
            nouveauDico();
        } else if (ae.getSource() == btEnregistrer) {
            enregistrer();
        } else if (ae.getSource() == btEnregistrerSous) {
            enregistrerSous();
        } else if (ae.getSource() == btReference) {
            chargeReference();
        } else if (ae.getSource() == btAnnuler) {
            quitter();
        } else if (ae.getSource() == btDeselect) {
            decocher();
        }
    }

    /**
	 * Décoche toutes les règles affichées
	 */
    private void decocher() {
        for (ArrayList<Object> al : tm.getArrayListOfData()) {
            al.set(1, new Boolean(false));
        }
        table.updateUI();
    }

    /**
	 * Rédéfinie de ItemListener 
	 * <p>efface le panneau de détail si {@link #jcbDetailCoup} est dessélectionné 
	 * et désactive {@link #lJtaReponse}</p>
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
    public void itemStateChanged(ItemEvent ie) {
        if (ie.getSource() == jcbDetailCoup) {
            if (!jcbDetailCoup.isSelected()) {
                jtaReponse.setText("");
                lJtaReponse.setEnabled(false);
            } else {
                lJtaReponse.setEnabled(true);
            }
        } else if (ie.getSource() == jcbRegles) {
            majTable();
        }
    }

    /**
	 * Mets à jour la table en filtrant les données
	 */
    private void majTable() {
        ArrayList<ArrayList<Object>> liste = new ArrayList<ArrayList<Object>>();
        for (int i = 0; i < donnees.size(); i++) {
            ArrayList<Object> ligne = donnees.get(i);
            if (jcbRegles.getSelectedItem().equals(texts.getText("rulesall"))) {
                liste.add(ligne);
            } else if (ligne.get(0) instanceof RegleLocution && jcbRegles.getSelectedItem().equals(texts.getText("rulesloc"))) {
                liste.add(ligne);
            } else if (ligne.get(0) instanceof RegleSigne && jcbRegles.getSelectedItem().equals(texts.getText("rulessigns"))) {
                liste.add(ligne);
            } else if (ligne.get(0) instanceof RegleSymbole && jcbRegles.getSelectedItem().equals(texts.getText("rulessymb"))) {
                liste.add(ligne);
            } else if (ligne.get(0) instanceof RegleEnsemble) {
                RegleEnsemble r = (RegleEnsemble) ligne.get(0);
                if (jcbRegles.getSelectedItem().equals(texts.getText("rulessignrules")) && r.isFor(RulesToolKit.SIGNE) || jcbRegles.getSelectedItem().equals(texts.getText("rulessymbrules")) && r.isFor(RulesToolKit.SYMBOLE) || jcbRegles.getSelectedItem().equals(texts.getText("rulesgenrules")) && r.isFor(RulesToolKit.ALL) || jcbRegles.getSelectedItem().equals(texts.getText("rulesgeneralcaserules")) && r.isFor(RulesToolKit.GENERAL)) {
                    liste.add(ligne);
                }
            }
        }
        tm.setArrayListOfData(liste);
        table.updateUI();
    }

    /**
	 * Méthode d'accès en écriture à {@link #modif}
	 * @param m valeur pour modif
	 */
    public void setModif(boolean m) {
        modif = m;
    }

    /**
	 * Classe interne décrivant le modèle de JTable utilisé pour {@link ConfDictCoup}
	 * @author bruno
	 *
	 */
    class TableModeleAbr extends DefaultTableModel {

        /** Pour la sérialisation, non utilisé */
        private static final long serialVersionUID = 1L;

        /** Les données de la table */
        private ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();

        /** Tableau contenant les classes des colonnes d'objets*/
        private Class<?>[] colClass = new Class<?>[] { Regle.class, Boolean.class };

        /** Tableau conteannt les noms des colonnes */
        private String[] columnNames = new String[] { texts.getText("rulecolumn"), texts.getText("selcolumn") };

        /** 
		 * Constructeur
		 * @param d les données de la table
		 */
        public TableModeleAbr(ArrayList<ArrayList<Object>> d) {
            super();
            data = d;
        }

        /**
		 * Retourne les données sous forme d'ArrayList double
		 * @return {@link #data}
		 */
        public ArrayList<ArrayList<Object>> getArrayListOfData() {
            return data;
        }

        /**
		 * MAJ des données sous forme d'ArrayList double
		 * @param d l'arraylist avec les nouvelles données
		 */
        public void setArrayListOfData(ArrayList<ArrayList<Object>> d) {
            data = d;
        }

        /**
		 * Stocke les données passées en paramètre dans la structure {@link #data}
		 * @param d liste
		 * @see javax.swing.table.DefaultTableModel#setDataVector(java.lang.Object[][], java.lang.Object[])
		 */
        public void setDataVector(ArrayList<ArrayList<Object>> d) {
            data = d;
        }

        /**
		 * Ajoute une ligne à {@link #data}
		 * @see javax.swing.table.DefaultTableModel#addRow(java.lang.Object[])
		 */
        @Override
        public void addRow(Object[] o) {
            ArrayList<Object> al = new ArrayList<Object>();
            for (int i = 0; i < o.length; i++) {
                al.add(o[i]);
            }
            data.add(al);
            this.fireTableRowsInserted(data.size() - 1, data.size() - 1);
            setModif(true);
        }

        /**
		 * Renvoie le nom de la colonne <code>col</code>
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
        @Override
        public String getColumnName(int col) {
            return columnNames[col].toString();
        }

        /**
		 * Affecte <code>value</code> à cellule (<code>row</code>,<code>col</code>) de {@link #data}
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
		 */
        @Override
        public void setValueAt(Object value, int row, int col) {
            data.get(row).set(col, value);
            fireTableCellUpdated(row, col);
            setModif(true);
        }

        /**
		 * Supprime la ligne <code>row</code>
		 * @see javax.swing.table.DefaultTableModel#removeRow(int)
		 */
        @Override
        public void removeRow(int row) {
            data.remove(row);
            this.fireTableRowsDeleted(row, row);
            setModif(true);
        }

        /**
		 * Renvoie le nombre de colonnes de {@link #data}
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
        @Override
        public int getColumnCount() {
            return data.get(0).size();
        }

        /**
		 * Renvoie le nombre de lignes de {@link #data}
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
        @Override
        public int getRowCount() {
            int retour = 0;
            if (data != null) {
                retour = data.size();
            }
            return retour;
        }

        /**
		 * Renvoie l'objet de la cellule (<code>row</code>,<code>col</code>) de {@link #data}
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
        @Override
        public Object getValueAt(int row, int col) {
            return data.get(row).get(col);
        }

        /**
		 * Redéfinition indiquant que toutes les cellules, sauf celles de la
		 * première colonne (n°), sont éditables
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
        @Override
        public boolean isCellEditable(int i, int j) {
            boolean retour = true;
            if (j == 0) {
                retour = false;
            }
            return retour;
        }

        /**
		 * Renvoie la classe des objets de la colonne <code>col</code>
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
        @Override
        public Class<?> getColumnClass(int col) {
            return colClass[col];
        }
    }

    /**
	 * Classe permettant de faire le rendu de la table
	 * @author bruno
	 *
	 */
    private class BrailleTableCellRenderer extends DefaultTableCellRenderer {

        /** pour la sérialisation (non utilisé)*/
        private static final long serialVersionUID = 1L;

        /**
		 * Constructeur
		 */
        public BrailleTableCellRenderer() {
            super();
        }

        /** 
		 * Fait afficher la colonne 1 de la table avec la police de la ligne secondaire de l'éditeur
		 * TODO: éventuellement, ajouter une option pour cette police
		 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
        @Override
        public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
            if (column == 0) {
                Font t;
                try {
                    t = Font.createFont(Font.PLAIN, new File("ui/fontes/DejaVuSans.ttf"));
                    t = t.deriveFont(Font.PLAIN, 12);
                } catch (Exception e) {
                    System.err.println(texts.getText("unknownpolice"));
                    t = new Font("DejaVu Sans", Font.PLAIN, 12);
                }
                cell.setFont(t);
            }
            return cell;
        }
    }
}
