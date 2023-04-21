package gate.creole.ontology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/** Represents a single ontology class. */
public class TClassImpl extends OntologyResourceImpl implements TClass {

    /** the id of the class */
    String id;

    /** the set of direct sub classes of this class */
    Set directSubClasses = new HashSet();

    /** the set of direct super classes of this class */
    Set directSuperClasses = new HashSet();

    /** The sub classes transitive closure set */
    Set subClassesTransitiveClosure = new HashSet();

    /** The super classes transitive closure set */
    Set superClassesTransitiveClosure = new HashSet();

    /**
   * Creates a new class given id,name,comment and ontology.
   * 
   * @param anId
   *          the id of the new class
   * @param aName
   *          the name of the new class
   * @param aComment
   *          the comment of the new class
   * @param anOntology
   *          the ontology to which the new class belongs
   */
    public TClassImpl(String anId, String aName, String aComment, Taxonomy anOntology) {
        super(aName, aComment, anOntology);
        id = anId;
    }

    /**
   * Gets the id of the class.
   * 
   * @return the id of the class
   */
    public String getId() {
        return id;
    }

    public void setURI(String theURI) {
        if (-1 == theURI.indexOf('#')) {
            theURI = getOntology().getSourceURI() + '#' + theURI;
        }
        uri = theURI;
        ontology.setModified(true);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String aComment) {
        comment = aComment;
        ontology.setModified(true);
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
        ontology.setModified(true);
    }

    public void addSubClass(TClass subClass) {
        this.directSubClasses.add(subClass);
        Set set;
        if (null != (set = subClass.getSuperClasses(TClass.DIRECT_CLOSURE))) {
            if (!set.contains(this)) {
                subClass.addSuperClass(this);
            }
        }
        ontology.setModified(true);
    }

    public void addSuperClass(TClass superClass) {
        directSuperClasses.add(superClass);
        Set set;
        if (null != (set = superClass.getSubClasses(TClass.DIRECT_CLOSURE))) {
            if (!set.contains(this)) {
                superClass.addSubClass(this);
            }
        }
        ontology.setModified(true);
    }

    public void removeSubClass(TClass subClass) {
        directSubClasses.remove(subClass);
        Set set;
        if (null != (set = subClass.getSuperClasses(TClass.DIRECT_CLOSURE))) {
            if (set.contains(this)) {
                subClass.removeSuperClass(this);
            }
        }
        ontology.setModified(true);
    }

    public void removeSuperClass(TClass superClass) {
        directSuperClasses.remove(superClass);
        Set set;
        if (null != (set = superClass.getSubClasses(TClass.DIRECT_CLOSURE))) {
            if (set.contains(this)) {
                superClass.removeSubClass(this);
            }
        }
        ontology.setModified(true);
    }

    public Set getSubClasses(byte closure) {
        Set result;
        switch(closure) {
            case DIRECT_CLOSURE:
                {
                    result = directSubClasses;
                    break;
                }
            case TRANSITIVE_CLOSURE:
                {
                    if (0 == subClassesTransitiveClosure.size() || getOntology().isModified()) {
                        inferSubClassesTransitiveClosure();
                    }
                    result = subClassesTransitiveClosure;
                    break;
                }
            default:
                {
                    throw new IllegalArgumentException("Unknown closure type " + closure);
                }
        }
        return new HashSet(result);
    }

    public Set getSuperClasses(byte closure) {
        Set result;
        switch(closure) {
            case DIRECT_CLOSURE:
                {
                    result = directSuperClasses;
                    break;
                }
            case TRANSITIVE_CLOSURE:
                {
                    if (0 == superClassesTransitiveClosure.size() || getOntology().isModified()) {
                        inferSuperClassesTransitiveClosure();
                    }
                    result = superClassesTransitiveClosure;
                    break;
                }
            default:
                {
                    throw new IllegalArgumentException("Unknown closure type: " + closure);
                }
        }
        return new HashSet(result);
    }

    public void inferSubClassesTransitiveClosure() {
        List bag = new ArrayList(directSubClasses);
        subClassesTransitiveClosure = new HashSet();
        TClass currentClass;
        while (bag.size() > 0) {
            currentClass = (TClass) bag.remove(0);
            if (subClassesTransitiveClosure.add(currentClass)) bag.addAll(currentClass.getSubClasses(TClass.DIRECT_CLOSURE));
        }
    }

    public void inferSuperClassesTransitiveClosure() {
        List bag = new ArrayList(directSuperClasses);
        superClassesTransitiveClosure = new HashSet();
        TClass currentClass;
        while (bag.size() > 0) {
            currentClass = (TClass) bag.remove(0);
            if (superClassesTransitiveClosure.add(currentClass)) bag.addAll(currentClass.getSuperClasses(TClass.DIRECT_CLOSURE));
        }
    }

    public boolean isTopClass() {
        return directSuperClasses.size() == 0;
    }

    public String toString() {
        return name;
    }

    public static Set getSubClasses(byte closure, Set classes) {
        Set result = new HashSet();
        Iterator ci = classes.iterator();
        TClass c;
        while (ci.hasNext()) {
            c = (TClass) ci.next();
            result.addAll(c.getSubClasses(closure));
        }
        return result;
    }

    public static Set getSuperClasses(byte closure, Set classes) {
        Set result = new HashSet();
        Iterator ci = classes.iterator();
        TClass c;
        while (ci.hasNext()) {
            c = (TClass) ci.next();
            result.addAll(c.getSuperClasses(closure));
        }
        return result;
    }

    public ArrayList getSubClassesVSDistance() {
        ArrayList result = new ArrayList();
        Set set;
        int level = 0;
        TClass c;
        Set levelSet = new HashSet();
        levelSet.add(this);
        boolean rollon = (0 < this.getSubClasses(TClass.DIRECT_CLOSURE).size());
        while (rollon) {
            set = new HashSet();
            Iterator li = levelSet.iterator();
            while (li.hasNext()) {
                c = (TClass) li.next();
                set.addAll(c.getSubClasses(TClass.DIRECT_CLOSURE));
            }
            if (0 < set.size()) {
                result.add(level++, set);
            }
            levelSet = set;
            rollon = 0 < levelSet.size();
        }
        return result;
    }

    public ArrayList getSuperClassesVSDistance() {
        ArrayList result = new ArrayList();
        Set set;
        int level = 0;
        TClass c;
        Set levelSet = new HashSet();
        levelSet.add(this);
        boolean rollon = (0 < this.getSuperClasses(TClass.DIRECT_CLOSURE).size());
        while (rollon) {
            set = new HashSet();
            Iterator li = levelSet.iterator();
            while (li.hasNext()) {
                c = (TClass) li.next();
                set.addAll(c.getSuperClasses(TClass.DIRECT_CLOSURE));
            }
            if (0 < set.size()) {
                result.add(level++, set);
            }
            levelSet = set;
            rollon = 0 < levelSet.size();
        }
        return result;
    }

    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof TClass) {
            TClass c = (TClass) o;
            result = true;
            if (null != this.getId() && null != c.getId()) result &= this.getId().equals(c.getId()); else result &= this.getId() == c.getId();
            if (null != this.getName() && null != c.getName()) result &= this.getName().equals(c.getName()); else result &= this.getName() == c.getName();
            if (null != this.getOntology() && null != c.getOntology()) result &= this.getOntology().equals(c.getOntology()); else result &= this.getOntology() == c.getOntology();
            if (null != this.getURI() && null != c.getURI()) result &= this.getURI().equals(c.getURI()); else result &= this.getURI() == c.getURI();
        }
        return result;
    }
}
