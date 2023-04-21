package fr.insa.rennes.pelias.platform;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Repr�sente une r�f�rence sur un PObject.
 * @author Guillaume Ray
 */
@XmlSeeAlso({ PSxSObjectReference.class })
public class PObjectReference implements Serializable {

    private static final long serialVersionUID = -7453093206544973243L;

    @XmlElement
    private Class<?> referencedClass;

    private UUID id;

    private String label;

    private PObject cache;

    @SuppressWarnings("unused")
    private PObjectReference() {
        this(PObject.class, UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    /**
	 * Initialise une nouvelle instance de PObjectReference.
	 * @param referencedClass Classe de l'objet r�f�renc�.
	 * @param id Identifiant de l'objet r�f�renc�.
	 * @throws NullPointerException type doit �tre non null.
	 * @throws NullPointerException id doit �tre non null.
	 */
    public PObjectReference(Class<?> referencedClass, UUID id) {
        cache = null;
        setReferencedClass(referencedClass);
        setId(id);
    }

    /**
	 * Initialise une nouvelle instance de PObjectReference � partir d'une r�f�rence existante.
	 * @param reference PObjectReference � dupliquer.
	 */
    public PObjectReference(PObjectReference reference) {
        cache = null;
        setReferencedClass(reference.getReferencedClass());
        setId(reference.getId());
        setLabel(reference.getLabel());
    }

    /**
	 * Obtient la classe de l'objet r�f�renc�.
	 * @return Classe de l'objet r�f�renc�.
	 */
    public Class<?> getReferencedClass() {
        return referencedClass;
    }

    /**
	 * D�finit la classe de l'objet r�f�renc�.
	 * @param referencedClass Classe de l'objet r�f�renc�.
	 * @throws NullPointerException type doit �tre non null.
	 */
    protected void setReferencedClass(Class<?> referencedClass) {
        if (referencedClass == null) throw new NullPointerException("referencedClass");
        this.referencedClass = referencedClass;
    }

    /**
	 * Obtient l'identifiant de l'objet r�f�renc�.
	 * @return Identifiant de l'objet r�f�renc�.
	 */
    public UUID getId() {
        return id;
    }

    /**
	 * D�finit l'identifiant de l'objet r�f�renc�.
	 * @param id Identifiant de l'objet r�f�renc�.
	 * @throws NullPointerException id doit �tre non null.
	 */
    public void setId(UUID id) {
        if (id == null) throw new NullPointerException("id");
        if (!id.equals(this.id)) cache = null;
        this.id = id;
    }

    /**
	 * Obtient le nom de la r�f�rence (g�n�ralement le dernier nom connu de l'objet r�f�renc�).
	 * @return Nom de la r�f�rence.
	 */
    public String getLabel() {
        return label;
    }

    /**
	 * D�finit le nom de la r�f�rence (g�n�ralement le dernier nom connu de l'objet r�f�renc�).
	 * @param label Nom de la r�f�rence.
	 */
    public void setLabel(String label) {
        if (label == null) label = "";
        this.label = label;
    }

    /**
	 * Obtient l'objet en cache.
	 * @return Objet en cache.
	 */
    protected PObject getCache() {
        return cache;
    }

    /**
	 * D�finit l'objet en cache.
	 * @param cache Objet en cache.
	 */
    protected void setCache(PObject cache) {
        this.cache = cache;
    }

    /**
	 * Obtient l'objet r�f�renc� depuis le d�p�t sp�cifi� ou le cache local.
	 * @param <T> Type de l'objet r�cup�r�.
	 * @param repository D�p�t permettant de r�cuperer l'objet.
	 * @param fromCache Prendre l'objet du cache local si possible.
	 * @param toCache Remplacer le cache local par l'objet du d�p�t.
	 * @return Objet r�f�renc�.
	 * @throws NullPointerException repository doit �tre non null.
	 */
    @SuppressWarnings("unchecked")
    public <T extends PObject> T resolve(IRepository<T> repository, boolean fromCache, boolean toCache) {
        T result = (T) getCache();
        if (!fromCache || (result == null)) result = repository.getObject(getId());
        if (toCache) setCache(result); else setCache(null);
        return result;
    }

    /**
	 * Obtient l'attachement sp�cifi�.
	 * @param repository D�p�t source
	 * @param attachment Attachement � r�cuperer.
	 * @return Attachement sp�cifi�.
	 * @throws PObjectNotFoundException L'objet r�f�renc� n'est pas pr�sent sur le d�p�t.
	 */
    public String getAttachment(IRepository<?> repository, UUID attachment) throws PObjectNotFoundException {
        return repository.getObjectAttachment(getId(), attachment);
    }

    /**
	 * D�finit un attachement.
	 * @param repository D�p�t de destination.
	 * @param attachment Attachement � d�finit.
	 * @param value Valeur de l'attachement.
	 * @return Valeur indiquant si l'attachement etait pr�sent sur le d�p�t avant l'appel.
	 * @throws PObjectNotFoundException L'objet r�f�renc� n'est pas pr�sent sur le d�p�t. 
	 */
    public boolean setAttachment(IRepository<?> repository, UUID attachment, String value) throws PObjectNotFoundException {
        return repository.putObjectAttachment(getId(), attachment, value, true);
    }

    /**
	 * Obtient une valeur indiquant si l'attachement est pr�sent sur le d�p�t sp�cifi�.
	 * @param repository D�p�t source.
	 * @param attachment Attachement dont on veut savoir s'il est pr�sent sur le d�p�t.
	 * @return Valeur indiquant si l'attachement est pr�sent sur le d�p�t.
	 * @throws PObjectNotFoundException L'objet r�f�renc� n'est pas pr�sent sur le d�p�t.
	 */
    public boolean attachmentExists(IRepository<?> repository, UUID attachment) throws PObjectNotFoundException {
        return repository.objectAttachmentExists(getId(), attachment);
    }

    /**
	 * Supprime l'attachement du d�p�t sp�fici�.
	 * @param repository D�p�t de destination.
	 * @param attachment Attachement � supprimer.
	 * @return Valeur indiquant si l'attachement etait pr�sent sur le d�p�t avant l'appel.
	 * @throws PObjectNotFoundException L'objet r�f�renc� n'est pas pr�sent sur le d�p�t.
	 */
    public boolean removeAttachment(IRepository<?> repository, UUID attachment) throws PObjectNotFoundException {
        return repository.removeObjectAttachment(getId(), attachment);
    }

    /**
	 * Supprime tous les attachements de la r�f�rence sur le d�p�t sp�cifi�.
	 * @param repository D�p�t de destination.
	 * @throws PObjectNotFoundException L'objet r�f�renc� n'est pas pr�sent sur le d�p�t.
	 */
    public void cleatAttachments(IRepository<?> repository) throws PObjectNotFoundException {
        repository.clearObjectAttachments(getId());
    }

    /**
	 * Obtient la liste des attachements de la r�f�rence sur le d�p�t sp�cifi�.
	 * @param repository D�p�t source.
	 * @return Liste des attachements de la r�f�rence sur le d�p�t sp�cifi�.
	 * @throws PObjectNotFoundException L'objet r�f�renc� n'est pas pr�sent sur le d�p�t.
	 */
    public List<UUID> enumerateAttachments(IRepository<?> repository) throws PObjectNotFoundException {
        return repository.enumerateObjectAttachments(getId());
    }

    /**
	 * Met � jour le nom de la r�f�rence avec le nom de l'objet sur le d�p�t sp�cifi�.
	 * @param repository D�p�t source.
	 * @throws PObjectNotFoundException L'objet r�f�renc� n'est pas pr�sent sur le d�p�t.
	 */
    public void updateLabel(IRepository<?> repository) throws PObjectNotFoundException {
        setLabel(repository.getObjectLabel(getId()));
    }

    /**
	 * Obtient la liste des objets ayant cette r�f�rence comme d�pendance publique sur le d�p�t sp�cifi�.
	 * @param repository D�p�t source.
	 * @return Liste des objets ayant cette r�f�rence comme d�pendance publique sur le d�p�t sp�cifi�.
	 * @throws PObjectNotFoundException L'objet r�f�renc� n'est pas pr�sent sur le d�p�t.
	 */
    public List<PObjectReference> getObjectRegisteredConsumers(IRepository<?> repository) throws PObjectNotFoundException {
        return repository.getObjectRegisteredConsumers(getId());
    }

    /**
	 * Obtient la liste des d�pendances la r�f�rence sur le d�p�t sp�cifi�.
	 * @param repository D�p�t source.
	 * @return Liste des d�pendances la r�f�rence sur le d�p�t sp�cifi�.
	 * @throws PObjectNotFoundException L'objet r�f�renc� n'est pas pr�sent sur le d�p�t.
	 */
    public List<PObjectReference> getObjectDeclaredDependencies(IRepository<?> repository) throws PObjectNotFoundException {
        return repository.getObjectDeclaredDependencies(getId());
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        UUID id = getId();
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final PObjectReference other = (PObjectReference) obj;
        UUID id = getId();
        UUID otherId = other.getId();
        if (id == null) {
            if (otherId != null) return false;
        } else if (!id.equals(otherId)) {
            return false;
        }
        return true;
    }
}
