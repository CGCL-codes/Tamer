package jaxe;

import org.apache.log4j.Logger;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMLocator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.rng.RngProperty;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.ValidationDriver;

/**
 * Fenêtre de validation du fichier avec le schéma
 */
public class ValidationFrame extends JFrame implements MouseListener, ActionListener {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(ValidationFrame.class);

    JaxeDocument doc;

    JaxeFrame jframe;

    JEditorPane editorPane;

    String contenu;

    ArrayList<Erreur> erreurs;

    public ValidationFrame(final JaxeDocument doc, final JaxeFrame jframe) {
        super(JaxeResourceBundle.getRB().getString("validation.Validation"));
        this.jframe = jframe;
        final JaxeMenuBar menuBar = new JaxeMenuBar(this, jframe);
        setJMenuBar(menuBar);
        newdoc(doc);
    }

    public void newdoc(final JaxeDocument doc) {
        this.doc = doc;
        if (doc.cfg == null) {
            JOptionPane.showMessageDialog(jframe, JaxeResourceBundle.getRB().getString("validation.Schema"), JaxeResourceBundle.getRB().getString("erreur.Erreur"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        final Rectangle fr = jframe.getBounds();
        setLocation(fr.x + fr.width / 2, fr.y + fr.height / 2);
        setSize(new Dimension(620, 460));
        affichage();
        miseAJour();
    }

    protected void affichage() {
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        final JScrollPane paneScrollPane = new JScrollPane(editorPane);
        paneScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        paneScrollPane.setPreferredSize(new Dimension(600, 400));
        final JPanel boutonsP = new JPanel();
        boutonsP.setLayout(new FlowLayout());
        final JButton boutonMAJ = new JButton(JaxeResourceBundle.getRB().getString("validation.MiseAJour"));
        boutonMAJ.addActionListener(this);
        boutonsP.add(boutonMAJ);
        final JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(paneScrollPane, BorderLayout.CENTER);
        contentPane.add(boutonsP, BorderLayout.NORTH);
        setContentPane(contentPane);
        editorPane.addMouseListener(this);
    }

    protected void print(final String s) {
        contenu += s;
    }

    protected void println(final String s) {
        contenu += s + "\n";
    }

    public void miseAJour() {
        if (doc.cfg.schemaURL == null) return;
        contenu = "";
        erreurs = new ArrayList<Erreur>();
        if (doc.cfg.getSchema() instanceof SchemaRelaxNG) {
            InputSource inSchema;
            try {
                inSchema = ValidationDriver.uriOrFileInputSource(doc.cfg.schemaURL.toExternalForm());
            } catch (MalformedURLException ex) {
                LOG.error("Erreur au chargement du schéma avec Jing", ex);
                inSchema = null;
            }
            ValidationDriver jingValidation = null;
            if (inSchema != null) {
                final PropertyMapBuilder prorietes = new PropertyMapBuilder();
                RngProperty.DATATYPE_LIBRARY_FACTORY.put(prorietes, new com.thaiopensource.datatype.xsd.DatatypeLibraryFactoryImpl());
                prorietes.put(ValidateProperty.ERROR_HANDLER, new RelaxNGErrorHandler());
                try {
                    jingValidation = new ValidationDriver(prorietes.toPropertyMap());
                    if (!jingValidation.loadSchema(inSchema)) LOG.error("Erreur au chargement du schéma avec Jing (jingValidation)");
                } catch (SAXException ex) {
                    LOG.error("Erreur au chargement du schéma avec Jing", ex);
                } catch (IOException ex) {
                    LOG.error("Erreur au chargement du schéma avec Jing", ex);
                }
                if (jingValidation != null) {
                    final String sdoc = JaxeDocument.DOMVersXML((org.w3c.dom.Document) doc.DOMdoc);
                    final StringReader reader = new StringReader(sdoc);
                    final InputSource in = new InputSource(reader);
                    boolean valide;
                    try {
                        valide = jingValidation.validate(in);
                    } catch (SAXException ex) {
                        LOG.error("Validation Jing : SAXException", ex);
                        valide = false;
                    } catch (IOException ex) {
                        LOG.error("Validation Jing : IOException", ex);
                        valide = false;
                    }
                    if (valide) print(JaxeResourceBundle.getRB().getString("validation.parfait"));
                }
            }
        } else {
            final boolean[] terreur = new boolean[1];
            try {
                final org.w3c.dom.Document docClone = (org.w3c.dom.Document) doc.DOMdoc.cloneNode(true);
                final DOMConfiguration config = docClone.getDomConfig();
                config.setParameter("schema-type", "http://www.w3.org/2001/XMLSchema");
                config.setParameter("schema-location", doc.cfg.schemaURL.toURI().toString());
                config.setParameter("validate", Boolean.TRUE);
                terreur[0] = false;
                final ErrorStorer ef = new ErrorStorer(terreur);
                config.setParameter("error-handler", ef);
                docClone.normalizeDocument();
            } catch (final Exception e) {
                LOG.error("miseAJour()", e);
                terreur[0] = true;
                print(e.getMessage());
            }
            if (!terreur[0]) print(JaxeResourceBundle.getRB().getString("validation.parfait"));
        }
        editorPane.setText(contenu);
        setVisible(true);
        editorPane.repaint();
    }

    /**
     * Le seul moyen d'obtenir les noeuds à l'origine d'erreurs dans une validation est d'utiliser
     * normalizeDocument(). Le problème est que cette méthode modifie le document d'origine.
     * On est donc obligé de cloner le document avant d'utiliser normalizeDocument().
     * Mais du coup il faut rechercher le noeud d'origine quand on a une erreur sur un
     * noeud du document cloné...
     */
    protected Node chercherOriginal(final Node clone) {
        final ArrayList<Integer> numeros = new ArrayList<Integer>();
        Node n1 = clone;
        while (n1.getNodeType() != Node.DOCUMENT_NODE) {
            Node n2 = n1.getPreviousSibling();
            int i = 0;
            while (n2 != null) {
                i++;
                n2 = n2.getPreviousSibling();
            }
            numeros.add(i);
            n1 = n1.getParentNode();
        }
        n1 = doc.DOMdoc;
        for (int i = numeros.size() - 1; i >= 0; i--) {
            n1 = n1.getFirstChild();
            if (n1 == null) {
                LOG.error("ValidationFrame.chercherOriginal : impossible de retrouver le noeud d'origine (1)");
                LOG.error("numeros: " + numeros);
                return (null);
            }
            final int nb = numeros.get(i);
            for (int j = 0; j < nb; j++) {
                if (n1 == null) {
                    LOG.error("ValidationFrame.chercherOriginal : impossible de retrouver le noeud d'origine (2)");
                    LOG.error("numeros: " + numeros);
                    return (null);
                }
                n1 = n1.getNextSibling();
            }
        }
        return (n1);
    }

    class ErrorStorer implements DOMErrorHandler {

        boolean terreur[];

        public ErrorStorer(final boolean terreur[]) {
            this.terreur = terreur;
        }

        public boolean handleError(final DOMError err) {
            final DOMLocator loc = err.getLocation();
            erreurs.add(new Erreur(chercherOriginal(loc.getRelatedNode()), loc.getLineNumber(), contenu.length()));
            terreur[0] = true;
            final String severity;
            switch(err.getSeverity()) {
                case DOMError.SEVERITY_WARNING:
                    severity = "Warning";
                    break;
                case DOMError.SEVERITY_ERROR:
                    severity = "Error";
                    break;
                case DOMError.SEVERITY_FATAL_ERROR:
                    severity = "Fatal Error";
                    break;
                default:
                    severity = "?";
            }
            print(severity);
            if (loc.getLineNumber() != -1) println("  at line number " + loc.getLineNumber());
            println(" : " + err.getMessage() + "\n");
            return (true);
        }
    }

    public void actionPerformed(final ActionEvent e) {
        miseAJour();
    }

    public void mouseClicked(final MouseEvent e) {
        final int pos = editorPane.viewToModel(e.getPoint());
        int p1 = 0;
        int p2 = 0;
        Node noeud = null;
        int ligne = 0;
        for (final Erreur err : erreurs) {
            if (err.p1 > pos) {
                p2 = err.p1;
                break;
            }
            p1 = err.p1;
            noeud = err.noeud;
            ligne = err.ligne;
        }
        if (noeud == null) {
            if (ligne > 0) {
                if (jframe.getSourceFrame() == null) jframe.setSourceFrame(new SourceFrame(doc, jframe)); else jframe.getSourceFrame().miseAJour();
                jframe.getSourceFrame().selectLigne(ligne);
            }
            return;
        }
        if (p2 == 0) p2 = contenu.length();
        editorPane.setCaretPosition(p1);
        editorPane.moveCaretPosition(p2);
        if (noeud.getNodeType() != Node.ELEMENT_NODE) return;
        final Element elsel = (Element) noeud;
        doc.textPane.selectElement(elsel);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                doc.textPane.allerElement(elsel);
            }
        });
    }

    public void mouseEntered(final MouseEvent e) {
    }

    public void mouseExited(final MouseEvent e) {
    }

    public void mousePressed(final MouseEvent e) {
    }

    public void mouseReleased(final MouseEvent e) {
    }

    class RelaxNGErrorHandler implements ErrorHandler {

        public void handleError(final String severity, SAXParseException exception) {
            erreurs.add(new Erreur(null, exception.getLineNumber(), contenu.length()));
            print(severity);
            if (exception.getLineNumber() != -1) print("  at line number " + exception.getLineNumber());
            println(" : " + exception.getMessage() + "\n");
        }

        public void warning(SAXParseException exception) throws SAXException {
            handleError("warning", exception);
        }

        public void error(SAXParseException exception) throws SAXException {
            handleError("error", exception);
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            handleError("fatalError", exception);
        }
    }

    class Erreur {

        public Node noeud;

        public int ligne;

        public int p1;

        public Erreur(final Node noeud, final int ligne, final int p1) {
            this.noeud = noeud;
            this.ligne = ligne;
            this.p1 = p1;
        }
    }

    public void imprimer() {
        final DocumentRenderer renderer = new DocumentRenderer();
        renderer.print(editorPane);
    }
}
