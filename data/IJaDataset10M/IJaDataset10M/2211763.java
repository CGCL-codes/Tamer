package namingservice.core.node;

import java.util.Hashtable;
import java.util.Vector;
import java.io.Serializable;

/**
 * Classe Node costituisce l'elemento minimo del database interno di un Host rappresenta un nodo della rete implementa l'interfaccia Serializzable per poter essere usata come parametro di una funzione che sfrutta RMI.
 * @author  Nicolas Tagliani e Vincenzo Frascino
 */
public class NodeMap implements Serializable {

    /**
	 * @uml.property  name="children"
	 */
    private Hashtable<String, NodeMap> children;

    /**
	 * @uml.property  name="names"
	 */
    private Vector<String> names;

    /**
	 * @uml.property  name="father"
	 * @uml.associationEnd  
	 */
    private NodeMap father;

    /**
	 * @uml.property  name="name"
	 */
    private String name;

    private String nodeInfo;

    private static final long serialVersionUID = 7526471155622776147L;

    /**
	 * @uml.property  name="nc"
	 * @uml.associationEnd  
	 */
    private NodeCore nc;

    /**
 	 * Costruttore Node si occupa di inizializzare il nodo
 	 * 
 	 * @param name nome del nodo
 	 * @param host nome completo del nodo
 	 * @param ip ip del nodo
 	 * @param father riferimento del padre
 	 * @param Info informazioni sul nodo
 	 */
    public NodeMap(String name, String host, String ip, NodeMap father, String Info) {
        super();
        this.father = father;
        this.children = new Hashtable<String, NodeMap>();
        this.names = new Vector<String>();
        this.name = name;
        this.nodeInfo = Info;
        this.nc = new NodeCore(host, ip);
    }

    /**
	 * @return
	 * @uml.property  name="name"
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * @param n
	 * @uml.property  name="name"
	 */
    public void setName(String n) {
        this.name = n;
    }

    /**
	 * @return
	 * @uml.property  name="father"
	 */
    public NodeMap getFather() {
        return this.father;
    }

    public NodeMap getRoot() {
        NodeMap n = this;
        while (n.getName() != "/") {
            n = n.getFather();
        }
        return n;
    }

    public NodeMap getSNode(String s) {
        NodeMap n = this;
        while (n.getName() != s) {
            n = n.getFather();
        }
        return n;
    }

    public String getInfo() {
        return this.nodeInfo;
    }

    public void setInfo(String Info) {
        this.nodeInfo = Info;
    }

    /**
 	 * Metodo addChild serve a registrare un client quando su di esso viene invocata la register
 	 * 
 	 * @param name nome client
 	 * @param host nome client completo
 	 * @param ip ip del client
 	 * @param father riferimento al padre nell'albero dei nodi
 	 * @param Info informazioni sul client
 	 */
    public void addChild(String name, String host, String ip, NodeMap father, String Info) {
        names.add(name);
        children.put(name, new NodeMap(name, host, ip, father, Info));
    }

    /**
 	 * Metodo getChild ritorna un riferimento ad un figlio di cui si conosce il nome 
 	 *  
 	 * @param name nome del figlio di cui si vuole il riferimento
 	 * @return figlio
 	 */
    public NodeMap getChild(String name) {
        return ((NodeMap) children.get(name));
    }

    /**
 	 * Metodo findChild ritorna la presenza o meno di un figlio nell'albero dato il suo nome
 	 * 
 	 * @param name nome del figlio
 	 * @return true se è presente
 	 */
    public boolean findChild(String name) {
        if (getChild(name) == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
 	 * Metodo recursiveFindChild cerca un figlio nell'albero e ne restituisce le informazioni
 	 * @param name nome del figlio
 	 * @return informazioni sul figlio
 	 */
    public String recursiveFindChild(String name) {
        if (findChild(name)) return getChild(name).getHostIP() + " " + getChild(name).getHostID(); else for (NodeMap node : children.values()) {
            String ret = node.recursiveFindChild(name);
            if (!ret.equals("Unknown Host")) {
                return ret;
            }
        }
        return "Unknown Host";
    }

    /**
 	 * Metodo removeChild rimuove un figlio dall'albero dato il nome
 	 * 
 	 * @param name nome del figlio
 	 * @return Ok se l'operazione è andata a buon fine
 	 */
    public String removeChild(String name) {
        names.remove(name);
        children.remove(name);
        return "OK";
    }

    public Vector<String> listChild() {
        return names;
    }

    public String getHostID() {
        return nc.getID();
    }

    public String getHostIP() {
        return nc.getIP();
    }

    /**
	 * @return
	 * @uml.property  name="children"
	 */
    public Hashtable<String, NodeMap> getChildren() {
        return this.children;
    }

    /**
	 * @return
	 * @uml.property  name="names"
	 */
    public Vector<String> getNames() {
        return this.names;
    }

    /**
 	 * Metodo updateNode aggiorna lo stato dell'albero dei figli in base allo stato di n
 	 * viene invocato in seguito ad una synch sul nodo
 	 * 
 	 * @param n nuovo stato del db interno del nodo
 	 */
    public void updateNode(NodeMap n) {
        this.children = n.getChildren();
        this.names = n.getNames();
    }
}
