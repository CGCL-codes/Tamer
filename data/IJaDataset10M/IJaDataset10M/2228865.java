package net.sourceforge.arcamplayer.library.collection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>Representa una lista inteligente o, mejor dicho, automática de la biblioteca de medios en
 * el modelo cliente.</p>
 * @author David Arranz Oveja
 * @author Pelayo Campa González-Nuevo
 */
public class SmartList extends ClassificationComponent implements PlayList {

    /**
	 * lista virtual de canciones. Es volatil y requiere ser generada por la biblioteca por primera vez.
	 */
    protected List<SongRef> virtuaList;

    /**
     * listado de reglas.
     */
    protected List<FilterRule> filter;

    /**
     * <p>Constructor de la lista que requiere un nombre y un contenedor padre.</p>
     * @param parent el padre.
     * @param name nombre.
     */
    public SmartList(ClassificationComposite parent, String name) {
        super(parent, name);
    }

    /**
     * <p>Constructor de la lista que requiere un nombre.</p>
     * @param name nombre.
     */
    public SmartList(String name) {
        super(null, name);
    }

    /**
     * Devuelve la lista de reglas.
     */
    private List<FilterRule> getFilter() {
        if (filter == null) {
            filter = new ArrayList<FilterRule>();
        }
        return this.filter;
    }

    /**
     * <p>Añade una regla a la lista.</p>
     */
    public void addFilterRule(FilterRule rule) {
        getFilter().add(rule);
    }

    /**
     * <p>Devuelve la lista de reglas.</p>
     * @return
     */
    public List<FilterRule> getRuleList() {
        if (filter == null) {
            filter = new ArrayList<FilterRule>();
        }
        return filter;
    }

    /**
     * <p>Devuelve un iterador para las reglas.</p>
     */
    public ListIterator<FilterRule> listIterator() {
        return (ListIterator<FilterRule>) getFilter().listIterator();
    }

    /**
	 * 
	 */
    @Override
    public boolean isContainer() {
        return false;
    }

    /**
	 * <p>Devuelve la lista virtual de canciones.</p>
	 * @return
	 */
    protected List<SongRef> getVirtuaList() {
        if (virtuaList == null) {
            virtuaList = new LinkedList<SongRef>();
        }
        return virtuaList;
    }

    /**
	 * <p>Asigna la lista virtual de canciones.</p>
	 * @param vlist
	 */
    public void setVirtuaList(List<SongRef> vlist) {
        this.virtuaList = vlist;
    }

    /**
	 * <p>Devuelve el listado de canciones.</p>
	 */
    public List<SongRef> getSongList() {
        return getVirtuaList();
    }

    /**
	 * 
	 */
    @Override
    public List<SongRef> getListOfSongs() {
        return getSongList();
    }
}
