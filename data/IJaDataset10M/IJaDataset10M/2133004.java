package fr.insa_rennes.pcreator.editiongraphique.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import org.eclipse.core.runtime.IAdaptable;

/**
 * Classe de base pour tous les objets du modèle.
 * @author Christophe Pincemaille, Aurélien Fourmi
 */
public abstract class Element implements IAdaptable, Serializable, PropertyChangeListener {

    /**
	 * Le parent de l'Element.
	 * Le parent est forcement un élément composite, 
	 * puisqu'il contient d'autres éléments.
	 */
    private ElementComposite parent;

    /**
	 * Les listeners de l'objet.
	 */
    private PropertyChangeSupport listeners;

    public static final String PROPERTY_ADD = "NodeAddChild";

    public static final String PROPERTY_REMOVE = "NodeRemoveChild";

    public static final String PROPERTY_LAYOUT = "NodeLayout";

    public static final String PROPERTY_RENAME = "NodeRename";

    /** Concerne un changement d'un attribut de la chaîne, tel que la version ou la description */
    public static final String PROPERTY_ATTRIBUTE_CHANGE = "AttributChaineModifié";

    public static final String PROPERTY_CONNECT = "ConnectionCreate";

    public static final String PROPERTY_MISE_EN_NIVEAUX = "MiseEnNiveaux";

    public static final String PROPERTY_LOCATION = "location";

    public static final String PROPERTY_EST_ENTREE_CHAINE = "entreeChaine";

    public static final String PROPERTY_AJOUT_PAUSE = "ajoutPause";

    public static final String PROPERTY_REMOVE_PAUSE = "removePause";

    public static final String PROPERTY_REMOVE_BLOC_ENTREES = "removeBlocEntrees";

    public static final String PROPERTY_REMOVE_BLOC_SORTIES = "removeBlocSorties";

    public static final String PROPERTY_REFRESH_ALL_CHILDREN = "refreshAllChildren";

    /**
	 * Nombre d'Elements créés.
	 */
    private static int nbElements = 0;

    /**
	 * Identifiant de l'Element.
	 * C'est un entier qui s'incrémente à chaque création d'objet.
	 */
    private int id;

    /**
	 * Constructeur par défaut
	 */
    public Element() {
        this.listeners = new PropertyChangeSupport(this);
        this.parent = null;
        this.id = nbElements++;
    }

    /**
	 * Accesseur pour l'identifiant de l'objet
	 * @return l'id de l'objet
	 */
    public int getId() {
        return this.id;
    }

    /**
	 * Accesseur pour le parent de l'objet
	 * @return l'élement parent de l'élément courant
	 */
    public ElementComposite getParent() {
        return this.parent;
    }

    /**
	 * @param parent the parent to set
	 */
    public void setParent(ElementComposite parent) {
        this.parent = parent;
    }

    /**
	 * Setter pour le layout
	 * @param layout
	 */
    public abstract void setLayout(Object layout);

    /**
	 * Getter pour le layout
	 * @return l'objet layout
	 */
    public abstract Object getLayout();

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        listeners.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public PropertyChangeSupport getListeners() {
        return listeners;
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        return null;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        getListeners().firePropertyChange(evt);
    }
}
