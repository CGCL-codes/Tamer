package finderXMLObject;

import java.util.ArrayList;
import java.util.List;
import javax.accessibility.AccessibleRole;
import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Element;
import finderXMLObject.Exception.CriteriaSearchException;

/**
 * Estende la classe astratta per la definizione di un criterio di ricerca.
 * Prevede l'utilizzo della tecnologia Xpath per effettuare le ricerche
 * all'interno del documento xml. Il metodo astratto <i>buildString</i>
 * permette di creare diversi criteri di ricerca a seconda delle necessita' e
 * dell'accuratezza con la quale si vuole ottenere il risultato.
 * 
 * @author Riccardo Costa
 * @version 1.0
 * 
 */
public abstract class XpathCriteriaSearch extends CriteriaSearch {

    /**
	 * Costruttore.
	 * 
	 * @param nome
	 *            Nome del criterio.
	 */
    public XpathCriteriaSearch(String nome) {
        super(nome);
    }

    /**
	 * Cerca un oggetto nel documento utilizzando il suo nome.
	 * 
	 * @param name
	 *            Nome dell'oggetto da cercare.
	 * @param node
	 *            Nodo all'interno del quale cercare.
	 * @return Lista degli elementi trovati.
	 */
    public ArrayList<Element> search(String name, Object node) {
        return search(name, null, node);
    }

    /**
	 * Cerca un oggetto nel documento utilizzando il suo nome e il suo tipo.
	 * 
	 * @param name
	 *            Nome dell'oggetto da cercare.
	 * @param type
	 *            Tipo dell'oggetto da cercare.
	 * @param node
	 *            Nodo all'interno del quale cercare.
	 * @return Lista degli elementi trovati.
	 */
    public ArrayList<Element> search(String name, AccessibleRole type, Object node) {
        if (name == null && type == null) return null;
        if (node == null) return null;
        List results = null;
        String searchString = buildString(name, type);
        try {
            results = find(searchString, node);
        } catch (CriteriaSearchException e) {
            return null;
        }
        if (results == null) return null;
        return buildListResult(results);
    }

    /**
	 * Cerca l'elemento all'interno di un nodo utilizzando XPath.
	 * 
	 * @param str
	 *            Stringa di tipo XPath.
	 * @param nod
	 *            Nodo in cui cercare.
	 * @return Elementi trovati.
	 * @throws CriteriaSearchException
	 *             Errore durante la ricerca.
	 */
    private List find(String str, Object nod) throws CriteriaSearchException {
        List results = null;
        JDOMXPath path = null;
        try {
            path = new JDOMXPath(str);
            results = path.selectNodes(nod);
        } catch (JaxenException e) {
            throw new CriteriaSearchException("Search error: " + e.getMessage());
        }
        return results;
    }

    /**
	 * Costruisce la stringa da utilizzare nella ricerca dell'oggetto.
	 * 
	 * @param name
	 *            Nome dell'elemento da cercare.
	 * @param type
	 *            Tipo dell'elemento da cercare.
	 * @return Stringa di ricerca.
	 */
    protected abstract String buildString(String name, AccessibleRole type);
}
