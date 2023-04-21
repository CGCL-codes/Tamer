package de.uni_leipzig.lots.common.objects;

import de.uni_leipzig.lots.common.exceptions.NoSuchEntityException;
import de.uni_leipzig.lots.server.persist.Repository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.Serializable;

/**
 * @author Alexander Kiel
 * @version $Id: RepositoryEntityRef.java,v 1.8 2007/10/23 06:29:21 mai99bxd Exp $
 */
public class RepositoryEntityRef<E extends Entity<ID>, ID extends Serializable> implements EntityRef<E>, Serializable {

    /**
     * Don't forget to change this if you change persistent fields.
     */
    private static final long serialVersionUID = -6913388769763586106L;

    @NotNull
    private ID id;

    @Nullable
    private transient Repository<E, ID> repository;

    public RepositoryEntityRef() {
    }

    public RepositoryEntityRef(@NotNull ID id) {
        this.id = id;
    }

    public RepositoryEntityRef(@NotNull ID id, @NotNull Repository<E, ID> repository) {
        this.id = id;
        this.repository = repository;
    }

    @NotNull
    public Serializable getId() {
        return id;
    }

    public void setId(@NotNull ID id) {
        this.id = id;
    }

    public boolean isRepositoryValid() {
        return repository != null;
    }

    @Nullable
    public Repository<E, ID> getRepository() {
        return repository;
    }

    public void setRepository(@Nullable Repository<E, ID> repository) {
        this.repository = repository;
    }

    @NotNull
    public E get() throws NoSuchEntityException {
        if (repository == null) {
            throw new IllegalStateException("repository not available");
        }
        return repository.load(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RepositoryEntityRef that = (RepositoryEntityRef) o;
        if (!id.equals(that.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        if (repository != null) {
            return "RepositoryEntityRef[" + repository.getEntityClass().getSimpleName() + ", " + id + "]";
        } else {
            return "RepositoryEntityRef[unknown, " + id + "]";
        }
    }
}
