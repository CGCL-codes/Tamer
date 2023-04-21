package api.client.bpmModel;

import java.util.HashMap;

/**
 * 
 * Inheritors (erbende Klassen):
 * BPELCommand; Container; 
 *
 */
public class Component extends ModellElement {

    /**
   * 
   * @element-type Utility
   */
    public HashMap<String, Utility> myUtility = new HashMap<String, Utility>();

    /**
 * 
 */
    private String namespace;

    /**
 * @return the namespace of this component
 */
    public String getNamespace() {
        return namespace;
    }

    /**
 * sets the namespace for this component
 * @param namespace
 */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
   * 
   * @param t
   * @return
   */
    protected StringBuffer accept(final Visitor t) {
        return t.visit(this);
    }

    /**
   * 
   * @param u
   */
    public void addUtility(final Utility u) {
        myUtility.put(u.getName(), u);
    }

    public Utility getUtility(final String s) {
        return myUtility.get(s);
    }

    public void shouldBeBefore(Component comp) {
        Connector conn = new Connector();
        this.myUtility.put(String.valueOf(conn.hashCode()), conn);
        comp.myUtility.put(String.valueOf(conn.hashCode()), conn);
        conn.setDirection(comp);
    }
}
