package eu.irreality.age;

public class SpellList extends EntityList {

    /**
	* La funci�n addElement de java.util.Vector.
	*
	*/
    public void addElement(Spell o) {
        laLista.addElement(o);
    }

    public void addSpell(Spell m) {
        addElement(m);
    }

    /**
	* La funci�n removeElement de java.util.Vector.
	*
	*/
    public boolean removeElement(Spell o) {
        return laLista.removeElement(o);
    }

    public int size() {
        return laLista.size();
    }

    public SpellList() {
        laLista = new java.util.Vector();
    }

    public SpellList(int initSize) {
        laLista = new java.util.Vector(initSize);
    }

    public void incrementSize(int increment) {
        if (increment > 0) laLista.setSize(laLista.size() + increment);
    }

    public boolean isEmpty() {
        return laLista.isEmpty();
    }

    public Spell elementAt(int i) {
        return (Spell) laLista.elementAt(i);
    }

    public void setElementAt(Spell nuevo, int i) {
        laLista.setElementAt(nuevo, i);
    }

    public boolean contains(Spell m) {
        for (int i = 0; i < size(); i++) {
            if (elementAt(i) != null && elementAt(i).equals(m)) return true;
        }
        return false;
    }

    public org.w3c.dom.Node getXMLRepresentation(org.w3c.dom.Document doc) {
        org.w3c.dom.Element suElemento = doc.createElement("SpellList");
        for (int i = 0; i < size(); i++) {
            org.w3c.dom.Element nuevoElemento = doc.createElement("SpellRef");
            Spell nuestroMob = (Spell) laLista.elementAt(i);
            nuevoElemento.setAttribute("id", String.valueOf(nuestroMob.getID()));
            suElemento.appendChild(nuevoElemento);
        }
        return suElemento;
    }

    public SpellList(World mundo, org.w3c.dom.Node n) throws XMLtoWorldException {
        if (!(n instanceof org.w3c.dom.Element)) {
            throw (new XMLtoWorldException("SpellList node not Element"));
        } else {
            org.w3c.dom.Element e = (org.w3c.dom.Element) n;
            this.laLista = new java.util.Vector();
            org.w3c.dom.NodeList nl = n.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                org.w3c.dom.Node hijo = nl.item(i);
                if (!(hijo instanceof org.w3c.dom.Element)) {
                    continue;
                } else {
                    org.w3c.dom.Element h = (org.w3c.dom.Element) hijo;
                    addSpell((Spell) mundo.getSpell(h.getAttribute("id")));
                }
            }
        }
    }
}
