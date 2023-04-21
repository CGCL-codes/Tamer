package net.sourceforge.bibliotheque.modele.dao.memory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.sourceforge.bibliotheque.modele.Collection;
import net.sourceforge.bibliotheque.modele.Editeur;
import net.sourceforge.bibliotheque.modele.dao.DAOException;
import net.sourceforge.bibliotheque.modele.dao.EditeurDAO;

public class EditeurDAOImpl implements EditeurDAO {

    private Set<Editeur> editeurs = null;

    private Set<Collection> collections = null;

    public EditeurDAOImpl(Set<Editeur> editeurs, Set<Collection> collections) {
        super();
        this.editeurs = editeurs;
        this.collections = collections;
    }

    public void addCollection(Collection collection) throws DAOException {
        if (!this.collections.add(collection)) {
            throw new DAOException("La collection " + collection.getNom() + " n'a pas été ajouté");
        } else {
            collection.setId(this.collections.size());
        }
    }

    public void addEditeur(Editeur editeur) throws DAOException {
        if (!this.editeurs.add(editeur)) {
            throw new DAOException("L'éditeur " + editeur.getNom() + " n'a pas été ajouté");
        } else {
            editeur.setId(this.editeurs.size());
        }
    }

    public Collection getCollectionById(int id) throws DAOException {
        Collection c = null;
        Iterator<Collection> i = this.collections.iterator();
        while (i.hasNext() && c == null) {
            Collection temp = i.next();
            if (temp.getId() == id) {
                c = temp;
            }
        }
        if (c == null) {
            throw new DAOException("Collection avec l'id " + id + " non trouvé");
        }
        return c;
    }

    public Set<Collection> getCollectionByNom(String nom) throws DAOException {
        Set<Collection> s = new HashSet<Collection>();
        Iterator<Collection> i = this.collections.iterator();
        while (i.hasNext()) {
            Collection temp = i.next();
            if (temp.getNom().equalsIgnoreCase(nom)) {
                s.add(temp);
            }
        }
        return s;
    }

    public Set<Editeur> editeurs() throws DAOException {
        return this.editeurs;
    }

    public Set<Collection> getCollectionByEditeur(Editeur e) throws DAOException {
        Set<Collection> s = new HashSet<Collection>();
        Iterator<Collection> i = this.collections.iterator();
        while (i.hasNext()) {
            Collection temp = i.next();
            if (temp.getEditeur().getNom().equalsIgnoreCase(e.getNom())) {
                s.add(temp);
            }
        }
        return s;
    }

    public Editeur getEditeurByCollection(Collection collection) throws DAOException {
        Editeur e = null;
        Iterator<Collection> i = this.collections.iterator();
        boolean trouve = false;
        while (i.hasNext() && !trouve) {
            Collection t = i.next();
            e = t.getEditeur();
            trouve = t.equals(collection);
        }
        return e;
    }

    public Editeur getEditeurById(int id) throws DAOException {
        Editeur e = null;
        Iterator<Editeur> i = this.editeurs.iterator();
        while (i.hasNext() && e == null) {
            Editeur temp = i.next();
            if (temp.getId() == id) {
                e = temp;
            }
        }
        if (e == null) {
            throw new DAOException("Editeur avec l'id " + id + " non trouvé");
        }
        return e;
    }

    public Set<Editeur> getEditeurByNom(String nom) throws DAOException {
        Set<Editeur> s = new HashSet<Editeur>();
        Iterator<Editeur> i = this.editeurs.iterator();
        while (i.hasNext()) {
            Editeur temp = i.next();
            if (temp.getNom().equals(nom)) {
                s.add(temp);
            }
        }
        return s;
    }

    public void updateCollection(Collection collection) throws DAOException {
        if (this.collections.add(collection)) {
            throw new DAOException("La collection " + collection.getNom() + " n'est pas dans la db");
        }
    }

    public void updateEditeur(Editeur editeur) throws DAOException {
        if (this.editeurs.add(editeur)) {
            throw new DAOException("L'éditeur " + editeur.getNom() + " n'est pas dans la db");
        }
    }
}
