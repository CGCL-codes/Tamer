package org.openconcerto.xml.persistence;

import org.openconcerto.utils.CollectionMap;
import org.openconcerto.utils.ExceptionHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.jdom.Element;
import org.jdom.filter.Filter;

/**
 * Gère la persistence des objets.
 * 
 * @author ILM Informatique 6 juil. 2004
 * @see #save(Persistent)
 * @see #getRef(Persistent)
 * @see #load(Class)
 */
public class PersistenceManager {

    private static final boolean SAVE_REFS = false;

    private static final String NULL = "null";

    /**
     * Permet d'obtenir un élément XML faisant référence à un objet Java. Cette méthode doit être
     * appelée par tout objet persistent désirant maintenir des références persistante sur d'autres
     * objets.
     * 
     * @param object l'objet Java à référencer.
     * @return la référence correspondante.
     */
    public static Element getRef(Persistent object) {
        return getRef(object, null);
    }

    public static Element getRef(Persistent object, String name) {
        final Element elem;
        if (object == null) {
            if (name == null) throw new IllegalArgumentException("persistent object and name cannot be both null");
            elem = new Element(NULL);
        } else {
            elem = XMLFactory.getElement(object.getClass());
            elem.setAttribute("id", getID(object));
            if (SAVE_REFS || !io.exists(object.getClass(), getID(object))) {
                save(object);
            }
        }
        if (name != null) elem.setAttribute("name", name);
        return elem;
    }

    public static List<Element> getRefs(Collection<? extends Persistent> perss) {
        final List<Element> res = new ArrayList<Element>(perss.size());
        for (final Persistent pers : perss) {
            res.add(getRef(pers));
        }
        return res;
    }

    /**
     * Permet d'obtenir un objet Java à partir d'un élément XML référence.
     * 
     * @param elem un élément obtenu grâce à {@link #getRef(Persistent)}.
     * @return un objet Java équivalent.
     */
    public static Persistent resolveRef(Element elem) {
        if (elem.getName().equals(NULL)) return null;
        final String id = elem.getAttributeValue("id");
        if (id == null) throw new IllegalArgumentException("Element not obtained from getRef (no id attribute).");
        Persistent res = get(id);
        if (res == null) {
            final Class elemClass = XMLFactory.getNonNullClass(elem.getName());
            res = loadFromID(elemClass, id);
        }
        return res;
    }

    public static Persistent resolveChildRef(Element parent, Class clazz) {
        return resolveRef(parent.getChild(XMLFactory.getElementName(clazz)));
    }

    public static Persistent resolveChildRef(Element parent, final String name) {
        final List children = parent.getContent(new Filter() {

            public boolean matches(Object obj) {
                if (obj instanceof Element) {
                    final Element elem = (Element) obj;
                    return name.equals(elem.getAttributeValue("name"));
                } else return false;
            }
        });
        if (children.size() == 0) throw new IllegalStateException("no element with name " + name + " found.");
        if (children.size() > 1) throw new IllegalStateException("more than one element with name " + name + " found.");
        return resolveRef((Element) children.get(0));
    }

    public static List<Persistent> resolveChildrenRef(Element parent, Class clazz) {
        final List children = parent.getChildren(XMLFactory.getElementName(clazz));
        final List<Persistent> res = new ArrayList<Persistent>(children.size());
        final Iterator iter = children.iterator();
        while (iter.hasNext()) {
            final Element element = (Element) iter.next();
            res.add(resolveRef(element));
        }
        return res;
    }

    private static final Set<Persistent> saving = new HashSet<Persistent>();

    private static File wd;

    private static PersistenceIO io;

    static {
        setDir(new File(System.getProperty("user.dir")));
    }

    /**
     * Set the root of the working tree. All files of this framework will be created below the
     * passed directory.
     * 
     * @param dir the new root.
     */
    public static void setDir(File dir) {
        wd = dir;
        io = new SingleXMLIO(wd);
    }

    public static File getDir(String name) {
        return new File(wd, name);
    }

    /**
     * Sauvegarde cet objet.
     * 
     * @param pers l'objet à sauvegarder.
     */
    public static synchronized void save(Persistent pers) {
        if (saving.contains(pers)) return;
        saving.add(pers);
        final Element elem = pers.toXML();
        final String expected = XMLFactory.getElementName(pers.getClass());
        if (!elem.getName().equals(expected)) throw new IllegalStateException("object does not produce XML as it said to XMLFactory (expected name: " + expected + " was: " + elem.getName() + ")");
        try {
            io.save(elem, pers, getID(pers));
        } catch (IOException e) {
            ExceptionHandler.die("probléme sauvegarde de " + pers, e);
        }
        saving.remove(pers);
    }

    /**
     * Sauvegarde une liste d'objets.
     * 
     * @param pers une liste de Persistent, pas forcément tous de la même classe.
     * @throws IOException if problem while saving.
     */
    public static void save(List pers) throws IOException {
        Iterator i = pers.iterator();
        io.setAutoCommit(false);
        while (i.hasNext()) {
            Persistent elem = (Persistent) i.next();
            save(elem);
        }
        io.setAutoCommit(true);
    }

    public static void delete(Persistent p) throws IOException {
        io.delete(p.getClass(), getID(p));
    }

    public static void delete(Collection c) throws IOException {
        Iterator i = c.iterator();
        while (i.hasNext()) {
            Persistent elem = (Persistent) i.next();
            delete(elem);
        }
    }

    public static void deleteAll() throws IOException {
        io.deleteAll();
    }

    /**
     * Charge un objet persistent du type et de l'id spécifié.
     * 
     * @param clazz la classe de l'objet, eg Eleve.class.
     * @param id l'id de l'élément.
     * @return l'objet correspondant.
     */
    private static Persistent loadFromID(Class clazz, final String id) {
        Persistent obj = get(id);
        if (obj == null) {
            synchronized (PersistenceManager.class) {
                if (stubs.get(id) == null) {
                    idsStack.push(id);
                    try {
                        final Element elem = (Element) io.load(clazz, id);
                        if (elem == null) throw new IllegalArgumentException("persistent id " + id + " of class " + clazz + " not found.");
                        obj = (Persistent) XMLFactory.fromXML(elem);
                        idMap.put(id, obj);
                    } catch (IOException e) {
                        throw ExceptionHandler.die("problème lecture", e);
                    }
                    idsStack.pop();
                    stubs.remove(id);
                } else {
                    obj = stubs.get(id);
                }
            }
        }
        return obj;
    }

    /**
     * Renvoie tous les éléments de cette classe.
     * 
     * @param clazz la classe des objets à charger.
     * @return une liste d'objets correspondant.
     */
    public static List load(Class clazz) {
        final Set<String> ids;
        try {
            ids = io.getIDs(clazz);
        } catch (IOException e) {
            throw ExceptionHandler.die("problème lecture", e);
        }
        return load(clazz, ids);
    }

    private static List<Persistent> load(Class clazz, Collection<String> ids) {
        final List<Persistent> result = new ArrayList<Persistent>(ids.size());
        for (final String id : ids) {
            final Persistent pers;
            if (get(id) == null) {
                pers = loadFromID(clazz, id);
            } else {
                pers = get(id);
            }
            result.add(pers);
        }
        return result;
    }

    public static CollectionMap<Class, Persistent> loadAll(File rootDir) {
        final PersistenceIO pio = new SingleXMLIO(rootDir);
        final CollectionMap<Class, String> mm;
        try {
            mm = pio.getIDs();
        } catch (IOException e) {
            throw ExceptionHandler.die("problème lecture", e);
        }
        final CollectionMap<Class, Persistent> res = new CollectionMap<Class, Persistent>();
        for (final Class clazz : mm.keySet()) {
            res.putAll(clazz, load(clazz, mm.getNonNull(clazz)));
        }
        return res;
    }

    public static synchronized void unload() {
        io.unload();
    }

    /**
     * Une map entre ID et objets. Chaque objet a un ID unique, ie un eleve et un prof ne peuvent
     * avoir le meme ID.
     */
    private static final BidiMap idMap = new DualHashBidiMap();

    private static final Map<String, Persistent> stubs = new HashMap<String, Persistent>();

    private static final Stack<String> idsStack = new Stack<String>();

    private static final Random r = new Random();

    /**
     * Si l'id n'est pas défini, donne un ID a cet objet.
     * 
     * @param obj l'objet à identifier.
     * @return le nouvel ID.
     */
    private static synchronized String getID(Persistent obj) {
        String id = (String) idMap.getKey(obj);
        if (id == null) {
            id = System.currentTimeMillis() + "." + r.nextLong();
            idMap.put(id, obj);
        }
        return id;
    }

    /**
     * Retourne l'objet correspondant à cet ID.
     * 
     * @param id l'id désiré.
     * @return l'objet correspondant.
     */
    private static synchronized Persistent get(String id) {
        return (Persistent) idMap.get(id);
    }

    public static synchronized void putStub(Persistent pers) {
        final String id = idsStack.peek();
        stubs.put(id, pers);
    }
}
