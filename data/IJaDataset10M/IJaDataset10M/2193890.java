package org.hironico.gui.treetable.xml;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xpath.internal.XPathAPI;
import com.sun.org.apache.xpath.internal.objects.XNodeSet;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXFindBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.search.Searchable;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Dérivation de la JXFindBar pour permettre une recherche dans le document
 * XML tout entier afin d'afficher les résultats dans une JTable.
 * @author niramousse
 * @since 2.1.0
 */
public class XmlSearchBar extends JXFindBar {

    private static final Logger logger = Logger.getLogger("org.hironico.gui.treetable.xml");

    private Document document = null;

    private List<String> lastResultPaths = new ArrayList<String>();

    private JXTable tableResults = null;

    private JButton btnXPathEval = null;

    public XmlSearchBar(Searchable searchable) {
        super(searchable);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        logger.info("Layout name: '" + getLayout().getClass().getName() + "'");
        add(getBtnXPathEval(), 0);
    }

    protected JButton getBtnXPathEval() {
        if (btnXPathEval == null) {
            btnXPathEval = new JButton();
            btnXPathEval.setText("XPath Eval");
            btnXPathEval.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    doXPathSearch(getSearchText());
                    showGlobalResults();
                }
            });
        }
        return btnXPathEval;
    }

    public void setTableResults(JXTable tableResults) {
        this.tableResults = tableResults;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    protected void searchDocument(Node fromNode, String whatToSearch, List<Node> results) {
        if (fromNode == null) {
            return;
        }
        if ((fromNode.getNodeName() != null) && (fromNode.getNodeName().indexOf(whatToSearch) != -1)) {
            results.add(fromNode);
        } else if ((fromNode.getNodeValue() != null) && (fromNode.getNodeValue().indexOf(whatToSearch) != -1)) {
            results.add(fromNode);
        } else {
            NamedNodeMap attribs = fromNode.getAttributes();
            if (attribs != null) {
                int count = attribs.getLength();
                for (int index = 0; index < count; index++) {
                    Node attrib = attribs.item(index);
                    if ((attrib.getNodeName().indexOf(whatToSearch) != -1) || (attrib.getNodeValue().indexOf(whatToSearch) != -1)) {
                        results.add(fromNode);
                    }
                }
            }
        }
        NodeList childList = fromNode.getChildNodes();
        for (int cpt = 0; cpt < childList.getLength(); cpt++) {
            searchDocument(childList.item(cpt), whatToSearch, results);
        }
    }

    protected String getPathToRoot(Node fromNode) {
        String path = "";
        Node theNode = fromNode;
        while (theNode.getParentNode() != null) {
            if (theNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                path = "@" + theNode.getNodeName() + path;
            } else if (theNode.getNodeType() == Node.TEXT_NODE) {
                path += "";
            } else {
                path = "/" + theNode.getNodeName() + path;
            }
            theNode = theNode.getParentNode();
        }
        return path;
    }

    /**
     * @return  le texte de recherche avec un trim dessus.
     * @since 2.1.0
     */
    protected String getSearchText() {
        String searchText = searchField.getText();
        if (searchText == null) {
            searchText = "";
        } else {
            searchText = searchText.trim();
        }
        return searchText;
    }

    /**
     * Ok on va chercher dans tout le document mais on va créer une liste de 
     * résultats qui permettrons d'alimenter une jtable. Cette jtable 
     * permettra la navigation rapide vers les balises des résultats
     * correspondants à la recherche.
     * @return entier montrant le résultat de la recherche via le searchable
     * @since 2.1.0
     */
    @Override
    protected int doSearch() {
        if (searchField == null) {
            return super.doSearch();
        }
        String searchText = getSearchText();
        if ((tableResults != null) && (document != null) && !"".equals(searchText)) {
            doContentSearch(searchText);
        }
        return super.doSearch();
    }

    /**
     * Permet de faire une évaluation d'expression XPath et d'afficher les
     * noeuds trouvés dans la table des résultats. Depuis la v2.3.0 on
     * regarde ce qui est recherché dans la formule (par exemple si on veut le 
     * texte des noeuds, on va l'afficher dans la table des résultats). 
     * @param xpath la formule XPath à évaluer.
     * @since 2.3.0 Affichage des résultats amélioré.
     */
    protected void doXPathSearch(String xpath) {
        if ("".equals(xpath)) {
            return;
        }
        lastResultPaths.clear();
        Map<String, Integer> resultPathMap = new HashMap<String, Integer>();
        try {
            XObject xobj = XPathAPI.eval(document, xpath);
            logger.info("xobj instance: " + xobj.getClass().getName());
            XNodeSet xns = (XNodeSet) xobj;
            if (xpath.endsWith("text()")) {
                String myStr = xns.str();
                while (!"".equals(myStr)) {
                    lastResultPaths.add(myStr);
                    myStr = xns.str();
                }
            } else {
                NodeList nodeList = xns.nodelist();
                for (int cpt = 0; cpt < nodeList.getLength(); cpt++) {
                    Node myNode = nodeList.item(cpt);
                    String myPath = getPathToRoot(myNode);
                    Integer pathCount = resultPathMap.get(myPath);
                    if (pathCount != null) {
                        pathCount = pathCount + 1;
                        resultPathMap.remove(myPath);
                    } else {
                        pathCount = 1;
                    }
                    resultPathMap.put(myPath, pathCount);
                    myPath = myPath + "[" + pathCount + "]";
                    lastResultPaths.add(myPath);
                }
            }
        } catch (Exception ex) {
            logger.error("Cannot perform XPath search.", ex);
        }
    }

    /**
     * Permet de faire une recherche de contenu, c'est à dire qu'on va scanner
     * les noeuds pour essayer de trouver ce que l'utilisateur a saisi.
     * @param searchText
     */
    protected void doContentSearch(String searchText) {
        List<Node> results = new ArrayList<Node>();
        searchDocument(document, searchText, results);
        lastResultPaths.clear();
        for (Node result : results) {
            String myPath = getPathToRoot(result);
            lastResultPaths.add(myPath);
        }
    }

    /**
     * Effacer les lignes de la table des résultats si jamais elle a été
     * définie pour cette search bar.
     */
    protected void clearTableResults() {
        if (tableResults == null) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tableResults.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
    }

    protected void showGlobalResults() {
        if (tableResults == null) {
            return;
        }
        clearTableResults();
        DefaultTableModel model = (DefaultTableModel) tableResults.getModel();
        model.setColumnIdentifiers(new String[] { "Search results" });
        model.setColumnCount(1);
        for (String path : lastResultPaths) {
            String[] row = new String[1];
            row[0] = path;
            model.addRow(row);
        }
    }

    /**
     * Redéfinition pour afficher les messages dans la table des résultats.
     * @since 2.1.0
     */
    @Override
    protected void showFoundMessage() {
        super.showFoundMessage();
        showGlobalResults();
    }

    /**
     * On montre quand même les résultats de rechercher globaux même si
     * la recherche 'directe' n'a pas donné de résultat.
     * @since 2.1.0
     */
    @Override
    protected void showNotFoundMessage() {
        super.showNotFoundMessage();
        showGlobalResults();
    }
}
